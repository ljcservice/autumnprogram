package com.ts.service.DoctOrder.OrderWork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ts.util.PageData;

public interface CommonService {
	
	/**
	 * 获得字段数据
	 * @return
	 * @throws Exception
	 */
	public Map<String, PageData> getCheckTypeDict() throws Exception;
	
	/**
	 * 获得字段数据
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getOrderClassDict() throws Exception;
}
