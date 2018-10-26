package com.westar.core.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.westar.base.conf.AlipayConfig;
import com.westar.base.model.ChargingStandards;
import com.westar.base.model.DiscountStandards;
import com.westar.base.model.Orders;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.OrderService;
import com.westar.core.web.FreshManager;

/**
 * 订单交易
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
	private static final Log loger = LogFactory.getLog(OrderController.class);
	
	@Autowired
	OrderService orderService;
	
	/**
	 * 跳转升级中心
	 * @param request
	 * @param activityMenu 被点击菜单标示符
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/order_center")
	public ModelAndView orderCenter(HttpServletRequest request){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/order/order_center");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		List<ChargingStandards> chargingStandards = orderService.listQueryChargingStandards(
				ConstantInterface.ORDER_CHARGINGTYPE_USERSCOPE);//获取收费标准
		view.addObject("chargingStandards",chargingStandards);
		List<DiscountStandards> discountStandards = orderService.listQueryDiscountStandards(
				ConstantInterface.ORDER_DISCOUNTTYPE_YEAR);//获取折扣标准
		view.addObject("discountStandards",discountStandards);
		//默认订单购买人数
		Integer defaultUsersNum = orderService.countOrgEnabledUsersNum(userInfo.getComId());
		defaultUsersNum = defaultUsersNum>ConstantInterface.ORG_DEFAULT_MEMBERS?defaultUsersNum:(ConstantInterface.ORG_DEFAULT_MEMBERS+10);
		defaultUsersNum = (defaultUsersNum-(defaultUsersNum%10))+((defaultUsersNum%10)==0?0:10);
		view.addObject("defaultUsersNum",defaultUsersNum);
		//团队结余
		Integer orgBalanceMoney = orderService.countOrgBalanceMoney(userInfo.getComId());
		view.addObject("orgBalanceMoney",orgBalanceMoney);
		return view;
	}
	
	/**
	 * 生成订单
	 * @param order 订单配置信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addOrder")
	public ModelAndView addOrder(Orders order) throws Exception{
		//清除缓存中所有的操作
		UserInfo userInfo = this.getSessionUser();
		Integer orderId = orderService.addOrder(userInfo,order);//生成订单
		ModelAndView view = new ModelAndView("redirect:/order/orderToPay?sid="+this.getSid()+"&orderId="+orderId);
		return view;
	}
	
	/**
	 * 获取团队订单记录
	 * @param order 订单查询参数
	 * @param activityMenu 左边点击菜单标示
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listOrders")
	public ModelAndView listOrders(Orders order,String activityMenu){
		//清除缓存中所有的操作
		ModelAndView view = new ModelAndView("/order/order_center");
		view.addObject("activityMenu",activityMenu);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		List<Orders> listOrders = orderService.listOrders(userInfo.getComId(),order);//获取订单记录
		view.addObject("listOrders", listOrders);
		view.addObject("order",order);
		Integer orgBalanceMoney = orderService.countOrgBalanceMoney(userInfo.getComId());
		view.addObject("orgBalanceMoney", orgBalanceMoney);
		return view;
	}
	
	/**
	 * 查看订单详情
	 * @param orderId 订单主键
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/orderDetail")
	public ModelAndView orderDetail(Integer orderId){
		ModelAndView view = new ModelAndView("/order/orderDetail");
		UserInfo userInfo = this.getSessionUser();
		Orders order = orderService.queryOrderById(userInfo.getComId(),orderId);//获取订单记录
		view.addObject("order",order);
		return view;
	}
	
	/**
	 * 计算订单价格
	 * @param usersNum 使用人数
	 * @param discountStandardId 折扣主键
	 * @return Orders 订单信息
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/calculatePrice")
	public Orders calculatePrice(Integer usersNum,Integer discountStandardId) throws Exception {
		Orders order = orderService.calculatePrice(usersNum,discountStandardId);//计算订单
		return order;
	}
	
	/**
	 * 展示平台使用条约
	 * @return
	 */
	@RequestMapping("/easyWinRules")
	public ModelAndView easyWinRules(){
		ModelAndView view = new ModelAndView("/order/easyWinRules");
		return view;
	}
	
	/**
	 * 订单支付
	 * @param orderId 订单主键
	 * @return
	 */
	@RequestMapping("/orderToPay")
	public ModelAndView orderToPay(Integer orderId){
		ModelAndView view = new ModelAndView("/order/order_center");
		view.addObject("activityMenu","payWays");
		UserInfo userInfo = this.getSessionUser();
		Orders order = orderService.queryOrderById(userInfo.getComId(),orderId);//获取订单记录
		view.addObject("order",order);
		view.addObject("userInfo",userInfo);
		return view;
	}
	
	/**
	 * 跳转支付
	 * @param orderId 订单主键
	 * @param payWay 支付方式
	 * @return
	 */
	@RequestMapping("/toPay")
	public ModelAndView toPay(Integer orderId,Integer payWay){
		ModelAndView view = new ModelAndView("/order/paidConfirm");
		view.addObject("orderId",orderId);
		view.addObject("payWay",payWay);
		return view;
	}
	
	/**
	 * 微信支付
	 * @param orderId 订单主键
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/weChatPay")
	public ModelAndView weChatPay(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,Integer orderId,Integer payWay) throws IOException{
		UserInfo userInfo = this.getSessionUser();
		
		ModelAndView view = new ModelAndView();
		if(payWay.equals(ConstantInterface.ORDER_PAIDWAY_WECHAT)){
			view.setViewName("/order/weChat/erWeiMa");
			String weChatPayurl = orderService.initWeChatOrder(orderId,userInfo);//微信下单
			if(null!=weChatPayurl && !"".equals(weChatPayurl.trim())){
				view.addObject("appLoadUrl", weChatPayurl);
				Orders order = orderService.queryOrderById(userInfo.getComId(), orderId);
				view.addObject("order", order);
			}else{
				view.setViewName("/error/500");
			}
			
		}else if(payWay.equals(ConstantInterface.ORDER_PAIDWAY_ALIPAY)){
			view.setViewName("/order/alily/erWeiMa");
			String form;
			try {
				form = orderService.initAlilyOrder(orderId, userInfo);
				if(null!=form && !"".equals(form.trim())){
					httpResponse.setContentType("text/html;charset=utf-8");
					httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
					httpResponse.getWriter().flush();
					httpResponse.getWriter().close();
					return null;
				}else{
					view.setViewName("/error/500");
				}
			} catch (Exception e) {
				view.setViewName("/error/500");
			}
		}
		return view;
	}
	
	/**
	 * 微信支付回调
	 * @param request
	 * @throws Exception 
	 */
	@RequestMapping("/weChatNotify")
	public void weChatNotify(HttpServletRequest request,HttpServletResponse response) throws Exception{
		orderService.initWeChatNotify(request, response);
	}
	
	/**
	 * 订单状态检测
	 * @param orderTradeNo 订单号
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/orderStatusCheck")
	public Orders orderStatusCheck(String orderTradeNo) throws Exception {
		Orders order = orderService.orderStatusCheck(orderTradeNo);
		return order;
	}
	
	/**
	 * 微信支付成功，结果友好提示
	 * @return
	 */
	@RequestMapping("/weChatPaidOk")
	public ModelAndView weChatPaidOk(){
		ModelAndView view = new ModelAndView("/order/weChat/weChatPaidOk");
		return view;
	}
	
	/**
	 * 通知信息(暂未使用)
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 * @throws AlipayApiException 
	 * @throws IOException 
	 */
	@RequestMapping("/alilyPayment/notify")
	public void paymenNotify(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) throws AlipayApiException, IOException{
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = httpRequest.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
				AlipayConfig.sign_type); //调用SDK验证签名
		
		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String outTradeNo = new String(httpRequest.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			
			//支付宝交易号
			String tradeNo = new String(httpRequest.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			
			//付款金额
			String totalAmount = new String(httpRequest.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			
			loger.info("trade_no:"+tradeNo+"<br/>out_trade_no:"+outTradeNo+"<br/>total_amount:"+totalAmount);
			
			httpResponse.setContentType("text/html;charset=utf-8");
			httpResponse.getWriter().write("success");//直接将完整的表单html输出到页面
			httpResponse.getWriter().flush();
			httpResponse.getWriter().close();
			
		}else {
			httpResponse.setContentType("text/html;charset=utf-8");
			httpResponse.getWriter().write("fail");//直接将完整的表单html输出到页面
			httpResponse.getWriter().flush();
			httpResponse.getWriter().close();
		}
	}
	/**
	 * 通知信息
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws AlipayApiException 
	 */
	@RequestMapping("/alilyPayment/return")
	public ModelAndView paymentReturn(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws UnsupportedEncodingException, AlipayApiException{
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = httpRequest.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		ModelAndView view = new ModelAndView("/order/alily/aliPayInfo");
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
				AlipayConfig.sign_type); //调用SDK验证签名
		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String orderTradeNo = new String(httpRequest.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//付款金额
			String orderCost = new String(httpRequest.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			
			Map<String,String> paramMap= new HashMap<String, String>();
			paramMap.put("orderTradeNo", orderTradeNo);
			paramMap.put("orderCost", orderCost);
			Map<String,String> map = orderService.initAlilyNotify(paramMap);
			String tradeState = map.get("tradeState");
			if(tradeState.equals("1")){
				view.addObject("tradeState", "1");
			}else{
				view.addObject("tradeState", "0");
				view.addObject("errorInfo", map.get("errorInfo"));
			}
			
		}else {
			view.addObject("tradeState", "0");
			view.addObject("errorInfo", "支付签名已修改,请重新支付");
		}
		return view;
	}
}
