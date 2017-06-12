package com.ts.entity.pdss.pdss.Beans.ias;

import com.hitzd.his.Beans.TBaseBean;

/**
 * 手术药品维护
 * @author autumn
 *
 */
public class TOperationDrugInfo extends TBaseBean
{
    private static final long serialVersionUID = 1L;
    
    /*id*/
    private String o_id;         
    /*手术code*/
    private String o_code;       
    /*手术名称*/
    private String o_name;       
    /*手术切口等级*/
    private String o_level;      
    /*手术药品code*/
    private String o_drug_code;  
    /*手术药品名称*/
    private String o_drug_name;  
    /*手术药品类码 */
    private String o_drug_Clase;
    /*规格*/
    private String o_drug_spce;  
    /*剂型*/
    private String o_drug_form;  
    /*科室代码*/
    private String o_dept_code;  
    /*科室名称*/
    private String o_dept_name;  
    /*医生代码*/
    private String o_doctor_code;
    /*医生名称*/
    private String o_doctor_name;
    /* 是否可用 1:可用 0:不可用*/
    private String is_use;
    /*记录创建时间 */
    private String createdate;
    /*操作时间*/
    private String operatordate;
    /*操作人*/
    private String operatoruser;
    public String getO_id()
    {
        return o_id;
    }
    public void setO_id(String o_id)
    {
        this.o_id = o_id;
    }
    public String getO_code()
    {
        return o_code;
    }
    public void setO_code(String o_code)
    {
        this.o_code = o_code;
    }
    public String getO_name()
    {
        return o_name;
    }
    public void setO_name(String o_name)
    {
        this.o_name = o_name;
    }
    public String getO_level()
    {
        return o_level;
    }
    public void setO_level(String o_level)
    {
        this.o_level = o_level;
    }
    public String getO_drug_code()
    {
        return o_drug_code;
    }
    public void setO_drug_code(String o_drug_code)
    {
        this.o_drug_code = o_drug_code;
    }
    public String getO_drug_name()
    {
        return o_drug_name;
    }
    public void setO_drug_name(String o_drug_name)
    {
        this.o_drug_name = o_drug_name;
    }
    public String getO_drug_spce()
    {
        return o_drug_spce;
    }
    public void setO_drug_spce(String o_drug_spce)
    {
        this.o_drug_spce = o_drug_spce;
    }
    public String getO_drug_form()
    {
        return o_drug_form;
    }
    public void setO_drug_form(String o_drug_form)
    {
        this.o_drug_form = o_drug_form;
    }
    public String getO_dept_code()
    {
        return o_dept_code;
    }
    public void setO_dept_code(String o_dept_code)
    {
        this.o_dept_code = o_dept_code;
    }
    public String getO_dept_name()
    {
        return o_dept_name;
    }
    public void setO_dept_name(String o_dept_name)
    {
        this.o_dept_name = o_dept_name;
    }
    public String getO_doctor_code()
    {
        return o_doctor_code;
    }
    public void setO_doctor_code(String o_doctor_code)
    {
        this.o_doctor_code = o_doctor_code;
    }
    public String getO_doctor_name()
    {
        return o_doctor_name;
    }
    public void setO_doctor_name(String o_doctor_name)
    {
        this.o_doctor_name = o_doctor_name;
    }
    public String getIs_use()
    {
        return is_use;
    }
    public void setIs_use(String is_use)
    {
        this.is_use = is_use;
    }
    public String getCreatedate()
    {
        return createdate;
    }
    public void setCreatedate(String createdate)
    {
        this.createdate = createdate;
    }
    public String getOperatordate()
    {
        return operatordate;
    }
    public void setOperatordate(String operatordate)
    {
        this.operatordate = operatordate;
    }
    public String getOperatoruser()
    {
        return operatoruser;
    }
    public void setOperatoruser(String operatoruser)
    {
        this.operatoruser = operatoruser;
    }
    public String getO_drug_Clase()
    {
        return o_drug_Clase;
    }
    public void setO_drug_Clase(String o_drug_Clase)
    {
        this.o_drug_Clase = o_drug_Clase;
    }
}
