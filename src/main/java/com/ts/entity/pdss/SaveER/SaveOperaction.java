package com.ts.entity.pdss.SaveER;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.persistent.Persistent4DB;
import com.ts.SchedulerHandler.ScheduleManager;
import com.ts.util.Logger;

@Service
@Transactional
public class SaveOperaction extends Persistent4DB implements ISaveOperaction
{
    private static final Logger log = Logger.getLogger(SaveOperaction.class);
    public void saveOperaction(TCommonRecord tcr) throws Exception
    {
        setQueryCode("ph");
        StringBuffer sb = new StringBuffer("insert into RS_EXARECORDS (ID,IP,ORDERS_OBJ,ANTI_OVER,ANTI_NUM,DRUG_OVER,MEDICAL_OVER,DRUG_ORDERS_NUM,");
        sb.append("PATIENT_ID,VISIT_ID,DOCTOR_NAME,DEPT_NAME,OPERATION_DATE,BIG_DRUG_OVER,CHECK_START_TIME,AllSingleCheckTime,EXA_DATE) values('");
        sb.append(tcr.get("ID")).append("','");
        sb.append(tcr.get("IP")).append("','");
        sb.append(tcr.get("ORDERS_OBJ")).append("','");
        sb.append(tcr.get("ANTI_OVER")).append("','");
        sb.append(tcr.get("ANTI_NUM")).append("','");
        sb.append(tcr.get("DRUG_OVER")).append("','");
        sb.append(tcr.get("MEDICAL_OVER")).append("','");
        sb.append(tcr.get("DRUG_ORDERS_NUM")).append("','");
        sb.append(tcr.get("PATIENT_ID")).append("','");
        sb.append(tcr.get("VISIT_ID")).append("','");
        sb.append(tcr.get("DOCTOR_NAME")).append("','");
        sb.append(tcr.get("DEPT_NAME")).append("','");
        sb.append(tcr.get("OPERATION_DATE")).append("','");
        sb.append(tcr.get("BIG_DRUG_OVER")).append("','");
        sb.append(tcr.get("CHECK_START_TIME")).append("','");
        sb.append(tcr.get("AllSingleCheckTime")).append("','");
        sb.append(tcr.get("EXA_DATE")).append("')");
        log.debug("审查结果插入:审查时间:" + tcr.get("OPERATION_DATE") + ": 审查医生:" + tcr.get("DOCTOR_NAME") + " 审查部门 :" + tcr.get("DEPT_NAME"));
        query.execute(sb.toString());
    }
}
