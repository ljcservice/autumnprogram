package com.ts.entity.app;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ts.entity.system.Role;

/**
 * 
 * ClassName: SysAppUser
 * 
 * @Description: API用户
 * @author 李世博
 * @date 2016年9月13日
 */
public class SysAppUser {

	private String USER_ID;
	private String USERNAME;
	private String PASSWORD;
	private String NAME;
	private String LAST_LOGIN;
	private String IP;
	private String AREA_ID;
	private String reuqestIp;
	private List<Role> role;
	private Set<String> apitype;// 可访问的业务
	private Map<String, Set<String>> showfield;// 可显示字段
	private Map<String, Map<String, Set<String>>> jurisdiction;// 可访问业务对应的
																// 可显示字段对应的数据权限集合

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getLAST_LOGIN() {
		return LAST_LOGIN;
	}

	public void setLAST_LOGIN(String lAST_LOGIN) {
		LAST_LOGIN = lAST_LOGIN;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getAREA_ID() {
		return AREA_ID;
	}

	public void setAREA_ID(String aREA_ID) {
		AREA_ID = aREA_ID;
	}

	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}

	public String getReuqestIp() {
		return reuqestIp;
	}

	public void setReuqestIp(String reuqestIp) {
		this.reuqestIp = reuqestIp;
	}

	public Set<String> getApitype() {
		return apitype;
	}

	public void setApitype(Set<String> apitype) {
		this.apitype = apitype;
	}

	public Map<String, Set<String>> getShowfield() {
		return showfield;
	}

	public void setShowfield(Map<String, Set<String>> showfield) {
		this.showfield = showfield;
	}

	public Map<String, Map<String, Set<String>>> getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(Map<String, Map<String, Set<String>>> jurisdiction) {
		this.jurisdiction = jurisdiction;
	}
}
