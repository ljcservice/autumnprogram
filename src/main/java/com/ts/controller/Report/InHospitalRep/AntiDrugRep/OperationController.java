package com.ts.controller.Report.InHospitalRep.AntiDrugRep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
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
    
    @RequestMapping(value="/DRANO007UI")
    public ModelAndView DRANO07UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO007");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO007")
    public ModelAndView DRANO07(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            page.setPd(pd);
            List<PageData> pds =  oper.DRANO007(page);
            mv.addObject("opers", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO007");
        return mv ;
    }
    
    @RequestMapping(value="DRANO07_Ajax01")
    @ResponseBody
    public Object DRANO07_01() throws Exception{
        PageData pd = this.getPageData();
        Map<String, Object> map = new HashMap<String , Object>();
        
        try
        {
            oper.updateDRANO007(pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
            map.put("result", "no");
            return map;
        }
        map.put("result", "ok");
        return map;
    }
    
    @RequestMapping(value="/DRANO008UI")
    public ModelAndView DRANO08UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO008");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO008")
    public ModelAndView DRANO08()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  oper.DRANO008(pd);
            mv.addObject("opers", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO008");
        return mv ;
    }
    
    @RequestMapping(value="/DRANO009UI")
    public ModelAndView DRANO09UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO009");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO009")
    public ModelAndView DRANO09()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  oper.DRANO009(pd);
            mv.addObject("opers", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO009");
        return mv ;
    }
}
