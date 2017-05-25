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
public class AdminstrationMatcherServiceImpl extends BasePageBean implements IDataMatcherService
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
		if (!"".equals(params.get("q_administration_name")))
		{
			TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("administration_name", "%" + params.get("q_administration_name") + "%", "Char", "like", "", "");
			lsWheres.add(crWheres);
		}
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
        JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
        List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
        TCommonRecord order = CaseHistoryHelperUtils.genOrderCR("administration_name", "desc");
        lsOrders.add(order);
		return ichh.fetchTable2PV(12, params.getInt("page"), "*", "comm.administration_dict", lsWheres, null, lsOrders,query);
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
		if(params.get("administration_name").indexOf("(") != -1 || params.get("administration_name").indexOf("（") != -1 ||
		        params.get("administration_name").indexOf(")") != -1 || params.get("administration_name").indexOf("）") != -1)
		{
		    crWheres = CaseHistoryHelperUtils.genWhereGbkCR("administration_name", "%" +  params.get("administration_name").replace("'", "''").replace("(","_").replace("（", "_").replace(")","_").replace("）","_")  + "%" , "Char", "like", "", "");
		}
		else
		{
		    crWheres = CaseHistoryHelperUtils.genWhereGbkCR("administration_name", params.get("administration_name").replace("'", "''"), "Char", "", "", "");
		}
		
		lsWheres.add(crWheres);
		List<TCommonRecord> lsGroups = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
		
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
		List<TCommonRecord> list = new ArrayList<TCommonRecord>();
		try
		{
			list = ichh.fetchAdministrationDict2CR("*", lsWheres, lsGroups, lsOrders, null);
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
		String sql = "select * from mc_route_std";
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
		if (!"".equals(params.get("q_std_route_id")))
			wheres.append(" and std_route_id = '").append(params.get("q_std_route_id")).append("' ");
		if (!"".equals(params.get("q_item_name")))
			wheres.append(" and item_name like '%").append(params.get("q_item_name")).append("%' ");
		LinkedHashMap<String, String> orders = new LinkedHashMap<String, String>();
		orders.put("std_route_id", "asc");
		return this.getScrollData(12, params.getInt("page"), "PDSS", wheres.toString(), null, orders, "mc_route_detail", null);
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
		String sql = "select * from administration_map where administration_map_id = '" + params.get("administration_map_id") + "'";
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
		String sql = "select * from administration_map";
		List<TCommonRecord> list = this.query.query(sql, new CommonMapper());
		Map<String,TCommonRecord> map = new HashMap<String, TCommonRecord>(5000);
		if(list != null)
		{
			for(TCommonRecord entity : list)
			{
				map.put(entity.get("administration_name_local"), entity);
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
			String sql = "update administration_map set oper_user = '" + params.get("oper_user") + 
						 "', oper_time=sysdate, last_date_time=sysdate, administration_id='" + params.get("administration_id") +
						 "', administration_name='" + params.get("administration_name") +
						 "' where administration_map_id = '" + params.get("administration_map_id") + "'";
			this.query.update(sql);
			return params.getInt("administration_map_id");
		}
		int administration_map_id = this.queryMaxId();
		String sql = "insert into administration_map (administration_name, administration_id, oper_user, " +
					 "oper_time, serial_no, administration_no_local, administration_name_local, input_code, " +
					 "is_injection, administration_map_id, last_date_time) " +
					 "values ('" + params.get("administration_name") + "', '" + params.get("administration_id") + 
					 "', '" + params.get("oper_user") + "', sysdate, '" + params.get("serial_no") + 
					 "', '" + params.get("administration_no_local") + "', '" + params.get("administration_name_local") +
					 "', '" + params.get("input_code") + "', '" + params.get("is_injection") +
					 "', '" + administration_map_id + "', sysdate)";
		this.query.update(sql);
		return administration_map_id;
	}
	
	/**
	 * 获取最大ID
	 * @return
	 */
	public int queryMaxId()
	{
		this.setQueryCode("PDSS");
		String sql = "select * from administration_map order by administration_map_id desc";
		TCommonRecord maxEntity = (TCommonRecord) this.query.queryForObject(sql, new CommonMapper());
		if (maxEntity == null)
			return 1;
		return maxEntity.getInt("administration_map_id") + 1;
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