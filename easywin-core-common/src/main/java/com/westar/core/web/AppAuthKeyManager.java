package com.westar.core.web;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.westar.base.model.UserInfo;
import com.westar.base.util.UUIDGenerator;

/**
 * 手机客户端授权码管理
 * 
 * @author wang-dev
 * 
 */
public class AppAuthKeyManager {

	/**
	 * 授权码容器
	 */
	private static final Map<String, UserInfo> AUTHKEY_CONTEXT = new ConcurrentHashMap<String, UserInfo>();

	/**
	 * 获取新的授权码
	 * @param uc
	 * @return
	 */
	public static String newAuthKey(UserInfo uc) {
		String authKey = UUIDGenerator.getUUID();
		AUTHKEY_CONTEXT.put(authKey, uc);
		return authKey;
	}
	/**
	 * 获取新的授权码
	 * @param uc
	 * @return
	 */
	public static String updateAuthKey(String authKey,UserInfo uc) {
		AUTHKEY_CONTEXT.put(authKey, uc);
		return authKey;
	}

	/**
	 * 从容器中移除授权码
	 * @param uid
	 */
	public static void removeAuthKey(Integer uid) {
		for (Iterator<Map.Entry<String, UserInfo>> it = AUTHKEY_CONTEXT.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, UserInfo> entry = it.next();
			if (entry.getValue().getId() == uid) {
				it.remove();
			}
		}
	}
	
	/**
	 * 验证授权码
	 * @param authKey
	 * @return
	 */
	public static boolean valAuthKey(String authKey){
		if(AUTHKEY_CONTEXT.containsKey(authKey)){
			return true;
		}
		return false;
	}
	
	/**
	 * 获得用户上下文
	 * @param authKey
	 * @return
	 */
	public static UserInfo getUserInfo(String authKey){
		return  AUTHKEY_CONTEXT.get(authKey);
	}
	/**
	 * 修改缓存数据
	 * @param authKey
	 * @param user
	 */
	public static void updateSessionUser(String authKey, UserInfo user) {
		AUTHKEY_CONTEXT.put(authKey, user);
		
	}

}
