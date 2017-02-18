package com.ts.service.pdss.pdss.Cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.RSBeans.TDrugInteractionRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugIvEffectRslt;
import com.ts.entity.pdss.pdss.RSBeans.TMedicareRslt;

public class BeanRSCache
{
    /* 互动信息结果缓存 */
    private static Map<String, TDrugInteractionRslt> drugInteraction = new LinkedHashMap<String, TDrugInteractionRslt>();
    /* 配伍 */
    private static Map<String, TDrugIvEffectRslt>   drugIvEffectRslt = new LinkedHashMap<String, TDrugIvEffectRslt>();
    /* 医保审查模块 */
    private static Map<String, TMedicareRslt>       drugMedicareRslt = new LinkedHashMap<String, TMedicareRslt>();
    
    /**
     *  获得医保审查结果
     * @param key
     * @return
     */
    public static TMedicareRslt getDrugMedicareRslt(String key )
    {
        if(drugMedicareRslt.containsKey(key))
            return drugMedicareRslt.get(key);
        return null;
    }
    
    /**
     * 设置医保审查结构
     * @param key
     * @param value
     */
    public static void setDrugMedicareRslt(String key,TMedicareRslt value)
    {
        if(drugMedicareRslt.containsKey(key))
            return ;
        drugMedicareRslt.put(key, value);
    }
    
    
    /**
     * 获得配伍信息结果
     * @param key
     * @return
     */
    public static TDrugIvEffectRslt getDrugIvEffectRslt(String key1 , String key2)
    {
        TDrugIvEffectRslt result = null;
        result = drugIvEffectRslt.get(key1 + key2);
        if(result == null)
            result = drugIvEffectRslt.get(key2 + key1);
        return result;
    }

    /**
     * 设置配伍信息结果
     * @param key
     * @param value
     */
    public static void setDrugIvEffectRslt(String key1,String key2, TDrugIvEffectRslt value)
    {
        TDrugIvEffectRslt result = null;
        result = drugIvEffectRslt.get(key1 + key2);
        if(result == null)
            result = drugIvEffectRslt.get(key2 + key1);
        if(result == null)
            drugIvEffectRslt.put(key1 + key2, value);
        result = null;
    }
    
    /**
     * 获得互动信息结果
     * @param key
     * @return
     */
    public static TDrugInteractionRslt getDrugInteraction(String key1 , String key2)
    {
        TDrugInteractionRslt result = null;
        result = drugInteraction.get(key1.toUpperCase() + "__" + key2.toUpperCase());
        if(result == null)
            result = drugInteraction.get(key2.toUpperCase() + "__" + key1.toUpperCase());
        return result;
       // return drugInteraction.get(key1.toUpperCase() + "__" + key2.toUpperCase());
        
    }

    /**
     * 设置互动信息结果
     * @param key
     * @param value
     */
    public static void setDrugInteraction(String key1,String key2, TDrugInteractionRslt value)
    {
        //TDrugInteractionRslt result = null;
        //result = drugInteraction.get(key1.toUpperCase() + "__" + key2.toUpperCase());
        //if(result == null)
        //    result = drugInteraction.get(key2 + key1);
        //if (result == null)
        //{
        drugInteraction.put(key1.toUpperCase() + "__" + key2.toUpperCase(), value);
        //}
        //result = null;
    }
    
    public static int loadDrugInteractionInfo()
    {
    	JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
    	String sql = "select Drug_No_LocalA, Drug_No_LocalB, Drug_Interaction_Maps from Drug_Interaction_Map";
    	@SuppressWarnings("unchecked")
		List<TCommonRecord> list = query.query(sql, new CommonMapper());
    	for (TCommonRecord cr : list)
    	{
    		TDrugInteractionRslt dir = new TDrugInteractionRslt();
    		String Key1 = cr.get("Drug_No_LocalA");
    		String Key2 = cr.get("Drug_No_LocalB");
    		String[] InteractionIDs = cr.get("Drug_Interaction_Maps").split(",");
    		List<TDrugInteractionInfo> listx = new ArrayList<TDrugInteractionInfo>();
    		for (String id : InteractionIDs)
    		{
    			TDrugInteractionInfo dii = BeanCache.getDrugInteractionInfo(id);
    			if (dii != null)
    				listx.add(dii);
    		}
    		TDrug drugA = BeanCache.getDrug(Key1);
    		TDrug drugB = BeanCache.getDrug(Key2);
    		dir.addDrugInfo(drugA, drugB, listx);
    		setDrugInteraction(Key1, Key2, dir);
    	}
    	query = null;
    	return list.size();
    }
}
