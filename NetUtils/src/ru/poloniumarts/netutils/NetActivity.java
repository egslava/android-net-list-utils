package ru.poloniumarts.netutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.androidquery.AQuery;

@SuppressLint("Registered")
public class NetActivity extends Activity {
	protected AQuery aq;
	protected Handler handler = new Handler();
	
	protected static Integer progressBarId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		tryToGetProgressBarId();
		
		aq = new AQuery(this);
	}
	
	void tryToGetProgressBarId() {
		if (progressBarId == null){
			progressBarId = getResources().getIdentifier("progress", "id", getApplicationContext().getPackageName() );
		}
	}
	
	public void progress(final boolean show){
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
		View view = findViewById(progressBarId);
		if (view != null && view.getVisibility() == View.VISIBLE){
			return true;
		}
		return false;
	}
}
