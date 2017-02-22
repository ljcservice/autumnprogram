package com.hitzd.his.Beans.Middle;

/**
 * 查询条件定义配置
 * @author Crystal
 */
public class TQueryConfig
{
	/* 唯一标识 */
	private String queryId;
	/* 所属表ID */
	private String tableId;
	/* 查询条件 */
	private String queryCondition;
	/* 备注 */
	private String remark;
	/* HIS标准，默认JWYH-304 */
	private String hisName;
	
	public TQueryConfig()
	{
		this.queryId = "";
		this.tableId = "";
		this.queryCondition = "";
		this.remark = "";
		this.hisName = "JWYH-304";
	}

	public String getQueryId()
	{
		return queryId;
	}

	public void setQueryId(String queryId)
	{
		this.queryId = queryId;
	}

	public String getTableId()
	{
		return tableId;
	}

	public void setTableId(String tableId)
	{
		this.tableId = tableId;
	}

	public String getQueryCondition()
	{
		return queryCondition;
	}

	public void setQueryCondition(String queryCondition)
	{
		this.queryCondition = queryCondition;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getHisName()
	{
		return hisName;
	}

	public void setHisName(String hisName)
	{
		this.hisName = hisName;
	}
}