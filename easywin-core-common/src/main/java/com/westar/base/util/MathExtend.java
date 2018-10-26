package com.westar.base.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class MathExtend {
	
	// 默认除法运算精度
	private static final int DEFAULT_DIV_SCALE = 10;

	/**
	 * 如果为null 默认0
	 * @param s
	 * @return
	 */
	public static String defaultZero(String s) {
		if (StringUtil.isBlank(s)) {
			s = "0";
		}
		return s;
	}

	/**
	 * 如果为null 默认0
	 * @param s
	 * @return
	 */
	public static Integer defaultZero(Integer s) {
		if (s == null) {
			s = 0;
		}
		return s;
	}

	/**
	 * 提供精确的加法运算。
	 * @param v1
	 * @param v2
	 * @return 两个参数的和
	 */

	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的加法运算
	 * @param v1
	 * @param v2
	 * @return 两个参数数学加和，以字符串格式返回
	 */

	public static String add(String v1, String v2) {
		if (StringUtil.isBlank(v1) || StringUtil.isBlank(v2)) {
			return "0";
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}

	/**
	 * 提供精确的减法运算。
	 * @param v1
	 * @param v2
	 * @return 两个参数的差
	 */
	public static double subtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算
	 * @param v1
	 * @param v2
	 * @return 两个参数数学差，以字符串格式返回
	 */
	public static String subtract(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).toString();
	}

	/**
	 * 提供精确的乘法运算。
	 * @param v1
	 * @param v2
	 * @return 两个参数的积
	 */
	public static double multiply(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 
	 * 提供精确的乘法运算
	 * 
	 * @param v1
	 * 
	 * @param v2
	 * 
	 * @return 两个参数的数学积，以字符串格式返回
	 * 
	 */

	public static String multiply(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).toString();
	}

	/**
	 * 如果小数点后面以0结尾则去掉
	 * @param str
	 * @return
	 */
	public static String delZero(String s) {
		if (s == null) {
			s = "0";
		}
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
	 * @param v1
	 * @param v2
	 * @return 两个参数的商
	 */
	public static String divide(double v1, double v2) {
		if (v2 == 0) {
			return "-";
		}
		return divide(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
	 * @param v1
	 * @param v2
	 * @param scale表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 * 
	 */

	public static String divide(double v1, double v2, int scale) {
		if (v2 == 0) {
			return "-";
		}
		return divide(String.valueOf(v1), String.valueOf(v2), scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
	 * @param v1
	 * @param v2
	 * @param scale表示需要精确到小数点以后几位
	 * @param roundMode表示用户指定的舍入模式
	 * @return 两个参数的商
	 */
	public static double divide(double v1, double v2, int scale, int roundMode) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, roundMode).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
	 * @param v1
	 * @param v2
	 * @return 两个参数的商，以字符串格式返回
	 */
	public static String divide(String v1, String v2) {
		return divide(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
	 * @param v1
	 * @param v2
	 * @param scale 表示需要精确到小数点以后几位
	 * @return 两个参数的商，以字符串格式返回
	 */
	public static String divide(String v1, String v2, int scale) {
		return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
	 * @param v1
	 * @param v2
	 * @param scale 表示需要精确到小数点以后几位
	 * @param roundMode 表示用户指定的舍入模式
	 * @return 两个参数的商，以字符串格式返回
	 */

	public static String divide(String v1, String v2, int scale, int roundMode) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, roundMode).toString();
	}

	/**
	 * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		return round(v, scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @param roundMode  指定的舍入模式
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale, int roundMode) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		return b.setScale(scale, roundMode).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN
	 * @param v   需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果，以字符串格式返回
	 */
	public static String round(String v, int scale) {
		return round(v, scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * @param v  需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @param roundMode 指定的舍入模式
	 * @return 四舍五入后的结果，以字符串格式返回
	 */
	public static String round(String v, int scale, int roundMode) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, roundMode).toString();
	}
	/**
	 * 求两数百分比字符串
	 * @param x 除数
	 * @param total 被除数
	 * @param minimum 保留几位小数
	 * @return
	 */
	public static String getPercent(int x,int total,int minimum){  
		if(total==0){throw new NumberFormatException("被除数不能为0！");}
		String result="";//接受百分比的值  
		double tempresult=(x*1.0)/total;  
		NumberFormat nf   =   NumberFormat.getPercentInstance();//注释掉的也是一种方法  
		nf.setMinimumFractionDigits( minimum ); //保留到小数点后几位  
		//DecimalFormat df1 = new DecimalFormat("0.00%");    //##.00%   百分比格式，后面不足2位的用0补齐  
		result=nf.format(tempresult);     
		//result= df1.format(tempresult);    
		return result;  
		}  
	
	/**
	 * 提供精确的加法运算
	 * @param 大于 时，返回 1
	 * @param 等于 时，返回 0 
	 * @param 小于 时，返回 -1
	 */

	public static int compareTo(String v1, String v2) {
		if (StringUtil.isBlank(v1) || StringUtil.isBlank(v2)) {
			return 0;
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.compareTo(b2);
	}
}
