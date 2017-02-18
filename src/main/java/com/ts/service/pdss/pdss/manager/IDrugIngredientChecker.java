package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;


/**
 * 重复成份审查子模块
 * @author liujc
 *
 */
public interface IDrugIngredientChecker
{

    public TDrugSecurityRslt check(TPatientOrder po);

	public TDrugSecurityRslt Check(String[] drugIds);
    
}
