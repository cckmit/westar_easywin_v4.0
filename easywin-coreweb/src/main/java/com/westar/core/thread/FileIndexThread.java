

package com.westar.core.thread;

import com.westar.base.model.Filecontent;
import com.westar.core.service.UploadService;

public class FileIndexThread implements Runnable {

	private UploadService uploadService;

	private Filecontent filecontent;

	public UploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(UploadService uploadService) {
		this.uploadService = uploadService;
	}

	public Filecontent getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(Filecontent filecontent) {
		this.filecontent = filecontent;
	}

	public FileIndexThread(UploadService uploadService, Filecontent filecontent) {
		this.uploadService = uploadService;
		this.filecontent = filecontent;
	}

	@Override
	public void run() {
		try {
			uploadService.addFileContent(filecontent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

