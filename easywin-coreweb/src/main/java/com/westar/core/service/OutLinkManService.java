package com.westar.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.enums.PubStateEnum;
import com.westar.base.model.LinkMan;
import com.westar.base.model.OlmAddress;
import com.westar.base.model.OlmAndCus;
import com.westar.base.model.OlmContactWay;
import com.westar.base.model.OlmLog;
import com.westar.base.model.OlmTalk;
import com.westar.base.model.OutLinkMan;
import com.westar.base.model.OutLinkManRange;
import com.westar.base.model.Upfiles;
import com.westar.base.model.OlmTalkUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.OutLinkManDao;
import com.westar.core.web.PaginationContext;

@Service
public class OutLinkManService {

	@Autowired
	OutLinkManDao outLinkManDao;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	/**
	 * 分页查询外部联系人
	 * @author hcj 
	 * @param outLinkMan
	 * @param user
	 * @return 
	 * @date 2018年8月25日 下午12:05:55
	 */
	public List<OutLinkMan> listPagedOutLinkMan(OutLinkMan outLinkMan, UserInfo user) {
		List<OutLinkMan> listOlm = outLinkManDao.listPagedOutLinkMan(outLinkMan,user);
		if(!CommonUtil.isNull(listOlm)) {
			for (OutLinkMan olm : listOlm) {
				//查询联系方式
				List<OlmContactWay> contactWays = outLinkManDao.listContactWayForShow(olm.getId(),user);
				olm.setListContactWay(contactWays);
				
				//查询联系地址
				List<OlmAddress> listAddress= outLinkManDao.listOlmAddressForShow(olm.getId(),user);
				olm.setListAddress(listAddress);
			}
		}
		return listOlm;
	}
	
	/**
	 * 添加外部联系人
	 * @param outLinkMan
	 * @param userInfo
	 */
	public void addOutLinkMan(OutLinkMan outLinkMan, UserInfo userInfo) {
		outLinkMan.setComId(userInfo.getComId());
		outLinkMan.setCreator(userInfo.getId());
		Integer olId = outLinkManDao.add(outLinkMan);
		if(olId != null && outLinkMan.getPubState() != null && outLinkMan.getPubState().equals(0)){
			//循环添加私有联系人范围记录
			if(outLinkMan.getListRangeUser() != null && outLinkMan.getListRangeUser().size() > 0){
				for (int i = 0; i < outLinkMan.getListRangeUser().size(); i++) {
					if(outLinkMan.getListRangeUser().get(i).getUserId() != null && !outLinkMan.getListRangeUser().get(i).getUserId().equals(userInfo.getId())){
						outLinkMan.getListRangeUser().get(i).setComId(userInfo.getComId());
						outLinkMan.getListRangeUser().get(i).setOutLinkManId(olId);
						outLinkManDao.add(outLinkMan.getListRangeUser().get(i));
					}
				}
			}
			//添加私有人员范围默认自己的记录
			OutLinkManRange or = new OutLinkManRange();
			or.setComId(userInfo.getComId());
			or.setOutLinkManId(olId);
			or.setUserId(userInfo.getId());
			outLinkManDao.add(or);
		}
		
		//循环添加联系方式
		if(!CommonUtil.isNull(outLinkMan.getListContactWay())) {
			for (int i = 0; i < outLinkMan.getListContactWay().size(); i++) {
				if(!CommonUtil.isNull(outLinkMan.getListContactWay().get(i).getContactWay()) && !CommonUtil.isNull(outLinkMan.getListContactWay().get(i).getContactWayCode()) ) {
					OlmContactWay olmContactWay = new OlmContactWay();
					olmContactWay.setContactWayCode(outLinkMan.getListContactWay().get(i).getContactWayCode());
					olmContactWay.setContactWay(outLinkMan.getListContactWay().get(i).getContactWay());
					olmContactWay.setComId(userInfo.getComId());
					olmContactWay.setOutLinkManId(olId);
					olmContactWay.setCreator(userInfo.getId());
					outLinkManDao.add(olmContactWay);
				}
			}
		}
		
		//循环添加联系地址
		if(!CommonUtil.isNull(outLinkMan.getListAddress())) {
			for (int i = 0; i < outLinkMan.getListAddress().size(); i++) {
				if(!CommonUtil.isNull(outLinkMan.getListAddress().get(i).getAddressCode()) && !CommonUtil.isNull(outLinkMan.getListAddress().get(i).getAddress()) ) {
					OlmAddress olmAddress = new OlmAddress();
					olmAddress.setAddressCode(outLinkMan.getListAddress().get(i).getAddressCode());
					olmAddress.setAddress(outLinkMan.getListAddress().get(i).getAddress());
					olmAddress.setComId(userInfo.getComId());
					olmAddress.setOutLinkManId(olId);
					olmAddress.setCreator(userInfo.getId());
					outLinkManDao.add(olmAddress);
				}
			}
		}
		
		//添加操作日志
		this.addOlmLog(userInfo.getComId(), olId, userInfo.getId(),"新增了外部联系人：" + outLinkMan.getLinkManName());
		
	}
	
