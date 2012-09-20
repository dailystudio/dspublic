package com.dailystudio.build;

import com.dailystudio.test.ActivityTestCase;

public class BuildSpecTest extends ActivityTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private String getResourceValue(String resName) {
		assertNotNull(resName);
		int resid = mTargetContext.getResources().getIdentifier(
				resName, "string", "com.dailystudio.test");
		assertEquals(true, resid > 0);
		
		return mTargetContext.getString(resid);
	}
	
	public void testBuildPlatform() {
		String platform = BuildSpec.getBuildPlatform(mTargetContext);
		assertEquals(platform, getResourceValue("build_platform"));

		String device = BuildSpec.getBuildDevice(mTargetContext);
		assertEquals(device, getResourceValue("build_device"));

		String manufacture = BuildSpec.getBuildManufacture(mTargetContext);
		assertEquals(manufacture, getResourceValue("build_manufacture"));
	}

}
