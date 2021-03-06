package ru.poloniumarts.netutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

/**
 * 
 * @author Vyacheslav Egorenkov (egslava@gmail.com) This class is created to
 *         replace standard SimpleAdapter class from Android SDK Example:
 * 
 *         <pre>
 * {@code
 *   String[] users = {"John", "Walter"}
 *   String[] messages = {"Hi", "Bye"}
 *   listView.setAdapter(new SuperSimpleAdapter().
 *      setLayout (R.layout.item_message).
 *      setMap  ( new int[]{R.id.name, R.id.message} ).
 *      setItems( users, messages) ));  
 * }
 * </pre>
 * 
 */
@EBean
public class SuperSimpleAdapter extends BaseAdapter {
	@RootContext
	Context context;

	LayoutInflater inflater;

	List<Object[]> items = new ArrayList<Object[]>();
	int layout = -1;
	int[] map;

	@AfterInject
	protected void init() {
		inflater = LayoutInflater.from(context);
	}

	/**
	 * Sets a content to a list
	 * 
	 * @param items
	 *            . Example:
	 * 
	 *            <pre>
	 * {@code
	 *      setItems(new Object[]{
	 *              new Object[]{"John", "Walter", "Smith"},
	 *              new Object[]{"Hi", "Hello", "How are you?"}
	 *            })
	 * </pre>
	 * 
	 *            list.getItem(0) will return [ "John", "Hi" ]
	 * 
	 * @return this
	 */
	public SuperSimpleAdapter setItemsByColumns(Object[]... items) {
		ArrayList<Object[]> result = new ArrayList<Object[]>(items[0].length);
		for(int i = 0; i < items[0].length; i++){
			result.add(new Object[items.length]);
		}
		
		transpose(items, result);
		this.items = result;
		return this;
	}
	
	/**
	 * Sets a content to a list
	 * 
	 * @param items
	 *            . Example:
	 * 
	 *            <pre>
	 * {@code
	 *      setItems(new Object[]{
	 *              new Object[]{"John", "Hi"},
	 *              new Object[]{"Walter", "Hello"}
	 *              new Object[]{"Smith", "How are you"}
	 *            })
	 * </pre>
	 * 
	 *            list.getItem(1) will return [ "Walter", "Hello" ]
	 * 
	 * @return this
	 */
	public SuperSimpleAdapter setItemsByRows(Object[]... items) {
		this.items = convertRow(items);
		return this;
	}

	/**
	 * Sets a content to a list
	 * 
	 * @param items
	 *            . Example:
	 * 
	 *            <pre>
	 * {@code
	 *      setItems(new Object[]{
	 *              new Object[]{"John", "Hi"},
	 *              new Object[]{"Walter", "Hello"}
	 *              new Object[]{"Smith", "How are you"}
	 *            })
	 * </pre>
	 * 
	 *            list.getItem(1) will return [ "Walter", "Hello" ]
	 * 
	 * @return this
	 */
	public SuperSimpleAdapter addRows(Object[]... items) {
		this.items.addAll(convertRow(items));
		return this;
	}
	
	// public SuperSimpleAdapter setItemsByJson(JSONObject data, JSONObject
	// pattern){
	// Iterator<String> keys = pattern.keys();
	// while(keys.hasNext()){
	// String key = keys.next();
	//
	// }
	//
	// }

	/**
	 * Converts JSON to array of JsonEntity Example:
	 * 
	 * @param data
	 *            - the source data (for example, from internet)
	 * @param template
	 *            - the rules point how to map a data to controls
	 * @return inner view of json for comtrol mapping
	 * @throws JSONException
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private ArrayList<JsonEntity> mapJson(JSONObject data, JSONObject template)
			throws JSONException {
		ArrayList<JsonEntity> result = new ArrayList<JsonEntity>();
		Iterator<String> keys = template.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object object = data.get(key);
			if (object instanceof JSONObject) {
				result.addAll(mapJson(data.getJSONObject(key),
						template.getJSONObject(key)));
			} else {
				JsonEntity entity = new JsonEntity();
				entity.view = template.getInt(key);
				entity.value = data.get(key);
				result.add(entity);
			}
		}
		return result;
	}

	static <T> void transpose(T[][] source, List<Object[]> result) {
		int srcWidth = source[0].length;
		int srcHeight = source.length;
		for (int i = 0; i < srcHeight; i++) {
			for (int j = 0; j < srcWidth; j++) {
				result.get(j)[i] = source[i][j];
			}
		}
	}

	public SuperSimpleAdapter setLayout(int layout) {
		this.layout = layout;
		return this;
	}

	public SuperSimpleAdapter setMap(int[] map) {
		this.map = map;
		return this;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result;
		ViewEntity[] row;
		if (convertView == null) {
			result = inflater.inflate(layout, null);
			row = getRow(result, map);
			result.setTag(row);
		} else {
			result = convertView;
			row = (ViewEntity[]) convertView.getTag();
		}

		setContent(row, items.get(position), map);

		return result;
	}

	static void setContent(ViewEntity[] row, Object[] items, int[] map) {
		int length = row.length;
		for (int i = 0; i < length; i++) {
			ViewEntity item = row[i];
			if ((item.type & TYPE.TEXT) != 0) {
				if (items[i] instanceof CharSequence){
					((TextView) item.view).setText((CharSequence) items[i]);
				}else{
					((TextView) item.view).setText(items[i].toString());
				}
			} else if ((item.type & TYPE.IMAGEVIEW) != 0) {
				if (items[i] instanceof Integer) {
					((ImageView) item.view)
							.setImageResource((Integer) items[i]);
				} else {
					// TODO: add aq support
					throw new RuntimeException(
							"Please, add AQuery to realise functional of loading images from URI");
				}
			}
			;

			if ((item.type & TYPE.CHECKABLE) != 0) {
				((Checkable) item.view).setChecked((Boolean) items[i]);
			}
		}
	}

	static ViewEntity[] getRow(View view, int[] map) {
		ViewEntity[] result;
		int length = map.length;
		result = new ViewEntity[length];
		for (int i = 0; i < length; i++) {
			result[i] = new ViewEntity();
			result[i].view = view.findViewById(map[i]);
			result[i].type = calculateType(result[i].view);
		}
		return result;
	}

	static int calculateType(View view) {
		int result = 0;
		if (view instanceof TextView) { // TextView and EditText
			result |= TYPE.TEXT;
		}
		if (view instanceof ImageView) {
			result |= TYPE.IMAGEVIEW;
		}
		if (view instanceof Checkable) {
			result |= TYPE.CHECKABLE;
		}
		return result;
	}
	
	static private ArrayList<Object[]> convertRow(Object[][] src){
		ArrayList<Object[]> result = new ArrayList<Object[]>(src.length);
		
		for (int i = 0; i < src.length; i++) {
			result.add( downcastArray( src[i] ) );
		}
		
		return result;
	}
	
	static private Object[] downcastArray(Object[] array){
		Object[] result = new Object[array.length];
		for(int i = 0; i < array.length; i++){
			result[i] = array[i];
		}
		return result;
	}
}

class TYPE {
	static final int TEXT = 1;
	static final int IMAGEVIEW = 2;
	static final int CHECKABLE = 4;
}

class ViewEntity {
	View view;

	/** @see TYPE */
	int type;
}

class JsonEntity {
	int view;
	Object value;
}
