package com.ts.service.system.role;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.system.Role;
import com.ts.entity.system.User;
import com.ts.util.PageData;

/**	角色接口类
 * @author xsl
 * 修改日期：2015.11.6
 */
public interface RoleManager {
	
	
	/**通过id查找
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findObjectById(PageData pd) throws Exception;
	
	/**保存修改
	 * @param role
	 * @throws Exception
	 */
	public void edit(Role role) throws Exception;
	
	/**删除角色
	 * @param ROLE_ID
	 * @throws Exception
	 */
	public void deleteRoleById(String ROLE_ID) throws Exception;
	
	
	/**通过id查找
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public Role findRoleById(String ROLE_ID) throws Exception ;

	/**
	 * 根据已经拥有的角色ID集合，对角色树进行判断是否被选中
	 * @param roleList
	 * @param hasRoleIds
	 * @return
	 */
	public List<Role> setCheckedRole(List<Role> roleList,List<String> hasRoleIds) throws Exception;
	
	/**
	 * 获取当前角色下的所有角色，递归处理，不涉及权限
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Role> listAllRole(String string) throws Exception;
	/**
	 * 获取当前角色下的所有角色，递归处理，不涉及权限
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Role> listAllAccRole(String string) throws Exception;

	/**根据ID 获取角色信息
	 * @param pd
	 * @throws Exception
	 */
	public PageData getRoleById(PageData pd) throws Exception;

	/**
	 * 通过ID获取其子一级菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Role> listSubRoleByParentId(String rOLE_ID) throws Exception;
	
	/**
	 * 保存角色
	 * @param role
	 * @throws Exception
	 */
	public void saveRole(Role role) throws Exception ;

	/**
	 * 更新角色的功能权限 
	 * @param ROLE_ID
	 * @param pds
	 * @throws Exception
	 */
	public void updateRoleMF(String ROLE_ID, List<PageData> pds) throws Exception ;

	/**
	 * 删除角色的功能权限 
	 * @param rOLE_ID
	 * @throws Exception
	 */
	public void deleteRoleAllRights(String rOLE_ID) throws Exception ;

	/**通过用户ID获取用户拥有的角色集合
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public List<Role> getUserRolesById(String USER_ID) throws Exception ;
	
	/**通过用户ID获取用户拥有的角色ID集合
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public List<String> getUserRoleIdsById(String USER_ID) throws Exception ;
	
	/**
	 * 获取用户的最高角色,0管理角色，1授权角色，2业务角色
	 * @param USER_Id
	 * @return
	 * @throws Exception
	 */
	public int getUserMaxRolesByUsId(String USER_Id) throws Exception;
	
	/**
	 * 显示用户所有角色ID及角色下属角色ID集合（包括子角色）
	 * @param user_ID
	 * @return
	 * @throws Exception 
	 */
	public List<String> listSubRoleByUsId(String USER_ID) throws Exception;
	
	/**
	 * 显示用户所有角色及角色下属角色树,根据可用角色过滤
	 * @param rOLE_ID
	 * @param valideIds
	 * @param type 0 用户角色设置，1角色管理
	 * @return
	 * @throws Exception
	 */
	public List<Role> listAllRoleValide(String rOLE_ID, List<String> valideIds,int type) throws Exception;

	/**
	 * 用户所有可用的角色树
	 * @param string 根节点角色ID
	 * @param user 登陆用户
	 * @return
	 * @throws Exception
	 */
	public List<Role> listUserAllValideRole(String string, User user,int type) throws Exception;
	/**
	 * 字段管理所有可用的角色树
	 * @param string 根节点角色ID
	 * @param user 登陆用户
	 * @return
	 * @throws Exception
	 */
	public List<Role> listUserAllAccessRole(String string, User user,int type) throws Exception;

	/**
	 * 验证是否有用户使用该角色
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 * add by zhy
	 */
	public boolean checkRoleUsed(String ROLE_ID) throws Exception;
	
	/**
	 * 
	 * @Title: listRolesByType 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return List<PageData>    返回类型 
	 * @throws
	 */
	public  List<PageData>  listRolesByType(Page page) throws Exception;
}
