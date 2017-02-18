package com.ts.service;

import java.util.List;

import com.hitzd.DBUtils.TCommonRecord;

/**
 * Author: apachexiong
 * Date: 7/26/13
 * Time: 2:18 PM
 * Package: com.hitzd.his.serivces
 * save the review result to database hissyssmanager.review_result
 */
public interface IReviewResultServ {
    /**
     *\
     *
     *  reviewResult includes:
     *  1.patient_review_id:  on medicine record have only one id
     *  2.patient_id: key key of patient
     *  3.patient_name: the name of patient
     *  4.org_code: the key of the department of the doctor who treat the patient
     *  5.org_name: the department's name
     *  6.doctor_code:the key of the doctor
     *  7.doctor_name: the name of the doctor
     *  8.order_date: the date of treatment
     *  9.dispensary_code: the code of drugs pharmacy
     *  10. dispensary_name : the name of drugs pharmacy
     *  11 review_error_codes : the review error code list
     *  12 review_error_message : the review result of the review
     *  13 state: the state of this record

     */
    public void saveReviewResult(TCommonRecord reviewResult);


    /**
     * 通过评审id来得到评审结果
     * get the review resultCode by patient_review_id
     * @param reviewRusultId
     * @return the record
     */
    public List<TCommonRecord> getReviewResultByReviewId(String reviewRusultId);




}
