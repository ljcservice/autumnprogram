package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;


/**
 * 给药途径字典
 * 
 * @author liujc
 */
public class TAdministration extends TBaseBean
{
    /* 本地给药途径编码 */
    private String ADMINISTRATION_NO_LOCAL;
    /* 本地给药途径名称 */
    private String ADMINISTRATION_NAME_LOCAL;
    /* ID */
    private String ADMINISTRATION_ID;
    /* 给药途径名称 */
    private String ADMINISTRATION_NAME;
    /* 输入码 */
    private String INPUT_CODE;
    /* 用药分类 */
    private String ADMINISTRATION_CLASS;
    /* 用药方法分类 */
    private String ROUTE_MONTH;
    /* 用药途径分类 */
    private String ROUTE_CLASS;
    /* 操作人 */
    private String OPER_USER;
    /* 操作时间 */
    private String OPER_TIME;

    public String getADMINISTRATION_ID()
    {
        return ADMINISTRATION_ID;
    }

    public void setADMINISTRATION_ID(String aDMINISTRATION_ID)
    {
        ADMINISTRATION_ID = aDMINISTRATION_ID;
    }

    public String getADMINISTRATION_NAME()
    {
        return ADMINISTRATION_NAME;
    }

    public void setADMINISTRATION_NAME(String aDMINISTRATION_NAME)
    {
        ADMINISTRATION_NAME = aDMINISTRATION_NAME;
    }

    public String getINPUT_CODE()
    {
        return INPUT_CODE;
    }

    public void setINPUT_CODE(String iNPUT_CODE)
    {
        INPUT_CODE = iNPUT_CODE;
    }

    public String getADMINISTRATION_CLASS()
    {
        return ADMINISTRATION_CLASS;
    }

    public void setADMINISTRATION_CLASS(String aDMINISTRATION_CLASS)
    {
        ADMINISTRATION_CLASS = aDMINISTRATION_CLASS;
    }

    public String getROUTE_MONTH()
    {
        return ROUTE_MONTH;
    }

    public void setROUTE_MONTH(String rOUTE_MONTH)
    {
        ROUTE_MONTH = rOUTE_MONTH;
    }

    public String getROUTE_CLASS()
    {
        return ROUTE_CLASS;
    }

    public void setROUTE_CLASS(String rOUTE_CLASS)
    {
        ROUTE_CLASS = rOUTE_CLASS;
    }

    public String getADMINISTRATION_NO_LOCAL()
    {
        return ADMINISTRATION_NO_LOCAL;
    }

    public void setADMINISTRATION_NO_LOCAL(String aDMINISTRATION_NO_LOCAL)
    {
        ADMINISTRATION_NO_LOCAL = aDMINISTRATION_NO_LOCAL;
    }

    public String getADMINISTRATION_NAME_LOCAL()
    {
        return ADMINISTRATION_NAME_LOCAL;
    }

    public void setADMINISTRATION_NAME_LOCAL(String aDMINISTRATION_NAME_LOCAL)
    {
        ADMINISTRATION_NAME_LOCAL = aDMINISTRATION_NAME_LOCAL;
    }

    public String getOPER_USER()
    {
        return OPER_USER;
    }

    public void setOPER_USER(String oPER_USER)
    {
        OPER_USER = oPER_USER;
    }

    public String getOPER_TIME()
    {
        return OPER_TIME;
    }

    public void setOPER_TIME(String oPER_TIME)
    {
        OPER_TIME = oPER_TIME;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((ADMINISTRATION_NO_LOCAL == null) ? 0
                        : ADMINISTRATION_NO_LOCAL.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TAdministration other = (TAdministration) obj;
        if (ADMINISTRATION_NO_LOCAL == null)
        {
            if (other.ADMINISTRATION_NO_LOCAL != null)
                return false;
        }
        else if (!ADMINISTRATION_NO_LOCAL.equals(other.ADMINISTRATION_NO_LOCAL))
            return false;
        return true;
    }
}
