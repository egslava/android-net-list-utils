package com.example.netutilssamples;

import java.util.List;

import ru.poloniumarts.netutils.ViewMapperAdapter;

import android.app.ListActivity;
import android.view.ViewGroup;

import com.example.netutilssamples.entities.University;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity
public class ComplexStructureActivity extends ListActivity{
	@Bean
	ViewMapperAdapter	adapter;
	
	List<University>	universities;

	@AfterInject
	void init(){
		universities = University.generate(10, 10, this);
		adapter.setObjects(universities);
		setListAdapter(adapter);
	}
}
