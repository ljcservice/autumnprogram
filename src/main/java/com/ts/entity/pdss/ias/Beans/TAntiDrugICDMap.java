package com.ts.entity.pdss.ias.Beans;

/**
 * 药品和国际标准码对照表 
 * @author Administrator
 *
 */
public class TAntiDrugICDMap 
{
	private String SerialNo;
	private String DrugID;
	private String ICDCode;
	private String Memo;

	public String getSerialNo() 
	{
		return SerialNo;
	}

	public void setSerialNo(String serialNo) 
	{
		SerialNo = serialNo;
	}

	public String getDrugID() 
	{
		return DrugID;
	}

	public void setDrugID(String drugID) 
	{
		DrugID = drugID;
	}

	public String getICDCode() 
	{
		return ICDCode;
	}

	public void setICDCode(String iCDCode)
	{
		ICDCode = iCDCode;
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
