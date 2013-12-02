package ru.poloniumarts.netutils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import ru.poloniumarts.netutlis.BuildConfig;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

// TODO: write doc
/**
 * This class is used for safe progress dialog callings...
 * Use this class instead of standard ProgressDialog
 * 
 * @author Вячеслав
 * 
 */
public class ProgressDialogInstance {
	static private final boolean		DEBUG			= BuildConfig.DEBUG;
	static private final String			TRYING_AGAIN	= "You're trying to show ProgressDialog again!";
	private static final String			HIDE_UNEXISTING	= "You're trying to hide unexisting ProgressDialog!";
	private static final String			GET_UNEXISTING	= "You're trying to get unexisting ProgressDialog!";
	private static final CharSequence	DEFAULT_TEXT	= "Please, wait";
	private static final CharSequence	DEFAULT_TITLE	= "Loading...";
	private static final String			CONTEXT_IS_NULL	= "You're trying to put null context";
	private static final String			DIALOG_IS_NULL	= "You're trying to put null dialog";

	static Map<Context, ProgressDialog>	mapper			= new HashMap<Context, ProgressDialog>();

	public static void show(Context context, ProgressDialog dialog) {
		if (context == null && DEBUG) {
			throw new InvalidParameterException(CONTEXT_IS_NULL);
		}
		if (dialog == null && DEBUG) {
			throw new InvalidParameterException(DIALOG_IS_NULL);
		}
		mapper.put(context, dialog);
	}

	public static void show(Context context,
							CharSequence title,
							CharSequence message,
							boolean indeterminate,
							boolean cancelable,
							DialogInterface.OnCancelListener cancelListener) {
		if (alreadyInUse(context)) {
			return;
		}
		ProgressDialog dialog = ProgressDialog.show(context, title, message, indeterminate, cancelable, cancelListener);
		show(context, dialog);
	}

	public static void show(Context context,
							CharSequence title,
							CharSequence message,
							boolean indeterminate,
							boolean cancelable) {
		if (alreadyInUse(context)) {
			return;
		}
		ProgressDialog dialog = ProgressDialog.show(context, title, message, indeterminate, cancelable);
		show(context, dialog);
	}

	public static void show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
		if (alreadyInUse(context)) {
			return;
		}
		ProgressDialog dialog = ProgressDialog.show(context, title, message, indeterminate);
		show(context, dialog);
	}

	public static void show(Context context, CharSequence title, CharSequence message) {
		if (alreadyInUse(context)) {
			return;
		}
		ProgressDialog dialog = ProgressDialog.show(context, title, message);
		show(context, dialog);
	}

	private static boolean alreadyInUse(Context context) {
		if (mapper.containsKey(context)) {
			if (DEBUG) {
				throw new IllegalStateException(TRYING_AGAIN);
			}
			return true;
		}
		return false;
	}

	public static void cancel(Context context) {
		if (!mapper.containsKey(context)) {
			if (DEBUG) {
				throw new IllegalStateException(HIDE_UNEXISTING);
			}
		}
		mapper.get(context).cancel();
		mapper.remove(context);
	}

	public static ProgressDialog get(Context context) {
		if (!mapper.containsKey(context)) {
			if (DEBUG) {
				throw new IllegalStateException(GET_UNEXISTING);
			}
			return ProgressDialog.show(context, DEFAULT_TITLE, DEFAULT_TEXT);
		}
		return mapper.get(context);
	}
}
