package com.hitzd.his.Beans;

import java.io.Serializable;

/**
 * @description 医嘱药物过敏信息类：PatOrderDrugSensitive 对应数据库表：医嘱药物过敏信息(PAT_ORDER_DRUG_SENSITIVE)
 * @author
 */
public class TPatOrderDrugSensitive implements Serializable{
	
    private static final long serialVersionUID = 1L;
    /* 药物过敏记录ID*/
	private String  patOrderDrugSensitiveID;

	/* 药敏信息代码 */
	private String  drugAllergenID;
	/* 过敏源*/
	//private String sensitiveSource;

	
    public String getPatOrderDrugSensitiveID()
    {
        return patOrderDrugSensitiveID;
    }

    public void setPatOrderDrugSensitiveID(String patOrderDrugSensitiveID)
    {
        this.patOrderDrugSensitiveID = patOrderDrugSensitiveID;
    }

    public String getDrugAllergenID()
    {
        return drugAllergenID;
    }

    public void setDrugAllergenID(String drugAllergenID)
    {
        this.drugAllergenID = drugAllergenID;
    }
//	public String getSensitiveSource() {
//		return sensitiveSource;
//	}
//	public void setSensitiveSource(String sensitiveSource) {
//		this.sensitiveSource = sensitiveSource;
//	}

    @Override
    public String toString()
    {
        return "TPatOrderDrugSensitive [patOrderDrugSensitiveID="
                + patOrderDrugSensitiveID + ", drugAllergenID=" + drugAllergenID
                + "]";
    }
}