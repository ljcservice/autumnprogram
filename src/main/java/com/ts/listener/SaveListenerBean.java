package com.ts.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hitzd.LListenerM.CLListenerManager2;
import com.ts.entity.pdss.SaveER.SaveThreadTCR;
import com.ts.entity.pdss.Saver.SaveThread;
import com.ts.service.pdss.timer.Scheduler;

/**
 * 
 * @author Administrator
 *
 */
public class SaveListenerBean extends CLListenerManager2
{
    @Override
    protected void ProgInit()
    {
        
        System.out.println("启动列队雷达 .....");
        ExecutorService es = Executors.newScheduledThreadPool(2);
        es.execute(new SaveThread());
        System.out.println("启动列队雷达完毕！");
        
        System.out.println("启动审查时间监控列队雷达 .....");
        es.execute(new SaveThreadTCR());
        System.out.println("启动列队雷达完毕！");
        /* 将peaas抓取数据 和 事后审核加入*/
        Scheduler s = new Scheduler();
        //ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        //ses.scheduleAtFixedRate(new HisAuditorTimer(), 1, 1, TimeUnit.HOURS);
        //DrugNoLocalCache.loadDrugNoLocals();
    }
}
