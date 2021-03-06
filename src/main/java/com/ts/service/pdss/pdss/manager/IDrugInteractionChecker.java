package com.ts.service.pdss.pdss.manager;


import java.util.List;

import com.hitzd.his.Beans.TPatOrderDrug;
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
     * @throws Exception 
     */
    public TDrugSecurityRslt Check(String[] ids) throws Exception;
    
    /**
     * 医嘱对象
     * @param po
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder  po);
}
