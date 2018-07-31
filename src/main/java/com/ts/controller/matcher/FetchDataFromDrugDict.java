package com.ts.controller.matcher;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.QueryResult;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.hitzd.his.Web.base.PubServlet;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.matcher.IDataMatcherService;
import com.ts.util.PageData;
import com.ts.util.app.AppUtil;

/**
 * Author: apachexiong
 * Date: 10/30/13
 * Time: 6:13 PM
 * Package: com.hitzd.mi.Web.Servlet
 * <p/>
 * <p/>
 * 此servlet程序用于配码程序.
 * <p/>
 * 目的范围：
 * 1. 从HIS中抓取数据
 * 抓取的数据是没有放入到PDSS.DRUG_MAP中的数据。
 * 可以单条抓取和批量抓取。
 * 2. 将PDSS.DRUG_MAP中的数据进行配码。
 * 配码程序增加新的功能。
 */

@Controller
@RequestMapping(value="/FetchDataFromDrugDict")
public class FetchDataFromDrugDict  extends BaseController   {

    @Resource(name = "drugMatcherServiceImpl")
    private IDataMatcherService drugMatcherServiceImpl;


    @RequestMapping(value="/query")
    protected ModelAndView query(Page page,HttpServletRequest request, HttpServletResponse response) {
        /**
         * 查询数据并且把这些数据放入到Session中
         */
    	ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String currentPage       = CommonUtils.getRequestParameter(request, "currentPage", "1");
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
//        String pageStr = CommonUtils.getRequestParameter(request, "page", "");
//        int page = Integer.valueOf(pageStr==null||"".equals(pageStr)?"1":pageStr);

        String p_drug_code = CommonUtils.getRequestParameter(request, "q_drug_code", "");
        request.setAttribute("q_drug_code",p_drug_code);
        mv.addObject("q_drug_code", p_drug_code);
        String p_drug_name = CommonUtils.getRequestParameter(request, "q_drug_name", "");
        request.setAttribute("q_drug_name",p_drug_name);
        mv.addObject("q_drug_name", p_drug_name);
        String toxi_propertyStr = CommonUtils.getRequestParameter(request,"toxi_property","");
        request.setAttribute("toxi_property",toxi_propertyStr);
        mv.addObject("toxi_property", toxi_propertyStr);
        String[] toxi_property = null;
        if(!"".equals(toxi_propertyStr)) {
        	toxi_property = toxi_propertyStr.indexOf(",")>0?toxi_propertyStr.split(","):new String[]{toxi_propertyStr};
        }
        HttpSession session = request.getSession();

        //所有的毒理分类
        List<String> propertyList = (List<String>)session.getAttribute("toxiPropertyList");
        Map<String,TCommonRecord> newDrug = (Map<String,TCommonRecord>)session.getAttribute("allNewDrug");

        if(null == propertyList || propertyList.size()<=0)
        {
            propertyList = getAllToxiPropertyType();
            session.setAttribute("toxiPropertyList",propertyList);
        }
        request.setAttribute("toxiPropertyList",propertyList);//放入返回值中
        mv.addObject("toxiPropertyList", propertyList);
        if(null == newDrug || newDrug.size() <=0 )
        {
            //所有的医院药品
            TCommonRecord param = new TCommonRecord();
            param.set("p_drug_name",p_drug_name);
            param.set("p_drug_code", p_drug_code);
            param.setObj("toxi_property", toxi_property);

            newDrug = getNoMapDrug(param);
            session.setAttribute("allNewDrug",newDrug);
            mv.addObject("allNewDrug", newDrug);
        }
        List<TCommonRecord> allNewDrug = new ArrayList<TCommonRecord>();
        if(newDrug!=null){
        	allNewDrug.addAll(newDrug.values());
        }
        
        //按条件筛选结果集来模仿数据库查询
        //药品编码
        if(!"".equals(p_drug_code)){
        	List<TCommonRecord> temp1 =  new ArrayList<TCommonRecord>();
        	for (TCommonRecord tCommonRecord : allNewDrug) {
        		if(p_drug_code.equals(tCommonRecord.get("drug_code"))) {
        			temp1.add(tCommonRecord);
        		}
			}
        	allNewDrug = temp1;
        } 
        //药品名称模糊匹配
        if(!"".equals(p_drug_name)){
        	List<TCommonRecord> temp1 =  new ArrayList<TCommonRecord>();
        	for (TCommonRecord tCommonRecord : allNewDrug) {
        		if(tCommonRecord.get("drug_name").contains(p_drug_name)) {
        			temp1.add(tCommonRecord);
        		}
			}
        	allNewDrug = temp1;
        } 
        //药品属性
        if(null != toxi_property && toxi_property.length > 0){
        	List<String> toxiPropertyList = new ArrayList<String>(Arrays.asList(toxi_property));
        	List<TCommonRecord> temp1 =  new ArrayList<TCommonRecord>();
        	for (TCommonRecord tCommonRecord : allNewDrug) {
        		if(toxiPropertyList.contains(tCommonRecord.get("toxi_property"))) {
        			temp1.add(tCommonRecord);
        		}
			}
        	allNewDrug = temp1;
        } 
        
        //对筛选后的结果进行分页
        PageView<TCommonRecord> pageView = new PageView<TCommonRecord>(page.getShowCount(),iPage);
        QueryResult<TCommonRecord> queryResult = new QueryResult<TCommonRecord>();
        queryResult.setTotalrecord(allNewDrug.size());

        queryResult.setResultlist(allNewDrug.subList(pageView.getMaxresult()*(iPage-1),
        		(allNewDrug.size()>pageView.getMaxresult()*iPage)?pageView.getMaxresult()*iPage:allNewDrug.size()));
        pageView.setQueryResult(queryResult);
        
        List<TCommonRecord> list = pageView.getRecords();
        mv.addObject("resultList", list);
        page.setCurrentPage(pageView.getCurrentpage());
        page.setCurrentResult((int)pageView.getPageindex().getStartindex());
        page.setTotalResult((int)pageView.getTotalrecord());
        page.setTotalPage((int)pageView.getTotalpage());
        page.setEntityOrField(true);
        mv.addObject("pageIndex", ((page.getCurrentPage() - 1) * page.getShowCount()));
        mv.addObject("pd", pd);
        mv.setViewName("matcher/FetchDataFromDrugDict/FetchDataFromDrugDict");
        return mv;
    }
    
