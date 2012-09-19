package com.dailystudio.build;

import com.dailystudio.R;

import android.content.Context;
import android.content.res.Resources;

public class BuildSpec {

	private static Resources getResources(Context context) {
		if (context == null) {
			return null;
		}
	
		return context.getResources();
	}
	
	public static String getBuildManufacture(Context context) {
		Resources res = getResources(context);
		if (res == null) {
			return null;
		}
		
		return res.getString(R.string.build_manufacture);
	}
	
	public static String getBuildDevice(Context context) {
		Resources res = getResources(context);
		if (res == null) {
			return null;
		}
		
		return res.getString(R.string.build_device);
	}
	
	public static String getBuildPlatform(Context context) {
		Resources res = getResources(context);
		if (res == null) {
			return null;
		}
		
		return res.getString(R.string.build_platform);
	}
	
	public static boolean matchedBuildPlatform(Context context, String minBuildPlatform, String maxBuildPlatform) {
		String buildPlatform = getBuildPlatform(context);
		boolean ignoreMax = false;
		
		if (buildPlatform == null) {
			return false;
		}
		
		if (minBuildPlatform == null) {
			return false;
		}
		
		if (maxBuildPlatform == null) {
			ignoreMax = true;
		}
		
		float platform = -1.0f;
		float minPlatform = 0.0f;
		float maxPlatform = 0.0f;
		
		try {
			platform = Float.parseFloat(buildPlatform);
			minPlatform = Float.parseFloat(minBuildPlatform);
			if (ignoreMax == false) {
				maxPlatform = Float.parseFloat(maxBuildPlatform);
			}
		} catch (NumberFormatException e) {
			platform = -1.0f;
			minPlatform = 0.0f;
			maxPlatform = 0.0f;
		}
		
		if (ignoreMax) {
			return (platform >= minPlatform);
		}
		
		return (platform >= minPlatform && platform <= maxPlatform);
	}
	
	public static boolean matchedBuildPlatform(Context context, String minBuildPlatform) {
		return matchedBuildPlatform(context, minBuildPlatform, null);
	}
	
}
