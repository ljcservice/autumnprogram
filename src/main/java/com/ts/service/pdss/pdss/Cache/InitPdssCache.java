package com.ts.service.pdss.pdss.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.ts.dao.DaoSupportPdss;
import com.ts.dao.redis.RedisDao;
import com.ts.entity.Page;
import com.ts.entity.pdss.mas.Beans.TMedicareCatalog;
import com.ts.entity.pdss.mas.Beans.TMemo;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TAllergIngrDrug;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.entity.pdss.pdss.Beans.TDrugDosage;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;
import com.ts.entity.pdss.pdss.Beans.TDrugPerformFreqDict;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;
import com.ts.entity.pdss.pdss.RSBeans.TDrugInteractionRslt;
import com.ts.entity.pdss.pdss.RSBeans.TMedicareRslt;
import com.ts.service.cache.CacheProcessor;
import com.ts.service.cache.CacheTemplate;
import com.ts.util.PageData;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class InitPdssCache {
	
	@Autowired
	private CacheTemplate cacheTemplate;
	
	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	
	/**
	 * 药物禁忌症对应
	 * @throws Exception
	 */
	public void setDrugDiagRel() throws Exception{
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TDrugDiagRel> res = (List<TDrugDiagRel>) dao.findForList("DrugMapper.queryDrugDiagRelPage",page);
			if(res==null)
				return;
			for(TDrugDiagRel drug:res){
				cacheTemplate.setObject(PdssCache.ddrCache,drug.getDRUG_CLASS_ID()+"_"+drug.getADMINISTRATION_ID(), -1, drug);
			}
			pageNum = page.getCurrentPage()+1;
		}
	}
	
	/**
	 * 用药途径单个缓存查询
	 */
	public void setAdministration() throws Exception{
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TAdministration> list=null;
	    	/* 用来区分 his 系统 传入 的是 本地码 或是 标准码 */
	    	if(Config.getParamValue("admin_conv_flag").equals("0")){
	    		list = (List<TAdministration>) dao.findForList("DrugMapper.getAdministrationPage",page);
	    	}else {
	    		list = (List<TAdministration>) dao.findForList("DrugMapper.getAdministrationLocalPage",page);
	    	}
			if(list==null)
				return;
			for(TAdministration t:list){
				cacheTemplate.setObject(PdssCache.drugadmini,t.getADMINISTRATION_ID(), -1, t);
			}
			pageNum = page.getCurrentPage()+1;
		}
	 }
	
	
   /**
	* 查询单2个成分是否药理冲突
	* @param Code1
	* @param Code2
	* @return
	* @throws Exception 
	*/
	public void setDrugInteractionInfo() throws Exception{
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TDrugInteractionInfo> list = (List<TDrugInteractionInfo>) dao.findForObject("DrugMapper.getDrugInteractionPage",page);
			if(list==null)
				return;
			for(TDrugInteractionInfo t:list){
				cacheTemplate.setObject(PdssCache.diiCache,t.getINGR_CLASS_CODE1()+"_"+t.getINGR_CLASS_CODE2(), -1, t);
			}
			pageNum = page.getCurrentPage()+1;
		}	
	}

   /**
    * 查询单个药品信息，使用缓存
    * @param id
    * @return
    * @throws Exception 
    */
    public void setDrugById() throws Exception{
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
	    	PageData pd = new PageData();
			List<TDrug> list = (List<TDrug>) dao.findForObject("DrugMapper.queryDrugPage",pd);
			if(list==null)
				return;
			for(TDrug t:list){
				cacheTemplate.setObject(PdssCache.drugCacheByLocal,t.getDRUG_NO_LOCAL(), -1, t);
			}
			pageNum = page.getCurrentPage()+1;
		}	
   }
    
   /**
    * 查询单个诊断对应的疾病，使用缓存
    * @param id
    * @return
    * @throws Exception 
    */
    public void setDiseageVsDiag() throws Exception{
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<PageData> list = (List<PageData>) dao.findForObject("DrugMapper.getDiseageVsDiagPage",page);
			if(list==null)
				return;
			for(PageData t:list){
				cacheTemplate.setObject(PdssCache.DiseageVsDiag,t.getString("diagnosis_code"), -1, t);
			}
			pageNum = page.getCurrentPage()+1;
		}	
   	}
   /**
    * 配伍信息,使用缓存
    * @param Code1
    * @param Code2
    * @return
    * @throws Exception 
    */
	public void setDrugIvEffect() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TDrugIvEffect> list = (List<TDrugIvEffect>) dao.findForObject("DrugMapper.queryDrugIvEffectPage",page);
			if(list==null)
				return;
			for(TDrugIvEffect t:list){
				cacheTemplate.setObject(PdssCache.drugIvEffect,t.getIV_CLASS_CODE1()+"_"+t.getIV_CLASS_CODE2(),-1,t);
			}
			pageNum = page.getCurrentPage()+1;
		}		
	}
	
   /**
    * 特殊人群
    * @param drugClass
    * @return
    * @throws Exception 
    */
   public void setDud() throws Exception
   {
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TDrugUseDetail> list = (List<TDrugUseDetail>) dao.findForObject("DrugMapper.getDudInfoPage",page);
			if(list==null)
				return;
			for(TDrugUseDetail t:list){
				cacheTemplate.setObject(PdssCache.dudCache,t.getDRUG_CLASS_ID(),-1,t);
			}
			pageNum = page.getCurrentPage()+1;
		}	
   }
   
   /**
    * 药物成分、药敏、药物分类与药物对照字典
    * @param allergenID    已应用快照
    * @param drugClassid
    * @return
    * @throws Exception 
    */
   public void getAid(final String allergenID) throws Exception
   {
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TAllergIngrDrug> list = (List<TAllergIngrDrug>) dao.findForObject("DrugMapper.getAidInfoPage",page);
			if(list==null)
				return;
			for(TAllergIngrDrug t:list){
				cacheTemplate.setObject(PdssCache.aidCache,t.getALLERG_INGR_DRUG_ID(),-1,t);
			}
			pageNum = page.getCurrentPage()+1;
		}	
   }
   
   
   /**
    * 药品剂量使用字典
    * @param allergenID    已应用快照
    * @param drugClassid
    * @return
    * @throws Exception 
    */
   public  void getDdg( ) throws Exception
   {
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TDrugDosage> list = (List<TDrugDosage>) dao.findForList("DrugMapper.getDdgInfoPage",page);
			if(list==null)
				return;
			for(TDrugDosage t:list){
				cacheTemplate.setObject(PdssCache.ddgCache, t.getDOSE_CLASS_ID()+"_"+ t.getADMINISTRATION_ID() ,-1,t);
			}
			pageNum = page.getCurrentPage()+1;
		}	
   }
   
   /**
    * 医嘱执行频率
    * @param performID
    * @return
    * @throws Exception
    */
   public  void queryDrugPerfom() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TDrugPerformFreqDict> list = (List<TDrugPerformFreqDict>) dao.findForList("DrugMapper.queryDrugPerfomPage",page);
			if(list==null)
				return;
			for(TDrugPerformFreqDict t:list){
				cacheTemplate.setObject(PdssCache.drugPerform, t.getPERFORM_FREQ_DICT_NO_LOCAL() ,-1,t);
			}
			pageNum = page.getCurrentPage()+1;
		}
   }
   
   
   /**
    * 医保用药 适应列表 
    * @param drug_ID
    * @return
    * @throws Exception 
    */
	public void queryMemoList() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<PageData> list = (List<PageData>) dao.findForList("DrugMapper.queryMemoPage",page);
			if(list==null)
				return;
			for(PageData t:list){
				cacheTemplate.setObject(PdssCache.commonCache, t.getString("DRUG_ID") ,-1,t);
			}
			pageNum = page.getCurrentPage()+1;
		}
	}
	
	
   /**
    * 医保用药 适应列表 
    * @param drug_ID
    * @return
    * @throws Exception 
    */
	public void  queryTMedicareCatalog(final String DRUG_ID) throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum <= page.getTotalPage()){
			List<TMedicareCatalog> list = (List<TMedicareCatalog>) dao.findForList("DrugMapper.queryTMedicareCatalogPage",page);
			if(list==null)
				return;
			for(TMedicareCatalog t:list){
				cacheTemplate.setObject(PdssCache.drugMedicare, t.getDRUG_ID() ,-1,t);
			}
			pageNum = page.getCurrentPage()+1;
		}
	}
}
