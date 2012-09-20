package com.dailystudio.database;

import java.util.List;

import android.content.Context;
import android.net.Uri;

import com.dailystudio.GlobalContextWrapper;
import com.dailystudio.development.Logger;
import com.dailystudio.test.ActivityTestCase;
import com.dailystudio.test.R;

@Deprecated
public class DatabaseRemoteImplTest extends ActivityTestCase {
	
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
	
	public void testGetAuthority() {
		DatabaseRemoteConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
		Logger.debug("authority: %s", remoteConn.getAuthority());
		assertEquals("com.dailystudio.database", remoteConn.getAuthority());
	}
	
	public void testGetBaseUri() {
		DatabaseRemoteConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
		Logger.debug("uri: %s", remoteConn.getBaseUri());
		assertEquals(Uri.parse("content://com.dailystudio.database/test.db"), remoteConn.getBaseUri());
	}
	
	public void testInsertDatabaseObject() {
		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
		DatabaseObject object = DatabaseObjectFactory.createDatabaseObject(SampleObject2.class);
		assertNotNull(object);
		
		object.setDoubleValue(SampleObject2.COLUMN_LAT, 0.1);
		object.setDoubleValue(SampleObject2.COLUMN_LON, 0.2);
		object.setDoubleValue(SampleObject2.COLUMN_ALT, 0.3);
		
		long rowId = remoteConn.insert(object);
		assertEquals(0, rowId);
	}
	
	public void testInsertDatabaseObjects() {
		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
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
		
		assertEquals(0, remoteConn.insert(objects));
	}
	
	public void testUpdateDatabaseObjects() {
		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
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
		
		assertEquals(0, remoteConn.insert(objects));
		
		DatabaseObject updateObject = null;
		
		Query query = new Query(SampleObject1.class);
		assertNotNull(query);
		ExpressionToken selection1 = SampleObject1.COLUMN_TIME.gt(5000l);
		assertNotNull(selection1);
		
		query.setSelection(selection1);
		
		updateObject = 
			DatabaseObjectFactory.createDatabaseObject(SampleObject1.class);

		updateObject.setLongValue(SampleObject1.COLUMN_TIME, 0l);
		
		assertEquals(0, remoteConn.update(query, updateObject));
	}

	public void testDeleteDatabaseObjects() {
		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
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
		
		assertEquals(0, remoteConn.insert(objects));
		
		Query query1 = new Query(SampleObject1.class);
		assertNotNull(query1);
		ExpressionToken selection1 = SampleObject1.COLUMN_TIME.gt(5000l);
		assertNotNull(selection1);
		
		query1.setSelection(selection1);
		
		assertEquals(0, remoteConn.delete(query1));

		Query query2 = new Query(SampleObject2.class);
		assertNotNull(query2);
		ExpressionToken selection2 = SampleObject2.COLUMN_LAT.gt(0.2).and(SampleObject2.COLUMN_LON.lt(0.8));
		assertNotNull(selection2);
		
		query2.setSelection(selection2);
		
		assertEquals(0, remoteConn.delete(query2));
	}

	public void testDeleteAllDatabaseObjects() {
		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
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
		
		remoteConn.insert(objects);
		
		Query query = new Query(SampleObject1.class);
		assertNotNull(query);
		
		assertEquals(0, remoteConn.delete(query));
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
		
		DatabaseConnectivity localConn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(localConn);
		
		localConn.insert(objects);

		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
		final Template templ = object.mTemplate;
		
		Column col = templ.getColumn("intValue");
		
		Query query = new Query(QueryObject.class);
		assertNotNull(query);
		ExpressionToken selToken = col.gt(5).and(col.lt(9));
		query.setSelection(selToken);
		
		List<DatabaseObject> results = remoteConn.query(query, null);
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
		
		localConn.delete(new Query(QueryObject.class));
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
		
		DatabaseConnectivity localConn =new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(localConn);
		
		localConn.insert(objects);

		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		assertNotNull(remoteConn);
		
		final Template templ = object.mTemplate;
		
		Column col = templ.getColumn("intValue");
		
		Query query = new Query(QueryObject.class);
		assertNotNull(query);
		ExpressionToken selToken = col.gte(0).and(col.lt(2));
		query.setSelection(selToken);
		OrderingToken groupByToken = col.groupBy();
		query.setGroupBy(groupByToken);
		
		List<DatabaseObject> results = remoteConn.query(query);
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
		
		localConn.delete(new Query(QueryObject.class));
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
		
		DatabaseConnectivity localConn =new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(localConn);
		
		localConn.insert(objects);

		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		
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
		
		results = remoteConn.query(query);
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
		
		results = localConn.query(query);
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

		localConn.delete(new Query(QueryObject.class));
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
		
		DatabaseConnectivity localConn =new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(localConn);
		
		localConn.insert(objects);

		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		
		Query query = new Query(QueryObject.class);
		assertNotNull(query);
		ExpressionToken limit = new ExpressionToken("5");
		query.setLimit(limit);
		
		List<DatabaseObject> results = remoteConn.query(query);
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
		
		localConn.delete(new Query(QueryObject.class));
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
		
		DatabaseConnectivity localConn = new DatabaseLocalConnectivity(mTargetContext, "test.db");
		assertNotNull(localConn);
		
		localConn.insert(objects);

		DatabaseConnectivity remoteConn = 
			new DatabaseRemoteConnectivity(mTargetContext, 
				"com.dailystudio.database", "test.db");
		
		final Template templ = object.mTemplate;
		
		Column col = templ.getColumn("intValue");
		
		Query query = new Query(QueryObject.class);
		assertNotNull(query);
		ExpressionToken selToken = col.gt(5).and(col.lt(9));
		query.setSelection(selToken);
		
		List<DatabaseObject> results = remoteConn.query(query, ProjectionObject.class);
		assertNotNull(results);
		assertEquals(1, results.size());
		
		DatabaseObject resultObject = results.get(0);

		Integer iCount = resultObject.getIntegerValue("count( _id )");
		assertNotNull(iCount);
		assertEquals(3, iCount.intValue());
		
		localConn.delete(new Query(QueryObject.class));
	}

}
