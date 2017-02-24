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

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.ts.dao.DaoSupportPdss;
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
import com.ts.service.pdss.pdss.RowMapper.DrugAllergIngrMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugPerfromMapper;
import com.ts.util.PageData;

@Service
@Transactional
public class PdssCache {
	private static String DRUG_ID = "DRUG_ID";//单个药品信息kEY
	private static String DRUG_INTERACTION = "DRUG_INTERACTION";//药品本地ID冲突KEY
	private static String DRUG_CODE_INTERACTION = "DRUG_CODE_INTERACTION";//药品成分冲突Key
	private static String DiseageVsDiag = "DiseageVsDiag";//诊断对应的疾病key
	private static String drugadmini = "drugadmini";//用药途径key
	private static String ddrCache = "ddrCache";//药物禁忌症对应key
	private static String drugIvEffect = "drugIvEffect";//配伍信息
	private static String dudCache = "dudCache";//特殊人群
    private static String aidCache  =  "aidCache"; /* 药物成分、药敏、药物分类与药物对照字典 */
    private static String ddgCache = "ddgCache"; //药品剂量使用字典
    private static String drugPerform ="drugPerform";/* 医嘱执行频率字典 */
    private static String commonCache = "commonCache";// 公用缓存
	
	@Autowired
	private CacheTemplate cacheTemplate;
	
	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	
    /**
     * 药物禁忌症对应，已应用数据库快照
     * @param DrugClassIDs
     * @param drugAdminis
     * @return
     * @throws Exception 
     */
    public List<TDrugDiagRel> queryDrugDiagRels(String[] drugClassIDs, String[] drugAdmini) throws Exception
    {
        final List<TDrugDiagRel> list = new ArrayList<TDrugDiagRel>();
        List<String> param = new ArrayList<String>();
        
        for (int i = 0; i < drugClassIDs.length; i++){
            for (int j = 0; j < drugAdmini.length; j++){
	        	if(param.contains(drugClassIDs[i]+"_"+drugAdmini[j])){
	        		//重复数据
	        	}else{
	        		TDrugDiagRel o = queryDrugDiagRel(drugClassIDs[i], drugAdmini[j]);
	        		if(o!=null){
	        			list.add(o);
	        		}
	        		param.add(drugClassIDs[i]+"_"+drugAdmini[j]);
	        	}
            }
        }       
        return list;	
    	
    }
    /**
     * 药物禁忌症对应，已应用数据库快照
     * @param DrugClassID
     * @param drugAdmini
     * @return
     * @throws Exception 
     */
  	public TDrugDiagRel queryDrugDiagRel(final String drugClassId,final String drugAdmini) throws Exception{
  		TDrugDiagRel t = cacheTemplate.cache(ddrCache,drugClassId+"_"+drugAdmini, new CacheProcessor<TDrugDiagRel>() {
  			@Override
  			public TDrugDiagRel handle() throws Exception {
            	PageData pd = new PageData();
            	pd.put("drugClassId", drugClassId);
            	pd.put("drugAdmini", drugAdmini);
            	TDrugDiagRel	res = (TDrugDiagRel) dao.findForObject("DrugMapper.queryDrugDiagRel",pd);
            	pd = null;
            	return res;
  			}
  		});
      	return t;
  	}
    
	/**
	 * 用药途径，多个查询
	 * @param administrationID
	 * @return
	 * @throws Exception 
	 */
	public List<TAdministration> queryAdministrations(String[] administrationID) throws Exception{
        final List<TAdministration> list = new ArrayList<TAdministration>();
        List<String> param = new ArrayList<String>();
        
        for (int i = 0; i < administrationID.length; i++)
        {	
        	if(param.contains(administrationID[i])){
        		//重复数据
        	}else{
        		TAdministration aid = queryAdministration(administrationID[i]);
        		list.add(aid);
        		param.add(administrationID[i]);
        	}
        }       
        return list;		
	}
	
