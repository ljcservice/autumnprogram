package com.ts.controller.matcher;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.hitzd.his.Web.base.PubServlet;
import com.ts.service.matcher.ICommonOperService;

/**
 * 修改密码
 * @author Crystal
 */
@Controller("UserInfo")
public class UserInfo extends PubServlet
{
	private static final long serialVersionUID = 1L;
	@Resource(name = "userInfoServiceImpl")
	private ICommonOperService userInfoServiceImpl;

	protected List<TCommonRecord> updPwd(HttpServletRequest request, HttpServletResponse response)
	{
		TCommonRecord params = new TCommonRecord();
		params.set("USERID", CommonUtils.getRequestParameter(request, "USERID", ""));
		params.set("PWD", CommonUtils.getRequestParameter(request, "PWD", ""));
		int resultNum = (Integer) userInfoServiceImpl.update(params);
		if (resultNum > 0)
			request.setAttribute("updateInfo", "ok");
		return null;
	}
	
	@Override
	protected List<TCommonRecord> option(String o, HttpServletRequest request, HttpServletResponse response)
	{
		if ("updPwd".equals(o))
			return updPwd(request, response);
		return null;
	}
}