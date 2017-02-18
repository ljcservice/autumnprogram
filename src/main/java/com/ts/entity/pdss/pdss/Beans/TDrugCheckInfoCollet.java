package com.ts.entity.pdss.pdss.Beans;

import java.io.Serializable;
import java.util.Date;

public class TDrugCheckInfoCollet implements Serializable
{

    private static final long serialVersionUID          = 1L;
    private String            groupnum                  = "";
    private String            deptCode                  = "";
    private String            deptName                  = "";
    private String            doctorCode                = "";
    private String            doctorName                = "";
    private String            doctorTitleCode           = "";
    private String            doctorTitleName           = "";
    /* 医嘱数 */
    private Integer           adviceNum                 = new Integer(0);
    /* 红色问题医嘱数 */
    private Integer           redQuesAdviceSum          = new Integer(0);
    /* 黄色问题医嘱数 */
    private Integer           yellowQuesAdviceSum       = new Integer(0);
    /* 红色问题医嘱变动数 */
    private Integer           redQuesAdviceChangeSum    = new Integer(0);
    /* 黄色问题医嘱变动数 */
    private Integer           YellowQuesAdviceChangeSum = new Integer(0);
    /* 红色警示问题数 */
    private Integer           redAdviceQuesSum          = new Integer(0);
    /* 黄色警示问题数 */
    private Integer           yellowAdviceQuesSum       = new Integer(0);

    private Integer           allergenRed               = new Integer(0);
    private Integer           allergenYellow            = new Integer(0);
    private Integer           dosageRed                 = new Integer(0);
    private Integer           dosageYellow              = new Integer(0);
    private Integer           iveffectRed               = new Integer(0);
    private Integer           iveffectYellow            = new Integer(0);
    private Integer           ingredientRed             = new Integer(0);
    private Integer           ingredientYellow          = new Integer(0);
    private Integer           interactionRed            = new Integer(0);
    private Integer           interactionYellow         = new Integer(0);
    private Integer           diaginfoRed               = new Integer(0);
    private Integer           diaginfoYellow            = new Integer(0);
    private Integer           administrationRed         = new Integer(0);
    private Integer           administrationYellow      = new Integer(0);

    private Integer           oldRed                    = new Integer(0);
    private Integer           oldYellow                 = new Integer(0);
    private Integer           kidRed                    = new Integer(0);
    private Integer           kidYellow                 = new Integer(0);
    private Integer           pregnantRed               = new Integer(0);
    private Integer           pregnantYellow            = new Integer(0);
    private Integer           hepaticalRed              = new Integer(0);
    private Integer           hepaticalYellow           = new Integer(0);
    private Integer           renalRed                  = new Integer(0);
    private Integer           renalYellow               = new Integer(0);
    private Integer           lactationRed              = new Integer(0);
    private Integer           lactationYellow           = new Integer(0);

    private Integer           sideRed                   = new Integer(0);
    private Integer           sideYellow                = new Integer(0);

    private Date              checkDate;

    public String getGroupnum()
    {
        return groupnum;
    }

    public void setGroupnum(String groupnum)
    {
        this.groupnum = groupnum;
    }

    public String getDeptCode()
    {
        return deptCode;
    }

