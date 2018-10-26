package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:需求过程管理的阶段状态
 * @author zzq
 * @date 2018年10月10日 上午9:16:15
 */
public enum DemandStateEnum {
	/**
	 * 待签收
	 */
	DEFAULT("待签收", "1"),
	/**
	 * 分析中
	 */
	ANALYSIS("待处理", "2"),
	/**
	 * 拒绝
	 */
	REJECT("拒绝", "-1"),
	/**
	 * 处理中
	 */
	HANDLING("处理中", "3"),
	/**
	 * 确认
	 */
	CONFIRM("待确认", "4"),
	/**
	 * 结束
	 */
	FINISH("结束", "5");
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private DemandStateEnum(String desc, String value) {
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

	public static DemandStateEnum getEnum(String value) {
		DemandStateEnum resultEnum = null;
		DemandStateEnum[] enumAry = DemandStateEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		DemandStateEnum[] ary = DemandStateEnum.values();
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
		DemandStateEnum[] ary = DemandStateEnum.values();
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
		DemandStateEnum[] enums = DemandStateEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (DemandStateEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
