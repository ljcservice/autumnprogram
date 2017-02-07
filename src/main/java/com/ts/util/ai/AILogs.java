package com.ts.util.ai;

import org.apache.ibatis.session.SqlSession;

import com.ts.dao.DaoSupportAi;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;

/**
 * 记录AI日志
 * @ClassName: AILogs 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月25日 下午4:03:21 
 *
 */
public class AILogs {
	
	/**
	 * 操作日志 
	 */
	public static Object saveOpLogs(String OP_DESC,DaoSupportAi dao,SqlSession sqlSession) throws Exception{
		PageData pd=new PageData();
		pd.put("OP_DESC", OP_DESC);
		pd.put("UPD_USER",Jurisdiction.getCurrentUser().getUSER_ID());
		if(dao==null)
			return sqlSession.insert("AIMapper.saveOpLog", pd);
		else 
			return dao.save("AIMapper.saveOpLog", pd); 
	} 
	
	/**
	 * 任务日志
	 */ 
	public static Object saveTaskLogs(PageData pd, DaoSupportAi dao,SqlSession sqlSession) throws Exception{
		pd.put("UPD_USER",Jurisdiction.getCurrentUser().getUSER_ID());
		if(dao==null)
			return sqlSession.insert("AIMapper.savaTaskLog", pd);
		else 
			return  dao.save("AIMapper.savaTaskLog", pd); 
	} 
	
}
