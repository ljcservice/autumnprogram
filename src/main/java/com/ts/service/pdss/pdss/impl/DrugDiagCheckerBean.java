package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.dao.DaoSupportPdss;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.entity.pdss.pdss.RSBeans.TDrugDiagRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugDiagChecker;
import com.ts.util.PageData;

/**
 *  禁忌症审查子模块
 * @author liujc
 *
 */
@Service
@Transactional 
public class DrugDiagCheckerBean extends Persistent4DB implements IDrugDiagChecker
{
	private final static Logger log = Logger.getLogger(DrugDiagCheckerBean.class);
	
	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	
	@Resource(name = "pdssCache")
	private PdssCache pdssCache;
   
    /**
     * 改造完成
     * ok
     */
	@Override
    public TDrugSecurityRslt Check( TPatientOrder po)
    {
    	try
    	{
	        /* 获得用药信息 */
	        TPatOrderDrug[] pods = po.getPatOrderDrugs() ;
	        /* 诊断id*/
	        TPatOrderDiagnosis[] patOds = po.getPatOrderDiagnosiss();
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
//	        Map<String, TDrug> drugs  = pdssCache.queryDrugMap(pods);
	        for(TPatOrderDrug pod : pods){
	        	// 获得药品信息
	        	TDrug  drug =  po.getDrugMap(pod.getDrugID());
	        	if( drug == null) continue;
	        	// 获得给药途径信息
	        	TAdministration drugAdmin = pdssCache.queryAdministration(pod.getAdministrationID());
	        	if(drugAdmin == null ) continue;
	        	// 获得 处置编码
	        	TDrugDiagRel ddr =  pdssCache.queryDrugDiagRel(drug.getDRUG_CLASS_ID(), drugAdmin.getADMINISTRATION_ID());
	        	if (ddr == null) continue;
	        	// 获得禁忌信息
	        	List<TDrugDiagInfo > ddis = pdssCache.getDrugDiagInfos(ddr.getCONTRAIND_ID());
	        	if(ddis == null) continue;
	        	// 用诊断过滤  禁忌信息 ，如何诊断中包含禁忌信息视为匹配上，故识别为问题，    尚未使用 疾病对照表，进行诊断匹配。
	        	for(TDrugDiagInfo ddi : ddis){
	        		for(TPatOrderDiagnosis patOd  : patOds)
	        		{
	        		    if((ddi.getDIAG_DESC() == null || "".equals(ddi.getDIAG_DESC())) || 
	        		            (patOd.getDiagnosisName().trim() == null || "".equals(patOd.getDiagnosisName().trim()))){
	        		        continue;
	        		    }
	        		            
//	        			if(patOd.getDiagnosisName().indexOf(ddi.getDIAG_DESC()) != -1
//	        			        || ddi.getDIAG_DESC().indexOf(patOd.getDiagnosisName()) != -1 )//
	        		    if(patOd.getDiagnosisName().trim().equals(ddi.getDIAG_DESC().trim()))
	        			{
	    	        		TDrugDiagRslt diarslt = new TDrugDiagRslt();
	    	        		drug.setRecMainNo(pod.getRecMainNo());
	    	        		drug.setRecSubNo(pod.getRecSubNo());
	    	        		diarslt.addDrugDiag(drug, ddr, ddi,patOd.getDiagnosisName());
	    	 	            diarslt.setRecMainNo(pod.getRecMainNo());
	    	 	            diarslt.setRecSubNo(pod.getRecSubNo());
	    	 	            result.regDiagCheckResult(drug, diarslt);
	        			}
	        		}
	        	}
	        }
	        
//	        	/* 药品id*/
////	        String[] drugs = new String[pods.length];
////	        /* 用药途径  */
////	        String[] admini = new String[pods.length];
////	        for (int i = 0; i < pods.length; i++)
////	        {
////	            drugs[i]  = pods[i].getDrugID();
////	            admini[i] = pods[i].getAdministrationID();
////	        }
//	        
//	        //判断传入的是否icd码 如果不是执行 
//	        if(!Config.getParamValue("diag_icd9_conv_flag").equals("1") && !Config.getParamValue("diag_icd10_conv_flag").equals("1") )
//	        {
//    	        /* 疾病诊断码sql组装     queryDiagDictMap 转换 诊断码 */
////    	        diagnosis = QueryUtils.queryDiagDictMap(diagnosis,query);
//    	       PageData p = new PageData();
//    	       p.put("diagnosis", diagnosis);
//    	       List<String> s = (List<String>) dao.findForList("DrugMapper.queryDiagDictMap", p);
//    	       if(s!=null){
//    	    	   diagnosis = s.toArray(new String[0]);
//    	       }
//	        }
//	        /* 检查是否有值 */
//	        if(diagnosis.length == 0)
//	            return new TDrugSecurityRslt();
//	        // 诊断对应的疾病 sql 组装 
//	        List<String> diaeVsDiags = new ArrayList<String>();
//	        Map<String, List<TCommonRecord>> diseVsdiagMap = new HashMap<String, List<TCommonRecord>>();
//	        for(String str : diagnosis)
//	        {
////	            List<TCommonRecord> list = QueryUtils.queryDiseageVsdiag(str, query);
//	        	List<PageData> list = pdssCache.getDiseageVsDiag(str);
////	            diseVsdiagMap.put(str, list);
//	            if(list == null || list.size() == 0 ){ 
//	            	continue;
//	            }
//	            for(PageData t : list)
//	            {
//	            	diaeVsDiags.add(t.get("disease_id").toString()) ;
//	            }
//	        }
//	        
//	        if(diaeVsDiags == null || diaeVsDiags.size() == 0 ) {
//	            return  new TDrugSecurityRslt();
//	        }
//	        /* 所有药品信息 */
////	        List<TDrug> drugslist  = QueryUtils.queryDrug(drugs, null, query);
//	        List<TDrug> drugslist = pdssCache.queryDrugListByIds(drugs);
//	        
//	        /* 用药途径 */
////	        List<TAdministration> adstraion = QueryUtils.queryAdministration(admini, null, query);
//	        List<TAdministration> adstraion = pdssCache.queryAdministrations(admini);
//	        
//	        /* 药品类别  组装 */
//	        String[] drugClassID  = new String[drugslist.size()];
//	        for(int i = 0 ;i<drugslist.size();i++)
//	        {
//	            TDrug drug = drugslist.get(i);
//	            if(drug.getDRUG_CLASS_ID()!=null)
//	                drugClassID[i] = drug.getDRUG_CLASS_ID();
//	        }
//	        admini = new String[adstraion.size()];
//	        for(int i = 0 ;i<adstraion.size();i++)
//	        {
//	            admini[i] = adstraion.get(i).getADMINISTRATION_ID();
//	        }
//	        /* 药物禁忌症对应信息 */
//	        List<TDrugDiagRel> drugDiagRels = new ArrayList<TDrugDiagRel>();
//	        if(drugClassID.length > 0)
//	        {
//	            drugDiagRels = pdssCache.queryDrugDiagRels(drugClassID, admini );
//	        }
//	        
//	        /*  药物禁忌症对应CONTRAIND_ID 组装sql*/
//	        List<String> drugDiagRelIds = new ArrayList<String>();
//	        for(TDrugDiagRel entry : drugDiagRels)
//	        {
//	        	drugDiagRelIds.add(entry.getCONTRAIND_ID());
//	        }
//	        /* 检查是否有值 */
//	        if(drugDiagRelIds.size()==0){
//	        	return new TDrugSecurityRslt();
//	        }
//	        /* 药物禁忌症信息 */
////	        String strSql = "select DRUG_DIAG_INFO_ID,DIAGNOSIS_DICT_ID,DRUG_DIAG_REL_ID,SEQ_ID,INTER_INDI,DIAG_DESC,DRUG_REF_SOURCE,CONTRAIND_ID from DRUG_DIAG_INFO where " +
////	        " disease_id in (" + DiaeVsDiag.toString() + ") ";
//	     
//	        PageData pp = new PageData();
//	        pp.put("diaeVsDiags", diaeVsDiags);
//	        if(drugDiagRelIds.size() > 0)
//	        {
//	        	pp.put("drugDiagRelIds", drugDiagRelIds);
//	        }
//	        List<TDrugDiagInfo> drugDiagInfos = (List<TDrugDiagInfo>) dao.findForList("getDrugDiagInfos",pp);
//	        /* 整理返回数据结果 */
//	        for(int i = 0 ;i<pods.length ; i++)
//	        {
//	            TPatOrderDrug pod = pods[i];
//	            TDrug drug = CommonUtils.getDrugInfoOne(drugslist, pod);
//	            TAdministration  adt =  CommonUtils.getAdministrationInfoOne(adstraion, pod);
//	            if(drug == null || adt == null)
//	                continue;
//	            /* 对每一个返回的药品标注上 医嘱序号 */
//	            drug.setRecMainNo(pod.getRecMainNo());
//	            drug.setRecSubNo(pod.getRecSubNo());
//	            /* 药物禁忌症对应 */
//	            TDrugDiagRel ddr = null;
//	            for(TDrugDiagRel entry : drugDiagRels)
//	            {
//	                if(entry.getDRUG_CLASS_ID().equals(drug.getDRUG_CLASS_ID()) && entry.getADMINISTRATION_ID().equals(adt.getADMINISTRATION_ID()))
//	                {
//	                    ddr = entry;
//	                    drugDiagRels.remove(entry);
//	                    break;
//	                }
//	            }
//	            if(ddr == null)
//	                continue;
//	            /* 获得药物禁忌症信息 
//	             * TODO 在 drugdiaginfo 中说明该字段为 DRUG_DIAG_REL_ID 不可重复 请确认。 
//	             * */
//	            TDrugDiagInfo ddi = null;
//	            for(int j = 0 ; j<drugDiagInfos.size();j++)
//	            {
//	                if(ddr.getCONTRAIND_ID().equals(drugDiagInfos.get(j).getCONTRAIND_ID()))
//	                {
//	                    ddi = drugDiagInfos.get(j);
//	                    break;
//	                }
//	            }
//	            if(ddi == null)
//	                continue;
//	            TDrugDiagRslt diarslt = new TDrugDiagRslt();
//	            diarslt.addDrugDiag(drug, ddr, ddi);
//	            diarslt.setRecMainNo(pod.getRecMainNo());
//	            diarslt.setRecSubNo(pod.getRecSubNo());
//	            result.regDiagCheckResult(drug, diarslt);
//	        }
//	        drugslist = null;
	        return result;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		log.warn(this.getClass().toString() + ":" + e.getMessage());
    		return new TDrugSecurityRslt();
    	}
    }

    
	/**
	 * 改造完成
	 */
    @Override
    public TDrugSecurityRslt Check(String[] drugs, String[] diagnosis) {
    	TDrugSecurityRslt result = new TDrugSecurityRslt();
        try
        {
        	/* 设置访问数据库代码 */
            this.setQueryCode("PDSS");
            /* 疾病诊断码sql组装 */
            /* 所有药品信息 */
            QueryUtils queryUtils = new QueryUtils();
//            List<TDrug> drugslist  = queryUtils.queryDrug(drugs, null, query);
            List<TDrug> drugslist = pdssCache.queryDrugListByIds(drugs);
            /* 药品类吗  组装 */
            String[] drugClassID  = new String[drugslist.size()];
            for(int i = 0 ;i<drugslist.size();i++)
            {
                TDrug drug      = drugslist.get(i);
                if(drug.getDRUG_CLASS_ID()!=null)
                    drugClassID[i] = drug.getDRUG_CLASS_ID();
            }
            /* 药物禁忌症对应信息 */
            //TODO 这里有的药品类码 但是 药品类码可能会重复
            List<TDrugDiagRel> drugDiagRels = new ArrayList<TDrugDiagRel>();
            if(drugClassID.length > 0)
            {
//                drugDiagRels = queryUtils.queryDrugDiagRel(drugClassID, null, query);
            }
            /*  药物禁忌症对应id 组装sql*/
            List<String> drugDiagRelIds = new ArrayList<String>();
            for(TDrugDiagRel entry : drugDiagRels)
            {
                drugDiagRelIds.add(entry.getDRUG_DIAG_REL_ID());
            }
            /* 药物禁忌症信息 */
          	PageData pd = new PageData();
          	pd.put("DIAGNOSIS_DICT_ID", diagnosis);
          	pd.put("DRUG_DIAG_REL_ID", drugDiagRelIds);
            List<TDrugDiagInfo> drugDiagInfos = (List<TDrugDiagInfo>) dao.findForList("DrugMapper.drugDiagInfosByRel",pd);
            //  List<TDrugDiagInfo> drugDiagInfos = (List<TDrugDiagInfo>) query.query(strSql , new DrugDiagInfoMapper());
            
            /* 整理数据结果返回  */
            for(TDrugDiagInfo entity : drugDiagInfos)
            {
                TDrugDiagRel ddr = null;
                TDrug       drug = null;
                /* 装载 药物禁忌症信息  */
                /* 装载 药物禁忌症对应*/
                for(TDrugDiagRel drugDiagRel : drugDiagRels)
                {
                    if (entity.getDRUG_DIAG_REL_ID().equals(drugDiagRel.getDRUG_DIAG_REL_ID()))
                    {
                        ddr = drugDiagRel ;
                        drugDiagRels.remove(drugDiagRel);
                        break;
                    }
                }
                /* 装载 药品信息  */
                for(TDrug _drug :drugslist)
                {
                    
                    if (ddr.getDRUG_CLASS_ID().equals(_drug.getDRUG_CLASS_ID()))
                    {
                        drug = _drug;
                        drugslist.remove(_drug);
                        break;
                    }
                }
                TDrugDiagRslt diarslt = new TDrugDiagRslt();
                diarslt.addDrugDiag(drug, ddr, entity,"");
                result.regDiagCheckResult(drug, diarslt);
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    	
        return result;
    }
}
