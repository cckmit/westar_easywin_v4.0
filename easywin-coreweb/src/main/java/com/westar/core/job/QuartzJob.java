package com.westar.core.job;

import java.text.ParseException;
import java.util.List;

import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.westar.base.model.Clock;
import com.westar.base.pojo.CustomJob;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.ClockService;

@Component
public class QuartzJob {
	
	@Autowired
	ClockService clockService;
	
	/**
	 * 启动5秒后执行，1小时执行过一次
	 */
	@Scheduled(initialDelay=5000,fixedDelay=60L*60*1000)
	public void autoAlarmClock() {
		
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//取出距离现在30分钟内执行的定时提醒
		List<Clock> listClocks = clockService.listAlarmClocks(nowTime,65);
		if(null!=listClocks && listClocks.size()>0){
			
			for (Clock clock : listClocks) {
				
				//执行时间转换成corn格式
				try {
					String cornTime = DateTimeUtil.transCorn(clock.getClockNextDate());
					CustomJob job = new CustomJob();
					job.setJobId("task_"+clock.getId());
					job.setJobGroup("group_"+clock.getId());
					job.setCronExpression(cornTime);//每五秒执行一次
					job.setStateFulljobExecuteClass(QuartzJobOne.class);
					
					JobDataMap paramsMap = new JobDataMap();
					paramsMap.put("task_"+clock.getId(),clock);
					QuartzManager.enableCronSchedule(job, paramsMap, true);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			//修改执行状态为待执行
			clockService.updateClockExecuteState(listClocks);
		}
	}
}
