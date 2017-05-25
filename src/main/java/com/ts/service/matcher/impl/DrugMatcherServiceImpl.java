package com.ts.service.matcher.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.Impl.BasePageBean;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.ts.service.matcher.IDataMatcherService;

@Service
@Transactional
public class DrugMatcherServiceImpl extends BasePageBean implements IDataMatcherService
{
	/**
	 * 获取HIS信息
	 * @param params
	 * @return
	 */
	@Override
	public PageView<TCommonRecord> queryHISForList(TCommonRecord params)
	{
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        if (!"".equals(params.get("q_drug_code")))
        {
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("drug_code", params.get("q_drug_code"), "Char", "", "", "");
            lsWheres.add(crWheres);
        }
        if (!"".equals(params.get("q_drug_name")))
        {
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("drug_name", "%" + params.get("q_drug_name") + "%", "Char", "like", "", "");
            lsWheres.add(crWheres);
        }
        List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
        TCommonRecord lsOrder = CaseHistoryHelperUtils.genOrderCR("drug_code", "asc");
        lsOrders.add(lsOrder);
        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
        JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
        return ichh.fetchTable2PV(12, params.getInt("page"), "*", "comm.drug_dict", lsWheres, null, lsOrders,query);


//        String sql = "select distinct pl.firm_id,pl.retail_price,dc.drug_code,dc.drug_name, dc.drug_spec,dc.units from drug_price_list pl,drug_dict dc " +
//                "where " +
//                "pl.drug_code=dc.drug_code(+) " +
//                "and pl.drug_spec=dc.drug_spec(+) " +
//                "and pl.units=dc.units(+) ";
//        if (!"".equals(params.get("q_drug_code"))) {
//            sql += " and drug_code like '%"+ params.get("q_drug_code") + "%' ";
//        }
//        if (!"".equals(params.get("q_drug_name"))) {
//            sql += " and drug_name like '%" + params.get("q_drug_name") +"%' ";
//        }
//        BasePageBean page = new BasePageBean();
//        return page.getScrollData(12, params.getInt("page"), "HIS", "", null, null, "("+sql + ")");
	}

