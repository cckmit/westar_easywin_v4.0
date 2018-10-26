package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.HelpType;

@Repository
public class WebDao extends BaseDao {
	
	/**
	 * 获取疑问列表
	 * @param nameCheck 名称比对
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HelpType> listQus(String nameCheck,Integer pId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,nvl(b.sunNum,0) as sunNum from helpType a");
		sql.append("\n left join");
		sql.append("\n (");
		sql.append("\n  select pid,count(pid) as sunNum from helpType where pid!=0 group by pid");
		sql.append("\n ) b on a.id = b.pid");
		sql.append("\n where 1=1");
		this.addSqlWhereLike(nameCheck, sql, args,"\n and a.name like ?");
		if(null!=pId && 0!=pId){
			sql.append("\n and a.pId=?");
			args.add(pId);
		}else{
			sql.append("\n and a.pId=0");
		}
		sql.append("\n order by a.openstate desc,a.ordernum asc");
//		sql.append("\n start with a.pid=0 CONNECT BY PRIOR a.id = a.pid");
//		sql.append("\n order siblings by a.ordernum asc");
		return this.listQuery(sql.toString(), args.toArray(),HelpType.class);
	}
	
	/**
	 * 获取分类解答集合
	 * @param pId 分类主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HelpType> listQA(Integer pId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.content from helpType a");
		sql.append("\n left join");
		sql.append("\n helpContent b on a.id = b.typekey ");
		sql.append("\n where a.pid=? and a.openstate=1");
		args.add(pId);
		sql.append("\n order by a.ordernum");
		return this.listQuery(sql.toString(), args.toArray(),HelpType.class);
	}
	
	/**
	 * 帮助信息筛选
	 * @param nameCheck 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HelpType> helpQASreach(String nameCheck){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");
		sql.append("\n select a.*,b.content,nvl(c.sunNum,0) as sunNum from helpType a");
		sql.append("\n left join");
		sql.append("\n helpContent b on a.id = b.typekey ");
		sql.append("\n left join");
		sql.append("\n (");
		sql.append("\n  select pid,count(pid) as sunNum from helpType where pid!=0 group by pid");
		sql.append("\n ) c on a.id = c.pid");
		sql.append("\n where a.pid<>0 and a.openstate=1");
		this.addSqlWhereLike(nameCheck, sql, args,"\n and a.name like ?");
		sql.append("\n ) a where a.sunNum=0 ");
		return this.pagedQuery(sql.toString(),null, args.toArray(),HelpType.class);
	}
	
	/**
	 * 获取可以作为父级的疑问列表
	 * @param nameCheck 名称比对
	 * @param id 当前对象主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HelpType> listQusForP(String nameCheck,Integer id){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from helpType a");
		sql.append("\n where a.pid=0 and a.openstate=1");
		if(null!=id){
			sql.append("\n and a.id<>?");
			args.add(id);
		}
		this.addSqlWhereLike(nameCheck, sql, args,"\n and a.name like ?");
		return this.pagedQuery(sql.toString(),"a.ordernum asc", args.toArray(),HelpType.class);
	}
	/**
	 * 
	 * 同级下的下一个排序序号
	 * @param pId 父级主键
	 * @return
	 */
	public HelpType nextOrderNum(int pId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select nvl(max(a.ordernum)+1,1) as nextOrderNum from");
		sql.append("\n Helptype a where a.pid=?");
		args.add(pId);
		return (HelpType) this.objectQuery(sql.toString(), args.toArray(),HelpType.class);
	}
	
	/**
	 * 更新同级排序
	 * @param pId 父级主键
	 * @param orderNum 即将插入的新排序号
	 */
	public void updateOrderNumByPid(int pId,int orderNum){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update helptype a set a.ordernum=a.ordernum+1");
		sql.append("\n  where a.openstate=1 and a.pid=? and a.ordernum>=?");
		args.add(pId);
		args.add(orderNum);
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 疑问解答详情
	 * @param qusId 疑问主键
	 * @return
	 */
	public HelpType queryQus(Integer qusId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.content,c.name as pName,nvl(d.sunNum,0) as sunNum from Helptype a");
		sql.append("\n left join Helpcontent b on a.id = b.typekey");
		sql.append("\n left join Helptype c on a.pid=c.id");
		sql.append("\n left join");
		sql.append("\n (");
		sql.append("\n  select pid,count(pid) as sunNum from helpType where pid!=0 group by pid");
		sql.append("\n ) d on a.id = d.pid");
		sql.append("\n  where a.id=?");
		args.add(qusId);
		return (HelpType) this.objectQuery(sql.toString(), args.toArray(),HelpType.class);
	}

}
