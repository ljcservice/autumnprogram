package com.hitzd.his.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;

/**
 * 药品基本信息  drug_map 查询 
 * @author jingcong
 *
 */
public class DrugUtils 
{
    /**
     * 根据规则确定药品类型
     * @param DrugCode
     * @return
     *   1 表示给定药品码是注射剂
     *   2 表示给定药品码是中药
     *   3 表示给定药品码是麻醉镇痛药
     *   4 表示给定药品码是精神药    
     *   5 表示给定药品码是抗菌药      
     *   0 表示不能给出药品分类
     *  PZ  品种，药品码前多少位可以标识一种药
     *  ZS  注射剂，用药途径分类码为多少时是注射剂
     *  ZY  中成药药品码以什么开头为中药
     *  MZ  麻醉镇痛药，药品码开头为何值时为麻醉药
     *  JS  精神药，同上
     *  KJ  抗菌药，同上
     */
	
	/**
	 * 是否是注射剂
	 * @param DrugCode
	 * @param DrugSpec
	 * @return
	 */
    public static boolean isZSDrug(String DrugCode, String DrugSpec)
	{
//		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
//			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and IS_INJECTION = '1' ";
//			if(DrugSpec !=  null  && !"".equals(DrugSpec)) sql += "and Drug_Spec = '" + DrugSpec + "'";
//			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			String key = DrugCode ; 
			if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
			TCommonRecord cr  = drugMapCollenct.get(key);
			return "1".equals(cr.get("IS_INJECTION"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
//			query = null;
		}
		return false;
	}
	
    /**
     * 是否是注射剂
     * @param DrugCode
     * @return
     */
    public static boolean isZSDrug(String DrugCode)
    {
        return isZSDrug(DrugCode, null);
    }
    
    /**
     * '溶剂标识，0是非溶剂，1是溶剂';
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean isImpregnant(String DrugCode , String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("is_impregnant"));
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return false ;
    }
    
    /**
     * '溶剂标识，0是非溶剂，1是溶剂';
     * @param DrugCode
     * @return
     */
    public static boolean isImpregnant(String DrugCode)
    {
        return  isImpregnant(DrugCode, null);
    }
    
    
    /**
     * 是否是外用药
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean isExternalDrug(String DrugCode, String DrugSpec)
    {
//    	JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
//			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and IS_EXTERNAL = '1' " ;
//			if(DrugSpec != null && !"".equals(DrugSpec)) sql += " and Drug_Spec = '" + DrugSpec + "' ";
//			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("IS_EXTERNAL"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
//			query = null;
		}
		return false;
    }
    
    /**
     * 是否是外用药
     * @param DrugCode
     * @return
     */
    public static boolean isExternalDrug(String DrugCode)
    {
        return isExternalDrug( DrugCode,null);
    }
    
    /**
     * 是否是抗过敏药物
     * @return
     */
    public static boolean isAntiAllergyDrug(String DrugCode , String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("IS_Allergy"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 是否是抗过敏药物
     * @param DrugCode
     * @return
     */
    public static boolean isAntiAllergyDrug(String DrugCode)
    {
        return isAntiAllergyDrug(DrugCode, null);
    }
        
    /**
     * 返回药品的毒理属性
     * @param DrugCode
     * @param DrugSpec
     * @return 返回值为毒药、麻药、精一、精二、贵重、放射、高危、毒麻。
     */
    public static String getDrugToxiProperty(String DrugCode, String DrugSpec)
    {
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "'";
			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			sql = null;
			if (cr == null)
				return "";
			else
				return cr.get("Toxi_Property");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			query = null;
		}
		return "";
    }
    /**
     * 中药饮片
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean isChineseDrug(String DrugCode, String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("IS_CHINESEDRUG"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 中药饮片
     * @param DrugCode
     * @return
     */
    public static boolean isChineseDrug(String DrugCode)
    {
        return isChineseDrug(DrugCode,null);
    }
    
    /**
     * 是否是中药注射剂
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean isCenterDrugZS(String DrugCode, String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("IS_INJECTION")) && "1".equals(cr.get("IS_PATENTDRUG")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
	
    /**
     * 是否是中药注射剂
     * @param DrugCode
     * @return
     */
    public static boolean isCenterDrugZS(String DrugCode)
    {
        return  isCenterDrugZS(DrugCode, null);
    }
    
    /**
     * 是否是中成药
     * @param DrugCode
     * @return
     */
	public static boolean isPatentDrug(String DrugCode,String DrugSpec)
	{
	    try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("IS_PATENTDRUG")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
	}
	
	 /**
     * 是否是中成药
     * @param DrugCode
     * @return
     */
    public static boolean isPatentDrug(String DrugCode)
    {
        return isPatentDrug(DrugCode, null);
    }
    
    /**
     * 抗肿瘤标识，0是非抗肿瘤药，1是抗肿瘤药';
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean isTumor(String DrugCode , String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("is_Tumor")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
	
    /**
     * 抗肿瘤标识，0是非抗肿瘤药，1是抗肿瘤药';
     * @param DrugCode
     * @return
     */
    public static boolean isTumor(String DrugCode)
    {
        return isTumor(DrugCode, null);
    }
	
    /**
     * 危险药物
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean isDanger(String DrugCode , String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("is_danger")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 危险药物
     * @param DrugCode
     * @return
     */
    public static boolean isDanger(String DrugCode )
    {
        return isDanger(DrugCode, null);
    }
    
    
    /**
     * 辅助用药 0 否 1 是
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean IsAssist(String DrugCode , String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("IS_ASSIST")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 辅助用药 0 否 1 是
     * @param DrugCode
     * @return
     */
    public static boolean IsAssist(String DrugCode )
    {
        return IsAssist(DrugCode, null);
    }
    
    /**
     * 辅助用药 0 否 1 是
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean isAlbumin(String DrugCode , String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("IS_ALBUMIN")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 白蛋白 0 否 1 是
     * @param DrugCode
     * @return
     */
    public static boolean isAlbumin(String DrugCode )
    {
        return isAlbumin(DrugCode, null);
    }
    
    
	/**
	 * 是否是精神用药
	 * @param DrugCode
	 * @return 返回精一、精二
	 */
	public static boolean isJSDrug(String DrugCode, String DrugSpec)
	{
	    try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("IS_PSYCHOTIC")) || "2".equals(cr.get("IS_PSYCHOTIC")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
	    
//		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
//		try
//		{
//			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "' and TOXI_PROPERTY like '%精%'";
//			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
//			sql = null;
//			if (cr == null)
//				return "";
//			else
//				return cr.get("TOXI_PROPERTY");
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//		}
//		finally
//		{
//			query = null;
//		}
//		return "";
	}
	
	
	/**
     * 是否是精神用药
     * @param DrugCode
     * @return 返回精一、精二
     */
    public static boolean isJSDrug(String DrugCode)
    {
        return isJSDrug(DrugCode, null);
    }
	
	/**
	 * 是否是医保用药
	 * @param DrugCode
	 * @param DrugSpec
	 * @return 0是非医保，1是甲类医保，2是乙类医保
	 */
    @Deprecated
	public static String isMedcareDrug1(String DrugCode, String DrugSpec)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "'";
			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			sql = null;
			if (cr == null)
				return "";
			else
				return cr.get("is_medcare");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			query = null;
		}
		return "";
	}

