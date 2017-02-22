package com.hitzd.his.Utils;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;

/**
 * 添加生成已经生成文件 
 * @author Administrator
 *
 */
public interface IFileMapper
{
	public boolean InsertDataFile(TCommonRecord param , JDBCQueryImpl query);
	
	public TCommonRecord SelectById(TCommonRecord parm , JDBCQueryImpl query);
	 
}
