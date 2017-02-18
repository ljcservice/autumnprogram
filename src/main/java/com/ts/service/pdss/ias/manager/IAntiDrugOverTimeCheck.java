package com.ts.service.pdss.ias.manager;

import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;

/**
 * 超疗程抗菌药物使用监测与提示
 * @author Administrator
 *
 */
public interface IAntiDrugOverTimeCheck
{
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrug);
}
