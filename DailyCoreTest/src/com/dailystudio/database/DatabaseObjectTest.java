package com.dailystudio.database;

import android.content.Intent;

import com.dailystudio.GlobalContextWrapper;
import com.dailystudio.database.DatabaseObject;
import com.dailystudio.test.ActivityTestCase;

@Deprecated
public class DatabaseObjectTest extends ActivityTestCase {

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

	public void testConvertClassToTable() {
		assertEquals("com_dailystudio_database_TestDatabaseObject", DatabaseObject.convertClassToTable(TestDatabaseObject.class));
		assertEquals("com_dailystudio_database_SampleObject1", DatabaseObject.convertClassToTable(SampleObject1.class));
		assertEquals("com_dailystudio_database_SampleObject2", DatabaseObject.convertClassToTable(SampleObject2.class));
	}
	
	public void testSetAndGetIntegerValue() {
		DatabaseObject object = null;

		object = DatabaseObjectFactory.createDatabaseObject();
		assertNotNull(object);
		assertEquals(true, object.isEmpty());
		
		object = new TestDatabaseObject(mContext);
		assertNotNull(object);

		object.setIntegerValue((Column)null, 1111);
		assertEquals(true, object.isEmpty());
		
		object.setIntegerValue((String)null, 2222);
		assertEquals(true, object.isEmpty());

		object.setIntegerValue(new Column("col_error", Column.COL_TYPE_INTEGER), 3333);
		assertEquals(true, object.isEmpty());

		object.setIntegerValue("unknown", 4444);
		assertEquals(true, object.isEmpty());

		object.setIntegerValue(TestDatabaseObject.COLUMN_INTEGER_VAL.columnName, 0x123456);
		assertEquals(0x123456, object.getIntegerValue(TestDatabaseObject.COLUMN_INTEGER_VAL.columnName));

		object.setIntegerValue(TestDatabaseObject.COLUMN_INTEGER_VAL, 0xfffff);
		assertEquals(0xfffff, object.getIntegerValue(TestDatabaseObject.COLUMN_INTEGER_VAL));
		
		assertEquals(0, object.getIntegerValue(TestDatabaseObject.COLUMN_LONG_VAL));
		assertEquals(0, object.getIntegerValue(TestDatabaseObject.COLUMN_DOUBLE_VAL));
		assertEquals(0, object.getIntegerValue(TestDatabaseObject.COLUMN_TEXT_VAL));
		
		assertEquals(0, object.getIntegerValue(new IntegerColumn("dummy")));
	}
	
	public void testSetAndGetLongValue() {
		DatabaseObject object = null;

		object = DatabaseObjectFactory.createDatabaseObject();
		assertNotNull(object);
		assertEquals(true, object.isEmpty());
		
		object = new TestDatabaseObject(mContext);
		assertNotNull(object);
		
		object.setLongValue((Column)null, 1l);
		assertEquals(true, object.isEmpty());
		
		object.setLongValue((String)null, 2l);
		assertEquals(true, object.isEmpty());

		object.setLongValue(new Column("col_error", Column.COL_TYPE_LONG), 3l);
		assertEquals(true, object.isEmpty());

		object.setLongValue("unknown", 4l);
		assertEquals(true, object.isEmpty());

		object.setLongValue(TestDatabaseObject.COLUMN_LONG_VAL.columnName, 123456789l);
		assertEquals(123456789l, object.getLongValue(TestDatabaseObject.COLUMN_LONG_VAL.columnName));

		object.setLongValue(TestDatabaseObject.COLUMN_LONG_VAL, 123454321l);
		assertEquals(123454321l, object.getLongValue(TestDatabaseObject.COLUMN_LONG_VAL));
		
		assertEquals(0l, object.getLongValue(TestDatabaseObject.COLUMN_INTEGER_VAL));
		assertEquals(0l, object.getLongValue(TestDatabaseObject.COLUMN_DOUBLE_VAL));
		assertEquals(0l, object.getLongValue(TestDatabaseObject.COLUMN_TEXT_VAL));
		
		assertEquals(0l, object.getLongValue(new LongColumn("dummy")));
	}
	
