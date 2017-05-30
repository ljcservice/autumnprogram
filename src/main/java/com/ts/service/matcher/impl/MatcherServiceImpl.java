package com.ts.service.matcher.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

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
	
	
	
}