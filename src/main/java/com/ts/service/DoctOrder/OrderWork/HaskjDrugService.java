package com.ts.service.DoctOrder.OrderWork;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

public interface HaskjDrugService {

	List<PageData> haskjDrug11(Page page)throws Exception ;
	List<PageData> haskjDrug11ByHJ(Page page)throws Exception ;
	List<PageData> haskjDrug12(Page page)throws Exception ;
	List<PageData> haskjDrug13(Page page)throws Exception ;
	List<PageData> haskjDrug13ByHJ(Page page)throws Exception ;
	List<PageData> haskjDrug14(Page page)throws Exception ;
	List<PageData> haskjDrug21(Page page)throws Exception ;
	List<PageData> haskjDrug22(Page page)throws Exception ;
}
