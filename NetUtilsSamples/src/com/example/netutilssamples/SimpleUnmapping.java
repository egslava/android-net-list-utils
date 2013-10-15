package com.example.netutilssamples;

import ru.poloniumarts.netutils.ViewMapper;
import utils.BirthdayInputHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.netutilssamples.entities.Person;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_simple_unmapping)
@OptionsMenu(R.menu.main)
public class SimpleUnmapping extends Activity {
	@Bean
	ViewMapper mapper;
	
	@ViewById 
	ViewGroup root;
	
	@ViewById
	EditText $birthday;
	
	Person person = new Person();
	
	@Bean
	BirthdayInputHelper birthdayInputHelper;

	@AfterViews
	public void init(){
		birthdayInputHelper.setUp($birthday);
	}
	
	@Click
	public void showUnmappedUser(){
		try{
			mapper.unmap(root, person);
			person.alert(this);
		}catch(Throwable t){
			new AlertDialog.Builder(this)
				.setTitle(t.getMessage())
				.show();
			t.printStackTrace();
		}
	}
}
