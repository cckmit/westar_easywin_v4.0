package com.westar.core.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.model.DailyLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Attention;
import com.westar.base.model.CustomerLog;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.ItemLog;
import com.westar.base.model.Leave;
import com.westar.base.model.MsgShareLog;
import com.westar.base.model.NewsInfo;
import com.westar.base.model.QuesLog;
import com.westar.base.model.TaskLog;
import com.westar.base.model.UserInfo;
import com.westar.base.model.VoteLog;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.AttentionDao;
import com.westar.core.web.PaginationContext;

@Service
public class AttentionService {

	@Autowired
	AttentionDao attentionDao;
	
	@Autowired
	MsgShareService msgShareService;

	/**
	 * 取消关注或标识关注
	 * @param atten
	 * @param attentionState 
	 * @param userInfo 
	 */
	public void delAttention(Attention atten, UserInfo userInfo, Integer attentionState) {
		attentionDao.delByField("attention", new String[]{"comId","userId","busType","busId"}, 
				new Object[]{atten.getComId(),atten.getUserId(),atten.getBusType(),atten.getBusId()});
		String log = "取消关注";
		//原来没有关注,现在关注了（需要添加数据）
		if(attentionState==0){
			attentionDao.add(atten);
			log = "添加关注";
		}
		this.addLog(atten,userInfo,log);
		
	}
	/**
	 * 标识关注
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @param userInfo 关注人员
	 */
	public void addAtten(String busType,Integer busId, UserInfo userInfo) {
		Attention atten = new Attention();
		//业务主键
		atten.setBusId(busId);
		//业务类型
		atten.setBusType(busType);
		//企业号
		atten.setComId(userInfo.getComId());
		//关注人员
		atten.setUserId(userInfo.getId());
		attentionDao.add(atten);
	}
	/**
	 * 取得具体关注信息
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public Attention getAtten(String busType,Integer busId, UserInfo userInfo) {
		return attentionDao.getAtten(busType,busId,userInfo);
	}
	/**
	 * 添加数据
	 * @param atten
	 * @param userInfo
	 * @param log
	 */
	private void addLog(Attention atten, UserInfo userInfo, String log) {
		//添加查看日志
		if(atten.getBusType().equals(ConstantInterface.TYPE_CRM)){//客户
			
			CustomerLog customerLog = new CustomerLog();
			//企业号
			customerLog.setComId(userInfo.getComId());
			//业务主键
			customerLog.setCustomerId(atten.getBusId());
			//操作人员
			customerLog.setUserId(userInfo.getId());
			//操作内容描述
			customerLog.setContent(log);
			//操作人员的姓名
			customerLog.setUserName(userInfo.getUserName());
			//添加客户查看日志
			attentionDao.add(customerLog);
			
		}else if(atten.getBusType().equals(ConstantInterface.TYPE_ITEM)){//项目
			
			ItemLog itemLog = new ItemLog();
			//企业号
			itemLog.setComId(userInfo.getComId());
			//业务主键
			itemLog.setItemId(atten.getBusId());
			//操作人员
			itemLog.setUserId(userInfo.getId());
			//操作内容描述
			itemLog.setContent(log);
			//操作人员的姓名
			itemLog.setUserName(userInfo.getUserName());
			//添加项目查看日志
			attentionDao.add(itemLog);
			
		}else if(atten.getBusType().equals(ConstantInterface.TYPE_TASK)){//任务
			
			TaskLog taskLog = new TaskLog();
			//企业号
			taskLog.setComId(userInfo.getComId());
			//业务主键
			taskLog.setTaskId(atten.getBusId());
			//操作人员
			taskLog.setUserId(userInfo.getId());
			//操作内容描述
			taskLog.setContent(log);
			//操作人员的姓名
			taskLog.setUserName(userInfo.getUserName());
			//添加任务查看日志
			attentionDao.add(taskLog);
		}else if(atten.getBusType().equals(ConstantInterface.TYPE_QUES)){//问答
			
			QuesLog quesLog = new QuesLog();
			//企业号
			quesLog.setComId(userInfo.getComId());
			//业务主键
			quesLog.setQuesId(atten.getBusId());
			//操作人员
			quesLog.setUserId(userInfo.getId());
			//操作内容描述
			quesLog.setContent(log);
			//操作人员的姓名
			quesLog.setUserName(userInfo.getUserName());
			//添加问答查看日志
			attentionDao.add(quesLog);
			
		}else if(atten.getBusType().equals(ConstantInterface.TYPE_VOTE)){//投票
			
			VoteLog voteLog = new VoteLog();
			//企业号
			voteLog.setComId(userInfo.getComId());
			//业务主键
			voteLog.setVoteId(atten.getBusId());
			//操作人员
			voteLog.setUserId(userInfo.getId());
			//操作内容描述
			voteLog.setContent(log);
			//操作人员的姓名
			voteLog.setUserName(userInfo.getUserName());
			//添加投票查看日志
			attentionDao.add(voteLog);
			
		}else if(atten.getBusType().equals("1")){//信息分享
			
			MsgShareLog msgShareLog = new MsgShareLog();
			//企业号
			msgShareLog.setComId(userInfo.getComId());
			//业务主键
			msgShareLog.setMsgId(atten.getBusId());
			//操作人员
			msgShareLog.setUserId(userInfo.getId());
			//操作内容描述
			msgShareLog.setContent(log);
			//操作人员的姓名
			msgShareLog.setUserName(userInfo.getUserName());
			//添加投票查看日志
			attentionDao.add(msgShareLog);
			
		}else if(atten.getBusType().equals(ConstantInterface.TYPE_DAILY)){//分享

			DailyLog dailyLog = new DailyLog();
			//企业号
			dailyLog.setComId(userInfo.getComId());
			//业务主键
			dailyLog.setDailyId(atten.getBusId());
			//操作人员
			dailyLog.setUserId(userInfo.getId());
			//操作内容描述
			dailyLog.setContent(log);
			//操作人员的姓名
			dailyLog.setUserName(userInfo.getUserName());
			//添加投票查看日志
			attentionDao.add(dailyLog);

		}
		
	}

