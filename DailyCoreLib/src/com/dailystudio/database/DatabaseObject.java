package com.dailystudio.database;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dailystudio.development.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

@Deprecated
public class DatabaseObject {

	private ContentValues mValues;
	
	protected Template mTemplate;
	
	protected Context mContext;
	
	public DatabaseObject(Context context) {
		mContext = context; 

		initMembers();
	}
	
	private void initMembers() {
		mValues = new ContentValues();
		mTemplate = new Template();
	}
	
	protected Template inflate(int resid) {
		TemplateInflater inflater = new TemplateInflater(mContext);
		
		return inflater.inflateTemplate(resid);
	}
	
	protected void setTemplate(Template templ) {
		if (templ == null) {
			return;
		}
		
		mTemplate = templ;
	}
	
	public Template getTemplate() {
		return mTemplate;
	}
	
	public final ContentValues getValues() {
		return mValues;
	}
	
	private boolean isColumnValid(String colName, String colType) {
		if (mTemplate == null) {
			return false;
		}
		
		boolean valid = mTemplate.containsColumn(colName, colType);
		
		if (valid == false) {
			Logger.warnning("Invalid column(%s) with type(%s)", colName, colType);
		}
		
		return valid;
	}
	
	public void setIntegerValue(Column column, Integer value) {
		if (column == null) {
			return;
		}
		
		setIntegerValue(column.columnName, value);
	}
	
	public void setIntegerValue(String colName, Integer value) {
		if (isColumnValid(colName, Column.COL_TYPE_INTEGER) == false) {
			return;
		}
		
		if (mValues == null) {
			return;
		}
		
		mValues.put(colName, value);
	}
	
	public void setDoubleValue(Column column, Double value) {
		if (column == null) {
			return;
		}
		
		setDoubleValue(column.columnName, value);
	}
	
	public void setDoubleValue(String colName, Double value) {
		if (isColumnValid(colName, Column.COL_TYPE_DOUBLE) == false) {
			return;
		}
		
		if (mValues == null) {
			return;
		}
		
		mValues.put(colName, value);
	}
	
	public void setLongValue(Column column, Long value) {
		if (column == null) {
			return;
		}
		
		setLongValue(column.columnName, value);
	}
	
	public void setLongValue(String colName, Long value) {
		if (isColumnValid(colName, Column.COL_TYPE_LONG) == false) {
			return;
		}
		
		if (mValues == null) {
			return;
		}
		
		mValues.put(colName, value);
	}
	
	public void setTextValue(Column column, String value) {
		if (column == null) {
			return;
		}
		
		setTextValue(column.columnName, value);
	}
	
	public void setTextValue(String colName, String value) {
		if (isColumnValid(colName, Column.COL_TYPE_TEXT) == false) {
			return;
		}
		
		if (mValues == null) {
			return;
		}
		
		mValues.put(colName, value);
	}
	
	public int getIntegerValue(Column column) {
		if (column == null) {
			return 0;
		}
		
		return getIntegerValue(column.columnName);
	}

	public int getIntegerValue(String colName) {
		if (isColumnValid(colName, Column.COL_TYPE_INTEGER) == false) {
			return 0;
		}
		
		if (mValues == null) {
			return 0;
		}
		
		Integer intVal = mValues.getAsInteger(colName);
		if (intVal == null) {
			return 0;
		}
		
		return intVal.intValue();
	}
	
	public long getLongValue(Column column) {
		if (column == null) {
			return 0l;
		}
		
		return getLongValue(column.columnName);
	}

	public long getLongValue(String colName) {
		if (isColumnValid(colName, Column.COL_TYPE_LONG) == false) {
			return 0l;
		}
		
		if (mValues == null) {
			return 0l;
		}
		
		Long longVal = mValues.getAsLong(colName);
		if (longVal == null) {
			return 0l;
		}
		
		return longVal.longValue();
	}
	
	public double getDoubleValue(Column column) {
		if (column == null) {
			return .0;
		}
		
		return getDoubleValue(column.columnName);
	}

	public double getDoubleValue(String colName) {
		if (isColumnValid(colName, Column.COL_TYPE_DOUBLE) == false) {
			return .0;
		}
		
		if (mValues == null) {
			return .0;
		}
		
		Double dbVal = mValues.getAsDouble(colName);
		if (dbVal == null) {
			return .0;
		}
		
		return dbVal.doubleValue();
	}
	
	public String getTextValue(Column column) {
		if (column == null) {
			return null;
		}
		
		return getTextValue(column.columnName);
	}

	public String getTextValue(String colName) {
		if (isColumnValid(colName, Column.COL_TYPE_TEXT) == false) {
			return null;
		}
		
		if (mValues == null) {
			return null;
		}
		
		return mValues.getAsString(colName);
	}
	
	public boolean isEmpty() {
		if (mValues == null || mValues.size() <= 0) {
			return true;
		}
		
		return false;
	}
	
	public String handleNullProjection() {
		return null;
	}
	
