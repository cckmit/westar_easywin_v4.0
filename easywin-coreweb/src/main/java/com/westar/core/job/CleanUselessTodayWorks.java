package com.westar.core.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.westar.core.service.MsgShareService;
import com.westar.core.service.TodayWorksService;
/**
 * 清楚OA待办事项列表中无用的记录
 */
@Component
public class CleanUselessTodayWorks {
	@Autowired
	TodayWorksService todayWorksService;
	//每天凌晨2点执行
	@Scheduled(cron="0 0 2 ? * *")
	public void autoCard(){
		todayWorksService.delUselessTodayWorks();
	}
}
