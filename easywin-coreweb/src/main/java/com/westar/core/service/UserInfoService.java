package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.Clock;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerHandOver;
import com.westar.base.model.Department;
import com.westar.base.model.ForMeDo;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.Item;
import com.westar.base.model.ItemHandOver;
import com.westar.base.model.JoinRecord;
import com.westar.base.model.JoinTemp;
import com.westar.base.model.MyLeaders;
import com.westar.base.model.Organic;
import com.westar.base.model.PassYzm;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.UserOrganic;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.Encodes;
import com.westar.base.util.MessageSender;
import com.westar.base.util.PassWordCreateUtil;
import com.westar.base.util.PinyinToolkit;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.UserInfoDao;
import com.westar.core.thread.MailSendThread;
import com.westar.core.web.SessionContext;

@Service
public class UserInfoService {

	@Autowired
	UserInfoDao userInfoDao;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	SystemLogService systemLogService;
	@Autowired
	IndexService indexService;
	@Autowired
	JiFenService jifenService;
	@Autowired
	CrmService crmService;
	@Autowired
	ItemService itemService;
	@Autowired
	TaskService taskService;
	@Autowired
	ClockService clockService;
	@Autowired
	PhoneMsgService phoneMsgService;
	@Autowired
	RegistService registService;
	@Autowired
	OrganicService organicService;
	@Autowired
	FinancialService financialService;
	
	@Autowired
	TodayWorksService todayWorksService;

	/**
	 * 查询用户分页列表 分页查询
	 * 
	 * @param userInfo
	 * @return 人员信息 集合
	 */
	public List<UserInfo> listPagedUserInfo(UserInfo userInfo) {
		List<UserInfo> list = userInfoDao.listPagedUserInfo(userInfo);
		return list;
	}
	
	/**
	 * 查询用户分页列表不分页
	 * 
	 * @param userInfo
	 * @return 人员信息 集合
	 */
	public List<UserInfo> listUserInfo(UserInfo userInfo) {
		List<UserInfo> list = userInfoDao.listUserInfo(userInfo);
		return list;
	}

	/**
	 * 新增用户 新增数据
	 * 
	 * @param userInfo
	 * @return 返回主键ID
	 */
	public Integer addUserInfo(UserInfo userInfo) {
		// 注册添加企业主键
		Organic organic = new Organic();
		// 公司编号
		organic.setOrgNum(userInfo.getComId());
		organic.setOrgName(userInfo.getOrgName());
		// 激活邮箱后启用
		organic.setEnabled("0");
		userInfoDao.add(organic);
		// 激活邮箱后启用
		userInfo.setEnabled("0");
		// 激活邮箱后审核通过
		userInfo.setCheckState("0");
		// 管理人员
		userInfo.setAdmin("1");
		// 用户名称不为空时此时添加用户名称全拼和简拼
		if (null != userInfo.getUserName()) {
			userInfo.setAllSpelling(PinyinToolkit.cn2Spell(userInfo
					.getUserName()));
			userInfo.setFirstSpelling(PinyinToolkit.cn2FirstSpell(userInfo
					.getUserName()));
		}
		String passwordMD5 = Encodes.encodeMd5(userInfo.getPassword());
		userInfo.setPassword(passwordMD5);
		Integer userId = userInfoDao.add(userInfo);
		return userId;
	}

	/**
	 * 禁用用户 批量禁用
	 * @param ids 所有要禁用的人员的主键ID
	 * @param comId 企业主键
	 * @param sessionUser 当前操作人
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void disableUserInfo(Integer[] ids, Integer comId,UserInfo sessionUser) {
		List<UserInfo> userOrgs = new ArrayList<UserInfo>();
		for (Integer id : ids) {
			UserOrganic userOrganic = (UserOrganic) userInfoDao.objectQuery(
					UserOrganic.class, id);
			// 取得将要被禁用的成员
			if (null != userOrganic && !userOrganic.getEnabled().equals(ConstantInterface.ENABLED_NO.toString())) {
				userOrganic.setEnabled(ConstantInterface.ENABLED_NO.toString());//启用状态为禁用
				userOrganic.setInService(ConstantInterface.USER_INSERVICE_NO);//服务状态为不可用
				userInfoDao.update(userOrganic);

				// 取消定时系统中数据
				clockService.disableClock(userOrganic.getUserId(),
						userOrganic.getComId());

				UserInfo userInfo = (UserInfo) userInfoDao.objectQuery(
						UserInfo.class, userOrganic.getUserId());
				userOrgs.add(userInfo);
			}

		}
		if(null!=userOrgs && userOrgs.size()>0){
			for (UserInfo userObj : userOrgs) {
				removeSessionUser(sessionUser, userObj);
			}
			//添加系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "禁用用户",
					ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		}
	}

	/**
	 * 启用用户 批量启用
	 * 
	 * @param ids
	 *            所有要启用的人员的主键ID
	 * @param comId
	 *            企业主键
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void enableUserInfo(Integer[] ids, Integer comId,UserInfo sessionUser) {
		List<UserInfo> userlist = new ArrayList<UserInfo>();
		for (Integer id : ids) {
			UserOrganic userOrganic = (UserOrganic) userInfoDao.objectQuery(
					UserOrganic.class, id);
			// 取得将要被启用的用户
			if (null != userOrganic && !userOrganic.getEnabled().equals(ConstantInterface.ENABLED_YES.toString())) {
				userOrganic.setEnabled(ConstantInterface.ENABLED_YES.toString());//启用状态
				userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);//服务状态为可用
				userInfoDao.update(userOrganic);

				UserInfo userInfo = (UserInfo) userInfoDao.objectQuery(
						UserInfo.class, userOrganic.getUserId());

				userlist.add(userInfo);
				// 重新启动闹铃
				List<Clock> listClocks = clockService.listUserClock(
						userOrganic.getUserId(), userOrganic.getComId());
				if (null != listClocks && listClocks.size() > 0) {
					for (Clock clock : listClocks) {
						clockService.updateRestartClock(clock);
					}
				}
			}

		}
		if(null!=userlist && userlist.size()>0){
			for (UserInfo userInfo : userlist) {
				//启用的人员
				userInfo.setComId(sessionUser.getComId());
				//获取个人直属上级集合
				List<ImmediateSuper> listPreSup = this.listImmediateSuper(userInfo);
				if(null!=listPreSup && listPreSup.size()>0){//离职人员有上级
					for (ImmediateSuper immediateSuperss : listPreSup) {
						SessionContext.updateSub(immediateSuperss.getLeader(), sessionUser.getComId(),1);
					}
				}
			}
			//添加系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "启用用户",
					ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		}
	}

	/**
	 * 用户授权 批量授权
	 * 
	 * @param ids
	 *            所有要授权 的人员的主键ID
	 * @param comId
	 *            企业主键
	 */
	public List<UserOrganic> updateGrant(Integer[] ids, Integer comId) {
		List<UserOrganic> userOrgs = new ArrayList<UserOrganic>();
		for (Integer id : ids) {
			UserOrganic userOrganic = (UserOrganic) userInfoDao.objectQuery(
					UserOrganic.class, id);
			// 将不是管理人员的人设置成管理人员
			if (null != userOrganic && userOrganic.getAdmin().equals("0")) {
				userOrgs.add(userOrganic);
				// 授权
				userOrganic.setAdmin("2");
				userInfoDao.update(userOrganic);
			}
		}
		return userOrgs;
	}

	/**
	 * 用户回收权限 批量回收
	 * 
	 * @param ids
	 *            所有要授权 的人员的主键ID
	 * @param comId
	 *            企业主键
	 */
	public List<UserOrganic> updateRevoke(Integer[] ids, Integer comId) {
		List<UserOrganic> userOrgs = new ArrayList<UserOrganic>();
		for (Integer id : ids) {
			UserOrganic userOrganic = (UserOrganic) userInfoDao.objectQuery(
					UserOrganic.class, id);
			// 将管理人员设置成非管理人员
			if (null != userOrganic && !userOrganic.getAdmin().equals("0")) {
				userOrgs.add(userOrganic);
				// 回收授权
				userOrganic.setAdmin("0");
				userInfoDao.update(userOrganic);
			}

			// 查询申请中需要本次失去权限的人员处理的
			List<JoinRecord> listJoinRecord = userInfoDao.listJoinRecord(comId,
					userOrganic.getUserId());
			// 有待办事项
			if (null != listJoinRecord && listJoinRecord.size() > 0) {
				for (JoinRecord joinRecord : listJoinRecord) {
					// 删除提醒消息
					todayWorksService.updateTodoWorkRead(joinRecord.getId(),
							comId, userOrganic.getUserId(),
							ConstantInterface.TYPE_APPLY, 0);
				}
			}

		}
		return userOrgs;
	}

	/**
	 * 修改用户 更新修改
	 * 
	 * @param userInfo
	 */
	public void updateUserInfo(UserInfo userInfo) {

		if (!"".equals(StringUtil.delNull(userInfo.getUserName()))) {
			userInfo.setAllSpelling(PinyinToolkit.cn2Spell(userInfo
					.getUserName()));
			userInfo.setFirstSpelling(PinyinToolkit.cn2FirstSpell(userInfo
					.getUserName()));
		}
		userInfoDao.update(userInfo);
	}

	/**
	 * 修改用户 更新修改 只更新用户表信息 其他相关联信息不管
	 * 
	 * @param userInfo
	 */
	public void updateUserInfoOnly(UserInfo userInfo) {
		userInfoDao.update(userInfo);
	}

	/**
	 * 查询用户信息详细 查询详细信息
	 * 
	 * @param id
	 *            要查询的人员的主键ID
	 * @return 人员信息
	 */
	public UserInfo getUserInfo(Integer comId, Integer id) {
		// 人员信息
		UserInfo userInfo = userInfoDao.getUserInfo(comId, id);
		if (null != userInfo) {
			Integer countSub = userInfoDao.countSubUser(id, comId);// 取得直属下属的成员数
			userInfo.setCountSub(countSub);
		}
		return userInfo;
	}

	/**
	 * 验证用户账号唯一性
	 * 
	 * @param loginName
	 *            登录名
	 * @return 账号唯一返回true
	 */
	public boolean validateLoginName(String loginName) {
		return userInfoDao.validateLoginName(loginName);
	}

	/**
	 * 根据账号和密码查找用户
	 * 
	 * @param loginName
	 * @param password
	 * @param comId
	 * @return
	 */
	public UserInfo userAuth(String loginName, String password, String comId) {
		UserInfo userInfo = userInfoDao.userAuth(loginName.toLowerCase(),
				password, comId);
		if (null != userInfo) {
			// 直属下属成员数
			Integer countSub = this.countSubUser(userInfo.getId(),
					userInfo.getComId());
			userInfo.setCountSub(countSub);
		}
		return userInfo;
	}

	/**
	 * 取得操作人员的直属下属成员数
	 * 
	 * @param userId
	 *            操作人员主键
	 * @param comId
	 *            企业号
	 * @return
	 */
	public Integer countSubUser(Integer userId, Integer comId) {
		Integer count = userInfoDao.countSubUser(userId, comId);
		return count;
	}

	/**
	 * 查询人员信息
	 * 
	 * @param userInfo
	 * @return
	 */
	public List<UserInfo> listUser(UserInfo userInfo) {
		return userInfoDao.listUser(userInfo);
	}

