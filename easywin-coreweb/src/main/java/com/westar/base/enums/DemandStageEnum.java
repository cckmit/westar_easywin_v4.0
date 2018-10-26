package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:需求过程管理的阶段
 * @author zzq
 * @date 2018年10月10日 上午9:16:15
 */
public enum DemandStageEnum {
	/**
	 * 创建需求
	 */
	ADD("创建需求", "1"),
	/**
	 * 拒绝受理
	 */
	REJECT("拒绝受理", "-1"),
	/**
	 * 审核
	 */
	ANALYSIS("需求审核", "2"),
	/**
	 * 解决问题
	 */
	HANDLING("解决问题", "3"),
	/**
	 * 确认
	 */
	CONFIRM("成果确认", "4"),
	/**
	 * 结束
	 */
	FINISH("结束", "5");
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private DemandStageEnum(String desc, String value) {
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

	public static DemandStageEnum getEnum(String value) {
		DemandStageEnum resultEnum = null;
		DemandStageEnum[] enumAry = DemandStageEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		DemandStageEnum[] ary = DemandStageEnum.values();
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
		DemandStageEnum[] ary = DemandStageEnum.values();
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
		DemandStageEnum[] enums = DemandStageEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (DemandStageEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
