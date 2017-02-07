package com.ts.service.ai.question.impl;


import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.question.manager.QueryQuestionManager;
import com.ts.util.PageData;
import com.ts.util.ai.AICommMethod;
import com.ts.util.ai.AIConst;

/**
 * 问题单查询
 * @ClassName: QueryQuestionService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年12月1日 上午11:09:08 
 *
 */
@Service("queryQuestionService")
public class QueryQuestionService implements QueryQuestionManager{

	@Resource(name = "sqlSessionTemplate_ai")
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;

	/**
	 * 获取列表信息
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> questionList(Page page) throws Exception{
		return (List<PageData>)dao.findForList("QuestionMapper.qlistPage", page);
	}
	
	/**
	 * 获取NLP切分结果信息
	 */
	public PageData getNLPRs(PageData pd)throws Exception{
		return (PageData)dao.findForObject("QuestionMapper.getNLPRs", pd);
	}
	/**
	 * 获取子列表信息
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> childList(PageData pd) throws Exception{
		List<PageData> list=(List<PageData>)dao.findForList("QuestionMapper.childList", pd);
//		String mts_code=pd.getString("mts_code");
//		if(AICommMethod.isNotEmpty(mts_code)){
//			String[] mts=mts_code.split(";");
//			for (int i = 0; i < mts.length; i++) {
//				String s = mts[i];
//				if(AICommMethod.isNotEmpty(s)&&!AIConst.AI_MTS_CODE_UNM.equals(s)){//此时代表MTS匹配上
//					PageData mts_r =new PageData();
//					mts_r.put("DIAG_NAME","MTS已匹配" );
//					mts_r.put("ORDERS", i+1);
//					list.add(mts_r);
//					
//				}
//			}
//		}
//		//获取子问题单列表，其中MTS已经匹配的结果需要
		return list;
	}
	
	/**
	 * 获取结果列表信息
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> resList(PageData pd) throws Exception{
		List<PageData> rsList= (List<PageData>)dao.findForList("QuestionMapper.resList", pd);
		for (PageData rs : rsList) {
			//设置标准词信息
			if(AICommMethod.isNotEmpty(rs)&&AICommMethod.isNotEmpty(rs.get("STAD_ID"))){
				String STD_CN="";
				String STD_CODE="";
				String[] stds=rs.get("STAD_ID").toString().split(",");//多个标准词用","分隔
				for (String string : stds) {
					PageData qpd=new PageData();
					qpd.put("STAD_DN_ID", string);
					PageData ontoPd=(PageData)dao.findForObject("AIMapper.getOnDiagByStd", qpd);
					if(AICommMethod.isEmpty(ontoPd)){
						STD_CN=STD_CN==""?"本体数据有误!":STD_CN+",本体数据有误!";
						STD_CODE=STD_CODE==""?"本体数据有误!":STD_CODE+",本体数据有误!";
					}else{
					STD_CN=STD_CN==""?AICommMethod.nvl(ontoPd.get("STAD_DN_CHN")):STD_CN+","+AICommMethod.nvl(ontoPd.get("STAD_DN_CHN"));
					STD_CODE=STD_CODE==""?AICommMethod.nvl(ontoPd.get("MAIN_CODE")):STD_CODE+","+AICommMethod.nvl(ontoPd.get("MAIN_CODE"));
					}
				}
				rs.put("STD_CN", STD_CN);
				rs.put("STD_CODE", STD_CODE);
			}
		}
		return rsList;
	}
	
	/**
	 * 问题单验证
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> checkList(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("QuestionMapper.checkList", pd);
	}
	
	/**
	 * 查询实时的MTS更新信息
	 * @return
	 * @throws Exception
	 */
	public PageData queryMtsInfo() throws Exception{
		return (PageData)dao.findForObject("QuestionMapper.queryMtsInfo", null);
	}
	
	/**
	 * 更新mts信息
	 * @param pd
	 * @throws Exception
	 */
	public void updateMtsInfo(PageData pd) throws Exception{
		dao.update("QuestionMapper.updateMtsInfo", pd);
	}
	
	/**
	 * 原始问题数据分页
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> originalQuestionPage(Page page) throws Exception{
		return (List<PageData>)dao.findForList("QuestionMapper.originalQuelistPage", page);
	}

	/**
	 * 重新开启问题单
	 * @param errorList
	 * @throws Exception 
	 */
	public void reOpenQuestion(List<PageData> errorList ) throws Exception {
		//有问题的子问题单id列表
		List<String> ids = (List<String>)dao.findForList("QuestionMapper.reloadQuestionIds", errorList);
		if(!CollectionUtils.isEmpty(ids)){
			//更新子问题单状态为1审
			dao.update("QuestionMapper.updateChildQbyIds", ids);
			//更新一审任务为未完成
			dao.update("QuestionMapper.updateOneCheckbyIds", ids);
			//删除二审任务
			dao.delete("QuestionMapper.deleteTwoCheckbyIds", ids);
			//删除问题单结果
			dao.delete("QuestionMapper.deleteQuestionRs", ids);
			//删除问题单历史结果
			dao.delete("deleteQuestionHisRs", ids);
		}
	}
	
	/**
	 * 更新原始问题单
	 * @param pd
	 * @throws Exception
	 */ 
	public void updateOriginalQue(String  BATCH_NUMBER) throws Exception{
		dao.update("QuestionMapper.updateOriginalQue", BATCH_NUMBER);
	}
}