	/**
	 * 删除外部联系人
	 * @param olmId
	 * @param userInfo
	 */
	public void delOlm(Integer olmId, UserInfo userInfo) {
		//删除外部联系人范围记录
		outLinkManDao.delByField("outLinkManRange", new String[] { "comId", "outLinkManId" },
					new Object[] { userInfo.getComId(), olmId });
		//删除外部联系人联系方式
		outLinkManDao.delByField("olmContactWay", new String[] { "comId", "outLinkManId" },
					new Object[] { userInfo.getComId(), olmId });
		//删除外部联系人联系地址
		outLinkManDao.delByField("olmAddress", new String[] { "comId", "outLinkManId" },
					new Object[] { userInfo.getComId(), olmId });
		//删除操作日志
		outLinkManDao.delByField("olmLog", new String[] { "comId", "outLinkManId" },
				new Object[] { userInfo.getComId(), olmId });
		//删除留言附件
		outLinkManDao.delByField("olmTalkUpfile", new String[] { "comId", "outLinkManId" },
						new Object[] { userInfo.getComId(), olmId });
		//删除留言
		outLinkManDao.delByField("olmTalk", new String[] { "comId", "outLinkManId" },
				new Object[] { userInfo.getComId(), olmId });
		//删除外部联系人
		outLinkManDao.delById(OutLinkMan.class, olmId);
	}
	
	/**
	 * 批量删除外部联系人
	 * @param ids
	 * @param userInfo
	 * @return
	 */
	public boolean delOlms(Integer[] ids, UserInfo userInfo) {
		if(!CommonUtil.isNull(ids)) {
			for (Integer id : ids) {
				this.delOlm(id, userInfo);
			}
		}
		return true;
	}
	
	/**
	 * 查询外部联系人范围列表
	 * @param outLinkManId
	 * @param user
	 * @return
	 */
	public List<OutLinkManRange> listOutLinkManRanges(Integer outLinkManId, UserInfo user) {
		return outLinkManDao.listOutLinkManRanges(outLinkManId,user);
	}
	
	/**
	 * 修改外部联系人
	 * @param outLinkMan
	 * @param userInfo
	 */
	public void updateOlm(OutLinkMan outLinkMan, UserInfo userInfo) {
		//删除私有人员范围
		outLinkManDao.delByField("outLinkManRange", new String[] { "comId", "outLinkManId" },
				new Object[] { userInfo.getComId(), outLinkMan.getId() });
		if(outLinkMan.getPubState() != null && outLinkMan.getPubState().equals(0)){
			//循环添加私有联系人范围记录
			if(outLinkMan.getListRangeUser() != null && outLinkMan.getListRangeUser().size() > 0){
				for (int i = 0; i < outLinkMan.getListRangeUser().size(); i++) {
					if(outLinkMan.getListRangeUser().get(i).getUserId() != null && !outLinkMan.getListRangeUser().get(i).getUserId().equals(userInfo.getId())){
						outLinkMan.getListRangeUser().get(i).setComId(userInfo.getComId());
						outLinkMan.getListRangeUser().get(i).setOutLinkManId(outLinkMan.getId());
						outLinkManDao.add(outLinkMan.getListRangeUser().get(i));
					}
				}
			}
			//添加私有人员范围默认自己的记录
			OutLinkManRange or = new OutLinkManRange();
			or.setComId(userInfo.getComId());
			or.setOutLinkManId(outLinkMan.getId());
			or.setUserId(userInfo.getId());
			outLinkManDao.add(or);
		}
		//更新外部联系人数据
		outLinkManDao.update(outLinkMan);
	}
	
