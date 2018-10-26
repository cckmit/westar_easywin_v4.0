package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:流程提交的操作类型
 * @author zzq
 * @date 2018年1月29日 下午2:37:26
 */
public enum WrokFlowOptEnum {
	/**
	 * 添加
	 */
	ADD("添加", "add"),
	/**
	 * 修改
	 */
	UPDATE("修改", "update"),
	/**
	 * 回退
	 */
	BACK("回退", "back");
	
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private WrokFlowOptEnum(String desc, String value) {
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

	public static WrokFlowOptEnum getEnum(String value) {
		WrokFlowOptEnum resultEnum = null;
		WrokFlowOptEnum[] enumAry = WrokFlowOptEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		WrokFlowOptEnum[] ary = WrokFlowOptEnum.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put("value", String.valueOf(ary[num].getValue()));
			map.put("desc", ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		WrokFlowOptEnum[] ary = WrokFlowOptEnum.values();
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
		WrokFlowOptEnum[] enums = WrokFlowOptEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (WrokFlowOptEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
