package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.service.DoctOrder.OrderWork.JmsyService;
import com.ts.util.PageData;

@Service(value="jmsyService")
public class JmsyServiceImpl implements JmsyService {
	@Resource(name="daoSupportPH")
	private DAO daoph;
	
	@Override
	public List<PageData> jmsy11(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("JmsyMapper.jmsy11", pd);
	}
	@Override
	public List<PageData> jmsy12(PageData pd)throws Exception {
		return (List<PageData>) daoph.findForList("JmsyMapper.jmsy12", pd);
	}

	@Override
	public List<PageData> jmsy21(PageData pd)throws Exception {
		return (List<PageData>) daoph.findForList("JmsyMapper.jmsy21", pd);
	}

	@Override
	public List<PageData> jmsy22(PageData pd)throws Exception {
		return (List<PageData>) daoph.findForList("JmsyMapper.jmsy22", pd);
	}
}
