package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugRepeat;
import com.ts.entity.pdss.pdss.RSBeans.TDrugIngredientRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugIngredientChecker;

/**
 * 重复成份审查子模块
 * @author liujc
 *
 */
@Service
public class DrugIngredientCheckerBean extends Persistent4DB implements IDrugIngredientChecker
{
	private final static Logger log = Logger.getLogger(DrugIngredientCheckerBean.class);
	
	@Resource(name = "pdssCache")
	private PdssCache pdssCache;
	
    @Override
    /**
     *  重复成分审查方法
     *  po 医嘱
     *  改造完毕
     */
    public TDrugSecurityRslt check(TPatientOrder po)
    {
    	try
    	{
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
	        TPatOrderDrug[] pods = po.getPatOrderDrugs();
	        //for(TPatOrderDrug pod :pods)
	        int counter = pods.length;
	        for (int i = 0; i < counter; i++)
	        {
	        	TPatOrderDrug podA = pods[i];
	        	// 此处要修改drugA的医嘱号，所以得new一个新的drug
	        	TDrug drugA = po.getDrugMap(podA.getDrugID()); // CommonUtils.getDrugInfoOne(drugs, pod);drugMap.get(podA.getDrugID());
	        	if(drugA == null) continue;
//	        	drugA = new TDrug(drugA);
	        	List<TDrug> listdrugs      = new ArrayList<TDrug>(); 
	        	List<TDrugRepeat> listInfo = new ArrayList<TDrugRepeat>();
	        	for (int j = i + 1; j < counter; j++ )
	        	{
//	        		if (i == j) continue;
	        		// TODO: 此处可以继续优化，A和B重复时，直接将结果放到A和B的里面即可
	        		// 有空时再优化
		        	TPatOrderDrug podB = pods[j];
		        	// 此处要修改drugB的医嘱号，所以得new一个新的drug
	        		TDrug drugB =  po.getDrugMap(podB.getDrugID());
	        		if(drugB == null) continue;
	        		if (podA.getDrugID().equalsIgnoreCase(podB.getDrugID()))  continue;
//	        		drugB = new TDrug(drugB);
//	        		if(drugA.getDRUG_NO_LOCAL().equalsIgnoreCase(drugB.getDRUG_NO_LOCAL())) continue;
	        		String drugClassIdA = drugA.getDRUG_CLASS_ID();
	        		String drugClassIdB = drugB.getDRUG_CLASS_ID();
	        		if(drugClassIdA == null || drugClassIdB == null) continue;
	        		TDrugRepeat drugRep = pdssCache.queryDrugRepeat(drugClassIdA, drugClassIdB);
	        		if(drugRep == null) continue;
	        		drugB.setRecMainNo(podB.getRecMainNo());
	        		drugB.setRecSubNo(podB.getRecSubNo());
	        		listdrugs.add(drugB);
	        		listInfo.add(drugRep);
//	        		if(drugA.getDRUG_CLASS_ID().equals(drugB.getDRUG_CLASS_ID()))
//	        		{
//	        			drugB.setRecMainNo(podB.getRecMainNo());
//	        			drugB.setRecSubNo(podB.getRecSubNo());
//	        			listdrugs.add(drugB);
//	        		}
	        	}
	        	if(listdrugs != null && listdrugs.size() > 0 )
	        	{
	        		TDrugIngredientRslt igRs = new TDrugIngredientRslt();
	        		igRs.addDrugRepeat(drugA, listdrugs,listInfo);
	        		igRs.setRecMainNo(podA.getRecMainNo());
	        		igRs.setRecSubNo(podA.getRecSubNo());
	        		/* 对每一个返回的药品标注上 医嘱序号 */
	        		drugA.setRecMainNo(podA.getRecMainNo());
	        		drugA.setRecSubNo(podA.getRecSubNo());
	        		result.regIngredientCheckResult(drugA, igRs);    
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
	public TDrugSecurityRslt Check(String[] drugIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
