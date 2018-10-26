package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Logs;
import com.westar.base.pojo.PageBean;

@Repository
public class LogsDao extends BaseDao {


	/**
	 * 日志记录
	 * @param comId 单位主键
	 * @param modId	模块主键	
	 * @param busType 模块类别
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageBean<Logs> listPageLog(Integer comId,Integer modId,String busType){
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select a.comId,a.userId,a.recordCreateTime,a.content,b.username,b.gender,c.uuid,c.filename  \n");
		sql.append("from logs a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.busId = ? and busType=? \n");
		args.add(comId);
		args.add(modId);
		args.add(busType);
		
		sql.append(")");
		return this.pagedBeanQuery(sql.toString(), " recordcreatetime desc", args.toArray(), Logs.class);
		
	}
	/**
	 * 最后一条日志记录
	 * @param comId
	 * @param modId
	 * @param busType
	 * @return
	 */
	public Logs queryLastLog(Integer comId, Integer modId, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select a.* from logs a  \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("where a.comId=? and a.busId = ? and busType=?  order by  a.recordcreatetime desc\n");
		args.add(comId);
		args.add(modId);
		args.add(busType);
		
		sql.append(")");
		sql.append("where rownum<=1 \n");
		return (Logs) this.objectQuery(sql.toString(), args.toArray(), Logs.class);
	}

}
