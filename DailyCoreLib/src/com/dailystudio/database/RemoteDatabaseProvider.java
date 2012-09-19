package com.dailystudio.database;

import java.util.List;

import com.dailystudio.development.Logger;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

@Deprecated
public class RemoteDatabaseProvider extends ContentProvider {

	private final static int SEG_INDEX_DATABASE = 0;
	private final static int SEG_INDEX_TABLE = SEG_INDEX_DATABASE + 1;
	private final static int SEG_INDEX_QUERY_ID = SEG_INDEX_DATABASE + 2;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Logger.debug("uri(%s)", uri);
		
		final String database = parseDatabaseFromUri(uri);
		final String table = parseTableFromUri(uri);
		final long queryId = parseQueryIdFromUri(uri);
		Logger.debug("database(%s)", database);
		Logger.debug("table(%s)", table);
		Logger.debug("queryId(%d)", queryId);
		
		final Context context = getContext();
		if (context == null) {
			return null;
		}
		
		SQLiteOpenHelper dbhelper = 
			new ISDatabaseOpenHelper(getContext(), database, 0x1);
		
		final SQLiteDatabase db = dbhelper.getReadableDatabase();
		if (db == null) {
			return null;
		}

		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;

		String[] decode = SortOrderEncoder.decode(sortOrder);
		if (decode != null) {
			groupBy = decode[0];
			having = decode[1];
			orderBy = decode[2];
			limit = decode[3];
		}
		
		Logger.debug("selection: (%s)", selection);
		Logger.debug("groupBy: (%s)", groupBy);
		Logger.debug("having: (%s)", having);
		Logger.debug("orderBy: (%s)", orderBy);
		Logger.debug("limit: (%s)", limit);
		
		Cursor c = null;
		try {
			c = db.query(table, projection, selection, null, groupBy, having, orderBy, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Logger.debug("c: (%s, count: %d)", 
				c,
				(c == null ? 0 : c.getCount()));
		if (c == null || c.getCount() <= 0) {
			if (c != null) {
				c.close();
			}
			
			db.close();
			
			return null;
		}
		
//		db.close();
		pendingToClosedDatabase(queryId, db);
		
		return c;
	}

	private void pendingToClosedDatabase(long queryId, SQLiteDatabase db) {
		if (queryId <= 0 || db == null) {
			return;
		}
		
		final Context context = getContext();
		if (context == null) {
			return;
		}

		RemoteSQLiteDatabaseMananger dbmgr = RemoteSQLiteDatabaseMananger.getInstance();
		if (dbmgr == null) {
			return;
		}
		
		dbmgr.addObject(new RemoteSQLiteDatabase(queryId, db));

		BroadcastReceiver receiver = new RemoteSQLiteDatabaseCloseReceiver();
		
		IntentFilter filter = new IntentFilter(RemoteSQLiteDatabaseCloseReceiver.ACTION_CLOSE_QUERIED_DATABASE);
		
		context.registerReceiver(receiver, filter);
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
	
	private String getSegmentFromUri(Uri uri, int segmentIndex) {
		if (uri == null) {
			return null;
		}
	
		List<String> segments = uri.getPathSegments();
		if (segments == null || segments.size() <= 0) {
			return null;
		}
		
		if (segmentIndex < 0 || segmentIndex >= segments.size()) {
			return null;
		}

		return segments.get(segmentIndex);
	}
	
	private String parseDatabaseFromUri(Uri uri) {
		return getSegmentFromUri(uri, SEG_INDEX_DATABASE);
	}

	private String parseTableFromUri(Uri uri) {
		return getSegmentFromUri(uri, SEG_INDEX_TABLE);
	}

	private long parseQueryIdFromUri(Uri uri) {
		String queryIdStr = getSegmentFromUri(uri, SEG_INDEX_QUERY_ID);
		
		long queryId = 0;
		try {
			queryId = Long.parseLong(queryIdStr);
		} catch (NumberFormatException e) {
			queryId = 0;
		}
		
		return queryId;
	}

}
