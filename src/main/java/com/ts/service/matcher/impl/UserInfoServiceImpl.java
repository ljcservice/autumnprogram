package com.ts.service.matcher.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.Impl.BasePageBean;
import com.ts.service.matcher.ICommonOperService;

/**
 * 修改密码
 * @author Crystal
 */
@Service
@Transactional
public class UserInfoServiceImpl extends BasePageBean implements ICommonOperService
{
	@Override
	public Object insert(TCommonRecord params)
	{
		return null;
	}

	@Override
	public Object delete(TCommonRecord params)
	{
		return null;
	}

	@Override
	public Object update(TCommonRecord params)
	{
		this.setQueryCode("PEAAS");
		StringBuffer sql = new StringBuffer();
		sql.append("update JILL_USERDEF set ");
		sql.append("USERPWD = '").append(params.get("PWD")).append("' ");
		sql.append("where USERID = '").append(params.get("USERID")).append("'");
		return query.update(sql.toString());
	}

	@Override
	public List<TCommonRecord> queryForList(TCommonRecord params)
	{
		return null;
	}

	@Override
	public PageView<TCommonRecord> queryForPageList(TCommonRecord params)
	{
		return null;
	}

	@Override
	public TCommonRecord queryForObject(TCommonRecord params)
	{
		return null;
	}
}