package com.ts.service.pdss.ias.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.service.pdss.ias.manager.IAntiDrugOverRateCheck;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;

/**
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class AntiDrugOverRateCheckBean extends Persistent4DB implements
		IAntiDrugOverRateCheck {

	@Override
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp) 
	{
		setQueryCode("PatientHistory");
		TAntiDrugSecurityCheckResult antiDrug = new TAntiDrugSecurityCheckResult();
		JDBCQueryImpl pdssQuery = DBQueryFactory.getQuery("PDSS");
		try
		{
			TDrug drug = (TDrug)QuerySingleUtils.queryDrug(antiDrugInp.getDrugID(),pdssQuery);
			if(drug == null)
				return antiDrug;
			String sensitCode = drug.getSENSIT_CODE();
			antiDrug.setDrug_ID(antiDrugInp.getDrugID());
			antiDrug.setDrugStandID(drug.getDRUG_ID());
			antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
			antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
			StringBuffer strSql = new StringBuffer();
			strSql.append("SELECT"); 
			strSql.append(" DRUG_SENSIT_RESULT.ANTI_NO,");
			strSql.append(" SUM(DECODE(DRUG_SENSIT_RESULT.TEST_RESULT, '1', 1,0)) / COUNT(*) rateInfo ");
			strSql.append(" FROM    DRUG_SENSIT_RESULT,GERM_TEST");  
			strSql.append(" WHERE   (DRUG_SENSIT_RESULT.TEST_NO = GERM_TEST.TEST_NO)");
			strSql.append(" and drug_sensit_result.anti_no = '").append(sensitCode).append("'");
			strSql.append(" and germ_test.ordering_dept = '" + antiDrugInp.getDoctor().getDoctorDeptID() + "'");
			strSql.append(" AND to_char(GERM_TEST.TEST_DATE,'yyyy-mm') >= '").append(DateUtils.getNowDateBeforeMonth(null)).append("'");
			strSql.append(" GROUP BY"); 
			strSql.append(" DRUG_SENSIT_RESULT.ANTI_NO ");
			Double rate = (Double)query.queryForObject(strSql.toString(), new RowMapper(){
					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException 
					{
						return  rs.getDouble("rateInfo");
					}
				});
			if(rate != null)
			{
				if(rate < 0.9d)
				{
					antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverRateCheck, "未超耐药菌百分比上月为:" + rate * 100 + "%", true,"抗菌药"));
				}
				else
				{
					antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverRateCheck, "超耐药菌百分比上月为:" + rate * 100 + "%", false,"抗菌药"));
				}
			}
			return antiDrug;
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			pdssQuery = null;
		}
		return antiDrug;
	}

}
