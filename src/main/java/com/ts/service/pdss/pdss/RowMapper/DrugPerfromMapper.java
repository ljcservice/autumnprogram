package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrugPerformFreqDict;
import com.ts.service.pdss.pdss.Cache.BeanCache;

/**
 * 医嘱执行频率
 * @author liujc
 *
 */
public class DrugPerfromMapper extends MapperBase implements RowMapper
{
    
    public DrugPerfromMapper()
    {
    }
    public DrugPerfromMapper(List<TDrugPerformFreqDict> list)
    {
        super(list);
    }

    @Override
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrugPerformFreqDict entity = new TDrugPerformFreqDict();
        entity.setPERFORM_FREQ_DICT_ID(rs.getString("PERFORM_FREQ_DICT_ID"));
        entity.setPERFORM_FREQ_DICT_NAME(rs.getString("PERFORM_FREQ_DICT_NAME"));
        entity.setFREQ_COUNTER(rs.getString("FREQ_COUNTER"));
        entity.setFREQ_INTERVAL(rs.getString("FREQ_INTERVAL"));
        entity.setFREQ_INTERVAL_UNITS(rs.getString("FREQ_INTERVAL_UNITS"));
        entity.setPERFORM_FREQ_DICT_NO_LOCAL(rs.getString("PERFORM_FREQ_DICT_NO_LOCAL"));
        entity.setPERFORM_FREQ_DICT_NAME_LOCAL(rs.getString("PERFORM_FREQ_DICT_NAME_LOCAL"));
        entity.setOPER_USER(rs.getString("OPER_USER"));
        entity.setOPER_TIME(rs.getString("OPER_TIME"));
        BeanCache.setPerformFreqDict(entity);
        CacheIt(entity);
        return entity;
    }
}
