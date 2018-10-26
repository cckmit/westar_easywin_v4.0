package com.westar.core.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.westar.core.service.UploadService;

//@Component
public class FileJob {
	@Autowired
	UploadService uploadService;

	/**
	 * 启动5秒后执行，30秒一次，自动从数据库下载附件
	 */
	//@Scheduled(initialDelay = 5000, fixedDelay = 30000)
	public void autoDownFile() {
		try {
			//TODO 单服务器不需要进行数据下载
			//uploadService.doAutoDownFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
