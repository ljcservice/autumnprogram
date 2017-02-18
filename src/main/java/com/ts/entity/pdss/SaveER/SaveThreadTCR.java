package com.ts.entity.pdss.SaveER;

import com.hitzd.DBUtils.TCommonRecord;

public class SaveThreadTCR implements Runnable
{
    public SaveThreadTCR(){}

    private static ISaveOperaction saveOperaction = new SaveOperaction();

    @Override
    public void run()
    {
        while (true)
        {
            // System.out.println("雷达开始扫描......");
            if (QueueBeanTCR.getSaveBeanRSSize() > 0)
            {
                try
                {
                    // System.out.println("雷达捕捉到猎物进行处理 ");
                    TCommonRecord tcr = QueueBeanTCR.getQueueSaveBeanRS();
                    if (tcr != null)
                    {
                        /* 处理信息   */
                        saveOperaction.saveOperaction(tcr);
                    }
                }
                catch (Exception e)
                {
                    System.out.println(" 保存队列出现问题 !");
                    e.printStackTrace();
                }
            }
            else
            {
                // System.out.println("扫描没有猎物等待5秒后继续 扫描 ");
                try
                {
                    Thread.sleep(10000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}