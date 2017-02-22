package com.hitzd.his.tree;

import java.util.ArrayList;
import java.util.List;

import com.hitzd.DBUtils.TCommonRecord;

/**
 * 菜单树对象 
 * @author 
 *
 */
public class TreeNode
{
    /**/
    public static String URLMenuID = "URLMenuID";
    
    private String   MenuID       = "";
    private String   MenuCaption  = "";
    private String   MenuDescribe = "";
    private String   MenuImage    = "";
    private String   MenuParentID = "";
    private String   ProgramID    = "";
    private String   MenuOrder    = "";
    private String   MenuIsNode   = "";
    private String   MenuURL      = "";
    private String   MenuHandler  = "";
    private String   MenuGroup    = "";
    private String   MenuParam    = "";
    private String   MenuIsRpt    = "";
    private String   MenuRefID    = "";
    /* 是否显示给节点*/
    private String   MenuIsView   = "";
    private TreeNode RefMenu      = null;
    
    /* 鲁廷明20140529修改  */
    /* 当前菜单向哪个服务器请求 */
    private String   Server       = "";

    public TreeNode getRefMenu()
    {
        return RefMenu;
    }

    public void setRefMenu(TreeNode refMenu)
    {
        RefMenu = refMenu;
    }

    private List<TreeNode> childs = new ArrayList<TreeNode>();

    public int getChildCount()
    {
        return childs.size();
    }

    public void addChild(TreeNode node)
    {
        childs.add(node);
    }

    public TreeNode getChild(int index)
    {
        return childs.get(index);
    }

    public void removeChild(int index)
    {
        childs.remove(index);
    }

    public TreeNode()
    {

    }

    public TreeNode(TCommonRecord cr)
    {
        MenuID       = cr.get("MenuID");
        MenuCaption  = cr.get("MenuCaption");
        MenuDescribe = cr.get("MenuDescribe");
        MenuImage    = cr.get("MenuImage");
        MenuParentID = cr.get("MenuParentID");
        ProgramID    = cr.get("ProgramID");
        MenuOrder    = cr.get("MenuOrder");
        MenuIsNode   = cr.get("MenuIsNode");
        MenuURL      = cr.get("MenuURL");
        MenuHandler  = cr.get("MenuHandler");
        MenuGroup    = cr.get("MenuGroup");
        MenuParam    = cr.get("MenuParam");
        MenuIsRpt    = cr.get("MenuIsRpt");
        MenuRefID    = cr.get("MenuRefID");
        MenuIsView   = cr.get("MenuIsView");
        Server       = cr.get("Server");
    }

    public String getMenuIsRpt()
    {
        return MenuIsRpt;
    }

    public void setMenuIsRpt(String menuIsRpt)
    {
        MenuIsRpt = menuIsRpt;
    }

    public String getMenuRefID()
    {
        return MenuRefID;
    }

    public void setMenuRefID(String menuRefID)
    {
        MenuRefID = menuRefID;
    }

    public String getMenuID()
    {
        return MenuID;
    }

    public void setMenuID(String menuID)
    {
        MenuID = menuID;
    }

    public String getMenuCaption()
    {
        return MenuCaption;
    }

    public void setMenuCaption(String menuCaption)
    {
        MenuCaption = menuCaption;
    }

    public String getMenuDescribe()
    {
        return MenuDescribe;
    }

    public void setMenuDescribe(String menuDescribe)
    {
        MenuDescribe = menuDescribe;
    }

    public String getMenuImage()
    {
        return MenuImage;
    }

    public void setMenuImage(String menuImage)
    {
        MenuImage = menuImage;
    }

    public String getMenuParentID()
    {
        return MenuParentID;
    }

    public void setMenuParentID(String menuParentID)
    {
        MenuParentID = menuParentID;
    }

    public String getProgramID()
    {
        return ProgramID;
    }

    public void setProgramID(String programID)
    {
        ProgramID = programID;
    }

    public String getMenuOrder()
    {
        return MenuOrder;
    }

    public void setMenuOrder(String menuOrder)
    {
        MenuOrder = menuOrder;
    }

    public String getMenuIsNode()
    {
        return MenuIsNode;
    }

    public void setMenuIsNode(String menuIsNode)
    {
        MenuIsNode = menuIsNode;
    }

    public String getMenuURL()
    {
        return MenuURL;
    }

    public void setMenuURL(String menuURL)
    {
        MenuURL = menuURL;
    }

    public String getMenuHandler()
    {
        return MenuHandler;
    }

    public void setMenuHandler(String menuHandler)
    {
        MenuHandler = menuHandler;
    }

    public String getMenuGroup()
    {
        return MenuGroup;
    }

    public void setMenuGroup(String menuGroup)
    {
        MenuGroup = menuGroup;
    }

    public String getMenuParam()
    {
        return MenuParam;
    }

    public void setMenuParam(String menuParam)
    {
        MenuParam = menuParam;
    }

    public String getMenuIsView()
    {
        return MenuIsView;
    }

    public void setMenuIsView(String menuIsView)
    {
        MenuIsView = menuIsView;
    }

	public String getServer() {
		return Server;
	}

	public void setServer(String server) {
		Server = server;
	}
    
    
}
