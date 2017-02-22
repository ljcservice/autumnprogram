package com.hitzd.his.Beans;

import java.io.Serializable;

/**
 * @description 病人住院信息类：PatOrderVisitInfo 对应数据库表：病人住院信息(PAT_ORDER_VISITINFO)
 * @author
 */
public class TPatOrderVisitInfo implements Serializable
{

    private static final long serialVersionUID = 1L;
    /* 病人本次住院标识 HIS系统ID*/
    private String visitID;
    /* 病人ID*/
    private String patientID;
    /* 入院科室*/
    private String inDept;
    /* 入院方式 使用代码，门诊、急诊、转入等*/
    private String inMode;
    /* 入院日期*/
    private String inDate;
    /* 住院目的*/
    // private String inCause;
    /* 出院科室*/
    private String outDept;
    /* 出院日期及时间*/
    // private String outDate;
    /* 出院方式*/
    // private String outMode;
    /* 接诊日期*/
    // private String consultingDate;
    /* 入院病情 使用代码, 危、急、一般*/
    private String patAdmCondition;
    /* 主治医师 */
    private String mainDoctor;
    /* 经治医师 */
    private String otherDoctor;
    /* 门诊医师*/
    private String consultingDoctor;
    public String getVisitID()
    {
        return visitID;
    }

    public void setVisitID(String visitID)
    {
        this.visitID = visitID;
    }

    public String getPatientID()
    {
        return patientID;
    }

    public void setPatientID(String patientID)
    {
        this.patientID = patientID;
    }

    public String getInDept()
    {
        return inDept;
    }

    public void setInDept(String inDept)
    {
        this.inDept = inDept;
    }

    public String getInMode()
    {
        return inMode;
    }

    public void setInMode(String inMode)
    {
        this.inMode = inMode;
    }

    public String getInDate()
    {
        return inDate;
    }

    public void setInDate(String inDate)
    {
        this.inDate = inDate;
    }

    // public String getInCause() {
    // return inCause;
    // }
    // public void setInCause(String inCause) {
    // this.inCause = inCause;
    // }
    public String getOutDept()
    {
        return outDept;
    }

    public void setOutDept(String outDept)
    {
        this.outDept = outDept;
    }

    // public String getOutDate() {
    // return outDate;
    // }
    // public void setOutDate(String outDate) {
    // this.outDate = outDate;
    // }
    // public String getOutMode() {
    // return outMode;
    // }
    // public void setOutMode(String outMode) {
    // this.outMode = outMode;
    // }
    // public String getConsultingDate() {
    // return consultingDate;
    // }
    // public void setConsultingDate(String consultingDate) {
    // this.consultingDate = consultingDate;
    // }
    public String getPatAdmCondition()
    {
        return patAdmCondition;
    }

    public void setPatAdmCondition(String patAdmCondition)
    {
        this.patAdmCondition = patAdmCondition;
    }

	public String getMainDoctor() 
	{
		return mainDoctor;
	}

	public void setMainDoctor(String mainDoctor) 
	{
		this.mainDoctor = mainDoctor;
	}

	public String getOtherDoctor()
	{
		return otherDoctor;
	}

	public void setOtherDoctor(String otherDoctor) 
	{
		this.otherDoctor = otherDoctor;
	}

	public String getConsultingDoctor()
	{
		return consultingDoctor;
	}

	public void setConsultingDoctor(String consultingDoctor) 
	{
		this.consultingDoctor = consultingDoctor;
	}

}
