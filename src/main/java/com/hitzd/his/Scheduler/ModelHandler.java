package com.hitzd.his.Scheduler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.hitzd.Annotations.MHPerformProp;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.SysLog.SysLog4Dcdt;
import com.hitzd.his.report.utils.JillClassLoader;

/**
 * 组件管理者
 * 
 * @author jingcong
 * 
 */
final public class ModelHandler extends SysLog4Dcdt implements Runnable
{
    /* 组件组号 */
    private String       mhGroupCode = "";

    /* 可选写入参数 */
    private List<Object> writerParam = new ArrayList<Object>();

    public List<Object> getWriterParam()
    {
        return writerParam;
    }

    public void setWriterParam(List<Object> writerParam)
    {
        this.writerParam = writerParam;
    }

    /**
     * 组件号。
     */
    public ModelHandler(String aMHGroupCode)
    {
        this.mhGroupCode = aMHGroupCode;
    }

    @Override
    public void run()
    {
        PerformIt();
    }

    /**
     * 执行处理该方法
     */
    @SuppressWarnings ({ "unchecked", "unused" })
    public Object PerformIt()
    {
        JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
        CommonMapper cmr = new CommonMapper();
        try
        {
            if ("".equals(this.mhGroupCode))
                throw new RuntimeException("组号为空！无法找到可用组件 ");
            String sql = "select * from modelHandler t where t.mh_action = 1 and t.mh_groupCode = '"
                    + this.mhGroupCode + "'";
            List<TCommonRecord> list = (List<TCommonRecord>) query.query(sql,
                    cmr);
            if (list == null || list.size() == 0)
                throw new RuntimeException("[" + sql + "]该sql未能检索可用的组件");
            for (TCommonRecord t : list)
            {
                /* 参数 */
                Object[] param = new Object[] {};
                // 组件类路径
                String mhClassPath = t.get("mh_ClassPath");
                // 组件Code
                String mhCode = t.get("mh_Code");
                // 组件名称
                String mhCaption = t.get("mh_Caption");
                // 组件级别
                String mhLevel = t.get("mh_Level");
                if ("".equals(mhClassPath))
                    throw new RuntimeException(mhCode + "," + mhCaption + ":没有登记组件路径!");

                Class clazz = JillClassLoader.loadClass(mhClassPath);
                Method performMethod = null;
                for (Method m : clazz.getMethods())
                {
                    if (m.isAnnotationPresent(MHPerformProp.class))
                    {
                        performMethod = m;
                        /* 处理已经注册好的参数 */
                        MHPerformProp mh = m.getAnnotation(MHPerformProp.class);
                        // 获得注册参数 
                        Class[] clazzParam = mh.MethodParam();
                        if (this.writerParam != null && this.writerParam.size() > 0 && clazzParam != null &&  this.writerParam.size() == clazzParam.length )
                        {
                            param = (Object[]) this.writerParam.toArray(new Object[0]);
                        }
                        else
                        {
                            new RuntimeException("输入参数与实现类参数个数不一致！");
                            if(clazzParam != null &&  clazzParam.length > 0 )
                            {
                                param = new Object[clazzParam.length];
                                for (int i =  0 ; i < clazzParam.length ; i++)
                                {
                                   Class c = clazzParam[i];
                                   param[i] =  this.writerParam.size() >= (i+1) ? this.writerParam.get(i):c.newInstance();
                                }
                            }
                        }
                        break;
                    }
                }
                Object obj = clazz.newInstance();
                Object result = performMethod.invoke(obj, param);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
            cmr = null;
        }

        return new Object();
    }

//    private static Object setParam4Type(Class cType,String value)
//    {
//        
//
//        
//    }
    
    
}
