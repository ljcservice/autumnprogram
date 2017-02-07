package com.ts.controller.ontology;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.controller.base.BaseController;
import com.ts.service.ontology.manager.OntoTreeManager;
import com.ts.service.ontology.manager.OntologyManager;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;
import com.ts.util.ontology.CodeUtil;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称：本体数管理
 * 创建人：xsl
 * 更新时间：2016年10月10日
 * @version
 */
@Controller
@RequestMapping(value="/ontoTree")
public class OntoTreeController extends BaseController {
	
	@Resource(name="ontoTreeService")
	private OntoTreeManager ontoTreeService;
	
	@Resource(name="ontologyService")
	private OntologyManager ontologyService;
	
	@RequestMapping(value="/tree")
	public void tree(String type,HttpServletResponse response)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> treeList = ontoTreeService.treeRootList(pd);
		//默认都显示 + 符号
		if(treeList!=null){
			for(PageData p:treeList){
				p.put("isParent", true);
			}
		}
		JSONArray arr = JSONArray.fromObject(treeList);
		// 替换为Ztree使用的参数TREE_ID
		String json = arr.toString();
		json = json.replaceAll("ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("NAME", "name").replaceAll("SUBMENU", "children").replaceAll("ISPARENT", "isParent");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
	}
	
	
	@RequestMapping(value="/treePidsById")
	public void treePidsById(String type,HttpServletResponse response)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = ontoTreeService.treePidsById(pd);
		JSONObject s = new JSONObject();
		s.put("result", result);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(s.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
	}
	/**
	 * 诊断当前节点是否可扩展，success 为可扩展
	 * 1. 诊断最多只能扩展2个编码 (如果2个编码必须1个是主要编码，1个是附加编码) , 父节点肯定不会超过2个， 页面要限制
	 * 2. //本条暂时废弃 附加编码有M98900/3的格式不扩码， 主要编码末尾可能为+号，附加编码末尾可能是*号  
	 * 3. 诊断只能挂2个编码 (必须1个是主要编码，1个是附加编码)  或者 1个编码 （ 主要或者附加）, 父节点肯定不会超过2个	
	 * 4. 只能在6位码下扩 (如果3位码即类目下无6位子节点无亚目时，可以扩；如果4位码即亚目下无6位子节点时，可以扩)	
	 * 5. * + 号必须成对出现,
	 * 
	 * 目前进入此方法的都是编码为3或4 位的节点
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/enableExtend")
	@ResponseBody
	public Object enableExtend()throws Exception{
		String errInfo = "success";
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			//本体类型
			String ontoType = pd.getString("ontoType");
			//查询当前节点的所有子节点，
			List<PageData> treeList = ontoTreeService.treeRootList(pd);
			for(PageData p:treeList){
				if(OsynConst.DIAG.equals(ontoType) ){
					//查询当前本体
					PageData ontology = ontologyService.queryOntologyById("diag", pd.getString("ID"));
					String main = CodeUtil.getAvalidIcdCode(p.getString("MAIN_CODE"));
					String add = CodeUtil.getAvalidIcdCode(p.getString("ADD_CODE"));
					//本体主要编码不为空，验证下级主要编码
					if(!Tools.isEmpty(ontology.getString("MAIN_CODE"))){
						//不包含亚目，不包含6位规范词，可可扩展
						if(!Tools.isEmpty(main) && ( main.length()==5 || main.length()==7 )){
							errInfo="父节点主要编码只能为6位码或者3、4位码,3、4位码时必须是叶子节点";
							break;
						}
					}
					//本体附加编码不为空，验证下级附加编码
					if(!Tools.isEmpty(ontology.getString("ADD_CODE"))){
						if(!Tools.isEmpty(add) && ( add.length()==5 || add.length()==7 )){
							errInfo="父节点附加编码只能为6位码或者3、4位码,3、4位码时必须是叶子节点";
							break;
						}
					}
				}else if (OsynConst.OP.equals(ontoType) ){
					String main = p.getString("MAIN_CODE");
					//不包含亚目，不包含6位规范词，可可扩展
					if(!Tools.isEmpty(main) && ( main.length()==5 )){
						errInfo="父节点主要编码只能为4位码或者3位码,3位码时必须是叶子节点";
						break;
					}
				}else if (OsynConst.DEP.equals(ontoType) ){
					
				}
			}
			
		} catch (Exception e) {
			errInfo="连接超时，请刷新页面重试！";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	} 
}
