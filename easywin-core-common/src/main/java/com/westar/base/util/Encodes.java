package com.westar.base.util;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

/**
 * 封装各种格式的编码解码工具类
 */
public class Encodes {
	private static Logger logger = Logger.getLogger(Encodes.class);
	
	private static final String DEFAULT_URL_ENCODING = "UTI-8";
	
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * Md5编码
	 * @param input
	 * @return
	 */
	public static String encodeMd5(String input){
		return DigestUtils.md5Hex(input);
	}
	
	/**
	 * Hex编码
	 * @param input
	 * @return
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHex(input).toString();
	}

	/**
	 * Hex解码
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static byte[] decodeHex(String input) throws Exception{
		return Hex.decodeHex(input.toCharArray());
	}
	
	/**
	 * Base64编码
	 * @param input
	 * @return
	 */
	public static String encodeBase64(String input) {
		byte[] output = Base64.encodeBase64(input.getBytes(), true);
		return new String(output);
	}
	/**
	 * Base64解码
	 * @param input
	 * @return
	 */
	public static String decodeBase64(String input) {
		byte[] b1 = Base64.decodeBase64(input.getBytes());
		return new String(b1);
	}
	
	/**
	 * Base64编码
	 * @param input
	 * @return
	 */
//	public static String encodeBase64(byte[] input) {
//		return Base64.encodeBase64String(input);
//	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548)
	 * @param input
	 * @return
	 */
//	public static String encodeUrlSafeBase64(byte[] input) {
//		return Base64.encodeBase64URLSafeString(input);
//	}

	/**
	 * Base64解码
	 * @param input
	 * @return
	 */
//	public static byte[] decodeBase64(String input) {
//		return Base64.decodeBase64(input);
//	}

	/**
	 * Base62编码
	 * @param input
	 * @return
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

	/**
	 * Html 转码
	 * @param html
	 * @return
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码
	 * @param htmlEscaped
	 * @return
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码
	 * @param xml
	 * @return
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码
	 * @param xmlEscaped
	 * @return
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8
	 * @param part
	 * @return
	 * @throws Exception
	 */
	public static String urlEncode(String part){
		String s="";
		try {
			s=URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (Exception e) {
			logger.error("Url编码失败。",e);
		}
		return s;
	}

	/**
	 * URL 解码, Encode默认为UTF-8
	 * @param part
	 * @return
	 * @throws Exception
	 */
	public static String urlDecode(String part){
		String s="";
		try {
			s=URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (Exception e) {
			logger.error("Url解码失败。",e);
		}
		return s;
	}
}
