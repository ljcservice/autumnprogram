package com.ts.controller.pdss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.Impl.BasePageBean;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.pdss.pdss.Beans.DrugUseAuth.TCkDrugUserAuth;
import com.ts.entity.system.User;
import com.ts.service.Config.IConfigDictService;
import com.ts.service.matcher.IBaseInfoManagerService;
import com.ts.service.pdss.DrugUseAuth.ICKDrugUserAuthService;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.util.DateUtil;
import com.ts.util.PageData;
import com.ts.util.UuidUtil;
import com.ts.util.app.AppUtil;

/**
 * 药物授权控制  
 * @author autumn
 *
 */
@Controller
@RequestMapping(value="/drugUserAuth")
public class DrugUserAuthController extends BaseController
{
    @Resource(name="cKDrugUserAuthServiceBean")
    private ICKDrugUserAuthService duaService;
    
    @Resource(name="baseInfoManagerServiceBean")
    IBaseInfoManagerService baseIM ;
    @Resource(name="configDictServiceBean")
    IConfigDictService configDict;

    @Resource(name = "pdssCache")
    private PdssCache pdssCache;
    
    /**
     * 药物授权显示
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/showDrugUserAuth")
    public ModelAndView showDrugUserAuth(Page page)throws Exception{
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
        List<TCkDrugUserAuth> duas = duaService.selectDrugUserAuthPage(page);
        findConfigDictByDoctor(mv);
        findConfigDict(mv);
        mv.addObject("deptDictsMap",deptDictsMap);
        mv.addObject("entitys", duas);
        mv.addObject("pd", pd);
        mv.setViewName("DoctOrder/DrugUserAuth/DrugUserAuthShow");
        return mv;
    }
    
    
    private void findConfigDict(ModelAndView mv )  throws Exception
    {
        List<PageData> CONTROLTYPE =  (List<PageData>) configDict.findConfigDictByType("CONTROLTYPE");
        Map<String, String> maps = new HashMap<String , String >();
        for(PageData entity : CONTROLTYPE)
        {
            maps.put(entity.getString("code"), entity.getString("name"));
        }
        mv.addObject("mapCY", maps);
        mv.addObject("CONTROLTYPEs", CONTROLTYPE);
    }
    
    private void findConfigDictByDoctor(ModelAndView mv) throws Exception
    {
//        List<PageData> JOBs =  (List<PageData>) configDict.findConfigDictByType("JOB");
//        List<PageData> CONTROLTYPE =  (List<PageData>) configDict.findConfigDictByType("CONTROLTYPE");
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
//        mv.addObject("JOBs", JOBs);
//        mv.addObject("CONTROLTYPE", CONTROLTYPE);
        mv.addObject("depts",depts);
//        mv.addObject("maps", maps);
    } 
    
    /**
     * 添加页面显示
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/addUIDrugUserAuth")
    public ModelAndView addUIDrugUserAuth() throws Exception
    {
        ModelAndView mv = this.getModelAndView();
        mv.addObject("MSG", "add");
        findConfigDict(mv);
        findConfigDictByDoctor(mv);
        mv.setViewName("DoctOrder/DrugUserAuth/DrugUserAuthEdit");
        return  mv;
    }
    
    
    /**
     * 
     * @param pd
     * @throws Exception
     */
    private void  setCacheDrugUserAuth(PageData pd) throws Exception
    {
        String drugCode = pd.getString("drug_code");
        String drugName = pd.getString("drug_name");
        String deptName = pd.getString("dept_name");
        String doctorName = pd.getString("doctor_name");
        delCacheDrugUserAuth(pd.getString("id"), drugCode, drugName, deptName, doctorName);
        Map<String , TCkDrugUserAuth> entity = pdssCache.queryDrugUserAuthByMap(drugCode, drugName, deptName, doctorName);
        if(entity == null) entity = new HashMap<String,TCkDrugUserAuth>();
        TCkDrugUserAuth dua = buildDrugUserAuth(pd);
        String controlType = pd.getString("control_type");
        entity.put(controlType, dua);
        pdssCache.setDrugUserAuthByMap(entity, drugCode, drugName, deptName, doctorName);
    }
    
