package com.westar.core.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Jifen;
import com.westar.base.model.JifenConfig;
import com.westar.base.model.JifenHistory;
import com.westar.base.model.JifenLev;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.JiFenDao;

@Service
public class JiFenService {

	@Autowired
	JiFenDao jiFenDao;

	/**
	 * 积分项配置列表
	 * @return
	 */
	public List<JifenConfig> listJiFenConfigs() {
		List<JifenConfig> list = jiFenDao.listJiFenConfigs();
		return list;
	}
	/**
	 * 积分项配置分页列表
	 * @return
	 */
	public List<JifenConfig> listPagedJiFenConfigs() {
		List<JifenConfig> list = jiFenDao.listPagedJiFenConfigs();
		return list;
	}

	/**
	 * 积分等级配置列表
	 * @return
	 */
	public List<JifenLev> listJifenLevConfig() {
		List<JifenLev> list = jiFenDao.listJifenLevConfig();
		return list;
	}
	/**
	 * 积分等级配置分页列表
	 * @return
	 */
	public List<JifenLev> listPagedJifenLevConfig() {
		List<JifenLev> list = jiFenDao.listPagedJifenLevConfig();
		return list;
	}

	/**
	 * 积分配置排序最大值
	 * @return
	 */
	public Integer queryJifenConfigOrderMax() {
		Integer maxOrder = jiFenDao.queryJifenConfigOrderMax();
		return maxOrder;
	}

	/**
	 * 验证类别代码是否有重复
	 * @param jifenCode
	 * @param id
	 * @return
	 */
	public boolean validateJifenCode(String jifenCode, Integer id) {
		Integer count = jiFenDao.validateJifenCode(jifenCode,id);
		//count为0,则数据库中没有重复的
		return count==0;
	}

	/**
	 * 添加积分类别
	 * @param jifenConfig
	 * @return
	 */
	public Integer addJifenType(JifenConfig jifenConfig) {
		return jiFenDao.add(jifenConfig);
	}

	/**
	 * 修改积分类别
	 * @param jifenConfig
	 */
	public void updateJifenConfig(JifenConfig jifenConfig) {
		jiFenDao.update(jifenConfig);
		
	}

	/**
	 * 删除积分配置
	 * @param id
	 */
	public void delJifenConfig(Integer id) {
		jiFenDao.delById(JifenConfig.class, id);
	}

	/**
	 * 验证等级名称是否有重复
	 * @param jifenCode
	 * @param id
	 * @return
	 */
	public boolean validateJifenLevName(String levName, Integer id) {
		Integer count = jiFenDao.validateJifenLevName(levName,id);
		//count为0,则数据库中没有重复的
		return count==0;
	}

	/**
	 * 添加积分等级
	 * @param jifenLev
	 * @return
	 */
	public Integer addJifenLev(JifenLev jifenLev) {
		return jiFenDao.add(jifenLev);
	}

	/**
	 * 修改积分等级
	 * @param jifenLev
	 */
	public void updateJifenLev(JifenLev jifenLev) {
		jiFenDao.update(jifenLev);
	}

	/**
	 * 删除积分等级
	 * @param id
	 */
	public void delJifenLev(Integer id) {
		jiFenDao.delById(JifenLev.class, id);
		
	}

