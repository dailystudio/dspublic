package com.dailystudio.app.async;

import java.util.ArrayList;
import java.util.List;

import com.dailystudio.compat.CompatAsyncTask;
import com.dailystudio.development.Logger;

public class AsyncTasksQueueExecutor {
	
	public abstract class QueuedAsyncTask<Params, Progress, Result> 
		extends CompatAsyncTask<Params, Progress, Result> {
		
		private Object[] mExecuteParams;
		
		@Override
		protected void onPostExecute(Result result) {
			super.onPostExecute(result);
			
			mRunningTask = null;
			
			checkAndExecutePendingTask();
		}
		
		@SuppressWarnings("unchecked")
		public void execute() {
			execute((Params[])mExecuteParams);
		}

	}
	
	private List<QueuedAsyncTask<?, ?, ?>> mPendingTasks = 
			new ArrayList<QueuedAsyncTask<?, ?, ?>>();
	
	private volatile QueuedAsyncTask<?, ?, ?> mRunningTask = null; 
	
	public void pendingOrExecuteTask(QueuedAsyncTask<?, ?, ?> task) {
		pendingOrExecuteTask(task, (Object[])null);
	}
	
	public void pendingOrExecuteTask(QueuedAsyncTask<?, ?, ?> task, 
			Object... params) {
		if (task == null) {
			return;
		}
		
		task.mExecuteParams = params;

		if (mRunningTask == null) {
			mRunningTask = task;
				
			Logger.debug("No pending task, exec task: %s", task);
			task.execute();
				
			return;
		}

		synchronized (mPendingTasks) {
			task.mExecuteParams = params;
			
			mPendingTasks.add(task);
			Logger.debug("Pending task: %s", task);
		}
	}
	
	private void checkAndExecutePendingTask() {
		QueuedAsyncTask<?, ?, ?> task = null;
		
		synchronized (mPendingTasks) {
			if (mPendingTasks.size() <= 0) {
				return;
			}
			
			task = mPendingTasks.remove(0);
		}
		
		Logger.debug("Try to exec pending task: %s", task);
		
		mRunningTask = task;
		
		task.execute();
	}
	
}
