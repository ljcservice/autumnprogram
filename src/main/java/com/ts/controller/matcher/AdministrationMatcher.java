package com.ts.controller.matcher;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.hitzd.his.Web.base.PubServlet;
import com.ts.service.matcher.IDataMatcherService;

/**
 * 给药途径配码
 * @author Crystal
 */
@Controller("AdministrationMatcher")
public class AdministrationMatcher extends PubServlet
{
	private static final long serialVersionUID = 1L;
	@Resource(name = "adminstrationMatcherServiceImpl")
	private IDataMatcherService adminMatcherServiceImpl;
	private int administration_map_id = 0;

	@Override
	protected List<TCommonRecord> query(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/AdministrationMatcher/Matcher_Main.jsp";
		TCommonRecord params = new TCommonRecord();
		params.set("page", CommonUtils.getRequestParameter(request, "page", "0"));
		String q_administration_name = CommonUtils.getRequestParameter(request, "q_administration_name", "").replace("(","_").replace("（", "_").replace(")","_").replace("）","_");
		params.set("q_administration_name", q_administration_name);
		request.setAttribute("pageView", this.adminMatcherServiceImpl.queryHISForList(params));
		request.setAttribute("adminMap", this.adminMatcherServiceImpl.queryPDSSMapedForMap(params));
		return null;
	}
	
	@Override
	protected List<TCommonRecord> modify(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/AdministrationMatcher/Matcher_Detail.jsp";
		TCommonRecord params = new TCommonRecord();
		String administration_name = "";
		String administration_name_his = CommonUtils.getRequestParameter(request, "administration_name_his", "");
		if ("".equals(administration_name_his))
		{
			try
			{
				administration_name = new String(CommonUtils.getRequestParameter(request, "administration_name", "").getBytes("ISO-8859-1"), "GBK");
				params.set("administration_name", administration_name);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		else
			params.set("administration_name", administration_name_his);
		if (this.administration_map_id > 0)
		{
			params.set("administration_map_id", this.administration_map_id + "");
			this.administration_map_id = 0;
		}
		else
		{
			params.set("administration_map_id", CommonUtils.getRequestParameter(request, "administration_map_id", ""));
		}
		request.setAttribute("hisEntity", this.adminMatcherServiceImpl.queryHISForObject(params));
		request.setAttribute("mapEntity", this.adminMatcherServiceImpl.queryPDSSMapForObject(params));
		return null;
	}

	@Override
	protected List<TCommonRecord> update(HttpServletRequest request, HttpServletResponse response)
	{
		TCommonRecord params = new TCommonRecord();
		params.set("administration_name", CommonUtils.getRequestParameter(request, "administration_name", ""));
		params.set("administration_id", CommonUtils.getRequestParameter(request, "administration_id", ""));
		params.set("oper_user", CommonUtils.getSessionUser(request).getUserName());
		params.set("administration_map_id", CommonUtils.getRequestParameter(request, "administration_map_id", ""));
		params.set("serial_no", CommonUtils.getRequestParameter(request, "serial_no", ""));
		params.set("administration_no_local", CommonUtils.getRequestParameter(request, "administration_no_local", ""));
		params.set("administration_name_local", CommonUtils.getRequestParameter(request, "administration_name_local", ""));
		params.set("input_code", CommonUtils.getRequestParameter(request, "input_code", ""));
		params.set("is_injection", CommonUtils.getRequestParameter(request, "is_injection", ""));
		this.administration_map_id = this.adminMatcherServiceImpl.save(params);
		return modify(request, response);
	}

	protected List<TCommonRecord> queryPDSSList(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/AdministrationMatcher/Dict_Query.jsp";
		TCommonRecord params = new TCommonRecord();
		params.set("page", CommonUtils.getRequestParameter(request, "page", "0"));
		params.set("q_std_route_id", CommonUtils.getRequestParameter(request, "q_std_route_id", ""));
		params.set("q_item_name", CommonUtils.getRequestParameter(request, "q_item_name", ""));
		if (!"".equals(params.get("q_std_route_id")) || !"".equals(params.get("q_item_name")))
			request.setAttribute("pageView", this.adminMatcherServiceImpl.queryPDSSDetailForList(params));
		return this.adminMatcherServiceImpl.queryPDSSMainForList(params);
	}
	
	@Override
	protected List<TCommonRecord> option(String o, HttpServletRequest request, HttpServletResponse response)
	{
		if ("queryPDSSList".equals(o))
			return queryPDSSList(request, response);
		return null;
	}
}