    /**
     * 删除redis缓存中的 内容
     * @param id
     * @param drugCode
     * @param drugName
     * @param deptName
     * @param doctorName
     * @throws Exception
     */
    private void delCacheDrugUserAuth(String id , String drugCode , String drugName , String deptName,String doctorName ) throws Exception
    {
        Map<String , TCkDrugUserAuth> mapDua = pdssCache.queryDrugUserAuthByMap(drugCode, drugName, deptName, doctorName);
        if(mapDua == null) return;
        for(String key : mapDua.keySet())
        {
            TCkDrugUserAuth entity =   mapDua.get(key);
            if(id.equals(entity.getID()))
            {
                mapDua.remove(key);
                break;
            }
        }
        pdssCache.setDrugUserAuthByMap(mapDua, drugCode, drugName, deptName, doctorName);
    }
    
    private TCkDrugUserAuth buildDrugUserAuth(PageData pd)
    {
        TCkDrugUserAuth entity = new TCkDrugUserAuth();
        entity.setID(pd.getString("ID"));
        entity.setDEPT_CODE(pd.getString("dept_code"));
        entity.setDEPT_NAME(pd.getString("dept_name"));
        entity.setDRUG_CODE(pd.getString("drug_code"));
        entity.setDRUG_NAME(pd.getString("drug_name"));
        entity.setCONTROL_TYPE(pd.getString("control_type"));
        entity.setDOCTOR_NAME(pd.getString("doctor_name"));
        entity.setDRUG_SPEC(pd.getString("drug_spec"));
        entity.setDRUG_FORM(pd.getString("drug_form"));
        entity.setDIAG_CODE(pd.getString("diag_code"));
        entity.setDIAG_NAME(pd.getString("diag_name"));
        entity.setT_VALUE(pd.getInt("t_value"));
        entity.setTOTAL_VALUE(pd.getInt("total_value"));
        entity.setUPDATE_DATE(pd.getString("update_date"));
        entity.setUPDATE_PERSION(pd.getString("update_persion"));
        return entity ;
    }
    
