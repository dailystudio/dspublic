package com.dailystudio.app.fragment;

import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public abstract class AbsCursorAdapterFragment extends AbsAdapterFragment<Cursor, Cursor> {
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		/*
		 * XXX: Here we could not swapCursor(), or
		 * 		after Oreintation is changed, loader will 
		 * 		return the lasted cursor (which is closed in
		 * 		swapCursor() and adapter will
		 * 		reference a closed Cursor and throw exception.
		 */
//		swapCursor(null);
	}

	@Override
	protected void bindAdapterView() {
		final ListAdapter oldAdapter = getAdapter();
		
		Cursor oldCursor = null;

		if (oldAdapter instanceof CursorAdapter) {
			oldCursor = ((CursorAdapter)oldAdapter).getCursor();
		}

		super.bindAdapterView();
		
		if (oldCursor != null) {
			swapCursor(oldCursor);
		}
	}
	
	@Override
	protected void bindData(BaseAdapter adapter, Cursor data) {
		swapCursor(data);
	}

	protected void swapCursor(Cursor c) {
		final BaseAdapter adapter = getAdapter();
		if (adapter instanceof CursorAdapter) {
			Cursor oldCursor = ((CursorAdapter)adapter).swapCursor(c);
			
			if (oldCursor != null && oldCursor != c) {
				oldCursor.close();
			}
		}
	}
	
}
