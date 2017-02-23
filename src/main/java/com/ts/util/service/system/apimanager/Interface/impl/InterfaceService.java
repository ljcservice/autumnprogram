package com.ts.service.system.apimanager.Interface.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.Interface.InterfaceManager;
import com.ts.util.PageData;


/**类名称：InterfaceService
 * @author 
 * 修改时间：2016年10月11日
 */
@Service("interfaceService")
public class InterfaceService implements InterfaceManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	
	/**接口列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listPdPageInterface(Page page)throws Exception{
		return (List<PageData>) dao.findForList("InterfaceMapper.interfacelistPage", page);
	}
		
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveI(PageData pd)throws Exception{
		dao.save("InterfaceMapper.saveI", pd);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteI(PageData pd)throws Exception{
		dao.delete("InterfaceMapper.deleteI", pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editI(PageData pd)throws Exception{
		dao.update("InterfaceMapper.editI", pd);
	}
	
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllInterface(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("InterfaceMapper.listAllInterface", pd);
	}
	
	/**批量删除用户
	 * @param IN_IDS
	 * @throws Exception
	 */
	public void deleteAllI(String[] IN_IDS)throws Exception{
		dao.delete("InterfaceMapper.deleteAllI", IN_IDS);
	}
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("InterfaceMapper.findById", pd);
	}
	
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getInterfaceCount(String value)throws Exception{
		return (PageData)dao.findForObject("InterfaceMapper.getInterfaceCount", value);
	}
	
}

