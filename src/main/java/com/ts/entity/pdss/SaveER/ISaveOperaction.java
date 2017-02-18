package com.ts.entity.pdss.SaveER;

import com.hitzd.DBUtils.TCommonRecord;
/**
 * 记录审查操作(执行时间，药品数量，用户IP，病人ID，住院序号，操作医生，医生部门)
 * @author Administrator
 *
 */
public interface ISaveOperaction
{
	/**
	 * 保存审查结果
	 * @param tcr
	 */
    public void saveOperaction(TCommonRecord tcr) throws Exception;
}
