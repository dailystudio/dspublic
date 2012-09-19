package com.dailystudio.database;

@Deprecated
public class QueryToken {
	
	protected StringBuilder mTokenBuilder = null;
	
	public QueryToken() {
		this(null);
	}

	public QueryToken(String token) {
		mTokenBuilder = new StringBuilder();
		
		if (token != null) {
			mTokenBuilder.append(token);
		}
	}
	
	protected QueryToken binaryOperator(String operator, QueryToken token) {
		return binaryOperator(operator, token, true);
	}

	protected QueryToken binaryOperator(String operator, QueryToken token, boolean withBrace) {
		if (operator == null || token == null) {
			return this;
		}
		
		String tstr = token.toString();
		if (tstr == null || tstr.length() <= 0) {
			return this;
		}
		
		if (mTokenBuilder == null || mTokenBuilder.length() <= 0) {
			return this;
		}
		
		if (withBrace) {
			mTokenBuilder.insert(0, Expression.OPERATOR_LEFT_BRACE);
			mTokenBuilder.append(Expression.OPERATOR_RIGHT_BRACE);
		}
		
		mTokenBuilder.append(operator);
		
		if (withBrace) {
			mTokenBuilder.append(Expression.OPERATOR_LEFT_BRACE);
		}
		mTokenBuilder.append(tstr);
		if (withBrace) {
			mTokenBuilder.append(Expression.OPERATOR_RIGHT_BRACE);
		}
		
		return this;
	}
	
	@Override
	public String toString() {
		if (mTokenBuilder == null) {
			return "";
		}
		
		return mTokenBuilder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof QueryToken == false) {
			return false;
		}
		
		QueryToken token = (QueryToken)o;
		
		String mstr = toString();
		String ostr = token.toString();
		if (mstr == null && ostr == null) {
			return true;
		} else if (mstr != null && ostr != null) {
			return mstr.equals(ostr);
		}
		
		
		return false; 
	}
	
}
