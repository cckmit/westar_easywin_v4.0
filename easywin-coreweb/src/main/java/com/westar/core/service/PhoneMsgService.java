package com.westar.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.PhoneMsg;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CommonUtil;
import com.westar.core.dao.PhoneMsgDao;

@Service
public class PhoneMsgService {
	/**
	 * 账号
	 */
	private final String userName = "CDJS001157";
	/**
	 * 密码
	 */
	private final String pwd = "zm0513@";
	/**
	 * 服务器地址
	 */
	private final String httpUrl = "https://sdk2.028lk.com/sdk2/BatchSend2.aspx";
	/**
	 * 编码
	 */
	private final String ENCODING="UTF-8";
	/**
	 * 短信模板
	 */
	public static String SMS_REGISTER_VCODE = "您正在申请手机号码注册，验证码为%s；20分钟后失效。如非本人操作，请致电028-85139468-824。【%s】";
	public static String SMS_AUTHORITY_VCODE = "您正在进行权限验证，验证码是%s；20分钟后失效。如非本人操作，请致电028-85139468-824。【%s】";
	public static String SMS_JOB_TO_DO = "您有新待办工作：%s，需要您办理。【%s】";
	public static String SMS_REGEIST_APPLY_OK = "您申请的团队%s,账号%s已被激活。【%s】";
	public static String SMS_REGEIST_APPLY_NO = "您申请的团队%s,账号%s审核未通过。%s【%s】";
	
	/**
	 * 需要初始密码
	 */
	public static String SMS_REGEIST_INVITE_PY = "请激活“%s”团队信息。激活码:%s,账号:%s,初始密码:%s【%s】";
	/**
	 * 不需要初始密码
	 */
	public static String SMS_REGEIST_INVITE_PN = "请激活“%s”团队信息。激活码:%s,账号:%s,密码为已有的系统密码【%s】";

	@Autowired
	PhoneMsgDao phoneMsgDao;
	
	@Autowired
	SystemLogService systemLogService;
	
	/**
	 * 保存短信到数据库
	 * @param msg
	 * @return
	 */
	public void saveMsg(String phone,String content,Integer comId){
		if(CommonUtil.isPhone(phone)&&!CommonUtil.isNull(content)&&!CommonUtil.isNull(comId)){
			PhoneMsg msg = new PhoneMsg();
			msg.setComId(comId);
			msg.setPhone(phone);
			msg.setContent(content);
			phoneMsgDao.add(msg);
		}
	}
	/**
	 * 根据用ID发送手机号码
	 * @param userId
	 */
//	public void saveMsgByUserid(Integer userId,String content){
//		UserInfo user = (UserInfo)phoneMsgDao.objectQuery(UserInfo.class,userId);
//		if(!CommonUtil.isNull(user)){
//			this.saveMsg(user.getMovePhone(), content);
//		}
//	}
	
