package com.hitzd.his.Service.hibernateService.Impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hitzd.persistent.Persistent4DB;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Service.hibernateService.IHiberBiz;
import com.hitzd.DBUtils.CommonMapper;

/**
 * 用于普通表操作。
 * @author tyl
 *
 */
@Service
@Transactional
public class HiberBiz extends Persistent4DB implements  Serializable,IHiberBiz{
	
	private static final long serialVersionUID = 1L;
	public HiberBiz() {		
		
	}
	public List getList(String sql) 
	{	
		setQueryCode("PEAAS");		
		List<TCommonRecord> crs = query.query(sql, new CommonMapper());
		return crs;
	}
	public void update(String sql)
	{	
		setQueryCode("PEAAS");	
		query.execute(sql);		
	}
	public void delete(String sql)
	{
		setQueryCode("PEAAS");	
		query.execute(sql);
	}
	public void insert(String sql)
	{
		setQueryCode("PEAAS");	
		query.execute(sql);
	}
}
