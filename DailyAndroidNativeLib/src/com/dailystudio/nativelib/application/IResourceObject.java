package com.dailystudio.nativelib.application;

import java.util.Comparator;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface IResourceObject {
	
	public static class ResourceObjectComparator implements Comparator<IResourceObject> {

		@Override
		public int compare(IResourceObject object1, IResourceObject object2) {
			if (object1 == null) {
				return -1;
			} else if (object2 == null) {
				return 1;
			}
			
			final CharSequence label1 = object1.getLabel();
			final CharSequence label2 = object2.getLabel();
			
			if (label1 == null) {
				return -1;
			} else if (label2 == null) {
				return 1;
			}
			
			return label1.toString().compareTo(label2.toString());
		}
		
	}
	
	public Drawable getIcon();
	public CharSequence getLabel();
	
	public void setIconDimension(int width, int height);
	public int getIconWidth();
	public int getIconHeight();
	
	public void resolveResources(Context context);
	
	public boolean isResourcesResolved();
	
}
