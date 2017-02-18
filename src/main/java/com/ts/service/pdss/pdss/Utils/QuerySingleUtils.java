package com.ts.service.pdss.pdss.Utils;


import java.util.ArrayList;
import java.util.List;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.ts.entity.pdss.mas.Beans.TMedicareCatalog;
import com.ts.entity.pdss.mas.Beans.TMemo;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;
import com.ts.entity.pdss.pdss.Beans.TDrugMap;
import com.ts.entity.pdss.pdss.Beans.TDrugPerformFreqDict;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;
import com.ts.service.pdss.pdss.Cache.BeanCache;
import com.ts.service.pdss.pdss.Cache.DBSnapshot;
import com.ts.service.pdss.pdss.RowMapper.DrugAdministrationMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugDiagRelMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugInteractionMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugIvEffectMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugMapMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugPerfromMapper;
import com.ts.service.pdss.pdss.RowMapper.DrugUseDetailMapper;
import com.ts.service.pdss.pdss.RowMapper.MedicareCatalogMapper;
import com.ts.service.pdss.pdss.RowMapper.MemoMapper;

/**
 * 单结果缓存查询 
 * @author Administrator
 *
 */
public class QuerySingleUtils
{
    // drug_no_local,drug_name_local,oper_user,oper_date,oper_type,DRUG_ID,DRUG_NAME,DRUG_CLASS_ID,DOSE_CLASS_ID,UNITS,DRUG_FORM,TOXI_PROPERTY,DOSE_PER_UNIT,DOSE_UNITS,INGR_CLASS_IDS,IV_CLASS_CODE,DRUG_INDICATOR,INPUT_CODE,SENSIT_CODE,USETYPE,direct_no
    protected static String drugSql = "select * from view_DRUG "
                                            + " where drug_no_local = ? ";

    /**
     * 查询本地药品
     */
    public static TDrug queryDrug(String DrugID,JDBCQueryImpl query)
    {
		if (!"PDSS".equals(query.getDbName()))
			new RuntimeException("数据源不是PDSS!");
		TDrug drug = BeanCache.getDrug(DrugID);
		if (drug == null) 
		{
			drug = (TDrug) query.queryForObject(drugSql, new Object[] { DrugID }, new DrugMapper());
			if (drug != null) 
			{
				BeanCache.putDrug(drug);
			}
		}
		return new TDrug(drug);
    }
    
    protected static String drugMapSql = "select * from drug_map t where t.DRUG_NO_LOCAL = ? ";
    
    /**
     * 药品map 使用 抗菌药物
     * @param Code
     * @param spec
     * @param query
     * @return
     */
    public static TDrugMap queryDrugMap(String Code, JDBCQueryImpl query)
    {
        if(!"".equals(query.getDbName()))
            new RuntimeException("数据源不是PDSS");
        TDrugMap drugMap = BeanCache.getDrugMap(Code );
        if(drugMap == null)
        {
            drugMap  = (TDrugMap) query.queryForObject(drugMapSql, new String[]{Code}, new DrugMapMapper());
        }
        return drugMap;
    }

    protected static String diiSql = "select DRUG_INTERACTION_INFO_ID,INGR_CLASS_ID1,INGR_CLASS_ID2,INTER_INDI,WAR_INFO,MEC_INFO,MAN_INFO,DIS_INFO,REF_SOURCE from DRUG_INTERACTION_INFO "
                                           + "where (INGR_CLASS_ID1 = ? and INGR_CLASS_ID2 = ? ) "
                                           + "   or (INGR_CLASS_ID1 = ? and INGR_CLASS_ID2 = ? )";

    /**
     * 药品互作用信息
     * 
     * @param Code1
     * @param Code2
     * @param wheres
     * @param query
     * @return
     */
    public static TDrugInteractionInfo queryDrugInteractionInfo(String Code1, String Code2,String wheres, JDBCQueryImpl query)
    {
        if (!"PDSS".equals(query.getDbName()))
            new RuntimeException("数据源不是PDSS!");
        TDrugInteractionInfo drugInteraction = new TDrugInteractionInfo();
        drugInteraction.setINGR_CLASS_CODE1(Code1);
        drugInteraction.setINGR_CLASS_CODE2(Code2);
        drugInteraction = BeanCache.getDrugInteractionInfo(drugInteraction);
        if(drugInteraction == null)
        {
            drugInteraction = new TDrugInteractionInfo();
            drugInteraction.setINGR_CLASS_CODE1(Code2);
            drugInteraction.setINGR_CLASS_CODE2(Code1);
            drugInteraction = BeanCache.getDrugInteractionInfo(drugInteraction);        
        }
        if(drugInteraction == null)
        {
            drugInteraction = (TDrugInteractionInfo) query.queryForObject(diiSql, new Object[]{Code1,Code2,Code2,Code1}, new DrugInteractionMapper());
            if(drugInteraction != null)
                BeanCache.setDrugInteractionInfo(drugInteraction);
        }
        return drugInteraction;
    }

