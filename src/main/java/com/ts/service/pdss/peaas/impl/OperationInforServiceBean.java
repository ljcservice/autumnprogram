package com.ts.service.pdss.peaas.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.peaas.Beans.TOperationDrug;
import com.ts.entity.pdss.peaas.Beans.TOperationType;
import com.ts.service.pdss.peaas.manager.IOperationInforService;

@Service
@Transactional
public class OperationInforServiceBean extends Persistent4DB implements IOperationInforService
{
    @SuppressWarnings ("unchecked")
    @Override
    public TOperationType[] getOperationTypes(String name)
    {
        setQueryCode("IAS");
        List<TOperationType> opertTypes = new ArrayList<TOperationType>();
        try
        {
            String sql = "select operation_no,operation_name,operation_remark from operation_type where operation_name like '%" + name + "%'";
            opertTypes = query.query(sql, new RowMapper()
                    {
                        @Override
                        public Object mapRow(ResultSet rs, int rowNum) throws SQLException
                        {
                            TOperationType o = new TOperationType();
                            o.setOperation_no(rs.getString("Operation_no"));
                            o.setOperation_name(rs.getString("Operation_name"));
                            o.setOperation_remark(rs.getString("Operation_remark"));
                            return o;
                        }
                    }
            );
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return (TOperationType[])opertTypes.toArray(new TOperationType[0]);
    }

    @SuppressWarnings ("unchecked")
    @Override
    public TOperationDrug[] getOperationDrugs(String operationID)
    {
        setQueryCode("IAS");
        List<TOperationDrug> operatDrug = new ArrayList<TOperationDrug>();
        try
        {
            String sql = "select oper_drug_id, oper_no, oper_drug_code, oper_drug_name, oper_drug_spec, oper_drug_form from operation_drug where oper_no = '" + operationID + "'";
            operatDrug = query.query(sql, new RowMapper()
                        {
                            @Override
                            public Object mapRow(ResultSet rs, int rowNum)throws SQLException
                            {
                                TOperationDrug  o = new TOperationDrug();
                                o.setOper_drug_id(rs.getString("Oper_drug_id"));
                                o.setOper_no(rs.getString("Oper_no"));
                                o.setOper_drug_code(rs.getString("Oper_drug_code")); 
                                o.setOper_drug_name(rs.getString("Oper_drug_name")); 
                                o.setOper_drug_spec(rs.getString("Oper_drug_spec")); 
                                o.setOper_drug_form(rs.getString("Oper_drug_form"));
                                return o;
                            }
                        }
            );
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return (TOperationDrug[]) operatDrug.toArray( new TOperationDrug[0]);
    }
}