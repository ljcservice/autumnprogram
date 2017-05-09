package com.hitzd.Transaction;



import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.springBeanManager.SpringBeanUtil;


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
//        DataSourceTransactionManager d = new DataSourceTransactionManager();
//        d.setDataSource(DBQueryFactory.getDataSource(TransactionSource));
//        this.setTransactionManager(d);
        // 2014-10-21 liujc 修改 事务bean不从spring 容器中获得    
        
        ts = "transactionManager" +  ("".equals(TransactionSource) ? "":"_" + TransactionSource );
        this.setTransactionManager((PlatformTransactionManager)SpringBeanUtil.getBean(ts));
    }
    
    public Object execute(TransaCallback transalCallback) throws TransactionException
    {
        Object obj = super.execute(transalCallback);
        return obj;
    }
    
}
