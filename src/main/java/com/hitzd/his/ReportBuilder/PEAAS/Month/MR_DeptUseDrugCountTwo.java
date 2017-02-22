package com.hitzd.his.ReportBuilder.PEAAS.Month;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.ReportBuilder.Interfaces.IReportBuilder;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.task.Task;
import com.hitzd.persistent.Persistent4DB;

public class MR_DeptUseDrugCountTwo extends Persistent4DB implements IReportBuilder
{
	@Override
	public String BuildReport(String ADate, Task AOwner)
	{
		Calendar cal = Calendar.getInstance();
        try
        {
        	cal.setTime(DateUtils.getDateFromString(ADate, "yyyy-MM-dd"));
            if(cal.get(Calendar.DAY_OF_MONTH) != 1) return "" ;
            cal.add(Calendar.MONTH, -1);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
            /* yyyy-MM */
            String MRDate = sf.format(cal.getTime());
            List<String> sqlsList = new ArrayList<String>();
            // 按处方数，不含外用药数据
            List <TCommonRecord> rs = this.getNoOutAntiDrugDeptCount(MRDate);
            for (TCommonRecord tcr : rs)
            {
            	String sql = "insert into MR_DEPTUSEDRUGCOUNTTWO (ORG_CODE, ORG_NAME, BELONG_YEARS, PRESC_NUM, PRESC_KJ_NUM, ORD, PRESC, OUTANTIDRUG) " +
            				 "values ('" + tcr.get("org_code") + "', '" + tcr.get("org_name") + "', '" + MRDate + "', '" + tcr.get("c") + "', '" + tcr.get("kj") + "', '" + tcr.get("ord") + "', '0', '0')";
            	sqlsList.add(sql);
            }
            // 按处方数，含外用药数据
            rs = this.getDeptUseDrugCount(MRDate);
            for (TCommonRecord tcr : rs)
            {
            	String sql = "insert into MR_DEPTUSEDRUGCOUNTTWO (ORG_CODE, ORG_NAME, BELONG_YEARS, PRESC_NUM, PRESC_KJ_NUM, ORD, PRESC, OUTANTIDRUG) " +
            				 "values ('" + tcr.get("org_code") + "', '" + tcr.get("org_name") + "', '" + MRDate + "', '" + tcr.get("c") + "', '" + tcr.get("kj") + "', '" + tcr.get("ord") + "', '0', '1')";
            	sqlsList.add(sql);
            }
            // 按处方数（人次），不含外用药数据
            rs = this.getNoAntiDeptPatientUDC(MRDate);
            for (TCommonRecord tcr : rs)
            {
            	String sql = "insert into MR_DEPTUSEDRUGCOUNTTWO (ORG_CODE, ORG_NAME, BELONG_YEARS, PRESC_NUM, PRESC_KJ_NUM, ORD, PRESC, OUTANTIDRUG) " +
            				 "values ('" + tcr.get("org_code") + "', '" + tcr.get("org_name") + "', '" + MRDate + "', '" + tcr.get("c") + "', '" + tcr.get("kj") + "', '" + tcr.get("ord") + "', '1', '0')";
            	sqlsList.add(sql);
            }
            // 按处方数（人次），含外用药数据
            rs = this.getDeptPatientUDC(MRDate);
            for (TCommonRecord tcr : rs)
            {
            	String sql = "insert into MR_DEPTUSEDRUGCOUNTTWO (ORG_CODE, ORG_NAME, BELONG_YEARS, PRESC_NUM, PRESC_KJ_NUM, ORD, PRESC, OUTANTIDRUG) " +
            				 "values ('" + tcr.get("org_code") + "', '" + tcr.get("org_name") + "', '" + MRDate + "', '" + tcr.get("c") + "', '" + tcr.get("kj") + "', '" + tcr.get("ord") + "', '1', '1')";
            	sqlsList.add(sql);
            }
            if (sqlsList != null && sqlsList.size() > 0)
            {
            	query.batchUpdate((String[])sqlsList.toArray(new String[0]));
            }
            else
            {
            	new RuntimeException("插入批量list不得为空！");
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        return "true";
	}
	
	/**
	 * 按处方数，不含外用药
	 * @param MRDate
	 * */
	@SuppressWarnings("unchecked")
	public List<TCommonRecord> getNoOutAntiDrugDeptCount(String MRDate)
	{
    	setQueryCode("PEAAS");
		StringBuffer sql = new StringBuffer();
		sql.append(" select org_code, org_name, c, kj, kj/c ord from ( ");
		sql.append(" select org_code, org_name, count(*) c, sum(decode(hasKJ, 1, 1, 0)) kj from ");
		sql.append(" presc t where t.order_type = '1' ");
		sql.append(" and to_char(to_date(t.order_date, 'yyyy-mm-dd'), 'yyyy-mm') = '").append(MRDate).append("' ");
		sql.append(" and id in (");
		sql.append(" select presc_id from presc_detail d where ");
		sql.append(" d.drug_code in (").append(this.getNoOutDrug()).append(")");
		sql.append(" and to_char(to_date(d.prescdate, 'yyyy-mm-dd'), 'yyyy-mm') = '").append(MRDate).append("' ");
		sql.append(" and d.antidrugflag = 1 ");
		sql.append(" group by presc_id) ");
		sql.append(" group by org_code, org_name) order by ord desc");
		List<TCommonRecord> deptNoOut = query.query(sql.toString(), new CommonMapper());
		List<TCommonRecord> deptUseDrug = this.getDeptUseDrugCount(MRDate);
		this.setCounter(deptUseDrug, deptNoOut);
		return deptUseDrug;
	}
	
	/**
	 * 按处方数，含外用药
	 * @param MRDate
	 * */
	@SuppressWarnings ("unchecked")
	public List<TCommonRecord> getDeptUseDrugCount(String MRDate)
	{
		setQueryCode("PEAAS");
		StringBuffer sql = new StringBuffer();
		sql.append(" select org_code, org_name, c, kj, kj/c ord from (");
		sql.append(" select org_code, org_name, count(*) c, sum(decode(hasKJ, 1, 1, 0)) kj from ");
		sql.append(" presc t  where t.order_type = '1' ");
		sql.append(" and to_char(to_date(t.order_date, 'yyyy-mm-dd'), 'yyyy-mm') = '").append(MRDate).append("' ");
		sql.append(" group by org_code, org_name) order by ord desc");
		return query.query(sql.toString(), new CommonMapper());
	}
    
    /**
     * 按处方数(人次)，不含外用药
	 * @param MRDate
     * */
    @SuppressWarnings("unchecked")
	public List<TCommonRecord> getNoAntiDeptPatientUDC(String MRDate)
    {
		setQueryCode("PEAAS");
		StringBuffer sql = new StringBuffer();
		sql.append(" select org_code, org_name,c,kj, kj/c ord  from ( ");
		sql.append(" select org_code, org_name, count(*) c , sum(decode(kj, 0, 0, 1)) kj from ( ");
		sql.append(" select org_code, org_name, count(*) c, sum(decode(hasKJ, 1, 1, 0)) kj from ");
		sql.append(" presc t  where    t.order_type = '1'  ");
		sql.append(" and to_char(to_date(t.order_date, 'yyyy-mm-dd'), 'yyyy-mm') = '").append(MRDate).append("' ");
		sql.append(" and id in (");
		sql.append(" select presc_id  from presc_detail d where ");
		sql.append(" d.drug_code in (").append(this.getNoOutDrug()).append(")");
		sql.append(" and to_char(to_date(d.prescdate, 'yyyy-mm-dd'), 'yyyy-mm') = '").append(MRDate).append("' ");
		sql.append(" and d.antidrugflag = 1 ");
		sql.append(" group by presc_id) ");
		sql.append(" group by t.org_code, t.org_name,patient_id ");
		sql.append(" ) group by org_code, org_name) order by ord desc");
		List<TCommonRecord> allOutAnti = query.query(sql.toString(), new CommonMapper());
		List<TCommonRecord> result = this.getDeptPatientUDC(MRDate);
		this.setCounter(result, allOutAnti);
		return result;
	}
    
    /**
     * 按处方数(人次)，含外用药
	 * @param MRDate
     * */
	@SuppressWarnings ("unchecked")
	public List<TCommonRecord> getDeptPatientUDC(String MRDate)
    {
		setQueryCode("PEAAS");
		StringBuffer sql = new StringBuffer();
		sql.append(" select org_code, org_name, c, kj, kj/c ord from ( ");
		sql.append(" select org_code, org_name, count(*) c, sum(decode(kj, 0, 0, 1)) kj from ( ");
		sql.append(" select org_code, org_name, count(*) c, sum(decode(hasKJ, 1, 1, 0)) kj from ");
		sql.append(" presc t where t.order_type = '1'  ");
		sql.append(" and to_char(to_date(t.order_date, 'yyyy-mm-dd'), 'yyyy-mm') = '").append(MRDate).append("' ");
		sql.append(" group by t.org_code, t.org_name, patient_id ");
		sql.append(" ) group by org_code, org_name ) order by ord desc");
		return query.query(sql.toString(), new CommonMapper());
    }
	
    /**
     * 计算外用的抗菌处方数和抗菌药物处方取出 
     * @param result
     * @param conut
     */
    private void setCounter(List<TCommonRecord> result , List<TCommonRecord> conut)
    {
        TCommonRecord deptMap = new TCommonRecord(); 
        for(TCommonRecord tx : conut)
        {
            deptMap.setObj(tx.get("org_name") + "-", tx);
        }
        for(TCommonRecord t : result)
        {
            TCommonRecord outDrug = (TCommonRecord)deptMap.getObj(t.get("org_name") + "-");
            if(outDrug == null) 
                outDrug = new TCommonRecord();
            t.set("c", String.valueOf(t.getInt("c") - outDrug.getInt("c")));
            t.set("kj", String.valueOf(t.getInt("kj") - outDrug.getInt("kj")));
            double c  = t.getDouble("c");
            double kj = t.getDouble("kj");
            if(c == 0)
            {
                t.set("ord", "0");    
            }
            else
            {
                t.set("ord", String.valueOf(kj/c));
            }
        }
    }
    
	private String getNoOutDrug()
	{
    	String result = DrugUtils.getExternalDrugNos();
    	return !"".equals(result)?result:"''";
	}
	
	@Override
	public String BuildReportWithCR(String ADate, TCommonRecord crPatInfo, Task AOwner)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogFileName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildBegin(String ADate, Task AOwner)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void buildOver(String ADate, Task AOwner)
	{
		// TODO Auto-generated method stub
	}
}