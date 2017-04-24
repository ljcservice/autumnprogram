package com.hitzd.his.Scheduler;

import com.hitzd.his.report.utils.ClassLoaderUnit;

public class SchedulerFactory 
{
    public static IScheduler getScheduler(String clazz)
    {
        synchronized(SchedulerFactory.class)
        {
        	Object obj = null;
			try 
			{
				obj = ClassLoaderUnit.loadClass(clazz).newInstance();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
        	return (IScheduler) obj;
        }
    }
}
