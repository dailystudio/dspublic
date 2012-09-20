package com.dailystudio.database;

import java.util.ArrayList;
import java.util.List;

import com.dailystudio.database.Column;
import com.dailystudio.database.Template;
import com.dailystudio.test.Asserts;

import android.test.AndroidTestCase;

@Deprecated
public class TemplateTest extends AndroidTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateATemplate() {
		Template templ = new Template();
		assertNotNull(templ);
		
		List<Column> columns = templ.listColumns();
		assertNotNull(columns);
		assertEquals(0, columns.size());
	}
	
	public void testAddColumn() {
		Template templ = new Template();
		assertNotNull(templ);
		
		Column column = new Column("_id", Column.COL_TYPE_INTEGER);
		assertNotNull(column);
		templ.addColumn(column);
		
		List<Column> columns = templ.listColumns();
		assertNotNull(columns);
		assertEquals(1, columns.size());
		
		Column actualColumn = columns.get(0);
		assertEquals(column, actualColumn);
	}

	public void testRemoveColumn() {
		Template templ = new Template();
		assertNotNull(templ);
		
		Column column = new Column("_id", Column.COL_TYPE_INTEGER);
		assertNotNull(column);
		templ.addColumn(column);
		
		List<Column> columns = templ.listColumns();
		assertNotNull(columns);
		assertEquals(1, columns.size());
		
		Column actualColumn = columns.get(0);
		assertEquals(column, actualColumn);
		
		templ.removeColumn(column);
		columns = templ.listColumns();
		assertEquals(0, columns.size());
	}

	public void testAddColumns() {
		Template templ = new Template();
		assertNotNull(templ);
		
		Column col1 = new Column("_id", Column.COL_TYPE_INTEGER);
		Column col2 = new Column("time", Column.COL_TYPE_LONG);
		Column col3 = new Column("dummy", Column.COL_TYPE_TEXT);
		assertNotNull(col1);
		assertNotNull(col2);
		assertNotNull(col3);
		
		Column[] array = { col1, col2, col3 }; 
		templ.addColumns(array);
		
		List<Column> columns = templ.listColumns();
		assertNotNull(columns);
		assertEquals(3, columns.size());
		
		List<Column> expected = new ArrayList<Column>();
		expected.add(col1);
		expected.add(col2);
		expected.add(col3);
		
		Asserts.assertEquals(expected, columns);
	}
	
	public void testGetColumn() {
		Template templ = new Template();
		assertNotNull(templ);
		
		Column col1 = new Column("_id", Column.COL_TYPE_INTEGER);
		Column col2 = new Column("time", Column.COL_TYPE_LONG);
		Column col3 = new Column("dummy", Column.COL_TYPE_TEXT);
		assertNotNull(col1);
		assertNotNull(col2);
		assertNotNull(col3);
		
		templ.addColumn(col1);
		templ.addColumn(col2);
		templ.addColumn(col3);
		
		Column result = null;
		result = templ.getColumn("_id");
		assertEquals(col1, result);
		result = templ.getColumn("time");
		assertEquals(col2, result);
		result = templ.getColumn("dummy");
		assertEquals(col3, result);
		result = templ.getColumn(null);
		assertNull(result);
		result = templ.getColumn("test");
		assertNull(result);
	}

	public void testListColumns() {
		Template templ = new Template();
		assertNotNull(templ);
		
		Column col1 = new Column("_id", Column.COL_TYPE_INTEGER);
		Column col2 = new Column("time", Column.COL_TYPE_LONG);
		Column col3 = new Column("dummy", Column.COL_TYPE_TEXT);
		assertNotNull(col1);
		assertNotNull(col2);
		assertNotNull(col3);
		
		templ.addColumn(col1);
		templ.addColumn(col2);
		templ.addColumn(col3);
		
		List<Column> columns = templ.listColumns();
		assertNotNull(columns);
		assertEquals(3, columns.size());
		
		List<Column> expected = new ArrayList<Column>();
		expected.add(col1);
		expected.add(col2);
		expected.add(col3);
		
		Asserts.assertEquals(expected, columns);
	}
	
	public void testContainsColumns() {
		Template templ = new Template();
		assertNotNull(templ);
		
		Column col1 = new Column("col1", Column.COL_TYPE_INTEGER);
		Column col2 = new Column("col2", Column.COL_TYPE_DOUBLE);
		Column col3 = new Column("col3", Column.COL_TYPE_LONG);
		Column col4 = new Column("col4", Column.COL_TYPE_TEXT);
		assertNotNull(col1);
		assertNotNull(col2);
		assertNotNull(col3);
		assertNotNull(col4);
		
		templ.addColumn(col1);
		templ.addColumn(col2);
		templ.addColumn(col3);
		templ.addColumn(col4);

		assertEquals(true, templ.containsColumn("col1", Column.COL_TYPE_INTEGER));
		assertEquals(true, templ.containsColumn("col2", Column.COL_TYPE_DOUBLE));
		assertEquals(true, templ.containsColumn("col3", Column.COL_TYPE_LONG));
		assertEquals(true, templ.containsColumn("col4", Column.COL_TYPE_TEXT));
		
		assertEquals(false, templ.containsColumn("col4", Column.COL_TYPE_INTEGER));
		assertEquals(false, templ.containsColumn("col3", Column.COL_TYPE_DOUBLE));
		assertEquals(false, templ.containsColumn("col2", Column.COL_TYPE_LONG));
		assertEquals(false, templ.containsColumn("col1", Column.COL_TYPE_TEXT));
		
		assertEquals(false, templ.containsColumn("col1", null));
		assertEquals(false, templ.containsColumn(null, Column.COL_TYPE_LONG));
		assertEquals(false, templ.containsColumn(null, null));
	}
	
	public void testToSQLTableCreationString() {
		Template templ1 = new Template();
		assertNotNull(templ1);
		
		Column col1 = new Column("col1", Column.COL_TYPE_INTEGER);
		Column col2 = new Column("col2", Column.COL_TYPE_DOUBLE);
		Column col3 = new Column("col3", Column.COL_TYPE_LONG);
		Column col4 = new Column("col4", Column.COL_TYPE_TEXT);
		assertNotNull(col1);
		assertNotNull(col2);
		assertNotNull(col3);
		assertNotNull(col4);
		
		templ1.addColumn(col1);
		templ1.addColumn(col2);
		templ1.addColumn(col3);
		templ1.addColumn(col4);
		
		String expected = 
			"CREATE TABLE IF NOT EXISTS sample ( col1 INTEGER, col2 DOUBLE, col3 LONG, col4 TEXT );";
		assertEquals(expected, templ1.toSQLTableCreationString("sample"));

		Template templ2 = new Template();
		assertNotNull(templ2);
		assertNull(templ2.toSQLTableCreationString("sample"));

		Template templ3 = new Template();
		assertNotNull(templ3);
		
		templ3.addColumn(col1);
		templ3.addColumn(col2);
		templ3.addColumn(col3);
		templ3.addColumn(col4);
		
		assertNull(templ3.toSQLTableCreationString(null));
	}

	public void testToSQLProjectionString() {
		Template templ1 = new Template();
		assertNotNull(templ1);
		
		Column col1 = new Column("col1", Column.COL_TYPE_INTEGER);
		Column col2 = new Column("col2", Column.COL_TYPE_DOUBLE);
		Column col3 = new Column("col3", Column.COL_TYPE_LONG);
		Column col4 = new Column("col4", Column.COL_TYPE_TEXT);
		assertNotNull(col1);
		assertNotNull(col2);
		assertNotNull(col3);
		assertNotNull(col4);
		
		templ1.addColumn(col1);
		templ1.addColumn(col2);
		templ1.addColumn(col3);
		templ1.addColumn(col4);
		
		String[] expected = {
				"col1",
				"col2",
				"col3",
				"col4",
		};
		
		Asserts.assertEquals(expected, templ1.toSQLProjection());

		Template templ2 = new Template();
		assertNotNull(templ2);
		assertNull(templ2.toSQLProjection());
	}

}
