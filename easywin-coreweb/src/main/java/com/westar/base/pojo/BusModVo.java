package com.westar.base.pojo;

/**
 * 
 * 描述:业务模块信息
 * @author zzq
 * @date 2018年7月2日 上午11:47:01
 */
public class BusModVo {
	//业务主键
	private Integer busId;
	//业务类型
	private String busType;
	//子模块业务主键
	private String subBusId;
	//模块蘑菇昵称
	private String modName;
	
	public BusModVo(){
		
	}
	
	public BusModVo(Integer busId,String busType,String modName){
		this.busId = busId;
		this.busType = busType;
		this.modName = modName;
	}
	public Integer getBusId() {
		return busId;
	}
	public void setBusId(Integer busId) {
		this.busId = busId;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getModName() {
		return modName;
	}
	public void setModName(String modName) {
		this.modName = modName;
	}

	public String getSubBusId() {
		return subBusId;
	}

	public void setSubBusId(String subBusId) {
		this.subBusId = subBusId;
	}
}
