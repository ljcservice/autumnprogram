package com.ts.controller.matcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.QueryResult;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.hitzd.his.Web.base.PubServlet;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;

/**
 * 科室提取类
 * @author dcdt
 *
 */
@Controller("FetchDataFromDeptDict")
public class FetchDataFromDeptDict extends PubServlet{

	@Override
	protected void excelPrint(HttpServletRequest request,
			HttpServletResponse response) {
		super.excelPrint(request, response);
	}

	@Override
	protected List<TCommonRecord> insert(HttpServletRequest request,
			HttpServletResponse response) {
		return super.insert(request,response);
	}
	/**
	 * HIS科室展示类
	 */
	@Override
	protected List<TCommonRecord> query(HttpServletRequest request,
			HttpServletResponse response) {
		
		String pageStr = CommonUtils.getRequestParameter(request, "page", "");
        int page = Integer.valueOf(pageStr==null||"".equals(pageStr)?"1":pageStr);
		
		//查询条件
		String deptCode = CommonUtils.getRequestParameter(request, "dept_code", "");
		String deptName = CommonUtils.getRequestParameter(request, "dept_name", "");
		String clinicAttr = CommonUtils.getRequestParameter(request, "clinic_attr", "");
		String outpOrInp = CommonUtils.getRequestParameter(request, "outp_or_inp", "");
		String internalOrSergery = CommonUtils.getRequestParameter(request, "internal_or_sergery", "");
		String inputCode = CommonUtils.getRequestParameter(request, "input_code", "").toUpperCase();
		TCommonRecord param = new TCommonRecord();
		
		request.setAttribute("dept_code", deptCode);
		request.setAttribute("dept_name", deptName);
		request.setAttribute("clinic_attr", clinicAttr);
		request.setAttribute("outp_or_inp", outpOrInp);
		request.setAttribute("internal_or_sergery", internalOrSergery);
		request.setAttribute("input_code", inputCode);
		
		param.set("dept_code", deptCode);
		param.set("dept_name", deptName);
		param.set("clinic_attr", clinicAttr);
		param.set("outp_or_inp", outpOrInp);
		param.set("internal_or_sergery", internalOrSergery);
		param.set("input_code", inputCode);
		
		//Session
		HttpSession session = request.getSession();
		
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
		//组织条件
		
		//组装变化条件，识别变化
		String changeKey = "";
		changeKey += deptCode + "-" + deptName + "-" + clinicAttr + "-" + outpOrInp + "-" + internalOrSergery + "-" + inputCode;
		String oldKey = (String)session.getAttribute("deptChangeKey4Fetch");
		Map<String,TCommonRecord> newDept = (Map<String,TCommonRecord>)session.getAttribute("deptMap4Fetch");
		if(newDept == null || !changeKey.equals(oldKey) || "-----".equals(changeKey)){
			newDept = getNoMapDept(param);
			session.setAttribute("deptChangeKey4Fetch", changeKey);
			session.setAttribute("deptMap4Fetch", newDept);
		}
		//分页操作
		//此时所有的newDept都是最新的
        List<TCommonRecord> allNewDept = new ArrayList<TCommonRecord>();
        allNewDept.addAll(newDept.values());

        PageView<TCommonRecord> pageView = new PageView<TCommonRecord>(12,page);

        QueryResult queryResult = new QueryResult();
        queryResult.setTotalrecord(allNewDept.size());

        queryResult.setResultlist(allNewDept.subList(pageView.getMaxresult()*(page-1),
        		(allNewDept.size()>pageView.getMaxresult()*page)?pageView.getMaxresult()*page:allNewDept.size()));
        
        pageView.setQueryResult(queryResult);
        request.setAttribute("pageView", pageView);
        this.forword = "/WebPage/FetchDataFromDeptDict/FetchDataFromDeptDict.jsp";
        return super.query(request, response);
	}

	@Override
	protected List<TCommonRecord> update(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.update(request, response);
	}

	/**
	 * 增加到PEAAS中
	 */
	@Override
	protected List<TCommonRecord> option(String o, HttpServletRequest request,
			HttpServletResponse response) {
		//Session
		HttpSession session = request.getSession();
		//抓取所有的map
        if("transferAll".equals(o)){
            Map<String,TCommonRecord> newDept = (Map<String,TCommonRecord>)session.getAttribute("deptMap4Fetch");//所有的药品
            List<TCommonRecord> deptList  = new ArrayList<TCommonRecord>();
            deptList.addAll(newDept.values());

            for(TCommonRecord dept: deptList){
                saveDataToDrugMap(dept);
            }
            newDept.clear();//清空缓存
        }else if("transferSingle".equals(o)){
            String deptCode = CommonUtils.getRequestParameter(request,"p_dept_code","");
            if(!"".equals(deptCode)){
                Map<String,TCommonRecord> newDept = (Map<String,TCommonRecord>)session.getAttribute("deptMap4Fetch");
                TCommonRecord dept = newDept.get(deptCode);
                newDept.remove(deptCode);
                saveDataToDrugMap(dept);
            }
        }
        query(request,response);
        return super.option(o, request, response);
	}
	
