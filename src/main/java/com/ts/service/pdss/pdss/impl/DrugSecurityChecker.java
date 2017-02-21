package com.ts.service.pdss.pdss.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderDrugSensitive;
import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResult;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Utils.CommonUtils;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugAdministrationChecker;
import com.ts.service.pdss.pdss.manager.IDrugAllergenChecker;
import com.ts.service.pdss.pdss.manager.IDrugDiagChecker;
import com.ts.service.pdss.pdss.manager.IDrugDosageChecker;
import com.ts.service.pdss.pdss.manager.IDrugIngredientChecker;
import com.ts.service.pdss.pdss.manager.IDrugInteractionChecker;
import com.ts.service.pdss.pdss.manager.IDrugIvEffectChecker;
import com.ts.service.pdss.pdss.manager.IDrugSecurityChecker;
import com.ts.service.pdss.pdss.manager.IDrugSideChecker;
import com.ts.service.pdss.pdss.manager.IDrugSpecPeopleChecker;
import com.ts.service.pdss.pdss.manager.IMedicareChecker;
import com.ts.service.pdss.pdss.manager.IPatientSaveCheckResult;

/**
 * 用药安全检查模块，该模块可以检查所有安全检查子项 包括相互作用、配伍、禁忌等
 * 
 * @author Administrator
 * 
 */
@Service
public class DrugSecurityChecker implements IDrugSecurityChecker
{
    /* 相互作用检查 */
    @Resource(name = "drugInteractionCheckerBean")
    private IDrugInteractionChecker drugInteractionCheckerBean;
    public TDrugSecurityRslt DrugInteractionCheck(TPatOrderDrug[] pods)
    {
        return drugInteractionCheckerBean.Check( pods);
    }
    public TDrugSecurityRslt DrugInteractionCheckS(List<String> ids)
    {
        return drugInteractionCheckerBean.Check(ids);
    }

    /* 配伍审查 */
    @Resource(name = "drugIvEffectCheckerBean")
    private IDrugIvEffectChecker drugIvEffectCheckerBean;

    public TDrugSecurityRslt DrugIvEffectCheck(TPatientOrder po)
    {
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        String[] DrugIds = new String[pods.length];
        String[] RecMainIds = new String[pods.length];
        String[] AdminIds = new String[pods.length];
        for (int i = 0; i < pods.length; i++)
        {
        	TPatOrderDrug drug = pods[i];
        	DrugIds[i] = drug.getDrugID();
        	RecMainIds[i] = drug.getRecMainNo();
        	AdminIds[i] = drug.getAdministrationID();
        }
        return DrugIvEffectCheckS(DrugIds, RecMainIds, AdminIds);
    }

    public TDrugSecurityRslt DrugIvEffectCheckS(String[] DrugIds, String[] RecMainIds, String[] AdministrationIds)
    {
    	return drugIvEffectCheckerBean.Check(DrugIds, RecMainIds, AdministrationIds);
    }
    
    /* 禁忌症审查 */
    @Resource(name = "drugDiagCheckerBean")
    private IDrugDiagChecker drugDiagCheckerBean;
    public TDrugSecurityRslt DrugDiagCheck( String param)
    {
        return this.drugDiagCheckerBean.Check( param);
    }
    public TDrugSecurityRslt DrugDiagCheckS(String[] drugs, String[] diagnosis)
    {
        return drugDiagCheckerBean.Check(drugs, diagnosis);
    }
    /* 特殊人群审查 */
    @Resource(name = "drugSpecPeopleCheckerBean")
    private IDrugSpecPeopleChecker drugSpecPeopleCheckerBean;
    public TDrugSecurityRslt DrugSpecPeopleCheck(TPatientOrder po)
    {
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        String[] Drugs = new String[pods.length];
        for (int i = 0; i < pods.length; i++)
            Drugs[i] = pods[i].getDrugID();
        String patType = "0"; // 普通
        if (po.getPatInfoExt().TheIsLact()) patType = "1";// 孕妇
        else if (po.getPatInfoExt().TheIsPregnant()) patType = "2";
        return DrugSpecPeopleCheckS(Drugs, po.getPatient().getDateOfBirth(), patType, po.getPatInfoExt().getIsLiverWhole(), po.getPatInfoExt().getIsKidneyWhole());
    }
    /**
     *多个本地药品码，出生日期，病人类别（孕妇、哺乳、普通），肝功能不全标志，肾功能不全标志
     **/
    public TDrugSecurityRslt DrugSpecPeopleCheckS(String[] DrugIds, String BirthDay, String patType, String isLiverWhole, String isKidneyWhole)
    {
        return drugSpecPeopleCheckerBean.Check(DrugIds, BirthDay, patType, isLiverWhole, isKidneyWhole);
    }

