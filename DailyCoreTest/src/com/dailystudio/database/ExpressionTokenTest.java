package com.dailystudio.database;

import com.dailystudio.database.Column;
import com.dailystudio.database.QueryToken;
import com.dailystudio.database.Template;
import com.dailystudio.database.TemplateInflater;
import com.dailystudio.test.ActivityTestCase;
import com.dailystudio.test.R;

@Deprecated
public class ExpressionTokenTest extends ActivityTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateExpressionToken() {
		ExpressionToken token = null;

		token = new ExpressionToken(1234);
		assertEquals(new QueryToken("1234"), token);

		token = new ExpressionToken(3.1415);
		assertEquals(new QueryToken("3.1415"), token);

		token = new ExpressionToken("a < 10");
		assertEquals(new QueryToken("a < 10"), token);
	}
	
	public void testAndOperation() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		
		ExpressionToken token = null;
		token = column.gt(1000);
		assertEquals(new QueryToken("intVal > \'1000\'"), token);
		
		token.and(column.lt(2000));
		assertEquals(new QueryToken("( intVal > \'1000\' ) AND ( intVal < \'2000\' )"), token);
	}
	
	public void testOrOperation() {
		Column column = null;
		
		column = new IntegerColumn("intVal");
		assertNotNull(column);
		
		ExpressionToken token = null;
		token = column.lt(1000);
		assertEquals(new QueryToken("intVal < \'1000\'"), token);
		
		token.or(column.gt(2000));
		assertEquals(new QueryToken("( intVal < \'1000\' ) OR ( intVal > \'2000\' )"), token);
	}
	
	public void testSamples() {
		Template template = new TemplateInflater(mContext).inflateTemplate(R.xml.test_query_01);
		assertNotNull(template);
		
		assertEquals(new QueryToken("( intValue > \'1000\' ) AND ( intValue < \'2000\' )"),
				template.getColumn("intValue").gt(1000).and(template.getColumn("intValue").lt(2000)));
	}
	
}
