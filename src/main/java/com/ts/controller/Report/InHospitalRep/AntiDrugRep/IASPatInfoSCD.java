package com.ts.controller.Report.InHospitalRep.AntiDrugRep;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.Report.InHospitalRep.AntiDrugRep.IIASPatInfoSCDService;
import com.ts.util.PageData;

@Controller
@RequestMapping(value="/InHospitalRep")
public class IASPatInfoSCD extends BaseController
{

    @Resource(name="iASPatInfoSCDServiceBean")
    private IIASPatInfoSCDService patInfo;
    
    
    @RequestMapping(value="/DRANO01UI")
    public ModelAndView DRANO01UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO001");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO01")
    public ModelAndView DRANO01()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  patInfo.DRANO01(pd);
            PageData       pdsum = patInfo.DRANO01sum(pd);
            mv.addObject("patinfos", pds);
            mv.addObject("patinfosum",pdsum);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO001");
        return mv ;
    }
    
    @RequestMapping(value="/DRANO02UI")
    public ModelAndView DRANO002UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO002");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }
    
    @RequestMapping(value="/DRANO02")
    public ModelAndView DRANO002(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            page.setPd(pd);
            List<PageData> pds = patInfo.DRANO02(page);
            mv.addObject("patinfos", pds);
            mv.addObject("pd", pd);
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO002");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }
    
    @RequestMapping(value="/DRANO03UI")
    public ModelAndView DRANO003UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO003");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }
    
    @RequestMapping(value="/DRANO03")
    public ModelAndView DRANO003(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            page.setPd(pd);
            List<PageData> pds = patInfo.DRANO03(page);
            mv.addObject("patinfos", pds);
            mv.addObject("pd", pd);
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO003");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }

}
