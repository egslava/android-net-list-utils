package ru.poloniumarts.netutils;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;

import com.androidquery.AQuery;

public class NetFragment extends Fragment{
	
	protected AQuery aq;
	private Handler handler = new Handler();
	
	protected static Integer progressBarId;
	
	@Override
	public void onResume() {
		super.onResume();
		tryToGetProgressBarId();
		aq = new AQuery(getView());
	}
	
	void tryToGetProgressBarId() {
		if (progressBarId == null){
            progressBarId = getResources().getIdentifier("progress", "id", getActivity().getApplicationContext().getPackageName());
		}
	}
	
	public void progress(final boolean show){
		if (aq == null){
			aq = new AQuery(getView());
		}
		if (Looper.getMainLooper().equals(Thread.currentThread())){
			aq.id(progressBarId).visibility(show?View.VISIBLE:View.GONE);
		}else{
			handler.post(new Runnable() {
				@Override
				public void run() {
					aq.id(progressBarId).visibility(show?View.VISIBLE:View.GONE);
				}
			});
		}
	}
	
	public boolean isBlocked(){
        View view = aq.id(progressBarId).getView();
        if (view != null && view.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
	}
}
