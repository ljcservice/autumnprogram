package com.ts.controller.ai.question;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.manager.AIManager;
import com.ts.service.ai.question.manager.QueryQuestionManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.ai.AICommMethod;
import com.ts.util.ai.AIConst;
import com.ts.util.ai.AILogs;
import com.ts.util.app.AppUtil;

/**
 * 问题单查询
 * @ClassName: QueryQuestionController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年12月1日 上午11:06:35 
 *
 */
@Controller
@RequestMapping(value="/queryQuestion")
public class QueryQuestionController  extends BaseController {
	
	
	@Resource(name="queryQuestionService")
	private QueryQuestionManager queryQuestionService;
	
	@Resource(name="aiService")
	private AIManager aiService;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;
	
	@RequestMapping(value="/listQuestion")
	public ModelAndView listQuestion(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			//queryType
			if(AICommMethod.isNotEmpty(pd.get("queryType"))&&"1".equals(pd.get("queryType").toString().trim())){//需要转码
				String BATCH_NUMBER=new String(pd.getString("BATCH_NUMBER").getBytes("ISO-8859-1"),"UTF-8").trim();
				pd.put("BATCH_NUMBER",BATCH_NUMBER);
			}
			String createStart = pd.getString("createStart");	//开始时间
			String createEnd = pd.getString("createEnd");		//结束时间
			if(createStart != null && !"".equals(createStart)){
				pd.put("createStart", createStart+" 00:00:00");
			}
			if(createEnd != null && !"".equals(createEnd)){
				pd.put("createEnd", createEnd+" 23:59:59");
			} 
			
			page.setPd(pd);
			List<PageData>	qList = queryQuestionService.questionList(page);	//列出问题单列表
			
			//设置数据来源列表
			pd.put("dt_id",AIConst.AI_DICT_ORIGIN );
			List<PageData>	originList = aiService.dictList(pd);
			
			mv.addObject("originList", originList);
			mv.setViewName("ai/question/question_list");
			mv.addObject("qList", qList);
			pd.put("createStart", createStart);
			pd.put("createEnd",createEnd );
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 问题单的字问题单及子问题单处理结果信息
	 * @Title: qInfo 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/qInfo")
	public ModelAndView qInfo()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		pd.put("o_diag_name", new String(pd.getString("o_diag_name").getBytes("ISO-8859-1"),"UTF-8").trim());
		pd.put("nlp_diag_name", new String(pd.getString("nlp_diag_name").getBytes("ISO-8859-1"),"UTF-8").trim());
		pd.put("mts_code",new String(pd.getString("mts_code").getBytes("ISO-8859-1"),"UTF-8").trim());
		//根据问题单的ID查询NLP切词结果信息
		PageData nlp_pd=queryQuestionService.getNLPRs(pd);
		
		
		List<PageData> child_list=queryQuestionService.childList(pd);//子问题单列表

		List<PageData> res_list=queryQuestionService.resList(pd);;//处理结果列表
		
		mv.setViewName("ai/question/q_info");
		mv.addObject("child_list", child_list);
		mv.addObject("res_list", res_list);
		mv.addObject("pd", pd);
		mv.addObject("nlp_pd", nlp_pd);
		return mv;
	}
	/**
	 * 
	 * @Title: MTSCheck 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/MTSCheck")
	@ResponseBody
	public Object MTSCheck() throws Exception {
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"调用MTS验证!",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"调用MTS验证");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String P_IDS = pd.getString("P_IDS");
			if(null != P_IDS && !"".equals(P_IDS)){
				String ArrayP_IDS[] = P_IDS.split(",");
				map.put("result", "此部分信息后期处理!");
					
			}else{
				map.put("result", "此部分信息后期处理!");
			}
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
