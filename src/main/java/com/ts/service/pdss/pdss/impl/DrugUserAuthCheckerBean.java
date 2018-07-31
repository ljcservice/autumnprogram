package com.ts.service.pdss.pdss.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.DrugUseAuth.TCkDrugUserAuth;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.pdss.RSBeans.DrugUserAuth.TDrugUserAuthResult;
import com.ts.entity.pdss.pdss.RSBeans.DrugUserAuth.TDrugUserAuthRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.manager.IDrugUserAuthChecker;
import com.ts.util.Logger;


/**
 * 药物授权控制审查 
 * @author autumn
 *
 */
@Service
@Transactional
public class DrugUserAuthCheckerBean implements IDrugUserAuthChecker
{
    Logger log = Logger.getLogger(DrugUserAuthCheckerBean.class);
    @Resource(name = "pdssCache")
    private PdssCache pdssCache;
    
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
        try
        {
            TDrugSecurityRslt rslt = new TDrugSecurityRslt();
            String deptName = po.getDoctorDeptName();
            String doctorName = po.getDoctorName();
            TPatOrderDrug[] poDrugs =  po.getPatOrderDrugs();
            Map<String,Integer>  mapdrugCount = new HashMap<String, Integer>();
            for(TPatOrderDrug pod : poDrugs)
            {
                TDrugUserAuthResult  duaResult = new TDrugUserAuthResult();
                //TODO 需要在选择药物的时候。只能查出已经匹配的药品 。
                TDrug drug = po.DrugMap().get(pod.getDrugID());
                String drugCode = pod.getDrugID();
                String drugName = pod.getDrugName();
                int drugCount = drugCount(pod);
                if(!mapdrugCount.containsKey(drugCode +  drugName))
                {
                    mapdrugCount.put(drugCode +  drugName, 0);
                }
                mapdrugCount.put(drugCode + drugName , drugCount  + mapdrugCount.get(drugCode + drugName));
                Map<String, TCkDrugUserAuth> duaMap = pdssCache.queryDrugUserAuthByMap(drugCode, drugName, deptName, doctorName);
                if(duaMap ==  null)  continue;
                // 允许(不看量) 
                if(duaMap.containsKey("6")) continue;
                if(duaMap.containsKey("5")){
                    // 禁用(不看量) 
                    TCkDrugUserAuth  controltype5 =  duaMap.get("5");
                    TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                    entity.setDua(controltype5);
                    entity.setQAInfo(doctorName + "医生您好,很抱歉您所要开具的【" + drugName + "】为医院禁开目录中药物，所以不能开具该药品!");
                    duaResult.addDrugUserAuth(entity);
                    rslt.regDrugUserAuthCheckResult(drug, duaResult);
                    continue;
                }
                //如果医生开具的用量小于等 0 则不进行一下判断
                if(drugCount <= 0) continue;
                //年量
                if(duaMap.containsKey("3")){ 
                    TCkDrugUserAuth  controltype3 =  duaMap.get("3");
                    int tValue =  controltype3.getT_VALUE();
                    int totalValue =  controltype3.getTOTAL_VALUE();
                    if(tValue < (totalValue + drugCount))
                    {
                        TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                        entity.setDua(controltype3);
                        entity.setQAInfo(doctorName + "医生您好，很抱歉您所要开具的【" + drugName + "】数量已经超出医院规定【全年的总量】:" 
                                + ((totalValue + drugCount) - tValue) + "个,全年总量为:" + tValue + ",已经开具为:" + totalValue);
                        duaResult.addDrugUserAuth(entity);
                    }else if(tValue < (totalValue + mapdrugCount.get(drugCode + drugName) ))
                    {
                        TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                        entity.setDua(controltype3);
                        entity.setQAInfo(doctorName + "医生您好，请将【" + drugName + "】开具在一起！");
                        duaResult.addDrugUserAuth(entity);
                    }
                    
                }
                //季度量
                if(duaMap.containsKey("4")){
                    TCkDrugUserAuth  controltype4 =  duaMap.get("4");
                    int tValue =  controltype4.getT_VALUE();
                    int totalValue =  controltype4.getTOTAL_VALUE();
                    if(tValue < (totalValue + drugCount))
                    {
                        TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                        entity.setDua(controltype4);
                        entity.setQAInfo(doctorName + "医生您好，很抱歉您所要开具的【" + drugName + "】数量已经超出医院规定【季度的总量】:" 
                                + ((totalValue + drugCount) - tValue) + "个,季度总量为:" + tValue + ",已经开具为:" + totalValue);
                        duaResult.addDrugUserAuth(entity);
                    }
                    else if(tValue < (totalValue + mapdrugCount.get(drugCode + drugName) ))
                    {
                        TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                        entity.setDua(controltype4);
                        entity.setQAInfo(doctorName + "医生您好，请将【" + drugName + "】开具在一起！");
                        duaResult.addDrugUserAuth(entity);
                    }
                }
                //月量
                if(duaMap.containsKey("2")){
                    TCkDrugUserAuth  controltype2 =  duaMap.get("2");
                    int tValue =  controltype2.getT_VALUE();
                    int totalValue =  controltype2.getTOTAL_VALUE();
                    if(tValue < (totalValue + drugCount))
                    {
                        TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                        entity.setDua(controltype2);
                        entity.setQAInfo(doctorName + "医生您好，很抱歉您所要开具的【" + drugName + "】数量已经超出医院规定【本月的总量】:" 
                                + ((totalValue + drugCount) - tValue) + "个,本月总量为:" + tValue + ",已经开具为:" + totalValue);
                        duaResult.addDrugUserAuth(entity);
                    }else if(tValue < (totalValue + mapdrugCount.get(drugCode + drugName) ))
                    {
                        TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                        entity.setDua(controltype2);
                        entity.setQAInfo(doctorName + "医生您好，请将【" + drugName + "】开具在一起！");
                        duaResult.addDrugUserAuth(entity);
                    }
                }
                
                //日量
                if(duaMap.containsKey("1")){
                    TCkDrugUserAuth  controltype1 =  duaMap.get("1");
                    int tValue =  controltype1.getT_VALUE();
                    int totalValue =  controltype1.getTOTAL_VALUE();
                    if(tValue < (totalValue + drugCount))
                    {
                        TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                        entity.setDua(controltype1);
                        entity.setQAInfo(doctorName + "医生您好，很抱歉您所要开具的【" + drugName + "】数量已经超出医院规定【单日的总量】为:" 
                                + ((totalValue + drugCount) - tValue) + ",单日总量为:" + tValue + ",已经开具为:" + totalValue);
                        duaResult.addDrugUserAuth(entity);
                    }else if(tValue < (totalValue + mapdrugCount.get(drugCode + drugName) ))
                    {
                        TDrugUserAuthRslt entity = new TDrugUserAuthRslt();
                        entity.setDua(controltype1);
                        entity.setQAInfo(doctorName + "医生您好，请将【" + drugName + "】开具在一起！");
                        duaResult.addDrugUserAuth(entity);
                    }
                }
                // 添加 审核 结果 
                if(duaResult.getDuarslt().size() > 0)
                {
                    TDrug td = new TDrug(drug);
                    td.setRecMainNo(pod.getRecMainNo());
                    td.setRecSubNo(pod.getRecSubNo());
                    rslt.regDrugUserAuthCheckResult(td,duaResult );
                }
            }
            return rslt;
        }
        catch(Exception e )
        {
            e.printStackTrace();
            log.warn(this.getClass().toString()+":" + e.getMessage());
            return new TDrugSecurityRslt();
        }
    }

    /**
     * 获得个数
     * @param pod
     * @return
     */
    private int drugCount(TPatOrderDrug pod)
    {
        int drugCount = 0;
        try
        {
            drugCount = Integer.parseInt(pod.getStartDateTime());
        }
        catch(Exception e )
        {
            log.warn(this.getClass().toString() + ":" +e.getMessage());
        }
        return drugCount;
    }
}
