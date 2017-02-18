package com.ts.entity.pdss.CustomAudit.RSBeans;

import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.CustomAudit.Beans.TCustomAuditInfo;
import com.ts.entity.pdss.pdss.Beans.TDrug;

/**
 * 用户自定审核 药品与问题 关联结果bean 
 * @author Administrator
 *
 */
public class TCustomCheckRslt
{
    /* 药品信息 */
    private TDrug  drug;
    
    public TDrug getDrug()
    {
        return drug;
    }

    public void setDrug(TDrug drug)
    {
        this.drug = drug;
    }
    
    private int RedCounts    = 0 ;
    private int YellowCounts = 0;
    private int BlueCounts   = 0;
    
    
    private List<TCustomAuditInfo> CADI = new ArrayList<TCustomAuditInfo>();
    
    public void addCustomAuditInfoList(List<TCustomAuditInfo> list)
    {
        if(list != null  && list.size() > 0 )
        {
            for(TCustomAuditInfo cai :  list)
            {
                addCustomAuditInfo(cai);
            }
        }
    }
    
    public void addCustomAuditInfo(TCustomAuditInfo addCADI)
    {
        this.CADI.add(addCADI);
    }
    
    /* 部分问题  直接返回该药品的所有问题  */
    public TCustomAuditInfo[] getTCustomAuditInfo()
    {
        return (TCustomAuditInfo[]) CADI.toArray(new TCustomAuditInfo[0]);
    }
    
//    
//    /**
//     * map 中的key 为问题编号 
//     */
//    private Map<String, TCustomAuditRslt> CARslt = new HashMap<String, TCustomAuditRslt>();
//    
//    /**
//     * 返回问题主键
//     * @return
//     */
//    public String[] getPromKey()
//    {
//        return (String[])CARslt.keySet().toArray(new String[0]);
//    }
//    
//    public TCustomAuditRslt getCustomARslt(String key)
//    {
//        return CARslt.get(key);
//    }
//   
//    /**
//     * 返回小结果数据集 
//     * @return
//     */
//    public TCustomAuditRslt[] getCustomARslts()
//    {
//        String[] keys = getPromKey();
//        TCustomAuditRslt[] CAR = new TCustomAuditRslt[keys.length];
//        for(int i = 0 ;i < keys.length ; i++)
//        {
//            CAR[i] = this.getCustomARslt(keys[i]);
//        }
//        return CAR;
//    }
    
//  public void CopyCustTCustomAuditRslt(TDrug drug ,TCustomCheckRslt CCR )
//  {
//      
//  }
  
    /**
     * 返回该药品的最高问题 级别 
     * @return
     */
    public String getAlertLevel()
    {
        if (RedCounts > 0)
            return "R/" + RedCounts;
        else if (YellowCounts > 0)
            return "Y/" + YellowCounts;
        else if(BlueCounts > 0 )
            return "B/" + BlueCounts;
        else
            return "-";
    }
    
    /**
     * 返回 药品是否有问题 
     * @return
     */
//    public String getCheckFlag()
//    {
//        int Counts = this.getChkBlueCounts() + this.getChkRedCounts() + this.getChkYellowCounts();
//        return Counts > 0 ? "1" :"0";
//    }
    
    private String enter = "#A#D";
    public String getCheckResult()
    {
        String checkResult  = "";
//        checkResult += "给药途径审查结果    -红灯：" + admRedCount + ", 黄灯：" + admYellowCount + enter;
//        checkResult += "过敏审查结果              -红灯：" + dagRedCount + ", 黄灯：" + dagYellowCount + enter;
//        checkResult += "禁忌审查结果              -红灯：" + ddiRedCount + ", 黄灯：" + ddiYellowCount + enter;
//        checkResult += "剂量审查结果              -红灯：" + ddgRedCount + ", 黄灯：" + ddgYellowCount + enter;
//        checkResult += "重复成份审查结果    -红灯：" + didRedCount + ", 黄灯：" + didYellowCount + enter;
//        checkResult += "相互作用审查结果    -红灯：" + diaRedCount + ", 黄灯：" + diaYellowCount + enter;
//        checkResult += "配伍审查结果              -红灯：" + dieRedCount + ", 黄灯：" + dieYellowCount + enter;
//        checkResult += "特殊人群审查结果    -红灯：" + dspRedCount + ", 黄灯：" + dspYellowCount + enter;
//        checkResult += "不良反应审查结果    -红灯：" + dhfRedCount + ", 黄灯：" + dhfYellowCount + enter;
        return checkResult;
    }

