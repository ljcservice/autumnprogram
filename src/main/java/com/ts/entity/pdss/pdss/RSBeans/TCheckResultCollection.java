package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;

import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityResult;
import com.ts.entity.pdss.mas.RSBeans.TMedcareSecurityRslt;
import com.ts.entity.pdss.peaas.RSBeans.TPrescSecurityRslt;

/**
 *  总输出结果集
 * @author Administrator
 *
 */
public class TCheckResultCollection implements Serializable
{
	private static final long serialVersionUID = 1L;

	/* 抗菌药审查结果 */
	private TAntiDrugSecurityResult[] adsr;
	/* 药物安全审查结果*/
	private TDrugSecurityRslt         dsr;
	/* 处方审查结果 */
	private TPrescSecurityRslt        psr;
	/* 医保审查结果*/
	private TMedcareSecurityRslt      msr;
//	/* 用户自定义审核 */
//	private TDrugCustomSecurityRslt   dcsr;
	
//	public TDrugCustomSecurityRslt getDcsr()
//    {
//        return dcsr;
//    }
//
//    public void setDcsr(TDrugCustomSecurityRslt dcsr)
//    {
//        this.dcsr = dcsr;
//    }

    public TAntiDrugSecurityResult[] getAdsr() 
	{
		return adsr;
	}
	
	public void setAdsr(TAntiDrugSecurityResult[] adsr)
	{
		this.adsr = adsr;
	}
	
	public TDrugSecurityRslt getDsr()
	{
		return dsr;
	}
	
	public void setDsr(TDrugSecurityRslt dsr) 
	{
		this.dsr = dsr;
	}
	
	public TPrescSecurityRslt getPsr() 
	{
		return psr;
	}
	
	public void setPsr(TPrescSecurityRslt psr) 
	{
		this.psr = psr;
	}
	
	public TMedcareSecurityRslt getMsr() 
	{
		return msr;
	}
	
	public void setMsr(TMedcareSecurityRslt msr) 
	{
		this.msr = msr;
	}
	
}
