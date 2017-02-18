package com.ts.service.pdss;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.persistent.Persistent4DB;
import com.ts.util.SqlBuilder;

/**
 * Author: apachexiong
 * Date: 7/26/13
 * Time: 2:37 PM
 * Package: com.hitzd.his.serivceBeans
 */
@Service("reviewResultService")
public class ReviewResultBean extends Persistent4DB implements IReviewResultServ
{
    private static Logger logger = Logger.getLogger("reviewResultService");

    @Override
    public void saveReviewResult(TCommonRecord reviewResult)
    {
        setQueryCode("HisSysManager");
        String sql = "insert into review_result"
                + "(patient_review_id,patient_id,patient_name,org_code,org_name,doctor_code,"
                + "doctor_name, dispensary_code, dispensary_name,review_error_code,review_error_message, order_date, state) values("
                + "'#patient_review_id#','#patient_id#','#patient_name#','#org_code#','#org_name#','#doctor_code#',"
                + "'#doctor_name#','#dispensary_code#','#dispensary_name#','#review_error_code#','#review_error_message#',#order_date#,'1'"
                + ")";

        sql = new SqlBuilder(sql)
                .setParam("patient_review_id",
                        reviewResult.get("patientReviewId"))
                .setParam("patient_id", reviewResult.get("patientId"))
                .setParam("patient_name", reviewResult.get("patientName"))
                .setParam("org_code", reviewResult.get("orgCode"))
                .setParam("org_name", reviewResult.get("orgName"))
                .setParam("doctor_code", reviewResult.get("doctorCode"))
                .setParam("doctor_name", reviewResult.get("doctorName"))
                .setParam("dispensary_code", reviewResult.get("dispensaryCode"))
                .setParam("dispensary_name", reviewResult.get("dispensaryName"))
                .setParam("review_error_code",reviewResult.get("reviewErrorCode"))
                .setParam("review_error_message",reviewResult.get("reviewErrorMessage"))
                .setParam("order_date", reviewResult.get("orderDate"))
                .buildSql();

        query.execute(sql);
        logger.info("病人:" + reviewResult.get("patientName") + " 已经储存一条点评信息");

    }

    @Override
    public List<TCommonRecord> getReviewResultByReviewId(String reviewRusultId)
    {
        setQueryCode("HisSysManager");
        String sql = "select * from review_result where  state='1' "
                + " and patient_review_id='#patient_review_id#'";
        sql = new SqlBuilder(sql).setParam("patient_review_id", reviewRusultId)
                .buildSql();

        List<TCommonRecord> reviewResult = (List<TCommonRecord>) query.query(sql, new CommonMapper());
        return reviewResult;
    }
}
