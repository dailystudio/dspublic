package com.dailystudio.database;

import android.database.sqlite.SQLiteDatabase;

@Deprecated
public interface DatabaseOpenHelper {
	
	public SQLiteDatabase getWritableDatabase();
	public SQLiteDatabase getReadableDatabase();
	
}
