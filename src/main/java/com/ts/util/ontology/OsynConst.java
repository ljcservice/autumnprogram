package com.ts.util.ontology;

/**
 * AI常量
 * @ClassName: AIConst 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月3日 下午1:58:51 
 *
 */
public class OsynConst {
	//本体类型
	public static final String DRUG = "51001"; //药品
	public static final String DIAG = "51005"; //诊断
	public static final String OP = "51003"; //手术
	public static final String DEP = "51006"; //科室
	
	//同义词术语类型(字符串类型)
	public static final String DRUG_NAME_COMMON_CODE = "10402";//药品通用名
	public static final String DRUG_NAME_GOODS_CODE = "10404";//药品商品名
	public static final String DRUG_NAME_CHEMI_CODE = "10405";//药品化学名
	public static final String DRUG_NAME_PRODUCT_CODE = "10403";//药品产品名
	public static final String DRUG_FACTORY_CODE = "10801";//药品生产企业
	public static final String DRUG_PACK_CODE = "10205";//药品包装材质
	public static final String DRUG_SPEC_CODE = "10204";//药品规格表
	public static final String PACK_SPEC_CODE = "10202";//药品包装规格
	public static final String PACK_SPEC_UNIT_CODE = "10206";//药品规格单位
	
	public static final String DRUG_FORM_CODE="10101";//药品剂型
	public static final String DRUG_ROUTE_CODE="10102";//药品给药途径
	public static final String DRUG_PREP_CODE="10103";//药品制剂（新编）
	public static final String DRUG_PREP_COMP_CODE="10104";//药品制剂成分（组）（新编）
	public static final String DRUG_CONCEPT_CODE="10105";//药品大概念
	public static final String DRUG_TREE_CHINESE_CODE="10406";//药品中药分类树
	public static final String DRUG_TREE_WESTERN_CODE="10407";//药品西药分类树
	
	public static final String DIAG_NAME_CODE = "10301";//诊断名称
	public static final String OPERATION_NAME_CODE = "10501";//手术名称
	public static final String DEPT_NAME_CODE = "10502";//科室名称
	
	//同义词术语类型(数值类型)
	public static final Integer DRUG_NAME_COMMON_NUM_CODE = 10402;//药品通用名
	public static final Integer DRUG_NAME_GOODS_NUM_CODE = 10404;//药品商品名
	public static final Integer DRUG_NAME_CHEMI_NUM_CODE = 10405;//药品化学名
	public static final Integer DRUG_NAME_PRODUCT_NUM_CODE = 10403;//药品产品名
	public static final Integer DRUG_FACTORY_NUM_CODE = 10801;//药品生产企业
	public static final Integer DRUG_PACK_NUM_CODE = 10205;//药品包装材质
	public static final Integer DRUG_SPEC_NUM_CODE = 10204;//药品规格表
	public static final Integer PACK_SPEC_NUM_CODE = 10202;//药品包装规格
	public static final Integer PACK_SPEC_UNIT_NUM_CODE = 10206;//药品规格单位
	
	public static final Integer DRUG_FORM_NUM_CODE=10101;//药品剂型
	public static final Integer DRUG_ROUTE_NUM_CODE=10102;//药品给药途径
	public static final Integer DRUG_PREP_NUM_CODE=10103;//药品制剂(新编)
	public static final Integer DRUG_PREP_COMP_NUM_CODE=10104;//药品制剂成分（组）（新编）
	public static final Integer DRUG_CONCEPT_NUM_CODE=10105;//药品大概念
	public static final Integer DRUG_TREE_CHINESE_NUM_CODE=10406;//药品中药分类树
	public static final Integer DRUG_TREE_WESTERN_NUM_CODE=10407;//药品西药分类树
	
	public static final Integer DIAG_NAME_NUM_CODE=10301;//诊断名称
	public static final Integer OPERATION_NAME_NUM_CODE = 10501;//手术名称
	public static final Integer DEPT_NAME_NUM_CODE = 10502;//科室名称
	
	//同义词类型
	public static final String OSYN_OTHER_TYPE="23107";//其他类型
	public static final String OSYN_LANGUAGE="23105";//语用同义词
	public static final String OSYN_SPECIAL_TYPE="23106";//专指同义词
	public static final String OSYN_FAULT_TYPE="23103";//同音字/错别字
	public static final String OSYN_FIRST_LETTER="23104";//拼音首字母
	public static final String OSYN_COMMON_CALLED="23101";//俗称
	public static final String OSYN_ABB="23102";//缩略语
	
	//词语类型：0同义词；1标准词
	public static final String OSYN_WORD="0";//同义词
	public static final String STAND_WORD="1";//标准词
	
	//诊断部位分类
	public static final String PART_HEAD="0";//头部
	public static final String PART_NECK="1";//颈部
	public static final String PART_CHEST="2";//胸部
	public static final String PART_STOMACH="3";//腹部
	public static final String PART_WAIST="4";//腰部
	public static final String PART_BACK="5";//背部
	public static final String PART_HIPS="6";//臀部
	public static final String PART_PELVIC="7";//盆腔
	public static final String PART_UPPER_LIMB="8";//上肢
	public static final String PART_LEGS="9";//下肢
	public static final String PART_SKIN="10";//皮肤
	public static final String PART_HANDS="11";//手
	public static final String PART_FOOTS="12";//足
	public static final String PART_EYES = "13";//眼
	public static final String PART_NOSE = "14";//鼻
	public static final String PART_MOUTH = "15";//口
	public static final String PART_EARS = "16";//耳
	//诊断人群分类
	public static final String MALE = "0";//男性
	public static final String FEMALE="1";//女性
	//本体与同义词操作类型:	0 新增1 修改2 修改父节点3 术语知识编辑4 停用术语 5 6  7 级联新增8 被动新增9 删除
}
