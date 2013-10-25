package com.example.netutilssamples;

import java.util.ArrayList;
import java.util.List;

import ru.poloniumarts.netutils.CursorScrollListener;
import ru.poloniumarts.netutils.CursorScrollListener.CursorScrollable;
import ru.poloniumarts.netutils.CursorScrollListener.DataSource;
import ru.poloniumarts.netutils.CursorScrollListener.ListType;

import com.example.netutilssamples.entities.Person;
import com.example.netutilssamples.entities.University;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;

import android.app.ListActivity;

@EActivity
public class DynamicListLoadingActivity extends ListActivity implements DataSource {
	@Bean
	CursorScrollListener scroller;
	
	@AfterInject
	void init(){
		scroller.init(ListType.FROM_BOTTOM_TO_TOP, this, getListView());
	}

	@Override
	public List<? extends CursorScrollable> getNextDataPortion(int startId, String direction) {
		return new ArrayList<Person>(Person.generatePeople(10, this) );
	}
}
