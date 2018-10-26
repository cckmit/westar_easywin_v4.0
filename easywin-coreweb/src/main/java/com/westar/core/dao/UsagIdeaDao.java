package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.UsagIdea;

@Repository
public class UsagIdeaDao extends BaseDao {

	/**
	 * 分页查询常用意见
	 * @param usagIdea
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UsagIdea> listPagedUsagIdea(UsagIdea usagIdea) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.comId,a.userId,a.idea from usagIdea a");
		sql.append("\n where a.userId=? and a.comid=?");
		args.add(usagIdea.getUserId());
		args.add(usagIdea.getComId());
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), UsagIdea.class);
	}

	

}
