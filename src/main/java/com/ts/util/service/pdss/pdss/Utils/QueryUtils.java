package com.ts.service.pdss.pdss.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
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
import com.ts.service.pdss.pdss.Cache.BeanCache;
import com.ts.service.pdss.pdss.Cache.DBSnapshot;
import com.ts.service.pdss.pdss.RowMapper.DrugAdministrationMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugAllergIngrMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugDiagRelMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugDosageMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugInteractionMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugIvEffectMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugPerfromMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugUseDetailMapper;
import com.ts.service.pdss.pdss.RowMapper.MedicareCatalogMapper;
import com.ts.service.pdss.pdss.RowMapper.MemoMapper;

/**
 * 常用表缓冲
 * @author 
 *
 */
public class QueryUtils
{
    // drug_no_local,drug_name_local,oper_user,oper_date,oper_type,DRUG_ID,DRUG_NAME,DRUG_CLASS_ID,DOSE_CLASS_ID,UNITS,DRUG_FORM,TOXI_PROPERTY,DOSE_PER_UNIT,DOSE_UNITS,INGR_CLASS_IDS,IV_CLASS_CODE,DRUG_INDICATOR,INPUT_CODE,SENSIT_CODE,usetype,direct_no
    protected static String drugSql = "select * from PDSS.view_DRUG "
                                            + "where drug_no_local in (?)";

