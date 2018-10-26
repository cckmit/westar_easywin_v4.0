package com.westar.core.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.westar.base.enums.ModSpStateEnum;
import com.westar.base.model.MeetSummary;
import com.westar.base.model.Meeting;
import com.westar.base.model.SummaryFile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.ModFormStepData;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.MeetSummaryDao;

@Service
public class MeetSummaryService {

	@Autowired
	MeetSummaryDao meetSummaryDao;
	
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	ModFlowService modFlowService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	
	/**
	 * 添加会议纪要
	 * @param meetSummary
	 * @param userInfo
	 * @throws Exception 
	 */
	public void addSummary(MeetSummary meetSummary, UserInfo userInfo) throws Exception {
		
		
		String modFlowConfStr = meetSummary.getModFlowConfStr();
		//设置团队主键
		meetSummary.setComId(userInfo.getComId());
		if(StringUtils.isEmpty(modFlowConfStr)){
			//不审批
			meetSummary.setSpState(ModSpStateEnum.NONE.getValue());
		}else{
			//审批中
			meetSummary.setSpState(ModSpStateEnum.DOING.getValue());
		}
		//查询是否已有数据
		MeetSummary meetSummaryT = meetSummaryDao.queryMeetSummary(userInfo,meetSummary.getMeetingId());
		Integer meetSummaryId = 0;
		if(null!=meetSummaryT){
			meetSummaryId = meetSummaryT.getId();
			
			meetSummaryDao.delByField("spflowhistep", new String[]{"comId","busId","busType"}, 
					new Object[]{userInfo.getComId(),meetSummaryId,ConstantInterface.TYPE_MEET_SUMMARY});
			
			meetSummary.setId(meetSummaryId);
			meetSummaryDao.update(meetSummary);
		}else{
			meetSummaryId = meetSummaryDao.add(meetSummary);
			meetSummary.setId(meetSummaryId);
		}
		
		//删除会议纪要附件信息
		meetSummaryDao.delByField("summaryFile", new String[]{"comId","meetingId"}, 
				new Object[]{userInfo.getComId(),meetSummary.getId()});
		
		//会议纪要
		List<SummaryFile> listSummaryFile = meetSummary.getListSummaryFile();
		if(null!=listSummaryFile && !listSummaryFile.isEmpty()){
			Integer meetingId = meetSummary.getMeetingId();
			for (SummaryFile summaryFile : listSummaryFile) {
				//设置企业号
				summaryFile.setComId(userInfo.getComId());
				//设置会议主键
				summaryFile.setMeetingId(meetingId);
				//设置会议纪要主键
				summaryFile.setMeetSummaryId(meetSummaryId);
				//会议纪要上传人
				summaryFile.setUserId(userInfo.getId());
				//添加会议
				meetSummaryDao.add(summaryFile);
				uploadService.updateUpfileIndex(summaryFile.getUpfileId(), userInfo, "add",meetSummary.getId(),ConstantInterface.TYPE_MEETING);
			}
		}
		
		//添加会议日志
		meetingService.addMeetLog(userInfo.getComId(), meetSummary.getMeetingId(), userInfo.getId(), userInfo.getUserName(), "发布会议纪要");
		
		//添加会议纪要审批信息
		this.addMeetSummarySpFlow(meetSummary, userInfo);
		//审批状态
		Integer spState = meetSummary.getSpState();
		//普通会议纪要需要告知的人员
		if(spState == 0){
			//会议需要告知的人员
			List<UserInfo> shares = meetingService.listMeetingViews(userInfo.getComId(), meetSummary.getMeetingId());
			//发送普通消息
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_MEETING, 
					meetSummary.getMeetingId(), "发布会议纪要", shares, userInfo.getId(), 0);
		}
		
