package com.dailystudio.database;

@Deprecated
public class ExpressionToken extends QueryToken {
	
	public ExpressionToken() {
		this(null);
	}

	public ExpressionToken(int intVal) {
		super(String.valueOf(intVal));
	}
	
	public ExpressionToken(double dbVal) {
		super(String.valueOf(dbVal));
	}
	
	public ExpressionToken(long longVal) {
		super(String.valueOf(longVal));
	}
	
	public ExpressionToken(String token) {
		super(token);
	}
	
	public ExpressionToken and(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_AND, token);
	}
	
	public ExpressionToken or(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_OR, token);
	}
	
}
