package com.hitzd.persistent;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.JdbcTemplateHander;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.TransactionTemp;

/**
 * 系统数据库源管理
 * @author Administrator
 */
public class Persistent4DB 
{
	protected JDBCQueryImpl query = null;
	protected TransactionTemp transactionTemp = null; 
	public void setQueryCode(String resourceID)
	{
	    query = new JDBCQueryImpl((new JdbcTemplateHander()).bind(DBQueryFactory.getDBQuery(resourceID),resourceID),resourceID);
	    transactionTemp = new TransactionTemp(resourceID);
	    if(query == null)
    	    new RuntimeException("The code name is not true , can't load datasource : " + resourceID);
	}
}