		//删除定时器
		meetSummaryDao.delByField("clock", new String[]{"comId","busId","busType"},
				new Object[]{userInfo.getComId(), meetSummary.getMeetingId(),ConstantInterface.TYPE_MEETING});
		
	}
	
	/**
	 * 添加会议的时候发起流程
	 * @param meeting 会议信息
	 * @param userInfo 当前操作人员
	 * @throws Exception
	 */
	private void addMeetSummarySpFlow(MeetSummary meetSummary, UserInfo userInfo) throws Exception {
		//会议信息
		Integer meetingId = meetSummary.getMeetingId();
		Meeting meeting = (Meeting) meetSummaryDao.objectQuery(Meeting.class, meetingId);
		
		Integer meetSummaryId = meetSummary.getId();
		//初始化流程信息
		Map<String,Object> paramMap = modFlowService.constrModFlowConf(meetSummary.getModFlowConfStr());
		Map<String,Object> result = null;
		if(paramMap.get("spType").equals("1")){//审批类的模块
			//审批状态，默认审核中
			meetSummary.setSpState(1);
			if(paramMap.get("flowType").equals("1")){//固定流程
				//流程主键
				Integer flowId = (Integer) paramMap.get("flowId");
				meetSummary.setFlowId(flowId);
				//流程部署信息
				String processInstanceId = modFlowService.initModFlowStartConf(userInfo,paramMap,meetSummaryId,ConstantInterface.TYPE_MEET_SUMMARY);
				meetSummary.setActInstaceId(processInstanceId);
				paramMap.put("actInstaceId", processInstanceId);
				//设置流程信息
				result = modFlowService.updateModFlowNextStep(userInfo,paramMap,meetSummaryId,ConstantInterface.TYPE_MEET_SUMMARY);
				if(result.get("status").toString().equals("f")){
					throw new Exception(result.get("info").toString());
				}else{
					//审批结果
					String spState = result.get("spState").toString();
					if(spState.equals("refuse")){//流程终止
						meetSummary.setSpState(3);
					}else if(spState.equals("finish")){//流程正常办结
						meetSummary.setSpState(2);
					}else if(spState.equals("next")){//流程正常办下一步骤
						Set<Integer> exectors = (Set<Integer>) result.get("exectors");
						modFlowService.addModFlowTodo(meeting.getTitle(), userInfo, meetSummaryId,ConstantInterface.TYPE_MEET_SUMMARY, paramMap, exectors);
					}else if(spState.equals("huiqian")){//流程是会签
						todayWorksService.delTodoWork(meetSummaryId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEET_SUMMARY, "1");
					}
				}
				meetSummaryDao.update(meetSummary);
			}else{
				List<UserInfo> stepUsers = (List<UserInfo>) paramMap.get("stepUsers");
				ModFormStepData modFormStepData = new ModFormStepData();
				Map<String,Object> freeMap = modFlowService.initFreeFlowStartConf(userInfo, stepUsers, meetSummaryId, ConstantInterface.TYPE_MEET_SUMMARY);
				modFormStepData.setNextStepUsers((List<UserInfo>) freeMap.get("nextStepUsers"));
				modFormStepData.setNextStepId(Integer.parseInt(freeMap.get("nextStepId").toString()));
				modFormStepData.setSpState(1);
				new Gson();
				paramMap.put("modFormStepData", modFormStepData);
				String processInstanceId = (String) freeMap.get("actInstaceId");
				meetSummary.setActInstaceId(processInstanceId);

				paramMap.put("actInstaceId", processInstanceId);
				//设置流程信息
				result = modFlowService.updateModFlowNextStep(userInfo,paramMap,meetSummaryId,ConstantInterface.TYPE_MEET_SUMMARY);
				if(result.get("status").toString().equals("f")){
					throw new Exception(result.get("info").toString());
				}else{
					//审批结果
					String spState = result.get("spState").toString();
					if(spState.equals("refuse")){//流程终止
						meetSummary.setSpState(3);
					}else if(spState.equals("finish")){//流程正常办结
						meetSummary.setSpState(2);
					}else if(spState.equals("next")){//流程正常办下一步骤
						Set<Integer> exectors = (Set<Integer>) result.get("exectors");
						modFlowService.addModFlowTodo(meeting.getTitle(), userInfo, meetSummaryId,ConstantInterface.TYPE_MEET_SUMMARY, paramMap, exectors);
					}else if(spState.equals("huiqian")){//流程是会签
						todayWorksService.delTodoWork(meetSummaryId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEET_SUMMARY, "1");
					}
				}
				meetSummaryDao.update(meetSummary);
			}
		}else{
			//审批状态，默认不审核
			meetSummary.setSpState(0);
			meetSummaryDao.update(meetSummary);
		}
	}
	
	/**
	 * 修改并审批会议纪要信息
	 * @param meetSummary 会议纪要信息
	 * @param redirectPage 跳转页面
	 * @return
	 * @throws Exception 
	 */
	public void updateSummary(MeetSummary meetSummary, UserInfo userInfo) throws Exception{
		//会议主键
		Integer meetSummaryId = meetSummary.getId();
		String actInstaceId = meetSummary.getActInstaceId();
		//审批状态
		Integer spState =0;
		
		String modFlowConfStr = meetSummary.getModFlowConfStr();
		if(!StringUtils.isEmpty(actInstaceId) && !StringUtils.isEmpty(modFlowConfStr)){
			JSONObject modFormStepDataObj = JSONObject.parseObject(modFlowConfStr);
			String modFormStepDataStr = modFormStepDataObj.getString("modFormStepData");
			//修改审批状态并发送消息通知
			Map<String, Object> result = modFlowService.updateSpModFlow(userInfo, meetSummaryId,ConstantInterface.TYPE_MEET_SUMMARY,
					actInstaceId , modFormStepDataStr);
			String spStateStr = result.get("spState").toString();
			spState = null == spState?0:Integer.parseInt(spStateStr);
		}
		//修改会议纪要信息
		meetSummaryDao.update(meetSummary);
		//删除会议纪要
		meetSummaryDao.delByField("summaryFile", new String[]{"comId","meetSummaryId"}, new Object[]{userInfo.getComId(),meetSummaryId});
		//会议纪要
		List<SummaryFile> listSummaryFile = meetSummary.getListSummaryFile();
		if(null!=listSummaryFile && !listSummaryFile.isEmpty()){
			Integer meetingId = meetSummary.getMeetingId();
			for (SummaryFile summaryFile : listSummaryFile) {
				//设置企业号
				summaryFile.setComId(userInfo.getComId());
				//设置会议主键
				summaryFile.setMeetingId(meetingId);
				//设置会议纪要主键
				summaryFile.setMeetSummaryId(meetSummaryId);
				//会议纪要上传人
				summaryFile.setUserId(userInfo.getId());
				//添加会议
				meetSummaryDao.add(summaryFile);
				uploadService.updateUpfileIndex(summaryFile.getUpfileId(), userInfo, "add",meetSummary.getId(),ConstantInterface.TYPE_MEETING);
			}
		}
	}
	
	/**
	 * 会议纪要，附件形式
	 * @param meetingId
	 * @param comId
	 * @return
	 */
	public List<SummaryFile> listSummaryFile(Integer meetingId, Integer comId) {
		List<SummaryFile> listFiles = meetSummaryDao.listSummaryFile(meetingId,comId);
		return listFiles;
	}
	/**
	 * 查询会议纪要信息
	 * @param userInfo 当前操作人员
	 * @param meetingId 会议主键
	 * @return
	 */
	public MeetSummary queryMeetSummary(UserInfo userInfo, Integer meetingId) {
		MeetSummary meetSummary = meetSummaryDao.queryMeetSummary(userInfo,meetingId);
		if(null==meetSummary){
			meetSummary = new MeetSummary();
			meetSummary.setSpState(0);
		}
		//会议纪要信息
		List<SummaryFile> summaryFiles = this.listSummaryFile(meetingId, userInfo.getComId());
		meetSummary.setListSummaryFile(summaryFiles);
		return meetSummary;
	}
	/**
	 * 查询会议纪要主要信息
	 * @param userInfo
	 * @param summaryId
	 * @return
	 */
	public MeetSummary querySummaryForMeet(UserInfo userInfo, Integer summaryId) {
		MeetSummary meetSummary = meetSummaryDao.querySummaryForMeet(userInfo,summaryId);
		return meetSummary;
	}
	
}
