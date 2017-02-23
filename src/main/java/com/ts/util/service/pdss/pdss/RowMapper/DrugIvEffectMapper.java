package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;
import com.ts.service.pdss.pdss.Cache.BeanCache;

/**
 * 对象映射类，此处进行加解密处理
 * 
 * @author Administrator
 * 
 */
public class DrugIvEffectMapper extends MapperBase implements RowMapper
{
    public DrugIvEffectMapper(List<TDrugIvEffect> list)
    {
        super(list);
    }

    public DrugIvEffectMapper()
    {

    }

    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrugIvEffect die = new TDrugIvEffect();
        die.setEFFECT_ID(rs.getString("EFFECT_ID"));
        die.setIV_CLASS_CODE1(rs.getString("IV_CLASS_CODE1"));
        die.setFIRM_ID1(rs.getString("FIRM_ID1"));
        die.setIV_CLASS_CODE2(rs.getString("IV_CLASS_CODE2"));
        die.setFIRM_ID2(rs.getString("FIRM_ID2"));
        //die.setIV_CLASS_CODE3(rs.getString("IV_CLASS_CODE3"));
        die.setRESULT_ID(rs.getString("RESULT_ID"));
        die.setREFER_INFO(rs.getString("REFER_INFO"));
        die.setREF_SOURCE(rs.getString("REF_SOURCE"));
        BeanCache.setCommonCache(die);
        CacheIt(die);
        return die;
    }
}
