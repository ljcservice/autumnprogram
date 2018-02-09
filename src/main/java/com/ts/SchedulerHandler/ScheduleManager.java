package com.ts.SchedulerHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DrugUtils;
import com.ts.FetcherHander.BusinessProcess.ProcessTask;
import com.ts.SchedulerHandler.ModelHandler;
import com.ts.entity.pdss.SaveER.SaveThreadTCR;
import com.ts.entity.pdss.Saver.SaveThread;
import com.ts.service.pdss.pdss.Cache.InitPdssCache;
import com.ts.util.ApplicationUtil;
import com.ts.util.Logger;

/**
 * 所有初始化加载
 * @author autumn
 *
 */
@Service
public class ScheduleManager
{
    private static final Logger log = Logger.getLogger(ScheduleManager.class);
    
    @Resource(name="initPdssCache")
    private InitPdssCache initPdssCache;
    
    private void initPdss() 
    {
        try
        {
            initPdssCache.loadLoaclCache();
            initPdssCache.setDrugUserAuth();
            initPdssCache.setOperationDrug();
        }
        catch( Exception e  )
        {
            e.printStackTrace();
            log.info("药品规则加载 出现异常 ！",e);
        }
    }
    
    /**
     * 所有初始化方法
     * 加载spring容器后，自己动执行该方法。
     */
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
        addJob("0 0 1 * * ? *","Drug_presc_Master");
//        log.info("住院病历抓取定时器启动.........");
        addJob("0 0 1 * * ? *","DataFetcherHospital");
        log.info("定时器 启动完毕！ ");
        /* 事后审查   */
        //System.out.println(" 事后审核定时器 启动 ...... ");
        //sess.scheduleAtFixedRate(new AuditTimer(),0,1,TimeUnit.HOURS);
        //System.out.println("定时器 启动完毕！ ");
        log.info("time end");
        
        log.info("Cache Build Start");
//        ApplicationUtil.getBean("");
        log.info("CacheBuild -- 加载药品缓存");
        DrugUtils.ReSetToxiProperty();
        log.info("CacheBuild -- 加载参数缓存");
        Config.initParam();
        log.info("CacheBuild -- 加载中间层缓存");
        Config.initTableConfig();
        log.info("Cache Build End");
        
        log.info("药品规则加载 Begin");
        initPdss();
        log.info("药品规则加载 Ends");
        
        log.info("加载执行任务 begin");
        // 0 0/5 * * * ? 
        addJob("0 0 1 * * ?", "ProcessTask",ProcessTask.class,null);
        log.info("加载执行任务 end");
        
        
        
    }
    
    
    private void addJob(String crn ,String jobName){
        Map<String, Object> parameter = new HashMap<String,Object>();
        parameter.put("mhGroupCode", jobName);
        QuartzFecherManager.addJob(jobName, ModelHandler.class, crn, parameter);
    }
    
    private void addJob(String crn ,String jobName,Class clazz ,Object parameter)
    {
        Map<String , Object> param = new HashMap<String , Object >();
        param.put("param", parameter);
        QuartzFecherManager.addJob(jobName, clazz , crn,param);
    }
    
    @PreDestroy
    public void ScheduleDestroy(){
        
        sess.shutdown();
    }
    private static ScheduledExecutorService sess = null;

}
