package com.hitzd.his.Scheduler;

import java.util.Map;

import com.hitzd.his.task.Task;

public interface IScheduler 
{
	// 任务开始执行
	public void performTask(Map<String, String> param, Task owner);
	// 上报任务状态
	public void reportStatus(Task owner);
}
