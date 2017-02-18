package com.ts.service.pdss.ias.impl;


import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.Beans.TAntiDrugUseRule;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.pdss.Beans.TDrugMap;
import com.ts.service.pdss.ias.manager.IAntiDrugExcessAuthorizationCheck;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;

/**
 * 
 * 超授权使用抗菌药物监测与提示
 * @author Administrator
 *
 */
@Service
@Transactional
public class AntiDrugExcessAuthorizationCheckBean extends Persistent4DB implements IAntiDrugExcessAuthorizationCheck 
{
    @Override
    public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp)
    {
        setQueryCode("PDSS");
        /* 药品信息 */
        TAntiDrugSecurityCheckResult antiDrug =  new TAntiDrugSecurityCheckResult();
        antiDrug.setDrug_ID(antiDrugInp.getDrugID());
        /* 本地药品  */
        antiDrug.setDrugStandID("");
        antiDrug.setDrug_name(antiDrug.getDrug_name());
        antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
        antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
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
            if(!DrugUtils.isKJDrug(antiDrugInp.getDrugID())) return antiDrug;
            TDrugMap dm       = QuerySingleUtils.queryDrugMap(antiDrugInp.getDrugID(),query);
            Integer antiLvL   = Integer.valueOf(dm.getANTI_LEVEL());
            String doctorLvl  = antiDrugInp.getDoctor().getDoctorTitleID().toUpperCase();
            boolean flag      = false;
            switch(antiLvL.intValue())
            {
                case 3:
                    drugInfo = "特殊级抗菌药物";
                    if("E".equals(doctorLvl)|| "D".equals(doctorLvl) 
                                || "C".equals(doctorLvl)  || "3".equals(doctorLvl))
                    {
                        flag = true;
                    }
                    break;
                case 2:
                    drugInfo = "限制级抗菌药物";
                    if(!("A".equals(doctorLvl) || "1".equals(doctorLvl)))
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
            if(flag)
            {
                antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugExcessAuthorizationCheck,"",true,"抗菌药",new String[]{drugInfo}));
            }
            else
            {
                context = context.replace("[0]",drugInfo).replace("[1]", "[" + antiDrugInp.getDoctor().getDoctorTitleName() + "]");
                antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugExcessAuthorizationCheck,context,false,"抗菌药",new String[]{drugInfo}));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return antiDrug;
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
    
    
	public TAntiDrugSecurityCheckResult Checker1(TAntiDrugInput antiDrugInp) 
	{
		setQueryCode("IAS");
		/* 药品信息  */
		TAntiDrugSecurityCheckResult antiDrug =  new TAntiDrugSecurityCheckResult();
		try
		{
			/* 判断是否是抗菌药物 */
			if(!DrugUtils.isKJDrug(antiDrugInp.getDrugID())) return antiDrug;
			//TODO 现在使用本地药品码作为主键 
			TAntiDrugUseRule adur = null ;// QueryUtils.getAntiDrugUseRule(antiDrugInp.getDrugID(), query);
			antiDrug.setDrug_ID(antiDrugInp.getDrugID());
			/* 本地药品  */
			antiDrug.setDrugStandID("");
			antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
			antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());

			if(adur == null) return antiDrug;
			boolean flag = false;
			if(adur.getUse_Dept() != null && !"*".equals(adur.getUse_Dept()))
			{
				String deptCodes[] = adur.getUse_Dept().split(";");
				for(String x : deptCodes)
				{
					if(x.equals(antiDrugInp.getDoctor().getDoctorDeptID()))
					{
						flag =  true;
						break;
					}
					antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugExcessAuthorizationCheck,"超授权使用抗菌药物",false));
				}
			}
			/* 此判断用来先判断部门是否有权限  用职称  判断 使用级别 */
			if(flag)
			{
				int level = 0;
				switch(Integer.parseInt(antiDrugInp.getDoctor().getDoctorTitleID()))
				{
					case 1:
						level = 4;
						break;
					case 2:
						level = 4;
						break;
					case 3:
						level = 2;
						break;
					case 4 :
						level = 1;
						break;
				}
				if(level > Integer.parseInt(adur.getUse_Level()))
				{
					antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugExcessAuthorizationCheck,"已经授权使用抗菌药物",true));
					InsertLevelHight(antiDrugInp);
				}
				else if(level != 4)
				{
					antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugExcessAuthorizationCheck,"超授权使用抗菌药物",false));
				}
				else if("4".equals(adur.getUse_Level()))
				{
					if("1".equals(adur.getAudit_Res()))
					{
						antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugExcessAuthorizationCheck,"授权使用抗菌药物",true));
						InsertLevelHight(antiDrugInp);
					}
					else
					{
						antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugExcessAuthorizationCheck,"没经过审批使用抗菌药物",false));
					}
				}
			}
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
		return antiDrug;
	}
	
	/**
	 *  保存 高级 职称的使用情况
	 * @param antiDrugInp
	 */
	private void InsertLevelHight(TAntiDrugInput antiDrugInp)
	{
	    setQueryCode("");
		StringBuffer strSql = new StringBuffer();
		strSql.append("insert into Anti_Drug_Use_Info(SERIAL_NO,Drug_ID,User_Name,Dept_Name,Use_Date,Patient_ID,Visit_Id,Order_No,Order_Sub_No,Memo)")
				.append(" values( ")
				.append("'").append(UUID.randomUUID().toString()).append("'")
				.append(",'").append(antiDrugInp.getDrugID()).append("'")
				.append(",'").append(antiDrugInp.getDoctor().getDoctorName()).append("'")
				.append(",'").append(antiDrugInp.getDoctor().getDoctorDeptName()).append("'")
				.append(",to_date(").append(DateUtils.getDateTime()).append(",'yyyy-dd-mm h24:mm:ss')")
				.append(",'").append(antiDrugInp.getPatientId()).append("'")
				.append(",'").append(antiDrugInp.getVisi_id()).append("'")
				.append(",'").append(antiDrugInp.getRecMainNo()).append("'")
				.append(",'").append(antiDrugInp.getRecSubNo()).append("'")
				.append(",'").append("").append("'")
				.append(")");
		query.execute(strSql.toString());
	}
}
