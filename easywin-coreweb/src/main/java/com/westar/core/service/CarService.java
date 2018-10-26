package com.westar.core.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Car;
import com.westar.base.model.CarMaintainRecord;
import com.westar.base.model.CarScopeDep;
import com.westar.base.model.CarScopeUser;
import com.westar.base.model.CarType;
import com.westar.base.model.CarUpfile;
import com.westar.base.model.CarUseRecord;
import com.westar.base.model.InsuranceRecord;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.CarDao;

@Service
public class CarService {

	@Autowired
	CarDao carDao;

	@Autowired
	SystemLogService systemLogService;

	@Autowired
	CarUpfileService carUpfileService;

	/***************************** 以下是车辆列表 *****************************************/
	/**
	 * 申请车辆列表
	 * 
	 * @param car
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> listCarApplyOfMin(CarUseRecord applyRecord, UserInfo userInfo) {
		return carDao.listCarApplyOfMin(applyRecord, userInfo);
	}

	/**
	 * 待批的车辆使用申请
	 * 
	 * @param car
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> listCarApplyToDo(CarUseRecord applyRecord, UserInfo userInfo) {
		return carDao.listCarApplyToDo(applyRecord, userInfo);
	}

	/**
	 * 正在使用的车辆
	 * 
	 * @param car
	 * @param userInfo
	 * @return
	 */
	public List<Car> listCarOfUsing(Car car, UserInfo userInfo) {
		return carDao.listCarOfUsing(car, userInfo);
	}

	/**
	 * 所有车辆
	 * 
	 * @param car
	 * @param userInfo
	 * @return
	 */
	public List<Car> listCarOfAll(Car car, UserInfo userInfo, boolean isModAdmin) {
		return carDao.listCarOfAll(car, userInfo, isModAdmin);
	}

