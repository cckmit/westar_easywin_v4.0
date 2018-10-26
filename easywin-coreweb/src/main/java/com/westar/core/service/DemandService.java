package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.enums.DemandStageEnum;
import com.westar.base.enums.DemandStateEnum;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.DemandFile;
import com.westar.base.model.DemandHandleHis;
import com.westar.base.model.DemandLog;
import com.westar.base.model.DemandProcess;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.BaseUpfile;
import com.westar.base.pojo.CommonLog;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.SerialNumberUtil;
import com.westar.core.dao.DemandDao;

/**
 * 
 * 描述: 需求管理的业务逻辑类
 * @author zzq
 * @date 2018年10月9日 下午5:58:20
 */
@Service
public class DemandService {

	@Autowired
	DemandDao demandDao;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ForceInPersionService forceInService;

	/**
	 * 分页查询自己的需求管理
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求管理查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedMineDemand(UserInfo sessionUser,
			DemandProcess demandProcess) {
		return demandDao.listPagedMineDemand(sessionUser,demandProcess);
	}
	/**
	 * 分页查询需要签收的需求
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求管理查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForAccept(UserInfo sessionUser,
			DemandProcess demandProcess) {
		return demandDao.listPagedDemandForAccept(sessionUser,demandProcess);
	}
	/**
	 * 分页查询需要签收的需求
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求管理查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForHandle(UserInfo sessionUser,
			DemandProcess demandProcess) {
		return demandDao.listPagedDemandForHandle(sessionUser,demandProcess);
	}
	/**
	 * 分页查询需要签收的需求
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求管理查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForConfirm(UserInfo sessionUser,
			DemandProcess demandProcess) {
		return demandDao.listPagedDemandForConfirm(sessionUser,demandProcess);
	}
	/**
	 * 分页查询需要签收的需求
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求管理查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForAll(UserInfo sessionUser,
			DemandProcess demandProcess) {
		//验证当前登录人是否是督察人员
	    boolean isForceInPersion = forceInService.isForceInPersion(sessionUser, ConstantInterface.TYPE_DEMAND_PROCESS);
		return demandDao.listPagedDemandForAll(sessionUser,demandProcess,isForceInPersion);
	}

	/**
	 * 添加需求管理
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求管理
	 */
	public void addDemand(UserInfo sessionUser, DemandProcess demandProcess) {
		
		//序列号
		String serialNum = this.getSerialNumber();
		demandProcess.setSerialNum(serialNum);
		//设置团队号
		demandProcess.setComId(sessionUser.getComId());
		//设置创建人
		demandProcess.setCreator(sessionUser.getId());
		//处理状态
		demandProcess.setState(DemandStateEnum.DEFAULT.getValue());
		//添加需求管理
		Integer demandId = demandDao.add(demandProcess);
		demandProcess.setId(demandId);
		
		//需求管理附件说明
		List<DemandFile> listDemandFile = demandProcess.getListDemandFile();
		if(null != listDemandFile && !listDemandFile.isEmpty()){
			List<Integer> listUpfileId = new ArrayList<Integer>();
			for (DemandFile demandFile : listDemandFile) {
				//需求附件说明属性赋值
				demandFile.setComId(sessionUser.getComId());
				demandFile.setCreator(sessionUser.getId());
				demandFile.setDemandId(demandId);
				
				//添加需求管理附件说明
				demandDao.add(demandFile);
				
				//用户附件启用缓存
				Integer fileId = demandFile.getUpfileId();
				listUpfileId.add(fileId);
			}
			//归档到文档中心
			fileCenterService.addModFile(sessionUser,listUpfileId,"");
		}
		//添加需求日志
		this.addDemandLog(sessionUser,demandId,"发布需求："+serialNum);
		
		//需求处理记录
		this.addDemandHandleHis(sessionUser,demandProcess,"1");
		
		//发布待办信息
		todayWorksService.addTodayWorks(sessionUser.getComId(), ConstantInterface.TYPE_DEMAND_PROCESS,
				demandId, "发布需求："+serialNum, demandProcess.getHandleUser(), sessionUser.getId(), ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_1);
	}
	/**
	 * 取得序列号
	 * @return
	 */
	private synchronized String getSerialNumber() {
		//序列号
		String serialNum = SerialNumberUtil.getSerialNumber();
		//是否存在序列号
		Integer nums = demandDao.checkSerialNumber(serialNum);
		
		//序列号已存在
		while(nums >= 1){
			//重新获取序列号
			serialNum = SerialNumberUtil.getSerialNumber();
			//再次查询序列号是否存在
			nums = demandDao.checkSerialNumber(serialNum);
		}
		
		return serialNum;
	}

