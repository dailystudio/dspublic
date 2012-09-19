package com.dailystudio.database;

import com.dailystudio.manager.ISingletonObject;

import android.database.sqlite.SQLiteDatabase;

@Deprecated
public class RemoteSQLiteDatabase implements ISingletonObject<Long>{

	private long mQueryId;
	private SQLiteDatabase mDb;
	
	public RemoteSQLiteDatabase(long queryId, SQLiteDatabase db) {
		mQueryId = queryId;
		mDb = db;
	}
	
	public void close() {
		if (mDb == null) {
			return;
		}
		
		if (!mDb.isOpen()) {
			return;
		}
		
		mDb.close();
	}

	@Override
	public Long getSingletonKey() {
		return mQueryId;
	}
	
}
