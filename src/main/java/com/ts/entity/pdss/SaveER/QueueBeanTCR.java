package com.ts.entity.pdss.SaveER;

import java.util.LinkedList;
import com.hitzd.DBUtils.TCommonRecord;
/**
 * 列队结果集
 * @author Administrator
 *
 */
public final class QueueBeanTCR
{

    private static LinkedList<TCommonRecord> queueBeanTCR = new LinkedList<TCommonRecord>();

    /**
     * 获得队中数据集
     * 
     * @return
     */
    public static TCommonRecord getQueueSaveBeanRS()
    {
        synchronized (queueBeanTCR)
        {
            if (queueBeanTCR.size() > 0)
            {
                return queueBeanTCR.removeFirst();
            }
        }
        return null;
    }

    /**
     * 获得数据集 在 队列 个数
     */
    public static int getSaveBeanRSSize()
    {
        synchronized (queueBeanTCR)
        {
            return queueBeanTCR.size();
        }
    }

    /**
     * 将总数据集 放到队列中
     * 
     * @param tcr
     */
    public static void setSaveBeanRS(TCommonRecord tcr)
    {
        synchronized (queueBeanTCR)
        {
            queueBeanTCR.addLast(tcr);
        }
    }

    private QueueBeanTCR()
    {
    }
}
