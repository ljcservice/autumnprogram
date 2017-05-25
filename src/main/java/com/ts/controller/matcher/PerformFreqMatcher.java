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
 * 给药频次配码
 * @author Crystal
 */
@Controller("PerformFreqMatcher")
public class PerformFreqMatcher extends PubServlet
{
	private static final long serialVersionUID = 1L;
	@Resource(name = "performFreqMatcherServiceImpl")
	private IDataMatcherService performFreqServiceImpl;
	private int perform_freq_dict_id = 0;

	@Override
	protected List<TCommonRecord> query(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/PerformFreqMatcher/Matcher_Main.jsp";
		TCommonRecord params = new TCommonRecord();
		params.set("page", CommonUtils.getRequestParameter(request, "page", "0"));
		String q_freq_desc = CommonUtils.getRequestParameter(request, "q_freq_desc", "").replace("(","_").replace("（", "_").replace(")","_").replace("）","_");
		params.set("q_freq_desc", q_freq_desc);
		request.setAttribute("pageView", this.performFreqServiceImpl.queryHISForList(params));
		request.setAttribute("performMap", this.performFreqServiceImpl.queryPDSSMapedForMap(params));
		return null;
	}
	
	@Override
	protected List<TCommonRecord> modify(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/PerformFreqMatcher/Matcher_Detail.jsp";
		TCommonRecord params = new TCommonRecord();
		String perform_freq_dict_name = "";
		String freq_desc = CommonUtils.getRequestParameter(request, "freq_desc", "");
		if ("".equals(freq_desc))
		{
			try
			{
				perform_freq_dict_name = new String(CommonUtils.getRequestParameter(request, "perform_freq_dict_name", "").getBytes("ISO-8859-1"), "GBK");
				params.set("perform_freq_dict_name", perform_freq_dict_name);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		else
			params.set("perform_freq_dict_name", freq_desc);
		if (this.perform_freq_dict_id > 0)
		{
			params.set("perform_freq_dict_map_id", this.perform_freq_dict_id + "");
			this.perform_freq_dict_id = 0;
		}
		else
		{
			params.set("perform_freq_dict_map_id", CommonUtils.getRequestParameter(request, "perform_freq_dict_map_id", ""));
		}
		request.setAttribute("hisEntity", this.performFreqServiceImpl.queryHISForObject(params));
		request.setAttribute("mapEntity", this.performFreqServiceImpl.queryPDSSMapForObject(params));
		return null;
	}

	@Override
	protected List<TCommonRecord> update(HttpServletRequest request, HttpServletResponse response)
	{
		TCommonRecord params = new TCommonRecord();
		params.set("perform_freq_dict_no_local", CommonUtils.getRequestParameter(request, "perform_freq_dict_no_local", ""));
		params.set("perform_freq_dict_name_local", CommonUtils.getRequestParameter(request, "perform_freq_dict_name_local", ""));
		params.set("perform_freq_dict_name", CommonUtils.getRequestParameter(request, "perform_freq_dict_name", ""));
		params.set("oper_user", CommonUtils.getSessionUser(request).getUserName());
		params.set("perform_freq_dict_map_id", CommonUtils.getRequestParameter(request, "perform_freq_dict_map_id", ""));
		params.set("perform_freq_dict_id", CommonUtils.getRequestParameter(request, "perform_freq_dict_id", ""));
		params.set("serial_no", CommonUtils.getRequestParameter(request, "serial_no", ""));
		params.set("freq_desc", CommonUtils.getRequestParameter(request, "freq_desc", ""));
		params.set("freq_counter", CommonUtils.getRequestParameter(request, "freq_counter", ""));
		params.set("freq_interval", CommonUtils.getRequestParameter(request, "freq_interval", ""));
		params.set("freq_interval_units", CommonUtils.getRequestParameter(request, "freq_interval_units", ""));
		params.set("freq_counter_pdss", CommonUtils.getRequestParameter(request, "freq_counter_pdss", ""));
		params.set("freq_interval_pdss", CommonUtils.getRequestParameter(request, "freq_interval_pdss", ""));
		params.set("freq_interval_units_pdss", CommonUtils.getRequestParameter(request, "freq_interval_units_pdss", ""));
		params.set("frequency_pdss", CommonUtils.getRequestParameter(request, "frequency_pdss", ""));
		this.perform_freq_dict_id = this.performFreqServiceImpl.save(params);
		return modify(request, response);
	}

	protected List<TCommonRecord> queryPDSSList(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/PerformFreqMatcher/Dict_Query.jsp";
		TCommonRecord params = new TCommonRecord();
		params.set("page", CommonUtils.getRequestParameter(request, "page", "0"));
		params.set("q_perform_freq_dict_name", CommonUtils.getRequestParameter(request, "q_perform_freq_dict_name", ""));
		request.setAttribute("pageView", this.performFreqServiceImpl.queryPDSSDetailForList(params));
		return null;
	}
	
	@Override
	protected List<TCommonRecord> option(String o, HttpServletRequest request, HttpServletResponse response)
	{
		if ("queryPDSSList".equals(o))
			return queryPDSSList(request, response);
		return null;
	}
}