package com.hitzd.his.Web.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hitzd.his.Web.Utils.CommonUtils;
import com.hitzd.his.sso.SSOController;

public class PHUtils extends HttpServlet
{

	private static final long serialVersionUID = 5283358021059901547L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		this.doPost(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String patient_id = CommonUtils.getRequestParameter(request, "patient_id", "");
		String visit_id   = CommonUtils.getRequestParameter(request, "visit_id", "");
		HttpSession session = request.getSession();
		String Token = (String)session.getAttribute(SSOController.Token);
		PrintWriter out = response.getWriter();
		out.print("<html>");
		if(!"".equals(visit_id))
		{
			out.print("<form name=\"xxxx\" method=\"post\"  action=\"/HIS_EPH/control/Pat_visitInfo\">");
			out.print("<input type=\"hidden\" name=\"o\" value=\"main\"/>");
		}
		else
		{
			out.print("<form name=\"xxxx\" method=\"post\"  action=\"/HIS_EPH/control/PatientInfo\">");
			out.print("<input type=\"hidden\" name=\"o\" value=\"query\"/>");
		}
		out.print("<input type=\"hidden\" name=\"patient_id\"  value=\"" + patient_id + "\"  />");
		out.print("<input type=\"hidden\" name=\"visit_id\"    value=\"" + visit_id   + "\"   />");
		out.print("<input type=\"hidden\" name=\"token\"       value=\"" + Token      +"\"   />");
		out.print("</form>");
		out.print("<script language=\"javascript\">");
		out.print("document.xxxx.submit();");
		out.print("</script>");
		out.print("</html>");
	}
	
}
