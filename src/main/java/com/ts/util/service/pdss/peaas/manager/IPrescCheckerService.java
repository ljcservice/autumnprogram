package com.ts.service.pdss.peaas.manager;

import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;

/**
 * 处方功能服务接口 
 * @author Administrator
 *
 */
public interface IPrescCheckerService
{
    /**
     * 返回该病人 门诊的所有就诊信息 
     * @param patient_id
     * @param back
     * @return
     */
    public TPrescPatMasterBean getUsePrescDetail(String patient_id,Integer SelDay, String[] back);  
}
