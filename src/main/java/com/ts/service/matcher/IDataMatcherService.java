package com.ts.service.matcher;


import java.util.List;
import java.util.Map;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.WebPage.PageView;

public interface IDataMatcherService
{
	/**
	 * 获取HIS信息
	 * @param params
	 * @return
	 */
	public PageView<TCommonRecord> queryHISForList(TCommonRecord params);
	
	/**
	 * 获取HIS单一信息
	 * @param params
	 * @return
	 */
	public TCommonRecord queryHISForObject(TCommonRecord params);
	
	/**
	 * 获取PDSS的信息
	 * @param params
	 * @return
	 */
	public List<TCommonRecord> queryPDSSMainForList(TCommonRecord params);
	
	/**
	 * 获取PDSS的明细信息
	 * @param params
	 * @return
	 */
	public PageView<TCommonRecord> queryPDSSDetailForList(TCommonRecord params);
	
	/**
	 * 获取PDSS配码单一信息
	 * @param params
	 * @return
	 */
	public TCommonRecord queryPDSSMapForObject(TCommonRecord params);
	
	/**
	 * 获取PDSS已配对信息
	 * @param params
	 * @return
	 */
	public Map<String,TCommonRecord> queryPDSSMapedForMap(TCommonRecord params);
	
	/**
	 * 保存配码结果
	 * @param params
	 * @return
	 */
	public int save(TCommonRecord params);
	
	/**
	 * 自动配码
	 * @param params
	 */
	public String matchAuto(TCommonRecord params);
}