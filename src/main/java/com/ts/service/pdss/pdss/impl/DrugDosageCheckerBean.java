package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderInfoExt;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDosage;
import com.ts.entity.pdss.pdss.Beans.TDrugPerformFreqDict;
import com.ts.entity.pdss.pdss.RSBeans.TDrugDosageRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugDosageChecker;

/**
 * 药物剂量审查
 * @author liujc
 *
 */
@Service
@Transactional
public class DrugDosageCheckerBean extends Persistent4DB implements IDrugDosageChecker
{
	private final static Logger log = Logger.getLogger(DrugDosageCheckerBean.class);
	
	@Resource(name = "pdssCache")
	private PdssCache pdssCache;
	
    /**
     * 药物剂量审查
     * ok
     */
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
    	try
    	{
	        //this.setQueryCode("PDSS");
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
	        TPatOrderInfoExt patInfoExt = po.getPatInfoExt();
	        if(patInfoExt == null)
	        {
	            patInfoExt = new TPatOrderInfoExt();
	            patInfoExt.setWeight("0");
	            patInfoExt.setHeight("0");
	        }
	        /* 病人体重 */
	        Double weight = new Double(patInfoExt.getWeight());
	        /* 病人高度 */
	        Double height = new Double(patInfoExt.getHeight());
	        /* 病人年龄（天）*/
	        Integer day = po.getPatient().calAgeDays().intValue();  
	        /* 如果检测参数为空 则不进行检查  weight == 0 && height == 0 &&  */
	        if(day == 0) return new TDrugSecurityRslt();
	        for(int i = 0 ;i<po.getPatOrderDrugs().length;i++)
	        {
	            TPatOrderDrug pod = po.getPatOrderDrugs()[i];
	            /* 每次使用剂量 */
	            Double dosage = new Double((Double) (pod.getDosage()== null || "".equals(pod.getDosage())?0d : Double.parseDouble(pod.getDosage())));
	            String doseUnits  = pod.getDoseUnits();
	            /* 药品*/
	            TDrug drug =  po.getDrugMap(pod.getDrugID());
	            TAdministration administr = pdssCache.queryAdministration(pod.getAdministrationID()) ;//(new String[]{pod.getAdministrationID()}, null, query);
	            if(drug == null || administr == null || drug.getDOSE_CLASS_ID() == null) 	continue;
	            /* 将缓存中 获取 剂量  */
	            List<TDrugDosage> ddgs = pdssCache.getDdg(drug.getDOSE_CLASS_ID(), administr.getADMINISTRATION_ID());
	            if(ddgs == null || ddgs.size() <= 0)  continue;
	            TDrugDosage ddg = null;
	            for(int ddgI = 0 ; ddgI < ddgs.size() ; ddgI++ )
	            {
	                TDrugDosage ddgx = ddgs.get(ddgI);
	                if(Integer.parseInt(ddgx.getAGE_LOW()) <= day && Integer.parseInt(ddgx.getAGE_HIGH()) >= day)
	                {
	                    ddg  = ddgx;
	                    ddgx = null;
	                    break;
	                }
	            }
	            if(ddg == null) continue;
	            /* 警告信息 */
	            List<String> dosageInfo = new ArrayList<String>();
	            if(weight != 0 )
	            {
	                /* 体重检查 */
	                if("1".equals(ddg.getWEIGHT_INDI())&& !"0".equals(ddg.getWEIGHT_HIGH()) && !"0".equals(ddg.getWEIGHT_LOW()))
	                {
	                    Double weightHigh = 0d;
	                    Double weightLow  = 0d;
	                    if(ddg.getWEIGHT_HIGH()!= null)
	                        weightHigh = new Double(ddg.getWEIGHT_HIGH());
	                    if(ddg.getWEIGHT_LOW() != null)
	                        weightLow  = new Double(ddg.getWEIGHT_LOW());
	                    if(weightHigh < weight || weightLow > weight)
	                        dosageInfo.add("体重受限,体重范围应为" + weightLow + "~" + weightHigh);
	                }
	            }
	            if(weight != 0 && height != 0)
                {
	                /* 每次剂量  */
	                int eachDoseResult = checkDoseEach(ddg, dosage, weight, height,doseUnits);
	                if(eachDoseResult<0)
	                {
	                    dosageInfo.add("低于每次最小剂量");
	                }
	                else if(eachDoseResult>0)
	                {
	                    dosageInfo.add("高于每次最大剂量");
	                }
                }
	            /* 频率标准码 次数 */
	            TDrugPerformFreqDict drugperform = pdssCache.queryDrugPerfom(pod.getPerformFreqDictID());
	            Double frequency = null;
	            if(drugperform != null) 
	            {
    	            frequency = Double.parseDouble(drugperform.getFREQ_COUNTER());
    	            /* 每天剂量 */
    	            int eachDayDoseResult = checkDoseDay(ddg, dosage, frequency,doseUnits);
    	            if(eachDayDoseResult < 0)
    	            {
    	                dosageInfo.add("低于每天最小剂量");
    	            }
    	            else 
    	            if(eachDayDoseResult > 0)
    	            {
    	                dosageInfo.add("高于每天最大剂量");
    	            }
    	            /* 每天频次 */
    	            if(frequency != null)
    	            {
    	                if(ddg.getDOSE_FREQ_LOW() != null && frequency < Double.parseDouble(ddg.getDOSE_FREQ_LOW())){
    	                    dosageInfo.add("低于每天最小频次");
    	                    
    	                }else if(ddg.getDOSE_FREQ_HIGH() != null && frequency > Double.parseDouble(ddg.getDOSE_FREQ_HIGH())){
    	                    dosageInfo.add("高于每天最大频次");
    	                }
    	            }
	            }
	            /* 用药 开始与结束时间 不为空 */
	            if(pod.getStartDateTime() != null && !"".equals(pod.getStartDateTime()) 
	                    && pod.getStopDateTime() != null && !"".equals(pod.getStopDateTime())) 
	            {
	                Date sTime = DateUtils.getDateFromString(pod.getStartDateTime(),DateUtils.FORMAT_DATETIME);//开始用药时间
	                Date eTime = null;//结束用药时间
	                if (pod.getStopDateTime() != null && !pod.getStopDateTime().equals("")) 
	                {
	                    eTime = DateUtils.getDateFromString(pod.getStopDateTime(),DateUtils.FORMAT_DATETIME);
	                }
	                /* 用药天数  */
	                int durResult = checkDur(ddg, eTime, sTime);
	                if(durResult<0)
	                {
	                    dosageInfo.add("用药天为:" + getDrugUseDay(sTime, eTime) + "天,低于最小用药天数，最小天数为: " + ddg.getDUR_LOW() + "天");
	                }
	                else if(durResult>0)
	                {
	                    dosageInfo.add("高于最大用药天数");
	                }
	                //3.7最大剂量
	                if(ddg.getDOSE_MAX_HIGH() != null && Double.parseDouble(ddg.getDOSE_MAX_HIGH())!=0)
	                {
	                    int durDay;
	                    if(eTime==null)
	                    {
	                        durDay = 1;
	                    }
	                    else
	                    {
	                        durDay=(int)getDrugUseDay(sTime, eTime);
	                    }
	                    if(frequency == null)
	                    {
	                        frequency = 1.0;
	                    }
	                    if(frequency * dosage*durDay > Double.parseDouble(ddg.getDOSE_MAX_HIGH()) 
	                            && Double.parseDouble(ddg.getDOSE_MAX_HIGH()) > 0)
	                    {
	                        dosageInfo.add("高于最大剂量");
	                    }
	                }
	            }
	            if(dosageInfo.size() <= 0) continue;
	            /* 对每一个返回的药品标注上 医嘱序号 */
	            drug.setRecMainNo(pod.getRecMainNo());
	            drug.setRecSubNo(pod.getRecSubNo());
	            TDrugDosageRslt dosageRS = new TDrugDosageRslt();
	            dosageRS.addDrugDosage(drug, dosageInfo);
	            dosageRS.setRecMainNo(pod.getRecMainNo());
	            dosageRS.setRecSubNo(pod.getRecSubNo());
	            result.regDosageCheckResult(drug, dosageRS);
	        }   
	        return result;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		log.warn(e.getMessage());
    		return new TDrugSecurityRslt();
    	}
    }

    /**
     * 药品使用天数
     * @param sTime
     * @param eTime
     * @return
     */
	private long getDrugUseDay(Date sTime, Date eTime) {
		return (eTime.getTime() - sTime.getTime()) / 1000 / (24 * 60 * 60);
	}
    
    /**
     *  计算标志 审查 
     *  用于没每次剂量
     * @param drugDosage
     * @param dosage 
     * @param weight 体重
     * @param height 身高
     * @param doseUnits 剂量单位
     * @return
     */
    private int checkDoseEach(TDrugDosage drugDosage,Double dosage, Double weight,  Double height,String doseUnits) 
    {
        double dosa = setDosageUnit(drugDosage.getDOSE_EACH_UNIT(), dosage, doseUnits);
        //下限
        Double low = null;
        if (drugDosage.getCAL_INDI().equals("1")) 
        {
        	//体重小于10公斤  面积（m2）= 0.035 * 体重（kg） + 0.1
        	//体重大于10公斤  面积= 0.0061 * 身高 + 0.0128 * 体重- 0.1529
        	//每次最小剂量（计算出） = 面积 * 每次最小剂量（数据库中字段）
        	//每次最高剂量（计算出） = 面积 * 每次最高剂量（数据库中字段）
            if (weight < 10) 
            {
                low = (0.035 * weight + 0.1) * Double.parseDouble(drugDosage.getDOSE_EACH_LOW());
            }
            if (weight > 10)
            {
                low = (0.0061 * height + 0.0128 * weight - 0.1529) * Double.parseDouble(drugDosage.getDOSE_EACH_LOW());
            }
        }
        else
        if (drugDosage.getCAL_INDI().equals("2")) 
        {
        	//每次最小剂量（计算出） = 体重 * 每次最小剂量（数据库中字段）
        	//每次最高剂量（计算出） = 体重 * 每次最高剂量（数据库中字段）
            low = weight * Double.parseDouble(drugDosage.getDOSE_EACH_LOW());
        }
        if (low != null && dosa < low)
            return -1;

        //上限
        Double high = null;
        if (drugDosage.getCAL_INDI().equals("1")) 
        {
            if (weight < 10)
                high = (0.035 * weight + 0.1) * Double.parseDouble(drugDosage.getDOSE_EACH_HIGH());

            if (weight > 10)
                high = (0.0061 * height + 0.0128 * weight - 0.1529) * Double.parseDouble(drugDosage.getDOSE_EACH_HIGH());

        }
        if (drugDosage.getCAL_INDI().equals("2")) 
        {
            high = weight * Double.parseDouble(drugDosage.getDOSE_EACH_HIGH());
        }

        if (high != null && dosa > high)
            return 1;
        
        return 0;
    }
    
    /**
     *   每天剂量
     * @param drugDosage
     * @param dosage
     * @param frequency
     * @return
     */
    private int checkDoseDay(TDrugDosage drugDosage,Double dosage, Double frequency,String doseUnits)
    {
        if(drugDosage.getDOSE_DAY_LOW()== null || drugDosage.getDOSE_DAY_HIGH() == null) return 0;
        double  dos = setDosageUnit(drugDosage.getDOSE_DAY_UNIT(),dosage,doseUnits) ;
        if(frequency==null)
            return 0;
        if ((frequency * dos) < Double.parseDouble(drugDosage.getDOSE_DAY_LOW()))
            return -1;
        if (((frequency * dos) > Double.parseDouble(drugDosage.getDOSE_DAY_HIGH()))
                    && Double.parseDouble(drugDosage.getDOSE_DAY_HIGH()) > 0)
            return 1;
        return 0;
    }
    
    private double  setDosageUnit(String ddunit , double dosage ,String doseUnits)
    {
        if(ddunit == null) return dosage;
        if("mg".toUpperCase().equals(doseUnits.toUpperCase()))
        {
            if(ddunit.toUpperCase().indexOf("mg".toUpperCase()) != -1)
            {
                return dosage;
            }
            else if(ddunit.toUpperCase().indexOf("g".toUpperCase()) != -1){
                return dosage / 1000;
            }
        }else if("g".toUpperCase().equals(doseUnits.toUpperCase()))
        {
            if(ddunit.toUpperCase().indexOf("mg".toUpperCase()) != -1)
            {
                return dosage * 1000;
            }
            else if(ddunit.toUpperCase().indexOf("g".toUpperCase()) != -1){ 
                return dosage ;
            }
        }
        return dosage;
    }
    
    /**
     * 用药天数
     * @param drugDosage
     * @param eTime
     * @param sTime
     * @return
     */
    private int checkDur(TDrugDosage drugDosage,Date eTime, Date sTime){
        if (sTime != null && eTime != null){
            if (drugDosage.getDUR_LOW()!=null && getDrugUseDay(sTime, eTime) < Double.parseDouble(drugDosage.getDUR_LOW())){
                return -1;
            }else if (drugDosage.getDUR_HIGH() != null && (getDrugUseDay(sTime, eTime) > Double.parseDouble(drugDosage.getDUR_HIGH()))
                    && Double.parseDouble(drugDosage.getDUR_HIGH()) > 0){
                return 1;
            }
        }
        return 0;
    }

	@Override
	public TDrugSecurityRslt Check(String[] drugIds, String[] dosages,
			String[] performFreqDictIds, String[] startDates,
			String[] stopDates, String weight, String height, String birthDay) {
		// TODO Auto-generated method stub
		return null;
	}
}
