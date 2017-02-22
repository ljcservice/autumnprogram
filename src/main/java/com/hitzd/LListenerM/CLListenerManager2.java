package com.hitzd.LListenerM;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

import com.hitzd.his.Utils.DictCache;

public class CLListenerManager2 extends ContextLoaderListener
{
    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        super.contextInitialized(event);
        ProgInit();
        DictCache.getNewInstance();
    }

    /**
     * 可以将所有启动后就执行的方法 放在里面 
     */
    protected void ProgInit()
    {
    }
}