	/**
	 * 获取HIS单一信息
	 * @param params
	 * @return
	 */
	@Override
	public TCommonRecord queryHISForObject(TCommonRecord params)
	{
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils
                .genWhereGbkCR("drug_code", params.get("drug_code").replace("'", "''"), "Char", "", "", "");
        TCommonRecord crWheres1 = CaseHistoryHelperUtils
                .genWhereGbkCR("drug_spec", params.get("drug_spec").replace("'", "''"), "Char", "", "", "");
//        TCommonRecord crWheres2 = CaseHistoryHelperUtils
//                .genWhereCR("units", params.get("units").replace("'", "''"), "Char", "", "", "");
		lsWheres.add(crWheres);
		lsWheres.add(crWheres1);
//		lsWheres.add(crWheres2);
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
		List<TCommonRecord> list = new ArrayList<TCommonRecord>();
		try
		{
			list = ichh.fetchDrugDict2CR("*", lsWheres, null, null, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		TCommonRecord entity = new TCommonRecord();
		if (list != null && list.size() > 0)
			entity = list.get(0);
		return entity;
	}
	
	/**
	 * 获取PDSS的信息
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> queryPDSSMainForList(TCommonRecord params)
	{
		this.setQueryCode("PDSS");
		String sql = "select * from drug where 1=1 ";
		if (!"".equals(params.get("q_drug_name")))
			sql += "and drug_name like '%" + params.get("q_drug_name") + "%' ";
		sql += "order by drug_id";
		return this.query.query(sql, new CommonMapper());
	}

	/**
	 * 获取PDSS的明细信息
	 * @param params
	 * @return
	 */
	@Override
	public PageView<TCommonRecord> queryPDSSDetailForList(TCommonRecord params)
	{
		StringBuffer wheres = new StringBuffer();
		if (!"".equals(params.get("q_drug_name")))
			wheres.append(" and drug_name like '%").append(params.get("q_drug_name")).append("%' ");
		LinkedHashMap<String, String> orders = new LinkedHashMap<String, String>();
		orders.put("drug_id", "asc");
		return this.getScrollData(12, params.getInt("page"), "PDSS", wheres.toString(), null, orders, "drug", null);
	}

	/**
	 * 获取PDSS配码单一信息
	 * @param params
	 * @return
	 */
	@Override
	public TCommonRecord queryPDSSMapForObject(TCommonRecord params)
	{
		this.setQueryCode("PDSS");
		String sql = "select * from drug_map where drug_map_id = '" + params.get("drug_map_id") + "'";
		return (TCommonRecord) this.query.queryForObject(sql, new CommonMapper());
	}

	/**
	 * 获取PDSS已配对信息
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, TCommonRecord> queryPDSSMapedForMap(TCommonRecord params)
	{
		this.setQueryCode("PDSS");
		String sql = "select * from drug_map";
		List<TCommonRecord> list = this.query.query(sql, new CommonMapper());
		Map<String,TCommonRecord> map = new HashMap<String, TCommonRecord>(5000);
		if(list != null)
		{
			for(TCommonRecord entity : list)
			{
				map.put(entity.get("drug_no_local")+entity.get("drug_spec") + entity.get("units"), entity);
			}
		}
		return map;
	}

	/**
	 * 保存配码结果
	 * @param params
	 * @return
	 */
	@Override
	public int save(TCommonRecord params)
	{
		/* 判断本地是否存在该配码：如果存在，修改原来的配码结果；否则新增配码结果 */
		TCommonRecord mapEntity = this.queryPDSSMapForObject(params);
		this.setQueryCode("PDSS");
		if (mapEntity != null)
		{
			String sql = "update drug_map set oper_user = '" + params.get("oper_user") + 
						 "', oper_time=sysdate, last_date_time=sysdate, drug_id='" + params.get("drug_id") + 
						 "', drug_name='" + params.get("drug_name") + 
						 "', drug_spec_pdss='" + params.get("drug_spec_pdss") + 
						 "', units_pdss='" + params.get("units_pdss") + 
						 "', drug_form_pdss='" + params.get("drug_form_pdss") + 
						 "', dose_per_unit_pdss='" + params.get("dose_per_unit_pdss") + 
						 "', dose_units_pdss='" + params.get("dose_units_pdss") + 
						 "', drug_indicator_pdss='" + params.get("drug_indicator_pdss") + 
						 "', toxi_property_pdss='" + params.get("toxi_property_pdss") + 
						 "', is_anti='" + params.get("is_anti") + 
						 "', is_basedrug='" + params.get("is_basedrug") + 
						 "', ddd_value='" + params.get("ddd_value") + 
						 "', ddd_unit='" + params.get("ddd_unit") +
						 "', is_exhilarant='" + params.get("is_exhilarant") +
						 "', is_injection='" + params.get("is_injection") + 
						 "', is_oral='" + params.get("is_oral") + 
						 "', is_impregnant='" + params.get("is_impregnant") + 
						 "', pharm_catalog='" + params.get("pharm_catalog") + 
						 "', drug_catalog='" + params.get("drug_catalog") + 
						 "', is_external='" + params.get("is_external") + 
						 "', is_chinesedrug='" + params.get("is_chinesedrug") + 
						 "', is_allergy='" + params.get("is_allergy") + 
						 "', ddd_value_x='" + params.get("ddd_value_x") + 
						 "', is_medcare_country='" + params.get("is_medcare_country") + 
						 "', is_medcare_local='" + params.get("is_medcare_local") + 
						 "', is_patentdrug='" + params.get("is_patentdrug") + 
						 "', is_tumor='" + params.get("is_tumor") + 
						 "', is_poison='" + params.get("is_poison") + 
						 "', is_psychotic='" + params.get("is_psychotic") + 
						 "', is_habitforming='" + params.get("is_habitforming") + 
						 "', is_radiation='" + params.get("is_radiation") + 
						 "', is_precious='" + params.get("is_precious") + 
						 "', is_danger='" + params.get("is_danger") + 
						 "', is_otc='" + params.get("is_otc") + 
						 "', is_medcare='" + params.get("is_medcare") + 
						 "', is_chinese_drug='" + params.get("is_patentdrug") + 
						 "', anti_level='" + params.get("anti_level") + 
						 "' where drug_map_id = '" + params.get("drug_map_id") + "'";
			this.query.update(sql);
			return params.getInt("id");
		}
		int maxId = this.queryMaxId();
		String sql = "insert into drug_map (drug_map_id, drug_no_local, drug_name_local, drug_spec, units, drug_form, " +
					 "dose_per_unit, dose_units, drug_indicator, toxi_property, input_code, drug_id, drug_name, " +
					 "drug_spec_pdss, units_pdss, drug_form_pdss, dose_per_unit_pdss, dose_units_pdss, " +
					 "drug_indicator_pdss, toxi_property_pdss, is_anti, is_basedrug, ddd_value, ddd_unit, " +
					 "is_exhilarant, is_injection, is_oral, is_impregnant, " +
					 "pharm_catalog, drug_catalog, is_external, is_chinesedrug, " +
					 "is_allergy, ddd_value_x, is_medcare_country, is_medcare_local, " +
					 "is_patentdrug, is_tumor, is_poison, is_psychotic, " +
					 "is_habitforming, is_radiation, is_precious, is_danger, " +
					 "is_otc, anti_level, is_medcare, oper_user, is_chinese_drug, oper_time, last_date_time) " +
					 "values ('" + maxId + 
					 "', '" + params.get("drug_no_local") + 
					 "', '" + params.get("drug_name_local") + 
					 "', '" + params.get("drug_spec") + 
					 "', '" + params.get("units") + 
					 "', '" + params.get("drug_form") + 
					 "', '" + params.get("dose_per_unit") + 
					 "', '" + params.get("dose_units") + 
					 "', '" + params.get("drug_indicator") + 
					 "', '" + params.get("toxi_property") + 
					 "', '" + params.get("input_code") + 
					 "', '" + params.get("drug_id") + 
					 "', '" + params.get("drug_name") + 
					 "', '" + params.get("drug_spec_pdss") + 
					 "', '" + params.get("units_pdss") + 
					 "', '" + params.get("drug_form_pdss") + 
					 "', '" + params.get("dose_per_unit_pdss") + 
					 "', '" + params.get("dose_units_pdss") + 
					 "', '" + params.get("drug_indicator_pdss") + 
					 "', '" + params.get("toxi_property_pdss") + 
					 "', '" + params.get("is_anti") + 
					 "', '" + params.get("is_basedrug") + 
					 "', '" + params.get("ddd_value") + 
					 "', '" + params.get("ddd_unit") +
					 "', '" + params.get("is_exhilarant") +
					 "', '" + params.get("is_injection") + 
					 "', '" + params.get("is_oral") + 
					 "', '" + params.get("is_impregnant") + 
					 "', '" + params.get("pharm_catalog") + 
					 "', '" + params.get("drug_catalog") + 
					 "', '" + params.get("is_external") + 
					 "', '" + params.get("is_chinesedrug") + 
					 "', '" + params.get("is_allergy") + 
					 "', '" + params.get("ddd_value_x") + 
					 "', '" + params.get("is_medcare_country") + 
					 "', '" + params.get("is_medcare_local") + 
					 "', '" + params.get("is_patentdrug") + 
					 "', '" + params.get("is_tumor") + 
					 "', '" + params.get("is_poison") + 
					 "', '" + params.get("is_psychotic") + 
					 "', '" + params.get("is_habitforming") + 
					 "', '" + params.get("is_radiation") + 
					 "', '" + params.get("is_precious") + 
					 "', '" + params.get("is_danger") + 
					 "', '" + params.get("is_otc") + 
					 "', '" + params.get("anti_level") + 
					 "', '" + params.get("is_medcare") + 
					 "', '" + params.get("oper_user") +  
					 "', '" + params.get("is_patentdrug") +  
					 "', sysdate, sysdate)";
		this.query.update(sql);
		return maxId;
	}
	
	/**
	 * 获取最大ID
	 * @return
	 */
	public int queryMaxId()
	{
		this.setQueryCode("PDSS");
		String sql = "select * from drug_map order by drug_map_id desc";
		TCommonRecord maxEntity = (TCommonRecord) this.query.queryForObject(sql, new CommonMapper());
		if (maxEntity == null)
			return 1;
		return maxEntity.getInt("drug_map_id") + 1;
	}

	/**
	 * 自动配码
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String matchAuto(TCommonRecord params)
	{
		JDBCQueryImpl pdssQuery = DBQueryFactory.getQuery("PDSS");
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
		String ps = params.get("params");
		int updCnt = 0;
		int drugCnt = 0;
		try
		{
			drugCnt = ichh.fetchDrugDict2CR("drug_code", null, null, null, null).size();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String strFields = "drug_code, drug_name, drug_spec, units, drug_form, dose_per_unit, dose_units, drug_indicator, toxi_property, input_code";
		List<TCommonRecord> dm_list = new ArrayList<TCommonRecord>();
		try
		{
			dm_list = ichh.fetchDrugDict2CR(strFields, null, null, null, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		for (TCommonRecord dm_name : dm_list)
		{
			String sqls = "select * from drug where replace(replace(drug_name, '（', '('), '）', ')') like '%" + DrugNameHandler(ps, dm_name.get("drug_name")) + "%' and rownum = 1";
			TCommonRecord tr = (TCommonRecord)pdssQuery.queryForObject(sqls, new CommonMapper());
			if (tr != null)
			{
				List<TCommonRecord> list = pdssQuery.query("select * from drug_map where drug_no_local='"+dm_name.get("drug_code")+"'", new CommonMapper());
				if(list != null && list.size()>0)
				{
					String pm_sql = "update drug_map set oper_user = '自动', oper_time=sysdate, last_date_time=sysdate, drug_id='" + tr.get("drug_id") +
									 "', drug_name='" + tr.get("drug_name") +
									 "', drug_spec_pdss='" + tr.get("drug_spec") +
									 "', units_pdss='" + tr.get("units") +
									 "', drug_form_pdss='" + tr.get("drug_form") +
									 "', dose_per_unit_pdss='" + tr.get("dose_per_unit") +
									 "', dose_units_pdss='" + tr.get("dose_units") +
									 "', drug_indicator_pdss='" + tr.get("drug_indicator") +
									 "', toxi_property_pdss='" + tr.get("toxi_property") +
									 "', is_anti='" + tr.get("is_anti") +
									 "', is_basedrug='" + tr.get("is_basedrug") +
									 "', ddd_value='" + tr.get("ddd_value") +
									 "', ddd_unit='" + tr.get("ddd_unit") +
									 "', is_exhilarant='" + tr.get("is_exhilarant") +
									 "', is_injection='" + tr.get("is_injection") +
									 "', is_oral='" + tr.get("is_oral") +
									 "', is_impregnant='" + tr.get("is_impregnant") +
									 "', pharm_catalog='" + tr.get("pharm_catalog") +
									 "', drug_catalog='" + tr.get("drug_catalog") +
									 "', is_external='" + tr.get("is_external") +
									 "', is_chinesedrug='" + tr.get("is_chinesedrug") +
									 "', is_allergy='" + tr.get("is_allergy") +
									 "', ddd_value_x='" + tr.get("ddd_value_x") +
									 "', is_medcare_country='" + tr.get("is_medcare_country") +
									 "', is_medcare_local='" + tr.get("is_medcare_local") +
									 "', is_patentdrug='" + tr.get("is_patentdrug") +
									 "', is_tumor='" + tr.get("is_tumor") +
									 "', is_poison='" + tr.get("is_poison") +
									 "', is_psychotic='" + tr.get("is_psychotic") +
									 "', is_habitforming='" + tr.get("is_habitforming") +
									 "', is_radiation='" + tr.get("is_radiation") +
									 "', is_precious='" + tr.get("is_precious") +
									 "', is_danger='" + tr.get("is_danger") +
									 "', is_medcare='" + tr.get("is_medcare") +
									 "', is_otc='" + tr.get("is_otc") +
									 "', is_chinese_drug='" + tr.get("is_patentdrug") +
									 "' where drug_no_local = '" + dm_name.get("drug_code") + "'";
					updCnt += pdssQuery.update(pm_sql);
				}
				else
				{
					String pm_sql = "insert into drug_map (drug_map_id, drug_no_local, drug_name_local, drug_spec, units, drug_form, " +
									 "dose_per_unit, dose_units, drug_indicator, toxi_property, input_code, drug_id, drug_name, " +
									 "drug_spec_pdss, units_pdss, drug_form_pdss, dose_per_unit_pdss, dose_units_pdss, " +
									 "drug_indicator_pdss, toxi_property_pdss, is_anti, is_basedrug, ddd_value, ddd_unit, " +
									 "is_exhilarant, is_injection, is_oral, is_impregnant, " +
									 "pharm_catalog, drug_catalog, is_external, is_chinesedrug, " +
									 "is_allergy, ddd_value_x, is_medcare_country, is_medcare_local, " +
									 "is_patentdrug, is_tumor, is_poison, is_psychotic, " +
									 "is_habitforming, is_radiation, is_precious, is_danger, " +
									 "is_otc, is_medcare, is_chinese_drug, oper_user, oper_time, last_date_time) " +
									 "values ('" + this.queryMaxId() +
									 "', '" + dm_name.get("drug_code") +
									 "', '" + dm_name.get("drug_name") +
									 "', '" + dm_name.get("drug_spec") +
									 "', '" + dm_name.get("units") +
									 "', '" + dm_name.get("drug_form") +
									 "', '" + dm_name.get("dose_per_unit") +
									 "', '" + dm_name.get("dose_units") +
									 "', '" + dm_name.get("drug_indicator") +
									 "', '" + dm_name.get("toxi_property") +
									 "', '" + dm_name.get("input_code") +
									 "', '" + tr.get("drug_id") +
									 "', '" + tr.get("drug_name") +
									 "', '" + tr.get("drug_spec") +
									 "', '" + tr.get("units") +
									 "', '" + tr.get("drug_form") +
									 "', '" + tr.get("dose_per_unit") +
									 "', '" + tr.get("dose_units") +
									 "', '" + tr.get("drug_indicator") +
									 "', '" + tr.get("toxi_property") +
									 "', '" + tr.get("is_anti") +
									 "', '" + tr.get("is_basedrug") +
									 "', '" + tr.get("ddd_value") +
									 "', '" + tr.get("ddd_unit") +
									 "', '" + tr.get("is_exhilarant") +
									 "', '" + tr.get("is_injection") +
									 "', '" + tr.get("is_oral") +
									 "', '" + tr.get("is_impregnant") +
									 "', '" + tr.get("pharm_catalog") +
									 "', '" + tr.get("drug_catalog") +
									 "', '" + tr.get("is_external") +
									 "', '" + tr.get("is_chinesedrug") +
									 "', '" + tr.get("is_allergy") +
									 "', '" + tr.get("ddd_value_x") +
									 "', '" + tr.get("is_medcare_country") +
									 "', '" + tr.get("is_medcare_local") +
									 "', '" + tr.get("is_patentdrug") +
									 "', '" + tr.get("is_tumor") +
									 "', '" + tr.get("is_poison") +
									 "', '" + tr.get("is_psychotic") +
									 "', '" + tr.get("is_habitforming") +
									 "', '" + tr.get("is_radiation") +
									 "', '" + tr.get("is_precious") +
									 "', '" + tr.get("is_danger") +
									 "', '" + tr.get("is_otc") +
									 "', '" + tr.get("is_medcare") +
									 "', '" + tr.get("is_patentdrug") +
									 "', '自动', sysdate, sysdate)";
					updCnt += pdssQuery.update(pm_sql);
				}
			}
		}
		return updCnt + "_" + drugCnt;
	}
	
	public static String DrugNameHandler(String filter, String drugName)
	{
		String[] filters = filter.split(",");
		for (String fil : filters)
		{
			drugName = drugName.replace(fil, "");
		}
		return drugName.replace(" ", "").replace("（", "(").replace("）", ")");
	}
}