    /**
     * 添加 药物控制信息 
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/addDrugUserAuth")
    public ModelAndView  addDrugUserAuth() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        User user = BaseController.getCurrentUser();
        pd.put("ID", UuidUtil.get32UUID());
        try
        {
            pd.put("total_value", "0");
            pd.put("update_persion", user.getUSERNAME());
            pd.put("update_Date", DateUtil.getDay());
            // 先更新 redis 缓存 
            setCacheDrugUserAuth(pd);
            duaService.addCKDrugUserAuth(pd);    
            mv.addObject("msg","success");
        }
        catch(Exception e )
        {
            logger.error(e.toString(), e);
        }
        mv.setViewName("save_result");
        return mv;
    }
    
    /**
     *  查询
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/hasDrugUserAuth")
    public Object hasDrugUserAuth() throws Exception
    {
        PageData pd = this.getPageData();
        Map<String, Object> map = new HashMap<String , Object>();
        map.put("result", "fail");
        try
        {
            boolean bool = duaService.hasDrugUserAuth(pd);
            if(!bool) map.put("result", "success");
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return map;
    }
    
    /**
     * 删除药物控制信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/delDrugUserAuth")
    @ResponseBody
    public Object delDrugUserAuth() throws Exception
    {
        PageData pd = this.getPageData();
        Map<String, Object> map = new HashMap<String , Object>();
        String id =  pd.getObjectString("ID");
        try
        {
            // 用id 查询 信息 
            TCkDrugUserAuth entity =  duaService.findByid(id);
            // 删除redis 缓存 
            delCacheDrugUserAuth(id, entity.getDRUG_CODE(), entity.getDRUG_NAME(), entity.getDEPT_NAME(), entity.getDOCTOR_NAME());
            duaService.deleteCKDrugUserAuth(id);
        }
        catch(Exception e )
        {
            logger.error(e.toString(),e);
        }
        
        map.put("result", "ok");
//        map.put("ngroupnum", pd.getString("ngroupnum"));
        return  AppUtil.returnObject(pd, map);
    }
    
    
    @RequestMapping(value="/editUIDrugUserAuth")
    public ModelAndView editUIDrugUserAuth() throws Exception 
    {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("MSG", "edit");
        String id = pd.getObjectString("ID");
        try
        {

            findConfigDict(mv);
            findConfigDictByDoctor(mv);
            TCkDrugUserAuth entity = duaService.findByid(id);
            mv.addObject("entity", entity);
        }
        catch(Exception e )
        {
            logger.error(e.toString(),e);
        }
        mv.setViewName("DoctOrder/DrugUserAuth/DrugUserAuthEdit");
        return mv;
    }
    
    @RequestMapping(value="editDrugUserAuth")
    public ModelAndView editDrugUserAuth() throws Exception 
    {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        User user = BaseController.getCurrentUser();
        try
        {
            pd.put("update_persion", user.getUSERNAME());
            pd.put("update_Date", DateUtil.getDay());
            // 更新redis缓存
            setCacheDrugUserAuth(pd);
            duaService.modifyByCKDrugUserAuth(pd);
            mv.addObject("msg","success");
        }
        catch(Exception e )
        {
            logger.error(e.toString(),e);
        }
        mv.setViewName("save_result");
        return mv;
    }
    
    public ModelAndView queryDept()
    {
        ModelAndView mv = this.getModelAndView();
        
        return mv;
    }
    
    /**
     * 药品 配对查询
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/queryByDrug")
    public ModelAndView queryDrug(Page page,HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
//        String drugName   = new String(CommonUtils.getRequestParameter(request, "drugName", "")).replace("(","_").replace("（", "_").replace(")","_").replace("）","_");//药品名称
        String drugName   = new String(CommonUtils.getRequestParameter(request, "drugName", ""));
        request.setAttribute("drugName",CommonUtils.getRequestParameter(request, "drugName", ""));
        String matched    = CommonUtils.getRequestParameter(request, "matched", "");
        request.setAttribute("matched",matched);
        String currentPage       = CommonUtils.getRequestParameter(request, "currentPage", "1");
        String whereField = CommonUtils.getRequestParameter(request, "whereField", "");
        request.setAttribute("whereField",whereField);
        String whereValue = CommonUtils.getRequestParameter(request, "whereValue", "");
        request.setAttribute("whereValue",whereValue);
        String q_is_anti  = CommonUtils.getRequestParameter(request, "q_is_anti", "");
        request.setAttribute("q_is_anti",q_is_anti);
        String q_is_psychotic = CommonUtils.getRequestParameter(request, "q_is_psychotic", "");
        request.setAttribute("q_is_psychotic",q_is_psychotic);
        String q_is_danger = CommonUtils.getRequestParameter(request, "q_is_danger", "");
        request.setAttribute("q_is_danger",q_is_danger);
        String drugCode  = CommonUtils.getRequestParameter(request,"drugCode","");
        request.setAttribute("drugCode",drugCode);

        int iPage         = 1;
        try
        {
            if(currentPage==null){
                iPage=1;
            }
            iPage = Integer.parseInt(currentPage);
        }
        catch (Exception ex)
        {
            iPage = 1;
        }
//        String where    = " and 1=1 and state='0' ";
        /* 取消逻辑删除功能  */
        String where    = "";
        if(!"".equals(drugCode)){
            where += " and drug_no_local like '%" + drugCode + "%' ";
        }
        if (drugName.length() > 0)
            where += " and drug_Name_Local like '%" + drugName + "%'";
        if (!"".equals(whereField))
            where += " and " + whereField + "='" + whereValue + "'";
        if (!"".equals(q_is_anti)){
            if ("0".equals(q_is_anti))
                where += " and is_anti='1'";
            else if("4".equals(q_is_anti)){//全身和局部抗菌药
                where += " and is_anti='1' and is_external='" + 1 + "' ";
            }else if("5".equals(q_is_anti)){
                where += " and is_anti='1' and is_external='" + 0 + "' ";
            }else{
                where += " and is_anti='1' and anti_level='" + q_is_anti + "'";
            }
        }

