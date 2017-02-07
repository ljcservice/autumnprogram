package com.ts.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import net.sf.json.JSONObject;
import com.ts.entity.ai.Total;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.ai.AICommMethod;

public class MTSHttpService {
	
	private static Logger logger = Logger.getLogger(MTSHttpService.class);
	private static String reloadURL ;
	private static String isReloadURL ;
	private static String checkURL ;
	private static String addURL;
	private static String addTermURL;
	private static String content = "{\"visitId\":\"123456789\",\"visitType\":\"01\",\"dataSource\":\"kfzxyy\",\"dataType\":\"02\",\"parameters\":\"05@#&NAME\",\"application\":\"AI\"}";
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("aiconfig");
		if (bundle == null) {
			throw new IllegalArgumentException("[ai.properties] is not found!");
		}
		reloadURL = bundle.getString("reloadURL");
		isReloadURL = bundle.getString("isReloadURL");
		checkURL = bundle.getString("checkURL");
		addURL = bundle.getString("addURL");
		addTermURL=bundle.getString("addTermURL");
	}
	
	/**
	 * 重启MTS
	 */
	public static synchronized void reLoadMTS(){
		TsThreadPoolExecutor.threadPoolExecutor.execute(new Runnable() {
			@Override
			public void run() {
				TsHttpClient.getRequest(reloadURL);
			}
		});
	}

	/**
	 * 判断重启mts是否成功
	 */
	public static boolean checkReLoadMTS() {
		String result = TsHttpClient.getRequest(isReloadURL);
		if("true".equals(result)){
			return true;
		}
		return false;
	}

	/**
	 * 批量检测问题单
	 * @param list
	 * @throws Exception 
	 */
	public static void checkQuestion(List<PageData> list,StringBuffer sb,List<PageData> errorList,Total t) throws Exception {
		if(list==null){
			return;
		}
		Map<String,String> requestHeadMap = new HashMap<String,String>();
		requestHeadMap.put("Content-Type","application/json;charset=UTF-8");
    	for(PageData pd:list){
	        try{
	        	String body = content.replace("NAME", pd.getString("O_DIAG_NAME"));
    			//mts返回数据
    			String result = TsHttpURL.post(checkURL, null, requestHeadMap, body, "UTF-8");
                if(result!=null){
        			JSONObject obj = JSONObject.fromObject(result);
        			String status = obj.getString("status");
        			if("2".equals(status)){
        				throw new Exception("http response is error,return mtx status:"+status);
        			}else if("0".equals(status)){
//                		String[] old_name = pd.getString("O_DIAG_NAME").split(";");
        				Object o = obj.getString("nlpResult");
        				if(o == null || "".equals(o.toString())){
        					throw new Exception("MTS service response is error! url:"+checkURL);
        				}
                		JSONObject mts_result = obj.getJSONObject("nlpResult");
                		if(mts_result == null ){
                			throw new Exception("http response is error,return mtx name is less !");
                		}
                		for(Object key : mts_result.keySet()){
                			String nlp_name = (String)mts_result.get(key);
                			if(Tools.isEmpty(nlp_name)||"none".equals(nlp_name)){
				                PageData error_pd = new PageData();
				                error_pd.put("Q_ID", pd.get("Q_ID"));
				                error_pd.put("NAME", key);
				                //添加到重置问题单列表
				                errorList.add(error_pd);
				                //增加错误信息
            	                sb.append(key).append("，");
                			}
                		}
                	}
                }
		    }catch(Exception e){
		    	logger.error("mts check info error Q_ID:"+pd.get("Q_ID").toString()+",NAME:"+pd.getString("O_DIAG_NAME"));
	        	throw e;
		    }
	        Thread.sleep(1000);
	        //更新进度条
			t.setNum(t.getNum()+1);
		}
	}
	
	/**
	 * 增加本体或者无用术语到MTS中
     * @throws Exception 
	 */
	public static String addToMTS(PageData pd) throws Exception{
		JSONObject j=new JSONObject();
		if(AICommMethod.isEmpty(pd.get("TERM_NAME"))){//本体更新
			j.put("DATA_TYPE_ID", 5);//标准化类型
			j.put("ORIG_DATA_ID", " ");//诊断同义词码
			j.put("ORIG_DATA_NAME", pd.get("DIAG_NAME"));//同义词名称
			//诊断信息串  需要 在    以"|" 符号 分割 ，需要 在 索引为   0、7、12   处  分别传入  “诊断标准词“、”诊断附码“，”诊断主码“，其他信息传空即可
			String str=pd.get("STAD_DIAG_NAME")+"| | | | | | |"+pd.get("ADD_CODE")+"| | | | |"+pd.get("MAIN_CODE")+"| | ";
			j.put("ORIG_DATA_STR",str);
			return TsHttpClient.postMTSJson(addURL,j.toString());//增加本体成功返回1失败返回0
		}else{//无用术语更新
			j.put("TERM_NAME", pd.get("TERM_NAME").toString());//标准化类型
			j.put("FLAG", 3);//当前为测试码，正式的诊断码为1
			return TsHttpClient.postMTSJson(addTermURL,j.toString());//增加本体成功返回1失败返回0
		}
	}
}
