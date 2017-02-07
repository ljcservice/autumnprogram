package com.ts.entity.system;

import java.io.Serializable;

/**
 * 
* 类名称：菜单功能
* 类描述： 
* @author 
* 作者单位： 
* 联系方式：
* 创建时间：2016年9月18日
* @version 2.0
 */
public class MenuFun implements Serializable{
	
	private String MF_ID;			//功能ID
	private String MENU_ID;			//菜单ID
	private String FUN_CODE;		//编码	
	private String INTRODUCTION;	//功能描述
	private String REMARK;			//备注
	
	public String getMF_ID() {
		return MF_ID;
	}
	public void setMF_ID(String mF_ID) {
		MF_ID = mF_ID;
	}
	public String getMENU_ID() {
		return MENU_ID;
	}
	public void setMENU_ID(String mENU_ID) {
		MENU_ID = mENU_ID;
	}
	public String getFUN_CODE() {
		return FUN_CODE;
	}
	public void setFUN_CODE(String fUN_CODE) {
		FUN_CODE = fUN_CODE;
	}
	public String getINTRODUCTION() {
		return INTRODUCTION;
	}
	public void setINTRODUCTION(String iNTRODUCTION) {
		INTRODUCTION = iNTRODUCTION;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	
}
