package com.ts.service.pdss.ias.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugAuditorRule;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugAuditorRuleDetail;
import com.ts.service.pdss.ias.RowMapper.TAntiDrugAuditorRuleDetailMapper;
import com.ts.service.pdss.ias.RowMapper.TAntiDrugAuditorRuleMapper;
import com.ts.service.pdss.ias.manager.IAntiDrugAuditor;

@Service
public class AntiDrugAuditorBean extends Persistent4DB implements IAntiDrugAuditor
{
	private static Map<String, Integer> paramsOrder = new HashMap<String, Integer>();
	static {
		paramsOrder.put("drugCode", 0);
		paramsOrder.put("drugName", 1);
		paramsOrder.put("drugForm", 2);
		paramsOrder.put("drugSpec", 3);
		paramsOrder.put("drugFirm", 4);
		paramsOrder.put("administration", 6);
		paramsOrder.put("doctorName", 7);
		paramsOrder.put("doctorTitle", 8);
		paramsOrder.put("deptCode", 9);
	}
	
	private static Map<String, String> paramsDesc = new HashMap<String, String>();
	static {
		paramsDesc.put("drugCode", "药品编码");
		paramsDesc.put("drugName", "药品名称");
		paramsDesc.put("drugForm", "剂型");
		paramsDesc.put("drugSpec", "规格");
		paramsDesc.put("drugFirm", "厂家");
		paramsDesc.put("administration", "给药途径");
	}

	/**
	 * 获取审核和登记开关
	 * @param DrugDoctorInfo 药品代码、名称、剂型、规格、厂家、数量、给药途径、医生姓名、职称、科室代码
	 * @return 审核、登记开关
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String[] getAntiDrugCheckRule(String[] DrugDoctorInfo)
	{
		String[] results = new String[]{"true", "true"};
		try
		{
			this.setQueryCode("IAS");
			StringBuffer sql = new StringBuffer();
			sql.append("select * from anti_drug_auditor_rule where drug_code='").append(DrugDoctorInfo[0]).append("'");
			List<TAntiDrugAuditorRule> ruleList = query.query(sql.toString(), new TAntiDrugAuditorRuleMapper());
			if (ruleList != null && ruleList.size() > 0)
			{
				System.out.println("药品码" + DrugDoctorInfo[0] + "的规则存在" + ruleList.size() + "条：");
				for (int i = ruleList.size() - 1; i >= 0; i--)
				{
					TAntiDrugAuditorRule rule = ruleList.get(i);
					boolean isEquals = true;
					Field[] fields = rule.getClass().getDeclaredFields();
					for (Field field : fields)
					{
						if (!"ruleId".equals(field.getName()) && !"doctorCode".equals(field.getName()) && !"drugName".equals(field.getName()) && !"deptName".equals(field.getName()) && !"auditSwitch".equals(field.getName()) && !"regSwitch".equals(field.getName()))
						{
							field.setAccessible(true);
							System.out.println("判断 " + paramsDesc.get(field.getName()) + " 属性...");
							if (field.get(rule) !=null && !"".equals(field.get(rule).toString()) && !"*".equals(field.get(rule).toString()))
							{
								// 给药途径可多选，格式为“给药途径1,给药途径2,给药途径3...”
								if ("administration".equals(field.getName()))
								{
									String[] administrations = field.get(rule).toString().trim().split(",");
									boolean isAdmin = false;
									for (String administration : administrations)
									{
										if (administration.equals(DrugDoctorInfo[paramsOrder.get(field.getName())].trim()))
										{
											isAdmin = true;
											break;
										}
									}
									if (!isAdmin)
									{
										isEquals = false;
										System.out.println(paramsDesc.get(field.getName()) + ":" + isEquals + "，将进行下一条规则的判断！！");
										break;
									}
								}
								else if (!DrugDoctorInfo[paramsOrder.get(field.getName())].trim().equals(field.get(rule).toString().trim()))
								{
									isEquals = false;
									System.out.println(paramsDesc.get(field.getName()) + ":" + isEquals + "，将进行下一条规则的判断！！");
									break;
								}
							}
						}
					}
					// 如果之前判断的属性已经不符合规则，那么不继续进行科室等信息的判断；否则，继续判断科室等信息
					if (isEquals)
					{
					// 查询该规则对应的科室信息
						sql.setLength(0);
						sql.append("select * from anti_drug_auditor_rule_detail where rule_id='").append(rule.getRuleId()).append("'");
						List<TAntiDrugAuditorRuleDetail> ruleDetailList = query.query(sql.toString(), new TAntiDrugAuditorRuleDetailMapper());
						if (ruleDetailList != null && ruleDetailList.size() > 0)
						{
							boolean isDept = false;
							for (TAntiDrugAuditorRuleDetail ruleDetail : ruleDetailList)
							{
								System.out.println("判断 科室 属性...");
								// 判断科室是否包含传入的参数
								if (ruleDetail.getDeptCode().equals(DrugDoctorInfo[paramsOrder.get("deptCode")].trim()))
								{
									isDept = true;
									System.out.println("判断 医生 属性...");
									if (ruleDetail.getDoctorName() != null && !"".equals(ruleDetail.getDoctorName()) && !"*".equals(ruleDetail.getDoctorName()))
									{
										// 如果科室包含传入的参数，判断科室中的医生是否包含传入的参数
										boolean isDoctor = false;
										int docIndex = 0;
										String[] doctors = ruleDetail.getDoctorName().split(",");
										for (int j = 0; j < doctors.length; j++)
										{
											if (doctors[j].equals(DrugDoctorInfo[paramsOrder.get("doctorName")].trim()))
											{
												docIndex = j;
												isDoctor = true;
												break;
											}
										}
										System.out.println("判断 医生职称 属性...");
										// 如果医生包含传入的参数，判断该医生职称与传入的参数是否一致
										if(isDoctor)
										{
											if (ruleDetail.getDoctorTitle() != null && !"".equals(ruleDetail.getDoctorTitle()) && !"*".equals(ruleDetail.getDoctorTitle()))
											{
												String[] doctorTitles = ruleDetail.getDoctorTitle().split(",");
												boolean isTitle = false;
												if (doctorTitles[docIndex].equals(DrugDoctorInfo[paramsOrder.get("doctorTitle")].trim()))
												{
													isTitle = true;
												}
												if (!isTitle)
												{
													isEquals = false;
													System.out.println("医生职称:" + isEquals + "，将进行下一条规则的判断！！");
												}
											}
										}
										else
										{
											isEquals = false;
											System.out.println("医生:" + isEquals + "，将进行下一条规则的判断！！");
										}
									}
									break;
								}
							}
							if (!isDept)
							{
								isEquals = false;
								System.out.println("科室:" + isEquals + "，将进行下一条规则的判断！！");
							}
						}
					}
					if (!isEquals || "true".equals(rule.getAuditSwitch()))
						ruleList.remove(i);
				}
				if (ruleList.size() > 0)
					results = new String[]{ruleList.get(0).getAuditSwitch(), ruleList.get(0).getRegSwitch()};
				System.out.println(DrugDoctorInfo[0] + ("true".equals(results[0]) ? "需要审核" : "无需审核"));
				return results;
			}
			System.out.println("不存在" + DrugDoctorInfo[0] + "相应的审核规则，需要审核");
			return new String[]{"true", "true"};
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return results;
	}
}