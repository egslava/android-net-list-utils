package com.example.netutilssamples.entities;

import java.util.ArrayList;
import java.util.Random;

import ru.poloniumarts.netutils.CursorScrollListener.CursorScrollable;
import ru.poloniumarts.netutils.Enableable;

public class EnableableDemo implements Enableable, CursorScrollable {

	public String text;
	protected boolean isEnabled;
	
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public EnableableDemo(boolean isEnabled) {
		super();
		this.isEnabled = isEnabled;
		if (isEnabled){
			text = "This row is enabled";
		}else{
			text = "This row is disabled";
		}
	}
	
	public static ArrayList<EnableableDemo> generate(int count){
		ArrayList<EnableableDemo> result = new ArrayList<EnableableDemo>();
		
		Random random = new Random();
		
		for (int i = 0; i < count; i++){
			result.add( new EnableableDemo(random.nextBoolean()));
		}
		
		return result;
	}

	protected static int autoIncrement;
	
	@Override
	public int getId() {
		return autoIncrement++;
	}
}
