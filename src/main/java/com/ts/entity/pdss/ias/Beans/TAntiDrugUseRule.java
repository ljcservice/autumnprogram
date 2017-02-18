package com.ts.entity.pdss.ias.Beans;

public class TAntiDrugUseRule {
	private String SERIAL_NO;
	private String Drug_ID;
	private String Use_Dept;
	private String Use_Level;
	private String Auditor;
	private String Audit_Date;
	private String Audit_Res;
	private String Memo;

	public String getSERIAL_NO()
	{
		return SERIAL_NO;
	}

	public void setSERIAL_NO(String sERIAL_NO)
	{
		SERIAL_NO = sERIAL_NO;
	}

	public String getDrug_ID() 
	{
		return Drug_ID;
	}

	public void setDrug_ID(String drug_ID)
	{
		Drug_ID = drug_ID;
	}

	public String getUse_Dept()
	{
		return Use_Dept;
	}

	public void setUse_Dept(String use_Dept) 
	{
		Use_Dept = use_Dept;
	}

	public String getUse_Level() 
	{
		return Use_Level;
	}

	public void setUse_Level(String use_Level) 
	{
		Use_Level = use_Level;
	}

	public String getAuditor() 
	{
		return Auditor;
	}

	public void setAuditor(String auditor) 
	{
		Auditor = auditor;
	}

	public String getAudit_Date() 
	{
		return Audit_Date;
	}

	public void setAudit_Date(String audit_Date)
	{
		Audit_Date = audit_Date;
	}

	public String getAudit_Res() 
	{
		return Audit_Res;
	}

	public void setAudit_Res(String audit_Res) 
	{
		Audit_Res = audit_Res;
	}

	public String getMemo()
	{
		return Memo;
	}

	public void setMemo(String memo) 
	{
		Memo = memo;
	}

}
