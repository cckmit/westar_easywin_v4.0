package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.DataDic;

@Repository
public class DataDicDao extends BaseDao {

	/**
	 * 增加字典表
	 * @param dataDic
	 * @return 返回主键ID
	 */
	public Integer addDataDic(DataDic dataDic) {
		return this.add(dataDic);
	}

	/**
	 * 根据id删除字典表
	 * @param id 要删除的信息的主键ID
	 */
	public void delDataDic(Integer id) {
		this.delById(DataDic.class, id);
	}

	/**
	 * 根据id批量删除字典表
	 * @param ids 要删除的信息的主键ID
	 */
	public void delDataDic(Integer[] ids) {
		this.delById(DataDic.class, ids);
	}

	/**
	 * 更新字典表
	 * @param dataDic
	 */
	public void updateDataDic(DataDic dataDic) {
		this.update(dataDic);
	}
	
	/**
	 * 查询字典表数量
	 * @return 数量
	 */
	public int countDataDic(){
		return this.countQuery(DataDic.class);
	}

	/**
	 * 根据id查询字典表
	 * @param id 主键ID
	 * @return 字典表信息
	 */
	public DataDic getDataDicById(Integer id) {
		return (DataDic) this.objectQuery(DataDic.class,id);
	}
	
	/**
	 * 查询字典表列表
	 * @param dataDic
	 * @return 字典表信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<DataDic>  listDataDic(DataDic dataDic){
		List<Object> args=new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("select  *  from datadic a where 1=1 ");
		this.addSqlWhere(dataDic.getMaintainable(), sql, args, " and a.maintainable=?");
		return this.listQuery(sql.toString(), args.toArray(), DataDic.class);
	}
	
	/**
	 * 查询字典表树形列表
	 * @return 字典表信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<DataDic>  listTreeDataDic(){
		StringBuffer sql=new StringBuffer();
		sql.append("select  id,parentId,maintainable,type,code,zvalue,zdescribe,LEVEL,");
		sql.append("case when level>2 then  SUBSTR(SYS_CONNECT_BY_PATH(zvalue,'>>'),5) else zvalue end  pathZvalue ");
		sql.append("from datadic START WITH parentID = -1 CONNECT BY PRIOR ID = parentID ORDER SIBLINGS BY id");
		return this.listQuery(sql.toString(), null, DataDic.class);
	}
	
	/**
	 * 根据类型和代码查询字典表树形列表
	 * @param type 字典表类别 
	 * @param code 字典表字典代码
	 * @return 字典表信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<DataDic>  listTreeDataDicByTypeAndCode(String type,String code){
		List<Object> args=new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("\n select  id,parentId,maintainable,type,code,zvalue,zdescribe,LEVEL,");
		sql.append("\n case when level>2 then  SUBSTR(SYS_CONNECT_BY_PATH(zvalue,'>>'),3) else zvalue end  pathZvalue ");
		sql.append("\n from datadic START WITH type=? and code=? CONNECT BY PRIOR ID = parentID ORDER SIBLINGS BY id");
		args.add(type);
		args.add(code);
		return this.listQuery(sql.toString(), args.toArray(), DataDic.class);
	}
	
	/**
	 *  查询字典表列表
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataDic>  listDataDic(String type){
		List<Object> args=new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("select  *  from datadic a where 1=1 and a.parentid!=-1 ");
		this.addSqlWhere(type, sql, args, " and a.type=?");
		sql.append("order by a.id asc ");
		return this.listQuery(sql.toString(), args.toArray(), DataDic.class);
	}
	
}
