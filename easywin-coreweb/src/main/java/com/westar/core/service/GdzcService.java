package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.westar.base.model.GdzcMaintainRecord;
import com.westar.base.model.GdzcReduceRecord;
import com.westar.base.model.GdzcType;
import com.westar.base.model.Gdzc;
import com.westar.base.model.GdzcUpfile;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.GdzcDao;

@Service
public class GdzcService {

	@Autowired
	GdzcDao gdzcDao;
	
	@Autowired
	SystemLogService systemLogService;
	/**
	 * 固定资产列表
	 * @param gdzc
	 * @param userInfo
	 * @return
	 */
	public List<Gdzc> listGdzc(Gdzc gdzc, UserInfo userInfo) {
		return gdzcDao.listGdzc(gdzc,userInfo);
	}
	/**
	 * 固定资产类型类别
	 * @param comId
	 * @param busType
	 * @return
	 */
	public List<GdzcType> listGdzcType(Integer comId, String busType) {
		return gdzcDao.listGdzcType(comId,busType);
	}
	/**
	 * 获取固定资产类型最大序列数
	 * @param comId
	 * @param busType
	 * @return
	 */
	public Integer queryGdzcTypeOrderMax(Integer comId, String busType) {
		GdzcType gdzcType = gdzcDao.queryGdzcTypeOrderMax(comId,busType);
		return null==gdzcType.getOrderNum()?1:gdzcType.getOrderNum();
	}
	/**
	 * 添加固定资产类型
	 * @param gdzcType
	 * @param userInfo
	 * @return
	 */
	public Integer addGdzcType(GdzcType gdzcType, UserInfo userInfo) {
		gdzcType.setComId(userInfo.getComId());
		return gdzcDao.add(gdzcType);
	}
	/**
	 * 修改固定资产类型名称
	 * @param gdzcType
	 * @param userInfo
	 * @return
	 */
	public boolean updateGdzcTypeName(GdzcType gdzcType, UserInfo userInfo) {
		boolean succ = true;
		try {
			GdzcType obj = (GdzcType) gdzcDao.objectQuery(GdzcType.class, gdzcType.getId());
			if(null!=obj && !obj.getTypeName().equals(gdzcType.getTypeName())){//有变动
				//更新固定资产类型名称
				gdzcDao.update(gdzcType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), 
						"更新固定资产类型名称:\""+gdzcType.getTypeName()+"\"",
						ConstantInterface.TYPE_GDZC,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}
	/**
	 * 修改固定资产排序
	 * @param gdzcType
	 * @param userInfo
	 * @return
	 */
	public boolean updateGdzcTypeOrder(GdzcType gdzcType, UserInfo userInfo) {
		boolean succ = true;
		try {
			GdzcType obj = (GdzcType) gdzcDao.objectQuery(GdzcType.class, gdzcType.getId());
			if(null!=obj && !obj.getOrderNum().equals(gdzcType.getOrderNum())){//有变动
				//更新固定资产类型
				gdzcDao.update(gdzcType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), 
						"更新固定资产类型:\""+obj.getTypeName()+"\"的排序为\""+gdzcType.getOrderNum()+"\"",
						ConstantInterface.TYPE_GDZC,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}	
	/**
	 * 删除固定资产类型
	 * @param gdzcType
	 * @param userInfo
	 * @return
	 */
	public boolean delGdzcType(GdzcType gdzcType, UserInfo userInfo) {
		boolean succ = true;
		try {
			Integer crmNum = gdzcDao.countGdzcByGdzcType(gdzcType.getId(),userInfo);
			if(crmNum>0){
				succ = false ;
			}else{
				//获取需要删除的固定资产类型
				GdzcType obj = (GdzcType) gdzcDao.objectQuery(GdzcType.class, gdzcType.getId());
				if(null!= obj){
					//固定资产类型删除
					gdzcDao.delById(GdzcType.class, gdzcType.getId());
					//添加系统日志记录 
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), 
							"删除固定资产类型:\""+obj.getTypeName()+"\"",ConstantInterface.TYPE_GDZC,
							userInfo.getComId(),userInfo.getOptIP());
				}
			}
			
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}
	/**
	 * 添加资产管理
	 * @param gdzc
	 * @param userInfo
	 */
	public void addGdzc(Gdzc gdzc, UserInfo userInfo) {
		gdzc.setComId(userInfo.getComId());
		Integer gdzcId = gdzcDao.add(gdzc);
		
		if(null != gdzc.getListGdzcUpfiles() && !gdzc.getListGdzcUpfiles().isEmpty()){
			for(Upfiles upfile :  gdzc.getListGdzcUpfiles()){
				GdzcUpfile gdzcUpfile = new GdzcUpfile();
				gdzcUpfile.setBusId(gdzcId);
				gdzcUpfile.setComId(userInfo.getComId());
				gdzcUpfile.setUpfileId(upfile.getId());
				gdzcUpfile.setUserId(userInfo.getId());
				gdzcUpfile.setBusType(ConstantInterface.TYPE_GDZC);
				gdzcDao.add(gdzcUpfile);
			}
		}
	}
	/**
	 * 修改固定资产
	 * @param gdzc
	 * @param userInfo
	 */
	public void updateGdzc(Gdzc gdzc, UserInfo userInfo) {
		gdzcDao.update(gdzc);
		
		if(null != gdzc.getListGdzcUpfiles() && !gdzc.getListGdzcUpfiles().isEmpty()){
			for(Upfiles upfile :  gdzc.getListGdzcUpfiles()){
				GdzcUpfile gdzcUpfile = new GdzcUpfile();
				gdzcUpfile.setBusId(gdzc.getId());
				gdzcUpfile.setComId(userInfo.getComId());
				gdzcUpfile.setUpfileId(upfile.getId());
				gdzcUpfile.setUserId(userInfo.getId());
				gdzcUpfile.setBusType(ConstantInterface.TYPE_GDZC);
				gdzcDao.add(gdzcUpfile);
			}
		}
	}
	//删除固定资产
	public void delGdzc(Gdzc gdzc) {
		gdzcDao.update(gdzc);
	}
	/**
	 * 通过ID查询固定资产详情
	 * @param gdzcId
	 * @param userInfo
	 * @return
	 */
	public Gdzc queryGdzcById(Integer gdzcId, UserInfo userInfo) {
		Gdzc gdzc = gdzcDao.queryGdzcById(gdzcId,userInfo);
		//设置附件总数
		List<GdzcUpfile> lists = gdzcDao.listGdzcUpfile(gdzcId,userInfo.getComId());
		if(lists != null) {
			gdzc.setFileNum(lists.size());
		}
		return gdzc;
	}
	/**
	 * 固定资产附件
	 * @param gdzcId
	 * @param comId
	 * @return
	 */
	public List<GdzcUpfile> listGdzcUpfile(Integer gdzcId, Integer comId) {
		return gdzcDao.listGdzcUpfile(gdzcId,comId);
	}
	/**
	 * 删除附件
	 * @param gdzcFileId
	 * @param userInfo
	 * @param busId
	 * @param busType
	 */
	public void delGdzcUpfile(Integer gdzcFileId, UserInfo userInfo, Integer busId, String busType) {
		GdzcUpfile gdzcUpfile = (GdzcUpfile) gdzcDao.objectQuery(GdzcUpfile.class, gdzcFileId);
		gdzcDao.delById(GdzcUpfile.class, gdzcFileId);
		
	}
	
	/***************************************维修记录**********************************/
	/**
	 * 固定资产维修记录
	 * @param gdzcId
	 * @param userInfo
	 * @return
	 */
	public List<GdzcMaintainRecord> listMaintainRecord(Integer gdzcId, UserInfo userInfo) {
		return gdzcDao.listMaintainRecord( gdzcId,  userInfo);
	}
	/**
	 * 添加记录
	 * @param gdzcMaintain
	 * @param userInfo
	 */
	public void addGdzcMaintainRecord(GdzcMaintainRecord gdzcMaintain, UserInfo userInfo) {
		gdzcMaintain.setComId(userInfo.getComId());
		Integer gdzcMaintainId = gdzcDao.add(gdzcMaintain);
		
		if(null != gdzcMaintain.getListGdzcUpfiles() && !gdzcMaintain.getListGdzcUpfiles().isEmpty()){
			for(Upfiles upfile :  gdzcMaintain.getListGdzcUpfiles()){
				GdzcUpfile gdzcUpfile = new GdzcUpfile();
				gdzcUpfile.setBusId(gdzcMaintainId);
				gdzcUpfile.setComId(userInfo.getComId());
				gdzcUpfile.setUpfileId(upfile.getId());
				gdzcUpfile.setUserId(userInfo.getId());
				gdzcUpfile.setBusType(ConstantInterface.TYPE_GDZC_MAINTAIN);
				gdzcDao.add(gdzcUpfile);
			}
		}
		
		Gdzc gdzc = new Gdzc();
		gdzc.setId(gdzcMaintain.getGdzcId());
		gdzc.setState(3);
		gdzcDao.update(gdzc);
	}
	/**
	 * 固定资产维修基本信息
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public GdzcMaintainRecord queryMaintainRecordById(Integer id, UserInfo userInfo) {
		return gdzcDao.queryMaintainRecordById(id,userInfo);
	}
	/**
	 * 修改固定资产维修记录
	 * @param gdzcMaintain
	 */
	public void updateMaintainRecord(GdzcMaintainRecord gdzcMaintain,UserInfo userInfo) {
		gdzcDao.update(gdzcMaintain);
		
		if(null != gdzcMaintain.getListGdzcUpfiles() && !gdzcMaintain.getListGdzcUpfiles().isEmpty()){
			for(Upfiles upfile :  gdzcMaintain.getListGdzcUpfiles()){
				GdzcUpfile gdzcUpfile = new GdzcUpfile();
				gdzcUpfile.setBusId(gdzcMaintain.getId());
				gdzcUpfile.setComId(userInfo.getComId());
				gdzcUpfile.setUpfileId(upfile.getId());
				gdzcUpfile.setUserId(userInfo.getId());
				gdzcUpfile.setBusType(ConstantInterface.TYPE_GDZC_MAINTAIN);
				gdzcDao.add(gdzcUpfile);
			}
		}
		if(null!= gdzcMaintain.getMaintainState() && gdzcMaintain.getMaintainState() == 1){
			Gdzc gdzc = new Gdzc();
			gdzc.setId(gdzcMaintain.getGdzcId());
			gdzc.setState(1);
			gdzcDao.update(gdzc);
		}
	}
	/**
	 * 删除维修记录
	 * @param gdzcMaintainRecord
	 */
	public void delGdzcMaintainRecord(GdzcMaintainRecord gdzcMaintainRecord) {
		gdzcDao.delById(GdzcMaintainRecord.class, gdzcMaintainRecord.getId());
	}
	/**
	 * 维修完成
	 * @param gdzc
	 * @param userInfo
	 */
	public void gdzcMaintainFinish (Gdzc gdzc, UserInfo userInfo) {
		//修改维修状态
		gdzcDao.update(gdzc);
		String nowDate = DateTimeUtil.getNowDateStr(0);
		//将最后一次维修记录完成时间修改成当前时间
		GdzcMaintainRecord gdzcMaintainRecord  = gdzcDao.queryLastRecord(gdzc.getId());
		if(null != gdzcMaintainRecord ){
			gdzcMaintainRecord.setEndDate(nowDate);
			gdzcDao.update(gdzcMaintainRecord);
		}
	}
	/***************************************维修记录**********************************/
	/***************************************减少记录**********************************/
	/**
	 * 减少记录列表
	 * @param gdzcId
	 * @param userInfo
	 * @return
	 */
	public List<GdzcReduceRecord> listGdzcReduceRecord(Integer gdzcId, UserInfo userInfo) {
		return gdzcDao.listGdzcReduceRecord(gdzcId,userInfo);
	}
	/**
	 * 添加减少记录
	 * @param gdzcReduce
	 * @return
	 */
	public void addReduceRecord(GdzcReduceRecord gdzcReduce, UserInfo userInfo) {
		gdzcReduce.setComId(userInfo.getComId());
		gdzcReduce.setReduceUser(userInfo.getId());
		Integer gdzcReduceId = gdzcDao.add(gdzcReduce);
		//改变资产状态
		Gdzc gdzc = new Gdzc();
		gdzc.setId(gdzcReduce.getGdzcId());
		gdzc.setState(4);
		gdzcDao.update(gdzc);
		
		if(null != gdzcReduce.getListGdzcUpfiles() && !gdzcReduce.getListGdzcUpfiles().isEmpty()){
			for(Upfiles upfile :  gdzcReduce.getListGdzcUpfiles()){
				GdzcUpfile gdzcUpfile = new GdzcUpfile();
				gdzcUpfile.setBusId(gdzcReduceId);
				gdzcUpfile.setComId(userInfo.getComId());
				gdzcUpfile.setUpfileId(upfile.getId());
				gdzcUpfile.setUserId(userInfo.getId());
				gdzcUpfile.setBusType(ConstantInterface.TYPE_GDZC_REDUCE);
				gdzcDao.add(gdzcUpfile);
			}
		}
	}
	/**
	 * 通过id查询记录详情
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public GdzcReduceRecord queryReduceRecordById(Integer id, UserInfo userInfo) {
		return gdzcDao.queryReduceRecordById(id,userInfo);
	}
	/**
	 * 修改减少记录
	 * @param gdzcReduce
	 * @return
	 */
	public void updateReduceRecord(GdzcReduceRecord gdzcReduce,UserInfo userInfo) {
		gdzcDao.update(gdzcReduce);
		
		if(null != gdzcReduce.getListGdzcUpfiles() && !gdzcReduce.getListGdzcUpfiles().isEmpty()){
			for(Upfiles upfile :  gdzcReduce.getListGdzcUpfiles()){
				GdzcUpfile gdzcUpfile = new GdzcUpfile();
				gdzcUpfile.setBusId(gdzcReduce.getId());
				gdzcUpfile.setComId(userInfo.getComId());
				gdzcUpfile.setUpfileId(upfile.getId());
				gdzcUpfile.setUserId(userInfo.getId());
				gdzcUpfile.setBusType(ConstantInterface.TYPE_GDZC_REDUCE);
				gdzcDao.add(gdzcUpfile);
			}
		}
		
	}
	/**
	 * 删除记录
	 * @param gdzcReduce
	 * @return
	 */
	public void delReduceRecord(GdzcReduceRecord gdzcReduce) {
		gdzcDao.delById(GdzcReduceRecord.class, gdzcReduce.getId());
		
	}
	
}
