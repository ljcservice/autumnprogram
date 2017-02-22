package com.hitzd.his.report.utils;

import java.util.*;

/**
 * 
 * @author jingcong
 *
 */
public class JillPage implements IPage 
{
    protected HashMap<String, String[]> parameters = null;
    protected HashMap<String, Object> attributes = null;
    protected HashMap<String, String> informations = null;
    
    public String getRealPath(String path)
    {
    	return "";
    }
    
    public void addInformation(String name, String values)
    {
        synchronized (informations) 
        {
        	informations.put(name, values);
        }
    }
    
    public Enumeration<String> getInformationNames()
    {
    	return (new Enumerator(informations.keySet()));
    }
    
    public String getInformation(String name)
    {
        String values = (String)informations.get(name);
        if (values != null)
            return (values);
        else
            return (null);
    }

    public void clearInformations()
    {
    	informations.clear();
    }
    
    public JillPage()
    {
    	parameters   = new HashMap<String, String[]>();
    	attributes   = new HashMap<String, Object>();
    	informations = new HashMap<String, String>();
    }

    public void addParameter(String name, String value)
    {
    	String[] values = {value};
    	addParameter(name, values);
    }
    
    public void addParameter(String name, String[] values)
    {
        synchronized (parameters) 
        {
            parameters.put(name, values);
        }
    }
    
    public String getParameter(String name)
    {
        String[] values = (String[])parameters.get(name);
        if (values != null)
            return (values[0]);
        else
            return (null);
    }
    
    public String getParameter(String name, String defValue)
    {
    	String value = getParameter(name);
    	if (value == null || "".equals(value))
    	{
    	    if (!"".equals(defValue)) return defValue;
    		return "";
    	}
    	return value;
    }
    
    public String getParameterIgnoreCase(String name, String defValue)
    {
    	Enumeration<String> enu = getParameterNames();
    	while (enu.hasMoreElements())
    	{
    		String XName = (String)enu.nextElement();
    		if (name.equalsIgnoreCase(XName))
    			return getParameter(XName, defValue);
    	}
    	return defValue;
    }
    
    
    public String[] getParameterValues(String name) 
    {
        String values[] = (String[]) parameters.get(name);
        if (values != null)
            return (values);
        else
            return (null);
    }
    
    public Enumeration<String> getParameterNames() 
    {
    	return (new Enumerator(parameters.keySet()));
    }

    
    public void clearParameters()
    {
    	parameters.clear();
    }
    
    public void setAttribute(String name, Object values)
    {
    	attributes.put(name, values);
    }
    
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }
    
    public void clearAttributes()
    {
    	attributes.clear();
    }
    
    public Enumeration<String> getAttributeNames()
    {
    	return (new Enumerator(attributes.keySet()));
    }
}