    public void setDeptCode(String deptCode)
    {
        this.deptCode = deptCode;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDoctorCode()
    {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode)
    {
        this.doctorCode = doctorCode;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getDoctorTitleCode()
    {
        return doctorTitleCode;
    }

    public void setDoctorTitleCode(String doctorTitleCode)
    {
        this.doctorTitleCode = doctorTitleCode;
    }

    public String getDoctorTitleName()
    {
        return doctorTitleName;
    }

    public void setDoctorTitleName(String doctorTitleName)
    {
        this.doctorTitleName = doctorTitleName;
    }

    public Integer getAdviceNum()
    {
        return adviceNum;
    }

    public void setAdviceNum(Integer adviceNum)
    {
        this.adviceNum = adviceNum;
    }

    public Integer getRedQuesAdviceSum()
    {
        return redQuesAdviceSum;
    }

    public void setRedQuesAdviceSum(Integer redQuesAdviceSum)
    {
        this.redQuesAdviceSum = redQuesAdviceSum;
    }

    public Integer getYellowQuesAdviceSum()
    {
        return yellowQuesAdviceSum;
    }

    public void setYellowQuesAdviceSum(Integer yellowQuesAdviceSum)
    {
        this.yellowQuesAdviceSum = yellowQuesAdviceSum;
    }

    public Integer getRedQuesAdviceChangeSum()
    {
        return redQuesAdviceChangeSum;
    }

    public void setRedQuesAdviceChangeSum(Integer redQuesAdviceChangeSum)
    {
        this.redQuesAdviceChangeSum = redQuesAdviceChangeSum;
    }

    public Integer getYellowQuesAdviceChangeSum()
    {
        return YellowQuesAdviceChangeSum;
    }

    public void setYellowQuesAdviceChangeSum(Integer yellowQuesAdviceChangeSum)
    {
        YellowQuesAdviceChangeSum = yellowQuesAdviceChangeSum;
    }

    public Integer getRedAdviceQuesSum()
    {
        return redAdviceQuesSum;
    }

    public void setRedAdviceQuesSum(Integer redAdviceQuesSum)
    {
        this.redAdviceQuesSum = redAdviceQuesSum;
    }

    public Integer getYellowAdviceQuesSum()
    {
        return yellowAdviceQuesSum;
    }

    public void setYellowAdviceQuesSum(Integer yellowAdviceQuesSum)
    {
        this.yellowAdviceQuesSum = yellowAdviceQuesSum;
    }

    public Integer getAllergenRed()
    {
        return allergenRed;
    }

    public void setAllergenRed(Integer allergenRed)
    {
        this.allergenRed = allergenRed;
    }

    public Integer getAllergenYellow()
    {
        return allergenYellow;
    }

    public void setAllergenYellow(Integer allergenYellow)
    {
        this.allergenYellow = allergenYellow;
    }

    public Integer getDosageRed()
    {
        return dosageRed;
    }

    public void setDosageRed(Integer dosageRed)
    {
        this.dosageRed = dosageRed;
    }

    public Integer getDosageYellow()
    {
        return dosageYellow;
    }

    public void setDosageYellow(Integer dosageYellow)
    {
        this.dosageYellow = dosageYellow;
    }

    public Integer getIveffectRed()
    {
        return iveffectRed;
    }

    public void setIveffectRed(Integer iveffectRed)
    {
        this.iveffectRed = iveffectRed;
    }

    public Integer getIveffectYellow()
    {
        return iveffectYellow;
    }

    public void setIveffectYellow(Integer iveffectYellow)
    {
        this.iveffectYellow = iveffectYellow;
    }

    public Integer getIngredientRed()
    {
        return ingredientRed;
    }

    public void setIngredientRed(Integer ingredientRed)
    {
        this.ingredientRed = ingredientRed;
    }

    public Integer getIngredientYellow()
    {
        return ingredientYellow;
    }

    public void setIngredientYellow(Integer ingredientYellow)
    {
        this.ingredientYellow = ingredientYellow;
    }

    public Integer getInteractionRed()
    {
        return interactionRed;
    }

    public void setInteractionRed(Integer interactionRed)
    {
        this.interactionRed = interactionRed;
    }

    public Integer getInteractionYellow()
    {
        return interactionYellow;
    }

    public void setInteractionYellow(Integer interactionYellow)
    {
        this.interactionYellow = interactionYellow;
    }

    public Integer getDiaginfoRed()
    {
        return diaginfoRed;
    }

    public void setDiaginfoRed(Integer diaginfoRed)
    {
        this.diaginfoRed = diaginfoRed;
    }

    public Integer getDiaginfoYellow()
    {
        return diaginfoYellow;
    }

    public void setDiaginfoYellow(Integer diaginfoYellow)
    {
        this.diaginfoYellow = diaginfoYellow;
    }

    public Integer getAdministrationRed()
    {
        return administrationRed;
    }

    public void setAdministrationRed(Integer administrationRed)
    {
        this.administrationRed = administrationRed;
    }

    public Integer getAdministrationYellow()
    {
        return administrationYellow;
    }

    public void setAdministrationYellow(Integer administrationYellow)
    {
        this.administrationYellow = administrationYellow;
    }

    public Integer getOldRed()
    {
        return oldRed;
    }

    public void setOldRed(Integer oldRed)
    {
        this.oldRed = oldRed;
    }

    public Integer getOldYellow()
    {
        return oldYellow;
    }

    public void setOldYellow(Integer oldYellow)
    {
        this.oldYellow = oldYellow;
    }

    public Integer getKidRed()
    {
        return kidRed;
    }

    public void setKidRed(Integer kidRed)
    {
        this.kidRed = kidRed;
    }

    public Integer getKidYellow()
    {
        return kidYellow;
    }

    public void setKidYellow(Integer kidYellow)
    {
        this.kidYellow = kidYellow;
    }

    public Integer getPregnantRed()
    {
        return pregnantRed;
    }

    public void setPregnantRed(Integer pregnantRed)
    {
        this.pregnantRed = pregnantRed;
    }

    public Integer getPregnantYellow()
    {
        return pregnantYellow;
    }

    public void setPregnantYellow(Integer pregnantYellow)
    {
        this.pregnantYellow = pregnantYellow;
    }

    public Integer getHepaticalRed()
    {
        return hepaticalRed;
    }

    public void setHepaticalRed(Integer hepaticalRed)
    {
        this.hepaticalRed = hepaticalRed;
    }

    public Integer getHepaticalYellow()
    {
        return hepaticalYellow;
    }

    public void setHepaticalYellow(Integer hepaticalYellow)
    {
        this.hepaticalYellow = hepaticalYellow;
    }

    public Integer getRenalRed()
    {
        return renalRed;
    }

    public void setRenalRed(Integer renalRed)
    {
        this.renalRed = renalRed;
    }

    public Integer getRenalYellow()
    {
        return renalYellow;
    }

    public void setRenalYellow(Integer renalYellow)
    {
        this.renalYellow = renalYellow;
    }

    public Integer getLactationRed()
    {
        return lactationRed;
    }

    public void setLactationRed(Integer lactationRed)
    {
        this.lactationRed = lactationRed;
    }

    public Integer getLactationYellow()
    {
        return lactationYellow;
    }

    public void setLactationYellow(Integer lactationYellow)
    {
        this.lactationYellow = lactationYellow;
    }

    public Integer getSideRed()
    {
        return sideRed;
    }

    public void setSideRed(Integer sideRed)
    {
        this.sideRed = sideRed;
    }

    public Integer getSideYellow()
    {
        return sideYellow;
    }

    public void setSideYellow(Integer sideYellow)
    {
        this.sideYellow = sideYellow;
    }

    public Date getCheckDate()
    {
        return checkDate;
    }

    public void setCheckDate(Date checkDate)
    {
        this.checkDate = checkDate;
    }

}
