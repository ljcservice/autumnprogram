package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ts.service.DoctOrder.OrderWork.CommonService;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.util.PageData;

@Service(value="commonService")
public class CommonServiceImpl implements CommonService{
	
	@Autowired
	private IOrderWorkService orderWorkService;
	
	/**
	 * 获得字段数据
	 * @return
	 * @throws Exception
	 */
	public Map<String, PageData> getCheckTypeDict() throws Exception
	{
		Map<String, PageData> rs = new HashMap<>();
		// 审核字典
		List<PageData> rsTypeDict = this.orderWorkService.selectRsTypeDict();
		for(PageData pd : rsTypeDict){
			rs.put(pd.getString("RS_TYPE_CODE"), pd);
		}
		return rs;
	}
	
	/**
	 * 获得字段数据
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getOrderClassDict() throws Exception {
		Map<String, String> rs = new HashMap<>();
		// 审核字典
		List<PageData> rsTypeDict = this.orderWorkService.getOrderClassDict();
		for(PageData pd : rsTypeDict){
			rs.put(pd.getString("ORDER_CLASS_CODE"), pd.getString("ORDER_CLASS_NAME"));
		}
		return rs;
	}
	
}
