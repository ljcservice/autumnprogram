package com.ts.service.pdss.ias.manager;

import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;


/**
 * 手术用抗菌药监测与提示
 * @author Administrator
 *
 */
@Deprecated
public interface IAntiDrugOperationCheck
{

    /**
     * 
     * @param antiDrugInp
     * @return
     */
    public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp);
}
