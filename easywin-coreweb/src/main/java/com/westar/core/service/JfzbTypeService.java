package com.westar.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Jfzb;
import com.westar.base.model.JfzbType;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.JfzbTypeDao;

@Service
public class JfzbTypeService {

	@Autowired
	JfzbTypeDao jfzbTypeDao;
	
	@Autowired
	SystemLogService systemLogService;

	/**
	 * 查询团队的积分指标
	 * @param comId 团队号
	 * @return
	 */
	public List<JfzbType> listJfzbType(Integer comId) {
		return jfzbTypeDao.listJfzbType(comId);
	}
	/**
	 * 查询团队的积分指标分类的最大排序号
	 * @param comId 团队号
	 * @return
	 */
	public Integer queryJfzbTypeOrderMax(Integer comId) {
		JfzbType jfzbType = jfzbTypeDao.initJfzbTypeOrder(comId);
		return null==jfzbType.getDicOrder()?1:jfzbType.getDicOrder();
	}
	/**
	 * 添加团队积分指分类
	 * @param jfzbType 积分指标分类信息 
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public Integer addJfzbType(JfzbType jfzbType, UserInfo sessionUser) {
		Integer id =  jfzbTypeDao.add(jfzbType);
		//添加系统日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "添加积分指标类型:\""+jfzbType.getTypeName()+"\"",
				ConstantInterface.TYPE_ORG,jfzbType.getComId(),sessionUser.getOptIP());
		return id;
	}
	/**
	 * 修改积分指标类型名称
	 * @param jfzbType
	 * @param sessionUser
	 */
	public void updateTypeName(JfzbType jfzbType, UserInfo sessionUser) {
		//更新客户类型名称
		jfzbTypeDao.update(jfzbType);
		//添加系统日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), 
				"更新积分指标类型名称\""+jfzbType.getTypeName()+"\"",ConstantInterface.TYPE_ORG,sessionUser.getComId(),sessionUser.getOptIP());
		
	}
	/**
	 * 修改积分指标类型排序
	 * @param jfzbType
	 * @param sessionUser
	 */
	public void dicOrderUpdate(JfzbType jfzbType, UserInfo sessionUser) {
		//更新指标类型类型名称
		jfzbTypeDao.update(jfzbType);
		//添加系统日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), 
				"更新客户类型\""+jfzbType.getTypeName()+"\"的排序为\""+jfzbType.getDicOrder()+"\"",
				ConstantInterface.TYPE_CRM,sessionUser.getComId(),sessionUser.getOptIP());
	
	}
	/**
	 * 删除分类信息
	 * @param jfzbType
	 * @param sessionUser
	 * @return
	 */
	@SuppressWarnings("static-access")
	public Map<String, Object> delJfbzType(JfzbType jfzbType, UserInfo sessionUser) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		//查询积分指标使用该分类的信息
		Integer jfzbNums = jfzbTypeDao.queryJfzbForCheck(jfzbType.getId());
		if(jfzbNums>0){
			map.put("status", "f");
			String info = "已有%s个积分标准使用该分类！请修改后再删除！";
			map.put("info",info.format(info, new Object[]{jfzbNums}) );
		}else{
			map.put("status", "y");
			//指标类型类型删除
			jfzbTypeDao.delById(JfzbType.class, jfzbType.getId());
			//添加系统日志记录 
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "删除积分指标类型:\""+jfzbType.getTypeName()+"\"",
					ConstantInterface.TYPE_ORG,sessionUser.getComId(),sessionUser.getOptIP());
		}
		return map;
	}
	
	
	
}