    @RequestMapping(value="/transferAll")
    @ResponseBody
	public Object transferAll(String o, HttpServletRequest request, HttpServletResponse response) {
    	String errInfo = "success";
    	Map<String,Object> map = new HashMap<String,Object>();
        try {
			HttpSession session  = request.getSession();
			//抓取所有的map
			Map<String,TCommonRecord> newDrug = (Map<String,TCommonRecord>)session.getAttribute("allNewDrug");//所有的药品
			List<TCommonRecord> drugList  = new ArrayList<TCommonRecord>();
			drugList.addAll(newDrug.values());

			for(TCommonRecord drug: drugList){
			    saveDataToDrugMap(drug);
			}
			newDrug.clear(); //清空缓存
		} catch (Exception e) {
		    e.printStackTrace();
			errInfo = "操作失败！";
		}
        map.put("result",errInfo);
		return  map ;            
    }
        
    @RequestMapping(value="/transferSingle")
    @ResponseBody
   	public Object  transferSingle(String o, HttpServletRequest request, HttpServletResponse response) {
    	String errInfo = "success";
    	Map<String,Object> map = new HashMap<String,Object>();
        try {
        	HttpSession session  = request.getSession();
        	//抓取所有的map
            String drugCode = CommonUtils.getRequestParameter(request,"drug_code","");
            String drugSpec = CommonUtils.getRequestParameter(request,"drug_spec","").replace(" ", "");
            String drugname = CommonUtils.getRequestParameter(request,"drug_name","").replace(" ", "");
            if(!"".equals(drugCode + drugSpec + drugname))
            {
                @SuppressWarnings("unchecked")
				Map<String,TCommonRecord> newDrug = (Map<String,TCommonRecord>)session.getAttribute("allNewDrug");
                TCommonRecord drug = newDrug.get(drugCode + drugSpec + drugname);
                newDrug.remove(drugCode + drugSpec + drugname);
                saveDataToDrugMap(drug);
            }
        } catch (Exception e) {
			errInfo = "操作失败！";
			e.printStackTrace();
		}
        map.put("result",errInfo);
		return  map ;  
    }

