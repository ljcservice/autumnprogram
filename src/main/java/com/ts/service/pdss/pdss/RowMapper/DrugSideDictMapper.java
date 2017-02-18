package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrugSideDict;
import com.ts.service.pdss.pdss.Utils.QueryUtils;

/**
 * 不良反应
 * @author liujc
 *
 */
public class DrugSideDictMapper implements RowMapper
{
    public DrugSideDictMapper()
    {
    }
    @Override
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrugSideDict entity = new TDrugSideDict();
        /* �����ô��� */
        entity.setSIDE_ID(rs.getString("SIDE_ID"));
        /* ���������� */
        entity.setDIAGNOSIS_DESC(rs.getString("DIAGNOSIS_DESC"));
        /* ���������� */
        entity.setSIDE_GROUP_ID(rs.getString("SIDE_GROUP_ID"));
        /* ��Ӧ���س̶� */
        entity.setSEVERITY(rs.getString("SEVERITY"));
        /* ���ID */
        String diagID = rs.getString("DIAGNOSIS_DICT_ID");
        entity.setDIAGNOSIS_DICT_ID(diagID);
        /* ������ */
        entity.setDiagnosis_dict_name(QueryUtils.queryDiagDict(diagID));
        /* ��������� */
        entity.setSEQ_NO(rs.getString("SEQ_NO"));
        /* ��ҩ;��ID */
        entity.setADMINISTRATION_ID(rs.getString("ADMINISTRATION_ID"));
        /* ��ҩ;��ID */
        entity.setDRUG_CLASS_ID(rs.getString("DRUG_CLASS_ID"));
        return entity;
    }
}
