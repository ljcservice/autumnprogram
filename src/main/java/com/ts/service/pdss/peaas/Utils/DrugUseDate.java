package com.ts.service.pdss.peaas.Utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DictCache;

/**
 * 计算用药的天数
 * @author Administrator
 *TODO DrugUseDate 需要使用redis 存储
 */
public class DrugUseDate {
    /* 频次基本信息  */
    private static Map<String, TCommonRecord> mapMinute = new HashMap<String, TCommonRecord>();
    /* 包装最小剂量个数  */
    private static Map<String, TCommonRecord> mapPackage = new HashMap<String, TCommonRecord>();

    /**
     * 返回频次基数
     */
    public TCommonRecord getMinute(String key) {
        return mapMinute.get(key);
    }

    /**
     * 返回包装基数
     * @param key
     * @return
     */
    public TCommonRecord getPackage(String key) {
        return mapPackage.get(key);
    }

    /**
     * 频次的基数
     */
    @SuppressWarnings("unchecked")
    public void setMapMinute() throws Exception {
        if (mapMinute.size() == 0) {
            JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
            try {
                /*
                 * table view_perform 
                 *  f_code,       1/12小时
                 *  f_Dayamount,  一天使用几次
                 *  f_DosageMinute  一次用药最小间隔  (分钟)
                 * */
                String sql = "select * from view_perform ";
                List<TCommonRecord> listTCom = (List<TCommonRecord>) query.query(sql, new CommonMapper());
                 for(TCommonRecord t : listTCom)
                 {
                     mapMinute.put(t.get("PERFORM_FREQ_DICT_NAME"), t );
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query = null;
            }
        }
    }

    /**
     * 包装基数
     * @throws Exception
    @SuppressWarnings("unchecked")
    public void setMapPackage() throws Exception {
        if (mapPackage.size() == 0) {
            JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
            try {
                String sql = " select t.drug_spec , t.units ," +
                        "t.amount_per_package ," +
                        "t.min_spec," +
                        "t.min_units " +
                        "from package_Dosage_untis t " +
                        "order by t.drug_spec ,t.amount_per_package";
                List<TCommonRecord> listTCom = query.query(sql, new CommonMapper());
                for (TCommonRecord t : listTCom) {
                    mapPackage.put(t.get("drug_spec") + t.get("units") + t.get("min_spec"), t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query = null;
            }
        }
    }
    */
    
    /**
     * 获得包装单位药品个数
     * @param drug_spec
     * @param units
     * @param min_spec
     * @return
     */
    @SuppressWarnings ("unchecked")
    public List<TCommonRecord> getMapPackage(String drug_spec ,String units , String min_spec)
    {
        // 2014-10-31 liujc 修改  min_spec 将最小规格去掉，并且取得结果中最多数量进行计算
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        try
        {
            String sql = "select t.drug_spec , t.units ,t.amount_per_package ,t.min_spec,t.min_units from package_Dosage_untis t where t.drug_spec = ? and  t.units = ? order by t.amount_per_package desc  ";// and t.min_spec = ?
            return query.query(sql, new Object[]{drug_spec, units },new CommonMapper());//min_spec
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query = null;
        }
        return null;
    }

    public DrugUseDate() {
        try {
            setMapMinute();
            // setMapPackage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 包装最小剂量个数 查询
     * select
     *   t.drug_spec , t.units ,t.amount_per_package
     * from
     *   comm.drug_price_list t
     * group by t.drug_spec,t.units,t.amount_per_package
     * @param tCom
     * @return private TCommonRecord getDrugPackageUnits(TCommonRecord tCom)
    {
    setQueryCode("HIS");
    if(this.mapPackage == null)
    this.mapPackage = new HashMap<String,TCommonRecord>();
    // drug_price_list 一个包装单位 的 总量
    String sql = "select t.drug_spec , t.units ,t.amount_per_package from Comm.drug_price_list where " +
    " unitis = '" + tCom.get("") + "' " +
    " and drug_spec = '" + tCom.get("") +
    "' group by t.drug_spec,t.units,t.amount_per_package ";
    TCommonRecord drugPriceList = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
    this.mapPackage.put(drugPriceList.get("drug_spec") + drugPriceList.get("units") , drugPriceList);
    return drugPriceList;
    }*/

    /**
     * 计算 药品可使用天数
     * @param tCom
     * @return
     */
    public int getPrescUseDate(TCommonRecord tCom)
    {
        double day = -1;
        try 
        {
            // 根据条件算一个包装里有多少最小单位
            String packageSepc = tCom.get("package_spec");
            String units       = tCom.get("units");
            String drugSpec    = tCom.get("DRUG_SPEC");
            /* 包装基数  */
            List<TCommonRecord> packageUnits = getMapPackage(packageSepc, units, drugSpec);
            //TCommonRecord tComPackageUnit =  this.getPackage(packageSepc + units + drugSpec);
            int packageUnit = packageUnits != null && packageUnits.size() > 0 ? packageUnits.get(0).getInt("amount_per_package") : 0;
            /* 频次基数  */
            TCommonRecord tComFrequency = this.getMinute((tCom.get("Frequency")));
            double frequency = tComFrequency != null ? tComFrequency.getDouble("freq_counter"):0d;
            /* 判读基数 是否存在   */
            if (packageUnit == 0 || frequency == 0) {
                return -1;
            }
            /* 使用最小用量  */
            double dosage = tCom.getDouble("DOSAGE");
            if(dosage == 0 ) {return -1;}
            /* 与用户购买个数 */
            int UsePackage = packageUnit * tCom.getInt("Amount");
            /* dosage 为用量  个数单位 默认为计算单位   */
            double Count = (double) (dosage * frequency);
            /* dosage 为用量  计算单位  */
            if("true".equals(Config.getParamValue("PDSS_DrugUseDate")))
            {
                /* 该药品的基本信息   */
                TCommonRecord drugTCom = getDrugBaseInfo(tCom.get("DRUG_CODE"), packageUnits, tCom.get("DRUG_SPEC"));
                /* 药品最小量 */
                double DosePerUnit = drugTCom != null ? drugTCom.getDouble("dose_per_unit") : 0d;
                if(DosePerUnit == 0) return -1 ;
                Count = (double) (dosage / DosePerUnit * frequency);
            }
            if(Count == 0 )
            {
            	day = -1;
            }
            else
            {
                day = UsePackage / Count;
            }
            System.out.println("用药天数:" + day + "," + tCom.get("presc_id") + ","
                    + tCom.get("drug_Code") + "," + tCom.get("Drug_name") + "," + tCom.get("Frequency")
                    + "," + tCom.get("DOSAGE") + "," + tCom.get("Amount"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 1 ;
        }
        return (int) Math.ceil(day);
    }
    
    /**
     * 根据最小单位到药品缓存中寻找
     * @param drug_code
     * @param packageUnits
     * @param drug_spec
     * @return
     */
    private TCommonRecord getDrugBaseInfo(String drug_code, List<TCommonRecord> packageUnits, String drug_spec) {
        /* 缓存信息  */
        DictCache dc = DictCache.getNewInstance();
        TCommonRecord reltT = null;
        for (TCommonRecord t : packageUnits) {
//            reltT = dc.getDrugDictKeyInfo(drug_code + t.get("MIN_UNITS") + drug_spec);
            reltT = dc.getDrugDictKeyInfo(drug_code + t.get("MIN_UNITS") + t.get("MIN_SPEC"));
            if (!"".equals(reltT.get("Drug_Code"))) return reltT;
        }
        return null;
    }
}
