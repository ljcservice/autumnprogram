package com.ts.controller.Report.InHospitalRep.AntiDrugRep;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.ts.util.MyDecimalFormat;
import com.ts.util.ObjectExcelView;
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
    @RequestMapping(value="/DRANO001Export")
    public ModelAndView DRANO001Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("就诊人数");
			titles.add("转科人数");
			titles.add("抗菌药物使用率");
			titles.add("DDD");
			titles.add("使用强度");
			titles.add(" ");
			dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<PageData>();
			List<PageData> varOList =  patInfo.DRANO001(pd);
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", varOList.get(i).get("dept_name"));
				vpd.put("var3", varOList.get(i).get("pat_count"));	//4
				vpd.put("var4", varOList.get(i).get("scd"));		//5
				vpd.put("var5", varOList.get(i).get("anti_count").toString()+"/"+varOList.get(i).get("pat_count").toString()
						+"*100="
						+(((BigDecimal)varOList.get(i).get("anti_count")).divide((BigDecimal)varOList.get(i).get("pat_count"),4, BigDecimal.ROUND_HALF_UP)).doubleValue() * 100 +"%"
					);
				vpd.put("var6", varOList.get(i).get("ddd_values"));
				vpd.put("var7", varOList.get(i).get("intensity") );
				vpd.put("var8", " ");
				 varList.add(vpd);
			}
        	PageData  pdsumTitle = new PageData();
        	pdsumTitle.put("var1", " ");
        	pdsumTitle.put("var2", "总出院人数");
        	pdsumTitle.put("var3", "转科人数");
        	pdsumTitle.put("var4", "总使用抗菌药人数");
        	pdsumTitle.put("var5", "抗菌药物使用率");
        	pdsumTitle.put("var6", "DDD(全部)");
        	pdsumTitle.put("var7", "总住院天数(全部)");
        	pdsumTitle.put("var8", "使用强度");
        	varList.add(pdsumTitle);
        	PageData  pdsum = patInfo.DRANO001sum(pd);
        	pdsumTitle.put("var1", "总计");
        	pdsum.put("var2", pdsum.get("pat_count"));
        	pdsum.put("var3", pdsum.get("scd"));
        	pdsum.put("var4", pdsum.get("anti_count"));
        	pdsum.put("var5", 
        			((BigDecimal)pdsum.get("anti_count")).longValue() + " / " +((BigDecimal)pdsum.get("pat_count")).longValue() +"* 100 =" +
        			MyDecimalFormat.format(((BigDecimal)pdsum.get("anti_count")).divide((BigDecimal)pdsum.get("pat_count"),4, BigDecimal.ROUND_HALF_UP).doubleValue()*100)+"%");
        	pdsum.put("var6", MyDecimalFormat.format(((BigDecimal)pdsum.get("ddd_values")).doubleValue()));
        	pdsum.put("var7", pdsum.get("admission_days"));
        	pdsum.put("var8",  MyDecimalFormat.format( ((BigDecimal)pdsum.get("intensity")).doubleValue()) );
        	varList.add(pdsum);
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
    @RequestMapping(value="/DRANO002Export")
    public ModelAndView DRANO002Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("就诊人数");
			titles.add("转科人数");
			titles.add("医生");
			titles.add("DDD强度");
			titles.add("抗菌药使用率");
			titles.add("限制级强度");
			titles.add("限制级使用率");
			titles.add("特殊级强度");
			titles.add("特殊级使用率");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = patInfo.DRANO002(page);
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", i+1);	//2
					vpd.put("var2", varOList.get(i).get("dept_name"));	//4
					vpd.put("var3", varOList.get(i).get("pat_count"));		//5
					vpd.put("var4", varOList.get(i).get("scd"));
					vpd.put("var5", varOList.get(i).get("doctor"));
					vpd.put("var6", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("ddd_intensity")).doubleValue()));
					vpd.put("var7", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("antiuserat")).doubleValue() *100)+ " %");
					vpd.put("var8",  MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("limit_ddd_intensity")).doubleValue()));
					vpd.put("var9", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("limit_ddd_count")).divide((BigDecimal)varOList.get(i).get("pat_count"),4, BigDecimal.ROUND_HALF_UP).doubleValue()*100) +"%");
					vpd.put("var10", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("spec_ddd_intensity")).doubleValue()));
					vpd.put("var11", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("spec_ddd_count")).divide((BigDecimal)varOList.get(i).get("pat_count"),4, BigDecimal.ROUND_HALF_UP).doubleValue()*100) +"%");
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
    @RequestMapping(value="/DRANO003Export")
    public ModelAndView DRANO003Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("病人");
			titles.add("医生");
			titles.add("当前科室住院天数");
			titles.add("DDD值");
			titles.add("DDD强度");
			titles.add("限制级DDD值");
			titles.add("限制级强度");
			titles.add("特殊级DDD值");
			titles.add("特殊级强度");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = patInfo.DRANO003(page);
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", i+1);	//2
					vpd.put("var2", varOList.get(i).get("dept_name"));	//4
					vpd.put("var3", varOList.get(i).get("patient_id"));		//5
					vpd.put("var4", varOList.get(i).get("doctor"));
					vpd.put("var5", varOList.get(i).get("funcdays"));
					vpd.put("var6", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("ddd_value")).doubleValue()));
					vpd.put("var7", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("ddd_intensity")).doubleValue()));
					vpd.put("var8", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("limit_ddd_value")).doubleValue()));
					vpd.put("var9", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("limit_ddd_intensity")).doubleValue()));
					vpd.put("var10", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("spec_ddd_value")).doubleValue()));
					vpd.put("var11", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("spec_ddd_intensity")).doubleValue()));
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
    @RequestMapping(value="/DRANO005Export")
    public ModelAndView DRANO005Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("抗菌药人次");
			titles.add("抗菌药总费");
			titles.add("药品总费");
			titles.add("人均使用抗菌药物费用");
			titles.add("抗菌药物费用占总药费的百分率");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            List<PageData> varOList =  patInfo.DRANO005(pd);
            BigDecimal antiCountCost = new BigDecimal(0);
            BigDecimal CountCost = new BigDecimal(0);
			for(int i=0;i<varOList.size();i++){
				antiCountCost = antiCountCost.add((BigDecimal)varOList.get(i).get("anti_costs_count"));
				CountCost = CountCost.add((BigDecimal)varOList.get(i).get("drug_costs_count"));
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", varOList.get(i).get("dept_name"));	//4
				vpd.put("var3", varOList.get(i).get("anti_count"));		//5
				vpd.put("var4", "￥ "+varOList.get(i).get("anti_costs_count").toString());
				vpd.put("var5", "￥ "+varOList.get(i).get("drug_costs_count").toString());
				if(((BigDecimal)varOList.get(i).get("anti_count")).doubleValue()==0){
					vpd.put("var6"," 0");
				}else{
					vpd.put("var6", ((BigDecimal)varOList.get(i).get("drug_costs_count")).divide((BigDecimal)varOList.get(i).get("anti_count"),2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				if(((BigDecimal)varOList.get(i).get("drug_costs_count")).doubleValue()==0){
					vpd.put("var7", varOList.get(i).get("anti_costs_count").toString() +"/" + varOList.get(i).get("drug_costs_count").toString() + "*100=0%");
				}else {
					vpd.put("var7", varOList.get(i).get("anti_costs_count").toString() +"/" + varOList.get(i).get("drug_costs_count").toString() + "*100="
							+	((BigDecimal)varOList.get(i).get("anti_costs_count")).divide((BigDecimal)varOList.get(i).get("drug_costs_count"),4, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				}
				varList.add(vpd);
			}
        	PageData  pdsum = patInfo.DRANO005sum(pd);
        	pdsum.put("var1", "合计");
        	pdsum.put("var2", "");
        	pdsum.put("var3", pdsum.get("anticount"));
        	pdsum.put("var4",  antiCountCost);
        	pdsum.put("var5",  CountCost);
        	pdsum.put("var6",  antiCountCost.divide((BigDecimal)pdsum.get("anticount"),2, BigDecimal.ROUND_HALF_UP));
        	pdsum.put("var7",antiCountCost +"/"+CountCost +"*100=" +
        			antiCountCost.divide(CountCost,4, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        	varList.add(pdsum);
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
    @RequestMapping(value="/DRANO006Export")
    public ModelAndView DRANO006Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("就诊人数");
			titles.add("转科人数");
			titles.add("抗菌药人次");
			titles.add("使用抗菌药物的百分率");
			titles.add("人均使用抗菌药物品种数");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            List<PageData> varOList =  patInfo.DRANO006(pd);
            BigDecimal antiCountnumber = new BigDecimal(0);
            BigDecimal CountCost = new BigDecimal(0);
			for(int i=0;i<varOList.size();i++){
				antiCountnumber = antiCountnumber.add((BigDecimal)varOList.get(i).get("anti_type_count"));
				CountCost = CountCost.add((BigDecimal)varOList.get(i).get("drug_costs_count"));
				PageData vpd = new PageData();
				vpd.put("var1", i+1);
				vpd.put("var2", varOList.get(i).get("dept_name"));
				vpd.put("var3", varOList.get(i).get("pat_count"));
				vpd.put("var4", varOList.get(i).get("scd"));
				vpd.put("var5",  varOList.get(i).get("anti_count") );
				vpd.put("var6", "￥ "+((BigDecimal)varOList.get(i).get("antiuserat") ).doubleValue()*100);
				vpd.put("var7", varOList.get(i).get("anti_type_count").toString() +"/" + varOList.get(i).get("anti_count").toString() + "*100="
					+	((BigDecimal)varOList.get(i).get("anti_type_count")).divide((BigDecimal)varOList.get(i).get("anti_count"),4, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				varList.add(vpd);
			}
        	PageData  pdsum = patInfo.DRANO006sum(pd);
        	pdsum.put("var1", "合计");
        	pdsum.put("var2", "");
        	pdsum.put("var3", pdsum.get("pat_count"));
        	pdsum.put("var4",   pdsum.get("scd_count"));
        	pdsum.put("var5",   pdsum.get("anti_count"));
        	pdsum.put("var6",  ( (BigDecimal)pdsum.get("anti_count")).divide((BigDecimal)pdsum.get("pat_count"),2, BigDecimal.ROUND_HALF_UP));
        	pdsum.put("var7",antiCountnumber +"/"+pdsum.get("anti_count") +"=" +
        			antiCountnumber.divide((BigDecimal)pdsum.get("anti_count"),4, BigDecimal.ROUND_HALF_UP).doubleValue());
        	
        	varList.add(pdsum);
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
    @RequestMapping(value="/DRNO003Export")
    public ModelAndView DRNO003Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("总费用");
			titles.add("药费费用");
			titles.add("药费比例");
			titles.add("抗菌费用");
			titles.add("抗菌比例");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
			List<PageData> varOList = patInfo.DRNO003(pd);
			BigDecimal CountCost = new BigDecimal(0);
			BigDecimal drugCountCost = new BigDecimal(0);
			BigDecimal antiCountCost = new BigDecimal(0);
			for(int i=0;i<varOList.size();i++){
				antiCountCost = antiCountCost.add( ((BigDecimal) varOList.get(i).get("anti")));
				drugCountCost = drugCountCost.add( ((BigDecimal) varOList.get(i).get("drug")));
				CountCost = CountCost.add( ((BigDecimal) varOList.get(i).get("med")));
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", varOList.get(i).get("dept_name"));	//4
				vpd.put("var3", varOList.get(i).get("med"));		//5
				vpd.put("var4", "￥ "+varOList.get(i).get("drug"));
				vpd.put("var5", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("ranking")).doubleValue()*100)  +"%");
				vpd.put("var6", "￥ "+varOList.get(i).get("anti").toString() );
				vpd.put("var7", MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("antiranking")).doubleValue()*100)  +"%");
				varList.add(vpd);
			}
			PageData count = new PageData();
			count.put("var1", "合计"); 
			count.put("var2", " "); 
			count.put("var3", "￥ "+MyDecimalFormat.format(CountCost.doubleValue())); 
			count.put("var4", "￥ "+MyDecimalFormat.format(drugCountCost.doubleValue())); 
			count.put("var5", MyDecimalFormat.format(drugCountCost.divide(CountCost,6, 4).doubleValue()*100) +"%"); 
			count.put("var6", "￥ "+antiCountCost.doubleValue()); 
			count.put("var7", MyDecimalFormat.format(antiCountCost.divide(drugCountCost,6, 4).doubleValue()*100) +"%"); 
			varList.add(count);
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
    @RequestMapping(value="/DRNO008Export")
    public ModelAndView DRNO008Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
            titles.add("序号");
			titles.add("科室");
			titles.add("医生");
			titles.add("总费用");
			titles.add("药费费用");
			titles.add("药费比例");
			titles.add("抗菌费用");
			titles.add("抗菌比例");
			dataMap.put("titles", titles);
			BigDecimal antiCountCost = new BigDecimal(0);
			BigDecimal CountCost = new BigDecimal(0);
			BigDecimal drugCountCost = new BigDecimal(0);
            List<PageData> varList = new ArrayList<PageData>();
				List<PageData> varOList = patInfo.DRNO008(pd);
				for(int i=0;i<varOList.size();i++){
					antiCountCost = antiCountCost.add( ((BigDecimal) varOList.get(i).get("anti")));
					CountCost = CountCost.add( ((BigDecimal) varOList.get(i).get("med")));
					drugCountCost = drugCountCost.add( ((BigDecimal) varOList.get(i).get("drug")));
					PageData vpd = new PageData();
					vpd.put("var1", i+1);	//2
					vpd.put("var2", varOList.get(i).get("dept_name"));	//4
					vpd.put("var3", varOList.get(i).get("doctor"));		//5
					vpd.put("var4",  "￥ "+MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("med")).doubleValue()));
					vpd.put("var5","￥ "+MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("drug")).doubleValue()));
					vpd.put("var6", MyDecimalFormat.format(((BigDecimal) varOList.get(i).get("ranking")).doubleValue()*100) + " %" );
					vpd.put("var7",  "￥ "+MyDecimalFormat.format(((BigDecimal)varOList.get(i).get("anti")).doubleValue()));
					vpd.put("var8", MyDecimalFormat.format(((BigDecimal) varOList.get(i).get("antiranking")).doubleValue()*100) + " %" );
					varList.add(vpd);
				}
				PageData count = new PageData();
				count.put("var1", "合计"); 
				count.put("var2", " "); 	count.put("var3", " "); 
				count.put("var4", "￥ "+MyDecimalFormat.format(CountCost.doubleValue())); 
				count.put("var5", "￥ "+MyDecimalFormat.format(drugCountCost.doubleValue())); 
				count.put("var6", MyDecimalFormat.format(drugCountCost.divide(CountCost,6, BigDecimal.ROUND_HALF_UP).doubleValue()*100) +"%"); 
				count.put("var7", "￥ "+MyDecimalFormat.format(antiCountCost.doubleValue())); 
				count.put("var8", MyDecimalFormat.format(antiCountCost.divide(drugCountCost,6, BigDecimal.ROUND_HALF_UP).doubleValue()*100) +"%"); 
				varList.add(count);
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
    @RequestMapping(value="/DRANO010Export")
    public ModelAndView DRANO010Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("抗菌药人次");
			titles.add("病原学送检例数");
			titles.add("病原学检查百分率");
			titles.add("限制级人次");
			titles.add("限制级送检例数");
			titles.add("限制级百分率");
			titles.add("特殊级人次");
			titles.add("特殊级送检例数");
			titles.add("特殊级百分率");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            List<PageData> varOList =  patInfo.DRANO010(pd);
            BigDecimal antiCountnumber = new BigDecimal(0);
            BigDecimal CountCost = new BigDecimal(0);
			for(int i=0;i<varOList.size();i++){
//				antiCountnumber = antiCountnumber.add(BigDecimal.valueOf(varOList.get(i).getDouble("anti_type_count")));
//				CountCost = CountCost.add((BigDecimal)varOList.get(i).get("drug_costs_count"));
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", varOList.get(i).get("dept_name"));	//4
				vpd.put("var3", varOList.get(i).get("anti_count"));		//5
				vpd.put("var4", varOList.get(i).get("submit_count"));
				vpd.put("var5",  ((BigDecimal)varOList.get(i).get("antiranking")).doubleValue()*100);
				vpd.put("var6", varOList.get(i).get("limit_anti_count"));
				vpd.put("var7", varOList.get(i).get("limit_submit_count"));
				vpd.put("var8",  ((BigDecimal)varOList.get(i).get("linitranking")).doubleValue()*100);
				vpd.put("var9", varOList.get(i).get("spec_anti_count"));
				vpd.put("var10", varOList.get(i).get("spec_submit_count"));
				vpd.put("var11",  ((BigDecimal)varOList.get(i).get("specranking")).doubleValue()*100);
				
//				vpd.put("var7", ((BigDecimal)varOList.get(i).get("drug_costs_count")).divide((BigDecimal)varOList.get(i).get("anti_count"),2, BigDecimal.ROUND_HALF_UP).doubleValue());
//				vpd.put("var8", varOList.get(i).get("outanti_count").toString() +"/" + varOList.get(i).get("outpat_count").toString() + "*100="
//					+	((BigDecimal)varOList.get(i).get("outanti_count")).divide((BigDecimal)varOList.get(i).get("outpat_count"),4, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				varList.add(vpd);
			}
        	PageData  pdsum = patInfo.DRANO010sum(pd);
        	pdsum.put("var1", "合计");
        	pdsum.put("var2", "");
        	pdsum.put("var3", pdsum.get("anti_count"));
        	pdsum.put("var4",  pdsum.get("submit_count"));
        	pdsum.put("var5",    ((BigDecimal)pdsum.get("antiranking")).doubleValue()*100);
        	pdsum.put("var6",  pdsum.get("limit_count"));
        	pdsum.put("var7",  pdsum.get("limit_submit_count"));
        	pdsum.put("var8",    ((BigDecimal)pdsum.get("limitranking")).doubleValue()*100);
        	pdsum.put("var9",  pdsum.get("spec_count"));
        	pdsum.put("var10",  pdsum.get("spec_submit_count"));
        	pdsum.put("var11",    ((BigDecimal)pdsum.get("specranking")).doubleValue()*100);
        	varList.add(pdsum);
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
    @RequestMapping(value="/DRANO011Export")
    public ModelAndView DRANO011Export(Page page)throws Exception{
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
			titles.add("抗菌药人次");
			titles.add("病原学送检例数");
			titles.add("病原学检查百分率");
			titles.add("限制级人次");
			titles.add("限制级送检例数");
			titles.add("限制级百分率");
			titles.add("特殊级人次");
			titles.add("特殊级送检例数");
			titles.add("特殊级百分率");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            int TotalPage = 1;
            page.setCurrentPage(1);
			page.setShowCount(1000);
			BigDecimal antiCountCost = new BigDecimal(0);
			BigDecimal CountCost = new BigDecimal(0);
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
	            List<PageData> varOList =  patInfo.DRANO011(page);
				for(int i=0;i<varOList.size();i++){
//					antiCountCost = antiCountCost.add((BigDecimal)varOList.get(i).get("anti_costs_count"));
//					CountCost = CountCost.add((BigDecimal)varOList.get(i).get("drug_costs_count"));
					PageData vpd = new PageData();
					vpd.put("var1", i+1); 
					vpd.put("var2", varOList.get(i).get("doctor"));	
					vpd.put("var3", varOList.get(i).get("dept_name"));	 
					vpd.put("var4", varOList.get(i).get("anti_count"));	 
					vpd.put("var5", varOList.get(i).get("submit_count"));
					vpd.put("var6",  ((BigDecimal)varOList.get(i).get("antiranking")).doubleValue()*100);
					vpd.put("var7", varOList.get(i).get("limit_anti_count"));
					vpd.put("var8", varOList.get(i).get("limit_submit_count"));
					vpd.put("var9",  ((BigDecimal)varOList.get(i).get("linitranking")).doubleValue()*100);
					vpd.put("var10", varOList.get(i).get("spec_anti_count"));
					vpd.put("var11", varOList.get(i).get("spec_submit_count"));
					vpd.put("var12",  ((BigDecimal)varOList.get(i).get("specranking")).doubleValue()*100);
					varList.add(vpd);
				}
			}
        	PageData  pdsum = patInfo.DRANO011sum(pd);
        	pdsum.put("var1", "合计");
        	pdsum.put("var2", "");
        	pdsum.put("var3", "");
        	pdsum.put("var4", pdsum.get("anti_count"));
        	pdsum.put("var5",  pdsum.get("submit_count"));
        	pdsum.put("var6",    ((BigDecimal)pdsum.get("antiranking")).doubleValue()*100);
        	pdsum.put("var7",  pdsum.get("limit_count"));
        	pdsum.put("var8",  pdsum.get("limit_submit_count"));
        	pdsum.put("var9",    ((BigDecimal)pdsum.get("limitranking")).doubleValue()*100);
        	pdsum.put("var10",  pdsum.get("spec_count"));
        	pdsum.put("var11",  pdsum.get("spec_submit_count"));
        	pdsum.put("var12",    ((BigDecimal)pdsum.get("specranking")).doubleValue()*100);
        	varList.add(pdsum);
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
    @RequestMapping(value="/DRANO012Export")
    public ModelAndView DRANO012Export(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try {
        	Map<String,Object> dataMap = new HashMap<String,Object>();
            page.setPd(pd);
            mv.addObject("pd", pd);
            
            List<String> titles = new ArrayList<String>();
			titles.add("序号");
			titles.add("科室");
			titles.add("就诊人数");
			titles.add("转科人数");
			titles.add("静脉输液例数");
			titles.add("静脉输液使用率");
			titles.add("抗菌药物静脉输液例数");
			titles.add("抗菌药物静脉输液占比");
			dataMap.put("titles", titles);
            
            List<PageData> varList = new ArrayList<PageData>();
            List<PageData> varOList =  patInfo.DRANO012(pd);
            BigDecimal antiCountCost = new BigDecimal(0);
            BigDecimal CountCost = new BigDecimal(0);
			for(int i=0;i<varOList.size();i++){
				//antiCountCost = antiCountCost.add((BigDecimal)varOList.get(i).get("anti_costs_count"));
				//CountCost = CountCost.add((BigDecimal)varOList.get(i).get("drug_costs_count"));
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", varOList.get(i).get("dept_name"));	//4
				vpd.put("var3", varOList.get(i).get("pat_count"));		//5
				vpd.put("var4", varOList.get(i).get("scd"));
				vpd.put("var5",  varOList.get(i).get("adminjmsysum") );
				vpd.put("var6", varOList.get(i).get("adminjmsysum").toString() +"/" + varOList.get(i).get("pat_count").toString() + "*100="
						+	((BigDecimal)varOList.get(i).get("intensity")).doubleValue() *100);
				vpd.put("var7",  varOList.get(i).get("adminantijmsysum") );
				vpd.put("var8", varOList.get(i).get("adminantijmsysum").toString() +"/" + varOList.get(i).get("adminjmsysum").toString() + "*100="
						+	((BigDecimal)varOList.get(i).get("intensityanti")).doubleValue() *100);
				
				varList.add(vpd);
			}
			PageData titil = new PageData();
			titil.put("var1", "");
			titil.put("var2", "");
			titil.put("var3", "总出院人数");
			titil.put("var4", "转科人数");
			titil.put("var5", "静脉输液例数");
			titil.put("var6", "静脉输液使用率");
			titil.put("var7", "抗菌药物静脉输液例数");
			titil.put("var8", "抗菌药物静脉输液占比");
			varList.add(titil);
        	PageData  pdsum = patInfo.DRANO012sum(pd);
        	pdsum.put("var1", "合计：");
        	pdsum.put("var2",  "");
        	pdsum.put("var3",  pdsum.get("pat_count"));
        	pdsum.put("var4",  pdsum.get("scd"));
        	pdsum.put("var5", pdsum.get("adminjmsysum"));
        	pdsum.put("var6",((BigDecimal)pdsum.get("adminjmsysum")).doubleValue() +"/"+((BigDecimal)pdsum.get("pat_count")).doubleValue() +"*100=" +
        			((BigDecimal)pdsum.get("intensity")).doubleValue());
        	pdsum.put("var7",   pdsum.get("adminantijmsysum"));
        	pdsum.put("var8",((BigDecimal)pdsum.get("adminantijmsysum")).doubleValue() +"/"+((BigDecimal)pdsum.get("adminjmsysum")).doubleValue() +"*100=" +
        			((BigDecimal)pdsum.get("intensityanti")).doubleValue());
        	varList.add(pdsum);
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
