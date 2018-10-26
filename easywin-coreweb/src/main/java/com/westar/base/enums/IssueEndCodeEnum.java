package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:问题结束代码
 * @author zzq
 * @date 2018年5月24日 下午8:28:17
 */
public enum IssueEndCodeEnum {
	/**
	 * 根本解决
	 */
	RESOLVED("根本解决", "1"),
	/**
	 * 变通方法
	 */
	CHANGEWAY("变通方法", "2"),
	/**
	 * 关闭
	 */
	STEILL("无法解决", "3"),
	/**
	 * 取消
	 */
	CANCEL("取消", "4");
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private IssueEndCodeEnum(String desc, String value) {
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

	public static IssueEndCodeEnum getEnum(String value) {
		IssueEndCodeEnum resultEnum = null;
		IssueEndCodeEnum[] enumAry = IssueEndCodeEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		IssueEndCodeEnum[] ary = IssueEndCodeEnum.values();
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
		IssueEndCodeEnum[] ary = IssueEndCodeEnum.values();
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
		IssueEndCodeEnum[] enums = IssueEndCodeEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (IssueEndCodeEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