    /* 重复成分审查 */
    @Resource(name = "drugIngredientCheckerBean")
    private IDrugIngredientChecker drugIngredientCheckerBean;
    public TDrugSecurityRslt DrugIngredientCheck(TPatientOrder po)
    {
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        String[] Drugs = new String[pods.length];
        for (int i = 0; i < pods.length; i++)
            Drugs[i] = pods[i].getDrugID();
        return DrugIngredientCheckS(Drugs);
    }

    public TDrugSecurityRslt DrugIngredientCheckS(String[] DrugIds)
    {
        return this.drugIngredientCheckerBean.Check(DrugIds);
    }
    /* 用药途径审查 */
    @Resource(name = "drugAdministrationCheckerBean")
    private IDrugAdministrationChecker drugAdministrationCheckerBean;
    public TDrugSecurityRslt DrugAdministrationCheck(TPatientOrder po)
    {
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        String[] DrugIds = new String[pods.length];
        String[] AdminIds = new String[pods.length];
        for (int i = 0; i < pods.length; i++)
        {
        	TPatOrderDrug drug = pods[i];
        	DrugIds[i] = drug.getDrugID();
        	AdminIds[i] = drug.getAdministrationID();
        }
        return DrugAdministrationCheckS(DrugIds, AdminIds);
    }
    public TDrugSecurityRslt DrugAdministrationCheckS(String[] DrugIds, String[] AdminIds)
    {
        return this.drugAdministrationCheckerBean.Check(DrugIds, AdminIds);
    }

    /* 过敏药物审查 */
    @Resource(name = "drugAllergenCheckerBean")
    private IDrugAllergenChecker drugAllergenCheckerBean;

    public TDrugSecurityRslt DrugAllergenCheck(TPatientOrder po)
    {
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        String[] DrugIds = new String[pods.length];
        for (int i = 0; i < pods.length; i++)
        {
        	TPatOrderDrug drug = pods[i];
        	DrugIds[i] = drug.getDrugID();
        }
        TPatOrderDrugSensitive[] podss = po.getPatOrderDrugSensitives();
        String[] SensitIds = new String[podss.length];
        for (int i = 0; i < podss.length; i++)
        {
        	SensitIds[i] = podss[i].getPatOrderDrugSensitiveID();
        }
        return DrugAllergenCheckS(DrugIds, SensitIds);
    }

    public TDrugSecurityRslt DrugAllergenCheckS(String[] DrugIds, String[] SensitIds)
    {
        return this.drugAllergenCheckerBean.Check(DrugIds, SensitIds);
    }
    
    /* 药物剂量审查 */
    @Resource(name = "drugDosageCheckerBean")
    private IDrugDosageChecker drugDosageCheckerBean;
    
    public TDrugSecurityRslt DrugDosageCheck(TPatientOrder po)
    {
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        String[] DrugIds = new String[pods.length];
    	String[] Dosages = new String[pods.length];
    	String[] PerformFreqDictIds = new String[pods.length];
		String[] StartDates = new String[pods.length];
		String[] StopDates = new String[pods.length];
        for (int i = 0; i < pods.length; i++)
        {
        	TPatOrderDrug drug = pods[i];
        	DrugIds[i] = drug.getDrugID();
        	Dosages[i] = drug.getDosage();
        	PerformFreqDictIds[i] = drug.getPerformFreqDictID();
        	StartDates[i] = drug.getStartDateTime();
        	StopDates[i] = drug.getStopDateTime();
        }
        return DrugDosageCheckS(DrugIds, Dosages, PerformFreqDictIds, 
        		StartDates, StopDates, po.getPatInfoExt().getWeight(), po.getPatInfoExt().getHeight(),
        		po.getPatient().getDateOfBirth());
    }
    /**多个本地药品码，剂量，x次/天，用药起始时间，用药中止时间（若为空，则按当天计算）,体重，身高，出生日期*/
    public TDrugSecurityRslt DrugDosageCheckS(String[] DrugIds, String[] Dosages, String[] PerformFreqDictIds, 
    		String[] StartDates, String[] StopDates, 
    		String Weight, String Height, String BirthDay)
    {
        return this.drugDosageCheckerBean.Check(DrugIds, Dosages, PerformFreqDictIds, 
        		StartDates, StopDates, Weight, Height, BirthDay);
    }
    
