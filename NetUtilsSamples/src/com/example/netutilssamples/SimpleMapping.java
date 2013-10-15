package com.example.netutilssamples;

import java.lang.reflect.Field;
import java.util.GregorianCalendar;

import ru.poloniumarts.netutils.ViewMapper;
import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.example.netutilssamples.entities.Person;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_simple_mapping)
@OptionsMenu(R.menu.main)
public class SimpleMapping extends Activity {
	@Bean
	ViewMapper mapper;
	
	@ViewById 
	ViewGroup root;
	
	Person person = new Person();
	
	@AfterViews
	public void init(){
		person.set("John", "Doe", "Example", new GregorianCalendar(1979, GregorianCalendar.JANUARY, 21).getTime(), "http://neogrotesque.net/wp-content/uploads/avatar-6.jpg");
		mapper.map(root, person);
	}
}

class TransientFields{
	public int				someInt;
	public transient int	someTransient;
}
