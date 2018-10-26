package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.ItemProgress;
import com.westar.base.model.ItemProgressTemplate;
import com.westar.base.model.ItemTemplateProgress;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.core.dao.ItemProgressDao;

@Service
public class ItemProgressService {

	@Autowired
	ItemProgressDao itemProgressDao;
	
	/**
	 * 分页查询项目进度模板
	 * @author hcj 
	 * @date: 2018年10月11日 上午9:09:15
	 * @param itemProgressTemplate
	 * @param userInfo
	 * @return
	 */
	public List<ItemProgressTemplate> listPagedProgressTemplate(ItemProgressTemplate itemProgressTemplate,
			UserInfo userInfo) {
		return itemProgressDao.listPagedProgressTemplate(itemProgressTemplate,userInfo);
	}
	
	/**
	 * 查询所有项目进度模板
	 * @author hcj 
	 * @date: 2018年10月12日 上午9:33:03
	 * @param itemProgressTemplate
	 * @param userInfo
	 * @return
	 */
	public List<ItemProgressTemplate> listProgressTemplate(UserInfo userInfo) {
		return itemProgressDao.listProgressTemplate(userInfo);
	}
	
	/**
	 * 添加项目进度模板
	 * @author hcj 
	 * @date: 2018年10月11日 上午11:42:30
	 * @param itemProgressTemplate
	 * @param userInfo
	 */
	public void addProgressTemplate(ItemProgressTemplate itemProgressTemplate, UserInfo userInfo) {
		//添加模板
		itemProgressTemplate.setComId(userInfo.getComId());
		itemProgressTemplate.setCreator(userInfo.getId());
		Integer templateId = itemProgressDao.add(itemProgressTemplate);
		//添加进度阶段
		if(!CommonUtil.isNull(itemProgressTemplate.getListsItemTemplateProgress())){
			for (ItemTemplateProgress itemTemplateProgress : itemProgressTemplate.getListsItemTemplateProgress()) {
				if(!CommonUtil.isNull(itemTemplateProgress) && !CommonUtil.isNull(itemTemplateProgress.getProgressName())){
					itemTemplateProgress.setComId(userInfo.getComId());
					itemTemplateProgress.setCreator(userInfo.getId());
					itemTemplateProgress.setTemplateId(templateId);
					itemProgressDao.add(itemTemplateProgress);
				}
				
			}
		}
	}
	
	/**
	 * 根据id获取项目进度模板详情
	 * @author hcj 
	 * @date: 2018年10月11日 下午1:13:15
	 * @param id
	 * @return
	 */
	public ItemProgressTemplate queryItemProgressTemplateById(Integer id) {
		ItemProgressTemplate itemProgressTemplate = (ItemProgressTemplate) itemProgressDao.objectQuery(ItemProgressTemplate.class, id);
		if(!CommonUtil.isNull(itemProgressTemplate)){
			ItemTemplateProgress itemTemplateProgress = new ItemTemplateProgress();
			itemTemplateProgress.setTemplateId(id);
			List<ItemTemplateProgress> lists = this.listItemTemplateProgress(itemTemplateProgress);
			itemProgressTemplate.setListsItemTemplateProgress(lists);
		}
		return itemProgressTemplate;
	}
	
	/**
	 * 查询项目模板进度list
	 * @author hcj 
	 * @date: 2018年10月11日 下午1:18:17
	 * @param itemTemplateProgress
	 * @return
	 */
	private List<ItemTemplateProgress> listItemTemplateProgress(ItemTemplateProgress itemTemplateProgress) {
		return itemProgressDao.listItemTemplateProgress(itemTemplateProgress);
	}
	
