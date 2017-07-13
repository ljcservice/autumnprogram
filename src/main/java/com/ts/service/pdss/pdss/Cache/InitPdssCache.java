package com.ts.service.pdss.pdss.Cache;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Utils.DrugUtils;
import com.ts.dao.DaoSupportPH;
import com.ts.dao.DaoSupportPdss;
import org.apache.log4j.Logger;
import com.ts.entity.Page;
import com.ts.entity.pdss.mas.Beans.TMedicareCatalog;
import com.ts.entity.pdss.mas.Beans.TMemo;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TAllergIngrDrug;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.entity.pdss.pdss.Beans.TDrugDosage;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;
import com.ts.entity.pdss.pdss.Beans.TDrugPerformFreqDict;
import com.ts.entity.pdss.pdss.Beans.TDrugRepeat;
import com.ts.entity.pdss.pdss.Beans.TDrugSideDict;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;
import com.ts.entity.pdss.pdss.Beans.ias.TOperationDrugInfo;
import com.ts.entity.pdss.pdss.RSBeans.TDrugInteractionRslt;
import com.ts.service.cache.CacheProcessor;
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
	@Resource(name="daoSupportPH")
	private DaoSupportPH  daoPH;

	private int showCount = 5000;
//	@PostConstruct()
	public void bulidRedisCache(){
	    try
		{
	        log.info("内容构建");
			setOperationDrug();
			setDrugRepeat();
			setDrugDiagRel();
			setDrugDiagInfo();
			setAdministration();
////	暂时不用 		setDrugInteractionInfo();
			setDrugInteractionMap();
			setDrugById();
			setDiseageVsDiag();
			setDrugIvEffect();
			setAid();
			setDdg();
			setDrugSideDict();
			setDrugPerfom();
			setDud();
//			setMemoList();
//			setTMedicareCatalog();
			log.info("内容构建完成");
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
	}

	
	/**
	 * 手术药品管理
	 * @throws Exception
	 */
	public void setOperationDrug() throws Exception
	{
	    log.info("删除手术药品管理缓存");
	    cacheTemplate.delKeys(PdssCache.OperationDrug);
	    log.info("加载手术用药");
	    Page page = new Page();
	    int pageNum = 1;
	    page.setShowCount(showCount);
	    page.setTotalPage(pageNum);
	    String key = null;
	    String keyName = null;
        List<TOperationDrugInfo> odinfo = new ArrayList<TOperationDrugInfo>();
        while( pageNum == 1 || pageNum <= page.getTotalPage()){ 
            page.setCurrentPage(pageNum);
            List<TOperationDrugInfo> ods = (List<TOperationDrugInfo>) daoPH.findForList("CKOperationDrug.queryCKOperationDrugPage", page);
            for(TOperationDrugInfo od : ods){
                if(key != null && !key.equals(od.getO_code()))
                {
                    cacheTemplate.setObject(PdssCache.OperationDrug, key ,-1, odinfo);
                    cacheTemplate.setObject(PdssCache.OperationDrug, keyName ,-1, odinfo);
                    odinfo = new ArrayList<TOperationDrugInfo>();
                }
                key = od.getO_code();
                keyName = od.getO_name();
                odinfo.add(od);
            }
            pageNum = page.getCurrentPage() + 1;
        }
        cacheTemplate.setObject(PdssCache.OperationDrug, key ,-1, odinfo);
        cacheTemplate.setObject(PdssCache.OperationDrug, keyName ,-1, odinfo);
//        odinfo = new ArrayList<TOperationDrugInfo>();
	}
	
	/**
	 * 重复用药
	 * @throws Exception
	 */
	public void setDrugRepeat() throws Exception{
	    log.info("重复给药");
	    Page page = new Page();
        int pageNum = 1;
        page.setShowCount(showCount);
        page.setTotalPage(pageNum);
        while( pageNum == 1 || pageNum <= page.getTotalPage()){
            page.setCurrentPage(pageNum);
            List<TDrugRepeat> res = (List<TDrugRepeat>) dao.findForList("DrugMapper.queryDrugRepeatPage", page);
            if (res == null)
                return;
            for (TDrugRepeat repeat : res) {
                String drugClassId1 = repeat.getDrug_class_1();
                String drugClassId2 = repeat.getDrug_class_2();
                if(drugClassId1.compareTo(drugClassId2) > 0)
                {
                    drugClassId1 = repeat.getDrug_class_2();
                    drugClassId2 = repeat.getDrug_class_1();
                }
                cacheTemplate.setObject(PdssCache.drugRepeatCache, repeat.getDrug_class_1() + "_" + repeat.getDrug_class_2(),-1, repeat);
            }
            pageNum = page.getCurrentPage() + 1;
            log.info("药物重复给药-- 第" + pageNum +"页");
        }
	}
	
	
	/**
	 * 药物禁忌症对应
	 * 
	 * @throws Exception
	 */
	public void setDrugDiagRel() throws Exception {
	    log.info("药物禁忌症对应");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
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
			log.info("药物禁忌症对应-- 第" + pageNum +"页");
		}
	}

	
	/**
	 * 药品禁忌症信息表
	 * @throws Exception
	 */
	public void setDrugDiagInfo() throws Exception{
	    log.info("药物禁忌症信息");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
		page.setTotalPage(pageNum);
		String key = null;
		List<TDrugDiagInfo> ddiRs = new ArrayList<TDrugDiagInfo>();
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TDrugDiagInfo> ddis = (List<TDrugDiagInfo>) dao.findForList("DrugMapper.getDrugDiagInfosPage", page);
			for(TDrugDiagInfo ddi : ddis){
				if(key != null && !key.equals(ddi.getCONTRAIND_ID()) ){
					cacheTemplate.setObject(PdssCache.ddisCache, key ,-1, ddiRs);
					ddiRs = new ArrayList<TDrugDiagInfo>();
				}
				key = ddi.getCONTRAIND_ID();
				ddiRs.add(ddi);
			}
			pageNum = page.getCurrentPage() + 1;
		}
		cacheTemplate.setObject(PdssCache.ddisCache, key ,-1, ddiRs);
	}
	
	
	/**
	 * 用药途径单个缓存查询
	 */
	public void setAdministration() throws Exception {
	    log.info("用药途径单个缓存");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
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
			log.info("用药途径单-- 第" + pageNum +"页");
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
		page.setShowCount(showCount);
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
			log.info("相互作用-- 第" + pageNum +"页");
		}
	}
	
	
	/**
     * 相互作用 
     * 
     * 
     * @param Code1
     * @param Code2
     * @return
     * @throws Exception
     */
	public void setDrugInteractionMap()	throws Exception {
	    log.info("相互作用");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
		page.setTotalPage(pageNum);
		List<TDrug> drugs = (List<TDrug>) dao.findForList("DrugMapper.queryDrugPage",page);
		for(int i =0 ;i<drugs.size() ; i++)
		{
			for(int j = i ; j < drugs.size() ; j++)
			{
				final TDrug drugA = drugs.get(i);
				final TDrug drugB = drugs.get(j);
				String key1 = drugA.getDRUG_NO_LOCAL();
				String key2 = drugB.getDRUG_NO_LOCAL();
				if(key1.compareTo(key2) > 0)
				{
				    key1 = drugB.getDRUG_NO_LOCAL();
				    key2 = drugA.getDRUG_NO_LOCAL();
				}
//				TDrugInteractionRslt t = 
				cacheTemplate.cache(PdssCache.drugInteraction , key1 + "_" + key2,-1, new CacheProcessor<TDrugInteractionRslt>() {
					@Override
					public TDrugInteractionRslt handle() {
						TDrugInteractionRslt res = new TDrugInteractionRslt();
						try {
		                    // 药品A成分 分割后 组装sql
		                    if(drugA.getINGR_CLASS_IDS() == null)
		                        return null;
		                    String[] ingrclassidsA = drugA.getINGR_CLASS_IDS().split(",");
		                    // 药品B成分  分割后 组装sql
		                    if(drugB == null || drugB.getINGR_CLASS_IDS() == null)
		                    	return null;
		                    String[] ingrclassidsB = drugB.getINGR_CLASS_IDS().split(",");
		                    Long x = System.currentTimeMillis();
		                    List<TDrugInteractionInfo> list = queryDrugInteractionInfo(ingrclassidsA,ingrclassidsB);
		                    if(list != null && list.size() > 0){
		                    	res.addDrugInfo(new TDrug(drugA), new TDrug(drugB), list);
		                    }
		                    else {
		                    	return null;
		                    }
						} catch (Exception e) {
							e.printStackTrace();
						}
						return res;
					}
				});
			}
		}
	}
	
	/**
     * 药品互作用信息，
     * 
     * @param Code1
     * @param Code2
     * @param wheres
     * @param query
     * @return
     * @throws Exception 
     */
    public List<TDrugInteractionInfo> queryDrugInteractionInfo(String[] Codes1, String[] Codes2) throws Exception
    {
    	 
    	PageData pd = new PageData();
    	StringBuffer code1 = new StringBuffer();
    	StringBuffer code2 = new StringBuffer();
    	for(String s: Codes1){
    		code1.append(s).append(",");
    	}
    	for(String s: Codes2){
    		code2.append(s).append(",");
    	}
    	code1.deleteCharAt(code1.length() - 1 );
    	code2.deleteCharAt(code2.length() - 1 );
    	pd.put("code1", code1.toString());
    	pd.put("code2", code2.toString());	
    	List<TDrugInteractionInfo>  listRs = (List<TDrugInteractionInfo>) dao.findForList("DrugMapper.getDrugInteraction", pd);
        return listRs;
    }

	/**
	 * 查询单个药品信息，使用缓存
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void setDrugById() throws Exception {
	    log.info("药品缓存 view_drug");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
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
			log.info("查询单个药品信息，使用缓存-- 第" + pageNum +"页");
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
	    log.info(" 查询单个诊断对应的疾病");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
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
			log.info(" 查询单个诊断对应的疾病，使用缓存-- 第" + pageNum +"页");
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
	    log.info("配伍禁忌");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
		page.setTotalPage(pageNum);
		String key1 = null;
		String key2 = null;
		List<TDrugIvEffect> dieRs = new ArrayList<>();
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);	
			List<TDrugIvEffect> list = (List<TDrugIvEffect>) dao.findForList("DrugMapper.queryDrugIvEffectPage",page);
			if (list == null || list.size() == 0)return;
			for(TDrugIvEffect die  : list){
				if(key1 != null && key2 != null && (!key1.equals(die.getIV_CLASS_CODE1())|| !key2.equals(die.getIV_CLASS_CODE2()))){
					cacheTemplate.setObject(PdssCache.drugIvEffect, key1 + "_" + key2, -1,dieRs);
					dieRs = new ArrayList<TDrugIvEffect>();
				}
				key1 = die.getIV_CLASS_CODE1();
				key2 = die.getIV_CLASS_CODE2();
				dieRs.add(die);
			}
			pageNum = page.getCurrentPage() + 1;
			log.info("配伍信息,使用缓存-- 第" + pageNum +"页");
		}
		cacheTemplate.setObject(PdssCache.drugIvEffect, key1 + "_" + key2, -1,dieRs);
//		while( pageNum == 1 || pageNum <= page.getTotalPage()){
//			page.setCurrentPage(pageNum);	
//			List<PageData>  pd = (List<PageData>) dao.findForList("DrugMapper.queryDrugIvEffectGroupPage",page);
//			if (pd == null || pd.size() == 0)return;
//			for(PageData p :pd){
//				List<TDrugIvEffect> list = (List<TDrugIvEffect>) dao.findForList("DrugMapper.queryDrugIvEffectList",p);
//				cacheTemplate.setObject(PdssCache.drugIvEffect, p.getInt("iv_class_code1") + "_" + p.getInt("iv_class_code2"), -1,list);
//			}
//			pageNum = page.getCurrentPage() + 1;
//		}
	}
	
	
	/**
	 * 不良反应 所有信息 使用缓存
	 * 
	 * @param Code1
	 * @param Code2
	 * @return
	 * @throws Exception
	 */
	public void setDrugSideDict() throws Exception {
	    log.info("不良反应");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
		page.setTotalPage(pageNum);
		String key1 = null;
		String key2 = null;
		List<TDrugSideDict> dsdRs = new ArrayList<TDrugSideDict>();
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);	
			List<TDrugSideDict> list = (List<TDrugSideDict>) dao.findForList("DrugMapper.getTDrugSideDictPage",page);
			if (list == null || list.size() == 0)return;
			for(TDrugSideDict did : list){
				if(key1 != null && key2 != null && (!key1.equals(did.getDRUG_CLASS_ID())|| !key2.equals(did.getADMINISTRATION_ID()))){
					cacheTemplate.setObject(PdssCache.drugSideDict,key1 + "_" + key2, -1,dsdRs);
					dsdRs = new ArrayList<TDrugSideDict>();
				}
				key1 = did.getDRUG_CLASS_ID();
				key2 = did.getADMINISTRATION_ID();
				dsdRs.add(did);
			}
			pageNum = page.getCurrentPage() + 1;
			log.info(" 不良反应 所有信息 使用缓存-- 第" + pageNum +"页");
		}
		cacheTemplate.setObject(PdssCache.drugSideDict,key1 + "_" + key2, -1,dsdRs);
