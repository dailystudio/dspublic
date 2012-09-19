package com.dailystudio.database;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

@Deprecated
public class TemplateInflater {

	private final static String TAG_COLUMN = "column";
	private final static String TAG_TEMPLATE = "template";

    private static final String ATTR_NAME = "name";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_PRIMARY = "primary";
    private static final String ATTR_ALLOW_NULL = "allowNull";
    
	protected Context mContext;
	
	public TemplateInflater(Context context) {
		mContext = context;
	}
	
	public Template inflateTemplate(int resid) {
		List<Template> templates = inflateTemplates(resid);
		if (templates == null || templates.size() <= 0) {
			return null;
		}
		
		return templates.get(0);
	}
	
	public List<Template> inflateTemplates(int resid) {
		if (resid <= 0) {
			return null;
		}
		
		XmlResourceParser parser = mContext.getResources().getXml(resid);
		if (parser == null) {
			return null;
		}

		return inflateTemplates(parser);
	}
	
	public List<Template> inflateTemplates(XmlResourceParser parser) {
		if (parser == null) {
			return null;
		}
		 
		Template template = null;
		List<Template> templs = null;
		 
		try {
			final int depth = parser.getDepth();

			int type;
			String name = null;
		        
			while (((type = parser.next()) != XmlPullParser.END_TAG ||
					parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
		
				if (type != XmlPullParser.START_TAG) {
					continue;
				}
		            
				name = parser.getName();
				if (name != null) {
					name = name.toLowerCase();
				}	
		         
				if (TAG_TEMPLATE.equals(name)) {
					template = createTemplate(parser);
					if (templs == null) {
						templs = new ArrayList<Template>();
					}
					 
					if (templs != null) {
						templs.add(template);
					}
				} else if (TAG_COLUMN.equals(name)) {
					Column column = createColumn(parser);
					if (column != null && template != null) {
						template.addColumn(column);
					}
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		    
		return templs;
	}

	public List<Column> inflateColumns(XmlResourceParser parser) {
		if (parser == null) {
			return null;
		}
		 
		Column column = null;
		List<Column> columns = null;
		 
		try {
			final int depth = parser.getDepth();
			
			int type;
			String name = null;
		        
			while (((type = parser.next()) != XmlPullParser.END_TAG ||
					parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
		
				if (type != XmlPullParser.START_TAG) {
					continue;
				}
		            
				name = parser.getName();
				if (name != null) {
					name = name.toLowerCase();
				}
		            
				if (TAG_COLUMN.equals(name)) {
					column = createColumn(parser);
					if (columns == null) {
						columns = new ArrayList<Column>();
					}
					 
					if (column != null && columns != null) {
						columns.add(column);
					}
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		    
		return columns;
	}
	 
	private Template createTemplate(XmlResourceParser parser) {
		return new Template();
	}
	
//	private Template createTemplate(XmlResourceParser parser) {
//		if (parser == null) {
//			return null;
//		}
//		 
//		String attrName = null;
//		int attrCount;
//		int i;
//	        
//		String attrTable = null;
//	        
//		attrCount = parser.getAttributeCount();
//		 
//		for (i = 0; i < attrCount; i++) {
//			attrName = parser.getAttributeName(i);
//			if (ATTR_TABLE.equals(attrName)) {
//				attrTable = parser.getAttributeValue(i);
//			}		 
//		}
//		 
//		Logger.debug("attrTable(%s)", attrTable);
//				 
//		Template templ = null;
//		if (attrTable != null) {
//			templ = new Template();
//			if (templ != null) {
//				templ.setTable(attrTable);
//			}
//		}
//		 
//		return templ;
//	}
	 
	private Column createColumn(XmlResourceParser parser) {
		if (parser == null) {
			return null;
		}
		 
		String attrName = null;
		int attrCount;
		int i;
	        
		String attrColName = null;
		String attrColType = null;
		String attrColNotNull = "true";
		String attrColPrimary = "false";
		 
		attrCount = parser.getAttributeCount();
		 
		for (i = 0; i < attrCount; i++) {
			attrName = parser.getAttributeName(i);
			if (ATTR_NAME.equals(attrName)) {
				attrColName = parser.getAttributeValue(i);
			} else if (ATTR_TYPE.equals(attrName)) {
				attrColType = parser.getAttributeValue(i);
			} else if (ATTR_ALLOW_NULL.equals(attrName)) {
				attrColNotNull = parser.getAttributeValue(i);
			} else if (ATTR_PRIMARY.equals(attrName)) {
				attrColPrimary = parser.getAttributeValue(i);
			}
		}
		 
		attrColType = Column.getNormalizedColumnType(attrColType);
				 
		Column column = createColumn(attrColName, attrColType, 
					Boolean.parseBoolean(attrColNotNull),
					Boolean.parseBoolean(attrColPrimary));
/*		Logger.debug("attrColName(%s), attrColType(%s), attrColNotNull(%s), attrColPrimary(%s) -> column(%s)",
				attrColName, attrColType, attrColNotNull, attrColPrimary,
				column.getClass().getSimpleName());
*/		 
		return column;
	}
	
	private Column createColumn(String colName, String colType, boolean allowNull, boolean isPrimary) {
		if (colName == null || colType == null) {
			return null;
		}
		
		Class<?> klass = convertToClass(colType);
		if (klass == null) {
			return null;
		}
		
		Constructor<?> constructor = null;
		
		try {
			constructor = klass.getConstructor(String.class, boolean.class, boolean.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			
			constructor = null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			
			constructor = null;
		} 

		if (constructor == null) {
			return null;
		}
		
		constructor.setAccessible(true);
		
		Object object = null;
		try {
			object = constructor.newInstance(new Object[] { colName, allowNull, isPrimary });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			
			object = null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			
			object = null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
			object = null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			
			object = null;
		}

		if (object instanceof Column == false) {
			return null;
		}
		
		return (Column)object;
	}
	
	private Class<?> convertToClass(String colType) {
		if (colType == null) {
			return null;
		}
		
		String pkgName = getPackageName(Column.class);
		if (pkgName == null) {
			return null;
		}
		
		String prefix = toUpperCaseFirstOnly(colType.toLowerCase());
		if (prefix == null) {
			return null;
		}
		
		Class<?> klass = null;
		try {
			klass = Class.forName(String.format("%s.%sColumn", 
					pkgName, prefix));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
			klass = null;
		}
		
		return klass;
	}
	
	private String getPackageName(Class<?> klass) {
    	if (klass == null) {
    		return null;
    	}
    	
    	String pkgName = null;
    	
    	pkgName = klass.getName().toString().replace(
    			String.format(".%s", klass.getSimpleName()), "");
    	
    	return pkgName;
	}
	
	private String toUpperCaseFirstOnly(String str) {
		if (str == null) {
			return null;
		}
		
		if(Character.isUpperCase(str.charAt(0))) {
			return str;  
		}
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(Character.toUpperCase(str.charAt(0)));
		builder.append(str.substring(1));
		
		return builder.toString();
	}  
	 
}
