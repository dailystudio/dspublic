package com.dailystudio.app.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActivityLauncher {
	
	public static interface OnExceptionHandler {
		
		public void onException(Intent intent, Exception e);
		
	}
	
	private static OnExceptionHandler sOnExceptionHandler = new OnExceptionHandler() {
		
		@Override
		public void onException(Intent intent, Exception e) {
			Log.w(ActivityLauncher.class.getSimpleName(), 
					String.format("onException():%s", e));
		}
		
	};
	
	public static void launchActivity(Context context, Intent intent, OnExceptionHandler oec) {
		if (context != null && intent != null) {
			/*
			 * XXX: Application should take care of line below
			 * 		by themselves
			 */
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			try {
				context.startActivity(intent);
			} catch (ActivityNotFoundException e) {
				if (oec != null) {
					oec.onException(intent, e);
				}
			} catch (SecurityException e) {
				if (oec != null) {
					oec.onException(intent, e);
				}
			}
		}
	}

	public static void launchActivity(Context context, Intent intent) {
		launchActivity(context, intent, sOnExceptionHandler);
	}
	
	public static void launchActivityForResult(Activity activity, Intent intent, int requestCode, OnExceptionHandler oec) {
		if (activity != null && intent != null) {
			try {
				activity.startActivityForResult(intent, requestCode);
			} catch (ActivityNotFoundException e) {
				if (oec != null) {
					oec.onException(intent, e);
				}
			} catch (SecurityException e) {
				if (oec != null) {
					oec.onException(intent, e);
				}
			}
		}
	}
	
	public static void launchActivityForResult(Activity activity, Intent intent, int requestCode) {
		launchActivityForResult(activity, intent, requestCode, sOnExceptionHandler);
	}

	
}
