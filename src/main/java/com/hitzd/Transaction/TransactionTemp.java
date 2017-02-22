package com.hitzd.Transaction;



import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import com.hitzd.Factory.DBQueryFactory;


/**
 * 事务模板类
 * @author Administrator
 *
 */
public class TransactionTemp extends TransactionTemplate
{
    private static final long serialVersionUID = 1L;
    private String ts = null;
    public TransactionTemp(String TransactionSource)
    {
        DataSourceTransactionManager d = new DataSourceTransactionManager();
        d.setDataSource(DBQueryFactory.getDataSource(TransactionSource));

        // 2014-10-21 liujc 修改 事务bean不从spring 容器中获得        
//        ts = "txManager" + TransactionSource;
//        if(ts == null || "".equals(ts))
//            throw new RuntimeException();
        //this.setTransactionManager((PlatformTransactionManager)SpringBeanUtil.getBean(ts));
        this.setTransactionManager(d);
    }
    
    public Object execute(TransaCallback transalCallback) throws TransactionException
    {
        Object obj = super.execute(transalCallback);
        return obj;
    }
    
}
