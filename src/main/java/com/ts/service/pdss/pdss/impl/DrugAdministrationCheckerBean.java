package com.ts.service.pdss.pdss.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDosage;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;
import com.ts.entity.pdss.pdss.RSBeans.TAdministrationRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.manager.IDrugAdministrationChecker;

/**
 * 用药途径审查子模块
 * @author liujc
 *
 */
@Service
@Transactional 
public class DrugAdministrationCheckerBean extends Persistent4DB implements IDrugAdministrationChecker
{
	private final static Logger log = Logger.getLogger(PatientSaveCheckResult.class);
	
	@Resource(name = "pdssCache")
	private PdssCache pdssCache;
	
	/**
	 * 
	 * 改造完毕 
	 * ok
	 */
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
    	try
    	{
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
	        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//	        Map<String, TDrug> mapDrug = pdssCache.queryDrugMap(pods);
	        if(pods == null) return new TDrugSecurityRslt();
	        int counter = pods.length;
	        for (int i = 0; i < counter; i++)
	        {
	        	TPatOrderDrug pod     = pods[i];
	        	System.out.print("药品名称 " + pod.getDrugName());
	        	TDrug drug            =  po.getDrugMap(pod.getDrugID());
	        	if(drug == null ) continue;
	        	System.out.print(";本地药品：" + drug.getDRUG_NAME_LOCAL());
	        	TAdministration admin = pdssCache.queryAdministration(pod.getAdministrationID());
	        	if(admin == null) continue;
	        	System.out.print(";本地途径：" + admin.getADMINISTRATION_NAME_LOCAL());
	        	String inforLevelRs ="";
	        	if(drug.getDOSE_CLASS_ID() == null) continue;
	        	System.out.println(";getDOSE_CLASS_ID：" + drug.getDOSE_CLASS_ID());
	        	List<TDrugDosage> ddgs = pdssCache.getDdg(drug.getDOSE_CLASS_ID());
	        	System.out.print(ddgs);
	        	if(ddgs == null) continue;
	        	TDrugUseDetail dud = new TDrugUseDetail();
	        	System.out.print("" + dud);
	        	boolean flag = false;
	        	String administrationTrue = "";
	        	for(TDrugDosage ddg : ddgs)
	        	{
	        	    if(ddg.getADMINISTRATION_ID().equals(admin.getADMINISTRATION_ID()))
	        	    {
	        	        flag = true;
	        	        break;
	        	    }
	        	    administrationTrue = ddg.getADMINISTRATION_ID();  
	        	}
	        	if(flag) continue;
	        	inforLevelRs = "R";
//	        	admin = pdssCache.queryAdministration(administrationTrue);
	        	dud.setFORBID_RUID(admin.getADMINISTRATION_ID());
	        	dud.setFORBID_CAUSE("禁止给药途径为" + admin.getADMINISTRATION_NAME_LOCAL() + "。");
	        	
//	        	TDrugUseDetail dud    = pdssCache.getDud(drug.getDRUG_CLASS_ID());
//	        	/* 需注意的用药途径 */
//	        	if(dud == null ) continue;
//                if (dud.getADVERT_RTID() != null)
//                {
//                    if (("," + dud.getADVERT_RTID() + ",").indexOf("," + admin.getADMINISTRATION_ID() + ",") != -1)
//                    {
//                        inforLevelRs = "G";
//                    }
//
//                }
//                /* 不宜的用药途径 */
//                if (dud.getINADVIS_RTID() != null)
//                {
//                    if (("," + dud.getINADVIS_RTID() + ",").indexOf("," + admin.getADMINISTRATION_ID() + ",") != -1)
//                    {
//                        inforLevelRs = "Y";
//                    }
//                }
//                /* 禁止的用药途径 */
//                if (dud.getFORBID_RUID() != null)
//                {
//                    if (("," + dud.getFORBID_RUID() + ",").indexOf("," + admin.getADMINISTRATION_ID() + ",") != -1)
//                    {
//                        inforLevelRs = "R";
//                    }
//                }                
	            if(!"".equals(inforLevelRs))
	            {
	            	drug.setRecMainNo(pod.getRecMainNo());
	            	drug.setRecSubNo(pod.getRecSubNo());
	                TAdministrationRslt adminirt = new TAdministrationRslt();
	                adminirt.setRecMainNo(pod.getRecMainNo());
	                adminirt.setRecSubNo(pod.getRecSubNo());
	                adminirt.addRslt(pod, drug, dud, inforLevelRs);
	                result.regAdministrationCheckResult(drug, adminirt);
	            }
	        }
	        
//	        TDrugSecurityRslt result = new TDrugSecurityRslt();
//	        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//	        String[] drugids = new String[pods.length];
//	        for (int i = 0; i < pods.length; i++)
//	        {
//	            drugids[i] = pods[i].getDrugID();
//	        }
//	        //List<TDrug> drugs = QueryUtils.queryDrug(drugids, null, query);
//	        List<TDrug> drugs = pdssCache.queryDrugListByIds(drugids);
//	        
//	        List<TDrugUseDetail> duds = pdssCache.queryDrugDudList(drugs);
//	        
//	        /* 进行用药途径比对 */
//	        for (int i = 0; i < pods.length; i++)
//	        {
//	            TPatOrderDrug pod = pods[i];
//	            /* 用途径标准码*/
//	            String administandid = "";
//	            //List<TAdministration> administandids = QueryUtils.queryAdministration(new String[]{pod.getAdministrationID()}, null, query);
//	            List<TAdministration> administandids = pdssCache.queryAdministrations(new String[]{pod.getAdministrationID()});
//	       	 
//	            if (administandids == null || administandids.size() <= 0)
//	                continue;
//	            administandid        = administandids.get(0).getADMINISTRATION_ID();
//	            TDrug drugRs         = null;
//	            TDrugUseDetail dudRs = null;
//	            String inforLevelRs  = "";
//	            String drugid        = pod.getDrugID();
//	            String drugClassCode = "";
//	            drugRs = pdssCache.queryDrugById(drugid);
//	            drugClassCode = drugRs.getDRUG_CLASS_ID();
//	            
//	            for (TDrugUseDetail dud : duds)
//	            {
//	                dudRs = dud;
//	                if (dud.getDRUG_CLASS_ID().equals(drugClassCode))
//	                {
//	                    /* 禁止的用药途径 */
//	                    if (dud.getFORBID_RUID() != null)
//	                    {
//	                        if (("," + dud.getFORBID_RUID() + ",").indexOf("," + administandid + ",") != -1)
//	                        {
//	                            inforLevelRs = "R";
//	                            break;
//	                        }
//	                    }
//	                    /* 不宜的用药途径 */
//	                    if (dud.getINADVIS_RTID() != null)
//	                    {
//	                        if (("," + dud.getINADVIS_RTID() + ",").indexOf("," + administandid + ",") != -1)
//	                        {
//	                            inforLevelRs = "Y";
//	                            break;
//	                        }
//	                    }
//	                    /* 需注意的用药途径 */
//	                    if (dud.getADVERT_RTID() != null)
//	                    {
//	                        if (("," + dud.getADVERT_RTID() + ",").indexOf("," + administandid + ",") != -1)
//	                        {
//	                            inforLevelRs = "G";
//	                            break;
//	                        }
//	
//	                    }
//	                }
//	            }
//	            if(!"".equals(inforLevelRs))
//	            {
//	                /* 对每一个返回的药品标注上 医嘱序号 */
//	                drugRs.setRecMainNo(pod.getRecMainNo());
//	                drugRs.setRecSubNo(pod.getRecSubNo());
//	                TAdministrationRslt adminirt = new TAdministrationRslt();
//	                adminirt.setRecMainNo(pod.getRecMainNo());
//	                adminirt.setRecSubNo(pod.getRecSubNo());
//	                adminirt.addRslt(pod, drugRs, dudRs, inforLevelRs);
//	                result.regAdministrationCheckResult(drugRs, adminirt);
//	            }
//	        }
	        return result;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		log.warn(this.getClass().toString() + ":" + e.getMessage());
    		return new TDrugSecurityRslt();
    	}
    }

	@Override
	public TDrugSecurityRslt Check(String[] drugIds, String[] adminIds) {
		// TODO Auto-generated method stub
		return null;
	}
}
