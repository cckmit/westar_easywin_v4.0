package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:任务逾期状态
 * @author zzq
 * @date 2018年7月6日 上午9:50:32
 */
public enum OverDueLevelEnum {
	/**
	 * 已逾期
	 */
	LEVELRED("已逾期", "red"),
	/**
	 * 三天内将到期
	 */
	LEVELYELLOW("将逾期", "yellow"),
	/**
	 * 未逾期
	 */
	LEVELGREEN("未逾期", "green"),
	/**
	 * 不统计
	 */
	LEVELGRAY("不统计", "gray"),;
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private OverDueLevelEnum(String desc, String value) {
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

	public static OverDueLevelEnum getEnum(String value) {
		OverDueLevelEnum resultEnum = null;
		OverDueLevelEnum[] enumAry = OverDueLevelEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		OverDueLevelEnum[] ary = OverDueLevelEnum.values();
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
		OverDueLevelEnum[] ary = OverDueLevelEnum.values();
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
		OverDueLevelEnum[] enums = OverDueLevelEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (OverDueLevelEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
