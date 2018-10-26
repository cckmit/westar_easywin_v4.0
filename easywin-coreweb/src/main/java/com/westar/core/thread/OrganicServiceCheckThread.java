package com.westar.core.thread;

import org.apache.log4j.Logger;

import com.westar.core.service.OrganicService;

public class OrganicServiceCheckThread implements Runnable {
	private static final Logger logger = Logger.getRootLogger();
	
	private OrganicService organicService;
	private Integer comId;
	
	
	/**
	 * 带参构造函数
	 * @param organicService
	 * @param comId
	 */
	public OrganicServiceCheckThread(OrganicService organicService,
			Integer comId) {
		super();
		this.organicService = organicService;
		this.comId = comId;
	}


	@Override
	public void run() {
		logger.info("检测团队服务配置.....................");
		organicService.updateOrgMemberToUseless(comId);

	}

}
