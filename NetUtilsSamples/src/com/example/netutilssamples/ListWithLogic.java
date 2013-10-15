package com.example.netutilssamples;

import java.util.List;

import ru.poloniumarts.netutils.ViewMapperAdapter;

import com.example.netutilssamples.entities.Person;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;

import android.app.ListActivity;

@EActivity
public class ListWithLogic extends ListActivity {
	@Bean
	ViewMapperAdapter	adapter;
	
	@AfterInject
	void init(){
		List<Person> people = Person.generatePeople(1000, this);
		adapter.setObjects(people);
		setListAdapter(adapter);
	}
}
