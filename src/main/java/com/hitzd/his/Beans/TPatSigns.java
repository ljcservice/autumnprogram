package com.hitzd.his.Beans;

import java.io.Serializable;

/**
 * 体征信息
 * 
 * @author Administrator
 * 
 */
public class TPatSigns implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String TWDate;
    private String TWValue;
    private String TWOK;
    private String XXDate;
    private String XXValue;
    private String CValue;
    private String XXOK;

    public String getCValue()
    {
		return CValue;
	}

	public void setCValue(String cValue) 
	{
		CValue = cValue;
	}

	public String getTWDate()
    {
        return TWDate;
    }

    public void setTWDate(String tWDate)
    {
        TWDate = tWDate;
    }

    public String getTWValue()
    {
        return TWValue;
    }

    public void setTWValue(String tWValue)
    {
        TWValue = tWValue;
    }

    public String getTWOK()
    {
        return TWOK;
    }

    public void setTWOK(String tWOK)
    {
        TWOK = tWOK;
    }

    public String getXXDate()
    {
        return XXDate;
    }

    public void setXXDate(String xXDate)
    {
        XXDate = xXDate;
    }

    public String getXXValue()
    {
        return XXValue;
    }

    public void setXXValue(String xXValue)
    {
        XXValue = xXValue;
    }

    public String getXXOK()
    {
        return XXOK;
    }

    public void setXXOK(String xXOK)
    {
        XXOK = xXOK;
    }

    @Override
    public String toString()
    {
        return "TPatSigns [TWDate=" + TWDate + ", TWValue=" + TWValue
                + ", TWOK=" + TWOK + ", XXDate=" + XXDate + ", XXValue="
                + XXValue + ", CValue=" + CValue + ", XXOK=" + XXOK + "]";
    }
    
}
