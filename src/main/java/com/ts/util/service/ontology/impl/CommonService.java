package com.ts.service.ontology.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ontology.manager.CommonManager;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.ontology.CodeUtil;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称：快捷键处理接口
 * 创建人：xingsilong
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("commonService")
public class CommonService implements CommonManager{

	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;

	/**
	 * 根据序列名称获取序列
	 */
	public Integer querySeqValue(String seq_name) throws Exception{
		return (Integer)dao.findForObject("CommonMapper.querySeqValue", seq_name);
	}
	
	/**
	 * 本体列表分页
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> ontoWidgetPage(Page page, String sqlName) throws Exception {
		return (List<PageData>)dao.findForList("CommonMapper."+sqlName+"OsynWidgetlistPage", page);
	}
	
	/**
	 * 根据本体类型和序列类型查询序列值
	 * @param ontoType 本体类型，区分操作的表
	 * @param type 0本体，1同义词，2树
	 * @return
	 * @throws Exception
	 */
	public Integer getSequenceID(String ontoType,int type) throws Exception{
		Integer id = null;
		if(type==0){
			//本体序列
			if(OsynConst.DRUG.equals(ontoType)){
				//
				id = this.querySeqValue("seq_on_drug_info");
			}else if (OsynConst.OP.equals(ontoType)){
				//手术
				id = this.querySeqValue("seq_on_operation");
			}else if (OsynConst.DIAG.equals(ontoType)){
				//诊断
				id = this.querySeqValue("seq_on_diag");
			}else if (OsynConst.DEP.equals(ontoType)){
				//科室
				id = this.querySeqValue("seq_on_dep");
			}else{
				throw new Exception("ontoType is error.");
			}
		}else if(type==1){
			//标准词序列
			if(OsynConst.DRUG.equals(ontoType)){
				//不支持药品标准词扩充  TODO 
			}else if (OsynConst.OP.equals(ontoType)||OsynConst.OPERATION_NAME_CODE.equals(ontoType)){
				//手术
				id = this.querySeqValue("seq_on_operation_name");
			}else if (OsynConst.DIAG.equals(ontoType)||OsynConst.DIAG_NAME_CODE.equals(ontoType)){
				//诊断
				id = this.querySeqValue("seq_on_diag_name");
			}else if (OsynConst.DEP.equals(ontoType)||OsynConst.DEPT_NAME_CODE.equals(ontoType)){
				//科室
				id = this.querySeqValue("seq_on_dep_name");
			}else{
				throw new Exception("seq_ontoType is error.");
			}
		}else if(type==2){
			//树序列
			if(OsynConst.DRUG.equals(ontoType)){
				//药品
				id = this.querySeqValue("seq_on_drug_cate");
			}else if (OsynConst.OP.equals(ontoType)){
				//手术
				id = this.querySeqValue("seq_on_operation_tree");
			}else if (OsynConst.DIAG.equals(ontoType)){
				//诊断
				id = this.querySeqValue("seq_on_diag_tree");
			}else if (OsynConst.DEP.equals(ontoType)){
				//科室
				id = this.querySeqValue("seq_on_dep_tree");
			}else{
				throw new Exception("ontoType is error.");
			}
		}
		return id;
	}
	/**
	 * 
	 * @param newOntology 新本体
	 * @param ontoType	本体类型
	 * @param pids 父节点ID集合，诊断最多2个，手术与科室为一个 ，药品待定
	 */
	public void setMainAddCode(String ontoType,PageData ontology, String[] pids)throws Exception{
		//重置主要编码与附加编码
		ontology.put("MAIN_CODE", null);
		ontology.put("ADD_CODE", null);
		//根据父节点设置新的编码
		for(String pid:pids){
			this.getCode(ontoType,ontology,pid);
		}
	}
	
	/**
	 * 
	 * @param newOntology 新本体
	 * @param ontoType	本体类型
	 * @param pids 父节点ID集合，诊断最多2个，手术与科室为一个 ，药品待定
	 */
	public void setMainAddCode(String ontoType,PageData ontology, List<String> pids)throws Exception{
		//重置主要编码与附加编码
		ontology.put("MAIN_CODE", null);
		ontology.put("ADD_CODE", null);
		if(pids!=null && pids.size()>0){
			//根据父节点设置新的编码
			for(String pid:pids){
				this.getCode(ontoType,ontology,pid);
			}
		}else{
			//只有科室有这种父节点为空的情况
			this.getCode(ontoType,ontology,null);
		}
	}
	
