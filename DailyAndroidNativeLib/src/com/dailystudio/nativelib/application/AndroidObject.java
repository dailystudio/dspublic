package com.dailystudio.nativelib.application;

import android.content.Context;
import android.graphics.drawable.Drawable;

public abstract class AndroidObject implements IResourceObject {
	
	protected Drawable mIcon;
	protected CharSequence mLabel;
	
	private volatile boolean mResourcesResolved = false;
	
	private int mIconWidth;
	private int mIconHeight;
	
	public Drawable getIcon() {
		return mIcon;
	}
	
	@Override
	public void setIconDimension(int width, int height) {
		mIconWidth = width;
		mIconHeight = height;
	}

	@Override
	public int getIconWidth() {
		return mIconWidth;
	}
	
	@Override
	public int getIconHeight() {
		return mIconHeight;
	}
	
	public CharSequence getLabel() {
		return mLabel;
	}
	
	@Override
	public void resolveResources(Context context) {
		resolveLabelAndIcon(context);
		
		mResourcesResolved = true;
	}
	
	@Override
	public boolean isResourcesResolved() {
		return mResourcesResolved;
	}

	abstract public void resolveLabelAndIcon(Context context);

}