	/**
	 * 查询当前在线人数
	 * 
	 * @return 在线人数
	 */
	public int countOnlineUser() {
		return userInfoDao.countOnlineUser();
	}

	/**
	 * 更改用户的最后在线时间
	 * 
	 * @param userid
	 *            人员主键ID
	 * @param comId
	 */
	public void updateLastOnlineTime(Integer userid, Integer comId) {
		UserOrganic userOrganic = new UserOrganic();
		// 企业编号
		userOrganic.setComId(comId);
		// 用户
		userOrganic.setUserId(userid);
		// 最后在线时间
		userOrganic.setLastOnlineTime(DateTimeUtil
				.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		// 修改在线改时间
		userInfoDao.updateLastOnLineTime(userOrganic);
	}

	/**
	 * 更新背景皮肤
	 * 
	 * @param userInfo
	 */
	public void updateUserInfoSkin(UserInfo userInfo) {
		userInfoDao.update(userInfo);
	}

	/**
	 * 添加新企业用户
	 * 
	 * @param organic
	 */
	public void addOrganic(Organic organic) {
		userInfoDao.add(organic);
	}

	/**
	 * 新企业用户注册主键验证
	 * 
	 * @param key
	 * @return
	 */
	public boolean orgKeyCheck(Integer key) {
		return userInfoDao.orgKeyCheck(key);
	}

	/**
	 * 修改密码前验证密码
	 * 
	 * @param id
	 * @param passwordMD5
	 *            加密后的密码
	 * @return
	 */
	public boolean validatePassword(String id, String passwordMD5) {
		return userInfoDao.validatePassword(id, passwordMD5);
	}

	/**
	 * 查询部门的用户列表
	 * 
	 * @param userInfo
	 * @return
	 */
	public List<UserInfo> listPagedUserForDep(UserInfo userInfo) {
		List<UserInfo> list = userInfoDao.listPagedUserForDep(userInfo);
		return list;
	}

	/**
	 * 查询分组的用户列表
	 * 
	 * @param userInfo
	 * @param grpId
	 * @return
	 */
	public List<UserInfo> listPagedUserForGrp(UserInfo userInfo, String grpId) {
		List<UserInfo> list = userInfoDao.listPagedUserForGrp(userInfo, grpId);
		return list;
	}

	/**
	 * 根据人员主键owner获取所属组群
	 * 
	 * @param comId
	 * @param owner
	 * @return
	 */
	public List<SelfGroup> listSelfGroup(Integer comId, Integer owner) {
		return userInfoDao.listSelfGroup(comId, owner);
	}

	/**
	 * 添加非管理人员
	 * 
	 * @param userInfo
	 */
	public void addUserinfoOnly(UserInfo userInfo) {
		userInfoDao.add(userInfo);

	}

	/**
	 * 验证系统中是否存在用户
	 * 
	 * @param account
	 * @return
	 */
	public UserInfo getUserInfoByAccount(String account) {
		UserInfo user = userInfoDao.getUserInfoByAccount(account);
		return user;
	}

	/**
	 * 待审核的
	 * 
	 * @param joinRecord
	 * @return
	 */
	public List<JoinRecord> listPagedForJoin(JoinRecord joinRecord) {
		List<JoinRecord> list = userInfoDao.listPagedForJoin(joinRecord);
		return list;
	}
	/**
	 * 批量审核用户信息
	 * @param ids 用户临时表的主键
	 * @param sessionUser 当前操作员
	 * @param state 1统同意 0 不同意
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<JoinRecord> batchCheckUserInfos(Integer[] ids, UserInfo sessionUser,String state) {
		List<JoinRecord> listJoinRecord = new ArrayList<JoinRecord>();
		//是否同意审核通过
		Boolean flag = state.equals(ConstantInterface.USER_CHECK_AGREE);
		for (Integer id : ids) {
			JoinRecord joinRecord = (JoinRecord) userInfoDao.objectQuery(JoinRecord.class, id);
			if(null!=joinRecord){
				if(flag){//审核通过，添加用户
					this.addApplyUser(joinRecord.getAccount().toLowerCase(), sessionUser,sessionUser.getOptIP());
				}
				
				// 删除待办提醒
				todayWorksService.updateTodoWorkRead(id, sessionUser.getComId(), null,
						ConstantInterface.TYPE_APPLY, 0);
				//账号
				String account = joinRecord.getAccount().toLowerCase();
				
				userInfoDao.delById(JoinRecord.class, joinRecord.getId());
				//删除申请加入
				userInfoDao.delByField("joinTemp", new String[]{"comId","account","joinType"}, 
						new Object[]{sessionUser.getComId(),account,ConstantInterface.JOIN_APPLY});
				listJoinRecord.add(joinRecord);
			}
			
		}
		if(flag){
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量同意用户加入企业", 
					ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
		}else{
			//系统日志
	        systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量拒绝用户加入企业",
	        		ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
		}
		return listJoinRecord;
	}
	/**
	 * 链接的有效性判断
	 * 
	 * @param confirmId
	 * @return
	 */
	public JoinRecord justConfirmId(String confirmId) {
		JoinRecord obj = userInfoDao.justConfirmId(confirmId);
		return obj;
	}

	/**
	 * 完善用户信息
	 * 
	 * @param user
	 *            新的用户信息
	 * @param recordId
	 *            加入记录主键
	 * @param joinType
	 *            加入类型
	 * @param userId
	 *            邀请人主键
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void finishInfo(UserInfo userInfo, Integer recordId,
			String joinType, Integer userId,String optIP) {

		// 删除加入记录
		userInfoDao.delById(JoinRecord.class, recordId);

		String account = userInfo.getEmail();
		if (null == account || "".equals(account)) {
			account = userInfo.getMovePhone();
		}
		// 先判断是否需要添加用户
		UserInfo obj = userInfoDao.getUserInfoByAccount(account);
		Integer id = 0;
		if (null == obj) {
			// 用户名称不为空时此时添加用户名称全拼和简拼
			if (null != userInfo.getUserName()) {
				userInfo.setAllSpelling(PinyinToolkit.cn2Spell(userInfo
						.getUserName()));
				userInfo.setFirstSpelling(PinyinToolkit.cn2FirstSpell(userInfo
						.getUserName()));
			}
			// 添加人员
			id = userInfoDao.add(userInfo);

		} else {
			id = obj.getId();
		}

		if ("0".equals(joinType)) {// 是注册的

			// 人员添加日志
			systemLogService.addSystemLog(id, userInfo.getUserName(), "激活用户信息",
					ConstantInterface.TYPE_USER, userInfo.getComId(),optIP);

			// 设立默认部门
			Integer depId = departmentService.addDefaultDep(id, userInfo, optIP);

			// 用户与企业的关系
			UserOrganic userOrganic = new UserOrganic();
			// 企业编号
			userOrganic.setComId(userInfo.getComId());
			// 用户主键
			userOrganic.setUserId(id);
			// 部门主键
			userOrganic.setDepId(depId);
			// 管理员
			userOrganic.setAdmin("1");
			// 用户激活
			userOrganic.setEnabled("1");
			// 设置默认积分
			userOrganic.setJifenScore(0);
			// 是否完善信息 默认不需要
			userOrganic.setAlterInfo("1");
			
			//设置用户启用
			userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);
			
			//是否为首席 默认不是
			userOrganic.setIsChief("0");

			userInfoDao.add(userOrganic);

			// 注册添加企业主键
			Organic organic = new Organic();
			// 公司编号
			organic.setOrgNum(userInfo.getComId());
			organic.setOrgName(userInfo.getOrgName());
			// 激活邮箱后启用
			organic.setEnabled("1");
			
			Integer orgId = userInfoDao.add(organic);

			// 企业添加日志
			systemLogService.addSystemLog(id, userInfo.getUserName(), "设立企业",
					ConstantInterface.TYPE_ORG, userInfo.getComId(),optIP);
			// 添加积分的历史记录
			jifenService.addJifen(userInfo.getComId(), id,
					ConstantInterface.TYPE_ORG, null, orgId);

		} else {
			// 取得默认的部门信息
			Department dep = departmentService.getDefalutDep(userInfo.getComId());

			// 用户与企业的关系
			UserOrganic userOrganic = new UserOrganic();
			// 企业编号
			userOrganic.setComId(userInfo.getComId());
			// 用户主键
			userOrganic.setUserId(id);
			// 部门主键
			userOrganic.setDepId(dep.getId());
			userOrganic.setEnabled("1");
			// 非管理员
			userOrganic.setAdmin("0");
			// 设置默认积分
			userOrganic.setJifenScore(0);
			// 是否完善信息 默认不需要
			userOrganic.setAlterInfo("1");
			//是否为首席 默认不是
			userOrganic.setIsChief("0");
			//设置用户启用
			userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);

			userInfoDao.add(userOrganic);

			// 完善用户信息
			if ("1".equals(joinType)) {// 申请的加入
				// 人员添加日志
				systemLogService.addSystemLog(id, userInfo.getUserName(),
						"激活用户信息", ConstantInterface.TYPE_USER,
						userInfo.getComId(),optIP);

			} else if ("2".equals(joinType)) {// 邀请的新增用户
				// 人员添加日志
				systemLogService.addSystemLog(id, userInfo.getUserName(),
						"激活用户信息", ConstantInterface.TYPE_USER,
						userInfo.getComId(),optIP);
				// 添加积分的历史记录
				jifenService.addJifen(userInfo.getComId(), userId,
						ConstantInterface.TYPE_USERINV, "成功邀请到同事加入企业",
						recordId);

			}

		}

	}


	/**
	 * 取得公司的唯一账号
	 * @param account 账号
	 * @param comId 团队号
	 * @param type 邮箱还是手机号
	 * @return
	 */
	public UserInfo getUserInfoByType(String account, Integer comId,String type) {
		UserInfo user = userInfoDao.getUserInfoByType(account, comId,type);
		return user;
	}

	/**
	 * 修改密码
	 * 
	 * @param userInfo
	 * @param sessionUser 
	 */
	public void updatePassword(UserInfo userInfo, UserInfo sessionUser) {
		if(null != sessionUser){//统一密码
			String email = sessionUser.getEmail();
			String movePhone = sessionUser.getMovePhone();
			
			String userName = sessionUser.getUserName();
			String passwd = userInfo.getPassword();
			
			if(!StringUtil.isBlank(email) && !StringUtil.isBlank(movePhone)){//
				//修改该账号的其他信息
				registService.updateJoinTemps(email,userName,passwd);
				//修改该账号的其他信息
				registService.updateJoinTemps(movePhone,userName,passwd);
			}else if(!StringUtil.isBlank(email)){
				//修改该账号的其他信息
				registService.updateJoinTemps(email,userName,passwd);
			}else if(!StringUtil.isBlank(movePhone)){
				//修改该账号的其他信息
				registService.updateJoinTemps(movePhone,userName,passwd);
			}
		}
		
		userInfoDao.updatePassword(userInfo);

	}

	/**
	 * 人员邀请记录
	 * 
	 * @param joinRecord
	 * @return
	 */
	public List<JoinRecord> listPagedInvUser(JoinRecord joinRecord) {
		List<JoinRecord> list = userInfoDao.listPagedInvUser(joinRecord);
		return list;
	}

	/**
	 * 删除人员邀请记录
	 * 
	 * @param ids
	 */
	public void delInvUser(Integer[] ids) {
		userInfoDao.delById(JoinRecord.class, ids);

	}

	/**
	 * 重新邀请时取出原来记录
	 * 
	 * @param id
	 * @return
	 */
	public JoinRecord getJoinRecord(Integer id) {
		return (JoinRecord) userInfoDao.objectQuery(JoinRecord.class, id);
	}

	/**
	 * 上次使用的分组
	 * 
	 * @param comId
	 *            企业编号
	 * @param userId
	 *            操作员
	 * @return
	 */
	public List<UsedGroup> listUsedGroup(Integer comId, Integer userId) {
		List<UsedGroup> list = userInfoDao.listUsedGroup(comId, userId);
		return list;
	}

	/**
	 * 获取人员在当前组织下所在的所有分组
	 * 
	 * @param comId
	 * @param userId
	 * @return
	 */
	public List<SelfGroup> listSelfGroupUserIn(Integer comId, Integer userId) {
		return userInfoDao.listSelfGroupUserIn(comId, userId);
	}

	/**
	 * 验证用户登陆信息
	 * 
	 * @param lowerCase
	 * @param passwordMD5
	 * @return
	 */
	public UserInfo checkUserInfo(String email, String passwordMD5) {
		return userInfoDao.checkUserInfo(email, passwordMD5);
	}

	/**
	 * 修改头像
	 * 
	 * @param comId
	 *            企业编号
	 * @param userId
	 *            用户主键
	 * @param bigHeadPortrait
	 *            大头像
	 * @param mediumHeadPortrait
	 *            中头像
	 * @param smallHeadPortrait
	 *            小头像
	 */
	public void updateUserOrganicImg(Integer comId, Integer userId,
			Integer bigHeadPortrait, Integer mediumHeadPortrait,
			Integer smallHeadPortrait) {
		userInfoDao.updateUserOrganicImg(comId, userId, bigHeadPortrait,
				mediumHeadPortrait, smallHeadPortrait);

	}

	/**
	 * 取得注册用户最近注册的信息
	 * 
	 * @param email
	 * @return
	 */
	public JoinRecord getConfirmIdForRegist(String email) {
		return userInfoDao.getConfirmIdForRegist(email);
	}

	/**
	 * 删除加入记录
	 * 
	 * @param id
	 *            加入记录主键
	 */
	public void delJoinRecord(Integer id) {
		userInfoDao.delById(JoinRecord.class, id);

	}

	/**
	 * 统计需要你查看的模块数据集合
	 * 
	 * @param comId
	 * @param userId
	 * @return
	 */
	public List<TodayWorks> listTodayWorks(Integer comId, Integer userId) {
		return userInfoDao.listTodayWorks(comId, userId);
	}

	/**
	 * 删除邮箱验证码
	 * 
	 * @param id
	 *            邮箱验证码主键
	 */
	public void delPassYzmById(Integer id) {
		userInfoDao.delById(PassYzm.class, id);

	}

	/**
	 * 添加邮箱验证码
	 * 
	 * @param passYzm
	 */
	public Integer addPassYzm(PassYzm passYzm) {
		return userInfoDao.add(passYzm);

	}

	/**
	 * 通过帐号修改用户
	 * @param user
	 */
	public void updateUserInfoByAccount(UserInfo user) {
		if(null != user){//统一密码
			String email = user.getEmail();
			String movePhone = user.getMovePhone();
			
			String userName = user.getUserName();
			String passwd = user.getPassword();
			
			if(!StringUtil.isBlank(email) && !StringUtil.isBlank(movePhone)){//
				//修改该账号的其他信息
				registService.updateJoinTemps(email,userName,passwd);
				//修改该账号的其他信息
				registService.updateJoinTemps(movePhone,userName,passwd);
			}else if(!StringUtil.isBlank(email)){
				//修改该账号的其他信息
				registService.updateJoinTemps(email,userName,passwd);
			}else if(!StringUtil.isBlank(movePhone)){
				//修改该账号的其他信息
				registService.updateJoinTemps(movePhone,userName,passwd);
			}
		}
		
		userInfoDao.updateUserInfoByAccount(user);

	}

	/**
	 * 树形查询部门的用户列表
	 * 
	 * @param userInfo
	 * @return
	 */
	public List<UserInfo> listPagedUserTreeForDep(UserInfo userInfo) {
		return userInfoDao.listPagedUserTreeForDep(userInfo);
	}

	/**
	 * 公司总人数
	 * 
	 * @param comId
	 * @return
	 */
	public Integer countUsers(Integer comId) {
		return userInfoDao.countUsers(comId);
	}

	/**
	 * 验证当前的用户的登录别名
	 * 
	 * @param nickName
	 * @param id
	 * @return
	 */
	public List<UserInfo> checkNickName(String nickName, Integer userId) {
		return userInfoDao.checkNickName(nickName, userId);
	}

	/**
	 * 用户信息单个修改
	 * 
	 * @param userInfo
	 */
	public void updateUserDetail(UserInfo userInfo, UserInfo sessioUser) {

		userInfo.setId(sessioUser.getId());
		if (null != userInfo.getNickname()
				&& !"".equals(userInfo.getNickname())) {// 修改登录别名
			userInfoDao.updateUserNickname(userInfo);
		}
		if (null != userInfo.getUserName()
				&& !"".equals(userInfo.getUserName())) {// 修改用户名
			if (!"".equals(StringUtil.delNull(userInfo.getUserName()))) {
				userInfo.setAllSpelling(PinyinToolkit.cn2Spell(userInfo
						.getUserName()));
				userInfo.setFirstSpelling(PinyinToolkit.cn2FirstSpell(userInfo
						.getUserName()));
			}
			userInfoDao.updateUserName(userInfo);
		}
		if (null != userInfo.getGender() && !"".equals(userInfo.getGender())) {// 修改用户性别
			userInfoDao.updateUserGender(userInfo);
		}
		if (null != userInfo.getBirthday()
				&& !"".equals(userInfo.getBirthday())) {// 修改用户生日
			userInfoDao.updateUserBirthday(userInfo);
		}
		if (null != userInfo.getJob() && !"".equals(userInfo.getJob())) {// 职位变动
			// 职位变动
			userInfoDao.updateUserOrganic(sessioUser.getComId(),
					sessioUser.getId(), userInfo.getJob());
		}
		if (null != userInfo.getQq() && !"".equals(userInfo.getQq())) {// 修改用户QQ
			userInfoDao.updateUserQq(userInfo);
		}
		if (null != userInfo.getLinePhone()
				&& !"".equals(userInfo.getLinePhone())) {// 修改用户座机号
			userInfoDao.updateUserLinePhone(userInfo);
		}
		if (null != userInfo.getWechat() && !"".equals(userInfo.getWechat())) {// 修改用户微信号
			userInfoDao.updateUserWechat(userInfo);
		}
		if (null != userInfo.getHireDate()
				&& !"".equals(userInfo.getHireDate())) {// 修改用户入职时间
			String hireDate = userInfo.getHireDate();
			String sessionHireDate = sessioUser.getHireDate().substring(0, 10);
			if (!hireDate.equals(sessionHireDate)) {
				hireDate = hireDate
						+ sessioUser.getHireDate().substring(10,
								sessioUser.getHireDate().length());
				userInfoDao.updateUserHireDate(sessioUser.getComId(),
						sessioUser.getId(), hireDate);

			}
		}

		if (null != userInfo.getUserName()// 姓名不为空
				&& null != userInfo.getGender()// 性别不为空
				&& null != userInfo.getNickname()// 绰号不为空
				&& null != userInfo.getJob()// 职位不为空
				&& null != userInfo.getBirthday()) {// 生日不为空

			// 添加积分
			jifenService.addJifen(sessioUser.getComId(), sessioUser.getId(),
					ConstantInterface.TYPE_USERINFO, "个人资料，完善基本信息", 0);
		} else if (null != userInfo.getQq()// QQ不为空
				&& null != userInfo.getLinePhone()// 固定电话不为空
				&& null != userInfo.getMovePhone()// 移动电话不为空
				&& null != userInfo.getWechat()// 微信不为空
		) {
			// 添加积分
			jifenService.addJifen(sessioUser.getComId(), sessioUser.getId(),
					ConstantInterface.TYPE_USERTEL, "个人资料，完善联系信息", 0);
		}

	}
	/**
	 * 修改用户名和密码
	 * @param userName 用户名
	 * @param password 密码
	 * @param sessioUser 当前操作员
	 */
	public UserInfo updateUserPassAndName(String userName,String password, UserInfo sessioUser){
		
		UserInfo userInfo = new UserInfo();
		userInfo.setId(sessioUser.getId());
		//设置用户名
		userInfo.setUserName(userName);
		if (null !=userName
				&& !"".equals(StringUtil.delNull(userName))) {// 修改用户名
			userInfo.setAllSpelling(PinyinToolkit.cn2Spell(userInfo
					.getUserName()));
			userInfo.setFirstSpelling(PinyinToolkit.cn2FirstSpell(userInfo
					.getUserName()));
		}
		//密码加密
		String passwordMD5 = Encodes.encodeMd5(password);
		userInfo.setPassword(passwordMD5);
		
		userInfoDao.update(userInfo);
		return userInfo;
		
	}

	/**
	 * 直属上级更新
	 * 
	 * @param userIds
	 * @param isChief 
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	public boolean updateImmediateSuper(Integer[] userIds, String isChief, UserInfo sessionUser)
			throws Exception {
		boolean succ = true;
		try {
			// 先删除直属上级原有配置
			userInfoDao.delByField("immediateSuper", new String[] { "comId",
					"creator" },
					new Object[] { sessionUser.getComId(), sessionUser.getId() });
			StringBuffer leaderName = null;
			
			//修改用户是否为首席
			userInfoDao.updateUserChief(isChief,sessionUser);
			if (null != userIds && userIds.length > 0) {
				// 直属上级更新
				leaderName = new StringBuffer();
				ImmediateSuper immediateSuper = null;
				for (Integer userId : userIds) {
					UserInfo sharerInfo = userInfoDao.getUserInfo(
							sessionUser.getComId(), userId);
					leaderName.append(sharerInfo.getUserName() + ",");
					immediateSuper = new ImmediateSuper();
					immediateSuper.setComId(sessionUser.getComId());
					immediateSuper.setCreator(sessionUser.getId());
					immediateSuper.setLeader(userId);
					userInfoDao.add(immediateSuper);

				}
				leaderName = new StringBuffer(leaderName.subSequence(0,
						leaderName.lastIndexOf(",")));
				// 添加直属上级更新日志
				systemLogService.addSystemLog(sessionUser.getId(),
						sessionUser.getUserName(),
						"直属上级更新为\"" + leaderName.toString() + "\"",
						ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
			} else {
				// 添加直属上级更新日志
				systemLogService.addSystemLog(sessionUser.getId(),
						sessionUser.getUserName(), "清空直属设置",
						ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
			}
			
			this.updateMyLeaders(sessionUser);//更新个人上级领导表myLeaders
			
		} catch (Exception e) {
			succ = false;
			throw e;
		}
		return succ;
	}

	/**
	 * 删除直属上级,并设定为首席
	 * 
	 * @param isChief
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Integer> updateNoLeader(String isChief, UserInfo sessionUser)
			throws Exception {
		List<Integer> leaderIds = new ArrayList<Integer>();
		
		//修改用户是否为首席
		userInfoDao.updateUserChief(isChief,sessionUser);
		
		List<ImmediateSuper> listImmediateSuper = this.listImmediateSuper(sessionUser);
		if(!CommonUtil.isNull(listImmediateSuper)){
			for (ImmediateSuper immediateSuper : listImmediateSuper) {
				leaderIds.add(immediateSuper.getLeader());
				// 删除直属上级
				userInfoDao.delByField("immediateSuper", new String[] { "comId",
						"creator", "leader" }, new Integer[] { sessionUser.getComId(),
						sessionUser.getId(), immediateSuper.getLeader() });
				// 删除直属上级日志
				systemLogService.addSystemLog(sessionUser.getId(),
						sessionUser.getUserName(), "删除直属上级\"" + immediateSuper.getLeaderName()
						+ "\"", ConstantInterface.TYPE_USER,
						sessionUser.getComId(),sessionUser.getOptIP());
			}
		}
		return leaderIds;
	}
	
	/**
	 * 该用户有上级,给出标识
	 * @param isChief
	 * @param sessionUser
	 * @throws Exception
	 */
	public void updateHasLeader(String isChief,UserInfo sessionUser)throws Exception{
		//修改用户不是首席
		userInfoDao.updateUserChief(isChief,sessionUser);
	}
	
	/**
	 * 删除直属上级
	 * 
	 * @param userId
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean delImmediateSuper(Integer userId, UserInfo userInfo)
			throws Exception {
		boolean succ = true;
		try {
			UserInfo delInfo = userInfoDao.getUserInfo(userInfo.getComId(),
					userId);
			// 删除直属上级
			userInfoDao.delByField("immediateSuper", new String[] { "comId",
					"creator", "leader" }, new Integer[] { userInfo.getComId(),
					userInfo.getId(), userId });
			// 删除直属上级日志
			systemLogService.addSystemLog(userInfo.getId(),
					userInfo.getUserName(), "删除直属上级\"" + delInfo.getUserName()
							+ "\"", ConstantInterface.TYPE_USER,
					userInfo.getComId(),userInfo.getOptIP());

		} catch (Exception e) {
			succ = false;
			throw e;
		}
		return succ;
	}

	/**
	 * 获取个人直属领导集合
	 * 
	 * @param userInfo
	 * @return
	 */
	public List<ImmediateSuper> listImmediateSuper(UserInfo userInfo) {
		return userInfoDao.listImmediateSuper(userInfo);
	}

	/**
	 * 企业的所有成员主键
	 * 
	 * @param comId
	 * @return
	 */
	public List<UserInfo> listUser(Integer comId) {
		return userInfoDao.listUser(comId);
	}

	/**
	 * 企业的所有成员主键，打卡编号
	 *
	 * @param comId
	 * @return
	 */
	public List<UserInfo> listUserWithEnNumber(Integer comId) {
		return userInfoDao.listUserWithEnNumber(comId);
	}

	/**
	 * 直属上级设置是否闭环验证
	 * 
	 * @param leader
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean superClosedLoopCheck(int leader, UserInfo userInfo){
		boolean succ = true;
		List<ImmediateSuper> listSuper = userInfoDao.superClosedLoopCheck(
				leader, userInfo);
		if (null == listSuper || listSuper.isEmpty()) {
			succ = true;
		} else {
			succ = false;
		}
		return succ;
	}

	/**
	 * 离职移交
	 * 
	 * @param userInfo
	 *            离职人信息
	 * @param userId
	 *            交接人主键
	 * @param userName
	 *            交接人姓名
	 */
	public List<ImmediateSuper> updateFordimission(UserInfo userInfo,
			Integer userId, String userName) {

		// 离职人员的所有上级
		List<ImmediateSuper> listSup = userInfoDao.listImmediateSuper(userInfo);

		// 删除定时系统中数据
		clockService.disableClock(userInfo.getId(), userInfo.getComId());

		// 超级管理权限的移除
		if (userInfo.getAdmin().equals("1")) {
			// 授权
			UserOrganic userOrganic = new UserOrganic();
			userOrganic.setComId(userInfo.getComId());
			userOrganic.setUserId(userId);

			userInfoDao
					.update("update userOrganic set admin='1' where comId=:comId and userId=:userId",
							userOrganic);
		}
		// 回收权限
		UserOrganic userOrganic = new UserOrganic();
		userOrganic.setComId(userInfo.getComId());
		userOrganic.setUserId(userInfo.getId());
		userInfoDao
				.update("update userOrganic set admin='0',enabled='0' where comId=:comId and userId=:userId",
						userOrganic);

		// 客户参与人删除
		userInfoDao.delByField("customerSharer", new String[] { "comId",
				"userId" },
				new Object[] { userInfo.getComId(), userInfo.getId() });
		// 离职人员负责的客户
		List<Customer> crmlist = crmService.listUserAllCrm(userInfo.getComId(),
				userInfo.getId());
		if (null != crmlist && crmlist.size() > 0) {
			for (Customer customer : crmlist) {

				CustomerHandOver crmHandOver = new CustomerHandOver();
				// 企业号
				crmHandOver.setComId(userInfo.getComId());
				// 客户主键
				crmHandOver.setCustomerId(customer.getId());
				// 原有人员
				crmHandOver.setFromUser(customer.getOwner());

				// 现有人员
				crmHandOver.setToUser(userId);
				// 添加移交记录
				userInfoDao.add(crmHandOver);

				// 客户移交
				customer.setOwner(userId);
				userInfoDao.update(customer);
				// 添加日志
				crmService.addCustomerLog(userInfo.getComId(),
						customer.getId(), userInfo.getId(),
						userInfo.getUserName() + "离职时把客户移交给了\"" + userName
								+ "\"");

				List<UserInfo> shares = this.listOwnerForMsg(userInfo.getComId(), customer.getId(),null, ConstantInterface.TYPE_CRM,null);
				// 添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo, userId,
						customer.getId(), userInfo.getUserName()
								+ "离职时把客户移交给了\"" + userName + "\"",
						ConstantInterface.TYPE_CRM, shares, null);
			}

		}
		// 项目参与人删除
		userInfoDao.delByField("itemSharer",
				new String[] { "comId", "userId" },
				new Object[] { userInfo.getComId(), userInfo.getId() });
		// 离职人员负责的项目
		List<Item> itemlist = itemService.listUserAllItem(userInfo.getComId(),
				userInfo.getId());
		if (null != itemlist && itemlist.size() > 0) {
			for (Item item : itemlist) {
				ItemHandOver itemHandOver = new ItemHandOver();
				// 企业号
				itemHandOver.setComId(userInfo.getComId());
				// 客户主键
				itemHandOver.setItemId(item.getId());
				// 原有人员
				itemHandOver.setFromUser(item.getOwner());
				// 现有人员
				itemHandOver.setToUser(userId);
				// 添加移交记录
				userInfoDao.add(itemHandOver);

				// 项目移交
				item.setOwner(userId);
				userInfoDao.update(item);
				// 添加日志
				itemService.addItemLog(userInfo.getComId(), item.getId(),
						userInfo.getId(), userInfo.getUserName(),
						"离职时把项目移交给了\"" + userName + "\"");
				// 已完成的项目不提醒
				if (null != item.getState() && item.getState() == 1) {
					List<UserInfo> shares = this.listOwnerForMsg(userInfo.getComId(), item.getId(),null, ConstantInterface.TYPE_ITEM,null);
					// 添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo, userId,
							item.getId(), userInfo.getUserName()
									+ "离职时把项目移交给了\"" + userName + "\"",
							ConstantInterface.TYPE_ITEM, shares, null);

				}
			}
		}
		//移交任务
		taskService.updateFordimission(userInfo, userId, userName);

