package com.ts.service.pdss.ias.manager;

import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;

/**
 * 超使用时机（预防使用）抗菌药物监测与提示
 * @author Administrator
 *
 */
public interface IAntiDrugOverMomentCheck 
{
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp);
}
