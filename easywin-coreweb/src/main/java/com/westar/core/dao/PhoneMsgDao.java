package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.PhoneMsg;
import com.westar.base.util.DateTimeUtil;

@Repository
public class PhoneMsgDao extends BaseDao{
	
	/**
	 * 查询待发送短信列表（查询前100条记录,只查询当天的）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PhoneMsg> listExpectSendSms(){
		String nowDate = DateTimeUtil.getNowDateStr(0);
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from phoneMsg where flag='0' and substr(recordCreateTime,0,10)='"+nowDate+"' and rownum<=100 order by id asc");
		
		return this.listQuery(sql.toString(), args.toArray(),PhoneMsg.class);
	}
	
	/**
	 * 将今天之前的短信修改成发送失败
	 */
	public void updateYestodayMsg(){
		String sql="update phoneMsg set flag='2' where flag='0' and substr(recordCreateTime,0,10)<'"+DateTimeUtil.getNowDateStr(0)+"'";
		this.getJdbcTemplate().execute(sql);
	}

}
