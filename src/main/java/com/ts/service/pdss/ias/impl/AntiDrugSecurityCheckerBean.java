package com.ts.service.pdss.ias.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitzd.his.Beans.TLabTest;
import com.hitzd.his.Beans.TOperationDict;
import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatVitalSigns;
import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityResult;
import com.ts.service.pdss.ias.Utils.WebServiceUtils;
import com.ts.service.pdss.ias.manager.IAntiDrugExcessAuthorizationCheck;
import com.ts.service.pdss.ias.manager.IAntiDrugMM;
import com.ts.service.pdss.ias.manager.IAntiDrugOperationCheck;
import com.ts.service.pdss.ias.manager.IAntiDrugOverMomentCheck;
import com.ts.service.pdss.ias.manager.IAntiDrugOverQuotaCheck;
import com.ts.service.pdss.ias.manager.IAntiDrugOverRateCheck;
import com.ts.service.pdss.ias.manager.IAntiDrugOverTimeCheck;
import com.ts.service.pdss.ias.manager.IAntiDrugSecurityChecker;
import com.ts.service.pdss.ias.manager.IAntiDrugSpecCheck;
import com.ts.util.HisSubCheckTime;

/**
 * 总体查询
 * @author Administrator
 *
 */
@Service
public class AntiDrugSecurityCheckerBean implements IAntiDrugSecurityChecker 
{
    
    @Resource(name="antiDrugMMBean")
    private IAntiDrugMM AntiDrugMM;
    
    /**
     * 获得手术列表
     * @return
     */
    public TOperationDict[] getOperationDict()
    {
        return AntiDrugMM.getOperationDict();
    }
    
    /**
     * 返回体检信息 
     */
    public TPatVitalSigns[] getpatVsVisitSigns(String patID , String Visitid)
    {
        return AntiDrugMM.getpatVsVisitSigns(patID, Visitid);
    }
    /**
     * 返回检验信息 
     * @param patid
     * @param visitid
     * @return
     */
    public TLabTest[] getPatLabTest(String patid , String visitid)
    {
        return AntiDrugMM.getPatLabTest(patid, visitid);
    }
    
    public TLabTest[] getpatLabTestNoDetail(String patid , String visitid)
    {
        return AntiDrugMM.getpatLabTestNoDetail(patid, visitid);
    }
    
    public TLabTest getpatLabTestDetail(String TestNO)
    {
        return AntiDrugMM.getpatLabTestDetail(TestNO);
    }
    
    /**
     * 返回手术信息 
     * @param patid
     * @param visitid
     * @return
     */
    public TPatOperation[] getPatVsVisitOperation(String patid , String visitid)
    {
        return AntiDrugMM.getPatVsVisitOperation(patid, visitid);
    }
    
    
	@Resource(name="antiDrugOverQuotaCheckBean")
	private IAntiDrugOverQuotaCheck antidrugOverQuota;
	public TAntiDrugSecurityCheckResult antiDrugOverQuotaA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo)
	{
		return this.antidrugOverQuota.Checker(WebServiceUtils.getDrugInfo( doctorInfo,diagnosis,operInfo,drugInfo,patientInfo));
	}

	@Resource(name="antiDrugOverTimeCheckBean")
	private IAntiDrugOverTimeCheck antidrugOver;
	public TAntiDrugSecurityCheckResult antiDrugOverTimeA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo)
	{
		return this.antidrugOver.Checker(WebServiceUtils.getDrugInfo( doctorInfo,diagnosis,operInfo,drugInfo,patientInfo));
	}

	@Resource(name="antiDrugExcessAuthorizationCheckBean")
	private IAntiDrugExcessAuthorizationCheck antidrugExcess;
	public TAntiDrugSecurityCheckResult antiDrugExcessAuthorizationA( String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo) 
	{
		return this.antidrugExcess.Checker(WebServiceUtils.getDrugInfo( doctorInfo,diagnosis,operInfo,drugInfo,patientInfo));
	}

	@Resource(name="antiDrugOverMomentCheckBean")
	private IAntiDrugOverMomentCheck antiDrugMoment;
	public TAntiDrugSecurityCheckResult antiDrugOverMomentA(String[] doctorInfo, String[] diagnosis, String[] operInfo,	String[] drugInfo,String[] patientInfo) 
	{
		return this.antiDrugMoment.Checker(WebServiceUtils.getDrugInfo( doctorInfo,diagnosis,operInfo,drugInfo,patientInfo));
	}
	
	@Resource(name="antiDrugOverRateCheckBean")
	private IAntiDrugOverRateCheck AntiDrugOverRate;
	public TAntiDrugSecurityCheckResult antiDrugOverRateA(String[] doctorInfo, String[] diagnosis, String[] operInfo,	String[] drugInfo,String[] patientInfo) 
	{
		return this.AntiDrugOverRate.Checker(WebServiceUtils.getDrugInfo( doctorInfo,diagnosis,operInfo,drugInfo,patientInfo));
	}
	
