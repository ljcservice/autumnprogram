package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityResult;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;

/**
 * 保存审查结果信息 
 * @author Administrator
 *
 */
public interface IPatientSaveCheckResult
{
	/**
	 * 构成UUID
	 * @param uuid
	 */
	public void setNgroupnum(String uuid);
	
	/**
	 * 数据审核来源
	 * @param in_rs_type
	 */
	public void setIn_rs_type(String in_rs_type);
    /**
     * 保存审查的基本信息
     * @param po
     */
    public void savePatientCheckInfo(TPatientOrder po,TDrugSecurityRslt dsr);
    
    /**
     * 互动信息
     * @param po
     * @param dsr
     */
    public void saveDrugInteractionCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 配伍信息
     * 
     * @param po
     * @return
     */
    public void saveDrugIvEffectCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 禁忌症审查
     * @param po
     * @param dsr
     */
    public void saveDrugDiagCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 特殊人群
     * 
     * @param po
     * @return
     */
    public void saveDrugSpecPeopleCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 重复成份审
     * 
     * @param po
     * @return
     */
    public void saveDrugIngredientCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 用药途径审查
     * 
     * @param po
     * @return
     */
    public void saveDrugAdministrationCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 过敏药物审查
     * 
     * @param po
     * @return
     */
    public void saveDrugAllergenCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 药物剂量审查
     * 
     * @param po
     * @return
     */
    public void saveDrugDosageCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 异常信号审查
     * 
     * @param po
     * @return
     */
    public void saveDrugSideCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 抗菌药物审查
     * @param dsr
     */
    public void saveAntiDrugCheckInfo(TDrugSecurityRslt dsr);
    
    /**
     * 药物授权控制管理 
     * @param po
     * @param dsr
     */
    public void saveDrugUserAuthCheckInfo(TPatientOrder po , TDrugSecurityRslt dsr);
    
    /**
     * 保存审查的总体结构 
     * @param po
     * @param dsr
     */
    public void saveDrugSecurityCheckInfo(TPatientOrder po , TDrugSecurityRslt dsr);
    
    /**
     * 抗菌药物审查结果保存 
     */
    public void saveAntiDrugSecutity(TPatientOrder po,TAntiDrugSecurityResult[] adsr);
    
    /**
     * 预防用药信息保存
     * @param po
     */
    public void savePreveUseDrug(TPatientOrder po );
    
    /**
     * 治疗用药信息保存
     * @param po
     */
    public void saveTreatUseDrug(TPatientOrder po);
}
