package com.hitzd.his.casehistory.helper;

import com.hitzd.DBUtils.TCommonRecord;

public class CaseHistoryHelperUtils 
{
	/**
	 * 等号：=
	 */
	public static final String EQ = "=";
	
	/**
	 * 模糊查询：like
	 */
	public static final String LIKE = "like";
	
	/**
	 * 取两值之间的数据：between..and..
	 */
	public static final String BETWEENAND = "between..and";
	
	/**
	 * 小于：<
	 */
	public static final String LESS = "<";
	
	/**
	 * 大于：>
	 */
	public static final String MORE = ">";
	
	/**
	 * 小于等于：<=
	 */
	public static final String LESSEQ = "<=";
	
	/**
	 * 大于等于：>=
	 */
	public static final String MOREEQ = ">=";
	
	/**
	 * 查询条件规定多个值：in
	 */
	public static final String IN = "in";
	
	/**
	 * 查询 is 为连接检索
	 */
	public static final String IS =  "is";
	
	/**
	 * @param lsWheres 条件列表，是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param strGroup group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param strOrder order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 */
	public static TCommonRecord genWhereCR(String FieldName, String FieldValue, String FieldType, String Relation, String GroupNo, String Condition)
	{
		TCommonRecord Result = new TCommonRecord();
		Result.set("FieldName", FieldName);
		Result.set("FieldValue", FieldValue);
		Result.set("FieldType", FieldType);
		Result.set("Relation", Relation);
		Result.set("GroupNo", GroupNo);
		Result.set("Condition", Condition);
		return Result;
	}
	
	/**
	 * 生成带中文的查询条件
	 * @param FieldName
	 * @param FieldValue
	 * @param FieldType
	 * @param Relation
	 * @param GroupNo
	 * @param Condition
	 * @return
	 */
	public static TCommonRecord genWhereGbkCR(String FieldName, String FieldValue, String FieldType, String Relation, String GroupNo, String Condition)
	{
		TCommonRecord Result = new TCommonRecord();
		Result.set("FieldName", FieldName);
		if (BETWEENAND.equals(Relation))
		{
			String[] values = FieldValue.split("&");
			FieldValue = "?" + values[0] + ":end&?" + values[1] + ":end";
			Result.set("FieldValue", FieldValue);
		}
		else
		{
			Result.set("FieldValue", "?" + FieldValue + ":end");
		}
		Result.set("FieldType", FieldType);
		Result.set("Relation", Relation);
		Result.set("GroupNo", GroupNo);
		Result.set("Condition", Condition);
		return Result;
	}
	
	/**
	 * 生成分组语句
	 * @param FieldName
	 * @return
	 */
	public static TCommonRecord genGroupCR(String FieldName)
	{
		TCommonRecord Result = new TCommonRecord();
		Result.set("FieldName", FieldName);
		return Result;
	}
	
	/**
	 * 生成排序语句
	 * @param FieldName
	 * @param By
	 * @return
	 */
	public static TCommonRecord genOrderCR(String FieldName, String By)
	{
		TCommonRecord Result = new TCommonRecord();
		Result.set("FieldName", FieldName);
		Result.set("By", By);
		return Result;
	}
	
	
}