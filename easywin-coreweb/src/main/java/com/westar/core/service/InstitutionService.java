package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Department;
import com.westar.base.model.InstituScopeByDep;
import com.westar.base.model.InstituScopeByUser;
import com.westar.base.model.InstituType;
import com.westar.base.model.Institution;
import com.westar.base.model.Logs;
import com.westar.base.model.RecycleBin;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.InstitutionDao;
import com.westar.core.thread.IndexUpdateThread;
import com.westar.core.thread.SendMsgThread;
import com.westar.core.thread.UpdateTodoWorksThread;

@Service
public class InstitutionService {

	@Autowired
	InstitutionDao institutionDao;
	
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	LogsService logsService;
	@Autowired
	UserInfoService  userInfoService;
	@Autowired
	TalkService  talkService;
	@Autowired
	FileCenterService fileCenterService;
	@Autowired
	IndexService indexService;
	@Autowired
	TodayWorksService todayWorksService;
	
	
	/**
	 * 分页查询制度
	 * @param institution
	 * @return
	 */
	public List<Institution> listPagedInstitu(Institution institution,UserInfo userInfo){
		List<Institution> list = institutionDao.listPagedInstitu(institution,userInfo);
		return list;
	}
	/**
	 * 获取权限下前N条数据
	 * @param userInfo
	 * @param num
	 * @return
	 */
	public List<Institution> firstNInstitu(UserInfo userInfo,Integer num){
		List<Institution> list = institutionDao.firstNInstitu(userInfo, num);
		return list;
	}
	
