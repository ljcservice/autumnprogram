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
 * 诊断配码
 * @author Crystal
 */
@Controller("DiagnosisMatcher")
public class DiagnosisMatcher extends PubServlet
{
	private static final long serialVersionUID = 1L;
	@Resource(name = "diagnosisMatcherServiceImpl")
	private IDataMatcherService diagnosisMatcherServiceImpl;
	private int diagnosis_map_id = 0;

	@Override
	protected List<TCommonRecord> query(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/DiagnosisMatcher/Matcher_Main.jsp";
		TCommonRecord params = new TCommonRecord();
		params.set("page", CommonUtils.getRequestParameter(request, "page", "0"));
		params.set("q_diagnosis_code", CommonUtils.getRequestParameter(request, "q_diagnosis_code", ""));
		String q_diagnosis_name = CommonUtils.getRequestParameter(request, "q_diagnosis_name", "").replace("(","_").replace("（", "_").replace(")","_").replace("）","_");
		params.set("q_diagnosis_name", q_diagnosis_name);
		request.setAttribute("pageView", this.diagnosisMatcherServiceImpl.queryHISForList(params));
		request.setAttribute("diagnosisMap", this.diagnosisMatcherServiceImpl.queryPDSSMapedForMap(params));
		return null;
	}
	
	@Override
	protected List<TCommonRecord> modify(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/DiagnosisMatcher/Matcher_Detail.jsp";
		TCommonRecord params = new TCommonRecord();
		String diagnosis_name = "";
		String diagnosis_name_his = CommonUtils.getRequestParameter(request, "diagnosis_name_his", "");
		if ("".equals(diagnosis_name_his))
		{
			try
			{
				diagnosis_name = new String(CommonUtils.getRequestParameter(request, "diagnosis_name", "").getBytes("ISO-8859-1"), "GBK");
				params.set("diagnosis_name", diagnosis_name);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		else
			params.set("diagnosis_name", diagnosis_name_his);
		if (this.diagnosis_map_id > 0)
		{
			params.set("diagnosis_map_id", this.diagnosis_map_id + "");
			this.diagnosis_map_id = 0;
		}
		else
		{
			params.set("diagnosis_map_id", CommonUtils.getRequestParameter(request, "diagnosis_map_id", ""));
		}
		request.setAttribute("hisEntity", this.diagnosisMatcherServiceImpl.queryHISForObject(params));
		request.setAttribute("mapEntity", this.diagnosisMatcherServiceImpl.queryPDSSMapForObject(params));
		return null;
	}

	@Override
	protected List<TCommonRecord> update(HttpServletRequest request, HttpServletResponse response)
	{
		TCommonRecord params = new TCommonRecord();
		params.set("diagnosis_map_id", CommonUtils.getRequestParameter(request, "diagnosis_map_id", ""));
		params.set("diagnosis_no_local", CommonUtils.getRequestParameter(request, "diagnosis_no_local", ""));
		params.set("diagnosis_name_local", CommonUtils.getRequestParameter(request, "diagnosis_name_local", ""));
		params.set("std_indicator", CommonUtils.getRequestParameter(request, "std_indicator", ""));
		params.set("approved_indicator", CommonUtils.getRequestParameter(request, "approved_indicator", ""));
		params.set("input_code", CommonUtils.getRequestParameter(request, "input_code", ""));
		params.set("infect_indicator", CommonUtils.getRequestParameter(request, "infect_indicator", ""));
		params.set("health_level", CommonUtils.getRequestParameter(request, "health_level", ""));
		params.set("outp_drug_day", CommonUtils.getRequestParameter(request, "outp_drug_day", ""));
		params.set("diagnosis_id", CommonUtils.getRequestParameter(request, "diagnosis_id", ""));
		params.set("diagnosis_code", CommonUtils.getRequestParameter(request, "diagnosis_code", ""));
		params.set("diagnosis_code2", CommonUtils.getRequestParameter(request, "diagnosis_code2", ""));
		params.set("diagnosis_name", CommonUtils.getRequestParameter(request, "diagnosis_name", ""));
		params.set("acute_indi", CommonUtils.getRequestParameter(request, "acute_indi", ""));
		params.set("side_indi", CommonUtils.getRequestParameter(request, "side_indi", ""));
		params.set("diag_indi", CommonUtils.getRequestParameter(request, "diag_indi", ""));
		params.set("diagnosis_class", CommonUtils.getRequestParameter(request, "diagnosis_class", ""));
		params.set("diagnosis_class_code", CommonUtils.getRequestParameter(request, "diagnosis_class_code", ""));
		params.set("renal_indi", CommonUtils.getRequestParameter(request, "renal_indi", ""));
		params.set("hepatic_indi", CommonUtils.getRequestParameter(request, "hepatic_indi", ""));
		params.set("cardio_idi", CommonUtils.getRequestParameter(request, "cardio_idi", ""));
		params.set("pulm_indi", CommonUtils.getRequestParameter(request, "pulm_indi", ""));
		params.set("neur_indi", CommonUtils.getRequestParameter(request, "neur_indi", ""));
		params.set("endo_indi", CommonUtils.getRequestParameter(request, "endo_indi", ""));
		params.set("icd9", CommonUtils.getRequestParameter(request, "icd9", ""));
		params.set("oper_user", CommonUtils.getSessionUser(request).getUserName());
		this.diagnosis_map_id = this.diagnosisMatcherServiceImpl.save(params);
		return modify(request, response);
	}

	protected List<TCommonRecord> queryPDSSList(HttpServletRequest request, HttpServletResponse response)
	{
		this.forword = "/WebPage/DiagnosisMatcher/Dict_Query.jsp";
		TCommonRecord params = new TCommonRecord();
		params.set("page", CommonUtils.getRequestParameter(request, "page", "0"));
		params.set("q_diagnosis_code", CommonUtils.getRequestParameter(request, "q_diagnosis_code", ""));
		params.set("q_diagnosis_name", CommonUtils.getRequestParameter(request, "q_diagnosis_name", ""));
		request.setAttribute("pageView", this.diagnosisMatcherServiceImpl.queryPDSSDetailForList(params));
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