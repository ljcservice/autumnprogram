package com.ts.controller.Report.InHospitalRep.AntiDrugRep;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.service.Report.InHospitalRep.AntiDrugRep.IOperationService;
import com.ts.util.PageData;

@Controller
@RequestMapping(value="/InHospitalRep")
public class OperationController extends BaseController
{

    @Resource(name="operationServiceBean")
    private IOperationService oper;
    
    
    @RequestMapping(value="/DRANO004UI")
    public ModelAndView DRANO01UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO004");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO004")
    public ModelAndView DRANO01()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  oper.DRANO004(pd);
            mv.addObject("opers", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO004");
        return mv ;
    }
}
