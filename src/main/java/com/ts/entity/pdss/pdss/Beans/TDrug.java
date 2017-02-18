package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.Beans.TBaseBean;

/**
 * 药品字典
 * 
 * @author liujc
 * 
 */
public class TDrug extends TBaseBean
{
    private static final long serialVersionUID = 1L;

    /* ID */
    private String DRUG_ID;
    /* 药品名称 */
    private String DRUG_NAME;
    /* 药品类码 */
    private String DRUG_CLASS_ID;
    /* 剂型码 */
    private String DOSE_CLASS_ID;
    /* 规格 */
    private String DRUG_SPEC;
    /* 生产厂家 */
    private String FIRM_ID;
    /* 单位 */
    private String UNITS;
    /* 剂型 */
    private String DRUG_FORM;
    /* 毒理分类 */
    private String TOXI_PROPERTY;
    /* 最小单位剂量 */
    private String DOSE_PER_UNIT;
    /* 剂量单位 */
    private String DOSE_UNITS;
    /* 成分码 */
    private String INGR_CLASS_IDS;
    /* 输液码 */
    private String IV_CLASS_CODE;
    /* 药品类别标志 */
    private String DRUG_INDICATOR;
    /* 输入码 */
    private String INPUT_CODE;
    /* 本地ID */
    private String DRUG_NO_LOCAL;
    /* 本地药品名称 */
    private String DRUG_NAME_LOCAL;
    /* 操作人 */
    private String OPER_USER;
    /* 操作时间*/
    private String OPER_TIME;
    /* 操作方式 */
    private String OPER_TYPE;
    /* 医嘱序号 */
	private String recMainNo  = "";
    /* 医嘱子序号 */
	private String recSubNo   = "";
	/* 药敏代码 */
	private String SENSIT_CODE = "";
	
	private boolean isAnti    = false;     

	private String UseType    = "";
	
	/* 说明书代码 */
	private String direct_no   = null;
	
	public String getDirect_no()
	{
		return direct_no;
	}

	public void setDirect_no(String direct_no)
	{
		this.direct_no = direct_no;
	}

	public TDrug() {}
	
	public TDrug(TDrug drugInput)
	{
		if( drugInput != null)
		{
			this.DRUG_ID         = drugInput.DRUG_ID;
			this.DRUG_NAME       = drugInput.DRUG_NAME;
			this.DRUG_CLASS_ID   = drugInput.DRUG_CLASS_ID;
			this.DOSE_CLASS_ID   = drugInput.DOSE_CLASS_ID;
			this.DRUG_SPEC       = drugInput.DRUG_SPEC;
			this.FIRM_ID         = drugInput.FIRM_ID;
			this.UNITS           = drugInput.UNITS;
			this.DRUG_FORM       = drugInput.DRUG_FORM;
			this.TOXI_PROPERTY   = drugInput.TOXI_PROPERTY;
			this.DOSE_PER_UNIT   = drugInput.DOSE_PER_UNIT;
			this.DOSE_UNITS      = drugInput.DOSE_UNITS;
			this.INGR_CLASS_IDS  = drugInput.INGR_CLASS_IDS;
			this.IV_CLASS_CODE   = drugInput.IV_CLASS_CODE;
			this.DRUG_INDICATOR  = drugInput.DRUG_INDICATOR;
			this.INPUT_CODE      = drugInput.INPUT_CODE;
			this.DRUG_NO_LOCAL   = drugInput.DRUG_NO_LOCAL;
			this.DRUG_NAME_LOCAL = drugInput.DRUG_NAME_LOCAL;
			this.OPER_USER       = drugInput.OPER_USER;
			this.OPER_TIME       = drugInput.OPER_TIME;
			this.OPER_TYPE       = drugInput.OPER_TYPE;
			this.recMainNo       = drugInput.recMainNo;
			this.recSubNo        = drugInput.recSubNo;
			this.isAnti          = drugInput.isAnti;
			this.SENSIT_CODE     = drugInput.SENSIT_CODE;
			this.UseType         = drugInput.UseType;
			this.direct_no       = drugInput.direct_no;
		}
	}
	
    public String getUseType()
    {
        return UseType;
    }

    public void setUseType(String useType)
    {
        UseType = useType;
    }

    public String getSENSIT_CODE()
    {
		return SENSIT_CODE;
	}

	public void setSENSIT_CODE(String sENSIT_CODE) 
	{
		SENSIT_CODE = sENSIT_CODE;
	}

	public boolean isAnti()
    {
		return isAnti;
	}

	public String getRecMainNo()
    {
		return recMainNo;
	}

	public String getRecSubNo()
	{
		return recSubNo;
	}

	public void setRecMainNo(String recMainNo) 
	{
		this.recMainNo = recMainNo;
	}

	public void setRecSubNo(String recSubNo) 
	{
		this.recSubNo = recSubNo;
	}

	public String getDRUG_ID()
    {
        return DRUG_ID != null ? DRUG_ID : "";
    }

    public void setDRUG_ID(String dRUG_ID)
    {
        DRUG_ID = dRUG_ID;
    }

    public String getDRUG_NAME()
    {
        return DRUG_NAME;
    }

    public void setDRUG_NAME(String dRUG_NAME)
    {
        DRUG_NAME = dRUG_NAME;
    }

    public String getDRUG_CLASS_ID()
    {
        return DRUG_CLASS_ID == null ?"":DRUG_CLASS_ID;
    }

    public void setDRUG_CLASS_ID(String dRUG_CLASS_ID)
    {
        DRUG_CLASS_ID = dRUG_CLASS_ID;
    }

