package com.westar.base.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.google.gson.JsonObject;
import com.westar.base.conf.AlipayConfig;
import com.westar.base.model.Orders;

/**
 * 平台支付
 * @author Administrator
 *
 */
public class AliPayMentUtil {
	
	private static Log loger = LogFactory.getLog(AliPayMentUtil.class);
	
	//支付宝支付
	private static final AlipayClient ALIPAY_CLIENT = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
			AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
			AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
	/**
	 * PC场景下单并支付
	 * @param out_trade_no
	 * @param total_amount
	 * @param subject
	 * @return
	 * @throws AlipayApiException
	 */
	public static String alipayTradePagePay(String out_trade_no,String total_amount,String subject) throws AlipayApiException {
		 AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
		 alipayRequest.setReturnUrl(AlipayConfig.return_url);
		 alipayRequest.setNotifyUrl(AlipayConfig.notify_url);//在公共参数中设置回跳和通知地址
		 /**
		  * 见开发文档 /电脑网站支付 /alipay.trade.page.pay
		  * out_trade_no	商户订单号
		  * product_code	销售产品码 FAST_INSTANT_TRADE_PAY
		  * total_amount	订单总金额，单位为元，精确到小数点后两位
		  * subject			订单标题
		  */
		 
		 JsonObject jsonObject = new JsonObject();
		 jsonObject.addProperty("out_trade_no", out_trade_no);
		 jsonObject.addProperty("product_code", "FAST_INSTANT_TRADE_PAY");
		 jsonObject.addProperty("total_amount", total_amount);
		 jsonObject.addProperty("subject", subject);
		 jsonObject.addProperty("qr_pay_mode", "5");
		 
		 alipayRequest.setBizContent(jsonObject.toString());//填充业务参数
		 
		 AlipayTradePagePayResponse result = ALIPAY_CLIENT.pageExecute(alipayRequest);
		 if(result.isSuccess()) {
			 return result.getBody();
		 }else{
			return result.getBody();
		 }
	}
	/**
	 * 查询账单信息
	 * @param out_trade_no
	 * @return
	 * @throws AlipayApiException
	 */
	public static String queryAliPay(String out_trade_no) throws AlipayApiException {
		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("out_trade_no", out_trade_no);
		alipayRequest.setBizContent(jsonObject.toString());
		AlipayTradeQueryResponse response = ALIPAY_CLIENT.execute(alipayRequest);
		if(response.isSuccess()){
			return response.getBody();
		}else {
			return response.getBody();
		}
	}
	/**
	 * 统一收单交易预支付接口
	 * @param out_trade_no
	 * @return
	 * @throws AlipayApiException
	 */
	@SuppressWarnings("unused")
	public static String addPrecreate(String out_trade_no,String total_amount,String subject) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("out_trade_no", out_trade_no);
		jsonObject.addProperty("total_amount", total_amount);
		jsonObject.addProperty("subject", subject);
		request.setBizContent(jsonObject.toString());
		AlipayTradePrecreateResponse response = ALIPAY_CLIENT.execute(request);
		return response.getBody();
		
	}

	/**
	 * 统一收单交易创建接口
	 * @param busWebTrade
	 * @return
	 * @throws AlipayApiException
	 */
	public static String addTradeCreate(Orders orders,String subject) 
			throws AlipayApiException {
		AlipayTradeCreateRequest alipayRequest = new AlipayTradeCreateRequest();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("out_trade_no", orders.getOrderTradeNo());
		jsonObject.addProperty("total_amount", orders.getTransactionMoney());
		jsonObject.addProperty("subject", subject);
		 
		alipayRequest.setBizContent(jsonObject.toString());//填充业务参数
		AlipayTradeCreateResponse response = ALIPAY_CLIENT.execute(alipayRequest);
		if(response.isSuccess()){
			return response.getBody();
		} else {
			return response.getBody();
		}
	}
	
	/**
	 * 查询账单信息
	 * @param out_trade_no
	 * @return
	 * @throws AlipayApiException
	 */
	public static String cancelAliPay(String out_trade_no) throws AlipayApiException {
		AlipayTradeCancelRequest alipayRequest = new AlipayTradeCancelRequest();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("out_trade_no", out_trade_no);
		alipayRequest.setBizContent(jsonObject.toString());
		AlipayTradeCancelResponse response = ALIPAY_CLIENT.execute(alipayRequest);
		if(response.isSuccess()){
			return response.getBody();
		}else {
			return response.getBody();
		}
	}
	
	/***
	 * 退款
	 * @param out_trade_no
	 * @param refund_amount
	 * @return
	 * @throws AlipayApiException
	 */
	public static String refundAliPay(String out_trade_no,String refund_amount,String refund_reason) throws AlipayApiException {
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("out_trade_no", out_trade_no);
		jsonObject.addProperty("refund_amount", refund_amount);
		if(!CommonUtil.isNull(refund_reason)) {
			jsonObject.addProperty("refund_reason", refund_reason);
		}
		alipayRequest.setBizContent(jsonObject.toString());
		AlipayTradeRefundResponse response = ALIPAY_CLIENT.execute(alipayRequest);
		return response.getBody();
	}
	
	
	//统一收单交易支付接口(未使用)
	private static void addAliPay() throws AlipayApiException {
		//实例化客户端
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		
		AlipayTradePayResponse response = ALIPAY_CLIENT.execute(request);
		if(response.isSuccess()){
			loger.info("调用成功");
		} else {
			loger.info("调用失败");
		}
	}
	
}