		// 删除督察人员
		userInfoDao.delByField("forceInPersion", new String[] { "comId",
				"userId" },
				new Object[] { userInfo.getComId(), userInfo.getId() });
		// 删除待办更新
		userInfoDao.delByField("todayworks",
				new String[] { "comId", "userId" },
				new Object[] { userInfo.getComId(), userInfo.getId() });
		//将离职人员的下属设定为接收人员
		userInfoDao.updateDismissionLeader(userInfo,userId);

		return listSup;
	}

	/**
	 * 用户头像信息
	 * 
	 * @param comId
	 * @param userId
	 * @return
	 */
	public UserInfo getUserHeadImg(Integer comId, Integer userId) {
		return userInfoDao.getUserHeadImg(comId, userId);
	}

	/**
	 * 取得企业的管理人员
	 * 
	 * @param comId
	 * @return
	 */
	public List<UserInfo> listOrgAdmin(Integer comId) {
		List<UserInfo> list = userInfoDao.listOrgAdmin(comId);
		return list;
	}

	

	/**
	 * 列出常用的人员
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            操作人员主键
	 * @param num
	 *            人员列举限制
	 * @return
	 */
	public List<UserInfo> listUsedUser(Integer comId, Integer userId,
			Integer num) {
		UserInfo sessionUser = new UserInfo();
		sessionUser.setComId(comId);
		sessionUser.setId(userId);
		List<UserInfo> list = userInfoDao.listUsedUser(sessionUser, null, num);
		return list;
	}
	public List<UserInfo> listUsedUserV2(UserInfo sessionUser, String onlySubState,
			Integer num) {
		List<UserInfo> list = userInfoDao.listUsedUser(sessionUser, onlySubState, num);
		return list;
	}

	/**
	 * 保存最近选择的人员
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            操作人员主键
	 * @param userIds
	 *            选择的人员主键
	 */
	public void addUsedUser(Integer comId, Integer userId, Integer[] userIds) {
		for (Integer usedUserId : userIds) {
			// 添加最近使用的人员
			userInfoDao.addUsedUser(comId, userId, usedUserId);
		}

	}

	/**
	 * 取得缓存用户是否为系统可用的用户
	 * 
	 * @param email
	 *            邮箱
	 * @param password
	 *            密码
	 * @param comId
	 *            企业号
	 * @param id
	 *            用户主键
	 * @return
	 */
	public UserInfo getUserInfoById(String id) {
		UserInfo userInfo = userInfoDao.getUserInfoById(id);
		return userInfo;
	}

	/**
	 * 获取当前登录人私有组及其成员
	 * 
	 * @param userInfo
	 * @return
	 */
	public List<UserInfo> listUserOfGroup(UserInfo userInfo) {
		return userInfoDao.listUserOfGroup(userInfo);
	}

	/**
	 * 获取企业部门以及部门人员
	 * 
	 * @param userInfo
	 * @return
	 */
	public List<UserInfo> listUserOfDep(UserInfo userInfo) {
		return userInfoDao.listUserOfDep(userInfo);
	}

	/**
	 * 取得用户的弹窗设置
	 * 
	 * @param userId
	 *            用户主键
	 * @param comId
	 *            企业号
	 * @return
	 */
	public UserOrganic getUserOrganic(Integer userId, Integer comId) {
		UserOrganic userOrganic = userInfoDao.getUserOrganic(userId, comId);
		return userOrganic;
	}

	/**
	 * 取得人员名片
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            人员主键
	 * @return
	 */
	public UserInfo getUserBaseInfo(Integer comId, Integer userId) {
		UserInfo userInfo = userInfoDao.getUserBaseInfo(comId, userId);
		return userInfo;
	}

	/**
	 * 查询聊天室的成员信息
	 * 
	 * @param comId
	 *            企业号
	 * @param userIds
	 *            成员主键
	 * @return
	 */
	public List<UserInfo> getUserInfoByIds(Integer comId, String userIds) {
		List<UserInfo> listUser = userInfoDao.getUserInfoByIds(comId, userIds);
		return listUser;
	}

	/**
	 * 修改部门
	 * 
	 * @param comId
	 *            企业编号
	 * @param userId
	 *            用户主键
	 * @param depId
	 *            部门主键
	 */
	public void updateUserDep(Integer comId, Integer userId, Integer depId) {
		userInfoDao.updateUserDep(comId, userId, depId);
	}

	/**
	 * 用户不需要提醒完善提醒
	 * 
	 * @param comId
	 *            企业编号
	 * @param userId
	 *            用户主键
	 */
	public void updateAlterInfo(Integer comId, Integer userId) {
		userInfoDao.updateAlterInfo(comId, userId);
	}

	/**
	 * 设置个性签名
	 * 
	 * @param selfIntr
	 *            个性签名
	 * @return
	 */
	public void updateUserIntr(Integer id, String selfIntr) {
		UserInfo userInfo = new UserInfo();
		// 设置人员主键
		userInfo.setId(id);
		// 设置个性签名
		userInfo.setSelfIntr(selfIntr);
		// 修改个性签名
		userInfoDao.update(userInfo);

	}

	/**
	 * 发送验证码
	 * @param sessionUser 当前操作人
	 * @param noticeType 通知类型
	 * @return
	 */
	public Map<String, Object> doSendPassYzm(UserInfo sessionUser, String noticeType) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 随机验证码生成
		Integer yzm = new CommonUtil().randomNum(4);
		PassYzm  passYzm = new PassYzm();
		passYzm.setPassYzm(yzm.toString());
		if (ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())) {
			if (CommonUtil.isPhone(sessionUser.getMovePhone())) {
				// 通过手机发送验证码
				phoneMsgService.sendMsg(sessionUser.getMovePhone(), new Object[]{yzm.toString()},
						ConstantInterface.MSG_AUTHORITY_VCODE,
						sessionUser.getComId(),sessionUser.getOptIP());
				passYzm.setAccount(sessionUser.getMovePhone());
				//先根据关键字删除验证码
				userInfoDao.delByField("passYzm", new String[]{"account"}, new Object[]{sessionUser.getMovePhone()});
			} else {
				// 电话号码不正确
				map.put("status", "n");
				map.put("info", "电话号码不正确！");
				return map;
			}
		} else if (ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())) {
			if (CommonUtil.isEmail(sessionUser.getEmail())) {
				// 邮件发送准备
				MessageSender sender = MessageSender.getMessageSender();
				// 标题
				String subject = "权限验证码";
				// 内容
				String body = "您的验证码为：" + yzm + "；" + "20分钟后失效；请及时输入验证。";
				// 发送邮件
				new Thread(new MailSendThread(sender, sessionUser.getEmail(),
						subject, body)).start();
				passYzm.setAccount(sessionUser.getEmail().toLowerCase());
				//先根据关键字删除验证码
				userInfoDao.delByField("passYzm", new String[]{"account"}, new Object[]{sessionUser.getEmail().toLowerCase()});
			} else {
				// 邮箱不正确
				map.put("status", "n");
				map.put("info", "邮箱不正确！");
				return map;
			}
		}
		//验证码持久化
		userInfoDao.add(passYzm);
		//执行成功标识
		map.put("status", "y");
		return map;
	}

	/**
	 * 审核申请的用户
	 * @param joinRecord
	 * @param basePath
	 * @param sessionUser
	 * @param checkState 审核结果 1 同意加入 2 拒绝加入
	 */
	public void updateCheckTempUser(JoinRecord joinRecord,
			UserInfo sessionUser, String checkState,String optIP) {
		//账号
		String account = joinRecord.getAccount().toLowerCase();
		
		if(checkState.equals("1")){//审核同意
			this.addApplyUser(account,sessionUser,optIP);
		}
    	
		if (null != sessionUser) {
			// 删除待办提醒
			todayWorksService
					.updateTodoWorkRead(joinRecord.getId(),
							sessionUser.getComId(), null,
							ConstantInterface.TYPE_APPLY, 0);
		}
		userInfoDao.delById(JoinRecord.class, joinRecord.getId());
		//删除申请加入
		userInfoDao.delByField("joinTemp", new String[]{"comId","account","joinType"}, 
				new Object[]{sessionUser.getComId(),account,1});
		
		//日志内容
		String content = "审核用户("+joinRecord.getAccount()+")信息";
		//系统日志
        systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), content, 
        		ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
		        
		
	}
	/**
	 * 添加用户，建立关系
	 * @param account
	 * @param sessionUser
	 */
	private void addApplyUser(String account,UserInfo sessionUser,String optIP) {
		/**
		 * 1、添加用户
		 * 2、建立关系
		 */
		//取得用户临时表数据
		JoinTemp joinTemp = registService.getJoInTempByAccount(account,sessionUser.getComId(),ConstantInterface.JOIN_APPLY);
		if(CommonUtil.isNull(joinTemp)) {
			return ;//已经加入到平台无需再审核
		}
		//注册人员名称
		String userName = joinTemp.getUserName();
		//密码
		String passwd = joinTemp.getPasswd();
		
		/*******************1、添加人员开始*********************************/
		// 先判断是否需要添加用户
		UserInfo obj = userInfoDao.getUserInfoByAccount(account);
		UserInfo newUser = new UserInfo();
		//新用户主键
		Integer userId = 0;
		if (null == obj) {
			// 用户名称不为空时此时添加用户名称全拼和简拼
			if (null != joinTemp.getUserName()) {
				newUser.setAllSpelling(PinyinToolkit.cn2Spell(joinTemp
						.getUserName()));
				newUser.setFirstSpelling(PinyinToolkit.cn2FirstSpell(joinTemp
						.getUserName()));
			}
			//设置名称
			newUser.setUserName(joinTemp.getUserName());
			//设置密码
			newUser.setPassword(joinTemp.getPasswd());
			//设置账号
			if(account.indexOf("@")>0){//邮箱为账号
				newUser.setEmail(account);
			}else{//手机为账号
				newUser.setMovePhone(account);
			}
			// 添加人员
			userId = userInfoDao.add(newUser);

		} else {
			userId = obj.getId();
			//注册人员名称
			userName = obj.getUserName();
			//密码
			passwd = obj.getPassword();
		}
		//修改该账号的其他信息
		registService.updateJoinTemps(account,userName,passwd);
		// 人员添加日志
		systemLogService.addSystemLog(userId, userName,
				"激活用户信息", ConstantInterface.TYPE_USER,
				sessionUser.getComId(),optIP);
		
		// 用户与企业的关系
		UserOrganic userOrganic = userInfoDao.getUserOrganic(userId, sessionUser.getComId());
		if(null!=userOrganic){
			userOrganic.setEnabled("1");
			
			userOrganic.setIsChief("0");
			userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);
			
			userInfoDao.update(userOrganic);
		}else{
			userOrganic = new UserOrganic();
			// 企业编号
			userOrganic.setComId(sessionUser.getComId());
			// 用户主键
			userOrganic.setUserId(userId);
			
			// 取得默认的部门信息
			Integer depId =  departmentService.getDefalutDep(sessionUser.getComId()).getId();
			// 部门主键
			userOrganic.setDepId(depId);
			// 管理员
			userOrganic.setAdmin("0");
			// 用户激活
			userOrganic.setEnabled("1");
			// 设置默认积分
			userOrganic.setJifenScore(0);
			// 是否完善信息 默认不需要
			userOrganic.setAlterInfo("1");
			
			userOrganic.setIsChief("0");
			userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);
			
			//设置用户启用
			userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);
			
			userInfoDao.add(userOrganic);
		}
		
	}
	/**
	 * 邀请用户激活链接
	 * @param joinTemp
	 * @param recordId
	 * @return 
	 */
	public UserInfo addInvUser(JoinTemp joinTemp,JoinRecord joinRecord,String optIP){
		//注册人员名称
		String userName = joinTemp.getUserName();
		//密码
		String passwd = joinTemp.getPasswd();
		
		//账号
		String account = joinTemp.getAccount();
		
		/*******************1、添加人员开始*********************************/
		// 先判断是否需要添加用户
		UserInfo obj = userInfoDao.getUserInfoByAccount(account);
		UserInfo newUser = new UserInfo();
		//新用户主键
		Integer userId = 0;
		if (null == obj) {
			// 用户名称不为空时此时添加用户名称全拼和简拼
			if (null != joinTemp.getUserName()) {
				newUser.setAllSpelling(PinyinToolkit.cn2Spell(joinTemp
						.getUserName()));
				newUser.setFirstSpelling(PinyinToolkit.cn2FirstSpell(joinTemp
						.getUserName()));
			}
			//设置名称
			newUser.setUserName(joinTemp.getUserName());
			//设置密码
			newUser.setPassword(joinTemp.getPasswd());
			//设置账号
			if(account.indexOf("@")>0){//邮箱为账号
				newUser.setEmail(account);
			}else{//手机为账号
				newUser.setMovePhone(account);
			}
			// 添加人员
			userId = userInfoDao.add(newUser);

		} else {
			userId = obj.getId();
			//注册人员名称
			userName = obj.getUserName();
			//密码
			passwd = obj.getPassword();
		}
		// 人员添加日志
		systemLogService.addSystemLog(userId, userName,
				"激活用户信息", ConstantInterface.TYPE_USER,
				joinTemp.getComId(),optIP);
		
		// 用户与企业的关系
		UserOrganic userOrganic = userInfoDao.getUserOrganic(userId, joinTemp.getComId());
		if(null!=userOrganic){
			userOrganic.setEnabled("1");
			userInfoDao.update(userOrganic);
		}else{
			userOrganic = new UserOrganic();
			// 企业编号
			userOrganic.setComId(joinTemp.getComId());
			// 用户主键
			userOrganic.setUserId(userId);
			
			userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);
			
			// 取得默认的部门信息
			Integer depId= 0;
			if(null != joinRecord && null != joinRecord.getUserId()){
				UserInfo invUser = userInfoDao.getUserBaseInfo(joinRecord.getComId(), joinRecord.getUserId());
				if(null != invUser){
					depId = invUser.getDepId();
				}else{
					depId =  departmentService.getDefalutDep(joinTemp.getComId()).getId();
				}
			}else{
				depId =  departmentService.getDefalutDep(joinTemp.getComId()).getId();
			}
			
			// 部门主键
			userOrganic.setDepId(depId);
			// 管理员
			userOrganic.setAdmin("0");
			// 用户激活
			userOrganic.setEnabled("1");
			// 设置默认积分
			userOrganic.setJifenScore(0);
			// 是否完善信息 默认不需要
			userOrganic.setAlterInfo("1");
			//设置用户的服务启动
			userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);
			
			userInfoDao.add(userOrganic);
		}
		
		// 添加积分的历史记录
		jifenService.addJifen(joinTemp.getComId(), joinRecord.getUserId(),
				ConstantInterface.TYPE_USERINV, "成功邀请到同事加入企业",
				joinRecord.getId());
		
		UserInfo userInfo = this.userAuth(account.toLowerCase(), passwd,joinRecord.getComId()+"");;
		return userInfo;
	}

	/**
	 * 检测帐号是否注册
	 * @param account
	 * @return
	 */
	public UserInfo checkInputAccount(String account) {
		return userInfoDao.checkInputAccount(account);
	}

	/**
	 * 更新客户帐号信息
	 * @param noticeType 帐号分类
	 * @param account 帐号
	 * @param curUser 当前操作人
	 */
	public void updateUserAccount(String noticeType, String account,UserInfo curUser) {
		//删除用户临时表的数据
		userInfoDao.delByField("joinrecord", new String[]{"comId","account"}, new Object[]{curUser.getComId(),account.toLowerCase()});
		userInfoDao.delByField("jointemp", new String[]{"comId","account"}, new Object[]{curUser.getComId(),account.toLowerCase()});
		
		if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
			curUser.setMovePhone(account);
			userInfoDao.updateUserMovePhone(curUser);
		}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
			curUser.setEmail(account);
			userInfoDao.updateUserEmail(curUser);
		}
	}

	/**
	 * 更新个人信息
	 * @param attrType 所需更新的用户属性类型
	 * @param userInfo 个人信息
	 */
	public boolean updateUserAttrs(String attrType, UserInfo userInfo) {
		if(StringUtil.isBlank(StringUtil.delNull(attrType)) || null==userInfo){return false;}
		boolean isSucc = false;//执行结果标识，默认失败。
		//系统日志内容
		StringBuffer content = new StringBuffer();
		
		if ("nickname".equals(attrType)) {
			if(StringUtil.isBlank(StringUtil.delNull(userInfo.getNickname()))){
				userInfo.setNickname(null);//清空
				content.append("清空登录别名。");
			}else{
				content.append("修改登录别名为："+userInfo.getNickname());
			}
			userInfoDao.updateUserNickname(userInfo);// 修改登录别名
		}else if ("userName".equals(attrType)) {
			if(!StringUtil.isBlank(StringUtil.delNull(userInfo.getUserName()))){
				userInfo.setAllSpelling(PinyinToolkit.cn2Spell(userInfo
						.getUserName()));
				userInfo.setFirstSpelling(PinyinToolkit.cn2FirstSpell(userInfo
						.getUserName()));
				content.append("修改姓名为："+userInfo.getUserName());
			}else{
				userInfo.setUserName(null);//清空
				content.append("清空姓名。");
			}
			userInfoDao.updateUserName(userInfo);//姓名更新
		}else if ("gender".equals(attrType) && !StringUtil.isBlank(StringUtil.delNull(userInfo.getGender()))) {
			userInfoDao.updateUserGender(userInfo);// 修改用户性别
			content.append("修改性别为："+("0".equals(userInfo.getGender())?"女":"男"));
		}else if ("birthday".equals(attrType)) {
			if(StringUtil.isBlank(StringUtil.delNull(userInfo.getBirthday()))){
				userInfo.setBirthday(null);//清空
				content.append("清空生日。");
			}else{
				content.append("修改生日为："+userInfo.getBirthday());
			}
			userInfoDao.updateUserBirthday(userInfo);// 修改用户生日
		}else if ("job".equals(attrType)) {
			if(StringUtil.isBlank(StringUtil.delNull(userInfo.getJob()))){
				userInfo.setJob(null);//清空
				content.append("清空职位。");
			}else{
				content.append("修改职位为："+userInfo.getJob());
			}
			userInfoDao.updateUserOrganic(userInfo.getComId(),userInfo.getId(), userInfo.getJob());// 职位变动
		}else if ("qq".equals(attrType)) {
			if(StringUtil.isBlank(StringUtil.delNull(userInfo.getQq()))){
				userInfo.setQq(null);//清空
				content.append("清空QQ号。");
			}else{
				content.append("修改QQ为："+userInfo.getQq());
			}
			userInfoDao.updateUserQq(userInfo);// 修改用户QQ
		}else if ("linePhone".equals(attrType)) {
			if(StringUtil.isBlank(StringUtil.delNull(userInfo.getLinePhone()))){
				userInfo.setLinePhone(null);//清空
				content.append("清空座机号。");
			}else{
				content.append("修改座机号为："+userInfo.getLinePhone());
			}
			userInfoDao.updateUserLinePhone(userInfo);// 修改用户座机号
		}else if ("wechat".equals(attrType)) {
			if(StringUtil.isBlank(StringUtil.delNull(userInfo.getWechat()))){
				userInfo.setWechat(null);//清空
				content.append("清空微信号。");
			}else{
				content.append("修改微信号为："+userInfo.getWechat());
			}
			userInfoDao.updateUserWechat(userInfo);// 修改用户微信号
		}else if ("hireDate".equals(attrType)) {
			if(StringUtil.isBlank(StringUtil.delNull(userInfo.getHireDate()))){
				userInfo.setHireDate(null);//清空
				content.append("清空入职时间。");
			}else{
				content.append("修改入职时间为："+userInfo.getHireDate());
			}
			userInfoDao.updateUserHireDate(userInfo.getComId(),userInfo.getId(),userInfo.getHireDate());// 修改用户入职时间
		}
		//积分添加
		if (null != userInfo.getUserName()// 姓名不为空
				&& null != userInfo.getGender()// 性别不为空
				&& null != userInfo.getNickname()// 绰号不为空
				&& null != userInfo.getJob()// 职位不为空
				&& null != userInfo.getBirthday()) {// 生日不为空

			// 添加积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(),
					ConstantInterface.TYPE_USERINFO, "个人资料，完善基本信息", 0);
		} else if (null != userInfo.getQq()// QQ不为空
				&& null != userInfo.getLinePhone()// 固定电话不为空
				&& null != userInfo.getMovePhone()// 移动电话不为空
				&& null != userInfo.getWechat()// 微信不为空
		) {
			// 添加积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(),
					ConstantInterface.TYPE_USERTEL, "个人资料，完善联系信息", 0);
		}
		userInfo = this.getUserBaseInfo(userInfo.getComId(),userInfo.getId());//获取个人信息
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),content.toString(),ConstantInterface.TYPE_USER, 
				userInfo.getComId(),userInfo.getOptIP());
		isSucc = true;//最后标识为成功
		return isSucc;
	}
	
	/**
	 * 验证当前操作人是否是超级管理员
	 * @param curUser
	 * @return
	 */
	public boolean isAdministrator(UserInfo curUser) {
		UserInfo administrator = userInfoDao.isAdministrator(curUser);
		if(null==administrator){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 验证当前操作人是否是管理员
	 * @param curUser
	 * @return
	 */
	public boolean isAdmin(UserInfo curUser) {
		UserInfo administrator = userInfoDao.isAdmin(curUser);
		if(null==administrator){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 获取直属上级信息
	 * @param userInfo 当前操作人
	 * @return
	 */
	public UserInfo queryDirectLeaderInfo(UserInfo userInfo) {
		return userInfoDao.queryDirectLeaderInfo(userInfo);
	}
	/**
	 * 获取模块负责人和留言父用户
	 * @param comId 企业主键
	 * @param modId 业务主键
	 * @param busType 业务类别
	 * @param talkParentId  父用户讨论信息主键
	 * @param pushUserIdSet 
	 * @return
	 */
	public List<UserInfo> listOwnerForMsg(Integer comId,Integer modId,Integer talkParentId,String busType, Set<Integer> pushUserIdSet){
		return userInfoDao.listOwnerForMsg(comId, modId, talkParentId, busType,pushUserIdSet);
	}

	/**
	 * 用户加入的团队个数
	 * @param userId
	 * @return
	 */
	public Integer countUserIn(Integer userId) {
		return userInfoDao.countUserIn(userId);
	}

	/**
	 * 邀请人员加入系统
	 * @param sessionUser 当前操作人员
	 * @param invtWay 邀请方式
	 * @param comId 团队号
	 * @param accounts 账号集合
	 * @param url 邀请链接
	 * @param basePath 邀请链接
	 */
	public void addInviteUser(UserInfo sessionUser, String invtWay, Integer comId, Set<String> accounts,
			String url, String basePath) {
		//通过邮箱邀请
		if(invtWay.equals(ConstantInterface.GET_BY_EMAIL)){
			//邮件发送准备
			MessageSender sender = MessageSender.getMessageSender();
			for (String inviteEmail : accounts) {
				//账号
				String account = inviteEmail.toLowerCase();
				//取得用户是否为系统人员
				UserInfo userinfo = this.getUserInfoByType(account,comId,ConstantInterface.GET_BY_EMAIL);
				if(null!=userinfo){//已经是系统人员
					continue;
				}
				invtUserByEmail(account,comId,basePath,sender,sessionUser);
			}
			
		}else if(invtWay.equals(ConstantInterface.GET_BY_PHONE)){//通过手机邀请
			for (String invitePhone : accounts) {
				//账号
				String account = invitePhone.toLowerCase();
				//取得用户是否为系统人员
				UserInfo userinfo = this.getUserInfoByType(account,comId,ConstantInterface.GET_BY_PHONE);
				if(null!=userinfo){//已经是系统人员
					continue;
				}
				invtUserByPhone(account,comId,sessionUser);
				
			}
		}
		
	}
	
	/**
	 * 通过手机号邀请人员
	 * @param account 账号
	 * @param comId 团队号
	 * @param sessionUser 当前操作人员
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void invtUserByPhone(String account, Integer comId,
			UserInfo sessionUser) {
		/**
		 * 第一步：判断是否邀请过（手机和邮箱分开），没有邀请过则添加邀请记录（设置密码，和名字），否则统一密码
		 */
		//判断用户是否被邀请过
		JoinTemp joinTemp = registService.getJoInTempByAccount(account, comId,ConstantInterface.JOIN_INVITE);
		
		//取得用户已有的的密码信息
		UserInfo userinfo =  this.getUserInfoByAccount(account);
		//明文六位密码
		String passwd = PassWordCreateUtil.getRandomS().toLowerCase(); 
		//加密后的
		String passwdMd5 = Encodes.encodeMd5(passwd);
		
		String userName = "普通系统用户";
		if(null != userinfo){//重新设置密码
			passwdMd5 = userinfo.getPassword();
			userName = userinfo.getUserName();
		}
		//没有被邀请过
		if(null == joinTemp){//设置名称
			registService.addJoinTempByType(comId,account, userName, passwdMd5,ConstantInterface.JOIN_INVITE);
		}else{//重置密码
			joinTemp.setPasswd(passwdMd5);
			registService.updateJoinTempByType(joinTemp);
		}
		/**
		 * 第二步：若有邀请码，则统一邀请码，否则设置邀请码
		 **/
		//邀请码
		String confirmId = new Random().nextInt(899999)+100000+"";
		if(null != userinfo){//是老用户
			//邮箱账号
			String email = userinfo.getEmail();
			//手机账号
			String movePhone = userinfo.getMovePhone();
			if(!StringUtil.isBlank(email) && !StringUtil.isBlank(movePhone)){//可能两种方式都邀请过
				List<JoinRecord> joinRecords = registService.getJoInRecordByDouAccount(email, movePhone, comId, ConstantInterface.JOIN_INVITE);
				if(null != joinRecords && !joinRecords.isEmpty()){//统一邀请码
					for (JoinRecord joinRecord : joinRecords) {
						
						joinRecord.setConfirmId(confirmId);
						//邀请人员
						joinRecord.setUserId(sessionUser.getId());
						
						registService.updateJoinRecord(joinRecord,null);
					}
				}else{//取用生成的邀请码
					JoinRecord joinRecord  = new JoinRecord();
					//默认 0待激活
					joinRecord = new JoinRecord(comId,account,ConstantInterface.JOIN_INVITE + "","0",confirmId);
					//默认审核通过
					joinRecord.setCheckState("1");
					//邀请人员
					joinRecord.setUserId(sessionUser.getId());
					registService.addJoinRecord(joinRecord);
				}
			}else if(!StringUtil.isBlank(email)){
				//添加加入记录
				addJoinRecord(email, comId, sessionUser, confirmId);
			}else if(!StringUtil.isBlank(movePhone)){
				//添加加入记录
				addJoinRecord(movePhone, comId, sessionUser, confirmId);
			}
			
		}else{//是新用户
			addJoinRecord(account, comId, sessionUser, confirmId);
		}
		/**
		 * 第三步：若是老用户，则发送邀请码，否则发送邀请码和密码
		 **/
		
		if(null!=userinfo){//不需要密码
			List<Object> args = new ArrayList<Object>();
			args.add(sessionUser.getOrgName());
			args.add(confirmId);
			args.add(account);
			phoneMsgService.sendMsg(account, args.toArray(),// 通过手机发送验证码
					ConstantInterface.MSG_REGEIST_INVITE_PN,
					comId,sessionUser.getOptIP());
		}else{//需要密码
			List<Object> args = new ArrayList<Object>();
			args.add(sessionUser.getOrgName());
			args.add(confirmId);
			args.add(account);
			args.add(passwd);
			phoneMsgService.sendMsg(account, args.toArray(),// 通过手机发送验证码
					ConstantInterface.MSG_REGEIST_INVITE_PY,
					comId,sessionUser.getOptIP());
		}
		
	}

	/**
	 * 通过邮件邀请用户
	 * @param account 账号
	 * @param comId 团队号
	 * @param basePath 链接地址
	 * @param sender 邮件发送器
	 * @param sessionUser 当前操作人员
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void invtUserByEmail(String account, Integer comId, String basePath, MessageSender sender, UserInfo sessionUser) {
		/**
		 * 第一步：判断是否邀请过（手机和邮箱分开），没有邀请过则添加邀请记录（设置密码，和名字），否则统一密码
		 */
		//判断用户是否被邀请过
		JoinTemp joinTemp = registService.getJoInTempByAccount(account, comId,ConstantInterface.JOIN_INVITE);
		
		//取得用户已有的的密码信息
		UserInfo userinfo =  this.getUserInfoByAccount(account);
		//明文六位密码
		String passwd = PassWordCreateUtil.getRandomS().toLowerCase(); 
		//加密后的
		String passwdMd5 = Encodes.encodeMd5(passwd);
		
		String userName = "普通系统用户";
		if(null != userinfo){//重新设置密码
			passwdMd5 = userinfo.getPassword();
			userName = userinfo.getUserName();
		}
		//没有被邀请过
		if(null == joinTemp){
			registService.addJoinTempByType(comId,account,userName , passwdMd5,ConstantInterface.JOIN_INVITE);
		}else{
			joinTemp.setPasswd(passwdMd5);
			registService.updateJoinTempByType(joinTemp);
		}
		
		/**
		 * 第二步：若有邀请码，则统一邀请码，否则设置邀请码
		 **/
		//邀请码
		String confirmId = new Random().nextInt(899999)+100000+"";
		if(null != userinfo){//是老用户
			//邮箱账号
			String email = userinfo.getEmail();
			//手机账号
			String movePhone = userinfo.getMovePhone();
			if(!StringUtil.isBlank(email) && !StringUtil.isBlank(movePhone)){//可能两种方式都邀请过
				List<JoinRecord> joinRecords = registService.getJoInRecordByDouAccount(email, movePhone, comId, ConstantInterface.JOIN_INVITE);
				if(null != joinRecords && !joinRecords.isEmpty()){//统一邀请码
					for (JoinRecord joinRecord : joinRecords) {
						
						joinRecord.setConfirmId(confirmId);
						//邀请人员
						joinRecord.setUserId(sessionUser.getId());
						
						registService.updateJoinRecord(joinRecord,null);
					}
				}else{//取用生成的邀请码
					
					JoinRecord joinRecord  = new JoinRecord();
					//默认 0待激活
					joinRecord = new JoinRecord(comId,account,ConstantInterface.JOIN_INVITE + "","0",confirmId);
					//默认审核通过
					joinRecord.setCheckState("1");
					//邀请人员
					joinRecord.setUserId(sessionUser.getId());
					registService.addJoinRecord(joinRecord);
				}
			}else if(!StringUtil.isBlank(email)){
				//添加加入记录
				addJoinRecord(email, comId, sessionUser, confirmId);
			}else if(!StringUtil.isBlank(movePhone)){
				//添加加入记录
				addJoinRecord(movePhone, comId, sessionUser, confirmId);
			}
			
		}else{//是新用户
			addJoinRecord(account, comId, sessionUser, confirmId);
		}
		
		/**
		 * 第三步：若是老用户，则发送邀请码，否则发送邀请码和密码
		 */
		//标题
		String subject = "企业"+sessionUser.getOrgName() +"邀请您加入"; 
		//内容
		String body = "<div>"+account+"您好！<div><br>"
		+"企业"+sessionUser.getOrgName()+"邀请您加入<br/>"
		+"您可以点击以下链接激活邮箱<br/>" 
		+ "<a href = "+basePath+"/web/login>"+basePath+"/web/login</a><br/>" 
		+ "</a></br>" 
		+ "激活码："+confirmId+"</br>"
		+ "账号："+account+"</br>";
		if(null == userinfo){//新用户
			body +="初始密码："+passwd;
		}else{
			body +="密码为已有的系统密码";
		}
		
		//发送邮件
        new Thread(new MailSendThread(sender, account, subject, body)).start();
		
	}

	/**
	 * 添加邀请记录
	 * @param account 账号
	 * @param comId 团队号
	 * @param sessionUser 当前操作人员
	 * @param confirmId 邀请码
	 */
	private void addJoinRecord(String account, Integer comId,
			UserInfo sessionUser, String confirmId) {
		//判断用户是否被邀请过
		JoinRecord joinRecord = registService.getJoInRecordByAccount(account, comId,ConstantInterface.JOIN_INVITE);
		if(null == joinRecord){
			//默认 0待激活
			joinRecord = new JoinRecord(comId,account,ConstantInterface.JOIN_INVITE + "","0",confirmId);
			//默认审核通过
			joinRecord.setCheckState("1");
			//邀请人员
			joinRecord.setUserId(sessionUser.getId());
			registService.addJoinRecord(joinRecord);
		}else{
			confirmId = joinRecord.getConfirmId();
			//邀请人员
			joinRecord.setUserId(sessionUser.getId());
			
			registService.updateJoinRecord(joinRecord,null);
			
		}
	}
	
	/**
	 * 获取团队使用人数上限零界值人员信息（按加入团队时间升序排列）
	 * @param comId 团队主键
	 * @return
	 */
	public UserInfo queryOrgUsersUpperLimitUser(Integer comId){
		return userInfoDao.queryOrgUsersUpperLimitUser(comId);
	}

	/**
	 * 更新团队人员的服务状态
	 * @param userOrgId 人员团队关系主键
	 * @param sessionUser 当前操作人信息
	 * @return
	 */
	public Map<String, Object> updateUserInService(Integer userOrgId,UserInfo sessionUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(null==sessionUser){
			resultMap.put("status", "f");
			resultMap.put("info", "session已失效");
			return resultMap;
		}
		//1、先禁用临界用户
		UserInfo upperLimitUser= this.queryOrgUsersUpperLimitUser(sessionUser.getComId());
		if(null!=upperLimitUser){
			UserOrganic userOrganic = new UserOrganic();
			userOrganic.setUserId(upperLimitUser.getId());
			userOrganic.setComId(sessionUser.getComId());
			userOrganic.setInService(ConstantInterface.USER_INSERVICE_NO);
			userInfoDao
			.update("update userOrganic set inService=:inService where comId=:comId and userId=:userId",
					userOrganic);
			removeSessionUser(sessionUser, upperLimitUser);//从sessionContext中移除用户
			//添加系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "限制用户“"+upperLimitUser.getUserName()+"”登录",
					ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		}
		//2、再启用激活用户
		UserOrganic userOrganic = new UserOrganic(userOrgId,sessionUser.getComId());
		userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);
		userInfoDao.update("update userOrganic set inService=:inService where comId=:comId and id=:id",
				userOrganic);
		//操作日志记录
		UserOrganic inServiceUserOrg = (UserOrganic) userInfoDao.objectQuery(UserOrganic.class, userOrgId);
		UserInfo inServiceUser = this.getUserBaseInfo(sessionUser.getComId(),inServiceUserOrg.getUserId());
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "激活用户“"+inServiceUser.getUserName()+"”登录",
				ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		resultMap.put("status", "y");
		return resultMap;
	}

	/**
	 * 从sessionContext中移除用户
	 * @param curUser 当前操作人
	 * @param removedUser 被移除的用户对象
	 */
	private void removeSessionUser(UserInfo curUser, UserInfo removedUser) {
		//移除登录成员;同userId的用户,登录不同的企业，可以正常操作
		SessionContext.removeSessionUser(removedUser.getId(),curUser.getComId());
		//禁用的人员
		removedUser.setComId(curUser.getComId());
		//获取个人直属上级集合
		List<ImmediateSuper> listPreSup = this.listImmediateSuper(removedUser);
		if(null!=listPreSup && listPreSup.size()>0){//人员有上级
			for (ImmediateSuper immediateSuperss : listPreSup) {
				SessionContext.updateSub(immediateSuperss.getLeader(), curUser.getComId(),-1);
			}
		}
	}

	/**
	 * 检查团队使用空间情况
	 * @param toEnabledUserNum 启用人数
	 * @param comId 团队主键
	 * @return
	 */
	public Organic checkOrgUsersFreeSpace(Integer toEnabledUserNum, Integer comId) {
		Organic organic = organicService.getOrgInfo(comId);
		if(toEnabledUserNum>(organic.getUsersUpperLimit()-organic.getMembers())){
			organic.setCanDo(false);
			organic.setMsg("启动失败！启用人数太多，团队只剩（"+(organic.getUsersUpperLimit()-organic.getMembers())+"）个席位可使用。");
		}else{
			organic.setCanDo(true);
		}
		return organic;
	}
	/**
	 * 更新直属上级
	 * @param userInfo
	 * @param curUser
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateUserLeader(UserInfo userInfo,
			UserInfo curUser) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		//直属上级设置
		List<UserInfo> listLeader = userInfo.getListUserInfo();
		List<Integer> userIds = new ArrayList<Integer>();
		boolean canSave = true;
		if(null!=listLeader){
			//直接上级是否闭环验证
			for(UserInfo leader:listLeader){
				userIds.add(leader.getId());
				if(leader.getId().equals(curUser.getId())){
					canSave = false;
					map.put("isSucc", "no");
					map.put("msg","直属上级设置失败！不能分享给自己，这样没意义！");
				}else if(!this.superClosedLoopCheck(leader.getId(),curUser)){
					UserInfo user = this.getUserInfo(curUser.getComId(),leader.getId());
					canSave = false;
					map.put("isSucc", "no");
					map.put("msg","直属上级设置失败！不能分享给\""+user.getUserName()+"\"；不然分享圈就成闭环了。");
				}
			}
		}
		if(canSave){
			//获取个人直属上级集合
			List<ImmediateSuper> listPreSup = this.listImmediateSuper(curUser);
			boolean succ = this.updateImmediateSuper((Integer[])userIds.toArray(new Integer[userIds.size()]),
					userInfo.getIsChief(),curUser);
			if(succ){
				if(null!=listPreSup && listPreSup.size()>0){//离职人员有上级
					for (ImmediateSuper immediateSuperss : listPreSup) {
						//减少原来上级的一个下级
						SessionContext.updateSub(immediateSuperss.getLeader(), curUser.getComId(),-1);
					}
				}
				//获取个人直属上级集合
				for(Integer userId:userIds){
					//添加一个下级
					SessionContext.updateSub(userId, curUser.getComId(),1);
				}
				
				this.updateMyLeaders(curUser);//更新个人上级领导表myLeaders
				map.put("isSucc", "yes");
				map.put("msg", " 个人直属上级更新成功！");
			}else{
				map.put("isSucc", "no");
				map.put("msg", "个人直属上级更新失败");
			}
		}
		return map;
	}
	
	/**
	 * 更新个人上级领导表
	 * @param curUser 当前操作人信息
	 */
	private void updateMyLeaders(UserInfo curUser) {
		userInfoDao.delByField("myLeaders", new String[]{"comId","creator"}, new Object[]{curUser.getComId(),curUser.getId()});
		List<ImmediateSuper> listImmediateSuper = userInfoDao.listLeadersFromImmediateSuper(curUser);
		if(!CommonUtil.isNull(listImmediateSuper)) {
			for (ImmediateSuper leader : listImmediateSuper) {
				MyLeaders myLeader = new MyLeaders();
				myLeader.setComId(curUser.getComId());
				myLeader.setCreator(curUser.getId());
				myLeader.setLeader(leader.getLeader());
				userInfoDao.add(myLeader);
			}
		}
	}

	/**
	 * 改变用户的编号
	 * @param sessionUser
	 * @param userOrganic
	 */
	public void updateEnrollNumber(UserInfo sessionUser, UserInfo userOrganic) {
		userOrganic.setComId(sessionUser.getComId());
		UserInfo newUser = (UserInfo) userInfoDao.objectQuery(UserInfo.class, userOrganic.getId());
		userInfoDao.update("update userOrganic set enrollNumber=:enrollNumber where comId=:comId and userId=:id",
				userOrganic);
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "更改用户“"+newUser.getUserName()+"”的编号为：‘"+userOrganic.getEnrollNumber()+"’",
				ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
	}
	/**
	 * 获取在职人员
	 * @param userInfo
	 * @param listUserInfo 人员筛选条件
	 * @param listDep 部门筛选条件
	 * @param isForceIn
	 * @return
	 */
	public List<UserInfo> listUserByAttence(UserInfo userInfo,List<UserInfo> listUserInfo,List<Department> listDep,boolean isForceIn){
		return userInfoDao.listUserByAttence(userInfo,listUserInfo,listDep,isForceIn);
	}
	/**
	 * 通过编号查询用户
	 * @param curUser
	 * @param enrollNumber
	 * @return
	 */
	public UserOrganic queryUserByEnrollNumber(UserInfo curUser, String enrollNumber) {
		return userInfoDao.queryUserByEnrollNumber(curUser,enrollNumber);
	}
	/**
	 * 取得在职用户信息
	 * @param comId
	 * @return
	 */
	public List<UserInfo> listAllEnabledUser(UserInfo userInfo) {
		return userInfoDao.listAllEnabledUser(userInfo);
	}
	/**
	 * 公告范围合并人员
	 * @param comId
	 * @param announId
	 * @return
	 */
	public List<UserInfo> listAnnounScopeUser(Integer comId,Integer announId){
		return userInfoDao.listAnnounScopeUser(comId, announId);
	}
	/**
	 * 制度范围合并人员
	 * @param comId
	 * @param announId
	 * @return
	 */
	public List<UserInfo> listInstituScopeUser(Integer comId,Integer instituId){
		return userInfoDao.listInstituScopeUser(comId, instituId);
	}
	/**
	 * 制度可查看人员及查看状态(分页)
	 * @param comId
	 * @param instituId
	 * @return
	 */
	public List<UserInfo> pagedInstituViewUser(Integer comId, Integer instituId,Integer creatorId) {
		return userInfoDao.pagedInstituViewUser(comId, instituId,creatorId);
	}
	/**
	 * 制度可查看人员及查看状态
	 * @param comId
	 * @param instituId
	 * @return
	 */
	public List<UserInfo> listInstituViewUser(Integer comId, Integer instituId,Integer creatorId) {
		return userInfoDao.listInstituViewUser(comId, instituId,creatorId);
	}
	/**
	 * 制度所有可查看人员及查看状态
	 * @param comId
	 * @param instituId
	 * @return
	 */
	public List<UserInfo> listAllEnabledRead(Integer comId, Integer instituId) {
		return userInfoDao.listAllEnabledRead(comId, instituId);
	}
	/**
	 * 制度已查看人员
	 * @param comId
	 * @param instituId
	 * @return
	 */
	public Integer countInstituRead(Integer comId, Integer instituId,Integer creatorId) {
		return userInfoDao.countInstituRead(comId, instituId, creatorId);
	}
	/**
	 * 取得在职用户阅读人员
	 * @param comId
	 * @return
	 */
	public Integer countAllEnabledRead(Integer comId,Integer instituId) {
		return userInfoDao.countAllEnabledRead(comId,instituId);
	}

	/**
	 * 是否有权限
	 * @param curUser
	 * @param userId
	 * @return
	 */
	public boolean authorCheck(UserInfo userInfo,Integer userId,boolean isForceIn){
		if(null != userInfoDao.authorCheck(userInfo, userId, isForceIn)){
			return true;
		}
		return false;
	}
	
	/**
	 * 添加或修改替岗人员
	 * @param sessionUser
	 * @param forMeDoId
	 */
	public Map<String,Object> updateForMeDo(UserInfo sessionUser,Integer forMeDoId){
		Map<String,Object> map = new HashMap<String,Object>();
		//查询自己是否离岗
//		Integer selfInsteadNum = userInfoDao.queryInsteadNum(sessionUser.getComId(),sessionUser.getId());
		//查询替岗人员是否离岗
		Integer insNum = userInfoDao.queryInsteadNum(sessionUser.getComId(),forMeDoId);
		if(insNum.equals(0)){//替岗人员没有离岗
			//删除当前人员原有的替岗人员
			userInfoDao.delByField("forMeDo", new String[]{"comId","creator"}, 
					new Object[]{sessionUser.getComId(),sessionUser.getId()});
			//添加替岗人员
			ForMeDo forMeDo = new ForMeDo();
			forMeDo.setComId(sessionUser.getComId());
			forMeDo.setUserId(forMeDoId);
			forMeDo.setCreator(sessionUser.getId());
			userInfoDao.add(forMeDo);
			map.put("status", "y");
			UserInfo forMeUser = this.getUserInfo(sessionUser.getComId(),forMeDoId);
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(),"设置工作代理人为“"+forMeUser.getUserName()+"”",
					ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
		}else{//替岗人员离岗了
			map.put("status", "f");
			map.put("info", "代理人员处于离岗状态，不能代理工作");
		}
		return map;
	}
	
	/**
	 * 删除替岗人员
	 * @param sessionUser
	 */
	public void delForMeDo(UserInfo sessionUser){
		userInfoDao.delByField("forMeDo", new String[]{"comId","creator"}, 
				new Object[]{sessionUser.getComId(),sessionUser.getId()});
		//系统日志
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(),"取消了工作代理",
				ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
	}
	
	/**
	 * 取得当前代理人员的代理对象集合
	 * @param sessionUser
	 * @return
	 */
	public List<ForMeDo> listForMeDo(UserInfo sessionUser){
		return userInfoDao.listForMeDo(sessionUser.getComId(),sessionUser.getId());
	}
	
	/**
	 * 查询代理人员
	 * @param comId 组织号
	 * @param userId 离岗人员主键
	 * @return
	 */
	public ForMeDo queryForMeDo(Integer comId,Integer userId){
		return userInfoDao.queryForMeDo(comId,userId);
	}
	
	/**
	 * 迭代查询工作代理人
	 * @param comId 组织号
	 * @param userId 离岗人员主键
	 * @return
	 */
	public ForMeDo queryFnialForMeDo(Integer comId,Integer userId){
		ForMeDo forMeDo = userInfoDao.queryForMeDo(comId,userId);
		if(!CommonUtil.isNull(forMeDo)) {
			ForMeDo forMeDo2 = userInfoDao.queryForMeDo(comId,forMeDo.getUserId());
			if(CommonUtil.isNull(forMeDo2)) {
				return forMeDo;
			}else {
				return this.queryFnialForMeDo(comId, forMeDo.getUserId());
			}
		}
		return null;
	}

	/**
	 * 查询团队的所有离岗人员
	 * @param comId
	 * @return
	 */
	public List<ForMeDo> listForMeDo(Integer comId) {
		return userInfoDao.listForMeDo(comId);
	}

	/**
	 * 是否为领导
	 * @param userInfo
	 * @return
	 */
	public Map<String, Object> checkTopLeaderState(UserInfo userInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserOrganic userOrganic = userInfoDao.checkTopLeaderState(userInfo);
		if(null!=userOrganic.getIsChief() && "1".equals(userOrganic.getIsChief())){
			map.put("topLeaderState", "y");
		}else{
			map.put("topLeaderState", "f");
		}
		return map;
	}

	/**
	 * 查询指定范围的人员用于推送消息
	 * @param comId
	 * @param sharerIds
	 * @return
	 */
	public List<UserInfo> listScopeUserForMsg(Integer comId,
			List<Integer> sharerIds) {
		return userInfoDao.listScopeUserForMsg(comId, sharerIds);
	}

	/**
	 * 查询部门人员
	 * @param depId 部门主键
	 * @param comId 团队号
	 * @return
	 */
	public List<UserInfo> listDepUser(Integer depId, Integer comId) {
		return userInfoDao.listDepUser(depId,comId);
	}

	/**
	 * 查询部门
	 * @param depId部门主键
	 * @param comId 团队号
	 * @return
	 */
	public List<UserInfo> listEnabledUser(Integer depId, Integer comId) {
		return userInfoDao.listEnabledUser(depId, comId);
	}

	/**
	 * 查询人员所有的下属
	 * @param userInfo
	 * @return
	 */
	public List<UserInfo> listUserAllSub(UserInfo userInfo) {
		return userInfoDao.listUserAllSub(userInfo);
	}

	/**
	 * 初始化团队人员关系
	 * @param comId
	 */
	public void initUserTree(Integer comId) {
		//删除人员数树
		userInfoDao.delByField("myLeaders", new String[]{"comId"}, new Object[]{comId});
		//初始化人员树
		userInfoDao.initUserTree(comId);
	}

	/**
	 * 更新userOrganic表
	 * @param userOrganic
	 */
	public void updateUserOrganic(UserOrganic userOrganic){
		userInfoDao.update(userOrganic);
	}
}
