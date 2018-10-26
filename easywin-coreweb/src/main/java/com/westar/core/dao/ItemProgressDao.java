package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ItemProgress;
import com.westar.base.model.ItemProgressTemplate;
import com.westar.base.model.ItemTemplateProgress;
import com.westar.base.model.UserInfo;

@Repository
public class ItemProgressDao extends BaseDao {
	
	/**
	 * 分页查询项目进度模板
	 * @author hcj 
	 * @date: 2018年10月11日 上午9:09:42
	 * @param itemProgressTemplate
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemProgressTemplate> listPagedProgressTemplate(ItemProgressTemplate itemProgressTemplate,
			UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.userName creatorName  from itemProgressTemplate a ");
		sql.append("\n left join userInfo b on b.id = a.creator ");
		sql.append("\n where a.comId=? ");
		args.add(userInfo.getComId());
		this.addSqlWhereLike(itemProgressTemplate.getTemplateName(), sql, args, " and a.templateName like ? ");
		sql.append("\n order by a.id desc ");
		return this.pagedQuery(sql.toString(),null,args.toArray(),ItemProgressTemplate.class);
	}
	
	/**
	 * 查询所有项目进度模板
	 * @author hcj 
	 * @date: 2018年10月12日 上午9:33:31
	 * @param itemProgressTemplate
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemProgressTemplate> listProgressTemplate(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.userName creatorName  from itemProgressTemplate a ");
		sql.append("\n left join userInfo b on b.id = a.creator ");
		sql.append("\n where a.comId=? ");
		args.add(userInfo.getComId());
		sql.append("\n order by a.id desc ");
		return this.listQuery(sql.toString(),args.toArray(),ItemProgressTemplate.class);
	}
	
	/**
	 * 查询项目模板进度list
	 * @author hcj 
	 * @date: 2018年10月11日 下午1:21:57
	 * @param itemTemplateProgress
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTemplateProgress> listItemTemplateProgress(ItemTemplateProgress itemTemplateProgress) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from itemTemplateProgress a ");
		sql.append("\n where a.templateId=? ");
		args.add(itemTemplateProgress.getTemplateId());
		sql.append("\n order by a.progressOrder asc ");
		return this.listQuery(sql.toString(),args.toArray(),ItemTemplateProgress.class);
	}
	
	/**
	 * 获取项目进度未执行的第一步骤
	 * @author hcj 
	 * @date: 2018年10月12日 下午1:16:14
	 * @param itemId
	 * @return
	 */
	public ItemProgress queryMinUnStartedProgressByItemId(Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n SELECT MIN(PROGRESSORDER),id from ITEMPROGRESS  where ITEMID = ? and startTime is null and ROWNUM = 1 GROUP BY id  order by id asc");
		args.add(itemId);
		return (ItemProgress) this.objectQuery(sql.toString(),args.toArray(),ItemProgress.class);
	}
	
	/**
	 * 获取项目进度已开始的最后步骤
	 * @author hcj 
	 * @date: 2018年10月12日 下午1:16:14
	 * @param itemId
	 * @return
	 */
	public ItemProgress queryMaxStartedProgressByItemId(Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n SELECT MAX(PROGRESSORDER),id from ITEMPROGRESS  where ITEMID = ? and startTime is not null and ROWNUM = 1 GROUP BY id  order by id desc");
		args.add(itemId);
		return (ItemProgress) this.objectQuery(sql.toString(),args.toArray(),ItemProgress.class);
	}
	
	/**
	 * 根据项目id查询项目进度配置
	 * @author hcj 
	 * @date: 2018年10月15日 上午9:33:47
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemProgress> listItemProgress(Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.userName from itemProgress a ");
		sql.append("\n left join userInfo b on b.id = a.userId ");
		sql.append("\n where a.itemId=? ");
		args.add(itemId);
		sql.append("\n order by a.progressOrder asc ");
		return this.listQuery(sql.toString(),args.toArray(),ItemProgress.class);
	}
	
	/**
	 * 回复进度至之前的阶段
	 * @author hcj 
	 * @date: 2018年10月15日 下午2:10:58
	 * @param itemProgress
	 * @param userInfo
	 */
	public void updateItemProgressToOld(ItemProgress itemProgress) {
		StringBuffer sql = new StringBuffer();
		sql = new StringBuffer("\n update itemProgress set finishTime = null,userId=:userId  where id=:id ");
		this.update(sql.toString(),itemProgress);
		sql = new StringBuffer("\n update itemProgress set finishTime = null,userId=null,startTime=null  where progressOrder>:progressOrder and itemId=:itemId ");
		this.update(sql.toString(),itemProgress);
	}
	
	/**
	 * 更新项目进度
	 * @author hcj 
	 * @date: 2018年10月15日 下午2:42:53
	 * @param itemProgress
	 */
	public void updateItemProgress(ItemProgress itemProgress) {
		StringBuffer sql = new StringBuffer();
		sql = new StringBuffer("\n update itemProgress set startTime=:startTime,userId=:userId  where id=:id ");
		this.update(sql.toString(),itemProgress);
		sql = new StringBuffer("\n update itemProgress set startTime=:startTime where progressOrder<:progressOrder and startTime is null ");
		this.update(sql.toString(),itemProgress);
		sql = new StringBuffer("\n update itemProgress set finishTime=:startTime where progressOrder<:progressOrder and finishTime is null ");
		this.update(sql.toString(),itemProgress);
		sql = new StringBuffer("\n update itemProgress set userId=:userId where progressOrder<:progressOrder and userId is null ");
		this.update(sql.toString(),itemProgress);
	}

	

}