	/**
	 * 积分历史
	 * @param jifenHistory
	 * @return
	 */
	public List<JifenHistory> listPagedJifenHistory(JifenHistory jifenHistory) {
		
		return jiFenDao.listPagedJifenHistory(jifenHistory);
	}
	/**
	 * 添加积分的历史记录
	 * @param comId 企业编号
	 * @param userId 用户主键
	 * @param jifenType 系统任务,日常任务还是永久性任务
	 * @param businessType 任务编码
	 * @param content 主表描述
	 * @param modId 模块主键
	 */
	public Integer addJifen(Integer comId, Integer userId,String businessType, String content,Integer modId) {
		//用户当前积分
		Integer allJifen = jiFenDao.getJifenScore(comId,userId);
		if(null==userId || 0==userId){//人不在系统中
			return allJifen;
		}
		//当前日期
		String today = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//业务类型所对应的积分项信息
		JifenConfig jifenConfig = jiFenDao.getJifenConfigByCode(businessType);
		if(null==jifenConfig){
			return 0;
		}
		//是否可以添加积分，默认可以
		boolean isAddJifen = true;
		if(ConstantInterface.JIFEN_SYS.equals(jifenConfig.getType())){//是系统任务
			//用户系统任务积分历史
			Integer jifeAllChange = jiFenDao.getJifenScore(comId,userId,jifenConfig.getId(),null);
			if(jifeAllChange>=jifenConfig.getSysMaxScore()){//系统任务已经完成
				isAddJifen = false;
			}
		}else if(ConstantInterface.JIFEN_DAY.equals(jifenConfig.getType())){//是每日任务
			//用户日常任务当天积分历史
			Integer jifeAllChange = jiFenDao.getJifenScore(comId,userId,jifenConfig.getId(),today);
			if(jifeAllChange>=jifenConfig.getDayMaxScore()){//日常任务已经完成
				isAddJifen = false;
			}
		}else if(ConstantInterface.JIFEN_PERMANENT.equals(jifenConfig.getType())){//永久性任务
			//永久性任务每天可以无限制的做（除了指定事项）
			if(ConstantInterface.TYPE_TASKDONE.equals(businessType)){//是任务委托
				//判断自己对该任务是否委托过一次
				List<JifenHistory> jifenHistory = jiFenDao.getJifenHistory(comId,userId,modId,jifenConfig.getId());
				if(null != jifenHistory && jifenHistory.size()>0){//已经积过分了
					return 0;
				}
			}else if(ConstantInterface.TYPE_TASKFINISH.equals(businessType)){
				//判断自己对该任务是否办结过一次
				List<JifenHistory> jifenHistory = jiFenDao.getJifenHistory(comId,userId,modId,jifenConfig.getId());
				if(null != jifenHistory && jifenHistory.size()>0){//已经积过分了
					return 0;
				}
			}
			
		}
		if(isAddJifen){
			if(allJifen!=0 && (allJifen + jifenConfig.getTypeScore())<0){//分值设为0
				//现有积分为原来的积分加上积分项标志的分数
				//积分历史
				JifenHistory jifenHistory = new JifenHistory();
				//企业编号
				jifenHistory.setComId(comId);
				//用户主键
				jifenHistory.setUserId(userId);
				//积分项主键
				jifenHistory.setConfigId(jifenConfig.getId());
				//积分项变化
				jifenHistory.setJifenChange(allJifen + jifenConfig.getTypeScore());
				//总积分
				jifenHistory.setAllScore(0);
				//总表描述
				jifenHistory.setContent(content);
				//模块主键（主要用于任务委托）
				jifenHistory.setModId(modId);
				
				jiFenDao.add(jifenHistory);
				
				allJifen = 0;
				//修改积分
				jiFenDao.updateJifen(comId,userId,0);
			}else if(allJifen==0 && jifenConfig.getTypeScore()<0){//不扣分
				
			}else{
				//现有积分为原来的积分加上积分项标志的分数
				allJifen = allJifen + jifenConfig.getTypeScore();
				//积分历史
				JifenHistory jifenHistory = new JifenHistory();
				//企业编号
				jifenHistory.setComId(comId);
				//用户主键
				jifenHistory.setUserId(userId);
				//积分项主键
				jifenHistory.setConfigId(jifenConfig.getId());
				//积分项变化
				jifenHistory.setJifenChange(jifenConfig.getTypeScore());
				//总积分
				jifenHistory.setAllScore(allJifen);
				//总表描述
				jifenHistory.setContent(content);
				//模块主键（主要用于任务委托）
				jifenHistory.setModId(modId);
				
				jiFenDao.add(jifenHistory);
				
				//修改积分
				jiFenDao.updateJifen(comId,userId,allJifen);
				
			}
			
		}
		return allJifen;
		
	}
	/**
	 * 积分配置
	 * @param id
	 * @return
	 */
	public JifenConfig getJifenConfigById(Integer id) {
		return (JifenConfig) jiFenDao.objectQuery(JifenConfig.class, id);
	}
	
	/**
	 * 积分名次
	 * @param comId
	 * @param type 1周2月
	 * @return
	 * @throws ParseException 
	 */
	public List<Jifen> listPagedJifenOrder(Integer comId, String type) throws ParseException {
		if(null == type || "".equals(type)){//总积分排名
			return jiFenDao.listPagedJifenOrder(comId);
		}else{//周积分排名或是月积分排名
			return jiFenDao.listPagedJifenOrder(comId,type);
		}
	}
	/**
	 * 计算个人名次所需数据集合
	 * @param comId
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	public List<Jifen> listJifenOrder(Integer comId, String type) throws ParseException {
		if(null == type || "".equals(type)){//总积分排名
			return jiFenDao.listPagedJifenOrder(comId);
		}else{//周积分排名或是月积分排名
			return jiFenDao.listPagedJifenOrder(comId,type);
		}
	}
	/**
	 * 当前操作员的总排名
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Jifen getAllOrder(Integer comId, Integer userId) {
		return jiFenDao.getAllOrder(comId, userId);
	}
	/**
	 * 取得积分以及排序
	 * @param comId
	 * @param userId
	 * @param type
	 * @return
	 * @throws ParseException 
	 */
	public Jifen getJifen(Integer comId, Integer userId, Integer type) throws ParseException {
		return jiFenDao.getJifen(comId, userId, type);
	}
	//获取个人积分排名情况
	public Jifen getUserOrder(Integer comId,Integer userid,Integer type) throws ParseException{
		if(null == type || "".equals(type)){//总积分排名
			return jiFenDao.getAllOrder(comId, userid);
		}else{//周积分排名或是月积分排名
			return jiFenDao.getJifen(comId, userid,type);
		}
	}
	/**
	 * 用户的最近等级
	 * @param jifenScore 当前总积分
	 * @return
	 */
	public JifenLev getUserJifenLevMin(Integer jifenScore) {
		JifenLev jifenLev = jiFenDao.getUserJifenLevMin(jifenScore);
		return jifenLev;
	}
	/**
	 * 用户的下一等级
	 * @param jifenScore 当前总积分
	 * @return
	 */
	public JifenLev getUserJifenLevNext(Integer jifenScore) {
		JifenLev jifenLev = jiFenDao.getUserJifenLevNext(jifenScore);
		return jifenLev;
	}
	
}
