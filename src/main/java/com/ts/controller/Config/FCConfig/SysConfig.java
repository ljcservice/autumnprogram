package com.ts.controller.Config.FCConfig;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.pdss.Message.CheckBeanMessage;
import com.ts.util.PageData;



@Controller
@RequestMapping(value="/SysConfig")
public class SysConfig extends BaseController
{

    @RequestMapping(value="/SysConfigViewUI")
    public ModelAndView SysConfigViewUI(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        page.setPd(pd);
        Map<String , CheckBeanMessage> mapCBM = CheckBeanMessage.getCheckBeanMesage();
        Iterator<CheckBeanMessage> iCBMs = mapCBM.values().iterator();
        mv.addObject("pd", pd);
        mv.addObject("iCBMs", iCBMs);
        mv.addObject("counter",mapCBM.size());
//        mv.addObject("ruleParams",ruleParams);
        mv.setViewName("Config/SysConfig/SysConfig");
        return mv;
    }

    @RequestMapping(value="/SysConfigClean")
    public ModelAndView SysConfigClean(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        CheckBeanMessage.clean();
        return SysConfigViewUI(page);
    }
    
}
