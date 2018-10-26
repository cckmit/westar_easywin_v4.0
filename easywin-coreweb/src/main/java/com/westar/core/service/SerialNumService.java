package com.westar.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.SerialNum;
import com.westar.base.model.SpSerialNumRel;
import com.westar.base.model.UserInfo;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.SerialNumDao;

@Service
public class SerialNumService {

	@Autowired
	SerialNumDao serialNumDao;

	/**
	 * 分页查询序列编号
	 * @param sessionUser 当前操作人员
	 * @param serialNum 审批序列查询条件
	 * @return
	 */
	public List<SerialNum> listPagedSerialNum(UserInfo sessionUser,
			SerialNum serialNum) {
		return serialNumDao.listPagedSerialNum(sessionUser,serialNum);
	}
	/**
	 * 分页查询序列编号
	 * @param sessionUser 当前操作人员
	 * @param serialNum 审批序列查询条件
	 * @return
	 */
	public List<SerialNum> listPagedSerialNumSelect(UserInfo sessionUser,
			SerialNum serialNum) {
		return serialNumDao.listPagedSerialNumSelect(sessionUser,serialNum);
	}
	
	/**
	 * 获取编号规则信息
	 * @param serialNumKey
	 * @return
	 */
	public SerialNum querySerialNum(Integer serialNumKey) {
		return (SerialNum) serialNumDao.objectQuery(SerialNum.class, serialNumKey);
	}
	
	/**
	 * 更新编号规则
	 * @param sessionUser
	 * @param serialNum
	 * @return
	 */
	public void updateSerialNum(UserInfo sessionUser, SerialNum serialNum) {
		serialNumDao.update(serialNum);
	}

	/**
	 * 删除自动编号规则
	 * @param sessionUser
	 * @param serialNumKey
	 */
	public void delSerialNum(UserInfo sessionUser, Integer serialNumKey) {
		serialNumDao.delByField("serialNum", new String[]{"comId","id"}, new Object[]{sessionUser.getComId(),serialNumKey});
		
	}

