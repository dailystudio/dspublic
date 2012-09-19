package com.dailystudio.app.loader;

import java.util.List;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.database.DatabaseConnectivity;
import com.dailystudio.dataobject.database.DatabaseObserver;
import com.dailystudio.dataobject.query.Query;

import android.content.Context;

public abstract class DatabaseObjectsLoader<T extends DatabaseObject> 
	extends AbsAsyncDataLoader<List<T>> {

	private class PrivateDatabaseObserver extends DatabaseObserver {

		public PrivateDatabaseObserver(Context context,
				Class<? extends DatabaseObject> klass) {
			super(context, klass);
		}

		@Override
		protected void onDatabaseChanged(Context context,
				Class<? extends DatabaseObject> objectClass) {
			onContentChanged();
		}
		
	};
	
	private DatabaseObserver mDataObserver;
	
	public DatabaseObjectsLoader(Context context) {
		super(context);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> loadInBackground() {
		if (mDataObserver != null) {
			mDataObserver.unregister();
			mDataObserver = null;
		}
		
		final Class<? extends DatabaseObject> objectClass = getObjectClass();
		if (objectClass == null) {
			return null;
		}
		
		final DatabaseConnectivity connectivity = 
			getDatabaseConnectivity(objectClass);
		if (connectivity == null) {
			return null;
		}
				
		final Query query = getQuery(objectClass);
		if (query == null) {
			return null;
		}
		
		final Class<? extends DatabaseObject> projectionClass =
			getProjectionClass();
	
		List<DatabaseObject> data = null;
		if (projectionClass == null) {
			data = connectivity.query(query);
		} else {
			data = connectivity.query(query, projectionClass);
		}
		
		mDataObserver = new PrivateDatabaseObserver(getContext(), objectClass);
		mDataObserver.register();
		
		return (List<T>)data;
	}
	
	protected DatabaseConnectivity getDatabaseConnectivity(
			Class<? extends DatabaseObject> objectClass) {
		return new DatabaseConnectivity(getContext(), objectClass);
	}
	
	protected Query getQuery(Class<? extends DatabaseObject> klass) {
		return new Query(klass);
	}
	
	protected Class<? extends DatabaseObject> getProjectionClass() {
		return null;
	}

	abstract protected Class<? extends DatabaseObject> getObjectClass();
	
}
