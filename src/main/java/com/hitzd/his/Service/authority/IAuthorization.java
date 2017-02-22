package com.hitzd.his.Service.authority;

import com.hitzd.his.Beans.frame.User;

/**
 * 登陆验证 
 * @author liujc
 *
 */
public interface IAuthorization
{
    /**
     *  平台主登陆入口
     * @param UserName
     * @param Password
     * @param ProgramID
     * @param is_pf
     * @return
     */
    public User VerifyLogin(String UserName, String Password, String ProgramID,boolean is_pf);
    /**
     * 平台公共登陆入口
     * @param UserName
     * @param Password
     * @param ProgramID
     * @return
     */
    public User VerifyLogin(String UserName, String Password, String ProgramID);
    /**
     * 返回状态code 
     * @return
     */
    public int getErrorCode();
}
