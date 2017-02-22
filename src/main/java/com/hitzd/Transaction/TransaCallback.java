package com.hitzd.Transaction;


import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.hitzd.DBUtils.TCommonRecord;

/**
 * 事务回调方法  用于回滚 
 *  尽量添加数据连接 
 * @author Administrator
 *
 */
public abstract class TransaCallback implements TransactionCallback
{
    private String strSqls = new String();
    private TCommonRecord tranParm = new TCommonRecord(); 
    public TransaCallback(TCommonRecord listCom)
    {
        this.setTranParm(listCom);
    }

    public TransaCallback() { }
    
    @Override
    public Object doInTransaction(TransactionStatus status)
    {
        try
        {
            this.ExceuteSqlRecord(); 
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
        finally
        {
            if(strSqls != null && strSqls.length() > 0)
            {
//                TransactionTemplate tt = new TransactionTemplate((PlatformTransactionManager)SpringBeanUtil.getBean("txManagerHisSysManager"));
//                JDBCQueryImpl logSql = DBQueryFactory.getQuery("");
//                TransaCallback td = new TransaCallback(logSql)
//                {
//                    @Override
//                    @SuppressWarnings ("unused")
//                    public void ExceuteSqlRecord()
//                    {
//                        for(String sql :strSqls)
//                        {
//                            String s = "insert into logSql(id , sqlrecord , source,recordDate ) values()";
//                            query.update(s);
//                        }
//                    }
//                };
//                tt.execute(td);
//                logSql = null;
                throw new RuntimeException();
            }            
        }
        return null;
    }
    
    /**
     * 用户执行sql 方法  
     * @return
     */
    abstract public void ExceuteSqlRecord();

	public void setTranParm(TCommonRecord tranParm) {
		this.tranParm = tranParm;
	}

	public TCommonRecord getTranParm() {
		return tranParm;
	}
    
}
