package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.ForceInPersion;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.ForceInPersionDao;

/**
 * 
 * 描述:监督人员的逻辑处理类
 * @author zzq
 * @date 2018年8月27日 上午10:44:16
 */
@Service
public class ForceInPersionService {

	@Autowired
	ForceInPersionDao forceInPersionDao;
	
	@Autowired
	SystemLogService systemLogService;

	/**
	 * 取得监督人员集合
	 * @param comId
	 * @param busType
	 * @return
	 */
	public List<ForceInPersion> listForceInPerson(Integer comId, String busType) {
		return forceInPersionDao.listForceInPerson(comId,busType);
	}

	/**
	 * 修改监督人员
	 * @param sessionUser
	 * @param forceInList
	 * @param busType
	 */
	public void updateForceInPersion(UserInfo userInfo,
			List<ForceInPersion> forceInList, String busType) {
		//删除督察人员
		forceInPersionDao.delByField("forceInPersion", new String[]{"comId","type"}, new Object[]{userInfo.getComId(),busType});
		StringBuilder userNames = new StringBuilder();
		if(null!=forceInList && !forceInList.isEmpty()){
			for (ForceInPersion forceIn : forceInList) {
				userNames.append("、");
				userNames.append(forceIn.getSharerName());
				forceInPersionDao.add(forceIn);
			}
		}
		
		String logContent = "";
		if(!"".equals(userNames)){
			logContent = userNames.toString().substring(1,userNames.length());
		}
		String log = "";
		if(busType.equals(ConstantInterface.TYPE_TASK)){
			log = "任务督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_ITEM)){
			log = "项目督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_PRODUCT)){
			log = "产品督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_CRM)){
			log = "客户督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_WEEK)){
			log = "周报督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_DAILY)){
			log = "周报督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_FLOW_SP)){
			log = "审批督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_ATTENCE)){
			log = "考勤督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_FINANCIAL)){
			log = "差旅督察人员变更为:\""+logContent +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_MEETING)){
			log = "会议督察人员变更为:\""+logContent +"\"";
		}else{
			log = busType+"_督察人员变更为:\""+logContent +"\"";
		}
		
		//添加系统日志记录 
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), log,busType,userInfo.getComId(),userInfo.getOptIP());
	}

	/**
	 * 删除监督人员
	 * @param userInfo
	 * @param forceIn
	 */
	public void delForceInPersion(UserInfo userInfo, ForceInPersion forceIn) {
		String busType = forceIn.getType();
		String forceInName = forceIn.getSharerName();
		//删除任务督察人员
		forceInPersionDao.delByField("forceInPersion", new String[]{"comId","type","userId"},new Object[]{userInfo.getComId(),busType,forceIn.getUserId()});
		String log = "";
		if(busType.equals(ConstantInterface.TYPE_TASK)){
			log = "删除任务督察人员:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_ITEM)){
			log = "删除项目督察人员:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_PRODUCT)){
			log = "删除产品督察人员:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_CRM)){
			log = "删除客户督察人员:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_WEEK)){
			log = "删除周报督察人员:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_DAILY)){
			log = "删除周报督察人员:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_FLOW_SP)){
			log = "删除审批督察人员:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_ATTENCE)){
			log = "删除考勤督察人员变更为:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_FINANCIAL)){
			log = "删除差旅督察人员变更为:\""+forceInName +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_MEETING)){
			log = "删除会议督察人员变更为:\""+forceInName +"\"";
		}else{
			log = "删除督察人员变更为:\""+forceInName +"\"";
		}
		//添加系统日志记录 
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),log,busType,userInfo.getComId(),userInfo.getOptIP());
	}
	
	/**
	 * 判断是否模块的监督人员
	 * @param userInfo 当前操作人员
	 * @param busType 业务类型
	 * @return
	 */
	public boolean isForceInPersion(UserInfo userInfo,String busType){
		ForceInPersion forceInPersion= forceInPersionDao.getForceInPersion(userInfo,busType);
		//不为空则是督察人员
		return  forceInPersion != null;
	}
	
	
}