    public int getChkRedCounts()
    {
        return this.RedCounts;
    }
    
    public int getChkYellowCounts()    
    {
        return this.YellowCounts;
    }
    
    public int getChkBlueCounts()
    {
        return this.BlueCounts;
    }
    
//    /* 互动作用  */
//    private int Chk01RedCount    = 0;
//    private int Chk01YellowCount = 0;
//    private int Chk01BlueCount   = 0;
//    private List<TCustomAuditInfo> Check01Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加相互作用信息 
//     * @param CustA
//     */
//    public void addCheck01Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check01Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck01Rslt()
//    {
//        return (TCustomAuditInfo[]) Check01Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck01Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk01BlueCount = this.Chk01BlueCount;
//        rslt.Chk01RedCount = this.Chk01RedCount;
//        rslt.Chk01YellowCount = this.Chk01YellowCount;
//        rslt.Check01Rslt      = this.Check01Rslt;
//    }
//    
//    /* 配伍禁忌  */
//    private int Chk02RedCount    = 0;
//    private int Chk02YellowCount = 0;
//    private int Chk02BlueCount   = 0;
//    private List<TCustomAuditInfo> Check02Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加配伍禁忌信息 
//     * @param CustA
//     */
//    public void addCheck02Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check02Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck02Rslt()
//    {
//        return (TCustomAuditInfo[]) Check02Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck02Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk02BlueCount   = this.Chk02BlueCount;
//        rslt.Chk02RedCount    = this.Chk02RedCount;
//        rslt.Chk02YellowCount = this.Chk02YellowCount;
//        rslt.Check02Rslt      = this.Check02Rslt;
//    }
//    
//    /* 禁忌症  */
//    private int Chk03RedCount    = 0;
//    private int Chk03YellowCount = 0;
//    private int Chk03BlueCount   = 0;
//    private List<TCustomAuditInfo> Check03Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加禁忌症信息 
//     * @param CustA
//     */
//    public void addCheck03Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check03Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck03Rslt()
//    {
//        return (TCustomAuditInfo[]) Check03Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck03Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk03BlueCount = this.Chk03BlueCount;
//        rslt.Chk03RedCount = this.Chk03RedCount;
//        rslt.Chk03YellowCount = this.Chk03YellowCount;
//        rslt.Check03Rslt      = this.Check03Rslt;
//    }
//    
//    /* 儿童  */
//    private int Chk04RedCount    = 0;
//    private int Chk04YellowCount = 0;
//    private int Chk04BlueCount   = 0;
//    private List<TCustomAuditInfo> Check04Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加儿童 
//     * @param CustA
//     */
//    public void addCheck04Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check04Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck04Rslt()
//    {
//        return (TCustomAuditInfo[]) Check04Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck04Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk04BlueCount = this.Chk04BlueCount;
//        rslt.Chk04RedCount = this.Chk04RedCount;
//        rslt.Chk04YellowCount = this.Chk04YellowCount;
//        rslt.Check04Rslt      = this.Check04Rslt;
//    }
//    
//    /* 老人  */
//    private int Chk05RedCount    = 0;
//    private int Chk05YellowCount = 0;
//    private int Chk05BlueCount   = 0;
//    private List<TCustomAuditInfo> Check05Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加老人 
//     * @param CustA
//     */
//    public void addCheck05Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check05Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck05Rslt()
//    {
//        return (TCustomAuditInfo[]) Check05Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck05Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk05BlueCount = this.Chk05BlueCount;
//        rslt.Chk05RedCount = this.Chk05RedCount;
//        rslt.Chk05YellowCount = this.Chk05YellowCount;
//        rslt.Check05Rslt      = this.Check05Rslt;
//    }
//    
//    /* 孕妇  */
//    private int Chk06RedCount    = 0;
//    private int Chk06YellowCount = 0;
//    private int Chk06BlueCount   = 0;
//    private List<TCustomAuditInfo> Check06Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加孕妇 
//     * @param CustA
//     */
//    public void addCheck06Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check06Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck06Rslt()
//    {
//        return (TCustomAuditInfo[]) Check06Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck06Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk06BlueCount = this.Chk06BlueCount;
//        rslt.Chk06RedCount = this.Chk06RedCount;
//        rslt.Chk06YellowCount = this.Chk06YellowCount;
//        rslt.Check06Rslt      = this.Check06Rslt;
//    }
//    
//    /* 哺乳  */
//    private int Chk07RedCount    = 0;
//    private int Chk07YellowCount = 0;
//    private int Chk07BlueCount   = 0;
//    private List<TCustomAuditInfo> Check07Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加哺乳 
//     * @param CustA
//     */
//    public void addCheck07Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check07Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck07Rslt()
//    {
//        return (TCustomAuditInfo[]) Check07Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck07Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk07BlueCount = this.Chk07BlueCount;
//        rslt.Chk07RedCount = this.Chk07RedCount;
//        rslt.Chk07YellowCount = this.Chk07YellowCount;
//        rslt.Check07Rslt      = this.Check07Rslt;
//    }
//    
//    /* 肝功  */
//    private int Chk08RedCount    = 0;
//    private int Chk08YellowCount = 0;
//    private int Chk08BlueCount   = 0;
//    private List<TCustomAuditInfo> Check08Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加肝功 
//     * @param CustA
//     */
//    public void addCheck08Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check08Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck08Rslt()
//    {
//        return (TCustomAuditInfo[]) Check08Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck08Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk08BlueCount = this.Chk08BlueCount;
//        rslt.Chk08RedCount = this.Chk08RedCount;
//        rslt.Chk08YellowCount = this.Chk08YellowCount;
//        rslt.Check08Rslt      = this.Check08Rslt;
//    }
//    
//    /* 肾功  */
//    private int Chk09RedCount    = 0;
//    private int Chk09YellowCount = 0;
//    private int Chk09BlueCount   = 0;
//    private List<TCustomAuditInfo> Check09Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加肾功 
//     * @param CustA
//     */
//    public void addCheck09Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check09Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck09Rslt()
//    {
//        return (TCustomAuditInfo[]) Check09Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck09Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk09BlueCount = this.Chk09BlueCount;
//        rslt.Chk09RedCount = this.Chk09RedCount;
//        rslt.Chk09YellowCount = this.Chk09YellowCount;
//        rslt.Check09Rslt      = this.Check09Rslt;
//    }
//    
//    /* 用药途径  */
//    private int Chk10RedCount    = 0;
//    private int Chk10YellowCount = 0;
//    private int Chk10BlueCount   = 0;
//    private List<TCustomAuditInfo> Check10Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加用药途径 
//     * @param CustA
//     */
//    public void addCheck10Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check10Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck10Rslt()
//    {
//        return (TCustomAuditInfo[]) Check10Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck10Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk10BlueCount = this.Chk10BlueCount;
//        rslt.Chk10RedCount = this.Chk10RedCount;
//        rslt.Chk10YellowCount = this.Chk10YellowCount;
//        rslt.Check10Rslt      = this.Check10Rslt;
//    }
//    
//    /* 重复成分  */
//    private int Chk11RedCount    = 0;
//    private int Chk11YellowCount = 0;
//    private int Chk11BlueCount   = 0;
//    private List<TCustomAuditInfo> Check11Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加重复成分 
//     * @param CustA
//     */
//    public void addCheck11Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check11Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck11Rslt()
//    {
//        return (TCustomAuditInfo[]) Check11Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck11Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk11BlueCount = this.Chk11BlueCount;
//        rslt.Chk11RedCount = this.Chk11RedCount;
//        rslt.Chk11YellowCount = this.Chk11YellowCount;
//        rslt.Check11Rslt      = this.Check11Rslt;
//    }
//    
//    /* 过敏   */
//    private int Chk12RedCount    = 0;
//    private int Chk12YellowCount = 0;
//    private int Chk12BlueCount   = 0;
//    private List<TCustomAuditInfo> Check12Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加过敏  
//     * @param CustA
//     */
//    public void addCheck12Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check12Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck12Rslt()
//    {
//        return (TCustomAuditInfo[]) Check12Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck12Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk12BlueCount = this.Chk12BlueCount;
//        rslt.Chk12RedCount = this.Chk12RedCount;
//        rslt.Chk12YellowCount = this.Chk12YellowCount;
//        rslt.Check12Rslt      = this.Check12Rslt;
//    }
//    
//    /* 剂量  */
//    private int Chk13RedCount    = 0;
//    private int Chk13YellowCount = 0;
//    private int Chk13BlueCount   = 0;
//    private List<TCustomAuditInfo> Check13Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加剂量 
//     * @param CustA
//     */
//    public void addCheck13Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check13Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck13Rslt()
//    {
//        return (TCustomAuditInfo[]) Check13Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck13Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk13BlueCount = this.Chk13BlueCount;
//        rslt.Chk13RedCount = this.Chk13RedCount;
//        rslt.Chk13YellowCount = this.Chk13YellowCount;
//        rslt.Check13Rslt      = this.Check13Rslt;
//    }
//    
//    /* 异常信号  */
//    private int Chk14RedCount    = 0;
//    private int Chk14YellowCount = 0;
//    private int Chk14BlueCount   = 0;
//    private List<TCustomAuditInfo> Check14Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加异常信号 
//     * @param CustA
//     */
//    public void addCheck14Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check14Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck14Rslt()
//    {
//        return (TCustomAuditInfo[]) Check14Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck14Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk14BlueCount = this.Chk14BlueCount;
//        rslt.Chk14RedCount = this.Chk14RedCount;
//        rslt.Chk14YellowCount = this.Chk14YellowCount;
//        rslt.Check14Rslt      = this.Check14Rslt;
//    }
//    
//    /* 禁用症  */
//    private int Chk15RedCount    = 0;
//    private int Chk15YellowCount = 0;
//    private int Chk15BlueCount   = 0;
//    private List<TCustomAuditInfo> Check15Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加禁用症 
//     * @param CustA
//     */
//    public void addCheck15Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check15Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck15Rslt()
//    {
//        return (TCustomAuditInfo[]) Check15Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck15Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk15BlueCount = this.Chk15BlueCount;
//        rslt.Chk15RedCount = this.Chk15RedCount;
//        rslt.Chk15YellowCount = this.Chk15YellowCount;
//        rslt.Check15Rslt      = this.Check15Rslt;
//    }
//    
//    /* 慎用症  */
//    private int Chk16RedCount    = 0;
//    private int Chk16YellowCount = 0;
//    private int Chk16BlueCount   = 0;
//    private List<TCustomAuditInfo> Check16Rslt = new ArrayList<TCustomAuditInfo>();
//    
//    /**
//     * 增加慎用症  
//     * @param CustA
//     */
//    public void addCheck16Rslt(TCustomAuditInfo CustA)
//    {
//        this.Check16Rslt.add(CustA);
//    }
//    
//    public TCustomAuditInfo[] getCheck16Rslt()
//    {
//        return (TCustomAuditInfo[]) Check16Rslt.toArray(new TCustomAuditInfo[0]);
//    }
//    
//    public void CopyCheck16Rslt(TCustomCheckRslt rslt)
//    {
//        rslt.Chk16BlueCount = this.Chk16BlueCount;
//        rslt.Chk16RedCount = this.Chk16RedCount;
//        rslt.Chk16YellowCount = this.Chk16YellowCount;
//        rslt.Check16Rslt      = this.Check16Rslt;
//    }
}
