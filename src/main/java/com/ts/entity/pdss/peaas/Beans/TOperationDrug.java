package com.ts.entity.pdss.peaas.Beans;

import com.hitzd.his.Beans.TBaseBean;

/**
 * 手术类型相关的抗菌药列表
 * @author Administrator
 *
 */
public class TOperationDrug extends TBaseBean
{
    private static final long serialVersionUID = 1L;
    
    /* 药品编号 */
    private String oper_drug_id;
    /* 手术编码 */
    private String oper_no;       
    /* 药品代码*/
    private String oper_drug_code;
    /* 药品名称 */
    private String oper_drug_name;
    /* 药品规格 */
    private String oper_drug_spec;
    /* 药品剂型 */
    private String oper_drug_form;
    
    public String getOper_drug_id()
    {
        return oper_drug_id;
    }
    public void setOper_drug_id(String oper_drug_id)
    {
        this.oper_drug_id = oper_drug_id;
    }
    public String getOper_no()
    {
        return oper_no;
    }
    public void setOper_no(String oper_no)
    {
        this.oper_no = oper_no;
    }
    public String getOper_drug_code()
    {
        return oper_drug_code;
    }
    public void setOper_drug_code(String oper_drug_code)
    {
        this.oper_drug_code = oper_drug_code;
    }
    public String getOper_drug_name()
    {
        return oper_drug_name;
    }
    public void setOper_drug_name(String oper_drug_name)
    {
        this.oper_drug_name = oper_drug_name;
    }
    public String getOper_drug_spec()
    {
        return oper_drug_spec;
    }
    public void setOper_drug_spec(String oper_drug_spec)
    {
        this.oper_drug_spec = oper_drug_spec;
    }
    public String getOper_drug_form()
    {
        return oper_drug_form;
    }
    public void setOper_drug_form(String oper_drug_form)
    {
        this.oper_drug_form = oper_drug_form;
    }
}
