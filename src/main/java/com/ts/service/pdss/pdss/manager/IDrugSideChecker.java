package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;

/**
 * 异常信号审查子
 * 不良反应
 * @author liujc
 *
 */
public interface IDrugSideChecker
{

    /**
     * 不良反应
     * @param po 医嘱对象
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po);

	public TDrugSecurityRslt Check(String[] drugIds, String[] adminIds,
			String[] sensitIds);
}
