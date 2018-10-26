package com.westar.core.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.RsdaBase;
import com.westar.base.model.RsdaBaseFile;
import com.westar.base.model.RsdaOther;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.RsdaBaseDao;

@Service
public class RsdaBaseService {

	@Autowired
	RsdaBaseDao rsdaBaseDao;

	/**
	 * 分页查询人事档案信息
	 * @param sessionUser 当前操作员
	 * @param rsdaBase 人事档案的基本信息
	 * @return
	 */
	public List<RsdaBase> listPagedRsdaBase(UserInfo sessionUser,
			RsdaBase rsdaBase) {
		return rsdaBaseDao.listPagedRsdaBase(sessionUser,rsdaBase);
	}

	/**
	 * 通过人员主键查询
	 * @param sessionUser 当前操作人员
	 * @param userId 人员主键
	 * @return
	 */
	public RsdaBase queryRsdaBaseByUserId(UserInfo sessionUser, Integer userId) {
		RsdaBase rsdaBase = rsdaBaseDao.queryRsdaBaseByUserId(sessionUser,userId);
		if(null!=rsdaBase && null != rsdaBase.getId()){
			rsdaBase.setListRsdaBaseFiles(rsdaBaseDao.listRsdaBaseFiles(rsdaBase.getId(),sessionUser));
		}
		return rsdaBase;
	}
	/**
	 * 通过人员主键查询
	 * @param sessionUser 当前操作人员
	 * @param rsdaId 人员主键
	 * @return
	 */
	public RsdaBase queryRsdaBaseByRsdaId(UserInfo sessionUser, Integer rsdaId) {
		RsdaBase rsdaBase = rsdaBaseDao.queryRsdaBaseByRsdaId(sessionUser,rsdaId);
		if(null!=rsdaBase && null != rsdaBase.getId()){
			rsdaBase.setListRsdaBaseFiles(rsdaBaseDao.listRsdaBaseFiles(rsdaBase.getId(),sessionUser));
		}
		return rsdaBase;
	}

	/**
	 * 修改人事档案信息 
	 * @param rsdaBase 事档案信息 
	 * @param sessionUser 当前操作人员
	 */
	public void updateRsdaBaseInfo(UserInfo sessionUser,RsdaBase rsdaBase) {
		Integer rsdaBaseId = rsdaBase.getId();
		//没有档案信息
		if(null == rsdaBaseId || rsdaBaseId == 0){
			rsdaBaseId = rsdaBaseDao.add(rsdaBase);
		}else{
			rsdaBaseDao.update(rsdaBase);
		}
		//修改工号
		if(!StringUtils.isEmpty(rsdaBase.getJobNumber())){
			RsdaOther rsdaOther = new RsdaOther();
			rsdaOther.setComId(sessionUser.getComId());
			rsdaOther.setUserId(sessionUser.getId());
			rsdaOther.setJobNumber(rsdaBase.getJobNumber());
			rsdaBaseDao.updteRsdaJobNum(rsdaOther);
		}
		
		
	}
	/**
	 * 修改人事档案信息 
	 * @param rsdaBase 事档案信息 
	 * @param sessionUser 当前操作人员
	 */
	public void updateRsdaBaseOhter(UserInfo sessionUser,RsdaBase rsdaBase) {
		Integer rsdaBaseId = rsdaBase.getId();
		//没有档案信息
		if(null == rsdaBaseId || rsdaBaseId == 0){
			rsdaBaseId = rsdaBaseDao.add(rsdaBase);
		}else{
			rsdaBaseDao.update(rsdaBase);
		}
		
		rsdaBaseDao.delByField("rsdaBaseFile", new String[]{"comId","rsdaBaseId"}, 
				new Object[]{sessionUser.getComId(),rsdaBaseId});
		
		List<RsdaBaseFile> listRsdaBaseFiles = rsdaBase.getListRsdaBaseFiles();
		if(null!=listRsdaBaseFiles && !listRsdaBaseFiles.isEmpty()){
			for (RsdaBaseFile rsdaBaseFile : listRsdaBaseFiles) {
				rsdaBaseFile.setRsdaBaseId(rsdaBaseId);
				rsdaBaseFile.setComId(sessionUser.getComId());
				rsdaBaseDao.add(rsdaBaseFile);
			}
		}
	}
	/**
	 * 修改人事档案信息 
	 * @param rsdaBase 事档案信息 
	 * @param sessionUser 当前操作人员
	 */
	public void updateRsdaBaseJob(UserInfo sessionUser,RsdaBase rsdaBase) {
		RsdaOther rsdaOther = rsdaBase.getRsdaOther();
		Integer rsdaOtherId = rsdaOther.getId();
		//没有档案信息
		if(null == rsdaOtherId || rsdaOtherId == 0){
			rsdaOther.setComId(sessionUser.getComId());
			rsdaOtherId = rsdaBaseDao.add(rsdaOther);
		}else{
			rsdaBaseDao.update(rsdaOther);
		}
		
	}
	
}
