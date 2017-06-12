package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Utils.DrugUtils;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugCheckResult;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugRslt;
import com.ts.service.pdss.pdss.ias.IAntiDrugExcessAuthorizationCheck;
import com.ts.service.pdss.pdss.ias.IAntiDrugOperationCheck;
import com.ts.service.pdss.pdss.ias.IAntiDrugSpecCheck;
import com.ts.service.pdss.pdss.manager.IAntiDrugSecurityChecker;


/**
 * 抗菌药物审核 
 * @author autumn
 *
 */
@Service("antiDrugSecurityChecker")
public class AntiDrugSecurityCheckerBean implements IAntiDrugSecurityChecker
{
    /**
     * 超授权使用抗菌药物监测与提示
     */
    @Resource(name="antiDrugExcessAuthorizationCheck")
    private IAntiDrugExcessAuthorizationCheck antiDrugExcessAuthorizationCheck;
    
    /**
     * 特殊用抗菌药监测与提示
     */
    @Resource(name="antiDrugSpecCheck")
    private IAntiDrugSpecCheck antiDrugSpecCheck;
    
    /**
     * 手术用抗菌药监测与提示
     */
    @Resource(name="antiDrugOperationCheckBean")
    private IAntiDrugOperationCheck AntiDrugOperationCheckBean;

    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
        TDrugSecurityRslt dsr = new TDrugSecurityRslt();
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        
        String patType = po.getPatType();
        try
        {
            for(TPatOrderDrug pod : pods)
            {
                TAntiDrugCheckResult  adcr = new TAntiDrugCheckResult();
                TDrug  drug =  po.getDrugMap(pod.getDrugID());
                //药品是否存在
                if(drug == null) continue;
                //是否是抗菌药物
                if(!DrugUtils.isKJDrug(pod.getDrugID()))continue;
                drug.setRecMainNo(pod.getRecMainNo());
                drug.setRecSubNo(pod.getRecSubNo());
                adcr.setDrug(drug);
                //手术用抗菌药监测与提示
                List<TAntiDrugRslt> adRSs  = AntiDrugOperationCheckBean.Checker(po, pod) ;
                if(adRSs != null && adRSs.size() > 0 ) {
                    adcr.setAntiRsCollection(drug,adRSs);
                }
                // 超授权使用抗菌药物监测与提示
                TAntiDrugRslt adRs = antiDrugExcessAuthorizationCheck.Checker(po, pod);
                if(adRs != null) adcr.addAntiRs(drug,adRs);
                // 特殊用抗菌药监测与提示
                adRs = antiDrugSpecCheck.Checker(po, pod);
                if(adRs != null) adcr.addAntiRs(drug,adRs);
                if(adcr.getAntiRs()!= null && adcr.getAntiRs().size() > 0)
                    dsr.regAntiDrugCheckResult(drug, adcr);
            }
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        return dsr;
    }

}
