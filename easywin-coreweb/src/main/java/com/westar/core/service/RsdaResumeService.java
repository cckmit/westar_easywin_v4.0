package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.RsdaResume;
import com.westar.base.model.RsdaResumeFile;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.RsdaResumeDao;

@Service
public class RsdaResumeService {

	@Autowired
	RsdaResumeDao rsdaResumeDao;

	/**
	 * 分页查询复职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaResume 复职信息查询条件
	 * @return
	 */
	public List<RsdaResume> listPagedRsdaResume(UserInfo sessionUser,
			RsdaResume rsdaResume) {
		return rsdaResumeDao.listPagedRsdaResume(sessionUser,rsdaResume);
	}

	/**
	 * 添加复职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaResume 复职信息
	 */
	public void addRsdaResume(UserInfo sessionUser, RsdaResume rsdaResume) {
		Integer comId = sessionUser.getComId();
		rsdaResume.setComId(comId);
		Integer resumeId = rsdaResumeDao.add(rsdaResume);
		
		List<RsdaResumeFile> listResumeFiles = rsdaResume.getListResumeFiles();
		if(null!=listResumeFiles && !listResumeFiles.isEmpty()){
			for (RsdaResumeFile rsdaResumeFile : listResumeFiles) {
				rsdaResumeFile.setComId(comId);
				rsdaResumeFile.setRsdaResumeId(resumeId);
				rsdaResumeDao.add(rsdaResumeFile);
			}
		}
		
	}

	/**
	 * 修改人员的复职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaResume 复职信息
	 */
	public void updateRsdaResume(UserInfo sessionUser, RsdaResume rsdaResume) {
		Integer resumeId = rsdaResume.getId();
		rsdaResumeDao.update(rsdaResume);
		
		//删除原有的复职附件
		rsdaResumeDao.delByField("rsdaResumeFile", new String[]{"rsdaResumeId"}, new Object[]{resumeId});
		//添加本次的复职信息
		List<RsdaResumeFile> listResumeFiles = rsdaResume.getListResumeFiles();
		if(null!=listResumeFiles && !listResumeFiles.isEmpty()){
			for (RsdaResumeFile rsdaResumeFile : listResumeFiles) {
				rsdaResumeFile.setComId(sessionUser.getComId());
				rsdaResumeFile.setRsdaResumeId(resumeId);
				rsdaResumeDao.add(rsdaResumeFile);
			}
		}
		
	}
	/**
	 * 通过复职主键查询复职信息
	 * @param sessionUser 当前操作人员
	 * @param resumeId 复职主键
	 * @return
	 */
	public RsdaResume queryRsdaResumeById(UserInfo sessionUser, Integer resumeId) {
		RsdaResume rsdaResume = rsdaResumeDao.queryRsdaResumeById(sessionUser,resumeId);
		if(null!=rsdaResume){
			rsdaResume.setListResumeFiles(rsdaResumeDao.listResumeFiles(sessionUser,resumeId));
		}
		return rsdaResume;
	}

	/**
	 * 删除复职信息
	 * @param sessionUser 当前操作人员
	 * @param ids 复职信息主键
	 */
	public void deleteRsdaResume(UserInfo sessionUser, Integer[] ids) {
		if(null!=ids){
			for (Integer resumeId : ids) {
				rsdaResumeDao.delByField("rsdaResumeFile", new String[]{"rsdaResumeId"}, new Object[]{resumeId});
			}
			rsdaResumeDao.delById(RsdaResume.class, ids);
		}
		
	}
	
	
	
}
