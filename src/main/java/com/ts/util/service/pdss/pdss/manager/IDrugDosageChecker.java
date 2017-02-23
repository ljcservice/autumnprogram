package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;


/**
 * 药物剂量审查
 * @author liujc
 *
 */
public interface IDrugDosageChecker
{
    /**
     * 药物剂
     * @param po
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po);

	public TDrugSecurityRslt Check(String[] drugIds, String[] dosages,
			String[] performFreqDictIds, String[] startDates,
			String[] stopDates, String weight, String height, String birthDay);
}
