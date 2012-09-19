package com.dailystudio.database;

import java.util.ArrayList;
import java.util.List;

import com.dailystudio.development.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@Deprecated
public class DatabaseLocalConnectivity extends DatabaseConnectivity {

	private SQLiteOpenHelper mDBOpenHelper = null;
	
	public DatabaseLocalConnectivity(Context context, String database) {
		super(context, database);

		if (mDatabase != null) {
			mDBOpenHelper = new ISDatabaseOpenHelper(mContext, database, 0x1);
		}
	}
	
	@Override
	public long insert(DatabaseObject object) {
		Logger.debug("object(%s), isEmpty(%s)", object, object.isEmpty());
		if (object == null || object.isEmpty()) {
			return 0;
		}
		
		if (mDBOpenHelper == null) {
			return 0;
		}
		
		final SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
		if (db == null) {
			return 0;
		}
		
		final ContentValues values = object.getValues();
		if (values == null) {
			db.close();
			
			return 0;
		}
		
		final Template template = object.getTemplate();
		if (template == null) {
			db.close();
			
			return 0;
		}
		
		final String table = DatabaseObject.convertClassToTable(object.getClass());
		if (table == null) {
			db.close();
			
			return 0;
		}
		
		if (checkOrCreateTable(db, table, template) == false) {
			return 0;
		}
		
		long rowId = -1;
		db.beginTransaction();
		try {
			rowId = db.insert(table, object.handleNullProjection(), values);
			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		db.close();
		
		if (rowId <= 0) {
			return 0;
		}
		
		return rowId;
	}

	@Override
	public int insert(DatabaseObject[] objects) {
		if (objects == null) {
			return 0;
		}
		
		final int count = objects.length;
		if (count <= 0) {
			return 0;
		}
		
		if (mDBOpenHelper == null) {
			return 0;
		}
		
		final SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
		if (db == null) {
			return 0;
		}
		
		int successful = 0;

		db.beginTransaction();
		try {
			ContentValues values = null;
			DatabaseObject object = null; 
			Template template = null;
			String table = null;
			long rowId = -1;
			
			for (int i = 0; i < count; i++) {
				object = objects[i];
				if (object == null || object.isEmpty()) {
					continue;
				}
				
				values = object.getValues();
				if (values == null) {
					continue;
				}
				
				template = object.getTemplate();
				if (template == null) {
					continue;
				}
				
				table = DatabaseObject.convertClassToTable(object.getClass());
				if (table == null) {
					continue;
				}

				if (checkOrCreateTable(db, table, template) == false) {
					continue;
				}

				rowId = db.insert(table, object.handleNullProjection(), values);
				
				if (rowId <= 0) {
					continue;
				}

				successful++;
			}
			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		db.close();
		
		return successful;
	}

	@Override
	public int update(Query query, DatabaseObject object) {
		Logger.debug("query(%s), object(%s, emptry(%s))", 
				query, 
				object, object.isEmpty());
		if (query == null || object == null || object.isEmpty()) {
			return 0;
		}
		
		if (mDBOpenHelper == null) {
			return 0;
		}
		
		final SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
		if (db == null) {
			return 0;
		}
		
		
		final ContentValues values = object.getValues();
		if (values == null) {
			db.close();
			
			return 0;
		}
		
		final Template template = object.getTemplate();
		if (template == null) {
			db.close();
			
			return 0;
		}
		
		final String table = DatabaseObject.convertClassToTable(object.getClass());
		if (table == null) {
			db.close();
			
			return 0;
		}
		
		if (checkOrCreateTable(db, table, template) == false) {
			return 0;
		}
		
		String selection = null;
		
		QueryToken selToken = query.getSelection();
		if (selToken != null) {
			selection = selToken.toString();
		}
		
		int affected = -1;
		db.beginTransaction();
		try {
			affected = db.update(table, object.getValues(), selection, null);
			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		db.close();
		
		if (affected <= 0) {
			return 0;
		}
		
		return affected;
	}

	public int delete(Query query) {
		Logger.debug("query(%s)", query);
		if (query == null) {
			return 0;
		}
		
		if (mDBOpenHelper == null) {
			return 0;
		}
		
		final SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
		if (db == null) {
			return 0;
		}
		
		
		final String table = DatabaseObject.convertClassToTable(query.getObjectClass());
		if (table == null) {
			db.close();
			
			return 0;
		}
		
		String selection = null;
		
		QueryToken selToken = query.getSelection();
		if (selToken != null) {
			selection = selToken.toString();
		}
		
		int affected = -1;
		db.beginTransaction();
		try {
			affected = db.delete(table, selection, null);
			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		db.close();
		
		if (affected <= 0) {
			return 0;
		}
		
		return affected;
	}

	@Override
	public List<DatabaseObject> query(Query query,
			Class<? extends DatabaseObject> projectionClass) {
		if (query == null) {
			return null;
		}
		
		DatabaseObjectFactory factory = DatabaseObjectFactory.getInstance();
		if (factory == null) {
			return null;
		}
		
		if (mDBOpenHelper == null) {
			return null;
		}
		
		final SQLiteDatabase db = mDBOpenHelper.getReadableDatabase();
		if (db == null) {
			return null;
		}

		final Class<? extends DatabaseObject> objectClass = query.getObjectClass();
		if (objectClass == null) {
			db.close();
			
			return null;
		}
		
		String table = DatabaseObject.convertClassToTable(objectClass);
		if (table == null) {
			db.close();
			
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
//		Logger.debug("selection: (%s)", selection);
//		Logger.debug("groupBy: (%s)", groupBy);
//		Logger.debug("having: (%s)", having);
//		Logger.debug("orderBy: (%s)", orderBy);
//		Logger.debug("limit: (%s)", limit);
//		
//		Logger.debug("projection: (%s)", projectionToString(projection));
		
		Cursor c = null;
		try {
			c = db.query(table, projection, selection, null, groupBy, having, orderBy, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (c == null || c.getCount() <= 0) {
			if (c != null) {
				c.close();
			}
			
			db.close();
			
			return null;
		}
		
		List<DatabaseObject> objects = new ArrayList<DatabaseObject>();
		DatabaseObject object = null;
		
		db.beginTransaction();
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

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		c.close();
		
		db.close();
		
		return objects;
	}

	protected boolean checkOrCreateTable(SQLiteDatabase db, String tableName, Template templ) {
		if (db == null || db.isReadOnly() 
				|| tableName == null || templ == null) {
			return false;
		}
		
		if (mDBOpenHelper == null) {
			return false;
		}

		String creationSQL = templ.toSQLTableCreationString(tableName);
		if (creationSQL == null) {
			return false;
		}
		
		boolean success = false;
		try {
	        db.execSQL(creationSQL);
	        success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}

}
