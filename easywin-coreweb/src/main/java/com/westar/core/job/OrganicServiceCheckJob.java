package com.westar.core.job;

import org.apache.log4j.Logger;

import com.westar.core.service.OrganicService;

/**
 * 检测团队服务配置
 *
 */
//@Component
public class OrganicServiceCheckJob {
	private static final Logger logger = Logger.getRootLogger();
	
//	@Autowired
	OrganicService organicService;
	
//	@Scheduled(cron = "0 0 1 * * ?")
	public void organicServiceCheck(){
		logger.info("检测团队服务配置.....................");
		organicService.updateOrganicService();
	}

}
