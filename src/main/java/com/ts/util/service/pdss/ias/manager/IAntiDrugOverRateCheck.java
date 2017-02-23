package com.ts.service.pdss.ias.manager;

import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;

/**
 * 超耐药菌百分比用药监测与提示
 * @author Administrator
 *
 */
public interface IAntiDrugOverRateCheck 
{
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp);
}
