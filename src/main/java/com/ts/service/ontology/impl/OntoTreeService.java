package com.ts.service.ontology.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupportAi;
import com.ts.service.ontology.manager.CommonManager;
import com.ts.service.ontology.manager.OntoTreeManager;
import com.ts.service.ontology.manager.OntologyManager;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.UuidUtil;
import com.ts.util.ontology.HelpUtil;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称： 树结构处理
 * 创建人：xingsilong
 * 修改时间：2016年10月27日
 * @version 
 */
@Service("ontoTreeService")
public class OntoTreeService implements OntoTreeManager{

	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;
	
	@Resource(name = "commonService")
	private CommonManager commonService;
	
	@Resource(name = "ontologyService")
	private OntologyManager ontologyService;
	

	@SuppressWarnings("unchecked")
	public List<PageData> treeRootList(PageData pd) throws Exception{
		List<PageData> list = null;
		//本体类型
		if(pd.getString("ontoType")==null){
			return null;
		}
		String ontoType = pd.getString("ontoType");
		String sqlname = null;
		if(OsynConst.DRUG.equals(ontoType)){
			//药品
			sqlname = "drug";
			String CATEGORY = pd.getString("CATEGORY");
			String id = pd.getString("id");
			if(Tools.isEmpty(id) && Tools.isEmpty(CATEGORY)){
				//初始化,增加西药、中药、中药材及饮片分类
				list = new ArrayList<PageData>();
				PageData p1 = new PageData();
				p1.put("TREE_ID", "0");
				p1.put("CATEGORY", 0);
				p1.put("NAME", "西药");
				PageData p2 = new PageData();
				p2.put("TREE_ID", "0");
				p2.put("CATEGORY", 1);
				p2.put("NAME", "中成药");
				PageData p3 = new PageData();
				p3.put("TREE_ID", "0");
				p3.put("CATEGORY", 2);
				p3.put("NAME", "中药材及饮片");
				list.add(p1);list.add(p2);list.add(p3);
				return list;
			}
		}else if (OsynConst.OP.equals(ontoType)){
			sqlname = "op";
		}else if (OsynConst.DIAG.equals(ontoType)){
			sqlname = "diag";
		}else if (OsynConst.DEP.equals(ontoType)){
			sqlname = "dep";
		}
		list = (List<PageData>)dao.findForList("TreeMapper."+sqlname+"TreeList", pd);

		if(OsynConst.DIAG.equals(ontoType) && !CollectionUtils.isEmpty(list)){
////			PageData parent = ontologyService.queryOntologyById(sqlname, pd.getString("ID"));
////			String p_m_cdoe = CodeUtil.getAvalidIcdCode(parent.getString("MAIN_CODE"));
////			String p_a_cdoe = CodeUtil.getAvalidIcdCode(parent.getString("ADD_CODE"));
//			for(PageData child:list){
//				String c_m_cdoe = CodeUtil.getAvalidIcdCode(child.getString("MAIN_CODE"));
//				String c_a_cdoe = CodeUtil.getAvalidIcdCode(child.getString("ADD_CODE"));
//				//如果孩子主要编码 和附加编码都不为空
//				if(c_m_cdoe!=null&&c_a_cdoe!=null){
//					child.put("NAME", child.getString("CN")+"["+child.getString("MAIN_CODE")+","+child.getString("ADD_CODE")+"]");
//				}else if(c_m_cdoe!=null){
//					//设置为主要编码，则不做操作，默认显示的就是主要编码
//					
//				}else if (c_a_cdoe!=null) {
//					//设置为附加编码
//					child.put("NAME", child.getString("CN")+"["+child.getString("ADD_CODE")+"]");
//				}
//			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public String treePidsById(PageData pd) throws Exception{
		List<PageData> list = null;
		//本体类型
		if(pd.getString("ontoType")==null){
			return null;
		}
		String ontoType = pd.getString("ontoType");
		String sqlname = null;
		String id = pd.getString("ID");
		if(OsynConst.DRUG.equals(ontoType)){
			//药品
			sqlname = "drug";
			String CATEGORY = pd.getString("CATEGORY");
			if(Tools.isEmpty(id) && Tools.isEmpty(CATEGORY)){
				//初始化,增加西药、中药、中药材及饮片分类
				//虚拟ID 转换为 0  TODO
				list = new ArrayList<PageData>();
				PageData p1 = new PageData();
				p1.put("TREE_ID", "0");
				p1.put("CATEGORY", 0);
				p1.put("NAME", "西药");
				PageData p2 = new PageData();
				p2.put("TREE_ID", "0");
				p2.put("CATEGORY", 1);
				p2.put("NAME", "中成药");
				PageData p3 = new PageData();
				p3.put("TREE_ID", "0");
				p3.put("CATEGORY", 2);
				p3.put("NAME", "中药材及饮片");
				list.add(p1);list.add(p2);list.add(p3);
				return null;
			}
		}else if (OsynConst.DIAG.equals(ontoType ) ){
			//诊断
			sqlname = "diag";
		}else if (OsynConst.OP.equals(ontoType ) ){
			//手术
			sqlname = "op";
		}else if (OsynConst.DEP.equals(ontoType)){
			//诊断
			sqlname = "dep";
		}else{
			return null;
		}
		list = (List<PageData>)dao.findForList("TreeMapper."+sqlname+"TreePidsById", pd);
		StringBuffer result = new StringBuffer();
		for(int i=0;i<list.size();i++){
			PageData p = list.get(i);
			Object o = p.get("ID");
			if(id.equals(o.toString())){
				result.append(((BigDecimal)p.get("ID")).longValue()+"##");
				//判断上级是否含有2个父节点次，含有2个则为多重结构
				int m = 0;
				PageData parent = null;
				if(i!=0){
					parent = list.get(i-1);
					for(PageData d :list){
						if(d.get("ID").toString().equals(parent.get("ID").toString())){
							m++;
						}
					}
				}
				if(i+1==list.size() && m>=2){
					String x = parent.get("ID").toString();
					int index = result.indexOf(x);
					//在第一个父节点后面加##
					String one = result.substring(0, index+x.length());
					String two = result.substring(index+x.length()+1, result.length());
					result = new StringBuffer();
					result.append(one).append(";").append(id).append("##").append(two);
				}
			}else{
				result.append(((BigDecimal)p.get("ID")).longValue()+";");
			}
		}
		String str = null;
		if(result.length()>0){
			str = result.substring(0, result.length()-2);
		}
		return str;
	}
	
	
	
	/**
	 * 保存本体父节点副本
	 * @param request
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String saveOntoParentTree(HttpServletRequest request, PageData pd) throws Exception{
		String msg = "success";
		String ONTO_ID = pd.getString("ONTO_ID");
		String sqlName = null;
		//本体类型
		String ontoType = pd.getString("ontoType");
		//字段转换
		if(OsynConst.DRUG.equals(ontoType)){
			//药品
		}else if (OsynConst.OP.equals(ontoType)){
			//判断诊断本体中手术名称是否存在
			sqlName = "op";
		}else if (OsynConst.DIAG.equals(ontoType)){
			//诊断
			sqlName = "diag";
		}else if (OsynConst.DEP.equals(ontoType)){
			//诊断
			sqlName = "dep";
		}
		//保存本体副本
		PageData oldOnto = ontologyService.selectOntologyById(sqlName, ONTO_ID);
		//判断是否更改了父节点，如果为更改则返回提示信息
		String[] pids = request.getParameterValues("PARENT_IDS");
		List<String> parentIds = (List<String>)oldOnto.get("PARENT_IDS");
		if(HelpUtil.checkModifyParent(pids,parentIds)){
			return "未修改父节点信息。";
		}
		if(pids!=null && pids.length==1){
			//验证是否是自己
			if(pd.getString("ONTO_ID").equals(pids[0])){
				return "父节点不能选择自己！";
			}
		}
		//字段转换成历史表使用
		if (OsynConst.OP.equals(ontoType)){
			oldOnto.put("D_ID", oldOnto.get("OP_ID"));
			oldOnto.put("DN_ID", oldOnto.get("ON_ID"));
			oldOnto.put("STAD_DN_CHN", oldOnto.get("STAD_OP_CHN"));
			oldOnto.put("STAD_DN_ENG", oldOnto.get("STAD_OP_ENG"));
			//oldOnto.put("MAIN_CODE", oldOnto.get("OP_CODE"));
			oldOnto.put("ORG_STAD_DN_CHN", oldOnto.get("ORG_STAD_OP_CHN"));
			oldOnto.put("ORG_STAD_DN_ENG", oldOnto.get("ORG_STAD_OP_ENG"));
		}else if(OsynConst.DEP.equals(ontoType)){
			oldOnto.put("D_ID", oldOnto.get("ID"));
			oldOnto.put("DN_ID", oldOnto.get("DN_ID"));
			//oldOnto.put("MAIN_CODE", oldOnto.get("DEP_NAME_CODE"));
			oldOnto.put("STAD_DN_CHN", oldOnto.get("DEP_STAD_NAME"));
			oldOnto.put("TERM_DEFIN", oldOnto.get("DEFINITION"));
			oldOnto.put("STAD_DN_ENG", "");
			oldOnto.put("ORG_STAD_DN_CHN", "");
			oldOnto.put("ORG_STAD_DN_ENG", "");
		}
		String H_ID = UuidUtil.get32UUID();
		oldOnto.putAll(pd);
		oldOnto.put("H_ID", H_ID);//历史ID
		oldOnto.put("OP_TYPE", 2);// 操作类型 0 新曾1修改2修改父节点
		oldOnto.put("STATUS", 0);//待审核
		oldOnto.put("ONTO_TYPE", ontoType);//本体类型
		oldOnto.put("UPD_DESC","快捷键修改父节点");
		oldOnto.put("OPERATION","");
		dao.save("OntologyMapper.saveOntoCopy", oldOnto);
		
		//保存父节点副本
		if(pids == null || pids.length==0){
			//为空则视为修改为顶级目录
			int TREE_ID = commonService.getSequenceID(ontoType,2);
			PageData pageData = new PageData();
			pageData.putAll(pd);
			pageData.put("H_ID", UuidUtil.get32UUID());
			pageData.put("TREE_ID", TREE_ID);
			pageData.put("D_ID", ONTO_ID);
			pageData.put("PARENT_ID", "");
			pageData.put("UPD_DESC", "快捷键修改父节点");
			pageData.put("STATUS", 0);//待审核
			pageData.put("OP_TYPE", 2);//操作类型：2修改父节点
			pageData.put("ONTO_TYPE", ontoType);
			pageData.put("ONTO_H_ID", H_ID);
			dao.save ("TreeMapper.saveOntoTreeHis", pageData);
		}else{
			for(String pid:pids){
				int TREE_ID = commonService.getSequenceID(pd.getString("ontoType"),2);
				PageData pageData = new PageData();
				pageData.putAll(pd);
				pageData.put("H_ID", UuidUtil.get32UUID());
				pageData.put("TREE_ID", TREE_ID);
				pageData.put("D_ID", ONTO_ID);
				pageData.put("PARENT_ID", pid);
				pageData.put("UPD_DESC", "快捷键修改父节点");
				pageData.put("STATUS", 0);//待审核
				pageData.put("OP_TYPE", 2);//操作类型：2修改父节点
				pageData.put("ONTO_TYPE", ontoType);
				pageData.put("ONTO_H_ID", H_ID);
				dao.save ("TreeMapper.saveOntoTreeHis", pageData);
			}
		}
		return msg;
	}
}
