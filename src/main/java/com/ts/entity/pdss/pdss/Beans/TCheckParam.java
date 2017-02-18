package com.ts.entity.pdss.pdss.Beans;

import java.io.Serializable;

public class TCheckParam implements Serializable
{

    private static final long serialVersionUID = 1L;

    /* 科室编码 */
    private String            VDeptCode;
    /* 科室名称 */
    private String            VDeptName;
    /* 医生编码 */
    private String            VDoctorCode;
    /* 医生姓名 */
    private String            VDoctorName;
    /* 患者ID */
    private String            VPatientID;
    /* 上次审查的批次号 */
    private String            lastNgroupnum    = null;

    public String getLastNgroupnum()
    {
        return lastNgroupnum;
    }

    public void setLastNgroupnum(String lastNgroupnum)
    {
        this.lastNgroupnum = lastNgroupnum;
    }

    public String getVDeptCode()
    {
        return VDeptCode;
    }

    public void setVDeptCode(String deptCode)
    {
        VDeptCode = deptCode;
    }

    public String getVDeptName()
    {
        return VDeptName;
    }

    public void setVDeptName(String deptName)
    {
        VDeptName = deptName;
    }

    public String getVDoctorCode()
    {
        return VDoctorCode;
    }

    public void setVDoctorCode(String doctorCode)
    {
        VDoctorCode = doctorCode;
    }

    public String getVDoctorName()
    {
        return VDoctorName;
    }

    public void setVDoctorName(String doctorName)
    {
        VDoctorName = doctorName;
    }

    public String getVPatientID()
    {
        return VPatientID;
    }

    public void setVPatientID(String patientID)
    {
        VPatientID = patientID;
    }
}
