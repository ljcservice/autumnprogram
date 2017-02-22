package com.hitzd.his.sso;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQuery;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Beans.frame.User;
import com.hitzd.his.Program.Web.Utils.CommonUtils;
import com.hitzd.his.Service.authority.Impl.Authorization;
import com.hitzd.his.Utils.DateUtils;

/**
 * 跨平台的控制
 * @author jingcong
 *
 */
public class SSOController 
{
	public static String Token = "Jill_SSO_Token";
	
	/**
	 * 用户获得
	 * @param request 
	 * @return
	 */
	public static User getUser(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(User.UserSessionCode);
		String token = CommonUtils.getRequestParameter(request, "token", "");
		/* 替换优先级 */
        if (token == null || "".equals(token)) 
        {
            token = (String)request.getSession().getAttribute(SSOController.Token);
        }
		if (user != null && token.equals(user.getUserToken())) return user;
		user = getUser(token, request.getContextPath());
		session.setAttribute(User.UserSessionCode, user);
		session.setAttribute(SSOController.Token, token);
		return user;
	}
	
	/**
	 * 获得用户 
	 * @param token      标记 用户跨平台 
	 * @param ProgramID  系统代码 
	 * @return
	 */
	public static User getUser(String token, String ProgramID)
	{
		if (token == null)
			return null;
		JDBCQuery query = DBQueryFactory.getQuery("PEAAS");
		String sql = "select * from Jill_Online where tokenid = '" + token + "' and logoutTime is null";
		@SuppressWarnings("unchecked")
		List<TCommonRecord> list = query.query(sql, new CommonMapper());
		if ((list == null) || (list.size() == 0))
			return null;
		TCommonRecord cr = list.get(0);
		
		String lastAccess = cr.get("lastAccess");
		String now = DateUtils.getDateTime();
		int X = DateUtils.minutesBetweenTwoDate(lastAccess, now);
		if (X > 480)
			return null;
		
        sql = "select * from Jill_UserDef where UserID = '" + cr.get("UserID") + "'";
		@SuppressWarnings("unchecked")
		List<TCommonRecord> lsUser = query.query(sql, new CommonMapper());
        if (lsUser.size() == 0)
        	return null;
        TCommonRecord crUser = lsUser.get(0);
        /*
         * 为了可以将集成模块与平台权限分离开
        ProgramID = ProgramID.replaceAll("/", "");
        String[] Programs = crUser.get("UserProgram").split(",");
        int ErrorCode = 30;
        for (int i = 0; i < Programs.length; i++)
        {
        	if (ProgramID.equals(Programs[i]))
        	{
        		ErrorCode = 0;
        		break;
        	}
        }
        if (ErrorCode == 30)
        {
        	return null;
        }       
        */
        User user = new User();
        user.setUserName(crUser.get("UserName"));
        user.setUserID(crUser.get("UserID"));
        user.setUserDesc(crUser.get("UserDesc"));
        user.setUserDuty(crUser.get("UserDuty"));
        user.setUserUnit(crUser.get("UserUnit"));
        user.setUserProgram(crUser.get("UserProgram"));
        // 2014-04-28 刘璟聪 增加 token 标示 
        user.setUserToken(token);
        sql = "update Jill_Online set lastAccess = '" + DateUtils.getDateTime() + "' where tokenid = '" + token + "'";
        try
        {
        	query.update(sql);
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        }
        Authorization auth = new Authorization(); 
        try
        {
        	user.setUserPower(auth.getRole(crUser.get("UserID")));
            user.setUserDept(auth.setUserDept(crUser.get("userdept")));
        }
        catch (Exception e)
        {
        }
        finally
        {
        	auth = null;
        }
        // user.setUserDept(getUserDept(cr.get("userdept")));
        query = null;
        return user;
	}
	
	 public static TCommonRecord getUserDept(String userDept)
	 {
    	TCommonRecord deptTcr = new TCommonRecord();
    	if(!"".equals(userDept)&&userDept!=null)
    	{
    		if(userDept.contains(","))
    		{
    			String[] deptString = userDept.split(",");
    			for(String s :deptString)
    			{
    				deptTcr.set("dept_code",s.split("-")[0]+","+deptTcr.get("dept_code"));
    				deptTcr.set("dept_name",s.split("-")[1]+","+deptTcr.get("dept_name"));
    			}
    			deptTcr.set("dept_code_name",userDept+",");
    		}
    		else
    		{
    			String[] deptContext = userDept.split("-");
    			deptTcr.set("dept_code",deptContext[0]);
				deptTcr.set("dept_name",deptContext[1]);
				deptTcr.set("dept_code_name",userDept+",");
    		}
    	}
    	return deptTcr;
	 }
    
	 /**
	  * 登出时候做处理 
	  * @param user
	  * @param token
	  * @param SysName
	  */
    public static void logoutUser(User user, String token,String SysName)
    {
    	if (user == null)
    		return;
		JDBCQuery query = DBQueryFactory.getQuery("PEAAS");
		String sql = "update Jill_Online set LogoutTime = '" + DateUtils.getDateTime() + "' where " +
			"UserID  = '" + user.getUserID() + "' and " +
			"TokenID = '" + token + "' ";
		/* 只退出自己所在系统的 token */
		if(SysName != null && !"".equals(SysName))
		{
		    sql += "and loginsystem = '" + SysName + "'";
		}
		else
		{
		    sql += " and loginsystem is null";
		}
		try
		{
			query.update(sql);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		query = null;
    }
    
	public static String loginUser(User user, String SysName)
	{
		JDBCQuery query = DBQueryFactory.getQuery("PEAAS");
		String token = UUID.randomUUID().toString();
		token = token.replace("-", "");
		if(user == null) throw new RuntimeException("用户登录失败,User = null");
		// 设置用户token标识 
		user.setUserToken(token);
		String sql = "insert into Jill_Online (TOKENID,USERID,LASTACCESS,LOGINTIME,LOGINSYSTEM) values (" + 
			"'" + token                   + "', " +
			"'" + user.getUserID()        + "', " +
			"'" + DateUtils.getDateTime() + "', " +
			"'" + DateUtils.getDateTime() + "', " +
			"'" + SysName                 + "'  " +
			")";
		try
		{
			query.update(sql);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		query = null;
		return token;
	}
	
	/**
	 * 生成跨平台连接
	 * @param session
	 * @return token=xxxxxxxxxx
	 */
	public static String getToken(HttpSession session)
	{
	    return "token=" + (String)session.getAttribute(SSOController.Token);
	}
	
	/**
     * 生成跨平台连接
     * @param session
     * @return xxxxxxxxxx
     */
    public static String getTokenCode(HttpSession session)
    {
        return (String)session.getAttribute(SSOController.Token);
    }
}
