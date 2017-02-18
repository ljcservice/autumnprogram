package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrugDiagInfo;
import com.ts.service.pdss.pdss.Cache.BeanCache;

public class DrugDiagInfoMapper extends MapperBase implements RowMapper
{
    public DrugDiagInfoMapper()
    {
    }

    public DrugDiagInfoMapper(List<TDrugDiagInfo> list)
    {
        super(list);
    }

    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrugDiagInfo entity = new TDrugDiagInfo();
        entity.setDRUG_DIAG_INFO_ID(rs.getString("DRUG_DIAG_INFO_ID"));
        String diagID = rs.getString("DIAGNOSIS_DICT_ID");
        entity.setDIAGNOSIS_DICT_ID(diagID);
//        entity.setDiagnosis_dict_name(QueryUtils.queryDiagDict(diagID));
        entity.setDiagnosis_dict_name("");
        entity.setDRUG_DIAG_REL_ID(rs.getString("DRUG_DIAG_REL_ID"));
        entity.setSEQ_ID(rs.getString("SEQ_ID"));
        entity.setINTER_INDI(rs.getString("INTER_INDI"));
        entity.setDIAG_DESC(rs.getString("DIAG_DESC"));
        entity.setDRUG_REF_SOURCE(rs.getString("DRUG_REF_SOURCE"));
        entity.setCONTRAIND_ID(rs.getString("CONTRAIND_ID"));
        BeanCache.setDrugDiagInfo(entity);
        CacheIt(entity);
        return entity;
    }
}
