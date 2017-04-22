package com.ts.SchedulerHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ts.SchedulerHandler.ModelHandler;
import com.ts.entity.pdss.SaveER.SaveThreadTCR;
import com.ts.entity.pdss.Saver.SaveThread;

@Service
public class ScheduleManager
{

    private static final Logger log = Logger.getLogger(ScheduleManager.class);
    @PostConstruct
    public void ScheduleInit(){
        
        log.info("启动列队雷达 .....");
        ExecutorService es = Executors.newScheduledThreadPool(2);
        es.execute(new SaveThread());
        log.info("启动列队雷达完毕！");
        log.info("启动审查时间监控列队雷达 .....");
        es.execute(new SaveThreadTCR());
        log.info("启动列队雷达完毕！");
        
        log.info("time start");
        /* 定时器 */
//        sess =  Executors.newSingleThreadScheduledExecutor();
        log.info("门诊处方抓取定时器 启动 .......");
        /* 对门诊及住院处方 的抓取  */
        //一小时执行一次 
        addJob("0 0 0/1 * * ? *","Drug_presc_Master");
        log.info("住院病历抓取定时器启动.........");
        addJob("0 0 0/1 * * ? *","DataFetcherHospital");
        log.info("定时器 启动完毕！ ");
        /* 事后审查   */
        //System.out.println(" 事后审核定时器 启动 ...... ");
        //sess.scheduleAtFixedRate(new AuditTimer(),0,1,TimeUnit.HOURS);
        //System.out.println("定时器 启动完毕！ ");
        log.info("time end");
    }
    
    
    private void addJob(String crn ,String jobName){
        Map<String, Object> parameter = new HashMap<String,Object>();
        parameter.put("mhGroupCode", jobName);
        QuartzFecherManager.addJob(jobName, ModelHandler.class, crn, parameter);
    }
    
    
    @PreDestroy
    public void ScheduleDestroy(){
        
        sess.shutdown();
    }
    private static ScheduledExecutorService sess = null;

}
