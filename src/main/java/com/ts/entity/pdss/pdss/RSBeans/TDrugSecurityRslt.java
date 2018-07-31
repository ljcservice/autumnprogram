package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugCheckInfoCollet;
import com.ts.entity.pdss.pdss.RSBeans.DrugUserAuth.TDrugUserAuthResult;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugCheckResult;

/**
 * 药物安全审查 结果集
 * @author Administrator
 *
 */
public class TDrugSecurityRslt extends TBaseResult implements Serializable
{

    private static final long        serialVersionUID = 1L;
    // 汇总表
    private TDrugCheckInfoCollet drugCheckInfoCollet;
    private String               checkResult;
    private Map<TDrug, TCheckResult> chkRslt          = new HashMap<TDrug, TCheckResult>();

    public String getAlertLevel()
    {
        String Result = "";
        try
        {
            TDrug[] drugs = getDrugs();
            for (int i = 0; i < drugs.length; i++)
            {
                TCheckResult cr = chkRslt.get(drugs[i]);
                if(cr == null)
                {
                    System.out.println("CheckResult = null-> drug :" + (drugs[i]!=null?drugs[i].getDRUG_NAME_LOCAL() + drugs[i].getDRUG_NO_LOCAL()
                            + ":" + drugs[i].getRecMainNo() + "," + drugs[i].getRecSubNo() :""));
                    continue;
                }
                Result += cr.getAlertLevel();
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return Result;
    }
    /**
     * 医保药品审查
     * @param dsr
     */
    public void CopyMedicareCheckResultTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for(TDrug drug : drugs)
        {
            TCheckResult cr  = chkRslt.get(drug);
            TCheckResult cr1 = dsr.chkRslt.get(drug);
            if(cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyMCRsltTo(cr1);
            dsr.chkRslt.put(drug, cr1);
        }
    }
    
    /** 相互作用审查结果复制 */
    public void CopyInteractionCheckResultTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyIARsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }
    }

    /** 配伍审查结果复制 */
    public void CopyIvEffectCheckResultTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyIERsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }
    }

    /** 禁忌症审查结果复制 */
    public void CopyDrugDiagInfoRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyDDIRsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }

    }

    /** 特殊人群审查结果复制 */
    public void CopyDrugSpecPeopleRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyDSPRsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }
    }

    /** 重复成份审查结果复制 */
    public void CopyDrugIngreDientCheckRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyDIDRsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }

    }

    /** 用药途径审查结果复制 */
    public void CopyAdministrationCheckRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyADMRsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }

    }

    /** 过敏药物审查结果复制 */
    public void CopyDrugAllergenCheckRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyDAGRsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }

    }

    /** 药物剂量审查结果复制 */
    public void CopyDrugDosageCheckRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyDDGRsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }
    }

    /** 异常信号审查结果复制 */
    public void CopySideCheckRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyDHRsltTo(cr1);
            dsr.chkRslt.put(drugs[i], cr1);
        }
    }
    
    /** 抗菌药物审查结果复制  */
    public void CopyAdcrCheckRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyADCRRsltTo(cr1); 
            dsr.chkRslt.put(drugs[i], cr1);
        }
    }
    
    /**
     * 药物授权控制结果 复制 
     * @param dsr
     */
    public void CopyDuAuthCheckRsltTo(TDrugSecurityRslt dsr)
    {
        TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            TCheckResult cr1 = dsr.chkRslt.get(drugs[i]);
            if (cr1 == null)
                cr1 = new TCheckResult();
            cr.CopyDuAuthRsltTo(cr1); 
            dsr.chkRslt.put(drugs[i], cr1);
        }
    }

    /**
     * 注册注册相互作用检查结果
     * 
     * @param da
     *            药品A
     * @param db
     *            药品B
     * @param dir
     *            检查结果
     */
    public void regInteractionCheckResult(TDrug da, TDrug db,
            TDrugInteractionRslt dir)
    {
        TCheckResult cr = chkRslt.get(da);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(da, cr);
        }
        cr.addInteractionRslt(dir);
    }

    /**
     * 注册过敏药物审查
     * 
     * @param drug
     * @param dar
     */
    public void regAllergenCheckResult(TDrug drug, TDrugAllergenRslt dar)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addDrugAllergenRslt(dar);
    }

    /**
     * 注册用药途径审查子模块
     * 
     * @param drug
     * @param sdr
     */
    public void regAdministrationCheckResult(TDrug drug, TAdministrationRslt sdr)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addAdminRslt(sdr);
    }

    /**
     * 注册药品禁忌审查结果
     * 
     * @param drug
     * @param ddr
     */
    public void regDiagCheckResult(TDrug drug, TDrugDiagRslt ddr)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addDrugDiagRslt(ddr);
    }

    /**
     * 注册 药物剂量审查
     * 
     * @param drug
     * @param ddr
     */
    public void regDosageCheckResult(TDrug drug, TDrugDosageRslt ddr)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addDrugDosageRslt(ddr);
    }

    /**
     * 注册药品重复成份审查结果
     * 
     * @param drug
     * @param didr
     */
    public void regIngredientCheckResult( TDrug drug, TDrugIngredientRslt didr)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addDrugIngredientRslt(didr);
    }

    /**
     * 注册配伍查结果
     * 
     * @param da
     *            药品A
     * @param db
     *            药品B
     * @param dir
     *            检查结果
     */
    public void regIvEffectCheckResult(TDrug da, TDrug db, TDrugIvEffectRslt dir)
    {
        TCheckResult cr = chkRslt.get(da);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(da, cr);
        }
        cr.addDrugIvEffectRslt(dir);

