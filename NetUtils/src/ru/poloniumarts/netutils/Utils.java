package ru.poloniumarts.netutils;

import java.util.Collection;

import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jess.ui.TwoWayAbsListView;

public class Utils {

	/*only for utils methods */
	private Utils() {}

	public static boolean isEmpty(@SuppressWarnings("rawtypes") Collection collection){
		return !(collection != null && !(collection.isEmpty()));
	}
	
	public static void setAdapter(AbsListView listView, ListAdapter adapter){
		if (listView instanceof ListView){
			((ListView)listView).setAdapter(adapter);
		}else if(listView instanceof GridView){
			((GridView)listView).setAdapter(adapter);
		}
	}
	
	public static void setAdapter(TwoWayAbsListView listView, ListAdapter adapter){
		listView.setAdapter(adapter);
	}
}
