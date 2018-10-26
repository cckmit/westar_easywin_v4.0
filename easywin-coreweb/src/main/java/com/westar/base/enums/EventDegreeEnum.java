package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:事项级别
 * @author zzq
 * @date 2018年5月24日 下午8:28:03
 */
public enum EventDegreeEnum {
	/**
	 * 一级事件
	 */
	LEVELONE("一级事件", "1"),
	/**
	 * 二级事件
	 */
	LEVELTWO("二级事件", "2"),
	/**
	 * 三级事件
	 */
	LEVELTHREE("三级事件", "3"),
	/**
	 * 四级事件
	 */
	LEVELFOUR("四级事件", "4");
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private EventDegreeEnum(String desc, String value) {
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

	public static EventDegreeEnum getEnum(String value) {
		EventDegreeEnum resultEnum = null;
		EventDegreeEnum[] enumAry = EventDegreeEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		EventDegreeEnum[] ary = EventDegreeEnum.values();
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
		EventDegreeEnum[] ary = EventDegreeEnum.values();
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
		EventDegreeEnum[] enums = EventDegreeEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (EventDegreeEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
