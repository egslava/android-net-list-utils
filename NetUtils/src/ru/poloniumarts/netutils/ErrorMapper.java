package ru.poloniumarts.netutils;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.HttpStatusCodeException;

import ru.poloniumarts.netutils.ViewMapper.NamingPolicy;
import ru.poloniumarts.netutils.ViewMapper.NamingPolicyDollarWithUndersores;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author Вячеслав (Viacheslav, egslava@gmail.com)
 * This class is used for automatic handling errors from server JSON to your views.
 * 
 * There're several variant of processing error:
 * 1. User is innocent and you can't help him. So you just throw an alert dialog 
 * with text of message and button "OK". For example,
 * 							"Sorry, server is unavailable now. Try again later!"
 * Example of that json is:
 * { error: "Sorry, server is unavailable now. Try again later!" }
 * 
 * 2. User made a mistake and you should point textfields with a hint.
 * For instance, you try to register user and server gives you message:
 * {
 * 	errors:{
 * 		username: "Should have more than 4 symbols",
 * 		password: "Should have more than 8 symbols"
 * }  
 * 
 * So you just should a show that errors on username and password edittexts, properly.
 * 
 * ATTENTION!!!
 * Your class have to name root layout as "root"
 */

@EBean
public class ErrorMapper {
	private static final int INVALID_TOKEN_STATUS_CODE = 401;

	@ViewById
	View root;
	
	@RootContext
	Context context;
	
	/** May be null! */
	@RootContext
	Activity activity;
	
	Handler handler = new Handler();
	
	public NamingPolicy		namer = new NamingPolicyDollarWithUndersores();
	
	public interface ExceptionObserver{
		public void onError(Exception e, ErrorMapper m);
	}
	
	public ExceptionObserver observer;
	
	@SuppressWarnings("unchecked")
	@UiThread
	public void map(JSONObject	json){
		JSONObject errors = json.optJSONObject("errors");
		Iterator<String> keys = errors.keys();
		while(keys.hasNext()){
			String key = keys.next();
			Object value = errors.opt(key);
			
			String valueToMap;
			if (value.getClass().isArray()) {
				valueToMap = StringUtils.join(value);
			} else if (value instanceof String) {
				valueToMap = (String) value;
			} else if (value instanceof JSONArray) {
				try {
					valueToMap = ((JSONArray) value).join(", ");
				} catch (JSONException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			} else {
				throw new UnsupportedOperationException(
						"Unknown type of error to map");
			}

			String idString = namer.fieldToId( new String[]{ key } );
			final String packageName = root.getContext().getPackageName();
			View view = root.findViewById(root.getResources().getIdentifier(idString, "id", packageName));
			
			if (view instanceof TextView){
				TextView text = (TextView) view;
				text.setError(valueToMap);
			}else{
				new AlertDialog.Builder(context)
					.setTitle(key)
					.setMessage(valueToMap)
					.setNeutralButton(android.R.string.ok, null)
					.show();
			}
		}
	}
	
	public void process(String response){
		process(response, null);
	}

	public void process(String response, OnClickListener onClickListener){
		JSONObject json;
		try {
			json = new JSONObject(response);
			if (json.has("errors")){
				map(json);
			};
			if (json.has("error")){
				alertDialog( json.getString("error"), onClickListener);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			alertDialog(response, onClickListener);
		}
	}

	private final int ALERT = android.R.string.dialog_alert_title;

	protected TokenCleaner cleaner;
	public void setTokenCleaner(TokenCleaner cleaner){
		this.cleaner = cleaner;
	}
	private void alertDialog(final String message, final DialogInterface.OnClickListener listener) {
		if (Thread.currentThread().equals(Looper.getMainLooper().getThread())){
			new AlertDialog.Builder(context)
				.setTitle(ALERT)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, listener)
				.show();
		}else{
			handler.post(new Runnable() {
				@Override
				public void run() {
					new AlertDialog.Builder(context)
					.setTitle(ALERT)
					.setMessage(message)
					.setPositiveButton(android.R.string.ok, listener)
					.show();
				}
			});	
		}
	}
	
	public void process(Exception e){
		process(e, false);
	}


	/**
	 * @param notify if is true and subscribers exists - don't do anything, just notify subscribers 
	 * */
	public void process(Exception e, boolean notify){
		process(e, notify, null);
	}


	/**
	 * @param notify if is true and subscribers exists - don't do anything, just notify subscribers 
	 * @param onClickListener TODO
	 * */
	public void process(Exception e, boolean notify, OnClickListener onClickListener){
		if (notify && observer != null){
			observer.onError(e, this);
			return;
		}
		
		if (e instanceof HttpStatusCodeException){
			HttpStatusCodeException scException = (HttpStatusCodeException) e;
			process(scException, onClickListener);
			return;
		}
		
		if ( !StringUtils.isEmpty(e.getLocalizedMessage())){
			alertDialog(e.getLocalizedMessage(), onClickListener);
		}else{
			alertDialog(e.getMessage(), onClickListener);
		}
	}
	
	private void process(HttpStatusCodeException	exception, final OnClickListener onClickListener){
		if (exception.getStatusCode().value() == INVALID_TOKEN_STATUS_CODE){
			alertDialog(exception.getMessage(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (onClickListener != null){
						onClickListener.onClick(dialog, which);
					}
					cleaner.clear();
//					LoginActivity_.intent(context)
//					.flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
//					.startWithoutAutoLogin(true)
//					.start();
					if (activity != null){
						activity.finish();
					}
				}
			});
			return;
		}
		String response = exception.getResponseBodyAsString();
		process(response, onClickListener);
	}
}
