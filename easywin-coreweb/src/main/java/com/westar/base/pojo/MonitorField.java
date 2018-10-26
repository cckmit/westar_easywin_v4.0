package com.westar.base.pojo;

import java.io.Serializable;

//计算控件的字段
public class MonitorField implements Serializable{
	//计算对象
	private String value;
	//计算类型
	private String type;
	
	//操作信息
	private String operate;
	//浮动数
	private String number;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
