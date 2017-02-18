package com.ts.entity.pdss.Saver;

import java.util.LinkedList;

/**
 * 队列
 * @author Administrator
 *
 */
public final class QueueBean 
{

	private static LinkedList<SaveBeanRS> QueueSaveBean = new LinkedList<SaveBeanRS>();
	
	/**
	 * 获得队中数据集
	 * @return
	 */
	public static SaveBeanRS getQueueSaveBeanRS()
	{
		synchronized (QueueSaveBean) 
		{
			if(QueueSaveBean.size() >  0 )
			{
				return QueueSaveBean.removeFirst();	
			}
		}
		return null;
	}
	
	/**
	 * 获得数据集 在 队列 个数
	 */
	public static int getSaveBeanRSSize()
	{
		synchronized (QueueSaveBean) 
		{
			return QueueSaveBean.size();	
		}
	}
	
	/**
	 * 将总数据集 放到队列中
	 * @param rs
	 */
	public static void setSaveBeanRS(SaveBeanRS rs)
	{
		synchronized (QueueSaveBean) 
		{
			QueueSaveBean.addLast(rs);
		}
	}
	
	private  QueueBean() {}
}
