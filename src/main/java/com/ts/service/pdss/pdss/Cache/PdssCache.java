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
import com.ts.entity.pdss.pdss.Beans.DrugUseAuth.TCkDrugUserAuth;
import com.ts.entity.pdss.pdss.Beans.ias.TOperationDrugInfo;
import com.ts.entity.pdss.pdss.RSBeans.TDrugInteractionRslt;
import com.ts.entity.pdss.pdss.RSBeans.TMedicareRslt;
import com.ts.service.cache.CacheProcessor;
import com.ts.service.cache.CacheTemplate;
import com.ts.util.PageData;

@Service
public class PdssCache {
	public static String drugCacheByLocal = "drugCacheByLocal";	//单个药品信息kEY，以本地药品码为主键的药品Bean 
	public static String drugInteraction  = "drugInteraction";	//互动信息结果缓存
	public static String diiCache         = "diiCache";			//药品成分冲突Key
	public static String DiseageVsDiag    = "DiseageVsDiag";	//诊断对应的疾病key
	public static String drugadmini       = "drugadmini";		//用药途径key
	public static String ddrCache         = "ddrCache";			// 药物禁忌症对应key
	public static String drugRepeatCache  = "drugRepeatCache";  // 重复给药key
	public static String ddisCache        = "ddisCache"; 		// 药物禁忌信息
	public static String drugIvEffect     = "drugIvEffect";		// 配伍信息
	public static String dudCache         = "dudCache";			// 特殊人群
	public static String aidCache         = "aidCache"; 		/* 药物成分、药敏、药物分类与药物对照字典 */
    public static String ddgCache         = "ddgCache"; 		//药品剂量使用字典
    public static String drugPerform      = "drugPerform";		/* 医嘱执行频率字典 */
    public static String commonCache      = "commonCache";		// 公用缓存
    public static String drugMedicareRslt = "drugMedicareRslt";	// 公用缓存
    public static String drugMedicare     = "drugMedicare";		// 公用缓存
    public static String diagnosisDict    = "diagnosisDict";    // 诊断字典
    public static String drugSideDict     = "drugSideDict";     // 不良反应
    public static String OperationDrug    = "operationdrug";    // 手术使用药品
    public static String DrugUserAuth     = "drugUserAuth";     // 药物控制授权 
    
	@Autowired
	private CacheTemplate cacheTemplate;
	
//	@Resource(name = "daoSupportPdss")
//	private DaoSupportPdss dao;
	
    /**
     * 药物禁忌症对应，已应用数据库快照
     * @param DrugClassIDs
     * @param drugAdminis
     * @return
     * @throws Exception 
     */
//    public List<TDrugDiagRel> queryDrugDiagRels(String[] drugClassIDs, String[] drugAdmini) throws Exception
//    {
//        final List<TDrugDiagRel> list = new ArrayList<TDrugDiagRel>();
//        List<String> param = new ArrayList<String>();
//        for (int i = 0; i < drugClassIDs.length; i++){
//            for (int j = 0; j < drugAdmini.length; j++){
//	        	if(param.contains(drugClassIDs[i]+"_"+drugAdmini[j])){
//	        		//重复数据
//	        	}else{
//	        		TDrugDiagRel o = queryDrugDiagRel(drugClassIDs[i], drugAdmini[j]);
//	        		if(o!=null){
//	        			list.add(o);
//	        		}
//	        		param.add(drugClassIDs[i]+"_"+drugAdmini[j]);
//	        	}
//            }
//        }       
//        return list;	
//    	
//    }

	/**
	 * 获得药物授权控制 实体 
	 * @param drugCode
	 * @param drugName
	 * @param deptName
	 * @param doctorName
	 * @return
	 * @throws Exception
	 */
	public Map<String, TCkDrugUserAuth> queryDrugUserAuthByMap(String drugCode , String drugName , String deptName , String doctorName) throws Exception
	{
	    String key = drugCode + "_" + drugName + "_" + deptName + "_" + doctorName;
	    return cacheTemplate.cache(DrugUserAuth, key , null);
	}
	
	
	/**
	 * 更新药物授权控制缓存 
	 * @param entity  要缓存 实体 
	 * @param drugCode
	 * @param drugName
	 * @param deptName
	 * @param doctorName
	 */
	public void setDrugUserAuthByMap(Map<String,TCkDrugUserAuth> entity ,String drugCode , String drugName , String deptName , String doctorName )
	{
	    String key = drugCode + "_" + drugName + "_" + deptName + "_" + doctorName;
	    cacheTemplate.setObject(DrugUserAuth, key, -1, entity);
	}
	
	
	
