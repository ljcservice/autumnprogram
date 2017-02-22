package com.hitzd.his.report.utils;

import javax.servlet.http.*;
import java.util.*;

/**
 * 获得Request 中的信息
 * @author jingcong
 *
 */
public class Http2PageAgent 
{
	public static IPage genPage(HttpServletRequest request,HttpServletResponse response)
	{
		JillPage jp = new JillPage();
		Enumeration<?> enu = request.getParameterNames();
		while (enu.hasMoreElements())
		{
			String name = (String)enu.nextElement();
			String[] values = (String[])request.getParameterValues(name); 
			jp.addParameter(name, values);
		}
		
		enu = request.getAttributeNames();
		while (enu.hasMoreElements())
		{
			String name = (String)enu.nextElement();
			Object value = request.getAttribute(name);
			jp.setAttribute(name, value);
		}
		jp.setAttribute("Session",  request.getSession());
		jp.setAttribute("Request",  request);
		jp.setAttribute("Response", response);
		
		
		jp.addInformation("HTTPAuthType",          request.getAuthType()             );
		jp.addInformation("HTTPCharacterEncoding", request.getCharacterEncoding()    );
		jp.addInformation("HTTPContentType",       request.getContentType()          );
		jp.addInformation("HTTPContextPath",       request.getContextPath()          );
		jp.addInformation("HTTPMethod",            request.getMethod()               );
		jp.addInformation("HTTPPathInfo",          request.getPathInfo()             );
		jp.addInformation("HTTPPathTranslated",    request.getPathTranslated()       );
		jp.addInformation("HTTPProtocol",          request.getProtocol()             );
		jp.addInformation("HTTPQueryString",       request.getQueryString()          );
		jp.addInformation("HTTPRemoteAddr",        request.getRemoteAddr()           );
		jp.addInformation("HTTPRemoteHost",        request.getRemoteHost()           );
		jp.addInformation("HTTPRemoteUser",        request.getRemoteUser()           );
		jp.addInformation("HTTPSessionId",         request.getRequestedSessionId()   );
		jp.addInformation("HTTPRequestURI",        request.getRequestURI()           );
		jp.addInformation("HTTPRequestURL",        request.getRequestURL().toString());
		jp.addInformation("HTTPScheme",            request.getScheme()               );
		jp.addInformation("HTTPServerName",        request.getServerName()           );
		jp.addInformation("HTTPServerPort",        request.getServerPort() + ""      );
		jp.addInformation("HTTPServletPath",       request.getServletPath()          );
		return jp;
	}
}
