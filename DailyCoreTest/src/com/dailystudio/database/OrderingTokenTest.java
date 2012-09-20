package com.dailystudio.database;

import com.dailystudio.database.Column;
import com.dailystudio.database.QueryToken;
import com.dailystudio.test.ActivityTestCase;

@Deprecated
public class OrderingTokenTest extends ActivityTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testWithOperation() {
		Column column1 = null;
		
		column1 = new IntegerColumn("intVal");
		assertNotNull(column1);
		
		Column column2 = null;
		
		column2 = new DoubleColumn("doubleVal");
		assertNotNull(column2);
		
		Column column3 = null;
		
		column3 = new LongColumn("longVal");
		assertNotNull(column3);
		
		Column column4 = null;
		
		column4 = new LongColumn("textVal");
		assertNotNull(column4);
		
		assertEquals(new QueryToken("intVal, doubleVal, longVal, textVal"), 
				column1.groupBy()
				.with(column2.groupBy())
				.with(column3.groupBy())
				.with(column4.groupBy()));
	}
	
}
