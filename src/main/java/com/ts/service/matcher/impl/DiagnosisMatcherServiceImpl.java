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
public class DiagnosisMatcherServiceImpl extends BasePageBean implements IDataMatcherService
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
		if (!"".equals(params.get("q_diagnosis_code")))
		{
			TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("diagnosis_code", params.get("q_diagnosis_code"), "Char", "", "", "");
			lsWheres.add(crWheres);
		}
		if (!"".equals(params.get("q_diagnosis_name")))
		{
			TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("diagnosis_name", "%" + params.get("q_diagnosis_name") + "%", "Char", "like", "", "");
			lsWheres.add(crWheres);
		}
		List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
		TCommonRecord lsOrder = CaseHistoryHelperUtils.genOrderCR("diagnosis_code", "asc");
		lsOrders.add(lsOrder);
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
        JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
		return ichh.fetchTable2PV(12, params.getInt("page"), "*", "comm.diagnosis_dict", lsWheres, null, lsOrders,query);
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
		TCommonRecord crWheres = new TCommonRecord();
		
		if(params.get("diagnosis_name").indexOf("(") != -1 || params.get("diagnosis_name").indexOf("（") != -1||
		        params.get("diagnosis_name").indexOf(")") != -1 || params.get("diagnosis_name").indexOf("）") != -1)
        {
		    crWheres = CaseHistoryHelperUtils.genWhereGbkCR("diagnosis_name", params.get("diagnosis_name").replace("'", "''").replace("(","_").replace("（", "_").replace(")","_").replace("）","_"), "Char", "like", "", "");
        }
		else
		{
		     crWheres = CaseHistoryHelperUtils.genWhereGbkCR("diagnosis_name", params.get("diagnosis_name").replace("'", "''"), "Char", "", "", "");    
		}
		
		lsWheres.add(crWheres);
		List<TCommonRecord> list = new ArrayList<TCommonRecord>();
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
		try
		{
			list = ichh.fetchDiagnosisDict2CR("*", lsWheres, null, null, null);
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
		String sql = "select * from diagnosis_dict where 1=1 ";
		if (!"".equals(params.get("q_diagnosis_code")))
			sql += "and diagnosis_code = '" + params.get("q_diagnosis_code") + "' ";
		if (!"".equals(params.get("q_diagnosis_name")))
			sql += "and diagnosis_name like '%" + params.get("q_diagnosis_name") + "%' ";
		sql += "order by diagnosis_code";
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
		if (!"".equals(params.get("q_diagnosis_code")))
			wheres.append(" and diagnosis_code = '").append(params.get("q_diagnosis_code")).append("' ");
		if (!"".equals(params.get("q_diagnosis_name")))
			wheres.append(" and diagnosis_name like '%").append(params.get("q_diagnosis_name")).append("%' ");
		LinkedHashMap<String, String> orders = new LinkedHashMap<String, String>();
		orders.put("diagnosis_code", "asc");
		return this.getScrollData(12, params.getInt("page"), "PDSS", wheres.toString(), null, orders, "diagnosis_dict", null);
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
		String sql = "select * from diagnosis_map where diagnosis_map_id = '" + params.get("diagnosis_map_id") + "'";
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
		String sql = "select * from diagnosis_map";
		List<TCommonRecord> list = this.query.query(sql, new CommonMapper());
		Map<String,TCommonRecord> map = new HashMap<String, TCommonRecord>(5000);
		if(list != null)
		{
			for(TCommonRecord entity : list)
			{
				map.put(entity.get("diagnosis_name_local"), entity);
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
			String sql = "update diagnosis_map set oper_user = '" + params.get("oper_user") + 
						 "', oper_time=sysdate, last_date_time=sysdate, diagnosis_id='" + params.get("diagnosis_id") + 
						 "', diagnosis_code='" + params.get("diagnosis_code") + 
						 "', diagnosis_code2='" + params.get("diagnosis_code2") + 
						 "', diagnosis_name='" + params.get("diagnosis_name") + 
						 "', acute_indi='" + params.get("acute_indi") + 
						 "', side_indi='" + params.get("side_indi") + 
						 "', diag_indi='" + params.get("diag_indi") + 
						 "', diagnosis_class='" + params.get("diagnosis_class") + 
						 "', diagnosis_class_code='" + params.get("diagnosis_class_code") + 
						 "', renal_indi='" + params.get("renal_indi") + 
						 "', hepatic_indi='" + params.get("hepatic_indi") + 
						 "', cardio_idi='" + params.get("cardio_idi") + 
						 "', pulm_indi='" + params.get("pulm_indi") + 
						 "', neur_indi='" + params.get("neur_indi") + 
						 "', endo_indi='" + params.get("endo_indi") + 
						 "', icd9='" + params.get("icd9") + 
						 "' where diagnosis_map_id = '" + params.get("diagnosis_map_id") + "'";
			this.query.update(sql);
			return params.getInt("id");
		}
		int maxId = this.queryMaxId();
		String sql = "insert into diagnosis_map (diagnosis_map_id, diagnosis_no_local, diagnosis_name_local, std_indicator, " +
					 "approved_indicator, input_code, infect_indicator, health_level, outp_drug_day, " +
					 "diagnosis_id, diagnosis_code, diagnosis_code2, diagnosis_name, " +
					 "acute_indi, side_indi, diag_indi, diagnosis_class, " +
					 "diagnosis_class_code, renal_indi, hepatic_indi, cardio_idi, " +
					 "pulm_indi, neur_indi, endo_indi, icd9, oper_user, oper_time, last_date_time) " +
					 "values ('" + maxId + "', '" + params.get("diagnosis_no_local") + "', '" + params.get("diagnosis_name_local") +
					 "', '" + params.get("std_indicator") + "', '" + params.get("approved_indicator") +  
					 "', '" + params.get("input_code") +  
					 "', '" + params.get("infect_indicator") + "', '" + params.get("health_level") +  
					 "', '" + params.get("outp_drug_day") + "', '" + params.get("diagnosis_id") +  
					 "', '" + params.get("diagnosis_code") + "', '" + params.get("diagnosis_code2") +  
					 "', '" + params.get("diagnosis_name") + "', '" + params.get("acute_indi") +  
					 "', '" + params.get("side_indi") + "', '" + params.get("diag_indi") +  
					 "', '" + params.get("diagnosis_class") + "', '" + params.get("diagnosis_class_code") +  
					 "', '" + params.get("renal_indi") + "', '" + params.get("hepatic_indi") +  
					 "', '" + params.get("cardio_idi") + "', '" + params.get("pulm_indi") +  
					 "', '" + params.get("neur_indi") + "', '" + params.get("endo_indi") +  
					 "', '" + params.get("icd9") + "', '" + params.get("oper_user") +  
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
		String sql = "select * from diagnosis_map order by diagnosis_map_id desc";
		TCommonRecord maxEntity = (TCommonRecord) this.query.queryForObject(sql, new CommonMapper());
		if (maxEntity == null)
			return 1;
		return maxEntity.getInt("diagnosis_map_id") + 1;
	}

	/**
	 * 自动配码
	 * @param params
	 */
	@Override
	public String matchAuto(TCommonRecord params)
	{
		return null;
	}
}