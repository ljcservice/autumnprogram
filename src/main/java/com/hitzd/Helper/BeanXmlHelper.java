package com.hitzd.Helper;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import com.hitzd.Annotations.BeanPKProperty;

public class BeanXmlHelper<T> 
{
	/* 保存地址 */
	private static String path ; 

	static
	{
		try
		{
		    /*
			InputStream inStream = BeanXmlHelper.class.getResourceAsStream("/commonConfig.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			String myPath = prop.get("common.xmlbean").toString();*/
			File file = new File(ReadCommPropHelper.getPropertiesValue("common.xmlbean"));
			if(!file.exists())
			{
				file.mkdirs();
				throw new RuntimeException("未找到的该文件:\"" + file.getCanonicalPath() + "\"");
			}
			path = file.getCanonicalPath() + "/";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存一个bean信息为xml
	 * @param bean
	 */
	public void Save(T bean)
	{
		try 
		{
			String fileName = getPKProperty(bean);
			// Save
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(path + bean.getClass().getSimpleName() + "-" + fileName + ".xml")));
			encoder.writeObject(bean);
			encoder.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存多个bean信息为xml
	 * @param beans
	 */
	public void Save(T[] beans)
	{
		for(T bean : beans)
		{
			Save(bean);	
		}
	}
	/**
	 * 返回一个bean
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T Load(String path)
	{
		T bean = null;
		try
		{
			File file = new File(path);
			if(!file.exists())
			{
				return null;
			}
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(path)));
			bean = (T) decoder.readObject();
			decoder.close();
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 返回一个bean
	 * @param bean
	 * @return
	 */
	public T Load(T bean)
	{
		T result = null;
		try 
		{
			String fileName = getPKProperty(bean);
			// Save
			result = Load(path + bean.getClass().getSimpleName() + "-" + fileName + ".xml");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		bean = null;
		return result;
	}
	
	/**
	 *  返回多个bean 
	 * @param beans
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T[] Load(T[] beans)
	{
		List<T> list = new ArrayList<T>();
		for(T bean : beans)
		{
			T obj = Load(bean);
			if(obj != null)
			{
				list.add(obj);	
			}
				
		}
		return (T[])list.toArray();
	}
	
	/**
	 * 查找主键
	 * @param x
	 * @return
	 */
	private String getPKProperty(T x)
	{
		String name = "";
		try
		{
			BeanInfo bean = Introspector.getBeanInfo(x.getClass());
			PropertyDescriptor[]  prop =  bean.getPropertyDescriptors();	
			for(PropertyDescriptor p : prop)
			{
				Method m = p.getReadMethod();
				if(m != null)
				{
					if(m.isAnnotationPresent(BeanPKProperty.class))
					{
						name = name + p.getReadMethod().invoke(x)+ "-";
					}	
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return name;
	}
}
