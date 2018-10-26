package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.JpushRegiste;

@Repository
public class JpushRegisteDao extends BaseDao {

	/**
	 * 通过用户取得极光注册标志
	 * @param userId 用户主键
	 * @return
	 */
	public JpushRegiste getJPushByUserId(Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from  jpushRegiste where userId=?");
		args.add(userId);
		return (JpushRegiste) this.objectQuery(sql.toString(), args.toArray(), JpushRegiste.class);
	}

}
