package com.ts.entity.pdss.peaas.Beans;

/**
 * 门诊病人信息主记录
 * @author Administrator
 * 
 */
public class TPrescPatMasterBean
{
    /* 病人标识号 */
    private String PATIENT_ID;
    /* 住院号 */
    private String INP_NO;
    /* 姓名 */
    private String NAME;
    /* 姓名拼音 */
    private String NAME_PHONETIC;
    /* 性别 */
    private String SEX;
    /* 出生日期 */
    private String DATE_OF_BIRTH;
    /* 出生地 */
    private String BIRTH_PLACE;
    /* 国籍 */
    private String CITIZENSHIP;
    /* 民族 */
    private String NATION;
    /* 身份证号 */
    private String ID_NO;
    /* 身份 */
    private String IDENTITY;
    /* 费别 */
    private String CHARGE_TYPE;
    /* 合同单位 */
    private String UNIT_IN_CONTRACT;
    /* 通信地址 */
    private String MAILING_ADDRESS;
    /* 邮政编码 */
    private String ZIP_CODE;
    /* 家庭电话号码 */
    private String PHONE_NUMBER_HOME;
    /* 单位电话号码 */
    private String PHONE_NUMBER_BUSINESS;
    /* 联系人姓名 */
    private String NEXT_OF_KIN;
    /* 与联系人关系 */
    private String RELATIONSHIP;
    /* 联系人地址 */
    private String NEXT_OF_KIN_ADDR;
    /* 联系人邮政编码 */
    private String NEXT_OF_KIN_ZIP_CODE;
    /* 联系人电话号码 */
    private String NEXT_OF_KIN_PHONE;
    /* 上次就诊日期 */
    private String LAST_VISIT_DATE;
    /* 重要人物标志 */
    private String VIP_INDICATOR;
    /* 建卡日期 */
    private String CREATE_DATE;
    /* 操作员 */
    private String OPERATOR;
    /* 医疗体系病人标志 */
    private String SERVICE_AGENCY;
    /* 单位邮编 */
    private String business_zip_code;

    /* 处方错误代码  */
    private String[] PrescProblemCode;
    
    /* 门诊就诊信息 */
    private TPrescMasterBean[] pmbs;
    
    /* 门诊就诊诊断 */
    private TPrescOutpMrBean[] pombs;
    
    public String getPATIENT_ID()
    {
        return PATIENT_ID;
    }

    public void setPATIENT_ID(String pATIENT_ID)
    {
        PATIENT_ID = pATIENT_ID;
    }

    public String getINP_NO()
    {
        return INP_NO;
    }

    public void setINP_NO(String iNP_NO)
    {
        INP_NO = iNP_NO;
    }

    public String getNAME()
    {
        return NAME;
    }

    public void setNAME(String nAME)
    {
        NAME = nAME;
    }

    public String getNAME_PHONETIC()
    {
        return NAME_PHONETIC;
    }

    public void setNAME_PHONETIC(String nAME_PHONETIC)
    {
        NAME_PHONETIC = nAME_PHONETIC;
    }

    public String getSEX()
    {
        return SEX;
    }

    public void setSEX(String sEX)
    {
        SEX = sEX;
    }

    public String getDATE_OF_BIRTH()
    {
        return DATE_OF_BIRTH;
    }

    public void setDATE_OF_BIRTH(String dATE_OF_BIRTH)
    {
        DATE_OF_BIRTH = dATE_OF_BIRTH;
    }

    public String getBIRTH_PLACE()
    {
        return BIRTH_PLACE;
    }

    public void setBIRTH_PLACE(String bIRTH_PLACE)
    {
        BIRTH_PLACE = bIRTH_PLACE;
    }

    public String getCITIZENSHIP()
    {
        return CITIZENSHIP;
    }

    public void setCITIZENSHIP(String cITIZENSHIP)
    {
        CITIZENSHIP = cITIZENSHIP;
    }

    public String getNATION()
    {
        return NATION;
    }

    public void setNATION(String nATION)
    {
        NATION = nATION;
    }

    public String getID_NO()
    {
        return ID_NO;
    }

    public void setID_NO(String iD_NO)
    {
        ID_NO = iD_NO;
    }

    public String getIDENTITY()
    {
        return IDENTITY;
    }

    public void setIDENTITY(String iDENTITY)
    {
        IDENTITY = iDENTITY;
    }

    public String getCHARGE_TYPE()
    {
        return CHARGE_TYPE;
    }

    public void setCHARGE_TYPE(String cHARGE_TYPE)
    {
        CHARGE_TYPE = cHARGE_TYPE;
    }

    public String getUNIT_IN_CONTRACT()
    {
        return UNIT_IN_CONTRACT;
    }

