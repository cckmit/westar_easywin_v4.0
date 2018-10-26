package com.westar.core.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.westar.base.model.ChargingStandards;
import com.westar.base.model.DiscountStandards;
import com.westar.base.model.Orders;
import com.westar.base.model.OrgUpgrade;
import com.westar.base.model.OrganicSpaceCfg;
import com.westar.base.model.UserInfo;
import com.westar.base.util.AliPayMentUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.WeChartPayMentUtil;
import com.westar.base.weChat.PayCommonUtil;
import com.westar.base.weChat.PayConfigUtil;
import com.westar.base.weChat.XMLUtil;
import com.westar.core.dao.OrderDao;

/**
 * 交易业务逻辑处理
 * 
 */

@Service
public class OrderService {
	
	private static final Logger logger = Logger.getRootLogger();
	
	private static Log log = LogFactory.getLog(OrderService.class);

	@Autowired
	OrderDao orderDao;
	@Autowired
	OrganicService organicService;
	/**
	 * 获取收费标准集合 chargingType 收费标准类型
	 * 
	 * @return
	 */
	public List<ChargingStandards> listQueryChargingStandards(
			String chargingType) {
		return orderDao.listQueryChargingStandards(chargingType);
	}

	/**
	 * 获取折扣标准集合
	 * 
	 * @param discountType
	 *            折扣类型
	 * @return
	 */
	public List<DiscountStandards> listQueryDiscountStandards(
			String discountType) {
		return orderDao.listQueryDiscountStandards(discountType);
	}