	/**
	 * 发送手机短信
	 * @param phone 电话号码
	 * @param content 短信内容
	 * @param busType 启用模版标识符
	 * @param comId 属于哪个团队的短信
	 */
	public void sendMsg(String phone,Object[] argsObj,String busType,Integer comId,String optIP){
		//验证电话号码是否正确，验证发送短信是否为空
		if(!CommonUtil.isPhone(phone)&& null !=argsObj && argsObj.length>0&&CommonUtil.isNull(comId)){
			return;
		}
		Object[]  args=Arrays.copyOf(argsObj,argsObj.length+1);
		args[argsObj.length]="捷成软件";
		String content = "";
		//模版套用
		if(ConstantInterface.MSG_REGISTER_VCODE.equals(busType)){
			//注册验证码模版
			content = String.format(SMS_REGISTER_VCODE, args);
//			content = SMS_REGISTER_VCODE.replace("XX", content);
		}else if(ConstantInterface.MSG_AUTHORITY_VCODE.equals(busType)){
			content = String.format(SMS_AUTHORITY_VCODE, args);
			//权限验证码模版
//			content = SMS_AUTHORITY_VCODE.replace("XX", content);
		}else if(ConstantInterface.MSG_JOB_TO_DO.equals(busType)){
			content = String.format(SMS_JOB_TO_DO, args);
			//待办工作模版
//			content = SMS_JOB_TO_DO.replace("XX", content);
		}else if (ConstantInterface.MSG_REGEIST_APPLY_OK.equals(busType)){
			content = String.format(SMS_REGEIST_APPLY_OK, args);
		}else if (ConstantInterface.MSG_REGEIST_APPLY_NO.equals(busType)){
			content = String.format(SMS_REGEIST_APPLY_NO, args);
		}else if(ConstantInterface.MSG_REGEIST_INVITE_PN.equals(busType)){//不需要密码
			
			content = String.format(SMS_REGEIST_INVITE_PN, args);
			
		}else if(ConstantInterface.MSG_REGEIST_INVITE_PY.equals(busType)){//需要密码
			
			content = String.format(SMS_REGEIST_INVITE_PY, args);
			
		}

		CloseableHttpClient client = HttpClients.createDefault();
		PhoneMsg sendSms = new PhoneMsg();
		//给短信内容加上签名
//		sendSms.setContent(content+"【捷成软件】");
		sendSms.setContent(content);
		//构建表单参数
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("CorpID", userName));//短信平台账号
		params.add(new BasicNameValuePair("Pwd", pwd));//短信平台密码
		params.add(new BasicNameValuePair("Mobile",phone));
		params.add(new BasicNameValuePair("Content", sendSms.getContent()));
		UrlEncodedFormEntity urlEncodedFormEntity=new UrlEncodedFormEntity(params, Consts.UTF_8);
		
		//设置请求和传输超时时间5秒
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5*1000).setConnectTimeout(5*1000).build();
		
		//创建链接
		HttpPost httpPost=new HttpPost(httpUrl);
		httpPost.setEntity(urlEncodedFormEntity);
		httpPost.setConfig(requestConfig);

		CloseableHttpResponse response=null;
		try {
			//发送请求
			response=client.execute(httpPost);
		}catch (Exception e) {
			//系统日志
	        systemLogService.addSystemLog(1,"系统平台", "用户（"+phone+"）发送手机短信错误反馈：短信Http接口调用失败。", 
	        		ConstantInterface.SYS_INFO,-1,optIP);
			return;
		}
		//response返回正常
		if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				 int resultCode=-2;
				 try {
					 resultCode=Integer.parseInt(EntityUtils.toString(entity, ENCODING));
				 } catch (Exception e) {
					//系统日志
			        systemLogService.addSystemLog(1,"系统平台", "用户（"+phone+"）发送手机短信错误反馈：短信发送读取response响应内容失败。",
			        		ConstantInterface.SYS_INFO,-1,optIP);
				 }
				 if(resultCode>=0){
					 this.saveMsg(phone, content,comId);//记录发送成功的短信信息 
				 }else{
					 //发送失败
					 String remark="";
					if(resultCode==-1){
						remark="账号未注册。";
					}else if(resultCode==-2){
						remark="其他错误。";
					}else if(resultCode==-3){
						remark="帐号或密码错误。";
					}else if(resultCode==-5){
						remark="余额不足，请充值。";
					}else if(resultCode==-6){
						remark="定时发送时间不是有效的时间格式。";
					}else if(resultCode==-7){
						remark="提交信息末尾未签名，请添加中文的企业签名【 】。";
					}else if(resultCode==-8){
						remark="发送内容需在1到300字之间。";
					}else if(resultCode==-9){
						remark="发送号码为空。";
					}else if(resultCode==-10){
						remark="定时时间不能小于系统当前时间。";
					}else if(resultCode==-100){
						remark="限制IP访问。";
					}
					//系统日志
			        systemLogService.addSystemLog(1,"系统平台", "用户（"+phone+"）发送手机短信错误反馈："+remark, 
			        		ConstantInterface.SYS_INFO,-1,optIP);
				 }
			 }
		}else{
			//系统日志
	        systemLogService.addSystemLog(1,"系统平台", "用户（"+phone+"）发送手机短信错误反馈：短信Http接口调用成功，但返回状态错误，错误码为【"+response.getStatusLine().getStatusCode()+"】。", 
	        		ConstantInterface.SYS_INFO,-1,optIP);
		}
	
	}
}