    public void setUNIT_IN_CONTRACT(String uNIT_IN_CONTRACT)
    {
        UNIT_IN_CONTRACT = uNIT_IN_CONTRACT;
    }

    public String getMAILING_ADDRESS()
    {
        return MAILING_ADDRESS;
    }

    public void setMAILING_ADDRESS(String mAILING_ADDRESS)
    {
        MAILING_ADDRESS = mAILING_ADDRESS;
    }

    public String getZIP_CODE()
    {
        return ZIP_CODE;
    }

    public void setZIP_CODE(String zIP_CODE)
    {
        ZIP_CODE = zIP_CODE;
    }

    public String getPHONE_NUMBER_HOME()
    {
        return PHONE_NUMBER_HOME;
    }

    public void setPHONE_NUMBER_HOME(String pHONE_NUMBER_HOME)
    {
        PHONE_NUMBER_HOME = pHONE_NUMBER_HOME;
    }

    public String getPHONE_NUMBER_BUSINESS()
    {
        return PHONE_NUMBER_BUSINESS;
    }

    public void setPHONE_NUMBER_BUSINESS(String pHONE_NUMBER_BUSINESS)
    {
        PHONE_NUMBER_BUSINESS = pHONE_NUMBER_BUSINESS;
    }

    public String getNEXT_OF_KIN()
    {
        return NEXT_OF_KIN;
    }

    public void setNEXT_OF_KIN(String nEXT_OF_KIN)
    {
        NEXT_OF_KIN = nEXT_OF_KIN;
    }

    public String getRELATIONSHIP()
    {
        return RELATIONSHIP;
    }

    public void setRELATIONSHIP(String rELATIONSHIP)
    {
        RELATIONSHIP = rELATIONSHIP;
    }

    public String getNEXT_OF_KIN_ADDR()
    {
        return NEXT_OF_KIN_ADDR;
    }

    public void setNEXT_OF_KIN_ADDR(String nEXT_OF_KIN_ADDR)
    {
        NEXT_OF_KIN_ADDR = nEXT_OF_KIN_ADDR;
    }

    public String getNEXT_OF_KIN_ZIP_CODE()
    {
        return NEXT_OF_KIN_ZIP_CODE;
    }

    public void setNEXT_OF_KIN_ZIP_CODE(String nEXT_OF_KIN_ZIP_CODE)
    {
        NEXT_OF_KIN_ZIP_CODE = nEXT_OF_KIN_ZIP_CODE;
    }

    public String getNEXT_OF_KIN_PHONE()
    {
        return NEXT_OF_KIN_PHONE;
    }

    public void setNEXT_OF_KIN_PHONE(String nEXT_OF_KIN_PHONE)
    {
        NEXT_OF_KIN_PHONE = nEXT_OF_KIN_PHONE;
    }

    public String getLAST_VISIT_DATE()
    {
        return LAST_VISIT_DATE;
    }

    public void setLAST_VISIT_DATE(String lAST_VISIT_DATE)
    {
        LAST_VISIT_DATE = lAST_VISIT_DATE;
    }

    public String getVIP_INDICATOR()
    {
        return VIP_INDICATOR;
    }

    public void setVIP_INDICATOR(String vIP_INDICATOR)
    {
        VIP_INDICATOR = vIP_INDICATOR;
    }

    public String getCREATE_DATE()
    {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String cREATE_DATE)
    {
        CREATE_DATE = cREATE_DATE;
    }

    public String getOPERATOR()
    {
        return OPERATOR;
    }

    public void setOPERATOR(String oPERATOR)
    {
        OPERATOR = oPERATOR;
    }

    public String getSERVICE_AGENCY()
    {
        return SERVICE_AGENCY;
    }

    public void setSERVICE_AGENCY(String sERVICE_AGENCY)
    {
        SERVICE_AGENCY = sERVICE_AGENCY;
    }

    public String getBusiness_zip_code()
    {
        return business_zip_code;
    }

    public void setBusiness_zip_code(String business_zip_code)
    {
        this.business_zip_code = business_zip_code;
    }

    public TPrescMasterBean[] getPmbs()
    {
        return pmbs;
    }

    public void setPmbs(TPrescMasterBean[] pmbs)
    {
        this.pmbs = pmbs;
    }

    public TPrescOutpMrBean[] getPombs()
    {
        return pombs;
    }

    public void setPombs(TPrescOutpMrBean[] pombs)
    {
        this.pombs = pombs;
    }

    public String[] getPrescProblemCode()
    {
        return PrescProblemCode;
    }

    public void setPrescProblemCode(String[] prescProblemCode)
    {
        PrescProblemCode = prescProblemCode;
    }
}

