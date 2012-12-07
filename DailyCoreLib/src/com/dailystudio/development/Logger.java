package com.dailystudio.development;

import android.util.Log;

public class Logger {
	
	private static enum LogToken {
		LOG_D,
		LOG_W,
		LOG_I,
		LOG_E,
	}
	
	private static final String UNKNOWN_METHOD = "UnknownMethod";
	private static final String UNKNOWN_CLASS = "UnknownClass";
	private static final int TRACE_BASE_INDEX = 3;
	
	private static final String DEBUG_MSG_TEMPL = "%s(): %s";
	
	private static volatile boolean sLogDebugEnabled = true;
	
	private static void output(String format, LogToken token, Object... args) {
		final String compose = String.format(DEBUG_MSG_TEMPL, 
				getCallingMethodName(2), format);
				
		Log.d(getCallingSimpleClassName(2), String.format(compose, args));
	}
	
	public static void setDebugEnabled(boolean enabled) {
		sLogDebugEnabled = enabled;
	}
	
	public static boolean isDebugEnabled() {
		return sLogDebugEnabled;
	}
	
	public static void info(String format, Object... args) {
		output(format, LogToken.LOG_I, args);
	}

	public static void debug(String format, Object... args) {
		if (sLogDebugEnabled) {
			output(format, LogToken.LOG_D, args);
		}
	}
	
	public static void warnning(String format, Object... args) {
		output(format, LogToken.LOG_W, args);
	}
	
	public static void error(String format, Object... args) {
		output(format, LogToken.LOG_E, args);
	}
	
	public static StackTraceElement getCallingElement(int traceLevel) {
		if (traceLevel < 0) {
			return null;
		}
		
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements == null) {
			return null;
		}

//		dumpStackTraceElements(elements);
		
		final int length = elements.length;
		if ((TRACE_BASE_INDEX + traceLevel) >= length) {
			return null;
		}
		
		
		return elements[TRACE_BASE_INDEX + traceLevel];
	}
	
/*	private static void dumpStackTraceElements(StackTraceElement[] elements) {
		if (elements == null) {
			return;
		}
		
		final int length = elements.length;
		for (int i = 0; i < length; i++) {
			Log.d(Logger.class.getSimpleName(),
					String.format("dumpStackTraceElements(): [%03d]: element[%s]",
							i, elements[i]));
		}
	}
*/
	public static String getCallingMethodName() {
		return getCallingMethodName(1);
	}
	
	public static String getCallingMethodName(int traceLevel) {
		StackTraceElement element = getCallingElement(traceLevel + 1);
		if (element == null) {
			return UNKNOWN_METHOD;
		}
		
		return element.getMethodName();
	}
	
	public static String getCallingClassName() {
		return getCallingClassName(1);
	}

	public static String getCallingSimpleClassName() {
		return getCallingSimpleClassName(1);
	}
	
	public static String getCallingSimpleClassName(int traceLevel) {
		String className = getCallingClassName(traceLevel + 1);
		if (className == null) {
			return UNKNOWN_CLASS;
		}
		
		Class<?> kls = null;
		try {
			kls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			kls = null;
		}
		
		if (kls == null) {
			return UNKNOWN_CLASS;
		}
		
		return kls.getSimpleName();
	}


	public static String getCallingClassName(int traceLevel) {
		StackTraceElement element = getCallingElement(traceLevel + 1);
		if (element == null) {
			return UNKNOWN_CLASS;
		}
		
		return element.getClassName();
	}
	
}
