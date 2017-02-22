package com.hitzd.his.Program.Web.Utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;





import com.hitzd.his.Beans.frame.User;
import com.hitzd.his.sso.SSOController;

/**
 * 共通的方法
 * @author liujc
 *
 */
public final class CommonUtils
{
    private CommonUtils() {}
    private static String WebExcelKey = "ExpExcelKey_"; 
    /**
     * session取得数据集 
     * @param request
     * @param key
     * @return 
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getSessionObject(HttpServletRequest request,String key)
    {
        return (List<Object>)request.getSession().getAttribute(WebExcelKey + key);
    }
    
    /**
     * 删除导出的结果集
     * @param request
     * @param key
     */
    public static void delSessionObject(HttpServletRequest request , String key )
    {
        Enumeration<Object> ER = request.getSession().getAttributeNames();
        while(ER.hasMoreElements())
        {
            String keyName = (String)ER.nextElement();
            if(keyName.indexOf(WebExcelKey) != -1)
            {
                request.getSession().removeAttribute(keyName);
            }
        }
    }
    
    /** session 中放 数据集 
     * @param request
     * @param obj
     */
    public static void setSessionObject(HttpServletRequest request,String key ,Object... obj)
    {
        List<Object>  list = new ArrayList<Object>();
        for(Object o : obj)
        {
            list.add(o);
        }
        delSessionObject(request,key);
        request.getSession().setAttribute(WebExcelKey + key, list);
    }
    
    /**
     * 设置 项目路径  
     */
    public static String  getWebRoot(HttpServletRequest request) {
        return request.getContextPath();
    }
    
    /**
     * 得到session中的用户信息
     * @param request
     * @return
     */
    public static User getSessionUser(HttpServletRequest request)
    {
        Object user = SSOController.getUser(request);
        
        return user == null ? null:(User)user;
    }

    /**
     *  得到request参数值
     * @param request
     * @param parameterName
     * @param defual
     * @return
     */
    public static String getRequestParameter(HttpServletRequest request,String parameterName,String defual)
    {
        if("".equals(parameterName))
            new RuntimeException("参数名字不能为空");
        return request.getParameter(parameterName)!= null?request.getParameter(parameterName).trim(): defual;
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
    public static double div(double v1, double v2, int scale)
    {
       if (scale < 0) {
        throw new IllegalArgumentException(
          "The scale must be a positive integer or zero");
       }
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
