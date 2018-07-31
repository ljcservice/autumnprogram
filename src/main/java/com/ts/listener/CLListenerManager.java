package com.ts.listener;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;


/**
 * 重写Spring Listener 
 * @author autumn
 *   
 */
public class CLListenerManager extends ContextLoaderListener
{  
//    private ConfigurableApplicationContext app ;
    
    public void contextInitialized(ServletContextEvent event)  
    {    
        super.contextInitialized(event);
        setScheduled2GC();
//        setDataSourec();
    }
    
//    private void setDataSourec(){
//
//        WebApplicationContext wac = super.getCurrentWebApplicationContext();
//        
//        String[] name =wac.getBeanNamesForAnnotation(Service.class);
//        
//        for(String s : name){
//            System.out.println(s);
//            if(!"aiService".equals(s)) continue;
//            Object bean = wac.getBean(s);
//            Class clazz = bean.getClass();
//            
//            Field[] fs = clazz.getFields();
//            
//            for(Field f : fs){
//                f.getAnnotation(MyDao.class);
//                System.out.println(f.getName());
//            }
//            
//        }
//        
//    }
    
    private ScheduledExecutorService scheduled;
    /**
     * 定时执行GC 
     */
    private void setScheduled2GC(){
        scheduled = (ScheduledExecutorService) Executors.newSingleThreadScheduledExecutor();
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run()
            {
                System.gc(); 
            }
        }, 0, 3, TimeUnit.MINUTES);
    }
    
}
