package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;

/**
 * 基础
 * @author Administrator
 *
 */
public class TBaseResult implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	/* 暂不使用*/
	protected int RedCount = 0;
	/* 暂不使用*/
	protected int YellowCount = 0;
    /* 医嘱序号 */
	protected String recMainNo  = "";
    /* 医嘱子序号 */
	protected String recSubNo   = "";
	/* 警示提醒 */
	protected String alertHint  = "";
	/* 警示说明 */
	protected String alertCause = "";
	/* 警示等级 */ 
	protected String alertLevel = "";
    /* 版本号 */
	protected int version = 2;
	
	public int getVersion()
    {
        return version;
    }

    protected void setVersion(int version)
    {
        this.version = version;
    }

	
    public String getRecMainNo()
    {
        return recMainNo;
    }

    public void setRecMainNo(String recMainNo)
    {
        if (recMainNo == null)
            recMainNo = "";
        this.recMainNo = recMainNo;
    }

    public String getRecSubNo()
    {
        return recSubNo;
    }

    public void setRecSubNo(String recSubNo)
    {
        if (recSubNo == null)
            recSubNo = "";
        this.recSubNo = recSubNo;
    }
	
	public String getAlertLevel() {
		return alertLevel;
	}
	public void setAlertLevel(String alertLevel) {
		this.alertLevel = alertLevel;
	}
	public int getRedCount() {
		return RedCount;
	}
	public void setRedCount(int redCount) {
		RedCount = redCount;
	}
	public int getYellowCount() {
		return YellowCount;
	}
	public void setYellowCount(int yellowCount) {
		YellowCount = yellowCount;
	}
	
	public String getResultInfo()
	{
		return "红灯：" + RedCount + "; 黄灯：" + YellowCount;
	}

	public String getAlertHint() 
	{
		return alertHint;
	}

	public void setAlertHint(String alertHint) 
	{
		this.alertHint = alertHint;
	}

	public String getAlertCause() 
	{
		return alertCause == null ? "" :alertCause;
	}

	public void setAlertCause(String alertCause)
	{
		this.alertCause = alertCause;
	}
}