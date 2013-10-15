package com.example.netutilssamples.itemviews;

import ru.poloniumarts.netutils.ViewMapper;
import ru.poloniumarts.netutils.ViewMapperItemView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.netutilssamples.R;
import com.example.netutilssamples.entities.Person;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.person)
public class PersonItemView extends RelativeLayout implements ViewMapperItemView{
	public PersonItemView(Context context) {
		super(context);
	}
	public PersonItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public PersonItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Bean
	ViewMapper	mapper;
	
	Person	currentPerson;
	
	@Override
	public ViewGroup setContentView(Object listItemData, Object listData) {
		if (currentPerson == listItemData){
			return this;
		}
		
		currentPerson = (Person)listItemData;
		mapper.map(this, currentPerson);
		
		return this;
	}
	
	int counter = 0;

	@Click
	void plus(){
		counter++;
		update();
	}
	
	@Click
	void minus(){
		counter--;
		update();
	}
	
	@ViewById
	TextView count;
	
	private void update() {
		count.setText( String.valueOf(counter) );
	}
}
