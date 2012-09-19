package com.dailystudio.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class Template {
	
	private final static String TABLE_SQL_BASE = "CREATE TABLE IF NOT EXISTS ";

	private List<Column> mColumns = null;
	private HashMap<String, Column> mColumnsMap = null;
	
	public Template() {
		initMembers();
	}
	
	private void initMembers() {
		mColumns = new ArrayList<Column>();
		mColumnsMap = new HashMap<String, Column>();
	}
	
	public synchronized void addColumns(Column[] columns) {
		if (columns != null) {
			for (Column col: columns) {
				if (col == null || col.isValid() == false) {
					continue;
				}
				
				if (mColumns != null) {
					mColumns.add(col);
				}
				
				if (mColumnsMap != null) {
					mColumnsMap.put(col.columnName, col);
				}
			}
		}
	}

	public synchronized void addColumn(Column column) {
		if (column == null || column.isValid() == false) {
			return;
		}
		
		if (mColumns != null) {
			mColumns.add(column);
		}
		
		if (mColumnsMap != null) {
			mColumnsMap.put(column.columnName, column);
		}
	}
	
	public synchronized void removeColumn(Column column) {
		if (column == null) {
			return;
		}
		
		if (mColumns != null) {
			mColumns.remove(column);
		}
		
		if (mColumnsMap != null && column.columnName != null) {
			mColumnsMap.remove(column.columnName);
		}
	}
	
	public synchronized List<Column> listColumns() {
		return new ArrayList<Column>(mColumns);
	}
	
	public synchronized Column getColumn(String colName) {
		if (colName == null) {
			return null;
		}
		
		if (mColumnsMap == null) {
			return null;
		}

		return mColumnsMap.get(colName);
	}

	public boolean containsColumn(String columnName, String colType) {
		if (columnName == null || colType == null) {
			return false;
		}
		
		if (mColumnsMap == null) {
			return false;
		}
		
		Column column = mColumnsMap.get(columnName);
		
		return (column != null && colType.equals(column.columnType));
	}
	
	public boolean isEmpty() {
		return (mColumns == null || mColumns.size() <= 0);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Template == false) {
			return false;
		}
		
		Template templ = (Template)o;

		if (isEmpty() && templ.isEmpty()) {
			return true;
		}
		
		if (mColumns.size() != templ.mColumns.size()) {
			return false;
		}
		
		final int count = mColumns.size();
		for (int i = 0; i < count; i++) {
			if (mColumns.get(i).equals(templ.mColumns.get(i)) == false) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("columns(%s)", mColumns);
	}
	
	public String[] toSQLProjection() {
		final List<Column> columns = listColumns();
		if (columns == null) {
			return null;
		}
		
		final int size = columns.size();
		if (size <= 0) {
			return null;
		}
		
		String[] projection = new String[size];
		Column column = null;
		for (int i = 0; i < size; i++) {
			column = columns.get(i);
			
			projection[i] = column.columnName;
		}
		
		return projection;
	}
	
	public String toSQLTableCreationString(String tableName) {
		if (tableName == null) {
			return null;
		}
		
		StringBuilder sqlBuilder = new StringBuilder(TABLE_SQL_BASE);
		
		sqlBuilder.append(tableName).append(" ( ");
		final List<Column> columns = listColumns();
		if (columns == null) {
			return null;
		}
		
		final int size = columns.size();
		if (size <= 0) {
			return null;
		}
		
		Column column = null;
		for (int i = 0; i < size; i++) {
			column = columns.get(i);
			
			sqlBuilder.append(column.toString());
			if (i != size - 1) {
				sqlBuilder.append(", ");
			} else {
				sqlBuilder.append(" );");
			}
		}
		
		return sqlBuilder.toString();
	}

}
