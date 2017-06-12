package com.ts.service.pdss.ias.manager;

import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;

/**
 * 超授权使用抗菌药物监测与提示
 * @author Administrator
 *
 */
@Deprecated
public interface IAntiDrugExcessAuthorizationCheck 
{
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp);
}
