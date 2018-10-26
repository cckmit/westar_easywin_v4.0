package com.westar.base.util;

import java.util.Map;
/**
 * 缓存配置信息
 * @author H87
 *
 */
public class PublicConfig {
	/**
	 * 文件转换 加载。
	 */
	public static Map<String, String> OFFICE_HOME = ResourceUtils.getResource("officehome").getMap();
	/**
	 * 极光推送文件配置 加载。
	 */
	public static Map<String, String> JPUSH = ResourceUtils.getResource("jpush").getMap();
	
	/**
	 * 节假日url
	 */
	public static Map<String, String> HOLIDAY_URL = ResourceUtils.getResource("holiday").getMap();
	/**
	 * 链接url
	 */
	public static Map<String, String> SERVER_URL = ResourceUtils.getResource("serverUrl").getMap();
	/**
	 * 平台管理账号配置信息
	 */
	public static Map<String, String> COMPANY_ACCOUNT = ResourceUtils.getResource("companyAccount").getMap();
	
	/**
	 * 支付相关信息
	 */
	public static Map<String, String> PAY_MENT_CONF = ResourceUtils.getResource("payment").getMap();
	
}
