package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.CarUpfile;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.CarUpfileDao;

@Service
public class CarUpfileService {

	@Autowired
	CarUpfileDao carUpfileDao;
	
	@Autowired
	UploadService uploadService;
	/**
	 * 添加车辆附件
	 * @param busId
	 * @param upfileId
	 * @param userId
	 * @param comId
	 * @param busType
	 */
	public void addCarUpfile(Integer busId,Integer upfileId,Integer userId,Integer comId,String busType){
		CarUpfile carUpfile = new CarUpfile();
		carUpfile.setComId(comId);
		carUpfile.setUserId(userId);
		carUpfile.setUpfileId(upfileId);
		carUpfile.setBusId(busId);
		carUpfile.setBusType(busType);
		carUpfileDao.add(carUpfile);
	}
	/**
	 * 查询车辆下模块附件
	 * @param busId 业务主键
	 * @param comId 企业号
	 * @param busType 业务类型
	 * @return
	 */
	public List<Upfiles> listCarUpfileByType(Integer busId,Integer comId,String busType){
		return carUpfileDao.listCarUpfileByType(busId,comId,busType);
	}
	/**
	 * 查询该车辆下所有的附件信息
	 * @param carId 车辆主建
	 * @param comId
	 * @return
	 */
	public List<CarUpfile> listCarUpfileByCar(Integer carId,Integer comId){
		return carUpfileDao.listCarUpfileByCar(carId,comId);
	}
	
	/**
	 * 删除车辆附件
	 * @param carFileId
	 * @param userInfo
	 * @throws Exception 
	 */
	public void delCarUpfile(Integer carFileId, UserInfo userInfo,Integer busId,String busType) throws Exception {
		CarUpfile carUpfile = (CarUpfile) carUpfileDao.objectQuery(CarUpfile.class, carFileId);
		carUpfileDao.delById(CarUpfile.class, carFileId);
		//uploadService.updateUpfileIndex(carUpfile.getUpfileId(),userInfo,"del",busId,busType);
	}
}
