package com.ts.service.pdss.pdss.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;
import com.ts.entity.pdss.pdss.RSBeans.TAdministrationRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
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
	
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
    	try
    	{
	        setQueryCode("PDSS");
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
	        TPatOrderDrug[] pods = po.getPatOrderDrugs();
	        String[] drugids = new String[pods.length];
	        for (int i = 0; i < pods.length; i++)
	        {
	            drugids[i] = pods[i].getDrugID();
	        }
	        List<TDrug> drugs         = QueryUtils.queryDrug(drugids, null, query);
	        List<TDrugUseDetail> duds = QueryUtils.queryDrugDud(drugs, query);
	        /* 进行用药途径比对 */
	        for (int i = 0; i < pods.length; i++)
	        {
	            TPatOrderDrug pod = pods[i];
	            /* 用途径标准码*/
	            String administandid = "";
	            List<TAdministration> administandids = QueryUtils.queryAdministration(new String[]{pod.getAdministrationID()}, null, query);
	            if (administandids == null || administandids.size() <= 0)
	                continue;
	            administandid        = administandids.get(0).getADMINISTRATION_ID();
	            TDrug drugRs         = null;
	            TDrugUseDetail dudRs = null;
	            String inforLevelRs  = "";
	            String drugid        = pod.getDrugID();
	            String drugClassCode = "";
	            List<TDrug> drug = QueryUtils.queryDrug(new String[]{drugid},null, query);
	            if (drug.size() > 0)
	            {
	                drugRs = drug.get(0);
	                drugClassCode = drug.get(0).getDRUG_CLASS_ID();
	            }
	            for (TDrugUseDetail dud : duds)
	            {
	                dudRs = dud;
	                if (dud.getDRUG_CLASS_ID().equals(drugClassCode))
	                {
	                    /* 禁止的用药途径 */
	                    if (dud.getFORBID_RUID() != null)
	                    {
	                        if (("," + dud.getFORBID_RUID() + ",").indexOf("," + administandid + ",") != -1)
	                        {
	                            inforLevelRs = "R";
	                            break;
	                        }
	                    }
	                    /* 不宜的用药途径 */
	                    if (dud.getINADVIS_RTID() != null)
	                    {
	                        if (("," + dud.getINADVIS_RTID() + ",").indexOf("," + administandid + ",") != -1)
	                        {
	                            inforLevelRs = "Y";
	                            break;
	                        }
	                    }
	                    /* 需注意的用药途径 */
	                    if (dud.getADVERT_RTID() != null)
	                    {
	                        if (("," + dud.getADVERT_RTID() + ",").indexOf("," + administandid + ",") != -1)
	                        {
	                            inforLevelRs = "G";
	                            break;
	                        }
	
	                    }
	                }
	            }
	            if(!"".equals(inforLevelRs))
	            {
	                /* 对每一个返回的药品标注上 医嘱序号 */
	                drugRs.setRecMainNo(pod.getRecMainNo());
	                drugRs.setRecSubNo(pod.getRecSubNo());
	                TAdministrationRslt adminirt = new TAdministrationRslt();
	                adminirt.setRecMainNo(pod.getRecMainNo());
	                adminirt.setRecSubNo(pod.getRecSubNo());
	                adminirt.addRslt(pod, drugRs, dudRs, inforLevelRs);
	                result.regAdministrationCheckResult(drugRs, adminirt);
	            }
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
	public TDrugSecurityRslt Check(String[] drugIds, String[] adminIds) {
		// TODO Auto-generated method stub
		return null;
	}
}
