package com.ts.service.pdss.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPdss;
import com.ts.entity.Page;
import com.ts.entity.pdss.DirectionImage;
import com.ts.service.pdss.ShowService;
import com.ts.util.PageData;

@Service(value="showService")
public class ShowServiceImpl implements ShowService{
	
	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	
	@SuppressWarnings("unchecked")
	public List<PageData> treeRootList(PageData pd) throws Exception{
		List<PageData> list = null;
		String type = pd.getString("ONTO_TYPE");
		if("0".equals(type)){//药品说明书
			if("1".equals(pd.getString("IS_LEAF"))){
				list = (List<PageData>)dao.findForList("ShowMapper.drugTreeList2", pd);
			}else{
				list = (List<PageData>)dao.findForList("ShowMapper.drugTreeList", pd);
			}
		}else if ("1".equals(type)){//个性化给药
			list = (List<PageData>)dao.findForList("ShowMapper.queryIndividualDict", pd);
		}else if ("2".equals(type)){//抗菌药物临床应用指导原则
			pd.put("CODE", 11);
			list = (List<PageData>)dao.findForList("ShowMapper.drugTreeList", pd);
		}else if ("3".equals(type)){//医学常用计算公式
			pd.put("CODE", 12);
			list = (List<PageData>)dao.findForList("ShowMapper.drugTreeList", pd);
		}else if ("4".equals(type)){//医药法规
			pd.put("CODE", 13);
			list = (List<PageData>)dao.findForList("ShowMapper.drugTreeList", pd);
		}else if ("5".equals(type)){//临床检验正常值及意义
			pd.put("CODE", 100);
			list = (List<PageData>)dao.findForList("ShowMapper.drugTreeList", pd);
		}else if ("6".equals(type)){//临床路径
			pd.put("id",pd.get("ID").toString());
			list = (List<PageData>)dao.findForList("ShowMapper.categoryTreeList", pd);
		}
		return list;
	}

	public List<PageData> queryList(Page page)throws Exception{
		List<PageData> list = null;
		list = (List<PageData>)dao.findForList("ShowMapper.drugDirectionPage", page);

		return list;
	}

	public List<PageData> drugDirectionDetail(PageData pd)throws Exception{
		List<PageData> list = null;
		String type = pd.getString("ONTO_TYPE");
		if("0".equals(type)){//药品说明书
			list = (List<PageData>)dao.findForList("ShowMapper.drugDirectionDetail", pd);
		}else if ("1".equals(type)){//个性化给药
		}else if ("2".equals(type)){//抗菌药物临床应用指导原则
		}else if ("3".equals(type)){//医学常用计算公式
		}else if ("4".equals(type)){//医药法规
		}else if ("5".equals(type)){//临床检验正常值及意义
		}else if ("6".equals(type)){//临床路径
		}
		return list;
	}
	/**
	 * 检验项目
	 */
	public List<PageData> queryIndividualItem(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ShowMapper.queryIndividualItem", pd);
	}
	/**
	 * 血液浓度采集时期
	 */
	public List<PageData> queryCollectDescDict(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ShowMapper.queryCollectDescDict", pd);
	}
	/**
	 * 血液浓度范围
	 */
	public List<PageData> queryConsistencyRange(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ShowMapper.queryConsistencyRange", pd);
	}

	public List<PageData> queryDrugRelrefDirection(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ShowMapper.queryDrugRelrefDirection", pd);
	}

	public List<PageData> queryClinicalPathwayInfo(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ShowMapper.queryClinicalPathwayInfo", pd);
	}

	public DirectionImage drugDirectionImage(PageData pd)throws Exception{
		return (DirectionImage)dao.findForObject("ShowMapper.drugDirectionImage", pd);
	}
}
