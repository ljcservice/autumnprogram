package com.hitzd.his.Service.hibernateService;

import java.util.List;

import com.hitzd.DBUtils.TCommonRecord;
/**
 * 
 * @author tyl
 *
 */
public interface IHiberBiz {

	/**
	 * 查询
	 * @param sql
	 * @return
	 */
	public List getList(String sql);
	/**
	 * 更新
	 * @param sql
	 */
	public void update(String sql) ;
	/**
	 * 删除
	 * @param sql
	 */
	public void delete(String sql);
	/**
	 * 插入
	 * @param sql
	 */
	public void insert(String sql);
}
