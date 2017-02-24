package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.dao.DaoSupportPdss;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;
import com.ts.entity.pdss.pdss.RSBeans.TDrugIvEffectRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Cache.BeanRSCache;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugIvEffectChecker;


/**
 * 配伍审查
 * @author liujc
 *
 */
@Service
@Transactional
public class DrugIvEffectCheckerBean extends Persistent4DB implements  IDrugIvEffectChecker{

	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	
	@Resource(name = "pdssCache")
	private PdssCache pdssCache;

	private final static Logger log = Logger.getLogger(DrugIvEffectCheckerBean.class);
	
	/**
	 * 
	 * 改造完成
	 */
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
    	try
    	{
	        this.setQueryCode("PDSS");
	        TDrugSecurityRslt Result = new TDrugSecurityRslt();
	    	TPatOrderDrug[] pods = po.getPatOrderDrugs();
	    	// 药品分组
	    	Map<String, List<TPatOrderDrug>> group = new HashMap<String, List<TPatOrderDrug>>(); 
	    	for (int i = 0; i < pods.length; i++)
	    	{
	    		TPatOrderDrug pod = pods[i];
	    		//经过 用药途径标准码转换 只有用药途径为3的保留下来 
	    		//TAdministration adm = QuerySingleUtils.queryAdministration(pod.getAdministrationID(), query);
	    		TAdministration adm = pdssCache.queryAdministration(pod.getAdministrationID());
	    		
	    		if(adm == null)
	    		    continue;
	    		// TODO: 此处用3代表了注射的用药途径，正确的做法是调用DrugUtils.isZSDrug(adm.getADMINISTRATION_ID)或者
	    		// DrugUtils.isZSDrug(pod.getAdministrationID())，根据isZSDrug的数据来源来确定，来源为His选第二个，来源为param选第一个
	    		if ("3".equals(adm.getADMINISTRATION_ID()))
	    		{
	    			List<TPatOrderDrug> list = group.get(pod.getRecMainNo());
	    			if (list == null)
	    				list = new ArrayList<TPatOrderDrug>();
	    			list.add(pod);
	    			group.put(pod.getRecMainNo(), list);
	    		}
	    	}
	    	for (List<TPatOrderDrug> indexList : group.values())
	    	{
	    		if (indexList.size() == 1) continue;
	    		
	    		Map<String, TPatOrderDrug> podsMap = new HashMap<String, TPatOrderDrug>();
	    		String[] Drugs = new String[indexList.size()];
	    		for (int i = 0; i < indexList.size(); i++)
	    		{
	    			TPatOrderDrug pd = indexList.get(i);
	    			Drugs[i] = pd.getDrugID();
	    			podsMap.put(Drugs[i], pd);
	    		}

	    		// 找出药品信息
//		        List<TDrug> drugs  = QueryUtils.queryDrug(Drugs, null, query);
	    		List<TDrug> drugs = pdssCache.queryDrugListByIds(Drugs);
	    		
		        //Map<String, TDrug> drugs  = QueryUtils.queryDrug(pods, null, query);
	            //TDrug[] arrDrugs = drugs.values().toArray(new TDrug[0]);
	            
		        // 药品分组配对
		        for (int i = 0; i < drugs.size(); i++)
		        {
		        	TDrug drugA        = drugs.get(i);
		        	String drugIvCode1 = drugA.getIV_CLASS_CODE();
		        	for (int j = i + 1; j < drugs.size(); j++)
		        	{
		        		TDrug drugB = drugs.get(j);
		        		String drugIvCode2 = drugB.getIV_CLASS_CODE();
		        		/* 数据集缓存中寻找*/
		        		//TDrugIvEffectRslt cacheInfo = BeanRSCache.getDrugIvEffectRslt(drugA.getDRUG_NO_LOCAL(), drugB.getDRUG_NO_LOCAL());
		        		/* 配伍信息*/
		        		List<TDrugIvEffect> list = pdssCache.queryDrugIvEffect(drugIvCode1, drugIvCode2);
		        		
		        		/* 配伍信息*/
		        		//List list = QueryUtils.queryDrugIvEffect(drugIvCode1, drugIvCode2, "", query);
		        		
		        		/* 配伍数据集 */
		        		if(list != null && list.size()>0)
		        		{
		        			TDrugIvEffectRslt drugiveff = new TDrugIvEffectRslt();
		        			/* 对每一个返回的药品标注上 医嘱序号 */
	        		    	drugA.setRecMainNo(podsMap.get(drugA.getDRUG_NO_LOCAL()).getRecMainNo());
	        		    	drugA.setRecSubNo(podsMap.get(drugA.getDRUG_NO_LOCAL()).getRecSubNo());
	        		    	drugB.setRecMainNo(podsMap.get(drugB.getDRUG_NO_LOCAL()).getRecMainNo());
	        		    	drugB.setRecSubNo(podsMap.get(drugB.getDRUG_NO_LOCAL()).getRecSubNo());
	        		    	
		                    drugiveff.addIvEffect(podsMap.get(drugA.getDRUG_NO_LOCAL()), podsMap.get(drugB.getDRUG_NO_LOCAL()), list);
		                    drugiveff.setRecMainNo(podsMap.get(drugA.getDRUG_NO_LOCAL()).getRecMainNo());
		                    drugiveff.setRecSubNo(podsMap.get(drugA.getDRUG_NO_LOCAL()).getRecSubNo());
		                    drugiveff.setRecMainNo2(podsMap.get(drugB.getDRUG_NO_LOCAL()).getRecMainNo());
		                    drugiveff.setRecSubNo2(podsMap.get(drugB.getDRUG_NO_LOCAL()).getRecSubNo());
		                    Result.regIvEffectCheckResult(drugA,drugB,drugiveff);    
		        		}
		        		
		        	}
		        }
		        
		        // TODO: 此处用优化查询方法，会导致结果返回后进行三层循环遍历，以确定数据之间的相互关系，效率可能非常低下，
		        // 因此考虑此处不使用优化查询方法。
		        /*
		        List<DrugIvEffect> list = QueryUtils.queryDrugIvEffect(drugIvCodes, drugIvCodes, "", query);
		        for (int i = 0; i < drugs.size(); i++)
		        {
		        	for (int j = i + 1; j < drugs.size(); j++)
		        	{
		        		Drug drugA = drugs.get(i);
		        		Drug drugB = drugs.get(j);
		        		for (int k = 0; k < list.size(); k++)
		        		{
		        			DrugIvEffect die = list.get(k);
		        			if (((die.getIV_CLASS_CODE1().equals(drugA.getIV_CLASS_CODE())) && (die.getIV_CLASS_CODE2().equals(drugB.getIV_CLASS_CODE()))) ||
			        		    ((die.getIV_CLASS_CODE2().equals(drugA.getIV_CLASS_CODE())) &&	(die.getIV_CLASS_CODE1().equals(drugB.getIV_CLASS_CODE())))
			        		   )
		        			{
		        	        	Result.addIvEffect(drugA, drugB, die);
			        		}
		        		}
		        	}
		        }
		        */
	    	}
	        return Result;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		log.warn(e.getMessage());
    		return new TDrugSecurityRslt();
    	}
    }

	@Override
	public TDrugSecurityRslt Check(String[] drugIds, String[] recMainIds,
			String[] administrationIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
