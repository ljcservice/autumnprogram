package com.hitzd.his.report.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Helper.CommonRecordHelper;
import com.hitzd.his.Beans.frame.User;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.hitzd.his.report.utils.Http2PageAgent;
import com.hitzd.his.report.utils.IPage;
import com.hitzd.his.report.utils.ReportHandler;
import com.hitzd.his.report.utils.ReportHandlerFactory;
import com.hitzd.his.sso.SSOController;
import com.hitzd.his.tree.TreeNode;

public class ReportFilter implements Filter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -148967369989244865L;
	private String ReportEntry = ""; 

	protected TCommonRecord getMenuInfo(String MenuID)
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
		try
		{
	        String sql = "select * from Jill_MenuDef where MenuID = '" + MenuID + "'";
			@SuppressWarnings("unchecked")
			List<TCommonRecord> crs = query.query(sql, new CommonMapper());
	        if (crs.size() == 0)
	        	return null;
	        return crs.get(0);
		}
		finally
		{
			query = null;
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void parseMenuParam(IPage page, String MenuParam)
	{
		if ((MenuParam == null) || (MenuParam.length() == 0))
			return;
		String[] Params = MenuParam.split("&");
		for (String param: Params)
		{
			String[] Values = param.split("=");
			if (Values.length == 2)
			{
				if (page.getParameter(Values[0]) == null)
					page.addParameter(Values[0], Values[1]);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain arg2) throws IOException, ServletException 
	{
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		/*存放具体引用菜单之前的菜单id*/
		String treeNodeMenuId = CommonUtils.getRequestParameter(request, TreeNode.URLMenuID, "");
        if("".equals(treeNodeMenuId)) treeNodeMenuId = (String)request.getSession().getAttribute(TreeNode.URLMenuID);
        request.getSession().setAttribute(TreeNode.URLMenuID, treeNodeMenuId);
		User user = SSOController.getUser((HttpServletRequest)req);
		if (user == null)
		{
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}
		String[] URIs = request.getRequestURI().split("/");
		if (URIs.length <= 2)
		{
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}
		String MenuID = URIs[URIs.length - 1];
		TCommonRecord MenuRec = getMenuInfo(MenuID);
		if (MenuRec == null)
		{
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}
		TCommonRecord crRef = null;
		String MenuUrl      = MenuRec.get("MenuUrl");
		String clazz        = MenuRec.get("MenuHandler");
		if (MenuRec.get("MenuRefID").length() > 0)
		{
			crRef = getMenuInfo(MenuRec.get("MenuRefID"));
			MenuUrl = crRef.get("MenuUrl");
			if (MenuUrl.length() == 0)
				MenuUrl = "/WebPage/reportView/" + crRef.get("MenuGroup") + "/" + crRef.get("MenuParentID") + "/" + MenuRec.get("MenuRefID") + "/" + MenuRec.get("MenuRefID"); // + "_detail.jsp";
			clazz = crRef.get("MenuHandler");
		}
		else
		{
			MenuUrl = MenuRec.get("MenuUrl");
			clazz = MenuRec.get("MenuHandler");
			if (MenuUrl.length() == 0)
				MenuUrl = "/WebPage/reportView/" + MenuRec.get("MenuGroup") + "/" + MenuRec.get("MenuParentID") + "/" + MenuID + "/" + MenuID; // + "_detail.jsp";
		}
		ReportHandler rpt = ReportHandlerFactory.getInstance(clazz);
		IPage page = Http2PageAgent.genPage(request,response);
		parseMenuParam(page, MenuRec.get("MenuParam"));
		page.setAttribute("MenuID", MenuID);
		if (rpt != null)
		{
			rpt.setForword(MenuUrl);
			request.setAttribute("QueryBar_File", MenuUrl + "_qb.jsp");
			List list = rpt.doReport(page);
			request.setAttribute("Detail_File", rpt.getForword());
			if(list != null)
			{
			    request.setAttribute("List", list);
			    if (page.getParameter("AjaxFlag", "").equalsIgnoreCase("true"))
			    {
			    	response.setHeader("Cache-Control", "no-cache");
			    	response.setContentType("text/json;charset=gbk");
			    	
					String Json = CommonRecordHelper.toJson(list);
					try
					{
						response.getWriter().print(Json);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					return;
			    }
			}
			@SuppressWarnings("unchecked")
			Enumeration<String> enu = (Enumeration<String>)page.getAttributeNames();
			while (enu.hasMoreElements())
			{
				String Name = (String)enu.nextElement();
				Object Obj  = page.getAttribute(Name);
				request.setAttribute(Name, Obj);
			}
			String o = request.getParameter("o");
            String fistSelect = request.getParameter("FS");
			o = o == null ? "" : o;
	         if (o.length() == 0 || "true".equals(fistSelect))
				request.getRequestDispatcher(ReportEntry).forward(request, response);
			else
				request.getRequestDispatcher(rpt.getForword()).forward(request, response);
		}		
	}

	@Override
	public void init(FilterConfig config) throws ServletException 
	{
		ReportEntry = config.getInitParameter("ReportEntry");
		if ((ReportEntry == null) || (ReportEntry.length() == 0))
			ReportEntry = "/WebPage/reportView/reportEntry.jsp";
	}
}
