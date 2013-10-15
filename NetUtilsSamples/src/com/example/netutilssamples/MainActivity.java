package com.example.netutilssamples;

import ru.poloniumarts.netutils.SuperSimpleAdapter;
import android.app.ListActivity;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;

@EActivity
public class MainActivity extends ListActivity {

	private String[][] menu = new String[][] { 
		{"Simple mapping", "ViewMapper.map"},
		{"Simple unmapping", "ViewMapper.unmap"},
		{"Simple list", "ViewMapperAdapter"},
		{"List with logic in list items", "ViewMapperAdapter and ItemViews"},
		{"Complex structure", "Can't have logic (now)"},
		{"Image loading (fallback url)", "ImageView tag for fallback id"} 
	};
	
	@Bean
	SuperSimpleAdapter adapter;

	@AfterInject
	public void init() {
		adapter
			.setLayout(android.R.layout.simple_list_item_2)
			.setMap(new int[] { android.R.id.text1, android.R.id.text2 })
			.setItemsByRows((Object[][])menu );
		setListAdapter(adapter);
	}
	
	@ItemClick
	public void listItemClicked(int position) {
		switch (position) {
		case 0:
			SimpleMapping_.intent(this).start();
			break;
		case 1:
			SimpleUnmapping_.intent(this).start();
			break;
		case 2:
			SimpleListActivity_.intent(this).start();
			break;
		case 3:
			ListWithLogic_.intent(this).start();
			break;
		case 4:
			ComplexStructureActivity_.intent(this).start();
			break;
		case 5:
			ImageLoadingFallbackId_.intent(this).start();
			break;
		}
	}
}
