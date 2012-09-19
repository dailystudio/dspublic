package com.dailystudio.database;

import com.dailystudio.development.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

@Deprecated
public class RemoteSQLiteDatabaseCloseReceiver extends BroadcastReceiver {

	public final static String ACTION_CLOSE_QUERIED_DATABASE = "dailystudio.intent.ACTION_CLOSE_QUERIED_DATABASE";
	
	public final static String EXTRA_QUERY_ID = "queryid";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (context == null || intent == null) {
			return;
		}

		final String action = intent.getAction();
		if (ACTION_CLOSE_QUERIED_DATABASE.equals(action)) {
			long queryId = intent.getLongExtra(EXTRA_QUERY_ID, 0);
			Logger.debug("queryId = %d", queryId);
			if (queryId <= 0) {
				return;
			}
			
			RemoteSQLiteDatabaseMananger dbmgr = 
				RemoteSQLiteDatabaseMananger.getInstance();
			if (dbmgr == null) {
				return;
			}
			
			RemoteSQLiteDatabase db = dbmgr.removeObject(queryId);
			Logger.debug("db = %s", db);
			
			if (db != null) {
				db.close();
			}
			
			context.unregisterReceiver(this);
		}
	}

}
