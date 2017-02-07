package com.ts.service.system.apimanager.Relation;



import java.util.List;

import com.ts.util.PageData;

/** 
 * 说明： Relation接口
 * 创建人：
 * 创建时间：2016-09-13
 * @version
 */
public interface RelationManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public boolean save(PageData pd);
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public boolean delete(PageData pd);
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteDRID(PageData pd) throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public boolean editA(PageData pd);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<String> listAllIds(PageData pd)throws Exception;
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<String> listAllName(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	/**通过DR_ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public boolean findByDRId(String DR_ID)throws Exception;
	/**通过DR_ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listByDRId(PageData pd)throws Exception;
	/**通过COL_RULE获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCol(PageData pd)throws Exception;
	
	
	/**批量更新数据
	 * @param pd
	 * @throws Exception
	 */
	public void editAll(List<PageData> pd)throws Exception;
	
	/**批量删除数据
	 * @param pd
	 * @throws Exception
	 */
	public boolean deleteID(List<PageData> pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] TR_IDS)throws Exception;
	
	/**批量添加
	 * @param pd
	 * @throws Exception
	 */
	public boolean saveAll(List<PageData> pd);
	
	/**
	 * 通过用户id返回表(SYS_ROLE_TABLE_RELATION)中该用户下的所有数据，供APP用户权限校验使用
	 */
	public List<PageData> findDataByUserId(PageData pd)throws Exception;
}

