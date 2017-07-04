package com.ts.service.matcher;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

public interface MatcherService {

	public List<PageData> drugMapListPage(Page page) throws Exception;

	public List<PageData> drugList(PageData pd) throws Exception;

	public PageData countMatcherSum() throws Exception;

	/**
	 * 查询出需要替换过滤的无用的字符串
	 * @return
	 */
	public List<String> getFilterList() throws Exception;
}