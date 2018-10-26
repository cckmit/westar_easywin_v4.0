package com.westar.base.pojo;

import java.io.Serializable;

/**
 * 系统后台返回显示页面后的弹窗消息
 */
public class Notification implements Serializable{
	
	//信息
	public final static int SUCCESS = 1;
	
	//失败
	public final static int ERROR = 2;

	//消息类型
	private Integer type;
	
	//消息类容
	private String content;
	
	public Notification(Integer type,String content){
		this.type=type;
		this.content=content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
