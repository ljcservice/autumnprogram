package com.hitzd.his.Beans.frame;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hitzd.DBUtils.TCommonRecord;

/**
 * 用户信息字段
 * @author jingcong
 *
 */
public class User
{
    /* session中名字 */
    public final static String UserSessionCode = "Customer";
    /* 用户id */
    private String             UserID          = "";
    /* 用户名字*/
    private String             UserName        = "";
    /* 访问权限  */
    private String             UserPower       = "";
    /* 模块具体功能访问权限  */
    private HashMap<String, List<String>>  userPowerFunc = new HashMap<String, List<String>>();
    /* 用户介绍 */
    private String             UserDesc        = "";
    /* 用户单位 */
    private String             UserUnit        = "";
    /* */
    private String             UserDuty        = "";
    /* 用户允许进入的平台  */
    private String             UserProgram     = "";
    /* 用户角色 目前 000 主任药师  ， 001 ，药师 */
    private String             UserRole        = "";
    /* 用户token ，注：一次用户登录平台后仅使用一个 Token编码  */
    private String             UserToken       = "";
    /* 用户能看到部门 */
    private TCommonRecord UserDept = new TCommonRecord();
    
    public String getUserRole()
    {
        return UserRole;
    }

    public void setUserRole(String userRole)
    {
        UserRole = userRole;
    }

    public String getUserProgram()
    {
        return UserProgram;
    }

    public void setUserProgram(String userProgram)
    {
        UserProgram = userProgram;
    }

    public String getUserUnit()
    {
        return UserUnit;
    }

    public void setUserUnit(String userUnit)
    {
        UserUnit = userUnit;
    }

    public String getUserDuty()
    {
        return UserDuty;
    }

    public void setUserDuty(String userDuty)
    {
        UserDuty = userDuty;
    }

    public TCommonRecord getUserDept()
    {
        return UserDept;
    }

    public void setUserDept(TCommonRecord userDept)
    {
        UserDept = userDept;
    }

    public String getUserID()
    {
        return UserID;
    }

    public void setUserID(String userID)
    {
        UserID = userID;
    }

    public String getUserName()
    {
        return UserName;
    }

    public void setUserName(String userName)
    {
        UserName = userName;
    }

    public void setUserPower(List<UserRolePower> userPowers)
    {
        UserPower = "";
        if(userPowers != null && userPowers.size() > 0)
        {
            StringBuffer sb = new StringBuffer();
            for(UserRolePower e : userPowers)
            {
                /* 用户的模块具体权限     模块名字[功能点1,功能点2,功能点3] */
                String roleFunc = e.getProgfunc(); //tcr.get("progfunc");
                if(roleFunc != null && !"".equals(roleFunc))
                {
                    /* 正则拆分  */
                    Pattern pt = Pattern.compile("(\\[)(.*?)(\\])");
                    Matcher mr = pt.matcher(roleFunc); 
                    while(mr.find())
                    {
                        String key   = roleFunc.replace(mr.group(0), "");
                        String value = mr.group(2);
                        /* 加模块功能权限 */
                        addUserPowerFunc(e.getProgid() + key, "".equals(value) ? null : Arrays.asList(value.split(",")));
                    }
                }
                //组织模块访问权限 
                sb.append("{").append(e.getProgid()).append("}");
            }
            UserPower = sb.toString();
        }
    }

    private void addUserPowerFunc(String key , List<String> value)
    {
        this.userPowerFunc.put(key, value);
    }
    
    public String getUserDesc()
    {
        return UserDesc;
    }

    public void setUserDesc(String userDesc)
    {
        UserDesc = userDesc;
    }

    public String getUserPower()
    {
        return UserPower;
    }
    
    /**
     * 返回该用户是否有指定模块的访问权限
     * 
     * @param modelName
     *            指定的模块
     * @return
     */
    public boolean hasPower(String modelName)
    {
        if (null == UserPower || "".equals(UserPower))
            return false;
        return UserPower.contains("{" + modelName + "}");
    }
    
    /**
     * 返回该用户是否具有某模块某个功能的使用权限
     * @param menuid
     * @param mFunc
     * @return
     */
    public boolean hasPowerFunc(String menuid , String mFunc)
    {
        if(menuid == null || "".equals(menuid) || mFunc == null || "".equals(mFunc))
            return false;
        List<String> mFuncs = this.userPowerFunc.get(menuid);
        if(mFuncs != null)
        {
            return mFuncs.contains(mFunc);
        }
        return  false;
    }

    public String getUserToken()
    {
        return UserToken;
    }

    public void setUserToken(String userToken)
    {
        UserToken = userToken;
    }
    
}