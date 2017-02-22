package com.hitzd.his.tree.Impl;

import java.util.List;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.tree.ITreeService;
import com.hitzd.his.tree.TreeNode;

/**
 * 菜单树构造 
 * @author jingcong
 *
 */
public class TreeService implements ITreeService 
{

	@Override
	public TreeNode getTrees(String ProgramID, String UserID) 
	{
		TreeNode node       = new TreeNode();
		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
		buildTrees(node, query, ProgramID, UserID);
		query = null;
		return node;
	}
	
	protected TCommonRecord getMenuInfo(JDBCQueryImpl query, String MenuID)
	{
        String sql = "select * from Jill_MenuDef where MenuID = '" + MenuID + "'";
		@SuppressWarnings("unchecked")
		List<TCommonRecord> crs = query.query(sql, new CommonMapper());
        if (crs.size() == 0)
        	return null;
        return crs.get(0);
	}	
	
	/**
	 * 构建菜单树
	 * @param pNode
	 * @param query
	 * @param ProgramID
	 * @param UserID
	 */
	public void buildTrees(TreeNode pNode, JDBCQueryImpl query, String ProgramID, String UserID)
	{
		List<TCommonRecord> list = getTreeRecords(query, pNode.getMenuID(), ProgramID, UserID);
		for (TCommonRecord cr: list)
		{
			TreeNode nodeChild = new TreeNode(cr);
			if (nodeChild.getMenuRefID().length() > 0)
			{
				TreeNode nodeRef = new TreeNode(getMenuInfo(query, nodeChild.getMenuRefID()));
				nodeChild.setRefMenu(nodeRef);
			}
			pNode.addChild(nodeChild);
			buildTrees(nodeChild, query, ProgramID, UserID);
		}
	}
	
	/**
	 * 检索菜单  menuisview = 1  可显示菜单
	 * @param query
	 * @param ParentID
	 * @param ProgramID
	 * @param UserID
	 * @return
	 */
	@SuppressWarnings ("unchecked")
    private List<TCommonRecord> getTreeRecords(JDBCQueryImpl query, String ParentID, String ProgramID, String UserID)
	{
		if("".equals(ParentID))
			ParentID = " is null ";
		else
			ParentID = " = '" + ParentID + "'";
		String sql = "select * from Jill_MenuDef where MenuParentID  " + ParentID + " and ProgramID = '" + ProgramID + "'  and MenuIsView = '1' order by  to_number(Menuorder),MENUID";
//		String sql = "select m.* from JILL_ROLEPOWER t, jill_menudef m where " +
//			"t.progid     = m.menuid and " +
//			"MenuParentID    " + ParentID  + " and " +
//			"ProgramID    = '" + ProgramID + "'  ";
//		if (!"".equals(ParentID))
//			sql += " and t.roleid     = '" + UserID + "' ";
//		sql += "order by  menuorder, MENUID"; 
		List<TCommonRecord> list = query.query(sql, new CommonMapper());
		return list;
	}
}
