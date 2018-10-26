package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:运营统计分类
 * @author zzq
 * @date 2018年8月28日 上午10:11:02
 */
public enum StaticPlatFormTypeEnum {
	/**
	 * 今日新增任务
	 */
	TODAYTASK("今日新增任务", "todayTask"),
	/**
	 * 今日新增客户
	 */
	TODAYCRM("今日新增客户", "todayCrm"),
	/**
	 * 今日日报
	 */
	TODAYDAILY("今日日报", "todayDaily"),
	/**
	 * 周报汇报
	 */
	WEEKREPORT("周报汇报", "weekReport"),
	/**
	 * 任务分析
	 */
	TASKBYDEP("任务分析", "taskByDep"),
	/**
	 * 任务王
	 */
	TASKTOP("任务王", "taskTop"),
	/**
	 * 任务分类
	 */
	TASKBYGRADE("任务分类", "taskByGrade"),
	/**
	 * 客户分类
	 */
	CRMBYTYPE("客户分类", "crmByType");
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private StaticPlatFormTypeEnum(String desc, String value) {
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

	public static StaticPlatFormTypeEnum getEnum(String value) {
		StaticPlatFormTypeEnum resultEnum = null;
		StaticPlatFormTypeEnum[] enumAry = StaticPlatFormTypeEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		StaticPlatFormTypeEnum[] ary = StaticPlatFormTypeEnum.values();
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
		StaticPlatFormTypeEnum[] ary = StaticPlatFormTypeEnum.values();
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
		StaticPlatFormTypeEnum[] enums = StaticPlatFormTypeEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (StaticPlatFormTypeEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
