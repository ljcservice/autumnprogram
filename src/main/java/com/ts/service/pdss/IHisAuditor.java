package com.ts.service.pdss;

import com.hitzd.his.Beans.TLabTest;
import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatVitalSigns;
import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityResult;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResultCollection;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.peaas.Beans.TOperationDrug;
import com.ts.entity.pdss.peaas.Beans.TOperationType;
import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;
import com.ts.entity.pdss.peaas.RSBeans.TPrescCheckRslt;
import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatVitalSigns;
import com.hitzd.his.Beans.TPatientOrder;

/**
 *  总体审查接口
 * @author Administrator
 *
 */
public interface IHisAuditor 
{
    
//    /**
//     * 门诊处方客户自定义审核
//     * @return
//     */
//    public TDrugCustomSecurityRslt  OutpCustomCheck(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo, String[][] patSigns, String[] patOper);
//    
//    /**
//     * 住院医嘱自定审核 
//     * @return
//     */
//    public TDrugCustomSecurityRslt InpCustomCheck(String[] doctorInfo,String[] patientInfo, String[][] drugInfo,String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper,String[][] antiDrug);
 
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
     * 返回 手术类型 
     * @param name
     * @return
     */
    public TOperationType[] getOperationTypes(String name);
    
    /**
     * 返回手术类型下的药品 
     * @param operationID
     * @return
     */
    public TOperationDrug[] getOperationDrugs(String operationID);
    
    /**
     * 进行权限审核，类似于抗菌药
     * 
     * @param PrescBeanInfo  科室代码、科室名称、医生代码、医生名称、职称代码、职称名称、本地药品码、本地药品名称，
     * @return
     */
    public String[] DrugAuthorizationCheck(String[] PrescBeanInfo);
    
    /**
     * 门诊药品审核(医生)
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @param patSigns
     * @param patOper
     * @return
     */
    public TPrescCheckRslt PrescCheckRs(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo, String[][] patSigns, String[] patOper);
    
    /**
     * 返回病人开具所有处方信息
     * @param patient_id
     * @param back
     * @return
     */
    public TPrescPatMasterBean getUsePrescDetail(String patient_id,Integer SelDay, String[] back); 
    
    /**
     * 预防用药记录保存保存
     * @param PreveUseInfo
     */
    public void SavePreveUseDrug(String[][] PreveUseInfo);
    
    /**
     * 治疗用药信息保存
     * @param TreatUseInfo
     */
    public void SaveTreatUseDrug(String[][] TreatUseInfo);
    
    
	/**
	 *      * 所有审查  并且 保存其结果  
     * @param po (patient_id , visit_id ) 参数 
	 * @return
	 */
    public TCheckResultCollection DrugSecurityCheckAllPO(TPatientOrder po);
	/**
	 * 使用 病人id 和住院id 审查 
	 * @param patientID
	 * @param visitID
	 * @return 返回 带有 药品代码 审查信息字符串 
	 */
	public String[] DrugCheckRSByPatientIdAndVisitId(String patientID , String visitID,String parm);
	
    /**
     * 审查后返回 字符数组 
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @param patSigns
     * @param patOper
     * @return
     */
    public String[] DrugCheckRSInfo(String[] doctorInfo,String[] patientInfo, String[][] drugInfo,String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper , String[][] antiDrug);
    
    /**
     * 根据单药品查询审查结果
     * @param drugNoLoacl
     * @param recMainNo
     * @param recSubNo
     * @return
     */
    public TCheckResultCollection getDrugSingleCheckRS(String drugNoLoacl , String recMainNo, String recSubNo);
    
    /**
     * 事后审查所用 
     * @param po
     */
    public void DrugSecutityCheckAllPO(TPatientOrder po);
    
    /**
     * 字符串组装医嘱对象
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @return
     */
    public TCheckResultCollection DrugSecurityCheckAll(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper, String[][] antiDrug);

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

