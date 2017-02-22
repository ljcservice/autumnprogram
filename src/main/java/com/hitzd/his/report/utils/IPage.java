package com.hitzd.his.report.utils;

import java.util.*;

/**
 *  策略 Web 参数 原型接口 Page 
 * @author jingcong
 *
 */
public interface IPage 
{
    public void addParameter(String name, String[] values);
    public void addParameter(String name, String value);
    public String getParameter(String name);
    public String getParameter(String name, String defValue);
    public String getParameterIgnoreCase(String name, String defValue);
    public void clearParameters();
    public Enumeration<String> getParameterNames();
    public String[] getParameterValues(String name);
    
    public void setAttribute(String name, Object values);
    public Object getAttribute(String name);
    public void clearAttributes();
    public Enumeration<String> getAttributeNames();
    
    public void addInformation(String name, String values);
    public String getInformation(String name);
    public void clearInformations();
    public Enumeration<String> getInformationNames();
    
    public String getRealPath(String path);
}