//        cr = chkRslt.get(db);
//        if (cr == null)
//        {
//            cr = new TCheckResult();
//            chkRslt.put(db, cr);
//        }
//        cr.addDrugIvEffectRslt(dir);
    }

    /**
     * 注册药品重复成份审查结果
     * 
     * @param drug
     * @param didr
     */
    public void regDrugHarmfulCheckResult(TDrug drug, TDrugHarmfulRslt dhf)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addDrugHarmfulRslt(dhf);
    }

    /**
     * 注册特殊人群
     * 
     * @param drug
     * @param dspr
     */
    public void regDrugSpecPeopleCheckResult(TDrug drug,
            TDrugSpecPeopleRslt dspr)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addDrugSpecPeopleRslt(dspr);
    }
    
    /**
     * 注册抗菌药物信息
     * 
     * @param drug
     * @param dspr
     */
    public void regAntiDrugCheckResult(TDrug drug,TAntiDrugCheckResult adcr)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addTAntiDrugCheckResult(adcr);
    }
    
    
    /**
     * 注册 药物授权控制 信息 
     * @param drug
     * @param dspr
     */
    public void regDrugUserAuthCheckResult(TDrug drug,TDrugUserAuthResult duAuth)
    {
        TCheckResult cr = chkRslt.get(drug);
        if (cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addDrugUserAuthResult(duAuth);
    }

    /**
     * 医保药品审查
     * @param drug
     * @param mcare
     */
    public void regMedicareCheckResult(TDrug drug , TMedicareRslt mcare)
    {
        TCheckResult cr = chkRslt.get(drug);
        if(cr == null)
        {
            cr = new TCheckResult();
            chkRslt.put(drug, cr);
        }
        cr.addMedicareRslt(mcare);
    }
    
    @XmlElement(name="getDrugs")
    public TDrug[] getDrugs()
    {
        TDrug[] drugs = chkRslt.keySet().toArray(new TDrug[0]);
        return drugs;
    }
    
    @XmlElement(name="getCheckResults")
    public TCheckResult[] getCheckResults()
    {
        TDrug[] drugs = getDrugs();
        TCheckResult[] crs = new TCheckResult[drugs.length];
        for (int i = 0; i < drugs.length; i++)
            crs[i] = chkRslt.get(drugs[i]);
        return crs;
    }
    
    public TCheckResult getCheckResult(TDrug t)
    {
    	TCheckResult r=  chkRslt.get(t);
    	return r;
    }
    


    /**
     * 返回结果列表
     * @return
     */

    @XmlElement(name="getCheckResult2")
    public String getCheckResult2()
    {
    	return FetchCheckResult("");
    }
    
    public String FetchCheckResult(String DrugCodeFlag)
    {
    	checkResult = "";
    	TDrug[] drugs = getDrugs();
    	for (int i = 0; i < drugs.length; i++)
    	{
    		TCheckResult cr = chkRslt.get(drugs[i]);
    		if(!"".equals(DrugCodeFlag))
			{
    			checkResult += "[" + drugs[i].getDRUG_NO_LOCAL() + "].[" + drugs[i].getDRUG_NAME_LOCAL() + "].";
			}
    		if(null != drugs[i]){
    			checkResult     = checkResult + drugs[i].getRecMainNo() + "." + drugs[i].getRecSubNo() + ".";
    		}
    		if (null != cr && cr.getAlertLevel().equalsIgnoreCase("R"))
    		{
    			checkResult += "R";
    		}
    		else if (null != cr && cr.getAlertLevel().equalsIgnoreCase("Y"))
    		{
    			checkResult += "Y";
    		}
    		else
    		{
    			checkResult += "*";
    		}
    		if (i != drugs.length - 1)
    		{
    			checkResult += ";";
    		}
    	}
        return checkResult;
    }
    /**
     * 返回 所有结果 红灯 黄灯信息  
     */
	public String getResultInfo()
	{
		RedCount = 0;
		YellowCount = 0;
    	TDrug[] drugs = getDrugs();
    	for (int i = 0; i < drugs.length; i++)
    	{
    		TCheckResult cr = chkRslt.get(drugs[i]);
    		checkResult = drugs[i].getRecMainNo() + "." + drugs[i].getRecSubNo() + ".";
    		if (null != cr && cr.getAlertLevel().equalsIgnoreCase("R"))
    			RedCount ++;
    		else
    		if (null != cr && cr.getAlertLevel().equalsIgnoreCase("Y"))
    			YellowCount ++;
    	}
		return "红灯：" + RedCount + "; 黄灯：" + YellowCount;
	}
	
	/**
	 * 返回当前审查结果 所含的最高级的警告 
	 * @return
	 */
	public String getResultLevel()
	{
	    TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TCheckResult cr = chkRslt.get(drugs[i]);
            if (null != cr && cr.getAlertLevel().equalsIgnoreCase("R"))
                return  "R";
//            else
//            if (null != cr && cr.getAlertLevel().equalsIgnoreCase("Y"))
//                YellowCount ++;
        }
	    return "Y" ;
	}
	
	/**
	 * 返回抗菌药物出现问题的最高级别
	 * @return
	 */
	public String getResultLevelByAnti()
	{
	    TDrug[] drugs = getDrugs();
        for (int i = 0; i < drugs.length; i++)
        {
            TDrug drug = drugs[i];
            if(!drug.isAnti())continue;
            TCheckResult cr = chkRslt.get(drugs[i]);
            if (null != cr && cr.getAlertLevel().equalsIgnoreCase("R"))
                return  "R";
//            else
//            if (null != cr && cr.getAlertLevel().equalsIgnoreCase("Y"))
//                YellowCount ++;
        }
	    return "Y";
	}

	/**
	 * 设置数据指点数据结果 
	 * @param drug
	 * @return
	 */
	public void setSingleCheckResult(TDrug drug , TCheckResult cr)
	{
	    chkRslt.put(drug, cr);
	}
	
    public void setCheckResult(String checkResult)
    {
        this.checkResult = checkResult;
    }

    public TDrugCheckInfoCollet getDrugCheckInfoCollet()
    {
        return drugCheckInfoCollet;
    }

    public void setDrugCheckInfoCollet(TDrugCheckInfoCollet drugCheckInfoCollet)
    {
        this.drugCheckInfoCollet = drugCheckInfoCollet;
    }
    
	public Map<TDrug, TCheckResult> getChkRslt() {
		return chkRslt;
	}
	
	public String getCheckResult() {
		return checkResult;
	}
    
}
