package com.ts.service.pdss.pdss.impl;

import org.springframework.stereotype.Service;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Utils.DrugUtils;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.manager.IDrugHospitalManageChecker;
@Service
public class DrugHospitalManageCheckerBean implements IDrugHospitalManageChecker
{
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
        TDrugSecurityRslt dsr = new TDrugSecurityRslt();
        String patType = po.getPatType();
        TPatOrderDrug[] pods = po.getPatOrderDrugs();
        for(TPatOrderDrug pod : pods)
        {
            
            //门诊操作 
            if(patType != null  && patType.equals("1"))
            {
                
            }
            //住院操作
            else if (patType != null && patType.equals("0")){
                
            }    
            
            /* 判断是否 为抗菌药物  */
//            if(!DrugUtils.isKJDrug(pod.getDrugID()))
               
            //抗菌
        }
        
        return dsr;
    }

    
    
}
