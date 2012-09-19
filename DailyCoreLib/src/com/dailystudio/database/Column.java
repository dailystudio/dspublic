package com.dailystudio.database;

import com.dailystudio.development.Logger;

@Deprecated
public class Column {
	
	public final static String COL_TYPE_INTEGER = "INTEGER";
	public final static String COL_TYPE_LONG = "LONG";
	public final static String COL_TYPE_DOUBLE = "DOUBLE";
	public final static String COL_TYPE_TEXT = "TEXT";
	
	private static final String WHITE_SPACE = " ";
	private static final String CONSTRAINT_NOT_NULL = " NOT NULL";
	private static final String CONSTRAINT_PRIMARY_KEY = " PRIMARY KEY";
	
	private static final ExpressionToken sEmtpyExpToken = new ExpressionToken();
	private static final OrderingToken sEmtpyOrderToken = new OrderingToken();

	public String columnType;
	public String columnName;
	public boolean allowNull;
	public boolean isPrimary;
	
	public Column(String colName, String colType) {
		this(colName, colType, true);
	}
	
	public Column(String colName, String colType, boolean allowNull) {
		this(colName, colType, allowNull, false);
	}
	
	public Column(String colName, String colType, boolean allowNull, boolean isPrimary) {
		columnType = colType;
		columnName = colName;
		
		this.allowNull = allowNull;
		this.isPrimary = isPrimary;
	}
	
	public boolean isValid() {
		return (columnName != null && columnType != null);
	}

	@Override
	public String toString() {
		if (columnName == null || columnType == null) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(columnName).append(WHITE_SPACE).append(columnType);
		if (allowNull == false) {
			builder.append(CONSTRAINT_NOT_NULL);
		}
		
		if (isPrimary) {
			builder.append(CONSTRAINT_PRIMARY_KEY);
		}
		
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Column == false) {
			return false;
		}
		
		if (columnName == null || columnType == null) {
			return false;
		}
		
		Column column = (Column)o;
		if (column.columnName == null || column.columnType == null) {
			return false;
		}

		return (columnName.equals(column.columnName)
				&& columnType.equals(column.columnType)
				&& (allowNull == column.allowNull)
				&& (isPrimary == column.isPrimary));
	}
	
	private static String[] sSupportedColumnTypes = {
		COL_TYPE_DOUBLE,
		COL_TYPE_INTEGER,
		COL_TYPE_LONG,
		COL_TYPE_TEXT,
	};
	
	public static String getNormalizedColumnType(String colType) {
		if (colType == null) {
			return null;
		}
		
		for (String normalized: sSupportedColumnTypes) {
			if (normalized.equalsIgnoreCase(colType)) {
				return normalized;
			}
		}
		
		return null;
	}
	
	/*
	 * Expression Stuff
	 */
	
	public ExpressionToken eq(Object value) {
		return binaryOperator(
				Expression.OPERATOR_EQ, value);
	}
	
	public ExpressionToken neq(Object value) {
		return binaryOperator(
				Expression.OPERATOR_NEQ, value);
	}
	
	public ExpressionToken gt(Object value) {
		return binaryOperator(
				Expression.OPERATOR_GT, value);
	}
	
	public ExpressionToken gte(Object value) {
		return binaryOperator(
				Expression.OPERATOR_GTE, value);
	}
	
	public ExpressionToken lt(Object value) {
		return binaryOperator(
				Expression.OPERATOR_LT, value);
	}
	
	public ExpressionToken lte(Object value) {
		return binaryOperator(
				Expression.OPERATOR_LTE, value);
	}
	
	public ExpressionToken in(Object lower, Object upper) {
		return this.gte(lower).and(this.lte(upper));
	}
	
	public ExpressionToken outOf(Object lower, Object upper) {
		return this.lt(lower).or(this.gt(upper));
	}
	
	public OrderingToken groupBy() {
		return new OrderingToken(columnName);
	}
	
	public OrderingToken orderByAscending() {
		return orderBy(Expression.ORDER_ASCENDING);
	}
	
	public OrderingToken orderByDescending() {
		return orderBy(Expression.ORDER_DESCENDING);
	}
	
	private OrderingToken orderBy(String order) {
		if (order == null) {
			order = Expression.ORDER_ASCENDING;
		}
		
		String token = String.format("%s %s",
				columnName, order);
		if (token == null) {
			return sEmtpyOrderToken;
		}
		
		return new OrderingToken(token);
	}
	
	protected ExpressionToken binaryOperator(String operator, Object value) {
		if (operator == null) {
			return sEmtpyExpToken;
		}
		
		if (value == null) {
			return sEmtpyExpToken;
		}
		
		if (columnName == null || columnType == null) {
			return sEmtpyExpToken;
		}
		
		if (matchColumnType(value) == false) {
			Logger.warnning("Illegal operation(%s) on column(type: %s) with value(%s)",
					operator,
					columnType,
					value.getClass().getSimpleName());
			return sEmtpyExpToken;
		}
		
		String valStr = valueToString(value);
		if (valStr == null) {
			return sEmtpyExpToken;
		}
		
		String token = String.format("%s %s \'%s\'",
				columnName, operator, valStr);
		if (token == null) {
			return sEmtpyExpToken;
		}
		
		return new ExpressionToken(token);
	}

	protected boolean matchColumnType(Object value) {
		return false;
	}
	
	protected String valueToString(Object value) {
		return null;
	}
	

}
