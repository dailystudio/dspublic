package com.dailystudio.database;

@Deprecated
public class LongColumn extends Column {
	
	public LongColumn(String colName) {
		this(colName, true);
	}
	
	public LongColumn(String colName, boolean allowNull) {
		this(colName, allowNull, false);
	}
	
	public LongColumn(String colName, boolean allowNull, boolean isPrimary) {
		super(colName, COL_TYPE_LONG, allowNull, isPrimary);
	}

	@Override
	protected boolean matchColumnType(Object value) {
		if (value == null) {
			return false;
		}
		
		return (value instanceof Long);
	}

	@Override
	protected String valueToString(Object value) {
		if (matchColumnType(value) == false) {
			return null;
		}
		
		return String.valueOf(((Long)value).longValue());
	}
	
	public ExpressionToken eq(long value) {
		return binaryOperator(
				Expression.OPERATOR_EQ, value);
	}
	
	public ExpressionToken neq(long value) {
		return binaryOperator(
				Expression.OPERATOR_NEQ, value);
	}
	
	public ExpressionToken gt(long value) {
		return binaryOperator(
				Expression.OPERATOR_GT, value);
	}
	
	public ExpressionToken gte(long value) {
		return binaryOperator(
				Expression.OPERATOR_GTE, value);
	}
	
	public ExpressionToken lt(long value) {
		return binaryOperator(
				Expression.OPERATOR_LT, value);
	}
	
	public ExpressionToken lte(long value) {
		return binaryOperator(
				Expression.OPERATOR_LTE, value);
	}
	
	public ExpressionToken in(long lower, long upper) {
		return this.gte(lower).and(this.lte(upper));
	}
	
	public ExpressionToken outOf(long lower, long upper) {
		return this.lt(lower).or(this.gt(upper));
	}
	
}
