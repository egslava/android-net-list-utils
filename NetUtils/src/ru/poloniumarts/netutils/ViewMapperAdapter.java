package ru.poloniumarts.netutils;

import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.lang3.ArrayUtils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

//TODO: when android annotation 3.0 will be produced, do it class parameterized
// ViewMapperAdapter<T> {
// private List<T> objects = new ArrayList<T>();
// }
@EBean
public class ViewMapperAdapter<T> extends BaseAdapter implements SectionIndexer, PinnedSectionListAdapter{

	private static final String TAG = ViewMapperAdapter.class.getSimpleName();

	@RootContext
	Context	context;
	
	private CopyOnWriteArrayList<T> objects = new CopyOnWriteArrayList<T>();
	public 	Object	listData;
	private Class<?> controllerClass = null;
	public	boolean	isItemsEnabled = true;
	private    Integer viewTypeCount;
	
	private CopyOnWriteArrayList<Object> filteredObjects; //fixes ConcurrentModificationException in PinnedSectionListView
	
	void setViewTypeCount(int viewTypeCount){
	    if (this.viewTypeCount != null){
	        throw new IllegalStateException("You're trying to set view type count when it's already been setted.");
	    }
	    this.viewTypeCount = viewTypeCount;
	}
	
//	public ViewMapperSectionIndexer indexer = new ViewMapperAdapterAlphabetSectionIndexer();
	public ViewMapperSectionIndexer indexer = new PinnedSectionIndexer();

	@AfterInject
	void init(){
		try {
		    ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		    Bundle bundle = ai.metaData;
		    itemViewPackage = bundle.getString("ViewMapperAdapter.itemViewsPackage");
		} catch (NameNotFoundException e) {
		    //TODO: solve this problem (meta-data is'nt so necessary now)
			String	metaDataWarning = "<meta-data android:name=\"ViewMapperAdapter.itemViewsPackage\" android:value=\"com.path.to.itemview\" />";
			Log.w(TAG, "Can't find package with item views. Please add that string to your AndroidManifest.xml:\n" + metaDataWarning);
		} catch (NullPointerException e) {
			String	metaDataWarning = "<meta-data android:name=\"ViewMapperAdapter.itemViewsPackage\" android:value=\"com.path.to.itemview\" />";
			Log.w(TAG, "Can't find package with item views. Please add that string to your AndroidManifest.xml:\n" + metaDataWarning);
		}
	}
	
	/**
	 * Never returns null. If setObjects(null) was called, getObjects() will return null
	 * @return list of objects
	 */
	public List<T> getObjects() {
		return objects;
	}

	/**
	 * Sets new list of objects to display.
	 * @param objects	objects. If null - just clears existing list (so getObjects() won't return null)
	 */
	public void setObjects(List<T> objects) {
		if (objects == null){
			this.objects.clear();
			return;
		}
		
		//fixes ConcurrentModificationException in PinnedSectionListView
		this.objects = new CopyOnWriteArrayList<T>(objects);
	}
	
	@Override
	public boolean isEnabled(int position) {
		Object object = objects.get(position);
		if ( object instanceof Enableable ){
			return ((Enableable) object).isEnabled();
		}
		return isItemsEnabled;
	}
	
    @Override
    public boolean isItemViewTypePinned(int viewType) {
        buildClassesMap();
        Class viewTypeClass = MapUtils.getElementByIndex(classesSet, viewType);
        
        return (viewTypeClass != null && Pinnable.class.isAssignableFrom( viewTypeClass ) );
    };
	
	@Override
	public int getCount() {
		if (filteredObjects != null){
			return filteredObjects.size();
		}
		return getObjects() == null ? 0 : getObjects().size();
	}

