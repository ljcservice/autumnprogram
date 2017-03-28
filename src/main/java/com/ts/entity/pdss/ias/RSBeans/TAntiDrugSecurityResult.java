package com.ts.entity.pdss.ias.RSBeans;

import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.pdss.RSBeans.TDrugAllergenRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugDosageRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSpecPeopleRslt;

public class TAntiDrugSecurityResult 
{
    /* 本地药品码 */
	private String drug_ID ;
	/* 本地药品名称 */
	private String drug_name;
	/* 主医嘱号 */
	private String order_No;
	/* 子医嘱号 */
	private String order_Sub_No;
	/* */
	private String drugStandID;
//	
	/*特殊人群*/
	private TDrugSpecPeopleRslt[]  dspList = null;
	/* 药物过敏审查结果对象 */
	private TDrugAllergenRslt[]    dagList = null;
	/* 剂量 */
	private TDrugDosageRslt[]      ddoList = null;
	
	public TDrugDosageRslt[] getDosageRslt()
	{
		return ddoList;
	}
	
	public void setDosageRslt(TDrugDosageRslt[] ddos)
	{
		this.ddoList = ddos;
	}
	
	public TDrugAllergenRslt[] getAllergenRslt()
	{
		return dagList;
	}
	
	public void setAllergenRslt(TDrugAllergenRslt[] dags)
	{
		this.dagList = dags;
	}
	
	public TDrugSpecPeopleRslt[] getDrugSpecPeopleRslt()
	{
		return dspList;
	}
	/**
	 * 
	 * @param specp
	 */
	public void setDspList(TDrugSpecPeopleRslt[] specp)
	{
		this.dspList = specp;
	}
	
	/** 
	 * 所有 抗菌药物审查结构 
	 */
	private List<TAntiDrugSecurityCheckResult> antiDrugSecurity = new ArrayList<TAntiDrugSecurityCheckResult>();
	
	public void addAntiDrugSecurity(TAntiDrugSecurityCheckResult[] aAnti)
	{
		for(TAntiDrugSecurityCheckResult x : aAnti)
		{
			addAntiDrugSecurity(x);
		}
	}
	
	public void addAntiDrugSecurity(TAntiDrugSecurityCheckResult aAnti)
	{
		this.drug_ID      = aAnti.getDrug_ID();
		this.order_No     = aAnti.getOrder_No();
		this.order_Sub_No = aAnti.getOrder_Sub_No();
		this.drugStandID  = aAnti.getDrugStandID();
		this.drug_name    = aAnti.getDrug_name();
		if(aAnti != null)
		{
			boolean flag = false;
			for(TAntiDrugSecurityCheckResult x : antiDrugSecurity)
			{
				if(x.getDrug_ID().equals(aAnti.getDrug_ID()) && x.getDrugStandID().equals(aAnti.getDrugStandID()) 
						&& x.getOrder_No().equals(aAnti.getOrder_No())&& x.getOrder_Sub_No().equals(aAnti.getOrder_Sub_No()))
				{
					for(TAntiDrugResult z : aAnti.getAntiDrugResult())
					{
						x.addAntiDrugResult(z);
					}
					flag = true;
				}
			}
			if(!flag)
			{
				antiDrugSecurity.add(aAnti);
			}
		}
	}
	
	public TAntiDrugSecurityCheckResult[] getTAntiDrugSecurity()
	{
		return (TAntiDrugSecurityCheckResult[])antiDrugSecurity.toArray(new TAntiDrugSecurityCheckResult[0]);
	}

	public String getDrug_ID()
	{
		return drug_ID;
	}

	public void setDrug_ID(String drug_ID) 
	{
		this.drug_ID = drug_ID;
	}

	public String getOrder_No()
	{
		return order_No;
	}

	public void setOrder_No(String order_No)
	{
		this.order_No = order_No;
	}

	public String getOrder_Sub_No() 
	{
		return order_Sub_No;
	}

	public void setOrder_Sub_No(String order_Sub_No)
	{
		this.order_Sub_No = order_Sub_No;
	}

	public String getDrugStandID() 
	{
		return drugStandID;
	}

	public void setDrugStandID(String drugStandID)
	{
		this.drugStandID = drugStandID;
	}

    public String getDrug_name()
    {
        return drug_name;
    }

    public void setDrug_name(String drug_name)
    {
        this.drug_name = drug_name;
    }
}
