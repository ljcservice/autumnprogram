package com.ts.entity.pdss.ias.Beans;

/**
 * 医生信息
 * 
 * @author Administrator
 * 
 */
public class TDoctor
{
    private String doctorDeptID;
    private String doctorDeptName;
    private String doctorID;
    private String doctorName;
    private String doctorTitleID;
    private String doctorTitleName;

    public String getDoctorDeptID()
    {
        return doctorDeptID;
    }

    public void setDoctorDeptID(String doctorDeptID)
    {
        this.doctorDeptID = doctorDeptID;
    }

    public String getDoctorDeptName()
    {
        return doctorDeptName;
    }

    public void setDoctorDeptName(String doctorDeptName)
    {
        this.doctorDeptName = doctorDeptName;
    }

    public String getDoctorID()
    {
        return doctorID;
    }

    public void setDoctorID(String doctorID)
    {
        this.doctorID = doctorID;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getDoctorTitleID()
    {
        return doctorTitleID == null || "".equals(doctorTitleID) ? "0" : this.doctorTitleID  ;
    }

    public void setDoctorTitleID(String doctorTitleID)
    {
        this.doctorTitleID = doctorTitleID;
    }

    public String getDoctorTitleName()
    {
        return doctorTitleName;
    }

    public void setDoctorTitleName(String doctorTitleName)
    {
        this.doctorTitleName = doctorTitleName;
    }
}