	/**
	 * 生成订单
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param order
	 *            订单配置信息
	 * @throws Exception 
	 */
	public Integer addOrder(UserInfo userInfo, Orders order) throws Exception {
		if ((null == order.getUsersNum() || 0 == order.getUsersNum())
				&& (null == order.getOrderType() || "".equals(order
						.getOrderType().trim()))) {
			return null;
		}
		OrgUpgrade orgUpgrade = null;// 团队升级订单配置表
		if (ConstantInterface.ORDER_ORDERTYPE_ORGUPGRADE.equals(order
				.getOrderType())) {
			// 获取收费标准
			ChargingStandards chargingStandard = this
					.queryChargingStandardsByUserScope(order.getUsersNum(),
							ConstantInterface.ORDER_CHARGINGTYPE_USERSCOPE);
			Integer originalPrice = chargingStandard.getPrice();
			order.setOriginalPrice(originalPrice);
			// 折扣标准
			DiscountStandards discountStandard = (DiscountStandards) orderDao
					.objectQuery(DiscountStandards.class,
							order.getDiscountStandardId());
			Float discount = discountStandard.getDiscount();
			order.setDiscount((float) discount);
			orgUpgrade = new OrgUpgrade();
			orgUpgrade.setChargId(chargingStandard.getId());
			orgUpgrade.setComId(userInfo.getComId());
			orgUpgrade.setDiscountId(discountStandard.getId());
			orgUpgrade.setUsersNum(order.getUsersNum());
			orgUpgrade.setYears(discountStandard.getDiscountStandard());
		}
		order.setComId(userInfo.getComId());// 团队主键
		order.setStatus(ConstantInterface.ORDER_STATUS_UNPAID);// 订单支付状态，0-未支付；1-已支付
		order.setBill(ConstantInterface.ORDER_BILL_NEEDLESS);// 是否需要发票；0-不需要；1-需要
		order.setProductNum(order.getUsersNum());// 商品数量
		Integer paidWay = order.getPaidWay();
		if(null == paidWay){
			paidWay = ConstantInterface.ORDER_PAIDWAY_OFFLINE;
		}
		order.setPaidWay(paidWay);// 默认支付方式
		Integer orderId = orderDao.add(order);// 订单持久化
		//构建订单号
		String orderTradeNo = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyyMMddHHmmssSSS)+orderId; 
		Orders orderCheck = orderDao.queryOrderByOrderTradeNo(orderTradeNo);
		if(null==orderCheck){
			order.setId(orderId);
			order.setOrderTradeNo(orderTradeNo);//当前时间+订单主键
			orderDao.update("update orders a set a.orderTradeNo=:orderTradeNo where a.comid=:comId and a.id=:id",order);
		}else{
			//订单号重复
			throw new Exception("订单号重复");
		}
		if (null != orgUpgrade) {
			orgUpgrade.setOrderId(orderId);
			orderDao.add(orgUpgrade);// 团队升级订单配置表
		}
		return orderId;
	}

	/**
	 * 根据使用人数查询收费标准
	 * 
	 * @param usersNum
	 *            使用人数
	 * @param chargingType
	 *            标准类型
	 * @return ChargingStandards 收费标准比对参数
	 */
	public ChargingStandards queryChargingStandardsByUserScope(
			Integer usersNum, String chargingType) {
		return orderDao.queryChargingStandardsByUserScope(usersNum,
				chargingType);
	}

	/**
	 * 获取订单记录
	 * 
	 * @param comId
	 *            团队主键
	 * @param order
	 *            订单筛选参数
	 * @return
	 */
	public List<Orders> listOrders(Integer comId, Orders order) {
		return orderDao.listOrders(comId, order);
	}

	/**
	 * 获取订单详情
	 * 
	 * @param comId
	 *            团队主键
	 * @param orderId
	 *            订单主键
	 * @return
	 */
	public Orders queryOrderById(Integer comId, Integer orderId) {
		if (null == orderId || 0 == orderId){
			return null;
		}
		Orders order = orderDao.queryOrderById(comId, orderId);
		order.setDiscountPrice(order.getOriginalTotalPrice()-order.getOrderCost());//折扣优惠
		comId = order.getComId();
		OrganicSpaceCfg orgSpaceCfg = orderDao.queryOrganicSpaceCfgByComId(comId);
		if(null!=orgSpaceCfg){
			Integer orgBalanceMoney = orgBalanceMoney(orgSpaceCfg);
			order.setOrgBalanceMoney(orgBalanceMoney);
//			Integer transactionPrice = order.getTransactionPrice()-orgBalanceMoney;//应补差价
//			order.setTransactionPrice(transactionPrice>0?transactionPrice:0);
		}else{
			order.setOrgBalanceMoney(0);//默认余额
		}
		return order;
	}

	/**
	 * 计算团队购买服务余额
	 * @param orgSpaceCfg 团队购买服务配置信息
	 * @return
	 */
	private Integer orgBalanceMoney(OrganicSpaceCfg orgSpaceCfg) {
		Integer orderCost = orgSpaceCfg.getOrderCost();//购买时费用
		int allServiceDates = DateTimeUtil.daysBetweenDate(//购买周期天数
				DateTimeUtil.parseDate(orgSpaceCfg.getStartDate(),DateTimeUtil.yyyy_MM_dd),
				DateTimeUtil.parseDate(orgSpaceCfg.getEndDate(),DateTimeUtil.yyyy_MM_dd));
		int leftServiceDates = DateTimeUtil.daysBetweenDate(//使用剩余天数
				DateTimeUtil.parseDate(orgSpaceCfg.getStartDate(),DateTimeUtil.yyyy_MM_dd),
				DateTimeUtil.parseDate(orgSpaceCfg.getEndDate(),DateTimeUtil.yyyy_MM_dd));
		Integer orgBalanceMoney = (orderCost/allServiceDates)*leftServiceDates;//团队购买服务余额
		return orgBalanceMoney;
	}

	/**
	 * 计算订单价格
	 * 
	 * @param usersNum
	 *            使用人数
	 * @param discountStandardId
	 *            折扣主键
	 * @return
	 * @throws Exception
	 */
	public Orders calculatePrice(Integer usersNum, Integer discountStandardId)
			throws Exception {
		Orders order = new Orders();
		try {
			// 获取收费标准
			ChargingStandards chargingStandard = this
					.queryChargingStandardsByUserScope(usersNum,
							ConstantInterface.ORDER_CHARGINGTYPE_USERSCOPE);
			Integer originalPrice = chargingStandard.getPrice();// 原始单价
			// 折扣标准
			DiscountStandards discountStandard = (DiscountStandards) orderDao
					.objectQuery(DiscountStandards.class, discountStandardId);
			Float discount = discountStandard.getDiscount();
			Integer years = discountStandard.getDiscountStandard();
			order.setOriginalTotalPrice((int) Math.floor(usersNum
					* originalPrice * years));// 原始总价
			order.setOrderCost((int) Math.floor(usersNum
					* originalPrice * years * discount));
			order.setSucc(true);
		} catch (Exception e) {
			order.setSucc(false);
			order.setPromptMsg("团队升级订单计算出错。");
			throw new Exception("团队升级订单计算出错。");
		}
		return order;
	}

	/**
	 * 微信支付
	 * 
	 * @param orderId
	 *            订单主键
	 * @param userInfo
	 *            当前操作人信息
	 */
	public String initWeChatOrder(Integer orderId,UserInfo userInfo) {
		try {
			//获取交易订单信息
			Orders order = orderDao.queryOrderById(userInfo.getComId(), orderId);
			if(null==order){return null;}
			Integer orderCost = order.getOrderCost();//订单应付金额
			Integer orgBalanceMoney = this.countOrgBalanceMoney(userInfo.getComId());//团队结余
			orderCost = orderCost - orgBalanceMoney;//应补差价
			if(orderCost<=0){
				logger.error("微信下单失败；原因：交易金额小于0。");
				throw new Exception("微信下单失败；原因：交易金额小于0。");
			}
			
			//订单信息
			String orderTradeNo = order.getOrderTradeNo();
			//交易状态
			Integer status =  order.getStatus();
			if(status == ConstantInterface.ORDER_STATUS_FAIL 
					|| null == orderTradeNo ){
				//构建订单号
				orderTradeNo = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyyMMddHHmmssSSS)+orderId;
				order.setStatus(ConstantInterface.ORDER_STATUS_UNPAID);
				order.setPaidWay(ConstantInterface.ORDER_PAIDWAY_WECHAT);
				order.setOrderTradeNo(orderTradeNo);
				orderDao.update(order);
			}else{
				order.setPaidWay(ConstantInterface.ORDER_PAIDWAY_WECHAT);
				orderDao.update(order);
			}
			
			orderCost = (int)(orderCost*100);//转换为分
//			Integer orderCost = 1;//研发时使用（1分）
			Map<String, String> map= WeChartPayMentUtil.initWeChatOrder(order.getOrderTradeNo(),"团队服务升级",orderCost.toString());//微信下订单
			//只有当返回标识和结果标识都为SUCCESS时，数据为成功状态
			if(ConstantInterface.RETURN_CODE_SUCCESS.equals(map.get("return_code").toUpperCase()) 
					&& ConstantInterface.RESULT_CODE_SUCCESS.equals(map.get("result_code").toUpperCase())){
				return map.get("code_url");
			}else if(ConstantInterface.RETURN_CODE_FAIL.equals(map.get("return_code").toUpperCase())){
				logger.info("微信下单失败；原因："+map.get("return_msg"));
			}else if(ConstantInterface.RESULT_CODE_FAIL.equals(map.get("result_code").toUpperCase())){
				logger.info("微信下单失败；原因："+map.get("err_code_des"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 支付宝支付
	 * 
	 * @param orderId
	 *            订单主键
	 * @param userInfo
	 *            当前操作人信息
	 * @throws Exception 
	 */
	public String initAlilyOrder(Integer orderId,UserInfo userInfo) throws Exception {
		//获取交易订单信息
		Orders order = orderDao.queryOrderById(userInfo.getComId(), orderId);
		if(null==order){return null;}
		Integer orderCost = order.getOrderCost();//订单应付金额
		Integer orgBalanceMoney = this.countOrgBalanceMoney(userInfo.getComId());//团队结余
		orderCost = orderCost - orgBalanceMoney;//应补差价
		if(orderCost<=0){
			logger.error("支付宝下单失败；原因：交易金额小于0。");
			throw new Exception("支付宝下单失败；原因：交易金额小于0。");
		}
		//订单信息
		String orderTradeNo = order.getOrderTradeNo();
		//交易状态
		Integer status =  order.getStatus();
		if(status == ConstantInterface.ORDER_STATUS_FAIL 
				|| null == orderTradeNo ){
			//构建订单号
			orderTradeNo = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyyMMddHHmmssSSS)+orderId;
			order.setStatus(ConstantInterface.ORDER_STATUS_UNPAID);
			order.setPaidWay(ConstantInterface.ORDER_PAIDWAY_ALIPAY);
			order.setOrderTradeNo(orderTradeNo);
			orderDao.update(order);
		}else{
			order.setPaidWay(ConstantInterface.ORDER_PAIDWAY_ALIPAY);
			orderDao.update(order);
		}
		String form = AliPayMentUtil.alipayTradePagePay(orderTradeNo, orderCost.toString(), "团队服务升级");
		return form;
	}

	
	/**
	 * 支付宝支付相关信息
	 * @param paramMap
	 * @throws AlipayApiException 
	 */
	public Map<String,String> initAlilyNotify(Map<String,String> paramMap) throws AlipayApiException{
		Map<String,String> map = new HashMap<String, String>();
		
		String orderTradeNo = paramMap.get("orderTradeNo");
		String total_fee = paramMap.get("orderCost");
		
		//交易金额验证
		/***************************开始*********************************/
		
		//订单信息
		Orders order = orderDao.queryOrderByOrderTradeNo(orderTradeNo);
		
		if(null == order) {
			map.put("tradeState", "0");
			map.put("errorInfo", "支付订单已失效!");
			return map;
		}
		
		OrganicSpaceCfg orgSpaceCfg = orderDao.queryOrganicSpaceCfgByComId(order.getComId());
		Integer orderCost = order.getOrderCost();
		if(null!=orgSpaceCfg){
			Integer orgBalanceMoney = orgBalanceMoney(orgSpaceCfg);
			order.setOrgBalanceMoney(orgBalanceMoney);
			orderCost = orderCost-orgBalanceMoney;//应补差价
		}
		//以下发布是需打开验证
		if(!total_fee.equals(orderCost.toString())){
			//退款
			this.updatePayBackAlily(orderTradeNo, total_fee, order);
			map.put("tradeState", "0");
			map.put("errorInfo", "交易金额与应付金额不匹配");
			return map;
		}
		/***************************结束*********************************/
		//升级服务
		this.initOrderServices(orderTradeNo, total_fee, order,ConstantInterface.ORDER_PAIDWAY_ALIPAY);
		
		// ////////执行自己的业务逻辑////////////////
		logger.info("支付成功");
		
		map.put("tradeState", "1");
		return map;
	}

	/**
	 * 支付宝退款
	 * @param orderTradeNo 交易流水号
	 * @param total_fee 交易费用
	 * @param order 订单信息
	 * @throws AlipayApiException
	 */
	public void updatePayBackAlily(String orderTradeNo, String total_fee,
			Orders order) throws AlipayApiException {
		order.setStatus(ConstantInterface.ORDER_STATUS_FAIL);//订单支付失败
		order.setOrderTradeNo(orderTradeNo);//订单编号
		order.setPaidTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));//支付时间
		order.setPaidWay(ConstantInterface.ORDER_PAIDWAY_ALIPAY);//支付方式
		
		String result =AliPayMentUtil.refundAliPay(orderTradeNo,total_fee,"交易金额与应付金额不匹配");
		JSONObject json = JSONObject.parseObject(result);
		JSONObject responseJson = json.getJSONObject("alipay_trade_refund_response");
		String code = responseJson.getString("code");
		String fund_change = responseJson.getString("fund_change");
		
		if(code.equals("10000") && "Y".equals(fund_change)) {
			orderDao.update("update orders a set a.status=:status,a.paidTime=:paidTime,a.paidWay=:paidWay where a.orderTradeNo=:orderTradeNo",order);
			logger.error("交易出错；订单“"+orderTradeNo+"”交易金额与应付金额不匹配。");
		}
	}

	/**
	 * 微信支付回调
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void initWeChatNotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 读取参数
		InputStream inputStream;
		StringBuffer sb = new StringBuffer();
		inputStream = request.getInputStream();
		String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				inputStream, "UTF-8"));
		while ((s = in.readLine()) != null) {
			sb.append(s);
		}
		in.close();
		inputStream.close();

		// 解析xml成map
		Map<String, String> m = new HashMap<String, String>();
		m = XMLUtil.doXMLParse(sb.toString());

		// 过滤空 设置 TreeMap
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String parameter = (String) it.next();
			String parameterValue = m.get(parameter);

			String v = "";
			if (null != parameterValue) {
				v = parameterValue.trim();
			}
			packageParams.put(parameter, v);
		}
		// 账号信息
		String key = PayConfigUtil.API_KEY; // key

		logger.info(packageParams);
		// 判断签名是否正确
		if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
			// ------------------------------
			// 处理业务开始
			// ------------------------------;
			String resXml = "";
			if (ConstantInterface.RESULT_CODE_SUCCESS.equals((String) packageParams.get("result_code"))) {
				// 这里是支付成功
				String out_trade_no = (String) packageParams.get("out_trade_no");
				String mch_id = (String) packageParams.get("mch_id");
				String openid = (String) packageParams.get("openid");
				String is_subscribe = (String) packageParams
						.get("is_subscribe");
				String total_fee = (String) packageParams.get("total_fee");//实际交易金额
				//交易金额验证
				/***************************开始*********************************/
				//订单信息
				Orders order = orderDao.queryOrderByOrderTradeNo(out_trade_no);
				Integer orderCost = order.getOrderCost();
				OrganicSpaceCfg orgSpaceCfg = orderDao.queryOrganicSpaceCfgByComId(order.getComId());
				if(null!=orgSpaceCfg){
					Integer orgBalanceMoney = orgBalanceMoney(orgSpaceCfg);
					order.setOrgBalanceMoney(orgBalanceMoney);
					orderCost = orderCost-orgBalanceMoney;//应补差价
				}
				orderCost = orderCost*100;//转换为分
				//以下发布是需打开验证
				if(!total_fee.equals(orderCost.toString())){
					order.setStatus(ConstantInterface.ORDER_STATUS_FAIL);//订单支付失败
					order.setOrderTradeNo(out_trade_no);//订单编号
					order.setPaidTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));//支付时间
					order.setPaidWay(ConstantInterface.ORDER_PAIDWAY_WECHAT);//支付方式
					orderDao.update("update orders a set a.status=:status,a.paidTime=:paidTime,a.paidWay=:paidWay where a.orderTradeNo=:orderTradeNo",order);
					logger.error("交易出错；订单“"+out_trade_no+"”交易金额与应付金额不匹配。");
					Map<String, String> map = WeChartPayMentUtil.weChatOrderRefund(out_trade_no,total_fee);//微信申请退款
					if(ConstantInterface.RETURN_CODE_SUCCESS.equals(map.get("return_code"))){
						resXml = "<xml>"
								+ "<return_code><![CDATA[SUCCESS]]></return_code>"
								+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
						BufferedOutputStream out = new BufferedOutputStream(
								response.getOutputStream());
						out.write(resXml.getBytes());
						out.flush();
						out.close();
					}
					return;
				}
				/***************************结束*********************************/
				//升级服务
				this.initOrderServices(out_trade_no, total_fee, order,ConstantInterface.ORDER_PAIDWAY_WECHAT);
				
				//反馈信息
				logger.info("mch_id:" + mch_id);
				logger.info("openid:" + openid);
				logger.info("is_subscribe:" + is_subscribe);
				logger.info("out_trade_no:" + out_trade_no);
				logger.info("total_fee:" + total_fee);
				logger.info("支付成功");
				// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
				resXml = "<xml>"
						+ "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

			} else {
				resXml = "<xml>"
						+ "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>"
						+ "</xml> ";
			}
			// ------------------------------
			// 处理业务完毕
			// ------------------------------
			BufferedOutputStream out = new BufferedOutputStream(
					response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		} else {
			logger.info("通知签名验证失败");
		}

	}

	/**
	 * 升级服务信息
	 * @param out_trade_no 交易流水号
	 * @param total_fee 交易费用
	 * @param order 订单信息
	 * @param paidWay
	 */
	public void initOrderServices(String out_trade_no, String total_fee,
			Orders order,Integer paidWay) {
		// ////////执行自己的业务逻辑////////////////
		order.setStatus(ConstantInterface.ORDER_STATUS_PAID);//订单支付状态
		order.setOrderTradeNo(out_trade_no);//订单编号
		order.setPaidTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));//支付时间
		order.setPaidWay(paidWay);//支付方式
		order.setTransactionMoney(Integer.parseInt(total_fee));//订单实际交易金额
		orderDao.update("update orders a set a.status=:status,a.paidTime=:paidTime,a.paidWay=:paidWay,a.transactionMoney=:transactionMoney where a.orderTradeNo=:orderTradeNo",order);
		//organicSpaceCfg中一个团队只能有一条数据
		orderDao.delByField("organicSpaceCfg", new String[]{"comId"}, new Object[]{order.getComId()});
		//初始化团队购买服务的配置信息
		OrganicSpaceCfg organicSpaceCfg = this.initOrganicSpaceCfgInfo(order);
		orderDao.add(organicSpaceCfg);//持久化初始化团队购买服务
		organicService.updateOrgMemberToInService(order.getComId(),order.getUsersNum());//激活团队成员InService为1
		// ////////执行自己的业务逻辑////////////////
	}

	/**
	 * 计算团队购买服务
	 * @param order 订单信息
	 * @return
	 */
	private OrganicSpaceCfg initOrganicSpaceCfgInfo(Orders order) {
		//团队使用空间配置表
		OrganicSpaceCfg organicSpaceCfg = new OrganicSpaceCfg();
		organicSpaceCfg.setComId(order.getComId());
		//计算服务购买的有效时间
		//团队服务信息重新初始化
		organicSpaceCfg.setStartDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
		organicSpaceCfg.setEndDate(DateTimeUtil.addDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd),DateTimeUtil.yyyy_MM_dd,1,order.getYears()));
		organicSpaceCfg.setStorageSpace(order.getUsersNum()*4);
		organicSpaceCfg.setUsersNum(order.getUsersNum());
		organicSpaceCfg.setOrderId(order.getId());
		return organicSpaceCfg;
	}
	
	/**
	 * 统计团队下人员启用状态为启用的人员数
	 * @param comId 团队主键
	 * @return
	 */
	public Integer countOrgEnabledUsersNum(Integer comId){
		return organicService.countOrgEnabledUsersNum(comId);
	}
	
	/**
	 * 计算团队结余
	 * @param comId 团队主键
	 * @return
	 */
	public Integer countOrgBalanceMoney(Integer comId){
		OrganicSpaceCfg orgSpaceCfg = orderDao.queryOrganicSpaceCfgByComId(comId);
		if(null!=orgSpaceCfg){
			Integer orgBalanceMoney = orgBalanceMoney(orgSpaceCfg);
			return orgBalanceMoney;
		}
		return 0;
	}

	/**
	 * 订单状态检测
	 * @param orderTradeNo 订单号
	 * @return
	 */
	public Orders orderStatusCheck(String orderTradeNo) {
		Orders order = new Orders();
		try {
			Map<String, String> map = WeChartPayMentUtil.weChatOrderQuery(orderTradeNo);
			if(ConstantInterface.RETURN_CODE_SUCCESS.equals(map.get("return_code").toUpperCase()) 
					&& ConstantInterface.RESULT_CODE_SUCCESS.equals(map.get("result_code").toUpperCase())
					&& ConstantInterface.ORDER_TRADE_STATE_SUCCESS.equals(map.get("trade_state").toUpperCase())){
				order.setStatus(ConstantInterface.ORDER_STATUS_PAID);
				return order;
			}else if(ConstantInterface.RETURN_CODE_FAIL.equals(map.get("return_code").toUpperCase())){
				logger.info("微信订单状态检测失败；原因："+map.get("return_msg"));
			}else if(ConstantInterface.RESULT_CODE_FAIL.equals(map.get("result_code").toUpperCase())){
				logger.info("微信订单状态检测失败；原因："+map.get("err_code_des"));
			}/*else{
				logger.info("微信订单状态检测失败；原因："+map.get("trade_state_desc"));
			}*/
		} catch (Exception e) {
			logger.error("订单“"+orderTradeNo+"”查询失败。");
			e.printStackTrace();
		}
		order.setStatus(ConstantInterface.ORDER_STATUS_UNPAID);
		return order;
	}

	/**
	 * 定时作废未支付的订单
	 * @throws Exception 
	 */
	public void updateUnPayedOrders() {
		List<Orders> listUnPayedOrders = orderDao.lisyUnPayedOrders();
		if(null == listUnPayedOrders || listUnPayedOrders.isEmpty() ){//没有需要作废的订单信息
			return;
		}
		
		Map<Integer, OrganicSpaceCfg> organicSpaceCfgMap = new HashMap<Integer, OrganicSpaceCfg>();
		
		for (Orders order : listUnPayedOrders) {
			//交易流水号
			String orderTradeNo = order.getOrderTradeNo();
			//支付方式
			Integer paidWay =  order.getPaidWay();
			//实际支付金额
			Integer orderCost = order.getOrderCost();
			
			//查询团队费用配置信息
			OrganicSpaceCfg orgSpaceCfg = organicSpaceCfgMap.get(order.getComId());
			if(null == orgSpaceCfg){
				orgSpaceCfg = orderDao.queryOrganicSpaceCfgByComId(order.getComId());
				organicSpaceCfgMap.put(order.getComId(), orgSpaceCfg);
			}
			if(null!=orgSpaceCfg){
				//计算团队购买服务余额
				Integer orgBalanceMoney = this.orgBalanceMoney(orgSpaceCfg);
				order.setOrgBalanceMoney(orgBalanceMoney);
				orderCost = orderCost-orgBalanceMoney;//应补差价
			}
			
			if(ConstantInterface.ORDER_PAIDWAY_WECHAT.equals(paidWay)){
				//转换为分
				orderCost = orderCost*100;
				Map<String, String> packageParams;
				try {
					packageParams = WeChartPayMentUtil.weChatOrderQuery(orderTradeNo);
					if(ConstantInterface.RETURN_CODE_FAIL.equalsIgnoreCase(packageParams.get("return_code"))){//通信失败
						continue;
					}
					
					/*************以显示通信成功的开始**********************/
					
					//没有查询结果信息
					if(ConstantInterface.RESULT_CODE_FAIL.equalsIgnoreCase(packageParams.get("result_code"))){
						order.setStatus(ConstantInterface.ORDER_STATUS_FAIL);//作废
						orderDao.update(order);
						continue;
					}
					//有查询结果信息
					String trade_state = packageParams.get("trade_state");
					if(ConstantInterface.ORDER_TRADE_STATE_SUCCESS.equalsIgnoreCase(trade_state)//交易支付成功
							|| ConstantInterface.ORDER_TRADE_STATE_CLOSED.equalsIgnoreCase(trade_state)){//交易结束，不可退款
						//以下发布是需打开验证
						//流水号
						String out_trade_no = packageParams.get("out_trade_no");
						//实际支付费用
						String total_fee = packageParams.get("total_fee");
						if(total_fee.equals(orderCost.toString()) 
								|| ConstantInterface.ORDER_TRADE_STATE_CLOSED.equalsIgnoreCase(trade_state)){
							//升级服务信息
							this.initOrderServices(out_trade_no, total_fee, order, ConstantInterface.ORDER_PAIDWAY_WECHAT);
							continue;
						}
						if(ConstantInterface.ORDER_TRADE_STATE_SUCCESS.equalsIgnoreCase(trade_state)){//交易支付成功
							//退款
							WeChartPayMentUtil.weChatOrderRefund(orderTradeNo, total_fee);
							//修改状态
							order.setStatus(ConstantInterface.ORDER_STATUS_FAIL);//订单支付失败
							order.setOrderTradeNo(orderTradeNo);//订单编号
							order.setPaidTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));//支付时间
							order.setPaidWay(ConstantInterface.ORDER_PAIDWAY_WECHAT);//支付方式
							orderDao.update("update orders a set a.status=:status,a.paidTime=:paidTime,a.paidWay=:paidWay where a.orderTradeNo=:orderTradeNo",order);
							logger.error("交易出错；订单“"+orderTradeNo+"”交易金额与应付金额不匹配。");
						}
					}else if(ConstantInterface.ORDER_TRADE_STATE_REFUND.equalsIgnoreCase(trade_state)//转入退款
							|| ConstantInterface.ORDER_TRADE_STATE_REVOKED.equalsIgnoreCase(trade_state)//已撤销（刷卡支付）
							|| ConstantInterface.ORDER_TRADE_STATE_PAYERROR.equalsIgnoreCase(trade_state)){//支付失败
						//修改状态
						order.setStatus(ConstantInterface.ORDER_STATUS_FAIL);//订单支付失败
						order.setOrderTradeNo(orderTradeNo);//订单编号
						order.setPaidTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));//支付时间
						order.setPaidWay(ConstantInterface.ORDER_PAIDWAY_WECHAT);//支付方式
						orderDao.update("update orders a set a.status=:status,a.paidTime=:paidTime,a.paidWay=:paidWay where a.orderTradeNo=:orderTradeNo",order);
						logger.info("微信订单状态检测失败；原因："+packageParams.get("err_code_des"));
					}
					/*************以显示通信成功的结束**********************/
				} catch (Exception e) {
					log.info("微信订单状态检测失败,订单“"+orderTradeNo+"”退款失败。");
				}
			}else if(ConstantInterface.ORDER_PAIDWAY_ALIPAY.equals(paidWay)){
				try {
					String result = AliPayMentUtil.queryAliPay(orderTradeNo);
					JSONObject json = JSONObject.parseObject(result);
					JSONObject responseJson = json.getJSONObject("alipay_trade_query_response");
					String code = responseJson.getString("code");
					if(!code.equals("10000")) {//没有查询到
						order.setStatus(ConstantInterface.ORDER_STATUS_FAIL);//作废
						orderDao.update(order);
						continue;
					}
					String trade_status = responseJson.getString("trade_status");
					if(trade_status.equalsIgnoreCase("TRADE_SUCCESS")//交易支付成功
							||trade_status.equalsIgnoreCase("TRADE_FINISHED")) {//交易结束，不可退款
						String total_amount = responseJson.getString("total_amount");
						String out_trade_no = responseJson.getString("out_trade_no");
						//以下发布是需打开验证
						if(total_amount.equals(orderCost.toString())
								|| trade_status.equalsIgnoreCase("TRADE_FINISHED")){
							//升级服务信息
							this.initOrderServices(out_trade_no, total_amount, order, ConstantInterface.ORDER_PAIDWAY_ALIPAY);
							continue;
						}
						
						if(trade_status.equalsIgnoreCase("TRADE_SUCCESS")){//交易支付成功
							//退款
							this.updatePayBackAlily(orderTradeNo, total_amount, order);
						}
					}else if(trade_status.equalsIgnoreCase("TRADE_CLOSED")){//未付款交易超时关闭，或支付完成后全额退款
						order.setStatus(ConstantInterface.ORDER_STATUS_FAIL);//作废
						orderDao.update(order);
						continue;
					}
				} catch (AlipayApiException e) {
					log.error("查询支付宝支付信息时，定时作废未支付的订单："+orderTradeNo+",网络异常");
				}
			}
		}
		
	}

	/**
	 * 分页查询系统支付信息
	 * @param orders
	 * @return
	 */
	public List<Orders> listPagedWebOrder(Orders orders) {
		
		return orderDao.listPagedWebOrder(orders);
	}
}
