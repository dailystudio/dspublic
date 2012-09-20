package com.dailystudio.database;

import android.test.AndroidTestCase;

import com.dailystudio.database.Column;
import com.dailystudio.database.QueryToken;

@Deprecated
public class ColumnTest extends AndroidTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateAColumn() {
		Column column = null;
		
		column = new Column("_id", Column.COL_TYPE_INTEGER);
		assertNotNull(column);
		assertEquals("_id", column.columnName);
		assertEquals(Column.COL_TYPE_INTEGER, column.columnType);
		assertEquals(true, column.allowNull);
		assertEquals(false, column.isPrimary);

	
		column = new Column("_id", Column.COL_TYPE_INTEGER, false);
		assertNotNull(column);
		assertEquals("_id", column.columnName);
		assertEquals(Column.COL_TYPE_INTEGER, column.columnType);
		assertEquals(false, column.allowNull);
		assertEquals(false, column.isPrimary);
		
		column = new Column("_id", Column.COL_TYPE_INTEGER, false, true);
		assertNotNull(column);
		assertEquals("_id", column.columnName);
		assertEquals(Column.COL_TYPE_INTEGER, column.columnType);
		assertEquals(false, column.allowNull);
		assertEquals(true, column.isPrimary);
	}

	public void testColumnToString() {
		Column column = null;
		
		column = new Column("_id", Column.COL_TYPE_INTEGER);
		assertNotNull(column);
		assertEquals("_id", column.columnName);
		assertEquals(Column.COL_TYPE_INTEGER, column.columnType);
		assertEquals(true, column.allowNull);
		assertEquals(false, column.isPrimary);
		assertEquals("_id INTEGER", column.toString());
	
		column = new Column("_id", Column.COL_TYPE_INTEGER, false);
		assertNotNull(column);
		assertEquals("_id", column.columnName);
		assertEquals(Column.COL_TYPE_INTEGER, column.columnType);
		assertEquals(false, column.allowNull);
		assertEquals(false, column.isPrimary);
		assertEquals("_id INTEGER NOT NULL", column.toString());
		
		column = new Column("_id", Column.COL_TYPE_INTEGER, false, true);
		assertNotNull(column);
		assertEquals("_id", column.columnName);
		assertEquals(Column.COL_TYPE_INTEGER, column.columnType);
		assertEquals(false, column.allowNull);
		assertEquals(true, column.isPrimary);
		assertEquals("_id INTEGER NOT NULL PRIMARY KEY", column.toString());
	}
	
	public void testGTOperator() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		assertEquals(new QueryToken("intVal > \'1000\'"), column.gt(1000));

		column = new LongColumn("longVal");
		assertNotNull(column);
		assertEquals(new QueryToken("longVal > \'987654321012345678\'"), column.gt(987654321012345678l));

		column = new DoubleColumn("doubleVal");
		assertNotNull(column);
		assertEquals(new QueryToken("doubleVal > \'3.141592653\'"), column.gt(3.141592653));

		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken("textVal > \'this is \"string\" gt. \'123\'?\'"), column.gt("this is \"string\" gt. \'123\'?"));
		
		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken(), column.gt(1000));
	}
	
	public void testGTEOperator() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		assertEquals(new QueryToken("intVal >= \'1000\'"), column.gte(1000));

		column = new LongColumn("longVal");
		assertNotNull(column);
		assertEquals(new QueryToken("longVal >= \'987654321012345678\'"), column.gte(987654321012345678l));

		column = new DoubleColumn("doubleVal");
		assertNotNull(column);
		assertEquals(new QueryToken("doubleVal >= \'3.141592653\'"), column.gte(3.141592653));

		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken("textVal >= \'this is \"string\" gte. \'123\'?\'"), column.gte("this is \"string\" gte. \'123\'?"));
		
		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken(), column.gte(1000));
	}
	
	public void testLTOperator() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		assertEquals(new QueryToken("intVal < \'1000\'"), column.lt(1000));

		column = new LongColumn("longVal");
		assertNotNull(column);
		assertEquals(new QueryToken("longVal < \'987654321012345678\'"), column.lt(987654321012345678l));

		column = new DoubleColumn("doubleVal");
		assertNotNull(column);
		assertEquals(new QueryToken("doubleVal < \'3.141592653\'"), column.lt(3.141592653));

		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken("textVal < \'this is \"string\" lt. \'123\'?\'"), column.lt("this is \"string\" lt. \'123\'?"));
		
		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken(), column.lt(1000));
	}
	
	public void testLTEOperator() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		assertEquals(new QueryToken("intVal <= \'1000\'"), column.lte(1000));

		column = new LongColumn("longVal");
		assertNotNull(column);
		assertEquals(new QueryToken("longVal <= \'987654321012345678\'"), column.lte(987654321012345678l));

		column = new DoubleColumn("doubleVal");
		assertNotNull(column);
		assertEquals(new QueryToken("doubleVal <= \'3.141592653\'"), column.lte(3.141592653));

		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken("textVal <= \'this is \"string\" lte. \'123\'?\'"), column.lte("this is \"string\" lte. \'123\'?"));
		
		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken(), column.lte(1000));
	}
	
	public void testEQOperator() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		assertEquals(new QueryToken("intVal == \'1000\'"), column.eq(1000));

		column = new LongColumn("longVal");
		assertNotNull(column);
		assertEquals(new QueryToken("longVal == \'987654321012345678\'"), column.eq(987654321012345678l));

		column = new DoubleColumn("doulbeVal");
		assertNotNull(column);
		assertEquals(new QueryToken("doulbeVal == \'3.141592653\'"), column.eq(3.141592653));

		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken("textVal == \'this is \"string\" eq. \'123\'?\'"), column.eq("this is \"string\" eq. \'123\'?"));
		
		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken(), column.eq(1000));
	}

	public void testNEQOperator() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		assertEquals(new QueryToken("intVal != \'1000\'"), column.neq(1000));

		column = new LongColumn("longVal");
		assertNotNull(column);
		assertEquals(new QueryToken("longVal != \'987654321012345678\'"), column.neq(987654321012345678l));

		column = new DoubleColumn("doubleVal");
		assertNotNull(column);
		assertEquals(new QueryToken("doubleVal != \'3.141592653\'"), column.neq(3.141592653));

		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken("textVal != \'this is \"string\" neq. \'123\'?\'"), column.neq("this is \"string\" neq. \'123\'?"));
		
		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken(), column.neq(1000));
	}

	public void testINOperator() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		assertEquals(new QueryToken("( intVal >= \'1000\' ) AND ( intVal <= \'2000\' )"), 
				column.in(1000, 2000));

		column = new LongColumn("longVal");
		assertNotNull(column);
		assertEquals(new QueryToken("( longVal >= \'12345678987654321\' ) AND ( longVal <= \'987654321012345678\' )"), 
				column.in(12345678987654321l, 987654321012345678l));

		column = new DoubleColumn("doubleVal");
		assertNotNull(column);
		assertEquals(new QueryToken("( doubleVal >= \'1.4142135\' ) AND ( doubleVal <= \'3.141592653\' )"), 
				column.in(1.4142135, 3.141592653));

		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken("( textVal >= \'ABC\' ) AND ( textVal <= \'DEF\' )"), 
				column.in("ABC", "DEF"));
		
		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken(), column.in(1000, 2000));
	}

	public void testOUTOFOperator() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		assertEquals(new QueryToken("( intVal < \'1000\' ) OR ( intVal > \'2000\' )"), 
				column.outOf(1000, 2000));

		column = new LongColumn("longVal");
		assertNotNull(column);
		assertEquals(new QueryToken("( longVal < \'12345678987654321\' ) OR ( longVal > \'987654321012345678\' )"), 
				column.outOf(12345678987654321l, 987654321012345678l));

		column = new DoubleColumn("doubleVal");
		assertNotNull(column);
		assertEquals(new QueryToken("( doubleVal < \'1.4142135\' ) OR ( doubleVal > \'3.141592653\' )"), 
				column.outOf(1.4142135, 3.141592653));

		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken("( textVal < \'ABC\' ) OR ( textVal > \'DEF\' )"), 
				column.outOf("ABC", "DEF"));
		
		column = new TextColumn("textVal");
		assertNotNull(column);
		assertEquals(new QueryToken(), column.outOf(1000, 2000));
	}

}