    protected static String ivSql = "select EFFECT_ID, IV_CLASS_CODE1, FIRM_ID1, IV_CLASS_CODE2, FIRM_ID2, IV_CLASS_CODE3, RESULT_ID, REFER_INFO, REF_SOURCE from DRUG_IV_EFFECT "
                                          + "where (IV_CLASS_CODE1 = ? and IV_CLASS_CODE2 = ?) "
                                          + "   or (IV_CLASS_CODE1 = ? and IV_CLASS_CODE2 = ?)";

    /**
     * 配伍信息
     * @param Code1
     * @param Code2
     * @param wheres
     * @param query
     * @return
     */
    public static TDrugIvEffect queryDrugIvEffect(String Code1, String Code2, JDBCQueryImpl query)
    {
        TDrugIvEffect die = new TDrugIvEffect();
        die.setIV_CLASS_CODE1(Code1);
        die.setIV_CLASS_CODE2(Code2);
        die = (TDrugIvEffect) BeanCache.getCommonCache(die);
        if (die == null)
        {
            die = (TDrugIvEffect) query.queryForObject(ivSql, new Object[]{Code1,Code2,Code2,Code1} , new DrugIvEffectMapper());
        }
        return die;
    }

    protected static String drugDigSql = "select DRUG_DIAG_REL_ID,CONTRAIND_ID,DRUG_CLASS_ID,ADMINISTRATION_ID,INDEX_ID from DRUG_DIAG_REL "
                                               + "where DRUG_CLASS_ID = ? and ADMINISTRATION_ID = ? ";

    /**
     * 禁忌症审查子模块
     * @param DrugClassIDs
     * @param wheres
     * @param query
     * @return
     */
    public static TDrugDiagRel queryDrugDiagRel(String DrugClassIDs,String adt , String wheres,
            JDBCQueryImpl query)
    {
        if (!"PDSS".equals(query.getDbName()))
            new RuntimeException("数据源不是PDSS!");
        TDrugDiagRel drugdiagrel = new TDrugDiagRel();
        drugdiagrel.setDRUG_CLASS_ID(DrugClassIDs);
        drugdiagrel.setADMINISTRATION_ID(adt);
        drugdiagrel = (TDrugDiagRel) BeanCache.getDrugDiagRel(drugdiagrel);
        if (drugdiagrel == null)
        {
            drugdiagrel = (TDrugDiagRel) query.queryForObject(drugDigSql,new Object[]{DrugClassIDs,adt} ,new DrugDiagRelMapper());
            if(drugdiagrel != null)
                BeanCache.setDrugDiagRel(drugdiagrel);
        }
        return drugdiagrel;
    }

    protected static String mcSql = "select DRUG_ID, DRUG_NAME, REMARK, APPLY_TYPE, FEE_TYPE from Medicare_catalog where "
                                          + "DRUG_ID = ? ";//and APPLY_TYPE = '1'";

    /**
     * 医保用药 
     * @param DrugCodes
     * @param wheres
     * @param query
     * @return
     */
    public static TMedicareCatalog queryMedicareCatalog(String DrugCode, String wheres,JDBCQueryImpl query)
    {
        if (!"PDSS".equals(query.getDbName()))
            new RuntimeException("数据源不是PDSS!");
        TMedicareCatalog mc = new TMedicareCatalog();
        mc.setDRUG_ID(DrugCode);
        mc = (TMedicareCatalog) BeanCache.getCommonCache(mc); 
        if (mc == null)
        {
            if(DBSnapshot.MedicareLocalInDB(DrugCode))
            {
                mc = (TMedicareCatalog) query.queryForObject(mcSql,new String[]{DrugCode}, new MedicareCatalogMapper());
                if(mc != null)
                    BeanCache.setCommonCache(mc);    
            }
        }
        return mc;
    }

    protected static String mmSql = "select ID, DRUG_ID, CHECK_ITEM_NAME, CHECK_ITEM_CODE, CHECK_TYPE from memo where "
                                          + "DRUG_ID = ? ";