	/**
	 * 手术药品管理  - 手术编码 或者 名字 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public List<TOperationDrugInfo> queryOperationDrugByCode(final String key ) throws Exception
	{
	    return cacheTemplate.cache(OperationDrug, key , null);
	}
	
	
	/**
	 * 药品重复给药 
	 * @param drugClassId1
	 * @param drugClassId2
	 * @return
	 * @throws Exception
	 */
	public TDrugRepeat queryDrugRepeat(final String drugClassId1,final String drugClassId2) throws Exception{
	    
	    String drugClassIdx1 = drugClassId1;
        String drugClassIdx2 = drugClassId2;
        if(drugClassId1.compareTo(drugClassId2) > 0)
        {
            drugClassIdx1 = drugClassId2;
            drugClassIdx2 = drugClassId1;
        }
	    TDrugRepeat t = cacheTemplate.cache(drugRepeatCache , drugClassIdx1 + "_" + drugClassIdx2, null);
        return t;
    }
	
    /**
     * 药物禁忌症对应，已应用数据库快照
     * @param DrugClassID
     * @param drugAdmini
     * @return
     * @throws Exception 
     */
  	public TDrugDiagRel queryDrugDiagRel(final String drugClassId,final String drugAdmini) throws Exception{
  		TDrugDiagRel t = cacheTemplate.cache(ddrCache , drugClassId + "_" + drugAdmini, null);
  		
//  		new CacheProcessor<TDrugDiagRel>() {
//  			@Override
//  			public TDrugDiagRel handle() throws Exception {
//            	PageData pd = new PageData();
//            	pd.put("drugClassId", drugClassId);
//            	pd.put("drugAdmini", drugAdmini);
//            	TDrugDiagRel res = (TDrugDiagRel) dao.findForObject("DrugMapper.queryDrugDiagRel",pd);
//            	pd = null;
//            	return res;
//  			}
//  		}
      	return t;
  	}
  	
  	/**
  	 * 获取药品禁忌信息数据，
  	 * @param contraind
  	 * @return List
  	 * @throws Exception
  	 */
  	public  List<TDrugDiagInfo> getDrugDiagInfos(final String contraind )throws Exception {
  		List<TDrugDiagInfo> rs = cacheTemplate.cache(ddisCache ,contraind, null);
  		return rs;
  	}
    
//	/**
//	 * 用药途径，多个查询
//	 * @param administrationID
//	 * @return
//	 * @throws Exception 
//	 */
//	public List<TAdministration> queryAdministrations(String[] administrationID) throws Exception{
//        final List<TAdministration> list = new ArrayList<TAdministration>();
//        List<String> param = new ArrayList<String>();
//        
//        for (int i = 0; i < administrationID.length; i++)
//        {	
//        	if(param.contains(administrationID[i])){
//        		//重复数据
//        	}else{
//        		TAdministration aid = queryAdministration(administrationID[i]);
//        		list.add(aid);
//        		param.add(administrationID[i]);
//        	}
//        }       
//        return list;		
//	}
//	
  	
  	/**
  	 * 用给药途径名字 获得给药信息
  	 * @param adminiName
  	 * @return
  	 * @throws Exception
  	 */
  	public TAdministration queryAdministrationByName(final String adminiName ) throws Exception {
  		TAdministration t = cacheTemplate.cache(drugadmini ,"name_" + adminiName, null);
  		if(t == null)
  			t =  cacheTemplate.cache(drugadmini , "code_" + adminiName, null);
  		return t;
  	}
  	
	/**
	 * 用药途径单个缓存查询
	 * @param administrationID
	 * @return
	 * @throws Exception 
	 */
	public TAdministration queryAdministration(final String administrationID) throws Exception{
		
		//只从redis中取得数据
		TAdministration t =  cacheTemplate.cache(drugadmini , "code_" + administrationID, null);
		if( t == null) 
			 t = cacheTemplate.cache(drugadmini ,"name_" + administrationID, null);
		
//		new CacheProcessor<TAdministration>() {
//			@Override
//			public TAdministration handle() throws Exception {
//				TAdministration res=null;
//				PageData pd = new PageData();
//				pd.put("ADMINISTRATION_ID", administrationID);
//		    	/* 修改 
//		    	 * 用来区分 his 系统 传入 的是 本地码 或是  诊断名称 */
//		    	if(Config.getParamValue("admin_conv_flag").equals("0"))
//		    	{
//		    		res = (TAdministration) dao.findForObject("DrugMapper.getAdministration",pd);
//		    	}else
//		    	{
//		    		res = (TAdministration) dao.findForObject("DrugMapper.getAdministrationLocal",pd);
//		    	}
//				return res;
//			}
//		}
		
    	return t;
	}
	
