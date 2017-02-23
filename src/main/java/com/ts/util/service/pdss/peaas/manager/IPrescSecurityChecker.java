package com.ts.service.pdss.peaas.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.peaas.Beans.TOperationDrug;
import com.ts.entity.pdss.peaas.Beans.TOperationType;
import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;
import com.ts.entity.pdss.peaas.RSBeans.TPrescCheckRslt;
import com.ts.entity.pdss.peaas.RSBeans.TPrescSecurityRslt;

/**
 * 大处方 
 * @author Administrator
 *
 */
public interface IPrescSecurityChecker
{

    /**
     *  大处方检查 
     * @param po
     * @return
     */
    public TPrescSecurityRslt PrescSecurityCheck(TPatientOrder po);
    
    /**
     * [处方病人基本信息] 
     *          PATIENT_ID;VISIT_DATE;VISIT_NO;SERIAL_NO;ORDERED_BY;DOCTOR;ORDER_DATE;
     * [处方详细信息 ]
     *          VISIT_DATE;VISIT_NO;SERIAL_NO;PRESC_NO;ITEM_NO;ITEM_CLASS;DRUG_CODE;DRUG_NAME;DRUG_SPEC;FIRM_ID;UNITS;AMOUNT;DOSAGE;DOSAGE_UNITS;
     *          ADMINISTRATION;FREQUENCY;PROVIDED_INDICATOR;COSTS;CHARGES;CHARGE_INDICATOR;DISPENSARY;REPETITION;ORDER_NO;ORDER_SUB_NO;
     *          FREQ_DETAIL;GETDRUG_FLAG;
     *  [门诊病历记录]
     *          PATIENT_ID;VISIT_DATE;VISIT_NO;ILLNESS_DESC;ANAMNESIS;FAMILY_ILL;MARRITAL;INDIVIDUAL;MENSES;MED_HISTORY;BODY_EXAM;DIAG_DESC;ADVICE;DOCTOR;ORDINAL;
     *          OPERATION_RECORD;MEDICAL_RECORD;
     *             
     *  * @return
     */
    public TPrescCheckRslt PrescCheck(String[] PrescInfo ,String[][] PrescDetail , String[][] PrescDiag);
    
    /**
     * 返回病人开具的所有处方 
     * @param patient_id
     * @param back
     * @return
     */
    public TPrescPatMasterBean getUsePrescDetail(String patient_id,Integer SelDay, String[] back);
    
    /**
     * 审查病人所有处方 
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @param patSigns
     * @param patOper
     * @return
     */
    public TPrescCheckRslt PrescCheckRs(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,
            String[][] patSigns, String[] patOper);
    
    /**
     * 进行权限审核，类似于抗菌药
     * 
     * @param PrescBeanInfo  科室代码、科室名称、医生代码、医生名称、职称代码、职称名称、本地药品码、本地药品名称，
     * @return
     */
    public String[] DrugAuthorizationCheck(String[] PrescBeanInfo);
    
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
}
