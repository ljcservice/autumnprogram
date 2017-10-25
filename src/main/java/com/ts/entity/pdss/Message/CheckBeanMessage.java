package com.ts.entity.pdss.Message;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 审核基本的信息 
 * @author autumn
 *
 */
public class CheckBeanMessage implements Runnable
{
    private String  ip ; 
    private String  doctor; 
    private String  dept;
    private String  orderCount;
    private String  checkTime;
    private String  patName;
    private int     useCount = 0;
    private Date    time;
    
    public CheckBeanMessage(String ip, String doctor, String orderCount,
            String checkTime, String patName,String dept)
    {
        this.ip = ip;
        this.dept = dept;
        this.doctor = doctor;
        this.orderCount = orderCount;
        this.checkTime = checkTime;
        this.patName = patName;
        this.useCount = 1;
        this.time = new Date();
    }
    
    public String getDept()
    {
        return dept;
    }

    public void setDept(String dept)
    {
        this.dept = dept;
    }

    public Date getTime()
    {
        return time;
    }

    public String getIp()
    {
        return ip;
    }


    public void setIp(String ip)
    {
        this.ip = ip;
    }


    public String getDoctor()
    {
        return doctor;
    }


    public void setDoctor(String doctor)
    {
        this.doctor = doctor;
    }


    public String getOrderCount()
    {
        return orderCount;
    }


    public void setOrderCount(String orderCount)
    {
        this.orderCount = orderCount;
    }


    public String getCheckTime()
    {
        return checkTime;
    }


    public void setCheckTime(String checkTime)
    {
        this.checkTime = checkTime;
    }


    public String getPatName()
    {
        return patName;
    }


    public void setPatName(String patName)
    {
        this.patName = patName;
    }
    
    public void addUseCount()
    {
        this.useCount++;
    }

    public int  getUseCount()
    {
        return this.useCount;
    }
    
    public String getMapKey()
    {
        return  this.ip + "_" + this.doctor;
    }
    
    private static Map<String , CheckBeanMessage>  mapCBM = new HashMap<String ,CheckBeanMessage>();
    
    public static void setCheckBeanMessage( String  ip ,  String doctor,String orderCount,String checkTime,String patName,String dept)
    {
        CheckBeanMessage cbm = new CheckBeanMessage(ip, doctor, orderCount, checkTime, patName,dept);
        
        if(mapCBM.containsKey(cbm.getMapKey()))
        {
            cbm = mapCBM.get(cbm.getMapKey());
            cbm.setCheckTime(checkTime);
            cbm.setPatName(patName);
            cbm.setDept(dept);
            cbm.setOrderCount(orderCount);
            cbm.addUseCount();
        }
        mapCBM.put(cbm.getMapKey(), cbm);
    }
    
    public static Map<String,CheckBeanMessage> getCheckBeanMesage()
    {
        synchronized (mapCBM)
        {
            return mapCBM;
        }
    }
    public static void clean()
    {
        synchronized (mapCBM)
        {
            mapCBM.clear();
        }
    }
    
    public static int getCheckBeanMessageSize()
    {
        synchronized (mapCBM)
        {
            return mapCBM.size();
        }
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
    }
}
