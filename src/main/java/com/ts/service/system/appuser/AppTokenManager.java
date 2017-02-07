package com.ts.service.system.appuser;



import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/** 
 * 说明： TokenManage接口
 * 创建人：
 * 创建时间：2016-09-13
 * @version
 */
public interface AppTokenManager{

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
	/**通过Token_Info 删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteTokenInfo(PageData pd)throws Exception;
	/**通过Validity 删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteValidity(PageData pd)throws Exception;
	
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
	/**通过用户id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUserId(PageData pd)throws Exception;
	/**通过刷新令牌获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByToken(PageData pd)throws Exception;
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	
}

