package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.model.Department;

@Repository
public class DepartmentDao extends BaseDao {
	
	/**
	 * 树形查找部门信息
	 * @param id 部门主键
	 * @param comId 公司主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Department> listTreeDep(Integer id, String enabled, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select case when rownum=1 then 1 else 0 end defDep,id,depname,parentid,enabled from department where 1=1");
		this.addSqlWhere(enabled, sql, args, " and enabled=? ");
		this.addSqlWhere(comId, sql, args, " and comId=? ");
		sql.append("\n start with parentid=-1 CONNECT BY PRIOR id = parentid");
		return this.listQuery(sql.toString(), args.toArray(), Department.class);
	}
	/**
	 * 查询本部门和下级部门信息
	 * @param depId 本门主键
	 * @param enabled 是否启用
	 * @param comId 团队号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Department> listTreeSonDep(Integer depId, String enabled, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select case when rownum=1 then 1 else 0 end defDep,id,depname,parentid,enabled from department where 1=1");
		this.addSqlWhere(enabled, sql, args, " and enabled=? ");
		this.addSqlWhere(comId, sql, args, " and comId=? ");
		sql.append("\n start with id=? CONNECT BY PRIOR id = parentid");
		args.add(depId);
		return this.listQuery(sql.toString(), args.toArray(), Department.class);
	}
	/**
	 * 查询本部门和下级部门信息
	 * @param depIds 本门主键
	 * @param enabled 是否启用
	 * @param comId 团队号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Department> listTreeSonDep(Integer[] depIds, String enabled, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select case when rownum=1 then 1 else 0 end defDep,id,depname,parentid,enabled from department where 1=1");
		this.addSqlWhere(enabled, sql, args, " and enabled=? ");
		this.addSqlWhere(comId, sql, args, " and comId=? ");
		this.addSqlWhereIn(depIds, sql, args, "\n start with id in ? CONNECT BY PRIOR id = parentid");
		return this.listQuery(sql.toString(), args.toArray(), Department.class);
	}

	/**
	 * 取得部门信息
	 * @param id
	 * @param comId  公司主键
	 * @return
	 */
	public Department getDep(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,case when b.depName is null then  c.orgName else b.depName end  parentName from department a ");
		sql.append("\n left join department b on a.parentId=b.id");
		sql.append("\n  left join organic c on a.comid=c.orgNum");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return (Department) this.objectQuery(sql.toString(), args.toArray(), Department.class);
	}

	/**
	 * 获得组织机构树JSON数据
	 * @param dep
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Department> listTreeOrganization(Department dep){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();

		sql.append("select * from ( \n");
		sql.append("select a.*,connect_by_root a.id as rootId,level as depLevel,connect_by_isleaf isleaf from department a \n");
		sql.append("where  a.comid = ? and enabled=1 \n");
		sql.append("start with a.parentid=? \n");
		args.add(dep.getComId());
		args.add(dep.getParentId());
		sql.append("connect by prior a.id = a.parentid \n");
		sql.append(") where 1 = 1 \n");

		//如果有rootId则去掉该部门以及所有下属部门
		this.addSqlWhere(dep.getRootId(),sql,args," and rootId <> ? \n");
		this.addSqlWhere(dep.getRootId(),sql,args," and parentId <> ? \n");
		this.addSqlWhere(dep.getRootId(),sql,args," and id <> ?");

		return this.listQuery(sql.toString(), args.toArray(), Department.class);
	}
	
	/**
	 * 禁用启用部门
	 * @param ids  部门主键id
	 * @param enabled  启用状态 0禁用 1启用
	 * @param comId 公司主键
	 */
	public void updateDepEnabled(Integer[] ids, String enabled, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		sql.append("update department set enabled=? where id=? and comId=?");
		for (Integer id : ids) {
			batchArgs.add(new Object[] { enabled, id ,comId});
		}
		this.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
	}

	/**
	 * 取得默认的部门信息
	 * @param comId
	 * @return
	 */
	public Department getDefalutDep(Integer comId) {
		StringBuffer sql = new StringBuffer("select * from (");
		sql.append("\n select rownum, a.* from department a where a.comId=? order by id asc");
		sql.append("\n ) a where rownum=1");
		return (Department) this.objectQuery(sql.toString(), new Object[]{comId}, Department.class);
	}

	/**
	 * 部门成员设置部门信息
	 * @param depId 部门信息
	 * @param comId 企业编号
	 * @param userId 用户主键
	 */
	public void updateUserDep(Integer depId, Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set depId=? where comId=? and  userId=? and ENABLED=1 and INSERVICE=1");
		args.add(depId);
		args.add(comId);
		args.add(userId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 设置为默认部门
	 * @param defDepId 默认部门主键
	 * @param depId 部门主键
	 * @param comId 企业编号
	 */
	public void updateUserDefDep(Integer defDepId,Integer depId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set depId=? where comId=? and  depId=? and ENABLED=1 and INSERVICE=1");
		args.add(defDepId);
		args.add(comId);
		args.add(depId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	
	/**
	 * 查询部门的树形所有父节点
	 * @param comId
	 * @return
	 */
	public void initDepTree(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into deptree(comId,depId,parentid ) ");
		sql.append("\n (");
		sql.append("\n  	select comId,depId,parentid from (");
		sql.append("\n 			select a.comId,a.depid,REGEXP_SUBSTR(a.pDepIds,'[^,]+',1,l) parentid from (");
		sql.append("\n 				select a.comid,a.id depId,SUBSTR(SYS_CONNECT_BY_PATH(a.parentid,','),2) pDepIds,level newLevel");
		sql.append("\n 				from department a");
		sql.append("\n 				where a.comId=?");
		args.add(comId);
		sql.append("\n 				start with a.parentid=-1");
		sql.append("\n 				connect by prior a.id=a.parentid");
		sql.append("\n 				order siblings by a.id");
		sql.append("\n 			)a");
		sql.append("\n 			,(select LEVEL l FROM DUAL CONNECT BY LEVEL<=10)b");
		sql.append("\n 			WHERE l <=newLevel");
		sql.append("\n 		) ");
		sql.append("\n ) ");
		this.excuteSql(sql.toString(), args.toArray());
	}

	

}
