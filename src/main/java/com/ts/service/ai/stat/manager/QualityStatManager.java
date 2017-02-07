package com.ts.service.ai.stat.manager;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 质量统计
 * @ClassName: QualityStatManager 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月23日 下午3:58:59 
 *
 */
public interface QualityStatManager {

	public List<PageData> sList(Page page) throws Exception;
	
	public List<PageData> sListAll(PageData pd) throws Exception;
	
	
}
