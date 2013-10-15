package ru.poloniumarts.netutils;

import java.util.List;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.api.Scope;
import com.jess.ui.TwoWayAbsListView;

@EBean(scope=Scope.Default)
public class CursorScrollListener implements OnScrollListener, com.jess.ui.TwoWayAbsListView.OnScrollListener {

	public static final int EMPTY_ID = -1;
	
	public DataSource dataSource;

	volatile public int smallestId = 	EMPTY_ID;
	volatile public int largestId = 	EMPTY_ID;
	private boolean isLoading;

	private boolean hasMoreDataUp = true;
	
	public ListType listType = ListType.FROM_BOTTOM_TO_TOP;
	
	public int numLoadedPages;
	public OnDataLoadedListened onDataLoadedListener;
	
	@SuppressWarnings("rawtypes")
	@Bean
	public ViewMapperAdapter adapter;
	
	public enum ListType{
		FROM_TOP_TO_BOTTOM,
		FROM_BOTTOM_TO_TOP
	}
	
	protected AbsListView absListView;
	protected TwoWayAbsListView twoWayAbsListView;

	protected boolean scrollBlock;
	
	/** (!!!) <b>Attention!</b> It override your list onScrollListener */
	public void init(ListType listType, DataSource	dataSource, AbsListView absListView){
		this.listType = listType;
		this.dataSource = dataSource;
		this.absListView = absListView;
		Utils.setAdapter(absListView, adapter);
		absListView.setOnScrollListener(this);
		this.twoWayAbsListView = null;
	}
	
