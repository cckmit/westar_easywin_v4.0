package com.westar.core.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.westar.base.cons.SessionKeyConstant;
import com.westar.base.model.UserInfo;

public class SessionContext {
	
	public static Map<Integer, HttpSession> sessionMap;
	
	public static Map<Integer, String> sidMap;
	
	/**
	 * TODO 多服务器部署的时候时候，其他服务器取不到
	 */
	public static List<String> listSessionId = new ArrayList<String>();

	/**
	 * 新用户登录后 添加到session中
	 * @param userid
	 * @param sid
	 * @param session
	 */
	public static void addSessionUser(Integer userid, String sid, HttpSession session) {
		if (sessionMap == null) {
			sessionMap = new HashMap<Integer, HttpSession>();
		}
		if (sidMap == null) {
			sidMap = new HashMap<Integer, String>();
		}
		sessionMap.put(userid, session);
		sidMap.put(userid, sid);
	}

	/**
	 * 用户注销时 或者同一用户在其他地方登录 从session中移除
	 * @param userid
	 */
	@SuppressWarnings("unchecked")
	public static void removeSessionUser(Integer userid) {
		if (sessionMap == null) {
			sessionMap = new HashMap<Integer, HttpSession>();
		}
		if (sidMap == null) {
			sidMap = new HashMap<Integer, String>();
		}
		String sid = sidMap.get(userid);
		HttpSession session = sessionMap.get(userid);
		if (sid != null && session != null && listSessionId.contains(session.getId())) {
			Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) session.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
			if (map == null) {
				map = new HashMap<String, Map<String, Object>>();
			}
			
			Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
			m.remove(sid);
			
			if(null!=TokenManager.tokenMap){
				//移除用户的token标识
				TokenManager.tokenMap.remove(sid);
				//重新设置token
				session.setAttribute(SessionKeyConstant.TOKEN_CONTEXT, TokenManager.tokenMap);
			}
			
		}
		
