package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:流程人员类型
 * @author zzq
 * @date 2018年1月29日 下午2:37:26
 */
public enum ActLinkTypeEnum {
	/**
	 * 办理人
	 */
	ASSIGNEE("办理人", "assignee"),
	/**
	 * 拥有人
	 */
	OWNER("拥有人", "owner"),
	/**
	 * 候选人
	 */
	CANDIDATE("候选人", "candidate"),
	/**
	 * 会签人
	 */
	HUIQIAN("会签人", "huiqian"),
	/**
	 * 会签中
	 */
	HUIQIANING("会签中", "huiqianing"),
	/**
	 * 参与人
	 */
	PARTICIPANT("参与人", "participant");
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private ActLinkTypeEnum(String desc, String value) {
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

	public static ActLinkTypeEnum getEnum(String value) {
		ActLinkTypeEnum resultEnum = null;
		ActLinkTypeEnum[] enumAry = ActLinkTypeEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		ActLinkTypeEnum[] ary = ActLinkTypeEnum.values();
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
		ActLinkTypeEnum[] ary = ActLinkTypeEnum.values();
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
		ActLinkTypeEnum[] enums = ActLinkTypeEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (ActLinkTypeEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
