package com.westar.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom.JDOMException;

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
import com.westar.base.weChat.HttpUtil;
import com.westar.base.weChat.PayCommonUtil;
import com.westar.base.weChat.PayConfigUtil;
import com.westar.base.weChat.XMLUtil;

/**
 * 平台支付
 * @author zzq
 *
 */
public class WeChartPayMentUtil {
	
	private static Log log = LogFactory.getLog(WeChartPayMentUtil.class);
	
	//微信证书地址配置
	private static String WECHATCERTPATH = null;
	
	static{
		Map<String, String> weChatCert = ResourceUtils.getResource("weChatCert").getMap();
		WECHATCERTPATH = weChatCert.get("cert.path");
	}
	
	/**
	 * 微信订单查询
	 * @param orderTradeNo 订单编号
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static  Map<String, String> weChatOrderQuery(String orderTradeNo) throws JDOMException,
			IOException {
		// 账号信息
		String appid = PayConfigUtil.APP_ID; // appid
		String mch_id = PayConfigUtil.MCH_ID; // 商业号
		String key = PayConfigUtil.API_KEY; // key
		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;

		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no",orderTradeNo);
		String sign = PayCommonUtil.createSign("UTF-8", packageParams,key);
		packageParams.put("sign", sign);
		String requestXML = PayCommonUtil.getRequestXml(packageParams);
		String resXml = HttpUtil.postData(PayConfigUtil.ORDERQUERY_URL, requestXML);//订单查询
		Map<String, String> map = XMLUtil.doXMLParse(resXml);
		return map;
	}
	
	/**
	 * 订单申请退款
	 * @param orderTradeNo 订单号
	 * @param refundMoney 退款金额
	 * @throws KeyStoreException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 * @throws KeyManagementException 
	 * @throws Exception 
	 */
	public static Map<String, String> weChatOrderRefund(String orderTradeNo,String refundMoney) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException{
		// 账号信息
		String appid = PayConfigUtil.APP_ID; // appid
		String mch_id = PayConfigUtil.MCH_ID; // 商业号
		String key = PayConfigUtil.API_KEY; // key
		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;
		
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no",orderTradeNo);
		packageParams.put("out_refund_no",orderTradeNo);
		packageParams.put("total_fee",refundMoney);
		packageParams.put("refund_fee",refundMoney);
		packageParams.put("op_user_id",mch_id);
		String sign = PayCommonUtil.createSign("UTF-8", packageParams,key);
		packageParams.put("sign", sign);
		String requestXML = PayCommonUtil.getRequestXml(packageParams);
		log.error("weChatCertPath="+WECHATCERTPATH);
		 /*读取证书文件,这一段是直接从微信支付平台提供的demo中copy的，所以一般不需要修改---- */
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(WECHATCERTPATH));
        try {
            keyStore.load(instream, mch_id.toCharArray());
        } finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        try {
			/*发送数据到微信的退款接口---- */
			HttpPost httpost = new HttpPost(PayConfigUtil.REFUND_URL);  
			httpost.setEntity(new StringEntity(requestXML, "UTF-8"));
			HttpResponse response = httpClient.execute(httpost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			Map<String, String> map = XMLUtil.doXMLParse(jsonStr);
			if(ConstantInterface.RETURN_CODE_SUCCESS.equals(map.get("return_code"))){
				log.info("订单“"+orderTradeNo+"”退款成功。");
			}else{
				log.info("订单“"+orderTradeNo+"”退款失败。");
			}
			return map;
		} catch (Exception e) {
			log.info("订单“"+orderTradeNo+"”退款失败。");
			return null;
		}
	}
	
	/**
	 * 微信下订单
	 * @param out_trade_no 订单号
	 * @param body 订单描述
	 * @param order_price  价格 注意：价格的单位是分
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> initWeChatOrder(String out_trade_no,String body,String order_price) throws Exception {
		// 账号信息
		String appid = PayConfigUtil.APP_ID; // appid
		// String appsecret = PayConfigUtil.APP_SECRET; // appsecret
		String mch_id = PayConfigUtil.MCH_ID; // 商业号
		String key = PayConfigUtil.API_KEY; // key

		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;

		// 获取发起电脑 ip
		String spbill_create_ip = PayConfigUtil.CREATE_IP;
		// 回调接口
		String notify_url = PayConfigUtil.NOTIFY_URL;
		String trade_type = "NATIVE";

		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", order_price);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);

		String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
		packageParams.put("sign", sign);

		String requestXML = PayCommonUtil.getRequestXml(packageParams);

		String resXml = HttpUtil.postData(PayConfigUtil.UFDODER_URL, requestXML);//下单

		Map<String, String> map = XMLUtil.doXMLParse(resXml);
		// String return_code = (String) map.get("return_code");
		// String prepay_id = (String) map.get("prepay_id");
		//String urlCode = (String) map.get("code_url");

		return map;
	}
	
	
}
