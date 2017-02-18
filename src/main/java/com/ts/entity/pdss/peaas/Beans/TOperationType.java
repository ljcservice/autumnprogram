package com.ts.entity.pdss.peaas.Beans;

import com.hitzd.his.Beans.TBaseBean;

/**
 * 手术类型
 * @author Administrator
 *
 */
public class TOperationType extends TBaseBean
{
    private static final long serialVersionUID = 1L;

    /* 编号 */
    private String operation_no;    
    /* 名称 */
    private String operation_name;
    /* 备注 */
    private String operation_remark;
    public String getOperation_no()
    {
        return operation_no;
    }
    public void setOperation_no(String operation_no)
    {
        this.operation_no = operation_no;
    }
    public String getOperation_name()
    {
        return operation_name;
    }
    public void setOperation_name(String operation_name)
    {
        this.operation_name = operation_name;
    }
    public String getOperation_remark()
    {
        return operation_remark;
    }
    public void setOperation_remark(String operation_remark)
    {
        this.operation_remark = operation_remark;
    }
}
