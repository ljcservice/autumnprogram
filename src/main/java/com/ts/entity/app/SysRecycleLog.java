package com.ts.entity.app;
/**
 * 
 * ClassName: SysRecycleLog
 * @Description: 日志实体
 * @author 李世博
 * @date 2016年9月25日
 */
public class SysRecycleLog {
	
	private String LOG_ID;
	private String INER_TYPE;
	private String INPUT;
	private String OUTPUT;
	private String USER_IP;
	private String USER_ID;
	private String CALL_DATE;
	private String CODE;

	public String getLOG_ID() {
		return LOG_ID;
	}
	public void setLOG_ID(String lOG_ID) {
		LOG_ID = lOG_ID;
	}
	public String getINER_TYPE() {
		return INER_TYPE;
	}
	public void setINER_TYPE(String iNER_TYPE) {
		INER_TYPE = iNER_TYPE;
	}
	public String getINPUT() {
		return INPUT;
	}
	public void setINPUT(String iNPUT) {
		INPUT = iNPUT;
	}
	public String getOUTPUT() {
		return OUTPUT;
	}
	public void setOUTPUT(String oUTPUT) {
		OUTPUT = oUTPUT;
	}
	public String getUSER_IP() {
		return USER_IP;
	}
	public void setUSER_IP(String uSER_IP) {
		USER_IP = uSER_IP;
	}
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getCALL_DATE() {
		return CALL_DATE;
	}
	public void setCALL_DATE(String cALL_DATE) {
		CALL_DATE = cALL_DATE;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	
	

}
