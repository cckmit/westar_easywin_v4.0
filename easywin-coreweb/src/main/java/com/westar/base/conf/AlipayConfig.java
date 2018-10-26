package com.westar.base.conf;

import com.westar.base.util.PublicConfig;
/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2017060907453332";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCDyjLyzQ40+KHfmyBVchbITxnBXi9DfSotLuuOcadvKQ/R1lddsQc0WSuEQWFWwezgqAXVToOu0P39Yeu1+YKBDzfwuJNq+jPlLvowMEjhg752R2KbQoHinasD/+zFI+sBVeaIQFhcXtvBco70W6bEOnBArA9UQ/5IxkfwvBK/0x74ahbmFAvZRZMcNhQ1pAF7KOjwgl14XDYs32z6V2QecGzZW2Y1IBMXAbgJ50oYlByS72GnnBB/8oAukcmai8wBNtNdhBy16hISBKXihbPSSEvtOY5RrXpcBfqYRtIrqAAgpOzQA5LEuuy1h6jYXa1/hc4QnjRrbqdtpEQZVAZFAgMBAAECggEAOAfER3QkwSKf96hFQ+6gfwMaSRrY71GMl+8l2Ma7T8heYHFHbZ7xGDF9DdqaHr+ODSmCaWDbHXzfL1XtV82/zU0CFIb0rfu4Co1BczFkGvTfCfxpkObiv80WwzmQmXQe2s+BlpX8uzDdL06cNomsyiqqROSiE8pgluIt/cyIECN96CGeIBlzQw8ACYRCcJvCVl4ZB2XbHFAdnWzzv0QQAKuFRJ0e0IuLtLlFUro3uK6N5QpJuciU3I3wJpeajnvJXLyXKGI6wDDN3kOD7IyXp+y1PPUZhBaRICgQcztp55FUbRgdu79T8mhdcWqjJ4IRYM4AIHmrTEv8Vqc3YZDTFQKBgQDPTB4jF+0UQ3pPxN7UvHzyR8Y837HbK9C+IiZz4B5xuDPdRPwAY1Uh6vQmYRGZtPJcDFxvXVcmzITej7rL27eC0YVoYZHRQ0r5ZaKBI+IG5SOE4XW1RHonj9SnA04y0eU46lIoexxoJQ/W02jfFarYirBW5EXpPETdqIgLR9kVmwKBgQCiwLBWBo0H/WlnHR/Ft7OM+w9mO9eR2jgB6xx8EpuFO3QL73UA51W4v0wOC0uoCdPgHfpwqvV6dCBFzUJGoaxUWpdjylQnIja9nMK0qXdE15E89yT8emDknFmZrEqFd/7r8PDvyo/Bp8c9Aco34AiABCfYFPa6+1m+Id/d7N4BnwKBgQCk2V5aAAvsAmFBEEk4GhCuxz8xZ57TsXtVyGiV25O6UYcFHnfcgyHPUD4KS0Uo28mkQ7w6X5sP4rKDHv+oYDkGUkJqKLVCGeaEgDuDwRpc61mcsYLevO77iRjOHAXSyjIkWcsuIAEOHPMEnqiWR2/OtjPXVb725JPLRduyRNewowKBgBbRNOeam4MXQAO4zY+GpaGKQSpwEVTdZyS9tYyffzY6k5Zox9qMlDQGfb8qTj0Mw6uUCFxrW2cDiNLAaB5G5EYfPUI8R3gFD++dXp69c38vSalmbMUI0zS5xsYIpZn4rMCfUugLWgQoZYJkEdmriwWHj553se+c9YP2c9BFOQq5AoGADQPGrtpEoWYCqOoSg7vxMdIkQq5EaYJV05pjMl1Ejy8s2uqRqok+AYIUAOVnrKqOl0UC964AnxzbgW0tJDsXxAJUnolgOeKMOzNxRSsQzDE/2GxvUt8QXznglPP3h6eUtLJBCM8X6fwtXcBCv0AaFUgc7HIhmJON4S6Y8qKKFZo=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArD/4nZGIS4Sr+Pq+7lFJSPMCQQoBVylgBg6AojWvK501Qkd4ZKMyRNvjRVny11B7yT3anR54tNlNTKd3joFSxuxbu1QZz+k0ZgGwChBnLdiiphno8xNj7yJBrFeTq2O7NSNYt1lDC2XaC8vS6qwSKDVI+CSseEkesFWv2G45GBGUEUiHmC+Qkzqr76SEJgn1RX1rcIAIukh05uPiHmf0m0TFPgxbPElxNErDQmvUVR/eCesDUtpN+SfY79nDrZHVQMTLcz1AzqTqUrbP9aID1gWtk40Iuxb8+RlsS2k1DzkYZSzrmrYtru4ELMEpMSqvLtL5wa1ykZEe0LXFlyK8XQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = PublicConfig.PAY_MENT_CONF.get("alipay.notify_url");

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = PublicConfig.PAY_MENT_CONF.get("alipay.return_url");

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}
