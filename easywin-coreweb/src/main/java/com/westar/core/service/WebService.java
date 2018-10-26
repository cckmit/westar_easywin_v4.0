package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.HelpContent;
import com.westar.base.model.HelpType;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.WebDao;

@Service
public class WebService {

	@Autowired
	WebDao webDao;
	
	/**
	 * 同级下的下一个排序序号
	 * @param pId 父级主键
	 * @return
	 */
	public int nextOrderNum(int pId){
		return webDao.nextOrderNum(pId).getNextOrderNum();
	}
	
	public void addQus(HelpType qus){
		qus.setOpenState(1);
		//先更新同级排序
		webDao.updateOrderNumByPid(qus.getpId(),qus.getOrderNum());
		webDao.add(qus);
	}
	
	/**
	 * 获取疑问列表
	 * @param nameCheck 名称比对
	 * @return
	 */
	public List<HelpType> listQus(String nameCheck,Integer pId){
		return webDao.listQus(nameCheck,pId);
	}
	
	/**
	 * 获取分类下的解答
	 * @param pId 分类主键
	 * @return
	 */
	public HelpType helpQA(Integer pId){
		HelpType helpVo = webDao.queryQus(pId);
		List<HelpType> ansList = webDao.listQA(pId);
		helpVo.setAnsList(ansList);
		return helpVo;
	}
	
	/**
	 * 帮助信息筛选
	 * @param nameCheck
	 * @return
	 */
	public List<HelpType> helpQASreach(String nameCheck){
		return webDao.helpQASreach(nameCheck);
	}
	
	/**
	 * 获取可以作为父级的疑问列表
	 * @param nameCheck 名称比对
	 * @param id 当前对象主键
	 * @return
	 */
	public List<HelpType> listQusForP(String nameCheck,Integer id){
		return webDao.listQusForP(nameCheck,id);
	}
	
	/**
	 * 疑问解答详情
	 * @param qusId 疑问主键
	 * @return
	 */
	public HelpType queryQus(Integer qusId){
		return webDao.queryQus(qusId);
	}
	/**
	 * 帮助维护更关心
	 * @param helpVo 数据参数
	 */
	public void updateHelp(HelpType helpVo){
		webDao.delByField("helpContent", new String[]{"typeKey"}, new Object[]{helpVo.getId()});
		//先更新同级排序
		webDao.updateOrderNumByPid(helpVo.getpId(),helpVo.getOrderNum());
		helpVo.setModifyTime(DateTimeUtil.getNowDateStr(1));
		webDao.update(helpVo);
		if(null!=helpVo.getContent() && !"".equals(helpVo.getContent().trim())){
			HelpContent helpContent = new HelpContent();
			helpContent.setTypeKey(helpVo.getId());
			helpContent.setContent(helpVo.getContent());
			helpContent.setModifyTime(DateTimeUtil.getNowDateStr(1));
			webDao.add(helpContent);
		}
	}
	
	/**
	 * 批量删除帮助
	 * @param ids 主键集合
	 */
	public void delHelp(Integer[] ids){
		if(null!=ids && ids.length>0){
			HelpType helpType = new HelpType();
			for(Integer id : ids){
				webDao.delByField("helpContent", new String[]{"typeKey"}, new Object[]{id});
				helpType.setId(id);
				webDao.update("update helpType a set a.pId=0 where a.pId=:id", helpType);
				webDao.delById("helpType", id);
			}
		}
	}
}
