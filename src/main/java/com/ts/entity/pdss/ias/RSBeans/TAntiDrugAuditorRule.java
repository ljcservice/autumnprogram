package com.ts.entity.pdss.ias.RSBeans;

public class TAntiDrugAuditorRule
{
	/* 唯一标识 */
	private String ruleId;
	/* 药品代码 */
	private String drugCode;
	/* 药品名称 */
	private String drugName;
	/* 药品规格 */
	private String drugSpec;
	/* 药品剂型 */
	private String drugForm;
	/* 药品厂家 */
	private String drugFirm;
	/* 给药途径 */
	private String administration;
	/* 审核开关 */
	private String auditSwitch;
	/* 登记开关 */
	private String regSwitch;

	public String getRuleId()
	{
		return ruleId;
	}

	public void setRuleId(String ruleId)
	{
		this.ruleId = ruleId;
	}

	public String getDrugCode()
	{
		return drugCode;
	}

	public void setDrugCode(String drugCode)
	{
		this.drugCode = drugCode;
	}

	public String getDrugName()
	{
		return drugName;
	}

	public void setDrugName(String drugName)
	{
		this.drugName = drugName;
	}

	public String getDrugSpec()
	{
		return drugSpec;
	}

	public void setDrugSpec(String drugSpec)
	{
		this.drugSpec = drugSpec;
	}

	public String getDrugForm()
	{
		return drugForm;
	}

	public void setDrugForm(String drugForm)
	{
		this.drugForm = drugForm;
	}

	public String getDrugFirm()
	{
		return drugFirm;
	}

	public void setDrugFirm(String drugFirm)
	{
		this.drugFirm = drugFirm;
	}

	public String getAdministration()
	{
		return administration;
	}

	public void setAdministration(String administration)
	{
		this.administration = administration;
	}

	public String getAuditSwitch()
	{
		return auditSwitch;
	}

	public void setAuditSwitch(String auditSwitch)
	{
		this.auditSwitch = auditSwitch;
	}

	public String getRegSwitch()
	{
		return regSwitch;
	}

	public void setRegSwitch(String regSwitch)
	{
		this.regSwitch = regSwitch;
	}
}