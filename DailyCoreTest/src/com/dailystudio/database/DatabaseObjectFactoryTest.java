package com.dailystudio.database;

import java.util.ArrayList;
import java.util.HashMap;

import com.dailystudio.GlobalContextWrapper;
import com.dailystudio.database.Template;
import com.dailystudio.database.TemplateInflater;
import com.dailystudio.test.ActivityTestCase;
import com.dailystudio.test.R;

import android.test.mock.MockCursor;

@Deprecated
public class DatabaseObjectFactoryTest extends ActivityTestCase {

	private static class MockData {
		int mIntVal;
		long mLongVal;
		double mDoubleVal;
		String mStringVal;
	}
	
	public static class SimpleMockCursor extends MockCursor {
		
		public static final String COL_INT_VAL = "intVal";
		public static final String COL_LONG_VAL = "longVal";
		public static final String COL_DOUBLE_VAL = "doubleVal";
		public static final String COL_TEXT_VAL = "textVal";
		
		private static final int IND_INT_VAL = 0;
		private static final int IND_LONG_VAL = 1;
		private static final int IND_DOUBLE_VAL = 2;
		private static final int IND_TEXT_VAL = 3;
		
		private static final String[] mColumnNames = {
			COL_INT_VAL,
			COL_LONG_VAL,
			COL_DOUBLE_VAL,
			COL_TEXT_VAL,
		};
		
		private static final int COUNT = 10;
		private static HashMap<String, Integer> mNameToIndexMap = new HashMap<String, Integer>();
		private static ArrayList<MockData> mData = new ArrayList<MockData>();
		
		static {
			mNameToIndexMap.put(COL_INT_VAL, IND_INT_VAL);
			mNameToIndexMap.put(COL_LONG_VAL, IND_LONG_VAL);
			mNameToIndexMap.put(COL_DOUBLE_VAL, IND_DOUBLE_VAL);
			mNameToIndexMap.put(COL_TEXT_VAL, IND_TEXT_VAL);
			
			MockData data = null;
			for (int i = 0; i < COUNT; i++) {
				data = new MockData();
				
				data.mIntVal = i;
				data.mLongVal = i * 1000l;
				data.mDoubleVal = i * 0.0001;
				data.mStringVal = String.format("Text%03d", i);
				
				mData.add(data);
			}
		}
		
		private int mIndex = 0;
		
		@Override
		public boolean moveToFirst() {
			mIndex = 0;
			
			return true;
		}
		
		@Override
		public boolean moveToLast() {
			mIndex = (COUNT - 1);
			return true;
		}
		
		@Override
		public boolean moveToNext() {
			if (mIndex == (COUNT - 1)) {
				return false;
			}
			
			mIndex++;
			
			return true;
		}
		
		@Override
		public boolean moveToPrevious() {
			if (mIndex == 0) {
				return false;
			}
			
			mIndex--;
			
			return true;
		}
		
		@Override
		public int getCount() {
			return COUNT;
		};
		
		@Override
		public int getColumnCount() {
			return 4;
		}
		
		@Override
		public int getColumnIndex(String columnName) {
			Integer index = mNameToIndexMap.get(columnName);
			if (index == null) {
				return -1;
			}
			
			return index.intValue();
		}
		
		@Override
		public int getColumnIndexOrThrow(String columnName) {
			Integer index = mNameToIndexMap.get(columnName);
			if (index == null) {
				throw new IllegalArgumentException(
						String.format("no such column %s", columnName));
			}
			
			return index.intValue();
		}
		
		@Override
		public int getPosition() {
			return mIndex;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex < 0 || columnIndex >= mColumnNames.length) {
				return null;
			}
			
			return mColumnNames[columnIndex];
		}
		
		@Override
		public int getInt(int columnIndex) {
			if (columnIndex != getColumnIndex(COL_INT_VAL)) {
				return -1;
			}
			
			if (mIndex < 0 || mIndex >= mData.size()) {
				return -1;
			}
			
			MockData data = mData.get(mIndex);
			if (data == null) {
				return -1;
			}
			
			return data.mIntVal;
		}

		@Override
		public long getLong(int columnIndex) {
			if (columnIndex != getColumnIndex(COL_LONG_VAL)) {
				return -1l;
			}
			
			if (mIndex < 0 || mIndex >= mData.size()) {
				return -1l;
			}
			
			MockData data = mData.get(mIndex);
			if (data == null) {
				return -1l;
			}
			
			return data.mLongVal;
		}

		@Override
		public double getDouble(int columnIndex) {
			if (columnIndex != getColumnIndex(COL_DOUBLE_VAL)) {
				return 0.f;
			}
			
			if (mIndex < 0 || mIndex >= mData.size()) {
				return 0.f;
			}
			
			MockData data = mData.get(mIndex);
			if (data == null) {
				return 0.f;
			}
			
			return data.mDoubleVal;
		}
		
		@Override
		public String getString(int columnIndex) {
			if (columnIndex != getColumnIndex(COL_TEXT_VAL)) {
				return null;
			}
			
			if (mIndex < 0 || mIndex >= mData.size()) {
				return null;
			}
			
			MockData data = mData.get(mIndex);
			if (data == null) {
				return null;
			}
			
			return data.mStringVal;
		}
		
	}

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
	
	public void testCreateDefaultDatabaseObject() {
		DatabaseObjectFactory factory = DatabaseObjectFactory.getInstance();
		assertNotNull(factory);
		
		DatabaseObject object1 = null;
		DatabaseObject object2 = null;
		
		object1 = factory.createObject(null);
		assertNotNull(object1);
		assertEquals(true, object1 instanceof DatabaseObject);

		object2 = factory.createObject(TestDatabaseObject.class);
		assertNotNull(object2);
		assertEquals(false, (object1 == object2));
		assertEquals(true, object2 instanceof TestDatabaseObject);
	}
	
	public void testCreateDatabaseObject() {
		TemplateInflater inflater = new TemplateInflater(mContext);
		assertNotNull(inflater);
		
		Template templ = inflater.inflateTemplate(R.xml.templ_test_database_object);
		assertNotNull(templ);
		
		DatabaseObjectFactory factory = DatabaseObjectFactory.getInstance();
		assertNotNull(factory);
		
		DatabaseObject object = null;

		object = factory.createObject(TestDatabaseObject.class);
		assertNotNull(object);
		assertEquals(true, object instanceof TestDatabaseObject);
		assertEquals(templ, object.getTemplate());
		
		MockCursor cursor = new SimpleMockCursor();
		assertNotNull(cursor);
		
		assertEquals(true, cursor.moveToFirst());
		
		int i = 0;
		do {
			object = factory.createObject(TestDatabaseObject.class);
			assertNotNull(object);
			
			object.fillValuesFromCursor(cursor);
			
			assertEquals(true, object instanceof TestDatabaseObject);
			assertEquals(templ, object.getTemplate());
			assertEquals(i, object.getIntegerValue(SimpleMockCursor.COL_INT_VAL));
			assertEquals(i * 1000l, object.getLongValue(SimpleMockCursor.COL_LONG_VAL));
			assertEquals(i * 0.0001, object.getDoubleValue(SimpleMockCursor.COL_DOUBLE_VAL));
			assertEquals(String.format("Text%03d", i), object.getTextValue(SimpleMockCursor.COL_TEXT_VAL));
			
			i++;
		} while (cursor.moveToNext());
			
	}
}
