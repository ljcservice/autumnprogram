package com.hitzd.his.Beans;

import java.io.Serializable;

public class TBaseBean implements Serializable
{
    /* 使用次数 */
    private long usrCount = 0l ;
    /* 创建时间  */
    private String createDate = "";
    /* 最后一次使用时间   */
    private String lastUseDate = "";
    
    public long getUsrCount()
    {
        return usrCount;
    }
    /**
     * 添加次数
     */
    public void addUsrCount()
    {
        this.usrCount++;
    }
    public String getCreateDate()
    {
        return createDate;
    }
    public void setCreateDate(String createDate)
    {
        this.addUsrCount();
        this.createDate = createDate;
    }
    public String getLastUseDate()
    {
        return lastUseDate;
    }
    public void setLastUseDate(String lastUseDate)
    {
        this.addUsrCount();
        this.lastUseDate = lastUseDate;
    }
}
