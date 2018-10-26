package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.RsdaIncFile;
import com.westar.base.model.RsdaIncentive;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.RsdaIncDao;

@Service
public class RsdaIncService {

	@Autowired
	RsdaIncDao rsdaIncDao;
	/**
	 * 分页查询人员的工作经历
	 * @param sessionUser 当前操作人员
	 * @param rsdaInc 工作经历查询条件
	 * @return
	 */
	public List<RsdaIncentive> listPagedRsdaInc(UserInfo sessionUser,
			RsdaIncentive rsdaInc) {
		return rsdaIncDao.listPagedRsdaInc(sessionUser,rsdaInc);
	}

	/**
	 * 添加工作经历
	 * @param sessionUser 当前操作人员
	 * @param rsdaInc 工作经历
	 */
	public void addRsdaInc(UserInfo sessionUser, RsdaIncentive rsdaInc) {
		rsdaInc.setComId(sessionUser.getComId());
		Integer rsdaIncId = rsdaIncDao.add(rsdaInc);
		List<RsdaIncFile> listRsdaIncFiles = rsdaInc.getListRsdaIncFiles();
		if(null!=listRsdaIncFiles && !listRsdaIncFiles.isEmpty()){
			for (RsdaIncFile rsdaIncFile : listRsdaIncFiles) {
				rsdaIncFile.setRsdaIncId(rsdaIncId);
				rsdaIncFile.setComId(sessionUser.getComId());
				rsdaIncDao.add(rsdaIncFile);
			}
		}
		
	}
	/**
	 * 修改工作经历
	 * @param sessionUser 当前操作人员
	 * @param jobHistory 工作经历
	 */
	public void updateRsdaInc(UserInfo sessionUser, RsdaIncentive rsdaInc) {
		Integer rsdaIncId = rsdaInc.getId();
		
		rsdaIncDao.delByField("rsdaIncFile", new String[]{"rsdaIncId"}, new Object[]{rsdaIncId});
		
		rsdaIncDao.update(rsdaInc);
		List<RsdaIncFile> listRsdaIncFiles = rsdaInc.getListRsdaIncFiles();
		if(null!=listRsdaIncFiles && !listRsdaIncFiles.isEmpty()){
			for (RsdaIncFile jobHisFile : listRsdaIncFiles) {
				jobHisFile.setRsdaIncId(rsdaIncId);
				jobHisFile.setComId(sessionUser.getComId());
				rsdaIncDao.add(jobHisFile);
			}
		}
		
	}

	/**
	 * 查询工作经历信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaIncId 工作经历的主键
	 * @return
	 */
	public RsdaIncentive queryRsdaIncById(UserInfo sessionUser, Integer rsdaIncId) {
		RsdaIncentive rsdaInc = rsdaIncDao.queryRsdaIncById(sessionUser,rsdaIncId);
		if(null!=rsdaInc){
			List<RsdaIncFile> listRsdaIncFiles =rsdaIncDao.listRsdaIncFiles(sessionUser,rsdaIncId);
			rsdaInc.setListRsdaIncFiles(listRsdaIncFiles);
		}
		return rsdaInc;
	}

	/**
	 * 删除奖惩信息
	 * @param sessionUser 当前操作人员
	 * @param ids 奖惩信息的主键
	 */
	public void deleteRsdaInc(UserInfo sessionUser, Integer[] ids) {
		for (Integer rsdaIncId : ids) {
			rsdaIncDao.delByField("rsdaIncFile", new String[]{"rsdaIncId"}, new Object[]{rsdaIncId});
			rsdaIncDao.delById(RsdaIncentive.class, rsdaIncId);
		}
		
	}
	
	
}
