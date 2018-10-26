package com.westar.core.service;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.westar.base.model.BandMAC;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.Encodes;
import com.westar.base.util.MACUtil;
import com.westar.core.dao.BandMACDao;

@Service
public class BandMACService {

	@Autowired
	BandMACDao bandMACDao;

	/**
	 * 查询MAC信息
	 * @return
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public BandMAC queryBandMAC() 
			throws SocketException, UnknownHostException{
		String macStr = MACUtil.getLocalMac();
		BandMAC bandMAC = bandMACDao.queryValiDateByMac(macStr);
		if(null == bandMAC){
			bandMAC = new BandMAC();
			bandMAC.setMacName(macStr);
		}
		return bandMAC;
	}
	/**
	 * 服务器的有效性检测
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public Map<String,Object> macValidatCheck() throws SocketException, UnknownHostException {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		String macStr = MACUtil.bandMAC.getMacName();
		if(!StringUtils.isEmpty(macStr)){
			macStr = macStr.toUpperCase();
			
			map.put("mac", macStr);
			
			BandMAC bandMAC = bandMACDao.queryValiDateByMac(macStr);
			if(null==bandMAC){
				map.put("status", "f1");
				map.put("info", "服务器未激活！");
				return map;
			}
			map.put("bandMAC", bandMAC);
			
			String serviceDateStr = bandMAC.getServiceDate();
			
			
			Date serviceDate = DateTimeUtil.parseDate(serviceDateStr, DateTimeUtil.yyyy_MM_dd);
			Long serviceDateTime = serviceDate.getTime();
			//未绑定MAC
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("mac", macStr);
			jsonObject.addProperty("timeEnd", serviceDate.getTime());
			
			String thisCode = Encodes.encodeMd5(jsonObject.toString());
			
			if(!thisCode.equals(bandMAC.getLicenseCode())){
				map.put("status", "f2");
				map.put("info", "服务器激活信息错误！");
				return map;
			}
			
			String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			Date nowDate = DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd);
			Long nowDateTime = nowDate.getTime();
			if(serviceDateTime >= nowDateTime){
				map.put("status", "y");
				return map;
			}else{
				map.put("status", "f3");
				map.put("info", "服务器激活信息已过期！");
				return map;
			}
		}
		return map;
		
	}

	/**
	 * 修改MAC激活信息
	 * @param bandMAC
	 * @return
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public Map<String, Object> updateMacValidat(BandMAC bandMAC)
			throws SocketException, UnknownHostException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String thisMac = bandMAC.getMacName().toUpperCase();
		BandMAC band = bandMACDao.queryValiDateByMac(thisMac);
		
		String macStr = MACUtil.getLocalMac();
		if(!StringUtils.isEmpty(macStr)){
			macStr = macStr.toUpperCase();
			
			if(!macStr.equals(thisMac)){
				map.put("status", "f1");
				map.put("info", "MAC地址不匹配！");
				return map;
			}
			
			String serviceDateStr = bandMAC.getServiceDate();
			Date serviceDate = DateTimeUtil.parseDate(serviceDateStr, DateTimeUtil.yyyy_MM_dd);
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("mac", bandMAC.getMacName());
			jsonObject.addProperty("timeEnd", serviceDate.getTime());
			
			String thisCode = Encodes.encodeMd5(jsonObject.toString());
			
			if(!thisCode.equals(bandMAC.getLicenseCode())){
				map.put("status", "f2");
				map.put("info", "服务器激活码错误！");
				return map;
			}
			map.put("status", "y");
		}
		
		if(map.get("status").equals("y")){
			
			MACUtil.setBandMAC(bandMAC);
			
			if(null != band ){
				bandMAC.setId(band.getId());
				bandMACDao.update(bandMAC);
			}else{
				bandMACDao.add(bandMAC);
			}
		}
		return map;
	}

	/**
	 * 发送验证码
	 * @param bandMAC
	 * @return
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public Map<String, Object> sendMacValiCode(BandMAC bandMAC) throws SocketException, UnknownHostException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String thisMac = bandMAC.getMacName().toUpperCase();
		String macStr = MACUtil.getLocalMac();
		if(!StringUtils.isEmpty(macStr)){
			macStr = macStr.toUpperCase();
			
			if(!macStr.equals(thisMac)){
				map.put("status", "f1");
				map.put("info", "MAC地址不匹配！");
				return map;
			}
			
			String serviceDateStr = bandMAC.getServiceDate();
			Date serviceDate = DateTimeUtil.parseDate(serviceDateStr, DateTimeUtil.yyyy_MM_dd);
			//未绑定MAC
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("mac", macStr);
			jsonObject.addProperty("timeEnd", serviceDate.getTime());
			
			String thisCode = Encodes.encodeMd5(jsonObject.toString());
			map.put("status", "y");
			map.put("licenseCode", thisCode);
			
		}
		return map;
	}
	
	
	
}