    /**
     * 得到所有的毒理分类
     * @return 毒理分类
     */
    private List<String> getAllToxiPropertyType(){
        List<TCommonRecord> toxiList = null;
        //查询药理分类
        String fieldsSql1 = "toxi_property";
        List<TCommonRecord> orderBy = new ArrayList<TCommonRecord>();
        ICaseHistoryHelper ichh1 = CaseHistoryFactory.getCaseHistoryHelper();


        orderBy.add(CaseHistoryHelperUtils.genGroupCR("toxi_property"));

        try{
            toxiList = ichh1.fetchDrugDict2CR(fieldsSql1,null,orderBy,null,null);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        List<String> propertyList = new ArrayList<String>();
        for(TCommonRecord toxi: toxiList){
            propertyList.add(toxi.get("toxi_property"));
        }

        return propertyList;

    }

    /**
     * 得到所有没有配对的药品
     * @param param 传入的参数
                    p_drug_code:
                    p_drug_name:
                    toxi_property:
     * @return
     */
    private Map<String, TCommonRecord> getNoMapDrug(TCommonRecord param)
    {
        Map<String,TCommonRecord> newDrug = new HashMap<String, TCommonRecord>();
        String p_drug_code = param.get("p_drug_code");
        String p_drug_name = param.get("p_drug_name");
        String[] toxi_property = (String[])param.getObj("toxi_property");

        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        if (!"".equals(p_drug_code))
        {
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("drug_code", p_drug_code, "Char", "", "", "");
            lsWheres.add(crWheres);
        }
        if (!"".equals(p_drug_name))
        {//转换成ASCII
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("drug_name", "%" + p_drug_name + "%", "Char", "like", "", "");
            lsWheres.add(crWheres);
        }
        
        if(null != toxi_property && toxi_property.length > 0) {
	        for(int i=0; i<toxi_property.length; i++)
	        {
	            String toxi = toxi_property[i];
	            if(i==0)
	            {
	                if (!"".equals(toxi)) 
	                {//todo 转换成ASCII
	                    TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("toxi_property", toxi, "Char", "", "","and");
	                    lsWheres.add(crWheres);
	                }
	            }
	            else
	            {
	                if (!"".equals(toxi)) 
	                {
	                    //todo 转换成ASCII
	                    TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("toxi_property", toxi, "Char", "", "","or");
	                    lsWheres.add(crWheres);
	                }
	            }
	        }
        }
        String fields = "drug_code,drug_name,drug_spec,units,drug_form,toxi_property,dose_per_unit,dose_units,drug_indicator,input_code";
        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
        List<TCommonRecord> drugList = null;
        try
        {
            drugList= ichh.fetchDrugDict2CR(fields,lsWheres,null,null,null);
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        if(drugList.size()>0)
        {
            for(TCommonRecord drug : drugList)
            {
                newDrug.put(drug.get("drug_code").replace(" ", "") + drug.get("drug_spec").replace(" ", "") + drug.get("drug_name").replace(" ", ""),drug);
            }
        }

        //2014-04-30 liujc 增加 DRUG_NAME_LOCAL 目的排除code 和 规格一样 名字不一样的药品
        //从DrugMap 中抓取数据, 并删除已经抓取数据
        String sql = "select drug_no_local,drug_spec,drug_name_local from pdss.drug_map";
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");

        List<TCommonRecord> mapDrugs = (List<TCommonRecord>)query.query(sql, new CommonMapper());

        if(mapDrugs.size()>0)
        {
            for(TCommonRecord drug : mapDrugs)
            {
                newDrug.remove(drug.get("drug_no_local").replace(" ", "") + drug.get("drug_spec").replace(" ", "") +  drug.get("drug_name_local").replace(" ", ""));
            }
        }

        return newDrug;
    }

    /**
     * 迁移数据到drug——map
     * @param params
     * 
     * 李果修改于2014年8月6日，将插入修改成预制参数类型，有两个好处
     * 好处一、批量插入时增加了插入速度；
     * 好处二、避免插入值中有特殊字符导致数据库插入失败；
     * 建议所有能用到批量插入的地方都修改成此类型
     */
    private void saveDataToDrugMap(TCommonRecord params){
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        String maxId = this.queryMaxId();
        String sql = "insert into drug_map (drug_map_id, drug_no_local, drug_name_local, drug_spec, units, drug_form, " +
                "dose_per_unit, dose_units, drug_indicator, toxi_property, input_code, oper_time, last_date_time) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Timestamp dateTime = new Timestamp(DateUtils.getLongTime());
       Object[]  sqlParams  = {maxId, params.get("drug_code"),params.get("drug_name"),params.get("drug_spec"),params.get("units"),
    		   params.get("drug_form"),params.get("dose_per_unit"),params.get("dose_units"),params.get("drug_indicator"),
    		   params.get("toxi_property"),params.get("input_code"),dateTime,dateTime};
        query.update(sql, sqlParams);
        // 添加的药品到缓存中。
        DrugUtils.loadDrugMapBySingle(maxId);
    }
    /**
     * 获取最大ID
     * @return
     */
    public String queryMaxId(){
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        String sql = "select max(drug_map_id) maxIndex from drug_map";
        TCommonRecord maxEntity = (TCommonRecord) query.queryForObject(sql, new CommonMapper());
        if (maxEntity == null)
            return "1";
        return (maxEntity.getLong("maxIndex") + 1) + "";
    }


}
