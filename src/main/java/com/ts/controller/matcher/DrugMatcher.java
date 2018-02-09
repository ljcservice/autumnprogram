package com.ts.controller.matcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.StopWatch;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.Impl.BasePageBean;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.matcher.IDataMatcherService;
import com.ts.service.matcher.MatcherService;
import com.ts.util.MyDecimalFormat;
import com.ts.util.PageData;
import com.ts.util.PatternUtils;
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
    @Resource
    private MatcherService matcherService;
    private int drug_map_id = 0;

    @RequestMapping(value="/autoMatcher")
    @ResponseBody
    public Object autoMatcher(Page page){ 
    	Map<String,Object> map = new HashMap<String,Object>();
    	HttpSession session = getRequest().getSession();
    	Object autoMatcher = session.getAttribute("autoMatcher");
    	if(autoMatcher!=null){
    		map.put("result","正在匹配中，请稍后再试...");
    		return map;
    	}
    	session.setAttribute("autoMatcher", 1);
    	PageData pd = this.getPageData();
    	StopWatch start = new StopWatch();
    	start.start();
    	String errInfo = "success";
    	List<PageData> waitingUpdateList = new ArrayList<PageData>();
    	int totalCount = 0;
    	int matcherSuccess = 0;
        try {
        	//查询出列表
        	page.setShowCount(100);
        	page.setCurrentPage(1);
        	page.setTotalResult(1000);
        	for(int i=1;i<=page.getTotalPage();i++){
        		page.setPd(pd);
        		page.setCurrentPage(i);
        		List<PageData>	drugMaplist = matcherService.drugMapListPage(page); 
        		if(i==1){totalCount=page.getTotalResult();}
        		List<PageData> drugList = null;
        		try {
        			//自动匹配
        			for(PageData drugmap:drugMaplist){
        				String oldStr =  drugmap.getString("DRUG_NAME_LOCAL").trim();
	        			PageData s = new PageData();
	        			//完全匹配药品
	        			s.put("STAD_DRUG_NAME",oldStr);
	        			drugList = matcherService.drugList( s);
	        			//模糊匹配
	        			if(drugList==null || drugList.size()==0){
	        				s = new PageData();
	        				s.put("DRUG_NAME",oldStr);
	        				drugList = matcherService.drugList( s);
	        			}
	        			String str =  drugmap.getString("DRUG_NAME_LOCAL").trim();
	        			//替换无用字符
	        			if(drugList==null || drugList.size()==0){
	        	        	List<String> filterList =  matcherService.getFilterList();
        	        		for(String st:filterList){
        	        			st = st.replaceAll("\\(","\\\\\\(");
        	        			st = st.replaceAll("\\)","\\\\\\)");
//        	        			if(str.indexOf(st)!= -1)
        	        			str = str.replaceAll(st, "");
        	        		}
        	        		str = str.trim();
	        			}
	        			//汉字去掉（ ( 内容  完全匹配
	        			if(drugList==null || drugList.size()==0){
	        				if(!oldStr.equals(str)){
	        					s = new PageData();
	        					s.put("STAD_DRUG_NAME", str );
	        					drugList = matcherService.drugList( s);
	        				}
	        			}
	        			//汉字去掉（ ( 内容 模糊 查询
	        			if(drugList==null || drugList.size()==0){
	        				if(!oldStr.equals(str)){
		        				s = new PageData();
		        				s.put("DRUG_NAME", str );
		        				drugList = matcherService.drugList( s);
	        				}
	        			}
	        			
	        			List<PageData> drugList2 = new ArrayList<PageData>();
	            		List<PageData> drugList3 = new ArrayList<PageData>();
	        			if(drugList==null||drugList.size()==0){
	        				continue;
	        			}
	        			// DRUG_SPEC UNITS DRUG_FORM
	        			String DRUG_SPEC = PatternUtils.delChinaChar(drugmap.getString("DRUG_SPEC"));
	        			String UNITS = drugmap.getString("UNITS");
	        			String DRUG_FORM = drugmap.getString("DRUG_FORM");
	        			PageData matcher = null;
	        			for(PageData drug : drugList){
	        				//规则1 
	        				if(DRUG_SPEC!=null && DRUG_SPEC.equals(drug.getString("DRUG_SPEC"))
	        						&& UNITS!=null && DRUG_SPEC.equals(drug.getString("UNITS"))
	        						&& DRUG_FORM!=null && DRUG_SPEC.equals(drug.getString("DRUG_FORM"))
	        					){
	        					matcher = drug;break;
	        				}else{
	        					//规则2
	        					if(DRUG_SPEC!=null && DRUG_SPEC.equals(drug.getString("DRUG_SPEC"))
		        						&& UNITS!=null && DRUG_SPEC.equals(drug.getString("UNITS"))
		        					){
		        					matcher = drug;break;
		        				}else if(DRUG_SPEC!=null && DRUG_SPEC.equals(drug.getString("DRUG_SPEC"))
		        						&& DRUG_FORM!=null && DRUG_SPEC.equals(drug.getString("DRUG_FORM"))
		        					){
		        					matcher = drug;break;
		        				}else if( UNITS!=null && DRUG_SPEC.equals(drug.getString("UNITS"))
		        						&& DRUG_FORM!=null && DRUG_SPEC.equals(drug.getString("DRUG_FORM"))
		        					){
		        					matcher = drug;break;
		        				}else{
		        					//规则3 
		        					if(DRUG_SPEC!=null && DRUG_SPEC.equals(drug.getString("DRUG_SPEC"))
			        						|| UNITS!=null && DRUG_SPEC.equals(drug.getString("UNITS"))
			        						|| DRUG_FORM!=null && DRUG_SPEC.equals(drug.getString("DRUG_FORM"))
			        					){
			        					matcher = drug;break;
			        				}
		        				}
	        				}
	        				if(drug.get("dose_class_id")==null || Tools.isEmpty(drug.get("dose_class_id").toString()) ){
	        					drugList3.add(drug);
	        				}else{
	        					drugList2.add(drug);
	        				}
	        			}
	        			//规则4
	        			if(matcher==null && drugList2.size()>0){
		        			//排序
		        			Collections.sort(drugList2, new Comparator<PageData>() {
		    					@Override
		    					public int compare(PageData p1, PageData p2) {
		    						String DRUG_SPEC = p1.getString("DRUG_SPEC");
		    						String UNITS = p1.getString("UNITS");
		    						String DRUG_FORM = p1.getString("DRUG_FORM");
		    						
		    						String DRUG_SPEC2 = p2.getString("DRUG_SPEC");
		    						String UNITS2 = p2.getString("UNITS");
		    						String DRUG_FORM2 = p2.getString("DRUG_FORM");
		    						Integer m = countNotNull(DRUG_SPEC,UNITS,DRUG_FORM);
		    						Integer n = countNotNull(DRUG_SPEC2,UNITS2,DRUG_FORM2);
		    						return -1 * m.compareTo(n);
		    					}
		    				});
		        			//排序完成后，取值最多的一个
		        			matcher = drugList2.get(0);
	        			}
	        			//规则5
	        			if(matcher==null && drugList3.size()>0){
		        			//排序
		        			Collections.sort(drugList3, new Comparator<PageData>() {
		    					@Override
		    					public int compare(PageData p1, PageData p2) {
		    						String DRUG_SPEC = p1.getString("DRUG_SPEC");
		    						String UNITS = p1.getString("UNITS");
		    						String DRUG_FORM = p1.getString("DRUG_FORM");
		    						
		    						String DRUG_SPEC2 = p2.getString("DRUG_SPEC");
		    						String UNITS2 = p2.getString("UNITS");
		    						String DRUG_FORM2 = p2.getString("DRUG_FORM");
		    						Integer m = countNotNull(DRUG_SPEC,UNITS,DRUG_FORM);
		    						Integer n = countNotNull(DRUG_SPEC2,UNITS2,DRUG_FORM2);
		    						return -1 * m.compareTo(n);
		    					}
		    				});
		        			//排序完成后，取值最多的一个
		        			matcher = drugList3.get(0);
	        			}
	        			if(matcher==null ){
	        				//前面啥都匹配不上，列表只能为空了，不存在这种情况
	        			}
	        			//匹配成功更新
	        			if(matcher!=null){
	        				matcherSuccess++;
	        				matcher.put("drug_map_id", drugmap.get("drug_map_id"));
	        				waitingUpdateList.add(matcher);
	        			}
	        			//电脑也需要休息啊
	        			Thread.sleep(100);
	        		}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        	for(PageData po:waitingUpdateList){
        		update(po);
        	}
	    } catch (Exception e) {
			errInfo = "操作失败！";
		}
        session.removeAttribute("autoMatcher");
	    map.put("result",errInfo);
	    String persent = "0";
	    if(totalCount!=0){
	    	persent = MyDecimalFormat.format( new BigDecimal(matcherSuccess).divide(new BigDecimal(page.getTotalResult()),4,4).doubleValue()*100);
	    }
	    start.stop();
	    start.getTime();
	    map.put("msg","当前条件下药品总数："+totalCount+"，匹配成功数："+matcherSuccess+"，匹配成功率："+persent+"%，耗时："
	    		+start.getTime()/(1000*60)+"分"+start.getTime()/1000+"秒"
	    		);
	    // 配对后重载 药品信息
	    DrugUtils.loadDrugMap();
		return map ; 
    }
    
    private void update(PageData matcher) {
    	String drug_map_id = matcher.get("drug_map_id").toString();
    	if(drug_map_id==null){
    		return;
    	}
    	String is_part = CommonUtils.getRequestParameter(matcher,"is_part","0");//0 全身 1 局部
        // 是否是外用药
        String is_external = CommonUtils.getRequestParameter(matcher,"is_external","0");//0 全身； 1 局部
        //是否是抗菌药
        String is_anti = CommonUtils.getRequestParameter(matcher,"is_anti","0"); //1抗菌药，0非抗菌药

    /*处理逻辑
    抗菌药分为外用和内用，全身就是内用，外用就是局部，当是抗菌药时is_part起作用，当不是抗菌药时is_external 起作用。
     */
        if("1".equals(is_anti)){
            is_external = is_part;
        }

        String sql = "update drug_map set oper_user = '" + getCurrentUser().getUSERNAME()+
                "', oper_time=sysdate, last_date_time=sysdate, drug_id='" + CommonUtils.getRequestParameter(matcher, "drug_id", "") +
                "', drug_name='" +  CommonUtils.getRequestParameter(matcher, "drug_name", "")  +
                "', drug_spec_pdss='" + CommonUtils.getRequestParameter(matcher, "drug_spec", "") +
                "', units_pdss='" + CommonUtils.getRequestParameter(matcher, "units", "") +
                "', drug_form_pdss='" + CommonUtils.getRequestParameter(matcher, "drug_form", "") +
                "', dose_per_unit_pdss='" + CommonUtils.getRequestParameter(matcher, "dose_per_unit", "") +
                "', dose_units_pdss='" + CommonUtils.getRequestParameter(matcher, "dose_units", "") +
                "', drug_indicator_pdss='" + CommonUtils.getRequestParameter(matcher, "drug_indicator", "") +
                "', toxi_property_pdss='" + CommonUtils.getRequestParameter(matcher, "toxi_property", "") +
                "', is_anti='" + CommonUtils.getRequestParameter(matcher, "is_anti", "") +
                "', is_basedrug='" + CommonUtils.getRequestParameter(matcher, "is_basedrug", "") +
                "', ddd_value='" + CommonUtils.getRequestParameter(matcher, "ddd_value", "") +
                "', ddd_unit='" + CommonUtils.getRequestParameter(matcher, "ddd_unit", "") +
                "', ddd_per_unit='" + CommonUtils.getRequestParameter(matcher, "ddd_per_unit", "") +
                "', is_exhilarant='" + CommonUtils.getRequestParameter(matcher, "is_exhilarant", "") +
                "', is_injection='" + CommonUtils.getRequestParameter(matcher, "is_injection", "") +
                "', is_oral='" + CommonUtils.getRequestParameter(matcher, "is_oral", "") +
                "', is_impregnant='" + CommonUtils.getRequestParameter(matcher, "is_impregnant", "") +
                "', pharm_catalog='" + CommonUtils.getRequestParameter(matcher, "pharm_catalog", "") +
                "', drug_catalog='" + CommonUtils.getRequestParameter(matcher, "drug_catalog", "") +
                "', is_external='" + is_external +
                "', is_chinesedrug='" + CommonUtils.getRequestParameter(matcher, "is_chinesedrug", "") +
                "', is_allergy='" + CommonUtils.getRequestParameter(matcher, "is_allergy", "") +
                "', ddd_value_x='" + CommonUtils.getRequestParameter(matcher, "ddd_value_x", "") +
                "', is_medcare_country='" + CommonUtils.getRequestParameter(matcher, "is_medcare_country", "") +
                "', is_medcare_local='" + CommonUtils.getRequestParameter(matcher, "is_medcare_local", "") +
                "', is_patentdrug='" + CommonUtils.getRequestParameter(matcher, "is_patentdrug", "") +
                "', is_tumor='" + CommonUtils.getRequestParameter(matcher, "is_tumor", "") +
                "', is_poison='" + CommonUtils.getRequestParameter(matcher, "is_poison", "") +
                "', is_psychotic='" + CommonUtils.getRequestParameter(matcher, "is_psychotic", "") +
                "', is_habitforming='" + CommonUtils.getRequestParameter(matcher, "is_habitforming", "") +
                "', is_radiation='" + CommonUtils.getRequestParameter(matcher, "is_radiation", "") +
                "', is_precious='" + CommonUtils.getRequestParameter(matcher, "is_precious", "") +
                "', is_danger='" + CommonUtils.getRequestParameter(matcher, "is_danger", "") +
                "', is_otc='" + CommonUtils.getRequestParameter(matcher, "is_otc", "") +
                "', anti_level='" + CommonUtils.getRequestParameter(matcher, "anti_level", "") +
                "', is_hormone='" + CommonUtils.getRequestParameter(matcher, "is_hormone", "") +
                "', is_cardiovascular='" + CommonUtils.getRequestParameter(matcher, "is_cardiovascular", "") +
                "', is_digestive='" + CommonUtils.getRequestParameter(matcher, "is_digestive", "") +
                "', is_biological='" + CommonUtils.getRequestParameter(matcher, "is_biological", "") +
                //"', is_medcare='" + yb +
                "', is_chinese_drug='" + CommonUtils.getRequestParameter(matcher, "is_patentdrug", "") +
                "', is_assist='" + CommonUtils.getRequestParameter(matcher, "is_assist", "") +
                "', is_albumin='" + CommonUtils.getRequestParameter(matcher, "is_albumin", "") +
                "' where drug_map_id = '" + CommonUtils.getRequestParameter(matcher, "drug_map_id", "") + "'";
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        int x = query.update(sql);
	}

	//统计数组中不为空的个数
    protected Integer countNotNull(String... drug_spec ) {
    	Integer x = 0;
    	for(String str:drug_spec){
    		if(!Tools.isEmpty(str)){
    			x++;
    		}
    	}
		return x;
	}


	private String controlName(PageData p,String str) {
    	try {
    		if(str==null)return null;
    		str = str.trim();
			//str = str.replaceAll("注射液", "");
//    		if(str.contains("(")){
//    			String w = str.substring(str.indexOf("("),str.length()-1);
//    			
//    		}
			str = str.substring(0, str.indexOf("(") );
			str = str.substring(0, str.indexOf("（") );
		} catch (Exception e) {
		}
		return str;
	}

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
    	
    	//统计匹配数量
    	try {
			mv.addObject("matcherCount", matcherService.countMatcherSum());
		} catch (Exception e) {
		}
    	HttpSession session = getRequest().getSession();
    	Object autoMatcher = session.getAttribute("autoMatcher");
    	mv.addObject("autoMatcher",autoMatcher);
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
    @ResponseBody
	public Object  update(HttpServletRequest request, HttpServletResponse response) {
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
                "', is_assist='" + CommonUtils.getRequestParameter(request, "is_assist", "") +
                "', is_albumin='" + CommonUtils.getRequestParameter(request, "is_albumin", "") +
                "' where drug_map_id = '" + CommonUtils.getRequestParameter(request, "drug_map_id", "") + "'";
        JDBCQueryImpl query      = DBQueryFactory.getQuery("PDSS");
        int x = query.update(sql);
        // 更新缓存中的药品 
        DrugUtils.loadDrugMapBySingle(CommonUtils.getRequestParameter(request, "drug_map_id", ""));
        
        return new HashMap<String,Object>();
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
    
    public static void main(String[] args) {
    	System.out.println("asdasd".replaceAll("\\(","\\\\\\\\("));
	}
}