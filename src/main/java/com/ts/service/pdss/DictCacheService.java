//package com.ts.service.pdss;
//
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import javax.annotation.Resource;
//import org.springframework.stereotype.Service;
//import com.hitzd.his.Utils.Config;
//import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
//import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
//import com.ts.dao.DaoSupportPH;
//import com.ts.dao.DaoSupportPdss;
//import com.ts.util.PageData;
//
///**
// * 数据来源 his 运行字典类
// * @author Administrator
// *
// */
//public class DictCacheService
//{
//    /* 医生 */
//    public static HashMap<String, PageData>   doctorMap = null;
//    /* 部门  */
//    public static HashMap<String, PageData>     deptMap = null;
//    /* 大部门 */
//    public static HashMap<String, PageData>     mergeDeptMap = null;
//    /* 标本 */
//    public static HashMap<String, PageData>    speciMap = null;
//    /* 微生物字典表*/
//    public static HashMap<String, PageData> germCodeMap = null;
//    /* 细菌药敏字典 */
//    public static HashMap<String, PageData> germSensMap = null;
//    /* 药品字典 */
//    public static HashMap<String, PageData> drugCodeMap = null;
//    /* 药品字典  code + units + drug_spec */
//    public static HashMap<String, PageData> drugdictMap = null;
//    /* 药品类字典 */
//    public static HashMap<String, PageData> drugClassMap = null;
//    /* 用药途径字典 */
//    public static HashMap<String, PageData>     adminMap = null;
//    /* 诊断字典*/
//    public static HashMap<String, PageData> diagnosisMap = null;
//    /* 频次字典*/
//    public static HashMap<String, PageData>   performMap = null;
//    /* 费别字典*/
//    public static HashMap<String, PageData>   chargeMap = null;
//    /* 身份字典*/
//    public static HashMap<String, PageData>   identityMap = null;
//    /* 剂型字典*/
//    public static HashMap<String, PageData>   formMap = null;
//    
//    
//	@Resource(name = "daoSupportHis")
//	private DaoSupportPdss daoHis;
//	
//	@Resource(name = "daoSupportPH")
//	private DaoSupportPH daoPH;
//    
//    public void initCache()
//    {
//        System.out.println("---------------------工厂创建 ");
//        doctorMap    = new HashMap<String, PageData>();
//        deptMap      = new LinkedHashMap<String, PageData>();
//        mergeDeptMap = new LinkedHashMap<String, PageData>();
//        speciMap     = new HashMap<String, PageData>();
//        drugCodeMap  = new HashMap<String, PageData>();
//        drugdictMap  = new HashMap<String, PageData>();
//        drugClassMap = new HashMap<String, PageData>();
//        germSensMap  = new HashMap<String, PageData>();
//        germCodeMap  = new HashMap<String, PageData>();
//        adminMap     = new HashMap<String, PageData>();
//        diagnosisMap = new HashMap<String, PageData>();
//        performMap   = new HashMap<String, PageData>();
//        chargeMap    = new HashMap<String, PageData>();
//        identityMap  = new HashMap<String, PageData>();
//        formMap      = new HashMap<String, PageData>();
//        //iasQuery     = DBQueryFactory.getQuery("IAS"); 
////        peaasQuery   = DBQueryFactory.getQuery("PEAAS");
//        /* 部门 */
//        this.setDept();
//        /* 大部门 */
//        this.setMergeDept();
//        /* 医生基本信息，构建时来源为用户维护表，使用过程中如没有，则自动缓存，但不添加数据库中，许用户自己维护*/
//        this.setDoctorCode();
//        /* 标本 */
//        this.setSpeci();
//        /* 微生物字典表 */
//        //this.setGermCode();
//        /* 细菌药敏字典  */
//        //this.setGermSens();
//        /* 药品字典表 */
//        this.setDrugDict();
//        /* 药品类字典表 */
//        this.setDrugClassDict();
//        /* 给药途径 */
//        this.setAdmin();
//        /* 诊断字典 */
//        this.setDiagnosis();
//        /* 频率字典 */
//        this.setPerformMap();
//        /* 费别字典 */
//        this.setChargeMap();
//        /* 身份字典 */
//        this.setIdentityMap();
//        /* 剂型字典 */
//        this.setFormMap();
//        System.out.println("---------------------工厂创建结束 ");
//    }
//
//
//    private void setPerformMap()
//    {
//        try
//        {
////        	ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////            List<PageData> list = ichh.fetchPerformFreqDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//            List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findPerformFreqDict", new PageData());
//            for(PageData t :list)
//            {
//                if(!t.get("serial_no").equals(""))
//                    performMap.put(t.getString("serial_no").toUpperCase(), t);    
//            }    
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    @SuppressWarnings("unchecked")
//    private void setAdmin()
//    {
//        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
//        try
//        {
////            List<PageData> list = ichh.fetchAdministrationDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//            List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findAdministrationDict", new PageData());
//            for(PageData t :list)
//            {
//                if(!t.get("serial_no").equals(""))
//                    adminMap.put(t.getString("serial_no").toUpperCase(), t);    
//            }            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    private void setDiagnosis()
//    {
//        try
//        {
////            List<PageData> list = ichh.fetchDiagnosisDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//            List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findDiagnosisDict", new PageData());
//            for(PageData t : list)
//            {
//                PageData tCom = diagnosisMap.get(t.get("diagnosis_code")) ;
//                if(tCom == null) 
//                {
//                    diagnosisMap.put(t.getString("diagnosis_code").toUpperCase(), t);
//                }
//                else
//                {
//                    tCom.put("diagnosis_name", tCom.getString("diagnosis_name") + ",[" + t.getString("diagnosis_name") + "]" );
//                    diagnosisMap.put(t.getString("diagnosis_code").toUpperCase(), tCom);
//                }
//            }            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * 频次 字典 
//     * 根据名字 得到 序号 
//     * @param perFormName
//     * @return
//     */
//    public PageData getPerformMapByName(String perFormName)
//    {
//        for(PageData cr : performMap.values())
//        {
//            if(perFormName.equalsIgnoreCase(cr.getString("freq_desc")))
//                return cr;
//        }
//        return new PageData();
//    }
//    
//    /**
//     * 频次字典表 
//     * @param admin  ： serial_no
//     * @return
//     */
//    public PageData getPerForm(String serial_no)
//    {
//        if (!performMap.containsKey(serial_no.toUpperCase()))
//            return new PageData();
//        return performMap.get(serial_no.toUpperCase());
//    }
//    
//    /**
//     * 用 名字检索id 诊断
//     * @param diagName
//     * @return
//     */
//    public PageData getDiagnosisByName(String diagName)
//    {
//        for(PageData cr : diagnosisMap.values())
//        {
//            if(diagName.trim().indexOf(cr.getString("diagnosis_Name")) != -1 )
//                return cr;
//            if(cr.getString("diagnosis_Name").indexOf(diagName) != -1)
//                return cr;
//        }  
//        return new PageData();
//    }
//    
//    /**
//     * his 中用code 查询出名字 
//     * @param diagID
//     * @return
//     */
//    public PageData getDiagnosisById(String diagID)
//    {
//        PageData result = this.diagnosisMap.get(diagID);
//        if(result == null)
//            result = new PageData();
//        return result;
//    }
//    
//    /**
//     * 用名字检索id 用药途径
//     * @param adminName
//     * @return
//     */
//    public PageData getAdminByName(String adminName)
//    {
//        for (PageData cr: adminMap.values())
//        {
//            if (adminName.equalsIgnoreCase(cr.getString("ADMINISTRATION_NAME")))
//                return cr;
//        }
//        return new PageData();
//    }
//    
//    /**
//     * 用药途径
//     * @param admin  ： serial_no
//     * @return
//     */
//    public PageData getAdmin(String admin)
//    {
//        if (!adminMap.containsKey(admin.toUpperCase()))
//            return new PageData();
//        return adminMap.get(admin.toUpperCase());
//    }
//    
//    /**
//     * 药品字典表
//     * @param hisQuery
//     */
//    private void setDrugDict()
//    {
//        try
//        {
//        	//TODO 长海 
//            /*String sql = "select * from comm.pham_basic_info";
//            List<PageData> list = hisQuery.query(sql, new CommonMapper());
//            for(PageData t :list)
//            {
//                drugCodeMap.put(t.get("pham_code").toUpperCase(), t);
//                drugdictMap.put(t.get("pham_code").toUpperCase() + t.get("pham_unit").toUpperCase() + t.get("pham_spec").toUpperCase(), t);
//            }*/
////            List<PageData> list = ichh.fetchDrugDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//            List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findDrugDict", new PageData());
//            for(PageData t :list)
//            {
//                drugCodeMap.put(t.getString("drug_code").toUpperCase(), t);
//                drugdictMap.put(t.getString("drug_code").toUpperCase() + t.getString("units").toUpperCase() + t.getString("drug_spec").toUpperCase(), t);
//            }            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * 药品字典表
//     * @param hisQuery
//     */
//    private void setDrugClassDict()
//    { 
//        try
//        {
////        	ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////            List<PageData>  list = ichh.fetchDrugClassDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//            List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findDrugClassDict", new PageData());
//            for(PageData t :list)
//            {
//            	drugClassMap.put(t.getString("class_code").toUpperCase(), t);    
//            }    
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    public String getDrugClassName(String drugCode)
//    {
//        if (drugClassMap.containsKey(drugCode.toUpperCase()))
//        {
//            return drugClassMap.get(drugCode.toUpperCase()).getString("CLASS_NAME");
//        }
//        return "";
//    }
//    
//    /**
//     * 细菌药敏字典 
//     * @param hisQuery
//     */
//    private void setGermSens()
//    {
////        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////        String strFields = "item_code,item_name";
//        try
//        {
////            List<PageData> list = ichh.fetchGermdrugsensitDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findGermdrugsensitDict", new PageData());
//        	for(PageData t :list)
//            {
//                germSensMap.put(t.getString("ITEM_CODE").toUpperCase(), t);    
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    
//    /**
//     * 微生物字典表 
//     * @param hisQuery
//     */
//    private void setGermCode()
//    {
//        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
//        try
//        {
//            PageData pd = new PageData();
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictPhMapper.findGermCodeDict", pd);
//            for(PageData t :list)
//            {
//                germCodeMap.put(t.getString("GERM_CODE").toUpperCase(), t);    
//            }            
//        }
//        catch(Exception e )
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    
//    /**
//     * 标本信息 
//     * @param hisQuery
//     */
//    private void setSpeci()
//    {
//        try
//        {
////        	ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////            List<PageData> list = ichh.fetchSpecimanDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//            List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findSpecimanDict", new PageData());
//            for(PageData t :list)
//            {
//                speciMap.put(t.getString("SPECIMAN_CODE").toUpperCase(), t);    
//            }
//            
//        }
//        catch(Exception e )
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * 返回 自定义的规则码 
//     * @param typeCode
//     */
//    public String getDrugTypeCode(String typeCode)
//    {
//        String re = Config.getParamValue(typeCode);
//        if(re == null)
//            new RuntimeException("没找到相对应的规则号！");
//        return re;
//    }
//    
//    /**
//     * 初始化 
//     * 医生信息
//     * @param query
//     */
//    @SuppressWarnings ("unchecked")
//    private void setDoctorCode()
//    {
//        /*  2014-10-29 liujc 修改  将医生基本信息让用户自己维护*/
////         = DBQueryFactory.getQuery("pdss");
////        List<PageData> list = query.query("select * from staff_dict ", new CommonMapper());
//        try
//        {
//        	//TODO xsl 到底用谁的dao
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictPHMapper.findSpecimanDict", new PageData());
//            for(PageData ds : list)
//            {
//                doctorMap.put(ds.getString("NAME").toUpperCase(), ds);
//            }
//        }
//        catch(Exception e )
//        {
//            e.printStackTrace();
//        }
//        
//        /*
//        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
//        String strFields = "*";
//        List<PageData> lsWheres = new ArrayList<PageData>();
//        List<PageData> lsGroup = new ArrayList<PageData>();
//        List<PageData> lsOrder = new ArrayList<PageData>();
//        try
//        {
//            //EMP_NO,NAME
//            List<PageData> list = ichh.fetchStaffDict2CR(strFields, lsWheres, lsGroup, lsOrder, query);
//            for(PageData ds : list)
//            {
//                doctorMap.put(ds.get("NAME").toUpperCase(), ds);
//            }            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            strFields = null;
//            lsWheres  = null;
//            lsGroup   = null;
//            lsOrder   = null;
//            ichh      = null;
//        }*/
//    }
//    
//    /**
//     * 初始化  
//     * 部门信息 
//     * @param query
//     */
//    private void setDept()
//    {
//        try
//        {
////	          ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////            PageData order = CaseHistoryHelperUtils.genOrderCR("dept_Name", "desc");
////            lsOrder.add(order);
////            List<PageData> depts = ichh.fetchDeptDict2CR(strFields, lsWheres, lsGroup, lsOrder, query);
//            List<PageData> depts = (List<PageData>) daoHis.findForList("DictHisMapper.findDeptDictHis", new PageData());
//        	for(PageData d : depts )
//            {
//                deptMap.put(d.getString("DEPT_CODE").toUpperCase(), d);
//            }            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * 初始化  
//     * 大部门信息 
//     * @param query
//     */
//    private void setMergeDept()
//    {
//        try
//        {
////            String sql = "SELECT * FROM peaas.dept_dict WHERE DEPT_CODE=PARENT_DEPT_CODE order by order_no";
////            4PEAAS = DBQueryFactory.getQuery("PEAAS");
////            List<PageData> depts = query4PEAAS.query(sql, new CommonMapper());
//            List<PageData> depts = (List<PageData>) daoPH.findForList("DictPhMapper.findDeptDictPH", new PageData());
//            for (PageData d : depts)
//            {
//                mergeDeptMap.put(d.getString("dept_code").toUpperCase(), d);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * 初始化
//     * 费别信息 
//     * @param query
//     */
//    private void setChargeMap()
//    {
//        try
//        {
////        	ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////            List<PageData> charge = ichh.fetchChargeTypeDict2CR(strFields, lsWheres, lsGroup, lsOrder, query);
//        	List<PageData> charge = (List<PageData>) daoHis.findForList("DictHisMapper.findChargeTypeDict", new PageData());
//        	for(PageData d : charge )
//            {
//                chargeMap.put(d.getString("charge_type_code").toUpperCase(), d);
//            }            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * 初始化
//     * 身份信息 
//     * @param query
//     */
//    private void setIdentityMap()
//    {
//        try
//        {
////			ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////			//            List<PageData> identity = ichh.fetchIdentityDict2CR(strFields, lsWheres, lsGroup, lsOrder, query);
//        	List<PageData> identity = (List<PageData>) daoHis.findForList("DictHisMapper.findIdentityDictHis", new PageData());
//            for(PageData d : identity )
//            {
//                identityMap.put(d.getString("identity_code").toUpperCase(), d);
//            }            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * 初始化
//     * 剂型信息 
//     * @param query
//     */
//    private void setFormMap()
//    {
//        try
//        {
//            List<PageData> drug_form = (List<PageData>) daoHis.findForList("DictHisMapper.findDrugFormDict", new PageData());
//            for(PageData d : drug_form )
//            {
//                formMap.put(d.getString("form_code").toUpperCase(), d);
//            }            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * 根据医生姓名获得医生代码
//     * @param DoctorName
//     * @return
//     */
//    public String getDoctorCode( String DoctorName)
//    {
//    	if(doctorMap.containsKey(DoctorName))
//    	{
//    		return doctorMap.get(DoctorName.toUpperCase()).getString("EMP_NO");
//    	}
//        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
//        try
//        {
////            PageData where = CaseHistoryHelperUtils.genWhereGbkCR("name", DoctorName, "Char", "", "", "");
////            lsWheres.add(where);
////            List<PageData> list = ichh.fetchStaffDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//            PageData pd = new PageData();
//            pd.put("name", DoctorName);
//            List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findStaffDict", pd);
//            PageData doctorInfo = list != null && list.size() > 0 ?list.get(0) : null;
//            if(doctorInfo != null)
//                doctorMap.put(DoctorName.toUpperCase(), doctorInfo);
//            return doctorInfo != null?doctorInfo.getString("EMP_NO"):"";
//        }
//        catch(Exception e )
//        {
//            e.printStackTrace();
//        }
//        return "";
//    }
//    
//    /**
//     *  返回所有医生记录
//     * @param hisQuery
//     * @param DoctorName
//     * @return
//     */
//    public PageData getDoctorInfo(String DoctorName)
//    {
//    	if(doctorMap.containsKey(DoctorName))
//    	{
//    		return doctorMap.get(DoctorName);
//    	}
//        try
//        {
////        	ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////            PageData where = CaseHistoryHelperUtils.genWhereGbkCR("name", DoctorName, "Char", "", "", "");
////            lsWheres.add(where);
////            List<PageData> list = ichh.fetchStaffDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//            PageData pd = new PageData();
//            pd.put("name", DoctorName);
//            List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findStaffDict", pd);
//            PageData doctorInfo = list != null && list.size() > 0 ?list.get(0) : null;
//            if(doctorInfo != null){
//            	doctorMap.put(DoctorName.toUpperCase(), doctorInfo);
//            	return doctorInfo;
//            }
//        }
//        catch(Exception e )
//        {
//            e.printStackTrace();
//        }
//        return new PageData();
//    }
//    
//    /**
//     * 根据部门代码获得部门名称
//     * @param DeptName
//     * @return
//     */
//    @Deprecated
//    public String getDeptName2(  String DeptCode)
//    {
////        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////        String strFields = "*";
////        List<PageData> lsWheres = new ArrayList<PageData>();
////        List<PageData> lsGroup = new ArrayList<PageData>();
////        List<PageData> lsOrder = new ArrayList<PageData>();
////        try
////        {   if (deptMap.containsKey(DeptCode.toUpperCase()))
////                return deptMap.get(DeptCode.toUpperCase()).get("DEPT_NAME");
////            PageData where = CaseHistoryHelperUtils.genWhereCR("dept_code", DeptCode, "Char", "", "", "");
////            lsWheres.add(where);
////            List<PageData> list = ichh.fetchDeptDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
////            PageData deptInfo = list != null && list.size() > 0 ?list.get(0):new PageData();
////            if(deptInfo != null)
////                deptMap.put(DeptCode.toUpperCase(), deptInfo);
////            return deptInfo!= null?deptInfo.get("DEPT_NAME"):"";
////        }
////        catch(Exception e)
////        {
////            e.printStackTrace();
////        }
////        finally
////        {
////            strFields = null;
////            ichh      = null;
////            lsWheres  = null;
////            lsGroup   = null;
////            lsOrder   = null;
////        }
//        return "";
//    }
//    /**
//     * 根据部门代码获得部门名称
//     * @param DeptName
//     * @return
//     */
//    public String getDeptName(String DeptCode)
//    {
//        try
//        {   
//        	if (deptMap.containsKey(DeptCode.toUpperCase()))
//        		return deptMap.get(DeptCode.toUpperCase()).getString("DEPT_NAME");
//        	PageData pd = new PageData();
//        	pd.put("dept_code", DeptCode);
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findDeptDictHis", pd);
//            PageData deptInfo = list != null && list.size() > 0 ?list.get(0):new PageData();
//            if(deptInfo != null){
//            	deptMap.put(DeptCode.toUpperCase(), deptInfo);
//            	return deptInfo.getString("DEPT_NAME");
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        return "";
//    }
//    
//    /**
//     * 获得大科室名称
//     * @param deptCode
//     * @return
//     */
//    public PageData getMergeDept(String deptCode)
//    {
//        if (mergeDeptMap.containsKey(deptCode.toUpperCase()))
//        {
//            return mergeDeptMap.get(deptCode.toUpperCase());
//        }
//        try
//        {
////            String sql = "select * from peaas.dept_dict where dept_code='" + deptCode.toUpperCase() + "'";
////            4PEAAS = DBQueryFactory.getQuery("PEAAS");
////            List<PageData> depts = query4PEAAS.query(sql, new CommonMapper());
//        	
//        	PageData pd = new PageData();
//        	pd.put("dept_code", deptCode);
//        	List<PageData> depts = (List<PageData>) daoHis.findForList("DictPhMapper.findDeptDictPH", pd);
//            if (depts != null && depts.size() > 0) {
//                return new PageData();
//            }  else {
//                mergeDeptMap.put(depts.get(0).getString("dept_code").toUpperCase(), depts.get(0));
//                return depts.get(0);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return new PageData();
//    }
//
//    /**
//     * 返回所有部门信息 
//     * @param hisQuery
//     * @param deptCode
//     * @return
//     */
//    public PageData getDeptInfoHis(String deptCode)
//    {
//        try
//        {   
//        	if (deptMap.containsKey(deptCode.toUpperCase()))
//        		return deptMap.get(deptCode.toUpperCase());
//        	PageData pd = new PageData();
//        	pd.put("dept_code", deptCode);
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findDeptDictHis", pd);
//            PageData deptInfo = list != null && list.size() > 0 ?list.get(0):new PageData();
//            if(deptInfo != null){
//            	deptMap.put(deptCode.toUpperCase(), deptInfo);
//            	return deptInfo;
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    
//    /**
//     * 返回所有部门 
//     * @return
//     */
//    @SuppressWarnings ("static-access")
//    public List<PageData> getDeptAll()
//    {
//        List<PageData> list = new LinkedList<PageData>();
//        for(PageData t : this.deptMap.values())
//        {
//            list.add(t);
//        }
//        return list;
//    }
//
//    /**
//     * 返回所有部门
//     * 
//     * @return
//     */
//    @SuppressWarnings ("static-access")
//    public List<PageData> getMergeDeptAll()
//    {
//        List<PageData> list = new LinkedList<PageData>();
//        for (PageData t : this.mergeDeptMap.values())
//        {
//            list.add(t);
//        }
//        return list;
//    }
//    
//    /**
//     * 返回所有费别 
//     * @return
//     */
//    @SuppressWarnings ("static-access")
//    public List<PageData> getChargeAll()
//    {
//        List<PageData> list = new LinkedList<PageData>();
//        for(PageData t : this.chargeMap.values())
//        {
//            list.add(t);
//        }
//        return list;
//    }
//    
//    /**
//     * 返回所有身份
//     * @return
//     */
//    @SuppressWarnings ("static-access")
//    public List<PageData> getIdentityAll()
//    {
//        List<PageData> list = new LinkedList<PageData>();
//        for(PageData t : this.identityMap.values())
//        {
//            list.add(t);
//        }
//        return list;
//    }
//    /**
//     * 返回所有剂型
//     * @return
//     */
//    @SuppressWarnings ("static-access")
//    public List<PageData> getFormAll()
//    {
//        List<PageData> list = new LinkedList<PageData>();
//        for(PageData t : this.formMap.values())
//        {
//            list.add(t);
//        }
//        return list;
//    }
//    /**
//     * 按条件过去数据 
//     * 目前只针对单对条件 
//     * @return
//     */
//    @SuppressWarnings ("static-access")
//    public List<PageData> getDeptWhere(String key,String value)
//    {
//        List<PageData> list = new LinkedList<PageData>();
//        for(PageData t : this.deptMap.values())
//        {
//            if(value.equals(t.get(key)))
//            {
//                list.add(t);    
//            }
//        }
//        return list;
//    }
////    
////    private PageData wheresEquals(PageData t ,String[] keys,String[] values )
////    {
////        for(int i = 0 ;i<keys.length;i++)
////        {
////            if(values[i].equals(t.get(keys[i])))
////            {
////                return t;
////            }
////        }
////        return null;
////    }
//    
//    /**
//     * 返回标本信息 
//     * @param hisQuery
//     * @param code
//     * @return
//     */
//    public PageData getSpeciInfo(String code)
//    {
//    	if(speciMap.containsKey(code.toUpperCase()))
//    		return speciMap.get(code.toUpperCase());
//        try
//        {
////            ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////            PageData where = CaseHistoryHelperUtils.genWhereCR("SPECIMAN_CODE", code, "Char", "", "", "");
////            List<PageData> list = ichh.fetchSpecimanDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//        	
//        	PageData pd = new PageData();
//        	pd.put("SPECIMAN_CODE", code);
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findSpecimanDict", pd);
//        	PageData speci = list != null && list.size() > 0 ?list.get(0):new PageData();
//            if(speci == null)
//              speci = new PageData();
//            speciMap.put(speci.getString("SPECIMAN_CODE").toUpperCase(),speci);
//            return speci;
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        return new PageData();
//    }
//    
//    /**
//     * 返回所有标本信息
//     * @return
//     */
//    @SuppressWarnings ("static-access")
//    public List<PageData> getSpeciAll()
//    {
//        List<PageData> list = new LinkedList<PageData>();
//        for(PageData t : this.speciMap.values())
//        {
//            list.add(t);
//        }
//        return list;
//    }
//    
//    /**
//     * 细菌药敏字典  
//     * @param hisQuery
//     * @param code
//     * @return
//     */
//    public PageData getGermSensInfo(String code)
//    {
////        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////        String strFields = "*";
////        List<PageData> lsWheres = new ArrayList<PageData>();
////        List<PageData> lsGroup = new ArrayList<PageData>();
////        List<PageData> lsOrder = new ArrayList<PageData>();
////        try
////        {
////            if(germSensMap.containsKey(code.toUpperCase()))
////                return germSensMap.get(code.toUpperCase());
////            PageData where = CaseHistoryHelperUtils.genWhereCR("Item_Code", code, "Char", "", "", "");
////            lsWheres.add(where);
////            PageData group = CaseHistoryHelperUtils.genGroupCR("item_code");
////            lsGroup.add(group);
////            group = CaseHistoryHelperUtils.genGroupCR("item_name");
////            lsGroup.add(group);
////            List<PageData> list = ichh.fetchGermdrugsensitDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
////            PageData germSens = list != null && list.size() > 0 ? list.get(0):new PageData();
////            if(germSens == null)
////                germSens = new PageData();
////            germSensMap.put(code.toUpperCase(),germSens);
////            return germSens;
////        }
//	      if(germSensMap.containsKey(code.toUpperCase()))
//	      return germSensMap.get(code.toUpperCase());
//        try
//        {
//        	PageData pd = new PageData();
//        	pd.put("Item_Code", code);
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findSpecimanDict", pd);
//        	PageData speci = list != null && list.size() > 0 ?list.get(0):new PageData();
//            if(speci == null)
//              speci = new PageData();
//            germSensMap.put(code.toUpperCase(),speci);
//            return speci;
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        return new PageData();
//    }
//    
//    /**
//     * 微生物字典表 
//     * @param hisQuery
//     * @param code
//     * @return
//     */
//    public PageData getGermCodeInfo(String code)
//    {
//        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
//        try
//        {
////            PageData where = CaseHistoryHelperUtils.genWhereCR("Germ_code", code, "Char", "", "", "");
////            lsWheres.add(where);
////            List<PageData> list = ichh.fetchGermCodeDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//        	PageData pd = new PageData();
//        	pd.put("Germ_code", code);
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findGermCodeDict", pd);
//            PageData germCode = list != null && list.size() > 0 ?list.get(0):new PageData();
//            if(germCode == null)
//                germCode = new PageData();
//            germCodeMap.put(code.toUpperCase(),germCode);
//            return germCode;
//        }
//        catch(Exception e )
//        {
//            e.printStackTrace();
//        }
//        return new PageData();
//    }
//    /**
//     * 返回所有细菌名字
//     * @return
//     */
//    /*public List<PageData>  getGermCodeAll()
//    {
//        List<PageData>  list = new ArrayList<PageData>();
//        for(PageData t : germCodeMap.values())
//        {
//            list.add(t);
//        }
//        return list;
//    }
//    
//    /**
//     * 按编号取得 药品信息
//     * @param code
//     * @return
//     */
//    public PageData getDrugDictInfo(String code)
//    {
//        if(drugCodeMap.containsKey(code.toUpperCase()))
//            return drugCodeMap.get(code.toUpperCase());
//        try
//        {
////        	ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////            PageData where = CaseHistoryHelperUtils.genWhereCR("drug_code", code, "Char", "", "", "");
////            List<PageData> list = ichh.fetchDrugDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
//        	PageData pd = new PageData();
//        	pd.put("drug_code", code);
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findDrugDict", pd);
//            PageData drugdict   = list != null && list.size() > 0 ?list.get(0):new PageData();
//            if(drugdict == null)
//                drugdict = new PageData();
//            drugCodeMap.put(code.toUpperCase(),drugdict);
//            return drugdict;
//        }
//        catch(Exception e )
//        {
//            e.printStackTrace();
//        }
//        return new PageData();
//    }
//    
//    /**
//     * 按编号取得 药品信息
//     * @param code
//     * @return
//     * 这个应该废弃，不在使用
//     */
//    @Deprecated
//    public PageData getDrugDictInfo2( String code)
//    {
////        if(drugCodeMap.containsKey(code.toUpperCase()))
////            return drugCodeMap.get(code.toUpperCase());
////        ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
////        try
////        {
////            PageData where = CaseHistoryHelperUtils.genWhereCR("drug_code", code, "Char", "", "", "");
////            lsWheres.add(where);
////            List<PageData> list = ichh.fetchDrugDict2CR(strFields, lsWheres, lsGroup, lsOrder, hisQuery);
////        	PageData pd = new PageData();
////        	pd.put("drug_code", code);
////        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findDrugDict", pd);
////            PageData drugdict = list != null && list.size() > 0 ?list.get(0):new PageData();
////            if(drugdict == null)
////                drugdict = new PageData();
////            drugCodeMap.put(code.toUpperCase(),drugdict);
////            return drugdict;
////        }
////        catch(Exception e )
////        {
////            e.printStackTrace();
////        }
////        finally
////        {
////            strFields = null;
////            lsWheres  = null;
////            lsGroup   = null;
////            lsOrder   = null;
////            ichh      = null;
////        }
//        return new PageData();
//    }
//    
//    /**
//     * 按编号 包装  规格 
//     * @param key =  drug_code + units + drug_spec
//     * @return
//     */
//    public PageData getDrugDictKeyInfo(String Key)
//    {
//        if(drugdictMap.containsKey(Key.toUpperCase()))
//            return drugdictMap.get(Key.toUpperCase());
//        PageData drugdict = new PageData();
//        drugdictMap.put(Key.toUpperCase(),drugdict);
//        return drugdict;
//    }
//    
//    public PageData getDrugInfoByCodeUnitSpec(String DrugCode, String Units, String DrugSpec)
//    {
//        return getDrugDictKeyInfo(DrugCode + Units + DrugSpec);
//    }
//
//    /**
//     * 按编号模糊查询药品名称
//     * @param code
//     * @return
//     */
//    public String getDrugNameLikely( String code)
//    {
//        try
//        {
//        	PageData pd = new PageData();
//        	pd.put("drug_code", code);
//        	List<PageData> list = (List<PageData>) daoHis.findForList("DictHisMapper.findDrugDictLike", pd);
//            PageData drugdict   = list != null && list.size() > 0 ?list.get(0):new PageData();
//            if(drugdict == null)
//                drugdict = new PageData();
//            return drugdict.getString("drug_NAME");
//        }
//        catch(Exception e )
//        {
//            e.printStackTrace();
//        }
//        return "";
//    }
//    
//    /**
//     * 获得所有药品信息 
//     * @return
//     */
//    public List<PageData> getDrugDictAll()
//    {
//        List<PageData> list = new LinkedList<PageData>();
//        for(PageData t : drugCodeMap.values())
//        {
//            list.add(t);
//        }
//        return list;
//    }
//    
//    /* 查找合大科室中的所有小科室 */
//    @SuppressWarnings ("unchecked")
//    public String getSearchMergeDept(String code)
//    {
//        int i = 0;
//        String searchmergeDeptMap = "";
//        try
//        {
////            String sql = "SELECT dept_code FROM peaas.dept_dict WHERE PARENT_DEPT_CODE='" + parm + "'  order by order_no";
////             = DBQueryFactory.getQuery("PEAAS");
////            List<PageData> depts = query.query(sql, new CommonMapper());
//        	
//            	PageData pd = new PageData();
//            	pd.put("PARENT_DEPT_CODE", code);
//            	List<PageData> depts = (List<PageData>) daoHis.findForList("DictPhMapper.findDeptDictPH", pd);
//            if (depts != null && depts.size() > 0)
//            {
//                for (PageData d : depts)
//                {
//                    i++;
//                    if (i == 1)
//                    {
//                        searchmergeDeptMap = d.getString("dept_code").toUpperCase();
//                    }
//                    else
//                    {
//                        searchmergeDeptMap += "," + d.getString("dept_code").toUpperCase();
//                    }
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return searchmergeDeptMap;
//    }
//}
