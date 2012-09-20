package com.dailystudio.database;

import com.dailystudio.test.ActivityTestCase;

@Deprecated
public class DatabaseConnectionTest extends ActivityTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGetDatabase() {
		DatabaseConnectivity conn = 
			new DatabaseLocalConnectivity(mTargetContext, "test-database.db");
		
		assertEquals("test-database.db", conn.getDatabase());
	}
	

}
