package com.ts.controller.ai.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.question.manager.ImportLogManager;
import com.ts.util.Const;
import com.ts.util.FileDownload;
import com.ts.util.FileUpload;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.PathUtil;
import com.ts.util.ai.AILogs;
import com.ts.util.app.AppUtil;

/**
 * 问题单导入日志
 * @ClassName: ImportLogController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年10月28日 下午1:53:51 
 *
 */
@Controller
@RequestMapping(value="/importLog")
public class ImportLogController  extends BaseController {
	
	
	@Resource(name="importLogService")
	private ImportLogManager importLogService;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;
	
	@RequestMapping(value="/listImportLogs")
	public ModelAndView listTaskParam(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			List<PageData>	logList = importLogService.logList(page);	//列出导入日志列表
			mv.setViewName("ai/question/importlog_list");
			mv.addObject("logList", logList);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**打开上传问题单页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadQ")
	@Rights(code="importLog/goUploadQ")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("ai/question/uploadq");
		return mv;
	}
	/**文件导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readFile")
	@Rights(code="importLog/readFile")
	public ModelAndView readFile(
			@RequestParam(value="filename",required=false) MultipartFile file
			) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;	//文件上传路径
			String OriginalFileName=file.getOriginalFilename();
			
			//判断同名文件是否已经导入过
			if(importLogService.getLogByFileName(OriginalFileName )!=null){
				logger.error("文件"+OriginalFileName+"已经导入过，请选择其他文件进行导入!");
				mv.addObject("msg","文件"+OriginalFileName+"已经导入过，请选择其他文件进行导入!");
				mv.setViewName("save_result");
				return mv;
			}
			
			OriginalFileName=OriginalFileName.substring(0, OriginalFileName.lastIndexOf('.'));//去掉扩展名
			
			String fileName =  FileUpload.fileUp(file, filePath,OriginalFileName );		//执行上传
			logger.info("上传文件，名称："+OriginalFileName+" 路径："+filePath);
			
			String flag=importLogService.importData2Base(filePath,fileName,logger);
//			importLogService.importData2Base(fileName);
			
//			//写入导入日志信息
//			PageData logpd=new PageData();
//			logpd.put("FILE_NAME", fileName);
//			logpd.put("STATUS", 0);
//			logpd.put("SUC_COUNT", 0);
//			logpd.put("FAIL_COUNT", 0);
//			logpd.put("IMPORT_USER", Jurisdiction.getCurrentUser().getUSER_ID());
//			int logid=importLogService.addLog(logpd); 
//			logpd.put("LOG_ID", logid);
//			logger.info("导入文件"+filePath+fileName+"开始");
//			//写入原始数据表			
//			List<PageData> listPd = (List)ObjectTxtRead.readTxt(filePath, fileName);
//			
//			pd.put("IMPORT_ID", logid);					//日志ID
//			int sucCount=0;
//			int failCount=0;
//			
//			for(int i=0;i<listPd.size();i++){		
//				pd.put("ORIGIN_ID", listPd.get(i).getString("var0"));	//来源ID
//				pd.put("MTS_RULE_ID", listPd.get(i).getString("var1"));	
//				pd.put("CLM_ID", listPd.get(i).getString("var2"));	
//				pd.put("CLM_TYPE", listPd.get(i).getString("var3"));
//				pd.put("O_DIAG_NAME", listPd.get(i).getString("var4"));
//				pd.put("O_DIAG_CODE", listPd.get(i).getString("var5"));
//				pd.put("NLP_DIAG_NAME", listPd.get(i).getString("var6"));
//				pd.put("MTS_DIAG_CODE", listPd.get(i).getString("var7"));
//				pd.put("TASK_TYPE_ID",listPd.get(i).getString("var8"));
//				pd.put("TASK_TYPE_CHILD_ID",listPd.get(i).getString("var9"));
//				pd.put("CREATE_MAN", Jurisdiction.getCurrentUser().getUSER_ID());
//				pd.put("UPDATE_MAN", Jurisdiction.getCurrentUser().getUSER_ID());
//				
//				//判断主问题单是否存在诊断名称，存在则将问题单的ID取出 否则将插入到问题单中
//				pd.put("Q_STATUS", 0);//问题单的状态
//				
//				int q_id=importLogService.checkQ(pd);
//				if(q_id == 0){
//					logger.error("问题单"+pd.getString("O_DIAG_NAME")+"创建失败!");
//					failCount++;
//					continue;
//				}else{
//					pd.put("Q_ID", q_id);
//				}
//				importLogService.saveO(pd);//保存原始数据
//				sucCount++;
//				logger.info("原始数据"+pd.get("O_DIAG_NAME")+"导入成功");
//			}
//			//修改导入的日志
//			logpd.put("SUC_COUNT", sucCount);
//			logpd.put("FAIL_COUNT", failCount);
//			logpd.put("STATUS", 1);//导入成功
//			importLogService.editLog(logpd);
//			
//			logger.info("导入文件"+filePath+fileName+"结束");
			flag=flag.replace("\"","\\\"");
			mv.addObject("msg",flag);
		}
		mv.setViewName("save_result");
		return mv;
	}
	/**下载模版 问题单的实例文件
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downModel")
	public void downExcel(HttpServletResponse response)throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"下载模板",dao, null);
		logger.info(Jurisdiction.getUsername()+"下载模板");
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "MtsQuestion-Template.txt", "MtsQuestion-Template.txt");
	}
	
	
	/**
	 * 撤销导入
	 * @Title: deleteImport 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/deleteImport")
	@ResponseBody
	public Object deleteImport() throws Exception {
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"撤销文件导入",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"撤销文件导入");
		String res="success";
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			
			res=importLogService.deleteImport(pd);
			
			map.put("result", res);
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}
	
}
