package com.hitzd.his.Beans;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.hitzd.Security.SecUtils;
import com.hitzd.his.Beans.BaseBean;

public class BaseBean 
{
	private Map<String, String> attributes = new HashMap<String, String>();
	private String TableName = "";
	private static Properties SecMap = new Properties();
	private static Properties tblKey = new Properties();
	static
	{
		try {
			SecMap.load(BaseBean.class.getResourceAsStream("/secMap.properties"));
			tblKey.load(BaseBean.class.getResourceAsStream("/tblKey.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setTableName(String tblName)
	{
		TableName = tblName;
	}
	
	public void set(String key, String value)
	{
		attributes.put(key, onSetValue(key, value));
	}
	
	public String onSetValue(String key, String value)
	{
		String fieldX = SecMap.getProperty(TableName);
		String[] fields = fieldX.split(",");
		for (int i = 0; i < fields.length; i++)
		{
			if (fields[i].equalsIgnoreCase(key))
				return SecUtils.Encode(value);
		}
		return value;
	}
	
	public String get(String key)
	{
		return attributes.get(key);
	}
	
    @Override
    public int hashCode()
    {
    	String[] keys = tblKey.getProperty(TableName).split(",");
        final int prime = 31;
        int result = 1;
        for (int i = 0; i < keys.length; i++)
        	result = prime * result
                + ((get(keys[i]) == null) ? 0 : get(keys[i]).hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseBean other = (BaseBean)obj;
    	String[] keys = tblKey.getProperty(TableName).split(",");
        for (int i = 0; i < keys.length; i++)
        {
	        if (get(keys[i]) == null)
	        {
	            if (other.get(keys[i]) != null)
	                return false;
	        }
	        else if (!get(keys[i]).equals(other.get(keys[i])))
	            return false;
        }
        return true;
    }
}