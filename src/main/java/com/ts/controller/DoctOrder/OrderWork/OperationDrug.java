package com.ts.controller.DoctOrder.OrderWork;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.DoctOrder.OrderWork.OpDrugService;
import com.ts.service.pdss.pdss.Cache.InitPdssCache;
import com.ts.util.DateUtil;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.UuidUtil;
import com.ts.util.app.AppUtil;

@Controller
@RequestMapping(value="/opDrug")
public class OperationDrug  extends BaseController{
    
	@Autowired
	private OpDrugService  opDrugService;
	@Resource(name="initPdssCache")
	private InitPdssCache  initPdss;

	/**
	 * 专家点评列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			// 当前登录专家
			User user = getCurrentUser();
			page.setPd(pd);
			List<PageData>	resultList = opDrugService.list(page);	//列出专家列表
			mv.addObject("resultList", resultList);
			mv.setViewName("DoctOrder/opDrug/list");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 专家点评列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			// 当前登录专家
			User user = getCurrentUser();
			page.setPd(pd);
			mv.setViewName("DoctOrder/opDrug/edit");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 专家点评列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = this.getPageData();
			// 当前登录专家
			User user = getCurrentUser();
			pd = opDrugService.selectOpdrugInfo(pd);
			mv.setViewName("DoctOrder/opDrug/edit");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 专家点评列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit()throws Exception{
		String errInfo = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			PageData pd = this.getPageData();
			// 当前登录专家
			User user = getCurrentUser();
			String O_ID = pd.getString("O_ID");
			pd.put("OPERATORDATE", DateUtil.getTime());
			pd.put("OPERATORUSER", user.getUSER_ID());
			if(Tools.isEmpty(O_ID)){
				pd.put("O_ID",  UuidUtil.get32UUID());
				errInfo = opDrugService.saveOpDrug(pd);
			}else{
				errInfo = opDrugService.updateOpDrug(pd);
			}
			map.put("result",errInfo);
		} catch(Exception e){
			map.put("result","操作失败");
			logger.error(e.toString(), e);
		}
		return map;
	}
	
	/**删除专家
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	public void deleteU(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除user");
		PageData pd = new PageData();
		pd = this.getPageData();
		opDrugService.deleteOpDrug(pd);
		out.write("success");
		out.close();
	}
	
	/**
	 * 批量删除
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAllU() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除user");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String USER_IDS = pd.getString("USER_IDS");
		if(null != USER_IDS && !"".equals(USER_IDS)){
			String ArrayUSER_IDS[] = USER_IDS.split(",");
			opDrugService.deleteAllOpDrug(ArrayUSER_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		map.put("result", "success");
		return AppUtil.returnObject(pd, map);
	}
	
	@RequestMapping(value="/loalRule")
    @ResponseBody
    public Object loadRule() throws Exception
    {
        Map<String,String> map = new HashMap<String,String>();
        String rsInfo = "success";
        PageData pd = this.getPageData();
        try
        {
            initPdss.setOperationDrug();
        }
        catch(Exception e ){
            logger.error(e.toString(), e);
        }
        map.put("result", rsInfo);
        return map ;
    }
	
	/**
	 * 选择数据
	 * @return
	 */
	@RequestMapping(value="/selectData")
	public ModelAndView selectData(Page page){
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = this.getPageData();
			List<PageData> list = null;
			String ONTO_TYPE = pd.getString("ONTO_TYPE");
			page.setPd(pd);
			if("100".equals(ONTO_TYPE)){
				//选择手术
				list = opDrugService.queryOperationPage(page);
			}else if ("101".equals(ONTO_TYPE)){
				//选择手术药品名称
				list = opDrugService.queryDrugMapPage(page);
			}else if ("102".equals(ONTO_TYPE)){
				//选择科室名称
				list = opDrugService.queryDeptPage(page);
			}else if ("103".equals(ONTO_TYPE)){
				//选择医生名称
				list = opDrugService.queryStaffPage(page);
			}
			mv.addObject("resultList", list);
			mv.setViewName("DoctOrder/opDrug/selectData");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
}