package com.ts.service;

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

/**
 *  �������ӿ�
 * @author Administrator
 *
 */
public interface IHisAuditor 
{
    
//    /**
//     * ���ﴦ���ͻ��Զ������
//     * @return
//     */
//    public TDrugCustomSecurityRslt  OutpCustomCheck(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo, String[][] patSigns, String[] patOper);
//    
//    /**
//     * סԺҽ���Զ���� 
//     * @return
//     */
//    public TDrugCustomSecurityRslt InpCustomCheck(String[] doctorInfo,String[] patientInfo, String[][] drugInfo,String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper,String[][] antiDrug);
 
    /**
     * ���������Ϣ 
     */
    public TPatVitalSigns[] getpatVsVisitSigns(String patID , String Visitid);
    /**
     * ���ؼ�����Ϣ 
     * @param patid
     * @param visitid
     * @return
     */
    public TLabTest[] getPatLabTest(String patid , String visitid);
    
    public TLabTest[] getpatLabTestNoDetail(String patid , String visitid);
    
    public TLabTest getpatLabTestDetail(String TestNO);
    
    /**
     * ����������Ϣ 
     * @param patid
     * @param visitid
     * @return
     */
    public TPatOperation[] getPatVsVisitOperation(String patid , String visitid);
    
    /**
     * ���� �������� 
     * @param name
     * @return
     */
    public TOperationType[] getOperationTypes(String name);
    
    /**
     * �������������µ�ҩƷ 
     * @param operationID
     * @return
     */
    public TOperationDrug[] getOperationDrugs(String operationID);
    
    /**
     * ����Ȩ����ˣ������ڿ���ҩ
     * 
     * @param PrescBeanInfo  ���Ҵ��롢������ơ�ҽ����롢ҽ����ơ�ְ�ƴ��롢ְ����ơ�����ҩƷ�롢����ҩƷ��ƣ�
     * @return
     */
    public String[] DrugAuthorizationCheck(String[] PrescBeanInfo);
    
    /**
     * ����ҩƷ���(ҽ��)
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
     * ���ز��˿������д�����Ϣ
     * @param patient_id
     * @param back
     * @return
     */
    public TPrescPatMasterBean getUsePrescDetail(String patient_id,Integer SelDay, String[] back); 
    
    /**
     * Ԥ����ҩ��¼���汣��
     * @param PreveUseInfo
     */
    public void SavePreveUseDrug(String[][] PreveUseInfo);
    
    /**
     * ������ҩ��Ϣ����
     * @param TreatUseInfo
     */
    public void SaveTreatUseDrug(String[][] TreatUseInfo);
    
    
	/**
	 *      * �������  ���� ��������  
     * @param po (patient_id , visit_id ) ���� 
	 * @return
	 */
    public TCheckResultCollection DrugSecurityCheckAllPO(TPatientOrder po);
	/**
	 * ʹ�� ����id ��סԺid ��� 
	 * @param patientID
	 * @param visitID
	 * @return ���� ���� ҩƷ���� �����Ϣ�ַ� 
	 */
	public String[] DrugCheckRSByPatientIdAndVisitId(String patientID , String visitID,String parm);
	
    /**
     * ���󷵻� �ַ����� 
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
     * ��ݵ�ҩƷ��ѯ�����
     * @param drugNoLoacl
     * @param recMainNo
     * @param recSubNo
     * @return
     */
    public TCheckResultCollection getDrugSingleCheckRS(String drugNoLoacl , String recMainNo, String recSubNo);
    
    /**
     * �º�������� 
     * @param po
     */
    public void DrugSecutityCheckAllPO(TPatientOrder po);
    
    /**
     * �ַ���װҽ������
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @return
     */
    public TCheckResultCollection DrugSecurityCheckAll(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper, String[][] antiDrug);

    /**
	 * ������ʹ�ÿ���ҩ��������ʾ

	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOverQuotaA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);
	
	/**
	 * ���Ƴ̿���ҩ��ʹ�ü������ʾ
	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOverTimeA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);
	
	/**
	 * ����Ȩʹ�ÿ���ҩ��
	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugExcessAuthorizationA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);

	/**
	 * ��ʹ��ʱ��Ԥ��ʹ�ã�����ҩ��������ʾ
	 * @param doctorInfo
	 * @param diagnosis
	 * @param operInfo
	 * @param drugInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOverMomentA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);
	
	/**
	 * ����ҩ��ٷֱ���ҩ�������ʾ
	 * @param doctorInfo
	 * @param diagnosis
	 * @param operInfo
	 * @param drugInfo
	 * @param patientInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOverRateA(String[] doctorInfo, String[] diagnosis, String[] operInfo,	String[] drugInfo,String[] patientInfo);
	
	/**
	 * �ܻ��ⷽ��
	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @return
	 */
	public TAntiDrugSecurityResult AntiDrugSecurityCheckerA( String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo);
	
