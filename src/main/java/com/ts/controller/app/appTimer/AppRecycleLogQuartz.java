package com.ts.controller.app.appTimer;


import org.springframework.scheduling.annotation.Scheduled;  
import org.springframework.stereotype.Component;  
  

import com.ts.controller.app.appuser.AppUserQueue;
import com.ts.controller.base.BaseController;
import com.ts.entity.app.SysRecycleLog;
import com.ts.service.system.apimanager.RecycleLog.AppRecycleLogManager;
import com.ts.util.PageData;

import javax.annotation.Resource;
 
/**
 * 
 * ClassName: AppRecycleLogQuartz
 * @Description: 定时保存操作日志
 * @author 李世博
 * @date 2016年9月28日
 */

@Component
public class AppRecycleLogQuartz extends BaseController {
	@Resource(name = "appRecycleLogService")
	private AppRecycleLogManager appRecycleLogService;

	// 需要注意@Scheduled这个注解，它可配置多个属性：cron = "0 0/1 15,* * * ?"\fixedRate\fixedDelay = 5000 表示每隔5秒执行
//	@Scheduled(cron = "0 0/1 * * * ?")
	public void RecycleQuartz() {
//		PageData pd = new PageData();
//		logBefore(logger, "日志队列数>>>:"+AppUserQueue.getQueueSysRecycleLogSize() );
//		try {
//			while(AppUserQueue.getQueueSysRecycleLogSize() > 0 )
//			{
//				SysRecycleLog srl = AppUserQueue.getQueueSysRecycleLog();
//				
//				pd.put("log_id",srl.getLOG_ID());
//				pd.put("iner_type", srl.getINER_TYPE());
//				pd.put("input", srl.getINPUT());
//				pd.put("output", srl.getOUTPUT());
//				pd.put("user_ip", srl.getUSER_IP());
//				pd.put("user_id", srl.getUSER_ID());
//				pd.put("call_date", srl.getCALL_DATE());
//				pd.put("code", srl.getCODE());
//				logBefore(logger, "数据插入：>>"+ pd);
//				appRecycleLogService.save(pd);
//				
//			}
//			logBefore(logger, "日志队列为空等待中！！！");

//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
 		    	
    }  
}  
