package com.dailystudio.database;

import android.content.Context;

import com.dailystudio.test.R;

@Deprecated
public class SampleObject2 extends DatabaseObject {

	public static final Column COLUMN_ID = new IntegerColumn("_id", false, true);
	public static final Column COLUMN_LON = new DoubleColumn("longitude");
	public static final Column COLUMN_LAT = new DoubleColumn("latitude");
	public static final Column COLUMN_ALT = new DoubleColumn("altitude");
	
	public SampleObject2(Context context) {
		super(context);
		
		initMembers();
	}
	
	private void initMembers() {
		Template template = inflate(R.xml.templ_sample_object_2);
		
		if (template != null) {
			setTemplate(template);
		}
	}

}
