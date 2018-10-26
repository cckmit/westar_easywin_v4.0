package com.westar.base.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ZkemSDK 
{
	private static ActiveXComponent zkem = new ActiveXComponent("zkemkeeper.ZKEM.1");
	
	/**
	 * 连接到考勤机
	 * @param address
	 * @param port
	 * @return
	 */
	public boolean connect(String address,int port)
	{
		boolean result = zkem.invoke("Connect_NET", address, port).getBoolean();
		return result;
	}
	
	/**
	 * 断开考勤机连接
	 */
	public void disConnect()
	{
		zkem.invoke("Disconnect");
	}
	
	/**
	 * 读取考勤数据到缓存中。配合getGeneralLogData使用。
	 * @return
	 */
	public boolean readGeneralLogData()
	{
		boolean result = zkem.invoke("ReadGeneralLogData",1).getBoolean();
		return result;
	}
	
	
	/**
	 * 读取该时间之后的最新考勤数据。 配合getGeneralLogData使用。
	 * @param lastest
	 * @return
	 */
	public boolean readLastestLogData(Date lastest)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(lastest);
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH) ;
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int minutes = c.get(Calendar.MINUTE);
		int seconds = c.get(Calendar.SECOND);
		
		
		Variant v0 = new Variant(1);
		Variant vLog = new Variant(1);
		Variant dwYear = new Variant(year,true);
		Variant dwMonth = new Variant(month,true);
		Variant dwDay = new Variant(day,true);
		Variant dwHour = new Variant(hours,true);
		Variant dwMinute = new Variant(minutes,true);
		Variant dwSecond = new Variant(seconds,true);
		
		boolean result = zkem.invoke("ReadLastestLogData",v0,vLog,dwYear,dwMonth,dwDay,dwHour,dwMinute,dwSecond).getBoolean();
		return result;
	}
	/**
	 * 日期数转化
	 * @param num
	 * @return
	 */
	public String changDateNum(Integer num){
		if(num >=1 && num <=9){
			return "0"+num;
		}else{
			return ""+num;
		}
	}
	
	/**
	 * 获取缓存中的考勤数据。配合readGeneralLogData / readLastestLogData使用。
	 * @return 返回的map中，包含以下键值：
	 	"EnrollNumber"   人员编号
		"Time"           考勤时间串，格式: yyyy-MM-dd HH:mm:ss
		"VerifyMode" 	0为密码验证/1为指纹验证/2为卡验证
		"InOutMode"		0-checkIn /1-checkOut /2-breakOut/3-breakIn/4-OTIn/5-OT-Out
		"Year"          考勤时间：年
		"Month"         考勤时间：月
		"Day"           考勤时间：日
		"Hour"			考勤时间：时
		"Minute"		考勤时间：分
		"Second"		考勤时间：秒
	 * @param number 员工编号
	 * @param endDate 截止日期 yyyy-MM-dd
	 */
	public List<Map<String,Object>> getGeneralLogData(String number)
	{
		Variant v0 = new Variant(1);
		Variant dwEnrollNumber = new Variant(number,true);
		Variant dwVerifyMode = new Variant(0,true);
		Variant dwInOutMode = new Variant(0,true);
		Variant dwYear = new Variant(0,true);
		Variant dwMonth = new Variant(0,true);
		Variant dwDay = new Variant(0,true);
		Variant dwHour = new Variant(0,true);
		Variant dwMinute = new Variant(0,true);
		Variant dwSecond = new Variant(0,true);
		Variant dwWorkCode = new Variant(0,true);
		List<Map<String,Object>> strList = new ArrayList<Map<String,Object>>();
		boolean newresult = false;
		do{
			Variant   vResult = Dispatch.call(zkem, "SSR_GetGeneralLogData",v0,dwEnrollNumber,dwVerifyMode,dwInOutMode,dwYear,dwMonth,dwDay,dwHour,
					dwMinute,dwSecond,dwWorkCode);	
			newresult = vResult.getBoolean();
			if(newresult)
			{
				String enrollNumber = dwEnrollNumber.getStringRef();
				
				//如果没有编号，则跳过。
				if(enrollNumber == null || enrollNumber.trim().length() == 0){
					continue;
				}
				
				Map<String,Object> m = new HashMap<String, Object>();
				
				String month  = changDateNum(dwMonth.getIntRef());
				String day  = changDateNum(dwDay.getIntRef());
			  
			    String time = dwYear.getIntRef()+"-"+ month+"-"+ day;
				    
				
				String hh = changDateNum(dwHour.getIntRef());
				String mm = changDateNum(dwMinute.getIntRef());
				String ss = changDateNum(dwSecond.getIntRef());
				
				m.put("EnrollNumber", enrollNumber);
				m.put("Time", time+ " " +hh+ ":" +mm+ ":" +ss);
				m.put("VerifyMode", dwVerifyMode.getIntRef());
				m.put("InOutMode", dwInOutMode.getIntRef());
				strList.add(m);
			}
		}while(newresult == true);
		return strList;
	}
	
	/**
	 * 获取用户信息
	 * @return 返回的Map中，包含以下键值:
	 * 	"EnrollNumber"  人员编号
		"Name"          人员姓名
		"Password"      人员密码
		"Privilege"     人员权限 3管理员/0普通用户
		"Enabled"       是否启用 1启用/0为禁用
	 */
	public List<Map<String,Object>> getAllUserInfo() 
	{
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		//将用户数据读入缓存中。
		boolean result = zkem.invoke("ReadAllUserID",1).getBoolean();
		
		Variant v0 = new Variant(1);
		Variant sdwEnrollNumber = new Variant("",true);
		Variant sName = new Variant("",true);
		Variant sPassword = new Variant("",true);
		Variant iPrivilege = new Variant(0,true);
		Variant bEnabled = new Variant(false,true);
		
		while(result)
		{	
			//从缓存中读取一条条的用户数据
			result = zkem.invoke("SSR_GetAllUserInfo", v0,sdwEnrollNumber,sName,sPassword,iPrivilege,bEnabled).getBoolean();

			//如果没有编号，跳过。
			String enrollNumber = sdwEnrollNumber.getStringRef();
			if(enrollNumber == null || enrollNumber.trim().length() == 0){
				continue;
			}
			
			//由于名字后面会产生乱码，所以这里采用了截取字符串的办法把后面的乱码去掉了，以后有待考察更好的办法。
			//只支持2位、3位、4位长度的中文名字。
			String name = "";
			if(sName.getStringRef().getBytes().length == 9 || sName.getStringRef().getBytes().length == 8)
			{
				name = sName.getStringRef().substring(0,3);
			}else if(sName.getStringRef().getBytes().length == 7 || sName.getStringRef().getBytes().length == 6)
			{
				name = sName.getStringRef().substring(0,2);
			}else if(sName.getStringRef().getBytes().length == 11 || sName.getStringRef().getBytes().length == 10)
			{
				name = sName.getStringRef().substring(0,4);
			}
			
			//如果没有名字，跳过。
			if(name.trim().length() == 0){
				continue;
			}
			
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("EnrollNumber", enrollNumber);
			m.put("Name", name);
			m.put("Password", sPassword.getStringRef());
			m.put("Privilege", iPrivilege.getIntRef());
			m.put("Enabled", bEnabled.getBooleanRef());
			
			resultList.add(m);
		}
		return resultList;
	}
	
	
	/**
	 * 设置用户信息
	 * @param number
	 * @param name
	 * @param password
	 * @param isPrivilege
	 * @param enabled
	 * @return
	 */
	public boolean setUserInfo(String number,String name,String password, int isPrivilege,boolean enabled)
	{
		Variant v0 = new Variant(1);
		Variant sdwEnrollNumber = new Variant(number,true);
		Variant sName = new Variant(name,true);
		Variant sPassword = new Variant(password,true);
		Variant iPrivilege = new Variant(isPrivilege,true);
		Variant bEnabled = new Variant(enabled,true);
		
		boolean result = zkem.invoke("SSR_SetUserInfo",v0,sdwEnrollNumber,sName,sPassword,iPrivilege,bEnabled).getBoolean();
		return result;
	}
	
	/**
	 * 获取用户信息
	 * @param number
	 * @return
	 */
	public Map<String,Object> getUserInfoByNumber(String number)
	{
		Variant v0 = new Variant(1);
		Variant sdwEnrollNumber = new Variant(number,true);
		Variant sName = new Variant("",true);
		Variant sPassword = new Variant("",true);
		Variant iPrivilege = new Variant(0,true);
		Variant bEnabled = new Variant(false,true);
		boolean result = zkem.invoke("SSR_GetUserInfo",v0 ,sdwEnrollNumber,sName,sPassword,iPrivilege,bEnabled).getBoolean();
		if(result)
		{
			//由于名字后面会产生乱码，所以这里采用了截取字符串的办法把后面的乱码去掉了，以后有待考察更好的办法。
			//只支持2位、3位、4位长度的中文名字。
			String name = "";
			if(sName.getStringRef().getBytes().length == 9 || sName.getStringRef().getBytes().length == 8)
			{
				name = sName.getStringRef().substring(0,3);
			}else if(sName.getStringRef().getBytes().length == 7 || sName.getStringRef().getBytes().length == 6)
			{
				name = sName.getStringRef().substring(0,2);
			}else if(sName.getStringRef().getBytes().length == 11 || sName.getStringRef().getBytes().length == 10)
			{
				name = sName.getStringRef().substring(0,4);
			}
			
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("EnrollNumber", number);
			m.put("Name", name);
			m.put("Password", sPassword.getStringRef());
			m.put("Privilege", iPrivilege.getIntRef());
			m.put("Enabled", bEnabled.getBooleanRef());
			return m;
		}
		return null;
	}
	
}
