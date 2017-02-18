package com.ts.service.pdss.pdss.manager;


import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
/**
 * 相互作用审查子模块
 * @author liujc
 *
 */
public interface IDrugInteractionChecker
{
    /**
     *  审查
     * @return
     */
    public TDrugSecurityRslt Check(String[] _Strs);
    
    /**
     * 医嘱对象
     * @param po
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po);
}
