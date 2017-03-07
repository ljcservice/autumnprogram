package com.ts.service.pdss.pdss.Cache;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ts.dao.DaoSupportPdss;
import org.apache.log4j.Logger;
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
import com.ts.service.cache.CacheTemplate;
import com.ts.util.PageData;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class InitPdssCache {

	private static final Logger log = Logger.getLogger(InitPdssCache.class);
	
	@Autowired
	private CacheTemplate cacheTemplate;

	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	
	
//	@PostConstruct()
	public void bulidRedisCache(){
		try
		{
//			log.info("内容构建");
//			log.info("药物禁忌症对应");
//			setDrugDiagRel();
//			log.info("用药途径单个缓存");
//			setAdministration();
//			log.info("相互作用");
//			setDrugInteractionInfo();
//			log.info("药品缓存 view_drug");
//			setDrugById();
//			log.info(" 查询单个诊断对应的疾病");
//			setDiseageVsDiag();
//			log.info("配伍禁忌");
//			setDrugIvEffect();
//			log.info("药物成分、药敏、药物分类与药物对照字典  目前放弃");
//			//getAid();
//			log.info("药品剂量使用字典");
//			getDdg();
			log.info("医嘱执行频率");
			queryDrugPerfom();
			log.info("特殊人群");
			setDud();
			log.info("医保内容");
			queryMemoList();
			log.info("医保用药 适应列表 ");
			queryTMedicareCatalog();
			log.info("内容构建完成");
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * 药物禁忌症对应
	 * 
	 * @throws Exception
	 */
	public void setDrugDiagRel() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TDrugDiagRel> res = (List<TDrugDiagRel>) dao.findForList("DrugMapper.queryDrugDiagRelPage", page);
			if (res == null)
				return;
			for (TDrugDiagRel drug : res) {
				cacheTemplate.setObject(PdssCache.ddrCache, drug.getDRUG_CLASS_ID() + "_" + drug.getADMINISTRATION_ID(),-1, drug);
			}
			pageNum = page.getCurrentPage() + 1;
		}
	}

	/**
	 * 用药途径单个缓存查询
	 */
	public void setAdministration() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage())
		{
			page.setCurrentPage(pageNum);
			List<TAdministration> list = (List<TAdministration>) dao.findForList("DrugMapper.getAdministrationPage",page);
	    	/* 用来区分 his 系统 传入 的是 本地码 或是 标准码 */
//	    	if(Config.getParamValue("admin_conv_flag").equals("0")){
//	    		list = (List<TAdministration>) dao.findForList("DrugMapper.getAdministrationPage",page);
//	    	}else {
//	    		list = (List<TAdministration>) dao.findForList("DrugMapper.getAdministrationLocalPage",page);
//	    	}
			if(list==null)
				return;
			for(TAdministration t:list){
				cacheTemplate.setObject(PdssCache.drugadmini ,"code_" +t.getADMINISTRATION_NO_LOCAL(), -1, t);
				cacheTemplate.setObject(PdssCache.drugadmini ,"name_" + t.getADMINISTRATION_NAME_LOCAL(), -1, t);
			}
			pageNum = page.getCurrentPage()+1;
		}
	}

	/**
	 * 相互作用 
	 * 查询单2个成分是否药理冲突
	 * 
	 * @param Code1
	 * @param Code2
	 * @return
	 * @throws Exception
	 */
	public void setDrugInteractionInfo() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TDrugInteractionInfo> list = (List<TDrugInteractionInfo>) dao.findForList("DrugMapper.getDrugInteractionPage",page);
			if(list==null)
				return;
			for(TDrugInteractionInfo t:list){
				cacheTemplate.setObject(PdssCache.diiCache ,t.getINGR_CLASS_CODE1() + "_" + t.getINGR_CLASS_CODE2(), -1, t);
			}
			pageNum = page.getCurrentPage()+1;
		}
	}

	/**
	 * 查询单个药品信息，使用缓存
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void setDrugById() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TDrug> list = (List<TDrug>) dao.findForList("DrugMapper.queryDrugPage",page);
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
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void setDiseageVsDiag() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<PageData> list = (List<PageData>) dao.findForList("DrugMapper.getDiseageVsDiagPage",page);
			if(list==null)
				return;
			for(PageData t:list){
				cacheTemplate.setObject(PdssCache.DiseageVsDiag ,t.getString("diagnosis_code"), -1, t);
			}
			pageNum = page.getCurrentPage()+1;
		}
	}

	/**
	 * 配伍信息,使用缓存
	 * 
	 * @param Code1
	 * @param Code2
	 * @return
	 * @throws Exception
	 */
	public void setDrugIvEffect() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);	
			List<TDrugIvEffect> list = (List<TDrugIvEffect>) dao.findForList("DrugMapper.queryDrugIvEffectPage",page);
			if (list == null)
				return;
			for (TDrugIvEffect t : list) {
				cacheTemplate.setObject(PdssCache.drugIvEffect, t.getIV_CLASS_CODE1() + "_" + t.getIV_CLASS_CODE2(), -1,t);
			}
			pageNum = page.getCurrentPage() + 1;
		}
	}

	/**
	 * 特殊人群
	 * 
	 * @param drugClass
	 * @return
	 * @throws Exception
	 */
	public void setDud() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TDrugUseDetail> list = (List<TDrugUseDetail>) dao.findForList("DrugMapper.getDudInfoPage",page);
			if(list==null)
				return;
			for(TDrugUseDetail t:list){
				cacheTemplate.setObject(PdssCache.dudCache ,t.getDRUG_CLASS_ID(),-1,t);
			}
			pageNum = page.getCurrentPage() + 1;
		}
	}

	/**
	 * 药物成分、药敏、药物分类与药物对照字典
	 * 目前放弃
	 * @param allergenID
	 * @param drugClassid
	 * @return
	 * @throws Exception
	 */
	public void getAid() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			List<TAllergIngrDrug> list = (List<TAllergIngrDrug>) dao.findForList("DrugMapper.getAidInfoPage", page);
			if (list == null)
				return;
			for (TAllergIngrDrug t : list) {
				cacheTemplate.setObject(PdssCache.aidCache, t.getALLERG_INGR_DRUG_ID(), -1, t);
			}
			pageNum = page.getCurrentPage() + 1;
		}
	}

	/**
	 * 药品剂量使用字典
	 * 
	 * @param allergenID
	 * @param drugClassid
	 * @return
	 * @throws Exception
	 */
	public void getDdg() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TDrugDosage> list = (List<TDrugDosage>) dao.findForList("DrugMapper.getDdgInfoPage", page);
			if (list == null) return;
			for (TDrugDosage t : list) {
				cacheTemplate.setObject(PdssCache.ddgCache, t.getDOSE_CLASS_ID() + "_" + t.getADMINISTRATION_ID(), -1,t);
			}
			pageNum = page.getCurrentPage() + 1;
		}
	}

	/**
	 * 医嘱执行频率
	 * 
	 * @param performID
	 * @return
	 * @throws Exception
	 */
	public void queryDrugPerfom() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TDrugPerformFreqDict> list = (List<TDrugPerformFreqDict>) dao.findForList("DrugMapper.queryDrugPerfomPage",page);
			if(list==null||list.size() == 0) return;
			for(TDrugPerformFreqDict t:list){
				cacheTemplate.setObject(PdssCache.drugPerform , "code_" +t.getPERFORM_FREQ_DICT_NO_LOCAL() ,-1,t);
				cacheTemplate.setObject(PdssCache.drugPerform , "name_" + t.getPERFORM_FREQ_DICT_NAME_LOCAL() ,-1,t);
			}
			pageNum = page.getCurrentPage()+1;
		}
	}

	/**
	 * 医保用药 适应列表
	 * 
	 * @param drug_ID
	 * @return
	 * @throws Exception
	 */
	public void queryMemoList() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TMemo> list = (List<TMemo>) dao.findForList("DrugMapper.queryMemoPage", page);
			if(list==null||list.size() == 0) return;
			for (TMemo t : list) {
				cacheTemplate.setObject(PdssCache.commonCache, t.getDRUG_ID(), -1, t);
			}
			pageNum = page.getCurrentPage() + 1;
		}
	}

	/**
	 * 医保用药 适应列表
	 * 
	 * @param drug_ID
	 * @return
	 * @throws Exception
	 */
	public void queryTMedicareCatalog() throws Exception {
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(3000);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TMedicareCatalog> list = (List<TMedicareCatalog>) dao.findForList("DrugMapper.queryTMedicareCatalogPage", page);
			if(list==null||list.size() == 0) return;
			for (TMedicareCatalog t : list) {
				cacheTemplate.setObject(PdssCache.drugMedicare, t.getDRUG_ID(), -1, t);
			}
			pageNum = page.getCurrentPage() + 1;
		}
	}
}
