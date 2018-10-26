package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Organic;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.UserAuth;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.Encodes;
import com.westar.core.dao.ClientApiDao;
import com.westar.core.web.AppAuthKeyManager;;

@Service
public class ClientApiService {
	@Autowired
	TodayWorksService todayWorksService;
	@Autowired
	JiFenService jifenService;
	@Autowired
	SystemLogService systemLogService;
	@Autowired
	OrganicService organicService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	ClientApiDao clientApiDao;

	/**
	 * 验证登录人是否合理以及获取参加的所有团队集合
	 * 
	 * @param account 帐号
	 * @param password 密码
	 * @return
	 */
	public HttpResult<List<Organic>> listUserOrg(String account, String password) {
		String passwordMD5 = Encodes.encodeMd5(password);
		// 查询用户所在的激活企业
		List<Organic> listOrg = organicService.listUserOrg(account.toLowerCase(), passwordMD5);
		if (null == listOrg || listOrg.size() == 0) {
			return new HttpResult<>("您的账号没有激活,或是团队号停用!");
		} else {
			return new HttpResult<>(listOrg);
		}
	}

	/**
	 * 登录用户权限验证
	 * @param account 账号
	 * @param password 密码
	 * @param comId 团队主键
	 * @return
	 */
	public HttpResult<UserAuth> userAuth(String account, String password, String comId) {
		String passwordMD5 = Encodes.encodeMd5(password);
		final UserInfo userInfo = userInfoService.userAuth(account.toLowerCase(), passwordMD5,comId);
		if (null != userInfo) {
			// 添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo
					.getUserName(), "从CS端进入平台", ConstantInterface.TYPE_LOGIN,
					userInfo.getComId(),userInfo.getOptIP());
			// 每日登录积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(),
					ConstantInterface.TYPE_LOGIN, "每日登录系统", 0);
			
			String authKey = AppAuthKeyManager.newAuthKey(userInfo);
			UserAuth userAuth = new UserAuth(userInfo, authKey);
			return new HttpResult<>(userAuth); 
		} else {
			return new HttpResult<>("登入异常，请及时联系管理员！");
		}
	}

	/**
	 * 获取个人代办工作事项集合
	 * @param todayWorks
	 * @param comId 团队主键
	 * @param userId 个人主键
	 * @return
	 */
	public List<TodayWorks> listPagedMsgTodo(TodayWorks todayWorks, Integer comId, Integer userId,List<String> modList) {
		List<TodayWorks> listPagedMsgTodo = todayWorksService.listPagedMsgTodo(todayWorks,comId,userId,modList);
		return listPagedMsgTodo;
	}

	/**
	 * 标记信息为已读 
	 * @param comId
	 * @param todayWorkId
	 */
	public void updateMessageReaded(Integer comId, Integer todayWorkId) {
		TodayWorks todayWork = new TodayWorks();
		todayWork.setComId(comId);
		todayWork.setId(todayWorkId);
		todayWork.setReadState(ConstantInterface.COMMON_YES);
		clientApiDao.update("update todayWorks a set a.readState=:readState where a.comid=:comId and a.id=:id", todayWork);
	}
	
	/**
     * 获取待办事项分类名称
     * @param job
     * @return
     */
    private String queryModuleTypeName(TodayWorks job) {
    	String moduleTypeName  = null;
    	if("1".equals(job.getBusType())) {
    		moduleTypeName = "闹铃";
    	}else if("015".equals(job.getBusType())) {
    		moduleTypeName = "加入申请";
    	}else if("017".equals(job.getBusType())) {
    		moduleTypeName = "会议确认";
    	}else if("018".equals(job.getBusType())) {
    		moduleTypeName = "会议申请";
    	}else if("040".equals(job.getBusType())) {
    		moduleTypeName = "制度";
    	}else if("046".equals(job.getBusType())) {
    		moduleTypeName = "会议审批";
    	}else if("047".equals(job.getBusType())) {
    		moduleTypeName = "会议纪要审批";
    	}else if("099".equals(job.getBusType())) {
    		moduleTypeName = "催办";
    	}else if("0103".equals(job.getBusType())) {
    		moduleTypeName = "领款通知";
    	}else if("06602".equals(job.getBusType())) {
    		moduleTypeName = "完成结算";
    	}else if("06601".equals(job.getBusType())) {
    		moduleTypeName = "财务结算";
    	}else if("067".equals(job.getBusType())) {
    		moduleTypeName = "变更";
    	}else if("02201".equals(job.getBusType())) {
    		moduleTypeName = "审批";
    	}else if("027010".equals(job.getBusType())) {
    		moduleTypeName = "用品采购审核";
    	}else if("027020".equals(job.getBusType())) {
    		moduleTypeName = "用品申领审核";
    	}else if("00306".equals(job.getBusType())) {
    		moduleTypeName = "任务报延";
    	}else if("100".equals(job.getBusType())) {
    		moduleTypeName = "分享";
    	}else {
    		moduleTypeName = job.getModuleTypeName();
    	}
    	job.setModuleTypeName(moduleTypeName);
    	return moduleTypeName;
    }
}
