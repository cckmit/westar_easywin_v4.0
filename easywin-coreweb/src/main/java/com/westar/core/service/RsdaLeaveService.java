package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.RsdaLeave;
import com.westar.base.model.RsdaLeaveFile;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.RsdaLeaveDao;

@Service
public class RsdaLeaveService {

	@Autowired
	RsdaLeaveDao rsdaLeaveDao;

	/**
	 * 分页查询的离职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaLeave 离职信息
	 * @return
	 */
	public List<RsdaLeave> listPagedRsdaLeave(UserInfo sessionUser,
			RsdaLeave rsdaLeave) {
		return rsdaLeaveDao.listPagedRsdaLeave(sessionUser,rsdaLeave);
	}
	/**
	 * 添加人员的离职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaLeave 离职信息
	 */
	public void addRsdaLeave(UserInfo sessionUser, RsdaLeave rsdaLeave) {
		rsdaLeave.setComId(sessionUser.getComId());
		Integer rsdaLeaveId = rsdaLeaveDao.add(rsdaLeave);
		
		List<RsdaLeaveFile> listRsdaLeaveFiles = rsdaLeave.getListRsdaLeaveFiles();
		if(null!=listRsdaLeaveFiles && !listRsdaLeaveFiles.isEmpty()){
			for (RsdaLeaveFile rsdaLeaveFile : listRsdaLeaveFiles) {
				rsdaLeaveFile.setComId(sessionUser.getComId());
				rsdaLeaveFile.setRsdaLeaveId(rsdaLeaveId);
				rsdaLeaveDao.add(rsdaLeaveFile);
			}	
		}
		
	}
	/**
	 * 修改离职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaLeave 离职信息
	 */
	public void updateRsdaLeave(UserInfo sessionUser, RsdaLeave rsdaLeave) {
		Integer rsdaLeaveId = rsdaLeave.getId();
		rsdaLeaveDao.update(rsdaLeave);
		
		rsdaLeaveDao.delByField("rsdaLeaveFile", new String[]{"rsdaLeaveId"}, new Object[]{rsdaLeaveId});
		
		List<RsdaLeaveFile> listRsdaLeaveFiles = rsdaLeave.getListRsdaLeaveFiles();
		if(null!=listRsdaLeaveFiles && !listRsdaLeaveFiles.isEmpty()){
			for (RsdaLeaveFile rsdaLeaveFile : listRsdaLeaveFiles) {
				rsdaLeaveFile.setComId(sessionUser.getComId());
				rsdaLeaveFile.setRsdaLeaveId(rsdaLeaveId);
				rsdaLeaveDao.add(rsdaLeaveFile);
			}	
		}
		
	}
	/**
	 * 根据主键查询离职
	 * @param sessionUser 当前操作人员
	 * @param rsdaLeaveId 离职信息主键
	 * @return
	 */
	public RsdaLeave queryRsdaLeaveById(UserInfo sessionUser,
			Integer rsdaLeaveId) {
		RsdaLeave rsdaLeave = rsdaLeaveDao.queryRsdaLeaveById(sessionUser,rsdaLeaveId);
		if(null!=rsdaLeave){
			List<RsdaLeaveFile> listRsdaLeaveFiles =rsdaLeaveDao.listRsdaLeaveFiles(sessionUser,rsdaLeaveId);
			rsdaLeave.setListRsdaLeaveFiles(listRsdaLeaveFiles);
		}
		return rsdaLeave;
	}
	/**
	 * 删除离职信息
	 * @param sessionUser 当前操作人员
	 * @param ids 离职信息主键
	 */
	public void deleteRsdaLeave(UserInfo sessionUser, Integer[] ids) {
		for (Integer rsdaLeaveId : ids) {
			rsdaLeaveDao.delByField("rsdaLeaveFile", new String[]{"rsdaLeaveId"}, new Object[]{rsdaLeaveId});
			rsdaLeaveDao.delById(RsdaLeave.class, rsdaLeaveId);
		}
	}
	
	
	
}
