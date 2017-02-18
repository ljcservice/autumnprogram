package com.ts.entity.pdss.ias.RSBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * 总数据集
 * 
 * @author Administrator
 * 
 */
public class TAntiDrugSecurityCheckResult
{
	/* 本地药品代码 */
	private String drug_ID;
	/* 标准药品码 */
	private String drugStandID;
	/* 药品名称 */
	private String Drug_name;
	/* 医嘱序号 */
	private String Order_No;
	/* 医嘱子序号 */
	private String Order_Sub_No;

	private List<TAntiDrugResult> results = new ArrayList<TAntiDrugResult>();
	
	public void addAntiDrugResult(TAntiDrugResult result)
	{
		results.add(result);
	}
	
	public TAntiDrugResult[] getAntiDrugResult()
	{
		return (TAntiDrugResult[])results.toArray(new TAntiDrugResult[0]);
	}
	
	public String getDrug_ID()
	{
		return drug_ID == null?"":this.drug_ID;
	}

	public void setDrug_ID(String drug_ID) 
	{
		this.drug_ID = drug_ID;
	}

	public String getDrugStandID() 
	{
		return drugStandID == null?"":this.drugStandID;
	}

	public void setDrugStandID(String drugStandID) 
	{
		this.drugStandID = drugStandID;
	}

	public String getOrder_No() 
	{
		return Order_No == null ? "" : this.Order_No;
	}

	public void setOrder_No(String order_No) 
	{
		Order_No = order_No;
	}

	public String getOrder_Sub_No() 
	{
		return Order_Sub_No == null ? "" : this.Order_Sub_No;
	}

	public void setOrder_Sub_No(String order_Sub_No) 
	{
		Order_Sub_No = order_Sub_No;
	}

    public String getDrug_name()
    {
        return Drug_name;
    }

    public void setDrug_name(String drug_name)
    {
        Drug_name = drug_name;
    }
}
