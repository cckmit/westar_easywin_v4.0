package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Car;
import com.westar.base.model.CarMaintainRecord;
import com.westar.base.model.CarScopeDep;
import com.westar.base.model.CarScopeUser;
import com.westar.base.model.CarType;
import com.westar.base.model.CarUseRecord;
import com.westar.base.model.InsuranceRecord;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class CarDao extends BaseDao {
	/***************************** 以下是车辆列表 *****************************************/
	/**
	 * 查询我的申请车辆记录
	 * @param car
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> listCarApplyOfMin(CarUseRecord applyRecord, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.carModel carName,b.carNum ");
		sql.append("\n from carUseRecord a");
		sql.append("\n inner join  car b on a.comid = b.comid and a.carId = b.id");
		sql.append("\n where a.comId = ? and  a.applyer = ? ");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		//申请时间段
		this.addSqlWhere(applyRecord.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(applyRecord.getEndDate(), sql, args, " and  substr(a.recordcreatetime,0,10)<=?");
		//类型筛选
		this.addSqlWhere(applyRecord.getState(), sql, args, " and a.state =?");
		
		if(null != applyRecord.getSearchLike() && !applyRecord.getSearchLike().isEmpty()){
			sql.append("\n and( ");
			this.addSqlWhereLike(applyRecord.getSearchLike(), sql, args, "  b.carNum like ? \n");
			this.addSqlWhereLike(applyRecord.getSearchLike(), sql, args, " or b.carModel like ? \n");
			this.addSqlWhereLike(applyRecord.getSearchLike(), sql, args, " or a.destination like ? \n");
			this.addSqlWhereLike(applyRecord.getSearchLike(), sql, args, " or a.reason like ? \n");
			sql.append("\n ) ");
		}
		return pagedQuery(sql.toString(), " a.state asc ", args.toArray(), CarUseRecord.class);
	}
	/**
	 * 待批的车辆申请记录
	 * @param car
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> listCarApplyToDo(CarUseRecord applyRecord, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.carModel carName,b.carNum,c.userName applyerName,c.gender,d.uuid userUuid,d.filename userFileName ");
		sql.append("\n from carUseRecord a");
		sql.append("\n inner join  car b on a.comid = b.comid and a.carId = b.id");
		sql.append("\n left join userinfo c on  a.applyer = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comId = ?  ");
		args.add(userInfo.getComId());
		//申请时间段
		this.addSqlWhere(applyRecord.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(applyRecord.getEndDate(), sql, args, " and  substr(a.recordcreatetime,0,10)<=?");
		//申请人
		this.addSqlWhere(applyRecord.getApplyer(), sql, args, " and  a.applyer=?");
		
		if(null != applyRecord.getSearchLike() && !applyRecord.getSearchLike().isEmpty()){
			sql.append("\n and( ");
			this.addSqlWhereLike(applyRecord.getSearchLike(), sql, args, "  b.carNum like ? \n");
			this.addSqlWhereLike(applyRecord.getSearchLike(), sql, args, " or b.carModel like ? \n");
			this.addSqlWhereLike(applyRecord.getSearchLike(), sql, args, " or a.destination like ? \n");
			this.addSqlWhereLike(applyRecord.getSearchLike(), sql, args, " or a.reason like ? \n");
			sql.append("\n ) ");
		}
		return pagedQuery(sql.toString(), " a.state,a.recordCreateTime desc", args.toArray(), CarUseRecord.class);
	}
	public List<Car> listCarOfUsing(Car car, UserInfo userInfo) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 所有车辆列表
	 * @param car
	 * @param userInfo
	 * @param isModAdmin
	 * @return
	 */
	public List<Car> listCarOfAll(Car car, UserInfo userInfo,boolean isModAdmin) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.*,a.qxEndDate,b.syxEndDate ");
		sql.append("\n from car c");
		sql.append("\n left join (select a.carid,max(a.endDate) qxEndDate from insuranceRecord a where a.comid= ? and  a.insuranceType =  '"+ConstantInterface.TYPE_CLGL_QX+"' group by a.carid) a");
		args.add(userInfo.getComId());
		sql.append("\n on  c.id = a.carId");
		sql.append("\n left join (select a.carid,max(a.endDate) syxEndDate from insuranceRecord a where a.comid= ? and  a.insuranceType =  '"+ConstantInterface.TYPE_CLGL_SYX+"' group by a.carid) b");
		args.add(userInfo.getComId());
		sql.append("\n on   c.id = b.carId");
		sql.append("\n where 1=1 and c.comId=?");
		args.add(userInfo.getComId());
		if(!isModAdmin){
			sql.append("\n and c.id in (");
			//部门范围权限
			sql.append("\n select carId from carScopeDep a where a.comId = ?");
			args.add(userInfo.getComId());
			sql.append("\n   and  exists (");
			sql.append("\n  select * from ( ");
			sql.append("\n  	select * from  department b ");
			sql.append("where  b.comid = ? and b.enabled=1 \n");
			args.add(userInfo.getComId());
			sql.append("start with b.id = ? \n");
			args.add(userInfo.getDepId());
			sql.append("connect by prior b.parentid  = b.id \n");
			sql.append("\n  )b where a.depId = b.id");
			sql.append("\n  )");
			//人员范围权限
			sql.append("\n union ");
			sql.append("\n select carId from carScopeUser b where b.comId = ? and b.userId = ?");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//未范围权限
			sql.append("\n union ");
			sql.append("\n select c.id carId from car c where c.comId = ? and c.scopeType = 0");
			args.add(userInfo.getComId());
			sql.append("\n )");
			sql.append("\n  and c.stateType = 1");
		}
		
		//查询创建时间段
		this.addSqlWhere(car.getStartDate(), sql, args, " and substr(c.recordcreatetime,0,10)>=?");
		this.addSqlWhere(car.getEndDate(), sql, args, " and substr(c.recordcreatetime,0,10)<=?");
		//类型筛选
		this.addSqlWhere(car.getStateType(), sql, args, " and c.stateType =?");
		
		if(null != car.getCarModel() && !car.getCarModel().isEmpty()){
			sql.append("\n and( ");
			this.addSqlWhereLike(car.getCarModel(), sql, args, "  c.carNum like ? \n");
			this.addSqlWhereLike(car.getCarModel(), sql, args, " or c.carModel like ? \n");
			sql.append("\n ) ");
		}
		return pagedQuery(sql.toString(), " c.recordCreateTime desc", args.toArray(), Car.class);
	}
	/***************************** 以上是车辆列表 *****************************************/
	/***************************** 以下是车辆类型管理 *****************************************/
	/**
	 * 获取车辆类型集合
	 * @param comId
	 * @return
	 */
	public List<CarType> listCarType(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from carType a where a.comId=? ");
		args.add(comId);
		sql.append(" order by a.orderNum asc");
		return this.listQuery(sql.toString(), args.toArray(),CarType.class);
	}
	/**
	 * 获取车辆类型最大序号
	 * @param comId
	 * @return
	 */
	public CarType queryCarTypeOrderMax(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.orderNum)+1  as orderNum from CarType a where a.comid =?");
		args.add(comId);
		return (CarType)this.objectQuery(sql.toString(), args.toArray(),CarType.class);
	}
	/**
	 * 查询处于某类型的车辆数量
	 * @param carTypeId
	 * @param userInfo
	 * @return
	 */
	public Integer countCarByCarType(Integer carTypeId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from car where carTypeId = ? and comId = ?");
		args.add(carTypeId);
		args.add(userInfo.getComId());
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 车辆申请人员范围
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarScopeUser> listCarScopeUser(Integer carId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.userName from carScopeUser a");
		sql.append("\n inner join userInfo b on  a.userId = b.id");
		sql.append("\n where a.comid = ? and a.carId =? ");
		args.add(userInfo.getComId());
		args.add(carId);
		return listQuery(sql.toString(), args.toArray(), CarScopeUser.class);
	}
	/**
	 * 车辆申请部门范围
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarScopeDep> listCarScopeDep(Integer carId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.depName from carScopeDep a");
		sql.append("\n inner join department b on  a.depId = b.id and a.comId = b.comId");
		sql.append("\n where a.comid = ? and a.carId =? ");
		args.add(userInfo.getComId());
		args.add(carId);
		return listQuery(sql.toString(), args.toArray(), CarScopeDep.class);
	}
	/**
	 * 车辆保险记录
	 * @param carId
	 * @param userInfo
	 * @param busType
	 * @return
	 */
	public List<InsuranceRecord> listInsuranceRecord(Integer carId, UserInfo userInfo,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from insuranceRecord a");
		sql.append("\n where a.comid = ? and a.carId =? ");
		args.add(userInfo.getComId());
		args.add(carId);
		this.addSqlWhere(busType, sql, args, " and a.insuranceType = ? ");
		sql.append("\n order by a.endDate desc ");
		return listQuery(sql.toString(), args.toArray(), InsuranceRecord.class);
	}
	/**
	 * 车辆维修记录列表
	 * @param carId 车辆主键
	 * @return
	 */
	public List<CarMaintainRecord> listMaintainRecord(Integer carId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.userName executorName,c.gender,d.uuid userUuid,d.filename userFileName from carMaintainRecord a");
		sql.append("\n left join userinfo c on  a.executor = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid = ? and a.carId =? ");
		args.add(userInfo.getComId());
		args.add(carId);
		return pagedQuery(sql.toString(), " a.recordCreateTime desc", args.toArray(),CarMaintainRecord.class);
	}
	/**
	 * 通过ID查询维修记录
	 * @param id
	 * @return
	 */
	public CarMaintainRecord queryMaintainRecordById(Integer id, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.userName executorName,c.gender,d.uuid userUuid,d.filename userFileName from carMaintainRecord a");
		sql.append("\n left join userinfo c on  a.executor = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid = ? and a.id =? ");
		args.add(userInfo.getComId());
		args.add(id);
		return (CarMaintainRecord) this.objectQuery(sql.toString(), args.toArray(), CarMaintainRecord.class);
	}
	/**
	 * 车辆使用记录
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> listCarUserRecord(Integer carId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.carModel carName,b.carNum ");
		sql.append("\n from carUseRecord a");
		sql.append("\n inner join  car b on a.comid = b.comid and a.carId = b.id");
		sql.append("\n where a.comId = ?  and a.carId = ? and state in (1,2)");
		args.add(userInfo.getComId());
		args.add(carId);
		return pagedQuery(sql.toString(), " a.startDate desc ", args.toArray(), CarUseRecord.class);
	}
	/**
	 * 可申请的车辆列表
	 * @param userInfo
	 * @return
	 */
	public List<Car> listApplyCar(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from car a ");
		sql.append("\n where a.stateType = 1 and a.comId = ?");
		args.add(userInfo.getComId());
		sql.append("\n and a.id in (");
		sql.append("\n select carId from carScopeDep a where a.comId = ?");
		args.add(userInfo.getComId());
		sql.append("\n   and  exists (");
		sql.append("\n  select * from ( ");
		sql.append("\n  	select * from  department b ");
		sql.append("where  b.comid = ? and b.enabled=1 \n");
		args.add(userInfo.getComId());
		sql.append("start with b.id = ? \n");
		args.add(userInfo.getDepId());
		sql.append("connect by prior b.parentid  = b.id \n");
		sql.append("\n  )b where a.depId = b.id");
		sql.append("\n  )");
		sql.append("\n union ");
		sql.append("\n select carId from carScopeUser b where b.comId = ? and b.userId = ?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n union ");
		sql.append("\n select c.id carId from car c where c.comId = ? and c.scopeType = 0");
		args.add(userInfo.getComId());
		sql.append("\n )");
		return listQuery(sql.toString(), args.toArray(), Car.class);
	}
	/**
	 * 通过主键获取申请的信息
	 * @param carApplyId
	 * @param userInfo
	 * @return
	 */
	public CarUseRecord queryCarApplyById(Integer carApplyId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.carModel carName,b.carNum");
		sql.append("\n from carUseRecord a");
		sql.append("\n inner join car b on a.comid = b.comid and a.carId = b.id");
		sql.append("\n where a.comId = ? and  a.id = ?");
		args.add(userInfo.getComId());
		args.add(carApplyId);
		return (CarUseRecord) objectQuery(sql.toString(), args.toArray(), CarUseRecord.class);
	}
	/**
	 * 该日期内的申请情况
	 * @param date
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> listApplyRecordByDate(String date,Integer carId,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		String dateMin = date+" 00:00:00";
		String dateMax = date+" 23:59:59";
		sql.append("\n select a.*");
		sql.append("\n from carUseRecord a");
		sql.append("\n where a.comId = ? and  a.carId = ?");
		args.add(userInfo.getComId());
		args.add(carId);
		sql.append("\n and a.startDate<? and a.endDate > ? and a.state in(0,1)");
		args.add(dateMax);
		args.add(dateMin);
		return this.listQuery(sql.toString(), args.toArray(), CarUseRecord.class);
	}
	/**
	 * 检查是否有申请时间冲突
	 * @param carApply
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> checkCarApply(CarUseRecord carApply, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*");
		sql.append("\n from carUseRecord a");
		sql.append("\n where a.comId = ? and  a.carId = ?");
		args.add(userInfo.getComId());
		args.add(carApply.getCarId());
		sql.append("\n and  a.state = 1");
		sql.append("\n and ( (a.endDate > ? and a.endDate < ?)");
		args.add(carApply.getStartDate());
		args.add(carApply.getEndDate());
		sql.append("\n or (a.startDate > ? and a.startDate < ?)");
		args.add(carApply.getStartDate());
		args.add(carApply.getEndDate());
		sql.append("\n or (a.startDate > ? and a.endDate > ?)");
		args.add(carApply.getStartDate());
		args.add(carApply.getEndDate());
		sql.append("\n )");
		return this.listQuery(sql.toString(), args.toArray(), CarUseRecord.class);
	}
	/**
	 * 查询该车辆未使用预约记录
	 * @param carId
	 * @param userInfo
	 * @return
	 */
	public List<CarUseRecord> noUseCarApply(Integer carId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*");
		sql.append("\n from carUseRecord a");
		sql.append("\n where a.comId = ? and  a.carId = ?");
		args.add(userInfo.getComId());
		args.add(carId);
		sql.append("\n and a.state = 1");
		return this.listQuery(sql.toString(), args.toArray(), CarUseRecord.class);
	}
}
