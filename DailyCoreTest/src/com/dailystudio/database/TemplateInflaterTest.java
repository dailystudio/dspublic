package com.dailystudio.database;

import java.util.ArrayList;
import java.util.List;

import com.dailystudio.database.Column;
import com.dailystudio.database.Template;
import com.dailystudio.database.TemplateInflater;
import com.dailystudio.test.ActivityTestCase;
import com.dailystudio.test.Asserts;
import com.dailystudio.test.R;

import android.content.res.XmlResourceParser;

@Deprecated
public class TemplateInflaterTest extends ActivityTestCase{

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testInflateColumn() {
		TemplateInflater inflater = new TemplateInflater(mContext);
		assertNotNull(inflater);
		
		XmlResourceParser parser = mContext.getResources().getXml(R.xml.test_templinflater_01);
		assertNotNull(parser);
		
		List<Column> columns = inflater.inflateColumns(parser);
		List<Column> expected = new ArrayList<Column>();
	        		
		Column col1 = new Column("_id", Column.COL_TYPE_INTEGER, false, true);
		Column col2 = new Column("time", Column.COL_TYPE_LONG);
		expected.add(col1);
		expected.add(col2);
		
		assertEquals(expected, columns);
	}
	
	public void testInflateTemplate() {
		TemplateInflater inflater = new TemplateInflater(mContext);
		assertNotNull(inflater);
		
		XmlResourceParser parser = mContext.getResources().getXml(R.xml.test_templinflater_02);
		assertNotNull(parser);
		
		List<Template> templs = inflater.inflateTemplates(parser);
		List<Template> expected = new ArrayList<Template>();
		
		Template templ = null;
		Column col1 = null;
		Column col2 = null;
		Column col3 = null;
		Column col4 = null;
		
		templ = new Template(); 
		assertNotNull(templ);
		col1 = new Column("_id", Column.COL_TYPE_INTEGER, false, true);
		col2 = new Column("time", Column.COL_TYPE_LONG, false);
		templ.addColumn(col1);
		templ.addColumn(col2);
		expected.add(templ);

		templ = new Template(); 
		assertNotNull(templ);
		col1 = new Column("_id", Column.COL_TYPE_INTEGER, false, true);
		col2 = new Column("latitude", Column.COL_TYPE_DOUBLE);
		col3 = new Column("longitude", Column.COL_TYPE_DOUBLE);
		col4 = new Column("altitude", Column.COL_TYPE_DOUBLE);
		templ.addColumn(col1);
		templ.addColumn(col2);
		templ.addColumn(col3);
		templ.addColumn(col4);
		expected.add(templ);
		
		Asserts.assertEquals(expected, templs);
	}
	
	public void testInflateTemplateFromXml() {
		TemplateInflater inflater = new TemplateInflater(mContext);
		assertNotNull(inflater);
		
		List<Template> templs = inflater.inflateTemplates(R.xml.test_templinflater_02);
		List<Template> expected = new ArrayList<Template>();
		
		Template templ = null;
		Column col1 = null;
		Column col2 = null;
		Column col3 = null;
		Column col4 = null;
		
		templ = new Template(); 
		assertNotNull(templ);
		col1 = new Column("_id", Column.COL_TYPE_INTEGER, false, true);
		col2 = new Column("time", Column.COL_TYPE_LONG, false);
		templ.addColumn(col1);
		templ.addColumn(col2);
		expected.add(templ);

		templ = new Template(); 
		assertNotNull(templ);
		col1 = new Column("_id", Column.COL_TYPE_INTEGER, false, true);
		col2 = new Column("latitude", Column.COL_TYPE_DOUBLE);
		col3 = new Column("longitude", Column.COL_TYPE_DOUBLE);
		col4 = new Column("altitude", Column.COL_TYPE_DOUBLE);
		templ.addColumn(col1);
		templ.addColumn(col2);
		templ.addColumn(col3);
		templ.addColumn(col4);
		expected.add(templ);
		
		Asserts.assertEquals(expected, templs);
	}
}
