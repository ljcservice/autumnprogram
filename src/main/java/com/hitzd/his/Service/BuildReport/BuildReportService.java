package com.hitzd.his.Service.BuildReport;


import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.WebPage.PageView;

/**
 * 报表计算汇总
 * @author Administrator
 *
 */
public interface BuildReportService
{
    /**
     * 检索出所有记录
     * @param pram
     * @return
     */
    public PageView<TCommonRecord> getList(TCommonRecord parm);
    
    /**
     *  添加记录 
     * @param pram
     */
    public void addOper(TCommonRecord parm);
    
    /**
     * 更新记录
     * @param parm
     */
    public void updateOper(TCommonRecord parm);
    
    /**
     * 删除记录 
     * @param id
     */
    public void deleteOper(String id);
    
    /**
     *  id检索数据
     * @param id
     * @return
     */
    public TCommonRecord findData(String id );
    
}
