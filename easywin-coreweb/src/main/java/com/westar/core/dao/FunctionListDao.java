package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.FunctionList;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class FunctionListDao extends BaseDao {
	
	/**
	 * 获取功能功能清单树
	 * @author hcj 
	 * @date: 2018年10月17日 下午1:22:22
	 * @param userInfo
	 * @param functionList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FunctionList> listTreeFun(UserInfo userInfo, FunctionList functionList) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from functionList where 1=1");
		this.addSqlWhere(functionList.getBusId(), sql, args, " and busId=? ");
		this.addSqlWhere(functionList.getBusType(), sql, args, " and busType=? ");
		this.addSqlWhereLike(functionList.getFunctionName(), sql, args, " and functionName like ? ");
		sql.append("\n start with parentid=-1 CONNECT BY PRIOR id = parentid");
		sql.append("\n order by id desc");
		return this.listQuery(sql.toString(), args.toArray(), FunctionList.class);
	}
	
	/**
	 * 根据id获取详情
	 * @author hcj 
	 * @date: 2018年10月17日 下午4:16:45
	 * @param id
	 * @return
	 */
	public FunctionList queryFunById(Integer id) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,case when a.parentId != -1 then b.functionName else nvl(c.itemName,d.name) end busName from functionList a");
		sql.append("\n left join functionList b on b.id=a.parentId ");
		sql.append("\n left join item c on c.id=a.busId and a.busType=? ");
		args.add(ConstantInterface.TYPE_ITEM);
		sql.append("\n left join product d on d.id=a.busId and a.busType=? ");
		args.add(ConstantInterface.TYPE_PRODUCT);
		sql.append("\n where a.id=? ");
		args.add(id);
		return (FunctionList) this.objectQuery(sql.toString(), args.toArray(), FunctionList.class);
	}
	
	/**
	 * 根据选择的项目或者产品导入功能清单
	 * @author hcj 
	 * @date: 2018年10月18日 上午10:34:18
	 * @param busId
	 * @param busType
	 * @param chooseBusType
	 * @param chooseBusId
	 * @param userInfo
	 */
	public void addImportFunctionList(Integer busId, String busType, String chooseBusType, Integer chooseBusId,
			UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n insert into functionList(comId,creator,busType,busId,parentId,functionName,functionDescribe)");
		sql.append("\n select ?,?,?,?,parentId,functionName,functionDescribe ");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(busId);
		args.add(busType);
		sql.append("\n from functionList where busId=? and busType=?");
		args.add(chooseBusId);
		args.add(chooseBusType);
		this.excuteSql(sql.toString(), args.toArray());
	}

	

}