	/**
	 * 客户页面选择外部联系人
	 * @param outLinkMan
	 * @param userInfo
	 * @return
	 */
	public PageBean<OutLinkMan> ajaxListForSelect(OutLinkMan outLinkMan, UserInfo userInfo) {
		List<OutLinkMan> list = outLinkManDao.listPagedOutLinkMan(outLinkMan,userInfo);
		List<OutLinkMan> results = new ArrayList<>();
		if(!CommonUtil.isNull(list)) {
			for (OutLinkMan olm : list) {
				//查询联系方式
				List<OlmContactWay> contactWays = outLinkManDao.listContactWayForShow(olm.getId(),userInfo);
				if(!CommonUtil.isNull(contactWays)) {
					for (OlmContactWay contactWay : contactWays) {
						OutLinkMan result = new OutLinkMan();
						BeanUtils.copyProperties(olm,result);
						result.setContactWay(contactWay.getContactWay());
						result.setContactWayValue(contactWay.getContactWayValue());
						results.add(result);
					}
				}else {
					OutLinkMan result = new OutLinkMan();
					BeanUtils.copyProperties(olm,result);
					results.add(result);
				}
			}
		}
		PageBean<OutLinkMan> pageBean = new PageBean<OutLinkMan>();
		pageBean.setRecordList(results);
		
		// 除开页面已有数据的总数
		Integer total = PaginationContext.getTotalCount();
		pageBean.setTotalCount(total);
				
		return pageBean;
	}
	
	/**
	 * 分页查询外部联系人信息
	 * @param comId 团队号
	 * @return
	 */
	public List<LinkMan> listPagedLinkMan(Integer comId){
		List<LinkMan> linkMans = outLinkManDao.listPagedLinkMan(comId);
		return linkMans;
	}
	
	/**
	 * 用于客户联系人信息导入外部联系人表
	 * 并添加外部联系人与客户的关系
	 * @param linkMans 联系人信息
	 * @param crmId_userIdMap 客户主键对应的用户主键
	 */
	public void initOutLinkMan(List<LinkMan> linkMans,Map<Integer, Integer> crmId_userIdMap) throws IllegalAccessException, InvocationTargetException {
		if(null !=linkMans && !linkMans.isEmpty()){
			for (LinkMan linkMan : linkMans) {
				Integer crmId = linkMan.getCustomerId();
				Integer creatorId = crmId_userIdMap.get(crmId);
				if(null == creatorId ){
					creatorId = linkMan.getCreatorId();
					crmId_userIdMap.put(crmId, creatorId);
				}
				OutLinkMan outLinkMan = new OutLinkMan();
				BeanUtils.copyProperties(linkMan,outLinkMan);
				outLinkMan.setId(null);
				
				outLinkMan.setCreator(creatorId);
				outLinkMan.setPubState(PubStateEnum.YES.getValue());
				int count = outLinkManDao.countOlm(outLinkMan);
				if(count < 1) {
					//添加联系人
					Integer olmId =  outLinkManDao.add(outLinkMan);
					outLinkMan.setId(olmId);
					outLinkManDao.update(outLinkMan);
					
					OlmAndCus oac = new OlmAndCus();
					oac.setComId(linkMan.getComId());
					oac.setCustomerId(crmId);
					oac.setOutLinkManId(olmId);
					outLinkManDao.add(oac);
				}
			}
		}
	}
	
	/**
	 * 查询外部联系人详情
	 * @param id
	 * @param user
	 * @return
	 */
	public OutLinkMan queryOutLinkManById(Integer id) {
		return (OutLinkMan) outLinkManDao.objectQuery(OutLinkMan.class, id);
	}
	
	/**
	 * 查询外部联系人联系方式
	 * @param olmId
	 * @param user
	 * @return
	 */
	public List<OlmContactWay> listOlmContactWay(Integer olmId, UserInfo user) {
		return outLinkManDao.listOlmContactWay(olmId,user);
	}
	
	/**
	 * 查询外部联系人联系地址
	 * @param olmId
	 * @param user
	 * @return
	 */
	public List<OlmAddress> listOlmAddress(Integer olmId, UserInfo user) {
		return outLinkManDao.listOlmAddress(olmId,user);
	}
	
