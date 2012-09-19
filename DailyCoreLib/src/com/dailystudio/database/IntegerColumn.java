package com.dailystudio.database;

@Deprecated
public class IntegerColumn extends Column {
	
	public IntegerColumn(String colName) {
		this(colName, true);
	}
	
	public IntegerColumn(String colName, boolean allowNull) {
		this(colName, allowNull, false);
	}
	
	public IntegerColumn(String colName, boolean allowNull, boolean isPrimary) {
		super(colName, COL_TYPE_INTEGER, allowNull, isPrimary);
	}

	@Override
	protected boolean matchColumnType(Object value) {
		if (value == null) {
			return false;
		}
		
		return (value instanceof Integer);
	}

	@Override
	protected String valueToString(Object value) {
		if (matchColumnType(value) == false) {
			return null;
		}
		
		return String.valueOf(((Integer)value).intValue());
	}
	
	public ExpressionToken eq(int value) {
		return binaryOperator(
				Expression.OPERATOR_EQ, value);
	}
	
	public ExpressionToken neq(int value) {
		return binaryOperator(
				Expression.OPERATOR_NEQ, value);
	}
	
	public ExpressionToken gt(int value) {
		return binaryOperator(
				Expression.OPERATOR_GT, value);
	}
	
	public ExpressionToken gte(int value) {
		return binaryOperator(
				Expression.OPERATOR_GTE, value);
	}
	
	public ExpressionToken lt(int value) {
		return binaryOperator(
				Expression.OPERATOR_LT, value);
	}
	
	public ExpressionToken lte(int value) {
		return binaryOperator(
				Expression.OPERATOR_LTE, value);
	}
	
	public ExpressionToken in(int lower, int upper) {
		return this.gte(lower).and(this.lte(upper));
	}
	
	public ExpressionToken outOf(int lower, int upper) {
		return this.lt(lower).or(this.gt(upper));
	}
	
}
