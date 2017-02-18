package com.ts.service.pdss.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hitzd.his.Scheduler.ModelHandler;

/**
 * 该类实现定时器调用功能
 * @author Administrator
 *
 */  
public class Scheduler 
{
    private static ScheduledExecutorService sess = null;
    static  
	{
	    System.out.println("time start");
        /* 定时器 */
        sess =  Executors.newSingleThreadScheduledExecutor();
        System.out.println("处方抓取定时器 启动 .......");
        /* 对门诊及住院处方 的抓取  */
        sess.scheduleAtFixedRate(new ModelHandler("Drug_presc_Master") ,0,1,TimeUnit.HOURS);
        System.out.println("定时器 启动完毕！ ");
        /* 事后审查   */
        //System.out.println(" 事后审核定时器 启动 ...... ");
        //sess.scheduleAtFixedRate(new AuditTimer(),0,1,TimeUnit.HOURS);
        //System.out.println("定时器 启动完毕！ ");
        System.out.println("time end");
	}

    public void closeSchedule()
    {
        sess.shutdown();
    }
}
