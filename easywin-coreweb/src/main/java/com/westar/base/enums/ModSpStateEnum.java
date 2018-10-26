package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:模块审批状态
 * @author zzq
 * @date 2018年3月23日 上午9:18:26
 */
public enum ModSpStateEnum {
	/**
	 * 不审批
	 */
	NONE("none", 0),
	/**
	 * 审批中
	 */
	DOING("doing", 1),
	/**
	 * 审核通过完结
	 */
	FIHISH("finish", 4),
	/**
	 * 审核不通过完结
	 */
	REFUSE("refuse", -1),
	/*******以上为存数据库需要的*************/
	/**
	 * 下一步
	 */
	NEXT("next", 6),
	/**
	 * 会签
	 */
	HUIQIAN("huiqian", 7),
	/**
	 * 会签中
	 */
	HUIQIANING("huiqianing", 8);
	
	/** 枚举值 */
	private Integer value;
	/** 描述 */
	private String desc;

	private ModSpStateEnum(String desc, Integer value) {
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

	public static ModSpStateEnum getEnum(Integer value) {
		ModSpStateEnum resultEnum = null;
		ModSpStateEnum[] enumAry = ModSpStateEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		ModSpStateEnum[] ary = ModSpStateEnum.values();
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
		ModSpStateEnum[] ary = ModSpStateEnum.values();
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
		ModSpStateEnum[] enums = ModSpStateEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (ModSpStateEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
