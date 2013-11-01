package com.example.netutilssamples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.poloniumarts.netutils.CursorScrollListener;
import ru.poloniumarts.netutils.CursorScrollListener.CursorScrollable;
import ru.poloniumarts.netutils.CursorScrollListener.DataSource;
import ru.poloniumarts.netutils.CursorScrollListener.ListType;
import ru.poloniumarts.netutils.CursorScrollListener_;

import com.example.netutilssamples.entities.Person;
import com.example.netutilssamples.entities.University;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;

import android.app.ListActivity;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

@EActivity
@OptionsMenu(R.menu.dynamic_list_loading_menu)
public class DynamicListLoadingActivity extends ListActivity implements DataSource {
	@Bean
	CursorScrollListener scroller;
	
	ListType	listType;
	
	private void initList(ListType listType) {
		Person.autoIncrement = 0;
		scroller = CursorScrollListener_.getInstance_(this);
		scroller.init(listType, this, getListView());
		this.listType = listType;
	}

	@Override
	public List<? extends CursorScrollable> getNextDataPortion(int startId, String direction) {
		Log.i("getNextDataPortion", String.format("startId: %d,  direction: %s", startId, direction));
		ArrayList<Person> newPeople = new ArrayList<Person>(Person.generatePeople(10, this));
		if (listType == ListType.FROM_TOP_TO_BOTTOM){
			Collections.reverse( newPeople );
		}

		return newPeople;
	}
	
	@OptionsItem
	void FROM_BOTTOM_TO_TOP(){
		initList(ListType.FROM_BOTTOM_TO_TOP);
	}
	
	@OptionsItem
	void FROM_TOP_TO_BOTTOM(){
		initList(ListType.FROM_TOP_TO_BOTTOM);
	}
	
}
