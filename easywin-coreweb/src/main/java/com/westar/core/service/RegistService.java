package com.westar.core.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.Department;
import com.westar.base.model.FileClassify;
import com.westar.base.model.JoinRecord;
import com.westar.base.model.JoinTemp;
import com.westar.base.model.Organic;
import com.westar.base.model.PassYzm;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserInfo;
import com.westar.base.model.UserOrganic;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.JpushUtils;
import com.westar.base.util.MessageSender;
import com.westar.base.util.PinyinToolkit;
import com.westar.base.util.StringUtil;
import com.westar.base.util.UUIDGenerator;
import com.westar.core.dao.RegistDao;
import com.westar.core.thread.MailSendThread;

@Service
public class RegistService {
	@Autowired
	RegistDao registDao;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	PhoneMsgService phoneMsgService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	OrganicService organicService;
	
	@Autowired
	BgypFlService bgypFlService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	
	private static final Log log = LogFactory.getLog(RegistService.class);
	
	/**
	 * 邮箱验证码验重
	 * @param passYzm
	 * @return
	 */
	public boolean passYzmCheck(Integer passYzm) {
		return registDao.passYzmCheck(passYzm);
	}
	
	/**
	 * 判断邮箱验证码是否过期
	 * @param account
	 * @return
	 */
	public PassYzm getPassYzm(String account) {
		PassYzm passYzm = registDao.getPassYzm(account);
		return passYzm;
	}
	