	/***************************** 以上是车辆列表 *****************************************/
	/**
	 * 添加车辆
	 * 
	 * @param car
	 * @param userInfo
	 */
	public void addCar(Car car, UserInfo userInfo) {
		car.setComId(userInfo.getComId());
		car.setCreator(userInfo.getId());

		if (null != car.getListScopeDep() || null != car.getListScopeUser()) {
			car.setScopeType(1);
		} else {
			car.setScopeType(0);
		}

		// 添加车辆
		Integer carId = carDao.add(car);
		// 车辆附件
		if (null != car.getListCarUpfiles() && !car.getListCarUpfiles().isEmpty()) {
			for (Upfiles upfile : car.getListCarUpfiles()) {
				carUpfileService.addCarUpfile(carId, upfile.getId(), userInfo.getId(), userInfo.getComId(),
						ConstantInterface.TYPE_CLGL);
			}
		}
		InsuranceRecord strongInsurance = car.getStrongInsurance();
		// 强险记录
		if (null != car.getStrongInsurance() && StringUtils.isNotEmpty(strongInsurance.getEndDate())) {
			strongInsurance.setCarId(carId);
			strongInsurance.setComId(userInfo.getComId());
			strongInsurance.setInsuranceType(ConstantInterface.TYPE_CLGL_QX);
			Integer qxId = carDao.add(strongInsurance);
			// 强险附件
			if (null != strongInsurance.getListUpfiles() && !strongInsurance.getListUpfiles().isEmpty()) {
				for (Upfiles upfile : strongInsurance.getListUpfiles()) {
					carUpfileService.addCarUpfile(qxId, upfile.getId(), userInfo.getId(), userInfo.getComId(),
							ConstantInterface.TYPE_CLGL_QX);
				}
			}

		}
		InsuranceRecord syInsurance = car.getSyInsurance();
		// 商业险记录
		if (null != car.getSyInsurance() && StringUtils.isNotEmpty(syInsurance.getEndDate())) {
			syInsurance.setCarId(carId);
			syInsurance.setComId(userInfo.getComId());
			syInsurance.setInsuranceType(ConstantInterface.TYPE_CLGL_SYX);

			Integer syxId = carDao.add(syInsurance);
			if (null != syInsurance.getListUpfiles() && !syInsurance.getListUpfiles().isEmpty()) {
				for (Upfiles upfile : syInsurance.getListUpfiles()) {
					carUpfileService.addCarUpfile(syxId, upfile.getId(), userInfo.getId(), userInfo.getComId(),
							ConstantInterface.TYPE_CLGL_SYX);
				}
			}
		}
		// 部门范围
		if (null != car.getListScopeDep() && !car.getListScopeDep().isEmpty()) {
			for (CarScopeDep carScopeDep : car.getListScopeDep()) {
				carScopeDep.setComId(userInfo.getComId());
				carScopeDep.setCarId(carId);
				carScopeDep.setCreator(userInfo.getId());
				carDao.add(carScopeDep);
			}
		}
		// 人员范围
		if (null != car.getListScopeUser() && !car.getListScopeUser().isEmpty()) {
			for (CarScopeUser carScopeUser : car.getListScopeUser()) {
				carScopeUser.setCarId(carId);
				carScopeUser.setComId(userInfo.getComId());
				carScopeUser.setCreator(userInfo.getId());
				carDao.add(carScopeUser);
			}
		}
		//添加工作轨迹
		systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_CLGL, 
				carId, "创建车辆\"" + car.getCarNum() + "\"", "创建车辆\"" + car.getCarNum() + "\"");
	}

	/**
	 * 修改车辆信息
	 * 
	 * @param car
	 * @param userInfo
	 */
	public void updateCar(Car car, UserInfo userInfo) {
		if (null != car.getListScopeDep() || null != car.getListScopeUser()) {
			car.setScopeType(1);
		} else {
			car.setScopeType(0);
		}
		// 修改 车辆基本信息
		carDao.update(car);
		// 删除范围申请范围
		carDao.delByField("carScopeDep", new String[] { "comId", "carId" },
				new Object[] { userInfo.getComId(), car.getId() });
		carDao.delByField("carScopeUser", new String[] { "comId", "carId" },
				new Object[] { userInfo.getComId(), car.getId() });
		// 添加申请范围
		// 部门范围
		if (null != car.getListScopeDep() && !car.getListScopeDep().isEmpty()) {
			for (CarScopeDep carScopeDep : car.getListScopeDep()) {
				carScopeDep.setComId(userInfo.getComId());
				carScopeDep.setCarId(car.getId());
				carScopeDep.setCreator(userInfo.getId());
				carDao.add(carScopeDep);
			}
		}
		// 人员范围
		if (null != car.getListScopeUser() && !car.getListScopeUser().isEmpty()) {
			for (CarScopeUser carScopeUser : car.getListScopeUser()) {
				carScopeUser.setCarId(car.getId());
				carScopeUser.setComId(userInfo.getComId());
				carScopeUser.setCreator(userInfo.getId());
				carDao.add(carScopeUser);
			}
		}
		// 添加附件
		if (null != car.getListCarUpfiles() && !car.getListCarUpfiles().isEmpty()) {
			for (Upfiles upfile : car.getListCarUpfiles()) {
				carUpfileService.addCarUpfile(car.getCarTypeId(), upfile.getId(), userInfo.getId(),
						userInfo.getComId(), ConstantInterface.TYPE_CLGL);
			}
		}

		// 强险记录
		if (null != car.getStrongInsurance()) {
			InsuranceRecord strongInsurance = car.getStrongInsurance();
			strongInsurance.setCarId(car.getId());
			strongInsurance.setComId(userInfo.getComId());
			strongInsurance.setInsuranceType(ConstantInterface.TYPE_CLGL_QX);
			Integer qxId = carDao.add(strongInsurance);
			// 强险附件
			if (null != strongInsurance.getListUpfiles() && !strongInsurance.getListUpfiles().isEmpty()) {
				for (Upfiles upfile : strongInsurance.getListUpfiles()) {
					carUpfileService.addCarUpfile(qxId, upfile.getId(), userInfo.getId(), userInfo.getComId(),
							ConstantInterface.TYPE_CLGL_QX);
				}
			}

		}
		// 商业险记录
		if (null != car.getSyInsurance()) {
			InsuranceRecord syInsurance = car.getSyInsurance();
			syInsurance.setCarId(car.getId());
			syInsurance.setComId(userInfo.getComId());
			syInsurance.setInsuranceType(ConstantInterface.TYPE_CLGL_SYX);

			Integer syxId = carDao.add(syInsurance);
			if (null != syInsurance.getListUpfiles() && !syInsurance.getListUpfiles().isEmpty()) {
				for (Upfiles upfile : syInsurance.getListUpfiles()) {
					carUpfileService.addCarUpfile(syxId, upfile.getId(), userInfo.getId(), userInfo.getComId(),
							ConstantInterface.TYPE_CLGL_SYX);
				}
			}
		}
	}

	/***************************** 以下是车辆类型配置 *****************************************/
	/**
	 * 获取车辆类型集合
	 * 
	 * @param comId
	 * @return
	 */
	public List<CarType> listCarType(Integer comId) {
		return carDao.listCarType(comId);
	}

	/**
	 * 添加车辆类型
	 * 
	 * @param carType
	 * @param userInfo
	 * @return
	 */
	public Integer addCarType(CarType carType, UserInfo userInfo) {
		Integer id = carDao.add(carType);
		// 添加系统日志记录
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "添加车辆类型:\"" + carType.getTypeName()
				+ "\"", ConstantInterface.TYPE_CLGL, userInfo.getComId(),userInfo.getOptIP());
		return id;
	}

	/**
	 * 获取车辆类型最大序号
	 * 
	 * @param comId
	 * @return
	 */
	public Integer queryCarTypeOrderMax(Integer comId) {
		CarType carType = carDao.queryCarTypeOrderMax(comId);
		return null == carType.getOrderNum() ? 1 : carType.getOrderNum();
	}

	/**
	 * 修改车辆类型名称
	 * 
	 * @param carType
	 * @param userInfo
	 * @return
	 */
	public boolean updateCarTypeName(CarType carType, UserInfo userInfo) {
		boolean succ = true;
		try {
			CarType obj = (CarType) carDao.objectQuery(CarType.class, carType.getId());
			if (null != obj && !obj.getTypeName().equals(carType.getTypeName())) {// 有变动
				// 更新车辆类型名称
				carDao.update(carType);
				// 添加系统日志记录
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),
						"更新车辆类型名称:\"" + carType.getTypeName() + "\"", ConstantInterface.TYPE_CLGL, 
						userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false;
		}
		return succ;
	}

	/**
	 * 修改车辆类型序号
	 * 
	 * @param carType
	 * @param userInfo
	 * @return
	 */
	public boolean updateCarTypeOrder(CarType carType, UserInfo userInfo) {
		boolean succ = true;
		try {
			CarType obj = (CarType) carDao.objectQuery(CarType.class, carType.getId());
			if (null != obj && !obj.getOrderNum().equals(carType.getOrderNum())) {// 有变动
				// 更新车辆类型
				carDao.update(carType);
				// 添加系统日志记录
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "更新车辆类型:\"" + obj.getTypeName()
						+ "\"的排序为\"" + carType.getOrderNum() + "\"", ConstantInterface.TYPE_CLGL, 
						userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false;
		}
		return succ;
	}

	/**
	 * 删除车辆类型
	 * 
	 * @param carType
	 * @param userInfo
	 * @return
	 */
	public boolean delCarType(CarType carType, UserInfo userInfo) {
		boolean succ = true;
		try {
			Integer crmNum = carDao.countCarByCarType(carType.getId(), userInfo);
			if (crmNum > 0) {
				succ = false;
			} else {
				// 获取需要删除的车辆类型
				CarType obj = (CarType) carDao.objectQuery(CarType.class, carType.getId());
				if (null != obj) {
					// 车辆类型删除
					carDao.delById(CarType.class, carType.getId());
					// 添加系统日志记录
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),
							"删除车辆类型:\"" + obj.getTypeName() + "\"", ConstantInterface.TYPE_CLGL, 
							userInfo.getComId(),userInfo.getOptIP());
				}
			}

		} catch (Exception e) {
			succ = false;
		}
		return succ;
	}

	/***************************** 以上是车辆类型配置 *****************************************/
	/**
	 * 通过ID查询车辆基本信息
	 * 
	 * @param carId
	 * @return
	 */
	public Car queryCarById(Integer carId, UserInfo userInfo) {
		Car car = (Car) carDao.objectQuery(Car.class, carId);
		List<Upfiles> listUpFiles = carUpfileService.listCarUpfileByType(carId, userInfo.getComId(),
				ConstantInterface.TYPE_CLGL);
		//设置文件总数
		List<CarUpfile> lists = carUpfileService.listCarUpfileByCar(carId, userInfo.getComId());
		if(lists != null) {
			car.setFileNum(lists.size());
		}
		if (null != listUpFiles && !listUpFiles.isEmpty()) {
			car.setListCarUpfiles(listUpFiles);
		}
		return car;
	}

	/**
	 * 车辆申请人员范围
	 * 
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarScopeUser> listCarScopeUser(Integer carId, UserInfo userInfo) {
		return carDao.listCarScopeUser(carId, userInfo);
	}

	/**
	 * 车辆申请部门范围
	 * 
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarScopeDep> listCarScopeDep(Integer carId, UserInfo userInfo) {
		return carDao.listCarScopeDep(carId, userInfo);
	}

	/**
	 * 强险记录
	 * 
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<InsuranceRecord> listQxRecord(Integer carId, UserInfo userInfo) {
		List<InsuranceRecord> list = carDao.listInsuranceRecord(carId, userInfo, ConstantInterface.TYPE_CLGL_QX);
		if (null != list && !list.isEmpty()) {
			for (InsuranceRecord insurance : list) {
				List<Upfiles> listUpFiles = carUpfileService.listCarUpfileByType(insurance.getId(),
						userInfo.getComId(), ConstantInterface.TYPE_CLGL_QX);
				if (null != listUpFiles && !listUpFiles.isEmpty()) {
					insurance.setListUpfiles(listUpFiles);
				}
			}
		}
		return list;
	}

	/**
	 * 商业险记录
	 * 
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<InsuranceRecord> listSyxRecord(Integer carId, UserInfo userInfo) {
		List<InsuranceRecord> list = carDao.listInsuranceRecord(carId, userInfo, ConstantInterface.TYPE_CLGL_SYX);
		if (null != list && !list.isEmpty()) {
			for (InsuranceRecord insurance : list) {
				List<Upfiles> listUpFiles = carUpfileService.listCarUpfileByType(insurance.getId(),
						userInfo.getComId(), ConstantInterface.TYPE_CLGL_SYX);
				if (null != listUpFiles && !listUpFiles.isEmpty()) {
					insurance.setListUpfiles(listUpFiles);
				}
			}
		}
		return list;
	}

	/**
	 * 车辆维修记录列表
	 * 
	 * @param carId
	 *            车辆主键
	 * @return
	 */
	public List<CarMaintainRecord> listMaintainRecord(Integer carId, UserInfo userInfo) {
		return carDao.listMaintainRecord(carId, userInfo);
	}

	/**
	 * 添加车辆维修记录
	 * 
	 * @param carId
	 * @param carMaintain
	 */
	public void addCarMaintainRecord(CarMaintainRecord carMaintain, UserInfo userInfo) {
		carMaintain.setComId(userInfo.getComId());
		carDao.add(carMaintain);
		Car car = new Car();
		car.setId(carMaintain.getCarId());
		car.setStateType(3);// 维修状态
		carDao.update(car);
	}

	/**
	 * 修改保险记录
	 * 
	 * @param insuranceRecord
	 * @return
	 * @throws Exception
	 */
	public void updateInsuranceRecord(InsuranceRecord insuranceRecord) {
		carDao.update(insuranceRecord);
	}

	/**
	 * 删除保险记录
	 * 
	 * @param insuranceRecord
	 * @return
	 * @throws Exception
	 */
	public void delInsuranceRecord(InsuranceRecord insuranceRecord, UserInfo userInfo) throws Exception {
		// 删除记录附件
		carDao.delByField("CarUpfile", new String[] { "comId", "busId", "busType" }, new Object[] {
				userInfo.getComId(), insuranceRecord.getId(), insuranceRecord.getInsuranceType() });
		carDao.delById(InsuranceRecord.class, insuranceRecord.getId());
	}

	/**
	 * 通过ID查询维修记录
	 * 
	 * @param id
	 * @return
	 */
	public CarMaintainRecord queryMaintainRecordById(Integer id, UserInfo userInfo) {
		return carDao.queryMaintainRecordById(id, userInfo);
	}

	/**
	 * 修改车辆维修记录
	 * 
	 * @param carMaintain
	 * @return
	 */
	public void updateMaintainRecord(CarMaintainRecord carMaintain) {
		carDao.update(carMaintain);
		if(null != carMaintain.getMaintainState() && carMaintain.getMaintainState()==1){
			Car car = new Car();
			car.setId(carMaintain.getCarId());
			car.setStateType(1);
			carDao.update(car);
		}
	}

	/**
	 * 删除维修记录
	 * 
	 * @param carMaintainRecord
	 */
	public void delCarMaintainRecord(CarMaintainRecord carMaintainRecord) {
		carDao.delById(CarMaintainRecord.class, carMaintainRecord.getId());
	}

	/**
	 * 添加车险记录
	 * 
	 * @param insuranceRecord
	 * @param userInfo
	 */
	public void addInsuranceRecord(InsuranceRecord insuranceRecord, UserInfo userInfo) {
		insuranceRecord.setComId(userInfo.getComId());
		Integer id = carDao.add(insuranceRecord);
		if (null != insuranceRecord.getListUpfiles() && !insuranceRecord.getListUpfiles().isEmpty()) {
			for (Upfiles upfile : insuranceRecord.getListUpfiles()) {
				carUpfileService.addCarUpfile(id, upfile.getId(), userInfo.getId(), userInfo.getComId(),
						insuranceRecord.getInsuranceType());
			}
		}

	}

	/**
	 * 车辆使用记录
	 * 
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> listCarUserRecord(Integer carId, UserInfo userInfo) {
		return carDao.listCarUserRecord(carId, userInfo);
	}

	/**
	 * 可申请的车辆列表
	 * 
	 * @param userInfo
	 * @return
	 */
	public List<Car> listApplyCar(UserInfo userInfo) {
		return carDao.listApplyCar(userInfo);
	}

	/**
	 * 添加车辆申请记录
	 * 
	 * @param carApply
	 * @param userInfo
	 */
	public void addCarApply(CarUseRecord carApply, UserInfo userInfo) {
		carApply.setComId(userInfo.getComId());
		// 申请状态
		carApply.setState(0);
		carApply.setApplyer(userInfo.getId());
		carDao.add(carApply);
	}

	/**
	 * 同意申请
	 * 
	 * @param carApply
	 * @param userInfo
	 */
	public void agreeCarApply(CarUseRecord carApply, UserInfo userInfo) {
		carApply.setState(1);
		carDao.update(carApply);
	}

	/**
	 * 拒绝申请
	 * 
	 * @param carApply
	 * @param userInfo
	 */
	public void voteCarApply(CarUseRecord carApply, UserInfo userInfo) {
		carApply.setState(3);
		carDao.update(carApply);
	}

	/**
	 * 通过主键获取申请的信息
	 * 
	 * @param carApplyId
	 * @param userInfo
	 * @return
	 */
	public CarUseRecord queryCarApplyById(Integer carApplyId, UserInfo userInfo) {
		return carDao.queryCarApplyById(carApplyId, userInfo);
	}

	/**
	 * 归还车辆
	 * 
	 * @param carApply
	 * @return
	 */
	public void returnCar(CarUseRecord carApply, UserInfo userInfo) {
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		carApply.setEndDate(nowTime);
		carApply.setState(2);
		carDao.update(carApply);
	}

	/**
	 * 撤回申请
	 * 
	 * @param carApply
	 */
	public void backoutApply(CarUseRecord carApply) {
		carApply.setState(4);
		carDao.update(carApply);
	}

	/**
	 * 该日期内该车辆申请情况
	 * 
	 * @param dateString
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> listCarApplyRecod(String dateString, Integer carId, UserInfo userInfo) {
		return carDao.listApplyRecordByDate(dateString, carId, userInfo);
	}

	/**
	 * 检查是否有申请时间冲突
	 * 
	 * @param carApply
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> checkCarApply(CarUseRecord carApply, UserInfo userInfo) {
		return carDao.checkCarApply(carApply, userInfo);
	}

	/**
	 * 查询该车辆未使用预约记录
	 * 
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> noUseCarApply(Integer carId, UserInfo userInfo) {
		return carDao.noUseCarApply(carId, userInfo);
	}

	/**
	 * 删除车辆
	 * 
	 * @param carId
	 * @param userInfo
	 */
	public void delCar(Integer carId, UserInfo userInfo) {
		// 删除使用记录
		carDao.delByField("carUseRecord", new String[] { "comId", "carId" },
				new Object[] { userInfo.getComId(), carId });
		// 删除范围
		carDao.delByField("carScopeUser", new String[] { "comId", "carId" },
				new Object[] { userInfo.getComId(), carId });
		carDao.delByField("carScopeDep", new String[] { "comId", "carId" }, new Object[] { userInfo.getComId(), carId });
		// 删除维修记录
		List<CarMaintainRecord> listCarMaintain = carDao.listMaintainRecord(carId, userInfo);
		for (CarMaintainRecord carMaintin : listCarMaintain) {
			// 删除车辆附件
			carDao.delByField("CarUpfile", new String[] { "comId", "busId", "busType" },
					new Object[] { userInfo.getComId(), carMaintin.getId(), ConstantInterface.TYPE_CLGL_WXJL });
		}
		carDao.delByField("carMaintainRecord", new String[] { "comId", "carId" }, new Object[] { userInfo.getComId(),
				carId });
		// 删除车辆保险记录
		List<InsuranceRecord> listBX = carDao.listInsuranceRecord(carId, userInfo, null);
		for (InsuranceRecord insurance : listBX) {
			// 删除车辆附件
			carDao.delByField("CarUpfile", new String[] { "comId", "busId", "busType" },
					new Object[] { userInfo.getComId(), insurance.getId(), insurance.getInsuranceType() });
		}
		carDao.delByField("InsuranceRecord", new String[] { "comId", "carId" }, new Object[] { userInfo.getComId(),
				carId });
		// 删除车辆附件
		carDao.delByField("CarUpfile", new String[] { "comId", "busId", "busType" }, new Object[] {
				userInfo.getComId(), carId, ConstantInterface.TYPE_CLGL });
		carDao.delById(Car.class, carId);

	}
}
