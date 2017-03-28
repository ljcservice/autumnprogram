package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;

/**
 * 过敏表DRUG_ALLERGEN
 * 过敏表已经和 ALLERG_INGR_DRUG 表  做成一个视图 此类现在不使用
 * @author liujc
 * 
 */
public class TDrugAllergen extends TBaseBean
{

	private static final long serialVersionUID = 1L;
	/* 过敏编码 */
    private String DRUG_ALLERGEN_ID;
    /* 输入码 */
    private String INPUT_CODE;
    /* 过敏名称 */
    private String DRUG_ALLERGEN_NAME;

    public String getDRUG_ALLERGEN_ID()
    {
        return DRUG_ALLERGEN_ID;
    }

    public void setDRUG_ALLERGEN_ID(String dRUG_ALLERGEN_ID)
    {
        DRUG_ALLERGEN_ID = dRUG_ALLERGEN_ID;
    }

    public String getINPUT_CODE()
    {
        return INPUT_CODE;
    }

    public void setINPUT_CODE(String iNPUT_CODE)
    {
        INPUT_CODE = iNPUT_CODE;
    }

    public String getDRUG_ALLERGEN_NAME()
    {
        return DRUG_ALLERGEN_NAME;
    }

    public void setDRUG_ALLERGEN_NAME(String dRUG_ALLERGEN_NAME)
    {
        DRUG_ALLERGEN_NAME = dRUG_ALLERGEN_NAME;
    }

}