	/**
	 * 添加序列编号
	 * @param sessionUser
	 * @param serialNum
	 */
	public Integer addSerialNum(UserInfo sessionUser, SerialNum serialNum) {
		serialNum.setComId(sessionUser.getComId());
		Integer year = DateTimeUtil.getYear();
		serialNum.setYear(year);
		Integer serialNumId = serialNumDao.add(serialNum);
		return serialNumId;
	}
	/**
	 * 构建序列编号
	 * @param sessionUser 当前操作人员
	 * @param serialNumId 序列编号主键
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public Map<String, Object> constrSerialNum(UserInfo sessionUser, Integer serialNumId,
			Integer busId, String busType) {
		Map<String, Object> result = new HashMap<String, Object>();
		//没有设置过关系
		SerialNum serialNumObj = (SerialNum) serialNumDao.objectQuery(SerialNum.class, serialNumId);
		//查询是否有关系
		SpSerialNumRel spSerialNumRel = serialNumDao.querySpSerialNumRel(sessionUser,busId,busType);
		if(null!=spSerialNumRel){
			result.put("serialNum", spSerialNumRel.getSerialNum());
			result.put("serialNumObj", serialNumObj);
			return result;
		}
		
		if(null==serialNumObj){
			return null;
		}
		result.put("serialNumObj", serialNumObj);
		
		//编码规则
		String serialType = serialNumObj.getSerialType();
		//序列格式化
		String serialFormat = serialNumObj.getSerialFormat();
		serialFormat = serialFormat.replaceAll("yyyy", "%s");
		serialFormat = serialFormat.replaceAll("MM", "%s");
		serialFormat = serialFormat.replaceAll("XX", "%s");
		//年份
		String levelOne = serialType.split("-")[0];
		Integer levelOneNum = Integer.parseInt(levelOne);
		//月份
		String levelTwo = serialType.split("-")[1];
		Integer levelTwoNum = Integer.parseInt(levelTwo);
		//序列号
		String levelThree = serialType.split("-")[2];
		Integer levelThreeNum = Integer.parseInt(levelThree);
		
		//编码起始值
		String startNum = serialNumObj.getStartNum();
		startNum= startNum.replaceAll("\\D+", "");
		//除开年份的
		startNum = startNum.substring(levelOneNum,startNum.length());
		//月份信息
		String monthStr = startNum.substring(0,levelTwoNum);
		//除开月份
		startNum = startNum.substring(levelTwoNum,startNum.length());
		
		Integer nowYear = DateTimeUtil.getYear();
		//当前月份
		Integer nowMonth = DateTimeUtil.getMonth()+1;
		Integer serialLevelOne =  serialNumObj.getYear();
		String serialNum  = "";
		if(nowYear.equals(serialLevelOne)){
			if(nowMonth.equals(Integer.parseInt(monthStr))){
				String serialLevelTwoStr = monthStr;
				
				Long serialLevelThree = Long.parseLong(startNum)+1;
				String serialLevelThreeStr = StringUtil.addZero(serialLevelThree.toString(), levelThreeNum);
				serialNum = String.format(serialFormat, new Object[]{serialLevelOne,serialLevelTwoStr,serialLevelThreeStr});
				
				Integer countserialNum = this.querySpSerialNumRelForCheck(serialNumObj.getId(), serialNum, busId, busType);
				while(countserialNum>0){
					serialLevelThree = serialLevelThree+1;
					serialLevelThreeStr = StringUtil.addZero(serialLevelThree.toString(), levelThreeNum);
					serialNum = String.format(serialFormat, new Object[]{serialLevelOne,serialLevelTwoStr,serialLevelThreeStr});
					countserialNum = this.querySpSerialNumRelForCheck(serialNumObj.getId(), serialNum, busId, busType);
				}
			}else{
				String serialLevelTwoStr = StringUtil.addZero(nowMonth, levelTwoNum);
				String serialLevelThreeStr = StringUtil.addZero("1", levelThreeNum);
				serialNum = String.format(serialFormat, new Object[]{serialLevelOne,serialLevelTwoStr,serialLevelThreeStr});
			}
		}else{
			String serialLevelTwoStr = StringUtil.addZero(nowMonth, levelTwoNum);
			String serialLevelThreeStr = StringUtil.addZero("1", levelThreeNum);
			serialNum = String.format(serialFormat, new Object[]{serialLevelOne+1,serialLevelTwoStr,serialLevelThreeStr});
		}
		result.put("serialNum", serialNum);
		
		//添加关系
		spSerialNumRel = new SpSerialNumRel();
		spSerialNumRel.setBusId(busId);
		spSerialNumRel.setBusType(busType);
		spSerialNumRel.setComId(sessionUser.getComId());
		spSerialNumRel.setSerialNum(serialNum);
		spSerialNumRel.setSerialNumId(serialNumId);
		
		serialNumDao.add(spSerialNumRel);
		return result;
	}
	/**
	 * 修改最近的规则使用信息
	 * @param userInfo
	 * @param busId
	 * @param busType
	 * @param startNum
	 */
	public void updateSerialNum(UserInfo userInfo, Integer busId,
			String busType, String startNum) {
		
		SpSerialNumRel spSerialNumRel = serialNumDao.querySpSerialNumRel(userInfo,busId,busType);
		Integer serialNumId = spSerialNumRel.getSerialNumId();
		SerialNum serialNum = new SerialNum();
		serialNum.setId(serialNumId);
		serialNum.setStartNum(startNum);
		serialNumDao.update(serialNum);
	}
	/**
	 * 查询用于验证的审批序列编号
	 * @param serialNumId
	 * @param serialNum
	 * @param busId 
	 * @param busType
	 * @return
	 */
	public Integer querySpSerialNumRelForCheck(Integer serialNumId,
			String serialNum, Integer busId, String busType) {
		return serialNumDao.querySpSerialNumRelForCheck(serialNumId,serialNum,busId,busType);
	}
}
