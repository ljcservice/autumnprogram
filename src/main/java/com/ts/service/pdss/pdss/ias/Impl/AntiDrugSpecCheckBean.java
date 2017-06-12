package com.ts.service.pdss.pdss.ias.Impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.Beans.TDrugMap;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugRslt;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;
import com.ts.service.pdss.pdss.ias.IAntiDrugSpecCheck;

/**
 * 特殊用抗菌药监测与提示
 * @author Administrator
 *
 */
@Service("antiDrugSpecCheck")
public class AntiDrugSpecCheckBean extends Persistent4DB implements IAntiDrugSpecCheck
{
    @Override
    public TAntiDrugRslt Checker(TPatientOrder po ,TPatOrderDrug poDrug)
    {
        setQueryCode("PDSS");
        TAntiDrugRslt drugRs = null;
        try
        {
            boolean flag = false;
            TDrugMap antidrug = QuerySingleUtils.queryDrugMap(poDrug.getDrugID(), query);
            if("3".equals(antidrug.getANTI_LEVEL()))
            {
            	flag = true;
            }
            if(flag)
            {
                drugRs = (new TAntiDrugRslt(TAntiDrugRslt.AntiDrugSpecCheck,"该药品为[特殊级抗菌药品]使用前 请及时送检标本！并且用药前请进行会诊！",false,"抗菌药"));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return drugRs;
    }
}
