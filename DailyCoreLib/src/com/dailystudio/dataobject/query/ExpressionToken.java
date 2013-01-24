package com.dailystudio.dataobject.query;

import com.dailystudio.dataobject.Column;

public class ExpressionToken extends QueryToken {
	
	public ExpressionToken() {
		this((String)null);
	}

	public ExpressionToken(Column column) {
		super(column);
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
	
	public ExpressionToken plus(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_PLUS, 
				token, true, true);
	}
	
	public ExpressionToken minus(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_MINUS, 
				token, true, true);
	}
	
	public ExpressionToken multiple(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_MULTIPLE,
				token, true, true);
	}
	
	public ExpressionToken divide(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_DIVIDE, 
				token, true, true);
	}
	
	public ExpressionToken modulo(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_MODULO, 
				token, true, true);
	}
	
	public ExpressionToken and(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_AND, token);
	}
	
	public ExpressionToken or(ExpressionToken token) {
		return (ExpressionToken) binaryOperator(Expression.OPERATOR_OR, token);
	}
	
}
