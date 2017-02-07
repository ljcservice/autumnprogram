package com.ts.service.system.apimanager.Relation.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.service.system.apimanager.Relation.RelationManager;
import com.ts.util.PageData;


/** 
 * 说明： RelationManage
 * 创建人：李世博
 * 创建时间：2016-09-13
 * @version
 */
@Service("relationService")
public class RelationService implements RelationManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public boolean save(PageData pd){
		boolean  temp = false;
		try {
			Object obj = dao.save("RelationMapper.save", pd);
			if("1".equals(obj.toString())){
				temp = true;
			}	
		} catch (Exception e) {
			temp = false;
		}
		return temp;
	}	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public boolean delete(PageData pd){
		boolean  temp = false;
		try {
		Object obj = dao.delete("RelationMapper.delete", pd);
		if("1".equals(obj.toString())){
			temp = true;
		}	
	} catch (Exception e) {
		temp = false;
	}
	return temp;
	}
	public void deleteDRID(PageData pd)throws Exception {
			dao.delete("RelationMapper.deleteDRID", pd);
			dao.delete("AccessibleFieldMapper.deleteA", pd);
		}
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public boolean editA(PageData pd){
		boolean temp = false;
		try {
			Object obj = dao.update("RelationMapper.editA", pd);
			if("1".equals(obj.toString())){
				temp = true;
			}
		} catch (Exception e) {
			temp = false;
		}
		return temp;
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("RelationMapper.listAll", pd);
	}
	/**通过DR_ID(查找)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listByDRId(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("RelationMapper.listByDRId", pd);
	}
	/**通过DR_ID(查找)
	 * @param pd
	 * @throws Exception
	 */
	
	public boolean findByDRId(String DR_ID)throws Exception{
		Integer count = (Integer)dao.findForObject("RelationMapper.findByDRId", DR_ID);
		if(count!=null && count >=1){
			return true;
		}
		return false;
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<String> listAllIds(PageData pd)throws Exception{
		List<PageData> list =  listAll(pd);
		List<String> result = new ArrayList<String>();
		for(PageData p:list){
			result.add(p.getString("DR_ID"));
		}
		return result;
	}
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<String> listAllName(PageData pd)throws Exception{
		pd =   findById(pd);
		List<String> result = new ArrayList<String>();
		String col =pd.getString("COL_RULE");
		if(!"".equals(col) && col != null){
			String[] s = col.split(";");
			for(String m:s){
				result.add(m);
			}
		}
		
		return result;
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("RelationMapper.findById", pd);
	}
	/**通过COL_RULE获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByCol(PageData pd)throws Exception{
		return (PageData)dao.findForObject("RelationMapper.findByCol", pd);
	}
	/**通过id批量删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] TR_IDS) throws Exception {
		dao.delete("RelationMapper.deleteAll", TR_IDS);
		
	}
	
	/**
	 * 通过用户id返回表(SYS_ROLE_TABLE_RELATION)中该用户下的所有数据，供APP用户权限校验使用
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findDataByUserId(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("RelationMapper.findDataByUserId", pd);
	}
	/**批量更新
	 * @param pd
	 * @throws Exception
	 */

	public void editAll(List<PageData> pd)throws Exception{
		dao.batchUpdate("RelationMapper.editA", pd);
	}
	/**批量删除数据
	 * @param pd
	 * @throws Exception
	 */
	
	public boolean deleteID(List<PageData> pd){
		boolean temp = false;
		try {
		Object obj = dao.batchDelete("RelationMapper.deleteID", pd);
		if(!"".equals(obj.toString())){
			temp = true;
		}	
	} catch (Exception e) {
		temp = false;
	}
	return temp;
}
	
	/**批量添加
	 * @param pd
	 * @throws Exception
	 */
	
	public boolean saveAll(List<PageData> pd){
		boolean temp = false;
		try {
			Object obj = dao.batchSave("RelationMapper.saveAll", pd);
			if(!"".equals(obj.toString())){
				temp = true;
			}	
		} catch (Exception e) {
			temp = false;
		}
		return temp;
	}
}