	@Override
	public Object getItem(int position) {
		if (filteredObjects != null){
			return filteredObjects.get(position);
		}
		return getObjects() == null ? null : getObjects().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		try {
			ViewMapperItemView item;
			
			if (view == null) {
				Class<?> controller = getControllerClass(getItem(position));
				Context context = viewGroup.getContext();
				Method method = controller.getMethod("build", Context.class);
				item = (ViewMapperItemView) method.invoke(null, context);
			} else {
				item = (ViewMapperItemView) view;
			}
			item.setContentView(getItem(position), listData);
			return (View) item;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected CopyOnWriteArraySet<Class> classesSet = new CopyOnWriteArraySet<Class>();    //fixes ConcurrentModificationException in PinnedSectionListView
	
	public String itemViewPackage;
	
	@SuppressWarnings("rawtypes")
	@Override
	public int getViewTypeCount() {
	    if (viewTypeCount != null){
	        return viewTypeCount;
	    }
	    
		buildClassesMap();
		
		/*This method shouldn't return 0 if list is empty
		 * Minimal value for result is 1. Event if the list is empty */
		
		viewTypeCount = Math.max( classesSet.size(), 1);
		return viewTypeCount;
	}

    private void buildClassesMap() {
        int count = 0;
		
        //toArray to fix ConcurrentModificationException
		Object[] objects = (  filteredObjects != null? filteredObjects : getObjects() ).toArray();
		for (Object object : objects) {
		    classesSet.add(object.getClass());
		}
		
		if (viewTypeCount != null && classesSet.size() > viewTypeCount){
		    throw new IllegalStateException("Your data has more item view types than you declared. Your data has " + classesSet.size() + " different types and you declared only " + viewTypeCount + " view types");
		}
    }
	
	@Override
	public int getItemViewType(int position) {
	    buildClassesMap();
		
		Integer value = ArrayUtils.indexOf(classesSet.toArray(), getItem(position).getClass() );  // index of a class in a set
		return value != null ? value : 0;
	}

	public void setControllerClass(Class<?> controllerClass) {
		this.controllerClass = controllerClass;
	}

	public Class<?> getControllerClass(Object object) {
		if (controllerClass != null)
			return controllerClass;
		
		try {
			if (itemViewPackage == null){
				return Class.forName(object.getClass().getName() + "ItemView_");
			}else{
				return Class.forName(itemViewPackage + "." + object.getClass().getSimpleName() + "ItemView_");
			}
		} catch (ClassNotFoundException e) {
			return MapperItemView_.class;
		}
	}
	
	public interface Filter{
		/** return true if object should be remained in list */
		public boolean remain(Object object);
	}
	
    synchronized public void setFilter(Filter filter) {

        if (filter == null) {
            filteredObjects = null;
        } else {
            try {

                // fixes ConcurrentModificationException in
                // PinnedSectionListView
                filteredObjects = new CopyOnWriteArrayList<Object>();
                for (Object object : getObjects()) {
                    if (filter.remain(object)) {
                        filteredObjects.add(object);
                    }
                }
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }

        notifyDataSetChanged();
    }

	/**
	 * Checks if cached by list view is invalid for current position
	 * 
	 * @param position
	 *            position of item in the list
	 * @param view
	 *            cached view
	 * @return true if invalid, false otherwise
	 */
	public boolean isItInvalidItemView(int position, View view) {
		return !(getControllerClass(getItem(position).getClass()) == view
				.getClass());
	}

	@Override
	public void notifyDataSetChanged() {
		indexer.notifyDataSetChanged();
		super.notifyDataSetChanged();
	}

	@Override
	public int getPositionForSection(int section) {
		return indexer.getPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return indexer.getSectionForPosition(position);
	}

	@Override
	public Object[] getSections() {
		return indexer.getSections();
	}
	
	public static class ViewMapperAdapterAlphabetSectionIndexer extends AlphabetSectionIndexer{

		@SuppressWarnings("rawtypes")
		ViewMapperAdapter	adapter;
		Indexerable			indexer;
		
		public void init(@SuppressWarnings("rawtypes") ViewMapperAdapter adapter, Indexerable indexer){
			this.adapter = adapter;
			this.indexer = indexer;
			notifyDataSetChanged();
		}

		@Override
		public char getFirstLetter(int position) {
			return indexer.getFirstLetter(position);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public int size() {
			if (adapter == null){
				return 0;
			}
			List filtered = adapter.filteredObjects;
			if (filtered != null){
				return filtered.size();
			}
			return adapter.getObjects()!=null?adapter.getObjects().size():0;
		}
	}
	
	public static interface Indexerable{
		public char getFirstLetter(int position);
	}
	
    public class PinnedSectionIndexer implements ViewMapperSectionIndexer{
        
        @Override
        public int getPositionForSection(int sectionIndex) {
            int positionForSection = -1;
            int section            = -1;
            
            // toArray to fix ConcurrentModificationException
            for (Object object : objects.toArray()) {
                if (object instanceof Pinnable){
                    section += 1;
                }
                
                positionForSection += 1;
                
                if (section == sectionIndex){
                    break;
                }
            }
            
            return positionForSection;
        }
    
        @Override
        public int getSectionForPosition(int position) {
            if (position >= objects.size()){
                throw new InvalidParameterException("Position of the element is " + position + ", but the total size is " + objects.size() + ". ");
            }
            
            int     section             = -1;
            
            for (int i = 0; i < position; i++){
                if (objects.get(i) instanceof Pinnable){
                    section += 1;
                }
            }
            
            return section;
        }
    
        @Override
        public Object[] getSections() {
            // fixes ConcurrentModificationException in PinnedSectionListView
            List<Object> sections = new CopyOnWriteArrayList<Object>();
            
            for (Object object : objects) {
                if (object instanceof Pinnable) {
                    sections.add(object);
                }
            }
            return sections.toArray();
        }

        @Override
        public void notifyDataSetChanged() {
        }
    }
}

class MapUtils{
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getElementByIndex(Set<T> set, int index) {
        return (T) set.toArray()[index];
    }
}
