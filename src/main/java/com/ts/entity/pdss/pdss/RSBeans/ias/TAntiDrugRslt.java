package com.ts.entity.pdss.pdss.RSBeans.ias;

/**
 * 抗菌药物统一结果 
 * @author autumn
 *
 */
public class TAntiDrugRslt
{
    //题目
    private String   tilte;
    //审核类型
    private String   CheckType;
    // 审核内容
    private String   Memo;
    // 审核结果标示
    private boolean  Result;
    
    private String[] bak;
    
    public TAntiDrugRslt() {}
    
    public TAntiDrugRslt(String ACheckType , String AMemo , boolean AResult) 
    {
        this.CheckType = ACheckType;
        this.Memo      = AMemo;
        this.Result    = AResult;
    }
    
    public TAntiDrugRslt(String ACheckType , String AMemo , boolean AResult,String tilte) 
    {
        this.tilte     = tilte;
        this.CheckType = ACheckType;
        this.Memo      = AMemo;
        this.Result    = AResult;
    }
    
   public TAntiDrugRslt(String ACheckType , String AMemo , boolean AResult,String tilte,String[] ABak) 
    {
        this.tilte     = tilte;
        this.CheckType = ACheckType;
        this.Memo      = AMemo;
        this.Result    = AResult;
        this.bak       = ABak;
    }
   
    public String getCheckType()
    {
        return CheckType;
    }
    public void setCheckType(String checkType)
    {
        CheckType = checkType;
    }
    public String getMemo() 
    {
        return Memo;
    }
    public void setMemo(String memo) 
    {
        Memo = memo;
    }
    public boolean isResult() {
        return Result;
    }
    public void setResult(boolean result)
    {
        Result = result;
    }

    public String getTilte()
    {
        return tilte;
    }

    public void setTilte(String tilte)
    {
        this.tilte = tilte;
    }

    public String[] getBak()
    {
        return bak;
    }

    public void setBak(String[] bak)
    {
        this.bak = bak;
    }

    public String getAntidrugoverquotacheck()
    {
        return AntiDrugOverQuotaCheck;
    }

    public String getAntidrugexcessauthorizationcheck()
    {
        return AntiDrugExcessAuthorizationCheck;
    }

    public String getAntidrugovertimecheck()
    {
        return AntiDrugOverTimeCheck;
    }

    public static String getAntidrugoverindicationcheck()
    {
        return AntiDrugOverIndicationCheck;
    }

    public String getAntidrugoverdosagecheck()
    {
        return AntiDrugOverDosageCheck;
    }

    public String getAntidrugovermomentcheck()
    {
        return AntiDrugOverMomentCheck;
    }

    public String getAntidrugoverratecheck() 
    {
        return AntiDrugOverRateCheck;
    }

    public String getAntidrugallergycheck()
    {
        return AntiDrugAllergyCheck;
    }

    public String getAntidrugliverandkidneycheck() 
    {
        return AntiDrugLiverAndKidneyCheck;
    }

    public String getAntidrugchildcheck()
    {
        return AntiDrugChildCheck;
    }
    
    public String getAntidrugoperationcheck()
    {
        return AntiDrugOperationCheck;
    }

    public String getAntidrugspeccheck()
    {
        return AntiDrugSpecCheck;
    }

    /**
     * 超分线使用抗菌药物监测与提示 
     * */
    public final static String AntiDrugOverQuotaCheck = "AntiDrugOverQuotaCheck";
    /**
     * 超授权使用抗菌药物监测与提示
     */
    public final static String AntiDrugExcessAuthorizationCheck = "AntiDrugExcessAuthorizationCheck";
    /**
     * 超疗程抗菌药物使用监测与提示
     */
    public final static String AntiDrugOverTimeCheck = "AntiDrugOverTimeCheck";
    /**
     * 超使用指证（适应症）抗菌药物监测与提示
     */
    public final static String AntiDrugOverIndicationCheck = "AntiDrugOverIndicationCheck";
    /**
     * 超剂量使用抗菌药物监测与提示
     */
    public final static String AntiDrugOverDosageCheck = "AntiDrugOverDosageCheck";
    /**
     * 超使用时机（预防使用）抗菌药物监测与提示
     */
    public final static String AntiDrugOverMomentCheck = "AntiDrugOverMomentCheck";
    /**
     * 超耐药菌百分比用药监测与提示
     */
    public final static String AntiDrugOverRateCheck = "AntiDrugOverRateCheck";
    /**
     * 过敏抗菌药物使用监测与提示
     */
    public final static String AntiDrugAllergyCheck = "AntiDrugAllergyCheck";
    /**
     * 肝、肾功能异常抗菌药物使用监测与提示
     */
    public final static String AntiDrugLiverAndKidneyCheck = "AntiDrugLiverAndKidneyCheck";
    /**
     * 儿童抗菌药物使用监测与提示
     */
    public final static String AntiDrugChildCheck = "AntiDrugChildCheck";
    
    /**
     * 手术用抗菌药监测与提示
     */
    public final static String AntiDrugOperationCheck = "AntiDrugOperationCheck";
    
    /**
     * 特殊用抗菌药监测与提示
     */
    public final static String AntiDrugSpecCheck = "AntiDrugSpecCheck";
}
