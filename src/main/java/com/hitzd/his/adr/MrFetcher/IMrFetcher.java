package com.hitzd.his.adr.MrFetcher;

import java.util.List;

import com.hitzd.DBUtils.TCommonRecord;

public interface IMrFetcher 
{
	public List<TCommonRecord> fetchMrList(String ADate);
	public void getWorkPath();
	public String fetchMrContent(String dstDir, TCommonRecord mrName, String resverd);
}
