package ru.poloniumarts.netutils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;

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
			mapper.map(this, listItemData);
			this.data = listItemData;
		}
		
		return this;
	}
}
