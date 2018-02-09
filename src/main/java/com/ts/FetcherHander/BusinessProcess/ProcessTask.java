package com.ts.FetcherHander.BusinessProcess;

import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.hitzd.springBeanManager.SpringBeanUtil;
import com.ts.dao.DaoSupportPH;
import com.ts.service.pdss.pdss.Cache.InitPdssCache;
import com.ts.util.Logger;

/**
 * 定时处理任务，
 */
//@Service("processTask")
public class ProcessTask implements Job
{
    
    private static Logger log  = Logger.getLogger(ProcessTask.class); 
    
//    @Resource(name="daoSupportPH")
    private DaoSupportPH  daoPH = (DaoSupportPH)SpringBeanUtil.getBean("daoSupportPH");

//    @Resource(name="initPdssCache")
    private InitPdssCache initPdssCache = (InitPdssCache)SpringBeanUtil.getBean("initPdssCache");
    
    @Override
    public void execute(JobExecutionContext context)  throws JobExecutionException
    {
        JobDataMap  dataMap =  context.getJobDetail().getJobDataMap();
        Map<String,Object> parameter = (Map<String,Object>)dataMap.get("parameterList");    //获取参数
        Object param =  parameter.get("param");
//        log.info("线程数:" + Thread.activeCount());
        try
        {
            log.info("线程ID" + Thread.currentThread().getId() + ",药物控制授权刷新累计用量! Begin");
            DayProcess();
            Calendar cal = Calendar.getInstance();
            // 每月中的天
            int day = cal.get(Calendar.DAY_OF_MONTH);
            MonthProcess(day);
            // 月份。
            int month = cal.get(Calendar.MONTH);
            QuarterProcess(month, day);
            // 年 天
            int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
            YearProcess(dayOfYear);
            initPdssCache.setDrugUserAuth();
            log.info("线程ID" + Thread.currentThread().getId() + ",药物控制授权刷新累计用量! End");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 年度更新
     * @param dayOfYear
     */
    private void YearProcess(int dayOfYear)
    {
        if(dayOfYear != 1) return ;
        Integer type = 3;
        try
        {
            daoPH.update("CKDrugUserAuth.updateDUAuthByTotalOfType", type);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
   
    /**
     * 季度更新
     * @param month
     * @param day
     */
    private void QuarterProcess(int month ,int day)
    {
        if(day !=1) return ;
        if(!(month == 0 || month == 4 || month == 7 || month == 10)) return ;
        Integer type = 4;
        try
        {
            daoPH.update("CKDrugUserAuth.updateDUAuthByTotalOfType", type);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 月度更新
     * @param day
     */
    private void MonthProcess(int day)
    {
        if(day != 1) return ;
        Integer type = 2;
        try
        {
            daoPH.update("CKDrugUserAuth.updateDUAuthByTotalOfType", type);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 每日更新
     */
    private void DayProcess()
    {
        Integer type = 1;
        try
        {
            daoPH.update("CKDrugUserAuth.updateDUAuthByTotalOfType", type);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