	/**
	 * 分页查询关注信息
	 * @param atten 关注的属性条件
	 * @param modList  关联的模块集合
	 * @return
	 */
	public List<Attention> listpagedAtten(Attention atten, List<String> modList) {
		List<Attention> list = attentionDao.listpagedAtten(atten,modList);
		return list;
	}
	/***
	 * 查看所有的关注数据，不分页
	 * @param atten
	 * @return
	 */
	public List<Attention> listAttenOfAll(Attention atten) {
		List<Attention> list = attentionDao.listAttenOfAll(atten);
		return list;
	}
	/**
	 * 我的所有关注
	 * @param atten
	 * @return
	 */
	public List<Attention> listMyAtten(Attention atten) {
		List<Attention> list = attentionDao.listMyAtten(atten);
		return list;
	}

	/**
	 * 
	 * @param ids
	 * @param userInfo
	 */
	public void delAttention(List<Integer> ids, UserInfo userInfo) {
		for (Integer id : ids) {
			Attention atten = (Attention) attentionDao.objectQuery(Attention.class, id);
			if(null!=atten){
				this.delAttention(atten, userInfo, 1);
			}
		}
		
	}
	/**
	 * 添加模块最新动态
	 * @param userInfo 操作员
	 * @param busId 业务主键
	 * @param content 变更内容
	 * @param busType 业务类型
	 */
	public void addNewsInfo(UserInfo userInfo, Integer busId,
			String content, String busType) {
		
		NewsInfo newsInfo = new NewsInfo(userInfo.getComId(), busId, busType, content, userInfo.getId());
		//先删除最新动态的信息，在表内只能保留一条数据
		attentionDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),busId,busType});
		//更新最新动态信息
		attentionDao.add(newsInfo);
		
	}
	/**
	 * 取得模块关注成员
	 * @param comId 企业号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public List<Attention> listAtten(Integer comId, Integer busId, String busType){
		List<Attention> list = attentionDao.listAtten(comId,busId,busType);
		return list;
		
	}
	
	/**
	 * 获取前N个关注的信息
	 * @param userInfo 操作员
	 * @param rowNum 结果数
	 * @return
	 */
	public List<Attention> firstNAttenList(UserInfo userInfo,Integer rowNum){
		return attentionDao.firstNAttenList(userInfo,rowNum);
	}
	
	/**
	 * 取得指定人员的关注数 app
	 * @param userId 指定人员
	 * @param comId 企业号
	 * @return
	 */
	public Integer countUserAtten(Integer userId, Integer comId) {
		return attentionDao.countUserAtten(userId,comId);
	}
	
}
