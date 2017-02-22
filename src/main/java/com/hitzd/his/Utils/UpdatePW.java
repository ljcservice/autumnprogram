package com.hitzd.his.Utils;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Service.hibernateService.IHiberBiz;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.hitzd.his.Web.base.PubServlet;


/**
 * 
 *
 */

@Component("UpdatePW")
public class UpdatePW extends PubServlet
{
	private static final long serialVersionUID = 1L;
	@Resource(name="hiberBiz")
	private IHiberBiz hiberBiz;
	@Override
	public List<TCommonRecord> option(String o, HttpServletRequest request,HttpServletResponse response)
	{
	    if("UserCusUpdate".equals(o))
		{
			try
			{
			    String userId   = CommonUtils.getRequestParameter(request, "userId", "");
	            String userName = CommonUtils.getRequestParameter(request, "username", "");
	            String pwd      = CommonUtils.getRequestParameter(request, "pwd", "");
	            StringBuffer sql  = new StringBuffer();
	            sql.append(" update jill_userdef set username = '").append(userName);
	            sql.append("'").append(",userpwd = '").append(pwd).append("' ");
	            sql.append(" where userid='").append(userId).append("'");
	            hiberBiz.update(sql.toString());
	            request.setAttribute("userUpdate", "true");
			}
			catch(Exception e )
			{
			    request.setAttribute("userUpdate", "false");
			    e.printStackTrace();
			}
			this.forword = "/WebPage/user/userCusUpdate.jsp";
		}
	    return null;
	}
}
