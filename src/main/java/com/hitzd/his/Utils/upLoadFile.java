package com.hitzd.his.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;

/**
 * 上传 下载 文件 
 * @author Administrator
 *
 */
public class upLoadFile 
{
	private HttpServletRequest req;
	private HttpServletResponse res;
	private InputStream input = null;
	/* 文件的真是地址 */
	private String fileRealPath;
	/* 文件的真实名字*/
	private String fileRealName;
	/* 文件地址*/
	private String filePath;
	/*文件名称*/
	private String fileName;
	/*文件扩展名*/
	private String fileSuffix;
	/**/
	private String filePathAssortName = "/" ;
	
	private long fileSize ;
	
	public void setFilePathAssort(String filePathAssort)
	{
		this.filePathAssortName =  filePathAssort ;
	}
	
	public String getFileRealPath()
	{
		return fileRealPath;
	}
	
	public String getFileRealName()
	{
		return fileRealName;
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public String getFileSuffix()
	{
		return fileSuffix;
	}
	
	public String getFilePathAssortName()
	{
		return filePathAssortName;
	}
	
	/**/
	public upLoadFile(HttpServletRequest req,HttpServletResponse res)
	{
		this.req = req;
		this.res = res;
		this.fileRealPath  = req.getSession().getServletContext().getRealPath("WEB-INF/upLoad");
	}
	
	/**
	 * 上传
	 * @param setQueryCode
	 * @param insertMapper
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean upLoading(String setQueryCode , IFileMapper  insertMapper)
	{
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024*1024*10);
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setSizeMax(1024*1024*10);
			Iterator<FileItem> listFiles = fileUpload.parseRequest(req).iterator();
			while(listFiles.hasNext())
			{
				FileItem fileItem = listFiles.next();
				if(!fileItem.isFormField())
				{
					if(fileItem.getSize()>0)
					{
						this.fileRealName = UUID.randomUUID().toString();
						this.fileSize     = fileItem.getSize();
						String fileName   = fileItem.getName();
						fileName          = fileName.substring(fileName.lastIndexOf("\\")+1);
						this.fileSuffix   = fileName.substring(fileName.lastIndexOf(".")+1);
						this.fileName     = fileName.substring(0,fileName.lastIndexOf("."));
						System.out.println(this.fileRealPath+this.filePathAssortName + this.fileRealName + "." + this.fileSuffix);
						this.filePath     = "/WEB-INF/upLoad" + this.filePathAssortName;
						File myFile       = new File(this.fileRealPath+this.filePathAssortName );
						if(!myFile.exists()) myFile.mkdirs();
						fileItem.write(new File(myFile, this.fileRealName + "." + this.fileSuffix));
						this.insertInfor(setQueryCode , insertMapper);
						return true;
					}
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 下载信息 
	 * @return
	 */
	public void downLoad(String setQueryCode , IFileMapper  SelectByIdMapper)
	{
		
		JDBCQueryImpl query = DBQueryFactory.getQuery(setQueryCode);
		try
		{
			TCommonRecord comm  = SelectByIdMapper.SelectById(null, query);
			/**/
			String readFilepath = req.getSession().getServletContext().getRealPath(comm.get("fileWebPath"));
			/**/
			String fileName     = comm.get("fileName");
			/**/
			String FileSuffix   = comm.get("FileSuffix");
			File file = new File(readFilepath);
			if(!file.exists())
			{ 
				new RuntimeException("文件不存在！" + readFilepath);
			}
			InputStream input = new BufferedInputStream(new FileInputStream(file));
			res.setContentType("APPLICATION/OCTET-STREAM"); 
			res.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("GBK"),"ISO8859-1") + "." + FileSuffix);
			ServletOutputStream output = res.getOutputStream();
			byte[] b = new byte[4096];
			int i ;
			while((i=input.read(b))!=-1)
			{
				output.write(b);
			}
			output.flush();
			output.close();
			input.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			query = null;
		}
		
		
	/*	 
		String uploadID     = (String)req.getParameter("InputUpLoadID");
		String readPath     = null;
		String fileName     = null;
		String filesuff     = null;
		String fileRealName = null;
		try{
			String x = ("select filewebpath, filerealname ,filename ,filesuffixname,taskid from gyc_upload where uploadid ='" + uploadID + "'");
			while(!rs.eof())
			{
				readPath     = req.getSession().getServletContext().getRealPath(rs.getValue("filewebpath"));
				fileRealName = rs.getValue("filerealname");
				fileName     = rs.getValue("filename");
				filesuff     = rs.getValue("filesuffixname");
				rs.next();
			}
			this.fileName = fileName;
			this.fileSuffix = filesuff;
			input = new BufferedInputStream(new FileInputStream(new File(readPath +"\\" 
						+ fileRealName + "." + filesuff)));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			query.closeConnection();
		}
		return input;*/
	}
	
	/**
	 * 关闭流
	 * @param input
	 */
	public void inputClose()
	{
		try 
		{
			if(input != null)
				input.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param parendID 
	 * @return
	 */
	public boolean insertInfor(String setQueryCode , IFileMapper insertMapper)
	{
		boolean result = false;
		JDBCQueryImpl query = DBQueryFactory.getQuery(setQueryCode);
		try
		{
			TCommonRecord tcomm = new TCommonRecord();
			tcomm.set("fileName", this.fileName);
			tcomm.set("fileRealName", this.fileRealName);
			tcomm.set("fileSuffix", this.fileSuffix);
			tcomm.set("fileRealPath", this.fileRealPath);
			tcomm.set("filePath", this.filePath);
			tcomm.set("fileSize", String.valueOf(this.fileSize));
			result = insertMapper.InsertDataFile(tcomm, query);
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			query =  null;
		}
		return result;
		/*
		JillQuery query = new JillQuery("HTC_GYC");
		String addDate  = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		try{
			query.executeUpdate("insert into GYC_Upload(UploadID, taskID, filename, filerealname, fileSuffixName, uploadperson, uploadDate, uploadtext,fileWebPath) " +
					"values(" 
					+ "'"  + UUID.randomUUID().toString() + "'"
					+ ",'" + parendID          + "'"
					+ ",'" + this.fileName     + "'"
					+ ",'" + this.fileRealName + "'"
					+ ",'" + this.fileSuffix   + "'"
					+ ",'" + userName          + "'"
					+ ",'" + addDate           + "'"
					+ ",'" + this.fileRealPath + "'"
					+ ",'" + this.filePath     + "'"
					+ ")");
			return  true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			query.closeConnection();
		}
		*/
	}
}
