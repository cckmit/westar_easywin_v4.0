package com.westar.core.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.westar.core.service.SystemLogService;

//@Component
public class SystemLogJob {
	@Autowired
	private SystemLogService systemLogService;
	
	//每天凌晨4点执行
	//@Scheduled(cron="0 0 4 ? * *")
	public void delMoreSystemLog(){
		systemLogService.delMoreSystemLog();
	}
}
