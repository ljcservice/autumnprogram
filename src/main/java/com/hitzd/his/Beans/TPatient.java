package com.hitzd.his.Beans;

import java.util.Calendar;

import com.hitzd.his.Utils.*;

/**
 * @description 病人信息类：Patient 对应数据库表：病人信息(PAT_MASTER_INFO)
 * @author
 */
public class TPatient implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    // 病人名称*/
    private String            name;

    // 病人性别*/
    private String            sex;

    // 病人出生日期*/
    private String            dateOfBirth;

    // 病人出生地*/
    private String            birthPlace;

    // 民族*/
    private String            nation;

    public TPatient()
    {
    }
    
    /**
     * 返回性别 true为男性 false女性
     * @return
     */
    public boolean TheIsSex()
    {
        if(this.sex != null)
        {
            if(this.sex.indexOf("男")!=-1||"MAN".equals(this.sex.toUpperCase())||"MALE".equals(this.sex.toUpperCase()))
            {
                return true;
            }
        }
        return false;
    }
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSex()
    {
        return this.sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getBirthPlace()
    {
        return this.birthPlace;
    }

    public void setBirthPlace(String birthPlace)
    {
        this.birthPlace = birthPlace;
    }

    public String getNation()
    {
        return this.nation;
    }

    public void setNation(String nation)
    {
        this.nation = nation;
    }

    public Long calAgeDays()
    {

        Long days = new Long(0);

        if (this.dateOfBirth == null)
            return days;

        days = (Calendar.getInstance().getTime().getTime() - DateUtils
                .getDateFromString(dateOfBirth).getTime())
                / 1000
                / (24 * 60 * 60);

        return days;
    }

    public Long calAgeYears()
    {
        Long rtn = new Long(0);

        if (this.dateOfBirth == null)
            return rtn;

        int year = Calendar.getInstance().getTime().getYear()
                - DateUtils.getDateFromString(dateOfBirth).getYear();
        if (year < 0)
            year = 0;
        rtn = new Long(year);

        return rtn;
    }

    public boolean getOldMan()
    {
        boolean rtn = false;

        if (this.calAgeYears().intValue() >= 65)
            rtn = true;

        return rtn;
    }

    public void setOldMan(boolean value)
    {
    	
    }
    
    public void setChild(boolean value)
    {
    	
    }
    public boolean getChild()
    {
        boolean rtn = false;

        if (this.calAgeYears().intValue() <= 18)
            rtn = true;

        return rtn;

    }

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

}