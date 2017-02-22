package com.hitzd.his.Beans.Middle;

/**
 * 中间层数据源字典表 
 * @author jingcong
 *
 */
public class TDBUrlConfig
{

    /* id */
    private String id;     
    /* 数据源名字*/
    private String db_url;
    /* 备注 */
    private String remark;
    /* 数据库类型 */
    private String db_base;
    /* 数据库用户名字 */
    private String  db_user;
    /* 数据库密码 */
    private String  db_pwd;  
    /* 数据库连接地址 */
    private String  conn_url;
    /* 配置文件是否存在连接  */
    private String  flag;    
    
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getDb_url()
    {
        return db_url;
    }
    public void setDb_url(String db_url)
    {
        this.db_url = db_url;
    }
    public String getRemark()
    {
        return remark;
    }
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    public String getDb_base()
    {
        return db_base;
    }
    public void setDb_base(String db_base)
    {
        this.db_base = db_base;
    }
    public String getDb_user()
    {
        return db_user;
    }
    public void setDb_user(String db_user)
    {
        this.db_user = db_user;
    }
    public String getDb_pwd()
    {
        return db_pwd;
    }
    public void setDb_pwd(String db_pwd)
    {
        this.db_pwd = db_pwd;
    }
    public String getConn_url()
    {
        return conn_url;
    }
    public void setConn_url(String conn_url)
    {
        this.conn_url = conn_url;
    }
    public String getFlag()
    {
        return flag;
    }
    public void setFlag(String flag)
    {
        this.flag = flag;
    }
}
