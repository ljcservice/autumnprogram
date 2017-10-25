package com.ts.entity.pdss.DrugUseAuth;

import java.io.Serializable;
import java.util.Date;
/**
 * 药物使用授权 实体bean
 * @author autumn
 *
 */
public class TCkDrugUserAuth implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /* 主键 */
    private String ID;            
    /* 药品 */
    private String  drug_code;     
    /* 药品名称 */
    private String drug_name;     
    /* 科室 */
    private String dept_name;     
    /* 医生 */
    private String doctor_name;   
    /* 规格 */
    private String drug_spec;     
    /* 剂型 */
    private String drug_form;     
    /* 控制类型 */
    private String control_type;  
    /* 阀值  */
    private Integer t_value;       
    /* 当前累计值 */
    private Integer total_value;   
    /* 更新人 */
    private String update_persion;
    /* 更新时间  */
    private String update_date;   
    /* 诊断名称 */
    private String diag_name;     
    /* 诊断编码 */
    private String diag_code;     
    /* 医保类型 */
    private String h_type;
    public String getID()
    {
        return ID;
    }
    public void setID(String iD)
    {
        ID = iD;
    }
    public String getDrug_code()
    {
        return drug_code;
    }
    public void setDrug_code(String drug_code)
    {
        this.drug_code = drug_code;
    }
    public String getDrug_name()
    {
        return drug_name;
    }
    public void setDrug_name(String drug_name)
    {
        this.drug_name = drug_name;
    }
    public String getDept_name()
    {
        return dept_name;
    }
    public void setDept_name(String dept_name)
    {
        this.dept_name = dept_name;
    }
    public String getDoctor_name()
    {
        return doctor_name;
    }
    public void setDoctor_name(String doctor_name)
    {
        this.doctor_name = doctor_name;
    }
    public String getDrug_spec()
    {
        return drug_spec;
    }
    public void setDrug_spec(String drug_spec)
    {
        this.drug_spec = drug_spec;
    }
    public String getDrug_form()
    {
        return drug_form;
    }
    public void setDrug_form(String drug_form)
    {
        this.drug_form = drug_form;
    }
    public String getControl_type()
    {
        return control_type;
    }
    public void setControl_type(String control_type)
    {
        this.control_type = control_type;
    }
    public Integer getT_value()
    {
        return t_value;
    }
    public void setT_value(Integer t_value)
    {
        this.t_value = t_value;
    }
    public Integer getTotal_value()
    {
        return total_value;
    }
    public void setTotal_value(Integer total_value)
    {
        this.total_value = total_value;
    }
    public String getUpdate_persion()
    {
        return update_persion;
    }
    public void setUpdate_persion(String update_persion)
    {
        this.update_persion = update_persion;
    }
    public String getUpdate_date()
    {
        return update_date;
    }
    public void setUpdate_date(String update_date)
    {
        this.update_date = update_date;
    }
    public String getDiag_name()
    {
        return diag_name;
    }
    public void setDiag_name(String diag_name)
    {
        this.diag_name = diag_name;
    }
    public String getDiag_code()
    {
        return diag_code;
    }
    public void setDiag_code(String diag_code)
    {
        this.diag_code = diag_code;
    }
    public String getH_type()
    {
        return h_type;
    }
    public void setH_type(String h_type)
    {
        this.h_type = h_type;
    }       
    
}
