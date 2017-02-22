package com.hitzd.his.Web.Utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.hitzd.his.Beans.frame.User;
import com.hitzd.his.sso.SSOController;
import com.hitzd.his.tree.TreeNode;

/**
 * 共通的方法
 * @author liujc
 *
 */
public final class CommonUtils
{
    private CommonUtils() {}
    
    /**
     * 设置 项目路径  
     */
    public static String  getWebRoot(HttpServletRequest request) {
        return request.getContextPath();
    }
    
    
    public static String genURL(TreeNode node, HttpServletRequest request)
    {
		String action = "control";
		if (node.getMenuIsRpt().equals("1"))
			action = "report";
		String Program = request.getContextPath();
		String url = node.getMenuID();
		String param = node.getMenuParam();
		if (node.getRefMenu() != null)
		{
			Program = "/" + node.getRefMenu().getProgramID();
			url = node.getRefMenu().getMenuID();
			if (node.getRefMenu().getMenuParam().length() > 0)
				param += "&" + node.getRefMenu().getMenuParam();
			param += "&token=" + (String)request.getSession().getAttribute(SSOController.Token);
		}
		url = Program + "/" + action + "/" + url + "?o=" + param;
		return url;
    }
    /**
     * 得到session中的用户信息
     * @param request
     * @return
     */
    public static User getSessionUser(HttpServletRequest request)
    {
    	User user = SSOController.getUser(request);
        return user;
    }

    /**
     *  得到request参数值
     * @param request
     * @param parameterName
     * @param defual
     * @return
     */
    public static String getRequestParameter(HttpServletRequest request,String parameterName,String defaultValue)
    {
        if("".equals(parameterName))
            new RuntimeException("参数名字不能为空");
        return (request.getParameter(parameterName)!= null && !"".equals(request.getParameter(parameterName)))? request.getParameter(parameterName).trim(): defaultValue;
    }
    
    /**
     * 返回当先日期 
     * @return
     */
    public static String getNowDate()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
        
    }
    
    /**
    * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
    * 定精度，以后的数字四舍五入。
    * @param v1 被除数
    * @param v2 除数
    * @param scale 表示表示需要精确到小数点以后几位。
    * @return 两个参数的商
    */
    public static double div(double v1, double v2, int scale) {
       if (scale < 0) {
        throw new IllegalArgumentException(
          "The scale must be a positive integer or zero");
       }
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    } 
}
