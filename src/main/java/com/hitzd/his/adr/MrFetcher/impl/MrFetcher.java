package com.hitzd.his.adr.MrFetcher.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.hwpf.extractor.WordExtractor;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.adr.mr.MrAgent;

/**
 * 获取军卫一号，基于word的病程记录的Fetcher
 * @author Administrator
 *
 */
public class MrFetcher extends MrBaseFetcher 
{
	@Override
	public List<TCommonRecord> fetchMrList(String ADate) 
	{
		JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
		String sql = 
			"select " +
			"  mrf.*, dept.dept_name " +
			"from (select mr.patient_id, " +
            "    mr.visit_id,     " +
            "    mr.file_no,      " +
            "    mr.file_name,    " +
            "    mr.topic,        " +
            "    mr.creator_name, " +
            "    mr.creator_id,   " +
            "    pin.dept_code    " +
            "  from " +
            "    medrec.Mr_File_Index mr, inpadm.pats_in_hospital pin " +
            "  where " +
            "    mr.last_modify_date_time     >= to_date('" + ADate + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') " +
            "    and mr.last_modify_date_time <= to_date('" + ADate + " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') " +
            "    and mr.patient_id = pin.patient_id   " +
            "    and mr.visit_id = pin.visit_id) mrf, " +
            "  comm.dept_dict dept " +
            "where mrf.dept_code = dept.dept_code   " +
            "order by patient_id, visit_id, file_no ";
		@SuppressWarnings("unchecked")
		List<TCommonRecord> list = query.query(sql, new CommonMapper());
		query = null;
		return list;
	}

	public String FetchFileFromServer(String dstDir, String RemoteFile, String PatientID, String FileName)
	{
		String dstFileName = GenMRFileName(dstDir, PatientID, FileName, true);
		MrAgent ma = new MrAgent();
		if (ma.get_file(Host, RemoteFile, dstFileName, 1) == 0)
		{
			ma = null;
			return dstFileName;
		}
		return "";
	}
	
	public String getWordContent(String fileName) throws Exception 
	{
		FileInputStream in = new FileInputStream(new File(fileName));
		//FileOutputStream out = new FileOutputStream(new File(fileName));
		WordExtractor extractor = new WordExtractor(in);
		String text =  extractor.getText();
		//byte[] _inBuf = text.getBytes();
		//out.write(_inBuf);
		//out.close();
		in.close();
		in = null;
		return text;
	}	

	@Override
	public String fetchMrContent(String dstDir, TCommonRecord mrName, String resverd) 
	{
		getWorkPath();
		String SrcFileName = GenMRFileName(mrPath, mrName.get("Patient_ID"), mrName.get("File_Name"), false);
		String DstFileName = FetchFileFromServer(dstDir, SrcFileName, mrName.get("Patient_ID"), mrName.get("File_Name"));
		if (DstFileName.length() > 0)
		{
			try
			{
				return getWordContent(DstFileName);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return "";
			}
		}
		return "";
	}
	
	public String GenMRFileName(String dstDir, String PatientID, String FileName, boolean isLocal)
	{
		String Dir1 = PatientID.substring(PatientID.length() - 2, PatientID.length());
		String Dir2 = PatientID.substring(0, PatientID.length() - 2);
		int len = Dir2.length();
		for (int i = 0; i < 8 - len; i++)
			Dir2 = "0" + Dir2;
		String Path = dstDir;
		if (Path.charAt(Path.length() - 1) == '\\')
			Path = Path + Dir1 + "\\" + Dir2 + "\\";
		else
			Path = Path + "\\" + Dir1 + "\\" + Dir2 + "\\";
		if (isLocal)
		{
			try
			{
				File file = new File(Path);
				if (!file.exists())
					file.mkdirs();
				file = null;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return Path + FileName;
	}
}
