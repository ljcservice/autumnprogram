package com.ts.service.pdss.pdss.ias.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.pdss.Beans.ias.TOperationDrugInfo;
import com.ts.entity.pdss.pdss.RSBeans.ias.TAntiDrugRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.ias.IAntiDrugOperationCheck;

/**
 * 手术用抗菌药监测与提示
 * @author autumn
 *
 */
@Service
public class AntiDrugOperationCheckBean implements IAntiDrugOperationCheck
{

    @Resource(name = "pdssCache")
    private PdssCache pdssCache;
    
    @Override
    public List<TAntiDrugRslt> Checker(TPatientOrder po, TPatOrderDrug poDrug)
    {

        TPatOperation[] patOpers =  po.getPatOperation();
        if(patOpers == null || patOpers.length == 0 ) return null;
        List<TAntiDrugRslt> rsList = new ArrayList<TAntiDrugRslt>(); 
        String drugCode = poDrug.getDrugID();
        String drugSpace = poDrug.getDosage() + poDrug.getDoseUnits();
        String poDeptCode = po.getDoctorDeptID();
        String poDeptName = po.getDoctorDeptName();
        String poDoctorCode = po.getDoctorID();
        String poDoctorName = po.getDoctorName();
        try
        {
            for(TPatOperation patO : patOpers)
            {
                String keyCode = patO.getOperCode();
                String keyName = patO.getOperName();
                String key = keyCode;
                if(keyCode== null || "".equals(keyCode)) key = keyName;
                // 每组结果 是按照  药品可用 排序  可用在列表前 
                List<TOperationDrugInfo> operDrug = pdssCache.queryOperationDrugByCode(key);
                if(operDrug == null || operDrug.size() == 0 ) return null;
                TAntiDrugRslt  rs = new TAntiDrugRslt();
                String[] strOD =  getOperationDrug(poDeptCode,poDeptName,operDrug);
                if(strOD == null || strOD.length == 0 ) continue;
                rs.setBak(strOD);
                for(TOperationDrugInfo od : operDrug)
                {
                    boolean condDept = false;
                    boolean condDoct = false;
                    if(!od.getO_drug_code().equals(drugCode)) continue;
                    String deptCode = od.getO_dept_code();
                    String deptName = od.getO_dept_name();
                    String doctCode = od.getO_doctor_code();
                    String doctName = od.getO_doctor_name();
                    if(deptCode.equalsIgnoreCase(poDeptCode) || deptName.equalsIgnoreCase(poDeptName))
                    {
                        condDept = true;
                    }
                    // 暂时不 考虑医生
//                    if(doctCode.equalsIgnoreCase(poDoctorCode) || doctName.equalsIgnoreCase(poDoctorName)){
//                        condDept = true;
//                    }
                    if(condDept && "1".equals(od.getIs_use()))
                    {
                        break;
                    }else if (condDept  && "0".equals(od.getIs_use()))
                    {
                        //TODO 组织结果返回问题
                        rs.setResult(false);
                        rs.setTilte( patO.getOperCode() + ",," + patO.getOperName() );
                        rs.setCheckType(TAntiDrugResult.AntiDrugOperationCheck);
                        rs.setMemo("药品:" + poDrug.getDrugName() + ",[超出]医院管理手术可用药品范围!请适当的调整用药." );
                        rsList.add(rs);
                        break;
                    }
                    if ("*".equals(deptName)  && "0".equals(od.getIs_use()))
                    {
                        //TODO 组织结果返回问题
                        rs.setResult(false);
                        rs.setTilte( patO.getOperCode() + ",," + patO.getOperName() );
                        rs.setCheckType(TAntiDrugResult.AntiDrugOperationCheck);
                        rs.setMemo("药品:" + poDrug.getDrugName() + ",[超出]医院管理手术可用药品范围!请适当的调整用药." );
                        //添加到数据
                        rsList.add(rs);
                    }
                }
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            drugCode = null;
            drugSpace = null;
            poDeptCode = null;
            poDeptName = null;
            poDoctorCode = null;
            poDoctorName = null;
        }
        return rsList;
    }
    
    private String[] getOperationDrug(String deptCode ,String deptName , List<TOperationDrugInfo> operDrug)
    {
        List<String>  strs = new ArrayList<String>();
        for(TOperationDrugInfo  entity : operDrug)
        {
            if("*".equalsIgnoreCase(entity.getO_dept_code()) 
                    || deptCode.equals(entity.getO_dept_code()) || deptName.equalsIgnoreCase(entity.getO_dept_name()))
            {
                strs.add(entity.getO_drug_code() + ",," + entity.getO_drug_name() 
                    + ",," + entity.getO_drug_spce() + ",," + entity.getO_drug_form() + ",," + entity.getIs_use());
            }
        }
        return  (String[]) strs.toArray(new String[0]);
    }

}