	/**
	 * 添加制度
	 * @param institution  制度
	 * @param msgShare 消息分享
	 * @param userInfo 
	 * @param sendWay
	 * @return
	 * @throws Exception
	 */
	public Integer addInstitu(Institution institution,UserInfo userInfo,String[] sendWay) throws Exception{
		//企业号
		institution.setComId(userInfo.getComId());
		//创建人
		institution.setCreator(userInfo.getId());
		//删除标识(正常)
		institution.setDelState(ConstantInterface.MOD_PREDEL_SATATE_NO);
		//阅读次数
		institution.setReadTime(0);
		//制度范围
		if((institution.getListScopeDep()!=null && institution.getListScopeDep().size()>0 )
				|| ( institution.getListScopeUser()!=null && institution.getListScopeUser().size()>0)){
			//设置范围
			institution.setScopeState(1);
		}else{
			//未设置范围
			institution.setScopeState(0);
		}
		//制度基本信息存入
		Integer institutionId = institutionDao.add(institution);
		institution.setId(institutionId);
		
		//存部门范围
		List<InstituScopeByDep> listScopeDep = institution.getListScopeDep();
		if(null != listScopeDep && !listScopeDep.isEmpty()){
			for (InstituScopeByDep institutionScopeByDep : listScopeDep) {
				if(null == institutionScopeByDep.getDepId() || institutionScopeByDep.getDepId() <=0){
					continue;
				}else{
					institutionScopeByDep.setInstituId(institutionId);
					institutionScopeByDep.setComId(userInfo.getComId());
					institutionDao.add(institutionScopeByDep);
				}
			}
		}
		//存人员范围
		List<InstituScopeByUser> listScopeUser = institution.getListScopeUser();
		if(null != listScopeUser && !listScopeUser.isEmpty()){
			for (InstituScopeByUser institutionScopeByUser : listScopeUser) {
				if(null == institutionScopeByUser.getUserId()|| institutionScopeByUser.getUserId() <=0){
					continue;
				}else{
					institutionScopeByUser.setComId(userInfo.getComId());
					institutionScopeByUser.setInstituId(institutionId);
					institutionDao.add(institutionScopeByUser);
				}
			}
		}
		
		//存日志
		logsService.addLogs(userInfo.getComId(),  institutionId, userInfo.getId(),
				userInfo.getUserName(), "创建制度:\""+institution.getTitle()+"\"", ConstantInterface.TYPE_INSTITUTION);
		
		//制度通知范围人
		List<UserInfo> shares = new ArrayList<UserInfo>();
		
		if(institution.getScopeState().equals(0)){
			shares = userInfoService.listAllEnabledUser(userInfo);
		}else{
			shares = userInfoService.listInstituScopeUser(userInfo.getComId(), institutionId);
		}
		Set<String> sendWays = new HashSet<String>();
		for(String send:sendWay){
			sendWays.add(send);
		}
		if(sendWays.contains("todo")){
			//添加待办提醒通知
			updateTodoWorksThread(userInfo, shares, institution.getId(), ConstantInterface.TYPE_INSTITUTION, "创建制度:\""+institution.getTitle()+"\"");
		}else if(sendWays.contains("msg")){
			sendMsg(userInfo, shares, institution.getId(), ConstantInterface.TYPE_INSTITUTION, "创建制度:\""+institution.getTitle()+"\"");
		}
		//添加工作轨迹
		systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_INSTITUTION, 
				institutionId, "创建制度\""+institution.getTitle()+"\"", "创建制度\""+institution.getTitle()+"\"");
		//创建索引
		this.updateInstituIndex(institutionId, userInfo, "add");
		return institutionId;
	}
	/**
	 * 发送普通消息提示线程
	 * @param userInfo 当前操作人
	 * @param shares 接受方
	 * @param busId 业务主键
	 * @param busType 业务类别
	 * @param content 内容
	 */
	private void sendMsg(UserInfo userInfo,List<UserInfo> shares,Integer busId,String busType,String content) {
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		//跟范围人员发送通知消息
		pool.execute(new SendMsgThread(todayWorksService, userInfo, shares, busId, busType, content));
	}
	/**
	 * 发送待办提示线程
	 * @param userInfo 当前操作人
	 * @param shares 接受方
	 * @param busId 业务主键
	 * @param busType 业务类别
	 * @param content 内容
	 */
	private void updateTodoWorksThread(UserInfo userInfo,List<UserInfo> shares,Integer busId,String busType,String content){
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		pool.execute(new UpdateTodoWorksThread(todayWorksService, userInfo, shares, busId, busType, content));
	}
	/**
	 * 更新制度索引
	 * @param InstituId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception 
	 */
	public void updateInstituIndex(Integer institutionId,UserInfo userInfo,String opType) throws Exception{
		//更新制度索引
		Institution institution = (Institution) institutionDao.objectQuery(Institution.class,institutionId);
		if(null==institution){return;}
		StringBuffer attStr = new StringBuffer(institution.getTitle());
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String index_key = userInfo.getComId()+"_"+ConstantInterface.TYPE_INSTITUTION+"_"+institutionId;
		//为制度创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				index_key,userInfo.getComId(),institutionId,ConstantInterface.TYPE_INSTITUTION,
				institution.getTitle(),attStr.toString(),DateTimeUtil.parseDate(institution.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,index_key));
		}
	}
	/**
	 * 更新制度标题
	 * @param institutioncement
	 * @param userInfo
	 * @return
	 */
	public boolean updateInstituTitle(Institution institution,UserInfo userInfo){
		boolean succ = true;
		try {
			//标题变更
			institutionDao.updateTitle(institution);
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),institution.getId(), userInfo.getId(), userInfo.getUserName(), "制度名称变更为\""+institution.getTitle()+"\"",ConstantInterface.TYPE_INSTITUTION);
			delViewRecord(userInfo.getComId(), institution.getId());
			this.updateTodoWork(userInfo, institution.getId(), "制度标题变更为\""+institution.getTitle()+"\"");
	
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}
	/**
	 * 制度说明更新
	 * @param task
	 * @return
	 */
	public boolean updateInstitumRemark(Institution institution,UserInfo userInfo){
		boolean succ = true;
		try {
			//更新制度进度
			institutionDao.updateInstituRemark(institution);
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),institution.getId(), userInfo.getId(), userInfo.getUserName(), "变更制度内容",ConstantInterface.TYPE_INSTITUTION);
			delViewRecord(userInfo.getComId(), institution.getId());
			this.updateTodoWork(userInfo, institution.getId(), "变更制度内容");
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			//logsService.addLogs(userInfo.getComId(),institution.getId(), userInfo.getId(), userInfo.getUserName(), "制度内容变更失败",ConstantInterface.TYPE_INSTITUTION);
		}
		return succ;
	}
	/**
	 * 制度类型更新
	 * @param institution
	 * @param userInfo
	 * @return
	 */
	public boolean updateInstituType(Institution institution,UserInfo userInfo){
		boolean succ = true;
		try {
			//更新制度进度
			institutionDao.update(institution);
			InstituType instituType = (InstituType) institutionDao.objectQuery(InstituType.class,institution.getInstituType());
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),institution.getId(), userInfo.getId(), userInfo.getUserName(), "变更制度类型为：\""+instituType.getTypeName()+"\"",ConstantInterface.TYPE_INSTITUTION);
			delViewRecord(userInfo.getComId(), institution.getId());
			this.updateTodoWork(userInfo, institution.getId(), "变更制度类型为："+instituType.getTypeName());
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}
	/**
	 * 增加阅读次数
	 * @param institution
	 */
	public void updateInstituRead(Integer institutionId){
		institutionDao.updateInstituRead(institutionId);
	}
	/**
	 * 添加制度人员范围
	 * @param institutionId
	 * @param userId
	 * @return
	 */
	public boolean addInstituScopeUser(Integer institutionId,Integer userId,UserInfo userInfo){
		boolean succ = true;
		try{
			this.addBeforeUpdateScope(institutionId,userInfo.getComId());
			
			InstituScopeByUser institutionScopeUser = new InstituScopeByUser();
			institutionScopeUser.setComId(userInfo.getComId());
			institutionScopeUser.setUserId(userId);
			institutionScopeUser.setInstituId(institutionId);
			institutionDao.add(institutionScopeUser);
			
			UserInfo newUser = userInfoService.getUserInfoById(userId.toString());
			
			logsService.addLogs(userInfo.getComId(),institutionId, userInfo.getId(), userInfo.getUserName(), "添加 \""+newUser.getUserName()+"\"至查看范围",ConstantInterface.TYPE_INSTITUTION);
		}catch(Exception e) {
			succ = false ;
		}
			
		return succ;
	}
	/**
	 * 添加制度部门范围
	 * @param institutionId
	 * @param depId
	 * @param userInfo
	 * @return
	 */
	public boolean addInstituScopeDep(Integer institutionId,Integer depId,UserInfo userInfo){
		boolean succ = true;
		try{
			this.addBeforeUpdateScope(institutionId,userInfo.getComId());
			
			InstituScopeByDep institutionScopeDep = new InstituScopeByDep();
			institutionScopeDep.setComId(userInfo.getComId());
			institutionScopeDep.setDepId(depId);
			institutionScopeDep.setInstituId(institutionId);
			institutionDao.add(institutionScopeDep);
			
			Department dep = (Department) institutionDao.objectQuery(Department.class, depId);
			logsService.addLogs(userInfo.getComId(),institutionId, userInfo.getId(), userInfo.getUserName(), "添加\""+dep.getDepName()+"\"下所有人员至查看范围",ConstantInterface.TYPE_INSTITUTION);
		}catch(Exception e) {
			succ = false ;
		}
			
		return succ;
	}
	
	/**
	 * 删除制度人员范围
	 * @param institutionId
	 * @param userId
	 * @return
	 */
	public boolean delInstituScopeUser(Integer institutionId,Integer userId,UserInfo userInfo){
		boolean succ = true;
		try{
			institutionDao.delByField("instituScopeByUser", new String[]{"comId","instituId","userId"},
					new Object[]{userInfo.getComId(),institutionId,userId});
			this.delAfterUpdateScope(institutionId,userInfo.getComId());
			UserInfo newUser = userInfoService.getUserInfoById(userId.toString());
			logsService.addLogs(userInfo.getComId(),institutionId, userInfo.getId(), userInfo.getUserName(), "将人员：\""+newUser.getUserName()+"\"从查看范围中删除",ConstantInterface.TYPE_INSTITUTION);
		}catch(Exception e) {
			succ = false ;
		}
			
		return succ;
	}
	/**
	 * 删除制度部门范围
	 * @param institutionId
	 * @param depId
	 * @param userInfo
	 * @return
	 */
	public boolean delInstituScopeDep(Integer institutionId,Integer depId,UserInfo userInfo){
		boolean succ = true;
		try{
			institutionDao.delByField("instituScopeByDep", new String[]{"comId","instituId","depId"},
					new Object[]{userInfo.getComId(),institutionId,depId});
			 this.delAfterUpdateScope(institutionId,userInfo.getComId());
			 
			Department dep = (Department) institutionDao.objectQuery(Department.class, depId);
			logsService.addLogs(userInfo.getComId(),institutionId, userInfo.getId(), userInfo.getUserName(), "将部门：\""+dep.getDepName()+"\"从查看范围中删除",ConstantInterface.TYPE_INSTITUTION);
		}catch(Exception e) {
			succ = false ;
		}
			
		return succ;
	}
	
	
	/**
	 * 制度查看权限验证
	 * @param comId
	 * @param institutionId
	 * @param userId
	 * @return
	 */
	public boolean authorCheck(Integer comId,Integer institutionId,Integer userId){
		List<Institution> listInstitu = institutionDao.authorCheck(comId,institutionId,userId);
		if(null!=listInstitu && !listInstitu.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 获取制度信息
	 * @param institutionId
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Institution getInstitutionInfo(Integer institutionId, UserInfo userInfo) {
		Institution institution  = institutionDao.getInstitutionInfo(institutionId,userInfo.getId());
		Logs log = logsService.queryLastLog(userInfo.getComId(), institutionId, ConstantInterface.TYPE_INSTITUTION);
		institution.setLastLog(log.getUserName()+"--"+log.getContent());
		return institution;
	}

	/**
	 * 批量删除制度
	 * @param ids
	 * @param sessionUser
	 * @return
	 * @throws Exception 
	 */
	public Boolean delPreInstitution(Integer[] ids, UserInfo userInfo) throws Exception {
		Boolean flag = true;
		if(null!=ids && ids.length>0){
			for (Integer id : ids) {
				
				Institution institutionTemp = (Institution) institutionDao.objectQuery(Institution.class, id);
				if(null==institutionTemp || institutionTemp.getDelState()==1){//不存在或是已经预删除了
					return false;
				}
				
				Institution institution =new Institution();
				//主键
				institution.setId(id);
				//预删除标识
				institution.setDelState(1);
				//修改投票信息
				institutionDao.update(institution);
				
				//删除数据更新记录
				institutionDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_INSTITUTION,id});
				//删除回收箱数据
				institutionDao.delByField("recycleBin", new String[]{"comId","busType","busId","userId"}, 
						new Object[]{userInfo.getComId(),ConstantInterface.TYPE_INSTITUTION,id,userInfo.getId()});
				
				//回收箱
				RecycleBin recyleBin =  new RecycleBin();
				//业务主键
				recyleBin.setBusId(id);
				//业务类型
				recyleBin.setBusType(ConstantInterface.TYPE_INSTITUTION);
				//企业号
				recyleBin.setComId(userInfo.getComId());
				//创建人
				recyleBin.setUserId(userInfo.getId());
				institutionDao.add(recyleBin);
				
				this.delInstitu(id, userInfo);
			}
		}
		
		
		return flag;
		
	}
	/**
	 * 永久删除制度
	 * @param institutionId
	 * @param userInfo
	 */
	public void delInstitu(Integer institutionId,UserInfo userInfo) throws Exception {
		//更新投票索引
		//this.updateVoteIndex(id,sessionUser,"del");
		
		Integer comId = userInfo.getComId();
		
		//删除日志
		logsService.delLogs(comId, institutionId,ConstantInterface.TYPE_INSTITUTION);
		//删除制度人员
		institutionDao.delByField("instituScopeByUser", new String[]{"comId","instituId"}, new Object[]{comId,institutionId});
		//删除制度部门
		institutionDao.delByField("instituScopeByDep", new String[]{"comId","instituId"}, new Object[]{comId,institutionId});

		//删除浏览记录
		institutionDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, new Object[]{comId,institutionId,ConstantInterface.TYPE_INSTITUTION});
		//最新动态
		//institutionDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, new Object[]{comId,id,ConstantInterface.TYPE_VOTE});
		
		Institution institution = institutionDao.getInstitutionInfo(institutionId, userInfo.getId());
		if(null!=institution){
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_INSTITUTION, 
					institutionId, "删除制度\""+institution.getTitle()+"\"", "删除制度\""+institution.getTitle()+"\"");
		}
		//删除制度
		institutionDao.delById(Institution.class, institutionId);
	}
	/********************************制度范围****************************************/
	/**
	 * 获取制度的制度部门范围
	 * @param comId
	 * @param institutionId
	 * @return
	 */
	public List<Department> listInstituScopeDep(Integer comId,Integer institutionId){
		List<Department> list = institutionDao.listInstituScopeDep(comId, institutionId);
		return list;
	}
	
	/**
	 * 获取制度的制度人员范围
	 * @param comId
	 * @param institutionId
	 * @return
	 */
	public List<UserInfo> listInstituScopeUser(Integer comId,Integer institutionId){
		 List<UserInfo> list = institutionDao.listInstituScopeUser(comId, institutionId);
		return list;
	}
	/**
	 * 删除人员和范围后检查是否需要更新制度范围
	 * @param institutionId
	 * @param comId
	 */
	public boolean delAfterUpdateScope(Integer institutionId,Integer comId){
		//删除只有无制度范围 修改制度范围状态
		List<UserInfo> user = this.listInstituScopeUser(comId, institutionId);
		List<Department> dep = this.listInstituScopeDep(comId, institutionId);
		if( (null == dep ||dep.isEmpty()) && (null == user || user.isEmpty()) ){
			institutionDao.updateScopeState(comId, institutionId, 0);
			return true;
		}
		return false;
	}
	/**
	 * 添加人员和范围前检查是否需要更新制度范围
	 * @param institutionId
	 * @param comId
	 */
	public boolean addBeforeUpdateScope(Integer institutionId,Integer comId){
		//删除只有无制度范围 修改制度范围状态
		List<UserInfo> user = this.listInstituScopeUser(comId, institutionId);
		List<Department> dep = this.listInstituScopeDep(comId, institutionId);
		if( (null != dep && !dep.isEmpty()) || (null != user && !user.isEmpty())){
			institutionDao.updateScopeState(comId, institutionId, 1);
			return true;
		}
		return false;
	}
	/**
	 * 删除查看范围
	 * @param type
	 * @param comId 类型
	 * @param busId 主键
	 * @param institutionId 制度主键
	 */
	public void delScope(String type,UserInfo userInfo,Integer busId,Integer institutionId) {
		 if(type.equals("dep")){
			institutionDao.delByField("instituScopeByDep", 
					new String[]{"comid","depId","instituId"}, new Object[]{userInfo.getComId(),busId,institutionId});
			
			Department dep = (Department) institutionDao.objectQuery(Department.class, busId);
			logsService.addLogs(userInfo.getComId(),institutionId, userInfo.getId(), userInfo.getUserName(),  "将部门：\""+dep.getDepName()+"\"从查看范围中删除",ConstantInterface.TYPE_INSTITUTION);
		 }else if(type.equals("user")){
			institutionDao.delByField("instituScopeByUser", 
					new String[]{"comid","userId","instituId"}, new Object[]{userInfo.getComId(),busId,institutionId});
			UserInfo newUser = userInfoService.getUserInfoById(busId.toString());
			logsService.addLogs(userInfo.getComId(),institutionId, userInfo.getId(), userInfo.getUserName(),  "将人员：\""+newUser.getUserName()+ "\"从查看范围中删除",ConstantInterface.TYPE_INSTITUTION);
		}
	}
	/**
	 * 修改查看范围
	 * @param type
	 * @param comId 类型
	 * @param busId 主键
	 * @param institutionId 制度主键
	 */
	public void updateScope(String type,Integer[] busId,Integer institutionId,UserInfo userInfo) {
	 if(type.equals("dep")){
			//清除部门范围
			institutionDao.delByField("instituScopeByDep",new String[]{"instituId"}, new Object[]{institutionId});
			String depName = "";
			if(null!= busId && busId.length>0){
				for(int i =0;i<busId.length;i++){
					 InstituScopeByDep dep = new InstituScopeByDep();
					 dep.setComId(userInfo.getComId());
					 dep.setInstituId(institutionId);
					 dep.setDepId(busId[i]);
					 institutionDao.add(dep);
					 Department department = (Department) institutionDao.objectQuery(Department.class, busId[i]);
					 depName += " "+department.getDepName()+" ";
				}
			 }
			logsService.addLogs(userInfo.getComId(),institutionId, userInfo.getId(), userInfo.getUserName(), "可查看部门更改为：\""+depName+ "\"",ConstantInterface.TYPE_INSTITUTION);
		}else if(type.equals("user")){
			//清除人员范围
			institutionDao.delByField("instituScopeByUser", new String[]{"instituId"}, new Object[]{institutionId});
			String userName = "";
			if(null!= busId && busId.length>0){
				for(int i =0;i<busId.length;i++){
					 InstituScopeByUser user = new InstituScopeByUser();
					 user.setComId(userInfo.getComId());
					 user.setInstituId(institutionId);
					 user.setUserId(busId[i]);
					 institutionDao.add(user);
					 UserInfo newUser = userInfoService.getUserInfoById(busId[i].toString());
					 userName += " "+newUser.getUserName()+" ";
				}
			 }
			logsService.addLogs(userInfo.getComId(),institutionId, userInfo.getId(), userInfo.getUserName(),  "可查看人员更改为：\""+userName+ "\"",ConstantInterface.TYPE_INSTITUTION);
		}
	}
	/***************************** 以下是制度类型配置 *****************************************/
	/**
	 * 获取制度类型集合
	 * 
	 * @param comId
	 * @return
	 */
	public List<InstituType> listInstituType(Integer comId) {
		return institutionDao.listInstituType(comId);
	}

	/**
	 * 添加制度类型
	 * 
	 * @param institutionType
	 * @param userInfo
	 * @return
	 */
	public Integer addInstituType(InstituType institutionType, UserInfo userInfo) {
		Integer id = institutionDao.add(institutionType);
		// 添加系统日志记录
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "添加制度类型:\"" + institutionType.getTypeName()
				+ "\"", ConstantInterface.TYPE_INSTITUTION, userInfo.getComId(),userInfo.getOptIP());
		return id;
	}

	/**
	 * 获取制度类型最大序号
	 * 
	 * @param comId
	 * @return
	 */
	public Integer queryInstituTypeOrderMax(Integer comId) {
		InstituType institutionType = institutionDao.queryInstituTypeOrderMax(comId);
		return null == institutionType.getOrderNum() ? 1 : institutionType.getOrderNum();
	}

	/**
	 * 修改制度类型名称
	 * 
	 * @param institutionType
	 * @param userInfo
	 * @return
	 */
	public boolean updateInstituTypeName(InstituType institutionType, UserInfo userInfo) {
		boolean succ = true;
		try {
			InstituType obj = (InstituType) institutionDao.objectQuery(InstituType.class, institutionType.getId());
			if (null != obj && !obj.getTypeName().equals(institutionType.getTypeName())) {// 有变动
				// 更新制度类型名称
				institutionDao.update(institutionType);
				// 添加系统日志记录
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),
						"更新制度类型名称:\"" + institutionType.getTypeName() + "\"", ConstantInterface.TYPE_INSTITUTION, 
						userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false;
		}
		return succ;
	}

	/**
	 * 修改制度类型序号
	 * 
	 * @param institutionType
	 * @param userInfo
	 * @return
	 */
	public boolean updateInstituTypeOrder(InstituType institutionType, UserInfo userInfo) {
		boolean succ = true;
		try {
			InstituType obj = (InstituType) institutionDao.objectQuery(InstituType.class, institutionType.getId());
			if (null != obj && !obj.getOrderNum().equals(institutionType.getOrderNum())) {// 有变动
				// 更新制度类型
				institutionDao.update(institutionType);
				// 添加系统日志记录
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "更新制度类型:\"" + obj.getTypeName()
						+ "\"的排序为\"" + institutionType.getOrderNum() + "\"", ConstantInterface.TYPE_INSTITUTION, 
						userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false;
		}
		return succ;
	}

	/**
	 * 删除制度类型
	 * 
	 * @param institutionType
	 * @param userInfo
	 * @return
	 */
	public boolean delInstituType(InstituType institutionType, UserInfo userInfo) {
		boolean succ = true;
		try {
			Integer crmNum = institutionDao.countInstituByInstituType(institutionType.getId(), userInfo);
			if (crmNum > 0) {
				succ = false;
			} else {
				// 获取需要删除的制度类型
				InstituType obj = (InstituType) institutionDao.objectQuery(InstituType.class, institutionType.getId());
				if (null != obj) {
					// 制度类型删除
					institutionDao.delById(InstituType.class, institutionType.getId());
					// 添加系统日志记录
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),
							"删除制度类型:\"" + obj.getTypeName() + "\"", ConstantInterface.TYPE_INSTITUTION,
							userInfo.getComId(),userInfo.getOptIP());
				}
			}

		} catch (Exception e) {
			succ = false;
		}
		return succ;
	}
	/**
	 * 获取制度类型
	 * @param comId
	 * @param instituType
	 * @return
	 */
	public InstituType queryInstituType(Integer instituType) {
		return (InstituType) institutionDao.objectQuery(InstituType.class, instituType);
	}
	/**
	 * 发待办消息
	 * @param userInfo
	 * @param instituId
	 * @param content
	 */
	public void updateTodoWork(UserInfo userInfo,Integer instituId,String content){
		//制度通知范围人
		List<UserInfo> shares = new ArrayList<UserInfo>();
		Institution institution = (Institution) institutionDao.objectQuery(Institution.class, instituId);
		if(institution.getScopeState().equals(0)){
			shares = userInfoService.listAllEnabledUser(userInfo);
		}else{
			shares = userInfoService.listInstituScopeUser(userInfo.getComId(), instituId);
		}
		updateTodoWorksThread(userInfo, shares,instituId, ConstantInterface.TYPE_INSTITUTION, content);
	}
	/**
	 * 删除查看记录
	 * @param comId
	 * @param instituId
	 */
	public void delViewRecord(Integer comId,Integer instituId){
		institutionDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, 
				new Object[]{comId,instituId,ConstantInterface.TYPE_INSTITUTION});
	}
}
