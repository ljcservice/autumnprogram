package com.ts.service.ontology.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ontology.manager.OsynManager;
import com.ts.util.PageData;
import com.ts.util.UuidUtil;
import com.ts.util.ontology.HelpUtil;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称：DiagService 诊断处理
 * 创建人：xingsilong
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("osynService")
public class OsynService implements OsynManager{

	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;


	public List<PageData> osynPage(Page page,String sqlName) throws Exception{
		List<PageData> list = (List<PageData>)dao.findForList("OsynMapper."+sqlName+"OsynlistPage", page);
		return list;
	}


	/**
	 * 查询同义词列表
	 */
	public List<PageData> osynListPage(Page page,String tableName)throws Exception{
		return (List<PageData>)dao.findForList("OsynMapper."+tableName+"listPage", page);
	}

	/**
	 * 查询单个同义词信息
	 */
	public PageData findDiagOsynById(PageData pd,String typeName) throws Exception {
		return (PageData)dao.findForObject("OsynMapper."+typeName+"OsynById", pd);
	}

	/**
	 * 审核通过新增词语表信息
	 */
	@Override
	public String insertOsynName(PageData pd,String typeName) throws Exception {
		// TODO Auto-generated method stub
		String msg="success";
		dao.save("OsynMapper."+typeName+"InsertOsynName",pd);
		//修改AI_ALTER_NAME_HIST表（审核拒绝时只执行修改副本表操作）
		PageData hisParam = new PageData();
		hisParam.put("H_ID",pd.get("H_ID"));
		hisParam.put("CHECK_TIME", new Date());
		hisParam.put("CHECK_USER",pd.get("CHECK_USER"));
		hisParam.put("STATUS","1");
		hisParam.put("CHECK_MEMO","确认审核");
		dao.update("AiAlterNameHistMapper.upAlterName", hisParam);//更新同义词历史信息
		return msg;
	}

