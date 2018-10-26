package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:是否为出差费用
 * @author zzq
 * @date 2018年6月22日 下午1:43:48
 */
public enum TripFeeEnum {
	/**
	 * 是
	 */
	YES("是", 1),
	/**
	 * 否
	 */
	NO("否", 0);
	
	/** 枚举值 */
	private Integer value;
	/** 描述 */
	private String desc;

	private TripFeeEnum(String desc, Integer value) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static TripFeeEnum getEnum(Integer value) {
		TripFeeEnum resultEnum = null;
		TripFeeEnum[] enumAry = TripFeeEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		TripFeeEnum[] ary = TripFeeEnum.values();
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
		TripFeeEnum[] ary = TripFeeEnum.values();
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
		TripFeeEnum[] enums = TripFeeEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (TripFeeEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
