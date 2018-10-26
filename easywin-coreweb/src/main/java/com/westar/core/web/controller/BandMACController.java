package com.westar.core.web.controller;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.filter.config.ConfigTools;
import com.westar.base.model.BandMAC;
import com.westar.base.util.MACUtil;
import com.westar.base.util.MessageSender;
import com.westar.base.util.PublicConfig;
import com.westar.core.service.BandMACService;
import com.westar.core.thread.MailSendThread;

/**
 * 服务起MAC绑定
 * @author zzq
 *
 */
@Controller
@RequestMapping("/bandMAC")
public class BandMACController extends BaseController{

	@Autowired
	BandMACService bandMACService;
	
	private static String USERNAME = PublicConfig.COMPANY_ACCOUNT.get("sys.account");  
	private static String PASSWORD = PublicConfig.COMPANY_ACCOUNT.get("sys.passwd");  
	
	/**
	 * 异步检测服务器的授权码有效性
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/macValidatCheck")
	public Map<String, Object> macValidatCheck(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = bandMACService.macValidatCheck();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 展示激活激活信息
	 * @return
	 */
	@RequestMapping("/macInfoTip")
	public ModelAndView macInfoTip(){
		ModelAndView mav = new ModelAndView("/bandmac/macInfoTip");
		return mav;
	}
	/**
	 * 展示激活激活信息
	 * @return
	 */
	@RequestMapping("/macInfoLogin")
	public ModelAndView macInfoLogin(){
		ModelAndView mav = new ModelAndView("/bandmac/macInfoLogin");
		return mav;
	}
	
	/**
	 * 后台管理员验证登陆
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/macValidatlogin")
	public Map<String, Object> macValidatlogin(String account,String password,
			String macStr) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isEmpty(account)
				|| StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(macStr)){
			map.put("status", "f");//用户名不对
			map.put("info", "参数错误");
			return map;
		}
		
		ConfigTools configTools = new ConfigTools();
		account = configTools.encrypt(account);
		password = configTools.encrypt(password);
		if(!USERNAME.equals(account)){
			map.put("status", "f1");//用户名不对
			map.put("info", "用户名不对");
			return map;
		}else if(!PASSWORD.equals(password)){
			map.put("status", "f2");//密码不对
			map.put("info", "密码不对");
			return map;
		}
		
		String thisMac = MACUtil.getLocalMac();
		if(!StringUtils.isEmpty(macStr)){
			macStr = macStr.toUpperCase();
			if(!macStr.equals(thisMac)){
				map.put("status", "f3");//绑定的MAC不对
				map.put("info", "绑定的MAC不对");
				return map;
			}
		}else{
			map.put("status", "f4");//没有绑定的MAC
			map.put("info", "没有绑定的MAC");
			return map;
		}
		
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 展示激活激活信息
	 * @return
	 */
	@RequestMapping("/macValidatSetPage")
	public ModelAndView macValidatSetPage(){
		ModelAndView mav = new ModelAndView("/bandmac/macValidatSet");
		return mav;
	}
	
	
	/**
	 * 后台管理员验证登陆
	 * @param account
	 * @param password
	 * @return
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/macValidatSet")
	public Map<String, Object> macValidatSet(HttpSession session,BandMAC bandMAC,String yzm) 
			throws SocketException, UnknownHostException{
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		if(StringUtils.isEmpty(bandMAC.getMacName())
				|| StringUtils.isEmpty(bandMAC.getServiceDate())
				|| StringUtils.isEmpty(bandMAC.getLicenseCode())
				|| StringUtils.isEmpty(yzm)){
			map.put("status", "f");//用户名不对
			map.put("info", "参数错误");
			return map;
		}
		
		
		String rand = (String) session.getAttribute("bandmac_rand");
		if(null!=rand && null!=yzm && yzm.equalsIgnoreCase(rand) ){
			map = bandMACService.updateMacValidat(bandMAC);
			if(map.get("status").equals("y")){
				session.removeAttribute("bandmac_rand");
			}
		}else{
			map.put("status", "n");
			map.put("info", "验证错误或失效");
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
	@ResponseBody
	@RequestMapping("/sendMacValiCode")
	public Map<String, Object> sendMacValiCode(BandMAC bandMAC,HttpSession httpSession) 
			throws SocketException, UnknownHostException{
		Map<String, Object> map = bandMACService.sendMacValiCode(bandMAC);
		if(map.get("status").equals("y")){
			//邮件发送准备
			MessageSender sender = MessageSender.getMessageSender();
			//标题
	        String subject = "服务激活码"; 
	        
	        String licenseCode = (String) map.get("licenseCode");
	        String macStr = bandMAC.getMacName().toUpperCase();
	        
	        //内容
			String body = "<div>您好！<div><br>";
			body= body +"服务器MAC地址为："+macStr+" <br/>";
			body= body +"激活码有效时间为："+bandMAC.getServiceDate()+" <br/>";
			body= body +"服务激活码为："+licenseCode+" <br/>";
			//发送邮件
			new Thread(new MailSendThread(sender, "2151541168@qq.com", subject, body)).start();
			map.remove("licenseCode");
		}
		return map;
	}
	
	
}