//	@Resource(name="antiDrugOperationCheckBean")
    private IAntiDrugOperationCheck AntiDrugOperation;
    public TAntiDrugSecurityCheckResult antiDrugOperationA(String[] doctorInfo, String[] diagnosis, String[] operInfo,   String[] drugInfo,String[] patientInfo) 
    {
        return this.AntiDrugOperation.Checker(WebServiceUtils.getDrugInfo( doctorInfo,diagnosis,operInfo,drugInfo,patientInfo));
    }
    
    @Resource(name="antiDrugSpecCheckBean")
    private IAntiDrugSpecCheck AntiDrugSppec;
    public TAntiDrugSecurityCheckResult antiDrugSpecA(String[] doctorInfo, String[] diagnosis, String[] operInfo,   String[] drugInfo,String[] patientInfo) 
    {
        return this.AntiDrugSppec.Checker(WebServiceUtils.getDrugInfo( doctorInfo,diagnosis,operInfo,drugInfo,patientInfo));
    }
	
	public TAntiDrugSecurityResult AntiDrugSecurityCheckerA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo)
	{
		TAntiDrugSecurityResult result = new TAntiDrugSecurityResult();
		TAntiDrugInput input = WebServiceUtils.getDrugInfo( doctorInfo,diagnosis,operInfo,drugInfo,patientInfo);
		
		long xx = System.currentTimeMillis();
//		TAntiDrugSecurityCheckResult antidrugOver   = this.antidrugOverQuota.Checker(input);
//		result.addAntiDrugSecurity(antidrugOver);
//		System.out.println("超分线使用抗菌药物监测与提示:" + (System.currentTimeMillis() - xx));
//		HisSubCheckTime.setSubCheckTime("[超分线使用抗菌药物监测与提示:" + (System.currentTimeMillis() - xx) + "]");
//		
//		xx = System.currentTimeMillis();
//		TAntiDrugSecurityCheckResult antidrugO      = this.antidrugOver.Checker(input);
//		result.addAntiDrugSecurity(antidrugO);
//		System.out.println("超疗程抗菌药物使用监测与提示:" + (System.currentTimeMillis() - xx));
//		HisSubCheckTime.setSubCheckTime("[超疗程抗菌药物使用监测与提示:" + (System.currentTimeMillis() - xx) + "]");
		
        xx = System.currentTimeMillis();
		TAntiDrugSecurityCheckResult antidrugExce   = this.antidrugExcess.Checker(input);
		if(antidrugExce != null) result.addAntiDrugSecurity(antidrugExce);
		System.out.println("超授权使用抗菌药物监测与提示:" + (System.currentTimeMillis() - xx));
		HisSubCheckTime.setSubCheckTime("[超授权使用抗菌药物监测与提示:" + (System.currentTimeMillis() - xx) + "]");
		
//		xx = System.currentTimeMillis();
//		TAntiDrugSecurityCheckResult antidrugMoment = this.antiDrugMoment.Checker(input);
//		result.addAntiDrugSecurity(antidrugMoment);
//		System.out.println("超使用时机（预防使用）抗菌药物监测与提示:" + (System.currentTimeMillis() - xx));
//		HisSubCheckTime.setSubCheckTime("[超使用时机（预防使用）抗菌药物监测与提示:" + (System.currentTimeMillis() - xx) + "]");
//		
//		xx = System.currentTimeMillis();
//		TAntiDrugSecurityCheckResult antidrugRate   = this.AntiDrugOverRate.Checker(input);
//		result.addAntiDrugSecurity(antidrugRate);
//		System.out.println("超耐药菌百分比用药监测与提示:" + (System.currentTimeMillis() - xx));
//		HisSubCheckTime.setSubCheckTime("[超耐药菌百分比用药监测与提示:" + (System.currentTimeMillis() - xx) + "]");
//		
//		xx = System.currentTimeMillis();
//		TAntiDrugSecurityCheckResult antidrugOper   = this.AntiDrugOperation.Checker(input);
//        result.addAntiDrugSecurity(antidrugOper);
//        System.out.println("手术用抗菌药监测与提示:" + (System.currentTimeMillis() - xx));
//        HisSubCheckTime.setSubCheckTime("[手术用抗菌药监测与提示:" + (System.currentTimeMillis() - xx) + "]");
//        
        xx = System.currentTimeMillis();
        TAntiDrugSecurityCheckResult antidrugspec   = this.AntiDrugSppec.Checker(input);
        if(antidrugspec != null)  result.addAntiDrugSecurity(antidrugspec);
        System.out.println("特殊用抗菌药监测与提示:" + (System.currentTimeMillis() - xx));
        HisSubCheckTime.setSubCheckTime("[特殊用抗菌药监测与提示:" + (System.currentTimeMillis() - xx) + "]");
        
        return result;
	}
}
