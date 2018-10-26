package com.westar.base.pojo;

import java.io.Serializable;

/**
 * 
 * 描述:模块审批信息
 * @author zzq
 * @date 2018年3月20日 下午4:10:06
 */
public class ModSpConf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1812881104133243782L;
	//执行人
	private Integer executor;
	//当前步骤类型
	private String stepType;
	//当前步骤是否有会签信息
	private String hasHuiqianState;
	
	private String content;

	public Integer getExecutor() {
		return executor;
	}

	public void setExecutor(Integer executor) {
		this.executor = executor;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public String getHasHuiqianState() {
		return hasHuiqianState;
	}

	public void setHasHuiqianState(String hasHuiqianState) {
		this.hasHuiqianState = hasHuiqianState;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