	/**
	 * 修改外部联系人单个字段
	 * @param outLinkMan
	 * @param userInfo
	 * @return 
	 */
	public boolean updateOlmByOne(OutLinkMan outLinkMan, UserInfo userInfo) {
		boolean succ = true;
		try {
			if(outLinkMan.getPubState() != null) {
				//删除私有人员范围
				outLinkManDao.delByField("outLinkManRange", new String[] { "comId", "outLinkManId" },
						new Object[] { userInfo.getComId(), outLinkMan.getId() });
				if(outLinkMan.getPubState().equals(0)){
					//添加私有人员范围默认自己的记录
					OutLinkManRange or = new OutLinkManRange();
					or.setComId(userInfo.getComId());
					or.setOutLinkManId(outLinkMan.getId());
					or.setUserId(userInfo.getId());
					outLinkManDao.add(or);
				}
			}
			
			outLinkManDao.update(outLinkMan);
			//添加操作日志
			this.addOlmLog(userInfo.getComId(), outLinkMan.getId(), userInfo.getId(),"修改了外部联系人属性");
			
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 更新外部联系人分享范围
	 * @param outLinkManId
	 * @param userIds
	 * @param userInfo
	 * @return
	 */
	public boolean updateOlmRange(Integer outLinkManId, Integer[] userIds, UserInfo userInfo) {
		//删除私有人员范围
		outLinkManDao.delByField("outLinkManRange", new String[] { "comId", "outLinkManId" },
				new Object[] { userInfo.getComId(),outLinkManId });
		if(!CommonUtil.isNull(userIds) && userIds.length > 0){
			//循环添加私有联系人范围记录
			for (Integer userId : userIds) {
				if(userId != null && !userId.equals(userInfo.getId())) {
					OutLinkManRange or = new OutLinkManRange();
					or.setComId(userInfo.getComId());
					or.setOutLinkManId(outLinkManId);
					or.setUserId(userId);
					outLinkManDao.add(or);
				}
			}
			//添加私有人员范围默认自己的记录
			OutLinkManRange or = new OutLinkManRange();
			or.setComId(userInfo.getComId());
			or.setOutLinkManId(outLinkManId);
			or.setUserId(userInfo.getId());
			outLinkManDao.add(or);
			//添加操作日志
			this.addOlmLog(userInfo.getComId(), outLinkManId, userInfo.getId(),"更新了外部联系人分享范围");
		}
		return true;
	}
	
	/**
	 * 删除分享人员
	 * @param outLinkManId
	 * @param userId
	 * @param userInfo
	 * @return
	 */
	public boolean delOlmRange(Integer outLinkManId, Integer userId, UserInfo userInfo) {
		//删除私有人员范围
		outLinkManDao.delByField("outLinkManRange", new String[] { "comId", "outLinkManId","userId" },
				new Object[] { userInfo.getComId(),outLinkManId,userId });
		UserInfo user = (UserInfo) outLinkManDao.objectQuery(UserInfo.class, userId);
		//添加操作日志
		this.addOlmLog(userInfo.getComId(), outLinkManId, userInfo.getId(),"把\""+user.getUserName()+"\"从分享范围内移除");
		return true;
	}
	
	/**
	 * 修改联系方式
	 * @param olmContactWay
	 * @param userInfo
	 * @return
	 */
	public boolean updateContactWay(OlmContactWay olmContactWay, UserInfo userInfo) {
		OlmContactWay old =  outLinkManDao.queryContactWayById(olmContactWay.getId());
		outLinkManDao.update(olmContactWay);
		olmContactWay =  outLinkManDao.queryContactWayById(olmContactWay.getId());
		//添加操作日志
		this.addOlmLog(userInfo.getComId(), olmContactWay.getOutLinkManId(), userInfo.getId(),"修改联系方式由\""+old.getContactWayValue()+"："+old.getContactWay()+"\"变更为\""+olmContactWay.getContactWayValue()+"："+olmContactWay.getContactWay()+"\"");
		return true;
	}
	
	/**
	 * 修改联系地址
	 * @param olmAddress
	 * @param userInfo
	 * @return
	 */
	public boolean updateAddress(OlmAddress olmAddress, UserInfo userInfo) {
		OlmAddress old = (OlmAddress) outLinkManDao.objectQuery(OlmAddress.class, olmAddress.getId());
		outLinkManDao.update(olmAddress);
		olmAddress  = (OlmAddress) outLinkManDao.objectQuery(OlmAddress.class, olmAddress.getId());
		//添加操作日志
		this.addOlmLog(userInfo.getComId(), olmAddress.getOutLinkManId(), userInfo.getId(),"修改联系地址由\""+("0".equals(old.getAddressCode())?"办公地址":"家庭地址")+"："+old.getAddress()+"\"变更为\""+("0".equals(olmAddress.getAddressCode())?"办公地址":"家庭地址")+"："+olmAddress.getAddress()+"\"");
		return true;
	}
	
	/**
	 * 添加联系方式
	 * @param olmContactWay
	 * @param userInfo
	 * @return
	 */
	public Integer addContactWay(OlmContactWay olmContactWay, UserInfo userInfo) {
		olmContactWay.setCreator(userInfo.getId());
		olmContactWay.setComId(userInfo.getComId());
		Integer id = outLinkManDao.add(olmContactWay);
		olmContactWay =  outLinkManDao.queryContactWayById(id);
		if(id > 0) {
			//添加操作日志
			this.addOlmLog(userInfo.getComId(), olmContactWay.getOutLinkManId(), userInfo.getId(),"新增了联系方式\""+olmContactWay.getContactWayValue()+"："+olmContactWay.getContactWay()+"\"");
		}
		return id;
	}
	
	/**
	 * 添加联系地址
	 * @param olmAddress
	 * @param userInfo
	 * @return
	 */
	public Integer addAddress(OlmAddress olmAddress, UserInfo userInfo) {
		olmAddress.setCreator(userInfo.getId());
		olmAddress.setComId(userInfo.getComId());
		Integer id = outLinkManDao.add(olmAddress);
		if(id > 0) {
			//添加操作日志
			this.addOlmLog(userInfo.getComId(), olmAddress.getOutLinkManId(), userInfo.getId(),"新增了联系地址\""+("0".equals(olmAddress.getAddressCode())?"办公地址":"家庭地址")+"："+olmAddress.getAddress()+"\"");
		}
		return id;
	}
	
	/**
	 * 删除联系地址
	 * @param olmAddress
	 * @param userInfo
	 * @return
	 */
	public boolean delAddress(OlmAddress olmAddress, UserInfo userInfo) {
		olmAddress = (OlmAddress) outLinkManDao.objectQuery(OlmAddress.class, olmAddress.getId());
		outLinkManDao.delById(OlmAddress.class, olmAddress.getId());
		//添加操作日志
		this.addOlmLog(userInfo.getComId(), olmAddress.getOutLinkManId(), userInfo.getId(),"删除了联系地址\""+("0".equals(olmAddress.getAddressCode())?"办公地址":"家庭地址")+"："+olmAddress.getAddress()+"\"");
		return true;
	}
	
	/**
	 * 删除联系方式
	 * @param olmContactWay
	 * @param userInfo
	 * @return
	 */
	public boolean delContactWay(OlmContactWay olmContactWay, UserInfo userInfo) {
		olmContactWay =  outLinkManDao.queryContactWayById(olmContactWay.getId());
		outLinkManDao.delById(OlmContactWay.class, olmContactWay.getId());
		//添加操作日志
		this.addOlmLog(userInfo.getComId(), olmContactWay.getOutLinkManId(), userInfo.getId(),"删除了联系方式\""+olmContactWay.getContactWayValue()+"："+olmContactWay.getContactWay()+"\"");
		return true;
	}

	
	/**
	 * 添加外部联系人日志记录
	 * @param comId
	 * @param outLinkManId
	 * @param userId
	 * @param userName
	 * @param content
	 */
	public void addOlmLog(Integer comId,Integer outLinkManId,Integer userId,String content){
		OlmLog  olmLog = new OlmLog();
		olmLog.setComId(comId);
		olmLog.setOutLinkManId(outLinkManId);
		olmLog.setContent(content);
		olmLog.setUserId(userId);
		outLinkManDao.add(olmLog);
	}
	
	/**
	 * 分页查询留言
	 * @param outLinkManId
	 * @param comId
	 * @return
	 */
	public List<OlmTalk> listPagedOlmTalk(Integer outLinkManId, Integer comId) {
		//外部联系人的分页留言信息
		List<OlmTalk> listOlmTalk = outLinkManDao.listPagedOlmTalk(outLinkManId,comId);
		//需要返回的结果信息
		List<OlmTalk> list = new ArrayList<OlmTalk>();
		if(null!=listOlmTalk && !listOlmTalk.isEmpty()){
			//查询留言的所有附件信息
			List<OlmTalkUpfile> talkUpfiles = outLinkManDao.listOlmTalkFile(comId, outLinkManId, null);
			//缓存留言与附件关系集合
			Map<Integer, List<OlmTalkUpfile>> map = new HashMap<Integer, List<OlmTalkUpfile>>();
			
			//存在附件信息
			if(null!=talkUpfiles && !talkUpfiles.isEmpty()){
				//遍历附件集合，缓存数据
				for(OlmTalkUpfile olmTalkUpfile:talkUpfiles){
					//留言主键
					Integer olmTalkId = olmTalkUpfile.getOlmTalkId();
					//取得对应的集合信息
					List<OlmTalkUpfile> olmTalkUpfiles = map.get(olmTalkId);
					if(null == olmTalkUpfiles){
						olmTalkUpfiles = new ArrayList<OlmTalkUpfile>();
					}
					//集合添加数据
					olmTalkUpfiles.add(olmTalkUpfile);
					//缓存数据
					map.put(olmTalkId, olmTalkUpfiles);
				}
			}
			//遍历留言信息
			for (OlmTalk olmTalk : listOlmTalk) {
				//留言设置附件关联
				olmTalk.setListOlmTalkFile(map.get(olmTalk.getId()));
				list.add(olmTalk);
			}
		}
		return list;
	}
	
	/**
	 * 添加外部联系人留言
	 * @param olmTalk
	 * @throws Exception 
	 */
	public Integer addOlmTalk(OlmTalk olmTalk,UserInfo userInfo) throws Exception{
		Integer id = outLinkManDao.add(olmTalk);
		//查询外部联系人信息
		OutLinkMan outLinkMan = (OutLinkMan) outLinkManDao.objectQuery(OutLinkMan.class, olmTalk.getOutLinkManId());
		
		Integer[] upfilesId = olmTalk.getUpfilesId();
		if(null!=upfilesId){
			for (Integer upfileId : upfilesId) {
				OlmTalkUpfile upfiles = new OlmTalkUpfile();
				//企业编号
				upfiles.setComId(olmTalk.getComId());
				//外部联系人主键
				upfiles.setOutLinkManId(olmTalk.getOutLinkManId());
				//留言的主键
				upfiles.setOlmTalkId(id);
				//上传人
				upfiles.setUserId(olmTalk.getSpeaker());
				//文件主键
				upfiles.setUpfileId(upfileId);
				
				outLinkManDao.add(upfiles);
				//为附件创建索引
				uploadService.updateUpfileIndex(upfileId,userInfo,"add",olmTalk.getOutLinkManId(),ConstantInterface.TYPE_OUTLINKMAN);
			}
			
			//添加到文档中心
			fileCenterService.addModFile(userInfo, Arrays.asList(upfilesId), outLinkMan.getLinkManName());
		}

		//添加信息分享人员
		List<OutLinkManRange> listOutLinkManRange = olmTalk.getListOlmSharer();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listOutLinkManRange && !listOutLinkManRange.isEmpty()){
			for (OutLinkManRange outLinkManSharer : listOutLinkManRange) {
				//人员主键
				Integer userId = outLinkManSharer.getUserId();
				pushUserIdSet.add(userId);
                outLinkManSharer.setOutLinkManId(olmTalk.getOutLinkManId());
                outLinkManSharer.setComId(userInfo.getComId());
                outLinkManSharer.setUserId(userId);
                if(outLinkMan.getPubState()==0) {
                	Integer count = outLinkManDao.queryOlmRangeCount(userId,olmTalk.getOutLinkManId());
                    if(count < 1) {
                    	outLinkManDao.add(outLinkManSharer);
                    }
                }
                
			}
		}
		//分享信息查看
		List<UserInfo> outLinkManShares = new ArrayList<UserInfo>();
		if (null != outLinkMan) {
			//查询消息的推送人员
			outLinkManShares = outLinkManDao.listOutLinkManRanges(olmTalk.getOutLinkManId(), userInfo.getComId(),pushUserIdSet);
			Iterator<UserInfo> userids =  outLinkManShares.iterator();
			for(;userids.hasNext();){
				UserInfo user = userids.next();
				if(user.getId().equals(userInfo.getId())){
					userids.remove();
                    outLinkManShares.remove(user);
				}
				//设置全部普通消息
				todayWorksService.updateTodayWorksBusSpecTo0(ConstantInterface.TYPE_OUTLINKMAN, olmTalk.getOutLinkManId(), user.getId());
			}
			// 添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_OUTLINKMAN, olmTalk.getOutLinkManId(), "参与外部联系人讨论:" + olmTalk.getContent(),
                    outLinkManShares, userInfo.getId(), 1);

			//清除上次查看记录
			viewRecordService.delViewRecord(userInfo,outLinkManShares,olmTalk.getOutLinkManId(),ConstantInterface.TYPE_OUTLINKMAN);
		}
		return id;
	}
	/**
	 * 根据主键id查询留言详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public OlmTalk queryOlmTalk(Integer id,Integer comId){
		OlmTalk olmTalk = outLinkManDao.queryOlmTalk(id,comId);
		//外部联系人留言的附件
		if(null!=olmTalk){
			olmTalk.setListOlmTalkFile(outLinkManDao.listOlmTalkFile(comId,olmTalk.getOutLinkManId(),id));
		}
		return olmTalk;
	}

	/**
	 * 删除外部联系人留言
	 * @param olmTalk
	 * @throws Exception 
	 */
	public void delOlmTalk(OlmTalk olmTalk,UserInfo userInfo) throws Exception{
		if("yes".equals(olmTalk.getDelChildNode())){
			//留言的附件
			List<OlmTalk> olmTalks = outLinkManDao.listOlmTalkUpfileForDel(olmTalk.getComId(), olmTalk.getId());
			for (OlmTalk olmTalkSingle : olmTalks) {
				//删除留言附件
				outLinkManDao.delByField("olmTalkUpfile", new String[]{"comId","olmTalkId"}, 
						new Object[]{olmTalkSingle.getComId(),olmTalkSingle.getId()});
			}
			//删除当前节点及其子节点回复
			outLinkManDao.delOlmTalk(olmTalk.getId(),olmTalk.getComId());
		}else{
			//删除留言附件
			outLinkManDao.delByField("olmTalkUpfile", new String[]{"comId","olmTalkId"}, 
					new Object[]{olmTalk.getComId(),olmTalk.getId()});
			
			//把子节点的parentId向上提一级
			outLinkManDao.updateOlmTalkParentId(olmTalk.getId(),olmTalk.getComId());
			//删除当前节点
			outLinkManDao.delByField("olmTalk", new String[]{"comId","id"},new Integer[]{olmTalk.getComId(),olmTalk.getId()});
		}
		
	}
	
