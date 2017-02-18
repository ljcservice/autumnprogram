package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TAllergIngrDrug;

/**
 * 药物成分、药敏、药物分类与药物对照字典
 * 
 * @author liujc
 * 
 */
public class DrugAllergIngrMapper extends MapperBase implements RowMapper
{

    public DrugAllergIngrMapper(List<TAllergIngrDrug> list)
    {
        super(list);
    }

    public DrugAllergIngrMapper()
    {
    }

    @Override
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TAllergIngrDrug entity = new TAllergIngrDrug();
        entity.setALLERG_INGR_DRUG_ID(rs.getString("ALLERG_INGR_DRUG_ID"));
        entity.setDRUG_ALLERGEN_ID(rs.getString("DRUG_ALLERGEN_ID"));
        entity.setDRUG_CLASS_ID(rs.getString("DRUG_CLASS_ID"));
        entity.setINPUT_CODE(rs.getString("INPUT_CODE"));
        entity.setDRUG_ALLERGEN_NAME(rs.getString("DRUG_ALLERGEN_NAME"));
//        if(list!=null)
        //BeanCache.setAllergIngrDrug(entity);
        CacheIt(entity);
        return entity;
    }

}
