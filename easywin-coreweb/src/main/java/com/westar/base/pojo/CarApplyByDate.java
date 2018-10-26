package com.westar.base.pojo;

import java.util.List;

import com.westar.base.model.CarUseRecord;

public class CarApplyByDate {
	//日期
	private String dateStr;
	//申请记录
	private List<CarUseRecord> listCarUseRecord;
	
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public List<CarUseRecord> getListCarUseRecord() {
		return listCarUseRecord;
	}
	public void setListCarUseRecord(List<CarUseRecord> listCarUseRecord) {
		this.listCarUseRecord = listCarUseRecord;
	}
	
	
}