		sessionMap.remove(userid);
		sidMap.remove(userid);
		
	}
	/**
	 * 团队注销时 从session中移除团队登录用户
	 * @param userid
	 * @param comId
	 */
	@SuppressWarnings("unchecked")
	public static void removeSessionUser(Integer userid,Integer comId) {
		if (sessionMap == null) {
			sessionMap = new HashMap<Integer, HttpSession>();
		}
		if (sidMap == null) {
			sidMap = new HashMap<Integer, String>();
		}
		String sid = sidMap.get(userid);
		HttpSession session = sessionMap.get(userid);
		if (sid != null && session != null && listSessionId.contains(session.getId())) {
			Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) session.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
			if (map == null) {
				map = new HashMap<String, Map<String, Object>>();
			}
			Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
			UserInfo user = (UserInfo) m.get(sid);
			//是注销的团队用户，直接移除
			if(null!=user && user.getComId().equals(comId)){
				m.remove(sid);
				sessionMap.remove(userid);
				sidMap.remove(userid);
			}
			if(null!=TokenManager.tokenMap){
				//移除用户的token标识
				TokenManager.tokenMap.remove(sid);
				//重新设置token
				session.setAttribute(SessionKeyConstant.TOKEN_CONTEXT, TokenManager.tokenMap);
			}
			
		}
	}

	/**
	 * 修改session中用户的管理权限
	 * @param userId 管理的用户主键
	 * @param comId 企业号
	 * @param bool 是否有管理权限
	 */
	@SuppressWarnings("unchecked")
	public static void grantAndRevokeUser(Integer userid, Integer comId,
			boolean bool) {
		if (sessionMap == null) {
			sessionMap = new HashMap<Integer, HttpSession>();
		}
		if (sidMap == null) {
			sidMap = new HashMap<Integer, String>();
		}
		String sid = sidMap.get(userid);
		HttpSession session = sessionMap.get(userid);
		if (sid != null && session != null) {
			Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) session.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
			if (map == null) {
				map = new HashMap<String, Map<String, Object>>();
			}
			Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
			UserInfo user = (UserInfo) m.get(sid);
			if(null!=user && user.getComId().equals(comId)){
				if(bool){//授权
					user.setAdmin("2");
					
				}else{//回收权限
					user.setAdmin("0");
				}
				m.put(sid, user);
				
				map.put(SessionKeyConstant.USER_CONTEXT, m);
				session.setAttribute(SessionKeyConstant.SESSION_CONTEXT, map);
			}
		}
		
	}
	/**
	 * 修改session中用户的管理权限
	 * @param userId 管理的用户主键
	 * @param comId 企业号
	 * @param bool 是否有管理权限
	 */
	@SuppressWarnings("unchecked")
	public static void grantAndRevokeAdmin(Integer userid, Integer comId,
			boolean bool) {
		if (sessionMap == null) {
			sessionMap = new HashMap<Integer, HttpSession>();
		}
		if (sidMap == null) {
			sidMap = new HashMap<Integer, String>();
		}
		String sid = sidMap.get(userid);
		HttpSession session = sessionMap.get(userid);
		if (sid != null && session != null) {
			Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) session.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
			if (map == null) {
				map = new HashMap<String, Map<String, Object>>();
			}
			Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
			UserInfo user = (UserInfo) m.get(sid);
			if(null!=user && user.getComId().equals(comId)){
				if(bool){//授权
					user.setAdmin("1");
					
				}else{//回收权限
					user.setAdmin("0");
				}
				m.put(sid, user);
				
				map.put(SessionKeyConstant.USER_CONTEXT, m);
				session.setAttribute(SessionKeyConstant.SESSION_CONTEXT, map);
			}
		}
		
	}
	/**
	 * 修改session中用户的下级个数
	 * @param userId 管理的用户主键
	 * @param comId 企业号
	 * @param num 添加个数
	 */
	@SuppressWarnings("unchecked")
	public static void updateSub(Integer userid, Integer comId, Integer num) {
		if (sessionMap == null) {
			sessionMap = new HashMap<Integer, HttpSession>();
		}
		if (sidMap == null) {
			sidMap = new HashMap<Integer, String>();
		}
		String sid = sidMap.get(userid);
		HttpSession session = sessionMap.get(userid);
		if (sid != null && session != null && listSessionId.contains(session.getId())) {
			Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) session.getAttribute(SessionKeyConstant.SESSION_CONTEXT);
			if (map == null) {
				map = new HashMap<String, Map<String, Object>>();
			}
			Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
			UserInfo user = (UserInfo) m.get(sid);
			if(null!=user && user.getComId().equals(comId)){
				//用户的下级个数
				Integer countSub = user.getCountSub();
				
				if(num==-1){//减少一个下级
					if(countSub>0){
						countSub = countSub-1;
						user.setCountSub(countSub);
					}
				}else if(num==1){//添加一个下级
					countSub = countSub+1;
					user.setCountSub(countSub);
					
				}
				m.put(sid, user);
				
				map.put(SessionKeyConstant.USER_CONTEXT, m);
				session.setAttribute(SessionKeyConstant.SESSION_CONTEXT, map);
				
			}
			
		}
		
	}
	
/*	
	private static SessionContext instance;
	
	private SessionContext(){}
	
	private Map<String, Map<String, Object>> context;
	
	public static SessionContext getInstance(){
		if(instance==null){
			instance=new SessionContext();
		}
		return instance;
	}
	
	public void addSessionMap(String key,Map<String, Object> value){
		if(context==null){
			context=new HashMap<String, Map<String,Object>>();
		}else{
			context.put(key, value);
		}
	}
	
	public void removeSessionMap(String key){
		if(context!=null){
			context.remove(key);
		}
	}
	
	public Map<String, Object> getSessionMap(String key){
		if(context!=null){
			return context.get(key);
		}else{
			return null;
		}
	}
	*/
}
