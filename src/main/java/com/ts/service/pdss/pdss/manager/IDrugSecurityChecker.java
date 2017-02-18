package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
public interface IDrugSecurityChecker
{
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
    public TDrugSecurityRslt DrugInteractionCheckS(String[] Drugs);
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
     * 返回单个药品信息 
     * @param drugCode
     * @return
     */
    public TDrug   getDrugDesc(String drugCode);
    
    /**
     * 返回多个药品信息
     * @param drugCode
     * @return
     */
    public TDrug[] getDrugDescA(String[] drugCode);
}
