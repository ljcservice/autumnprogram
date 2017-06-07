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
public class IASPatInfoSCDController extends BaseController
{

    @Resource(name="iASPatInfoSCDServiceBean")
    private IIASPatInfoSCDService patInfo;
    
    
    @RequestMapping(value="/DRANO001UI")
    public ModelAndView DRANO001UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO001");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO001")
    public ModelAndView DRANO001()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  patInfo.DRANO001(pd);
            PageData       pdsum = patInfo.DRANO001sum(pd);
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
    
    @RequestMapping(value="/DRANO002UI")
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
    
    @RequestMapping(value="/DRANO002")
    public ModelAndView DRANO002(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            page.setPd(pd);
            List<PageData> pds = patInfo.DRANO002(page);
            mv.addObject("patinfos", pds);
            mv.addObject("pd", pd);
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO002");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }
    
    @RequestMapping(value="/DRANO003UI")
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
    
    @RequestMapping(value="/DRANO003")
    public ModelAndView DRANO003(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            page.setPd(pd);
            List<PageData> pds = patInfo.DRANO003(page);
            mv.addObject("patinfos", pds);
            mv.addObject("pd", pd);
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO003");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }

    
    @RequestMapping(value="/DRANO005UI")
    public ModelAndView DRANO005UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO005");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO005")
    public ModelAndView DRANO005()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  patInfo.DRANO005(pd);
            PageData       pdsum = patInfo.DRANO005sum(pd);
            mv.addObject("patinfos", pds);
            mv.addObject("patinfosum",pdsum);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO005");
        return mv ;
    }
    
    @RequestMapping(value="/DRANO006UI")
    public ModelAndView DRANO006UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO006");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO006")
    public ModelAndView DRANO006()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  patInfo.DRANO006(pd);
            PageData       pdsum = patInfo.DRANO006sum(pd);
            mv.addObject("patinfos", pds);
            mv.addObject("patinfosum",pdsum);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO006");
        return mv ;
    }
    
    
    /**
     * 科室费用占比统计 UI
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/DRNO003UI")
    public ModelAndView DRNO003UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO003");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ; 
    }
    
    
    /**
     * 科室费用占比统计
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/DRNO003")
    public ModelAndView DRNO003()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  patInfo.DRNO003(pd);
            mv.addObject("patinfos", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO003");
        return mv ;
    }
    
    
    /**
     * 科室费用占比统计 UI
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/DRNO008UI")
    public ModelAndView DRNO008UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO008");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ; 
    }
    
    
    /**
     * 科室费用占比统计
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/DRNO008")
    public ModelAndView DRNO008()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  patInfo.DRNO008(pd);
            mv.addObject("patinfos", pds);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/DrugRep/DRNO008");
        return mv ;
    }
    
    
    /**
     * 病原学送检统计-科室 
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/DRANO010UI")
    public ModelAndView DRANO010UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO010");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    
    /**
     * 病原学送检统计-科室 
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/DRANO010")
    public ModelAndView DRANO010()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  patInfo.DRANO010(pd);
            PageData       pdsum = patInfo.DRANO010sum(pd);
            mv.addObject("patinfos", pds);
            mv.addObject("patinfosum",pdsum);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO010");
        return mv ;
    }
    
    
    /**
     * 病原学送检统计-医生
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/DRANO011UI")
    public ModelAndView DRANO011UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO011");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    /**
     * 病原学送检统计-医生 
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/DRANO011")
    public ModelAndView DRANO011(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            page.setPd(pd);
            List<PageData> pds =  patInfo.DRANO011(page);
            PageData       pdsum = patInfo.DRANO011sum(pd);
            mv.addObject("patinfos", pds);
            mv.addObject("patinfosum",pdsum);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO011");
        return mv ;
    }
    
    @RequestMapping(value="/DRANO012UI")
    public ModelAndView DRANO012UI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO012");
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv ;
    }
    
    @RequestMapping(value="/DRANO012")
    public ModelAndView DRANO012()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try
        {
            List<PageData> pds =  patInfo.DRANO012(pd);
            PageData       pdsum = patInfo.DRANO012sum(pd);
            mv.addObject("patinfos", pds);
            mv.addObject("patinfosum",pdsum);
            mv.addObject("pd", pd);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("Reprot/InHospitalRep/AntiDrugRep/DRANO012");
        return mv ;
    }
    
}