	/**
	 * 查询需求信息用于需求发布人员查看
	 * @param sessionUser 当前操作人员
	 * @param demandId 需求主键
	 * @return
	 */
	public DemandProcess queryDemandForView(UserInfo sessionUser,
			Integer demandId) {
		DemandProcess demandProcess = demandDao.queryDemandForCreator(sessionUser,demandId);
		if(null != demandProcess){
			
			String state = demandProcess.getState();
			String stateName = DemandStateEnum.getEnum(state).getDesc();
			demandProcess.setStateName(stateName);
			
			List<DemandHandleHis> listDemandHandleHis = demandDao.listDemandHandleHis(sessionUser,demandId);
			//最有一条数据
			DemandHandleHis lastDemandHandleHis = listDemandHandleHis.get(listDemandHandleHis.size()-1);
			if(DemandStateEnum.REJECT.getValue().equals(state)){
				//拒绝受理
				boolean blnNext = true;
				for(Integer subStep = 3;subStep <= 5;subStep++ ){
					
					DemandStageEnum demandStageEnum = DemandStageEnum.getEnum(subStep.toString());
					DemandHandleHis demandHandleHis = new DemandHandleHis();
					if(blnNext){
						demandHandleHis.setCurStep("-1");
						blnNext = false;
					}else{
						demandHandleHis.setCurStep("0");
					}
					demandHandleHis.setStageName(demandStageEnum.getDesc());
					
					listDemandHandleHis.add(demandHandleHis);
				}
			}else{
				Integer curStep = Integer.parseInt(lastDemandHandleHis.getStep());
				if(!curStep.equals(5)){
					boolean blnNext = true;
					for(Integer subStep = curStep + 1;subStep <= 5;subStep++ ){
						
						DemandStageEnum demandStageEnum = DemandStageEnum.getEnum(subStep.toString());
						DemandHandleHis demandHandleHis = new DemandHandleHis();
						if(blnNext){
							demandHandleHis.setCurStep("-1");
							blnNext = false;
						}else{
							demandHandleHis.setCurStep("0");
						}
						demandHandleHis.setStageName(demandStageEnum.getDesc());
						
						listDemandHandleHis.add(demandHandleHis);
					}
				}
			}
			
			demandProcess.setListDemandHandleHis(listDemandHandleHis);
		}
		return demandProcess;
	}
	
	/**
	 * 分页查询需求的附件信息
	 * @param userInfo
	 * @param demandId
	 * @return
	 */
	public PageBean<BaseUpfile> listPagedDemandUpfile(UserInfo userInfo,
			Integer demandId) {
		return demandDao.listPagedDemandUpfile(userInfo,demandId);
	}
	/**
	 * 分页查询需求的日志信息
	 * @param userInfo 当前操作人员
	 * @param demandId 需求主键 
	 * @return
	 */
	public PageBean<CommonLog> listPagedDemandLog(UserInfo userInfo,
			Integer demandId) {
		
		return demandDao.listPagedDemandLog(userInfo,demandId);
	}
	
