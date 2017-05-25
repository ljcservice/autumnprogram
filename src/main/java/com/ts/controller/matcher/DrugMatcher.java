package com.ts.controller.matcher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.Impl.BasePageBean;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.matcher.IDataMatcherService;
import com.ts.util.PageData;
import com.ts.util.Tools;

/**
 * 药品配码
 *
 * @author Crystal
 */
@Controller
@RequestMapping(value="/DrugMatcher")
public class DrugMatcher extends BaseController {
    private static final long serialVersionUID = 1L;
    @Resource(name = "drugMatcherServiceImpl")
    private IDataMatcherService drugMatcherServiceImpl;
    private int drug_map_id = 0;

    /**
     * 药品 配对查询
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/query")
    public ModelAndView query(Page page,HttpServletRequest request, HttpServletResponse response) {
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
        page.setCurrentPage(pageView.getCurrentpage());
        page.setCurrentResult((int)pageView.getPageindex().getStartindex());
        page.setTotalResult((int)pageView.getTotalrecord());
        page.setTotalPage((int)pageView.getTotalpage());
        page.setEntityOrField(true);
//        mv.addObject("page", page);
        mv.addObject("pd", pd);
    	mv.setViewName("matcher/drugMatcher/MatcherMain");
        return mv;
    }
    
    /**
     * 删除所选行
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/deleteIt")
    public void deleteIt(HttpServletRequest request, HttpServletResponse response){
        String drug_map_id = CommonUtils.getRequestParameter(request,"dmi","");
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        //判空操作
        if("".equals(drug_map_id)){
            return  ;
        }
        //将删除的药品回填到药品导入列表中
        HttpSession session = request.getSession();
        Map<String,TCommonRecord> newDrug = (Map<String,TCommonRecord>)session.getAttribute("allNewDrug");
        if(null != newDrug) {
        	String querySql = "SELECT drug_no_local drug_code,drug_name_local drug_name,drug_spec,"
        			+ "units,drug_form,toxi_property,dose_per_unit,dose_units,drug_indicator,input_code "
        			+ "FROM drug_map where drug_map_id=" + drug_map_id;
        	TCommonRecord resultRec = (TCommonRecord) query.queryForObject(querySql, new CommonMapper());
        	newDrug.put(resultRec.get("drug_code").replace(" ", "") + resultRec.get("drug_spec").replace(" ", "") + resultRec.get("drug_name").replace(" ", ""),resultRec);
        }
        String sql = "delete from pdss.drug_map where drug_map_id=" + drug_map_id;
        query.execute(sql);
    }
    
    /**
     * 配对程序
     * 参数
     * 1：  drug_map_id id
     * 2：  page 修改的page
     * 3.  t 时间
     * @return
     */
    @RequestMapping(value="/matcherIt")
    public ModelAndView matcherIt(HttpServletRequest request, HttpServletResponse response){
//        this.forword = "/WebPage/DrugMatcher/matcher.jsp";
    	ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
    	String drug_map_id = CommonUtils.getRequestParameter(request,"drug_map_id","");//id
//        request.setAttribute("drug_map_id", drug_map_id);
        mv.addObject("drug_map_id", drug_map_id);
        
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);
//        try {
//            response.flushBuffer();
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        JDBCQueryImpl query      = DBQueryFactory.getQuery("PDSS");
        String sql               = "select * from Drug_Map ";
        if (drug_map_id.length() > 0)
            sql += " where drug_map_id = '" + drug_map_id + "'";
        else
            sql += " where 1<>1 ";
        List<TCommonRecord> list = query.query(sql, new CommonMapper());
        TCommonRecord cr = new TCommonRecord();
        if (list.size() > 0)
            cr = list.get(0);

//        request.setAttribute("drug",cr);
        mv.addObject("drug", cr);
        mv.addObject("cr", cr);
        String  is_medcare = cr.get("is_medcare");
        if(!Tools.isEmpty(is_medcare)){
	        for(int i=1;i<=21;i++){
	        	if (is_medcare.length() >= i)
	        	{
	        		mv.addObject("is_medcare"+i, is_medcare.substring(i - 1, i));
	        	}
	        }
        }
        mv.setViewName("matcher/drugMatcher/matcher");
        return mv;
    }
    @RequestMapping(value="/queryPDSSList")
    protected ModelAndView queryPDSSList(HttpServletRequest request, HttpServletResponse response) {
//      this.forword = "/WebPage/DrugMatcher/DictQuery.jsp";
    	ModelAndView mv = this.getModelAndView();
    	mv.setViewName("matcher/drugMatcher/DictQuery");
        PageData pd = this.getPageData();
      response.setHeader("Pragma","No-cache");
      response.setHeader("Cache-Control","no-cache");
      response.setDateHeader("Expires", 0);
      if(CommonUtils.getRequestParameter(request, "drugName", "").equals("")){//判断为空则直接返回,
      	return mv;
      }
      JDBCQueryImpl query      = DBQueryFactory.getQuery("PDSS");
      String sql               = "select * from Drug";
      String DrugName          = new String(CommonUtils.getRequestParameter(request, "drugName", ""));
      String where             = " 1=1 ";
      if (DrugName.length() > 0) where += " and drug_Name like '%" + DrugName + "%'";
      if (" 1=1 ".equalsIgnoreCase(where))
          where = " 1 <> 1 ";
      sql += " where " + where;
      sql += " order by drug_id";
      List<TCommonRecord> list = query.query(sql, new CommonMapper());
      mv.addObject("drugList",list);
      return mv;
  }
    
    
    
    
    
    
    
    @RequestMapping(value="/modify")
    protected List<TCommonRecord> modify(HttpServletRequest request, HttpServletResponse response) {
//        this.forword = "/WebPage/DrugMatcher/Matcher_Detail.jsp";
        TCommonRecord params = new TCommonRecord();
        String drug_code_his = CommonUtils.getRequestParameter(request, "drug_code_his", "");
        String drug_spec_his = CommonUtils.getRequestParameter(request, "drug_spec_his", "");
        String units_his = CommonUtils.getRequestParameter(request, "units_his", "");

        if ("".equals(drug_code_his))
            params.set("drug_code", CommonUtils.getRequestParameter(request, "drug_code", ""));
        else params.set("drug_code", drug_code_his);
        params.set("drug_spec", CommonUtils.getRequestParameter(request, "drug_spec", ""));
        params.set("units", CommonUtils.getRequestParameter(request, "units", ""));

        if (this.drug_map_id > 0) {
            params.set("drug_map_id", this.drug_map_id + "");
            this.drug_map_id = 0;
        } else {
            params.set("drug_map_id", CommonUtils.getRequestParameter(request, "drug_map_id", ""));
        }
        request.setAttribute("hisEntity", this.drugMatcherServiceImpl.queryHISForObject(params));
        request.setAttribute("mapEntity", this.drugMatcherServiceImpl.queryPDSSMapForObject(params));
        return null;
    }


    @RequestMapping(value="/update")
    protected void update(HttpServletRequest request, HttpServletResponse response) {
    	com.ts.entity.system.User user = getCurrentUser();
        String ybNumStr = CommonUtils.getRequestParameter(request, "ybnum", "");
        int ybNum = Integer.parseInt(ybNumStr);
        String yb = "";
        for (int i = 1; i <= ybNum; i++)
        {
            yb += CommonUtils.getRequestParameter(request, "yb" + i, "0");
        }
        // 局部还是全身用药
        String is_part = CommonUtils.getRequestParameter(request,"is_part","0");//0 全身 1 局部
        // 是否是外用药
        String is_external = CommonUtils.getRequestParameter(request,"is_external","0");//0 全身； 1 局部
        //是否是抗菌药
        String is_anti = CommonUtils.getRequestParameter(request,"is_anti","0"); //1抗菌药，0非抗菌药

    /*处理逻辑
    抗菌药分为外用和内用，全身就是内用，外用就是局部，当是抗菌药时is_part起作用，当不是抗菌药时is_external 起作用。
     */
        if("1".equals(is_anti)){
            is_external = is_part;
        }

        String sql = "update drug_map set oper_user = '" + user.getUSERNAME()+
                "', oper_time=sysdate, last_date_time=sysdate, drug_id='" + CommonUtils.getRequestParameter(request, "drug_id", "") +
                "', drug_name='" +  CommonUtils.getRequestParameter(request, "drug_name", "")  +
                "', drug_spec_pdss='" + CommonUtils.getRequestParameter(request, "drug_spec_pdss", "") +
                "', units_pdss='" + CommonUtils.getRequestParameter(request, "units_pdss", "") +
                "', drug_form_pdss='" + CommonUtils.getRequestParameter(request, "drug_form_pdss", "") +
                "', dose_per_unit_pdss='" + CommonUtils.getRequestParameter(request, "dose_per_unit_pdss", "") +
                "', dose_units_pdss='" + CommonUtils.getRequestParameter(request, "dose_units_pdss", "") +
                "', drug_indicator_pdss='" + CommonUtils.getRequestParameter(request, "drug_indicator_pdss", "") +
                "', toxi_property_pdss='" + CommonUtils.getRequestParameter(request, "toxi_property_pdss", "") +
                "', is_anti='" + CommonUtils.getRequestParameter(request, "is_anti", "") +
                "', is_basedrug='" + CommonUtils.getRequestParameter(request, "is_basedrug", "") +
                "', ddd_value='" + CommonUtils.getRequestParameter(request, "ddd_value", "") +
                "', ddd_unit='" + CommonUtils.getRequestParameter(request, "ddd_unit", "") +
                "', ddd_per_unit='" + CommonUtils.getRequestParameter(request, "ddd_per_unit", "") +
                "', is_exhilarant='" + CommonUtils.getRequestParameter(request, "is_exhilarant", "") +
                "', is_injection='" + CommonUtils.getRequestParameter(request, "is_injection", "") +
                "', is_oral='" + CommonUtils.getRequestParameter(request, "is_oral", "") +
                "', is_impregnant='" + CommonUtils.getRequestParameter(request, "is_impregnant", "") +
                "', pharm_catalog='" + CommonUtils.getRequestParameter(request, "pharm_catalog", "") +
                "', drug_catalog='" + CommonUtils.getRequestParameter(request, "drug_catalog", "") +
                "', is_external='" + is_external +
                "', is_chinesedrug='" + CommonUtils.getRequestParameter(request, "is_chinesedrug", "") +
                "', is_allergy='" + CommonUtils.getRequestParameter(request, "is_allergy", "") +
                "', ddd_value_x='" + CommonUtils.getRequestParameter(request, "ddd_value_x", "") +
                "', is_medcare_country='" + CommonUtils.getRequestParameter(request, "is_medcare_country", "") +
                "', is_medcare_local='" + CommonUtils.getRequestParameter(request, "is_medcare_local", "") +
                "', is_patentdrug='" + CommonUtils.getRequestParameter(request, "is_patentdrug", "") +
                "', is_tumor='" + CommonUtils.getRequestParameter(request, "is_tumor", "") +
                "', is_poison='" + CommonUtils.getRequestParameter(request, "is_poison", "") +
                "', is_psychotic='" + CommonUtils.getRequestParameter(request, "is_psychotic", "") +
                "', is_habitforming='" + CommonUtils.getRequestParameter(request, "is_habitforming", "") +
                "', is_radiation='" + CommonUtils.getRequestParameter(request, "is_radiation", "") +
                "', is_precious='" + CommonUtils.getRequestParameter(request, "is_precious", "") +
                "', is_danger='" + CommonUtils.getRequestParameter(request, "is_danger", "") +
                "', is_otc='" + CommonUtils.getRequestParameter(request, "is_otc", "") +
                "', anti_level='" + CommonUtils.getRequestParameter(request, "anti_level", "") +
                "', is_hormone='" + CommonUtils.getRequestParameter(request, "is_hormone", "") +
                "', is_cardiovascular='" + CommonUtils.getRequestParameter(request, "is_cardiovascular", "") +
                "', is_digestive='" + CommonUtils.getRequestParameter(request, "is_digestive", "") +
                "', is_biological='" + CommonUtils.getRequestParameter(request, "is_biological", "") +
                "', is_medcare='" + yb +
                "', is_chinese_drug='" + CommonUtils.getRequestParameter(request, "is_patentdrug", "") +
                "' where drug_map_id = '" + CommonUtils.getRequestParameter(request, "drug_map_id", "") + "'";
        JDBCQueryImpl query      = DBQueryFactory.getQuery("PDSS");
        int x = query.update(sql);

    }

    protected ModelAndView matchAuto(HttpServletRequest request, HttpServletResponse response) {
        TCommonRecord params = new TCommonRecord();
        params.set("params", CommonUtils.getRequestParameter(request, "params", ""));
        params.set("oper_user", CommonUtils.getSessionUser(request).getUserName());
        String cnts = this.drugMatcherServiceImpl.matchAuto(params);
        request.setAttribute("updCnt", cnts.split("_")[0]);
        request.setAttribute("drugCnt", cnts.split("_")[1]);
        return null;
    }

    

    /**
     * excel 到处所有数据, 优化处理
     * @param request
     * @param response
     */
    protected void excelPrint(HttpServletRequest request, HttpServletResponse response) {
        String drugName   = new String(CommonUtils.getRequestParameter(request, "drugName", ""));//药品名称
        request.setAttribute("drugName",drugName);
        String matched    = CommonUtils.getRequestParameter(request, "matched", "");
        request.setAttribute("matched",matched);
        String page       = CommonUtils.getRequestParameter(request, "page", "1");
        request.setAttribute("page",page);
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
            iPage = Integer.parseInt(page);
        }
        catch (Exception ex)
        {
            iPage = 1;
        }

        String where    = " state='0' ";
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
                where += " and is_psychotic = '1' or is_psychotic = '2' ";
            }else if("1".equals(q_is_psychotic)){
                where += " and is_psychotic = '1' ";
            }else if("2".equals(q_is_psychotic)){
                where += " and is_psychotic = '2' ";
            }
        }
        //危险等级
        if(!"".equals(q_is_danger)){
            if("0".equals(q_is_danger)){
                where += " and is_danger='1' or is_danger='2' or is_danger='3' ";
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
        String sql = "select * from pdss.drug_map where " + where + " order by drug_no_local asc";
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        List<TCommonRecord> drugList = query.query(sql, new CommonMapper());
//        setSessionObject(request,drugList);
        query(null,request,response);
//        this.forword = "/WebPage/DrugMatcher/DrugMatcher_Excel.jsp";
    }
}