package com.ts.entity.pdss.ias.RSBeans;

public class TAntiDrugAuditorRuleDetail
{
	/* 外键 */
	private String ruleId;
	/* 科室代码 */
	private String deptCode;
	/* 科室名称 */
	private String deptName;
	/* 医生代码 */
	private String doctorCode;
	/* 医生姓名 */
	private String doctorName;
	/* 职称代码 */
	private String doctorTitle;
	/* 职称描述 */
	private String titleDesc;

	public String getRuleId()
	{
		return ruleId;
	}

	public void setRuleId(String ruleId)
	{
		this.ruleId = ruleId;
	}

	public String getDeptCode()
	{
		return deptCode;
	}

	public void setDeptCode(String deptCode)
	{
		this.deptCode = deptCode;
	}

	public String getDeptName()
	{
		return deptName;
	}

	public void setDeptName(String deptName)
	{
		this.deptName = deptName;
	}

	public String getDoctorCode()
	{
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode)
	{
		this.doctorCode = doctorCode;
	}

	public String getDoctorName()
	{
		return doctorName;
	}

	public void setDoctorName(String doctorName)
	{
		this.doctorName = doctorName;
	}

	public String getDoctorTitle()
	{
		return doctorTitle;
	}

	public void setDoctorTitle(String doctorTitle)
	{
		this.doctorTitle = doctorTitle;
	}

	public String getTitleDesc()
	{
		return titleDesc;
	}

	public void setTitleDesc(String titleDesc)
	{
		this.titleDesc = titleDesc;
	}
}