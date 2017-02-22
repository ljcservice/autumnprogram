package com.hitzd.his.Beans.frame;

/**
 * 用户权限表 
 * @author jingcong
 */
public class UserRolePower
{
    /* 用户id*/
    private String roleid ;
    /* 菜单id */
    private String progid;
    /* 菜单允许的方法 */
    private String progfunc;
    /* 菜单所属系统 */
    private String sysid ;
    
    public String getRoleid()
    {
        return roleid;
    }
    public void setRoleid(String roleid)
    {
        this.roleid = roleid;
    }
    public String getProgid()
    {
        return progid;
    }
    public void setProgid(String progid)
    {
        this.progid = progid;
    }
    public String getProgfunc()
    {
        return progfunc;
    }
    public void setProgfunc(String progfunc)
    {
        this.progfunc = progfunc;
    }
    public String getSysid()
    {
        return sysid;
    }
    public void setSysid(String sysid)
    {
        this.sysid = sysid;
    } 
}
