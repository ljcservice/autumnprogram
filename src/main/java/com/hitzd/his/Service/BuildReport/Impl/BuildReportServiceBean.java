package com.hitzd.his.Service.BuildReport.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.Impl.BasePageBean;
import com.hitzd.his.Service.BuildReport.BuildReportService;
import com.hitzd.DBUtils.CommonMapper;

@Service
@Transactional
public class BuildReportServiceBean extends BasePageBean implements BuildReportService
{
    @Override
    public PageView<TCommonRecord> getList(TCommonRecord pram)
    {
        setQueryCode("PEAAS");
        StringBuffer sql = new StringBuffer();
        if(pram.get("reportCode") != null && !"".equals(pram.get("reportCode")))
        {
            sql.append(" and reportCode like '%").append(pram.get("reportCode")).append("%'");
        }
        if(pram.get("reportName") != null && !"".equals(pram.get("reportName")))
        {
            sql.append(" and reportName like '%").append(pram.get("reportName")).append("%'");
        }
        if(pram.get("systemCode") != null && !"".equals(pram.get("systemCode")))
        {
            sql.append(" and progCode like '%").append(pram.get("systemCode")).append("%'");
        }
        if(pram.get("reportAction")!= null && !"".equals(pram.get("reportAction")))
        {
            sql.append(" and  action = '").append(pram.get("reportAction")).append("' ");
        }
        return this.getScrollData(12, pram.getInt("page"), "PEAAS", sql.toString(),"reportBuild");
    }

    @Override
    public void addOper(TCommonRecord parm)
    {
        try
        {
            setQueryCode("PEAAS");
            StringBuffer sql = new StringBuffer("insert into reportBuild(reportid,reportCode,reportName,classPath,createDate,createpersion,orderNo,action,progCode) values (?,?,?,?,?,?,?,?,?)");
            query.update(sql.toString(), new Object[]{parm.get("reportid"),parm.get("reportCode"),parm.get("reportName")
                ,parm.get("classPath"),parm.get("createDate"),parm.get("createpersion"),parm.get("orderNo"),parm.get("action"),parm.get("progCode")}) ;
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void updateOper(TCommonRecord parm)
    {
        try
        {
            setQueryCode("PEAAS");
            StringBuffer sql = new StringBuffer("update reportBuild set reportCode = ?,reportName = ?,classPath = ?,createDate = ?,createpersion = ?,orderNo = ?,action = ?,progCode = ? where reportid = ?");
            query.update(sql.toString(),new Object[]{parm.get("reportCode"),parm.get("reportName"),parm.get("classPath"),parm.get("createDate")
                ,parm.get("createpersion"),parm.get("orderNo"),parm.get("action"),parm.get("progCode"),parm.get("reportid")});
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOper(String id)
    {
        try
        {
            setQueryCode("PEAAS");
            StringBuffer sql = new StringBuffer("delete reportBuild where reportid = ? ");
            query.update(sql.toString(),new Object[]{id});    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public TCommonRecord findData(String id)
    {
        setQueryCode("PEAAS");
        StringBuffer sql = new StringBuffer("select * from reportBuild where reportid = ? ");
        return (TCommonRecord)query.queryForObject(sql.toString(), new Object[]{id},new CommonMapper());
    }
}
