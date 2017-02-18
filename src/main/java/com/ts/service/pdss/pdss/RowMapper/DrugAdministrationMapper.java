package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.service.pdss.pdss.Cache.BeanCache;

/**
 * 用药途径
 * 
 * @author liujc
 * 
 */
public class DrugAdministrationMapper extends MapperBase implements RowMapper
{
    public DrugAdministrationMapper()
    {
    }
    public DrugAdministrationMapper(List<TAdministration> list)
    {
        super(list);
    }
    @Override
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TAdministration entity = new TAdministration();
        /* 本地给药途径编码 */
        entity.setADMINISTRATION_NO_LOCAL(rs.getString("ADMINISTRATION_NO_LOCAL"));
        /* 本地给药途径名称 */
        entity.setADMINISTRATION_NAME_LOCAL(rs.getString("ADMINISTRATION_NAME_LOCAL"));
        /* ID */
        entity.setADMINISTRATION_ID(rs.getString("ADMINISTRATION_ID"));
        /* 给药途径名称 */
        entity.setADMINISTRATION_NAME(rs.getString("ADMINISTRATION_NAME"));
        /* 输入码 */
        entity.setINPUT_CODE(rs.getString("INPUT_CODE"));
        /* 用药分类 */
        entity.setADMINISTRATION_CLASS(rs.getString("ADMINISTRATION_CLASS"));
        /* 用药方法分类 */
        entity.setROUTE_MONTH(rs.getString("ROUTE_METHOD"));
        /* 用药途径分类 */
        entity.setROUTE_CLASS(rs.getString("ROUTE_CLASS"));
        /* 操作人 */
        entity.setOPER_USER(rs.getString("OPER_USER"));
        /* 操作时间 */
        entity.setOPER_TIME(rs.getString("OPER_TIME"));
        BeanCache.setAdministration(entity);
        CacheIt(entity);
        return entity;
    }

}
