package com.westar.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.DemandProcess;
import com.westar.base.model.DemandTalk;
import com.westar.base.model.DemandTalkUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.BaseTalk;
import com.westar.base.pojo.BaseUpfile;
import com.westar.base.pojo.PageBean;
import com.westar.core.dao.DemandTalkDao;
import com.westar.core.web.PaginationContext;

/**
 * 
 * 描述:需求管理业务逻辑层
 * 
 * @author zzq
 * @date 2018年10月12日 下午1:36:46
 */
@Service
public class DemandTalkService {

	private static final Logger logger = LoggerFactory .getLogger(DemandTalkService.class);

	@Autowired
	DemandTalkDao demandTalkDao;

	@Autowired
	FileCenterService fileCenterService;

	/**
	 * 分页查询留言信息
	 * 
	 * @param sessionUser
	 *            当前操作人员
	 * @param demandId
	 *            需求主键
	 * @return
	 */
	public PageBean<BaseTalk> listPagedDemandTalk(UserInfo sessionUser,
			Integer demandId) {
		List<BaseTalk> result = new ArrayList<>();
		//查询留言信息
		List<BaseTalk> recordList = demandTalkDao.listDemandTalk(demandId,sessionUser.getComId());
		if(null != recordList && !recordList.isEmpty()){
			for (BaseTalk baseTalk : recordList) {
				//查询留言附件
				List<BaseUpfile> listTalkFile = demandTalkDao.listTalkFile(sessionUser, demandId, baseTalk.getId());
				baseTalk.setListTalkFile(listTalkFile);
				result.add(baseTalk);
			}
		}
		//封装结果
		PageBean<BaseTalk> pageBean = new PageBean<BaseTalk>();
		pageBean.setRecordList(result);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}

	/**
	 * 添加留言信息
	 * 
	 * @param sessionUser
	 *            当前操作人员
	 * @param baseTalk
	 *            留言信息
	 * @return 
	 */
	public Integer addTalk(UserInfo sessionUser, BaseTalk baseTalk) {
		DemandTalk demandTalk = new DemandTalk();
		BeanUtils.copyProperties(baseTalk, demandTalk);
		// 属性复制
		demandTalk.setDemandId(baseTalk.getBusId());
		demandTalk.setComId(sessionUser.getComId());
		demandTalk.setSpeaker(sessionUser.getId());

		Integer talkId = demandTalkDao.add(demandTalk);

		List<BaseUpfile> listTalkFile = baseTalk.getListTalkFile();
		if (null != listTalkFile && !listTalkFile.isEmpty()) {
			List<Integer> listUpfile = new ArrayList<Integer>(
					listTalkFile.size());
			for (BaseUpfile baseUpfile : listTalkFile) {
				listUpfile.add(baseUpfile.getUpfileId());

				DemandTalkUpfile demandTalkUpfile = new DemandTalkUpfile();
				demandTalkUpfile.setComId(sessionUser.getComId());
				demandTalkUpfile.setDemandId(baseTalk.getBusId());
				demandTalkUpfile.setTalkId(talkId);
				demandTalkUpfile.setUpfileId(baseUpfile.getUpfileId());
				demandTalkUpfile.setUserId(sessionUser.getId());

				demandTalkDao.add(demandTalkUpfile);

			}
			// 需求查询
			DemandProcess demandProcess = (DemandProcess) demandTalkDao
					.objectQuery(DemandProcess.class, baseTalk.getBusId());
			// 添加到文档中心
			fileCenterService.addModFile(sessionUser, listUpfile, "需求"
					+ demandProcess.getSerialNum());
		}
		return talkId;

	}
	/**
	 * 查询留言信息
	 * @param sessionUser 当前操作人员
	 * @param talkId 留言主键
	 * @return
	 */
	public BaseTalk queryTalk(UserInfo sessionUser,Integer talkId){
		BaseTalk baseTalk = demandTalkDao.queryTalk(sessionUser,talkId);
		//查询留言附件
		List<BaseUpfile> listTalkFile = demandTalkDao.listTalkFile(sessionUser, baseTalk.getBusId(), baseTalk.getId());
		baseTalk.setListTalkFile(listTalkFile);
		return baseTalk;
	}
	/**
	 * 删除留言信息
	 * 
	 * @param sessionUser 当前操作人员
	 * @param baseTalk
	 */
	public void delTalk(UserInfo sessionUser, BaseTalk baseTalk) {
		// 需求主键
		Integer demandId = baseTalk.getBusId();
		Integer talkId = baseTalk.getId();
		// 查询用于删除的留言信息
		List<DemandTalk> listTalk = demandTalkDao.listTreeTalkForDel(
				sessionUser, talkId);
		if (null != listTalk && !listTalk.isEmpty()) {
			for (DemandTalk demandTalk : listTalk) {
				demandTalkDao.delByField("demandTalkUpfile",
						new String[] { "comId", "talkId" },
						new Object[] { sessionUser.getComId(),demandTalk.getId() });
				
				demandTalkDao.delById(DemandTalk.class, demandTalk.getId());
			}
		}
	}

}
