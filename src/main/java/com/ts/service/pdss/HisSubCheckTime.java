package com.ts.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 暂时记录每个单项审查的时间 
 * @author Administrator
 *
 */
public class HisSubCheckTime
{
    private static Map<String, String> SubCheckTime = new HashMap<String, String>();
    
    /**
     * 添加单项审查信息
     * @param CheckTimeInfo
     */
    public static void setSubCheckTime(String CheckTimeInfo)
    {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    	if(request != null)
    	{
	        String ip      = request.getRemoteAddr();
	        if(ip != null)
	        {
	            String content = SubCheckTime.get(ip);
	            content += "; " + CheckTimeInfo;
	            SubCheckTime.put(ip,content);
	        }
    	}
    }
    
    /**
     * 获得单项审查的所有信息
     * @return
     */
    public static String getSubCheckTimeInfo()
    
    {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String ip      = request.getRemoteAddr();
        String content = SubCheckTime.get(ip);
        SubCheckTime.put(ip,"");
        return content;
    }
}
