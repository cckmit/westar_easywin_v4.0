package com.westar.core.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.westar.base.cons.SessionKeyConstant;
import com.westar.base.model.ViewRecord;

/**
 * 防止页面刷新,造成重复记录
 * @author H87
 *
 */
public class FreshManager {
	
	/**
	 * 存放上一步操作userId_comId,操作常量
	 */
	public static Map<String, ViewRecord> preOptMap;
	/**
	 * 从缓存中核对是否为刷新操作
	 * @param request
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean checkOpt(HttpServletRequest request,ViewRecord obj){
		
		//取得请求的session
		HttpSession session=request.getSession();
		//从session中取得前一步操作
		preOptMap = (Map<String, ViewRecord>) session.getAttribute(SessionKeyConstant.PREOPT_CONTEXT);
		if(null==preOptMap){//没有前一步操作
			preOptMap = new HashMap<String, ViewRecord>();
		}
		String sid = request.getParameter("sid");
		
		//取得操作人员的前一步操作类型
		ViewRecord viewRecord = preOptMap.get(sid);
		
		if(null!=viewRecord && viewRecord.equals(obj)){//前一步和当前操作是同一个
			return false;
		}else{//没有存前一步的操作,或是前一步和当前操作不是同一个
			preOptMap.put(sid, obj);
			session.setAttribute(SessionKeyConstant.PREOPT_CONTEXT, preOptMap);
			return true;
		}
	}
	/**
	 * 清除上一步操作
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static void removePreOpt(HttpServletRequest request){
		//取得请求的session
		HttpSession session=request.getSession();
		//从session中取得前一步操作
		preOptMap = (Map<String, ViewRecord>) session.getAttribute(SessionKeyConstant.PREOPT_CONTEXT);
		if(null==preOptMap){//没有前一步操作
			preOptMap =  new HashMap<String, ViewRecord>();
		}
		String sid = request.getParameter("sid");
		preOptMap.remove(sid);
		session.setAttribute(SessionKeyConstant.PREOPT_CONTEXT, preOptMap);
	}
}
