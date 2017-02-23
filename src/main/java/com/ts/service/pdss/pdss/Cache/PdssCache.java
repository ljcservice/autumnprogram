package com.ts.service.pdss.pdss.Cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Utils.Config;
import com.ts.dao.DaoSupportPdss;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;
import com.ts.entity.pdss.pdss.RSBeans.TDrugInteractionRslt;
import com.ts.service.cache.CacheProcessor;
import com.ts.service.cache.CacheTemplate;
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
//			    		sql = administrationidSql.replaceAll("X1", strWheres.toString());
		    		res = (TAdministration) dao.findForObject("DrugMapper.getAdministration",administrationID);
		    	}else {
//			    		sql = administrationidSqlLocal.replace("X1", strWheres.toString());
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
				return r;
			}
		});
    	return t;
	}
}
