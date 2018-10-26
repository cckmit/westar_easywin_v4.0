

package com.westar.core.thread;

import com.westar.base.model.FileData;
import com.westar.core.service.UploadService;

public class FileInsertThread implements Runnable {

	private UploadService uploadService;

	private FileData fileData;

	public UploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(UploadService uploadService) {
		this.uploadService = uploadService;
	}

	public FileData getFileData() {
		return fileData;
	}

	public void setFileData(FileData fileData) {
		this.fileData = fileData;
	}

	public FileInsertThread(UploadService uploadService, FileData fileData) {
		this.uploadService = uploadService;
		this.fileData = fileData;
	}

	@Override
	public void run() {
		try {
			uploadService.addFileData(fileData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

