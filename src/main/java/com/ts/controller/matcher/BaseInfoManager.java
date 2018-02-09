package com.ts.controller.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitzd.his.Utils.Config;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.Config.IConfigDictService;
import com.ts.service.matcher.IBaseInfoManagerService;
import com.ts.util.PageData;

/**
 * 基本信息维护
 * @author autumn
 *
 */
@Controller
@RequestMapping(value="/BaseInfoManager")
public class BaseInfoManager extends BaseController
{

    @Resource(name="baseInfoManagerServiceBean")
    IBaseInfoManagerService baseIM ;
    @Resource(name="configDictServiceBean")
    IConfigDictService configDict;
    
    /**
     * 显示科室信息 
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/showByDeptDict")
    public ModelAndView showByDeptDict(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        page.setPd(pd);
        
        List<PageData> pds = baseIM.showByDeptDict(page);
        
        // 查询 config_dict 表 
        findConfigDictByDept(mv);
        
        mv.addObject("entitys", pds);
        mv.addObject("pd", pd);
        mv.setViewName("matcher/DeptManager/DeptDictManager");
        return mv;
    }

    private void findConfigDictByDept(ModelAndView mv) throws Exception
    {
        List<PageData> CLINIC_ATTRs =  (List<PageData>) configDict.findConfigDictByType("CLINIC_ATTR");
        List<PageData> OUTP_OR_INPs =  (List<PageData>) configDict.findConfigDictByType("OUTP_OR_INP");
        List<PageData> INTERNAL_OR_SERGERYs =  (List<PageData>) configDict.findConfigDictByType("INTERNAL_OR_SERGERY");
        
        Map<String, String> maps = new HashMap<String ,String>();
        for(PageData entity : CLINIC_ATTRs)
        {
            maps.put(entity.getString("type") + entity.getString("code"), entity.getString("name"));
        }
        for(PageData entity : OUTP_OR_INPs)
        {
            maps.put(entity.getString("type") + entity.getString("code"), entity.getString("name"));
        }
        for(PageData entity : INTERNAL_OR_SERGERYs)
        {
            maps.put(entity.getString("type") + entity.getString("code"), entity.getString("name"));
        }
        
        mv.addObject("CLINIC_ATTRs", CLINIC_ATTRs);
        mv.addObject("OUTP_OR_INPs", OUTP_OR_INPs);
        mv.addObject("INTERNAL_OR_SERGERYs", INTERNAL_OR_SERGERYs);
        mv.addObject("map",maps);
    }
    
    @RequestMapping(value="/addDeptDictUI")
    public ModelAndView addDeptDictUI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        mv.addObject("MSG", "add");
        // 查询 config_dict 表 
        findConfigDictByDept(mv);
        mv.setViewName("matcher/DeptManager/DeptDictEdit");
        return mv;
    }
    
    @RequestMapping(value="/addDeptDict")
    public ModelAndView addDeptDict()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("msg","success");
//        pd.put("ruleid", get32UUID());
        try
        {
            pd.put("INPUT_CODE", pd.getString("INPUT_CODE").toUpperCase());
            baseIM.insertByDeptDict(pd);
        }
        catch(Exception e )
        {
            e.printStackTrace();
            mv.addObject("msg","failed");
        }
        mv.setViewName("save_result");
        return mv;
    }
    
    @RequestMapping(value="/editDeptDictUI")
    public ModelAndView editDeptDictUI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData  pd = this.getPageData();
        String deptCode = pd.getString("deptCode");
        PageData entity = baseIM.findDeptByCode(deptCode);
        // 查询 config_dict 表 
        findConfigDictByDept(mv);
        mv.addObject("entity", entity);
        mv.addObject("MSG","edit");
        mv.setViewName("matcher/DeptManager/DeptDictEdit");
        return mv;
    }
    
    @RequestMapping(value="/editDeptDict")
    public ModelAndView editDeptDict()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("msg","success");
//        pd.put("ruleid", get32UUID());
        try
        {
            pd.put("INPUT_CODE", pd.getString("INPUT_CODE").toUpperCase());
            baseIM.updateByDeptDict(pd);
        }
        catch(Exception e )
        {
            e.printStackTrace();
            mv.addObject("msg","failed");
        }
        mv.setViewName("save_result");
        return mv;
    }
    
    @RequestMapping(value="/DelDeptDict")
    @ResponseBody
    public Object DelDeptDict() throws Exception
    {
        Map<String,String> map = new HashMap<String,String>();
        
        String rsInfo = "success";
        PageData pd = this.getPageData();
        try
        {
            this.baseIM.deleteByDeptDict(pd.getString("deptCode"));
        }
        catch(Exception e ){
            logger.error(e.toString(), e);
        }
        map.put("result", rsInfo);
        return map ;
    }
    
    @RequestMapping(value="/isValidDeptCode")
    @ResponseBody
    public Object isValidDeptCode() throws Exception
    {
        Map<String ,String> map = new HashMap<String , String>();
        String rsInfo = "success";
        PageData pd = this.getPageData();
        try
        {
            PageData entity = this.baseIM.findDeptByCode(pd.getString("deptCode"));
            if(entity != null)
                rsInfo = "fail";
        }
        catch(Exception e ){
            logger.error(e.toString(), e);
        }
        map.put("result", rsInfo);
        return map ;
    }
    
    
    
    private void findConfigDictByDoctor(ModelAndView mv) throws Exception
    {
        List<PageData> JOBs =  (List<PageData>) configDict.findConfigDictByType("JOB");
        List<PageData> TITLEs =  (List<PageData>) configDict.findConfigDictByType("TITLE");
        
        List<PageData> depts = baseIM.showByDeptDict(this.getPageData());
        
        Map<String, String> maps = new HashMap<String ,String>();
        for(PageData entity : depts)
        {
            maps.put(entity.getString("dept_Code"), entity.getString("dept_Name"));
        }
//        for(PageData entity : OUTP_OR_INPs)
//        {
//            maps.put(entity.getString("type") + entity.getString("code"), entity.getString("name"));
//        }
//        for(PageData entity : INTERNAL_OR_SERGERYs)
//        {
//            maps.put(entity.getString("type") + entity.getString("code"), entity.getString("name"));
//        }
//        
        mv.addObject("JOBs", JOBs);
        mv.addObject("TITLEs", TITLEs);
        mv.addObject("depts",depts);
        mv.addObject("maps", maps);
    } 
    
    /**
     * 显示医生信息 
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/showByDoctorDict")
    public ModelAndView showByDoctorDict(Page page)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String deptDicts = pd.getString("deptDicts");
        Map<String, String> deptDictsMap = new  HashMap<String , String>();
        List<String > depts = new ArrayList<String>();
        if(!"".equals(deptDicts)) {
            for(String s : deptDicts.split(","))
            {
                depts.add("'" + s + "'");
                deptDictsMap.put(s, s);
            }
            
            pd.put("deptDicts",depts.toArray());
        }
        page.setPd(pd);
        List<PageData> pds = baseIM.showByDoctorDict(page);
        
        // 查询 config_dict 表 
        findConfigDictByDoctor(mv);
        
        mv.addObject("deptDictsMap",deptDictsMap);
        mv.addObject("entitys", pds);
        mv.addObject("pd", pd);
        mv.setViewName("matcher/DoctorManager/DoctorDictManager");
        return mv;
    }

    
    @RequestMapping(value="/addDoctorDictUI")
    public ModelAndView addDoctorDictUI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        mv.addObject("MSG", "add");
        // 查询 config_dict 表 
        findConfigDictByDoctor(mv);
        mv.setViewName("matcher/DoctorManager/DoctorDictEdit");
        return mv;
    }
    
    @RequestMapping(value="/addDoctorDict")
    public ModelAndView addDoctorDict()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("msg","success");
//        pd.put("ruleid", get32UUID());
        try
        {
            pd.put("INPUT_CODE", pd.getString("INPUT_CODE").toUpperCase());
            baseIM.insertByDoctorDict(pd);
        }
        catch(Exception e )
        {
            e.printStackTrace();
            mv.addObject("msg","failed");
        }
        mv.setViewName("save_result");
        return mv;
    }
    
    @RequestMapping(value="/editDoctorDictUI")
    public ModelAndView editDoctorDictUI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData  pd = this.getPageData();
        String DoctorCode = pd.getString("DoctorCode");
        PageData entity = baseIM.findDoctorByCode(DoctorCode);
        // 查询 config_dict 表 
        findConfigDictByDoctor(mv);
        mv.addObject("entity", entity);
        mv.addObject("MSG","edit");
        mv.setViewName("matcher/DoctorManager/DoctorDictEdit");
        return mv;
    }
    
    @RequestMapping(value="/editDoctorDict")
    public ModelAndView editDoctorDict()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("msg","success");
//        pd.put("ruleid", get32UUID());
        try
        {
            pd.put("INPUT_CODE", pd.getString("INPUT_CODE").toUpperCase());
            baseIM.updateByDoctorDict(pd);
        }
        catch(Exception e )
        {
            e.printStackTrace();
            mv.addObject("msg","failed");
        }
        mv.setViewName("save_result");
        return mv;
    }
    
    @RequestMapping(value="/DelDoctorDict")
    @ResponseBody
    public Object DelDoctorDict() throws Exception
    {
        Map<String,String> map = new HashMap<String,String>();
        
        String rsInfo = "success";
        PageData pd = this.getPageData();
        try
        {
            this.baseIM.deleteByDoctorDict(pd.getString("emp_no"));
        }
        catch(Exception e ){
            logger.error(e.toString(), e);
        }
        map.put("result", rsInfo);
        return map ;
    }
    
    @RequestMapping(value="/isValidDoctorCode")
    @ResponseBody
    public Object isValidDoctorCode() throws Exception
    {
        Map<String ,String> map = new HashMap<String , String>();
        String rsInfo = "success";
        PageData pd = this.getPageData();
        try
        {
            PageData entity = this.baseIM.findDoctorByCode(pd.getString("DoctorCode"));
            if(entity != null)
                rsInfo = "fail";
        }
        catch(Exception e ){
            logger.error(e.toString(), e);
        }
        map.put("result", rsInfo);
        return map ;
    }
    
}
