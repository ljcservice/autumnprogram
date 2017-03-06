package com.ts.controller.pdss;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ts.service.pdss.DictCacheService;
import com.ts.util.Logger;
import com.ts.util.PageData;

/**
 * 数据字典处理
 * @author silong.xing
 */
@Component
public class DictController {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="dictCacheService")
	private DictCacheService dictCacheService;
	
	/**
	 * 重新初始化字典数据，每周天晚上12点。
	 */
//	@Scheduled(fixedRate = 1000 * 60 * 1)
	public void reload()  throws Exception{
		logger.info("init dict info start.");
		init();
		logger.info("init dict info end.");
	}

	/**
	 * 初始化字典数据
	 */
	
	public void init () throws Exception{
		try {
			dictCacheService.initCache();
		} catch (Exception e) {
			logger.error("init dict info error！！！", e);
		}
		
	}

	private void setMap(List<PageData> dictList,final Map<String, String> map) {
		map.clear();
		if(dictList!=null){
			for(PageData p:dictList){
				map.put(p.getString("KEY"), p.getString("VALUE"));
			}
		}
	}
	
	
}
