package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.JobHisFile;
import com.westar.base.model.JobHistory;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.JobHistoryDao;

@Service
public class JobHistoryService {

	@Autowired
	JobHistoryDao jobHistoryDao;

	/**
	 * 分页查询人员的工作经历
	 * @param sessionUser 当前操作人员
	 * @param jobHistory 工作经历查询条件
	 * @return
	 */
	public List<JobHistory> listPagedJobHistory(UserInfo sessionUser,
			JobHistory jobHistory) {
		return jobHistoryDao.listPagedJobHistory(sessionUser,jobHistory);
	}

	/**
	 * 添加工作经历
	 * @param sessionUser 当前操作人员
	 * @param jobHistory 工作经历
	 */
	public void addJobHistory(UserInfo sessionUser, JobHistory jobHistory) {
		Integer jobHisId = jobHistoryDao.add(jobHistory);
		List<JobHisFile> listJobHisFiles = jobHistory.getListJobHisFiles();
		if(null!=listJobHisFiles && !listJobHisFiles.isEmpty()){
			for (JobHisFile jobHisFile : listJobHisFiles) {
				jobHisFile.setJobHisId(jobHisId);
				jobHisFile.setComId(sessionUser.getComId());
				jobHistoryDao.add(jobHisFile);
			}
		}
		
	}
	/**
	 * 修改工作经历
	 * @param sessionUser 当前操作人员
	 * @param jobHistory 工作经历
	 */
	public void updateJobHistory(UserInfo sessionUser, JobHistory jobHistory) {
		Integer jobHisId = jobHistory.getId();
		jobHistoryDao.update(jobHistory);
		
		jobHistoryDao.delByField("jobHisFile", new String[]{"jobHisId"}, new Object[]{jobHisId});
		
		List<JobHisFile> listJobHisFiles = jobHistory.getListJobHisFiles();
		if(null!=listJobHisFiles && !listJobHisFiles.isEmpty()){
			for (JobHisFile jobHisFile : listJobHisFiles) {
				jobHisFile.setJobHisId(jobHisId);
				jobHisFile.setComId(sessionUser.getComId());
				jobHistoryDao.add(jobHisFile);
			}
		}
		
	}

	/**
	 * 查询工作经历信息
	 * @param sessionUser 当前操作人员
	 * @param jobHisId 工作经历的主键
	 * @return
	 */
	public JobHistory queryJobHistoryById(UserInfo sessionUser, Integer jobHisId) {
		JobHistory jobHistory = jobHistoryDao.queryJobHistoryById(sessionUser,jobHisId);
		if(null!=jobHistory){
			List<JobHisFile> listJobHisFiles =jobHistoryDao.listJobHisFiles(sessionUser,jobHisId);
			jobHistory.setListJobHisFiles(listJobHisFiles);
		}
		return jobHistory;
	}

	/**
	 * 删除工作经历信息
	 * @param sessionUser 当前操作人员
	 * @param ids 工作经历的主键
	 */
	public void deleteJobHistory(UserInfo sessionUser, Integer[] ids) {
		for (Integer jobHisId : ids) {
			jobHistoryDao.delByField("jobHisFile", new String[]{"jobHisId"}, new Object[]{jobHisId});
			jobHistoryDao.delById(JobHistory.class, jobHisId);
		}
		
	}
	
	
	
}
