package com.ts.service.dst.datasource2;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/** 
 * 说明： 第2数据源接口
 * 创建人：
 * 创建时间：2016-04-29
 * @version
 */
public interface DataSource2Manager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**
	 * 查询app
	 * 
	 * */
	public List<PageData> json(PageData pd)throws Exception;
	
	/**
	 * 分页查询app
	 * 
	 * */
	public List<PageData> jsonPage(Page page)throws Exception;
	
}

