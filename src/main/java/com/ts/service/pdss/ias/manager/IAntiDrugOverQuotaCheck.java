package com.ts.service.pdss.ias.manager;

import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;

/**
 * 超分线使用抗菌药物监测与提示
 * @author Administrator
 *
 */
public interface IAntiDrugOverQuotaCheck 
{
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp);
}
