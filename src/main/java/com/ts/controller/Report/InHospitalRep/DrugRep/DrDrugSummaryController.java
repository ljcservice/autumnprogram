package com.ts.controller.Report.InHospitalRep.DrugRep;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.Report.InHospitalRep.AntiDrugRep.IIASPatInfoSCDService;
import com.ts.service.Report.InHospitalRep.DrugRep.IDrDrugSummaryService;
import com.ts.util.PageData;

/**
 * 药品信息
 * @author autumn
 *
 */
@Controller
@RequestMapping(value="/InHospitalRep")
public class DrDrugSummaryController extends BaseController
{
    @Resource(name="drDrugSummaryServiceBean")
    private IDrDrugSummaryService dds;
    
    @RequestMapping(value="/DRNO001UI")
    public ModelAndView DRANO001UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO001");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRNO001")
    public ModelAndView DRANO001(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            page.setPd(pd);
            List<PageData> pds = dds.DRNO001(page);
            mv.addObject("druginfos", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO001"); 
        return mv ;
    }
    
    @RequestMapping(value="/DRNO002UI")
    public ModelAndView DRANO002UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO002");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRNO002")
    public ModelAndView DRANO002(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            page.setPd(pd);
            List<PageData> pds = dds.DRNO002(page);
            mv.addObject("druginfos", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO002"); 
        return mv ;
    }
    
    @RequestMapping(value="/DRNO004UI")
    public ModelAndView DRANO004UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO004");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRNO004")
    public ModelAndView DRANO004(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            page.setPd(pd);
            List<PageData> pds = dds.DRNO004(page);
            mv.addObject("druginfos", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO004"); 
        return mv ;
    }
    
    
    @RequestMapping(value="/DRNO005UI")
    public ModelAndView DRANO005UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO005");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRNO005")
    public ModelAndView DRANO005(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            page.setPd(pd);
            List<PageData> pds = dds.DRNO005(page);
            mv.addObject("druginfos", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO005"); 
        return mv ;
    }
    
    @RequestMapping(value="/DRNO006UI")
    public ModelAndView DRANO006UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO006");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRNO006")
    public ModelAndView DRANO006(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            page.setPd(pd);
            List<PageData> pds = dds.DRNO006(page);
            mv.addObject("druginfos", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO006"); 
        return mv ;
    }
    
    @RequestMapping(value="/DRNO007UI")
    public ModelAndView DRANO007UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO007");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRNO007")
    public ModelAndView DRANO007(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            page.setPd(pd);
            List<PageData> pds = dds.DRNO007(page);
            mv.addObject("druginfos", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO007"); 
        return mv ;
    }
}
