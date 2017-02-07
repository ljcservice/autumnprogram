package com.ts.controller.app.appTimer;


import org.springframework.scheduling.annotation.Scheduled;  
import org.springframework.stereotype.Component;  

import com.ts.controller.base.BaseController;
import com.ts.service.system.appuser.AppTokenManager;
import com.ts.util.app.SessionAppMap;

import javax.annotation.Resource;
/**
 *   
 * ClassName: AppDelMapQuartz
 * @Description: 删除Map缓存过期的token
 * @author 李世博
 * @date 2016年9月22日
 */

@Component
public class AppDelMapQuartz extends BaseController {
	@Resource(name = "appTokenService")
	private AppTokenManager appTokenService;

	// 需要注意@Scheduled这个注解，它可配置多个属性：cron = "0 0/3 15 * * * ?"\fixedRate\fixedDelay = 5000 表示每隔5秒执行
	@Scheduled(cron = "0 0/59 * * * ?")
	public void Quartz() {
		logBefore(logger, "进入删除过期MapToken！！！");
		SessionAppMap.reMovetimeOut();
 		    	
    }  
}  
