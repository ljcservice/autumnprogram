package com.ts.service.pdss.ias.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.pdss.Beans.TDrugMap;
import com.ts.service.pdss.ias.manager.IAntiDrugSpecCheck;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;

/**
 * 特殊用抗菌药监测与提示
 * @author Administrator
 *
 */
@Service
@Transactional
public class AntiDrugSpecCheckBean extends Persistent4DB implements IAntiDrugSpecCheck
{
    @Override
    public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp)
    {
        setQueryCode("PDSS");
        TAntiDrugSecurityCheckResult antiDrug = new TAntiDrugSecurityCheckResult();
        try
        {
            boolean flag = false;
            TDrugMap antidrug = QuerySingleUtils.queryDrugMap(antiDrugInp.getDrugID(), query);
            if(antidrug == null)
            {
            	 antiDrug.setDrug_ID(antiDrugInp.getDrugID());
                 antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
                 antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
                 antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugSpecCheck,"没有该药品",false,"抗菌药"));
                 return antiDrug;
            }
            if("3".equals(antidrug.getANTI_LEVEL()))
            {
            	flag = true;
            }
            if(flag)
            {
                antiDrug.setDrug_ID(antiDrugInp.getDrugID());
                antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
                antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
                antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugSpecCheck,"该药品为特殊药品请及时送检标本！",false,"抗菌药"));
            }
            else
            {
                antiDrug.setDrug_ID(antiDrugInp.getDrugID());
                antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
                antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
                antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugSpecCheck,"非特殊用药",true,"抗菌药"));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return antiDrug;
    }
}
