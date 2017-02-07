package com.ts.entity.app;

/**
 * 
 * ClassName: AppToken 
 * @Description: 令牌实体
 * @author 李世博
 * @date 2016年9月28日
 */
public class AppToken {
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getExample_parameter() {
		return example_parameter;
	}
	public void setExample_parameter(String example_parameter) {
		this.example_parameter = example_parameter;
	}
	public SysAppUser getAppUser() {
		return appUser;
	}
	public void setAppUser(SysAppUser appUser) {
		this.appUser = appUser;
	}
	private String status;
	private String access_token;
	private String token_type;
	private long expires_in;
	private String refresh_token;
	private String example_parameter;
	private SysAppUser appUser;

}
