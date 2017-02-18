package com.ts.entity.pdss.ias.Beans;

/**
 * 手术信息 
 * @author Administrator
 *
 */
public class TOperator 
{
	private String OperCode;
	private String OperName; 
	private String OperLevel;
	private String OperStartTime;
	private String OperEndTime;
	
	
	public String getOperCode()
	{
		return OperCode;
	}
	public void setOperCode(String operCode) 
	{
		OperCode = operCode;
	}
	public String getOperName()
	{
		return OperName;
	}
	public void setOperName(String operName)
	{
		OperName = operName;
	}
	public String getOperLevel() 
	{
		return OperLevel;
	}
	public void setOperLevel(String operLevel)
	{
		OperLevel = operLevel;
	}
	public String getOperStartTime()
	{
		return OperStartTime;
	}
	public void setOperStartTime(String operStartTime)
	{
		OperStartTime = operStartTime;
	}
	public String getOperEndTime()
	{
		return OperEndTime;
	}
	public void setOperEndTime(String operEndTime)
	{
		OperEndTime = operEndTime;
	}
}
