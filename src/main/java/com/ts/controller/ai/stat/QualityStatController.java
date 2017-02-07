package com.ts.controller.ai.stat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.ai.manager.AIManager;
import com.ts.service.ai.stat.manager.QualityStatManager;
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;
import com.ts.util.ai.AIConst;

/**
 * 质量统计
 * @ClassName: QualityStatController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月23日 下午3:54:31 
 *
 */
@Controller
@RequestMapping(value="/qualityStat")
public class QualityStatController  extends BaseController {
	
	
	@Resource(name="qualityStatService")
	private QualityStatManager qualityStatService;
	
	@Resource(name="aiService")
	private AIManager aiService;
	
	/**
	 * 质量统计列表
	 * @Title: sList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/sList")
	public ModelAndView sList(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			
			List<PageData>	sList = qualityStatService.sList(page);	
			mv.addObject("sList", sList);//列表信息
			
			//设置任务类型及任务子类型列表
			pd.put("dt_id",AIConst.AI_DICT_TASK_TYPE );
			List<PageData>	taskTypeList = aiService.dictList(pd);	
			pd.put("dt_id",AIConst.AI_DICT_TASK_TYPE_CHILD );
			List<PageData>	taskTypeChildList = aiService.dictList(pd);
			
			mv.addObject("taskTypeList", taskTypeList);
			mv.addObject("taskTypeChildList", taskTypeChildList);
			mv.addObject("pd", pd);
			mv.setViewName("ai/stat/quality_stat");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**导出统计信息到EXCEL
	 * @return
	 */
	@RequestMapping(value="/excel")
	@Rights(code="qualityStat/excel")
	public ModelAndView exportExcel(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
				String keywords = pd.getString("keywords");				//关键词检索条件
				if(null != keywords && !"".equals(keywords)){
					pd.put("keywords", keywords.trim());
				}
				String ALLOT_START = pd.getString("ALLOT_START");	//开始时间
				String ALLOT_END = pd.getString("ALLOT_END");		//结束时间
				if(ALLOT_START != null && !"".equals(ALLOT_START)){
					pd.put("ALLOT_START", ALLOT_START+" 00:00:00");
				}
				if(ALLOT_END != null && !"".equals(ALLOT_END)){
					pd.put("ALLOT_END", ALLOT_END+" 00:00:00");
				} 
				Map<String,Object> dataMap = new HashMap<String,Object>();
				List<String> titles = new ArrayList<String>();
				titles.add("责任人"); 		//1
				titles.add("任务类型");  		//2
				titles.add("任务子类型");			//3
				titles.add("任务步骤");			//4
				titles.add("分配总数");			//5
				titles.add("未处理");			//6
				titles.add("已处理");		//7
				titles.add("已提交");	//8
				titles.add("采纳率");	//9
				dataMap.put("titles", titles);
				List<PageData> sList =  qualityStatService.sListAll(pd);
				List<PageData> varList = new ArrayList<PageData>();
				for(int i=0;i<sList.size();i++){
					PageData vpd = new PageData();
					String step=sList.get(i).get("STEP").toString();
					int adopt_count=Integer.valueOf(sList.get(i).get("adopt_count").toString());
					int x2_count=Integer.valueOf(sList.get(i).get("x2_count").toString());
					vpd.put("var1", sList.get(i).get("DUTY_USER"));				//1
					vpd.put("var2", sList.get(i).get("TASK_TYPE"));				//2
					vpd.put("var3", sList.get(i).get("TASK_TYPE_CHILD"));		//3
					if("1".equals(step))
						vpd.put("var4", "一审");	
					else if("2".equals(step))
						vpd.put("var4", "二审");	//4
					vpd.put("var5", sList.get(i).get("all_count").toString());				//5
					vpd.put("var6", sList.get(i).get("x0_count").toString());				//6
					vpd.put("var7", sList.get(i).get("x1_count").toString());				//7
					vpd.put("var8", x2_count);									//8
					if("2".equals(step))
						vpd.put("var9","100%");									//9
					else if("1".equals(step) && adopt_count==0)
						vpd.put("var9","0%");									//9
					else if("1".equals(step) && adopt_count!=0)
						vpd.put("var9",String.format("%.2f",adopt_count*100.00/ x2_count)+"%");		//9
					varList.add(vpd);
				}
				dataMap.put("varList", varList);
				ObjectExcelView erv = new ObjectExcelView();					//执行excel操作
				mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
}
