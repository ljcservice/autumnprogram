package com.ts.service.pdss.peaas.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.peaas.Beans.TPrescDetailBean;
import com.ts.entity.pdss.peaas.Beans.TPrescMasterBean;
import com.ts.entity.pdss.peaas.Beans.TPrescOutpMrBean;
import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;
import com.ts.service.pdss.peaas.RowMapper.PrescDetailBeanRM;
import com.ts.service.pdss.peaas.RowMapper.PrescMasterBeanRM;
import com.ts.service.pdss.peaas.RowMapper.PrescOutpMrBeanRM;
import com.ts.service.pdss.peaas.RowMapper.PrescPatMasterBeanRM;
import com.ts.service.pdss.peaas.manager.IPrescCheckerService;

/**
 * 处方功能服务
 * @author Administrator
 *
 */
@Service
public class PrescCheckerServiceBean extends Persistent4DB implements IPrescCheckerService
{
    /* 保存病人的就诊信息  key = patient_id + currDate */
//    public Map<String,TPrescPatMasterBean> mapPMBS = new HashMap<String,TPrescPatMasterBean>();

    @SuppressWarnings ({ "unchecked" })
    @Override
    public TPrescPatMasterBean getUsePrescDetail(String patient_id,Integer SelDay, String[] back)
    {
//        patient_id = "J005538";
        if(patient_id == null && "".equals(patient_id)) return null;
        TPrescPatMasterBean ppmb = new TPrescPatMasterBean();
        List<TPrescMasterBean> listPrescMs = new ArrayList<TPrescMasterBean>();
        List<TPrescOutpMrBean> listPrescMR = new ArrayList<TPrescOutpMrBean>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ADate         = sdf.format(new Date());
        PrescDetailBeanRM pdbRM     = new PrescDetailBeanRM();
        PrescMasterBeanRM pmbRM     = new PrescMasterBeanRM();
        PrescOutpMrBeanRM pomRM     = new PrescOutpMrBeanRM();
        PrescPatMasterBeanRM ppmbRM = new PrescPatMasterBeanRM();
        setQueryCode("HIS");
        try
        {
            /* 病人主记录 */
            String sql  = "select patient_id, inp_no, name, name_phonetic, sex, date_of_birth, birth_place, citizenship, nation, id_no, identity, charge_type, " +
            		"unit_in_contract, mailing_address, zip_code, phone_number_home, phone_number_business, next_of_kin, relationship, next_of_kin_addr, next_of_kin_zip_code, next_of_kin_phone, last_visit_date, vip_indicator, create_date, operator, insurance_id, insurance_card_no, insurance_id_no from MEDREC.PAT_MASTER_INDEX where patient_id = '" + patient_id + "'";
            ppmb = (TPrescPatMasterBean)query.queryForObject(sql, ppmbRM);
            /* 处方信息 */
            sql  = "select PATIENT_ID,VISIT_DATE,VISIT_NO,SERIAL_NO,ORDERED_BY,DOCTOR,ORDER_DATE from outpdoct.outp_orders  " +
            		        "where to_char(VISIT_DATE,'yyyy-mm-dd') = '" + ADate + "' and PATIENT_ID = '" + patient_id + "' order by VISIT_DATE ";
            listPrescMs = query.query(sql, pmbRM);
            for(TPrescMasterBean p : listPrescMs)
            {
                List<TPrescDetailBean> pdbs = new ArrayList<TPrescDetailBean>();
                /* 处方明细 */
                // ,FREQ_DETAIL,GETDRUG_FLAG
                sql = " select VISIT_DATE,VISIT_NO,SERIAL_NO,PRESC_NO,ITEM_NO,ITEM_CLASS,DRUG_CODE,DRUG_NAME,DRUG_SPEC,FIRM_ID,UNITS,AMOUNT,DOSAGE,DOSAGE_UNITS,ADMINISTRATION,FREQUENCY,PROVIDED_INDICATOR,COSTS,CHARGES,CHARGE_INDICATOR,DISPENSARY,REPETITION,ORDER_NO,ORDER_SUB_NO from outpdoct.OUTP_PRESC " +
                    " where to_char(visit_date,'yyyy-mm-dd') = '" + ADate + "' and SERIAL_NO = '" + p.getSERIAL_NO() + "' order by VISIT_DATE,ITEM_NO";
                pdbs = query.query(sql, pdbRM);
                p.setPdbs((TPrescDetailBean[])pdbs.toArray( new TPrescDetailBean[0]));
            }
            sql = "select * from outpdoct.outp_mr t where t.PATIENT_ID = '" + patient_id + "' and to_char(visit_date,'yyyy-mm-dd') = '" + ADate + "'" ;
            listPrescMR = query.query(sql, pomRM);
            /* 门诊处方信息 */
            ppmb.setPmbs((TPrescMasterBean[])listPrescMs.toArray(new TPrescMasterBean[0]));
            /* 门诊诊断信息 */
            ppmb.setPombs((TPrescOutpMrBean[])listPrescMR.toArray(new TPrescOutpMrBean[0]));
            //mapPMBS.put(patient_id + "-" + ADate, ppmb);
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            pdbRM  = null;
            pmbRM  = null;
            pomRM  = null;
            ppmbRM = null;
        }
        return ppmb;
    }
}
