package com.hitzd.his.SysLog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import com.hitzd.his.Utils.DateUtils;

/**
 * 内部内容日志 
 * @author jingcong
 *
 */
public class SysLog4Dcdt
{
    
    /* 导入日志 */
    protected Vector<String> vctLog     = new Vector<String>();
    
    /**
     * 
     * 调试级别，用来控制Log信息的输出，级别越大，输出信息越简单
     * 
     */
    private int DebugLevel = 0;
    public void setDebugLevel(int debugLevel)
    {
        System.out.println(toString() + " 设置调试输出级别为" + debugLevel);
        DebugLevel = debugLevel;
    }
    
    /**
     * 输入日志
     * @param info 日志内容 
     */
    protected void Log(String info)
    {
        Log(DebugLevel, info);
    }
    
    /**
     * 输入日志
     * @param level 日志级别
     * @param info  
     */
    protected void Log(int level, String info)
    {
        String s = DateUtils.getDateTime() + "  " + info;
        if (level >= DebugLevel)
        {
            vctLog.add(s);
            System.out.println(s);
        }
    }
    
    /**
     * 保存日志 
     * @param FileName
     * @return
     */
    public String saveLog(String FileName)
    {
        File file = new File(FileName);
        File tempF = new File(file.getParent());
        try
        {
            if (!tempF.exists())
            {
                tempF.mkdirs();
            }
            FileWriter o = new FileWriter(file);
            for (String x : vctLog)
            {
                o.write(x + "\n");
            }
            o.flush();
            o.close();
        }
        catch (IOException e)
        {
            System.out.print("保存日志文件出现异常");
            e.printStackTrace();
        }
        System.out.println("日志保存在" + file.getAbsolutePath());
        vctLog.clear();
        return file.getAbsolutePath();
    }
}
