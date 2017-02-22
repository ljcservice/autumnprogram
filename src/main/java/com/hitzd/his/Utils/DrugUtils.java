package com.hitzd.his.Utils;

import java.util.ArrayList;

import java.util.List;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;

/**
 * 药品基本信息
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
     *   
     *  PZ  品种，药品码前多少位可以标识一种药
     *  ZS  注射剂，用药途径分类码为多少时是注射剂
     *  ZY  中成药药品码以什么开头为中药
     *  MZ  麻醉镇痛药，药品码开头为何值时为麻醉药
     *  JS  精神药，同上
     *  KJ  抗菌药，同上
     */
	
	/**
	 * 是否是注射剂
	 * 
	 */
    public static boolean isZSDrug(String DrugCode, String DrugSpec)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "' and IS_INJECTION = '1'";
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
     * 是否是外用药
     * @param DrugCode
     * @param DrugSpec
     * @return
     */
    public static boolean isExternalDrug(String DrugCode, String DrugSpec)
    {
    	JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "' and IS_EXTERNAL = '1'";
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
     * 获得外用药编号列表
     * @return
     */
    public static String getExternalDrugNos()
    {
    	JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
    	try
    	{
    		String sql = "select drug_no_local from drug_map where IS_EXTERNAL = '1'";
    		@SuppressWarnings("unchecked")
			List<TCommonRecord> list = query.query(sql, new CommonMapper());
            StringBuffer sbfr = new StringBuffer();
    		for (TCommonRecord cr : list)
    		{
    			sbfr.append("'").append(cr.get("drug_no_local")).append("',");
    		}
            if(sbfr.length() > 0)
            {
                sbfr.deleteCharAt(sbfr.length() - 1 );
            }
            return sbfr.toString();
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
     * 获得抗过敏药物编号列表
     * @return
     */
    public static String getAntiAllergyDrugNos()
    {
    	JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
    	try
    	{
    		String sql = "select drug_no_local from drug_map where IS_Allergy = '1'";
    		@SuppressWarnings("unchecked")
			List<TCommonRecord> list = query.query(sql, new CommonMapper());
            StringBuffer sbfr = new StringBuffer();
    		for (TCommonRecord cr : list)
    		{
    			sbfr.append("'").append(cr.get("drug_no_local")).append("',");
    		}
            if(sbfr.length() > 0)
            {
                sbfr.deleteCharAt(sbfr.length() - 1 );
            }
            return sbfr.toString();
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
     * 毒药
     */
    @Deprecated
    public static String getDYList()
    {
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where IS_POISON = '1' ";
			@SuppressWarnings("unchecked")
			List<TCommonRecord> list = query.query(sql, new CommonMapper());
			sql = null;
            StringBuffer sbfr = new StringBuffer();
    		for (TCommonRecord cr : list)
    		{
    			sbfr.append("'").append(cr.get("drug_no_local")).append("',");
    		}
            if(sbfr.length() > 0)
            {
                sbfr.deleteCharAt(sbfr.length() - 1 );
            }
            return sbfr.toString();
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
     * 是否是中药注射剂
     * @param drugCode
     * @return
     */
    public static boolean isCenterDrugZS(String DrugCode, String DrugSpec)
    {
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "' and IS_INJECTION = '1' and IS_CHINESEDRUG = '1'";
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
     * 是否是中药
     * @param DrugCode
     * @return
     */
	public static boolean isZYDrug(String DrugCode)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and IS_PATENTDRUG = '1'";
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
	 * 是否是麻醉药
	 * @param DrugCode
	 * @return
	 */
	@Deprecated
	public static boolean isMZDrug(String DrugCode)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and IS_HABITFORMING = '1' ";
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
	 * 是否是精神用药
	 * @param DrugCode
	 * @return 返回精一、精二
	 */
	@Deprecated
	public static String isJSDrug(String DrugCode, String DrugSpec)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
		try
		{
			String sql = "select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "' and TOXI_PROPERTY like '%精%'";
			TCommonRecord cr = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			sql = null;
			if (cr == null)
				return "";
			else
				return cr.get("TOXI_PROPERTY");
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
		/*
		String value = Config.getParamValue("JS");
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
	 * 是否是医保用药
	 * @param DrugCode
	 * @param DrugSpec
	 * @return 0是非医保，1是甲类医保，2是乙类医保
	 */
	public static String isMedcareDrug(String DrugCode, String DrugSpec)
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

    /* 国家基本药物*/
    private static List<TCommonRecord> Base = new ArrayList<TCommonRecord>();
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllBaseDrug()
	{
	    if(Base.size() > 0 ) return Base;
	    JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
	    CommonMapper  cmr   = new CommonMapper();
	    try
	    {
	    	String sql = "select * from Drug_Map where  is_basedrug='1'";
	        Base = query.query(sql, cmr);
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
	    return Base;
	}
	/**
	 * 是否是国家基本药品目录中的药品
	 * @param DrugCode
	 * @param DrugSpec
	 * @return
	 */
	public static boolean isCountryBase(String DrugCode, String DrugSpec)
	{
		if(Base.size() <= 0 ) getAllBaseDrug();
		try
		{
			for(TCommonRecord t : Base)
			{
			    if((DrugCode + DrugSpec).equals(t.get("drug_no_local") + t.get("drug_spec")))
			    {
			        return true;
			    }
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
		
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
	 *  是否标准注射剂药品 
	 * @param drugCode
	 * @return 已取消
	 */
	@Deprecated
	public static boolean isZSNormDrug(String drugCode)
	{
	    TCommonRecord drug = DictCache.getNewInstance().getDrugDictInfo(drugCode);
        if("针剂".equals(drug.get("drug_form")))
        {
            return true;    
        }
        return false;
	}
	
	/* 抗菌药物集合  */
	private static List<TCommonRecord> KJDrug = new ArrayList<TCommonRecord>();
	@SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllKJDrug()
	{
	    if(KJDrug.size() > 0 ) return KJDrug;
	    JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
	    CommonMapper  cmr   = new CommonMapper();
	    try
	    {
	        String sql = "select * from Drug_Map where is_anti = '1'";
	        KJDrug = query.query(sql, cmr);
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
	    return KJDrug;
	}
	
	/**
	 * 返回抗菌药列表 
	 * @return
	 */
	public static String getKJDrugList()
	{
	    if(KJDrug.size() <= 0 ) getAllKJDrug();
	    StringBuffer sb = new StringBuffer();
	    for(TCommonRecord t : KJDrug)
	    {
	        sb.append("'").append(t.get("drug_no_local")).append("',");
	    }
	    if(sb.length() > 1) sb.deleteCharAt(sb.length() - 1 );
	    return sb.toString();
	}
	
	/**
	 * 是否是抗菌药
	 * @param DrugCode
	 * @return
	 */
	public static boolean isKJDrug(String DrugCode)
	{
	    if(KJDrug.size() <= 0 ) 
	    {
	        getAllKJDrug();
	    }
		try
		{
			for(TCommonRecord t : KJDrug)
			{
			    if(DrugCode.equals(t.get("drug_no_local")))
			    {
			        return true;
			    }
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
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
	 * 返回药品登记
	 * @param DrugCode
	 * @param DrugSpec
	 * @return 1是非限制用药，2是限制用药，3是特殊用药
	 */
	public static String getDrugLevel(String DrugCode, String DrugSpec)
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
    	else if (isZYDrug(DrugCode)) return "2";
    	else if (isMZDrug(DrugCode)) return "3";
    	else if (isJSDrug(DrugCode, DrugSpec).length() > 0) return "4";
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
    public static void ReSetToxiProperty()
    {
        DM.clear();
        DY.clear();
        MY.clear();
        YJ.clear();
        EJ.clear();
        FS.clear();
        GZ.clear();
        KJDrug.clear();
        Base.clear();
    }

    /* 毒麻药*/
    private static List<TCommonRecord> DM = new ArrayList<TCommonRecord>();
    /**
     * 返回所有毒麻药  
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllDM()   
    {
        if(DM.size() > 0 )
            return DM;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper cmr    = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where Is_Poison = '1' and IS_HABITFORMING = '1' ";
            DM = query.query(sql, cmr);
            return DM;
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
        return DM ;
    }
    
    /**
     * 
     * 返回麻药 列表药物 'code','code','code' 带有cache中 
     * @return
     */
    public static String getDMList()
    {
        if(DM.size() <= 0 ) getAllDM();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(DM.size() < 0 ) return "";
            for(TCommonRecord t : DM)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }
    
    /**
     * 返回是否为毒麻药 
     * @param Code
     * @return
     */
    public static boolean isDMDrug(String drugCode )
    {
        if(DM.size() <= 0 ) getAllDM();
        for(TCommonRecord t : DM)
        {
            if(drugCode.equals(t.getObj("DRUG_NO_LOCAL")))
                return true;
        }
        return false;
    }
    
    /* 毒药 */
    private static List<TCommonRecord> DY = new ArrayList<TCommonRecord>();
    /**
     * 返回麻药 从 drug_map 表出数据
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllDY()
    {
        
        if(DY.size() > 0 )
            return DY;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper cmr = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where IS_POISON = '1' " ;
            DY = query.query(sql, cmr);
            return DY ;
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            query  = null;
            cmr    = null;
        }
        return DY;
    }
    
    /**
     * 
     * 返回麻药 列表药物 'code','code','code' 带有cache中 
     * @return
     */
    public static String getDYList1()
    {
        if(DY.size() <= 0 ) getAllDY();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(DY.size() < 0 ) return "";
            for(TCommonRecord t : DY)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }
    
    /**
     * 返回是否为毒药 
     * @param Code
     * @return
     */
    public static boolean isDDrug(String drugCode)
    {
        if(DY.size() <= 0 ) getAllDY();
        for(TCommonRecord t : DY)
        {
            if(drugCode.equals(t.getObj("DRUG_NO_LOCAL")))
                return true;
        }
        return false;
    }
    
    /* 麻药 */
    private static List<TCommonRecord> MY = new ArrayList<TCommonRecord>();
    
    /**
     * 返回麻药 从 drug_map 表出数据
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllMY()
    {
        if(MY.size() > 0 )
            return MY;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper cmr    = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where IS_HABITFORMING = '1' ";
            MY = query.query(sql, cmr);
            return MY;
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
            cmr   = null;
        }
        return MY;
    }
    
    /**
     * 返回麻药 列表药物 'code','code','code'
     * @return
     */
    public static String getMYList()
    {
        if(MY.size() <= 0 ) getAllMY();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(MY.size() < 0 ) return "";
            for(TCommonRecord t : MY)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }
    
    /**
     * 返回是否为麻药 
     * @param Code
     * @return
     */
    public static boolean isMDrug(String drugCode)
    {
        if(MY.size() <= 0 ) getAllMY();
        for(TCommonRecord t : MY)
        {
            if(drugCode.equals(t.getObj("DRUG_NO_LOCAL")))
                return true;
        }
        return false;
    }
    
    /* 一类精 */
    private static List<TCommonRecord> YJ = new ArrayList<TCommonRecord>();
    
    /**
     * 返回一类精神药物 从 drug_map 表出数据
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllYJ()
    {
        if(YJ.size() > 0 )
            return YJ;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper  cmr   = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where IS_PSYCHOTIC = '1' ";
            YJ = query.query(sql, cmr);
            return YJ;
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
        return YJ;
    }
    
    /**
     * 返回一类精神列表药物 'code','code','code'
     * @return
     */
    public static String getYJList()
    {
        if(YJ.size() <= 0 ) getAllYJ();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(YJ.size() < 0 ) return "";
            for(TCommonRecord t : YJ)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }
    
    /**
     * 返回是否为一类精神药物 
     * @param Code
     * @return
     */
    public static boolean isYJDrug(String drugCode)
    {
        if(YJ.size() <= 0 ) getAllYJ();
        for(TCommonRecord t : YJ)
        {
            if(drugCode.equals(t.getObj("DRUG_NO_LOCAL")))
                return true;
        }
        return false;
    }
    
    /* 二类精 */
    private static List<TCommonRecord> EJ = new ArrayList<TCommonRecord>();
    
    /**
     * 返回 二类精神药物 
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllEJ()
    {
        if(EJ.size() > 0 )
            return EJ;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper  cmr   = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where IS_PSYCHOTIC = '2' ";
            EJ = query.query(sql, cmr);
            return EJ;
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
        return EJ;
    }
    
    /**
     * 返回二类精神列表药物 'code','code','code'
     * @return
     */
    public static String getEJList()
    {
        if(EJ.size() <= 0 ) getAllEJ();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(EJ.size() < 0 ) return "";
            for(TCommonRecord t : EJ)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }
    
    /**
     * 返回是否为二类精神药物 
     * @param Code
     * @return
     */
    public static boolean isEJDrug(String drugCode)
    {
        if(EJ.size() <= 0 ) getAllEJ();
        for(TCommonRecord t : EJ)
        {
            if(drugCode.equals(t.getObj("DRUG_NO_LOCAL")))
                return true;
        }
        return false;
    }
    
    /* 放射 */
    private static List<TCommonRecord> FS = new ArrayList<TCommonRecord>();
    
    /**
     * 返回 放射药品 drug_map 数据
     * @return
     */
    @SuppressWarnings ("unchecked")
    /**
     *  返回所有放射药物 drug_map 数据
     */
    public static List<TCommonRecord> getAllFS()
    {
        if(FS.size() > 0 )
            return FS;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper  cmr   = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where IS_RADIATION = '1' ";
            FS = query.query(sql, cmr);
            return FS;
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
        return FS;
    }
    
    /**
     * 返回放射列表药物 'code','code','code'
     * @return
     */
    public static String getFSList()
    {
        if(FS.size() <= 0 ) getAllFS();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(FS.size() < 0 ) return "";
            for(TCommonRecord t : FS)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }
    
    /**
     * 返回是否为放射药物 
     * @param Code
     * @return
     */
    public static boolean isFSDrug(String drugCode)
    {
        if(FS.size() <= 0 ) getAllFS();
        for(TCommonRecord t : FS)
        {
            if(drugCode.equals(t.getObj("DRUG_NO_LOCAL")))
                return true;
        }
        return false;
    }
    
    /* 贵重  */
    private static List<TCommonRecord> GZ = new ArrayList<TCommonRecord>();
    /**
     * 返回 贵重药品 drug_map 数据
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllGZ()
    {
        if(GZ.size() > 0 )
            return GZ;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper  cmr   = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where IS_PRECIOUS = '1' ";
            GZ = query.query(sql, cmr);
            return GZ;
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
        return GZ;
    }

    /**
     * 返回贵重药物列表   'code','code','code'
     * @return
     */
    public static String getGZList()
    {
        if(GZ.size() <= 0 ) getAllGZ();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(GZ.size() < 0 ) return "";
            for(TCommonRecord t : GZ)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }
    
    /**
     * 返回是否为贵重药物 
     * @param Code
     * @return
     */
    public static boolean isGZDrug(String drugCode)
    {
        if(GZ.size() <= 0 ) getAllGZ();
        for(TCommonRecord t : GZ)
        {
            if(drugCode.equals(t.getObj("DRUG_NO_LOCAL")))
                return true;
        }
        return false;
    }
    
    /* 限制  */
    private static List<TCommonRecord> XZ = new ArrayList<TCommonRecord>();
    
    /**
     * 返回 限制药品 drug_map 数据
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllXZ()
    {
        if(XZ.size() > 0 )
            return XZ;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper  cmr   = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where is_anti='1' and  anti_level='2'";
            XZ = query.query(sql, cmr);
            return XZ;
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
        return XZ;
    }

    /**
     * 返回限制药物列表   'code','code','code'
     * @return
     */
    public static String getXZList()
    {
        if(XZ.size() <= 0 ) getAllXZ();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(XZ.size() < 0 ) return "";
            for(TCommonRecord t : XZ)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }
    
    
    /* 特殊 */
    private static List<TCommonRecord> TS = new ArrayList<TCommonRecord>();
    /**
     * 返回 特殊药品 drug_map 数据
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> getAllTS()
    {
        if(TS.size() > 0 )
            return TS;
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        CommonMapper  cmr   = new CommonMapper();
        try
        {
            String sql = "select * from drug_map where  is_anti='1' and  anti_level='3'";
            TS = query.query(sql, cmr);
            return TS;
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
        return TS;
    }

    /**
     * 返回特殊药物列表   'code','code','code'
     * @return
     */
    public static String getTSList()
    {
        if(TS.size() <= 0 ) getAllTS();
        StringBuffer sql = new StringBuffer();
        try
        {
            if(TS.size() < 0 ) return "";
            for(TCommonRecord t : TS)
            {
                sql.append("'").append(t.get("DRUG_NO_LOCAL")).append("',");
            }
            if(sql.length() > 0 )
                sql.deleteCharAt(sql.length() - 1 );
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return sql.toString();
    }

    /**
     * 去除审核中重复的信息 
     * 每次是使用后需要 调用一下 clearDistinctDrugIngerInfo() 方法来清空 过滤的数据
     * @param drug1 可以唯一确定药品1
     * @param drug2 可以唯一确定药品2
     * @return 
     * 
     */
    private static List<String> Distinctdrug = new ArrayList<String>();
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
    public static void clearDistinctDrugIngerInfo()
    {
        Distinctdrug.clear();
    }
}
