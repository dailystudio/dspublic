package com.dailystudio.app.fragment;

import java.util.List;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public abstract class AbsArrayAdapterFragment<T> extends AbsAdapterFragment<T, List<T>> {
	
    @Override
    protected void bindData(BaseAdapter adapter, List<T> data) {
        if (data == null) {
            return;
        }
        
        if (adapter instanceof ArrayAdapter == false) {
            return;
        }
        
        @SuppressWarnings("unchecked")
        ArrayAdapter<T> objectAdapter =
            (ArrayAdapter<T>)adapter;
        
        objectAdapter.clear();
        
        for (T o: data) {
            objectAdapter.add(o);
        }
    }
	
}
