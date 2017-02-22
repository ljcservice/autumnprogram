package com.hitzd.his.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务管理器，每次任务执行时，都要将task实例放进任务管理器里，
 * 以便于随时查看任务状态
 * @author Administrator
 *
 */
public class TaskManager 
{
	private static List<Task> list = new ArrayList<Task>();
	
	public static int getTaskCount()
	{
		return list.size();
	}
	
	public static Task getTask(int index)
	{
		if ((index >= 0) && (index < list.size()))
			return list.get(index);
		else
			return null;
	}
	
	public synchronized static int putTask(Task task)
	{
		list.add(task);
		return list.size() - 1;
	}
	
	public static void startTask(int index)
	{
		Task task = getTask(index);
		if (task != null)
			task.runIt();
		task.taskOver();
	}
	
	public static void deleteTask(int index)
	{
		if ((index >= 0) && (index < list.size()))
		{
			@SuppressWarnings("unused")
			Task task = getTask(index);
			list.remove(index);
			task = null;
		}
	}
}