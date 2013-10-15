package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.TextView;

@EBean
public class BirthdayInputHelper implements OnDateSetListener, OnFocusChangeListener,
		OnClickListener {

	@RootContext
	Context context;
	
	DatePickerDialog datePickerDialog;
	
	// control user should to select to start pick date
	TextView	control;
	
	@AfterInject
	public void init(){
		datePickerDialog = new DatePickerDialog(context, this, 1992, GregorianCalendar.JANUARY, 16);
	}
	
	public void setUp(TextView v){
		control = v;
		control.setOnFocusChangeListener(this);
		control.setOnClickListener(this);
	}
	
	// user has clicked on the date EditText
	@Override
	public void onClick(View arg0) {
		datePickerDialog.show();
	}

	// user has clicked on the date EditText
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			onClick(v);
		}
	}

	// user chosen the birthday
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Date time = new GregorianCalendar(year, monthOfYear, dayOfMonth)
				.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy",
				Locale.getDefault());
		control.setText(formatter.format(time));
	}
}