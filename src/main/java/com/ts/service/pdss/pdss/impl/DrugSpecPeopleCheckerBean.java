package com.ts.service.pdss.pdss.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderInfoExt;
import com.hitzd.his.Beans.TPatient;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSpecPeopleRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugSpecPeopleChecker;

/**
 * 特殊人群
 * @author liujc
 *
 */
@Service
@Transactional
public class DrugSpecPeopleCheckerBean extends Persistent4DB implements IDrugSpecPeopleChecker
{
	private final static Logger log = Logger.getLogger(DrugSpecPeopleCheckerBean.class);
	
	@Resource(name = "pdssCache")
	private PdssCache pdssCache;
	
    /**
     *  特殊人群审查
     *  改造完毕
     */
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
    	try
    	{
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
	        /*  病人主要信息 */
	        TPatient patient = po.getPatient();
	        /* 病人扩展信息*/
	        TPatOrderInfoExt poiext = po.getPatInfoExt();
	        if(patient == null || poiext == null)
	            return result;
	        if (!(poiext.TheIsKidneyWhole() || poiext.TheIsLiverWhole() || 
	        	  poiext.TheIsPregnant() || poiext.TheIsLact() || 
	        	  patient.getChild() || patient.getOldMan()))
	        	return result;
	        /* 获得所有药品id */
//	        String[] drugids = CommonUtils.getPoDrugIDs(po);
	        // List<TDrug> drugs = QueryUtils.queryDrug(drugids, null, query);
	        // 利用map去掉重复的药品
	        //Map<String, TDrug> drugMap = QueryUtils.queryDrug(po.getPatOrderDrugs(), null, query);
//	        Map<String, TDrug> drugMap = pdssCache.queryDrugMap(po.getPatOrderDrugs());
	        
	        //Map<String, TDrugUseDetail> duds = QueryUtils.queryDrugDud(drugMap, query);
	        Map<String, TDrugUseDetail> duds = pdssCache.queryDrugDudMap(po.DrugMap()); 
	        
	        for(TPatOrderDrug pod : po.getPatOrderDrugs())
	        {
	            /* 得到药品*/ 
	            TDrug drug =  po.getDrugMap(pod.getDrugID()); // CommonUtils.getDrugInfoOne(drugs, pod);
	            if(drug == null)
	                continue;
	            /* 药品信息*/
	            TDrugUseDetail dud = duds.get(drug.getDRUG_CLASS_ID());
	            /*
	            for(int i= 0 ;i<duds.size();i++)
	            {
	                if(duds.get(i).getDRUG_CLASS_ID().equals(drug.getDRUG_CLASS_ID()))
	                {
	                    dud = duds.get(i);
	                    break;
	                }
	            }
	            */
	            if(dud == null)
	                continue;
	            TDrugSpecPeopleRslt specPeopRslt = new TDrugSpecPeopleRslt();
	            /*  肾功不全*/
	            if(poiext.TheIsKidneyWhole()&& dud.getRENAL_INDI() != null && "1".equals(dud.getRENAL_INDI()))
	            {
	                specPeopRslt.addRenal(dud);
	            }
	            /* 肝功不全*/
	            if(poiext.TheIsLiverWhole() && dud.getHEPATICAL_INDI() != null && "1".equals(dud.getHEPATICAL_INDI()))
	            {
	                specPeopRslt.addHepatical(dud);
	            }
	            /* 判断男女 女则为false*/
	            if(!patient.TheIsSex())
	            {
	                /* 孕妇*/
	                if(poiext.TheIsPregnant() && dud.getPREGNANT_INDI() != null && !"".equals(dud.getPREGNANT_INDI()))
	                {
	                    specPeopRslt.addPregnant(dud);
	                }
	                /* 哺乳期*/
	                if(poiext.TheIsLact() && dud.getLACT_INDI() != null && !"".equals(dud.getPREGNANT_INDI()))
	                {
	                    specPeopRslt.addLact(dud);
	                }
	            }
	            /* 儿童*/
	            if(patient.getChild() && dud.getKID_INDI() != null && !"".equals(dud.getKID_INDI()))
	            {
	                long low = Long.parseLong(dud.getKID_LOW());
	                long high = Long.parseLong(dud.getKID_HIGH());
	                long days = patient.calAgeDays();
	                if ((low <= days) && (days <= high)) 
	                {
	                    specPeopRslt.addChild(dud);
	                }
	            }
	            /* 老人*/
	            else if(patient.getOldMan() && dud.getOLD_INDI() != null && !"".equals(dud.getOLD_INDI()))
	            {
	                specPeopRslt.addOld(dud);
	            }
	            // 此处drug来自于缓存，改动drug的信息，可以直接set，如果对drug信息有改动，则要new一个新的drug
	            TDrug drg = new TDrug(drug);
	            specPeopRslt.setDrug(drg);
	            specPeopRslt.setRecMainNo(pod.getRecMainNo());
	            specPeopRslt.setRecSubNo(pod.getRecSubNo());
	            
	            drg.setRecMainNo(pod.getRecMainNo());
	            drg.setRecSubNo(pod.getRecSubNo());
	            
	            if(specPeopRslt.getIsFlag())
	            	result.regDrugSpecPeopleCheckResult(drg, specPeopRslt);
	        }
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
	public TDrugSecurityRslt Check(String[] drugIds, String birthDay,
			String patType, String isLiverWhole, String isKidneyWhole) {
		// TODO Auto-generated method stub
		return null;
	}



}
