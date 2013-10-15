package ru.poloniumarts.netutils;

import java.math.BigInteger;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

@EViewGroup
public class MapperItemView extends RelativeLayout implements ViewMapperItemView{
	@Bean
	ViewMapper mapper;

	Object data;
	
	public MapperItemView(Context context) {
		super(context);
	}

	public MapperItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MapperItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@SuppressWarnings("unused")
	@Override
	public MapperItemView setContentView(Object listItemData, Object listData){
		View view;

		if (this.data == null){
			inflate(getContext(), ViewMapper.getLayoutIdForObject(listItemData, getContext()), this);
		};
		
		if (this.data != listItemData){
			//TODO: remove this profile info
			long startTime = System.nanoTime();
			mapper.map(this, listItemData);
			long endTime = System.nanoTime();
			Log.e("map", "Aqquired " + (endTime - startTime) + " nanosec");
			this.data = listItemData;
		}
		
		return this;
	}
	BigInteger sumTime; //TODO: remove this
	int			sumCount;//TODO: remove this
	
}