	/**
	 * 审核通过修改对应的词语表信息
	 */
	@Override
	public String updateDiagName(HttpServletRequest request, PageData pd) throws Exception {
		String msg="success";
		String h_id = pd.getString("H_ID");//新数据历史ID
		PageData newOnto = (PageData) dao.findForObject("AiAlterNameHistMapper.findAlterById",h_id);
		//术语类型：10801：药品生产企业；10401：药品名称；10202：药品包装规格；10206：药品规格单位；10204：药品规格；10205：药品包装材质；10301：诊断
		String ontoType =String.valueOf(newOnto.get("ONTO_TYPE"));
		
		String typeName="";
		if(OsynConst.DIAG_NAME_CODE.equals(ontoType)){//诊断
			typeName="diag";
		}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)) {//手术
			typeName="op";
		}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)) {//科室
			typeName="dep";
		} else if (OsynConst.DRUG_FACTORY_CODE.equals(ontoType)) {
			typeName="drugFactory";//药品生产企业
		}else if (OsynConst.DRUG_SPEC_CODE.equals(ontoType)) {
			typeName="drugSpec";//药品规格
		}else if (OsynConst.DRUG_PACK_CODE.equals(ontoType)) {
			typeName="drugPack";//药品包装材质
		}else if (OsynConst.PACK_SPEC_CODE.equals(ontoType)) {
			typeName="packSpec";//药品包装规格
		}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(ontoType)) {
			typeName="packSpecUnit";//药品包装规格单位
		}else if (OsynConst.DRUG_FORM_CODE.equals(ontoType)) {
			typeName="drugForm";//药品剂型
		}else if (OsynConst.DRUG_ROUTE_CODE.equals(ontoType)) {
			typeName="drugRoute";//药品给药途径
		}else{
			typeName="drug";//药品名称
		}
		
		/*************判断审核时专家是否修改属性***************/
		Integer checkModifyFlag = new Integer(0);
		if(request!=null){
			checkModifyFlag +=setRequest2Pd(request,newOnto,"DN_CHN");
			checkModifyFlag +=setRequest2Pd(request,newOnto,"DN_ENG");
			checkModifyFlag +=setRequest2Pd(request,newOnto,"ORG_CN_CHN");
			checkModifyFlag +=setRequest2Pd(request,newOnto,"ORG_DN_ENG");
			checkModifyFlag +=setRequest2Pd(request,newOnto,"OSYN_TYPE");
			checkModifyFlag +=setRequest2Pd(request,newOnto,"IS_DISABLE");
			checkModifyFlag +=setRequest2Pd(request,newOnto,"DESCRIPTION");
			pd.put("checkModifyFlag",checkModifyFlag);
		}
		//操作类型：1修改，4停用术语9删除;newOnto:修改的同义词信息
		int opType = ((BigDecimal)newOnto.get("OP_TYPE")).intValue();
		
		if(opType==0){/**新增术语**/
			dao.save("OsynMapper."+typeName+"InsertOsynName",newOnto);
		}else if (opType==1) {/**修改术语**/
			String dn_id= newOnto.getString("DN_ID");//原数据ID
			String stad_dn_id = newOnto.getString("STAD_DN_ID");//对应的标准词ID
			//若修改的是标准词，若修改名称时，需要修改对应同义词的标准名称，且原来的标准词新增一条同义词记录
			if(dn_id.equals(stad_dn_id)){
				//获取修改词语的原数据信息
				PageData param = new PageData();
				param.put("ON_ID",dn_id);
				param.put("ON_NAME_ID",dn_id);
				PageData oldOnto = this.findDiagOsynById(param, typeName);
					/*****************1、当标准词含有对应的同义词时，先修改对应的同义词信息，并添加同义词历史记录****************/
					PageData osynPd = new PageData();
					pd.put("STAD_ID",dn_id);
					pd.put("WORD_TYPE","0");//查询同义词	
					PageData upPd = HelpUtil.compareUpd(ontoType,oldOnto,newOnto);//获取修改项
					//若标准词信息修改项为：术语名称中文、英文、简称；来源术语名称中文、英文、简称；国家、地区、区号、停用标记、停用描述中的一项或多项，则更改对应的同义词
					if(!upPd.isEmpty()){
						List<PageData> osynList = (List<PageData>) dao.findForList("OsynMapper."+typeName+"OsynById", osynPd);//获取对应的同义词信息列表
						if(osynList.size()>0){
							upPd.put("STAD_CHN",newOnto.getString("DN_CHN"));
							upPd.put("STAD_ID",dn_id);
							dao.update("OsynMapper."+typeName+"UpdateOsynName",upPd);
						}
					}
					/*********2、当修改标准词信息时，修改了标准词名称，则需要将原标准词信息变更为同义词，插入一条同义词和一条同义词历史记录信息**********/
					String newChn = newOnto.getString("DN_CHN");//术语中文名
					String newEng = newOnto.getString("DN_ENG");//术语英文名
					String oldChn = "";//修改前术语名称
					
					if(OsynConst.DRUG_FACTORY_CODE.equals(ontoType)){//药品生产企业
						oldChn=oldOnto.getString("FAC_CHN");
					}else if (OsynConst.PACK_SPEC_CODE.equals(ontoType)) {//药品包装规格
						oldChn=oldOnto.getString("PS_CHN");
					}else if (OsynConst.DRUG_SPEC_CODE.equals(ontoType)) {//药品规格表
						oldChn=oldOnto.getString("DRUG_SP");
					}else if (OsynConst.DRUG_PACK_CODE.equals(ontoType)) {//药品包装材质
						oldChn=oldOnto.getString("DP_CHN");
					}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(ontoType)) {//药品规格单位
						oldChn=oldOnto.getString("PSU_CHN");
						String oldEng = oldOnto.getString("PSU_ENG");
						if(!oldChn.equals(newChn)||!oldEng.equals(newEng)){//若修改中文名或英文名
							/*********当修改的是规格单位中文或英文名时，需要同步更新对应的包装规格表中的规格单位信息**************/
							PageData packSpec = new PageData();
							packSpec.put("FK_PSU_ID",dn_id);
							List<PageData> list = (List<PageData>)dao.findForList("OsynMapper.packSpecOsynById", packSpec);
							if (!list.isEmpty()) {
								packSpec.put("STAD_PS_UNIT_CHN",newChn);
								packSpec.put("STAD_PS_UNIT_ENG",newEng);
								dao.update("OsynMapper.packSpecUpdateByPsu",packSpec);
							}
						}
					}else if (OsynConst.DIAG_NAME_CODE.equals(ontoType)) {//诊断
						oldChn=oldOnto.getString("DN_CHN");
					}else if (OsynConst.DRUG_FORM_CODE.equals(ontoType)) {//药品剂型
						oldChn=oldOnto.getString("F_CHN_NAME");
					}else if (OsynConst.DRUG_ROUTE_CODE.equals(ontoType)) {//药品给药途径
						oldChn=oldOnto.getString("DR_CHN");
					}else{//药品名称
						oldChn=oldOnto.getString("CHN_NAME");
					}
					//若修改的是生产企业名称，需将修改前的生产企业信息变更为同义词，需新增一个同义词信息
					if(!newChn.equals(oldChn)){
						PageData newOsyn =new PageData();
						newOsyn.putAll(oldOnto);
						newOsyn=HelpUtil.insertOsynHisOption(oldOnto, newOnto, ontoType);
						newOsyn.put("DN_ID",UuidUtil.get32UUID());
						this.insertOsynName(newOsyn, typeName);//新增药品生产企业同义词
						//增加一条同义词历史
						newOsyn.put("CHECK_USER", newOnto.get("UPDATE_MAN"));
						newOsyn.put("CHECK_TIME", newOnto.get("UPDATE_TIME"));
						newOsyn.put("CHECK_MEMO", "标准词名称更改，原本标准词变更为同义词");
						newOsyn.put("STATUS", 1);//审核通过
						newOsyn.put("OP_TYPE", 8);//操作类型：被动新增
						newOsyn.put("UPD_DESC", "标准词名称更改，原本标准词变更为同义词");
						newOsyn.put("H_ID", UuidUtil.get32UUID());
						dao.save("AiAlterNameHistMapper.saveOsynHis", newOsyn);
					}
					/*********************3、修改标准词信息*****************/
					dao.update("OsynMapper."+typeName+"UpdateOsynName",newOnto);
			}else{//若不是标准词，则直接修改同义词信息
				dao.update("OsynMapper."+typeName+"UpdateOsynName",newOnto);
			}
		}else if (opType==4) {/**停用术语**/
			dao.update("OsynMapper."+typeName+"OsynStop",newOnto);
		}
		//修改AI_ALTER_NAME_HIST表（审核拒绝时只执行修改副本表操作）