	/**
	 * ���� 
	 * @param doctorInfo
	 * @param diagnosis
	 * @param operInfo
	 * @param drugInfo
	 * @param patientInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugOperationA(String[] doctorInfo, String[] diagnosis, String[] operInfo,   String[] drugInfo,String[] patientInfo);
	
	/**
	 * ����
	 * @param doctorInfo
	 * @param diagnosis
	 * @param operInfo
	 * @param drugInfo
	 * @param patientInfo
	 * @return
	 */
	public TAntiDrugSecurityCheckResult antiDrugSpecA(String[] doctorInfo, String[] diagnosis, String[] operInfo,   String[] drugInfo,String[] patientInfo);

	 /**
     * ȫ�ּ�麯�����һ���Լ�����еļ����Ŀ PatientOrder ҽ������
     * 
     * @return
     */
    public TDrugSecurityRslt DrugSecurityCheck(TPatientOrder po);
    /**
     * �ַ���װҽ������
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @return
     */
    public TDrugSecurityRslt DrugSecurityCheckS(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * ������Ϣ
     * 
     * @param Drugs
     * @return
     */
    public TDrugSecurityRslt DrugInteractionCheck(TPatientOrder po );
    /**
     *  ������Ϣ
     */
    public TDrugSecurityRslt DrugInteractionCheckS(String[] Drugs);
    public TDrugSecurityRslt DrugInteractionCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * ������Ϣ
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugIvEffectCheck(TPatientOrder po);
    /** 
     * ����
     */
    public TDrugSecurityRslt DrugIvEffectCheckS(String[] DrugIds, String[] RecMainIds, String[] AdministrationIds);
    public TDrugSecurityRslt DrugIvEffectCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * ���֢���
     * 
     * @param drugs
     * @param diagnosis
     * @return
     */
    public TDrugSecurityRslt DrugDiagCheck(TPatientOrder po);
    /**
     * ���֢���
     * 
     * @param drugs
     * @param diagnosis
     * @return
     */
    public TDrugSecurityRslt DrugDiagCheckS(String[] drugs, String[] diagnosis);
    public TDrugSecurityRslt DrugDiagCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);
    /**
     * ������Ⱥ
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugSpecPeopleCheck(TPatientOrder po);
    /**
     *  ������Ⱥ
     */
    public TDrugSecurityRslt DrugSpecPeopleCheckS(String[] DrugIds, String BirthDay, String patType, String isLiverWhole, String isKidneyWhole);
    public TDrugSecurityRslt DrugSpecPeopleCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * �ظ��ɷ���
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugIngredientCheck(TPatientOrder po);
    /** 
     * �ظ���ҩ
     */
    public TDrugSecurityRslt DrugIngredientCheckS(String[] DrugIds);
    public TDrugSecurityRslt DrugIngredientCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * ��ҩ;�����
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugAdministrationCheck(TPatientOrder po);
    /** 
     * ҩƷ��ҩ;�������
     */
    public TDrugSecurityRslt DrugAdministrationCheckS(String[] DrugIds, String[] AdminIds);
    public TDrugSecurityRslt DrugAdministrationCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * ����ҩ�����
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugAllergenCheck(TPatientOrder po);
    /**
     * ҩ��������
     */
    public TDrugSecurityRslt DrugAllergenCheckS(String[] DrugIds, String[] SensitIds);
    public TDrugSecurityRslt DrugAllergenCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * ҩ��������
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugDosageCheck(TPatientOrder po);
    /** 
     * ҩ����������
     */
    public TDrugSecurityRslt DrugDosageCheckS(String[] DrugIds, String[] Dosages, String[] PerformFreqDictIds, 
            String[] StartDates, String[] StopDates, 
            String Weight, String Height, String BirthDay);
    public TDrugSecurityRslt DrugDosageCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);

    /**
     * �쳣�ź����
     * 
     * @param po
     * @return
     */
    public TDrugSecurityRslt DrugSideCheck(TPatientOrder po);
    /** 
     * �쳣�ź������ ���˲�����Ӧ
     */
    public TDrugSecurityRslt DrugSideCheckS(String[] DrugIds, String[] AdminIds, String[] SensitIds);
    public TDrugSecurityRslt DrugSideCheckA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);
    
    /**
     * ҽ�����
     * @param po
     * @return
     */
    public TDrugSecurityRslt MedicareChecker(TPatientOrder po);
    public TDrugSecurityRslt MedicareCheckerS(String DrugID, String DiagnoseCode);
    public TDrugSecurityRslt MedicareCheckerA(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation);
    
	/**
	 * ��ȡ��˺͵Ǽǿ���
	 * @param DrugDoctorInfo ҩƷ���롢��ơ����͡���񡢳��ҡ���������ҩ;����ҽ������ְ�ơ����Ҵ���
	 * @return ��ˡ��Ǽǿ���
	 */
	public String[] getAntiDrugCheckRule(String[] DrugDoctorInfo);
}
