<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">
var pageIndex;

//关闭窗口
function closeWin(){
	window.parent.location.replace("/")
}
	function setWindow(data,indexParam){
		pageIndex = indexParam;
		$("#macSpan").html(data.mac)
		if(data.status=='f1'){
		}else if(data.status=='f2'){
			 
		}else if(data.status=='f3'){
			 
		}
		$(function(){
			$("#contentBody").on("click","#sendLicCode",function(){//发送服务激活码
				macValidateSet.sendLicCode(data.mac)
			})
			$("#contentBody").on("click","#updateLicCode",function(){//设置
				macValidateSet.updateLicCode(data.mac);
			})
		})
	}
	
	var macValidateSet = {
			sendLicCode:function(mac){
				var serviceDate = $("#contentBody").find("input[name='serviceDate']").val();
				if(!serviceDate){
					layer.tips("请填写有效时间！",$("#contentBody").find("input[name='serviceDate']"),{tips:1});
					return null;
				}
				
				var params = {};
				params.serviceDate = serviceDate;
				params.macName = mac;
				
				if(params){
					getSelfJSON("/bandMAC/sendMacValiCode", params, function(data){
						if(data.status=='y'){
							showNotification(1,"发送成功");
						}else{
							showNotification(2,data.info);
						}
					})
				}
			},
			updateLicCode:function(mac){
				var serviceDate = $("#contentBody").find("input[name='serviceDate']").val();
				if(!serviceDate){
					layer.tips("请填写有效时间！",$("#contentBody").find("input[name='serviceDate']"),{tips:1});
					return null;
				}
				
				var licenseCode = $("#contentBody").find("input[name='licenseCode']").val();
				if(!licenseCode){
					layer.tips("请填写激活码！",$("#contentBody").find("input[name='licenseCode']"),{tips:1});
					return null;
				}
				
				var yzm = $("#contentBody").find("input[name='yzm']").val();
				if(!yzm){
					layer.tips("请填写正确验证码！",$("#contentBody").find("input[name='yzm']"),{tips:1});
					return null;
				}
				
				var params = {};
				params.serviceDate = serviceDate;
				params.licenseCode = licenseCode;
				params.macName = mac;
				params.yzm = yzm;
				
				if(params){
					getSelfJSON("/bandMAC/macValidatSet", params, function(data){
						if(data.status=='y'){
							window.parent.location.replace("/")
						}else if(data.status=='n'){
							layer.tips("请填写验证码！",$("#contentBody").find("input[name='yzm']"),{tips:1});
						}else{
							showNotification(2,data.info);
						}
					})
				}
			}
			
	}
</script>
</head>
<body>
	<div class="container no-padding" style="width: 100%">	
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;">
	            	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	                	<span class="widget-caption themeprimary" style="font-size: 20px" id="titleHead">服务激活</span>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" id="contentBody"
            			style="height: 360px;overflow-y:auto;position: relative;">
            		
            			<div style="text-align: center;">
	            			<div class="margin-bottom-10" style="font-size: 25px">激活信息 </div>
	            			<div class="widget radius-bordered" style="height:230px;text-align:left;">
	            				<div class="clear-fix margin-top-10 padding-left-20" style="height: 100px">
									<div style="line-height: 35px">
										<span style="font-size: 18px;width: 150px;">服务器MAC:</span>
										<span style="font-size: 15px;line-height: 30px;" id="macSpan">
											
										</span>
									</div>
									<div style="line-height: 35px">
										<span style="font-size: 18px;width: 150px;">&nbsp;有&nbsp;效&nbsp;时&nbsp;间:</span>
										<span style="font-size: 15px;line-height: 30px;">
											<input type="text" name="serviceDate" onClick="WdatePicker({minDate:'%y-%M-{%d}'})" style="width: 250px" readonly="readonly">
										</span>
									</div>
									<div style="line-height: 35px">
										<span style="font-size: 18px;width: 150px;">服务激活码:</span>
										<span style="font-size: 15px;line-height: 30px;">
											<input type="text" name="licenseCode" style="width: 250px">
										</span>
									</div>
									<div style="line-height: 35px">
										<span style="font-size: 18px;width: 150px;">&nbsp;&nbsp;验&nbsp;&nbsp;证&nbsp;&nbsp;码&nbsp;:</span>
										<span style="font-size: 15px;line-height: 30px;">
												<input name="yzm" datatype="*"
										 type="text" placeholder="验证码" ajaxurl="/registe/checkYzm/bandmac" 
										  style="width: 150px;" maxlength="4">
										</span>
										<span style="line-height: 35px;text-align: center;">
											 <img style="height: 30px;vertical-align: middle;" 
											 src="/registe/AuthImage/bandmac" id="yz" width="100" 
											onclick="this.src='/registe/AuthImage/bandmac?rnd=' + Math.random();$('#yzm').val('')"/>
										
										</span>
									</div>
									
									<div class="clearfix margin-top-10" style="vertical-align: middle;text-align: center;">
										<button type="button" class="btn btn-success ws-btnRed margin-right-50" id="sendLicCode">生成</button>
										<button type="button" class="btn btn-info ws-btnBlue margin-right-50" id="updateLicCode">激活</button>
										<button type="button" class="btn btn-default" style="background-color: #ccc;" onclick="closeWin()" >关闭</button>
									</div>
								</div>
	            			</div>
	            			<div style="text-align: left;">
	            				<span style="color: red;">注：服务管理员登录功能仅提供给供应商的实施人员！</span>
	            			</div>
	            		</div>
					</div>
					
            	</div>
            </div>
        </div>
     </div>
</form>

</body>
</html>
