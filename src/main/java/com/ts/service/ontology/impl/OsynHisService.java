package com.ts.service.ontology.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ontology.manager.OsynHisManager;
import com.ts.util.PageData;

/** 
 * 类名称：词语历史维护
 * 创建人：zq
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("osynHisService")
@SuppressWarnings("unchecked")
public class OsynHisService implements OsynHisManager{

	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;

	/**
	 * 查询单个词语历史变更记录
	 */
	@Override
	public List<PageData> nameHistPage(Page page,String tableName) throws Exception {
		return (List<PageData>)dao.findForList("AiAlterNameHistMapper."+tableName+"HistlistPage",page);
	}

	/**
	 * 查询审核词语信息
	 */
	@Override
	public List<PageData> checkNameList(Page page) throws Exception {
		return (List<PageData>)dao.findForList("AiAlterNameHistMapper.checkNamelistPage",page);
	}

	/**
	 * 查询审核单个新数据详细信息
	 */
	@Override
	public PageData alterNameDetail(PageData page) throws Exception {
		return (PageData)dao.findForObject("AiAlterNameHistMapper.alterNameDetail", page);
	}

	/**
	 * 更新词语副本信息
	 */
	@Override
	public String updateAlterById(PageData page) throws Exception {
		String msg = "success";
		dao.update("AiAlterNameHistMapper.upAlterName",page);
		return msg;
	}

	/**
	 * 根据ID查询单个副本全部信息
	 */
	@Override
	public PageData findAlterById(String h_id) throws Exception {
		return (PageData) dao.findForObject("AiAlterNameHistMapper.findAlterById",h_id);
	}
	
	/**
	 * 新增词语副本信息
	 */
	public String saveDiagOsyn(PageData pd) throws Exception {
		String msg = "success";
		dao.save("AiAlterNameHistMapper.saveOsynHis", pd);
		return msg;
	}
	/**
	 * 查询同义词历史
	 * @param param
	 * @return
	 */
	public List<PageData> queryOsynHis(PageData param)throws Exception {
		return (List<PageData>)dao.findForList("AiAlterNameHistMapper.osynHisList", param);
	}
}
