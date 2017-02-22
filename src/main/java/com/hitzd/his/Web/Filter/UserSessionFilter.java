package com.hitzd.his.Web.Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitzd.his.Beans.frame.User;
import com.hitzd.his.Program.Web.Utils.CommonUtils;
import com.hitzd.his.sso.SSOController;

/**
 *  拦截session 中是否存在
 */
public class UserSessionFilter implements Filter {

    public UserSessionFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
	    HttpServletRequest req = (HttpServletRequest)request;
	    User user = SSOController.getUser(req);
	    if(user != null)
	    {
	        chain.doFilter(req, response); 
	        return;
	    }
	    ((HttpServletResponse)response).sendRedirect(req.getContextPath() + "/index.jsp");
	}
	public void init(FilterConfig fConfig) throws ServletException {}

}
