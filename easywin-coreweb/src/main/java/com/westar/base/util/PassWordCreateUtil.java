package com.westar.base.util;

import java.util.Random;
/**
 * 随机密码取得 6 位
 * @author H87
 *
 */
public class PassWordCreateUtil {  
	
	public static String getRandomS() { 
		String s = ""; 
		Random getR = new Random(); 
		int n = 0; 
		int m = 0; 
		for (int i = 0; i < 6; i++) { 
			if (n == 3) { 
				s += PassWordCreateUtil.getSz(); 
				m++; 
			} else if (m == 3) { 
				s += PassWordCreateUtil.getZm(); 
				n++; 
			} else { 
				int ri = getR.nextInt(2); 
				int temp = ri == 0 ? m++ : n++; 
				s += ri == 0 ? PassWordCreateUtil.getSz() : PassWordCreateUtil.getZm(); 
			} 
		} 
		return s; 
	} 
	// 随机数字的字母，区分大小写 
	private static String getZm() { 
		Random getR = new Random(); 
		char sSS = (char) (getR.nextInt(26) + 97);// 小写字母97--122=a---z 
		char sBs = (char) (getR.nextInt(26) + 65);// 大写65--90=A----Z 
		char[] stemp = { sSS, sBs }; int i = getR.nextInt(2); 
		String sS = String.valueOf(stemp[i]); return sS; 
	} 
	// 随机数字的字符串 
	private static String getSz() { 
		Random getR = new Random(); 
		int getI = getR.nextInt(10) + 48;// 数字48--57=0---9 
		String sI = String.valueOf((char) getI); return sI; 
	} 
}  