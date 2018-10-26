package com.westar.base.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.westar.base.model.BandMAC;

/**
 * 本机MAC工具
 * @author zzq
 *
 */
public class MACUtil {
	//本地IP
	private final static String NEED_CLEAR_IP ="127.0.0.1";
	
	
	public static BandMAC bandMAC= null;
	
	//设置系统软件激活信息
	public static void setBandMAC(BandMAC param){
		bandMAC = param;
	}
	
	/**
	 * 验证系统是否正常激活
	 * @return
	 */
	public static boolean checkSysLic(){
		if(null==bandMAC){
			return false;
		}
		//激活时间
		String serviceDateStr = bandMAC.getServiceDate();
		Date serviceDate = DateTimeUtil.parseDate(serviceDateStr, DateTimeUtil.yyyy_MM_dd);
		Long serviceDateTime = serviceDate.getTime();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("mac", bandMAC.getMacName());
		jsonObject.addProperty("timeEnd", serviceDate.getTime());
		
		String thisCode = Encodes.encodeMd5(jsonObject.toString());
		
		if(!thisCode.equals(bandMAC.getLicenseCode())){
			return false;
		}
		
		String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		Date nowDate = DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd);
		Long nowDateTime = nowDate.getTime();
		if(serviceDateTime >= nowDateTime){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 取得服务器MAC
	 * @return
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public static String getLocalMac() throws SocketException, UnknownHostException{
		String macStr = null;
		String os = System.getProperty("os.name");
		if(!StringUtils.isEmpty(os)){
			os = os.toLowerCase();
			if(os.startsWith("windows")){
				macStr = getWindowsMACAddress();
			}else if(os.startsWith("linux")){
				macStr = getLnixMACAddress();
			}
		}
		return macStr;
	}
	
	
	/**
	 * 取得windows的MAC
	 * @return
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	private static String getWindowsMACAddress() throws SocketException, UnknownHostException {
		
		String address = getHostAddress();
		
		String str = "";
		String macAddress = "";
		// 获取非本地IP的MAC地址
		try {
			Process p = Runtime.getRuntime().exec("nbtstat -A " + address);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			BufferedReader br = new BufferedReader(ir);
			
			while ((str = br.readLine()) != null) {
				if(str.indexOf("MAC")>1){
					macAddress = str.substring(str.indexOf("MAC")+9, str.length());
					macAddress = macAddress.trim();
					break;
				}
			}
			p.destroy();
			br.close();
			ir.close();
		} catch (IOException ex) {
			
		}
		
		if(StringUtils.isEmpty(macAddress)){
			InetAddress inetAddress = InetAddress.getLocalHost();
		      // 貌似此方法需要JDK1.6。
		      byte[] mac = NetworkInterface.getByInetAddress(inetAddress)
		          .getHardwareAddress();
		      // 下面代码是把mac地址拼装成String
		      StringBuilder sb = new StringBuilder();
		      for (int i = 0; i < mac.length; i++) {
		        if (i != 0) {
		          sb.append("-");
		        }
		        // mac[i] & 0xFF 是为了把byte转化为正整数
		        String s = Integer.toHexString(mac[i] & 0xFF);
		        sb.append(s.length() == 1 ? 0 + s : s);
		      }
		      // 把字符串所有小写字母改为大写成为正规的mac地址并返回  
		      macAddress =  sb.toString().toUpperCase(); 
		}
		return macAddress;
	}
	

	/**
	 * 取得IP地址
	 * @return
	 */
	private static String getHostAddress() {
	    Enumeration<NetworkInterface> allNetInterfaces;
	    try {
	      allNetInterfaces = NetworkInterface.getNetworkInterfaces();
	    } catch (SocketException e) {
	    	return null;
	    }
	    if (allNetInterfaces ==null) {
	    	return null;
	    }
	    while (allNetInterfaces.hasMoreElements()) {
	      NetworkInterface networkInterface = allNetInterfaces.nextElement();
	      Enumeration<InetAddress> address = networkInterface.getInetAddresses();
	      InetAddress ip = null;
	      while (address.hasMoreElements()) {
	        ip =address.nextElement();
	        if (ip !=null && ip instanceof Inet4Address) {
	          String hostAddress = ip.getHostAddress();
	          if (!NEED_CLEAR_IP.equals(hostAddress)) {
	            return hostAddress;
	          }
	        }
	      }
	    }
	    return null;
	  }
		 /** 
		 * 获取unix网卡的mac地址. 非windows的系统默认调用本方法获取. 
		 * 如果有特殊系统请继续扩充新的取mac地址方法. 
		 *  
		 * @return mac地址 
		 * @throws UnknownHostException 
		 * @throws SocketException 
		 */  
		private static String getLnixMACAddress() throws UnknownHostException, SocketException {  
			String macStr = null;
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			if (network != null) {
				byte[] mac = network.getHardwareAddress();
				if(mac != null) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
					}
					macStr = sb.toString();
				}else{
					BufferedReader bufferedReader = null;  
					Process process = null;  
					try {  
						// linux下的命令，一般取eth0作为本地主网卡  
						process = Runtime.getRuntime().exec("ifconfig eth0");  
						// 显示信息中包含有mac地址信息  
						bufferedReader = new BufferedReader(new InputStreamReader(  
								process.getInputStream()));  
						String line = null;  
						int index = -1;  
						while ((line = bufferedReader.readLine()) != null) {  
							// 寻找标示字符串[hwaddr]  
							index = line.toLowerCase().indexOf("hwaddr");  
							if (index >= 0) {// 找到了  
								// 取出mac地址并去除2边空格  
								macStr = line.substring(index + "hwaddr".length() + 1).trim();  
								break;  
							}  
						}  
					} catch (IOException e) {  
						e.printStackTrace();  
					} finally {  
						try {  
							if (bufferedReader != null) {  
								bufferedReader.close();  
							}  
						} catch (IOException e1) {  
							e1.printStackTrace();  
						}  
						bufferedReader = null;  
						process = null;  
					}  
				}
			}
			
			
			
		    return macStr;  
		}  
	

	 
}