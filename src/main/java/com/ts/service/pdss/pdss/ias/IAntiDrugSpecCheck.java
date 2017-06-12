package com.ts.service.pdss.pdss.ias;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugRslt;

/**
 * 特殊用抗菌药监测与提示
 * @author Administrator
 *
 */
public interface IAntiDrugSpecCheck
{
    /**
     * 审核
     * @param antiDrugInp
     * @return
     */
    public TAntiDrugRslt Checker(TPatientOrder po ,TPatOrderDrug poDrug);
}
