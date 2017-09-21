package com.ts.controller.Report.InHospitalRep.AntiDrugRep;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.Report.InHospitalRep.AntiDrugRep.IAntiDrugUseService;
import com.ts.util.PageData;
/**
 * 
 * @author autumn
 *
 */
@Controller
@RequestMapping(value="/InHospitalRep")
public class AntiDrugUseController extends BaseController
{
    @Resource(name="antiDrugUseServiceBean")
    private IAntiDrugUseService adu;
    
    @RequestMapping(value="/DRANO013UI")  
    public ModelAndView DRANO013UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("DoctOrder/report/antiDrugUse/antiDrugUseList");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }
    
    @RequestMapping(value="/DRANO013")
    public ModelAndView DRANO013(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        page.setPd(pd);
        try
        {
            List<PageData> pds =  adu.DRANO013(page);
            PageData       pdsum = adu.DRANO013Sum(pd); 
            mv.addObject("antiUses", pds);
            mv.addObject("antiUseSum",pdsum); 
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("DoctOrder/report/antiDrugUse/antiDrugUseList");
        return mv ;
    }
    
    @RequestMapping(value="/DRANO014UI")
    public ModelAndView DRANO014UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("DoctOrder/report/antiDrugUse/antiDrugUsePatList");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }
    
    @RequestMapping(value="/DRANO014")
    public ModelAndView DRANO014(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        page.setPd(pd);
        try
        {
            List<PageData> pds = adu.DRANO014(page);
            mv.addObject("antiUses", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("DoctOrder/report/antiDrugUse/antiDrugUsePatList");
        return mv ;
    }
}
