package com.ts.service.ai.stat.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.stat.manager.QualityStatManager;
import com.ts.util.PageData;

/**
 * 质量统计
 * @ClassName: QualityStatService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月23日 下午3:58:46 
 *
 */
@Service("qualityStatService")
public class QualityStatService implements QualityStatManager{

	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;


	/**
	 * 获取列表
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> sList(Page page) throws Exception{
		return(List<PageData>)dao.findForList("QualityStatMapper.statlistPage", page);
	}

	/**
	 * 获取全部列表
	 */
	@Override
	public List<PageData> sListAll(PageData pd) throws Exception {
		return(List<PageData>)dao.findForList("QualityStatMapper.statList", pd);
	}
}
