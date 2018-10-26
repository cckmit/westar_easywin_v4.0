package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ChargingStandards;
import com.westar.base.model.DiscountStandards;
import com.westar.base.model.Orders;
import com.westar.base.model.OrganicSpaceCfg;
import com.westar.base.util.ConstantInterface;

/**
 * 交易持久化
 *
 */
@Repository
public class OrderDao extends BaseDao {

	/**
	 * 获取收费标准集合
	 * chargingType 收费标准类型
	 * @param chargingType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChargingStandards> listQueryChargingStandards(String chargingType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.chargingStandard,a.price,a.storagespace");
		sql.append("\n from chargingStandards a where a.chargingType=? order by a.chargingStandard asc");
		args.add(chargingType);
		return this.listQuery(sql.toString(), args.toArray(),ChargingStandards.class);
	}

	/**
	 * 获取折扣标准集合
	 * @param discountType 折扣类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DiscountStandards> listQueryDiscountStandards(String discountType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordcreatetime,a.discountStandard,a.discount,a.describle ");
		sql.append("\n from discountStandards a where a.discountType=? order by a.discountStandard asc");
		args.add(discountType);
		return this.listQuery(sql.toString(), args.toArray(),DiscountStandards.class);
	}

	/**
	 * 根据使用人数查询收费标准
	 * @param usersNum 使用人数
	 * @param chargingType 标准类型
	 * @return ChargingStandards 收费标准比对参数
	 */
	public ChargingStandards queryChargingStandardsByUserScope(Integer usersNum,String chargingType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from (");
		sql.append("\n select a.id,a.recordcreatetime,a.chargingStandard,a.price,a.storagespace ");
		sql.append("\n from chargingStandards a ");
		sql.append("\n where ? <= a.chargingStandard and a.chargingType=? order by a.chargingStandard asc");
		args.add(usersNum);
		args.add(chargingType);
		sql.append("\n ) where rownum=1");
		return (ChargingStandards) this.objectQuery(sql.toString(), args.toArray(),ChargingStandards.class);
	}

	/**
	 * 获取订单记录
	 * @param comId 团队主键
	 * @param order 订单筛选参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Orders> listOrders(Integer comId, Orders order) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordcreatetime,ceil(a.productnum*a.originalPrice*orgUpgrade.years*a.discount) as orderCost,a.linkername,a.status,a.ordertype,dic.zvalue as orderTypeName,");
		sql.append("\n ROUND(TO_NUMBER(sysdate - to_date(a.recordcreatetime,'yyyy-mm-dd hh24:mi:ss'))) as overDates,");
		sql.append("\n a.orderTradeNo");
		sql.append("\n from orders a ");
		sql.append("\n inner join datadic dic on dic.type='orderType' and a.ordertype=dic.code");
		sql.append("\n left join orgUpgrade orgUpgrade on a.id = orgUpgrade.Orderid and a.comid = orgUpgrade.Comid");
		sql.append("\n where a.comId=?");
		args.add(comId);
		//查询创建时间段
		this.addSqlWhere(order.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(order.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		return this.pagedQuery(sql.toString(), " a.recordcreatetime desc", args.toArray(), Orders.class);
	}

	/**
	 * 获取订单详情
	 * @param comId 团队主键
	 * @param orderId 订单主键
	 * @return
	 */
	public Orders queryOrderById(Integer comId, Integer orderId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id，a.comId,a.recordcreatetime,a.productnum,a.originalprice,a.paidtime,");
		sql.append("\n ceil(a.productnum*a.originalPrice*orgUpgrade.years) as originalTotalPrice,");//原始价格
		sql.append("\n ceil(a.productnum*a.originalPrice*orgUpgrade.years*a.discount) as orderCost,");//实际订单金额
		sql.append("\n ceil((a.productnum*a.originalPrice*orgUpgrade.years*a.discount)/a.productnum) as actualprice,");//实际单价
		sql.append("\n a.linkername,a.status,a.ordertype,a.orderTradeNo,orderType.zvalue as orderTypeName,");
		sql.append("\n ROUND(TO_NUMBER(sysdate - to_date(a.recordcreatetime,'yyyy-mm-dd hh24:mi:ss'))) as overDates,");
		sql.append("\n org.orgname,paidWay.zvalue as paidWayName,orgUpgrade.Usersnum as usersNum,a.discount,orgUpgrade.Years,");
		sql.append("\n charg.chargingstandard,orgUpgrade.years,discount.describle as discountDescrible");
		sql.append("\n from orders a ");
		sql.append("\n inner join organic org on a.comid = org.orgnum");
		sql.append("\n left join datadic orderType on orderType.type='orderType' and a.ordertype=orderType.code");
		sql.append("\n left join datadic paidWay on paidWay.type='paidWay' and a.paidWay=paidWay.code");
		sql.append("\n left join orgUpgrade orgUpgrade on a.id = orgUpgrade.Orderid and a.comid = orgUpgrade.Comid");
		sql.append("\n left join chargingStandards charg on charg.id = orgUpgrade.Chargid");
		sql.append("\n left join discountStandards discount on discount.id = orgUpgrade.Discountid");
		sql.append("\n where 1=1 and a.id=? ");
		args.add(orderId);
		this.addSqlWhere(comId, sql, args, "\n and a.comId=? ");
		return (Orders) this.objectQuery(sql.toString(), args.toArray(),Orders.class);
	}

	/**
	 * 根据订单号查询订单信息
	 * @param orderTradeNo
	 * @return
	 */
	public Orders queryOrderByOrderTradeNo(String orderTradeNo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.comId,a.id,a.recordcreatetime,a.productnum,a.originalprice,a.paidtime,");
		sql.append("\n ceil(a.productnum*a.originalPrice*orgUpgrade.years) as originalTotalPrice,");
		sql.append("\n ceil(a.productnum*a.originalPrice*orgUpgrade.years*a.discount) as orderCost,");
		sql.append("\n ceil((a.productnum*a.originalPrice*orgUpgrade.years*a.discount)/a.productnum) as actualprice,");
		sql.append("\n a.linkername,a.status,a.ordertype,a.orderTradeNo,orderType.zvalue as orderTypeName,");
		sql.append("\n ROUND(TO_NUMBER(sysdate - to_date(a.recordcreatetime,'yyyy-mm-dd hh24:mi:ss'))) as overDates,");
		sql.append("\n org.orgname,paidWay.zvalue as paidWayName,orgUpgrade.Usersnum as usersNum,a.discount,orgUpgrade.Years,");
		sql.append("\n charg.chargingstandard,orgUpgrade.years,discount.describle as discountDescrible");
		sql.append("\n from orders a ");
		sql.append("\n inner join organic org on a.comid = org.orgnum");
		sql.append("\n left join datadic orderType on orderType.type='orderType' and a.ordertype=orderType.code");
		sql.append("\n left join datadic paidWay on paidWay.type='paidWay' and a.paidWay=paidWay.code");
		sql.append("\n left join orgUpgrade orgUpgrade on a.id = orgUpgrade.Orderid and a.comid = orgUpgrade.Comid");
		sql.append("\n left join chargingStandards charg on charg.id = orgUpgrade.Chargid");
		sql.append("\n left join discountStandards discount on discount.id = orgUpgrade.Discountid");
		sql.append("\n where a.ordertradeno=?");
		args.add(orderTradeNo);
		return (Orders) this.objectQuery(sql.toString(), args.toArray(),Orders.class);
	}

	/**
	 * 获取有效的团队服务购买配置信息
	 * @param comId 团队主键
	 * @return OrganicSpaceCfg
	 */
	public OrganicSpaceCfg queryOrganicSpaceCfgByComId(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordcreatetime,a.comid,a.orderid,a.usersnum,a.storagespace,a.startdate,a.enddate,");
		sql.append("\n ceil(b.productnum*b.originalPrice*c.years*b.discount) as orderCost,b.transactionmoney");
		sql.append("\n from organicSpaceCfg a ");
		sql.append("\n inner join orders b on a.orderid=b.id and a.comid=b.comid");
		sql.append("\n inner join orgUpgrade c on b.id=c.orderid and b.comid=c.comid");
		sql.append("\n where a.comid=? and a.enddate>=to_char(sysdate,'yyyy-mm-dd')");
		args.add(comId);
		return (OrganicSpaceCfg)this.objectQuery(sql.toString(), args.toArray(),OrganicSpaceCfg.class);
	}

	/**
	 * 查询所有未支付的订单
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Orders> lisyUnPayedOrders(){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("\n select a.comId,a.id,a.recordcreatetime,a.productnum,a.originalprice,a.paidWay,a.paidtime,");
		sql.append("\n ceil(a.productnum*a.originalPrice*orgUpgrade.years) as originalTotalPrice,");
		sql.append("\n ceil(a.productnum*a.originalPrice*orgUpgrade.years*a.discount) as orderCost,");
		sql.append("\n ceil((a.productnum*a.originalPrice*orgUpgrade.years*a.discount)/a.productnum) as actualprice,");
		sql.append("\n a.linkername,a.status,a.ordertype,a.orderTradeNo,orderType.zvalue as orderTypeName,");
		sql.append("\n ROUND(TO_NUMBER(sysdate - to_date(a.recordcreatetime,'yyyy-mm-dd hh24:mi:ss'))) as overDates,");
		sql.append("\n org.orgname,paidWay.zvalue as paidWayName,orgUpgrade.Usersnum as usersNum,a.discount,orgUpgrade.Years,");
		sql.append("\n charg.chargingstandard,orgUpgrade.years,discount.describle as discountDescrible");
		sql.append("\n from orders a ");
		sql.append("\n inner join organic org on a.comid = org.orgnum");
		sql.append("\n left join datadic orderType on orderType.type='orderType' and a.ordertype=orderType.code");
		sql.append("\n left join datadic paidWay on paidWay.type='paidWay' and a.paidWay=paidWay.code");
		sql.append("\n left join orgUpgrade orgUpgrade on a.id = orgUpgrade.Orderid and a.comid = orgUpgrade.Comid");
		sql.append("\n left join chargingStandards charg on charg.id = orgUpgrade.Chargid");
		sql.append("\n left join discountStandards discount on discount.id = orgUpgrade.Discountid");
		sql.append("\n where a.status="+ConstantInterface.ORDER_STATUS_UNPAID+" and a.paidWay>0");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), Orders.class);
	}

	/**
	 * 查询后台信息
	 * @param orders
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Orders> listPagedWebOrder(Orders orders) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from (");
		sql.append("\n select a.id,a.recordcreatetime,a.paidWay,a.paidTime,ceil(a.productnum*a.originalPrice*orgUpgrade.years*a.discount) as orderCost,");
		sql.append("\n a.linkername,a.linkerPhone,a.status,a.ordertype,dic.zvalue as orderTypeName,org.orgname,");
		sql.append("\n ROUND(TO_NUMBER(sysdate - to_date(a.recordcreatetime,'yyyy-mm-dd hh24:mi:ss'))) as overDates,");
		sql.append("\n a.orderTradeNo");
		sql.append("\n from orders a ");
		sql.append("\n inner join organic org on org.orgNum = a.comId");
		sql.append("\n inner join datadic dic on dic.type='orderType' and a.ordertype=dic.code");
		sql.append("\n left join orgUpgrade orgUpgrade on a.id = orgUpgrade.Orderid and a.comid = orgUpgrade.Comid");
		sql.append("\n where 1=1");
		//查询创建时间段
		this.addSqlWhere(orders.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(orders.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		sql.append("\n )a where 1=1");
		Integer status = orders.getStatus();
		if( null != status){
			if(status == 0){
				this.addSqlWhere(orders.getStatus(), sql, args, " and a.status = ?");
				this.addSqlWhere(7, sql, args, " and a.overDates <= ?");
			}else if(status == -1){
				this.addSqlWhere(0, sql, args, " and a.status = ?");
				this.addSqlWhere(7, sql, args, " and a.overDates > ?");
			}else{
				this.addSqlWhere(orders.getStatus(), sql, args, " and a.status = ?");
			}
		}
		return this.pagedQuery(sql.toString(), " a.id ", args.toArray(), Orders.class);
	}
}
