package com.ts.service.pdss.pdss.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Utils.DateUtils;
import com.ts.entity.pdss.mas.Beans.TMemo;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TAllergIngrDrug;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.entity.pdss.pdss.Beans.TDrugDosage;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugMap;
import com.ts.entity.pdss.pdss.Beans.TDrugPerformFreqDict;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;

public class BeanCache
{
    /* 药品字典 */
    //private static Map<TDrug, TDrug>                               drugCache       = new LinkedHashMap<TDrug, TDrug>();
    /* 以本地药品码为主键的药品Bean */
    private static Map<String, TDrug>                           drugCacheByLocal   = new HashMap<String, TDrug>();
    /* 药品途径 */
    private static Map<TAdministration, TAdministration>           drugadmini      = new LinkedHashMap<TAdministration, TAdministration>();
    /* 医嘱执行频率字典 */
    private static Map<TDrugPerformFreqDict, TDrugPerformFreqDict> drugPerform     = new LinkedHashMap<TDrugPerformFreqDict, TDrugPerformFreqDict>();
    /* 药物相互作用信息 */
    private static Map<TDrugInteractionInfo, TDrugInteractionInfo> diiCache        = new LinkedHashMap<TDrugInteractionInfo, TDrugInteractionInfo>();
    private static Map<String, TDrugInteractionInfo>               diiCacheEx      = new LinkedHashMap<String, TDrugInteractionInfo>();
    /* 药物禁忌症对应 */
    private static Map<TDrugDiagRel, TDrugDiagRel>                 ddrCache        = new LinkedHashMap<TDrugDiagRel, TDrugDiagRel>();
    /* 药物禁忌症信息 */
    private static Map<TDrugDiagInfo, TDrugDiagInfo>               ddiCache        = new LinkedHashMap<TDrugDiagInfo, TDrugDiagInfo>();
    /* 药物成分、药敏、药物分类与药物对照字典 */
    private static Map<String,List<TAllergIngrDrug>>               aidCache        = new LinkedHashMap<String, List<TAllergIngrDrug>>();
    /* 特殊人群 */
    private static Map<String, TDrugUseDetail>                     dudCache        = new LinkedHashMap<String, TDrugUseDetail>();
    /* 药品剂量使用字典 */
    private static Map<String, List<TDrugDosage>>                  ddgCache        = new LinkedHashMap<String, List<TDrugDosage>>();
    /* 适应列表 */
    private static Map<String, List<TMemo>>                        memoCache       = new LinkedHashMap<String, List<TMemo>>();
    /* 公用缓存 */
    private static Map<Object, Object>                             commonCache     = new LinkedHashMap<Object, Object>();
    /* 药品map( 使用抗菌药物 ) */
    private static Map<String,TDrugMap>                            drugMap        = new LinkedHashMap<String, TDrugMap>();
    /* 疾病与诊断 缓存 */
    private static Map<String, List<TCommonRecord>>                      diseageVsdiag  = new HashMap<String, List<TCommonRecord>>();
    /* 缓存最大数  */
    private static int                                             dbCacheMaxStore = 0;
    private static boolean viewCache = false;
    static
    {
        try
        {
           /**
            Properties properties = new Properties();
            InputStream inputStream = BeanCache.class
                    .getResourceAsStream("/DBCachecfg.properties");
            properties.load(inputStream);
            */
            // dbCacheMaxStore =
            // Integer.parseInt(properties.getProperty("dbCacheMaxStore"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setViewCache(boolean value)
    {
    	viewCache = value;
    }
    
    public static boolean getViewCache()
    {
    	return viewCache;
    }
    
    public static void setMemo(String key , List<TMemo> value)
    {
        
    }
    
    /**
     * 过敏缓存
     * 只有过敏将类的使用信息在queryutils 中进行累加
     * @param key
     * @return
     */
    public static List<TAllergIngrDrug> getAid(String key)
    {
        if(aidCache.containsKey(key))
        {
            return aidCache.get(key);
        }
        if (viewCache)
        	System.out.println("缓存未命中-过敏：" + key);
        return null;
    }
    
    /**
     * 过敏新缓存添加
     * @param key
     * @param drug
     */
    public static void setAid(String key,TAllergIngrDrug value)
    {
        if(aidCache.containsKey(key))
        {
            value.setCreateDate(DateUtils.getDateTime());
            aidCache.get(key).add(value);
        }
        else
        {
            aidCache.put(key, new ArrayList<TAllergIngrDrug>());    
        }
    }
    
    /**
     * 设置药物剂量
     * @param key
     * @param value
     */
    public static void setDdg(String key , List<TDrugDosage> value)
    {
        for(int i = 0 ;i<value.size();i++)
        {
            value.get(i).setCreateDate(DateUtils.getDateTime());
        }
        ddgCache.put(key, value);
    }
    
    /**
     * 获得药物剂量
     * @param key
     * @return
     */
    public static List<TDrugDosage> getDdg(String key)
    {
        if(ddgCache.containsKey(key))
        {
            for(int i = 0 ;i<ddgCache.get(key).size();i++)
            {
                ddgCache.get(key).get(i).setLastUseDate(DateUtils.getDateTime());
            }
            return ddgCache.get(key);
        }
        if (viewCache)
        	System.out.println("缓存未命中-药物剂量：" + key);
        return null;
    }
    
    /**
     * 特殊人群
     * @param drugClass
     * @return
     */
    public static TDrugUseDetail getDud(String drugClass)
    {
        if(dudCache.containsKey(drugClass))
        {
            dudCache.get(drugClass).setLastUseDate(DateUtils.getDateTime());
            return dudCache.get(drugClass);
        }
        if (viewCache)
        	System.out.println("缓存未命中-特殊人群：" + drugClass);
        return null;
    }
    
    /**
     * 特殊人群
     * @param drugClass
     * @param dud
     */
    public static void setDud(String drugClass,TDrugUseDetail dud)
    {
        dud.setCreateDate(DateUtils.getDateTime());
        dudCache.put(drugClass, dud);
    }
    
    /**
     * 获得药品信息，已进入快照
     * @param drug
     * @return
     */
    public static TDrug getDrug(String DrugNoLocal)
    {
        if (drugCacheByLocal.containsKey(DrugNoLocal))
        {
            TDrug d = drugCacheByLocal.get(DrugNoLocal);
            d.setLastUseDate(DateUtils.getDateTime());
            return new TDrug(d);
        }
        if (viewCache)
        	System.out.println("缓存未命中--药品信息：" + DrugNoLocal);
        return null;
    }
    
    public static Map<String, TDrug>  getDrugAll()
    {
        return drugCacheByLocal;
    }
    
    /**
     * 设置药品信息 
     * @param drug
     */
    public static void putDrug(TDrug drug)
    {
        drug.setCreateDate(DateUtils.getDateTime());
        drugCacheByLocal.put(drug.getDRUG_NO_LOCAL(), drug);
    }
  
 
    /**
     * 已进入快照
     */
    public static TDrugInteractionInfo getDrugInteractionInfo(TDrugInteractionInfo dii)
    {
        if (diiCache.containsKey(dii))
        {
            diiCache.get(dii).setLastUseDate(DateUtils.getDateTime());
            return diiCache.get(dii);
        }
        if (viewCache)
        	System.out.println("缓存未命中-相互作用：" + dii.getINGR_CLASS_CODE1() + " vs " + dii.getINGR_CLASS_CODE2());
        return null;
    }

    public static void setDrugInteractionInfo(TDrugInteractionInfo dii)
    {
        dii.setCreateDate(DateUtils.getDateTime());
        diiCache.put(dii, dii);
        diiCacheEx.put(dii.getDRUG_INTERACTION_INFO_ID(), dii);
    }

    public static TDrugInteractionInfo getDrugInteractionInfo(String Id)
    {
    	if (diiCacheEx.containsKey(Id))
    	{
    		TDrugInteractionInfo dii = diiCacheEx.get(Id);
    		dii.setLastUseDate(DateUtils.getDateTime());
    		return dii; 
    	}
    	return null;
    }
    
    /**
     * 已进入快照
     * @param ddr
     * @return
     */
    public static TDrugDiagRel getDrugDiagRel(TDrugDiagRel ddr)
    {
        if (ddrCache.containsKey(ddr))
        {
            ddrCache.get(ddr).setLastUseDate(DateUtils.getDateTime());
            return ddrCache.get(ddr);
        }
        if (viewCache)
        	System.out.println("缓存未命中-禁忌症：" + ddr.getADMINISTRATION_ID() + " vs " + ddr.getDRUG_CLASS_ID());
        return null;
    }

    /**
     * 药物禁忌症对应
     * @param ddr
     */
    public static void setDrugDiagRel(TDrugDiagRel ddr)
    {
        ddr.setCreateDate(DateUtils.getDateTime());
        ddrCache.put(ddr, ddr);
    }

    
    public static TDrugDiagInfo getDrugDiagInfo(TDrugDiagInfo ddi)
    {
        if (ddiCache.containsKey(ddi))
        {
            ddiCache.get(ddi).setLastUseDate(DateUtils.getDateTime());
            return ddiCache.get(ddi);
        }
        if (viewCache)
        	System.out.println("缓存未命中-禁忌症x：" + ddi.getDRUG_DIAG_INFO_ID());
        return null;
    }

    public static void setDrugDiagInfo(TDrugDiagInfo ddi)
    {
        ddi.setCreateDate(DateUtils.getDateTime());
        ddiCache.put(ddi, ddi);
    }

    /**
     * 通用缓存
     * 
     * @param obj
     * @return 
     * @return
     */
    public static Object getCommonCache(Object obj)
    {
        if (commonCache.containsKey(obj))
            return commonCache.get(obj);
        return null;
    }

    /**
     * 通用缓存
     * 
     * @param obj
     * @return
     */
    public static void setCommonCache(Object obj)
    {
        commonCache.put(obj, obj);
    }

    /**
     * 用药途径
     * 
     * @param obj
     * @return
     */
    public static TAdministration getAdministration(TAdministration obj)
    {
        if (drugadmini.containsKey(obj))
        {
        	TAdministration adm = drugadmini.get(obj);
        	adm.setLastUseDate(DateUtils.getDateTime());
            return adm;
        }
        if (viewCache)
        	System.out.println("缓存未命中-用药途径：" + obj.getADMINISTRATION_ID());
        return null;
    }

    /**
     * 用药途径
     * 
     * @param obj
     * @return
     */
    public static void setAdministration(TAdministration obj)
    {
        obj.setCreateDate(DateUtils.getDateTime());
        drugadmini.put(obj, obj);
    }

    /**
     * 医嘱执行频率
     * 
     * @param obj
     * @return
     */
    public static TDrugPerformFreqDict getDrugPerform(TDrugPerformFreqDict obj)
    {
        if (drugPerform.containsKey(obj))
        {
            drugPerform.get(obj).setLastUseDate(DateUtils.getDateTime());
            return drugPerform.get(obj);
        }
        if (viewCache)
        	System.out.println("缓存未命中-执行频率：" + obj.getPERFORM_FREQ_DICT_ID());
        return null;
    }

    /**
     * 医嘱执行频率
     * 
     * @param obj
     * @return
     */
    public static void setPerformFreqDict(TDrugPerformFreqDict obj)
    {
        obj.setCreateDate(DateUtils.getDateTime());
        drugPerform.put(obj, obj);
    }
    
    /**
     * 药品map设置
     * @param dm
     */
    public static void setDrugMap(TDrugMap dm)
    {
        if(!drugMap.containsKey(dm.getDRUG_NO_LOCAL() + dm.getDRUG_SPEC()))
        {
            dm.setCreateDate(DateUtils.getDateTime());
            drugMap.put(dm.getDRUG_NO_LOCAL() , dm);    
        }
    }
    
    /**
     * 获得 drugMap 信息 
     * @param Code
     * @return
     */
    public static TDrugMap getDrugMap(String Code)
    {
        if(drugMap.containsKey(Code))
        {
            drugMap.get(Code).setLastUseDate(DateUtils.getDateTime());
            return drugMap.get(Code);
        }
        if (viewCache)
            System.out.println("缓存未命中-药品map ：" + Code );
        return null;
    }
    
    public static void setDiseageVsDiag(String DiagCodekey , List<TCommonRecord> listTCom)
    {
        diseageVsdiag.put(DiagCodekey, listTCom);
    }
    
    public static List<TCommonRecord> getDiseageVsDiag(String diagCodeKey)
    {
        if(diseageVsdiag.containsKey(diagCodeKey))
           return  diseageVsdiag.get(diagCodeKey);
        return null;
    }
}
