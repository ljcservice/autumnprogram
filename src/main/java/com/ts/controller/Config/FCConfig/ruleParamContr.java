package com.ts.controller.Config.FCConfig;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    
    @RequestMapping(value="/AddRuleParam")
    public ModelAndView AddRuleParam() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        
        mv.setViewName("");
        return mv;
    }
    
    @RequestMapping(value="/UpdateRuleParamViewIU")
    public ModelAndView UpdateRuleParamViewIU() throws Exception
    {
        ModelAndView mv =  this.getModelAndView();
        PageData pd = this.getPageData();
        
        
        mv.setViewName("");
        return mv;
    }
    
    @RequestMapping(value="/UpdateRuleParam")
    public ModelAndView UpdateRuleParam() throws Exception
    {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        
        mv.setViewName("");
        return mv;
    }
    
    
    @RequestMapping(value="/DelRuleParam")
    public ModelAndView DelRuleParam() throws Exception
    {
        ModelAndView  mv = this.getModelAndView();
        PageData pd = this.getPageData();
        
        mv.setViewName("");
        return mv ;
    }
}
