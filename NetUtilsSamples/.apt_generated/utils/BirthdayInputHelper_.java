//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public final class BirthdayInputHelper_
    extends BirthdayInputHelper
{

    private Context context_;

    private BirthdayInputHelper_(Context context) {
        context_ = context;
        init_();
    }

    public void afterSetContentView_() {
        if (!(context_ instanceof Activity)) {
            return ;
        }
    }

    /**
     * You should check that context is an activity before calling this method
     * 
     */
    public View findViewById(int id) {
        Activity activity_ = ((Activity) context_);
        return activity_.findViewById(id);
    }

    @SuppressWarnings("all")
    private void init_() {
        if (context_ instanceof Activity) {
            Activity activity = ((Activity) context_);
        }
        context = context_;
        init();
    }

    public static BirthdayInputHelper_ getInstance_(Context context) {
        return new BirthdayInputHelper_(context);
    }

    public void rebind(Context context) {
        context_ = context;
        init_();
    }

}
