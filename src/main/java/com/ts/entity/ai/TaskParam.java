package com.ts.entity.ai;

import java.io.Serializable;
import java.util.Date;

import com.ts.entity.Page;

/**
 * 任务配置实体
 * @ClassName: TaskParam 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy
 * @date 2016年10月25日 上午10:54:52 
 *
 */
public class TaskParam implements Serializable{
	
	private static final long serialVersionUID = 5973418227530760720L;
	
	private Integer P_ID;		//主键id
	private Integer TASK_TYPE_ID;	//任务类型 来自字典表
	private Integer TASK_TYPE_CHILD_ID; 	//子任务类型 来自MTS返回值
	private String EXP_ONE_ROLE;		//一审角色
	private String EXP_TWO_ROLE;	//二审角色
	private String STATUS;			//	状态 0 有效  1 失效
	private String CREATE_MAN;		//创建人
	private Date CREATE_TIME;		//创建时间
	private String UPDATE_MAN;		//修改人
	private Date UPDATE_TIME;		//修改时间
	
	private Page page;			//分页对象

	public Integer getP_ID() {
		return P_ID;
	}

	public void setP_ID(Integer p_ID) {
		P_ID = p_ID;
	}

	
	public Integer getTASK_TYPE_ID() {
		return TASK_TYPE_ID;
	}

	public void setTASK_TYPE_ID(Integer tASK_TYPE_ID) {
		TASK_TYPE_ID = tASK_TYPE_ID;
	}

	public Integer getTASK_TYPE_CHILD_ID() {
		return TASK_TYPE_CHILD_ID;
	}

	public void setTASK_TYPE_CHILD_ID(Integer tASK_TYPE_CHILD_ID) {
		TASK_TYPE_CHILD_ID = tASK_TYPE_CHILD_ID;
	}

	public String getEXP_ONE_ROLE() {
		return EXP_ONE_ROLE;
	}

	public void setEXP_ONE_ROLE(String eXP_ONE_ROLE) {
		EXP_ONE_ROLE = eXP_ONE_ROLE;
	}

	public String getEXP_TWO_ROLE() {
		return EXP_TWO_ROLE;
	}

	public void setEXP_TWO_ROLE(String eXP_TWO_ROLE) {
		EXP_TWO_ROLE = eXP_TWO_ROLE;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}


	public String getCREATE_MAN() {
		return CREATE_MAN;
	}

	public void setCREATE_MAN(String cREATE_MAN) {
		CREATE_MAN = cREATE_MAN;
	}

	public String getUPDATE_MAN() {
		return UPDATE_MAN;
	}

	public void setUPDATE_MAN(String uPDATE_MAN) {
		UPDATE_MAN = uPDATE_MAN;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Date getCREATE_TIME() {
		return CREATE_TIME;
	}

	public void setCREATE_TIME(Date cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}

	public Date getUPDATE_TIME() {
		return UPDATE_TIME;
	}

	public void setUPDATE_TIME(Date uPDATE_TIME) {
		UPDATE_TIME = uPDATE_TIME;
	}

	
	
}
