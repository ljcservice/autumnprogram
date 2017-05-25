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
public class PerformFreqMatcherServiceImpl extends BasePageBean implements IDataMatcherService
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
		if (!"".equals(params.get("q_freq_desc")))
		{
			TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("freq_desc", "%" + params.get("q_freq_desc") + "%", "Char", "like", "", "");
			lsWheres.add(crWheres);
		}
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
        JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
        List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
        TCommonRecord order = CaseHistoryHelperUtils.genOrderCR("SERIAL_NO", "desc");
        lsOrders.add(order);
		return ichh.fetchTable2PV(12, params.getInt("page"), "*", "comm.perform_freq_dict", lsWheres, null, lsOrders,query);
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
		if(params.get("perform_freq_dict_name").indexOf("(") != -1 || params.get("perform_freq_dict_name").indexOf("（") != -1||
		        params.get("perform_freq_dict_name").indexOf(")") != -1 || params.get("perform_freq_dict_name").indexOf("）") != -1)
        {
		    crWheres = CaseHistoryHelperUtils.genWhereGbkCR("freq_desc", params.get("perform_freq_dict_name").replace("'", "''") , "Char", "", "", "");
        }
		else
		{
			crWheres = CaseHistoryHelperUtils.genWhereGbkCR("freq_desc", "%" + params.get("perform_freq_dict_name")
		    		.replace("'", "''")
		    		.replace("(","_")
		    		.replace("（", "_")
		    		.replace(")","_")
		    		.replace("）","_") + "%", "Char", "like", "", "");    
		}
		
		lsWheres.add(crWheres);
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
        JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
		List<TCommonRecord> list = new ArrayList<TCommonRecord>();
		try
		{
			list = ichh.fetchPerformFreqDict2CR("*", lsWheres, null, null, query);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list.get(0);
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
		String sql = "select * from perform_freq_dict where 1=1 ";
		if (!"".equals(params.get("q_perform_freq_dict_name")))
			sql += "and perform_freq_dict_name = '" + params.get("q_perform_freq_dict_name") + "' ";
		sql += "order by perform_freq_dict_id";
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
		if (!"".equals(params.get("q_perform_freq_dict_name")))
			wheres.append(" and perform_freq_dict_name like '%").append(params.get("q_perform_freq_dict_name")).append("%' ");
		LinkedHashMap<String, String> orders = new LinkedHashMap<String, String>();
		orders.put("perform_freq_dict_id", "asc");
		return this.getScrollData(12, params.getInt("page"), "PDSS", wheres.toString(), null, orders, "perform_freq_dict", null);
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
		String sql = "select * from perform_freq_dict_map where perform_freq_dict_map_id = '" + params.get("perform_freq_dict_map_id") + "'";
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
		String sql = "select * from perform_freq_dict_map";
		List<TCommonRecord> list = this.query.query(sql, new CommonMapper()); 
		Map<String,TCommonRecord> map = new HashMap<String, TCommonRecord>(5000);
		if(list != null)
		{
			for(TCommonRecord entity : list)
			{
				map.put(entity.get("perform_freq_dict_name_local"), entity);
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
			String sql = "update perform_freq_dict_map set oper_user = '" + params.get("oper_user") + 
						 "', oper_time=sysdate, last_date_time = sysdate, perform_freq_dict_id='" + params.get("perform_freq_dict_id") + 
						 "', perform_freq_dict_name='" + params.get("perform_freq_dict_name").replace("'", "''") + 
						 "', freq_counter_pdss='" + params.get("freq_counter_pdss").replace("'", "''") + 
						 "', freq_interval_pdss='" + params.get("freq_interval_pdss").replace("'", "''") + 
						 "', freq_interval_units_pdss='" + params.get("freq_interval_units_pdss").replace("'", "''") + 
						 "', frequency_pdss='" + params.get("frequency_pdss").replace("'", "''") + 
						 "' where perform_freq_dict_map_id = '" + params.get("perform_freq_dict_map_id") + "'";
			this.query.update(sql);
			return params.getInt("perform_freq_dict_map_id");
		}
		int maxId = this.queryMaxId();
		String sql = "insert into perform_freq_dict_map (perform_freq_dict_no_local, perform_freq_dict_name_local, perform_freq_dict_name, " +
					 "perform_freq_dict_id, oper_user, oper_time, serial_no, freq_desc, freq_counter, " +
					 "freq_interval, freq_interval_units, freq_counter_pdss, freq_interval_pdss, " +
					 "freq_interval_units_pdss, frequency_pdss, perform_freq_dict_map_id, last_date_time) " +
					 "values ('" + params.get("perform_freq_dict_no_local").replace("'", "''") + 
					 "', '" + params.get("perform_freq_dict_name_local").replace("'", "''") + 
					 "', '" + params.get("perform_freq_dict_name").replace("'", "''") + 
					 "', '" + params.get("perform_freq_dict_id") + "', '" + params.get("oper_user") + 
					 "', sysdate, '" + params.get("serial_no") + 
					 "', '" + params.get("freq_desc").replace("'", "''") + "', '" + params.get("freq_counter") +
					 "', '" + params.get("freq_interval") + "', '" + params.get("freq_interval_units") +
					 "', '" + params.get("freq_counter_pdss") + "', '" + params.get("freq_interval_pdss") +
					 "', '" + params.get("freq_interval_units_pdss") + "', '" + params.get("frequency_pdss") +
					 "', '" + maxId + "', sysdate)";
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
		String sql = "select * from perform_freq_dict_map order by perform_freq_dict_map_id desc";
		TCommonRecord maxEntity = (TCommonRecord) this.query.queryForObject(sql, new CommonMapper());
		if (maxEntity == null)
			return 1;
		return maxEntity.getInt("perform_freq_dict_map_id") + 1;
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