	/**
	 * 分页查询需求用户选择
	 * @param userInfo
	 * @param demandProcess
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForSelect(UserInfo userInfo,
			DemandProcess demandProcess) {
		//创建人查询
		Integer cretorId = demandProcess.getCreator();
		if(null != cretorId && cretorId >0 ){
			List<UserInfo> listCreator = demandProcess.getListCreator();
			if(null == listCreator){
				listCreator = new ArrayList<UserInfo>();
			}
			UserInfo creator = new UserInfo();
			creator.setId(cretorId);
			listCreator.add(creator);
			
			demandProcess.setListCreator(listCreator);
		}
		
		return demandDao.listPagedDemandForSelect(userInfo,demandProcess);
	}
	/**
	 * 需求受理与拒绝
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求信息
	 * @return 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public HttpResult<String> doHandleDemand(UserInfo sessionUser, DemandProcess demandProcess) {
		String state = demandProcess.getState();
		//拒绝受理
		if(DemandStateEnum.REJECT.getValue().equals(state)){
			this.rejectDemand(sessionUser,demandProcess);
		}else if(DemandStateEnum.ANALYSIS.getValue().equals(state)){
			//开始分析需求
			this.acceptDemand(sessionUser,demandProcess);
		}else if(DemandStateEnum.HANDLING.getValue().equals(state)){
			//开始处理
			this.handleDemand(sessionUser,demandProcess);
		}else if(DemandStateEnum.CONFIRM.getValue().equals(state)){
			//用于结果确认
			this.updateConfirmDemandHandle(sessionUser, demandProcess);
		}else if(DemandStateEnum.FINISH.getValue().equals(state)){
			//需求结束
			this.updateFinishDemandHandle(sessionUser, demandProcess);
		}
		return new HttpResult<String>().ok("操作成功!");
	}
	/**
	 * 开始受理需求处理
	 * @param sessionUser
	 * @param demandProcess
	 */
	private void acceptDemand(UserInfo sessionUser, DemandProcess demandProcess) {
		demandDao.update(demandProcess);
		
		//取得序列号
		String serialNum = demandProcess.getSerialNum();
		if(StringUtils.isEmpty(serialNum)){
			DemandProcess demandProcessObj = (DemandProcess) demandDao.objectQuery(DemandProcess.class, demandProcess.getId());
			serialNum = demandProcessObj.getSerialNum();
		}
		
		//添加需求日志
		this.addDemandLog(sessionUser,demandProcess.getId()
				,sessionUser.getUserName()+"受理需求:"+serialNum);
		
		//添加需求处理过程
		demandProcess.setContent(sessionUser.getUserName()+"受理需求:"+serialNum);
		demandProcess.setHandleUser(sessionUser.getId());
		this.addDemandHandleHis(sessionUser,demandProcess,"2");
		
		//发布通知信息
		todayWorksService.addTodayWorks(sessionUser.getComId(), ConstantInterface.TYPE_DEMAND_PROCESS,
				demandProcess.getId(), sessionUser.getUserName()+"受理需求:"+serialNum,
				demandProcess.getHandleUser(), sessionUser.getId(), ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_0);
			
	}
	/**
	 * 开始处理需求处理
	 * @param sessionUser
	 * @param demandProcess
	 */
	private void handleDemand(UserInfo sessionUser, DemandProcess demandProcess) {
		DemandProcess demandProcessObj = demandDao.queryDemandForCreator(sessionUser, demandProcess.getId());
		demandProcess.setHandleUser(demandProcessObj.getItemOwnerId());
		demandDao.update(demandProcess);
		
		//取得序列号
		String serialNum = demandProcess.getSerialNum();
		if(StringUtils.isEmpty(serialNum)){
			serialNum = demandProcessObj.getSerialNum();
		}
		
		//添加需求日志
		this.addDemandLog(sessionUser,demandProcess.getId()
				,sessionUser.getUserName()+"下发需求:"+serialNum);
		
		//添加需求处理过程
		demandProcess.setContent(sessionUser.getUserName()+"下发需求:"+serialNum);
		demandProcess.setHandleUser(sessionUser.getId());
		this.addDemandHandleHis(sessionUser,demandProcess,"3");
		
		//刪除自己的待办
		todayWorksService.delTodayWorksByOwner(sessionUser.getComId()
				, sessionUser.getId(), demandProcess.getId()
				, ConstantInterface.TYPE_DEMAND_PROCESS);
		
		//发布通知信息
		todayWorksService.addTodayWorks(sessionUser.getComId(), ConstantInterface.TYPE_DEMAND_PROCESS,
				demandProcess.getId(), sessionUser.getUserName()+"下发需求:"+serialNum,
				demandProcessObj.getItemOwnerId(), sessionUser.getId(), ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_1);
		
	}
	/**
	 * 拒绝需求处理
	 * @param sessionUser
	 * @param demandProcess
	 */
	private void rejectDemand(UserInfo sessionUser, DemandProcess demandProcess) {
		
		DemandProcess demandProcessObj = (DemandProcess) demandDao.objectQuery(DemandProcess.class, demandProcess.getId());
		demandProcess.setHandleUser(demandProcessObj.getCreator());
		
		demandDao.update(demandProcess);
		//添加需求日志
		this.addDemandLog(sessionUser,demandProcess.getId()
				,"拒绝受理需求，拒绝原因：" + demandProcess.getRejectReason());
		
		//需求处理记录
		demandProcess.setContent(demandProcess.getRejectReason());
		this.updateFinishDemandHandle(sessionUser,demandProcess);
		
		//添加需求处理过程
		demandProcess.setContent(sessionUser.getUserName()+"拒绝需求:"+demandProcessObj.getSerialNum());
		demandProcess.setHandleUser(sessionUser.getId());
		this.addDemandHandleHis(sessionUser,demandProcess,"-1");
		
		//刪除自己的待办
		todayWorksService.delTodayWorksByOwner(sessionUser.getComId()
				, sessionUser.getId(), demandProcess.getId()
				, ConstantInterface.TYPE_DEMAND_PROCESS);
		
		//发布通知信息
		todayWorksService.addTodayWorks(sessionUser.getComId(), ConstantInterface.TYPE_DEMAND_PROCESS,
				demandProcess.getId(), sessionUser.getUserName()+"拒绝受理需求，拒绝原因："+demandProcess.getRejectReason(),
				demandProcess.getHandleUser(), sessionUser.getId(), ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_0);
			
	}
	/**
	 * 添加需求日志
	 * @param sessionUser 当前操作人员
	 * @param demandId 需求主键
	 * @param content 日志内容 
	 */
	public void addDemandLog(UserInfo sessionUser, Integer demandId,
			String content) {
		//需求日志属性赋值
		DemandLog demandLog = new DemandLog();
		demandLog.setComId(sessionUser.getComId());
		demandLog.setUserId(sessionUser.getId());
		demandLog.setUserName(sessionUser.getUserName());
		demandLog.setDemandId(demandId);
		demandLog.setContent(content);
		//添加需求日志
		demandDao.add(demandLog);
	}
	/**
	 * 需求处理过程
	 * @param sessionUser
	 * @param demandProcess
	 * @param step 
	 */
	private void addDemandHandleHis(UserInfo sessionUser,
			DemandProcess demandProcess, String step) {
		//修改需求处理记录
		this.updateDemandHandleState(sessionUser,demandProcess);
		
		//添加新的审批步骤
		DemandHandleHis demandHandleHis = new DemandHandleHis();
		demandHandleHis.setComId(sessionUser.getComId());
		//设置处理人员
		demandHandleHis.setUserId(demandProcess.getHandleUser());
		demandHandleHis.setDemandId(demandProcess.getId());
		demandHandleHis.setCurStep("1");
		demandHandleHis.setStep(step);
		demandHandleHis.setState(demandProcess.getState());
		
		demandDao.add(demandHandleHis);
	}
	/**
	 * 设置确认需求处理
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求条件信息
	 */
	private void updateConfirmDemandHandle(UserInfo sessionUser,
			DemandProcess demandProcess){
		DemandProcess demandProcessObj = (DemandProcess) demandDao.objectQuery(DemandProcess.class, demandProcess.getId());
		demandProcess.setHandleUser(demandProcessObj.getCreator());
		demandDao.update(demandProcess);
		//修改需求处理记录
		this.updateDemandHandleState(sessionUser,demandProcess);
		
		//添加需求日志
		this.addDemandLog(sessionUser,demandProcess.getId()
				,"已完成需求："+demandProcessObj.getSerialNum()+"的处理，提交结果确认！");
		
		//刪除自己的待办
		todayWorksService.delTodayWorksByOwner(sessionUser.getComId()
				, sessionUser.getId(), demandProcess.getId()
				, ConstantInterface.TYPE_DEMAND_PROCESS);
		//发布通知信息
		todayWorksService.addTodayWorks(sessionUser.getComId(), ConstantInterface.TYPE_DEMAND_PROCESS,
				demandProcess.getId(), sessionUser.getUserName()+"已完成需求："+demandProcessObj.getSerialNum()+"的处理吗，请及时确认！",
				demandProcess.getHandleUser(), sessionUser.getId(), ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_1);
				
		//添加新的审批步骤
		DemandHandleHis demandHandleHis = new DemandHandleHis();
		demandHandleHis.setComId(sessionUser.getComId());
		//设置处理人员
		demandHandleHis.setUserId(demandProcess.getHandleUser());
		demandHandleHis.setDemandId(demandProcess.getId());
		demandHandleHis.setCurStep("1");
		demandHandleHis.setStep("4");
		demandHandleHis.setState(demandProcess.getState());
		
		demandDao.add(demandHandleHis);
	}
	/**
	 * 设置办结
	 * @param sessionUser
	 * @param demandProcess
	 */
	private void updateFinishDemandHandle(UserInfo sessionUser,
			DemandProcess demandProcess){
		DemandProcess demandProcessObj = (DemandProcess) demandDao.objectQuery(DemandProcess.class, demandProcess.getId());
		String state = demandProcess.getState();
		if(StringUtils.isNotEmpty(state) && DemandStateEnum.REJECT.getValue().equals(state)){
			demandProcess.setState(DemandStateEnum.REJECT.getValue());
		}else{
			demandProcess.setState(DemandStateEnum.FINISH.getValue());
		}
		demandDao.update(demandProcess);
		
		this.updateDemandHandleState(sessionUser,demandProcess);

		//添加需求日志
		this.addDemandLog(sessionUser,demandProcess.getId()
				,"需求："+demandProcessObj.getSerialNum()+"的处理结果已确认，结束该需求！");
		//刪除自己的待办
		todayWorksService.delTodayWorksByOwner(sessionUser.getComId()
				, sessionUser.getId(), demandProcess.getId()
				, ConstantInterface.TYPE_DEMAND_PROCESS);
		
	}
	/**
	 * 需求处理记录
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求信息
	 */
	private void updateDemandHandleState(UserInfo sessionUser,
			DemandProcess demandProcess) {
		DemandHandleHis curDemandHandleHis = demandDao.queryCruDemandHandleHis(sessionUser.getComId(),demandProcess.getId());
		if(null != curDemandHandleHis){
			//设置结束时间
			String finishTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
			curDemandHandleHis.setFinishTime(finishTime);
			curDemandHandleHis.setUserId(sessionUser.getId());
			curDemandHandleHis.setCurStep("0");
			curDemandHandleHis.setState(demandProcess.getState());
			curDemandHandleHis.setContent(demandProcess.getContent());
			demandDao.update(curDemandHandleHis);
			
		}
		
	}
	/**
	 * 需求催办
	 * @param userInfo 当前操作人员
	 * @param demandId 需求主键
	 * @return
	 */
	public Map<String, Object> queryRemindConf(UserInfo userInfo, Integer demandId) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		DemandProcess demandProcess = (DemandProcess) demandDao.objectQuery(DemandProcess.class, demandId);
		map.put("busModName", "需求"+demandProcess.getSerialNum());
		String state = demandProcess.getState();
		if(state.equals(DemandStateEnum.FINISH.getValue())
				|| state.equals(DemandStateEnum.REJECT.getValue())){
			map.put("status", "f");
			map.put("info","需求已经处理结束！");
		}else{
			map.put("status", "y");
			String defMsg = "请尽快办理“需求"+demandProcess.getSerialNum()+"”的相关事宜！";
			map.put("defMsg", defMsg);

			//事项的执行人员信息
			List<BusRemindUser> listReminderUser = this.listDemandRemindExecutor(userInfo,demandId);
			map.put("listRemindUser", listReminderUser);
		}
		return map;
	}
	/**
	 * 查询用于提醒的人员
	 * @param userInfo 当前操作人员
	 * @param demandId 需求主键
	 * @return
	 */
	private List<BusRemindUser> listDemandRemindExecutor(UserInfo userInfo,
			Integer demandId) {
		return demandDao.listDemandRemindExecutor(userInfo,demandId);
	}
	/**
	 * 通过id查询需求关联需求的数量
	 * @author hcj 
	 * @date: 2018年10月16日 下午1:09:43
	 * @param ids
	 * @return
	 */
	public Integer queryDemandCountByItemIds(Integer[] ids) {
		return demandDao.queryDemandCountByItemIds(ids);
	}
	
	
}
