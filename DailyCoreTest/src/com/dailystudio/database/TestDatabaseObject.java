package com.dailystudio.database;

import android.content.Context;

import com.dailystudio.test.R;

@Deprecated
public class TestDatabaseObject extends DatabaseObject {
	
	public static final Column COLUMN_INTEGER_VAL = new IntegerColumn("intVal");
	public static final Column COLUMN_LONG_VAL = new LongColumn("longVal");
	public static final Column COLUMN_DOUBLE_VAL = new DoubleColumn("doubleVal");
	public static final Column COLUMN_TEXT_VAL = new TextColumn("textVal");
	
	public TestDatabaseObject(Context context) {
		super(context);
		
		initMembers();
	}
	
	private void initMembers() {
		Template template = inflate(R.xml.templ_test_database_object);
		
		if (template != null) {
			setTemplate(template);
		}
	}

}