	/**
	 * 删除外部联系人留言和外部联系人关联附件
	 * @param outLinkManId 外部联系人转
	 * @param outLinkManFileId 附件关联主键
	 * @param type 类型 outLinkMan talk
	 * @return
	 * @throws Exception 
	 */
	public void delOutLinkManUpfile(Integer outLinkManFileId, String type,UserInfo userInfo,Integer outLinkManId) throws Exception {
		OlmTalkUpfile outLinkManTalkUpfile = (OlmTalkUpfile) outLinkManDao.objectQuery(OlmTalkUpfile.class, outLinkManFileId);
		outLinkManDao.delById(OlmTalkUpfile.class, outLinkManFileId);
		//模块日志添加
		Upfiles upfiles = (Upfiles) outLinkManDao.objectQuery(Upfiles.class, outLinkManTalkUpfile.getUpfileId());
		this.addOlmLog(userInfo.getComId(),outLinkManId,userInfo.getId(),"删除了外部联系人留言附件："+upfiles.getFilename());
	}
	
	/**
	 * 查询留言总数
	 * @param olmId
	 * @param user
	 * @return
	 */
	public Integer countTalks(Integer olmId, UserInfo user) {
		return outLinkManDao.countTalks(olmId,user.getComId());
	}
	
	/**
	 * 查询联系方式用于列表显示
	 * @author hcj 
	 * @param outLinkManId
	 * @param userInfo
	 * @return 
	 * @date 2018年8月25日 下午1:57:31
	 */
	public List<OlmContactWay> listContactWayForShow(Integer outLinkManId, UserInfo userInfo) {
		return outLinkManDao.listContactWayForShow(outLinkManId, userInfo);
	}
}
