package com.ts.service.pdss.pdss.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderDrugSensitive;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.Beans.TAllergIngrDrug;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TDrugAllergenRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Utils.CommonUtils;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugAllergenChecker;

/**
 * 过敏药物审查
 * 
 * @author liujc
 * 
 */
@Service
@Transactional
public class DrugAllergenCheckerBean extends Persistent4DB implements IDrugAllergenChecker
{
	private final static Logger log = Logger.getLogger(DrugAllergenCheckerBean.class);
	
    /**
     * 过敏药物审查
     */
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
    	try
    	{
	        this.setQueryCode("PDSS");
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
	        /* 医嘱药物所有过敏信息类 */
	        TPatOrderDrugSensitive[] podsensitive = po.getPatOrderDrugSensitives();
	        if (podsensitive == null)
	            return result;
	        /* 获得医嘱中所有药物id */
	        String[] drugIDs = new String[po.getPatOrderDrugs().length];
	        for (int i = 0; i < po.getPatOrderDrugs().length; i++)
	        {
	            drugIDs[i] = po.getPatOrderDrugs()[i].getDrugID();
	        }
	        /* 获得医嘱中所有药物信息 */
	        List<TDrug> drugs = (List<TDrug>) QueryUtils.queryDrug(drugIDs, null,query);
	        /* 
	         * TODO 得到的过敏信息数据 放入得字段是否有误？ getDrugAllergenID 或 getPatOrderDrugSensitiveID
	         * 所有过敏信息 
	         * */
	        String[] drugAllergenIDs = new String[podsensitive.length];
	        for (int i = 0; i < podsensitive.length; i++)
	        {
	            drugAllergenIDs[i] = podsensitive[i].getPatOrderDrugSensitiveID();
	        }
	        /* 将过敏的药品放入结果集中 */
	        for(int i = 0 ;i<po.getPatOrderDrugs().length ;i++)
	        {
	            TPatOrderDrug pod = po.getPatOrderDrugs()[i];
	            TDrug drug = CommonUtils.getDrugInfoOne(drugs, pod);
	            if(drug == null)
	                continue;
	//            List<TAllergIngrDrug> aids = (List<TAllergIngrDrug>) QueryUtils.queryAllergen(drugAllergenIDs,new String[]{drug.getDRUG_CLASS_ID()}, null, query);
	            List<TAllergIngrDrug> aids = (List<TAllergIngrDrug>) QueryUtils.queryAllergen(drugAllergenIDs,drug.getDRUG_CLASS_ID(), query);
	            if(aids != null && aids.size() > 0 )
	            {
	                TDrugAllergenRslt dart  = new TDrugAllergenRslt();
	                dart.addDrug(drug, aids);
	                dart.setRecMainNo(pod.getRecMainNo());
	                dart.setRecSubNo(pod.getRecSubNo());
	                /* 对每一个返回的药品标注上 医嘱序号 */
	                drug.setRecMainNo(pod.getRecMainNo());
	                drug.setRecSubNo(pod.getRecSubNo());
	                result.regAllergenCheckResult(drug, dart);    
	            }
	        }
	        drugs = null;
	        return result;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		log.warn(e.getMessage());
    		return new TDrugSecurityRslt();
    	}
    }

	@Override
	public TDrugSecurityRslt Check(String[] drugIds, String[] sensitIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
