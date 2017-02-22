package com.hitzd.his.FetcherManager;

import java.util.List;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Scheduler.ReportScheduler;

/**
 * 增加一个中间层
 * @author Administrator
 *
 */
@Deprecated
public class FetcherManagerBean extends ReportScheduler
{
    public FetcherManagerBean()
    {
        
    }

    private String sql = "select funcCode , funcName ,progCode,HisfieldName,context from HisFieldContrast where funcCode = ? ";
    
    private TCommonRecord TParam = new TCommonRecord();
    
    
    public TCommonRecord getFetcherParam()
    {
        return TParam;
    }

    public void SetFetcherParm(String FunCode)
    {
        long x = System.currentTimeMillis();
        
        try
        {
            List<TCommonRecord> listRecord = disposeBaseData(FunCode);
            for (TCommonRecord t : listRecord)
            {
                TParam.set(t.get("progCode"), t.get("HisfieldName"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(" 中间层数据耗时 :" + (System.currentTimeMillis() - x));
    }

    @SuppressWarnings ("unchecked")
    private List<TCommonRecord> disposeBaseData(String FunCode)
    {
        JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
        CommonMapper cmr = new CommonMapper();
        List<TCommonRecord> listTCom = null;
        try
        {
            listTCom = query.query(this.sql, new Object[] { FunCode }, cmr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
            cmr = null;
        }
        return listTCom;
    }

}
