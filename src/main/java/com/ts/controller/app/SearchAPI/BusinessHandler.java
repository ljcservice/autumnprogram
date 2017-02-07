package com.ts.controller.app.SearchAPI;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.app.SearchAPI.ResultHandler.IResultHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.app.AppToken;
import com.ts.service.system.apimanager.AccessibleField.AccessibleFieldManager;
import com.ts.util.app.SessionAppMap;
import com.ts.util.app.Enum.EnumStatus;

import net.sf.json.JSONObject;

/**
 * 
 * ClassName: BusinessHandler
 * 
 * @Description: 查询API类型和权限验证
 * @author 李世博
 * @date 2016年9月28日
 */
@Controller
@RequestMapping(value = "/app")
public class BusinessHandler extends BaseController {
	public static ConcurrentHashMap<String, String> typeMap = new ConcurrentHashMap<String, String>();
	static {
		// 增加sql项目对应码key：项目编码；value：查询数据库表名（用逗号分隔的事必填字段，无此字段return false）
		typeMap.put("1", "kn_instruction");// 药品信息
		typeMap.put("2", "on_diag");// 导诊信息
		typeMap.put("3", "fac_hospital,area_id");// 医院信息
		typeMap.put("4", "fac_department,hos_id");// 科室信息
		typeMap.put("5", "fac_medical,dep_id");// 医生信息

		// 增加solr项目对应码key：项目编码 ；value：配置文件中的key连接solr服务器使用
		typeMap.put("6", "solr.ts_kn_medical_faq");// 智能问答模块
		typeMap.put("7", "solr.ts_kn_operation");// 手术知识推送
		typeMap.put("8", "solr.ts_kn_disease");// 诊断知识
		typeMap.put("9", "solr.ts_kn_assist_examine");// 辅助检查知识
		typeMap.put("10", "solr.ts_kn_drug_base");// 新编药物学药品知识信息
		typeMap.put("11", "solr.ts_kn_instruction");// 药品说明书模块solr版
		typeMap.put("12", "solr.ts_on_diag");// 导诊知识模块solr版
		typeMap.put("13", "solr.ts_v_product");// 区域商品搜药检索查询业务
		typeMap.put("14", "solr.ts_v_sort_search");// 分类检索查询业务
	}

	@Resource(name = "accessibleFieldService")
	private AccessibleFieldManager accessibleFieldService;
	
	@Resource(name = "solrQABusiness")
	private IResultHandler SolrQABusiness; // solr查询数据集
	@Resource(name = "sqlQABusiness")
	private IResultHandler SqlQABusiness; // sql查询数据集

	@RequestMapping(value = "/BusinessHandler", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object BusinessHandler(@RequestBody String json) {
		logBefore(logger, "业务接口");
		logBefore(logger, "--传入Json--:" + json);
		Object ddbEntity = new Object();
		try {
			JSONObject jo = getJsonObject(json);
			String token = jo.getString("token");
			String apitype = jo.getString("apitype");
			String ip = getRequest().getRemoteAddr(); // 获取客户端ip
			logBefore(logger, "ip>>>:" + ip);

			// 判断安全token是否存在或有效
			if (SessionAppMap.isTokenvalider(token)) {
				// 判断有效时间
				if (SessionAppMap.isTimeOut(token)) {
					AppToken appToken = SessionAppMap.getAppUser(token);
					// 判断用户是否有访问该业务的权限
					Set<String> list = appToken.getAppUser().getApitype();
					if (list.contains(apitype)) {
						Calendar now = new GregorianCalendar();
						// 按秒延时SECOND,按分延时 MINUTE ,按小时延时HOUR_OF_DAY,按天延时DAY_OF_MONTH
						now.add(Calendar.HOUR_OF_DAY, 1);
						appToken.setAccess_token(token);
						appToken.setExpires_in(now.getTimeInMillis());
						SessionAppMap.setAppUser(appToken);
						
						switch (apitype) {
						case "1":
							ddbEntity = SqlQABusiness.getRSHandler(jo); // 药品信息
							break;
						case "2":
							ddbEntity = SqlQABusiness.getRSHandler(jo); // 导诊信息
							break;
						case "3":
							ddbEntity = SqlQABusiness.getRSHandler(jo); // 医院信息
							break;
						case "4":
							ddbEntity = SqlQABusiness.getRSHandler(jo); // 科室信息
							break;
						case "5":
							ddbEntity = SqlQABusiness.getRSHandler(jo); // 医生信息
							break;
						case "6":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // solr知识问答查询
							break;
						case "7":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // 手术知识推送
							break;
						case "8":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // 诊断知识
							break;
						case "9":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // 辅助检查知识
							break;
						case "10":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // 新编药物学药品知识信息
							break;
						case "11":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // 药品信息solr版
							break;
						case "12":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // 导诊信息solr版
							break;
						case "13":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // 区域商品搜药检索查询业务
							break;
						case "14":
							ddbEntity = SolrQABusiness.getRSHandler(jo); // 分类检索查询业务
							break;
						default:
							ddbEntity = "{\"status\":\"" + EnumStatus.not_API_data_type.getEnumValue() + "\"}";
							break;
						}
					} else {
						ddbEntity = "{\"status\":\"" + EnumStatus.Unauthorized_access.getEnumValue() + "\"}";
						logBefore(logger, "无该业务的访问权限");
					}
				} else {
					ddbEntity = "{\"status\":\"" + EnumStatus.request_timeout.getEnumValue() + "\"}";
					logBefore(logger, "令牌失效，令牌超时");
				}
			} else {
				ddbEntity = "{\"status\":\"" + EnumStatus.illegal_identity.getEnumValue() + "\"}";
				logBefore(logger, "令牌失效，非法身份");
			}
		} catch (Exception e) {
			ddbEntity = "{\"status\":\"" + EnumStatus.Parameter_error.getEnumValue() + "\"}";
			logBefore(logger, "参数错误");
		}

		logBefore(logger, "--返回Json--:" + ddbEntity);
		logAfter(logger);
		return ddbEntity;
	}
}