	/**
	 * 是否是国家基本药品目录中的药品
	 * @param DrugCode
	 * @param DrugSpec
	 * @return
	 */
    public static boolean isCountryBase(String DrugCode, String DrugSpec)
	{
	    try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("is_basedrug")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
	}
	
    /**
     * 是否是国家基本药品目录中的药品
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
	public static boolean isCountryBase(String DrugCode)
	{
	    return isCountryBase(DrugCode, null);
	}
	
	/**
	 * 是否是兴奋剂
	 * @param DrugCode
	 * @return
	 */
	public static boolean isExhilarantDrug(String DrugCode)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and is_Exhilarant = '1'";
			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			sql = null;
			return cr != null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			query = null;
		}
		return false;
	
	}
	
	/**
	 * 是否是口服制剂
	 * @param DrugCode
	 * @param DrugSpec
	 * @return
	 */
	public static boolean IsOralDrug(String DrugCode, String DrugSpec)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and is_oral = '1'";
			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			sql = null;
			return cr != null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			query = null;
		}
		return false;
		
	}

	/**
     * 是否是口服制剂
     * @param DrugCode
     * @return
     */
    public static boolean IsOralDrug(String DrugCode)
    {
        return IsOralDrug(DrugCode,null);
    }
	

	/**
	 * 是否是抗菌药
	 * @param DrugCode
	 * @return
	 */
	public static boolean isKJDrug(String DrugCode,String DrugSpec)
	{
	    try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("is_anti")));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
	}
	
	/**
     * 是否是抗菌药
     * @param DrugCode
     * @return
     */
	public static boolean isKJDrug(String DrugCode)
	{
	    return isKJDrug(DrugCode,null);
	}
	
	/**
	 * 判断是否是介入手术
	 * @param OperName
	 * @param OperID
	 * @return
	 */
	public static boolean isJROperation(String OperName, String OperID)
	{
		String value = Config.getParamValue("JROperation");
		if (value == null) return false;
		String[] v = value.split(";");
		for (String x : v)
		{
			if (OperName.equalsIgnoreCase(x))
				return true;
		}
		return false;
	}
	
	/**
	 * 是否是特殊级抗菌药
	 * @param DrugCode
	 * @param DrugSpec
	 * @return 
	 */
	public static boolean isSpecDrug(String DrugCode, String DrugSpec)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where is_anti = '1' and Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "' and anti_level = '3'";
			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			sql = null;
			return cr != null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			query = null;
		}
		return false;
		/*
		String value = Config.getParamValue("SpecDrug");
        if (value == null) return false;
        String[] v = value.split(";");
        for (String x : v)
        {
            if (DrugCode.toUpperCase().startsWith(x.toUpperCase()))
                return true;
        }
        return false;
        */
	}
	
	/**
	 * 返回抗菌药品 分级 药品登记
	 * @param DrugCode
	 * @param DrugSpec
	 * @return 1是非限制用药，2是限制用药，3是特殊用药
	 */
	public static String getDrugAntiByLevel(String DrugCode, String DrugSpec)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where is_anti = '1' and  Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "'";
			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			sql = null;
			if (cr == null)
				return "";
			else
				return cr.get("anti_level");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			query = null;
		}
		return "";
	}
	
	/**
     * 返回抗菌药品 分级 药品登记
     * @param DrugCode
     * @return 1是非限制用药，2是限制用药，3是特殊用药
     */
    public static String getDrugAntiByLevel(String DrugCode)
    {
        return getDrugAntiByLevel(DrugCode,null);
    }
	
	
	/**
	 * 是否是限制用药
	 * @param DrugCode
	 * @param DrugSpec
	 * @return
	 */
	public static boolean isLimitDrug(String DrugCode, String DrugSpec)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where is_anti = '1' and  Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "' and anti_level = '2'";
			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			sql = null;
			return cr != null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			query = null;
		}
		return false;
		/*
		String value = Config.getParamValue("LimitDrug");
        if (value == null) return false;
        String[] v = value.split(";");
        for (String x : v)
        {
            if (DrugCode.toUpperCase().startsWith(x.toUpperCase()))
                return true;
        }
        return false;
        */
	}
	
	/**
	 * 返回药品类型
	 * @param DrugCode
	 * @param DrugSpec
	 * @param administration
	 * @return 1是注射剂、2是中药、3是麻醉药、4是精神药、5是抗菌药
	 */
    public static String getDrugType(String DrugCode, String DrugSpec, String administration)
    {
    	if (isZSDrug(DrugCode, DrugSpec)) return "1";
    	else if (isPatentDrug(DrugCode)) return "2";
    	else if (isMDrug(DrugCode)) return "3";
    	else if (isJSDrug(DrugCode, DrugSpec)) return "4";
    	else if (isKJDrug(DrugCode)) return "5";
    	else return "0";
    }
    
    /**
     * 该规则指定了医嘱中用药途径为何值时，标识药品是在手术中使用的
     * @param administration
     * @return
     */
    public static boolean isUseMiddle(String administration)
    {
    	String[] values = Config.getParamValue("Use_In_Oper").split(";");
    	for (String v: values)
    	{
    		if (v.trim().equalsIgnoreCase(administration.trim()))
    			return true;
    	}
    	return false;
    }
    
    /**
     * 该规则指定了医嘱中Item_Class，医嘱那些类型为医嘱的用药医嘱 
     * @param ItemClass
     * @return
     */
    public static boolean isDrugInOrder(String ItemClass)
    {
    	String[] values = Config.getParamValue("Drug_In_Order").split(";");
    	for (String v : values)
    	{
    		if (v.trim().equalsIgnoreCase(ItemClass))
    			return true;
    	}
    	return false;
    }
    
    /**
     * 感染标志
     * @param DiagnosisType
     * @return
     */
    public static boolean isInfect(String DiagnosisType)
    {
    	String[] values = Config.getParamValue("Infect_Indi").split(";");
    	for (String v: values)
    	{
    		if (v.trim().equalsIgnoreCase(DiagnosisType))
    			return true;
    	}
    	return false;
    }
    
    /**
     * 重置药品库 drug_map
     */
    @PostConstruct
    public static void ReSetToxiProperty()
    {
        loadDrugMap();
    }
    
    /**
     * 返回是否为毒麻药 
     * @param Code
     * @return
     */
    public static boolean isDMDrug(String DrugCode ,String DrugSpec )
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return ("1".equals(cr.get("IS_POISON")) && "1".equals("IS_HABITFORMING"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 返回是否为毒麻药 
     * @param Code
     * @return
     */
    public static boolean isDMDrug(String DrugCode )
    {
        return  isDMDrug(DrugCode,null);
    }
    
    
    /**
     * 返回是否为毒药 
     * @param Code
     * @return
     */
    public static boolean isDDrug(String DrugCode, String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("IS_POISON"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 返回是否为毒药 
     * @param Code
     * @return
     */
    public static boolean isDDrug(String DrugCode)
    {
        return isDDrug(DrugCode, null);
    }
    
    /**
     * 返回是否为麻药 
     * @param DrugCode 药品代码
     * @param DrugSpec 规格
     * @return
     */
    public static boolean isMDrug(String DrugCode,String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("IS_HABITFORMING"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 返回是否为麻药 
     * @param DrugCode
     * @return
     */
    public static boolean isMDrug(String DrugCode)
    {
        return isMDrug(DrugCode, null);
    }
    
    
    /**
     * 返回是否为一类精神药物 
     * @param Code
     * @return
     */
    public static boolean isYJDrug(String DrugCode , String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("IS_PSYCHOTIC"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    public static boolean isYJDrug(String DrugCode)
    {
        return isYJDrug(DrugCode,null);
    }
    
    /**
     * 返回是否为二类精神药物 
     * @param Code
     * @return
     */
    public static boolean isEJDrug(String DrugCode,String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "2".equals(cr.get("IS_PSYCHOTIC"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 返回是否为二类精神药物 
     * @param DrugCode
     * @return
     */
    public static boolean isEJDrug(String DrugCode)
    {
        return isEJDrug(DrugCode,null);
    }
    
    /**
     * 返回是否为放射药物 
     * @param Code
     * @return
     */
    public static boolean isFSDrug(String DrugCode,String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("IS_RADIATION"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 返回是否为放射药物 
     * @param DrugCode
     * @return
     */
    public static boolean isFSDrug(String DrugCode)
    {
        return isFSDrug(DrugCode, null);
    }
    
    /**
     * 返回是否为贵重药物 
     * @param Code
     * @return
     */
    public static boolean isGZDrug(String DrugCode,String DrugSpec)
    {
        try
        {
            String key = DrugCode ; 
            if(DrugSpec != null && !"".equals(DrugSpec)) key+= "_" + DrugSpec;
            TCommonRecord cr  = drugMapCollenct.get(key);
            return "1".equals(cr.get("IS_PRECIOUS"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * 返回是否为贵重药物 
     * @param DrugCode
     * @return
     */
    public static boolean isGZDrug(String DrugCode)
    {
        return isGZDrug(DrugCode, null);
    }
    
    /**
     * 
     * 去除审核中重复的信息 
     * 每次是使用后需要 调用一下 clearDistinctDrugIngerInfo() 方法来清空 过滤的数据
     * @param drug1 可以唯一确定药品1
     * @param drug2 可以唯一确定药品2
     * @return 
     * @deprecated
     */
    @Deprecated
    private static List<String> Distinctdrug = new ArrayList<String>();
    @Deprecated
    public static boolean isDistinctDrugIngerInfo(String drug1 , String drug2)
    {
        try
        {   
            if(Distinctdrug.contains(new String(drug2 + drug1)))
            {
                return true;
            }
            Distinctdrug.add(drug1 + drug2);
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 清除去重记录
     * 
     */
    @Deprecated
    public static void clearDistinctDrugIngerInfo()
    {
        Distinctdrug.clear();
    }
    
    /* 将药品基本信息   全部缓存起来   key= 药品代码 ，  key = 药品代码 _ 药品规格*/
    private static Map<String, TCommonRecord>  drugMapCollenct = new HashMap<String , TCommonRecord>();
    
    /**
     * 加载所有药品基本信息 drug_map表
     */
    @SuppressWarnings ("unchecked")
    public static void loadDrugMap()
    {
        drugMapCollenct.clear();
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper  cmr   = new CommonMapper();
        try
        {
            String sql = "select * from drug_map ";
            List<TCommonRecord>  dms  = query.query(sql, cmr);
            for(TCommonRecord entity : dms)
            {
                if(!drugMapCollenct.containsKey(entity.get("drug_no_local"))) 
                    drugMapCollenct.put(entity.get("drug_no_local"), entity);
                if(!drugMapCollenct.containsKey(entity.get("drug_no_local") + "_" + entity.get("drug_spec"))) 
                    drugMapCollenct.put(entity.get("drug_no_local") + "_" + entity.get("drug_spec"), entity);
            }
//            if(drugMapCollenct.size() != dms.size())
//            {
//                for(TCommonRecord entity : dms)
//                {
//                    if(drugMapCollenct.containsKey(entity.get("drug_no_local") + entity.get("drug_spec"))) continue;
//                }
//            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
            cmr   = null;
        }
    }
    
    /**
     * 更新缓冲药品信息
     * @param drugMapId
     */
    public static void loadDrugMapBySingle(String drugMapId)
    {
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper cmr = new CommonMapper();
        try
        {
            String sql = "select * from  drug_map where drug_map_id = ?";
            TCommonRecord  tcom = (TCommonRecord)query.queryForObject(sql, new Object[]{drugMapId},cmr); 
            if(tcom != null ) 
            {
                drugMapCollenct.put(tcom.get("drug_no_local"), tcom);
                drugMapCollenct.put(tcom.get("drug_no_local") + "_" + tcom.get("drug_spec"), tcom);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            query = null;
            cmr   = null;
        }
    }
}
