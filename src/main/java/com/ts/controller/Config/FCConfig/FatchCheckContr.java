package com.ts.controller.Config.FCConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.ReportBuilder.Interfaces.IReportBuilder;
import com.hitzd.his.Utils.DateUtils;
import com.ts.FetcherHander.InHospital.Check.InHospitalCheck;
import com.ts.FetcherHander.OutPatient.Check.IOutPatientCheck;
import com.ts.SchedulerHandler.ModelHandler;
import com.ts.controller.base.BaseController;
import com.ts.util.PageData;

/**
 * 
 * @author autumn
 *
 */
@Controller
@RequestMapping(value="/FCConfig")
public class FatchCheckContr extends BaseController
{
    @Resource(name="outPatientCheckBean")
    private IOutPatientCheck OutPatientCheckBean;
    
    @Resource(name="inHospitalCheckBean")
    private InHospitalCheck InHospitalCheckBean;

    @RequestMapping(value="/FCConfigUI")
    public ModelAndView FCConfigUI()throws Exception{
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("Config/FCConfig/FatchCheckConfig");
        return mv;
    }
    
    @RequestMapping(value="/FCConfigExec")
    public ModelAndView ExecConfig() throws Exception {
        ModelAndView mv   = FCConfigUI();
        PageData pd       = this.getPageData();
        String beginDate  = pd.getString("beginDate");
        String endDate    = pd.getString("endDate");
        String fatchType  = pd.getString("fatchType");
        String execType   = pd.getString("execType");
        String reportCode = pd.getString("reportCode");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar  calbeginDate = Calendar.getInstance();
        Calendar  calEndDate   = Calendar.getInstance();
        calbeginDate.setTime(DateUtils.getDateFromString(beginDate));
        calEndDate.setTime(DateUtils.getDateFromString(endDate));
        while(calbeginDate.getTime().getTime() <= calEndDate.getTime().getTime() ){
            switch (fatchType)
            {
                case "-1":
                    execFunction(fatchType,execType, reportCode, sdf.format(calbeginDate.getTime().getTime()));
                    break;
                case "0":
                    execFunction(fatchType,execType, reportCode, sdf.format(calbeginDate.getTime().getTime()));
                    break;
                case "1":
                    execFunction(fatchType,execType, reportCode, sdf.format(calbeginDate.getTime().getTime()));
                    break;
            }
            calbeginDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        //mv.setViewName("");
        mv.addObject("pd", pd);
        return mv;
    }

    private void execFunction(String fatchType,String execType, String reportCode, String strDate)
    {
        switch (execType)
        {
            case "0":
                if("-1".equals(fatchType) || "0".equals(fatchType))
                {
                    ModelHandler mOut = getModelHandler("Drug_presc_Master",strDate);
                    mOut.PerformIt();
                }
                if("-1".equals(fatchType) || "1".equals(fatchType))
                {
                    ModelHandler mIn  = getModelHandler("DataFetcherHospital", strDate);
                    mIn.PerformIt();
                }
                break;
            case "1":
                if("-1".equals(fatchType) || "0".equals(fatchType))
                {
                    checkOuthos(strDate);
                }
                if("-1".equals(fatchType) || "1".equals(fatchType))
                {
                    checkeInHos(strDate);
                }
                break;
            case "2":
                reportBuild(strDate, reportCode);
                break;
        }
    }
    
    private void reportBuild(String strDate,String type){
        
        JDBCQueryImpl peaasQuery = DBQueryFactory.getQuery("ph");
        String sql = "";
        //修正构建报表无法使用"全部"构建功能  2014-07-18 liujc 
        if("-1".equals(type)) 
        {
            sql = "select classpath , REPORTNAME  from REPORTBUILD  where action = 1 ";
        }
        else
        {
            sql = "select classpath , REPORTNAME from REPORTBUILD  where reportcode='" + type + "' and action = 1 ";
        }
        List<TCommonRecord> peaasList = peaasQuery.query(sql.toString(), new CommonMapper());
        String Error = null;
        if(peaasList != null && peaasList.size()!=0)
        {
            for(TCommonRecord tcr : peaasList)
            {
                System.out.println("构建报表为：" + tcr.get("REPORTNAME"));
                Class clazz = null;
                try 
                {
                    clazz = Class.forName(tcr.get("classpath"));
                    IReportBuilder rb = (IReportBuilder) clazz.newInstance();
                    System.out.println("开始构造" + strDate + "的日报表");
                    Error = rb.BuildReport(strDate, null);
                    if (Error.length() > 0)
                    {  
                        System.out.println((new Date()) + " " + Error);
                    }
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 住院审核
     * @param strDate
     */
    private void checkeInHos(String strDate)
    {
        this.InHospitalCheckBean.InHospitalCheck(new Object[]{strDate});
    }
    
    /**
     * 门诊审核 
     * @param strDate
     */
    private void checkOuthos(String strDate)
    {
        OutPatientCheckBean.OutPatientCheck(new Object[]{strDate});
    }
    
    /**
     * 抓取数据 
     * @param groupId
     * @param strDate
     * @return
     */
    private ModelHandler getModelHandler(String groupId, String strDate){
        ModelHandler m = new ModelHandler(groupId);
        List<Object> Param = new ArrayList<Object>();
        Param.add(strDate);
        m.setWriterParam(Param);
        return m; 
    }
}