	/**
	 * 用药途径单个缓存查询
	 * @param administrationID
	 * @return
	 * @throws Exception 
	 */
	public TAdministration queryAdministration(final String administrationID) throws Exception{
		TAdministration t = cacheTemplate.cache(drugadmini,administrationID, new CacheProcessor<TAdministration>() {
			@Override
			public TAdministration handle() throws Exception {
				TAdministration res=null;
		    	/* 用来区分 his 系统 传入 的是 本地码 或是 标准码 */
		    	if(Config.getParamValue("admin_conv_flag").equals("0")){
		    		res = (TAdministration) dao.findForObject("DrugMapper.getAdministration",administrationID);
		    	}else {
		    		res = (TAdministration) dao.findForObject("DrugMapper.getAdministrationLocal",administrationID);
		    	}
				return res;
			}
		});
    	return t;
	}
	
	 //查询两个药品，是否互相药理冲突
    public TDrugInteractionRslt queryDrugInteraction(final TDrug drugA,final TDrug drugB) throws Exception{
        String key1 = drugA.getDRUG_NO_LOCAL();
        String key2 = drugB.getDRUG_NO_LOCAL();
        if (drugA.getDRUG_NO_LOCAL().compareTo(drugB.getDRUG_NO_LOCAL()) > 0)
        {
        	key1 = drugB.getDRUG_NO_LOCAL();
            key2 = drugA.getDRUG_NO_LOCAL();
        }
    	TDrugInteractionRslt t = cacheTemplate.cache(DRUG_INTERACTION+key1+key2, new CacheProcessor<TDrugInteractionRslt>() {
			@Override
			public TDrugInteractionRslt handle() {
				TDrugInteractionRslt res=null;
				try {
                    // 药品A成分 分割后 组装sql
                    if(drugA.getINGR_CLASS_IDS() == null)
                        return null;
                    String[] ingrclassidsA = drugA.getINGR_CLASS_IDS().split(",");
                    // 药品B成分  分割后 组装sql
                    if(drugB == null || drugB.getINGR_CLASS_IDS() == null)
                    	return null;
                    String[] ingrclassidsB = drugB.getINGR_CLASS_IDS().split(",");
                    List<TDrugInteractionInfo> list = queryDrugInteractionInfo(ingrclassidsA,ingrclassidsB, null);
                    if(list != null && list.size() > 0){
                    	res.addDrugInfo(new TDrug(drugA), new TDrug(drugB), list);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
				return res;
			}
		});
    	return t;
    }
    
    /**
     * 药品互作用信息，已应用数据库快照
     * 
     * @param Code1
     * @param Code2
     * @param wheres
     * @param query
     * @return
     * @throws Exception 
     */
    public List<TDrugInteractionInfo> queryDrugInteractionInfo(String[] Codes1, String[] Codes2,String wheres) throws Exception
    {
        List<TDrugInteractionInfo> list = new ArrayList<TDrugInteractionInfo>();
        for (int i = 0; i < Codes1.length; i++)
        {
            for (int j = 0; j < Codes2.length; j++)
            {
            	String Code1 = Codes1[i];
            	String Code2 = Codes2[j];
            	if (Code1.equals(Code2))
            		continue;
            	TDrugInteractionInfo diix = null;
                // 数据库中小号在前面
                if (Integer.parseInt(Code1) > Integer.parseInt(Code2)) {
                	diix = getDrugInteractionInfo(Code2,Code1);
                }else{
                	diix = getDrugInteractionInfo(Code1,Code2);
                }
                if (diix != null)
                {
                	list.add(diix);
                }
            }
        }
        return list;
    }
    /**
     * 查询单2个成分是否药理冲突
     * @param Code1
     * @param Code2
     * @return
     * @throws Exception 
     */
    public TDrugInteractionInfo getDrugInteractionInfo(final String code1, final String code2) throws Exception{
    	TDrugInteractionInfo t = cacheTemplate.cache(DRUG_CODE_INTERACTION+code1+"_"+code2, new CacheProcessor<TDrugInteractionInfo>() {
			@Override
			public TDrugInteractionInfo handle() throws Exception {
				PageData pd = new PageData();
				pd.put("code1", code1);
				pd.put("code2", code2);
				TDrugInteractionInfo res = (TDrugInteractionInfo) dao.findForObject("DrugMapper.getDrugInteraction",pd);
				pd = null;
				return res;
			}
		});
    	return t;
    }
    
    /**
     * 查询单个药品信息，使用缓存
     * @param id
     * @return
     * @throws Exception 
     */
    public TDrug queryDrugById(final String id) throws Exception{
    	TDrug t = cacheTemplate.cache(DRUG_ID+id, new CacheProcessor<TDrug>() {
			@Override
			public TDrug handle() throws Exception {
				TDrug res = (TDrug) dao.findForObject("DrugMapper.queryDrugById",id);
				return res;
			}
		});
    	return t;
    }
    /**
     * 查询单个药品信息，使用缓存
     * @param id
     * @return
     * @throws Exception 
     */
    public List<TDrug> queryDrugListByIds(String[] ids) throws Exception{
    	List<TDrug> rs = new ArrayList<TDrug>();
    	final Set<String> set = new HashSet<String>();
    	for(String id:ids){
    		if(!set.contains(id)){
    			set.add(id);
    			TDrug t = queryDrugById(id);
    			if(t!=null)
    				rs.add(t);
    		}
    	}
    	return rs;
    }
    /**
     * 查询单个药品信息，使用缓存
     * @param id
     * @return
     * @throws Exception 
     */
    public List<TDrug> queryDrugListByIds(List<String> ids) throws Exception{
    	List<TDrug> rs = new ArrayList<TDrug>();
    	final Set<String> set = new HashSet<String>();
    	for(String id:ids){
    		if(!set.contains(id)){
    			set.add(id);
    			TDrug t = queryDrugById(id);
    			if(t!=null)
    				rs.add(t);
    		}
    	}
    	return rs;
    }
    
    /**
     * 查询单个诊断对应的疾病，使用缓存
     * @param id
     * @return
     * @throws Exception 
     */
    public List<TCommonRecord> getDiseageVsDiag(final String Diag_Code) throws Exception{
    	List<TCommonRecord> t = cacheTemplate.cache(DiseageVsDiag+Diag_Code, new CacheProcessor<List<TCommonRecord>>() {
			@Override
			public List<TCommonRecord> handle() throws Exception {
				List<TCommonRecord> r = (List<TCommonRecord>) dao.findForObject("DrugMapper.getDiseageVsDiag",Diag_Code);
				return r;
			}
		});
    	return t;
    }
    /**
     * 配伍信息,使用缓存
     * @param Code1
     * @param Code2
     * @return
     * @throws Exception 
     */
	public List<TDrugIvEffect> queryDrugIvEffect(final String drugIvCode1,final String drugIvCode2) throws Exception {
    	List<TDrugIvEffect> t = cacheTemplate.cache(drugIvEffect,drugIvCode1+drugIvCode2, new CacheProcessor<List<TDrugIvEffect>>() {
			@Override
			public List<TDrugIvEffect> handle() throws Exception {
				PageData pd = new PageData();
				pd.put("code1", drugIvCode1);
				pd.put("code2", drugIvCode2);
				List<TDrugIvEffect> r = (List<TDrugIvEffect>) dao.findForObject("DrugMapper.queryDrugIvEffect",pd);
				pd = null;
				return r;
			}
		});
    	return t;
	}
	/**
	 * 查询药品MAP，去重复，使用缓存
	 * @param patOrderDrugs
	 * @return key 为DRUG_NO_LOCAL，并不是drugID请注意
	 * @throws Exception 
	 */
	public Map<String, TDrug> queryDrugMap(TPatOrderDrug[] poDrugs) throws Exception {
		Map<String, TDrug> map = new HashMap<String, TDrug>();
        for (int i = 0; i < poDrugs.length; i++)
        {
        	if (map.containsKey(poDrugs[i].getDrugID())){
        		continue;
        	}
        	TDrug drug = queryDrugById(poDrugs[i].getDrugID());
        	if (drug != null){
        		map.put(drug.getDRUG_NO_LOCAL(), drug);
        	}
        }
		return map;
	}
	
	
    /**
     * 药品特殊人群
     *  返回Map的形式以便于快速查找
     * @param drugs
     * @param query
     * @return
     * @throws Exception 
     */
    public Map<String, TDrugUseDetail> queryDrugDudMap(Map<String, TDrug> drugs ) throws Exception
    {
        Map<String, TDrugUseDetail> map = new HashMap<String, TDrugUseDetail>();
        Set entrySet = drugs.entrySet();
        for(Iterator<Entry<String, TDrug>> it = entrySet.iterator(); it.hasNext();)
        {  
        	Entry<String, TDrug> entry = (Entry<String, TDrug>)it.next();
        	TDrug drug = (TDrug)entry.getValue();
        	TDrugUseDetail dud = (TDrugUseDetail)getDud(drug.getDRUG_CLASS_ID());
        
            if(!map.containsKey(dud.getDRUG_CLASS_ID())){
            	map.put(dud.getDRUG_CLASS_ID(), dud);
            }
        }
        return map;
    }
    /**
     * 药品特殊人群
     *  返回Map的形式以便于快速查找
     * @param drugs
     * @param query
     * @return
     * @throws Exception 
     */
    public List<TDrugUseDetail> queryDrugDudList(List<TDrug> drugs ) throws Exception
    {
        List<TDrugUseDetail> list = new ArrayList<TDrugUseDetail>();
        for(TDrug drug:drugs)
        {  
        	TDrugUseDetail dud = (TDrugUseDetail)getDud(drug.getDRUG_CLASS_ID());
        	list.add(dud);
        }
        return list;
    }
    /**
     * 特殊人群
     * @param drugClass
     * @return
     * @throws Exception 
     */
    public TDrugUseDetail getDud(final String drugClass) throws Exception
    {
    	TDrugUseDetail t = cacheTemplate.cache(dudCache,drugClass, new CacheProcessor<TDrugUseDetail>() {
			@Override
			public TDrugUseDetail handle() throws Exception {
				PageData pd = new PageData();
				pd.put("DRUG_CLASS_ID", drugClass);
				TDrugUseDetail r = (TDrugUseDetail) dao.findForObject("InfoMapper.getDudInfoById",drugClass);
				pd = null;
				return r;
			}
		});
    	return t;
    	
    }
    
    
    /**
     * 药物成分、药敏、药物分类与药物对照字典
     * @param allergenID    已应用快照
     * @param drugClassid
     * @return
     * @throws Exception 
     */
    public List<TAllergIngrDrug> queryAllergen(String[] allergenID , String drugClassid ) throws Exception
    {
        List<TAllergIngrDrug> allerg = new ArrayList<TAllergIngrDrug>();
        for(int i = 0  ;i <allergenID.length ; i++) {
            List<TAllergIngrDrug>  allergs = getAid(allergenID[i]);
            if(allergs!=null){
                TAllergIngrDrug entry = new TAllergIngrDrug();
                entry.setDRUG_CLASS_ID(drugClassid);
                entry.setDRUG_ALLERGEN_ID(allergenID[i]);
                if(allergs.contains(entry))
                {
                    for(int j = 0 ;j < allergs.size();j++)
                    {
                        if(drugClassid.equals(allergs.get(j).getDRUG_CLASS_ID()))
                        {
                            allergs.get(j).setLastUseDate(DateUtils.getDateTime());
                            allerg.add(allergs.get(j));
                        }
                    }
                }
            }
        }
        
        return allerg;
    }
    
    /**
     * 药物成分、药敏、药物分类与药物对照字典
     * @param allergenID    已应用快照
     * @param drugClassid
     * @return
     * @throws Exception 
     */
    public List<TAllergIngrDrug> getAid(final String allergenID) throws Exception
    {
    	List<TAllergIngrDrug> t = cacheTemplate.cache(aidCache,allergenID, new CacheProcessor<List<TAllergIngrDrug>>() {
			@Override
			public List<TAllergIngrDrug> handle() throws Exception {
				PageData pd = new PageData();
				pd.put("drug_allergen_id", allergenID);
				List<TAllergIngrDrug> r = (List<TAllergIngrDrug>) dao.findForObject("InfoMapper.getAidInfoById",pd);
				pd = null;
				return r;
			}
		});
    	return t;
    	
    }
    
    
    /**
     * 药品剂量使用字典
     * @param allergenID    已应用快照
     * @param drugClassid
     * @return
     * @throws Exception 
     */
    public  List<TDrugDosage> getDdg(final String drugClass ,final String administation ) throws Exception
    {
    	List<TDrugDosage> t = cacheTemplate.cache(ddgCache, drugClass + administation , new CacheProcessor<List<TDrugDosage>>() {
			@Override
			public List<TDrugDosage> handle() throws Exception {
				PageData pd = new PageData();
				pd.put("DOSE_CLASS_ID", drugClass);
				pd.put("ADMINISTRATION_ID", administation);
				List<TDrugDosage> r = (List<TDrugDosage>) dao.findForObject("InfoMapper.getDdgInfoById",pd);
				pd = null;
				return r;
			}
		});
    	return t;
    	
    }
    
    /**
    * 医嘱执行频率
    * @param performID
    * @param wheres
    * @param query
    * @return
     * @throws Exception 
    */
    public List<TDrugPerformFreqDict> queryDrugPerfoms(String[] performIDs) throws Exception {
        List<TDrugPerformFreqDict> list = new ArrayList<TDrugPerformFreqDict>();
            for(String performID:performIDs) {
            	TDrugPerformFreqDict t = queryDrugPerfom(performID);
            	if(t!=null){
            		list.add(t);
            	}
            }
        return list;
    }
    
    /**
     * 医嘱执行频率
     * @param performID
     * @return
     * @throws Exception
     */
    public  TDrugPerformFreqDict queryDrugPerfom(final String performID  ) throws Exception {
    	TDrugPerformFreqDict t = cacheTemplate.cache(drugPerform, performID, new CacheProcessor<TDrugPerformFreqDict>() {
			@Override
			public TDrugPerformFreqDict handle() throws Exception {
				PageData pd = new PageData();
				pd.put("performID", performID);
				TDrugPerformFreqDict r = (TDrugPerformFreqDict) dao.findForObject("InfoMapper.queryDrugPerfom",pd);
				pd = null;
				return r;
			}
		});
    	return t;
    }
    /**
     * 医保用药 适应列表 
     * @param drug_ID
     * @return
     * @throws Exception 
     */
	public List<TMemo> queryMemoList(final String DRUG_ID) throws Exception {
    	List<TMemo> t = cacheTemplate.cache(commonCache, DRUG_ID , new CacheProcessor<List<TMemo>>() {
			@Override
			public List<TMemo> handle() throws Exception {
				PageData pd = new PageData();
				pd.put("DRUG_ID", DRUG_ID);
				List<TMemo> r = (List<TMemo>) dao.findForObject("InfoMapper.queryMemoList",pd);
				pd = null;
				return r;
			}
		});
    	return t;
	}
	
	
    /**
     * 医保用药 适应列表 
     * @param drug_ID
     * @return
     * @throws Exception 
     */
	public TMedicareCatalog queryTMedicareCatalog(final String DRUG_ID) throws Exception {
    	TMedicareCatalog t = cacheTemplate.cache(commonCache, DRUG_ID , new CacheProcessor<TMedicareCatalog>() {
			@Override
			public TMedicareCatalog handle() throws Exception {
				PageData pd = new PageData();
				pd.put("DRUG_ID", DRUG_ID);
				TMedicareCatalog r = (TMedicareCatalog) dao.findForObject("InfoMapper.queryTMedicareCatalog",pd);
				pd = null;
				return r;
			}
		});
    	return t;
	}
	public TMedicareRslt getDrugMedicareRslt(String drugID) throws Exception {
		TMedicareRslt t = cacheTemplate.cache(commonCache, DRUG_ID , new CacheProcessor<TMedicareRslt>() {
			@Override
			public TMedicareRslt handle() throws Exception {
				TDrug drug = queryDrugById(DRUG_ID);
                if(drug == null) return null;
            	TMedicareRslt mdrsl = new TMedicareRslt();
                TMedicareCatalog  mcare = queryTMedicareCatalog(drug.getDRUG_NO_LOCAL());
                if(mcare == null){
                    mdrsl.setFlag(false);
                    mdrsl.setAlertInfo("该药为医保外用药");
                    mdrsl.setMemo(new ArrayList<TMemo>());
                }else{
                    mdrsl.setFlag(true);
                    List<TMemo> memos = queryMemoList(mcare.getDRUG_ID());
                    mdrsl.setMemo(memos);
                }
                mdrsl.setDrug(drug);
                mdrsl.setMedicareCatalog(mcare);
                return mdrsl;
			}
		});
    	return t;
	}
}
