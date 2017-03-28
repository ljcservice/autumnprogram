package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.dao.DaoSupportPdss;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.RSBeans.TDrugInteractionRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.cache.CacheProcessor;
import com.ts.service.cache.CacheTemplate;
import com.ts.service.pdss.pdss.Cache.DBSnapshot;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugInteractionChecker;
import com.ts.util.PageData;

/**
 * 相互作用审查子模块
 * @author liujc
 * 
 */
@Service
public class DrugInteractionCheckerBean extends Persistent4DB implements  IDrugInteractionChecker
{
    private final static Logger log = Logger.getLogger(DrugInteractionCheckerBean.class);
    
	@Resource(name = "pdssCache")
	private PdssCache pdssCache; 
    
    /**
     * 互动信息查询
     * 改造完成，应用缓存
     */
    @Override
    public TDrugSecurityRslt Check(TPatientOrder  po)
    {
    	try
    	{
            /* 设置访问数据库代码 */
//            this.setQueryCode("PDSS");
            /* 药品组合情况*/
            TDrugSecurityRslt dsr = new TDrugSecurityRslt() ;
            /* 查找drugs 中的所有要药品*/
            TPatOrderDrug[] pods = po.getPatOrderDrugs();
            // 通过map过滤掉重复的药品码
            Map<String, TDrug> drugs  = pdssCache.queryDrugMap(pods);
//            List<String> ids = new ArrayList<String>();
//            for(TPatOrderDrug tp:pods){
//            	ids.add(tp.getDrugID());
//            }
//            List<TDrug> drugs = pdssCache.queryDrugListByIds(ids);
            
            // 下面循环无重复的药品码之间的配对
            // 将配对结果放入缓存中
            // 后面所有的审查结果从缓存中取出来
            // 只有这个循环读取数据库
            for (int i = 0; i < pods.length; i++)
            {
            	TPatOrderDrug podA = pods[i];
            	TDrug drugA = drugs.get(podA.getDrugID());
            	if(drugA == null || "".equals(drugA.getDRUG_ID())) continue;
            	Long ff = System.currentTimeMillis();
            	for (int j = i + 1 ; j < pods.length; j++)
            	{
            		if (i == j) continue;
            		TPatOrderDrug podB = pods[j];
            		TDrug drugB = drugs.get(podB.getDrugID());
            		if(drugB == null || "".equals(drugB.getDRUG_ID())) continue;
            		if (podA.getDrugID().equalsIgnoreCase(podB.getDrugID()))
            			continue;
                    // 此处从缓存中取结果=====================================
                    String key1 = podA.getDrugID();
                    String key2 = podB.getDrugID();
//                    TDrug drugA = pdssCache.queryDrugById(key1);
//                    TDrug drugB = pdssCache.queryDrugById(key2);
                    TDrugInteractionRslt diRslt = pdssCache.queryDrugInteraction(drugA,drugB);
                    if ((diRslt != null) && (diRslt.getDrugInteractionInfo().length > 0))
                    {
                        TDrugInteractionRslt copyCache = diRslt.deepClone();
                    	copyCache.setRecMainNo(podA.getRecMainNo());
                    	copyCache.setRecSubNo(podA.getRecSubNo());
                    	copyCache.setRecMainNo2(podB.getRecMainNo());
                    	copyCache.setRecSubNo2(podB.getRecSubNo());
                    	/* 对每一个返回的药品标注上 医嘱序号 */
                    	// 需要判断diRslt中的drugA和这里的drugA是否一样，不一样的话需要互换一下
                    	// 确保这里的A始终在diRslt的A上面
                        if (!copyCache.getDrugA().getDRUG_NO_LOCAL().equals(podA.getDrugID()))
                        {
                        	TDrug drugTemp = copyCache.getDrugB();
                        	copyCache.setDrugB(copyCache.getDrugA());
                        	copyCache.setDrugA(drugTemp);
                        }
                        copyCache.getDrugA().setRecMainNo(podA.getRecMainNo());
                       	copyCache.getDrugA().setRecSubNo(podA.getRecSubNo());
                       	/* 对每一个返回的药品标注上 医嘱序号 */
                       	copyCache.getDrugB().setRecMainNo(podB.getRecMainNo());
                       	copyCache.getDrugB().setRecSubNo(podB.getRecSubNo());
                       	dsr.regInteractionCheckResult(copyCache.getDrugA(), copyCache.getDrugB(), copyCache);
                    }
            	}
            	
            	System.out.println("次循环结束时间 ： " + (System.currentTimeMillis() - ff));
            }
//            drugs = null;
            return  dsr;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		log.warn(e.getMessage());
    		return new TDrugSecurityRslt();
    	}
    }
    
    
    /*
            /* 组合本地药品
            for (int i = 0 ; i < pods.length ; i++)
            {
                TDrug drugA = drugs.get(pods[i].getDrugID()); // CommonUtils.getDrugInfoOne(drugs, pods[i]);
                if(drugA == null)
                    continue;
                for(int j = i + 1; j < pods.length; j++)
                {
                	//System.out.println("循环" + i + "-" + j + " 耗时：" + (System.currentTimeMillis() - xx));
                    TDrug drugB = drugs.get(pods[j].getDrugID()); //CommonUtils.getDrugInfoOne(drugs,pods[j]);
                    if(drugB == null || drugA.getDRUG_NO_LOCAL().equals(drugB.getDRUG_NO_LOCAL()))
                        continue;
                    TDrugInteractionRslt diRslt = null;
                    
                    // 此处从缓存中取结果=====================================
                    String key1 = drugA.getDRUG_NO_LOCAL();
                    String key2 = drugB.getDRUG_NO_LOCAL();
                    if (drugA.getDRUG_NO_LOCAL().compareTo(drugB.getDRUG_NO_LOCAL()) > 0)
                    {
                    	key1 = drugB.getDRUG_NO_LOCAL();
                        key2 = drugA.getDRUG_NO_LOCAL();
                    }
                    diRslt = BeanRSCache.getDrugInteraction(key1, key2);
                    if (diRslt != null)
                    {
                        if(diRslt.getDrugInteractionInfo().length > 0)
                        {
                        	TDrugInteractionRslt copyCache = new TDrugInteractionRslt();

                        	/* 对每一个返回的药品标注上 医嘱序号 
                            drugA.setRecMainNo(pods[i].getRecMainNo());
                            drugA.setRecSubNo(pods[i].getRecSubNo());
                            /* 对每一个返回的药品标注上 医嘱序号 
                            drugB.setRecMainNo(pods[j].getRecMainNo());
                            drugB.setRecSubNo(pods[j].getRecSubNo());
                            
                        	copyCache.addDrugInfo(drugA, drugB, diRslt.getListDruginfo());
                        	
                        	copyCache.setRecMainNo(pods[i].getRecMainNo());
                        	copyCache.setRecSubNo(pods[i].getRecSubNo());
                        	copyCache.setRecMainNo2(pods[j].getRecMainNo());
                        	copyCache.setRecSubNo2(pods[j].getRecSubNo());
                            
                        	dsr.regInteractionCheckResult(drugA, drugB, copyCache);
                        }
                    	continue;
                    }
                    // 以上从缓存中取结果=======================================
                    
                    diRslt = new TDrugInteractionRslt();
                    /* 将结果缓存cache中
                    BeanRSCache.setDrugInteraction(key1, key2, diRslt);

                    /* 药品A成分 分割后 组装sql
                    if(drugA.getINGR_CLASS_IDS() == null)
                        continue;
                    String[] ingrclassidsA = drugA.getINGR_CLASS_IDS().split(",");
                    /* 药品B成分  分割后 组装sql
                    if(drugB == null || drugB.getINGR_CLASS_IDS() == null)
                        continue;
                    String[] ingrclassidsB = drugB.getINGR_CLASS_IDS().split(",");
//                    /* 从结果缓存中寻找数据  
//                    if(("133".equals(pods[i].getRecMainNo()) && "1".equals(pods[i].getRecSubNo()) ) )
//                    {
//                    	System.out.println(pods[i].getRecMainNo() + " : " + pods[i].getRecSubNo() + " : " + pods[j].getRecMainNo() + " : " + pods[j].getRecSubNo());
//                    }
                    TDrugInteractionRslt cacheInfo = BeanRSCache.getDrugInteraction(drugA.getDRUG_NO_LOCAL(),drugB.getDRUG_NO_LOCAL()); 
                    if(cacheInfo != null)
                    {
                        if(cacheInfo.getDrugInteractionInfo().length > 0)
                        {
                        	TDrugInteractionRslt copyCache = new TDrugInteractionRslt();

                        	/* 对每一个返回的药品标注上 医嘱序号 
                            drugA.setRecMainNo(pods[i].getRecMainNo());
                            drugA.setRecSubNo(pods[i].getRecSubNo());
                            /* 对每一个返回的药品标注上 医嘱序号 
                            drugB.setRecMainNo(pods[j].getRecMainNo());
                            drugB.setRecSubNo(pods[j].getRecSubNo());
                            
                        	copyCache.addDrugInfo(drugA, drugB, cacheInfo.getListDruginfo());
                        	
                        	copyCache.setRecMainNo(pods[i].getRecMainNo());
                        	copyCache.setRecSubNo(pods[i].getRecSubNo());
                        	copyCache.setRecMainNo2(pods[j].getRecMainNo());
                        	copyCache.setRecSubNo2(pods[j].getRecSubNo());
                            
                        	dsr.regInteractionCheckResult(drugA, drugB, copyCache);
                        }
                        continue;
                    }
                    /* 查询互动信息 
                    List<TDrugInteractionInfo> list = QueryUtils.queryDrugInteractionInfo(ingrclassidsA,ingrclassidsB, null, query);
                    if(list != null && list.size() > 0)
                    {
                        diRslt.addDrugInfo(drugA, drugB, list);
                        diRslt.setRecMainNo(pods[i].getRecMainNo());
                        diRslt.setRecSubNo(pods[i].getRecSubNo());
                        diRslt.setRecMainNo2(pods[j].getRecMainNo());
                        diRslt.setRecSubNo2(pods[j].getRecSubNo());
                        
                        /* 对每一个返回的药品标注上 医嘱序号 
                        drugA.setRecMainNo(pods[i].getRecMainNo());
                        drugA.setRecSubNo(pods[i].getRecSubNo());
                        /* 对每一个返回的药品标注上 医嘱序号 
                        drugB.setRecMainNo(pods[j].getRecMainNo());
                        drugB.setRecSubNo(pods[j].getRecSubNo());
                        
                        dsr.regInteractionCheckResult(drugA, drugB, diRslt);
                    }
                }
            }
     */
    
