package com.westar.base.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串工具类（主要使用APACHE提供的字符串工具类）
 */
public class StringUtil {

	/**
	 * 去空 如果是NULL则返回空字符
	 * @param str
	 * @return
	 */
	public static String delNull(String str) {
		if (str == null) {
			str = "";
		}
		return str;
	}
	/**
	 * 转义
	 * @param str
	 * @return
	 */
	public static String change(String str){
		str = replace(str, "%", "westar");
		str = replace(str, "^", "westar");
		str = replace(str, ";", "westar");
		str = replace(str, "&", "westar");
		str = replace(str, "#", "westar");
		return str;
	}

	/**
	 * 把多行文本框里输入并保存到数据库的内容转换成HTML显示
	 * 
	 * @param s
	 * @return
	 */
	public static String toHtml(String s) {
		s = delNull(s);
		s = replace(s, "<", "&lt;");
		s = replace(s, ">", "&gt;");
		s = replace(s, "\t", "    ");
		s = replace(s, "\r\n", "<br>");
		s = replace(s, "\n", "<br>");
		s = replace(s, " ", "&nbsp;");
		return s;
	}
	
	

	/**
	 * 把多行文本框里输入并保存到数据库的内容转换成HTML显示
	 * 
	 * @param source
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static String replace(String source, String oldString, String newString) {
		if (source == null){
			return "";
		}
		StringBuffer output = new StringBuffer();
		Integer lengOfSource = source.length();
		Integer lengOfOld = oldString.length();
		Integer posStart = 0;
		Integer pos;
		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
			posStart = pos + lengOfOld;
		}

		if (posStart < lengOfSource){
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	/**
	 * 检查空字符串 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}

	/**
	 * 清除空白字符
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return StringUtils.trim(str);
	}

	/**
	 * 取得字符串的缩写
	 * @param str
	 * @param maxWidth
	 * @return
	 */
	public static String abbreviate(String str, int maxWidth) {
		return StringUtils.abbreviate(str, maxWidth);
	}

	/**
	 * 劈分字符串
	 * @param str
	 * @param separatorChars
	 * @return
	 */
	public static String[] split(String str, String separatorChars) {
		return StringUtils.split(str, separatorChars);
	}

	/**
	 * 将数组合并为字符串
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String join(String[] array, String separator) {
		return StringUtils.join(array, separator);
	}

	/**
	 * 截取字符串
	 * @param str
	 * @param start
	 * @return
	 */
	public static String substring(String str, int start) {
		return StringUtils.substring(str, start);
	}

	/**
	 * 截取字符串
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static String substring(String str, int start, int end) {
		return StringUtils.substring(str, start, end);
	}

	/**
	 * 截取嵌套字符串
	 * @param str
	 * @param open
	 * @param close
	 * @return
	 */
	public static String substringBetween(String str, String open, String close) {
		return StringUtils.substringBetween(str, open, close);
	}

	/**
	 * 判断两个字符串是否相等，有非空处理
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1, String str2) {
		return StringUtils.equals(str1, str2);
	}

	/**
	 * 返回要查找的字符串所在位置，有非空处理
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static int indexOf(String str, String searchStr) {
		return StringUtils.indexOf(str, searchStr);
	}

	/**
	 * 将字符串中的首字母大写
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		return StringUtils.capitalize(str);
	}

	/**
	 * 取得某字符串在另一字符串中出现的次数
	 * @param str
	 * @param sub
	 * @return
	 */
	public static int countMatches(String str, String sub) {
		return StringUtils.countMatches(str, sub);
	}

	/**
	 * 字符串str不足num位数的，在前面补齐"0"
	 * @param str 要处理的字符串
	 * @param num 处理后的长度
	 * @return
	 */
	public static String addZero(String str, long num) {
		StringBuffer ss = new StringBuffer();
		while (ss.length() != num - str.length()) {
			ss.append("0");
		}
		ss.append(str);
		return ss.toString();
	}

