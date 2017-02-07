package com.ts.entity.system;

import java.io.Serializable;
import java.util.List;

/**
 * 
* 类名称：角色
* 类描述： 
* @author 
* 作者单位： 
* 联系方式：
* 创建时间：2014年3月10日
* @version 1.0
 */
public class Role implements Serializable{
	
	private String ROLE_ID;
	private String ROLE_NAME;
	private String PARENT_ID;
	private String INTRODUCTION;
	private Integer ROLE_TYPE;
	
	private String ROLE_URL;
	private String target;
	private Role parentRole;
	private List<Role> subRole;
	private boolean hasRole = false;
	private boolean chk = false;
	private boolean noCheck = false;
	private boolean open = false;
	
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	public String getROLE_NAME() {
		return ROLE_NAME;
	}
	public void setROLE_NAME(String rOLE_NAME) {
		ROLE_NAME = rOLE_NAME;
	}
	public String getPARENT_ID() {
		return PARENT_ID;
	}
	public void setPARENT_ID(String pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}
	public String getINTRODUCTION() {
		return INTRODUCTION;
	}
	public void setINTRODUCTION(String iNTRODUCTION) {
		INTRODUCTION = iNTRODUCTION;
	}
	public Integer getROLE_TYPE() {
		return ROLE_TYPE;
	}
	public void setROLE_TYPE(Integer rOLE_TYPE) {
		ROLE_TYPE = rOLE_TYPE;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getROLE_URL() {
		return ROLE_URL;
	}
	public void setROLE_URL(String rOLE_URL) {
		ROLE_URL = rOLE_URL;
	}
	public Role getParentRole() {
		return parentRole;
	}
	public void setParentRole(Role parentRole) {
		this.parentRole = parentRole;
	}
	public List<Role> getSubRole() {
		return subRole;
	}
	public void setSubRole(List<Role> subRole) {
		this.subRole = subRole;
	}
	public boolean isHasRole() {
		return hasRole;
	}
	public void setHasRole(boolean hasRole) {
		this.hasRole = hasRole;
	}
	public boolean isChk() {
		return chk;
	}
	public void setChk(boolean chk) {
		this.chk = chk;
	}
	public boolean isNoCheck() {
		return noCheck;
	}
	public void setNoCheck(boolean noCheck) {
		this.noCheck = noCheck;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	
}