	/**
	 * 根据parentID 找到父节点的所有下级节点最大编码，然后继续编码
	 * @param ontoType 本体类型
	 * @param parentId 父节点ID
	 * @param ontology 当前的本体，如果存在则会把对应编码放入ontology中。如果入参ontology为空，请注意父节点编码带有*号的时，请在主要编码后加上+号
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getCode(String ontoType,PageData ontology,String parentId) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd.put("ID", parentId);
		String sqlName = null;
		if(OsynConst.DRUG.equals(ontoType) ){
			sqlName = "drug";
		}else if (OsynConst.OP.equals(ontoType) ){
			sqlName = "op";
		}else if (OsynConst.DIAG.equals(ontoType) ){
			sqlName = "diag";
		}else if (OsynConst.DEP.equals(ontoType) ){
			sqlName = "dep";
		}
		//查询父节点信息
		PageData parent  = null;
		if(parentId!=null){
			parent = (PageData)dao.findForObject("OntologyMapper."+sqlName+"ById", parentId);
		}
		if( parent == null){
			if(OsynConst.DEP.equals(ontoType)){
				parent = new PageData();
			}else{
				throw new Exception("parent id null,parentId:"+parentId);
			}
		}
		String p_maincode = parent.getString("MAIN_CODE");
		String p_addcode = parent.getString("ADD_CODE");
		if(OsynConst.OP.equals(ontoType) ){
			p_maincode = parent.getString("OP_CODE");
		}else if(OsynConst.DEP.equals(ontoType) ){
			p_maincode = parent.getString("DEP_NAME_CODE");
		}
		//查询出孩子节点列表
		@SuppressWarnings("unchecked")
		List<PageData> list = (List<PageData>)dao.findForList("TreeMapper."+sqlName+"TreeList", pd);
		List<PageData> mainList = new ArrayList<PageData>();
		List<PageData> addList = new ArrayList<PageData>();
		//过滤掉当前本体,和不正确格式的孩子编码节点数据
		if(ontology!=null && !CollectionUtils.isEmpty(list)){
			for(int i=0;i<list.size();){
				PageData onto = list.get(i);
				if(ontology.get("D_ID").toString().equals(onto.get("ID").toString())){
					list.remove(i);
				}else{
					if(OsynConst.DRUG.equals(ontoType) ){
						mainList = list;
						addList = list;
					}else if (OsynConst.OP.equals(ontoType) ){
						mainList = list;
						addList = list;
					}else if (OsynConst.DIAG.equals(ontoType) ){
						String MAIN_CODE = onto.getString("MAIN_CODE");
						if(!Tools.isEmpty(p_maincode) && !Tools.isEmpty(MAIN_CODE)){
							String start  = p_maincode.substring(0,1);
							//正确孩子节点为8位码，如B08.400AA或B08.400AA*或B08.400AA+
							if(MAIN_CODE.startsWith(start)){
								mainList.add(onto);
							}
						}
						String ADD_CODE = onto.getString("ADD_CODE");
						if(!Tools.isEmpty(p_addcode) && !Tools.isEmpty(ADD_CODE)){
							String start  = p_addcode.substring(0,1);
							//正确孩子节点为8位码，如B08.400AA或B08.400AA*或B08.400AA+ 或者形态学编码：M98900/1AA
							if(ADD_CODE.startsWith(start)&&ADD_CODE.length()>=9){
								addList.add(onto);
							}
						}
					}else if (OsynConst.DEP.equals(ontoType) ){
						mainList = list;
						addList = list;
					}
					i++;
				}
			}
		}
		//设置主要编码
		if(OsynConst.DEP.equals(ontoType)  || !Tools.isEmpty(p_maincode)){
			String lastAgCode = null;
			if(!CollectionUtils.isEmpty(mainList)){
				//排序
				Collections.sort(mainList, new Comparator<PageData>() {
					@Override
					public int compare(PageData p1, PageData p2) {
						String code1 = p1.getString("MAIN_CODE")==null?"":p1.getString("MAIN_CODE");
						//获取有效ICD码值 去掉  [* +]
						code1 = CodeUtil.getAvalidIcdCode(code1);
						String code2 = p2.getString("MAIN_CODE")==null?"":p2.getString("MAIN_CODE");
						//获取有效ICD码值 去掉  [* +]
						code2 = CodeUtil.getAvalidIcdCode(code2);
						//倒序
						return -1 * ( code1.compareTo(code2) );
					}
				});
				lastAgCode = mainList.get(0).getString("MAIN_CODE");
			}
			String code = CodeUtil.makeAgCodeByIcd(ontoType, lastAgCode, p_maincode);
			if(ontology!=null){
				ontology.put("MAIN_CODE", code);
				CodeUtil.setSpecialCode(ontology);
			}
			map.put("MAIN_CODE", code);
		}
		//设置附加编码
		if(!Tools.isEmpty(p_addcode)){
			String lastAgCode = null;
			if(!CollectionUtils.isEmpty(addList)){
				//排序
				Collections.sort(addList, new Comparator<PageData>() {
					@Override
					public int compare(PageData p1, PageData p2) {
						String code1 = p1.getString("ADD_CODE")==null?"":p1.getString("ADD_CODE");
						//获取有效ICD码值 去掉  [* +]
						code1 = CodeUtil.getAvalidIcdCode(code1);
						String code2 = p2.getString("ADD_CODE")==null?"":p2.getString("ADD_CODE");
						//获取有效ICD码值 去掉  [* +]
						code2 = CodeUtil.getAvalidIcdCode(code2);
						//倒序
						return -1 * ( code1.compareTo(code2) );
					}
				});
				lastAgCode = addList.get(0).getString("ADD_CODE");
			}
			String code = CodeUtil.makeAgCodeByIcd(ontoType, lastAgCode, p_addcode);
			if(ontology!=null){
				ontology.put("ADD_CODE", code);
				CodeUtil.setSpecialCode(ontology);
			}
			map.put("ADD_CODE", code);
		}
		return map;
	}
	
}
