package com.hitzd.his.report.utils;

/**
 * 构建报表处理类
 * @author
 *
 */
public class ReportHandlerFactory 
{
	private static Object Locker = new Object();

	public synchronized static ReportHandler getInstance(String Objects) 
	{
		//synchronized (Locker) {
			try 
			{
				return (ReportHandler) ClassLoaderUnit.loadClass(Objects)
						.newInstance();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return null;
		//}
	}
}
