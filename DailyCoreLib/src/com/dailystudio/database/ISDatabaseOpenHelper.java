package com.dailystudio.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@Deprecated
public class ISDatabaseOpenHelper extends SQLiteOpenHelper implements
		DatabaseOpenHelper {

	public ISDatabaseOpenHelper(Context context, String name,
			int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
