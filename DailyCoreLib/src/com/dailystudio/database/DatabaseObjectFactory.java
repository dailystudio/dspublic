package com.dailystudio.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;

import com.dailystudio.factory.Factory;

@Deprecated
public class DatabaseObjectFactory extends Factory<DatabaseObject, Class<? extends DatabaseObject>> {

	public synchronized static final DatabaseObjectFactory getInstance() {
		return Factory.getInstance(DatabaseObjectFactory.class);
	}
	
	public static final DatabaseObject createDatabaseObject(Class<? extends DatabaseObject> params) {
		DatabaseObjectFactory factory = DatabaseObjectFactory.getInstance();
		if (factory == null) {
			return null;
		}
		
		return factory.createObject(params);
	}
	
	public static final DatabaseObject createDatabaseObject() {
		return createDatabaseObject(null);
	}
	
	@Override
	protected void initMembers() {
		super.initMembers();
	}
	
	@Override
	protected DatabaseObject newObject(Class<? extends DatabaseObject> klass) {
		if (klass == null) {
			klass = DatabaseObject.class;
		}
		
//		Logger.debug("klass(%s)", klass.getName());
		
		Object object = null;
		Constructor<?> constructor = null;
			
		try {
			constructor = klass.getConstructor(Context.class);
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//				
			constructor = null;
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//				
			constructor = null;
		} 
			
//		Logger.debug("constructor(%s)", constructor);

		try {
			if (constructor == null) {
				object = klass.newInstance();
			} else {
				constructor.setAccessible(true);
				
				final Context context = getContext();
				if (context != null) {
					object = constructor.newInstance(new Object[] { context });
				}
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			
			object = null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			
			object = null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
			object = null;
		}
		
		if (object instanceof DatabaseObject == false) {
			return null;
		}

		DatabaseObject dObject = (DatabaseObject)object;
//		Logger.debug("klass(%s), dObject(%s)", klass, dObject);
		
		return dObject;
	}
	
}
