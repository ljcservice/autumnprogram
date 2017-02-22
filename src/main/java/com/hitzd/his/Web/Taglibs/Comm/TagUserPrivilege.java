package com.hitzd.his.Web.Taglibs.Comm;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.hitzd.his.Beans.frame.User;
import com.hitzd.his.tree.TreeNode;

/**
 * 模块功能权限过滤 Tag
 * @author jingcong
 *
 */
public class TagUserPrivilege extends TagSupport
{
    private static final long serialVersionUID = 1L;
    
    /* 菜单模块 */
    private String menuId   = "";
    /* 模块功能 */
    private String menuFunc = "";
    
    public String getMenuId()
    {
        return menuId;
    }

    public void setMenuId(String menuId)
    {
        this.menuId = menuId;
    }

    public String getMenuFunc()
    {
        return menuFunc;
    }

    public void setMenuFunc(String menuFunc)
    {
        this.menuFunc = menuFunc;
    }

    
    @Override
    public int doStartTag() throws JspException
    {
        User user        = (User)pageContext.getSession().getAttribute(User.UserSessionCode);
        String uRlMenuid = (String)pageContext.getSession().getAttribute(TreeNode.URLMenuID);
        if(user == null)
            return SKIP_BODY;
        //uRlMenuid +
        if(!user.hasPowerFunc(uRlMenuid + this.menuId, this.menuFunc))
        {
            return SKIP_BODY;
        }
        return EVAL_PAGE;
    }
}
