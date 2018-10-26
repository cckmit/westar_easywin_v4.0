package com.westar.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Talk;
import com.westar.base.model.TalkUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.BusModVo;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.TalkDao;

import edu.emory.mathcs.backport.java.util.Arrays;

@Service
public class TalkService {

	@Autowired
	TalkDao talkDao;
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	IndexService indexService;

	@Autowired
	UploadService uploadService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	AttentionService attentionService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	LogsService logsService;
	/************************************************讨论*****************************************/
	/**
	 * 添加讨论回复
	 * @param voteTalk
	 * @throws Exception 
	 */
	public Integer addTalk(Talk talk,UserInfo userInfo,BusModVo busModVo) throws Exception {
		talk.setBusType(busModVo.getBusType());
		Integer talkId = talkDao.add(talk);
		Integer[] upfilesId = talk.getUpfilesId();
		if(null!=upfilesId && upfilesId.length>0){
			for (Integer upfileId : upfilesId) {
				TalkUpfile talkUpfile = new TalkUpfile();
				//企业编号
				talkUpfile.setComId(talk.getComId());
				//模块主键
				talkUpfile.setBusId(talk.getBusId());
				//模块类别
				talkUpfile.setBusType(busModVo.getBusType());
				//讨论主键
				talkUpfile.setTalkId(talkId);
				//附件主键
				talkUpfile.setUpfileId(upfileId);
				//上传人
				talkUpfile.setUserId(talk.getTalker());
				
				talkDao.add(talkUpfile);
				//为讨论附件创建索引
				uploadService.updateUpfileIndex(upfileId, userInfo, "add",talk.getBusId(),busModVo.getBusType());
			}
			fileCenterService.addModFile(userInfo, Arrays.asList(upfilesId), busModVo.getModName());
			
		}
		//更新索引
//		this.updateVoteIndex(voteTalk.getVoteId(),userInfo,"update");
		return talkId;
		
	}

	/**
	 * 获取单个讨论
	 * @param talkId
	 * @param comId 
	 * @return
	 */
	public Talk getTalk(Integer talkId, Integer comId,String busType) {
		Talk talk = talkDao.getTalk(talkId,comId,busType);
		//转换
		if(null!=talk){
			String content = StringUtil.toHtml(talk.getContent());
			//讨论的附件
			talk.setListTalkUpfile(talkDao.listTalkFiles(comId,talk.getBusId(),talk.getId(),busType));
			talk.setContent(content);
		}
		return talk;
	}
	/**
	 *  删除讨论的回复
	 * @param talk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> delTalk(Talk talk,String delChildNode,UserInfo userInfo,String busType) throws Exception {
		List<Integer> childIds = new ArrayList<Integer>();
		//子节点为空删除自己
		if(null==delChildNode){
			childIds.add(talk.getId());
			//删除附件
			talkDao.delByField("talkUpfile", new String[]{"comId","talkId","busType"}, new Object[]{talk.getComId(),talk.getId(),busType});
			//删除讨论
			talkDao.delById(Talk.class, talk.getId());
			
		}else if("yes".equals(delChildNode)){//删除自己和所有的子节点
			//获取待删除的讨论
			List<Talk> listTalk = talkDao.listTalkForDel(talk.getComId(), talk.getId(),busType);
			
			for (Talk delTalk: listTalk) {
				childIds.add(delTalk.getId());
				//删除讨论的附件
				talkDao.delByField("talkUpfile", new String[]{"comId","talkId","busType"}, 
						new Object[]{delTalk.getComId(),delTalk.getId(),busType});
			}
			//删除当前节点及其子节点回复
			talkDao.delTalk(talk.getId(),talk.getComId());
			
		}else if("no".equals(delChildNode)){//删除自己,将子节点提高一级
			childIds.add(talk.getId());
			//删除附件
			talkDao.delByField("talkUpfile", new String[]{"comId","talkId","busType"}, 
					new Object[]{talk.getComId(),talk.getId(),busType});
			
			talkDao.updateTalkParentId(talk.getId(),talk.getComId());
			talkDao.delById(Talk.class, talk.getId());
		}
		
		return childIds;
	}
	/**
	 * 获取前五条讨论
	 * @param comId 公司编号
	 * @param busId 模块主键
	 * @return
	 */
	public List<Talk> listFiveTalk(Integer busId,Integer comId,String busType) {
		//的讨论
		List<Talk> list = talkDao.listFiveTalk(busId,comId,busType);
		List<Talk> talks = new ArrayList<Talk>();
		for (Talk talk : list) {
			//处理回复的内容
			talk.setContent(StringUtil.toHtml(talk.getContent()));
			//讨论的附件
			talk.setListTalkUpfile(talkDao.listTalkFiles(comId,busId,talk.getId(),busType));
			talks.add(talk);
		}
		return talks;
	}
	
	/**
	 * 获取讨论
	 * @param comId 公司编号
	 * @param busId 模块主键
	 * @return
	 */
	public List<Talk> listPagedTalk(Integer busId,Integer comId,String busType) {
		//的讨论
		List<Talk> list = talkDao.listPagedTalk(busId,comId,busType);
		List<Talk> talks = new ArrayList<Talk>();
		for (Talk talk : list) {
			//处理回复的内容
			talk.setContent(StringUtil.toHtml(talk.getContent()));
			//讨论的附件
			talk.setListTalkUpfile(talkDao.listTalkFiles(comId,busId,talk.getId(),busType));
			talks.add(talk);
		}
		return talks;
	}
	/**
	 * 讨论的附件
	 * @param comId
	 * @param busId
	 * @return
	 */
	public List<TalkUpfile> listPagedFiles(Integer comId, Integer busId,String busType) {
		// 讨论附件
		List<TalkUpfile> list = talkDao.listPagedTalkFiles(comId,busId,busType);
		return list;
	}
	
	/**
	 * 查询模块的所有留言附件信息
	 * @param comId 团队号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public List<TalkUpfile> listTalkFiles(Integer comId, Integer busId,String busType) {
		// 讨论附件
		List<TalkUpfile> list = talkDao.listTalkFiles(comId,busId,null,busType);
		return list;
	}
	
	
	/**
	 * 删除讨论附件
	 * @param comId
	 * @param busId
	 * @param busType
	 */
	public void delTalkUpfiles(Integer comId, Integer busId,String busType){
		talkDao.delByField("talkUpfile", new String[]{"comId","busId","busType"}, new Object[]{comId,busId,busType});
	}
	/**
	 * 删除讨论
	 * @param comId
	 * @param busId
	 * @param busType
	 */
	public void delTalk(Integer comId, Integer busId,String busType){
		talkDao.delByField("talk", new String[]{"comId","busId","busType"}, new Object[]{comId,busId,busType});
	}
}
