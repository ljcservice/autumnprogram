package com.ts.service.ontology.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**本体处理接口
 * @author xsl
 */
public interface OntologyManager {


	/**
	 * 本体列表
	 * @param page
	 * @param sqlName
	 * @return
	 * @throws Exception
	 */
	public List<PageData> ontologyPage(Page page,String sqlName) throws Exception;
	
	/**
	 * 保存本体 副本
	 * @param pd 所有数据
	 * @param ontoType 本体类型
	 * @param  type 新增火修改
	 * @throws Exception
	 */
	public String saveOntoCopy(HttpServletRequest request ,PageData pd ,int type)  throws Exception;
	
	/**
	 * 根据历史ID 查询本体
	 * @param sqlName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageData selectOntologyById(String sqlName, String id) throws Exception ;
	
	/**
	 * 查询本体信息，不包含父节点
	 * @param sqlName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageData queryOntologyById(String sqlName, String id) throws Exception ;
	
	/**
	 * 获取树上级信息
	 * @param sqlName
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> queryParentOntoById(String sqlName, String string) throws Exception ;
	
	/**
	 * 获取历史树上级信息
	 * @param sqlName
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> queryParentOntoHisInfo(String sqlName, PageData pd) throws Exception ;
	
	/**
	 * 根据历史ID 查询本体历史
	 * @param sqlName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageData selectOntologyHisById(String sqlName, String id) throws Exception ;
	
	/**
	 * 审核列表
	 * @param page
	 * @param sqlName
	 * @return
	 * @throws Exception
	 */
	public List<PageData> ontoCheckPage(Page page, String sqlName) throws Exception;

	/**
	 * 查询单个历史信息，根据本体ID
	 * @param pd
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> selectOntologyHisByOntoId(PageData pd) throws Exception;
	
	/**
	 * 单个审核拒绝
	 * @param pd
	 * @throws Exception
	 */
	public String updateCheckRefuse(PageData pd) throws Exception;

	/**
	 * 单个审核通过
	 * @param pd
	 * @throws Exception
	 */
	public String updateCheckPass(HttpServletRequest request,PageData pd) throws Exception;


	/**
	 * 批量审核通过
	 * @param pd
	 * @throws Exception
	 */
	public String updateCheckPassAll(PageData pd) throws Exception;

	/**
	 * 批量审核拒绝
	 * @param pd
	 * @throws Exception
	 */
	public String updateCheckRefuseAll(PageData pd) throws Exception;

	/**
	 * 查询本体历史
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> queryOntoHisPage(String sqlName,Page pd) throws Exception;

	/**
	 * 查询单个本体的所有历史
	 * @param sqlName
	 * @param pd
	 * @return
	 */
	public List<PageData> queryOntoHisDetail(String sqlName, PageData pd) throws Exception;

	/**
	 * 快捷键-停用本体
	 * @param pd
	 * @throws Exception
	 */
	public void stopOntology(PageData pd) throws Exception;

	/**
	 * 诊断维护页面，科室分类选择
	 * @param sqlName
	 * @param oNTO_ID
	 * @return
	 * @throws Exception
	 */
	public List<PageData> queryDepCategory( String oNTO_ID) throws Exception;

	/**
	 * 诊断审核页面，科室分类选择
	 * @param sqlName
	 * @param oNTO_ID
	 * @return
	 * @throws Exception
	 */
	public List<PageData> queryDepCategoryHis(PageData pd) throws Exception;
	
	/**
	 * 知识本体编辑保存
	 * @param pd
	 * @throws Exception
	 */
	public void knowledgeOntoEdit(PageData pd) throws Exception;
	/**
	 * 本体导入
	 * @param xwk excel工作区
	 * @param ontoType 本体类型
	 * @param isCheck 是否需要审核
	 * @return
	 * @throws Exception
	 */
	public String importOntology(Workbook xwk,PageData pd) throws Exception;

	/**
	 * 查询上一条历史信息，以本体ID 和 更新时间为条件
	 * @param sqlName
	 * @param newOnto
	 * @return
	 * @throws Exception
	 */
	public String queryBrotherHisInfo( PageData newOnto) throws Exception;
	
}
