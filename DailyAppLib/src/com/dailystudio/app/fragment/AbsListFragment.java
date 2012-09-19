package com.dailystudio.app.fragment;

import com.dailystudio.app.widget.DeferredHandler;
import com.dailystudio.development.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

public abstract class AbsListFragment<T> extends AbsLoaderFragment<T>
	implements OnItemClickListener {
	
	public interface OnListItemSelectedListener {
		
        public void onListItemSelected(Object itemData);
        
    }

	private AbsListView mListView;
	private ListAdapter mAdapter;
	
    private OnListItemSelectedListener mOnListItemSelectedListener;
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindListView();
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		if (mOnListItemSelectedListener != null) {
			final ListAdapter adapter = getListAdapter();
			if (adapter == null) {
				return;
			}
			
			Object data = adapter.getItem(position);
			
			mOnListItemSelectedListener.onListItemSelected(data);
		}
	}

	protected int getListViewId() {
		return android.R.id.list;
	}
	
	protected void bindListView() {
		final View fragmentView = getView();
		if (fragmentView == null) {
			return;
		}

		AbsListView oldListView = mListView;

		if (oldListView != null) {
			oldListView.clearDisappearingChildren();
			oldListView.clearAnimation();
			oldListView.setAdapter(null);
			oldListView.setOnItemClickListener(null);
			oldListView.setVisibility(View.GONE);
		}
		
		mAdapter = onCreateAdapter();
		
		mListView = (AbsListView) fragmentView.findViewById(getListViewId());
		if (mListView != null) {
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(this);
			mListView.setVisibility(View.VISIBLE);
			mListView.scheduleLayoutAnimation();
		}
	}
	
	public ListAdapter getListAdapter() {
		return mAdapter;
	}
	
	public AbsListView getListView() {
		return mListView;
	}
	
 	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        if (activity instanceof OnListItemSelectedListener) {
        	mOnListItemSelectedListener = (OnListItemSelectedListener) activity;
        } else {
        	Logger.warnning("host activity does not implements: %s", 
        			OnListItemSelectedListener.class.getSimpleName());
        }
    }

 	@Override
 	public void onLoadFinished(Loader<T> loader, T data) {
 		bindData(mAdapter, data);
 	};
 	
 	@Override
 	public void onLoaderReset(Loader<T> loader) {
 		bindData(mAdapter, null);
 	};
 	
 	protected void removeCallbacks(Runnable r) {
 		mHandler.removeCallbacks(r);
 	}
 	
 	protected void post(Runnable r) {
 		mHandler.post(r);
 	}
 	
 	protected void postDelayed(Runnable r, long delayMillis) {
 		mHandler.postDelayed(r, delayMillis);
 	}
 	
	protected void notifyAdapterChanged() {
		post(notifyAdapterChangedRunnable);
	}
 	
	protected void notifyAdapterChangedOnIdle() {
		mDeferredHandler.postIdle(notifyAdapterChangedRunnable);
	}
 	
	abstract protected void bindData(ListAdapter listAdapter, T data);

	abstract protected ListAdapter onCreateAdapter();
	
	private Handler mHandler = new Handler();
	private DeferredHandler mDeferredHandler = new DeferredHandler();
	
	private Runnable notifyAdapterChangedRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mAdapter instanceof BaseAdapter == false) {
				return;
			}
			
			((BaseAdapter)mAdapter).notifyDataSetChanged();
		}
		
	};


}
