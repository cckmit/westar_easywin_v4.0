package com.westar.core.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;

import com.westar.base.cons.SessionKeyConstant;


public class TokenManager {

	/**
	 * Session存的最大令牌数量
	 */
	public final static int MAX_TOKEN=99;
	
	/**
	 * 存放一个人给2个令牌空间
	 */
	public static Map<String, LinkedList<String>> tokenMap;
	
	/**
	 * 令牌放到Session中的键名称
	 */
	public final static String TOKEN_PARAM="token";
	
	/**
	 * 获取新的令牌
	 * @param request
	 * @return
	 */
	public static String newToken(HttpServletRequest request){
		//生成令牌
		String token=RandomStringUtils.random(32,false,true);
		//取得当前操作人员的令牌集合
		LinkedList<String> tokens=getTokensInCurrentSession(request);
		//取得操作人员的sessionId
		String sid = request.getParameter("sid");
		HttpSession session=request.getSession();
		synchronized(tokens) {
			//当前操作人员的令牌集合数大于最大数，移除
			if(tokens.size()>=MAX_TOKEN){
				tokens.removeFirst();
			}
			//添加令牌
			tokens.add(token);
			tokenMap.put(sid, tokens);
			//放入session
			session.setAttribute(SessionKeyConstant.TOKEN_CONTEXT, tokenMap);
		}
		return token;
	}
	
	/**
	 * 获取当前Session中所有的令牌
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  static LinkedList<String> getTokensInCurrentSession(HttpServletRequest request){
		LinkedList<String> tokens=null;
		
		HttpSession session=request.getSession();
		//取得操作人员的sessionId
		String sid = request.getParameter("sid");
		tokenMap = (Map<String, LinkedList<String>>) session.getAttribute(SessionKeyConstant.TOKEN_CONTEXT);
		if(null==tokenMap){
			tokenMap = new HashMap<String, LinkedList<String>>();
		}
		tokens=tokenMap.get(sid);
		if(tokens==null){
			tokens=new LinkedList<String>();
			tokenMap.put(sid, tokens);
			session.setAttribute(SessionKeyConstant.TOKEN_CONTEXT, tokenMap);
		}
		return tokens;
	}
	
	/**
	 * 判断当前Session中是否有对应的令牌
	 * @param request
	 * @param token
	 * @return
	 */
	public static boolean hasToken(HttpServletRequest request,String token){
		return getTokensInCurrentSession(request).contains(token);
	}
	
	/**
	 * 销毁令牌
	 * @param request
	 * @param token
	 */
	public static void destroyToken(HttpServletRequest request, String token) {
		LinkedList<String> tokens=getTokensInCurrentSession(request);
		HttpSession session=request.getSession();
		synchronized(tokens) {
			tokens.remove(token);
			session.setAttribute(SessionKeyConstant.TOKEN_CONTEXT, tokenMap);
		}
	}

}
