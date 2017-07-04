package com.ts.service.matcher.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupportPdss;
import com.ts.entity.Page;
import com.ts.service.matcher.MatcherService;
import com.ts.util.PageData;

@Service
public class MatcherServiceImpl  implements MatcherService
{
	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	
	public List<PageData> drugMapListPage(Page page) throws Exception{
		return (List<PageData>)dao.findForList("MatcherMapper.drugMapListPage", page);
	}

	public List<PageData> drugList(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("MatcherMapper.drugList", pd);
	}
	

	public PageData countMatcherSum() throws Exception{
		return (PageData)dao.findForObject("MatcherMapper.countMatcherSum", new PageData());
	}
	
	/**
	 * 查询出需要替换过滤的无用的字符串
	 * @return
	 */
	public List<String> getFilterList() throws Exception{
		List<String> list = (List<String>)dao.findForList("MatcherMapper.getFilterList", new PageData());
		if(list==null){
			return new  ArrayList<String>();
		}
		return list;
	}
	
}