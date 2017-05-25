package com.ts.service.matcher;


import java.util.List;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.WebPage.PageView;

/**
 * 普通表操作
 * @author Crystal
 */
public interface ICommonOperService
{
	/**
	 * 插入
	 * @param params 参数集
	 */
	public Object insert(TCommonRecord params);

	/**
	 * 删除
	 * @param params 参数集
	 */
	public Object delete(TCommonRecord params);

	/**
	 * 更新
	 * @param params 参数集
	 */
	public Object update(TCommonRecord params);
	
	/**
	 * 非分页查询
	 * @param params 参数集
	 * @return
	 */
	public List<TCommonRecord> queryForList(TCommonRecord params);
	
	/**
	 * 分页查询
	 * @param params 参数集
     * @return
	 * */
	public PageView<TCommonRecord> queryForPageList(TCommonRecord params);
	
	/**
	 * 查询单一对象
	 * @param params 参数集
	 * @return
	 * */
	public TCommonRecord queryForObject(TCommonRecord params);
}