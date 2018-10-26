package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:报销方式
 * @author zzq
 * @date 2018年4月26日 上午10:16:18
 */
public enum LoanReportWayEnum {
	/**
	 * 汇报后报销
	 */
	AFTERREPORT("汇报后报销", "1"),
	/**
	 * 直接报销
	 */
	DIRECT("直接报销", "2"),
	/**
	 * 关联模块报销
	 */
	RELATEITEM("关联模块报销", "3");
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private LoanReportWayEnum(String desc, String value) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static LoanReportWayEnum getEnum(String value) {
		LoanReportWayEnum resultEnum = null;
		LoanReportWayEnum[] enumAry = LoanReportWayEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		LoanReportWayEnum[] ary = LoanReportWayEnum.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = getEnum(ary[num].getValue()).toString();
			map.put("value", String.valueOf(ary[num].getValue()));
			map.put("desc", ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		LoanReportWayEnum[] ary = LoanReportWayEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", String.valueOf(ary[i].getValue()));
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	/**
	 * 取枚举的json字符串
	 * 
	 * @return
	 */
	public static String getJsonStr() {
		LoanReportWayEnum[] enums = LoanReportWayEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (LoanReportWayEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
