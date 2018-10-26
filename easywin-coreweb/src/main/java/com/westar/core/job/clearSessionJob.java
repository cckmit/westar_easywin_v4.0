package com.westar.core.job;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.westar.core.web.FreshManager;
import com.westar.core.web.SessionContext;
import com.westar.core.web.TokenManager;
/**
 * 定时清除session中没有使用的数据
 * @author zzq
 *
 */

@Component
public class clearSessionJob {

	//每天凌晨4点执行
	@SuppressWarnings("unchecked")
	@Scheduled(cron="0 0 4 ? * *")
	public void clearSession(){
		if(null!= SessionContext.sessionMap && SessionContext.sessionMap.size()>0){
			//sessionMap中所有用户的主键
			Set<Integer> userIds = SessionContext.sessionMap.keySet();
			//保存sessionMap中需要移除的用户主键
			Set<Integer> useIdSet = new HashSet<Integer>();
			//遍历sessionMap
			for(Iterator<Integer> it = userIds.iterator();it.hasNext();){
				//用户主键
				Integer userId =it.next();
				//用户的session
				HttpSession session = SessionContext.sessionMap.get(userId);
				//用户已经注销(没有登录系统)
				if(!SessionContext.listSessionId.contains(session.getId()) && null != SessionContext.sidMap 
						&& SessionContext.sidMap.size()>0){
					//保存sessionMap中需要移除的用户主键
					useIdSet.add(userId);
					//用户的sid
					String sid = SessionContext.sidMap.get(userId);
					//移除用户的sid
					SessionContext.sidMap.remove(userId);
					
					if(null!=FreshManager.preOptMap){
						//移除用户的前一步操作
						FreshManager.preOptMap.remove(sid);
					}
					if(null!=TokenManager.tokenMap){
						//移除用户的token标识
						TokenManager.tokenMap.remove(sid);
					}
				}
			}
			
			for (Integer userId : useIdSet) {
				//移除session
				SessionContext.sessionMap.remove(userId);
			}
			
		}
	}
}