    /* 异常信号审查 */
    @Resource(name = "drugSideCheckerBean")
    private IDrugSideChecker drugSideCheckerBean;
    public TDrugSecurityRslt DrugSideCheck(TPatientOrder po)
    {
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        String[] DrugIds = new String[pods.length];
        String[] AdminIds = new String[pods.length];
        for (int i = 0; i < pods.length; i++)
        {
        	TPatOrderDrug drug = pods[i];
        	DrugIds[i] = drug.getDrugID();
        	AdminIds[i] = drug.getAdministrationID();
        }
        TPatOrderDrugSensitive[] podss = po.getPatOrderDrugSensitives();
        String[] SensitIds = new String[podss.length];
        for (int i = 0; i < podss.length; i++)
        {
        	SensitIds[i] = podss[i].getPatOrderDrugSensitiveID();
        }
        return DrugSideCheckS(DrugIds, AdminIds, SensitIds);
    }
    public TDrugSecurityRslt DrugSideCheckS(String[] DrugIds, String[] AdminIds, String[] SensitIds)
    {
    	return this.drugSideCheckerBean.Check(DrugIds, AdminIds, SensitIds);
    }
    /* 相互作用检查 */
    @Resource(name = "medicareChecker")
    private IMedicareChecker medicareCheckerBean;
    
