package com.hitzd.his.adr.MrFetcher.impl;

import java.util.List;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.adr.MrFetcher.IMrFetcher;

public class MrBaseFetcher implements IMrFetcher 
{
	protected static String mrPath = null;
	protected static String mrTempPath;
	public static String getMrPath() {
		return mrPath;
	}

	public static String getMrTempPath() {
		return mrTempPath;
	}

	public static String getHost() {
		return Host;
	}

	public static String getUser() {
		return User;
	}

	public static String getPwd() {
		return Pwd;
	}

	protected static String Host;
	protected static String User;
	protected static String Pwd;

	@Override
	public List<TCommonRecord> fetchMrList(String ADate) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("null")
	@Override
	public void getWorkPath() 
	{
		if (mrPath != null) return;
		JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
		String sql = "select * from medrec.mr_work_path t";
		@SuppressWarnings("unchecked")
		List<TCommonRecord> list = query.query(sql, new CommonMapper());
		if ((list != null) || ( list.size() > 0))
		{
			TCommonRecord cr = list.get(0);
			mrPath = cr.get("MR_Path");
			mrTempPath = cr.get("templet_Path");
			Host = cr.get("IP_ADDR");
			User = cr.get("File_User");
			Pwd = cr.get("File_PWD");
		}
		query = null;
	}

	@Override
	public String fetchMrContent(String dstDir, TCommonRecord mrName, String resverd) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
