package com.hitzd.DBUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.*;

/**
 * 通用数据集   
 * @author Administrator
 *
 */
public class TCommonRecord implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6805580253508412294L;
	private Map<String, String> attributes = new HashMap<String, String>();
	private Map<String, Object> objects = new HashMap<String, Object>();
	
	public void setObj(String key, Object obj)
	{
		objects.put(key.toUpperCase(), obj);
	}
	
	public Object getObj(String key)
	{
		return objects.get(key.toUpperCase());
	}
	
	public void set(String key, String value)
	{
		attributes.put(key.toUpperCase(), value);
	}
	
	
	public List<String> getKeys()
	{
		List<String> res = new ArrayList<String>();
		for (String key: attributes.keySet())
			res.add(key);
		return res;
	}
	
	public String get(String key  )
	{
	    String ret = attributes.get(key.toUpperCase());
		return ret == null ? "" : ret; //.replaceAll("'", "''");
	}
	
	/**
	 * 要处理单引号的情况
	 * @param key
	 * @param singleq true : 要处理单引号，false ：不处理；
	 * @return
	 */
	public String get(String key , boolean singleq)
	{
	    if(!singleq) return get(key);
	    String ret = attributes.get(key.toUpperCase());
	    return ret == null ? "" : ret.replaceAll("'", "''");
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(String key)
	{
	    String value = get(key);
        if ((value == null) || (value.length() == 0)) value = "0";
        try
        {
            return Long.parseLong(value);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            return 0;
        }
	}
	
	public int getInt(String key)
	{
		String value = get(key);
		if ((value == null) || (value.length() == 0)) value = "0";
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			return 0;
		}
	}
	
	public double getDouble(String key)
	{
		String value = get(key);
		if (value == null || "".equals(value)) value = "0.0";
		try
		{
			return Double.parseDouble(value);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return 0.0;
		}
	}
	/**
	 * 将指定字符字符替换为空字符串。  
	 * @param key
	 * @param str
	 * @return
	 */
	public String get(String key,String[] str)
	{
	    String ret = attributes.get(key.toUpperCase());
	    ret = ret == null ? "" : ret;
	    String result = null;
	    for(String s : str)
	    {
	        result = ret.replace(s, "");
	    }
        return  result;
	}
	
	
	
	
	/**
	 * 将指定字符字符替换为空字符串。 
	 * @param key
	 * @param strs
	 * @return
	 */
	public String get(String key , String strs)
	{
	    return get(key,new String[]{strs});
	}
	/**
	 * 得到YYYY-MM-DD格式的字符串
	 * @param key
	 * @return
	 */
	public String getDateString(String key){
		String value = get(key);
		value = value.length()>10?value.substring(0,10):value;
		return value;
	}
	/**
	 * 得到YYYY-MM-DD HI24:MI:SS格式的字符串
	 * @param key
	 * @return
	 */
	public String getDateTimeString(String key){
		String value = get(key);
		value = value.length()>19?value.substring(0,19):value;
		return value;
	}
	
	/**
	 * 复制对象
	 * @return
	 */
    public Object deepClone() 
    {
        try
        {
         // 将对象写到流里
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(this);
            // 从流里读出来
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (oi.readObject());
        }
        catch(Exception t)
        {
            t.printStackTrace();
        }
        return this;
    }

}
