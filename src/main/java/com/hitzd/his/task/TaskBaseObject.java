package com.hitzd.his.task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.hitzd.his.Scheduler.IScheduler;
import com.hitzd.his.Scheduler.SchedulerFactory;
import com.hitzd.his.Utils.DateUtils;

/**
 * 任务对象基类，任务对象包含一个IScheduler接口的子类对象，
 * 具体任务由子类对象完成，任务基类对象只负责记录子类对象的执行状态等信息
 * 
 * @author Administrator
 *
 */

public class TaskBaseObject 
{
	public final static String TaskRunning   = "Running";
	public final static String TaskPausing   = "Pausing";
	public final static String BeforeRunning = "BeforeRunning";
	public final static String AfterRunning  = "AfterRunning";
	public final static String TaskError     = "TaskError";
	
	public TaskBaseObject()
	{
		taskID = UUID.randomUUID().toString();
	}
	
	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	// 任务编号
	private String taskID             = "";
	// 任务开始时间
	private String startTime          = "";
	// 任务结束时间
	private String endTime            = "";
	// 任务相关的参数
	private Map<String, String> param = new HashMap<String, String>();
	// 任务当前状态
	private String status             = "";
	// 任务需要处理的数据总数量
	private int TotalCount            = 0;
	// 任务处理的当前数量
	private int CurCount              = 0;
	// 执行任务的具体类名称
	private String clazz              = "";
	// 任务当前状态
	private String taskStatus         = "";
	// 错误信息
	private String ErrorInfo          = "";
	// 任务标题
	private String taskTitle          = "";
	// 任务描述
	private String taskDesc           = "";
	// 任务当前状态描述
	private String taskStatusDesc     = "";
	
	public void taskOver()
	{
		setTaskStatus(AfterRunning);
	}
	
	public String getTaskStatusDesc() 
	{
		return taskStatusDesc;
	}

	public void setTaskStatusDesc(String taskStatusDesc) 
	{
		this.taskStatusDesc = taskStatusDesc;
	}

	public String getTaskTitle() 
	{
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) 
	{
		this.taskTitle = taskTitle;
	}

	public String getTaskDesc() 
	{
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) 
	{
		this.taskDesc = taskDesc;
	}

	public void runIt()
	{
		if ((clazz == null) && ("".equals(clazz)))
		{
			setTaskStatus(TaskError);
			setErrorInfo("no clazz be defined!");
		}
		
		IScheduler is = SchedulerFactory.getScheduler(clazz);
		if (is == null)
		{
			setTaskStatus(TaskError);
			setErrorInfo("clazz " + clazz + " can not be create!");
		}
		try
		{
			setStartTime(DateUtils.getDateTime());
			setTaskStatus(TaskRunning);
			is.performTask(param, (Task)this);
			setTaskStatus(AfterRunning);
			setEndTime(DateUtils.getDateTime());
		}
		catch (Exception ex)
		{
			setTaskStatus(TaskError);
			setErrorInfo("Task Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public String getStartTime() 
	{
		return startTime;
	}
	public void setStartTime(String startTime) 
	{
		this.startTime = startTime;
	}
	public String getEndTime() 
	{
		return endTime;
	}
	public void setEndTime(String endTime) 
	{
		this.endTime = endTime;
	}
	public Map<String, String> getParam() 
	{
		return param;
	}
	public String getParamValue(String key)
	{
		return param.get(key);
	}
	public void setParam(String key, String value) 
	{
		param.put(key, value);
	}
	public String getStatus() 
	{
		return status;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	public int getTotalCount() 
	{
		return TotalCount;
	}
	public void setTotalCount(int totalCount) 
	{
		TotalCount = totalCount;
	}
	public int getCurCount() 
	{
		return CurCount;
	}
	public void setCurCount(int curCount) 
	{
		CurCount = curCount;
	}
	public String getClazz() 
	{
		return clazz;
	}
	public void setClazz(String clazz) 
	{
		this.clazz = clazz;
	}
	public String getTaskStatus() 
	{
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) 
	{
		this.taskStatus = taskStatus;
	}
	public String getErrorInfo() 
	{
		return ErrorInfo;
	}
	public void setErrorInfo(String errorInfo) 
	{
		ErrorInfo = errorInfo;
	}
}