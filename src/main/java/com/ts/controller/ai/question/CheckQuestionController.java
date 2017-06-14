package com.ts.controller.ai.question;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.entity.ai.Total;
import com.ts.entity.system.User;
import com.ts.plugin.MTSHttpService;
import com.ts.service.ai.manager.AIManager;
import com.ts.service.ai.question.manager.QueryQuestionManager;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.ai.AIConst;
import com.ts.util.ai.AILogs;
import com.ts.util.app.AppUtil;

/**
 * 问题单校验 -- 
 * @ClassName: CheckQuestionController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xsl 
 * @date 2016年12月30日 上午11:22:42 
 *
 */
@Controller
@RequestMapping(value="/checkQuestion")
public class CheckQuestionController  extends BaseController {
	
	@Resource(name="queryQuestionService")
	private QueryQuestionManager queryQuestionService;
	
	@Resource(name="aiService")
	private AIManager aiService;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;
	
	@RequestMapping(value="/listQuestion")
	public ModelAndView listQuestion(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			//设置数据来源列表
			pd.put("dt_id",AIConst.AI_DICT_ORIGIN );
			List<PageData>	originList = aiService.dictList(pd);
			mv.addObject("originList", originList);
			if(Tools.isEmpty(pd.getString("ORIGIN_ID"))){
				//菜单进入时无查询条件，ORIGIN_ID默认测试医院
				pd.put("ORIGIN_ID",originList.get(0).get("D_KEY"));
			}
			User u = getCurrentUser();
			//判断当前用户是否正在执行mts校验
			String BATCH_NUMBER = Const.QUESTION_MTS_CHECK.get(u.getUSER_ID());
			if(BATCH_NUMBER!=null){
				mv.addObject("CHECKING", 1);
				//判断当前用户是否正在执行mts校验
				Total t = Const.QUESTION_MTS_BATCH_NUMBER.get(BATCH_NUMBER);
				if(t!=null){
					//计算进度百分比
					if(((t.getNum()*100)/t.getCount() )>= 100){
						mv.addObject("CHECKING", 0);
					}
				}
			}else{
				mv.addObject("CHECKING", 0);
			}
			
			//查询MTS 更新信息如果没有更新信息则不执行查询
			PageData obj = queryQuestionService.queryMtsInfo();
			if(obj != null && obj.get("UPDATE_TIME")!=null){
				pd.put("update_time", obj.get("UPDATE_TIME"));
				List<PageData>	qList = queryQuestionService.checkList(pd);	//列出问题单列表
				//过滤正在执行校验的批次号
				if(Const.QUESTION_MTS_CHECK.size()>0){
					for(PageData p:qList){
						String batch_number = p.getString("batch_number");
						if(Const.QUESTION_MTS_CHECK.containsValue(batch_number)){
							p.put("checking", 1);
						}else{
							p.put("checking", 0);
						}
					}
				}
				mv.addObject("qList", qList);
			}else{
				throw new Exception("table ai_mts_info no data!");
			}
				
			//如果正在刷新mts
			if(((BigDecimal)obj.get("RELOADING")).intValue()==1){
				//调用MTS是否重启成功接口
				boolean flag  = MTSHttpService.checkReLoadMTS();
				if(flag){
					//更新ai_mts_info数据
					PageData m = new PageData();
					m.put("RELOADING", 0);
					obj.put("RELOADING", 0);
					queryQuestionService.updateMtsInfo(m);
				}
			}

			mv.addObject("update_time", obj.get("UPDATE_TIME"));
			mv.addObject("RELOADING", obj.get("RELOADING"));
			mv.setViewName("ai/question/check_list");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 刷新MTS数据
	 * @Title: refreshMTS 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/refreshMTS")
	@ResponseBody
	public Object refreshMTS() throws Exception {
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"刷新MTS",dao, null);
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			Date date = new Date();
			//检测MTS 是否正在执行刷新
			PageData mts = queryQuestionService.queryMtsInfo();
			if(((BigDecimal)mts.get("RELOADING")).intValue()==0){
				pd.put("RELOADING", 1);
				queryQuestionService.updateMtsInfo(pd);
				//调用MTS刷新接口
				MTSHttpService.reLoadMTS();
				logger.info(Jurisdiction.getUsername()+"刷新MTS");
				pd.put("UPDATE_MAN", getCurrentUser().getUSERNAME());
			}
			while(true){
				Thread.sleep(10000);
				//调用MTS是否重启成功接口
				boolean flag  = MTSHttpService.checkReLoadMTS();
				if(flag){
					//更新ai_mts_info数据
					pd.put("update_time", date);
					pd.put("reloading", 0);
					queryQuestionService.updateMtsInfo(pd);
					map.put("result", "success");
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			map.put("result", "操作失败！");
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * MTS验证
	 * @Title: MTSCheck 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/MTSCheck")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Object MTSCheck() throws Exception {
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"调用MTS验证!",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"调用MTS验证");
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = getPageData();
		//检测MTS 是否正在执行刷新
		PageData mts = queryQuestionService.queryMtsInfo();
		if(((BigDecimal)mts.get("RELOADING")).intValue()==1){
			map.put("result", "MTS正在刷新中。请稍后操作！");
			return AppUtil.returnObject(pd, map);
		}
		map.put("result", "success");
		StringBuffer  sb = new StringBuffer("未匹配名称：");
		List<PageData> errorList = new ArrayList<PageData>();
		try {
			pd = this.getPageData();
			Page page = new Page();
			page.setCurrentPage(1);
			page.setShowCount(1000);
			page.setPd(pd);
			//根据批次号获取问题的列表
			if(Tools.isEmpty(pd.getString("BATCH_NUMBER")) || Tools.isEmpty(pd.getString("ORIGIN_ID"))){
				return null;
			}
			//放入更新mts标识
			Const.QUESTION_MTS_CHECK.put(getCurrentUser().getUSER_ID(),pd.getString("BATCH_NUMBER"));
			List<PageData> list = queryQuestionService.originalQuestionPage(page);
			//存放进度
			Total t = new Total(0,list.size());
			Const.QUESTION_MTS_BATCH_NUMBER.put(pd.getString("BATCH_NUMBER"), t);
			//循环校验
			MTSHttpService.checkQuestion(list,sb,errorList,t);
			
			for(int index =2; index<=page.getTotalPage();index++){
				page.setCurrentPage(index);
				list = queryQuestionService.originalQuestionPage(page);
				//循环校验
				MTSHttpService.checkQuestion(list,sb,errorList,t);
			}
			if(!CollectionUtils.isEmpty(errorList)){
				//存在错误，重置问题单
				queryQuestionService.reOpenQuestion(errorList);
				StringBuffer  msg = new StringBuffer("<b>错误的问题单已经回退至一审状态，请重新编码，谢谢。</b><br/>错误问题单数量："+errorList.size());
				msg.append("。").append(sb);
				map.put("msg", msg.toString());
			}else{
				//无问题更新原始问题单为已经验正
				queryQuestionService.updateOriginalQue(pd.getString("BATCH_NUMBER"));
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			map.put("result", "操作失败！请稍后重试。");
		} finally {
			//清除进度
			Const.QUESTION_MTS_CHECK.remove(getCurrentUser().getUSER_ID());
			Const.QUESTION_MTS_BATCH_NUMBER.remove(pd.getString("BATCH_NUMBER"));
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}
	/**
	 * 
	 * @Title: 查询mtscheck进度
	 * @throws
	 */
	@RequestMapping(value="/queryMTSCheck")
	@ResponseBody
	public Object queryMTSCheck() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"查询MTS验证进度");
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map.put("result", "success");
			User u = getCurrentUser();
			//判断当前用户是否正在执行mts校验
			String BATCH_NUMBER = Const.QUESTION_MTS_CHECK.get(u.getUSER_ID());
			if(BATCH_NUMBER!=null){
				Total t = Const.QUESTION_MTS_BATCH_NUMBER.get(BATCH_NUMBER);
				if(t!=null){
					//计算进度百分比
					map.put("progress", (t.getNum()*100)/t.getCount());
				}else{
					map.put("progress", 1);
				}
			}else{
				map.put("progress", 1);
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			map.put("result", "操作失败！请稍后重试。");
		}
		return AppUtil.returnObject(pd, map);
	}
}