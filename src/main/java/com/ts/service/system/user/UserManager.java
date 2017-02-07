package com.ts.service.system.user;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.util.PageData;


/** 用户接口类
 * @author xsl
 * 修改时间：2015.11.2
 */
public interface UserManager {
	
	/**登录判断
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public User getUserByNameAndPwd(PageData pd)throws Exception;
	
	/**
	 * 查找用户
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public User findUserById(String USER_ID)throws Exception;
	
	/**更新登录时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateLastLogin(User user)throws Exception;
	
	/**通过USERNAEME获取数据
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(String name)throws Exception;
	
	/**保存用户IP
	 * @param pd
	 * @throws Exception
	 */
	public void saveIP(PageData pd)throws Exception;
	
	/**用户列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listUsers(Page page)throws Exception;
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUE(PageData pd)throws Exception;
	
	/**通过编号获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUN(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception;
	
	/**保存用户
	 * @param user
	 * @throws Exception
	 */
	public void saveU(User user)throws Exception;
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception;
	
	/**批量删除用户
	 * @param USER_IDS
	 * @throws Exception
	 */
	public void deleteAllU(String[] USER_IDS)throws Exception;
	
	/**用户列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllUser(PageData pd)throws Exception;
	
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getUserCount(String value)throws Exception;
	
	/**
	 * 删除用户的所有角色
	 * @param uSER_ID
	 * @throws Exception
	 */
	public void deleteUserAllRoles(String uSER_ID) throws Exception ;

	/**
	 * 更新用户角色
	 * @param uSER_ID
	 * @param pds
	 * @throws Exception
	 */
	public void updateUserRoles(String uSER_ID, List<PageData> pds) throws Exception ;
	
	/**用户列表(下拉框使用)
	 * @param USER_IDS
	 * @throws Exception
	 */
	public List<PageData> listSimpleUser(PageData pd) throws Exception;
}
