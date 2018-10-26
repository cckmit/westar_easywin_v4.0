package com.westar.core.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.enums.ConsumeStatusEnum;
import com.westar.base.model.Consume;
import com.westar.base.model.ConsumeLog;
import com.westar.base.model.ConsumeType;
import com.westar.base.model.ConsumeUpfile;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.ConsumeDao;
import com.westar.core.web.PaginationContext;

@Service
public class ConsumeService {
	
	private static Logger logger = LoggerFactory.getLogger(ConsumeService.class);

	@Autowired
	ConsumeDao consumeDao;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	/**
	 * 分页查询消费记录
	 * @param consume
	 * @param userInfo
	 * @return
	 */
	public List<Consume> listPagedConsume(Consume consume, UserInfo userInfo,String order) {
		return consumeDao.listPagedConsume(consume,userInfo,order);
	}
	
	/**
	 * 查询费用类型
	 * @param comId
	 * @return
	 */
	public List<ConsumeType> listConsumeType(Integer comId) {
		return consumeDao.listConsumeType(comId);
	}
	
	/**
	 * 更新费用类型属性值
	 * @param name 修改的字段
	 * @param val 修改的字段值
	 * @param id
	 * @return
	 */
	public boolean updateConsumNames(String name, String val, Integer id, UserInfo userInfo) {
		boolean succ = true;
		try {
			//获取需要更新的费用类型
			ConsumeType orgObj = (ConsumeType) consumeDao.objectQuery(ConsumeType.class, id);
			if(null!=orgObj){
				if(name.equals("typeName") && !val.equals(orgObj.getTypeName())) {//有变动
					//更新费用类型名
					consumeDao.update("update consumeType a set a.typeName='"+val+"' where a.comid="+userInfo.getComId()+" and a.id="+id+"",orgObj);
					//添加系统日志记录 
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "更新费用类型\""+orgObj.getTypeName()+"\"的类型名为\""+val+"\"",
							ConstantInterface.TYPE_CONSUME,userInfo.getComId(),userInfo.getOptIP());
				}else if(name.equals("typeOrder")) {
					//更新费用类型排序
					consumeDao.update("update consumeType a set a.typeOrder='"+val+"' where a.comid="+userInfo.getComId()+" and a.id="+id+"",orgObj);
				}else if(name.equals("showConsumePersonNum")) {
					//更新费用类型消费人数
					consumeDao.update("update consumeType a set a.showConsumePersonNum='"+val+"' where a.comid="+userInfo.getComId()+" and a.id="+id+"",orgObj);
				}else if(name.equals("showStartDate")) {
					//更新费用类型开始时间
					consumeDao.update("update consumeType a set a.showStartDate='"+val+"' where a.comid="+userInfo.getComId()+" and a.id="+id+"",orgObj);
				}else if(name.equals("showEndDate")) {
					//更新费用类型结束时间
					consumeDao.update("update consumeType a set a.showEndDate='"+val+"' where a.comid="+userInfo.getComId()+" and a.id="+id+"",orgObj);
				}else if(name.equals("showLeavePlace")) {
					//更新费用类型出发地
					consumeDao.update("update consumeType a set a.showLeavePlace='"+val+"' where a.comid="+userInfo.getComId()+" and a.id="+id+"",orgObj);
				}else if(name.equals("showArrivePlace")) {
					//更新费用类型目的地
					consumeDao.update("update consumeType a set a.showArrivePlace='"+val+"' where a.comid="+userInfo.getComId()+" and a.id="+id+"",orgObj);
				}else if(name.equals("checkLa")) {
					//更新费用类型目的地和出发地
					consumeDao.update("update consumeType a set a.showArrivePlace='"+val+"',a.showLeavePlace="+val+" where a.comid="+userInfo.getComId()+" and a.id="+id+"",orgObj);
				}
				
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 删除费用类型
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public boolean deleteConsumeType(Integer id, UserInfo userInfo) {
		boolean succ = true;
		try {
			//获取需要更新的费用类型
			ConsumeType orgObj = (ConsumeType) consumeDao.objectQuery(ConsumeType.class, id);
			//删除费用类型
			if(null!=orgObj){
				consumeDao.delById(ConsumeType.class, id);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除费用类型\""+orgObj.getTypeName()+"\"",
						ConstantInterface.TYPE_CONSUME,userInfo.getComId(),userInfo.getOptIP());
				}
			} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 新增费用类型
	 * @param consumeType
	 * @param userInfo
	 * @return
	 */
	public Integer addConsumeType(ConsumeType consumeType, UserInfo userInfo) {
		Integer id =  consumeDao.add(consumeType);
		//添加系统日志记录 
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "添加费用类型:\""+consumeType.getTypeName()+"\"",
				ConstantInterface.TYPE_CONSUME,userInfo.getComId(),userInfo.getOptIP());
		return id;
	}
	
	/**
	 * 查询最大的费用类型序号
	 * @param comId
	 * @return
	 */
	public Integer queryConsumeTypeOrderMax(Integer comId) {
		ConsumeType consumeType = consumeDao.initConsumeTypeOrder(comId);
		return null==consumeType.getTypeOrder()?1:consumeType.getTypeOrder();
	}
	
	/**
	 * 分页查询消费记录发票
	 * @param comId
	 * @param consumeId
	 * @return
	 */
	public List<ConsumeUpfile> listPagedConsumeUpfile(Integer comId, Integer consumeId) {
		return consumeDao.listPagedConsumeUpfile(comId,consumeId);
	}
	/**
	 * 查询所有的记账附件
	 * @param comId 团队号
	 * @param consumeId 记账主键
	 * @return
	 */
	public List<ConsumeUpfile> listConsumeUpfile(Integer comId, Integer consumeId) {
		return consumeDao.listConsumeUpfile(comId,consumeId);
	}
	
	/**
	 * 获取消费记录详情
	 * @param id
	 * @return
	 */
	public Consume getConsumeById(Integer id) {
		Consume consume = consumeDao.getConsumeById(id);
		return consume;
	}

	/**
	 * 根据id获取费用类型
	 * @param id
	 * @return
	 */
	public ConsumeType getConsumeTypeById(Integer id) {
		return (ConsumeType) consumeDao.objectQuery(ConsumeType.class, id);
	}
	
	
	/**
	 * 添加消费记录
	 * @param consume
	 * @param userInfo
	 * @throws Exception
	 */
	public void addConsume(Consume consume, UserInfo userInfo){
		
		// 消费记录所在公司
		consume.setComId(userInfo.getComId());
		// 创建人
		consume.setCreator(userInfo.getId());
		// 状态
		consume.setStatus(ConsumeStatusEnum.UNREIMBURSED.getValue());
				
		List<ConsumeUpfile> listUpfiles = consume.getListUpfiles();
		//设置发票数量
		if(!CommonUtil.isNull(listUpfiles)) {
			consume.setInvoiceNum(listUpfiles.size());
		}else {
			consume.setInvoiceNum(0);
		}
		Integer consumeId = consumeDao.add(consume);
		//发票
		if(!CommonUtil.isNull(listUpfiles)){
			//缓存附件主键信息
			List<Integer> listFileId = new ArrayList<Integer>();
			
			for (ConsumeUpfile consumeUpfile : listUpfiles) {
				consumeUpfile.setConsumeId(consumeId);
				consumeUpfile.setComId(consume.getComId());
				consumeUpfile.setUserId(consume.getCreator());
				consumeDao.add(consumeUpfile);
				//为附件创建索引
				try {
					uploadService.updateUpfileIndex(consumeUpfile.getUpfileId(), userInfo, "add",consumeId,ConstantInterface.TYPE_CONSUME);
				} catch (Exception e) {
					logger.info("记账添加附件索引失败！");
				}
				
				listFileId.add(consumeUpfile.getUpfileId());
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo,listFileId,consume.getDescribe());
		}
		this.addConsumeLog(userInfo.getComId(), consumeId, userInfo.getId(), userInfo.getUserName(), "新增了消费记录");
		
	}
	/**
	 * 修改消费记录
	 * @param consume
	 * @param userInfo
	 * @param from 来源（移动端标识为app）
	 * @return
	 */
	public boolean updateConsumes(Consume consume, UserInfo userInfo,String from) {
		boolean succ = true;
		//移动端维护发票信息
		if(!CommonUtil.isNull(from) && "app".equals(from)) {
			//删除发票附件
			consumeDao.delByField("consumeUpfile", new String[]{"comId","consumeId"}, new Object[]{userInfo.getComId(),consume.getId()});
			if(!CommonUtil.isNull(consume.getListUpfiles())) {
				consume.setInvoiceNum(consume.getListUpfiles().size());
				//缓存附件主键信息
				List<Integer> listFileId = new ArrayList<Integer>();
				
				for (ConsumeUpfile consumeUpfile : consume.getListUpfiles()) {
					consumeUpfile.setConsumeId(consume.getId());
					consumeUpfile.setComId(userInfo.getComId());
					consumeUpfile.setUserId(userInfo.getId());
					consumeDao.add(consumeUpfile);
					//为附件创建索引
					try {
						uploadService.updateUpfileIndex(consumeUpfile.getUpfileId(), userInfo, "add",consume.getId(),ConstantInterface.TYPE_CONSUME);
					} catch (Exception e) {
						logger.info("记账添加附件索引失败！");
					}
					
					listFileId.add(consumeUpfile.getUpfileId());
				}
				//归档到文档中心
				fileCenterService.addModFile(userInfo,listFileId,consume.getDescribe());
			}else {
				consume.setInvoiceNum(0);
			}
		}
		consumeDao.update(consume);
		this.addConsumeLog(userInfo.getComId(), consume.getId(), userInfo.getId(), userInfo.getUserName(), "修改了消费记录属性");
		return succ;
	}
	
	
	/**
	 * 删除发票附件
	 * @param consumeUpFileId
	 * @param userInfo
	 * @param consumeId
	 */
	public void delConsumeUpfile(Integer consumeUpFileId, UserInfo userInfo, Integer consumeId) {
		Consume consume = (Consume) consumeDao.objectQuery(Consume.class, consumeId);
		if(consume != null && consume.getInvoiceNum() > 0) {
			//修改发票数
			consume.setInvoiceNum(consume.getInvoiceNum()-1);
			consumeDao.update("update consume a set a.invoiceNum=:invoiceNum where a.comid=:comId and a.id=:id", consume);
		}
		ConsumeUpfile consumeUpfile = (ConsumeUpfile) consumeDao.objectQuery(ConsumeUpfile.class, consumeUpFileId);
		if(!CommonUtil.isNull(consumeUpfile)) {
			Upfiles upfiles = (Upfiles) consumeDao.objectQuery(Upfiles.class, consumeUpfile.getUpfileId());
			this.addConsumeLog(userInfo.getComId(), consume.getId(), userInfo.getId(), userInfo.getUserName(), "删除了发票附件："+upfiles.getFilename());
			consumeDao.delById(ConsumeUpfile.class, consumeUpFileId);
		}
		
		
	}

	/**
	 * 分页查询
	 * @param userInfo当前操作人员
	 * @param consume 记账功能查询条件
	 * @return
	 */
	public PageBean<Consume> listPagedConsumeForSelect(UserInfo userInfo,
			Consume consume) {
		//分页查询数据
		List<Consume> list = consumeDao.listPagedConsumeForSelect(userInfo,consume);
		
		//构建分页信息
		PageBean<Consume> pageBean = new PageBean<Consume>();
		pageBean.setRecordList(list);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	
	/**
	 * 删除消费记录
	 * @param userInfo
	 * @param ids
	 */
	public void deleteConsumes(UserInfo userInfo, Integer[] ids) {
		Integer comId = userInfo.getComId();
		for (Integer consumeId : ids) {
			//判断是否可以删除
			Consume consume = (Consume) consumeDao.objectQuery(Consume.class, consumeId);
			if(consume != null && consume.getStatus().equals(ConsumeStatusEnum.UNREIMBURSED.getValue())) {
				//删除发票附件
				consumeDao.delByField("consumeUpfile", new String[]{"comId","consumeId"}, new Object[]{comId,consumeId});
				//删除操作日志
				consumeDao.delByField("consumeLog", new String[]{"comId","consumeId"}, new Object[]{comId,consumeId});
				//删除消费记录
				consumeDao.delById(Consume.class, consumeId);
			}
		}
		
	}
	
	/**
	 * 编辑页面新增发票
	 * @param id 
	 * @param fileIds
	 * @param userInfo
	 * @throws Exception 
	 */
	public void addFiles(Integer id, String[] fileIds, UserInfo userInfo) throws Exception {
		
		Consume consume = (Consume) consumeDao.objectQuery(Consume.class, id);
		if(!CommonUtil.isNull(consume)) {
			//缓存附件主键信息
			List<Integer> listFileId = new ArrayList<Integer>();
			for (String upfileId : fileIds) {
				ConsumeUpfile consumeUpfile = new ConsumeUpfile();
				consumeUpfile.setConsumeId(id);
				consumeUpfile.setComId(consume.getComId());
				consumeUpfile.setUserId(consume.getCreator());
				consumeUpfile.setUpfileId(Integer.parseInt(upfileId));
				consumeDao.add(consumeUpfile);
				//为附件创建索引
				uploadService.updateUpfileIndex(consumeUpfile.getUpfileId(), userInfo, "add",id,ConstantInterface.TYPE_CONSUME);
				
				listFileId.add(consumeUpfile.getUpfileId());
			}
			//修改发票数
			consume.setInvoiceNum(consume.getInvoiceNum()+listFileId.size());
			consumeDao.update("update consume a set a.invoiceNum=:invoiceNum where a.comid=:comId and a.id=:id", consume);
			this.addConsumeLog(userInfo.getComId(), consume.getId(), userInfo.getId(), userInfo.getUserName(), "新上传了发票");
			//归档到文档中心
			fileCenterService.addModFile(userInfo,listFileId,consume.getDescribe());
		}
		
		
	}
	
	/**
	 * 添加消费记录日志
	 * @param comId
	 * @param consumeId
	 * @param userId
	 * @param userName
	 * @param content
	 */
	public void addConsumeLog(Integer comId,Integer consumeId,Integer userId,String userName,String content){
		ConsumeLog consumeLog = new ConsumeLog();
		consumeLog.setComId(comId);
		consumeLog.setConsumeId(consumeId);
		consumeLog.setContent(content);
		consumeLog.setUserId(userId);
		consumeLog.setUserName(userName);
		consumeDao.add(consumeLog);
	}
	
	/**
	 * 通过费用类型id查询消费记录表是否使用改费用类型
	 * @param id
	 * @return
	 */
	public Integer countConsumeTypeById(Integer id) {
		return consumeDao.countConsumeTypeById(id);
	}
	
	/**
	 * 查询待报销消费记录总数
	 * @param curUser
	 * @return
	 */
	public Integer countUrConsume(UserInfo curUser) {
		return consumeDao.countUrConsume(curUser);
	}
	
	
}