//		PageData hisParam = new PageData();
//		hisParam.put("H_ID",h_id);
//		pd.put("CHECK_TIME", new Date());
//		hisParam.put("CHECK_USER",pd.get("CHECK_USER"));
		pd.put("STATUS","1");
		pd.put("CHECK_MEMO","确认审核");
		dao.update("AiAlterNameHistMapper.upAlterName", pd);//更新同义词历史信息
		return msg;
	}
	//比较审核前后数据是否相等
	private int setRequest2Pd(HttpServletRequest request, PageData pd,	String name) {
		String value = request.getParameter(name);
		if(value!=null){
			pd.put(name, value);
			return 1;
		}else{
			return 0;
		}
	}

	/**
	 * 查询药品名称列表
	 */
	@Override
	public List<PageData> drugNameList(Page page) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>)dao.findForList("OsynMapper.drugNamelistPage",page);
	}
	
	/**
	 * 检查诊断名称是否存在
	 * @param sqlName
	 * @param newOntology
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public boolean checkExistName(String sqlName,PageData pd) throws Exception {
		List list = (List<PageData>)dao.findForList("OsynMapper."+sqlName+"CheckExistName", pd);
		if(!CollectionUtils.isEmpty(list)){
			return true;
		}
		return false;
	}

	/**
	 * 批量审批通过
	 */
	@Override
	public String checkPassAll(PageData pd) throws Exception {
		String ids = pd.getString("H_IDS");
		String[] h_ids = ids.split(",");
		//循环审核的H_ID
		for(String H_ID:h_ids){
			pd.put("H_ID", H_ID);
			String msg = updateDiagName(null, pd);
			if(!"success".equals(msg)){
				return msg;
			}
		}
		return "success";
	}

	/**
	 * 判断本体是否停用
	 */
	@Override
	public boolean checkOntoDisable(String sqlName, PageData pd) throws Exception {
		boolean flag = false;
		PageData disableData = (PageData)dao.findForObject("OntologyMapper."+sqlName+"IdFindByNameId",pd);
		if (disableData.isEmpty()) {
			return true;
		}
		String isDisable = String.valueOf(disableData.get("IS_DISABLE"));
		if("0".equals(isDisable)){
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}
}