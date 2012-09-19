package com.dailystudio.database;

@Deprecated
public class Query {

	private Class<? extends DatabaseObject> mObjectClass;
	
	private ExpressionToken mSelection;
	private ExpressionToken mHaving;
	private ExpressionToken mLimit;
	private OrderingToken mGroupBy;
	private OrderingToken mOrderBy;
	
	public Query (Class<? extends DatabaseObject> klass) {
		mObjectClass = klass;
	}

	public Class<? extends DatabaseObject> getObjectClass() {
		return mObjectClass;
	}
	
	public void setSelection(ExpressionToken selToken) {
		mSelection = selToken;
	}
	
	public ExpressionToken getSelection() {
		return mSelection;
	}

	public void setGroupBy(OrderingToken groupByToken) {
		mGroupBy = groupByToken;
	}
	
	public OrderingToken getGroupBy() {
		return mGroupBy;
	}

	public void setHaving(ExpressionToken havingToken) {
		mHaving = havingToken;
	}
	
	public ExpressionToken getHaving() {
		return mHaving;
	}

	public void setOrderBy(OrderingToken orderByToken) {
		mOrderBy = orderByToken;
	}
	
	public OrderingToken getOrderBy() {
		return mOrderBy;
	}

	public void setLimit(ExpressionToken limitToken) {
		mLimit = limitToken;
	}
	
	public ExpressionToken getLimit() {
		return mLimit;
	}
	
}
