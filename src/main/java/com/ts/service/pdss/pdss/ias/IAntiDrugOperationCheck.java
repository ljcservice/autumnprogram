package com.ts.service.pdss.pdss.ias;

import java.util.List;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugRslt;

/**
 * 手术用抗菌药监测与提示
 * @author Administrator
 *
 */
public interface IAntiDrugOperationCheck
{

    /**
     * 抗菌药物审核
     * @param 
     * @return
     */
    public List<TAntiDrugRslt> Checker(TPatientOrder po ,TPatOrderDrug poDrug);
}
