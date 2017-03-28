package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;

/**
 * @description 特殊人群审查结果
 */
public class TDrugSpecPeopleRslt extends TBaseResult implements Serializable{
	
    private static final long serialVersionUID = 1L;
    private TDrug drug ;
    /* 儿童 */
    private String childLevel   = "0";
    private TDrugUseDetail drugchild ;
    /* 老人 */
    private String oldLevel    = "0";
    private TDrugUseDetail drugold ;
    /* 孕妇 */
    private String pregnantLevel    = "0";
    private TDrugUseDetail drugpregnant ;
    /* 哺乳期 */
    private String lactLevel    = "0";
    private TDrugUseDetail druglact ;
    /* 肝功不全 */
    private String hepaticalLevel    = "0";
    private TDrugUseDetail drughepatical ;
    /* 肾功不全  */
    private String renalLevel    = "0";
    private TDrugUseDetail drugrenal ;
    
    private boolean isFlag = false;
    
	@XmlElement(name="getIsFlag")
    public boolean getIsFlag()
    {
    	return this.isFlag;
    }
	
	public TDrugSpecPeopleRslt() 
    {
        setVersion(2);
    }
    
	@XmlElement(name="getDrug")
    public TDrug getDrug()
    {
        return drug;
    }

    public void setDrug(TDrug drug)
    {
        this.drug = drug;
    }

    @XmlElement(name="getChildLevel")
    public String getChildLevel()
    {
        return childLevel;
    }

    @XmlElement(name="getDrugchild")
    public TDrugUseDetail getDrugchild()
    {
        return drugchild;
    }

    @XmlElement(name="getOldLevel")
    public String getOldLevel()
    {
        return oldLevel;
    }

    @XmlElement(name="getDrugold")
    public TDrugUseDetail getDrugold()
    {
        return drugold;
    }

    @XmlElement(name="getPregnantLevel")
    public String getPregnantLevel()
    {
        return pregnantLevel;
    }

    @XmlElement(name="getDrugpregnant")
    public TDrugUseDetail getDrugpregnant()
    {
        return drugpregnant;
    }

    @XmlElement(name="getLactLevel")
    public String getLactLevel()
    {
        return lactLevel;
    }

    @XmlElement(name="getDruglact")
    public TDrugUseDetail getDruglact()
    {
        return druglact;
    }

    @XmlElement(name="getHepaticalLevel")
    public String getHepaticalLevel()
    {
        return hepaticalLevel;
    }

    @XmlElement(name="getDrughepatical")
    public TDrugUseDetail getDrughepatical()
    {
        return drughepatical;
    }

    @XmlElement(name="getRenalLevel")
    public String getRenalLevel()
    {
        return renalLevel;
    }

    @XmlElement(name="getDrugrenal")
    public TDrugUseDetail getDrugrenal()
    {
        return drugrenal;
    }

    /**
     * 设置总体警告灯
     * @param level
     */
    private void setAlterLevel(String level)
    {
        if("R".equals(this.alertLevel))
            return;
        this.alertLevel = level;
    }
    
    /**
     * 添加肾功不全
     * @param dud
     */
    public void addRenal(TDrugUseDetail dud)
    {
        if(!"5".equals(dud.getRENAL_INDI()))
        {
            if(dud.getHEPATICAL_INDI().equals("1")||dud.getHEPATICAL_INDI().equals("2"))
            {
                this.renalLevel = "R";
            }
            else
            {
                this.renalLevel = "Y";
            }
            this.drugrenal = (dud); 
        }
        setAlterLevel(this.renalLevel);
        isFlag = true;
    }
    
    /**
     * 添加哺肝功不全
     * @param dud
     */
    public void addHepatical(TDrugUseDetail dud)
    {
        if(!"5".equals(dud.getHEPATICAL_INDI()))
        {
            if(dud.getHEPATICAL_INDI().equals("1")||dud.getHEPATICAL_INDI().equals("2"))
            {
                this.hepaticalLevel = "R";
            }
            else
            {
                this.hepaticalLevel = "Y";
            }
            this.drughepatical = (dud);   
        }
        setAlterLevel(this.hepaticalLevel);
        isFlag = true;
    }
    
    /**
     * 添加哺乳期
     * @param dud
     */
    public void addLact(TDrugUseDetail dud)
    {
        if(!dud.getPREGNANT_INDI().equals("5"))
        {
            if(dud.getHEPATICAL_INDI().equals("1")||dud.getHEPATICAL_INDI().equals("2"))
            {
                this.lactLevel = "R";
            }
            else
            {
                this.lactLevel = "Y";
            }
            this.druglact =(dud);    
        }
        setAlterLevel(this.lactLevel);
        isFlag = true;
    }
    
    /**
     * 添加孕妇
     * @param dud
     */
    public void addPregnant(TDrugUseDetail dud)
    {
        if(!dud.getPREGNANT_INDI().equals("5"))
        {
            if(dud.getPREGNANT_INDI().equals("1")||dud.getPREGNANT_INDI().equals("2"))
            {
                this.pregnantLevel = "R";
            }
            else
            {
                this.pregnantLevel = "Y";
            }
            this.drugpregnant = (dud);    
        }
        setAlterLevel(this.pregnantLevel);
        isFlag = true;
    }
    
    /**
     * 添加老人
     * @param dud
     */
    public void addOld(TDrugUseDetail dud)
    {
        if(!dud.getOLD_INDI().equals("5"))
        {
            if(dud.getOLD_INDI().equals("1")||dud.getOLD_INDI().equals("2"))
            {
                this.oldLevel = "R";
            }
            else
            {
                this.oldLevel = "Y";
            }
            this.drugold = (dud);    
        }
        setAlterLevel(this.oldLevel);
        isFlag = true;
    }
    
    /**
     * 添加儿童
     * @param dud
     */
    public void addChild(TDrugUseDetail dud)
    {
        if(!dud.getKID_INDI().equals("5"))
        {
            if(dud.getKID_INDI().equals("1")||dud.getKID_INDI().equals("2"))
            {
                this.childLevel = "R";
            }
            else
            {
                this.childLevel = "Y";
            }
            this.drugchild = (dud);    
        }
        setAlterLevel(this.childLevel);
        isFlag = true;
    }
}