	/**
	 * 根据关键字删除验证码
	 * @param account
	 */
	public void delPassYzm(String account){
		registDao.delByField("passYzm", new String[]{"account"}, new Object[]{account});//先根据关键字删除验证码
	}
	/**
	 * 添加平临时表
	 * @param account 账号
	 * @param userName 用户名称
	 * @param passwordMD5 密码
	 * @param isSysUser 是否为系统用户
	 */
	public Integer addJoinTemp(String account, String userName, String passwordMD5, boolean isSysUser) {
		//加入的临时表
		JoinTemp joinTemp = new JoinTemp();
		//设置密码
		joinTemp.setPasswd(passwordMD5);
		//设置账号
		joinTemp.setAccount(account);
		//设置用户名
		joinTemp.setUserName(userName);
		//默认类型为未操作
		joinTemp.setJoinType(-1);

		//临时表主键
		Integer joinTempId = 0;
		//查询临时表中的
		JoinTemp joinObj = registDao.findInvalidUser(account);
		if(null!=joinObj){//有脏数据
			joinTempId = joinObj.getId();
			joinTemp.setId(joinTempId);
			registDao.update(joinTemp);//覆盖数据
			if(!isSysUser){//非系统用户,统一临时表的密码
				registDao.updatePasswd(account,passwordMD5);
			}
		}else{
			joinTempId = registDao.add(joinTemp);
			joinTemp.setId(joinTempId);
		}
		return joinTempId;
	}
	/**
	 * 取得临时表用户信息
	 * @param joinTempId
	 * @return
	 */
	public JoinTemp getJoInTempById(Integer joinTempId) {
		return (JoinTemp) registDao.objectQuery(JoinTemp.class, joinTempId);
	}
	/**
	 * 添加验证码
	 * @param account 账号
	 * @param joinTempId 临时表用户信息主键
	 * @return
	 */
	public Map<String, Object> addRegistYzm(String account, Integer joinTempId,String optIP) {
		Map<String, Object> map = new HashMap<String, Object>();
		JoinTemp joinTemp = (JoinTemp) registDao.objectQuery(JoinTemp.class, joinTempId);
		if(null==joinTemp){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//账号小写
		account = account.toLowerCase();
		
		String accountStr = joinTemp.getAccount().toLowerCase();
		if(!account.equals(accountStr)){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//删除上次的验证码
		registDao.delByField("passYzm", new String[]{"account"}, new Object[]{account});
		map.put("status", "y");
		//重新生成验证码
		PassYzm passYzm = new PassYzm();
		//账号
		passYzm.setAccount(account);
		
		//邮箱验证码
		String yzm = this.passYzm(4);
		passYzm.setPassYzm(yzm);
		//添加邮箱验证码
		userInfoService.addPassYzm(passYzm);
		
		if(account.indexOf("@")>0){//是邮箱注册
			String subject = "[捷成办公]邮箱验证"; // 标题
			String body = "您好！ "+account+"<br>感谢您选择<b>捷成办公</b>您最近申请了邮箱验证，您的验证码是:<br>"
					+ yzm + "<br>" + "20分钟后失效；请及时输入验证。";// 内容
			//邮件发送准备
			MessageSender sender = MessageSender.getMessageSender();
			//发送邮件
			new Thread(new MailSendThread(sender, account, subject, body)).start();
		}else{
			phoneMsgService.sendMsg(account, new Object[]{yzm}, ConstantInterface.MSG_REGISTER_VCODE, -1,optIP);
		}
		return map;
	}
	
	/**
	 * 邮箱验证码
	 * @param length
	 * @return
	 */
	public String passYzm(int length){
		Integer key = new CommonUtil().randomNum(4);
        if(this.passYzmCheck(Integer.parseInt(key.toString()))){
        	return this.passYzm(length);
        }else{
        	return key.toString();
        }
	}
	
	/**
	 * 生成团队号
	 * @param length
	 * @return
	 */
	private Integer orgKey(int length){
		Integer key = new CommonUtil().randomNum(length);
        if(userInfoService.orgKeyCheck(key)){
        	return this.orgKey(length);
        }else{
        	return key;
        }
	}

	/**
	 * 创建团队并设置创建人员
	 * 1、添加人员
	 * 2、注册企业信息
	 * 3、设立部门
	 * 4、建立企业和人员部门关系
	 * @param joinTemp
	 * @param orgName
	 */
	public UserInfo addOrgAndUser(JoinTemp joinTemp, String orgName,String optIP) {
		//注册账号
		String account = joinTemp.getAccount().toLowerCase();
		//注册人员名称
		String userName = joinTemp.getUserName();
		//密码
		String passwd = joinTemp.getPasswd();
		
		/*******************1、添加人员开始*********************************/
		// 先判断是否需要添加用户
		UserInfo obj = userInfoService.getUserInfoByAccount(account);
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
			userId = registDao.add(newUser);

		} else {
			userId = obj.getId();
			
			//注册人员名称
			userName = obj.getUserName();
			//密码
			passwd = obj.getPassword();
		}
		//删除用户临时表的信息
		registDao.delById(JoinTemp.class, joinTemp.getId());
		//修改该账号的其他信息
		registDao.updateJoinTemps(account,userName,passwd);
		
		/*******************1、添加人员结束*********************************/
		/*******************2、注册企业信息开始*********************************/
		//由于是注册的，需这是企业号
		Integer comId = this.orgKey(6);
		//添加企业
		
		// 注册添加企业主键
		Organic organic = new Organic();
		// 公司编号
		organic.setOrgNum(comId);
		organic.setOrgName(orgName);
		// 激活邮箱后启用
		organic.setEnabled("1");
		
		Integer orgId = registDao.add(organic);

		// 企业添加日志
		systemLogService.addSystemLog(userId, joinTemp.getUserName(), "设立企业",
				ConstantInterface.TYPE_ORG, comId,optIP);
		// 添加积分的历史记录
		jifenService.addJifen(comId, userId,
				ConstantInterface.TYPE_ORG, null, orgId);
		// 人员添加日志
		systemLogService.addSystemLog(userId, joinTemp.getUserName(), "激活用户信息",
				ConstantInterface.TYPE_USER, comId,optIP);
		
		FileClassify fileClassify = new FileClassify();
		fileClassify.setComId(comId);
		fileClassify.setIsSys("1");
		fileClassify.setParentId(-1);
		fileClassify.setType(ConstantInterface.TYPE_LEARN);
		fileClassify.setTypeName("公共文件夹");
		fileClassify.setUserId(userId);
		
		/*******************2、注册企业信息结束*********************************/
		/*******************3、设立部门开始*********************************/
		
		UserInfo userForDep = new UserInfo();
		userForDep.setComId(comId);
		userForDep.setUserName(joinTemp.getUserName());
		
		//添加默认部门
		Integer depId = departmentService.addDefaultDep(userId, userForDep, optIP);
		
		/*******************3、设立部门结束*********************************/
		/*******************4、建立企业和人员部门关系开始*********************************/
		
		// 用户与企业的关系
		UserOrganic userOrganic = new UserOrganic();
		// 企业编号
		userOrganic.setComId(comId);
		// 用户主键
		userOrganic.setUserId(userId);
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
		//设置用户在服务中
		userOrganic.setInService(ConstantInterface.USER_INSERVICE_YES);

		registDao.add(userOrganic);
		/*******************4、建立企业和人员部门关系结束*********************************/
		
		/*******************5、初始化办公分类开始*********************************/
		try {
			bgypFlService.initBgypFl(comId);
		} catch (DocumentException e) {
			log.error("文件解析错误");
		} catch (IOException e) {
			log.error("文件读取错误");
		}
		/*******************5、初始化办公分类结束*********************************/
		
		//后登陆
		UserInfo userInfo = userInfoService.userAuth(account, passwd,comId+"");
		return userInfo;

		
	}
	
	/**
	 * 判断用户是否有一个激活码
	 * 
	 * @param userEmail
	 * @param comId
	 * @return
	 */
	public JoinRecord getConfirmId(String userEmail, Integer comId) {
		JoinRecord obj = registDao.getConfirmId(userEmail, comId);
		return obj;
	}

	/**
	 * 申请加入团队或取消加入团队
	 * @param account 账号
	 * @param joinTempId 用户临时表主键
	 * @param comId 
	 * @param applyState
	 * @param joinTempObj 
	 */
	public Map<String,Object> updateJoinTempType(String account,
			Integer comId, Integer applyState, JoinTemp joinTempObj) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(applyState==0){//是申请加入
			
			JoinTemp joinTempA = registDao.getJoInTempByAccount(account,comId,ConstantInterface.JOIN_APPLY);
			if(null != joinTempA){//已申请加入该团队
				map.put("status", "f1");
				map.put("info", "已申请加入该团队");
				return map;
			}
			
			//判断用户是否有一个激活码，有就用原来的，没有就重新生成一个
			JoinRecord  obj =registDao.getConfirmId(account,comId);
			//激活码
			String confirmId = UUIDGenerator.getUUID();
			//取得企业的管理人员
			List<UserInfo> shares = userInfoService.listOrgAdmin(comId);
			UserInfo userInfo = new UserInfo();
			userInfo.setComId(comId);
			userInfo.setId(0);
			
			String joinNote = joinTempObj.getUserName()+"申请加入企业";
			//加入记录主键
			Integer joinRecordId= 0;
			if(null!=obj){//以前就有加入或是邀请记录
				joinRecordId = obj.getId();
				if("1".equals(obj.getJoinType()) && !"0".equals(obj.getCheckState())){//申请加入已审核
					obj.setConfirmId(confirmId);
					obj.setCheckState("0");
					//更新加入记录
					this.updateJoinRecord(obj,null);//修改确认码以及审核状态
					obj = null;
				}else{
					//若是申请加入的或是已经邀请的直接发邮件
					confirmId = obj.getConfirmId();
					obj.setRecordCreateTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					this.updateJoinRecord(obj,null);
				}
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,null, joinRecordId, joinNote, 
						ConstantInterface.TYPE_APPLY, shares, null);
				
			}else{//没有加入记录
				JoinRecord joinRecord = new JoinRecord();
				
				joinRecord.setAccount(account);
				//加入方式
				joinRecord.setJoinType("1");
				//未激活
				joinRecord.setState("0");
				//激活码
				joinRecord.setConfirmId(confirmId);
				joinRecord.setComId(comId);
				joinRecord.setJoinNote(joinNote);
				//未审核
				joinRecord.setCheckState("0");
				joinRecordId =  this.addJoinRecord(joinRecord);
				
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,null, joinRecordId, joinTempObj.getUserName()+"申请加入企业", 
						ConstantInterface.TYPE_APPLY, shares, null);
				
			}
			
