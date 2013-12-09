package ru.poloniumarts.netutils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class DimensionUtils {

    public static float convertDpsToPx(float dps, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, context.getResources().getDisplayMetrics());
    }

    /**
     * Set size of the view as the proportion of screen width.
     * @param widthAspect Aspect ration for the view's width (e.g. 0.5f means "half the screen width")
     * @param heightAspect Aspect ration for the view's height (e.g. 0.5f means "half the screen width")
     */
    public static void setViewSizeFromScreenWidth(View view, float widthAspect, float heightAspect) {
        setViewSizeFromScreenWidth(view, widthAspect, heightAspect, 0, 0);
    }

    /**
     * Set size of the view as the proportion of screen width.
     * @param widthAspect Aspect ration for the view's width (e.g. 0.5f means "half the screen width")
     * @param heightAspect Aspect ration for the view's height (e.g. 0.5f means "half the screen width")
     * @param reduceWidthDp after calculating width, reduce it to the given number of device independent pixels
     * @param reduceHeightDp after calculating height, reduce it to the given number of device independent pixels
     */
    public static void setViewSizeFromScreenWidth(View view, float widthAspect, float heightAspect, int reduceWidthDp, int reduceHeightDp  ) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)view.getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        ViewGroup.LayoutParams lp;
        lp = view.getLayoutParams();
        lp.width = (int) (metrics.widthPixels * widthAspect - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, reduceWidthDp, metrics));
        lp.height = (int) (metrics.widthPixels * heightAspect - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, reduceHeightDp, metrics));
        view.setLayoutParams(lp);
    }
}
