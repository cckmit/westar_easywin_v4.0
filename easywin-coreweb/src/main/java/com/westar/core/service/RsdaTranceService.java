package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.RsdaTrance;
import com.westar.base.model.RsdaTranceFile;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.RsdaTranceDao;

@Service
public class RsdaTranceService {

	@Autowired
	RsdaTranceDao rsdaTranceDao;

	/**
	 * 分页查询人事调动信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaTrance 人事调动的查询条件
	 * @return
	 */
	public List<RsdaTrance> listPagedRsdaTrance(UserInfo sessionUser,
			RsdaTrance rsdaTrance) {
		return rsdaTranceDao.listPagedRsdaTrance(sessionUser,rsdaTrance);
	}

	/**
	 * 添加人事调动信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaTrance 人事调动信息
	 */
	public void addRsdaTrance(UserInfo sessionUser, RsdaTrance rsdaTrance) {
		rsdaTrance.setComId(sessionUser.getComId());
		Integer radaTranceId = rsdaTranceDao.add(rsdaTrance);
		List<RsdaTranceFile> listTranceFiles = rsdaTrance.getListTranceFiles();
		if(null!=listTranceFiles && !listTranceFiles.isEmpty()){
			for (RsdaTranceFile rsdaTranceFile : listTranceFiles) {
				rsdaTranceFile.setTranceId(radaTranceId);
				rsdaTranceFile.setComId(sessionUser.getComId());
				rsdaTranceDao.add(rsdaTranceFile);
			}
		}
	}

	/**
	 * 修改人事调动信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaTrance 人事调动的信息
	 */
	public void updateRsdaTrance(UserInfo sessionUser, RsdaTrance rsdaTrance) {
		Integer radaTranceId = rsdaTrance.getId();
		rsdaTranceDao.update(rsdaTrance);
		
		rsdaTranceDao.delByField("rsdaTranceFile", new String[]{"tranceId"}, new Object[]{radaTranceId});
		List<RsdaTranceFile> listTranceFiles = rsdaTrance.getListTranceFiles();
		if(null!=listTranceFiles && !listTranceFiles.isEmpty()){
			for (RsdaTranceFile rsdaTranceFile : listTranceFiles) {
				rsdaTranceFile.setTranceId(radaTranceId);
				rsdaTranceFile.setComId(sessionUser.getComId());
				rsdaTranceDao.add(rsdaTranceFile);
			}
		}
	}

	/**
	 * 根据主键查询人事调动信息
	 * @param sessionUser 当前操作人员
	 * @param radaTranceId 人事调动主键
	 * @return
	 */
	public RsdaTrance queryRsdaTranceById(UserInfo sessionUser,
			Integer radaTranceId) {
		RsdaTrance rsdaTrance = rsdaTranceDao.queryRsdaTranceById(sessionUser,radaTranceId);
		if(null!=rsdaTrance){
			List<RsdaTranceFile> listTranceFiles =rsdaTranceDao.listTranceFiles(sessionUser,radaTranceId);
			rsdaTrance.setListTranceFiles(listTranceFiles);
		}
		return rsdaTrance;
	}

	/**
	 * 删除人事调动信息
	 * @param sessionUser 当前操作人员
	 * @param ids 人事调动主键
	 */
	public void deleteRsdaTrance(UserInfo sessionUser, Integer[] ids) {
		for (Integer radaTranceId : ids) {
			rsdaTranceDao.delByField("rsdaTranceFile", new String[]{"tranceId"}, new Object[]{radaTranceId});
			rsdaTranceDao.delById(RsdaTrance.class, radaTranceId);
		}
		
	}
	
	
	
}
