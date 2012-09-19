package com.dailystudio.database;

import com.dailystudio.manager.Manager;
import com.dailystudio.manager.SingletonManager;

@Deprecated
public class RemoteSQLiteDatabaseMananger extends SingletonManager<Long, RemoteSQLiteDatabase> {
	
	public static synchronized RemoteSQLiteDatabaseMananger getInstance() {
		return Manager.getInstance(RemoteSQLiteDatabaseMananger.class);
	}

}
