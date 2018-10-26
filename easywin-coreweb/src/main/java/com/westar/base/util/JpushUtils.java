package com.westar.base.util;

import java.util.HashMap;
import java.util.Map;

import com.westar.base.model.Logs;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;

import com.google.gson.Gson;
import com.westar.base.model.JpushRegiste;
import com.westar.base.model.TodayWorks;
import com.westar.core.service.JpushRegisteService;

/**
 * 极光推送消息
 * @author H87
 *
 */
public class JpushUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JpushUtils.class);
	
	private static JpushRegisteService jpushRegisteService;
	public static void setJpushRegisteService(
			JpushRegisteService jpushRegisteService) {
		JpushUtils.jpushRegisteService = jpushRegisteService;
	}
	
	private static final String APPKEY = PublicConfig.JPUSH.get("jpush.appkey");  
	private static final String MASTERSECRET = PublicConfig.JPUSH.get("jpush.mastersecret");  
	private static final String DAY = PublicConfig.JPUSH.get("jpush.offlineday");
	public static JPushClient jpushClient = null;
    
	//发送待办消息
	public static void sendTodoMessage(Integer comId,final Integer userId,Integer modifyer,
			Integer todoId,Integer busId,String busType,Integer clockId,String content){
		if(null==jpushClient){
			jpushClient = new JPushClient(MASTERSECRET, APPKEY,true,Integer.valueOf(DAY));
		}
		TodayWorks todatWorks = new TodayWorks();
		todatWorks.setId(todoId);
		todatWorks.setComId(comId);
		todatWorks.setUserId(userId);
		todatWorks.setModifyer(modifyer);
		todatWorks.setBusId(busId);
		todatWorks.setBusType(busType);
		todatWorks.setContent(content);
		todatWorks.setClockId(clockId);
		Gson g = new Gson();
		final String msgInfo = g.toJson(todatWorks);
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				/*try {
					JpushRegiste jpushRegiste = jpushRegisteService.getJPushByUserId(userId);
					if(null!=jpushRegiste && null!=jpushRegiste.getRegistrationId() && !"".equals(jpushRegiste.getRegistrationId())){
						String registrationID = jpushRegiste.getRegistrationId();
						String appSource = jpushRegiste.getAppSource();
						
						if(StringUtils.isEmpty(appSource) || "1".equals(appSource)){
							jpushClient.sendMessageWithRegistrationID("您有新消息", msgInfo, registrationID);
						}
						if(StringUtils.isEmpty(appSource) || "2".equals(appSource)){
							Map<String, String> extras = new HashMap<String, String>();
							extras.put("info", msgInfo);
							jpushClient.sendIosNotificationWithRegistrationID("您有新消息", extras, registrationID);
						}
					}
				} catch (APIConnectionException e) {
					logger.error("消息：" + msgInfo + ".推送失败！",e);
					//e.printStackTrace();
				} catch (APIRequestException e) {
					logger.error("消息：" + msgInfo + ".推送失败！",e);
					//e.printStackTrace();
				}*/
				
			}
		});
		thread.start();
		
	}
	//发送通知消息
	public static void sendNotice(String content){
		if(null==jpushClient){
			jpushClient = new JPushClient(MASTERSECRET, APPKEY,Integer.valueOf(DAY));
		}
		try {
			jpushClient.sendNotificationAll(content);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
	}
}
