package com.westar.core.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BiaoQing;

@Repository
public class BiaoQingDao extends BaseDao {
	
	/**
	 * 查询表情包集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BiaoQing> listBiaoQing(){
		String sql ="select a.* from BiaoQing a order by a.recordcreatetime asc ";
		return this.listQuery(sql, null, BiaoQing.class);
	}
	/**
	 * 根据ID删除表情数据库表数据
	 * @param ids
	 */
	public void delBiaoQing(Integer[] ids){
		this.delById(BiaoQing.class, ids);
	}
}
