package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Jfzb;
import com.westar.base.model.JfzbDepScope;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.core.dao.JfzbDao;
import com.westar.core.web.PaginationContext;

@Service
public class JfzbService {

	@Autowired
	JfzbDao jfzbDao;

	/**
	 * 分页查询积分指标信息
	 * @param jfzb 积分指标查询条件
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<Jfzb> listPagedJfbz(Jfzb jfzb, UserInfo sessionUser) {
		return jfzbDao.listPagedJfbz(jfzb,sessionUser);
	}
	/**
	 * 添加积分指标
	 * @param jfzb积分指标信息
	 * @param sessionUser 当前操作人员
	 */
	public void addJfzb(Jfzb jfzb, UserInfo sessionUser) {
		
		//设置团队号
		jfzb.setComId(sessionUser.getComId());
		//设置录入人员
		jfzb.setRecorderId(sessionUser.getId());
		//添加积分指标
		Integer jfzbId = jfzbDao.add(jfzb);
		//addJfzbDepScope
		this.addJfzbDepScope(jfzb, sessionUser, jfzbId);
		
	}
	/**
	 * 通过主键查询积分标准信息
	 * @param jfzbId 积分标准主键
	 * @return
	 */
	public Jfzb queryJfzbById(Integer jfzbId) {
		Jfzb jfzb  = jfzbDao.queryJfzbById(jfzbId);
		if(null!=jfzb){
			List<JfzbDepScope> listJfzbDepScope = this.listJfzbDepScope(jfzbId);
			jfzb.setListJfzbDepScope(listJfzbDepScope);
		}
		return jfzb;
	}
	/**
	 * 查询适用部门范围
	 * @param jfzbId 积分标准主键
	 * @return
	 */
	private List<JfzbDepScope> listJfzbDepScope(Integer jfzbId) {
		return jfzbDao.listJfzbDepScope(jfzbId);
	}
	/**
	 * 修改积分标准
	 * @param jfzb 积分标准信息
	 * @param sessionUser 当前操作人员
	 */
	public void updateJfzb(Jfzb jfzb, UserInfo sessionUser) {
		Integer jfzbId = jfzb.getId();
		jfzbDao.update(jfzb);
		
		//删除原有数据
		jfzbDao.delByField("jfzbDepScope", new String[]{"comId","jfzbId"}, 
				new Object[]{sessionUser.getComId(),jfzbId});
		//addJfzbDepScope
		this.addJfzbDepScope(jfzb, sessionUser, jfzbId);
				
		
	}
	/**
	 * 添加积分标准范围
	 * @param jfzb 积分标准
	 * @param sessionUser 当前操作人员
	 * @param jfzbId  积分标准主键
	 */
	public void addJfzbDepScope(Jfzb jfzb, UserInfo sessionUser, Integer jfzbId) {
		//
		List<JfzbDepScope> jfzbDepScopes = jfzb.getListJfzbDepScope();
		if(null != jfzbDepScopes && !jfzbDepScopes.isEmpty()){
			for (JfzbDepScope jfzbDepScope : jfzbDepScopes) {
				//设置团队号
				jfzbDepScope.setComId(sessionUser.getComId());
				//设置积分标准主键
				jfzbDepScope.setJfzbId(jfzbId);
				//添加范围
				jfzbDao.add(jfzbDepScope);
			}
		}
	}
	/**
	 * 删除积分标准信息
	 * @param ids 积分标准主键集合信息
	 * @param sessionUser 当前操作人员
	 */
	public void deleteJfzb(Integer[] ids, UserInfo sessionUser) {
		if(null!=ids && ids.length>0){
			for (Integer jfzbId : ids) {
				jfzbDao.delByField("jfzbDepScope", new String[]{"comId","jfzbId"}, 
						new Object[]{sessionUser.getComId(),jfzbId});
			}
			//删除数据信息
			jfzbDao.delById(Jfzb.class, ids);
		}
		
	}
	/**
	 * 分页查询积指标信息
	 * @param jfzb 积分指标信息
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public List<Jfzb> listAllJfzb(Jfzb jfzb, UserInfo userInfo) {
		// 验证当前登录人是否是督察人员
		List<Jfzb> jfzbs = jfzbDao.listAllJfzb(jfzb,userInfo);
		return jfzbs;
	}
	
	
	
}
