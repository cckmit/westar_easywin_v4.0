package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Intro;
import com.westar.base.model.UserInfo;

@Repository
public class IntroDao extends BaseDao {
	
	/**
	 * 查询引导
	 * @param busType
	 * @param userInfo
	 * @return
	 */
	public Intro queryIntroState(String busType, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from intro where comId = ? and userId = ? and introState = 0 and busType= ?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(busType);
		return (Intro) this.objectQuery(sql.toString(), args.toArray(), Intro.class);
	}


}
