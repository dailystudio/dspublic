package com.dailystudio.database;

import java.util.ArrayList;
import java.util.List;

import com.dailystudio.development.Logger;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;

@Deprecated
public class DatabaseRemoteConnectivity extends DatabaseConnectivity {
	
	private ContentResolver mResolver = null;
	private String mAuthority = null;
	private Uri mBaseUri = null;
	
	public DatabaseRemoteConnectivity(Context context, String authority, String database) {
		super(context, database);
		
		mAuthority = authority;
		
		Uri baseUri = null;
		
		try {
			baseUri = Uri.parse("content://" + mAuthority);
		} catch (NullPointerException e) {
			baseUri = null;
			
			Logger.warnning("authority in NULL");
		}
		
		mResolver = mContext.getContentResolver();
		if (baseUri != null) {
			Builder uriBuilder = baseUri.buildUpon();
			if (uriBuilder != null && mDatabase != null) {
				uriBuilder.appendPath(mDatabase);
				
				mBaseUri = uriBuilder.build();
			}
		}
	}
	
	public String getAuthority() {
		return mAuthority;
	}
	
	public Uri getBaseUri() {
		return mBaseUri;
	}
	
	@Override
	public long insert(DatabaseObject object) {
		Logger.warnning("method is NOT supported");
		return 0;
	}

	@Override
	public int insert(DatabaseObject[] objects) {
		Logger.warnning("method is NOT supported");
		return 0;
	}

	@Override
	public int update(Query query, DatabaseObject object) {
		Logger.warnning("method is NOT supported");
		return 0;
	}

	@Override
	public int delete(Query query) {
		Logger.warnning("method is NOT supported");
		return 0;
	}

	@Override
	public List<DatabaseObject> query(Query query,
			Class<? extends DatabaseObject> projectionClass) {
		if (query == null) {
			return null;
		}
		
		if (mBaseUri == null || mResolver == null) {
			return null;
		}
		
		DatabaseObjectFactory factory = DatabaseObjectFactory.getInstance();
		if (factory == null) {
			return null;
		}
		
		final Class<? extends DatabaseObject> objectClass = query.getObjectClass();
		if (objectClass == null) {
			return null;
		}
		
		String table = DatabaseObject.convertClassToTable(objectClass);
		if (table == null) {
			return null;
		}
		
		Builder builder = mBaseUri.buildUpon();
		if (builder == null) {
			return null;
		}
		
		builder.appendPath(table);
		
		final long queryId = System.currentTimeMillis();
		
		builder.appendPath(String.valueOf(queryId));
		
		final Uri finalUri = builder.build();
		if (finalUri == null) {
			return null;
		}
		
		String selection = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		QueryToken selToken = query.getSelection();
		if (selToken != null) {
			selection = selToken.toString();
		}
		
		QueryToken groupByToken = query.getGroupBy();
		if (groupByToken != null) {
			groupBy = groupByToken.toString();
		}
		
		QueryToken havingToken = query.getHaving();
		if (havingToken != null) {
			having = havingToken.toString();
		}
		
		QueryToken orderByToken = query.getOrderBy();
		if (orderByToken != null) {
			orderBy = orderByToken.toString();
		}
		
		QueryToken limitToken = query.getLimit();
		if (limitToken != null) {
			limit = limitToken.toString();
		}
		
		String projection[] = createProjection(projectionClass);
		Logger.debug("selection: (%s)", selection);
		Logger.debug("groupBy: (%s)", groupBy);
		Logger.debug("having: (%s)", having);
		Logger.debug("orderBy: (%s)", orderBy);
		Logger.debug("limit: (%s)", limit);
		
		Logger.debug("projection: (%s)", projectionToString(projection));
		
		String sortOrder = SortOrderEncoder.encode(
				groupBy, having, orderBy, limit);
		Cursor c = null;
		try {
			Logger.debug("finalUri: (%s)", finalUri);
			Logger.debug("sortOrder: (%s)", sortOrder);

			c = mResolver.query(finalUri, projection, selection, null, sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (c == null || c.getCount() <= 0) {
			if (c != null) {
				c.close();
			}
			return null;
		}
		
		List<DatabaseObject> objects = new ArrayList<DatabaseObject>();
		DatabaseObject object = null;
		
		try {
			if (c.moveToFirst()) {
				do {
					if (projectionClass != null) {
						object = factory.createObject(projectionClass);
					} else {
						object = factory.createObject(objectClass);
					}
					
					if (object != null) {
						object.fillValuesFromCursor(c);
						
						objects.add(object);
					}
				} while (c.moveToNext());
			}
		} finally {
		}
		
		c.close();
		
		broadcastCloseSQLiteDatabase(queryId);
		
		return objects;
	}
	
	private void broadcastCloseSQLiteDatabase(long queryId) {
		if (queryId <= 0) {
			return;
		}
		
		Intent i = 
			new Intent(RemoteSQLiteDatabaseCloseReceiver.ACTION_CLOSE_QUERIED_DATABASE);

		i.putExtra(RemoteSQLiteDatabaseCloseReceiver.EXTRA_QUERY_ID, queryId);
		
		mContext.sendBroadcast(i);
	}

}