	/** (!!!) <b>Attention!</b> It override your list onScrollListener */
	public void init(ListType listType, DataSource	dataSource, TwoWayAbsListView twoWayAbsListView){
		this.listType = listType;
		this.dataSource = dataSource;
		this.twoWayAbsListView = twoWayAbsListView;
		twoWayAbsListView.setAdapter(adapter);
		twoWayAbsListView.setOnScrollListener(this);
		this.absListView = null;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScrollStateChanged(TwoWayAbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		onScroll(adapter, firstVisibleItem, visibleItemCount, totalItemCount);
	}
	
	@Override
	public void onScroll(TwoWayAbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		onScroll(adapter, firstVisibleItem, visibleItemCount, totalItemCount);
	}

	protected int lastFirstVisibleItem = 0;
	void onScroll(ListAdapter adapter, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (scrollBlock){
			scrollBlock = false;
			return;
		}
		if (isLoading || (!hasMoreDataUp)) {
			return;
		}

		switch (listType) {
		
		case FROM_BOTTOM_TO_TOP:
			if (firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
				isLoading = true;
				getNextDataPortion(smallestId, Direction.UP, adapter);
			}
			break;
			
		case FROM_TOP_TO_BOTTOM:
			if (firstVisibleItem <= totalItemCount / 2){
				isLoading = true;
				getNextDataPortion(smallestId, Direction.UP, adapter);
			}
			break;
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Background
	void getNextDataPortion(int startId, String direction, ListAdapter listAdapter) {
		if (listAdapter == null) {
			throw new RuntimeException("Please! Set up listAdapter before call setOnScrollListener, loadNewier or loadOlder");
		}
		if (dataSource == null){
			throw new RuntimeException("Please! Set up dataSource before setOnScrollListener call or loadNewier or loadOlder");
		}
		try {
			List<? extends CursorScrollable> nextDataPortion = dataSource.getNextDataPortion(startId, direction);
			if (nextDataPortion.size() == 0) {
				if (direction == Direction.UP){
					hasMoreDataUp = false;
				}
				return;
			}
			ViewMapperAdapter adapter = (ViewMapperAdapter) listAdapter;

			if(Utils.isEmpty(adapter.getObjects())){
				switch(listType){
				case FROM_BOTTOM_TO_TOP:
					smallestId = nextDataPortion.get(nextDataPortion.size() - 1).getId();
					largestId = nextDataPortion.get(0).getId();
					break;
				case FROM_TOP_TO_BOTTOM:
					smallestId = nextDataPortion.get(0).getId();
					largestId = nextDataPortion.get( nextDataPortion.size() - 1 ).getId();
					break;
				}
			}
			
			if (direction.equals(Direction.UP)){
				switch(listType){
				case FROM_BOTTOM_TO_TOP:
					adapter.getObjects().addAll(nextDataPortion);
					smallestId = nextDataPortion.get(nextDataPortion.size() - 1).getId();
					break;
				case FROM_TOP_TO_BOTTOM:
					adapter.getObjects().addAll(0, nextDataPortion);
					smallestId = nextDataPortion.get(0).getId();
					break;
				}
			}else if (direction.equals(Direction.DOWN)){
				switch(listType){
				case FROM_TOP_TO_BOTTOM:
					adapter.getObjects().addAll(nextDataPortion);
					largestId = nextDataPortion.get( nextDataPortion.size() - 1 ).getId();
					break;
				case FROM_BOTTOM_TO_TOP:
					adapter.getObjects().addAll(0, nextDataPortion);
					largestId = nextDataPortion.get(0).getId();
					break;
				}
			}

//			scrollBlock  = true;
//			if (direction.equals(Direction.UP)){
				notifyDataSetChanged(adapter, direction, nextDataPortion.size());
//			}
			
		} catch (Throwable e) {
//			if ( e.getStatusCode().value() == )
			//TODO: see droplr screenshot - print a stacktrace
			e.printStackTrace();
//			hasMoreDataUp = false;
			if(onDataLoadedListener != null){
				onDataLoadedListener.onDataLoaded(numLoadedPages);
			}else{
				System.nanoTime();
			}
			throw new RuntimeException(e);
		} finally {
			isLoading = false;
		}
	}

	// snippet
	//сохраняем положение
	//	int index = absListView.getFirstVisiblePosition();
	//	View v = absListView.getChildAt(0);
	//	int top = (v == null) ? 0 : v.getTop();

	//	notifyDataSetChanged();
		
	//	((ListView)absListView).setSelectionFromTop(index, top);
	
	protected boolean hasFirstDataPortionLoaded;
	
	@SuppressWarnings("rawtypes")
	@UiThread
	void notifyDataSetChanged(ListAdapter adapter, String direction, int numOfNewElements) {
		if (onDataLoadedListener != null){
			onDataLoadedListener.onDataLoaded(numLoadedPages);
			numLoadedPages++;
		}
		
		if (!changedUpperPartOfList(direction)){
			((ViewMapperAdapter) adapter).notifyDataSetChanged();
			return;
		}
		
		int index = 0;
		int top = 0;
		if (absListView != null){
			index = absListView.getFirstVisiblePosition();
			View v = absListView.getChildAt(0);
			top = (v == null) ? 0 : v.getTop();
		}else if (twoWayAbsListView != null){
			index = twoWayAbsListView.getFirstVisiblePosition();
			View v = absListView.getChildAt(0);
			top = (v == null) ? 0: v.getTop();
		}
		
		((ViewMapperAdapter) adapter).notifyDataSetChanged();
		
		if(absListView != null){
			//success!!!
			if (absListView instanceof ListView){
				((ListView)absListView).setSelectionFromTop(index + numOfNewElements, top);
			}else{
				absListView.setSelection(index + numOfNewElements);
				absListView.scrollBy(0, top);
			}
		}
	}

	private boolean changedUpperPartOfList(String direction) {
		if (direction == Direction.UP && listType == ListType.FROM_TOP_TO_BOTTOM){
			return true;
		}
		
		if (direction == Direction.DOWN && listType == ListType.FROM_BOTTOM_TO_TOP){
			return true;
		}
		
		return false;
	}
	
	public void loadEarlier(){
		if (isLoading || (!hasMoreDataUp)) {
			return;
		}
		isLoading = true;
		getNextDataPortion(smallestId, Direction.UP, adapter);
	}
	
	public void loadNewier(){
		if (isLoading) {
			return;
		}
		isLoading = true;
		getNextDataPortion(largestId, Direction.DOWN, adapter);
	}

	public interface DataSource {
		public List<? extends CursorScrollable> getNextDataPortion(int startId, String direction);
	}
	
	public interface OnDataLoadedListened{

		/**
		 * 
		 * @param numberOfTimes
		 *            - number of times when data is loading. For example, if
		 *            data is needed first time the parameter will be equals 0
		 */
		public void onDataLoaded(int numberOfTimes);
	}

	public interface CursorScrollable {
		public int getId();
	}
}
