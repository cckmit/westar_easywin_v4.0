package com.westar.base.util;

import java.util.UUID;

/**
 * 生成UUID
 */
public class UUIDGenerator {
	
	/**
	 * 获得一个UUID
	 * @return
	 */
	public synchronized static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.replace("-", "");
	}

	/**
	 * 获得指定数目的UUID
	 * @param num
	 * @return
	 */
	public synchronized static String[] getUUID(int num) {
		if (num < 1) {
			return null;
		}
		String[] ss = new String[num];
		for (int i = 0; i < num; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}
}
