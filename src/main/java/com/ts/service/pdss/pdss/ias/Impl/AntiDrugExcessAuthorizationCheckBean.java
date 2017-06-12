package com.ts.service.pdss.pdss.ias.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;

import com.ts.entity.pdss.pdss.Beans.TDrugMap;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugRslt;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;
import com.ts.service.pdss.pdss.ias.IAntiDrugExcessAuthorizationCheck;

/**
 * 
 * 超授权使用抗菌药物监测与提示
 * @author Administrator
 *
 */
@Service("antiDrugExcessAuthorizationCheck")
public class AntiDrugExcessAuthorizationCheckBean extends Persistent4DB implements IAntiDrugExcessAuthorizationCheck 
{
    @Override
    public TAntiDrugRslt Checker(TPatientOrder po ,TPatOrderDrug poDrug)
    {
        setQueryCode("PDSS");
        TAntiDrugRslt drugRs = null;
        
        /* 提示信息 */
        /**
         * 
         * 你下达的当前医嘱属性为二线以上抗菌药物,根据《抗菌药物临床应用指导原则》中的“抗菌药物临床使用权限管理”规定，应由上级主管医生下达当前的抗菌药物或调整给药方案！你当前的职称是[1]
         * 
         * 
         */
        String context  =  "你下达的当前医嘱属性为[0],根据《抗菌药物临床应用指导原则》中的“抗菌药物临床使用权限管理”规定，应由上级主管医生下达当前的抗菌药物或调整给药方案！你当前的职称是[1]"; //Config.getParamValue("AntiDrugExcessAuthorizationCheck");
        //Stirng x = "您下达的当前医嘱属性为二线以上抗菌药物，根据《抗菌药物临床应用指导原则》中的“抗菌药物临床使用权限管理”规定，使用该类抗菌药物时，必须符合主治医师职称用药！您当前的职称是[ + + ];"
        String drugInfo = "";
        try
        {
            TDrugMap dm       = QuerySingleUtils.queryDrugMap(poDrug.getDrugID(),query);
            Integer antiLvL   = Integer.valueOf(dm.getANTI_LEVEL());
            String doctorLvl  = po.getDoctorTitleID().toUpperCase();
            boolean flag      = false;
            switch(antiLvL.intValue())
            {
                case 3:
                    drugInfo = "特殊级抗菌药物";
                    if("E".equals(doctorLvl)|| "D".equals(doctorLvl) 
                                || "C".equals(doctorLvl)  || "1".equals(doctorLvl))
                    {
                        flag = true;
                    }
                    break;
                case 2:
                    drugInfo = "限制级抗菌药物";
                    if(!"3".equals(doctorLvl))
                    {
                        flag = true;
                    }
                    break;
                case 1:
                    drugInfo = "非限制级抗菌药物";
                    flag = true;
                    break;
                case 0 :
                    flag = true;
                    break;
            }
            if(!flag)
            {
                context = context.replace("[0]","[" + drugInfo + "]").replace("[1]", "[" + po.getDoctorTitleName() + "]");
                drugRs = (new TAntiDrugRslt(TAntiDrugRslt.AntiDrugExcessAuthorizationCheck,context,false,"抗菌药",new String[]{drugInfo}));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return drugRs;
    }
    
    /**
     *  医生职称
     * @param lvl
     * @return
     */
    private String getDoctorTilteName(Integer lvl)
    {
        String tilteName = "";
        switch(lvl)
        {
            case 1 :
                tilteName = "主任医师";
            case 2 :
                tilteName = "副主任医师";
                break;
            case 3 :
                tilteName = "主治医师";
                break;
            case 4 :
                tilteName = "医师";
                break;
        }
        return tilteName;
    }
    
}