	@Override
	public TDrugSecurityRslt MedicareChecker(TPatientOrder po) 
	{
		return null;
	}
	@Override
	public TDrugSecurityRslt MedicareCheckerS(String DrugID, String DiagnoseCode) 
	{
		return null;
	}
    /**
     * 参数字符串及字符串数组
     * @param doctorInfo    -- doctorDeptID,doctorDeptName,doctorID,doctorName,doctorTitleID,doctorTitleName
     * @param patientInfo   -- patientID,name,sex,dateOfBirth,birthPlace,nation,isLact,isPregnant,insureanceType,__
     *                         insuranceNo,isLiverWhole,isKidneyWhole,isWorking,height,weight,visitID,inDept,inMode,__
     *                         inDate,outDept,outDate,outMode,patAdmCondition
     * @param drugInfo      -- {drugID,drugStandID,DrugName,recMainNo,recSubNo,dosage,doseUnits,administrationID,__
     *                          administrationStandID,performFreqDictID,performFreqDictStandID,performFreqDictText,startDateTime,__
     *                          stopDateTime,doctorDept,doctor,isGroup,firmID,isNew,dvaliddate}
     * @param diagnosisInfo -- {diagnosisDictID}
     * @param sensitiveInfo -- {patOrderDrugSensitiveID,drugAllergenID}
     * @return
     */
    public TDrugSecurityRslt DrugSecurityCheckS(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	//TODO 在此加上调试开关  
    	return DrugSecurityCheck(CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation));
    }
    
    @Resource(name="patientSaveCheckResult")
    private IPatientSaveCheckResult patientSavaBean;
    
    /**
     * 全局检查函数，可以一次性检查所有的检查项目 PatientOrder (医嘱对象)
     * 
     * @return
     */
    public TDrugSecurityRslt DrugSecurityCheck(TPatientOrder po)
    {
        TDrugSecurityRslt dsr = new TDrugSecurityRslt();
        /* 相互作用检查 */
        long xx = System.currentTimeMillis();
        this.drugInteractionCheckerBean.Check(po.getPatOrderDrugs()).CopyInteractionCheckResultTo(dsr);
        System.out.println("互动信息:" + (System.currentTimeMillis() - xx));
        /* 配伍审查 */
        xx = System.currentTimeMillis();
        this.drugIvEffectCheckerBean.Check(po).CopyIvEffectCheckResultTo(dsr);
        System.out.println("配伍审查:" + (System.currentTimeMillis() - xx));
        /* 禁忌症审查 */
        xx = System.currentTimeMillis();
        
//   TODO     this.drugDiagCheckerBean.Check(po).CopyDrugDiagInfoRsltTo(dsr);
        System.out.println("禁忌症  :" + (System.currentTimeMillis() - xx));
        /* 特殊人群审查 */
        xx = System.currentTimeMillis();
        this.drugSpecPeopleCheckerBean.Check(po).CopyDrugSpecPeopleRsltTo(dsr);
        System.out.println("特殊人群:" + (System.currentTimeMillis() - xx));
        /* 重复成份审查 */
        xx = System.currentTimeMillis();
        this.drugIngredientCheckerBean.check(po).CopyDrugIngreDientCheckRsltTo(dsr);
        System.out.println("重复成份:" + (System.currentTimeMillis() - xx));
        /* 用药途径审查 */
        xx = System.currentTimeMillis();
        this.drugAdministrationCheckerBean.Check(po).CopyAdministrationCheckRsltTo(dsr);
        System.out.println("用药途径:" + (System.currentTimeMillis() - xx));
        /* 过敏药物审查 */
        xx = System.currentTimeMillis();
        this.drugAllergenCheckerBean.Check(po).CopyDrugAllergenCheckRsltTo(dsr);
        System.out.println("过敏药物:" + (System.currentTimeMillis() - xx));
        /* 药物剂量审查 */
        xx = System.currentTimeMillis();
        this.drugDosageCheckerBean.Check(po).CopyDrugDosageCheckRsltTo(dsr);
        System.out.println("药物剂量:" + (System.currentTimeMillis() - xx));
        /* 异常信号审查 */
        xx = System.currentTimeMillis();
        this.drugSideCheckerBean.Check(po).CopySideCheckRsltTo(dsr);
        System.out.println("不良反应:" + (System.currentTimeMillis() - xx));
        /* 医保审查 */
        xx = System.currentTimeMillis();
        this.medicareCheckerBean.Check(po).CopyMedicareCheckResultTo(dsr);
        System.out.println("医保用药:" + (System.currentTimeMillis() - xx));
        return dsr;
    }
    
    private final static String charNR = "\n\r";
    
    /**
     *  显示结果信息 "正式使用可注掉"  
     * @param dsr
     */
    private void OutResultInfo(TDrugSecurityRslt dsr)
    {
    	TDrug[]      drugs = dsr.getDrugs();
    	TCheckResult[] crts= dsr.getCheckResults();
    	for(int i = 0 ; i < drugs.length ;i++)
    	{
    		TDrug    drug =  drugs[i];
    		String rsInfo = "";
    		rsInfo += " ID:"				+ drug.getDRUG_ID() + charNR;
    		rsInfo += " 药品名称:"			+ drug.getDRUG_NAME() + charNR;
    		rsInfo += " 药品类码:"			+ drug.getDRUG_CLASS_ID() + charNR;
    		rsInfo += " 剂型码 :"			+ drug.getDOSE_CLASS_ID() + charNR;
    		rsInfo += " 规格 :"				+ drug.getDRUG_SPEC() + charNR;
    		rsInfo += " 生产厂家:"			+ drug.getFIRM_ID() + charNR;
    		rsInfo += " 单位:"				+ drug.getUNITS() + charNR;
    		rsInfo += " 剂型 :"				+ drug.getDRUG_FORM() + charNR;
    		rsInfo += " 毒理分类 :"			+ drug.getTOXI_PROPERTY() + charNR;
    		rsInfo += " 最小单位剂量 :"		+ drug.getDOSE_PER_UNIT() + charNR;
    		rsInfo += " 剂量单位:"			+ drug.getDOSE_UNITS() + charNR;
    		rsInfo += " 成分码 :"			+ drug.getINGR_CLASS_IDS() + charNR;
    		rsInfo += " 输液码:"				+ drug.getIV_CLASS_CODE() + charNR;
    		rsInfo += " 药品类别标志 :"		+ drug.getDRUG_INDICATOR() + charNR;
    		rsInfo += " 输入码:"				+ drug.getINPUT_CODE() + charNR;
    		rsInfo += " 本地ID:"				+ drug.getDRUG_NO_LOCAL() + charNR;
    		rsInfo += " 本地药品名称 :"		+ drug.getDRUG_NAME_LOCAL() + charNR;
    		rsInfo += " 操作人:"				+ drug.getOPER_USER() + charNR;
    		rsInfo += " 操作时间:"			+ drug.getOPER_TIME() + charNR;
    		rsInfo += " 操作方式:"			+ drug.getOPER_TYPE() + charNR;
    		String crtInfo = "";
    		TCheckResult crt = crts[i];
    		crtInfo += " 药物给药途径审查结果对象:"	+  crt.getAdministrationRslt().length 	+ " 个," + charNR;
    		crtInfo += " 药物过敏审查结果对象:"		+  crt.getDrugAllergenRslt().length   	+ " 个," + charNR;
    		crtInfo += " 药品禁忌审查结果对象:"		+ crt.getDrugDiagRslt().length        	+ " 个," + charNR;
    		crtInfo += " 药物剂量审查结果对象:"		+ crt.getDrugDosageRslt().length      	+ " 个," + charNR;
    		crtInfo += " 药物重复成份审查结果对象:"	+ crt.getDrugIngredientRslt().length  	+ " 个," + charNR;
    		crtInfo += " 药品相互作用审查结果对象:"	+ crt.getDrugInteractionRslt().length 	+ " 个," + charNR;
    		crtInfo += " 药品配伍审查结果对象:"		+ crt.getDrugIvEffectRslt().length    	+ " 个," + charNR;
    		crtInfo += " 特殊人群审查结果对象:"		+ crt.getDrugSpecPeopleRslt().length  	+ " 个," + charNR;
    		crtInfo += " 不良反应审查结果对象:"		+ crt.getSideRslt().length            	+ " 个," + charNR;
    		crtInfo += " 医保审查:"					+ crt.getMedicareRslt().length			+ " 个," + charNR;
    		System.out.println("====================== 检查药品信息 ======================");
    		System.out.println(" [ " + rsInfo + " ] ");
    		System.out.println("====================== 检查结果信息  ======================");
    		System.out.println(crt.getCheckResult());
    		System.out.println("====================== 检查各结构个数  ====================");
    		System.out.println(crtInfo);
    	}
    }
    
    @Override
    public TDrugSecurityRslt DrugIvEffectCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
    	TDrugSecurityRslt dsr = this.drugIvEffectCheckerBean.Check(po);
    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
    	this.patientSavaBean.saveDrugIvEffectCheckInfo(dsr);
        return dsr;
    }

    @Override
    public TDrugSecurityRslt DrugSpecPeopleCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo,patSigns,patOperation);
    	TDrugSecurityRslt dsr = this.drugSpecPeopleCheckerBean.Check(po);
    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
    	this.patientSavaBean.saveDrugSpecPeopleCheckInfo(dsr);
    	return dsr; 
    }

    @Override
    public TDrugSecurityRslt DrugIngredientCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
    	TDrugSecurityRslt dsr = this.drugIngredientCheckerBean.check(po);
    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
    	this.patientSavaBean.saveDrugIngredientCheckInfo(dsr);
    	return dsr; 
    }

    @Override
    public TDrugSecurityRslt DrugAdministrationCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation) 
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
    	TDrugSecurityRslt dsr = this.drugAdministrationCheckerBean.Check(po);
    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
    	this.patientSavaBean.saveDrugAdministrationCheckInfo(dsr);
    	return dsr;
    }

    @Override
    public TDrugSecurityRslt DrugAllergenCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
    	TDrugSecurityRslt dsr = this.drugAllergenCheckerBean.Check(po);
    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
    	this.patientSavaBean.saveDrugAllergenCheckInfo(dsr);
    	return dsr;
    }

    @Override
    public TDrugSecurityRslt DrugDosageCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
    	TDrugSecurityRslt dsr = this.drugDosageCheckerBean.Check(po);
    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
    	this.patientSavaBean.saveDrugDosageCheckInfo(dsr);
    	return dsr;
    }

    @Override
    public TDrugSecurityRslt DrugSideCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
    	TDrugSecurityRslt dsr = this.drugSideCheckerBean.Check(po);
    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
    	this.patientSavaBean.saveDrugSideCheckInfo(dsr);
    	return dsr;
    }
    @Override
    public TDrugSecurityRslt DrugInteractionCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
    	TDrugSecurityRslt dsr = this.drugInteractionCheckerBean.Check(po.getPatOrderDrugs());
    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
    	this.patientSavaBean.saveDrugInteractionCheckInfo(dsr);
    	return dsr;
    }
    @Override
    public TDrugSecurityRslt DrugDiagCheckA(String[] doctorInfo,
            String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = this.drugDiagCheckerBean.Check(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugDiagCheckInfo(dsr);
//    	return dsr;
    	return null;
    }
	@Override
	public TDrugSecurityRslt MedicareCheckerA(String[] doctorInfo,
			String[] patientInfo, String[][] drugInfo,
			String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
	{
		TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
    	TDrugSecurityRslt dsr = medicareCheckerBean.Check(po);
    	return dsr;
	}
    
	@Override
	public TDrug getDrugDesc(String drugCode)
	{
	    return getDrugDescA(new String[]{drugCode})[0];
	}
	@Override
	public TDrug[] getDrugDescA(String[] drugCode)
	{
	    JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
	    List<TDrug> drugs = QueryUtils.queryDrug(drugCode, "", query);
	    query = null;
	    return (TDrug[])drugs.toArray(new TDrug[0]);
	}
}
