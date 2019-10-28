package com.ts.service.pdss.peaas.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.peaas.Beans.TOperationDrug;
import com.ts.entity.pdss.peaas.Beans.TOperationType;
import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;
import com.ts.entity.pdss.peaas.RSBeans.TPrescCheckRslt;
import com.ts.entity.pdss.peaas.RSBeans.TPrescSecurityRslt;
import com.ts.service.pdss.pdss.manager.IDrugAllergenChecker;
import com.ts.service.pdss.pdss.manager.IDrugDiagChecker;
import com.ts.service.pdss.pdss.manager.IDrugDosageChecker;
import com.ts.service.pdss.pdss.manager.IDrugInteractionChecker;
import com.ts.service.pdss.pdss.manager.IDrugIvEffectChecker;
import com.ts.service.pdss.peaas.manager.IOperationInforService;
import com.ts.service.pdss.peaas.manager.IPrescCheckerService;
import com.ts.service.pdss.peaas.manager.IPrescDoctorChecker;
import com.ts.service.pdss.peaas.manager.IPrescSecurityChecker;

/**
 * 大处方审查
 * @author Administrator
 *
 */
@Service
@Deprecated
public class PrescSecurityCheckBean implements IPrescSecurityChecker
{

    /* 相互作用检查 */
    @Resource(name = "drugInteractionCheckerBean")
    private IDrugInteractionChecker drugInteractionCheckerBean;
    
    /* 配伍审查 */
    @Resource(name = "drugIvEffectCheckerBean")
    private IDrugIvEffectChecker drugIvEffectCheckerBean;
    
    /* 禁忌症审查 */
    @Resource(name = "drugDiagCheckerBean")
    private IDrugDiagChecker drugDiagCheckerBean;
    
    /* 过敏药物审查 */
    @Resource(name = "drugAllergenCheckerBean")
    private IDrugAllergenChecker drugAllergenCheckerBean;
    
    /* 药物剂量审查 */
    @Resource(name = "drugDosageCheckerBean")
    private IDrugDosageChecker drugDosageCheckerBean;
    
    /**
     *  大处方 审查  
     */
    @Override
    public TPrescSecurityRslt PrescSecurityCheck(TPatientOrder po)
    {
        
        TPrescSecurityRslt psr = new TPrescSecurityRslt();
        TDrugSecurityRslt dsr = new TDrugSecurityRslt();
        /* 相互作用检查 */
        this.drugInteractionCheckerBean.Check(po).CopyInteractionCheckResultTo(dsr);
        /* 禁忌症审查 */
//        this.drugDiagCheckerBean.Check(po).CopyDrugDiagInfoRsltTo(dsr);
        /* 配伍审查 */
        this.drugIvEffectCheckerBean.Check(po).CopyIvEffectCheckResultTo(dsr);
        /* 过敏药物审查 */
        this.drugAllergenCheckerBean.Check(po).CopyDrugAllergenCheckRsltTo(dsr);
        /* 药物剂量审查 */
        this.drugDosageCheckerBean.Check(po).CopyDrugDosageCheckRsltTo(dsr);
        psr.setDsr(dsr);
        return psr;
    }

    /* 病人处方信息  */
    @Resource(name= "prescCheckerServiceBean")
    private IPrescCheckerService  pcs ;
    @Override
    public TPrescPatMasterBean getUsePrescDetail(String patient_id, Integer SelDay ,String[] back)
    {
        return pcs.getUsePrescDetail(patient_id,SelDay, back);
    }
    
    @Resource(name="prescDoctorCheckerBean")
    private IPrescDoctorChecker pdc;
    @Override
    public TPrescCheckRslt PrescCheck(String[] PrescInfo, String[][] PrescDetail, String[][] PrescDiag)
    {
        return null;
    }

    @Override
    public TPrescCheckRslt PrescCheckRs(String[] doctorInfo, String[] patientInfo, String[][] drugInfo,
            String[][] diagnosisInfo, String[][] sensitiveInfo, String[][] patSigns, String[] patOper)
    {
        return pdc.PrescCheckRs(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns, patOper);
    }
    
    @Override
    public String[] DrugAuthorizationCheck(String[] PrescBeanInfo)
    {
        return pdc.DrugAuthorizationCheck(PrescBeanInfo);
    }

    @Resource(name="operationInforServiceBean")
    private IOperationInforService operationIS;
    @Override
    public TOperationType[] getOperationTypes(String name)
    {
        
        return operationIS.getOperationTypes(name);
    }

    @Override
    public TOperationDrug[] getOperationDrugs(String operationID)
    {
        return operationIS.getOperationDrugs(operationID);
    }
}