	/**
     * 迁移数据到dept_dict
     * @param params
     */
    private void saveDataToDrugMap(TCommonRecord params){
        JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
        String sql = "INSERT INTO " +
        		"  PEAAS.DEPT_DICT ( " +
        		" ORDER_NO,DEPT_CODE,DEPT_NAME,DEPT_ALIAS, " +
        		" CLINIC_ATTR,OUTP_OR_INP,INTERNAL_OR_SERGERY,INPUT_CODE, " +
        		" PARENT_DEPT_CODE,PARENT_DEPT_NAME   " +
        		")VALUES( " +
        		" '"+params.get("serial_no")+"','"+params.get("dept_code")+"','"+params.get("dept_name")+"','"+params.get("dept_alias")+"'," +
        		" '"+params.get("CLINIC_ATTR")+"','"+params.get("OUTP_OR_INP")+"','"+params.get("INTERNAL_OR_SERGERY")+"','"+params.get("INPUT_CODE")+"'," +
        		" '"+params.get("DEPT_CODE")+"','"+params.get("DEPT_NAME")+"'" +
        		")";
        query.execute(sql);
    }

	/**
     * 得到所有没有配对的科室
     * @param param 传入的参数
                    dept_code,
                    dept_name,
                    clinic_attr,
                    outp_or_inp,
                    internal_or_sergery,
                    input_code      
     * @return
     */
    private Map<String, TCommonRecord> getNoMapDept(TCommonRecord param){
        Map<String,TCommonRecord> newDept = new LinkedHashMap<String, TCommonRecord>();
        String deptCode = param.get("dept_code", "");
		String deptName = param.get("dept_name", "");
		String clinicAttr = param.get("clinic_attr", "");
		String outpOrInp = param.get("outp_or_inp", "");
		String internalOrSergery = param.get("internal_or_sergery", "");
		String inputCode = param.get("input_code", "");

        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        if (!"".equals(deptCode)){
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("dept_code", "%" + deptCode + "%", "Char", "like", "", "");
            lsWheres.add(crWheres);
        }
        if (!"".equals(deptName)) {//todo 转换成ASCII
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("dept_name", "%" + deptName + "%", "Char", "like", "", "");
            lsWheres.add(crWheres);
        }
        if (!"".equals(clinicAttr)) {//todo 转换成ASCII
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("clinic_attr", clinicAttr, "Char", "", "", "");
            lsWheres.add(crWheres);
        }
        if (!"".equals(outpOrInp)) {//todo 转换成ASCII
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("outp_or_inp", outpOrInp, "Char", "", "", "");
            lsWheres.add(crWheres);
        }
        if (!"".equals(internalOrSergery)) {//todo 转换成ASCII
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("internal_or_sergery", internalOrSergery, "Char", "", "", "");
            lsWheres.add(crWheres);
        }
        if (!"".equals(inputCode)) {//todo 转换成ASCII
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereGbkCR("input_code", "%" + inputCode + "%", "Char", "like", "", "");
            lsWheres.add(crWheres);
        }
        List<TCommonRecord> orders = new ArrayList<TCommonRecord>();
        TCommonRecord order = CaseHistoryHelperUtils.genOrderCR("serial_no", "");
        orders.add(order);
        

        String fields = "serial_no,dept_code,dept_name,dept_alias,clinic_attr,outp_or_inp,internal_or_sergery,input_code";
        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();

        List<TCommonRecord> drugList = null;
        try{
            drugList= ichh.fetchDeptDict2CR(fields,lsWheres,null,orders,null);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        if(drugList.size()>0){
            for(TCommonRecord drug : drugList){
                newDept.put(drug.get("dept_code"),drug);
            }
        }

        //从dept_dict中查询, 并删除已经抓取数据
        String sql = "select dept_code from peaas.dept_dict";
        JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");

        List<TCommonRecord> mapDrugs = (List<TCommonRecord>)query.query(sql, new CommonMapper());

        if(mapDrugs.size()>0){
            for(TCommonRecord drug : mapDrugs){
                newDept.remove(drug.get("dept_code"));
            }
        }
        return newDept;
    }
}
