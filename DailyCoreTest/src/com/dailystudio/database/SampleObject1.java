package com.dailystudio.database;

import android.content.Context;

import com.dailystudio.test.R;

@Deprecated
public class SampleObject1 extends DatabaseObject {

	public static final Column COLUMN_ID = new IntegerColumn("_id", false, true);
	public static final Column COLUMN_TIME = new LongColumn("time", false);
	
	public SampleObject1(Context context) {
		super(context);
		
		initMembers();
	}
	
	private void initMembers() {
		Template template = inflate(R.xml.templ_sample_object_1);
		
		if (template != null) {
			setTemplate(template);
		}
	}

}
