package com.hitzd.his.Scheduler;

import com.hitzd.his.report.utils.JillClassLoader;

public class SchedulerFactory 
{
    public static IScheduler getScheduler(String clazz)
    {
        synchronized(SchedulerFactory.class)
        {
        	Object obj = null;
			try 
			{
				obj = JillClassLoader.loadClass(clazz).newInstance();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
        	return (IScheduler) obj;
        }
    }
}
