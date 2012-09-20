package com.dailystudio.database;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dailystudio.GlobalContextWrapper;
import com.dailystudio.development.Logger;
import com.dailystudio.test.ActivityTestCase;
import com.dailystudio.test.R;

@Deprecated
public class DatabaseLocalConnectionTest extends ActivityTestCase {
	
	private static class QueryObject extends DatabaseObject {

		public QueryObject(Context context) {
			super(context);
			
			initMembers();
		}
		
		private void initMembers() {
			Template template = inflate(R.xml.templ_query_object);
			
			if (template != null) {
				setTemplate(template);
			}
		}

	}
	
	private static class ProjectionObject extends DatabaseObject {
		
		static Column COLUMN_ID_COUNT = new IntegerColumn("count( _id )"); 

		public ProjectionObject(Context context) {
			super(context);
			
			initMembers();
		}
		
		private void initMembers() {
			final Template templ = getTemplate();
			
			templ.addColumn(COLUMN_ID_COUNT);
		}
		
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		GlobalContextWrapper.bindContext(mContext);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		GlobalContextWrapper.unbindContext(mContext);
	}
	
	public void testInsertDatabaseObject() {
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		DatabaseObject object = DatabaseObjectFactory.createDatabaseObject(SampleObject2.class);
		assertNotNull(object);
		
		object.setDoubleValue(SampleObject2.COLUMN_LAT, 0.1);
		object.setDoubleValue(SampleObject2.COLUMN_LON, 0.2);
		object.setDoubleValue(SampleObject2.COLUMN_ALT, 0.3);
		
		long rowId = conn.insert(object);
		assertEquals(true, (rowId > 0));
		
		ISDatabaseOpenHelper dbhelper = 
			new ISDatabaseOpenHelper(mTargetContext, "test.db", 0x1);
		assertNotNull(dbhelper);
		
		SQLiteDatabase sqlDB = dbhelper.getReadableDatabase();
		assertNotNull(sqlDB);
		Cursor c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject2.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(1, c.getCount());
		
		assertEquals(true, c.moveToFirst());
		assertEquals(0.1, c.getDouble(c.getColumnIndex(SampleObject2.COLUMN_LAT.columnName)));
		assertEquals(0.2, c.getDouble(c.getColumnIndex(SampleObject2.COLUMN_LON.columnName)));
		assertEquals(0.3, c.getDouble(c.getColumnIndex(SampleObject2.COLUMN_ALT.columnName)));

		c.close();
		
		sqlDB.close();

		conn.delete(new Query(SampleObject2.class));
	}
	
	public void testInsertDatabaseObjects() {
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		final int count = 10;
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				object = DatabaseObjectFactory.createDatabaseObject(SampleObject1.class);
				assertNotNull(object);
				
				object.setLongValue(SampleObject1.COLUMN_TIME, (1000l * i));
			} else {
				object = DatabaseObjectFactory.createDatabaseObject(SampleObject2.class);
				assertNotNull(object);
				
				object.setDoubleValue(SampleObject2.COLUMN_LAT, 0.1 * i);
				object.setDoubleValue(SampleObject2.COLUMN_LON, 0.2 * i);
				object.setDoubleValue(SampleObject2.COLUMN_ALT, 0.3 * i);
			}
			
