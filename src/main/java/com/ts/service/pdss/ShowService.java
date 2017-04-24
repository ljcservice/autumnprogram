package com.ts.service.pdss;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

public interface ShowService {

	public List<PageData> treeRootList(PageData pd) throws Exception;

	public List<PageData> queryList(Page page)throws Exception;

	public List<PageData> drugDirectionDetail(PageData pd)throws Exception;

	public List<PageData> queryIndividualItem(PageData pd)throws Exception;

	public List<PageData> queryCollectDescDict(PageData pd)throws Exception;

	public List<PageData> queryConsistencyRange(PageData pd)throws Exception;

	public List<PageData> queryDrugRelrefDirection(PageData pd)throws Exception;

	public List<PageData> queryClinicalPathwayInfo(PageData pd)throws Exception;
	
}