	public Intent convertToIntent() {
		if (mValues == null) {
			return null;
		}
		
		if (mTemplate == null) {
			return null;
		}
		
		Intent i = new Intent();
		
		List<Column> columns = mTemplate.listColumns();
		if (columns == null) {
			return null;
		}
		
		String colName = null;
		String colType = null;
		
		for (Column column: columns) {
			if (column == null 
					|| column.columnName == null
					|| column.columnType == null) {
				continue;
			}
			
			colName = column.columnName;
			colType = column.columnType;
		
			if (colType.equals(Column.COL_TYPE_INTEGER)) {
				int iVal = getIntegerValue(column);
				
				i.putExtra(colName, iVal);
			} else if (colType.equals(Column.COL_TYPE_LONG)) {
				long lVal = getLongValue(column);
				
				i.putExtra(colName, lVal);
			} else if (colType.equals(Column.COL_TYPE_DOUBLE)) {
				double dVal = getDoubleValue(column);
				
				i.putExtra(colName, dVal);
			} else if (colType.equals(Column.COL_TYPE_TEXT)) {
				String sVal = getTextValue(column);
				
				if (sVal != null) {
					i.putExtra(colName, sVal);
				}
			} 
		}
		
		return i;
	}
	
	public void fillValuesFromCursor(Cursor c) {
		if (c == null)  {
			return;
		}
		
		if (mValues == null) {
			return;
		}
		
		if (mTemplate == null) {
			return;
		}
		
		List<Column> columns = mTemplate.listColumns();
		if (columns == null) {
			return;
		}
		
		int columnIndex = -1;
		String colName = null;
		String colType = null;
		
		for (Column column: columns) {
			if (column == null 
					|| column.columnName == null
					|| column.columnType == null) {
				continue;
			}
			
			colName = column.columnName;
			colType = column.columnType;
			try {
				columnIndex = c.getColumnIndexOrThrow(colName);
/*				Logger.debug("colName(%s), colType(%s), columnIndex(%s)",
						colName, colType, columnIndex);
*/
				if (colType.equals(Column.COL_TYPE_INTEGER)) {
					int iVal = c.getInt(columnIndex);
//					Logger.debug("iVal(%s)", iVal);
					
					setIntegerValue(colName, iVal);
				} else if (colType.equals(Column.COL_TYPE_LONG)) {
					long lVal = c.getLong(columnIndex);
//					Logger.debug("lVal(%s)", lVal);
					
					setLongValue(colName, lVal);
				} else if (colType.equals(Column.COL_TYPE_DOUBLE)) {
					double dVal = c.getDouble(columnIndex);
//					Logger.debug("dVal(%s)", dVal);
					
					setDoubleValue(colName, dVal);
				} else if (colType.equals(Column.COL_TYPE_TEXT)) {
					String sVal = c.getString(columnIndex);
//					Logger.debug("sVal(%s)", sVal);
					
					if (sVal != null) {
						setTextValue(colName, sVal);
					}
				} 
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	public String toSQLSelectionString() {
		if (isEmpty()) {
			return "";
		}
		
		if (mTemplate == null) {
			return "";
		}
		
		final List<Column> columns = mTemplate.listColumns();
		if (columns == null || columns.size() <= 0) {
			return "";
		}
		
		final List<String> parts = new ArrayList<String>();

		String colType = null;
		String colName = null;
		
		for (Column column: columns) {
			colType = column.columnType;
			colName = column.columnName;
			
			if (colType == null || colName == null) {
				continue;
			}
			
			if (colType.equals(Column.COL_TYPE_INTEGER)) {
				Integer iVal = getIntegerValue(colName);
				if (iVal != null) {
					parts.add(String.format("%s = \'%d\'", 
							colName, iVal.intValue()));
				}
			} else if (colType.equals(Column.COL_TYPE_LONG)) {
				Long lVal = getLongValue(column.columnName);
				if (lVal != null) {
					parts.add(String.format("%s = \'%d\'", 
							colName, lVal.longValue()));
				}
			} else if (colType.equals(Column.COL_TYPE_DOUBLE)) {
				Double dVal = getDoubleValue(column.columnName);
				if (dVal != null) {
					parts.add(String.format("%s = \'%s\'", 
							colName, 
							new DecimalFormat("0.####################").format(dVal.doubleValue())));
				}
			} else if (colType.equals(Column.COL_TYPE_TEXT)) {
				String sVal = getTextValue(column.columnName);
				if (sVal != null) {
					parts.add(String.format("%s = \'%s\'", 
							colName, sVal));
				}
			} 
		}
		
		final int partsCount = parts.size();

		if (partsCount <= 0) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < partsCount; i++) {
			builder.append(parts.get(i));
			if (i != partsCount - 1) {
				builder.append(" AND ");
			}
		}
		
		return builder.toString();
	}
	
	public static String convertClassToTable(Class<? extends DatabaseObject> klass) {
		String className = klass.getName();
		if (className == null) {
			return null;
		}
			
		return className.replace('.', '_');
	}

}
