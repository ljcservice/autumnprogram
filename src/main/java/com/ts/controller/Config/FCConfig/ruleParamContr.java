package com.ts.controller.Config.FCConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitzd.his.Utils.Config;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.Config.RuleParamService;
import com.ts.util.PageData;

/**
 * 系统参数
 * @author autumn
 *
 */
@Controller
@RequestMapping(value="/RuleParam")
public class ruleParamContr extends BaseController
{

    @Resource(name="ruleParamServiceBean")
    private RuleParamService ruleparam;
    
    @RequestMapping(value="/ruleParamViewUI")
    public ModelAndView RuleParamViewUI(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        page.setPd(pd);
        List<PageData> ruleParams = ruleparam.listRuleParam(page);
        mv.addObject("pd", pd);
        mv.addObject("ruleParams",ruleParams);
        mv.setViewName("Config/RuleParamConfig/RuleParamConfig");
        return mv;
    }
    
    @RequestMapping(value="/addRuleParamViewIU")
    public ModelAndView AddRuleParamViewIU() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("MSG", "add");
        mv.setViewName("Config/RuleParamConfig/RuleParamEdit");
        return mv;
    }
    
    @RequestMapping(value="/addRuleParam")
    public ModelAndView AddRuleParam() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("msg","success");
        pd.put("ruleid", get32UUID());
        try
        {
            this.ruleparam.insertRuleParam(pd);
            // 设置缓存参数
            Config.setParamValue(pd.getString("RULECODE"), pd.getString("RULEVALUE"));
            
        }
        catch(Exception e )
        {
            e.printStackTrace();
            mv.addObject("msg","failed");
        }
        mv.setViewName("save_result");
        return mv;
    }
    
    @RequestMapping(value="/updateRuleParamViewIU")
    public ModelAndView UpdateRuleParamViewIU() throws Exception
    {
        ModelAndView mv =  this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("MSG", "update");
        try
        {
             pd = this.ruleparam.findRuleParmByRuleCode(pd);
        }
        catch(Exception e )
        {
            e.printStackTrace();
            mv.addObject("msg","failed");
        }
        mv.addObject("pd",pd);
        mv.setViewName("Config/RuleParamConfig/RuleParamEdit");
        return mv;
    }
    
    @RequestMapping(value="/updateRuleParam")
    public ModelAndView UpdateRuleParam() throws Exception
    {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("msg","success");
        try
        {
            this.ruleparam.updateRuleParam(pd);
            // 设置缓存参数
            Config.setParamValue(pd.getString("RULECODE"), pd.getString("RULEVALUE"));
        }
        catch(Exception e )
        {
            e.printStackTrace();
            mv.addObject("msg","failed");
        }
        mv.setViewName("save_result");
        return mv;
    }
    
    
    @RequestMapping(value="/DelRuleParam")
    @ResponseBody
    public Object DelRuleParam() throws Exception
    {
        Map<String,String> map = new HashMap<String,String>();
        
        String rsInfo = "success";
        PageData pd = this.getPageData();
        try
        {
            this.ruleparam.deleteRuleParam(pd);
        }
        catch(Exception e ){
            logger.error(e.toString(), e);
        }
        map.put("result", rsInfo);
        return map ;
    }
    
    @RequestMapping(value="/isValidRuleCode")
    @ResponseBody
    public Object isValidRuleCode() throws Exception
    {
        Map<String,String> map = new HashMap<String,String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try{
            pd = this.getPageData();
            if(this.ruleparam.findRuleParmByRuleCode(pd) != null)
            {
                errInfo = "error";
            }
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        map.put("result", errInfo);         
        return map;
    }
}