	/**
	 * 更新项目进度模板
	 * @author hcj 
	 * @date: 2018年10月11日 下午2:12:11
	 * @param itemProgressTemplate
	 * @param userInfo
	 */
	public void updateProgressTemplate(ItemProgressTemplate itemProgressTemplate, UserInfo userInfo) {
		//更新模板
		itemProgressDao.update(itemProgressTemplate);
		//删除原有进度阶段
		itemProgressDao.delByField("itemTemplateProgress", new String[]{"comId","templateId"}, new Object[]{userInfo.getComId(),itemProgressTemplate.getId()});
		//添加进度阶段
		if(!CommonUtil.isNull(itemProgressTemplate.getListsItemTemplateProgress())){
			for (ItemTemplateProgress itemTemplateProgress : itemProgressTemplate.getListsItemTemplateProgress()) {
				if(!CommonUtil.isNull(itemTemplateProgress) && !CommonUtil.isNull(itemTemplateProgress.getProgressName())){
					itemTemplateProgress.setComId(userInfo.getComId());
					itemTemplateProgress.setCreator(userInfo.getId());
					itemTemplateProgress.setTemplateId(itemProgressTemplate.getId());
					itemProgressDao.add(itemTemplateProgress);
				}
			}
		}
	}
	
	/**
	 * 批量删除项目进度模板
	 * @author hcj 
	 * @date: 2018年10月11日 下午2:33:10
	 * @param ids
	 * @param userInfo
	 */
	public void delProgressTemplates(Integer[] ids, UserInfo userInfo) {
		if(!CommonUtil.isNull(ids)){
			for (Integer id : ids) {
				//删除模板进度
				itemProgressDao.delByField("itemTemplateProgress", new String[]{"comId","templateId"}, new Object[]{userInfo.getComId(),id});
				//删除模板
				itemProgressDao.delById(ItemProgressTemplate.class, id);
			}
		}
	}
	
	/**
	 * 克隆模板
	 * @author hcj 
	 * @date: 2018年10月11日 下午2:59:53
	 * @param id
	 * @param userInfo
	 */
	public void addCopyProgressTemplate(Integer id, UserInfo userInfo) {
		ItemProgressTemplate itemProgressTemplate = this.queryItemProgressTemplateById(id);
		if(!CommonUtil.isNull(itemProgressTemplate)){
			itemProgressTemplate.setTemplateName(itemProgressTemplate.getTemplateName() + "(克隆)");
			this.addProgressTemplate(itemProgressTemplate, userInfo);
		}
		
	}
	
	/**
	 * 获取项目进度未执行的第一步骤
	 * @author hcj 
	 * @date: 2018年10月12日 下午1:14:11
	 * @param itemId
	 * @return
	 */
	public ItemProgress queryMinUnStartedProgressByItemId(Integer itemId) {
		return itemProgressDao.queryMinUnStartedProgressByItemId(itemId);
	}
	
	
	/**
	 * 获取项目进度已开始的最后一步骤
	 * @author hcj 
	 * @date: 2018年10月12日 下午1:14:11
	 * @param itemId
	 * @return
	 */
	public ItemProgress queryMaxStartedProgressByItemId(Integer itemId) {
		return itemProgressDao.queryMaxStartedProgressByItemId(itemId);
	}
	
	/**
	 * 根据项目id查询项目进度配置
	 * @author hcj 
	 * @date: 2018年10月15日 上午9:32:53
	 * @param itemId
	 * @return
	 */
	public List<ItemProgress> listItemProgress(Integer itemId) {
		return itemProgressDao.listItemProgress(itemId);
	}
	
	/**
	 * 回复项目进度
	 * @author hcj 
	 * @date: 2018年10月15日 下午1:57:26
	 * @param itemProgress
	 * @param userInfo 
	 * @param userInfo
	 */
	public void updateItemProgressToOld(ItemProgress itemProgress) {
		itemProgressDao.updateItemProgressToOld(itemProgress);
	}
	
	/**
	 * 更新进度
	 * @author hcj 
	 * @date: 2018年10月15日 下午2:39:18
	 * @param itemProgress
	 */
	public void updateItemProgress(ItemProgress itemProgress) {
		itemProgressDao.updateItemProgress(itemProgress);
	}
	
}
