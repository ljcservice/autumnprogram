package com.hitzd.his.Service.authority.Impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Security.SecUtils;
import com.hitzd.his.Beans.frame.User;
import com.hitzd.his.Beans.frame.UserRolePower;
import com.hitzd.his.Beans.frame.Mapper.UserRolePowerMapper;
import com.hitzd.his.Service.authority.IAuthorization;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.persistent.Persistent4DB;

/**
 * 登录认证对象
 */
@Service
@Transactional
public class Authorization extends Persistent4DB implements IAuthorization, Serializable
{
	private static final long serialVersionUID = 1L;
       
    public Authorization() 
    {
        
    }

    private int ErrorCode = 0;
    
    public int getErrorCode()
    {
    	return ErrorCode;
    }
    
    public User VerifyLogin(String UserName, String Password, String ProgramID,boolean is_pf)
    {
        setQueryCode("PEAAS");
        String sql = "select * from Jill_UserDef where UserID = '" + UserName + "'";
        List<TCommonRecord> crs = query.query(sql, new CommonMapper());
        if (crs.size() == 0)
        {
            ErrorCode = 10;
            return null;
        }
        TCommonRecord cr = crs.get(0);
        if (!cr.get("userpwd").equals(SecUtils.Encode(Password))) 
        {
            ErrorCode = 20;
            return null;
        }
        if(!is_pf)
        {
            ProgramID = ProgramID.replaceAll("/", "");
            String[] Programs = cr.get("UserProgram").split(",");
            ErrorCode = 30;
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
        }
        ErrorCode = 0;
        User user = new User();
        user.setUserName(cr.get("UserName"));
        user.setUserID(cr.get("UserID"));
        user.setUserDesc(cr.get("UserDesc"));
        user.setUserPower(this.getRole(UserName));
        user.setUserDuty(cr.get("UserDuty"));
        user.setUserUnit(cr.get("UserUnit"));
        user.setUserProgram(cr.get("UserProgram"));
        user.setUserDept(this.setUserDept(cr.get("userdept")));
        return user;
    }
    
    /**
     * 校验登录，通过返回User对象，ErrorCode = 0;
     * 不通过的话，getErrorCode()返回校验错误信息
     * 用户不存在  ErrorCode = 10
     * 密码错误  ErrorCode = 20
     * 没有对该系统的访问权限 ErrorCode = 30
     */
    @SuppressWarnings("unchecked")
	public User VerifyLogin(String UserName, String Password, String ProgramID)
    {
        return this.VerifyLogin(UserName, Password, ProgramID, false);
    }
    
    public TCommonRecord setUserDept(String userDept)
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
     * 返回用户使用菜单
     * @param userId
     * @return
     */
    @SuppressWarnings ("unchecked")
    public List<UserRolePower> getRole(String userId)
	{
    	StringBuffer UserPower = new StringBuffer();
    	String sql= "select * from JILL_ROLEPOWER t where t.roleid='" + userId + "'";
    	this.setQueryCode("PEAAS");
    	List<UserRolePower> list = this.query.query(sql.toString(), new UserRolePowerMapper());
    	if(list != null && list.size() > 0)
    	{
    		for(UserRolePower tcr : list)
    		{
//    			UserPower.append("{").append(tcr.get("progid")).append("}");
    		}
    	}
    	return list;
	}
}