			//修改用户临时表中的
			JoinTemp joinTemp = new JoinTemp();
			//申请加入的企业号
			joinTemp.setComId(comId);
			joinTemp.setAccount(account);
			joinTemp.setPasswd(joinTempObj.getPasswd());
			joinTemp.setUserName(joinTempObj.getUserName());
			//设置加入方式为 申请加入
			joinTemp.setJoinType(1);
			//修改数据
			registDao.add(joinTemp);
			
			if(null!=shares && shares.size()>0){
				for (UserInfo user : shares) {
					//取得待办事项主键
					TodayWorks todayWorks = todayWorksService.getUserTodayWork(comId,user.getId(),
							joinRecordId,ConstantInterface.TYPE_APPLY,0);
					if(null!=todayWorks){
						JpushUtils.sendTodoMessage(comId, user.getId(), -1,
								todayWorks.getId(), joinRecordId, ConstantInterface.TYPE_APPLY,0,joinTempObj.getUserName()+"申请加入企业");
					}
				}
			}
		}else if(applyState==1){//取消申请加入
			
			JoinTemp joinTempA = registDao.getJoInTempByAccount(account,comId,ConstantInterface.JOIN_APPLY);
			if(null == joinTempA){//已取消加入该团队
				map.put("status", "f2");
				map.put("info", "已取消加入该团队");
				return map;
			}
			/**
			 * 1 删除已有申请;
			 * 2 修改用户临时表中的数据
			 */
			UserInfo userInfo = new UserInfo();
			userInfo.setComId(comId);
			userInfo.setId(0);
			//判断用户是否有一个激活码，有就用原来的，没有就重新生成一个
			JoinRecord  obj =registDao.getConfirmId(account,comId);
			if(null!=obj){//以前就有加入或是邀请记录
				//更新加入记录
				this.updateJoinRecord(obj,null);//修改确认码以及审核状态
				registDao.delById(JoinRecord.class, obj.getId());
				// 删除待办提醒
				todayWorksService.updateTodoWorkRead(obj.getId(), obj.getComId(), null,
						ConstantInterface.TYPE_APPLY, 0);
			}
			//取消申请加入
			registDao.delByField("joinTemp", new String[]{"comId","account","joinType"}, 
					new Object[]{comId,account,1});
		}
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 使得链接失效
	 * 
	 * @param joinRecord
	 * @param userInfo
	 */
	public void updateJoinRecord(JoinRecord joinRecord, UserInfo userInfo) {
		registDao.update(joinRecord);
		if (null != userInfo) {
			// 删除待办提醒
			todayWorksService
					.updateTodoWorkRead(joinRecord.getId(),
							userInfo.getComId(), null,
							ConstantInterface.TYPE_APPLY, 0);
		}

	}

	/**
	 * 通过账号取得用户信息
	 * @param account 账号
	 * @param comId 申请的企业
	 * @return
	 */
	public JoinTemp getJoInTempByAccount(String account, Integer comId,Integer joinType) {
		return registDao.getJoInTempByAccount(account,comId,joinType);
	}
	/**
	 * 统一临时表中数据
	 * @param account 账号
	 * @param userName 用户名称
	 * @param passwd 密码
	 */
	public void updateJoinTemps(String account, String userName, String passwd) {
		registDao.updateJoinTemps(account, userName, passwd);
		
	}

	/**
	 * 添加邀请记录
	 * @param comId 团队号
	 * @param account 账号
	 * @param userName 用户名称
	 * @param passwdMd5 加密后的密码 
	 * @param joinType 加入类型 1 申请 2邀请加入
	 */
	public void addJoinTempByType(Integer comId, String account, String userName,
			String passwdMd5, Integer joinType) {
		JoinTemp joinTemp = new JoinTemp();
		//设置团队号
		joinTemp.setComId(comId);
		//设置账号
		joinTemp.setAccount(account);
		//设置密码
		joinTemp.setPasswd(passwdMd5);
		//设置用户名称
		joinTemp.setUserName(userName);
		//设置加入放置
		joinTemp.setJoinType(joinType);
		
		registDao.add(joinTemp);
	}

	/**
	 * 修改邀请加入的密码
	 * @param joinTemp
	 */
	public void updateJoinTempByType(JoinTemp joinTemp) {
		registDao.update(joinTemp);
		
	}

	/**
	 * 取得团队的邀请码
	 * @param account 账号
	 * @param comId 团队号
	 * @param joinInvite 加入类型
	 * @return
	 */
	public JoinRecord getJoInRecordByAccount(String account, Integer comId,
			Integer joinType) {
		
		return registDao.getJoInRecordByAccount(account,comId,joinType);
	}
	

	/**
	 * 添加记录用于邮箱确认
	 * 
	 * @param joinRecord
	 */
	public Integer addJoinRecord(JoinRecord joinRecord) {
		return registDao.add(joinRecord);
	}

	/**
	 * 拒绝加入团队
	 * @param account 账号
	 * @param comId 团队号
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void rejectInOrg(String account, Integer comId,String sendMsg) {
		/**
		 * 思路：1、查看系统中是否有用户存在
		 * 2、若是有，则分别取出该用户的邮箱和账号，并删除邀请信息
		 * 3、否则根据账号删除邀请信息
		 */
		UserInfo userInfo = userInfoService.getUserInfoByAccount(account.toLowerCase());
		if(null != userInfo){
			String email = userInfo.getEmail();
			String movePhone = userInfo.getMovePhone();
			
			if(!StringUtil.isBlank(email) && !StringUtil.isBlank(movePhone)){
				//删除加入申请临时表
				registDao.delByField("joinTemp", new String[]{"comId","account","joinType"},
						new Object[]{comId,email.toLowerCase(),ConstantInterface.JOIN_INVITE});
				
				//删除加入申请临时表
				registDao.delByField("joinTemp", new String[]{"comId","account","joinType"},
						new Object[]{comId,movePhone.toLowerCase(),ConstantInterface.JOIN_INVITE});
				
				List<JoinRecord> joinRecords = this.getJoInRecordByDouAccount(email, movePhone, comId, ConstantInterface.JOIN_INVITE);
				if(null!=joinRecords && !joinRecords.isEmpty()){
					JoinRecord joinRecord = joinRecords.get(0);
					if(null != sendMsg){
						//发送拒绝消息
						todayWorksService.sendMsgForOne(comId,ConstantInterface.TYPE_NOTICE,
								joinRecord.getUserId(),0,"("+userInfo.getUserName()+")拒绝加入团队");
					}
					
					for (JoinRecord joinRecordObj : joinRecords) {
						registDao.delById(JoinRecord.class, joinRecordObj.getId());
					}
				}
			}else if(!StringUtil.isBlank(email)){
				//删除加入申请临时表
				registDao.delByField("joinTemp", new String[]{"comId","account","joinType"},
						new Object[]{comId,email.toLowerCase(),ConstantInterface.JOIN_INVITE});
				//判断用户是否被邀请过
				JoinRecord joinRecord = this.getJoInRecordByAccount(email.toLowerCase(), comId,ConstantInterface.JOIN_INVITE);
				if(null != joinRecord){
					if(null != sendMsg){
						//发送拒绝消息
						todayWorksService.sendMsgForOne(comId,ConstantInterface.TYPE_NOTICE,
								joinRecord.getUserId(),0,"("+userInfo.getUserName()+")拒绝加入团队");
					}
					registDao.delById(JoinRecord.class, joinRecord.getId());
				}
				
			}else if(!StringUtil.isBlank(movePhone)){
				//删除加入申请临时表
				registDao.delByField("joinTemp", new String[]{"comId","account","joinType"},
						new Object[]{comId,movePhone.toLowerCase(),ConstantInterface.JOIN_INVITE});
				//判断用户是否被邀请过
				JoinRecord joinRecord = this.getJoInRecordByAccount(movePhone.toLowerCase(), comId,ConstantInterface.JOIN_INVITE);
				if(null != joinRecord){
					if(null != sendMsg){
						//发送拒绝消息
						todayWorksService.sendMsgForOne(comId,ConstantInterface.TYPE_NOTICE,
								joinRecord.getUserId(),0,"("+userInfo.getUserName()+")拒绝加入团队");
					}
					registDao.delById(JoinRecord.class, joinRecord.getId());
				}
			}
			
		}else{
			//删除加入申请临时表
			registDao.delByField("joinTemp", new String[]{"comId","account","joinType"},
					new Object[]{comId,account.toLowerCase(),ConstantInterface.JOIN_INVITE});
			//判断用户是否被邀请过
			JoinRecord joinRecord = this.getJoInRecordByAccount(account.toLowerCase(), comId,ConstantInterface.JOIN_INVITE);
			if(null != joinRecord){
				if(null != sendMsg){
					//发送拒绝消息
					todayWorksService.sendMsgForOne(comId,ConstantInterface.TYPE_NOTICE,
							joinRecord.getUserId(),0,"("+account+")拒绝加入团队");
				}
				registDao.delById(JoinRecord.class, joinRecord.getId());
			}
		}
	}
	/**
	 * 激活加入团队
	 * @param confirmCodeParam 邀请码
	 * @param comIdParam 团队号
	 * @param joinTempId 用户临时数据主键
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> agreeInOrg(String confirmCodeParam, Integer comIdParam,
			Integer joinTempId,JoinTemp joinTemp,String optIP) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(null == joinTemp){
			//取得用户临时数据
			joinTemp = (JoinTemp) registDao.objectQuery(JoinTemp.class, joinTempId);
		}
		/**
		 * 思路：1、若系统中没有该用户信息，则直接激活账号，并删除临时表用户和邀请记录，同时统一临时表用户的名字和密码
		 * 2、系统中有该用户账号，激活账号后统一系统的密码和账号，包括临时用户表
		 * 3、删除团队的申请信息和邀请信息以及代办信息
		 */
		//用户账号
		String account = joinTemp.getAccount().toLowerCase();
		//团队号
		Integer comId = joinTemp.getComId();
		
		if(!comIdParam.equals(comId)){
			map.put("status", "f");
			map.put("info", "太调皮了，乱改团队号");
		}else{
			UserInfo userInfoObj = userInfoService.getUserInfoByAccount(account);
			if(null == userInfoObj){//是新用户
				//查询用户临时表的信息
				JoinRecord joinRecord = registDao.getJoInRecordByAccount(account,comId,ConstantInterface.JOIN_INVITE);
				//激活用户
				this.addInvUser(confirmCodeParam, joinTempId, joinTemp, map,
						joinRecord,optIP);
				if(map.get("status").equals("y")){
					
					//删除用户临时表
					registDao.delById(JoinRecord.class, joinRecord.getId());
					//删除邀请
					registDao.delByField("joinTemp", new String[]{"comId","account","joinType"}, 
							new Object[]{comId,account,ConstantInterface.JOIN_INVITE});
				}
			}else{//是老用户
				//邮箱账号
				String email = userInfoObj.getEmail();
				//手机账号
				String movePhone = userInfoObj.getMovePhone();
				if(!StringUtil.isBlank(email) && !StringUtil.isBlank(movePhone)){//可能两种方式都邀请过
					List<JoinRecord> joinRecords = registDao.getJoInRecordByDouAccount(email, movePhone, comId, ConstantInterface.JOIN_INVITE);
					if(null == joinRecords || joinRecords.isEmpty()){//没有邀请
						map.put("status", "f");
						map.put("info", "邀请码已失效");
						//删除邀请记录
						registDao.delById(JoinTemp.class, joinTempId);
					}else{
						//取得第一个邀请记录
						JoinRecord joinRecord = joinRecords.get(0);
						//激活用户
						this.addInvUser(confirmCodeParam, joinTempId, joinTemp, map,
								joinRecord,optIP);
						if(map.get("status").equals("y")){
							
							//删除临时表数据，在一个团队同一个用户
							for (JoinRecord joinRecordObj : joinRecords) {
								registDao.delById(JoinRecord.class, joinRecordObj.getId());
								
								//删除代办
								registDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
										new Object[]{comId,ConstantInterface.TYPE_APPLY,joinRecordObj.getId()});
							}
							//删除邀请
							registDao.delByField("joinTemp", new String[]{"comId","account","joinType"}, 
									new Object[]{comId,email,ConstantInterface.JOIN_INVITE});
							//删除邀请
							registDao.delByField("joinTemp", new String[]{"comId","account","joinType"}, 
									new Object[]{comId,movePhone,ConstantInterface.JOIN_INVITE});
						}
					}
				}else if(!StringUtil.isBlank(email) ){
					//查询用户临时表的信息
					JoinRecord joinRecord = registDao.getJoInRecordByAccount(email,comId,ConstantInterface.JOIN_INVITE);
					//激活用户
					this.addInvUser(confirmCodeParam, joinTempId, joinTemp, map,
							joinRecord,optIP);
					if(map.get("status").equals("y")){
						
						//删除代办
						registDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
								new Object[]{comId,ConstantInterface.TYPE_APPLY,joinRecord.getId()});
						//删除临时表的信息
						registDao.delById(JoinRecord.class, joinRecord.getId());
						//删除邀请
						registDao.delByField("joinTemp", new String[]{"comId","account","joinType"}, 
								new Object[]{comId,email,ConstantInterface.JOIN_INVITE});
					}
					
				}else if(!StringUtil.isBlank(movePhone)){
					//查询用户临时表的信息
					JoinRecord joinRecord = registDao.getJoInRecordByAccount(movePhone,comId,ConstantInterface.JOIN_INVITE);
					//激活用户
					this.addInvUser(confirmCodeParam, joinTempId, joinTemp, map,
							joinRecord,optIP);
					if(map.get("status").equals("y")){
						//删除代办
						registDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
								new Object[]{comId,ConstantInterface.TYPE_APPLY,joinRecord.getId()});
						//删除临时表的信息
						registDao.delById(JoinRecord.class, joinRecord.getId());
						//删除邀请
						registDao.delByField("joinTemp", new String[]{"comId","account","joinType"}, 
								new Object[]{comId,movePhone,ConstantInterface.JOIN_INVITE});
					}
					
				}
			}
		}
		return map;
	}
	/**
	 * 查询账号的用户临时信息
	 * @param email 邮箱账号
	 * @param movePhone 手机账号
	 * @param comId 团队
	 * @param joinType 加入类型
	 * @return
	 */
	public List<JoinRecord> getJoInRecordByDouAccount(String email,String movePhone,Integer comId,Integer joinType){
		List<JoinRecord> joinRecords = registDao.getJoInRecordByDouAccount(email, movePhone, comId, ConstantInterface.JOIN_INVITE);
		return joinRecords;
	}

	/**
	 * 邀请的用户激活
	 * @param confirmCodeParam 激活码
	 * @param joinTempId 用户临时表主键
	 * @param joinTemp 用户临时表信息
	 * @param map 返回结果的map
	 * @param joinRecord 加入记录
	 */
	private void addInvUser(String confirmCodeParam, Integer joinTempId,
			JoinTemp joinTemp, Map<String, Object> map, JoinRecord joinRecord,String optIP) {
		if(null==joinRecord){
			map.put("status", "f");
			map.put("info", "邀请码已失效");
			registDao.delById(JoinTemp.class, joinTempId);
		}else{
			String confirmCode = joinRecord.getConfirmId();
			if(null== confirmCodeParam || !confirmCodeParam.equals(confirmCode)){
				map.put("status", "f");
				map.put("info", "邀请码输入错误，请重新输入!");
			}else{
				UserInfo userInfo = userInfoService.addInvUser(joinTemp,joinRecord,optIP);
				if(null == userInfo){
					map.put("status", "f");
					map.put("info", "团队人数已达上线，请联系管理员升级人数!");
				}else{
					map.put("status", "y");
					map.put("userInfo", userInfo);
				}
				/****************统一账号的密码和名字***************************/
				//邮箱账号
				String email = userInfo.getEmail();
				//手机号账号
				String movePhone = userInfo.getMovePhone();
				
				//新的用户名
				String userName = userInfo.getUserName();
				//新的密码
				String passwd = userInfo.getPassword();
				
				if(!StringUtil.isBlank(email) && !StringUtil.isBlank(movePhone)){//
					//修改该账号的其他信息
					this.updateJoinTemps(email,userName,passwd);
					//修改该账号的其他信息
					this.updateJoinTemps(movePhone,userName,passwd);
				}else if(!StringUtil.isBlank(email)){
					//修改该账号的其他信息
					this.updateJoinTemps(email,userName,passwd);
				}else if(!StringUtil.isBlank(movePhone)){
					//修改该账号的其他信息
					this.updateJoinTemps(movePhone,userName,passwd);
				}
			}
		}
	}

	/**
	 * 取得临时用户用于登录验证
	 * @param account账号
	 * @return
	 */
	public List<JoinTemp> getJoInTemp4Login(String account) {
		return registDao.getJoInTemp4Login(account);
	}

}