    /**
     * 改造完成
     * @throws Exception 
     */
    public TDrugSecurityRslt Check(String[] ids) throws Exception
    {
        /* 药品组合情况*/
        TDrugSecurityRslt dsr = new TDrugSecurityRslt();
        /* 查找drugs 中的所有要药品*/
        List<TDrug> drugs  = pdssCache.queryDrugListByIds(ids);
        /* 组合本地药品*/
        for(int i =0 ; i <= drugs.size() ; i++)
        {
            for(int j = i+1 ;j< drugs.size();j++)
            {
                TDrugInteractionRslt diRslt = new TDrugInteractionRslt();
                /* 药品A成分 分割后 组装sql*/
                if(drugs.get(i).getINGR_CLASS_IDS() == null)
                    continue;
                String[] ingrclassidsA = drugs.get(i).getINGR_CLASS_IDS().split(",");
                /* 药品B成分  分割后 组装sql*/
                if(drugs.get(j).getINGR_CLASS_IDS() == null)
                    continue;
                String[] ingrclassidsB = drugs.get(j).getINGR_CLASS_IDS().split(",");
                /*TODO: 需要统计一下5的数量占比多少，如果超过50%以上则可以通过sql过滤掉5的情况，提高数据库查询速度。*/
                List<TDrugInteractionInfo> list = pdssCache.queryDrugInteractionInfo(ingrclassidsA, ingrclassidsB, null); 
                diRslt.addDrugInfo(drugs.get(i), drugs.get(j), list);
                dsr.regInteractionCheckResult(drugs.get(i), drugs.get(j), diRslt);
            }
        }
        return  dsr;
    }
}
