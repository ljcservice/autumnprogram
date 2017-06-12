package com.ts.service.pdss.ias.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.Beans.TAntiDrugUseRule;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.service.pdss.ias.Utils.QueryUtils;
import com.ts.service.pdss.ias.manager.IAntiDrugOperationCheck;

/**
 * 手术 抗菌药品审查 
 * @author Administrator
 */
@Deprecated
public class AntiDrugOperationCheckBean extends Persistent4DB implements IAntiDrugOperationCheck
{

    @Override
    public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp)
    {
        setQueryCode("PEAAS");
        TAntiDrugSecurityCheckResult antiDrug = new TAntiDrugSecurityCheckResult();
        antiDrug.setDrug_ID(antiDrugInp.getDrugID());
        antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
        antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
        try
        {
            String operLvl = antiDrugInp.getOperator().getOperLevel();
            if(operLvl != "1")
            {
                antiDrug.setDrug_ID(antiDrugInp.getDrugID());
                antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
                antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
                antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOperationCheck,"不是I切口",false,"抗菌药"));
                return antiDrug;
            }
            TAntiDrugUseRule adur = QueryUtils.getAntiDrugUseRule(antiDrugInp.getDrugID(), query);
            if(adur == null )
            	return antiDrug; 
            boolean flag = false;
            if("*".equals(adur.getUse_Dept()))
            {
            	flag = true;
            }
            else if(adur.getUse_Dept().indexOf(antiDrugInp.getDoctor().getDoctorDeptID()) != -1)
            {
            	flag = true;
            }
            if(flag)
            {
                antiDrug.setDrug_ID(antiDrugInp.getDrugID());
                antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
                antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
                antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOperationCheck,"药品未超范围",true,"抗菌药"));
            }
            else
            {
                antiDrug.setDrug_ID(antiDrugInp.getDrugID());
                antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
                antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
                antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOperationCheck,"药品超范围",false,"抗菌药"));
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return antiDrug;
    }
}