			objects[i] = object;
		}
		
		conn.insert(objects);
		
		ISDatabaseOpenHelper dbhelper = 
			new ISDatabaseOpenHelper(mTargetContext, "test.db", 0x1);
		assertNotNull(dbhelper);
		
		SQLiteDatabase sqlDB = dbhelper.getReadableDatabase();
		assertNotNull(sqlDB);
		Cursor c = null;
		
		c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject1.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(5, c.getCount());
		assertEquals(true, c.moveToFirst());
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				assertEquals(i * 1000l, c.getLong(c.getColumnIndex("time")));
				c.moveToNext();
			}
		}
		c.close();

		c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject2.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(5, c.getCount());
		assertEquals(true, c.moveToFirst());
		for (int i = 0; i < 5; i++) {
			if (i % 2 != 0) {
				assertEquals(0.1 * i, c.getDouble(c.getColumnIndex("latitude")));
				assertEquals(0.2 * i, c.getDouble(c.getColumnIndex("longitude")));
				assertEquals(0.3 * i, c.getDouble(c.getColumnIndex("altitude")));
				c.moveToNext();
			}
		}
		c.close();
		
		sqlDB.close();

		conn.delete(new Query(SampleObject1.class));
		conn.delete(new Query(SampleObject2.class));
	}
	
	public void testUpdateDatabaseObjects() {
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		final int count = 10;
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				object = DatabaseObjectFactory.createDatabaseObject(SampleObject1.class);
				assertNotNull(object);
				
				object.setLongValue(SampleObject1.COLUMN_TIME, (1000l * i));
			} else {
				object = DatabaseObjectFactory.createDatabaseObject(SampleObject2.class);
				assertNotNull(object);
				
				object.setDoubleValue(SampleObject2.COLUMN_LAT, 0.1 * i);
				object.setDoubleValue(SampleObject2.COLUMN_LON, 0.2 * i);
				object.setDoubleValue(SampleObject2.COLUMN_ALT, 0.3 * i);
			}
			
			objects[i] = object;
		}
		
		conn.insert(objects);
		
		DatabaseObject updateObject = null;
		
		Query query1 = new Query(SampleObject1.class);
		assertNotNull(query1);
		ExpressionToken selection1 = SampleObject1.COLUMN_TIME.gt(5000l);
		assertNotNull(selection1);
		
		query1.setSelection(selection1);
		
		updateObject = 
			DatabaseObjectFactory.createDatabaseObject(SampleObject1.class);

		updateObject.setLongValue(SampleObject1.COLUMN_TIME, 0l);
		
		conn.update(query1, updateObject);

		Query query2 = new Query(SampleObject2.class);
		assertNotNull(query2);
		ExpressionToken selection2 = SampleObject2.COLUMN_LAT.gt(0.2).and(SampleObject2.COLUMN_LON.lt(0.8));
		assertNotNull(selection2);
		
		query2.setSelection(selection2);
		
		updateObject = 
			DatabaseObjectFactory.createDatabaseObject(SampleObject2.class);

		updateObject.setDoubleValue(SampleObject2.COLUMN_ALT, 12.34);
		
		conn.update(query2, updateObject);
		
		ISDatabaseOpenHelper dbhelper = 
			new ISDatabaseOpenHelper(mTargetContext, "test.db", 0x1);
		assertNotNull(dbhelper);
		
		SQLiteDatabase sqlDB = dbhelper.getReadableDatabase();
		assertNotNull(sqlDB);
		Cursor c = null;
		
		c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject1.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(5, c.getCount());
		assertEquals(true, c.moveToFirst());
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				if (i * 1000l > 5000l) {
					assertEquals(0, c.getLong(c.getColumnIndex("time")));
				} else {
					assertEquals(i * 1000l, c.getLong(c.getColumnIndex("time")));
				}
				c.moveToNext();
			}
		}
		c.close();

		c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject2.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(5, c.getCount());
		assertEquals(true, c.moveToFirst());
		for (int i = 0; i < 5; i++) {
			if (i % 2 != 0) {
				if ((0.1 * i > 0.2) && (0.2 * i < 0.8)) {
					assertEquals(0.1 * i, c.getDouble(c.getColumnIndex("latitude")));
					assertEquals(0.2 * i, c.getDouble(c.getColumnIndex("longitude")));
					assertEquals(12.34, c.getDouble(c.getColumnIndex("altitude")));
				} else {
					assertEquals(0.1 * i, c.getDouble(c.getColumnIndex("latitude")));
					assertEquals(0.2 * i, c.getDouble(c.getColumnIndex("longitude")));
					assertEquals(0.3 * i, c.getDouble(c.getColumnIndex("altitude")));
				}
				c.moveToNext();
			}
		}
		c.close();

		sqlDB.close();

		conn.delete(new Query(SampleObject1.class));
		conn.delete(new Query(SampleObject2.class));
	}

	public void testDeleteDatabaseObjects() {
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		final int count = 10;
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				object = DatabaseObjectFactory.createDatabaseObject(SampleObject1.class);
				assertNotNull(object);
				
				object.setLongValue(SampleObject1.COLUMN_TIME, (1000l * i));
			} else {
				object = DatabaseObjectFactory.createDatabaseObject(SampleObject2.class);
				assertNotNull(object);
				
				object.setDoubleValue(SampleObject2.COLUMN_LAT, 0.1 * i);
				object.setDoubleValue(SampleObject2.COLUMN_LON, 0.2 * i);
				object.setDoubleValue(SampleObject2.COLUMN_ALT, 0.3 * i);
			}
			
			objects[i] = object;
		}
		
		conn.insert(objects);
		
		Query query1 = new Query(SampleObject1.class);
		assertNotNull(query1);
		ExpressionToken selection1 = SampleObject1.COLUMN_TIME.gt(5000l);
		assertNotNull(selection1);
		
		query1.setSelection(selection1);
		
		conn.delete(query1);

		Query qParams2 = new Query(SampleObject2.class);
		assertNotNull(qParams2);
		ExpressionToken selection2 = SampleObject2.COLUMN_LAT.gt(0.2).and(SampleObject2.COLUMN_LON.lt(0.8));
		assertNotNull(selection2);
		
		qParams2.setSelection(selection2);
		
		conn.delete(qParams2);
		
		ISDatabaseOpenHelper dbhelper = 
			new ISDatabaseOpenHelper(mTargetContext, "test.db", 0x1);
		assertNotNull(dbhelper);
		
		SQLiteDatabase sqlDB = dbhelper.getReadableDatabase();
		assertNotNull(sqlDB);
		Cursor c = null;
		
		c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject1.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(3, c.getCount());
		assertEquals(true, c.moveToFirst());
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				if (i * 1000l > 5000l) {
					continue;
				} else {
					assertEquals(i * 1000l, c.getLong(c.getColumnIndex("time")));
				}
				c.moveToNext();
			}
		}
		c.close();

		c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject2.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(4, c.getCount());
		assertEquals(true, c.moveToFirst());
		for (int i = 0; i < 5; i++) {
			if (i % 2 != 0) {
				if ((0.1 * i > 0.2) && (0.2 * i < 0.8)) {
					continue;
				} else {
					assertEquals(0.1 * i, c.getDouble(c.getColumnIndex("latitude")));
					assertEquals(0.2 * i, c.getDouble(c.getColumnIndex("longitude")));
					assertEquals(0.3 * i, c.getDouble(c.getColumnIndex("altitude")));
				}
				c.moveToNext();
			}
		}
		c.close();
		
		sqlDB.close();

		conn.delete(new Query(SampleObject1.class));
		conn.delete(new Query(SampleObject2.class));
	}

	public void testDeleteAllDatabaseObjects() {
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		final int count = 10;
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 10; i++) {
			object = DatabaseObjectFactory.createDatabaseObject(SampleObject1.class);
			assertNotNull(object);
				
			object.setLongValue(SampleObject1.COLUMN_TIME, (1000l * i));
			objects[i] = object;
		}
		
		conn.insert(objects);
		
		Query query = new Query(SampleObject1.class);
		assertNotNull(query);
		
		conn.delete(query);

		ISDatabaseOpenHelper dbhelper = 
			new ISDatabaseOpenHelper(mTargetContext, "test.db", 0x1);
		assertNotNull(dbhelper);

		SQLiteDatabase sqlDB = dbhelper.getReadableDatabase();
		assertNotNull(sqlDB);
		Cursor c = null;
		
		c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject1.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(0, c.getCount());
		c.close();

		sqlDB.close();

		conn.delete(new Query(SampleObject1.class));
	}

	public void testFillValuesFromCursor() {
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);

		DatabaseObject object = DatabaseObjectFactory.createDatabaseObject(SampleObject2.class);
		assertNotNull(object);
		
		object.setDoubleValue(SampleObject2.COLUMN_LAT, 0.1);
		object.setDoubleValue(SampleObject2.COLUMN_LON, 0.2);
		object.setDoubleValue(SampleObject2.COLUMN_ALT, 0.3);
		
		conn.insert(object);
		
		ISDatabaseOpenHelper dbhelper = 
			new ISDatabaseOpenHelper(mTargetContext, "test.db", 0x1);
		assertNotNull(dbhelper);
		
		SQLiteDatabase sqlDB = dbhelper.getReadableDatabase();
		assertNotNull(sqlDB);
		Cursor c = sqlDB.query(DatabaseObject.convertClassToTable(SampleObject2.class), null, null, null, null, null, null);
		assertNotNull(c);
		assertEquals(1, c.getCount());
		
		assertEquals(true, c.moveToFirst());
		DatabaseObject resultObject = DatabaseObjectFactory.createDatabaseObject(SampleObject2.class);
		assertNotNull(resultObject);
		
		resultObject.fillValuesFromCursor(c);
		assertEquals(0.1, resultObject.getDoubleValue(SampleObject2.COLUMN_LAT));
		assertEquals(0.2, resultObject.getDoubleValue(SampleObject2.COLUMN_LON));
		assertEquals(0.3, resultObject.getDoubleValue(SampleObject2.COLUMN_ALT));

		c.close();
		
		sqlDB.close();

		conn.delete(new Query(SampleObject2.class));
	}

	public void testSimpleQuery() {
		final int count = 10;
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 10; i++) {
			object = DatabaseObjectFactory.createDatabaseObject(QueryObject.class);
			assertNotNull(object);
			
			object.setIntegerValue("intValue", i);
			object.setDoubleValue("doubleValue", ((double) i * 2));
			object.setTextValue("textValue", String.format("%04d", i * 3));
			
			objects[i] = object;
		}
		
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		conn.insert(objects);

		final Template templ = object.mTemplate;
		
		Column col = templ.getColumn("intValue");
		
		Query query = new Query(QueryObject.class);
		assertNotNull(query);
		ExpressionToken selToken = col.gt(5).and(col.lt(9));
		query.setSelection(selToken);
		
		List<DatabaseObject> results = conn.query(query);
		assertNotNull(results);
		assertEquals(3, results.size());
		
		DatabaseObject resultObject = null;
		for (int i = 6; i <= 8; i++) {
			resultObject = results.get(i - 6);
			assertNotNull(resultObject);
			
			Logger.debug("resultObject(%s)", resultObject.toSQLSelectionString());
			Integer iVal = resultObject.getIntegerValue("intValue");
			assertNotNull(iVal);
			assertEquals(i, iVal.intValue());

			Double dVal = resultObject.getDoubleValue("doubleValue");
			assertNotNull(dVal);
			assertEquals(((double) i * 2), dVal.doubleValue());

			String sVal = resultObject.getTextValue("textValue");
			assertNotNull(sVal);
			assertEquals(String.format("%04d", i * 3), sVal);
		}
		
		conn.delete(new Query(QueryObject.class));
	}

	public void testQueryWithGroupBy() {
		final int count = (3 * 10);
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 10; j++) {
				object = DatabaseObjectFactory.createDatabaseObject(QueryObject.class);
				assertNotNull(object);
				
				object.setIntegerValue("intValue", i);
				object.setDoubleValue("doubleValue", ((double) i * 2));
				object.setTextValue("textValue", String.format("%04d", i * 3));
				
				objects[i * 10 + j] = object;
			}
		}
		
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		conn.insert(objects);

		final Template templ = object.mTemplate;
		
		Column col = templ.getColumn("intValue");
		
		Query query = new Query(QueryObject.class);
		assertNotNull(query);
		ExpressionToken selToken = col.gte(0).and(col.lt(2));
		query.setSelection(selToken);
		OrderingToken groupByToken = col.groupBy();
		query.setGroupBy(groupByToken);
		
		List<DatabaseObject> results = conn.query(query);
		assertNotNull(results);
		assertEquals(2, results.size());
		
		DatabaseObject resultObject = null;
		for (int i = 0; i < 2; i++) {
			resultObject = results.get(i);
			assertNotNull(resultObject);
				
			Logger.debug("resultObject(%s)", resultObject.toSQLSelectionString());
			Integer iVal = resultObject.getIntegerValue("intValue");
			assertNotNull(iVal);
			assertEquals(i, iVal.intValue());
	
			Double dVal = resultObject.getDoubleValue("doubleValue");
			assertNotNull(dVal);
			assertEquals(((double) i * 2), dVal.doubleValue());
	
			String sVal = resultObject.getTextValue("textValue");
			assertNotNull(sVal);
			assertEquals(String.format("%04d", i * 3), sVal);
		}
		
		conn.delete(new Query(QueryObject.class));
	}

	public void testQueryWithOrderBy() {
		final int count = (3 * 10);
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 10; j++) {
				object = DatabaseObjectFactory.createDatabaseObject(QueryObject.class);
				assertNotNull(object);
				
				object.setIntegerValue("intValue", j);
				object.setDoubleValue("doubleValue", ((double) j * 2));
				object.setTextValue("textValue", String.format("%04d", j * 3));
				
				objects[i * 10 + j] = object;
			}
		}
		
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		conn.insert(objects);

		final Template templ = object.mTemplate;
		
		Column col = templ.getColumn("intValue");
		
		Query query = null;
		List<DatabaseObject> results = null;
		OrderingToken orderByToken = null;
		DatabaseObject resultObject = null;
		
		query = new Query(QueryObject.class);
		assertNotNull(query);
		orderByToken = col.orderByAscending();
		query.setOrderBy(orderByToken);
		
		results = conn.query(query);
		assertNotNull(results);
		assertEquals(30, results.size());
		
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 3; i++) {
				resultObject = results.get(j * 3 + i);
				assertNotNull(resultObject);
					
				Logger.debug("resultObject(%s)", resultObject.toSQLSelectionString());
				Integer iVal = resultObject.getIntegerValue("intValue");
				assertNotNull(iVal);
				assertEquals(j, iVal.intValue());
		
				Double dVal = resultObject.getDoubleValue("doubleValue");
				assertNotNull(dVal);
				assertEquals(((double) j * 2), dVal.doubleValue());
		
				String sVal = resultObject.getTextValue("textValue");
				assertNotNull(sVal);
				assertEquals(String.format("%04d", j * 3), sVal);
			}
		}
		
		query = new Query(QueryObject.class);
		assertNotNull(query);
		orderByToken = col.orderByDescending();
		query.setOrderBy(orderByToken);
		
		results = conn.query(query);
		assertNotNull(results);
		assertEquals(30, results.size());
		
		for (int j = 9; j >= 0; j--) {
			for (int i = 0; i < 3; i++) {
				resultObject = results.get((9 - j) * 3 + i);
				assertNotNull(resultObject);
					
				Logger.debug("resultObject(%s)", resultObject.toSQLSelectionString());
				Integer iVal = resultObject.getIntegerValue("intValue");
				assertNotNull(iVal);
				assertEquals(j, iVal.intValue());
		
				Double dVal = resultObject.getDoubleValue("doubleValue");
				assertNotNull(dVal);
				assertEquals(((double) j * 2), dVal.doubleValue());
		
				String sVal = resultObject.getTextValue("textValue");
				assertNotNull(sVal);
				assertEquals(String.format("%04d", j * 3), sVal);
			}
		}

		conn.delete(new Query(QueryObject.class));
	}

	public void testLimitQuery() {
		final int count = 10;
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 10; i++) {
			object = DatabaseObjectFactory.createDatabaseObject(QueryObject.class);
			assertNotNull(object);
			
			object.setIntegerValue("intValue", i);
			object.setDoubleValue("doubleValue", ((double) i * 2));
			object.setTextValue("textValue", String.format("%04d", i * 3));
			
			objects[i] = object;
		}
		
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		conn.insert(objects);

		Query query = new Query(QueryObject.class);
		assertNotNull(query);
		ExpressionToken limit = new ExpressionToken("5");
		query.setLimit(limit);
		
		List<DatabaseObject> results = conn.query(query);
		assertNotNull(results);
		assertEquals(5, results.size());
		
		DatabaseObject resultObject = null;
		for (int i = 0; i < 5; i++) {
			resultObject = results.get(i);
			assertNotNull(resultObject);
			
			Logger.debug("resultObject(%s)", resultObject.toSQLSelectionString());
			Integer iVal = resultObject.getIntegerValue("intValue");
			assertNotNull(iVal);
			assertEquals(i, iVal.intValue());

			Double dVal = resultObject.getDoubleValue("doubleValue");
			assertNotNull(dVal);
			assertEquals(((double) i * 2), dVal.doubleValue());

			String sVal = resultObject.getTextValue("textValue");
			assertNotNull(sVal);
			assertEquals(String.format("%04d", i * 3), sVal);
		}
		
		conn.delete(new Query(QueryObject.class));
	}

	public void testQueryWithProjectionClass() {
		final int count = 10;
		DatabaseObject object = null;
		DatabaseObject[] objects = new DatabaseObject[count];
		assertNotNull(objects);
		
		for (int i = 0; i < 10; i++) {
			object = DatabaseObjectFactory.createDatabaseObject(QueryObject.class);
			assertNotNull(object);
			
			object.setIntegerValue("intValue", i);
			object.setDoubleValue("doubleValue", ((double) i * 2));
			object.setTextValue("textValue", String.format("%04d", i * 3));
			
			objects[i] = object;
		}
		
		DatabaseConnectivity conn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(conn);
		
		conn.insert(objects);

		final Template templ = object.mTemplate;
		
		Column col = templ.getColumn("intValue");
		
		Query query = new Query(QueryObject.class);
		assertNotNull(query);
		ExpressionToken selToken = col.gt(5).and(col.lt(9));
		query.setSelection(selToken);
		
		List<DatabaseObject> results = conn.query(query, ProjectionObject.class);
		assertNotNull(results);
		assertEquals(1, results.size());
		
		DatabaseObject resultObject = results.get(0);

		Integer iCount = resultObject.getIntegerValue("count( _id )");
		assertNotNull(iCount);
		assertEquals(3, iCount.intValue());
		
		conn.delete(new Query(QueryObject.class));
	}

}