//		for(TDrugIvEffect die  : list){
//			if(key1 != null && key2 != null && (!key1.equals(die.getIV_CLASS_CODE1())|| !key2.equals(die.getIV_CLASS_CODE2()))){
//				cacheTemplate.setObject(PdssCache.drugIvEffect, key1 + "_" + key2, -1,list);
//				dieRs = new ArrayList<TDrugIvEffect>();
//			}
//			key1 = die.getIV_CLASS_CODE1();
//			key2 = die.getIV_CLASS_CODE2();
//			dieRs.add(die);
//		}
//		while( pageNum == 1 || pageNum <= page.getTotalPage()){
//			page.setCurrentPage(pageNum);	
//			List<PageData>  pd = (List<PageData>) dao.findForList("DrugMapper.getTDrugSideDictGroupList",page);
//			if (pd == null || pd.size() == 0)return;
//			for(PageData p :pd){
//				List<TDrugIvEffect> list = (List<TDrugIvEffect>) dao.findForList("DrugMapper.getTDrugSideDictList",p);
//				cacheTemplate.setObject(PdssCache.drugIvEffect, p.getInt("DRUG_CLASS_ID") + "_" + p.getInt("ADMINISTRATION_ID"), -1,list);
//			}
//			pageNum = page.getCurrentPage() + 1;
//		}
	}

	/**
	 * 特殊人群
	 * 
	 * @param drugClass
	 * @return
	 * @throws Exception
	 */
	public void setDud() throws Exception {
	    log.info("特殊人群");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
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
			log.info("特殊人群-- 第" + pageNum +"页");
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
	public void setAid() throws Exception {
	    log.info("药物成分、药敏、药物分类与药物对照字典  目前放弃");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
		page.setCurrentPage(pageNum);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			List<TAllergIngrDrug> list = (List<TAllergIngrDrug>) dao.findForList("DrugMapper.getAidInfoPage", page);
			if (list == null)
				return;
			for (TAllergIngrDrug t : list) {
				cacheTemplate.setObject(PdssCache.aidCache, t.getDRUG_CLASS_ID(), -1, t);
			}
			pageNum = page.getCurrentPage() + 1;
			log.info("药物成分、药敏、药物分类与药物对照字典 -- 第" + pageNum +"页");
		}
	}

	/**
	 * 药品剂量使用字典
	 * @param allergenID
	 * @param drugClassid
	 * @return
	 * @throws Exception
	 */
	public void setDdg() throws Exception {
	    log.info("药品剂量使用字典");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
		page.setTotalPage(pageNum);
		String key1 = null;
		String key2 = null;
		List<TDrugDosage> ddgRs = new ArrayList<>();
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TDrugDosage> list = (List<TDrugDosage>) dao.findForList("DrugMapper.getDdgInfoPage", page);
			if(list == null || list.size() == 0) return ;
			for(TDrugDosage ddg : list){
				if(key1 != null && key2 != null && (!key1.equals(ddg.getDOSE_CLASS_ID())|| !key2.equals(ddg.getADMINISTRATION_ID()))){
					cacheTemplate.setObject(PdssCache.ddgCache,key1 + "_" + key2, -1,ddgRs);
					ddgRs = new ArrayList<TDrugDosage>();
				}
				key1 = ddg.getDOSE_CLASS_ID();
				key2 = ddg.getADMINISTRATION_ID();
				ddgRs.add(ddg);
			}
			pageNum = page.getCurrentPage() + 1;
			log.info("药品剂量使用字典-- 第" + pageNum +"页");
		}
		cacheTemplate.setObject(PdssCache.ddgCache,key1 + "_" + key2, -1,ddgRs);
	}

	/**
	 * 医嘱执行频率
	 * 
	 * @param performID
	 * @return
	 * @throws Exception
	 */
	public void setDrugPerfom() throws Exception {
	    log.info("医嘱执行频率");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
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
			log.info("医嘱执行频率-- 第" + pageNum +"页");
		}
	}

