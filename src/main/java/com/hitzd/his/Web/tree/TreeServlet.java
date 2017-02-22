package com.hitzd.his.Web.tree;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.hitzd.his.Beans.frame.User;
import com.hitzd.his.Service.authority.Impl.Authorization;
import com.hitzd.his.sso.SSOController;
import com.hitzd.his.tree.ITreeService;
import com.hitzd.his.tree.TreeNode;
import com.hitzd.his.tree.Impl.TreeService;

/**
 * 建立用户菜单
 */
@Component("TreeServlet")
public class TreeServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public TreeServlet() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}

	private void filterTree(User user, TreeNode node)
	{
		for (int i = node.getChildCount() - 1; i >= 0; i--)
		{
			TreeNode nodeChild = node.getChild(i);
			if (nodeChild.getMenuIsNode().equals("1"))
			{
				if (!user.hasPower(nodeChild.getMenuID()))
					node.removeChild(i);
			}
			else
			{
				if (nodeChild.getChildCount() == 0)
					node.removeChild(i);
				else
					filterTree(user, nodeChild);
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		User user = SSOController.getUser(request);
		if (user == null)
		{
			response.sendRedirect("/");
			return ;
		}
		Authorization auth = new Authorization();
		user.setUserPower(auth.getRole(user.getUserID()));
		String ProgramID     = request.getContextPath().substring(1,  request.getContextPath().length());
		ITreeService treeSrv = new TreeService();
		TreeNode nodes       = treeSrv.getTrees(ProgramID, user.getUserID());
		/* 用户过滤菜单 */
		filterTree(user, nodes);
		request.setAttribute("nodes", nodes);
		String destPage = this.getInitParameter("MenuPage");
		if ((destPage == null) || (destPage.length() == 0))
			destPage = (String)request.getParameter("MenuPage");
		if ((destPage == null) || (destPage.length() == 0))
			destPage = "/WebPage/menu.jsp";
		request.getRequestDispatcher(destPage).forward(request, response);
	}
}