	/**
	 * 字符串str不足num位数的，在前面补齐"0"
	 * @param str 要处理的字符串
	 * @param num 处理后的长度
	 * @return
	 */
	public static String addZero(int s, long num) {
		return StringUtil.addZero(String.valueOf(s), num);
	}
	
	/**
	 * 字符串截取，超出部分添加省略号
	 * 
	 * @param str
	 * @param num
	 * @param escapeXml 是否转转义html
	 * @return
	 * @throws Exception
	 */
	public static String cutString(String str, int num,String escapeXml) {
		if (str != null) {
			if(StringUtils.isNotEmpty(escapeXml) && "true".equals(escapeXml)){
				str = str.replaceAll("<[.[^>]]*>","");
			}
			 byte[] buf;
			try {
				buf = str.getBytes("GBK");
				str = cutByteByGBK(buf,num);
			} catch (UnsupportedEncodingException e) {
				try {
					buf = str.getBytes("utf-8");
					str = cutByteByU8(buf,num);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} catch (IOException exception) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return str;
	}
	public static String cutByteByGBK(byte[] buf, int num) throws IOException { 
		
		String s =null;  
		if(buf.length>num){
			int count = 0;   
			
			int i =0;  
			for(i = num-1 ; i >= 0 ; i--){  
				if(buf[i]<0){
					count++;  
				}
				else{
					break;            
				}
			}  
			//因為GBK用兩個字節表示一個漢字  
			if(count%2 == 0) {
				s =new String(buf,0,num,"GBK")+"..";  
				
			}
			else  {
				s= new String(buf,0,num-1,"GBK")+"..";  
			}
			
		}else{
			s= new String(buf,0,buf.length,"GBK");  
		}
        return s; 
     }
	 public static String cutByteByU8(byte[] buf, int num) throws IOException {  
		 String s =null; 
		 if(buf.length>num){
			 int count = 0;   
			 int i =0;  
			 for(i = num-1 ; i >= 0 ; i--){  
				 if(buf[i]<0) {
					 count++;  
				 }
				 else  {
					 break;            
				 }
			 }  
			 //因為UTF-8三個字節表示一個漢字  
			 if(count%3 == 0)  {
				 s =new String(buf,0,num,"utf-8")+"..";  
			 }
			 else if(count%3 == 1) {
				 s= new String(buf,0,num-1,"utf-8")+"..";  
			 }
			 else if(count%3 == 2) {
				 s= new String(buf,0,num-2,"utf-8")+"..";  
			 }
		 }else{
			 s =new String(buf,0,buf.length,"utf-8");  
		 }
	        return s;  
	    }

	 /**
	  * 截取日志完整表情
	  * @param content
	  * @param len
	  * @return
	  */
	public static String cutStrFace(String content, Integer len) {
		if(null==len){//默认23
			len=23;
		}
		//截取的内容
		String newContent = content;
		if(content.length()>len){
			newContent = content.substring(0,len);
			//最后一个表情符开头所在位置
			Integer indexS = newContent.lastIndexOf("[");
			//最后一个表情符结尾所在位置
			Integer indexE = newContent.indexOf("]");
			if(indexS>=(len-3) && indexE<indexS){//若表情符不完整
				if(content.length()>=indexS+4){
					newContent = content.substring(0,indexS+4);
				}
			}
			newContent = newContent+"...";
		}
		return newContent;
	}
	/**
	 * html字符转换成转义字符
	 * @param str
	 * @return
	 */
	public static String repalceHtmlStr(String str){
		if(null==str || "".equals(str.trim())){return "";}
		//换行符替换
		String newStr = StringUtil.replace(str,"<br>","\n");
		newStr = StringUtil.replace(newStr,"<br/>","\n");
		//换行符替换
		newStr = StringUtil.replace(newStr,"&nbsp;","\b");
		//其它
		newStr = StringUtil.replace(newStr,"&lt;","<");
		newStr = StringUtil.replace(newStr,"&gt;",">");
		return newStr.trim();
	}
	
	/**
	 * 特殊字符处理
	 * @param src
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String urlEncode(String src)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(src, "UTF-8").replace("+", "%20");
	}

}
