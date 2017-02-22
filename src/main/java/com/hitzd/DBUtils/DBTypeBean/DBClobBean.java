package com.hitzd.DBUtils.DBTypeBean;

/**
 * 数据处理Clob 
 * @author jingcong
 *
 */
public class DBClobBean
{
    private String valueClob;
    
    private long valueLength;
    
    public DBClobBean(String value)
    {
        if(value == null) throw new RuntimeException("DBClobBean 参数为空! ");
        this.valueClob =  value;
        this.valueLength = value.length();
    }

    public String getValueClob()
    {
        return valueClob;
    }

    public void setValueClob(String valueClob)
    {
        this.valueClob = valueClob;
        this.valueLength = valueClob.length();
    }

    public long getValueLength()
    {
        return valueLength;
    }
    
    
}
