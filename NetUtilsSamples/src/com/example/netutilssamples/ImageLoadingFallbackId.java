package com.example.netutilssamples;

import ru.poloniumarts.netutils.ViewMapper;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.ViewGroup;

@EActivity(R.layout.activity_image_loading_fallback_id)
public class ImageLoadingFallbackId extends Activity{
	@Bean
	ViewMapper mapper;
	
	@ViewById
	ViewGroup root;
	
	@AfterViews
	void init(){
		mapper.map(root, URLs.get());
	}
}

class URLs{
	public String validURL;
	public String invalidURL;
	
	public static URLs get(){
		URLs result = new URLs();
		result.validURL = "http://s1.iconbird.com/ico/1012/ColobrushIcons/w256h2561350817736heart2.png";
		result.invalidURL = "http://blahblahblah.net/blahblahblah.jpg";
		return result;
	}
}