//	public void setDiagnons
	
	/**
	 * 医保用药 适应列表
	 * 
	 * @param drug_ID
	 * @return
	 * @throws Exception
	 */
	public void setMemoList() throws Exception {
	    log.info("医保内容");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TMemo> list = (List<TMemo>) dao.findForList("DrugMapper.queryMemoPage", page);
			if(list==null||list.size() == 0) return;
			for (TMemo t : list) {
				cacheTemplate.setObject(PdssCache.commonCache, t.getDRUG_ID(), -1, t);
			}
			pageNum = page.getCurrentPage() + 1;
			log.info("医保用药 适应列表-- 第" + pageNum +"页");
		}
	}

	/**
	 * 医保用药 适应列表
	 * 
	 * @param drug_ID
	 * @return
	 * @throws Exception
	 */
	public void setTMedicareCatalog() throws Exception {
	    log.info("医保用药 适应列表 ");
		Page page = new Page();
		int pageNum = 1;
		page.setShowCount(showCount);
		page.setTotalPage(pageNum);
		while( pageNum == 1 || pageNum <= page.getTotalPage()){
			page.setCurrentPage(pageNum);
			List<TMedicareCatalog> list = (List<TMedicareCatalog>) dao.findForList("DrugMapper.queryTMedicareCatalogPage", page);
			if(list==null||list.size() == 0) return;
			for (TMedicareCatalog t : list) {
				cacheTemplate.setObject(PdssCache.drugMedicare, t.getDRUG_ID(), -1, t);
			}
			pageNum = page.getCurrentPage() + 1;
			log.info("医保用药 适应列表-- 第" + pageNum +"页");
		}
	}
}