	 /**
     * 全局检查函数，可以一次性检查所有的检查项目 PatientOrder 医嘱对象
     * 
     * @return
     */
    public TDrugSecurityRslt DrugSecurityCheck(TPatientOrder po);
    /**
     * 字符串组装医嘱对象
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @return
     */
    public TDrugSecurityRslt DrugSecurityCheckS(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * 互动信息
     * 
     * @param Drugs
     * @return
     */
    public TDrugSecurityRslt DrugInteractionCheck(TPatientOrder po );
    /**
     *  互动信息
     */
//    public TDrugSecurityRslt DrugInteractionCheckS(String[] Drugs);
    public TDrugSecurityRslt DrugInteractionCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * 配伍信息
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugIvEffectCheck(TPatientOrder po);
    /** 
     * 配伍
     */
    public TDrugSecurityRslt DrugIvEffectCheckS(String[] DrugIds, String[] RecMainIds, String[] AdministrationIds);
    public TDrugSecurityRslt DrugIvEffectCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * 禁忌症审查
     * 
     * @param drugs
     * @param diagnosis
     * @return
     */
    public TDrugSecurityRslt DrugDiagCheck(TPatientOrder po);
    /**
     * 禁忌症审查
     * 
     * @param drugs
     * @param diagnosis
     * @return
     */
    public TDrugSecurityRslt DrugDiagCheckS(String[] drugs, String[] diagnosis);
    public TDrugSecurityRslt DrugDiagCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);
    /**
     * 特殊人群
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugSpecPeopleCheck(TPatientOrder po);
    /**
     *  特殊人群
     */
    public TDrugSecurityRslt DrugSpecPeopleCheckS(String[] DrugIds, String BirthDay, String patType, String isLiverWhole, String isKidneyWhole);
    public TDrugSecurityRslt DrugSpecPeopleCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * 重复成份审
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugIngredientCheck(TPatientOrder po);
    /** 
     * 重复用药
     */
    public TDrugSecurityRslt DrugIngredientCheckS(String[] DrugIds);
    public TDrugSecurityRslt DrugIngredientCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * 用药途径审查
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugAdministrationCheck(TPatientOrder po);
    /** 
     * 药品给药途径审查结果
     */
    public TDrugSecurityRslt DrugAdministrationCheckS(String[] DrugIds, String[] AdminIds);
    public TDrugSecurityRslt DrugAdministrationCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * 过敏药物审查
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugAllergenCheck(TPatientOrder po);
    /**
     * 药物过敏审查
     */
    public TDrugSecurityRslt DrugAllergenCheckS(String[] DrugIds, String[] SensitIds);
    public TDrugSecurityRslt DrugAllergenCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * 药物剂量审查
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugDosageCheck(TPatientOrder po);
    /** 
     * 药物剂量审查结果
     */
    public TDrugSecurityRslt DrugDosageCheckS(String[] DrugIds, String[] Dosages, String[] PerformFreqDictIds, 
            String[] StartDates, String[] StopDates, 
            String Weight, String Height, String BirthDay);
    public TDrugSecurityRslt DrugDosageCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * 异常信号审查
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugSideCheck(TPatientOrder po);
    /** 
     * 异常信号审查子 病人不良反应
     */
    public TDrugSecurityRslt DrugSideCheckS(String[] DrugIds, String[] AdminIds, String[] SensitIds);
    public TDrugSecurityRslt DrugSideCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);
    
    /**
     * 医保审查
     * @param po
     * @return
     */
    public TDrugSecurityRslt MedicareChecker(TPatientOrder po);
    public TDrugSecurityRslt MedicareCheckerS(String DrugID, String DiagnoseCode);
    public TDrugSecurityRslt MedicareCheckerA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);
    
	/**
	 * 获取审核和登记开关
	 * @param DrugDoctorInfo 药品代码、名称、剂型、规格、厂家、数量、给药途径、医生姓名、职称、科室代码
	 * @return 审核、登记开关
	 */
	public String[] getAntiDrugCheckRule(String[] DrugDoctorInfo);
}
