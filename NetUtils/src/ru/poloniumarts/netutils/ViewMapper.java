package ru.poloniumarts.netutils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class ViewMapper {
	@RootContext
	Context context;
	
	/** if set to true (default), mapper will skip static fields */
	public static boolean skipStaticFields = true;

	public static interface NamingPolicy {
		/**
		 * @param id
		 *            - the view id. For example, for view with id
		 *            R.id.$user__name it's a string "$user__name"
		 * @return path to value in object or null if view shouldn't be mapped.
		 *         Example: new String[] {"user", "name"}
		 */
		public String[] idToFields(String id);

		/**
		 * @param fields
		 *            - path to value. For example, we have class Person and
		 *            Passport:
		 * 
		 *            <pre>
		 * {@code
		 * person: {
		 *   name: "Name",
		 *   surname: "Surname",
		 *   passport: {
		 *     id: 1
		 *   }
		 * }
		 * }
		 * </pre>
		 * 
		 *            so path to id will have the next value: new String[]
		 *            {"passport", "id" }
		 * 
		 * @return id of object in view. For example: "$passport__id"
		 */
		public String fieldToId(String[] fields);

		/**
		 * @param viewId
		 *            - id of view. For example "textViewName" or "$name"
		 * @return true if view value should be added to the object, else -
		 *         false
		 */
		public boolean shouldBeDeserialized(String viewId);
	}

	public static class NamingPolicyDollarWithUndersores implements NamingPolicy {
		@Override
		public String[] idToFields(String id) {
			if (!id.startsWith("$")) {
				return null;
			}

			return id.substring(1).split("__");
		}

		@Override
		public String fieldToId(String[] fields) {
			StringBuilder result = new StringBuilder("$");

			for (int i = 0; i < fields.length; i++) {
				result.append(fields[i]);

				if (i < fields.length - 1) {
					result.append("__");
				}
			}
			return result.toString();
		}

		@Override
		public boolean shouldBeDeserialized(String viewId) {
			return viewId.startsWith("$");
		}
	}

	NamingPolicy namingPolicy = new NamingPolicyDollarWithUndersores();

	private String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";

	public String getDateFormat() {
		return dateFormat;
	}

	public ViewMapper setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	/**
	 * gets layout for list item 
	 * @param data for example class Person
	 * @param context 
	 * @return id of xml layout "person.xml"
	 */
	public static int getLayoutIdForObject(Object data, Context context) {
		String className = data.getClass().getSimpleName();
		className = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(className), "_").toLowerCase(Locale.getDefault());
		int viewId = context.getResources().getIdentifier(className, "layout", context.getPackageName());
		
		if (viewId == 0){
			String message = String.format("Can not find layout R.layout.%s for class %s in package %s", className, data.getClass().getSimpleName(), context.getPackageName());
			throw new RuntimeException(message);
		}
		return viewId;
	}
	
	public void unmap(View view, Object template) {
		unmap((ViewGroup) view, template);
	}

	public void unmap(ViewGroup view, Object template) {
		for (int i = 0; i < view.getChildCount(); i++) {
			View child = view.getChildAt(i);
			
			if (child instanceof ViewGroup) {
				unmap((ViewGroup) child, template);
				continue;
			}
			
			int idAsInt = child.getId();
			if (idAsInt == View.NO_ID){
				continue;
			}
			String id = context.getResources().getResourceEntryName(idAsInt);

			if (!(child instanceof ViewGroup || namingPolicy.shouldBeDeserialized(id))) {
				continue;
			}

			String[] path = namingPolicy.idToFields(id);
			
			if (child instanceof Checkable){
				viewToField(path, template, ((Checkable) child).isChecked(), child);
			}else if (child instanceof TextView){
				viewToField(path, template, ((TextView) child).getText().toString(), child);
			}
		}
	}

	public void map(View view, Object obj) {									
		map(new AQuery(view), view, obj, new ArrayList<String>());			
	}																			

	public void map(AQuery aq, View view, Object obj) {							
		map(aq, view, obj, new ArrayList<String>());							
	}																			

	@SuppressWarnings("unchecked")
	private void map(AQuery aq, View view, Object obj, List<String> path) {
		if (view == null){
			throw new InvalidParameterException("View can not be null");
		}
		if (obj == null){
			throw new InvalidParameterException("obj can not be null");	
		}
		try {
			Class<? extends Object> c = obj.getClass();
			
			for (Field field : c.getFields()) {
				
				if (Modifier.isStatic(field.getModifiers()) && skipStaticFields ){
					continue;
				}
				
				if (field.get(obj) == null){
					
					path.add(field.getName());
					
					View control = getSubView(view, path);
					
					if (control instanceof ImageView){
						Object objectTag = control.getTag();
						if (objectTag != null && objectTag instanceof String){
							int fallBackId = getImageFallBackIdByTag(objectTag);
							aq.id(control).image(fallBackId);
						};
					}
					path.remove(path.size() - 1);
					continue;
				}
				
				path.add(field.getName());
				if (isBasicType(field.getType()) || field.getType() == Date.class) {
					View control = getSubView(view, path);
					Object value = field.get(obj);

					if (control instanceof Checkable){
						aq.id(control).checked(Boolean.parseBoolean(value.toString()));
					}else if (control instanceof TextView) {
						if (value instanceof Date) {
							String format = (String) control.getTag();
							if (format == null) {
								format = getDateFormat();
							}
							String formattedDate = new SimpleDateFormat(format, Locale.getDefault()).format((Date) value);
							aq.id(control).text(formattedDate);
						} else {
							aq.id(control).text(String.valueOf(value));
						}
					} else if (control instanceof ImageView) {
						if (value instanceof Integer) {
							aq.id(control).image((Integer) value);
						} else if (value instanceof String) {
							Object objectTag = control.getTag();
							if (objectTag != null && objectTag instanceof String){
								int fallBackId = getImageFallBackIdByTag(objectTag);
//								aq.id(control).image(fallBackId);	//for lists
								aq.id(control).image((String) value, true, true, 0, fallBackId);
							}else{
								aq.id(control).image((String) value, true, true);
							}
							
						}
					} else if (control instanceof AdapterView) {
						AdapterView<Adapter> adapterView = (AdapterView<Adapter>) view;
						adapterView.addView(null);
					}
				} else if (field.getType().isArray()){
					String[] pathArray = new String[path.size()];
					path.toArray(pathArray);
					int id = context.getResources().getIdentifier(namingPolicy.fieldToId(pathArray), "id",
							view.getContext().getPackageName());
					AdapterView<Adapter> control = (AdapterView<Adapter>) view.findViewById(id);
					
					try{
						Object[] value = (Object[]) field.get(obj);
						
						if (value != null){
							ViewMapperAdapter<Object> adapter = new ViewMapperAdapter<Object>();
							adapter.setObjects(Arrays.asList(value));
							
							if (control != null){
								control.setAdapter(adapter);
							}
							//adapterView.addView(null);
						}
					}catch (ClassCastException e){
						String logMessage = "Can't convert field " + obj.getClass() + "." + field.getName() + " to Object[]. ";
						logMessage += "If you want to map this field, please wrap it to Object type (Integer, Boolean, and so forth)";
						Log.w("ViewMapper", logMessage);
						path.remove(path.size() - 1);
						continue;
					}
				} else {
					map(aq, view, field.get(obj), path);
				}
				path.remove(path.size() - 1);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private int getImageFallBackIdByTag(Object viewTag) {
		String fallBackString;
		fallBackString = (String) viewTag;
		int fallBackId = context.getResources().getIdentifier(fallBackString, "drawable", context.getPackageName());
		return fallBackId;
	}

	private View getSubView(View view, List<String> path) {
		String[] pathArray = new String[path.size()];
		path.toArray(pathArray);
		int id = context.getResources().getIdentifier(namingPolicy.fieldToId(pathArray), "id",
				view.getContext().getPackageName());
		View control = view.findViewById(id);
		return control;
	}

	/**
	 * Maps the value of one view to one field of object
	 * 
	 * @param path
	 *            path to object. for example: ["user", "name"]
	 * @param object
	 *            - an object that has field with path path
	 * @param value
	 *            - any value
	 * @param view
	 * 			  - is a view above value from
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	protected final void viewToField(String[] path, Object object, Object value, View view) {
		try {
				
			Object currentNode = getNode(path, object);

			Field field = currentNode.getClass().getField(path[path.length - 1]);
			Class<?> typeTo = field.getType();
			Object valueTo = field.get(currentNode);
			Class<?> typeFrom = object.getClass();
			if (typeTo == int.class || typeTo == short.class || typeTo == byte.class || typeTo == long.class
					|| valueTo instanceof Number) {

				if (value instanceof Number) {
					field.set(currentNode, value);
				} else {
					String anyToString = value.toString();
					Long longValue = Long.valueOf(anyToString);
					field.set(currentNode, narrovingNumberConversion((Class<? extends Number>) typeTo, longValue));
				}
				return;
			}else if (typeTo == Date.class){
				String dateFormat = null;
				if (view != null){
					dateFormat = (String) view.getTag();
				}
				if (dateFormat == null){
					dateFormat = getDateFormat();
				}
				Date date = new SimpleDateFormat(dateFormat, Locale.getDefault()).parse((String) value);
				field.set(object, typeTo.cast(date));
			}else if (typeTo == Boolean.class || typeTo == boolean.class){
				field.set(object, Boolean.parseBoolean(value.toString()));
			}
			else{
				field.set(object, typeTo.cast(String.valueOf(value)));
			}

		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			//just don't do anything
//			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * For instance, we have the next object structure:
	 * 
	 * <pre>
	 * {@code
	 * {
	 * 	name: "John",
	 * 	surname: "White",
	 * 	documents:{
	 * 	passport:{
	 * 		id: 3
	 * 	}
	 *  }
	 * }
	 * }
	 * </pre>
	 * 
	 * @param path
	 *            has the value ["documents", "passport"]
	 * @param object
	 *            - the object above
	 * @return instance (not a copy!) to object:
	 * 
	 *         <pre>
	 * {@code
	 * passport:{
	 * 	id: 3
	 * }
	 * }
	 * </pre>
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	protected static Object getNode(String[] path, Object object) throws IllegalAccessException, NoSuchFieldException {
		Object currentNode = object; // current node in an object tree
		for (int i = 0; i < path.length - 1; i++) {
			currentNode = currentNode.getClass().getField(path[i]).get(currentNode);
		}
		return currentNode;
	}

	/**
	 * Function that solve the problem with Numbers and narrowing primitive
	 * conversion.
	 * 
	 * @param outputType
	 *            - The type of output
	 * @param value
	 *            - Number object to be narrowed.
	 */
	private static Number narrovingNumberConversion(Class<? extends Number> outputType, Number value) {

		if (value == null) {
			return null;
		}
		if (Byte.class.equals(outputType)) {
			return value.byteValue();
		}
		if (Short.class.equals(outputType)) {
			return value.shortValue();
		}
		if (Integer.class.equals(outputType)) {
			return value.intValue();
		}
		if (Long.class.equals(outputType)) {
			return value.longValue();
		}
		if (Float.class.equals(outputType)) {
			return value.floatValue();
		}
		if (Double.class.equals(outputType)) {
			return value.doubleValue();
		}
		if (byte.class.equals(outputType)) {
			return value.byteValue();
		}
		if (short.class.equals(outputType)) {
			return value.shortValue();
		}
		if (int.class.equals(outputType)) {
			return value.intValue();
		}
		if (long.class.equals(outputType)) {
			return value.longValue();
		}
		if (float.class.equals(outputType)) {
			return value.floatValue();
		}
		if (double.class.equals(outputType)) {
			return value.doubleValue();
		}

		throw new RuntimeException("Can not complete narroving number conversion: output type has invalid value");

	}

	private static final Set<Class<?>> WRAPPER_TYPES = getBasicTypes();

	public static boolean isBasicType(Class<?> clazz) {
		return WRAPPER_TYPES.contains(clazz);
	}

	private static Set<Class<?>> getBasicTypes() {
		Set<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		ret.add(String.class);

		ret.add(boolean.class);
		ret.add(char.class);
		ret.add(byte.class);
		ret.add(short.class);
		ret.add(int.class);
		ret.add(long.class);
		ret.add(float.class);
		ret.add(double.class);
		ret.add(void.class);
		return ret;
	}
}
