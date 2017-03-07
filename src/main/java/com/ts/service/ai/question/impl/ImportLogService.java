package com.ts.service.ai.question.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.impl.AIService;
import com.ts.service.ai.question.manager.ImportLogManager;
import com.ts.util.Jurisdiction;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.ai.AICommMethod;
import com.ts.util.ai.AIConst;
import com.ts.util.ai.AILogs;

import net.sf.json.JSONObject;

/**
 * 导入日志
 * @ClassName: ImportLogService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy
 * @date 2016年10月28日 下午2:00:29 
 *
 */
@Service("importLogService")
public class ImportLogService implements ImportLogManager{

	@Resource(name = "sqlSessionTemplate_PDSS")
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;

	@Resource(name="aiService")
	private AIService aiService;
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 获取列表信息
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> logList(Page page) throws Exception{
		return (List<PageData>)dao.findForList("ImportLogMapper.loglistPage", page);
	}
	
	/**
	 * 根据文件名称获取日志信息
	 */
	public PageData getLogByFileName(String FILE_NAME) throws Exception{
		
		 return (PageData)dao.findForObject("ImportLogMapper.getLogByFileName", FILE_NAME);
	}
	/**
	 * 导入问题单数据到数据库
	 * @Title: importData2Base 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param fileName
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public String importData2Base(String filePath,String fileName,Logger logger)throws Exception{
		String flag="success";
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		//批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		try{
			AILogs.saveOpLogs("问题单文件"+fileName+"导入数据库",null, sqlSession);//写操作日志
			logger.info("导入文件"+fileName+"开始");
			//写入导入日志信息
			PageData logpd=new PageData();
			logpd.put("FILE_NAME", fileName);
			logpd.put("STATUS", 0);
			logpd.put("SUC_COUNT", 0);
			logpd.put("FAIL_COUNT", 0);
			logpd.put("IMPORT_USER", Jurisdiction.getCurrentUser().getUSER_ID());
			sqlSession.insert("ImportLogMapper.addLog", logpd);
			logger.info("导入文件"+fileName+"写入日志");
			
			//写入原始数据表	
			Collection<PageData> Cpd=readTxt(filePath, fileName,logger,logpd);
			if(Cpd==null||Cpd.isEmpty())
			{
				flag="读取文件信息失败："+logpd.get("error")==null?"文件不包含有效格式的数据!":logpd.get("error").toString();
				return flag;
			}
			
			int sucCount=0;
			int failCount=0;
			for (PageData pd : Cpd) {
				
				pd.put("IMPORT_ID", Integer.valueOf((logpd.get("IMPORT_ID").toString())));	//日志ID
				
				logger.info("导入数据信息：IMPORT_ID:"+logpd.get("IMPORT_ID")+";ORIGIN_ID:"+pd.get("ORIGIN_ID")
				+";MTS_RULE_ID:"+pd.get("MTS_RULE_ID")+";CLM_ID:"+pd.get("CLM_ID")+";CLM_TYPE:"+pd.get("CLM_TYPE")
				+";O_DIAG_NAME"+pd.get("O_DIAG_NAME")+";NLP_DIAG_NAME"+pd.get("NLP_DIAG_NAME")+
				";MTS_DIAG_CODE"+pd.get("MTS_DIAG_CODE")+";CREATE_MAN"+pd.get("CREATE_MAN")+"操作人："+Jurisdiction.getUsername());
				
				//判断主问题单是否存在诊断名称，存在则将问题单的ID取出 否则将插入到问题单中
				int q_id=0;
				PageData res=(PageData)dao.findForObject("ImportLogMapper.checkQ", pd);
				if(res==null)
				{
					pd.put("STATUS", 0);//问题单的状态
					sqlSession.insert("ImportLogMapper.addQ", pd);
					q_id= Integer.valueOf((pd.get("Q_ID").toString()) );
					logger.info("问题单数据"+pd.get("O_DIAG_NAME")+"写入成功");
				}else
				{
					q_id= Integer.valueOf((res.get("Q_ID").toString()) );
				}
				
				if(q_id == 0){
					logger.error("问题单数据"+pd.getString("O_DIAG_NAME")+"写入失败!");
					failCount++;
					continue;
				}else{
					
					pd.put("Q_ID", q_id);
				}
				sqlSession.insert("ImportLogMapper.addO",pd);//保存原始数据
				sucCount++;
				logger.info("原始数据"+pd.get("O_DIAG_NAME")+"写入成功");
			}
			//修改导入的日志
			logpd.put("SUC_COUNT", sucCount);
			logpd.put("FAIL_COUNT", Integer.valueOf(logpd.get("FAIL_COUNT").toString())+failCount);//失败的数量，包含数据列异常及数据库操作异常
			logpd.put("STATUS", 1);//导入成功
			sqlSession.update("ImportLogMapper.editLog",logpd);
			
			logger.info("导入文件"+fileName+",成功数量："+sucCount+",失败数量："+logpd.get("FAIL_COUNT"));
			flag=AICommMethod.isEmpty(logpd.get("error"))?flag:logpd.get("error").toString();
			sqlSession.flushStatements();
			sqlSession.commit();
			sqlSession.clearCache();
	}catch(Exception e){
		logger.error("导入文件"+fileName+"失败:"+e.getMessage());
		flag="导入文件"+fileName+"失败:"+e.getMessage();
		sqlSession.rollback();
		sqlSession.clearCache();
	}finally{
		logger.info("导入文件"+fileName+"结束");
		sqlSession.close();
	}
		return flag;
		
	}
	/**
	 * @param filepath //文件路径
	 * @param filename //文件名
	 * @return list
	 */
	public Collection readTxt(String filepath, String filename,Logger logger,PageData logPd) {
		Map<Object ,PageData> varMap=new HashMap<>();
		int fail_count=0;//失败的数量
		try {
			int task_type_id=0;//任务类型
			int task_type_child_id=0;//任务子类型
			String BATCH_NUMBER="";//批次号
			int row=0;//行号
			InputStreamReader read = new InputStreamReader (new FileInputStream(new File(filepath, filename)),"utf-8");
			//FileReader fr = new FileReader(filepath+"//"+ filename);
			BufferedReader br = new BufferedReader(read);
			String line = null;
			line = br.readLine();//读取一行
			while(line != null){
				if(row==0){
				    if(line.startsWith("\uFEFF")){  
				        line = line.replace("\uFEFF", "");  
				       }  
					String q_type= line;//问题单类型标志 
					if(q_type!=null&&q_type.trim().equals("诊断")){
						task_type_id=AIConst.AI_TASK_DIAG;
						task_type_child_id=AIConst.AI_TASK_CHILD_NLP;//诊断的子类型都为NLP审核
						logger.info("导入文件"+filename+"的类型为诊断!");
					}else{
						logger.info("导入文件"+filename+"不包含类型信息!");
						logPd.put("error", "文件模板信息有误! ");
						return null;
					}
				}else if(row==1){
					BATCH_NUMBER=line;//问题单列名称
					if(AICommMethod.isEmpty(BATCH_NUMBER)||BATCH_NUMBER.indexOf("#q&#")>-1){
						logger.info("导入文件"+filename+"不包含批次号信息!");
						logPd.put("error", "文件不包含批次信息，请添加批次重新导入! ");
						return null;
					}
					logPd.put("BATCH_NUMBER", BATCH_NUMBER);
					
				}else{
					//数据行开始
					PageData varpd = new PageData();
					String str = line;
					int flag_line=1;//当前行数据有效性 1有效0无效
					AICommMethod.counter=0;
					int  str_count=AICommMethod.stringNumbers(str, "#q&#");
					if(str_count!=5)
					{
						String errorInfo="第"+row+"行数据包含的列数量不合法，跳过当前行数据导入";
						logger.error(errorInfo);
						logPd.put("error", AICommMethod.isEmpty(logPd.get("error"))?errorInfo:logPd.get("error")+"</br>"+errorInfo);
						fail_count++;
					}else{
						String[] abc = str.split("#q&#");
						if(abc.length<=4)
						{
							String errorInfo="第"+row+"行数据不包含原始诊断名称信息，跳过当前行数据导入";
							logger.error(errorInfo);
							logPd.put("error", AICommMethod.isEmpty(logPd.get("error"))?errorInfo:logPd.get("error")+"</br>"+errorInfo);
							fail_count++;
						}else{
						for (int i = 0; i < abc.length; i++) {
							
							int flag_for=0;//结束for循环的标志
							
							//校验数据结构、数据类型
							String val=abc[i];
							switch (i) {
							case 0://数据来源
								//校验数据来源是否在字典表中
								if(val!=null&&val!=""&&val.length()!=0){
									varpd.put("DT_ID", AIConst.AI_DICT_ORIGIN);
									varpd.put("D_KEY", val);
									boolean flag=aiService.checkInDict(varpd);
									if(!flag){
										//此处校验由于目前的数据不合法所以讲数据来源先不进行校验，直接将值设置为10001开封数据
										val="10001";
	//									logger.error("数据来源：第"+row+"行第"+(i+1)+"列数据"+val+"不合法，跳过当前行数据导入");
	//									flag_for=1;
									}
								}
								varpd.put("ORIGIN_ID", val);	//来源ID
								break;
							case 1://MTS匹配类型
								//校验匹配规则是否在字典表中
								if(val!=null&&val!=""&&val.length()!=0){
									varpd.put("DT_ID", AIConst.AI_DICT_MTS_MATCH_TYPE);
									varpd.put("D_KEY", val);
									boolean flag=aiService.checkInDict(varpd);
									if(!flag){
										//此处校验由于目前的数据不合法所以讲数据来源先不进行校验，直接将值设置为24301词词匹配
										val="24301";
	//									logger.error("MTS匹配规则第"+row+"行第"+(i+1)+"列数据"+val+"不合法，跳过当前行数据导入");
	//									flag_for=1;
									}
								}
								varpd.put("MTS_RULE_ID", val);
								break;
							case 2:
								varpd.put("CLM_ID", val);
								break;
							case 3:
								varpd.put("CLM_TYPE", val);
								break;
							case 4://原始诊断名称
								//校验非空
								if(AICommMethod.isEmpty(val)){
									String errorInfo="原始诊断名称：第"+row+"行第"+(i+1)+"列数据"+val+"为空，不合法，跳过当前行数据导入";
									logger.error(errorInfo);
									logPd.put("error", AICommMethod.isEmpty(logPd.get("error"))?errorInfo:logPd.get("error")+"</br>"+errorInfo);
									flag_for=1;
									break;
								}
								varpd.put("O_DIAG_NAME", val);
								break;
							case 5://MTS匹配结果，此列或者为空或者全部的切词及匹配结果对应的json格式
								String NLP_DIAG_NAME="";
								String MTS_DIAG_CODE="";
								if(val!=null&&val!=""&&val.length()!=0){
									
									JSONObject jsonObject=new JSONObject();
									try {
										jsonObject = JSONObject.fromObject(val);
									} catch (Exception e) {
										String errorInfo="原始诊断名称：第"+row+"行第"+(i+1)+"列数据"+val+"不为Json格式数据，不合法，跳过当前行数据导入";
										logger.error(errorInfo);
										logPd.put("error", AICommMethod.isEmpty(logPd.get("error"))?errorInfo:logPd.get("error")+"</br>"+errorInfo);
										flag_for=1;
										break;
									}
							        Iterator<String> iterator = jsonObject.keys();
							        while(iterator.hasNext()){
							                String key = (String) iterator.next();
							                String value = jsonObject.getString(key);
							                if(value.toLowerCase().equals("none")){//未匹配上的结果
							                	value=AIConst.AI_MTS_CODE_UNM;
							                }else{
							                	value=value.replaceAll("@#&", "");//匹配上的结果
							                }
							                if(AICommMethod.isEmpty(NLP_DIAG_NAME)){
							                	NLP_DIAG_NAME=key;
							                	MTS_DIAG_CODE=value;
							                }else{
							                	NLP_DIAG_NAME=NLP_DIAG_NAME+";"+key;
							                	MTS_DIAG_CODE=MTS_DIAG_CODE+";"+value;
							                }
							        }
							        
								}
								varpd.put("NLP_DIAG_NAME", NLP_DIAG_NAME);
								varpd.put("MTS_DIAG_CODE", MTS_DIAG_CODE);
								break;
							default:
								break;
							}
							if(flag_for==1)//结束for循环
							{
								flag_line=0;//当前行数据有效性 :0无效
								break;//结束for循环
							}
						}
						if(flag_line==1){
							varpd.put("TASK_TYPE_ID",task_type_id);
							varpd.put("TASK_TYPE_CHILD_ID",task_type_child_id);
							varpd.put("BATCH_NUMBER", BATCH_NUMBER);
							varpd.put("CREATE_MAN", Jurisdiction.getCurrentUser().getUSER_ID());
							varpd.put("UPDATE_MAN", Jurisdiction.getCurrentUser().getUSER_ID());
							//校验当前的问题单是否已经包含在列表中，是则不放人否则放入列表
							Object key=varpd.get("O_DIAG_NAME");
							if(varMap.containsKey(key)){//已经包含当前的问题单，判断是否之前一个没有MTS匹配值 ，当前的有值
								PageData old=varMap.get(key);
								if(old.get("NLP_DIAG_NAME")==null && varpd.get("NLP_DIAG_NAME")!=null ){
									varMap.put(key, varpd);
									logger.info("当前数据"+key+"是重复数据，但是包含匹配结果，则替换掉前一个数据!");
								}
								logger.info("当前数据"+key+"是重复数据，不进行导入!");
							}else{
								varMap.put(key, varpd);
							}
						}else{
							fail_count++;
						}
					}
					}
				}
				
			    line = br.readLine();
			    row++;
			} 

			read.close();
			br.close();
			
		} catch (Exception e) {
			logger.error(e);
		}
		logPd.put("FAIL_COUNT", fail_count);
		return varMap.values();
	}

	
	public String deleteImport(PageData pd) throws Exception {
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		//批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		
		String res ="success";
		
		try{
			AILogs.saveOpLogs("问题单文件撤销导入",null, sqlSession);//写操作日志
			logger.info("问题单文件撤销导入开始，导入日志ID："+pd.getString("IMPORT_ID")+"开始");
			
			//首先校验当然的导入下的问题单是否能够删除，即问题单的状态为初始状态0
			List<PageData> list=(List<PageData>)dao.findForList("ImportLogMapper.checkDeleteImport", pd);
			if(list.isEmpty()){
				//首先获取问题单信息
				List<PageData> q_list=(List<PageData>)dao.findForList("ImportLogMapper.selectDeleteQ", pd);
				//删除原始数据
				sqlSession.delete("ImportLogMapper.deleteOByImportLog", pd);
				logger.info("问题单文件撤销导入,删除原始数据");
				
				//删除问题单，判断问题单是否也属于其他的原始数据，是则不需要删除否则需要删除
				if(AICommMethod.isNotEmpty(q_list)){
					sqlSession.delete("ImportLogMapper.batchDeleteQ", q_list);
					logger.info("问题单文件撤销导入,删除问题单数据");
				}
				
				//更新导入日志的状态为'已撤销'
				pd.put("STATUS", 3);
				sqlSession.update("ImportLogMapper.editLog", pd);
				logger.info("问题单文件撤销导入,更新问题单的导入日志的状态为'已撤销'");
			}else{
				res="不能够撤销导入，原因：问题单正在处理中，问题单的诊断名称";
				for (PageData pageData : list) {
					res=res+"【"+pageData.getString("O_DIAG_NAME")+"】";
				}
				logger.error("问题单文件撤销导入失败，失败原因："+res);
			}
			
			sqlSession.flushStatements();
			sqlSession.commit();
			sqlSession.clearCache();
		}catch(Exception e){
			logger.error("问题单文件撤销导入失败，失败原因："+e.getMessage());
			res="操作失败! 原因："+e.getMessage();
			sqlSession.rollback();
			sqlSession.clearCache();
		}finally{
			logger.info("问题单文件撤销导入结束");
			sqlSession.close();
		}
		return res;
	}
}
