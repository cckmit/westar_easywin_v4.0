package com.westar.base.pojo;

import java.util.List;

/**
*区域表
*/
public class Area4App{

/*主键id*/
 private Integer id;
 /*区域名称*/
 private String areaName;
 
 //区域子集
 private List<Area4App> children;

 /**
 * 主键id
 * @param id
 */
 public void setId(Integer id) {
 	this.id=id;
 }

 /**
 * 主键id
 * @return
 */
 public Integer getId() {
 	return id;
 }

 /**
 *区域名称
 * @param areaName
 */
 public void setAreaName(String areaName) {
 	this.areaName = areaName;
 }

 /**
 *区域名称
 * @return
 */
 public String getAreaName() {
 	return areaName;
 }

public List<Area4App> getChildren() {
	return children;
}

public void setChildren(List<Area4App> children) {
	this.children = children;
}
}
