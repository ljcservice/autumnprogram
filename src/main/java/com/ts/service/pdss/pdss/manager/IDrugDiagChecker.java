package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;

/**
 *  禁忌症审查子模块
 * @author liujc
 *
 */
public interface IDrugDiagChecker
{
    /**
     * 禁忌症审查
     * check
     * @param drugs
     * @param diagnosis
     * @return
     */
    public TDrugSecurityRslt Check(String[] drugs ,String[] diagnosis);
    /**
     * 禁忌症审查 
     * @param drugs
     * @param diagnosis
     * @return
     */
    public TDrugSecurityRslt Check( String param);
}
