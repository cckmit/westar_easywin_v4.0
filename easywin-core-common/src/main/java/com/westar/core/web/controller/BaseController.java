package com.westar.core.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.westar.base.cons.CommonConstant;
import com.westar.base.cons.SessionKeyConstant;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.RequestContextHolderUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.UUIDGenerator;
import com.westar.core.web.AppAuthKeyManager;
import com.westar.core.web.SessionContext;
import com.westar.core.web.TokenManager;

public abstract class BaseController {
	
	/**
	 * 跳转刷新父页面
	 */
	protected final String LAYER_CLOSE_REFRESHPARENT = "/refreshParent";

	protected  final String LAYER_REFRESH_SELF = "/refreshSelf";

	/**
	 * 分享范围类型-所有同事
	 */
	public static final int ALL_COLLEAGUE = 0;
	/**
	 * 分享范围类型-根据群组ID界定
	 */
	public static final int BY_GROUPID = 1;
	/**
	 * 分享范围类型-自己
	 */
	public static final int MYSELF = 2;

	/**
	 * 注销用户
	 * @param sid
	 */
	@SuppressWarnings("unchecked")
	protected void logOut(String sid) {
		HttpSession session = RequestContextHolderUtil.getSession();
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) session.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
		if (map == null) {
			map = new HashMap<String, Map<String, Object>>(14);
		}
		Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
		if (m == null) {
			m = new HashMap<String, Object>(14);
		}
		 
		//移除的对象
		UserInfo userInfo = (UserInfo) m.remove(sid);
		if(null!=userInfo){
			//sessionMap中移除当前用户
			SessionContext.sessionMap.remove(userInfo.getId());
			//sidMap中移除当前用户
			SessionContext.sidMap.remove(userInfo.getId());
		}
		
		if(null!=TokenManager.tokenMap){
			//移除用户的token标识
			TokenManager.tokenMap.remove(sid);
			//重新设置token
			session.setAttribute(SessionKeyConstant.TOKEN_CONTEXT, TokenManager.tokenMap);
		}
		
		map.put(SessionKeyConstant.USER_CONTEXT, m);
		session.setAttribute(SessionKeyConstant.SESSION_CONTEXT, map);
	}

	/**
	 * 获取保存在Session中的对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object getSessionObj(String key) {
		HttpSession sessionForGet = RequestContextHolderUtil.getSessionForGet();
		if(null==sessionForGet){
			return null;
		}
		Object obj = null;
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) sessionForGet.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
		if (map == null) {
			map = new HashMap<String, Map<String, Object>>(14);
		}
		Map<String, Object> m = map.get(key);
		if (m == null) {
			m = new HashMap<String, Object>(14);
		}
		String sid = RequestContextHolderUtil.getRequest().getParameter("sid");
		if (map != null && sid != null) {
			obj = m.get(sid);
		}
		if(null == obj){
			String authKey = RequestContextHolderUtil.getRequest().getParameter(CommonConstant.APP_AUTH_KEY);
			if(!StringUtils.isEmpty(authKey)){
				obj = AppAuthKeyManager.getUserInfo(authKey);
			}else{
				obj = AppAuthKeyManager.getUserInfo(sid);
			}
		}
		return obj;
	}

	/**
	 * 获取保存在Session中的登录人员信息
	 * @return
	 */
	protected UserInfo getSessionUser() {
		return (UserInfo) this.getSessionObj(SessionKeyConstant.USER_CONTEXT);
	}
	/**
	 * 修改登录人员信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected void updateSessionUser(UserInfo obj) {
		HttpSession session = RequestContextHolderUtil.getSession();
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) session.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
		Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
		m.put(this.getSid(), obj);
		map.put(SessionKeyConstant.USER_CONTEXT, m);
		session.setAttribute(SessionKeyConstant.SESSION_CONTEXT, map);
		
	}

	/**
	 * 保存对象到Session中
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	protected String setSessionObj(String key, Object obj) {
		HttpSession session = RequestContextHolderUtil.getSession();
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) session.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
		if (map == null) {
			map = new HashMap<String, Map<String, Object>>(14);
		}
		String sid = UUIDGenerator.getUUID();
		Map<String, Object> m = map.get(key);
		if (m == null) {
			m = new HashMap<String, Object>(14);
		}
		m.put(sid, obj);
		map.put(SessionKeyConstant.USER_CONTEXT, m);
		session.setAttribute(SessionKeyConstant.SESSION_CONTEXT, map);
		return sid;
	}

	/**
	 * 保存Web消息
	 * @param type
	 * @param content
	 */
	protected void setNotification(int type, String content) {
		Notification notification = new Notification(type, content);
		RequestContextHolderUtil.getSession().setAttribute(CommonConstant.NOTIFICATION_CONTEXT, notification);
	}

	/**
	 * 获取SID，如果为NULL 返回空字符
	 * @return
	 */
	protected String getSid() {
		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		return StringUtil.delNull(request.getParameter("sid"));
	}

}