    /**
     * 医保用药 适应列表 缓存
     * @param DrugCodes
     * @param wheres
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<TMemo> queryMemo(String DrugCode,JDBCQueryImpl query)
    {
        
        List<TMemo> mm = new ArrayList<TMemo>();
        mm = (List<TMemo>) BeanCache.getCommonCache("Memo_" + DrugCode);
        if(mm == null)
        {
            mm = (List<TMemo>)query.query(mmSql,new Object[]{DrugCode} ,new MemoMapper(mm));
        }
        return mm;
    }

    protected static String AllergenIDSql = "select drug_allergen_id,input_code,drug_allergen_name,allerg_ingr_drug_id,drug_class_id from view_allergen where " +
                "drug_allergen_id in = ? and drug_class_id = ? ";

//    /**
//     *  药物成分、药敏、药物分类与药物对照字典 
//     * 
//     * @param AllergenID
//     * @param wheres
//     * @param query
//     * @return
//     */
//    public static TAllergIngrDrug queryAllergen(String AllergenID,String drugClassID ,JDBCQueryImpl query)
//    {
//        TAllergIngrDrug aid = new TAllergIngrDrug();
//        aid.setDRUG_ALLERGEN_ID(AllergenID);
//        aid.setDRUG_CLASS_ID(drugClassID);
//        aid = (TAllergIngrDrug) BeanCache.getAllergIngrDrug(aid);
//        if(aid == null)
//        {
//            aid = (TAllergIngrDrug) query.queryForObject(AllergenIDSql,new Object[]{AllergenID},new DrugAllergIngrMapper());    
//            if(aid != null)
//                BeanCache.setAllergIngrDrug(aid);
//        }
//        return aid;
//    }

    protected static String administrationidSql = "select ADMINISTRATION_ID,ADMINISTRATION_NAME,INPUT_CODE,ADMINISTRATION_CLASS,ROUTE_METHOD,ROUTE_CLASS,"
                + "ADMINISTRATION_NO_LOCAL,ADMINISTRATION_NAME_LOCAL,ADMINISTRATION_ID,OPER_USER,OPER_TIME from view_administration " +
                    "where administration_no_local = ? ";

    /**
     * 用药途径
     * 
     * @param administrationID
     * @param wheres
     * @param query
     * @return
     */
    public static TAdministration queryAdministration(String administrationID, JDBCQueryImpl query)
    {
        TAdministration aid = new TAdministration();
        aid.setADMINISTRATION_NO_LOCAL(administrationID);
        aid = (TAdministration) BeanCache.getAdministration(aid);
        if(aid == null)
        {
            aid = (TAdministration) query.queryForObject(administrationidSql,new Object[]{administrationID},new DrugAdministrationMapper());
        }
        return aid;
    }

    protected static String performSql = "select PERFORM_FREQ_DICT_ID,PERFORM_FREQ_DICT_NAME,FREQ_COUNTER,FREQ_INTERVAL,FREQ_INTERVAL_UNITS,PERFORM_FREQ_DICT_NO_LOCAL,PERFORM_FREQ_DICT_NAME_LOCAL,PERFORM_FREQ_DICT_ID,OPER_USER,OPER_TIME"
                                               + " from view_perform where perform_freq_dict_no_local = ? ";

    /**
     * 医嘱执行频率
     * 
     * @param performID
     * @param wheres
     * @param query
     * @return
     */
    public static TDrugPerformFreqDict queryDrugPerfom(String performID,JDBCQueryImpl query)
    {
        TDrugPerformFreqDict aid = new TDrugPerformFreqDict();
        aid.setPERFORM_FREQ_DICT_NO_LOCAL(performID);
        aid = (TDrugPerformFreqDict) BeanCache.getDrugPerform(aid);
        if(aid == null)
        {
            aid = (TDrugPerformFreqDict) query.query(performSql, new DrugPerfromMapper());
            if(aid != null)
                BeanCache.setPerformFreqDict(aid);
        }
        return aid;
    }
    
    protected static String drugDud = "select drug_use_detail_id, drug_class_id, last_date_time, pregnant_indi, pregnant_info, pregnant_info_ref, lact_indi, lact_info, lact_info_ref, kid_indi, kid_low, kid_high, kid_info, kid_info_ref, old_indi, old_info, old_info_ref, hepatical_indi, renal_indi, forbid_ruid, forbid_cause, inadvis_rtid, inadvis_cause, advert_rtid, advert_cause" +
                    " from DRUG_USE_DETAIL where DRUG_CLASS_ID = ?";

    /**
     * 药品信息
     * @param drugs
     * @param query
     * @return
     */
    public static TDrugUseDetail queryDrugDud(TDrug drug, JDBCQueryImpl query)
    {
            TDrugUseDetail dud = new TDrugUseDetail();
            dud = (TDrugUseDetail) BeanCache.getDud(drug.getDRUG_CLASS_ID());
            if (dud == null)
                dud = (TDrugUseDetail) query.queryForObject(drugDud,new Object[]{drug.getDRUG_CLASS_ID()}, new DrugUseDetailMapper());
        return dud;
    }
}
