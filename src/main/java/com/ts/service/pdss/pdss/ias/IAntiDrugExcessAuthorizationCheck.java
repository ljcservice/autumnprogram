package com.ts.service.pdss.pdss.ias;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugRslt;

/**
 * 超授权使用抗菌药物监测与提示
 * @author Administrator
 *
 */
public interface IAntiDrugExcessAuthorizationCheck 
{
	public TAntiDrugRslt Checker(TPatientOrder po ,TPatOrderDrug poDrug);
}
