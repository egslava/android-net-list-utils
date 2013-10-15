package com.example.netutilssamples;

import java.util.List;
import java.util.ResourceBundle.Control;

import ru.poloniumarts.netutils.ViewMapperAdapter;
import android.app.ListActivity;

import com.example.netutilssamples.entities.Person;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity
public class SimpleListActivity extends ListActivity {
	@Bean
	ViewMapperAdapter	adapter;
	
	@AfterInject
	void init(){
		List<Person> people = Person.generatePeople(1000, this);
		
		//it's a string from AndroidManifest.xml. But for this example we will think
		//that AndroidManifest.xml doesn't contain this metadata
		
		adapter.itemViewPackage = null;
		adapter.setObjects(people);
		setListAdapter(adapter);
	}
}
