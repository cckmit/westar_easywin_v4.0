package com.westar.base.pojo;

import java.util.List;

/**
 * 模块统计
 * @author H87
 *
 */
public class ModStaticVo {
	
	//统计的数目
	private Integer value;
	//统计类型
	private String name;
	//类型代码(或类型id)
	private String type;
	//统计的子类信息
	private  List<ModStaticVo> childModStaticVo;
	
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ModStaticVo> getChildModStaticVo() {
		return childModStaticVo;
	}
	public void setChildModStaticVo(List<ModStaticVo> childModStaticVo) {
		this.childModStaticVo = childModStaticVo;
	}


	@Override
	public String toString() {
		return "ModStaticVo{" + "value=" + value + ", name='" + name + '\'' + ", type='" + type + '\'' + ", childModStaticVo=" + childModStaticVo + '}';
	}
}