        //精神药物
        if(!"".equals(q_is_psychotic)){
            if("0".equals(q_is_psychotic)){
                where += " and (is_psychotic = '1' or is_psychotic = '2') ";
            }else if("1".equals(q_is_psychotic)){
                where += " and is_psychotic = '1' ";
            }else if("2".equals(q_is_psychotic)){
                where += " and is_psychotic = '2' ";
            }
        }
        //危险等级
        if(!"".equals(q_is_danger)){
            if("0".equals(q_is_danger)){
                where += " and (is_danger='1' or is_danger='2' or is_danger='3') ";
            }else if("1".equals(q_is_danger)){
                where += " and is_danger='1' ";
            }else if("2".equals(q_is_danger)){
                where += " and is_danger='2' ";
            }else if("3".equals(q_is_danger)){
                where += " and is_danger='3' ";
            }
        }

        if (!"".equals(matched))
            where += " and drug_id " + matched;
        BasePageBean bpb = new BasePageBean();
        LinkedHashMap<String, String> Orders = new LinkedHashMap<String, String>();
        Orders.put("drug_no_local", "asc");
        PageView<TCommonRecord> pageView = bpb.getScrollData(page.getShowCount(), iPage, "PDSS", where, Orders, "Drug_Map");
        List<TCommonRecord> list = pageView.getRecords();
        mv.addObject("resultList", list);
        page.setCurrentjztsFlag(1);
        page.setCurrentPage(pageView.getCurrentpage());
        page.setCurrentResult((int)pageView.getPageindex().getStartindex());
        page.setTotalResult((int)pageView.getTotalrecord());
        page.setTotalPage((int)pageView.getTotalpage());
        page.setEntityOrField(true);
//        mv.addObject("page", page);
        mv.addObject("pd", pd);
        mv.setViewName("DoctOrder/DrugUserAuth/MatcherMain");
        
        //统计匹配数量
        try {
//            mv.addObject("matcherCount", matcherService.countMatcherSum());
        } catch (Exception e) {
        }
        HttpSession session = getRequest().getSession();
        Object autoMatcher = session.getAttribute("autoMatcher");
        mv.addObject("autoMatcher",autoMatcher);
        return mv;
    }
    
//    @RequestMapping(value="/queryByDept")
//    public ModelAndView queryDept(Page page) throws Exception
//    {
//        ModelAndView mv = this.getModelAndView();
//        PageData pd = this.getPageData();
//        page.setPd(pd);
//        List<PageData> entitys = (List<PageData>) duaService.queryDept(page);
//        mv.addObject("entitys", entitys);
//        mv.setViewName("DoctOrder/DrugUserAuth/DrugUserAuthDept");
//        return mv;
//    }
    
    
    @ResponseBody
    @RequestMapping(value="/findDoctorName")
    public Object findDoctorName()  throws Exception
    {
        Map<String , Object>  map = new HashMap<String ,Object>();
        map.put("result", "fail");
        try
        {
            PageData pd = this.getPageData();
            pd.put("deptdicts", new String[]{"'" + pd.getString("dept_code") + "'"});
            List<PageData> entitys = baseIM.showByDoctorDict(pd);
            map.put("entitys", entitys);
            map.put("result", "success");
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return map;
    }
}
