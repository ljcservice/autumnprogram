package com.ts.controller.ontology;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.ontology.impl.StandardService;
import com.ts.util.PageData;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称：术语标准词同义词管理
 * 创建人：xsl
 * 更新时间：2016年10月10日
 * @version
 */
@Controller
@RequestMapping(value="/standard")
public class StandardController extends BaseController {
	
	@Resource(name="standardService")
	private StandardService standardService;

	//弹窗-标准词查询
		@RequestMapping(value="/osynStand")
		public ModelAndView osynStand(Page page)throws Exception{
			ModelAndView mv = this.getModelAndView();
			try{
				PageData pd = new PageData();
				pd = this.getPageData();
				//标准词
				pd.put("osynFlag", "1");
				pd.put("WORD_TYPE","1");
				pd.put("TERM_CATEGORY",pd.get("ONTOTYPE"));
				page.setPd(pd);
				List<PageData> list = null;
				String sqlName = null;
				String listName="";
				//标准词类别
				Integer ontoType = Integer.valueOf(pd.getString("ontoType"));
				//初始化不执行查询
				if(OsynConst.DRUG_NAME_COMMON_NUM_CODE.equals( ontoType)){
					sqlName = "drugName";//药品通用名
					listName="drug/name/choose_drugname_stad_list";
				}else if(OsynConst.DRUG_NAME_PRODUCT_NUM_CODE.equals( ontoType)){
					sqlName = "drugName";//药品产品名
					listName="drug/name/choose_drugname_stad_list";
				}else if(OsynConst.DRUG_NAME_GOODS_NUM_CODE.equals( ontoType)){
					sqlName = "drugName";//药品商品名
					listName="drug/name/choose_drugname_stad_list";
				}else if(OsynConst.DRUG_NAME_CHEMI_NUM_CODE.equals( ontoType)){
					sqlName = "drugName";//药品化学名
					listName="drug/name/choose_drugname_stad_list";
				}else if(OsynConst.DRUG_FACTORY_NUM_CODE.equals( ontoType)){
					sqlName = "drugFactory";//药品生产企业
					listName="drug/factory/choose_factory_stad_list";
				}else if(OsynConst.DRUG_SPEC_NUM_CODE.equals( ontoType)){
					sqlName = "drugSpec";//药品规格
					listName="drug/spec/choose_spec_stad_list";
				}else if(OsynConst.DRUG_PACK_NUM_CODE.equals( ontoType)){
					sqlName = "drugPack";//药品包装材质
					listName="drug/pack/choose_pack_stad_list";
				}else if(OsynConst.PACK_SPEC_NUM_CODE.equals( ontoType)){
					sqlName = "packSpec";//药品包装规格
					listName="drug/packspec/choose_packspec_stad_list";
				}else if(OsynConst.PACK_SPEC_UNIT_NUM_CODE.equals( ontoType)){
					sqlName = "packSpecUnit";//药品包装规格单位
					listName="drug/packspecUnit/choose_packspec_unit_stad_list";
				}else if (OsynConst.DIAG_NAME_NUM_CODE.equals( ontoType)){
					sqlName = "diagOsyn";//诊断
					listName="choose_standard_list";
				}else if (OsynConst.DRUG_FORM_NUM_CODE.equals( ontoType)){
					sqlName = "drugForm";//药品剂型
					listName="drug/form/choose_form_stad_list";
				}else if (OsynConst.DRUG_ROUTE_NUM_CODE.equals( ontoType)){
					sqlName = "drugRoute";//药品给药途径
					listName="drug/route/choose_route_stad_list";
				}else if (OsynConst.DRUG_PREP_NUM_CODE.equals( ontoType)){
					sqlName = "drugPrep";//药品制剂
					listName="drug/prep/choose_prep_stad_list";
				}else if (OsynConst.DRUG_PREP_COMP_NUM_CODE.equals( ontoType)){
					sqlName = "drugPrepComp";//药品制剂成分(组)
					listName="drug/prepComp/choose_prepcomp_stad_list";
				}else if (OsynConst.DRUG_CONCEPT_NUM_CODE.equals( ontoType)){
					sqlName = "drugPrepComp";//药品大概念
					listName="drug/prepComp/choose_prepcomp_stad_list";
				}else if (OsynConst.DEPT_NAME_NUM_CODE.equals(ontoType)) {
					sqlName="dep";//科室名称标准词
					listName="dep/choose_dep_stad_list";
				}else if (OsynConst.OPERATION_NAME_NUM_CODE.equals(ontoType)) {
					sqlName="op";//手术名称标准词
					listName="op/choose_op_stad_list";
				}
				list = standardService.chooseStandPage(page,sqlName);
				mv.setViewName("ontology/osyn/"+listName);
				mv.addObject("pd", pd);
				mv.addObject("ontoType", ontoType);
				mv.addObject("resultList",list);
			} catch(Exception e){
				logger.error(e.toString(), e);
			}
			return mv;
		}
}
