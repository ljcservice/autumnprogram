package com.ts.service.pdss.peaas.manager;
import com.ts.entity.pdss.peaas.RSBeans.TPrescCheckRslt;

public interface IPrescDoctorChecker
{
    /**
     * [病人主信息]
     *          PATIENT_ID;INP_NO;NAME;NAME_PHONETIC;SEX;DATE_OF_BIRTH;BIRTH_PLACE;CITIZENSHIP;NATION;ID_NO;IDENTITY;CHARGE_TYPE;
     *          UNIT_IN_CONTRACT;MAILING_ADDRESS;ZIP_CODE;PHONE_NUMBER_HOME;PHONE_NUMBER_BUSINESS;NEXT_OF_KIN;RELATIONSHIP;NEXT_OF_KIN_ADDR;
     *          NEXT_OF_KIN_ZIP_CODE;NEXT_OF_KIN_PHONE;LAST_VISIT_DATE;VIP_INDICATOR;CREATE_DATE;OPERATOR;SERVICE_AGENCY;business_zip_code;
     * [处方病人基本信息]
     *          PATIENT_ID;VISIT_DATE;VISIT_NO;SERIAL_NO;ORDERED_BY;DOCTOR;ORDER_DATE;
     * [处方详细信息 ]
     *          VISIT_DATE;VISIT_NO;SERIAL_NO;PRESC_NO;ITEM_NO;ITEM_CLASS;DRUG_CODE;DRUG_NAME;DRUG_SPEC;FIRM_ID;UNITS;AMOUNT;DOSAGE;DOSAGE_UNITS;
     *          ADMINISTRATION;FREQUENCY;PROVIDED_INDICATOR;COSTS;CHARGES;CHARGE_INDICATOR;DISPENSARY;REPETITION;ORDER_NO;ORDER_SUB_NO;
     *          FREQ_DETAIL;GETDRUG_FLAG;
     * [门诊病历记录]
     *          PATIENT_ID;VISIT_DATE;VISIT_NO;ILLNESS_DESC;ANAMNESIS;FAMILY_ILL;MARRITAL;INDIVIDUAL;MENSES;MED_HISTORY;BODY_EXAM;DIAG_DESC;ADVICE;DOCTOR;ORDINAL;
     *          OPERATION_RECORD;MEDICAL_RECORD;
     *  @return
    */
    public TPrescCheckRslt PrescCheck(String[] PatMaster,String[] PrescInfo ,String[][] PrescDetail , String[][] PrescDiag);
    
    /**
     * 审核
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @param patSigns
     * @param patOper
     * @return
     */
    public TPrescCheckRslt PrescCheckRs(String[] doctorInfo,String[] patientInfo, String[][] drugInfo,String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper);
    
    /**
     * 进行权限审核，类似于抗菌药
     * 
     * @param PrescBeanInfo  科室代码、科室名称、医生代码、医生名称、职称代码、职称名称、本地药品码、本地药品名称，
     * @return
     */
    public String[] DrugAuthorizationCheck(String[] PrescBeanInfo);
}
