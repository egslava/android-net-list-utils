package ru.poloniumarts.netutils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

//TODO: when android annotation 3.0 will be produced, do it class parameterized
// ViewMapperAdapter<T> {
// private List<T> objects = new ArrayList<T>();
// }
@EBean
public class ViewMapperAdapter<T> extends BaseAdapter implements SectionIndexer{

	private static final String TAG = ViewMapperAdapter.class.getSimpleName();

	@RootContext
	Context	context;
	
	private List<T> objects = new ArrayList<T>();
	public 	Object	listData;
	private Class<?> controllerClass = null;
	public	boolean	isItemsEnabled = true;
	
	private List<Object> filteredObjects;
	
	public ViewMapperAdapterAlphabetSectionIndexer indexer = new ViewMapperAdapterAlphabetSectionIndexer();

	@AfterInject
	void init(){
		try {
		    ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		    Bundle bundle = ai.metaData;
		    itemViewPackage = bundle.getString("ViewMapperAdapter.itemViewsPackage");
		} catch (NameNotFoundException e) {
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
		this.objects = objects;
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
	protected HashMap<Class, Integer> classesMap;
	
	public String itemViewPackage;
	
	@SuppressWarnings("rawtypes")
	@Override
	public int getViewTypeCount() {
		classesMap	= new HashMap<Class, Integer>();

		int count = 0;
		
		List<? extends Object> objects = filteredObjects != null? filteredObjects : getObjects();
		for (Object object : objects) {
			if (!classesMap.containsKey(object.getClass())){
				classesMap.put(object.getClass(), count);
				count ++;
			}
		}
		
		/*This method shouldn't return 0 if list is empty
		 * Minimal value for result is 1. Event if the list is empty */
		return Math.max( classesMap.size(), 1);
	}
	
	@Override
	public int getItemViewType(int position) {
		if (classesMap == null){
			return 0;
		}
		
		Integer value = classesMap.get(getItem(position).getClass());
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
	
	synchronized public void setFilter(Filter filter){
		
		if (filter == null){
			filteredObjects = null;
		}else{
			try{
				filteredObjects = new ArrayList<Object>();
				for (Object object: getObjects()) {
					if (filter.remain(object)){
						filteredObjects.add(object);
					}
				}
			}catch(ConcurrentModificationException e){
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
	 *            chached view
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
//		return Math.min(objects.size(), section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return indexer.getSectionForPosition(position);
//		return Math.min(objects.size(), position);
	}

	@Override
	public Object[] getSections() {
		return indexer.getSections();
//		Integer[] result = new Integer[objects.size()];
//		for(int i = 0; i < objects.size(); i++){
//			result[i] = new Integer(i);
//		}
//		return result;
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
	};

}
