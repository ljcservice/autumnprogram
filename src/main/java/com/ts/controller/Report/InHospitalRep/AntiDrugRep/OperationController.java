package com.ts.controller.Report.InHospitalRep.AntiDrugRep;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.ts.util.ObjectExcelView;
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
    @RequestMapping(value="/DRANO004Export")
    public ModelAndView DRANO004Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			if("count".equals(pd.getString("findType"))){
				titles.add("手术总例数");
				titles.add("使用抗菌药例数");
			}else{
				titles.add("手术总人次");
				titles.add("使用抗菌药人次数");
			}
			titles.add("比例");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
				List<PageData> varOList = oper.DRANO004(pd);
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", varOList.get(i).get("dept_name"));	//4
				vpd.put("var3", varOList.get(i).get("coun"));		//5
				vpd.put("var4", varOList.get(i).get("anti"));
				vpd.put("var5", ((BigDecimal)varOList.get(i).get("ratAnti")).doubleValue()*100 );
				varList.add(vpd);
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
    @RequestMapping(value="/DRANO007Export")
    public ModelAndView DRANO007Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            page.setShowCount(1000);
            mv.addObject("pd", pd);
            List<String> titles = new ArrayList<String>();
			titles.add("患者姓名");
			titles.add("手术式");
			titles.add("医生姓名");
			titles.add("住院科室");
			titles.add("手术日期");
			titles.add("原切口类型");
			titles.add("现切口类型");
			titles.add("抗菌药");
			titles.add("联合用药");
			titles.add("品种");
			titles.add("疗程");
			titles.add("时机");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
			int TotalPage = 1;
            //分批查询,最大查询2万条
            for(int pag = 1;pag<=TotalPage&&pag<=20;pag++){
                List<PageData> varOList =  oper.DRANO007(page);
                TotalPage = page.getTotalPage();
                if(varList==null){
                    varList = new ArrayList<PageData>(TotalPage*page.getShowCount());
                }
                if(varOList!=null){
        			for(int i=0;i<varOList.size();i++){
        				PageData vpd = new PageData();
        				vpd.put("var1", varOList.get(i).get("name"));	//2
        				vpd.put("var2", varOList.get(i).get("OPERATION_DESC"));	//4
        				vpd.put("var3", varOList.get(i).get("OPERATOR"));		//5
        				vpd.put("var4", varOList.get(i).get("dept_name"));
        				vpd.put("var5", varOList.get(i).get("OPERATING_DATE"));
        				vpd.put("var6", varOList.get(i).get("WOUND_GRADE"));
        				vpd.put("var7", varOList.get(i).get("WOUND_GRADE_UPDATE"));
        				vpd.put("var8", "1".equals(varOList.get(i).getString("HAS_ANTI"))?"是":"");
        				String LH = varOList.get(i).getString("LH");
        				if("1".equals(LH)){
        					vpd.put("var9", "一联");
        				}if("2".equals(LH)){
        					vpd.put("var9", "二联");
        				}if("3".equals(LH)){
        					vpd.put("var9", "三联");
        				}if("4".equals(LH)){
        					vpd.put("var9", "多联");
        				}
        				vpd.put("var10", "1".equals(varOList.get(i).getString("PZ"))?"是":"");
        				vpd.put("var11", "1".equals(varOList.get(i).getString("IS_TREATMENT"))?"是":"");
        				vpd.put("var12", "1".equals(varOList.get(i).getString("IS_TIMING"))?"是":"");
        				varList.add(vpd);
        			}
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
    
    @RequestMapping(value="/DRANO008Export")
    public ModelAndView DRANO008Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("手术总例数");
			titles.add("抗菌药使用率");
			titles.add("时机合理率");
			titles.add("疗程合理率");
			titles.add("品种合理率");
			titles.add("一联使用率");
			titles.add("二联使用率");
			titles.add("三联使用率");
			titles.add("多联使用率");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
			List<PageData> varOList = oper.DRANO008(pd);
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", i+1);
				vpd.put("var2", varOList.get(i).get("dept_name")); 
				vpd.put("var3", varOList.get(i).get("patsum"));	 
				vpd.put("var4", ((BigDecimal)varOList.get(i).get("hasantisum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("ranking")).doubleValue()*100 +"%"
						);
				vpd.put("var5", ((BigDecimal)varOList.get(i).get("timingsum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("timingranking")).doubleValue()*100 +"%"
						);
				vpd.put("var6", ((BigDecimal)varOList.get(i).get("timingsum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("timingranking")).doubleValue()*100 +"%"
						);
				vpd.put("var7", ((BigDecimal)varOList.get(i).get("treatsum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("treatranking")).doubleValue()*100 +"%"
						);
				vpd.put("var8", ((BigDecimal)varOList.get(i).get("pzsum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("pzranking")).doubleValue()*100 +"%"
						);
				vpd.put("var9", ((BigDecimal)varOList.get(i).get("lh1sum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("lh1ranking")).doubleValue()*100 +"%"
						);
				vpd.put("var10",((BigDecimal)varOList.get(i).get("lh2sum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("lh2ranking")).doubleValue()*100 +"%"
						);
				vpd.put("var11",((BigDecimal)varOList.get(i).get("lh3sum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("lh3ranking")).doubleValue()*100 +"%"
						);
				vpd.put("var12",((BigDecimal)varOList.get(i).get("lh4sum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("lh4ranking")).doubleValue()*100 +"%"
						);
				varList.add(vpd);
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
    @RequestMapping(value="/DRANO009Export")
    public ModelAndView DRANO009Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("医生");
			titles.add("科室");
			titles.add("手术总例数");
			titles.add("抗菌药使用率");
			titles.add("时机合理率");
			titles.add("疗程合理率");
			titles.add("品种合理率");
			titles.add("一联使用率");
			titles.add("二联使用率");
			titles.add("三联使用率");
			titles.add("多联使用率");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
			List<PageData> varOList = oper.DRANO009(pd);
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", varOList.get(i).get("operator"));	//4
				vpd.put("var3", varOList.get(i).get("dept_name"));		//5
				vpd.put("var4", varOList.get(i).get("patsum"));
				vpd.put("var5", ((BigDecimal)varOList.get(i).get("hasantisum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("ranking")).doubleValue()*100 +"%"
						);
				vpd.put("var6", ((BigDecimal)varOList.get(i).get("timingsum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("timingranking")).doubleValue()*100 +"%"
						);
				vpd.put("var7", ((BigDecimal)varOList.get(i).get("treatsum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("treatranking")).doubleValue()*100 +"%"
						);
				vpd.put("var8", ((BigDecimal)varOList.get(i).get("pzsum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("pzranking")).doubleValue()*100 +"%"
						);
				vpd.put("var9", ((BigDecimal)varOList.get(i).get("lh1sum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("lh1ranking")).doubleValue()*100 +"%"
						);
				vpd.put("var10",((BigDecimal)varOList.get(i).get("lh2sum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("lh2ranking")).doubleValue()*100 +"%"
						);
				vpd.put("var11",((BigDecimal)varOList.get(i).get("lh3sum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("lh3ranking")).doubleValue()*100 +"%"
						);
				vpd.put("var12",((BigDecimal)varOList.get(i).get("lh4sum")).doubleValue()
						+"/"+((BigDecimal)varOList.get(i).get("patsum")).doubleValue()+"="
						+ ((BigDecimal)varOList.get(i).get("lh4ranking")).doubleValue()*100 +"%"
						);
				varList.add(vpd);
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
