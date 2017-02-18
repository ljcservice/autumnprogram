package com.ts.entity.pdss.pdss.Beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;

/**
 * 
 * @author Administrator
 *
 */
public class TCheckLog implements Serializable
{

    public static final String        CHECK_SAVE            = "0";  // 保存
    public static final String        CHECK_COMMIT          = "1";  // 提交
    public static final String        CHECK_AUTO            = "2";  // 自动分析

    private static final long         serialVersionUID      = 1L;
    /* 审核类型 */
    private String                    checkType             = null;
    /* 审核时间 */
    private Date                      checkDate             = null;
    /* 上次审查的批次号 */
    private String                    lastNgroupnum         = null;
    /* 审核结果 */
    private TDrugSecurityRslt          checkDrugSecurityRslt = null;
    /* 医嘱信息 */
    private TPatientOrder              patientOrder          = null;
    /* 问题医嘱 */
    private Map<String, List<String>> questionOrder         = null;

    public TDrugSecurityRslt getCheckDrugSecurityRslt()
    {
        return checkDrugSecurityRslt;
    }

    public void setCheckDrugSecurityRslt(TDrugSecurityRslt checkDrugSecurityRslt)
    {
        this.checkDrugSecurityRslt = checkDrugSecurityRslt;
    }

    public String getLastNgroupnum()
    {
        return lastNgroupnum;
    }

    public void setLastNgroupnum(String lastNgroupnum)
    {
        this.lastNgroupnum = lastNgroupnum;
    }

    public String getCheckType()
    {
        return checkType;
    }

    public void setCheckType(String checkType)
    {
        this.checkType = checkType;
    }

    public Date getCheckDate()
    {
        return checkDate;
    }

    public void setCheckDate(Date checkDate)
    {
        this.checkDate = checkDate;
    }

    public TPatientOrder getPatientOrder()
    {
        return patientOrder;
    }

    public void setPatientOrder(TPatientOrder patientOrder)
    {
        this.patientOrder = patientOrder;
    }

    public Map<String, List<String>> getQuestionOrder()
    {
        return questionOrder;
    }

    public void setQuestionOrder(Map<String, List<String>> questionOrder)
    {
        this.questionOrder = questionOrder;
    }

}