    public String getDOSE_CLASS_ID()
    {
        return DOSE_CLASS_ID;
    }

    public void setDOSE_CLASS_ID(String dOSE_CLASS_ID)
    {
        DOSE_CLASS_ID = dOSE_CLASS_ID;
    }

    public String getDRUG_SPEC()
    {
        return DRUG_SPEC;
    }

    public void setDRUG_SPEC(String dRUG_SPEC)
    {
        DRUG_SPEC = dRUG_SPEC;
    }

    public String getFIRM_ID()
    {
        return FIRM_ID;
    }

    public void setFIRM_ID(String fIRM_ID)
    {
        FIRM_ID = fIRM_ID;
    }

    public String getUNITS()
    {
        return UNITS;
    }

    public void setUNITS(String uNITS)
    {
        UNITS = uNITS;
    }

    public String getDRUG_FORM()
    {
        return DRUG_FORM;
    }

    public void setDRUG_FORM(String dRUG_FORM)
    {
        DRUG_FORM = dRUG_FORM;
    }

    public String getTOXI_PROPERTY()
    {
        return TOXI_PROPERTY;
    }

    public void setTOXI_PROPERTY(String tOXI_PROPERTY)
    {
        TOXI_PROPERTY = tOXI_PROPERTY;
    }

    public String getDOSE_PER_UNIT()
    {
        return DOSE_PER_UNIT;
    }

    public void setDOSE_PER_UNIT(String dOSE_PER_UNIT)
    {
        DOSE_PER_UNIT = dOSE_PER_UNIT;
    }

    public String getDOSE_UNITS()
    {
        return DOSE_UNITS;
    }

    public void setDOSE_UNITS(String dOSE_UNITS)
    {
        DOSE_UNITS = dOSE_UNITS;
    }

    public String getINGR_CLASS_IDS()
    {
        return INGR_CLASS_IDS;
    }

    public void setINGR_CLASS_IDS(String iNGR_CLASS_IDS)
    {
        INGR_CLASS_IDS = iNGR_CLASS_IDS;
    }

    public String getIV_CLASS_CODE()
    {
        return IV_CLASS_CODE;
    }

    public void setIV_CLASS_CODE(String iV_CLASS_CODE)
    {
        IV_CLASS_CODE = iV_CLASS_CODE;
    }

    public String getDRUG_INDICATOR()
    {
        return DRUG_INDICATOR;
    }

    public void setDRUG_INDICATOR(String dRUG_INDICATOR)
    {
        DRUG_INDICATOR = dRUG_INDICATOR;
    }

    public String getINPUT_CODE()
    {
        return INPUT_CODE;
    }

    public void setINPUT_CODE(String iNPUT_CODE)
    {
        INPUT_CODE = iNPUT_CODE;
    }

    public String getDRUG_NO_LOCAL()
    {
        return DRUG_NO_LOCAL;
    }

    public void setDRUG_NO_LOCAL(String dRUG_NO_LOCAL)
    {
    	
    	/* 判断是否是抗菌药物 */
		this.isAnti = DrugUtils.isKJDrug(dRUG_NO_LOCAL);
        DRUG_NO_LOCAL = dRUG_NO_LOCAL;
    }

    public String getDRUG_NAME_LOCAL()
    {
        return DRUG_NAME_LOCAL;
    }

    public void setDRUG_NAME_LOCAL(String dRUG_NAME_LOCAL)
    {
        DRUG_NAME_LOCAL = dRUG_NAME_LOCAL;
    }

    public String getOPER_USER()
    {
        return OPER_USER;
    }

    public void setOPER_USER(String oPER_USER)
    {
        OPER_USER = oPER_USER;
    }

    public String getOPER_TIME()
    {
        return OPER_TIME;
    }

    public void setOPER_TIME(String oPER_TIME)
    {
        OPER_TIME = oPER_TIME;
    }

    public String getOPER_TYPE()
    {
        return OPER_TYPE;
    }

    public void setOPER_TYPE(String oPER_TYPE)
    {
        OPER_TYPE = oPER_TYPE;
    }

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((DRUG_NO_LOCAL == null) ? 0 : DRUG_NO_LOCAL.hashCode());
		result = prime * result
				+ ((recMainNo == null) ? 0 : recMainNo.hashCode());
		result = prime * result
				+ ((recSubNo == null) ? 0 : recSubNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TDrug other = (TDrug) obj;
		if (DRUG_NO_LOCAL == null) {
			if (other.DRUG_NO_LOCAL != null)
				return false;
		}
		else if (!DRUG_NO_LOCAL.equals(other.DRUG_NO_LOCAL))
			return false;
		if (recMainNo == null) 
		{
			if (other.recMainNo != null)
				return false;
		}
		else if (!recMainNo.equals(other.recMainNo))
			return false;
		if (recSubNo == null) 
		{
			if (other.recSubNo != null)
				return false;
		} 
		else if (!recSubNo.equals(other.recSubNo))
			return false;
		return true;
	}
    
//
//    @Override
//    public int hashCode()
//    {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result
//                + ((DRUG_NO_LOCAL == null) ? 0 : DRUG_NO_LOCAL.hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        TDrug other = (TDrug) obj;
//        if (DRUG_NO_LOCAL == null)
//        {
//            if (other.DRUG_NO_LOCAL != null)
//                return false;
//        }
//        else if (!DRUG_NO_LOCAL.equals(other.DRUG_NO_LOCAL))
//            return false;
//        return true;
//    }
}
