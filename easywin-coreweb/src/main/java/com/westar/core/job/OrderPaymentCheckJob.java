package com.westar.core.job;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.westar.core.service.OrderService;
import com.westar.core.service.SystemLogService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.SessionContext;
import com.westar.core.web.TokenManager;
/**
 * 定时清理无效订单
 * @author zzq
 *
 */

@Component
public class OrderPaymentCheckJob {
	
	@Autowired
	private OrderService orderService;
	
	//每天凌晨4点执行
	@Scheduled(cron="0 0 4 ? * *")
	public void clearSession(){
		try {
			orderService.updateUnPayedOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}