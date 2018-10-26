package com.westar.core.thread;

import java.util.List;

import com.westar.base.model.WebeditorFileData;
import com.westar.core.service.UploadService;

public class WebeditorFileThread implements Runnable {
	private UploadService uploadService;
	private String type;
	private Integer id;
	private List<String> listFileName;

	public UploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(UploadService uploadService) {
		this.uploadService = uploadService;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getListFileName() {
		return listFileName;
	}

	public void setListFileName(List<String> listFileName) {
		this.listFileName = listFileName;
	}

	public WebeditorFileThread(UploadService uploadService, String type, Integer id, List<String> listFileName) {
		this.uploadService = uploadService;
		this.type = type;
		this.id = id;
		this.listFileName = listFileName;
	}

	@Override
	public void run() {
		try {
			List<String> list = this.getListFileName();
			if (list != null) {
				for (String filename : list) {
					WebeditorFileData d = new WebeditorFileData();
					d.setFilename(filename);
					d.setRelationId(this.getId());
					d.setRelationType(this.getType());
					uploadService.addWebeditorFileData(d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
