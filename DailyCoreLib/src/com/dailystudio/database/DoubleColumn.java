package com.dailystudio.database;

@Deprecated
public class DoubleColumn extends Column {
	
	public DoubleColumn(String colName) {
		this(colName, true);
	}
	
	public DoubleColumn(String colName, boolean allowNull) {
		this(colName, allowNull, false);
	}
	
	public DoubleColumn(String colName, boolean allowNull, boolean isPrimary) {
		super(colName, COL_TYPE_DOUBLE, allowNull, isPrimary);
	}

	@Override
	protected boolean matchColumnType(Object value) {
		if (value == null) {
			return false;
		}
		
		return (value instanceof Double);
	}

	@Override
	protected String valueToString(Object value) {
		if (matchColumnType(value) == false) {
			return null;
		}
		
		return String.valueOf(((Double)value).doubleValue());
	}
	
	public ExpressionToken eq(double value) {
		return binaryOperator(
				Expression.OPERATOR_EQ, value);
	}
	
	public ExpressionToken neq(double value) {
		return binaryOperator(
				Expression.OPERATOR_NEQ, value);
	}
	
	public ExpressionToken gt(double value) {
		return binaryOperator(
				Expression.OPERATOR_GT, value);
	}
	
	public ExpressionToken gte(double value) {
		return binaryOperator(
				Expression.OPERATOR_GTE, value);
	}
	
	public ExpressionToken lt(double value) {
		return binaryOperator(
				Expression.OPERATOR_LT, value);
	}
	
	public ExpressionToken lte(double value) {
		return binaryOperator(
				Expression.OPERATOR_LTE, value);
	}
	
	public ExpressionToken in(double lower, double upper) {
		return this.gte(lower).and(this.lte(upper));
	}
	
	public ExpressionToken outOf(double lower, double upper) {
		return this.lt(lower).or(this.gt(upper));
	}
	
}
