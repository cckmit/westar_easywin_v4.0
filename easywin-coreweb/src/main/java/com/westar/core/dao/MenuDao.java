package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.westar.base.model.AddSoon;
import com.westar.base.model.AddSoonSet;
import com.westar.base.model.Menu;
import com.westar.base.model.MenuHead;
import com.westar.base.model.MenuHome;
import com.westar.base.model.MenuHomeSet;
import com.westar.base.model.MenuRate;
import com.westar.base.model.MenuSet;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class MenuDao extends BaseDao {

	final static String LOCAL_IP = "127.0.0.1";

	/**
	 * 根据id查询该菜单下的所有子节点，如果id为null则查询所有
	 * @param id 菜单主键id
	 * @param businessType 菜单业务类别,详细见字典表businessType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listTreeMenu(Integer id, String enabled) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from menu_view a where 1=1");
		this.addSqlWhere(enabled, sql, args, " and a.enabled=? ");
		sql.append("\n order by a.orderno");
		return this.listQuery(sql.toString(), args.toArray(), Menu.class);
	}

	/**
	 * 查询指定的人员有权限并且启用的菜单 
	 * @param id 菜单主键id
	 * @param userid  人员主键id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listRoleMenu(Integer id, Integer userid, String ip) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from menu a where a.enabled=1 ");
		/* 如果不是本机访问 则只显示menuRole=0 的 */
		if (!ip.equals(LOCAL_IP)) {
			sql.append(" and a.menuRole='0'");
		}
		if (id != null) {
			sql.append(" and a.parentId=? ");
			args.add(id);
		} else {
			sql.append(" and a.parentId=-1 ");
		}
		sql.append("\n order by a.orderNo");
		return this.listQuery(sql.toString(), args.toArray(), Menu.class);
	}
	
	/**
	 * 根据父级权限代码查询菜单
	 * @param authCode
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listMenuByParentAuthCode(String parentAuthCode,Integer userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from menu a left join menu b on (a.parentId=b.id) ");
		sql.append("\n where a.enabled=1  and b.authCode=? ");
		sql.append("\n and a.id in(select  distinct  menuId  from roleMenu a inner join roleUser b on(a.roleId=b.roleId) where b.userId=? )");
		sql.append("\n order by a.orderNo");
		return this.listQuery(sql.toString(),new Object[]{parentAuthCode,userId}, Menu.class);
	}
	
	/**
	 * 查询某人是否具有某个菜单的权限
	 * @param authCode
	 * @param userId
	 * @return
	 */
	public boolean authAccess(String authCode,Integer userId){
		int count=0;
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select count(a.id) from  menu a where  a.enabled=1 and a.authCode=? ");
		sql.append("\n  and a.id in(select  distinct  menuId  from roleMenu a inner join roleUser b on(a.roleId=b.roleId) where b.userId=? )  ");
		//sql.append("\n start  with a.parentId=-1 ");
		//sql.append("\n connect by prior a.id=a.parentId");
		try {
			count=this.getJdbcTemplate().queryForObject(sql.toString(),new Object[]{authCode,userId}, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			//不作处理
		}
		if(count>0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 根据parentId查询该父节点下所有子节点的最大排序号
	 * @param parentId 父菜单主键id
	 * @return
	 */
	public int getCurrentLevelMaxOrderNo(int parentId) {
		String sql = "select case when max(orderNo) is null then 0 else  max(orderNo) end  from menu where parentId=?";
		return this.countQuery(sql, new Object[] { parentId });
	}

	/**
	 * 查询菜单详细    查询详细信息
	 * @param id 菜单主键id
	 * @return
	 */
	public Menu getMenu(Integer id) {
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,case when b.menuName is null then '根菜单' else b.menuName end  parentName from menu a ");
		sql.append("\n left join menu b on a.parentId=b.id");
		sql.append("\n where 1=1 and a.id=?");
		return (Menu) this.objectQuery(sql.toString(), new Object[] { id }, Menu.class);
	}

	/**
	 * 禁用启用菜单
	 * @param ids  菜单主键id
	 * @param enabled  启用状态 0禁用 1启用
	 */
	public void updateMenuEnabled(Integer[] ids, String enabled) {
		StringBuffer sql = new StringBuffer();
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		sql.append("update menu set enabled=? where id=? ");
		for (Integer id : ids) {
			batchArgs.add(new Object[] { enabled, id });
		}
		this.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
	}

	/**
	 * 根据parentId和preOrderNo查询该父级节点下的排序号大于或者等于preOrderNo的子节点
	 * @param parentId 父菜单主键id
	 * @param preOrderNo  父菜单排序号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listMenuByParentIdAndPreOrderNo(Integer parentId, Integer preOrderNo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("select a.id from menu a where 1=1 ");
		this.addSqlWhere(parentId, sql, args, " and a.parentId=?");
		this.addSqlWhere(preOrderNo, sql, args, " and a.orderNo>=?");
		return this.listQuery(sql.toString(), args.toArray(), Menu.class);
	}

	/**
	 * 菜单排序号向后位移一位
	 * @param ids 菜单主键id
	 */
	public void offset(Integer[] ids) {
		StringBuffer sql = new StringBuffer();
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		sql.append("update menu set orderNo=orderNo+1 where id=? ");
		for (Integer id : ids) {
			batchArgs.add(new Object[] { id });
		}
		this.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
	}
	
	/**
	 * 根据权限代码查询菜单数量
	 * @param authCode
	 * @return
	 */
	public int countByAuthCode(String authCode) {
		String sql = " select count(id) as countNum from menu where authCode=? ";
		return this.countQuery(sql, new Object[] { authCode });
	}

	/**
	 * 判断大模块的权限
	 * @param businesstype
	 * @param userId
	 * @return
	 */
	public boolean authMenuAccess(String businesstype, Integer userId) {

		int count=0;
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select case when tb.countNum is null then 0 else tb.countNum end as countNum from(");
		sql.append("\n select  count(menuId) as countNum  from roleMenu a ");
		sql.append("\n inner join roleUser b on(a.roleId=b.roleId) ");
		sql.append("\n inner join menu c on(a.menuId=c.id) ");
		sql.append("\n where b.userId=? and c.businesstype=? ");
		sql.append("\n ) tb ");
		try {
			args.add(userId);
			args.add(businesstype);
			count=this.getJdbcTemplate().queryForObject(sql.toString(),args.toArray(), Integer.class);
		} catch (EmptyResultDataAccessException e) {
			//不作处理
		}
		if(count>0){
			return true;
		}else{
			return false;
		}
	
	}

	/**
	 * 添加菜单使用频率
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 */
	public void addMenuRate(UserInfo sessionUser, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update menuRate set rate=rate+1 where comId=? and userid=? and bustype=?");
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		args.add(busType);
		
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		//没有修改到数据
		if(result == 0){
			//菜单使用频率
			MenuRate menuRate = new MenuRate();
			//所在企业号
			menuRate.setComId(sessionUser.getComId());
			//用户主键
			menuRate.setUserId(sessionUser.getId());
			//业务类型
			menuRate.setBusType(busType);
			//第一次使用
			menuRate.setRate(1);
			this.add(menuRate);
		}
	}

	/**
	 * 取得头部的显示菜单
	 * @param comId 团队号
	 * @param userId 当前操作员主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MenuHead> listHeadMenu(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,"+comId+" as comId,"+userId+" as userId,");
		sql.append("\n 	case when b.rate is null then 0 ");
		sql.append("\n 		when a.busType='003' or a.busType='012' or a.busType='005' then b.rate+1 else b.rate end usedRate,");
		sql.append("\n 	case when a.busType='003' or a.busType='012' or a.busType='005' then 1 else 0 end newOrder2 ");
		sql.append("\n 	from menuHead a left join menuRate b ");
		sql.append("\n on a.busType=b.bustype ");
		this.addSqlWhere(comId, sql, args, "and b.comid=?");
		this.addSqlWhere(userId, sql, args, "and b.userId=?");
		sql.append("\n order by usedRate desc,newOrder2 desc,a.id");
		return this.listQuery(sql.toString(), args.toArray(), MenuHead.class);
	}

	/**
	 * 查询菜单设置信息
	 * @param comId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MenuSet> listMenuSet(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.menuname,a.id menuHead,b.id, ");
		sql.append("\n 	case when b.opened is null then");
		sql.append("\n  	case when a.bustype='003' or a.bustype='005' or a.bustype='012' then '1' else '0' end");
		sql.append("\n   else b.opened end opened");
		sql.append("\n from menuhead a left join menuSet b on a.id =b.menuHead ");
		this.addSqlWhere(comId, sql, args, "and b.comid=?");
		this.addSqlWhere(userId, sql, args, "and b.userId=?");
		return this.listQuery(sql.toString(), args.toArray(), MenuSet.class);
	}
	
	/**
	 * 添修改头部菜单显示信息
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 */
	public void updateMenuSet(UserInfo sessionUser, Integer menuHead,String openState) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update menuSet set opened=? where comId=? and userid=? and menuHead=?");
		args.add(openState);
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		args.add(menuHead);
		
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		//没有修改到数据
		if(result == 0){
			//菜单使用频率
			MenuSet menuSet = new MenuSet();
			//所在企业号
			menuSet.setComId(sessionUser.getComId());
			//用户主键
			menuSet.setUserId(sessionUser.getId());
			//菜单主键
			menuSet.setMenuHead(menuHead);
			//设置使用状态
			menuSet.setOpened(openState);
			this.add(menuSet);
		}
	}

	/**
	 * 取得首页需要展示的模块
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @param countSub 下属数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MenuHome> listMenuHome(Integer comId, Integer userId,Integer countSub) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.*,case when a.newOrder is null then rownum else a.newOrder end as orderNo from(");
		sql.append("\n select a.*,b.orderNo as newOrder,b.width,");
		sql.append("\n 	case when b.opened is null then a.isdefault else b.opened end openState");
		sql.append("\n from menuHome a left join menuHomeSet b on a.bustype=b.bustype");
		this.addSqlWhere(comId, sql, args, " and b.comid=?");
		this.addSqlWhere(userId, sql, args, " and b.userId=?");
		if(countSub<=0||countSub == null){
			sql.append("\n  where a.bustype <> '"+ConstantInterface.TYPE_SUBTASK+"'");
		}
		sql.append("\n order by a.id ");
		sql.append("\n  )a order by orderNo,a.newOrder ");
		return this.listQuery(sql.toString(), args.toArray(), MenuHome.class);
	}
	/**
	 *  首页模块展示设置
	 * @param comId
	 * @param userId
	 * @param countSub 下属数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MenuHomeSet> listMenuHomeSet(Integer comId, Integer userId,Integer countSub) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.*,case when a.newOrder is null then rownum else a.newOrder end as orderNo from(");
		sql.append("\n select a.title,a.busType,b.orderNo as newOrder,case when b.width is null then 0 else b.width end width,");
		sql.append("\n 	case when b.opened is null then a.isdefault else b.opened end opened ");
		sql.append("\n from menuHome a left join menuHomeSet b on a.bustype=b.bustype ");
		this.addSqlWhere(comId, sql, args, " and b.comid=?");
		this.addSqlWhere(userId, sql, args, " and b.userId=?");
		if(countSub<=0||countSub ==null){
			sql.append("\n  where a.bustype <> '"+ConstantInterface.TYPE_SUBTASK+"'");
		}
		sql.append("\n order by a.id ");
		sql.append("\n  )a order by orderNo,a.newOrder ");
		return this.listQuery(sql.toString(), args.toArray(), MenuHomeSet.class);
	}
	
	/**
	 * 修改首页模块展示设置
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 */
	public void updateMenuHomeSet(UserInfo sessionUser, String busType,String openState) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update menuHomeSet set opened=? where comId=? and userid=? and busType=?");
		args.add(openState);
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		args.add(busType);
		
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		//没有修改到数据
		if(result == 0){
			//菜单使用频率
			MenuHomeSet menuHomeSet = new MenuHomeSet();
			//所在企业号
			menuHomeSet.setComId(sessionUser.getComId());
			//用户主键
			menuHomeSet.setUserId(sessionUser.getId());
			//模块类型
			menuHomeSet.setBusType(busType);
			//设置使用状态
			menuHomeSet.setOpened(openState);
			this.add(menuHomeSet);
		}
	}
	/**
	 * 修改模块展示的大小
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 * @param width 模块占据大小
	 */
	public void updateMenuWidth(UserInfo sessionUser, String busType,Integer width) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update menuHomeSet set width=? where comId=? and userid=? and busType=?");
		args.add(width);
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		args.add(busType);
		
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		//没有修改到数据
		if(result == 0){
			//菜单使用频率
			MenuHomeSet menuHomeSet = new MenuHomeSet();
			//所在企业号
			menuHomeSet.setComId(sessionUser.getComId());
			//用户主键
			menuHomeSet.setUserId(sessionUser.getId());
			//模块类型
			menuHomeSet.setBusType(busType);
			//设置使用状态
			menuHomeSet.setWidth(width);
			this.add(menuHomeSet);
		}
	}
	/**
	 * 修改首页展示排序
	 * @param list
	 */
	public void updateMenuHomeOrder(MenuHomeSet menuHomeSet) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update menuHomeSet set orderNo=?, opened=? where comId=? and userid=? and busType=?");
		
		args.add(menuHomeSet.getOrderNo());
		args.add(menuHomeSet.getOpened());
		args.add(menuHomeSet.getComId());
		args.add(menuHomeSet.getUserId());
		args.add(menuHomeSet.getBusType());
		
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		//没有修改到数据
		if(result == 0){
			this.add(menuHomeSet);
		}
	}
	/**
	 * 获取快捷添加配置
	 * @param userInfo
	 * @param openState 启用状态筛选
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AddSoon> listAddSoonSetByState(UserInfo userInfo,Integer openState) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select a.* from (");
		sql.append("\n select a.*,case when b.opened is null then a.isdefault else b.opened end openState,b.orderNo");
		sql.append("\n from addsoon a");
		sql.append("\n left join Addsoonset b on  a.busType = b.busType");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and b.comid=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and b.userId=?");
		sql.append("\n )a where 1=1 ");
		this.addSqlWhere(openState, sql, args, " and a.openState= ? ");
		sql.append("\n order by a.orderNo ,a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), AddSoon.class);
	}
	/**
	 *  修改快捷显示
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 */
	public void updateAddSoonSet(UserInfo sessionUser, String busType,Integer openState) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update addSoonSet set opened=? where comId=? and userid=? and busType=?");
		args.add(openState);
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		args.add(busType);
		
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		//没有修改到数据
		if(result == 0){
			AddSoonSet addSoonSet = new AddSoonSet();
			//所在企业号
			addSoonSet.setComId(sessionUser.getComId());
			//用户主键
			addSoonSet.setUserId(sessionUser.getId());
			//模块类型
			addSoonSet.setBusType(busType);
			//设置使用状态
			addSoonSet.setOpened(openState);
			this.add(addSoonSet);
		}
	}
	/**
	 * 修改首页展示排序
	 * @param list
	 */
	public void updateAddSoonOrder(AddSoonSet addSoonSet) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update addSoonSet set orderNo=?, opened=? where comId=? and userid=? and busType=?");
		
		args.add(addSoonSet.getOrderNo());
		args.add(addSoonSet.getOpened());
		args.add(addSoonSet.getComId());
		args.add(addSoonSet.getUserId());
		args.add(addSoonSet.getBusType());
		
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		//没有修改到数据
		if(result == 0){
			this.add(addSoonSet);
		}
	}
}
