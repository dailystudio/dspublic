package com.dailystudio.database;

import java.util.List;

import com.dailystudio.development.Logger;

import android.content.Context;

@Deprecated
public abstract class DatabaseConnectivity {
	
	protected Context mContext;
	protected String mDatabase;
	
	public DatabaseConnectivity(Context context, String database) {
		mContext = context;
		mDatabase = database;
	}
	
	public String getDatabase() {
		return mDatabase;
	}

	abstract public long insert(DatabaseObject object);
	
	abstract public int insert(DatabaseObject[] objects);
	
	abstract public int update(Query query, DatabaseObject object);
	
	abstract public int delete(Query query);

	public List<DatabaseObject> query(Query query) {
		return query(query, null);
	}

	public abstract List<DatabaseObject> query(Query query,
			Class<? extends DatabaseObject> projectionClass);

	protected String projectionToString(String[] projection) {
		if (projection == null) {
			return null;
		}
		
		StringBuilder builder = new StringBuilder("column [ ");
		
		final int count = projection.length;
		for (int i = 0; i < count; i++) {
			builder.append(projection[i]);
			if (i != (count - 1)) {
				builder.append(", ");
			}
		}
		
		builder.append(" ]");
		
		return builder.toString();
	}
	
	protected String[] createProjection(
			Class<? extends DatabaseObject> projectionClass) {
		if (projectionClass == null) {
			return null;
		}
		
		DatabaseObjectFactory factory = DatabaseObjectFactory.getInstance();
		Logger.debug("factory: (%s)", factory);
		if (factory == null) {
			return null;
		}
		
		DatabaseObject prjObject = factory.createObject(projectionClass);
		Logger.debug("prjObject: (%s)", prjObject);
		if (prjObject == null) {
			return null;
		}
		
		final Template templ = prjObject.getTemplate();
		Logger.debug("templ: (%s)", templ);
		if (templ == null) {
			return null;
		}
		
		return templ.toSQLProjection();
	}
	
}
