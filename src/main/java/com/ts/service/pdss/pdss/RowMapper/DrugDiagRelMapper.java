package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.service.pdss.pdss.Cache.BeanCache;

public class DrugDiagRelMapper extends MapperBase implements RowMapper
{

    public DrugDiagRelMapper(List<TDrugDiagRel> list)
    {
        super.list = list;
    }

    public DrugDiagRelMapper()
    {
    }

    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrugDiagRel drugDiagRel = new TDrugDiagRel();
        drugDiagRel.setDRUG_DIAG_REL_ID(rs.getString("DRUG_DIAG_REL_ID"));
        drugDiagRel.setCONTRAIND_ID(rs.getString("CONTRAIND_ID"));
        drugDiagRel.setDRUG_CLASS_ID(rs.getString("DRUG_CLASS_ID"));
        drugDiagRel.setADMINISTRATION_ID(rs.getString("ADMINISTRATION_ID"));
        drugDiagRel.setINDEX_ID(rs.getString("INDEX_ID"));
        //if(list!=null)
        BeanCache.setDrugDiagRel(drugDiagRel);
        CacheIt(drugDiagRel);
        return drugDiagRel;
    }
}