	public void testSetAndGetDoubleValue() {
		DatabaseObject object = null;

		object = DatabaseObjectFactory.createDatabaseObject();
		assertNotNull(object);
		assertEquals(true, object.isEmpty());
		
		object = new TestDatabaseObject(mContext);
		assertNotNull(object);
		
		object.setDoubleValue((Column)null, 1.1);
		assertEquals(true, object.isEmpty());
		
		object.setDoubleValue((String)null, 2.2);
		assertEquals(true, object.isEmpty());

		object.setDoubleValue(new Column("col_error", Column.COL_TYPE_DOUBLE), 3.3);
		assertEquals(true, object.isEmpty());

		object.setDoubleValue("unknown", 4.4);
		assertEquals(true, object.isEmpty());

		object.setDoubleValue(TestDatabaseObject.COLUMN_DOUBLE_VAL.columnName, 1.414);
		assertEquals(1.414, object.getDoubleValue(TestDatabaseObject.COLUMN_DOUBLE_VAL.columnName));
		
		object.setDoubleValue(TestDatabaseObject.COLUMN_DOUBLE_VAL, 1.732);
		assertEquals(1.732, object.getDoubleValue(TestDatabaseObject.COLUMN_DOUBLE_VAL));

		assertEquals(.0, object.getDoubleValue(TestDatabaseObject.COLUMN_INTEGER_VAL));
		assertEquals(.0, object.getDoubleValue(TestDatabaseObject.COLUMN_LONG_VAL));
		assertEquals(.0, object.getDoubleValue(TestDatabaseObject.COLUMN_TEXT_VAL));
		
		assertEquals(.0, object.getDoubleValue(new DoubleColumn("dummy")));
	}
	
	public void testSetAndGetStringValue() {
		DatabaseObject object = null;

		object = DatabaseObjectFactory.createDatabaseObject();
		assertNotNull(object);
		assertEquals(true, object.isEmpty());
		
		object = new TestDatabaseObject(mContext);
		assertNotNull(object);
		
		object.setTextValue((Column)null, "text1");
		assertEquals(true, object.isEmpty());
		
		object.setTextValue((String)null, "text2");
		assertEquals(true, object.isEmpty());

		object.setTextValue(new Column("col_error", Column.COL_TYPE_TEXT), "text3");
		assertEquals(true, object.isEmpty());

		object.setTextValue("unknown", "text4");
		assertEquals(true, object.isEmpty());
		
		object.setTextValue(TestDatabaseObject.COLUMN_TEXT_VAL.columnName, "test");
		assertEquals("test", object.getTextValue(TestDatabaseObject.COLUMN_TEXT_VAL.columnName));

		object.setTextValue(TestDatabaseObject.COLUMN_TEXT_VAL, "abcd");
		assertEquals("abcd", object.getTextValue(TestDatabaseObject.COLUMN_TEXT_VAL));
		
		assertNull(object.getTextValue(TestDatabaseObject.COLUMN_INTEGER_VAL));
		assertNull(object.getTextValue(TestDatabaseObject.COLUMN_LONG_VAL));
		assertNull(object.getTextValue(TestDatabaseObject.COLUMN_DOUBLE_VAL));
		
		assertNull(object.getTextValue(new TextColumn("dummy")));
	}
	
	public void testIsEmpty() {
		DatabaseObject object = null;

		object = DatabaseObjectFactory.createDatabaseObject();
		assertNotNull(object);
		assertEquals(true, object.isEmpty());
		
		object = new TestDatabaseObject(mContext);
		assertNotNull(object);
		
		object.setTextValue(TestDatabaseObject.COLUMN_TEXT_VAL, "test");
		assertEquals(false, object.isEmpty());
	}
	
	public void testToSQLSelectionString() {
		DatabaseObject object = new TestDatabaseObject(mContext);
		assertNotNull(object);
		
		object.setIntegerValue("intVal", 888);
		object.setLongValue("longVal", 123456789l);
		object.setDoubleValue("doubleVal", 3.1415926);
		object.setTextValue("textVal", "This is a text value");
		
		assertEquals("intVal = \'888\' AND longVal = \'123456789\' AND doubleVal = \'3.1415926\' AND textVal = \'This is a text value\'", 
				object.toSQLSelectionString());
	}
	
	public void testConvertToIntent() {
		DatabaseObject object = new TestDatabaseObject(mContext);
		assertNotNull(object);
		
		object.setIntegerValue("intVal", 888);
		object.setLongValue("longVal", 123456789l);
		object.setDoubleValue("doubleVal", 3.1415926);
		object.setTextValue("textVal", "This is a text value");
		
		Intent actual = object.convertToIntent();
		assertNotNull(actual);
		
		assertEquals(888, actual.getIntExtra("intVal", 0));
		assertEquals(123456789l, actual.getLongExtra("longVal", 0l));
		assertEquals(3.1415926, actual.getDoubleExtra("doubleVal", .0));
		assertEquals("This is a text value", actual.getStringExtra("textVal"));
	}
	
}