	 //查询两个药品，是否互相药理冲突
    public TDrugInteractionRslt queryDrugInteraction(final TDrug drugA,final TDrug drugB) throws Exception{
        // 如果没有药品就过。
    	if(drugA == null || drugB == null) return null;
    	String key1 = drugA.getDRUG_NO_LOCAL();
        String key2 = drugB.getDRUG_NO_LOCAL();
        if (key1.compareTo(key2) > 0)
        {
        	key1 = drugB.getDRUG_NO_LOCAL();
            key2 = drugA.getDRUG_NO_LOCAL();
        }
//        Long l = System.currentTimeMillis();
    	TDrugInteractionRslt t = cacheTemplate.cache(drugInteraction , key1 + "_" + key2, null );
//    	if(t == null)
//    		t = cacheTemplate.cache(drugInteraction , key2 + "_" + key1, null );
//    	new CacheProcessor<TDrugInteractionRslt>() {
//			@Override 
//			public TDrugInteractionRslt handle() {
//				TDrugInteractionRslt res = new TDrugInteractionRslt();
//				try {
//                    // 药品A成分 分割后 组装sql
//                    if(drugA.getINGR_CLASS_IDS() == null)
//                        return null;
//                    String[] ingrclassidsA = drugA.getINGR_CLASS_IDS().split(",");
//                    // 药品B成分  分割后 组装sql
//                    if(drugB == null || drugB.getINGR_CLASS_IDS() == null)
//                    	return null;
//                    String[] ingrclassidsB = drugB.getINGR_CLASS_IDS().split(",");
//                    Long x = System.currentTimeMillis();
//                    List<TDrugInteractionInfo> list = queryDrugInteractionInfo(ingrclassidsA,ingrclassidsB, null);
//                    System.out.println("TDrugInteractionInfo:" + (System.currentTimeMillis() - x));
//                    if(list != null && list.size() > 0){
//                    	res.addDrugInfo(new TDrug(drugA), new TDrug(drugB), list);
//                    }
//                    else {
//                    	return null;
//                    }
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return res;
//			}
//		}
//    	System.out.println(System.currentTimeMillis() - l);
    	return t;
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
    public List<TDrugInteractionInfo> queryDrugInteractionInfo(String[] Codes1, String[] Codes2,String wheres) throws Exception
    {
        List<TDrugInteractionInfo> list = new ArrayList<TDrugInteractionInfo>();
        for (int i = 0; i < Codes1.length; i++)
        {
            for (int j = 0 ; j < Codes2.length; j++)
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
    	TDrugInteractionInfo t = cacheTemplate.cache(diiCache , code1 + "_" + code2, null);
    	//去除缓存中没有的时 ，重新查数据库 
//    	new CacheProcessor<TDrugInteractionInfo>() {
//			@Override
//			public TDrugInteractionInfo handle() throws Exception {
//				PageData pd = new PageData();
//				pd.put("code1", code1);
//				pd.put("code2", code2);
//				TDrugInteractionInfo res = (TDrugInteractionInfo) dao.findForObject("DrugMapper.getDrugInteraction",pd);
//				pd = null;
//				return res;
//			}
//		}
    	return t;
    }
    
    /**
     * 查询单个药品信息，使用缓存
     * @param id
     * @return
     * @throws Exception 
     */
    public TDrug queryDrugById(final String id) throws Exception{
    	TDrug t = cacheTemplate.cache(drugCacheByLocal, id, null);
//    	new CacheProcessor<TDrug>() {
//			@Override
//			public TDrug handle() throws Exception {
//				PageData pd = new PageData();
//				pd.put("id", id);
//				@SuppressWarnings("unchecked")
//				List<TDrug> drugs = (List<TDrug>) dao.findForList("DrugMapper.queryDrugById", pd);
//				TDrug res = null;
//				if(drugs != null && drugs.size() > 0 )
//					res = drugs.get(0);
//				return res;
//			}
//		}
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
    public List<PageData> getDiseageVsDiag(final String Diag_Code) throws Exception{
    	List<PageData> t = cacheTemplate.cache(DiseageVsDiag,Diag_Code,null);
    	
    	
//    	 new CacheProcessor<List<PageData>>() {
// 			@Override
// 			public List<PageData> handle() throws Exception {
// 				PageData pd = new PageData();
// 				pd.put("diagnosis_code",Diag_Code);
// 				List<PageData> r = (List<PageData>) dao.findForObject("DrugMapper.getDiseageVsDiag",pd);
// 				return r;
// 			}
// 		}
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

		List<TDrugIvEffect> t = null;
		if (Integer.parseInt(drugIvCode1) > Integer.parseInt(drugIvCode2)) {
			t = cacheTemplate.cache(drugIvEffect,drugIvCode2 + "_" + drugIvCode1,null);
		}else
		{
			t = cacheTemplate.cache(drugIvEffect,drugIvCode1 + "_" + drugIvCode2,null);
		}
    	//    	, new CacheProcessor<List<TDrugIvEffect>>() {
//			@Override
//			public List<TDrugIvEffect> handle() throws Exception {
//				PageData pd = new PageData();
//				pd.put("code1", drugIvCode1);
//				pd.put("code2", drugIvCode2);
//				List<TDrugIvEffect> r = (List<TDrugIvEffect>) dao.findForObject("DrugMapper.queryDrugIvEffect",pd);
//				pd = null;
//				return r;
//			}
//		}
    	
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
        	if(dud == null) continue;
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
//    public List<TDrugUseDetail> queryDrugDudList(List<TDrug> drugs ) throws Exception
//    {
//        List<TDrugUseDetail> list = new ArrayList<TDrugUseDetail>();
//        for(TDrug drug:drugs)
//        {  
//        	TDrugUseDetail dud = (TDrugUseDetail)getDud(drug.getDRUG_CLASS_ID());
//        	list.add(dud);
//        }
//        return list;
//    }
    /**
     * 药品  特殊人群，给药途径
     * @param drugClass
     * @return
     * @throws Exception 
     */
    public TDrugUseDetail getDud(final String drugClass) throws Exception
    {
    	if(drugClass == null || "".equals(drugClass)) return null;
    	TDrugUseDetail t = cacheTemplate.cache(dudCache , drugClass,null);
    	
//    	 new CacheProcessor<TDrugUseDetail>() {
// 			@Override
// 			public TDrugUseDetail handle() throws Exception {
// 				PageData pd = new PageData();
// 				pd.put("DRUG_CLASS_ID", drugClass);
// 				TDrugUseDetail r = (TDrugUseDetail) dao.findForObject("DrugMapper.getDudInfoById",drugClass);
// 				pd = null;
// 				return r;
// 			}
// 		}
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
    	List<TAllergIngrDrug> t = cacheTemplate.cache(aidCache,allergenID,null);
//    	new CacheProcessor<List<TAllergIngrDrug>>() {
//			@Override
//			public List<TAllergIngrDrug> handle() throws Exception {
//				PageData pd = new PageData();
//				pd.put("drug_allergen_id", allergenID);
//				List<TAllergIngrDrug> r = (List<TAllergIngrDrug>) dao.findForObject("InfoMapper.getAidInfoById",pd);
//				pd = null;
//				return r;
//			}
//		}
    	return t;
    	
    }
    
    
    /**
     * 药品剂量使用字典
     * @param allergenID    
     * @param drugClassid
     * @return
     * @throws Exception 
     */
    public  List<TDrugDosage> getDdg(final String dosageClass ) throws Exception
    {
        List<TDrugDosage> t = cacheTemplate.cache(ddgCache, dosageClass, null);
        return t;
    }
    
    /**
     * 药品剂量使用字典
     * @param allergenID    
     * @param drugClassid
     * @return
     * @throws Exception 
     */
    public  List<TDrugDosage> getDdg(final String dosageClass ,final String administation ) throws Exception
    {
    	List<TDrugDosage> t = cacheTemplate.cache(ddgCache, dosageClass + "_" + administation , null);
    	
//    	 new CacheProcessor<List<TDrugDosage>>() {
// 			@Override
// 			public List<TDrugDosage> handle() throws Exception {
// 				PageData pd = new PageData();
// 				pd.put("DOSE_CLASS_ID", drugClass);
// 				pd.put("ADMINISTRATION_ID", administation);
// 				List<TDrugDosage> r = (List<TDrugDosage>) dao.findForObject("InfoMapper.getDdgInfoById",pd);
// 				pd = null;
// 				return r;
// 			}
// 		}
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
//    public List<TDrugPerformFreqDict> queryDrugPerfoms(String[] performIDs) throws Exception {
//        List<TDrugPerformFreqDict> list = new ArrayList<TDrugPerformFreqDict>();
//            for(String performID:performIDs) {
//            	TDrugPerformFreqDict t = queryDrugPerfom(performID);
//            	if(t!=null){
//            		list.add(t);
//            	}
//            }
//        return list;
//    }
    
    /**
     * 医嘱执行频率  code 
     * @param performID
     * @return
     * @throws Exception
     */
    public  TDrugPerformFreqDict queryDrugPerfom(final String performID  ) throws Exception {
    	TDrugPerformFreqDict t = cacheTemplate.cache(drugPerform, "code_" + performID,null);
    	if(t == null)
    	       t = cacheTemplate.cache(drugPerform,"name_" + performID , null);
//    	, new CacheProcessor<TDrugPerformFreqDict>() {
//			@Override
//			public TDrugPerformFreqDict handle() throws Exception {
//				PageData pd = new PageData();
//				pd.put("performID", performID);
//				TDrugPerformFreqDict r = (TDrugPerformFreqDict) dao.findForObject("InfoMapper.queryDrugPerfom",pd);
//				pd = null;
//				return r;
//			}
//		}
    	return t;
    }

    /**
     * 医嘱执行频率  name
     * @param performID
     * @return
     * @throws Exception
     */
    public  TDrugPerformFreqDict queryDrugPerfomByName(final String performID  ) throws Exception {
    	TDrugPerformFreqDict t = cacheTemplate.cache(drugPerform, "name_" + performID , null);
    	return t;
    }
    
    /**
     * 医保用药 适应列表 
     * @param drug_ID
     * @return
     * @throws Exception 
     */
	public List<TMemo> queryMemoList(final String DRUG_ID) throws Exception {
    	List<TMemo> t = cacheTemplate.cache(commonCache, DRUG_ID , null);
    	
//    	new CacheProcessor<List<TMemo>>() {
//			@Override
//			public List<TMemo> handle() throws Exception {
//				PageData pd = new PageData();
//				pd.put("DRUG_ID", DRUG_ID);
//				List<TMemo> r = (List<TMemo>) dao.findForObject("InfoMapper.queryMemoList",pd);
//				pd = null;
//				return r;
//			}
//		}
    	return t;
	}
	
	/**
	 * 诊断信息   用Code 获得诊断信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public PageData queryDiagnosisDictByCode(final String code) throws Exception{
		PageData pd = cacheTemplate.cache(diagnosisDict + "Code_" + code, null);
		return pd;
	}
	
	/**
	 *诊断信息    用 id 获得诊断信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageData queryDiagnosisDictById(final String id) throws Exception{
		PageData pd = cacheTemplate.cache(diagnosisDict + "ID_" + id , null);
		return pd;
	}
	
	/**
	 * 不良反应字典 加载
	 * @param drugClassID
	 * @param adminID
	 * @return
	 * @throws Exception
	 */
	public List<TDrugSideDict> queryDSD(final String drugClassID , final String adminID) throws Exception{
		List<TDrugSideDict> drugSDs = cacheTemplate.cache(drugSideDict , drugClassID + "_" + adminID, null);
		return drugSDs;
	}
	
	
    /**
     * 医保用药 适应列表 
     * @param drug_ID
     * @return
     * @throws Exception 
     */
	public TMedicareCatalog queryTMedicareCatalog(final String DRUG_ID) throws Exception {
    	TMedicareCatalog t = cacheTemplate.cache(drugMedicare, DRUG_ID , null);
    	
//    	new CacheProcessor<TMedicareCatalog>() {
//			@Override
//			public TMedicareCatalog handle() throws Exception {
//				PageData pd = new PageData();
//				pd.put("DRUG_ID", DRUG_ID);
//				TMedicareCatalog r = (TMedicareCatalog) dao.findForObject("DrugMapper.queryTMedicareCatalog",pd);
//				pd = null;
//				return r;
//			}
//		})
    	return t;
	}
	
	public TMedicareRslt getDrugMedicareRslt(final String drugID) throws Exception {
		TMedicareRslt t = cacheTemplate.cache(drugMedicareRslt, drugID , null);
		
//		new CacheProcessor<TMedicareRslt>() {
//			@Override
//			public TMedicareRslt handle() throws Exception {
//				TDrug drug = queryDrugById(drugID);
//                if(drug == null) return null;
//            	TMedicareRslt mdrsl = new TMedicareRslt();
//                TMedicareCatalog  mcare = queryTMedicareCatalog(drug.getDRUG_NO_LOCAL());
//                if(mcare == null){
//                    mdrsl.setFlag(false);
//                    mdrsl.setAlertInfo("该药为医保外用药");
//                    mdrsl.setMemo(new ArrayList<TMemo>());
//                }else{
//                    mdrsl.setFlag(true);
//                    List<TMemo> memos = queryMemoList(mcare.getDRUG_ID());
//                    mdrsl.setMemo(memos);
//                }
//                mdrsl.setDrug(drug);
//                mdrsl.setMedicareCatalog(mcare);
//                return mdrsl;
//			}
//		}
    	return t;
	}
}
