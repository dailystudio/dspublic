package com.dailystudio.database;

@Deprecated
public class TextColumn extends Column {
	
	public TextColumn(String colName) {
		this(colName, true);
	}
	
	public TextColumn(String colName, boolean allowNull) {
		this(colName, allowNull, false);
	}
	
	public TextColumn(String colName, boolean allowNull, boolean isPrimary) {
		super(colName, COL_TYPE_TEXT, allowNull, isPrimary);
	}

	@Override
	protected boolean matchColumnType(Object value) {
		if (value == null) {
			return false;
		}
		
		return (value instanceof String);
	}

	@Override
	protected String valueToString(Object value) {
		if (matchColumnType(value) == false) {
			return null;
		}
		
		return (String)value;
	}
	
	public ExpressionToken eq(String value) {
		return binaryOperator(
				Expression.OPERATOR_EQ, value);
	}
	
	public ExpressionToken neq(String value) {
		return binaryOperator(
				Expression.OPERATOR_NEQ, value);
	}
	
	public ExpressionToken gt(String value) {
		return binaryOperator(
				Expression.OPERATOR_GT, value);
	}
	
	public ExpressionToken gte(String value) {
		return binaryOperator(
				Expression.OPERATOR_GTE, value);
	}

	public ExpressionToken lt(String value) {
		return binaryOperator(
				Expression.OPERATOR_LT, value);
	}

	public ExpressionToken lte(String value) {
		return binaryOperator(
				Expression.OPERATOR_LTE, value);
	}

	public ExpressionToken in(String lower, String upper) {
		return this.gte(lower).and(this.lte(upper));
	}

	public ExpressionToken outOf(String lower, String upper) {
		return this.lt(lower).or(this.gt(upper));
	}

}
