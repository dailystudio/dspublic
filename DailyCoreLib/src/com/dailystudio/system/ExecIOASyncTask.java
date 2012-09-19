package com.dailystudio.system;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import com.dailystudio.compat.CompatAsyncTask;

abstract class ExecIOASyncTask extends CompatAsyncTask<Void, Void, Void> {

	protected String mTag;
	protected volatile boolean mEndFlag = false;
	
	public ExecIOASyncTask(String tag) {
		mTag = tag;
	}
	
	public void start() {
		mEndFlag = false;

		execute((Void)null);
	}
	
	public void stop() {
		mEndFlag = true;
		
		cancel(true);
	}
	
	public void waitForStop() {
		try {
			get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (CancellationException e) {
			e.printStackTrace();
		}
	}
	
}
