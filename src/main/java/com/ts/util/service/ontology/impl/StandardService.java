package com.ts.service.ontology.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.entity.ai.AiAlterNameHist;
import com.ts.service.ontology.manager.OsynManager;
import com.ts.service.ontology.manager.StantardManager;
import com.ts.util.PageData;

/** 
 * 类名称：DiagService 诊断处理
 * 创建人：xingsilong
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("standardService")
public class StandardService implements StantardManager{

	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;


	public List<PageData> osynPage(Page page,String sqlName) throws Exception{
		List<PageData> list = (List<PageData>)dao.findForList("OsynMapper.osyn"+sqlName+"listPage", page);
		return list;
	}
	/**
	 * 选择标准词列表
	 */
	public List<PageData> chooseStandPage(Page page,String sqlName) throws Exception{
		List<PageData> list = (List<PageData>)dao.findForList("OsynMapper."+sqlName+"listPage",page);
		return list;
	}

}
