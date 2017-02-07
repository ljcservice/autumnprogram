package com.ts.controller.app.appTimer;


import org.springframework.scheduling.annotation.Scheduled;  
import org.springframework.stereotype.Component;  

import com.ts.controller.base.BaseController;
import com.ts.service.system.appuser.AppTokenManager;
import com.ts.util.PageData;

import javax.annotation.Resource;
 /**
  *  
  * ClassName: AppDelTokenQuartz
  * @Description: 定时删除过期的刷新Token
  * @author 李世博
  * @date 2016年9月20日
  */

@Component
public class AppDelTokenQuartz extends BaseController {
	@Resource(name = "appTokenService")
	private AppTokenManager appTokenService;

	// 需要注意@Scheduled这个注解，它可配置多个属性：cron = "0 0/1 15,* * * ?"\fixedRate\fixedDelay = 5000 表示每隔5秒执行
	@Scheduled(cron = "0 0 03 * * ?")
	public void Quartz() {
		PageData pd = new PageData();
		Long dates = System.currentTimeMillis(); // 获取当前时间戳
		try {
			StringBuffer del = new StringBuffer();
			del.append("delete").append(" from sys_token_info where ").append("VALIDITY <"+ dates).toString();
			pd.put("sql", del);
			appTokenService.deleteValidity(pd);
			logBefore(logger, "删除过期Token！！！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		    	
    }  
}  
