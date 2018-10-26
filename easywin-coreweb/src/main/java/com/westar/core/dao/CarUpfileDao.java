package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.CarUpfile;
import com.westar.base.model.Upfiles;

@Repository
public class CarUpfileDao extends BaseDao {
	/**
	 * 查询车辆下模块附件
	 * @param busId 业务主键
	 * @param comId 企业号
	 * @param busType 业务类型
	 * @return
	 */
	public List<Upfiles> listCarUpfileByType(Integer busId, Integer comId, String busType) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 查询该车辆下所有的附件信息
	 * @param carId 车辆主建
	 * @param comId
	 * @return
	 */
	public List<CarUpfile> listCarUpfileByCar(Integer carId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n ");
		//车辆附件
		sql.append("\n select b.filename fileName,b.uuid,b.fileExt,c.userName creatorName,c.gender,d.uuid userUuid,d.filename userFileName");
		sql.append("\n ,a.id,a.comid,a.recordCreateTime,a.busType,a.busId,a.upfileId ");
		sql.append("\n from carUpfile a ");
		sql.append("\n inner join upfiles b on a.comId = b.comId and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid = ? and a.busId = ?");
		args.add(comId);
		args.add(carId);
		//保险附件
		sql.append("union all  \n");
		sql.append("\n select b.filename fileName,b.uuid,b.fileExt,c.userName creatorName,c.gender,d.uuid userUuid,d.filename userFileName");
		sql.append("\n ,a.id,a.comid,a.recordCreateTime,a.busType,a.busId,a.upfileId ");
		sql.append("\n from carUpfile a ");
		sql.append("\n inner join insuranceRecord i on a.comId = i.comId and a.busId = i.id");
		sql.append("\n inner join car car on car.comId = i.comId and car.id = i.carId");
		sql.append("\n inner join upfiles b on a.comId = b.comId and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid = ? and car.id = ?");
		args.add(comId);
		args.add(carId);
		return listQuery(sql.toString(), args.toArray(), CarUpfile.class);
	}

	

}
