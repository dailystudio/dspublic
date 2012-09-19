package com.dailystudio.nativelib.application;

import android.content.ComponentName;
import android.content.pm.ResolveInfo;

public abstract class AndroidComponentObject extends AndroidObject {
	
	public static class ComponentComparator extends ResourceObjectComparator {

		@Override
		public int compare(IResourceObject object1, IResourceObject object2) {
			int ret = super.compare(object1, object2);
			if (ret != 0) {
				return ret;
			}
			
			if (object1 instanceof AndroidComponentObject == false) {
				return -1;
			} else if (object2 instanceof AndroidComponentObject == false) {
				return 1;
			}
			
			final AndroidComponentObject comp1 = (AndroidComponentObject)object1;
			final AndroidComponentObject comp2 = (AndroidComponentObject)object2;
			
			if (comp1.mComponentName == null) {
				return -1;
			} else if (comp1.mComponentName == null) {
				return 1;
			}
			
			final String compName1 = comp1.mComponentName.getShortClassName();
			final String compName2 = comp2.mComponentName.getShortClassName();
			
			return compName1.compareTo(compName2);
		}
		
	}	
	
	private ComponentName mComponentName = null;
	
	public AndroidComponentObject(ResolveInfo rInfo) {
		mComponentName = extractComponent(rInfo);
	}
	
	public AndroidComponentObject(ComponentName compName) {
		mComponentName = compName;
	}
	
	public ComponentName getComponentName() {
		return mComponentName;
	}
	
	@Override
	public CharSequence getLabel() {
		CharSequence label = super.getLabel();
		if (label != null) {
			return label;
		}
		
		if (mComponentName == null) {
			return null;
		}
		
		return mComponentName.flattenToString();
	}
	
	abstract protected ComponentName extractComponent(ResolveInfo rInfo);

}
