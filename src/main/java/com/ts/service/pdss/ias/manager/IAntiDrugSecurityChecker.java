package com.ts.service.pdss.ias.manager;

import com.hitzd.his.Beans.TLabTest;
import com.hitzd.his.Beans.TOperationDict;
import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatVitalSigns;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityResult;

public interface IAntiDrugSecurityChecker 
{
    /**
     * 获得手术列表
     * @return
     */
    public TOperationDict[] getOperationDict();
    /**
     * 返回体检信息 
     */
    public TPatVitalSigns[] getpatVsVisitSigns(String patID , String Visitid);
    /**
     * 返回检验信息 
     * @param patid
     * @param visitid
     * @return
     */
    public TLabTest[] getPatLabTest(String patid , String visitid);
    
    public TLabTest[] getpatLabTestNoDetail(String patid , String visitid);
    
    public TLabTest getpatLabTestDetail(String TestNO);
    
    /**
     * 返回手术信息 
     * @param patid
     * @param visitid
     * @return
     */
    public TPatOperation[] getPatVsVisitOperation(String patid , String visitid);
    
	/**
	 * 超分线使用抗菌药物监测与提示

	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOverQuotaA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);
	
	/**
	 * 超疗程抗菌药物使用监测与提示
	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOverTimeA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);
	
	/**
	 * 超授权使用抗菌药物
	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugExcessAuthorizationA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);

	/**
	 * 超使用时机（预防使用）抗菌药物监测与提示
	 * @param doctorInfo
	 * @param diagnosis
	 * @param operInfo
	 * @param drugInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOverMomentA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);
	
	/**
	 * 超耐药菌百分比用药监测与提示
	 * @param doctorInfo
	 * @param diagnosis
	 * @param operInfo
	 * @param drugInfo
	 * @param patientInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOverRateA(String[] doctorInfo, String[] diagnosis, String[] operInfo,	String[] drugInfo,String[] patientInfo);
	
	/**
	 * 总汇监测方法
	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @return
	 */
	public TAntiDrugSecurityResult AntiDrugSecurityCheckerA( String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);
	
	/**
	 * 手术 
	 * @param doctorInfo
	 * @param diagnosis
	 * @param operInfo
	 * @param drugInfo
	 * @param patientInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOperationA(String[] doctorInfo, String[] diagnosis, String[] operInfo,   String[] drugInfo,String[] patientInfo);
	
	/**
	 * 特殊
	 * @param doctorInfo
	 * @param diagnosis
	 * @param operInfo
	 * @param drugInfo
	 * @param patientInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugSpecA(String[] doctorInfo, String[] diagnosis, String[] operInfo,   String[] drugInfo,String[] patientInfo);
}
