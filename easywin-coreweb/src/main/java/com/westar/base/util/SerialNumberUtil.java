package com.westar.base.util;

import java.util.Random;

/**
 * 
 * 描述:序列号帮助类
 * @author zzq
 * @date 2018年9月27日 下午8:33:52
 */
public class SerialNumberUtil {
	/**
	 * yyMMdd-五位随机数
	 * @return
	 */
	public static String getSerialNumber(){
		String serialNumber = "";
		String datestr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyyMMdd);
		int x = new Random().nextInt(9000) + 1000;
		serialNumber = datestr + x;
		return serialNumber;
	}
}