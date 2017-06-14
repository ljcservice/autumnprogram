package com.ts.controller.Report.InHospitalRep.DrugRep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.Report.InHospitalRep.AntiDrugRep.IIASPatInfoSCDService;
import com.ts.service.Report.InHospitalRep.DrugRep.IDrDrugSummaryService;
import com.ts.util.ObjectExcelView;
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
    @RequestMapping(value="/DRNO001Export")
    public ModelAndView DRNO001Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("药名");
			titles.add("规格");
			titles.add("厂家");
			titles.add("金额");
			titles.add("使用量");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = dds.DRNO001(page);
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", i+1);	//2
					vpd.put("var2", varOList.get(i).get("drug_name"));	//4
					vpd.put("var3", varOList.get(i).get("drug_spec"));		//5
					vpd.put("var4", varOList.get(i).get("firm_id"));
					vpd.put("var5", "￥ "+varOList.get(i).get("sumcosts").toString());
					vpd.put("var6", varOList.get(i).get("sumamount").toString()+varOList.get(i).get("drug_units").toString());
					varList.add(vpd);
				}
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
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
    @RequestMapping(value="/DRNO002Export")
    public ModelAndView DRNO002Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("药名");
			titles.add("规格");
			titles.add("厂家");
			titles.add("金额");
			titles.add("使用量");
			titles.add("科室");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = dds.DRNO002(page);
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", i+1);	//2
					vpd.put("var2", varOList.get(i).get("drug_name"));	//4
					vpd.put("var3", varOList.get(i).get("drug_spec"));		//5
					vpd.put("var4", varOList.get(i).get("firm_id"));
					vpd.put("var5", varOList.get(i).get("costs"));
					vpd.put("var6", varOList.get(i).get("amount").toString()+varOList.get(i).get("drug_units").toString());
					vpd.put("var7", varOList.get(i).get("dept_name"));
					 varList.add(vpd);
				}
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
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
    @RequestMapping(value="/DRNO004Export")
    public ModelAndView DRNO004Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("药名");
			titles.add("规格");
			titles.add("厂家");
			titles.add("金额");
			titles.add("使用量");
			titles.add("科室");
			titles.add("医生");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = dds.DRNO004(page);
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", i+1);	//2
					vpd.put("var2", varOList.get(i).get("drug_name"));	//4
					vpd.put("var3", varOList.get(i).get("drug_spec"));		//5
					vpd.put("var4", varOList.get(i).get("firm_id"));
					vpd.put("var5", "￥ "+varOList.get(i).get("sumcosts").toString());
					vpd.put("var6", varOList.get(i).get("sumamount").toString()+varOList.get(i).get("drug_units").toString());
					vpd.put("var7", varOList.get(i).get("dept_name"));
					vpd.put("var8", varOList.get(i).get("doctor_name"));
					 varList.add(vpd);
				}
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
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
    @RequestMapping(value="/DRNO005Export")
    public ModelAndView DRNO005Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("医生");
			titles.add("金额");
			titles.add("科室");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = dds.DRNO005(page);
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", i+1);
					vpd.put("var2", varOList.get(i).get("doctor_name"));
					vpd.put("var3", "￥ "+varOList.get(i).get("costs").toString());
					vpd.put("var4", varOList.get(i).get("dept_name"));
					 varList.add(vpd);
				}
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
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
    @RequestMapping(value="/DRNO006Export")
    public ModelAndView DRNO006Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("药名");
			titles.add("规格");
			titles.add("科室");
			titles.add("医生");
			titles.add("金额");
			titles.add("数量");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = dds.DRNO006(page);
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", i+1);	//2
					vpd.put("var2", varOList.get(i).get("drug_name"));	//4
					vpd.put("var3", varOList.get(i).get("drug_spec"));		//5
					vpd.put("var4", varOList.get(i).get("dept_name"));
					vpd.put("var5", varOList.get(i).get("doctor_name"));
					vpd.put("var6", "￥ "+varOList.get(i).get("costs").toString());
					vpd.put("var7", varOList.get(i).get("amount").toString()+varOList.get(i).get("drug_units").toString());
					 varList.add(vpd);
				}
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
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
    }@RequestMapping(value="/DRNO007Export")
    public ModelAndView DRNO007Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("药名");
			titles.add("规格");
			titles.add("厂家");
			titles.add("金额");
			titles.add("数量");
			titles.add("日期");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = dds.DRNO007(page);
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", i+1);	//2
					vpd.put("var2", varOList.get(i).get("drug_name"));	//4
					vpd.put("var3", varOList.get(i).get("drug_spec"));		//5
					vpd.put("var4", varOList.get(i).get("firm_id"));
					vpd.put("var5", "￥ "+varOList.get(i).get("costs").toString());
					vpd.put("var6", varOList.get(i).get("amount").toString()+varOList.get(i).get("drug_units").toString());
					vpd.put("var7", varOList.get(i).get("rpt_date"));
					 varList.add(vpd);
				}
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
        }
        catch(Exception e)
        {
            logger.error(e.toString(), e);
        }
        return mv ;
    }
}
