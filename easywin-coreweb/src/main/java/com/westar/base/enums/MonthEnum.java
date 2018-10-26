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
public enum MonthEnum {
	/**
	 * 一月
	 */
	Jan("一月",1),
	/**
	 * 二月
	 */
    Feb("二月",2),
    /**
	 * 三月
	 */
    Mar("三月",3),
    /**
	 * 四月
	 */
    Apr("四月",4),
    /**
	 * 五月
	 */
    May("五月",5),
    /**
	 * 六月
	 */
    Jun("六月",6),
    /**
	 * 七月
	 */
    Jul("七月",7),
    /**
	 * 八月
	 */
    Aug("八月",8),
    /**
	 * 九月
	 */
    Sep("九月",9),
    /**
	 * 十月
	 */
    Oct("十月",10),
    /**
	 * 十一月
	 */
    Nov("十一月",11),
    /**
	 * 十二月
	 */
    Dec("十二月",12);
	
	/** 枚举值 */
	private Integer value;
	/** 描述 */
	private String desc;

	private MonthEnum(String desc, Integer value) {
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

	public static MonthEnum getEnum(Integer value) {
		MonthEnum resultEnum = null;
		MonthEnum[] enumAry = MonthEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		MonthEnum[] ary = MonthEnum.values();
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
		MonthEnum[] ary = MonthEnum.values();
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
		MonthEnum[] enums = MonthEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (MonthEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