    /**
     * 查询本地药品，已应用数据库快照
     * 
     * @param DrugIDs 本地药品码
     * @param wheres
     * @param oper
     *            ，操作符，如果不为空，则拼写 Drug_ClassID + oper + "'" + DrugClassID +
     *            "'"，为空则采用Drug_Class_Id in ()的形式
     * @param query
     * @return ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
     *         如果DrugClassIDs与Wheres条件产生冲突，例如： DrugClassIDs = "1,2,3", wheres =
     *         " Drug_Class_Id > 2 ";
     *         此时2个条件产生冲突，wheres不包含1和2，结果集将包含1和2，应用查询时需要注意！！！！
     *         ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！ queryDrug("a", "",
     *         "=", query); // Drug_Class_ID = 'a'; queryDrug("a,b,c", "", "",
     *         query); // Drug_Class_ID in ('a', 'b', 'c'); queryDrug("a,b,c",
     *         " and Drug_ID='333' ", "", query); // Drug_Class_Id in ('a', 'b',
     *         'c') and Drug_ID='333'; queryDrug("a", " and Drug_ID='333' ",
     *         "=", query); // Drug_Class_Id = 'a' and Drug_ID='333';
     */
    public static List<TDrug> queryDrug(String[] DrugIDs, String wheres,JDBCQueryImpl query)
    {
    	if (!"PDSS".equals(query.getDbName()))
    		new RuntimeException("数据源不是PDSS!");
    	final List<TDrug> list = new ArrayList<TDrug>();
    	try
    	{
    	        StringBuffer strWheres = new StringBuffer();
    	        for (int i = 0; i < DrugIDs.length; i++)
    	        {
    	            TDrug drug = BeanCache.getDrug(DrugIDs[i]);
    	            if (drug == null)
    	            {
    	            	// 看看快照中是否存在
    	            	if (DBSnapshot.DrugNoLocalInDB(DrugIDs[i]))
    	            		strWheres.append("'").append(DrugIDs[i]).append("',");
    	                drug = null;
    	            }
    	            else if(!list.contains(drug))
    	                list.add(new TDrug(drug));
    	        }
    	        /* 如果 缓存取出所有数据就不需要再次查询 */
    	        if (strWheres.length() > 0)
    	        {
    	            strWheres.deleteCharAt(strWheres.length() - 1);
    	            String sql = drugSql.replace("?", strWheres);
    	            if (wheres != null && wheres.length() > 0)
    	                sql += wheres;
    	            query.query(sql, new DrugMapper(list));
    	        }
    	        return list;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return list;
    }

    //，已应用数据库快照
    public static Map<String, TDrug> queryDrug(TPatOrderDrug[] poDrugs, String wheres, JDBCQueryImpl query)
    {
    	if (!"PDSS".equals(query.getDbName()))
    		new RuntimeException("数据源不是PDSS!");
    	final Map<String, TDrug> map = new HashMap<String, TDrug>();
    	try
    	{
    	        StringBuffer strWheres = new StringBuffer();
    	        for (int i = 0; i < poDrugs.length; i++)
    	        {
    	        	if (map.containsKey(poDrugs[i].getDrugID()))
    	        		continue;
    	            TDrug drug = BeanCache.getDrug(poDrugs[i].getDrugID());
    	            if (drug == null)
    	            {
    	            	// 如果数据库快照中有该数据，就拼到sql中
    	            	if (DBSnapshot.DrugNoLocalInDB(poDrugs[i].getDrugID()))
    	            		strWheres.append("'").append(poDrugs[i].getDrugID()).append("',");
    	                drug = null;
    	            }
    	            else 
    	            if(!map.containsKey(drug.getDRUG_NO_LOCAL()))
    	                map.put(drug.getDRUG_NO_LOCAL(), drug);
    	        }
    	        /* 如果 缓存取出所有数据就不需要再次查询 */
    	        if (strWheres.length() > 0)
    	        {
    	            strWheres.deleteCharAt(strWheres.length() - 1);
    	            String sql = drugSql.replace("?", strWheres);
    	            if (wheres != null && wheres.length() > 0)
    	                sql += wheres;
    	            query.query(sql, new DrugMapper(map));
    	        }
    	        return map;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return map;
    }
    
    protected static String diiSql = "select DRUG_INTERACTION_INFO_ID,INGR_CLASS_ID1,INGR_CLASS_ID2,INTER_INDI,WAR_INFO,MEC_INFO,MAN_INFO,DIS_INFO,REF_SOURCE from DRUG_INTERACTION_INFO "
                                           + "where (INGR_CLASS_ID1 in (X1) and INGR_CLASS_ID2 in (X2)) "
                                           + "   or (INGR_CLASS_ID1 in (X2) and INGR_CLASS_ID2 in (X1))";

    /**
     * 药品互作用信息，已应用数据库快照
     * 
     * @param Code1
     * @param Code2
     * @param wheres
     * @param query
     * @return
     */
    public static List<TDrugInteractionInfo> queryDrugInteractionInfo(String[] Code1, String[] Code2,String wheres, JDBCQueryImpl query)
    {
        final List<TDrugInteractionInfo> list = new ArrayList<TDrugInteractionInfo>();
        StringBuffer strWheres1 = new StringBuffer();
        StringBuffer strWheres2 = new StringBuffer();
        for (int i = 0; i < Code1.length; i++)
        {
            for (int j = 0; j < Code2.length; j++)
            {
            	if (Code1.equals(Code2))
            		continue;
                TDrugInteractionInfo dii = new TDrugInteractionInfo();
                dii.setINGR_CLASS_CODE1(Code1[i]);
                dii.setINGR_CLASS_CODE2(Code2[j]);
                // 数据库中小号在前面
                if (Integer.parseInt(Code1[i]) > Integer.parseInt(Code2[j]))
                {
	                dii.setINGR_CLASS_CODE1(Code2[j]);
	                dii.setINGR_CLASS_CODE2(Code1[i]);
                }
                TDrugInteractionInfo diix = BeanCache.getDrugInteractionInfo(dii);
                if (diix == null)
                {
                	// 如果数据库快照中有该数据，就拼到sql里去
                	if (DBSnapshot.DrugInteractionInfoInDB(dii))
                	{
                		if (BeanCache.getViewCache())
                			System.out.println("查找数据库" + dii.getINGR_CLASS_CODE1() + "-" + dii.getINGR_CLASS_CODE2());
	                    if (strWheres1.indexOf("'" + Code1[i] + "'") < 0)
	                        strWheres1.append("'").append(Code1[i]).append("',");
	                    if (strWheres2.indexOf("'" + Code2[j] + "'" ) < 0)
	                        strWheres2.append("'").append(Code2[j]).append("',");
                	}
                }
                else if(!list.contains(diix))
                    list.add(diix);
                    
            }
        }
        /* 如果 缓存取出所有数据就不需要再次查询 */
        if (strWheres1.length() > 0)
            strWheres1.deleteCharAt(strWheres1.length() - 1);
        if (strWheres2.length() > 0)
            strWheres2.deleteCharAt(strWheres2.length() - 1);
        if (strWheres1.length() > 0 || wheres != null)
        {
            String sql = diiSql.replaceAll("X1", strWheres1.toString())
                    .replaceAll("X2", strWheres2.toString());
            if (wheres != null && wheres.length() > 0)
                sql += wheres;
            query.query(sql, new DrugInteractionMapper(list));
        }
        return list;
    }

    protected static String ivSql = "select EFFECT_ID, IV_CLASS_CODE1, FIRM_ID1, IV_CLASS_CODE2, FIRM_ID2,RESULT_ID, REFER_INFO, REF_SOURCE from DRUG_IV_EFFECT "
                                          + "where (IV_CLASS_CODE1 = X1 and IV_CLASS_CODE2 = X2) "
                                          + "   or (IV_CLASS_CODE1 = X2 and IV_CLASS_CODE2 = X1)";

    /**
     * 配伍信息
     * 已应用数据库快照
     * @param Code1
     * @param Code2
     * @param wheres
     * @param query
     * @return
     */
    public static List<TDrugIvEffect> queryDrugIvEffect(String Code1, String Code2,String wheres, JDBCQueryImpl query)
    {
        final List<TDrugIvEffect> list = new ArrayList<TDrugIvEffect>();
        TDrugIvEffect die = new TDrugIvEffect();
        die.setIV_CLASS_CODE1(Code1);
        die.setIV_CLASS_CODE2(Code2);
        TDrugIvEffect diex = (TDrugIvEffect) BeanCache.getCommonCache(die);
        if (diex == null)
        {
            System.out.println("配伍缓存比较  " + Code1 + ":" + Code2 + DBSnapshot.DrugIvEffectInDB(die));
//        	if (DBSnapshot.DrugIvEffectInDB(die))
        	{
	            String sql = ivSql.replaceAll("X1", Code1).replaceAll("X2", Code2);
	            if (wheres != null && wheres.length() > 0)
	                sql += wheres;
	            query.query(sql, new DrugIvEffectMapper(list));
        	}
        }
        else if(!list.contains(diex))
            list.add(diex);
        return list;
    }

    protected static String drugDigSql = "select DRUG_DIAG_REL_ID,CONTRAIND_ID,DRUG_CLASS_ID,ADMINISTRATION_ID,INDEX_ID from DRUG_DIAG_REL "
                                               + "where DRUG_CLASS_ID in (X1) and ADMINISTRATION_ID in (X2)";
    /**
     * 药物禁忌症对应，已应用数据库快照
     * @param DrugClassIDs
     * @param wheres
     * @param query
     * @return
     */
    public static List<TDrugDiagRel> queryDrugDiagRel(String[] DrugClassIDs, String[] drugAdmini , String wheres,JDBCQueryImpl query)
    {
        if (!"PDSS".equals(query.getDbName()))
            new RuntimeException("数据源不是PDSS!");
        final List<TDrugDiagRel> list = new ArrayList<TDrugDiagRel>();
        StringBuffer strWheres1 = new StringBuffer();
        StringBuffer strWheres2 = new StringBuffer();
        for (int i = 0; i < DrugClassIDs.length; i++)
        {
            for (int j = 0; j < drugAdmini.length; j++)
            {
                TDrugDiagRel ddr = new TDrugDiagRel();
                ddr.setADMINISTRATION_ID(drugAdmini[j]);
                ddr.setDRUG_CLASS_ID(DrugClassIDs[i]);
                TDrugDiagRel ddrx = BeanCache.getDrugDiagRel(ddr);
                if (ddrx == null)
                {
                	if (DBSnapshot.DrugDiagRelInDB(ddr))
                	{
	                    if (strWheres1.indexOf("'" + DrugClassIDs[i] + "'") < 0)
	                        strWheres1.append("'").append(DrugClassIDs[i]).append("',");
	                    if (strWheres2.indexOf("'" + drugAdmini[j] + "'") < 0)
	                        strWheres2.append("'").append(drugAdmini[j]).append("',");
                	}
                }
                else if(!list.contains(ddrx))
                    list.add(ddrx);
            }
        }
        /* 如果 缓存取出所有数据就不需要再次查询 */
        if (strWheres1.length() > 0)
            strWheres1.deleteCharAt(strWheres1.length() - 1);
        if (strWheres2.length() > 0)
            strWheres2.deleteCharAt(strWheres2.length() - 1);
        if (strWheres1.length() > 0 || wheres != null)
        {
            String sql = drugDigSql.replaceAll("X1", strWheres1.toString())
                    .replaceAll("X2", strWheres2.toString());
            if (wheres != null && wheres.length() > 0)
                sql += wheres;
            query.query(sql, new DrugDiagRelMapper(list));
        }
        return list;
    }
    
    protected static String mcSql = "select DRUG_ID, DRUG_NAME, REMARK, APPLY_TYPE, FEE_TYPE from Medicare_catalog where " +
        "DRUG_ID in (X1) "; //and APPLY_TYPE != '1'";
    /**
     * 
     * @param DrugCodes
     * @param wheres
     * @param query
     * @return
     */
    public static List<TMedicareCatalog> queryMedicareCatalog(String[] DrugCodes, String wheres, JDBCQueryImpl query)
    {
        final List<TMedicareCatalog> list = new ArrayList<TMedicareCatalog>();
        StringBuffer strWheres = new StringBuffer();
        for (int i = 0; i < DrugCodes.length; i++)
        {
            TMedicareCatalog mc = new TMedicareCatalog();
            mc.setDRUG_ID(DrugCodes[i]);
            mc = (TMedicareCatalog)BeanCache.getCommonCache(mc);
            if (mc == null)
            {
                if (strWheres.indexOf("'" + DrugCodes[i] + "'" ) < 0)
                    strWheres.append("'").append(DrugCodes[i]).append("',");
            }
            else if(!list.contains(mc))
                list.add(mc);    
            
        }
        if(strWheres.length() > 0) 
            strWheres.deleteCharAt(strWheres.length() - 1);
        if(strWheres.length() > 0 || wheres!=null )
        {
            String sql = mcSql.replaceAll("X1", strWheres.toString());
            if (wheres != null && wheres.length() > 0)
                sql += wheres;
            query.query(sql, new MedicareCatalogMapper(list));
        }
        return list;
    }
    
    protected static String mmSql = "select ID, DRUG_ID, CHECK_ITEM_NAME, CHECK_ITEM_CODE, CHECK_TYPE from memo where " +
                        "DRUG_ID in (X1)";
    /**
     * 已应用快照
     * @param DrugCodes
     * @param wheres
     * @param query
     * @return
     */
    public static List<TMemo> queryMemo(String[] DrugCodes, String wheres, JDBCQueryImpl query)
    {
        final List<TMemo> list = new ArrayList<TMemo>();
        StringBuffer strWheres = new StringBuffer();
        for (int i = 0; i < DrugCodes.length; i++)
        {
            TMemo mm = new TMemo();
            mm.setDRUG_ID(DrugCodes[i]);
            TMemo mmx = (TMemo)BeanCache.getCommonCache(mm);
            if (mmx == null)
            {
            	if (DBSnapshot.MemoInDB(mm))
            	{
	                if (strWheres.indexOf("'" + DrugCodes[i] + "'") < 0)
	                    strWheres.append("'").append(DrugCodes[i]).append("',");
            	}
            }
            else if(!list.contains(mmx))
                list.add(mmx);
                
        }
        if(strWheres.length() > 0) 
            strWheres.deleteCharAt(strWheres.length() - 1);
        if(strWheres.length() > 0 || wheres!=null )
        {
            String sql = mmSql.replaceAll("X1", strWheres.toString());
            if (wheres != null && wheres.length() > 0)
                sql += wheres;
            query.query(sql, new MemoMapper(list));
        }
        return list;
    }
    
    protected static String allergenSql =  "select drug_allergen_id,input_code,drug_allergen_name,allerg_ingr_drug_id,drug_class_id from view_allergen where " +
                " drug_allergen_id in (X1) ";
    
    /**
     * 药物成分、药敏、药物分类与药物对照字典
     * @param allergenID    已应用快照
     * @param drugClassid
     * @param query
     * @return
     */
    public static List<TAllergIngrDrug> queryAllergen(String[] allergenID , String drugClassid ,JDBCQueryImpl query)
    {
        List<TAllergIngrDrug> allerg = new ArrayList<TAllergIngrDrug>();
        StringBuffer strWheres = new StringBuffer();
        for(int i = 0  ;i <allergenID.length ; i++)
        {
            List<TAllergIngrDrug>  allergs = BeanCache.getAid(allergenID[i]);
            if(allergs == null)
            {
            	if (DBSnapshot.AllergenIDInDB(allergenID[i]))
            	{
	                if (strWheres.indexOf("'" + allergenID[i] + "'") < 0)
	                {
	                    strWheres.append("'").append(allergenID[i]).append("',");
	                    BeanCache.setAid(allergenID[i], null);
	                }
            	}
            }
            else
            {
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
        if(strWheres.length() > 0 )
        {
            strWheres.deleteCharAt(strWheres.length() - 1);
            String sql = allergenSql.replace("X1", strWheres.toString());
            @SuppressWarnings("unchecked")
            List<TAllergIngrDrug> allergs = query.query(sql, new DrugAllergIngrMapper());
            for(int j = 0 ;j < allergs.size();j++)
            {
                BeanCache.setAid(allergs.get(j).getDRUG_ALLERGEN_ID(), allergs.get(j));
                if(drugClassid.equals(allergs.get(j).getDRUG_CLASS_ID()))
                {
                    allerg.add(allergs.get(j));
                }
            }
        }
        return allerg;
    }
//    
//    protected static String AllergenIDSql = "select drug_allergen_id,input_code,drug_allergen_name,allerg_ingr_drug_id,drug_class_id from view_allergen where " +
//            " drug_allergen_id in (X1) and drug_class_id in (X2)";
//    /**
//     * 药物成分、药敏、药物分类与药物对照字典  
//     * TODO 注意   drug_allergen_id ,  drug_class_id 可能间接形成联合主键 请确认
//     * @param AllergenID
//     * @param wheres
//     * @param query
//     * @return
//     */
//    public static List<TAllergIngrDrug> queryAllergen(String[] AllergenID,String[] drugClassID , String wheres, JDBCQueryImpl query)
//    {
//        final List<TAllergIngrDrug> list = new ArrayList<TAllergIngrDrug>();
//        StringBuffer strWheres1 = new StringBuffer();
//        StringBuffer strWheres2 = new StringBuffer();
//        for (int i = 0; i < AllergenID.length; i++)
//        {
//            for (int j = 0; j < drugClassID.length; j++)
//            {
//                TAllergIngrDrug aid = new TAllergIngrDrug();
//                aid.setDRUG_ALLERGEN_ID(AllergenID[i]);
//                aid.setDRUG_CLASS_ID(drugClassID[j]);
//                aid = BeanCache.getAllergIngrDrug(aid);
//                if (aid == null)
//                {
//                    if (strWheres1.indexOf(AllergenID[i] + ",") < 0)
//                        strWheres1.append(AllergenID[i]).append(",");
//                    if (strWheres2.indexOf(drugClassID[j] + ",") < 0)
//                        strWheres2.append(drugClassID[j]).append(",");
//                }
//                else if(!list.contains(aid))
//                    list.add(aid);    
//                    
//            }
//        }
//        /* 如果 缓存取出所有数据就不需要再次查询 */
//        if (strWheres1.length() > 0)
//            strWheres1.deleteCharAt(strWheres1.length() - 1);
//        if (strWheres2.length() > 0)
//            strWheres2.deleteCharAt(strWheres2.length() - 1);
//        if (strWheres1.length() > 0 || wheres != null)
//        {
//            String sql = AllergenIDSql.replaceAll("X1", strWheres1.toString())
//                    .replaceAll("X2", strWheres2.toString());
//            if (wheres != null && wheres.length() > 0)
//                sql += wheres;
//            query.query(sql, new DrugAllergIngrMapper(list));
//        }
//        return list;
//    }
    
    protected  static String administrationidSql      = "select ADMINISTRATION_ID,ADMINISTRATION_NAME,INPUT_CODE,ADMINISTRATION_CLASS,ROUTE_METHOD,ROUTE_CLASS," +
    			"ADMINISTRATION_NO_LOCAL,ADMINISTRATION_NAME_LOCAL,ADMINISTRATION_ID,OPER_USER,OPER_TIME from view_administration where ADMINISTRATION_ID in (X1)";
    protected  static String administrationidSqlLocal = "select ADMINISTRATION_ID,ADMINISTRATION_NAME,INPUT_CODE,ADMINISTRATION_CLASS,ROUTE_METHOD,ROUTE_CLASS," +
				"ADMINISTRATION_NO_LOCAL,ADMINISTRATION_NAME_LOCAL,ADMINISTRATION_ID,OPER_USER,OPER_TIME from view_administration where ADMINISTRATION_NO_LOCAL in (X1)";
    
    /**
     * 用药途径 
     * @param administrationID
     * @param wheres
     * @param query
     * @return
     */
    public static List<TAdministration> queryAdministration(String[] administrationID, String wheres, JDBCQueryImpl query)
    {
        final List<TAdministration> list = new ArrayList<TAdministration>();
        StringBuffer strWheres = new StringBuffer();
        for (int i = 0; i < administrationID.length; i++)
        {
            TAdministration aid = new TAdministration();
            aid.setADMINISTRATION_NO_LOCAL(administrationID[i]);
            aid = (TAdministration)BeanCache.getAdministration(aid);
            if (aid == null)
            {
                if (strWheres.indexOf("'" + administrationID[i] + "'") < 0)
                    strWheres.append("'").append(administrationID[i]).append("',");
            }
            else if(!list.contains(aid))
                list.add(aid);
                
        }
        if(strWheres.length() > 0) 
            strWheres.deleteCharAt(strWheres.length() - 1);
        if(strWheres.length() > 0 || wheres!=null )
        {
        	String sql ;
        	/* 用来区分 his 系统 传入 的是 本地码 或是 标准码 */
        	if (Config.getParamValue("admin_conv_flag").equals("0"))
        	{
        		sql = administrationidSql.replaceAll("X1", strWheres.toString());
        	}
        	else
        	{
        		sql = administrationidSqlLocal.replace("X1", strWheres.toString());
        	}
            if (wheres != null && wheres.length() > 0)
                sql += wheres;
            query.query(sql, new DrugAdministrationMapper(list));
        }
        return list;
    }
    
    protected  static String performSql = "select PERFORM_FREQ_DICT_ID,PERFORM_FREQ_DICT_NAME,FREQ_COUNTER,FREQ_INTERVAL,FREQ_INTERVAL_UNITS,PERFORM_FREQ_DICT_NO_LOCAL,PERFORM_FREQ_DICT_NAME_LOCAL,PERFORM_FREQ_DICT_ID,OPER_USER,OPER_TIME" +
    		    " from view_perform where perform_freq_dict_no_local in (X1)";

    /**
    * 医嘱执行频率
    * @param performID
    * @param wheres
    * @param query
    * @return
    */
    public static List<TDrugPerformFreqDict> queryDrugPerfom(String[] performID, String wheres, JDBCQueryImpl query)
    {
        final List<TDrugPerformFreqDict> list = new ArrayList<TDrugPerformFreqDict>();
        StringBuffer strWheres = new StringBuffer();
        for (int i = 0; i < performID.length; i++)
        {
            TDrugPerformFreqDict aid = new TDrugPerformFreqDict();
            aid.setPERFORM_FREQ_DICT_NO_LOCAL(performID[i]);
            aid = (TDrugPerformFreqDict)BeanCache.getDrugPerform(aid);
            if (aid == null)
            {
                if (strWheres.indexOf("'" + performID[i] + "'") < 0)
                    strWheres.append("'").append(performID[i]).append("',");
            }
            else if(!list.contains(aid))
                list.add(aid);
        }
        if(strWheres.length() > 0) 
            strWheres.deleteCharAt(strWheres.length() - 1);
        if(strWheres.length() > 0 || wheres!=null )
        {
            String sql = performSql.replaceAll("X1", strWheres.toString());
            if (wheres != null && wheres.length() > 0)
                sql += wheres;
            query.query(sql, new DrugPerfromMapper(list));
        }
        return list;
    }
    
    protected static String drugDud = "select drug_use_detail_id, drug_class_id, last_date_time, pregnant_indi, pregnant_info, pregnant_info_ref, lact_indi, lact_info, lact_info_ref, kid_indi, kid_low, kid_high, kid_info, kid_info_ref, old_indi, old_info, old_info_ref, hepatical_indi, renal_indi, forbid_ruid, forbid_cause, inadvis_rtid, inadvis_cause, advert_rtid, advert_cause" +
    		                " from DRUG_USE_DETAIL where DRUG_CLASS_ID in (X1)";
    /**
     * 药品信息，已进入快照
     * @param drugs
     * @param query
     * @return
     */
    public static List<TDrugUseDetail> queryDrugDud(List<TDrug> drugs ,JDBCQueryImpl query)
    {
        final List<TDrugUseDetail> list = new ArrayList<TDrugUseDetail>();
        StringBuffer strWheres = new StringBuffer();
        for (int i = 0; i < drugs.size(); i++)
        {
            TDrugUseDetail dud = (TDrugUseDetail)BeanCache.getDud(drugs.get(i).getDRUG_CLASS_ID());
            if (dud == null)
            {
            	if (DBSnapshot.DrugUseDetailInDB(drugs.get(i).getDRUG_CLASS_ID()))
            	{
	                if (strWheres.indexOf("'" + drugs.get(i).getDRUG_CLASS_ID() + "'") < 0)
	                    strWheres.append("'").append(drugs.get(i).getDRUG_CLASS_ID()).append("',");
            	}
            }
            else if(!list.contains(dud))
                list.add(dud);
        }
        if(strWheres.length() > 0) 
        {
            strWheres.deleteCharAt(strWheres.length() - 1);
            String sql = drugDud.replaceAll("X1", strWheres.toString());
            query.query(sql, new DrugUseDetailMapper(list));
        }
        return list;
    }
    
    // 返回Map的形式以便于快速查找
    public static Map<String, TDrugUseDetail> queryDrugDud(Map<String, TDrug> drugs ,JDBCQueryImpl query)
    {
        final Map<String, TDrugUseDetail> map = new HashMap<String, TDrugUseDetail>();
        StringBuffer strWheres = new StringBuffer();
        Set entrySet = drugs.entrySet();
        for(Iterator<Entry<String, TDrug>> it = entrySet.iterator(); it.hasNext();)
        {  
            Entry<String, TDrug> entry = (Entry<String, TDrug>)it.next();
            TDrug drug = (TDrug)entry.getValue();
            TDrugUseDetail dud = new TDrugUseDetail();
            dud = (TDrugUseDetail)BeanCache.getDud(drug.getDRUG_CLASS_ID());
            if (dud == null)
            {
            	if (DBSnapshot.DrugUseDetailInDB(drug.getDRUG_CLASS_ID()))
            	{
	                if (strWheres.indexOf("'" + drug.getDRUG_CLASS_ID() + "'") < 0)
	                    strWheres.append("'").append(drug.getDRUG_CLASS_ID()).append("',");
            	}
            }
            else if(!map.containsKey(dud))
                map.put(dud.getDRUG_CLASS_ID(), dud);
        }
        if(strWheres.length() > 0) 
        {
            strWheres.deleteCharAt(strWheres.length() - 1);
            String sql = drugDud.replaceAll("X1", strWheres.toString());
            query.query(sql, new DrugUseDetailMapper(map));
        }
        return map;
    }
    
    protected static String drugDOSAGE = "select drug_dosage_id, dose_class_id, administration_id, drug_form, dose_units, age_indi, age_low, age_high, weight_indi, weight_low, weight_high, cal_indi, dose_each_low, dose_each_high, dose_each_unit, dose_day_low, dose_day_high, dose_day_unit, dose_max_high, dose_max_unit, dose_freq_low, dose_freq_high, dur_low, dur_high, special_desc, reference_info from DRUG_DOSAGE where DOSE_CLASS_ID = ? and ADMINISTRATION_ID = ? ";
    /**
     * 药品剂量使用字典，已进入快照
     * @param drugClass
     * @param administation
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<TDrugDosage> queryDrugDosage(String drugClass ,String administation ,JDBCQueryImpl query)
    {
        String key = drugClass + "+" + administation;
        List<TDrugDosage> list = BeanCache.getDdg(key);
        if(list == null)
        {
        	if (DBSnapshot.DosageInDB(key))
        	{
	            list = query.query(drugDOSAGE,new Object[]{drugClass,administation},new DrugDosageMapper());
	            BeanCache.setDdg(key, list);
        	}
        	else
        	{
        		list = new ArrayList<TDrugDosage>();
        	}
        }
        return list;
    }
    
//    protected static String diagDict = " select id, diagnosis_no_local, diagnosis_name_local, diagnosis_id, oper_user, oper_time, oper_type from diagnosis_map where diagnosis_id = ? ";
    protected static String diagDict = " select id, diagnosis_code,diagnosis_name from diagnosis where diagnosis_code = ? ";
    public static String queryDiagDict(String Diagid )
    {
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        try
        {
            return (String)query.queryForObject(diagDict, new Object[]{Diagid}, new RowMapper()
            {
                @Override 
                public Object mapRow(ResultSet rs, int rowNum)
                        throws SQLException
                {
                    return rs.getString("diagnosis_name");  
                }
            });      
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
        finally
        {
            query = null;
        }
    }
    
    protected static String diagDictMap = "select ICD9,id, diagnosis_no_local, diagnosis_name_local, diagnosis_id, oper_user, oper_time, oper_type from diagnosis_map ";
    //protected static String diagDictMap = " select id, diagnosis_code,diagnosis_name from diagnosis ";
    @SuppressWarnings("unchecked")
    
     /**
     * 诊断信息转换
     */
    public static List<TCommonRecord> queryDiagTComDictMap(String[] localDiagCode ,JDBCQueryImpl query)
    {
        if(localDiagCode == null || localDiagCode.length == 0 )
            return new ArrayList<TCommonRecord>();
        StringBuffer sbf = new StringBuffer();
        for(String str : localDiagCode)
        {
            sbf.append("'").append(str).append("',");
        }
        if(sbf.length()>0)
            sbf.deleteCharAt(sbf.length() - 1);
        List<TCommonRecord> list = query.query(diagDictMap + " where diagnosis_no_local in (" + sbf.toString() + ")", new CommonMapper());
        return list;
    }
    
    /**
     * 诊断信息转换
     */
	@SuppressWarnings ("unchecked")
    public static String[] queryDiagDictMap(String[] localDiagCode ,JDBCQueryImpl query)
    {
    	if(localDiagCode == null || localDiagCode.length == 0 )
    		return new String[0];
    	StringBuffer sbf = new StringBuffer();
    	for(String str : localDiagCode)
    	{
    		sbf.append("'").append(str).append("',");
    	}
    	if(sbf.length()>0)
    		sbf.deleteCharAt(sbf.length() - 1);
    	List<String> list = query.query(diagDictMap + " where diagnosis_no_local in (" + sbf.toString() + ")", new RowMapper(){
    		@Override
	    	public Object mapRow(ResultSet rs, int arg1) throws SQLException
	    	{
	    		return rs.getString("ICD9");
	    	}}
    	);
    	return (String[])list.toArray(new String[0]);
    }
    
    
    @SuppressWarnings("unchecked")
    /**
     * 诊断信息转换
     */
    public static String[] queryDiagNameDictMap(String[] localDiagCode ,JDBCQueryImpl query)
    {
        if(localDiagCode == null || localDiagCode.length == 0 )
            return new String[0];
        StringBuffer sbf = new StringBuffer();
        for(String str : localDiagCode)
        {
            sbf.append("'").append(str).append("',");
        }
        if(sbf.length()>0)
            sbf.deleteCharAt(sbf.length() - 1);
        List<String> list = query.query(diagDictMap + " where diagnosis_name_local in (" + sbf.toString() + ")", new RowMapper(){
            @Override
            public Object mapRow(ResultSet rs, int arg1) throws SQLException
            {
                return rs.getString("ICD9");
            }}
        );
        return (String[])list.toArray(new String[0]);
    }
    
    private  static String diseageVsdiag = "select t.disease_id,t.diagnosis_code,t.disease_name from view_Disease_vs_diagnosis t where t.diagnosis_code = ?";
    
    /**
     *  查找 诊断 与 疾病 
     * @param Diag_Code
     * @param query
     * @return
     */
    @SuppressWarnings ("unchecked")
    public static List<TCommonRecord> queryDiseageVsdiag(String Diag_Code , JDBCQueryImpl query)
    {
        CommonMapper cmr = new CommonMapper();
        List<TCommonRecord> result = null;
        try
        {
            if(Diag_Code == null || "".equals(Diag_Code) )
                return new ArrayList<TCommonRecord>();
            result = BeanCache.getDiseageVsDiag(Diag_Code);
            if(result == null)
            {
                result = query.query(diseageVsdiag, new Object[]{Diag_Code} ,cmr);   
                BeanCache.setDiseageVsDiag(Diag_Code, result);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cmr = null;
        }
        return result;
    }
    
}
