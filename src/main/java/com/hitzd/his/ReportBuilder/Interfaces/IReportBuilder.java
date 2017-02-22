package com.hitzd.his.ReportBuilder.Interfaces;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.task.Task;

public interface IReportBuilder 
{
	/**
	 *  创建需要报表抽取实现
	 *  返回错误信息，无错误返回空字符串
	 * @param ADate
	 * @return
	 */
	public String BuildReport(String ADate, Task AOwner);
	
	public String BuildReportWithCR(String ADate, TCommonRecord crPatInfo, Task AOwner);
	/**
	 *  返回本次的日志文件名
	 * @return
	 */
	public String getLogFileName();
	
	public void buildBegin(String ADate, Task AOwner);
	public void buildOver(String ADate, Task AOwner);
}
