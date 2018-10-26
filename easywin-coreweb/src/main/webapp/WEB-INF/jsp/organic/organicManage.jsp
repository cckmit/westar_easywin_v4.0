<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<%pageContext.setAttribute("requestURI",request.getRequestURI().replace("/","_").replace(".","_"));%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<body>
<div class="page-content">
         	<!-- Page Body -->
            <div class="page-body">
            	<div class="row">
                	<div class="col-md-12 col-xs-12 ">
                    	<div class="widget">
                         	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                            	 <span class="widget-caption themeprimary" style="font-size: 18px">团队管治</span>
                            </div>
                            <div class="widget-body padding-top-50">
	                            <form class="subform"  method="post" id="orgForm">
								<input type="hidden" value="${organic.id}" name="id"/>
								<input type="hidden" name="sid" value="${param.sid }"/>
								<input type="hidden" name="yzmVal" id="yzmVal"/>
								<input type="hidden" name="redirectPage"/>
								<input type="hidden" name="enabled"/>
								<input type="hidden" name="orgNum" value="${organic.orgNum }"/>
								<input type="hidden" name="noticeType" id="noticeType" value="${empty userInfo.movePhone?'email':'phone'}"/>
	                            	<div class="row">
	                            		<div class="col-sm-6">
	                            			<div>
												<div class="ws-pic">
													<img src="/static/images/js.png"/>
												</div>
												<p class="ws-tests">解散后团队的所有数据将清除</p>
												<a href="javascript:void(0)" onclick="disMissOrg()" class="ws-trends">解散团队 <font color="red">(谨慎使用）</font> </a>
											</div>
	                            		</div>
	                            		<div class="col-sm-6">
	                            			<div>
												<div class="ws-pic">
													<img src="/static/images/dj.png"/>
												</div>
												<p class="ws-tests">冻结后团队的注册人员能登录，其他成员不能进入，团队数据仍存在 </p>
												<c:if test="${organic.enabled==1}">
													<a href="javascript:void(0)" onclick="disableOrg()" class="ws-trends">冻结团队</a>
												</c:if>
												<c:if test="${organic.enabled==0}">
													<a href="javascript:void(0)" onclick="enableOrg()" class="ws-trends">恢复团队</a>
												</c:if>
											</div>
	                            		</div>
										<div class="col-sm-6">
											<div>
												<div class="ws-pic">
													<img src="/static/images/yj.png"/>
												</div>
												<p class="ws-tests">团队只能有一位系统管理员，移交后当前帐号会被降为普通人员。</p>
												<a href="javascript:void(0)" onclick="transferAuthorityPage()" class="ws-trends">团队移交</a>
											</div>
										</div>
	                            		<div class="ws-clear"></div>
	                            	</div>
	                            </form>
                            </div>
                        </div>             
                </div>
                <!-- /Page Body -->
            </div>
            
        </div>
<style type="text/css">
.aui_content{
	padding-left: 0px !important;
}
.sendBtn{
    display: inline-block;
    background: #D0EEFF;
    border: 1px solid #99D3F5;
    border-radius: 4px;
    padding: 4px 12px;
    overflow: hidden;
    color: #1E88C7;
    text-decoration: none;
    text-indent: 0;
    line-height: 20px;
    width: 100px;
    text-align: center;
    cursor: pointer;
}
.resendBtn{
 	display: inline-block;
    background: #F5F5F5;
    border: 1px solid #DDD;
    border-radius: 4px;
    padding: 4px 12px;
    overflow: hidden;
    color:#ACA899;
    text-decoration: none;
    text-indent: 0;
    line-height: 20px;
    width: 120px;
    text-align: center;
    cursor: auto;
}
/**定义一个单选按钮显示样式*/
.inputradio {
	width: 16px !important;
	height: 16px !important;
	float: left !important;
	opacity:1 !important;
	position:static !important;
	margin-top: 9px !important;
	margin-left: 5px !important;
}

.radiotext {
	float: left !important;
	margin-top: 2px !important;
	font-size: 12px !important;
}
</style>
<script type="text/javascript">
//解散团队
function disMissOrg(){
	var content="";
	content+='\n	<font color="red">注:解散团队后,团队的团队数据将全部清除,不能够被恢复!</font>';
	content+='\n	<ul class="tickets-list">';
	content+='\n  		<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
	content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">获取方式：</div>';
	content+='\n	    	<div class="ws-form-group">';
	if(!strIsNull(${userInfo.movePhone})){
		content+='\n				<input type="radio" name="noticeType"';
		content+='\n					class="inputradio" checked="checked" value="phone" onclick="byWhich(this)" />';
		content+='\n				<i class="radiotext">手机</i>';
	}
	if(!strIsNull("${userInfo.email}")){
		content+='\n				<input type="radio" name="noticeType"';
		content+='\n					class="form-control inputradio"';
		content+=					strIsNull(${userInfo.movePhone})?"checked=\"checked\"":"";
		content+='\n							value="email"  onclick="byWhich(this)"/>';
		content+='\n				<i class="radiotext">邮件</i>';
	}
	content+='\n			</div>';
	content+='\n		</li>';
	if(!strIsNull(${userInfo.movePhone})){
		content+='\n  		<li id="phone" class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
		content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">接收手机</div>';
		content+='\n 			<div class="ticket-user pull-left ps-right-box">';
		content+='\n 				<div readonly="readonly" style="width: 250px;border: 1px solid #d5d5d5;">';
		content+='\n 				<span class="padding-left-10">${userInfo.movePhone}</span>';
		content+='\n 				</div>';
		content+='\n 			</div>';
		content+='\n		</li>';
	}
	if(!strIsNull("${userInfo.email}")){
		content+='\n  		<li id="email" class="ticket-item no-shadow ps-listline margin-top-5"';
		content+=				strIsNull(${userInfo.movePhone})?"style=\"border: 0;\">":"style=\"border: 0;display:none\">";
		content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">接收邮箱</div>';
		content+='\n 			<div class="ticket-user pull-left ps-right-box">';
		content+='\n 				<div readonly="readonly" style="width: 250px;border: 1px solid #d5d5d5;">';
		content+='\n 				<span class="padding-left-10">${userInfo.email}</span>';
		content+='\n 				</div>';
		content+='\n 			</div>';
		content+='\n		</li>';
	}
	content+='\n  		<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
	content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">验证码</div>';
	content+='\n 			<div class="ticket-user pull-left ps-right-box">';
	content+='\n 				<div style="float: left">';
	content+='\n 					<input type="text" id="yzmLayer" style="width:100px" class="colorpicker-default form-control"';
	content+='\n 						onblur="checkYzm()" style="float: left;" />';
	content+='\n 					<div style="clear: both"></div>';
	content+='\n 				</div>';
	content+='\n 				<button type="button" id="sendMail" class="sendBtn" onclick="sendPassYzm()">获取验证码</button>';
	content+='\n 				<div style="clear: both"></div>';
	content+='\n 				<span id="msg" style="color:red;float:left;width:auto !important;"></span>';
	content+='\n 			</div>';
	content+='\n		</li>';
	content+='\n 	</ul>';

	layui.use('layer', function(){
		var layer = layui.layer;
		window.top.layer.open({
			title: '解散团队',
			type:0,
			icon:7,
			area:'450px',
			content:content,
			btn:['解散','关闭'],
			success:function(){
				$("#yzmLayer").focus(function(){
					$("#msg").html("");
				})
			},yes:function(index,layero){
				var yzm = $("#yzmLayer").val();
				if(!strIsNull(yzm)){
					var msg = $("#msg").html();
					if(msg){
			        	return false;
					}else{
						window.top.layer.close(index);

						window.top.layer.confirm("确定解散团队，清除团队数据?", {
							  btn: ['确定','取消']//按钮
						  ,title:'询问框'
						  ,icon:3
						}, function(index){
							$("#orgForm").attr("action","/organic/disMissOrg/${requestURI}?yzm="+yzm);
							$("#orgForm input[name='enabled']").val("-1");
							$("#orgForm input[name='redirectPage']").val(window.location.href);
							$('#orgForm').submit();
						});
					}
				}else{
					$("#msg").html("请输入验证码");
		        	return false;
				}
			}
		})
	});


}



//冻结团队
function disableOrg(){
	var content="";
	content+='\n	<font color="red">注:冻结团队后,团队的数据不会被清除,非超级管理员不得登录!</font>';
	content+='\n	<ul class="tickets-list">';
	content+='\n  		<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
	content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">获取方式：</div>';
	content+='\n	    	<div class="ws-form-group">';
	if(!strIsNull(${userInfo.movePhone})){
		content+='\n				<input type="radio" name="noticeType"';
		content+='\n					class="inputradio" checked="checked" value="phone" onclick="byWhich(this)" />';
		content+='\n				<i class="radiotext">手机</i>';
	}
	if(!strIsNull("${userInfo.email}")){
		content+='\n				<input type="radio" name="noticeType"';
		content+='\n					class="form-control inputradio"';
		content+=					strIsNull(${userInfo.movePhone})?"checked=\"checked\"":"";
		content+='\n							value="email"  onclick="byWhich(this)"/>';
		content+='\n				<i class="radiotext">邮件</i>';
	}
	content+='\n			</div>';
	content+='\n		</li>';
	if(!strIsNull(${userInfo.movePhone})){
		content+='\n  		<li id="phone" class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
		content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">接收手机</div>';
		content+='\n 			<div class="ticket-user pull-left ps-right-box">';
		content+='\n 				<div readonly="readonly" style="width: 250px;border: 1px solid #d5d5d5;">';
		content+='\n 				<span class="padding-left-10">${userInfo.movePhone}</span>';
		content+='\n 				</div>';
		content+='\n 			</div>';
		content+='\n		</li>';
	}
	if(!strIsNull("${userInfo.email}")){
		content+='\n  		<li id="email" class="ticket-item no-shadow ps-listline margin-top-5"';
		content+=				strIsNull(${userInfo.movePhone})?"style=\"border: 0;\">":"style=\"border: 0;display:none\">";
		content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">接收邮箱</div>';
		content+='\n 			<div class="ticket-user pull-left ps-right-box">';
		content+='\n 				<div readonly="readonly" style="width: 250px;border: 1px solid #d5d5d5;">';
		content+='\n 				<span class="padding-left-10">${userInfo.email}</span>';
		content+='\n 				</div>';
		content+='\n 			</div>';
		content+='\n		</li>';
	}
	
	content+='\n  		<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
	content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">验证码</div>';
	content+='\n 			<div class="ticket-user pull-left ps-right-box">';
	content+='\n 				<div style="float: left">';
	content+='\n 					<input type="text" id="yzmLayer" style="width:100px" class="colorpicker-default form-control"';
	content+='\n 						onblur="checkYzm()" style="float: left;" />';
	content+='\n 					<div style="clear: both"></div>';
	content+='\n 				</div>';
	content+='\n 				<button type="button" id="sendMail" class="sendBtn" onclick="sendPassYzm()">获取验证码</button>';
	content+='\n 				<div style="clear: both"></div>';
	content+='\n 				<span id="msg" style="color:red;float:left;width:auto !important;"></span>';
	content+='\n 			</div>';
	content+='\n		</li>';
	content+='\n 	</ul>';
	layui.use('layer', function(){
		var layer = layui.layer;
		window.top.layer.open({
			title: '冻结团队',
			type:0,
			icon:7,
			area:'450px',
			content:content,
			btn:['冻结','关闭'],
			success:function(){
				$("#yzmLayer").focus(function(){
					$("#msg").html("");
				})
			},yes:function(index,layero){
				var yzm = $("#yzmLayer").val();
				if(!strIsNull(yzm)){
					var msg = $("#msg").html();
					if(msg){
			        	return false;
					}else{
						window.top.layer.close(index);

						window.top.layer.confirm("确定冻结团队?", {
							  btn: ['确定','取消']//按钮
						  ,title:'询问框'
						  ,icon:3
						}, function(index){
							$("#orgForm").attr("action","/organic/disableOrg/${requestURI}?yzm="+yzm);
							$("#orgForm input[name='redirectPage']").val(window.location.href);
							$("#orgForm input[name='enabled']").val("0");
							$('#orgForm').submit();
						});
					}
				}else{
					$("#msg").html("请输入验证码");
		        	return false;
				}
			}
		})
	});

}
//恢复团队
function enableOrg(){


	var content="";
	content+='\n	<font color="red">注:恢复团队后,团队数据可以使用,团队成员可以登录!</font>';
	content+='\n	<ul class="tickets-list">';
	content+='\n  		<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
	content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">获取方式：</div>';
	content+='\n	    	<div class="ws-form-group">';
	if(!strIsNull(${userInfo.movePhone})){
		content+='\n				<input type="radio" name="noticeType"';
		content+='\n					class="inputradio" checked="checked" value="phone" onclick="byWhich(this)" />';
		content+='\n				<i class="radiotext">手机</i>';
	}
	if(!strIsNull("${userInfo.email}")){
		content+='\n				<input type="radio" name="noticeType"';
		content+='\n					class="form-control inputradio"';
		content+=					strIsNull(${userInfo.movePhone})?"checked=\"checked\"":"";
		content+='\n							value="email"  onclick="byWhich(this)"/>';
		content+='\n				<i class="radiotext">邮件</i>';
	}
	content+='\n			</div>';
	content+='\n		</li>';
	if(!strIsNull(${userInfo.movePhone})){
		content+='\n  		<li id="phone" class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
		content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">接收手机</div>';
		content+='\n 			<div class="ticket-user pull-left ps-right-box">';
		content+='\n 				<div readonly="readonly" style="width: 250px;border: 1px solid #d5d5d5;">';
		content+='\n 				<span class="padding-left-10">${userInfo.movePhone}</span>';
		content+='\n 				</div>';
		content+='\n 			</div>';
		content+='\n		</li>';
	}
	if(!strIsNull("${userInfo.email}")){
		content+='\n  		<li id="email" class="ticket-item no-shadow ps-listline margin-top-5"';
		content+=				strIsNull(${userInfo.movePhone})?"style=\"border: 0;\">":"style=\"border: 0;display:none\">";
		content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">接收邮箱</div>';
		content+='\n 			<div class="ticket-user pull-left ps-right-box">';
		content+='\n 				<div readonly="readonly" style="width: 250px;border: 1px solid #d5d5d5;">';
		content+='\n 				<span class="padding-left-10">${userInfo.email}</span>';
		content+='\n 				</div>';
		content+='\n 			</div>';
		content+='\n		</li>';
	}
	content+='\n  		<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
	content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">验证码</div>';
	content+='\n 			<div class="ticket-user pull-left ps-right-box">';
	content+='\n 				<div style="float: left">';
	content+='\n 					<input type="text" id="yzmLayer" style="width:100px" class="colorpicker-default form-control"';
	content+='\n 						onblur="checkYzm()" style="float: left;" />';
	content+='\n 					<div style="clear: both"></div>';
	content+='\n 				</div>';
	content+='\n 				<button type="button" id="sendMail" class="sendBtn" onclick="sendPassYzm()">获取验证码</button>';
	content+='\n 				<div style="clear: both"></div>';
	content+='\n 				<span id="msg" style="color:red;float:left;width:auto !important;"></span>';
	content+='\n 			</div>';
	content+='\n		</li>';
	content+='\n 	</ul>';

	layui.use('layer', function(){
		var layer = layui.layer;
		window.top.layer.open({
			title: '恢复团队',
			type:0,
			icon:7,
			area:'450px',
			content:content,
			btn:['解冻','关闭'],
			success:function(){
				$("#yzmLayer").focus(function(){
					$("#msg").html("");
				})
			},yes:function(index,layero){
				var yzm = $("#yzmLayer").val();
				if(!strIsNull(yzm)){
					var msg = $("#msg").html();
					if(msg){
			        	return false;
					}else{
						window.top.layer.close(index);

						window.top.layer.confirm("确定恢复团队?", {
							  btn: ['确定','取消']//按钮
						  ,title:'询问框'
						  ,icon:3
						}, function(index){
							$("#orgForm").attr("action","/organic/enableOrg/${requestURI}?yzm="+yzm);
							$("#orgForm input[name='redirectPage']").val(window.location.href);
							$("#orgForm input[name='enabled']").val("1");
							$('#orgForm').submit();
						});
					}
				}else{
					$("#msg").html("请输入验证码");
		        	return false;
				}
			}
		})
	});
}

//移交对象id，用于单选时赋值持久
var transferObj = -1;
var transferDes = "";

//移交说明赋值
function setDes(ts){
    transferDes = $(ts).val();
}


//移交权限
function transferAuthority(sid){
    userOne("none", "none", "", sid,function(options){
        if(options.length>0){
            $.each(options,function(i,vo){
                transferObj = $(vo).val();
                $("#transferName").html("");
                $("#transferName").html("移交人：" + $(vo).text() + "&nbsp;&nbsp;");
                return false;
            });
        }
    });
}

//团队系统管理员权限移交
function transferAuthorityPage(){
    var content="";
    content+='\n	<font color="red">注:团队只能有一位系统管理员，移交后当前帐号会被降为普通人员!</font>';
    content+='\n	<ul class="tickets-list">';
    content+='\n  		<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
    content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">获取方式：</div>';
    content+='\n	    	<div class="ws-form-group">';
    if(!strIsNull(${userInfo.movePhone})){
        content+='\n				<input type="radio" name="noticeType"';
        content+='\n					class="inputradio" checked="checked" value="phone" onclick="byWhich(this)" />';
        content+='\n				<i class="radiotext">手机</i>';
    }
    if(!strIsNull("${userInfo.email}")){
        content+='\n				<input type="radio" name="noticeType"';
        content+='\n					class="form-control inputradio"';
        content+=					strIsNull(${userInfo.movePhone})?"checked=\"checked\"":"";
        content+='\n							value="email"  onclick="byWhich(this)"/>';
        content+='\n				<i class="radiotext">邮件</i>';
    }
    content+='\n			</div>';
    content+='\n		</li>';
    if(!strIsNull(${userInfo.movePhone})){
        content+='\n  		<li id="phone" class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
        content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">接收手机</div>';
        content+='\n 			<div class="ticket-user pull-left ps-right-box">';
        content+='\n 				<div readonly="readonly" style="width: 250px;border: 1px solid #d5d5d5;">';
        content+='\n 				<span class="padding-left-10">${userInfo.movePhone}</span>';
        content+='\n 				</div>';
        content+='\n 			</div>';
        content+='\n		</li>';
    }
    if(!strIsNull("${userInfo.email}")){
        content+='\n  		<li id="email" class="ticket-item no-shadow ps-listline margin-top-5"';
        content+=				strIsNull(${userInfo.movePhone})?"style=\"border: 0;\">":"style=\"border: 0;display:none\">";
        content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">接收邮箱</div>';
        content+='\n 			<div class="ticket-user pull-left ps-right-box">';
        content+='\n 				<div readonly="readonly" style="width: 250px;border: 1px solid #d5d5d5;">';
        content+='\n 				<span class="padding-left-10">${userInfo.email}</span>';
        content+='\n 				</div>';
        content+='\n 			</div>';
        content+='\n		</li>';
    }

    content+='\n  		<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">';
    content+='\n 			<div class="pull-left gray ps-left-text" style="font-size: 15px;min-width:70px">验证码</div>';
    content+='\n 			<div class="ticket-user pull-left ps-right-box">';
    content+='\n 				<div style="float: left">';
    content+='\n 					<input type="text" id="yzmLayer" style="width:100px" class="colorpicker-default form-control"';
    content+='\n 						onblur="checkYzm()" style="float: left;" />';
    content+='\n 					<div style="clear: both"></div>';
    content+='\n 				</div>';
    content+='\n 				<button type="button" id="sendMail" class="sendBtn" onclick="sendPassYzm()">获取验证码</button>';
    content+='\n 				<div style="clear: both"></div>';
    content+='\n 				<span id="msg" style="color:red;float:left;width:auto !important;"></span>';
    content+='\n 			</div>';
    content+='\n		</li>';
    content+='\n 	</ul>';
    layui.use('layer', function(){
        var layer = layui.layer;
        window.top.layer.open({
            title: '团队移交',
            type:0,
            icon:7,
            area:'450px',
            content:content,
            btn:['下一步','关闭'],
            success:function(){
                $("#yzmLayer").focus(function(){
                    $("#msg").html("");
                })
            },yes:function(index,layero){
                var yzm = $("#yzmLayer").val();
                if(!strIsNull(yzm)){
                    var msg = $("#msg").html();
                    if(msg){
                        return false;
                    }else{
                        window.top.layer.close(index);

                        var netStep = "<div style=\"width:99%;height:250px;padding:10px;\">" +
										  "<div class=\"pull-left padding-left-5\" style=\"width: 230px\">" +
                            				  "<font style=\"color:red;\">*</font><span id=\"transferName\">移交人：</span>" +
											  "<a href=\"javascript:void(0);\" class=\"btn btn-primary btn-xs no-margin-top margin-bottom-5\" title=\"人员选择\"  onclick=\"transferAuthority('${param.sid}')\">" +
											  "<i class=\"fa fa-plus\"></i>选择</a>" +
											  "<span id=\"noTransfer\" style=\"color:red;float:right;display:none;\">请选择移交人！</span>" +
										  "</div>" +
										  "<div class=\"pull-left padding-left-5\" style=\"width:100%;margin-top:10px;\">" +
											  "<font style=\"color:red;\">*</font><span id=\"transferName\">移交说明：<span id=\"noDes\" style=\"color:red;margin-left:15px;display:none;\">请填写移交说明！</span></span>" +
											  "<textarea onblur=\"setDes(this)\" style=\"width:100%;height:180px;padding:10px;\"></textarea>" +
										  "</div>" +
									  "</div>";

                        layui.use('layer', function(){
                            var layer = layui.layer;
                            window.top.layer.open({
                                title: '团队移交',
                                type:0,
                                area:'450px',
                                content:netStep,
                                btn:['移交','关闭'],
                                yes:function(index,layero){
                                    //检查人员选择是否为空
                                    if(transferObj == -1){
                                        //显示警告信息
                                        $("#noTransfer").show();
                                        return;
									}
									//隐藏警告信息
                                    $("#noTransfer").hide();
                                    //检查移交说明是否为空
									if(transferDes == null || transferDes == ''){
									    //显示警告信息
									    $("#noDes").show();
                                        return;
									}
									//隐藏警告信息
                                    $("#noDes").hide();
                                    $.ajax({
                                        type : "post",
                                        url : "/organic/transferAuthority",
                                        data : {sid:sid,transferUserId:transferObj,transferDes:transferDes},
                                        success : function(data){
                                            var d = jQuery.parseJSON(data);
                                            if(d.status == "y"){
                                                //权限在session中，所以需要重新登录.
                                                window.top.layer.msg("移交成功！", {icon:1});
                                                //定时执行，3秒后执行
                                                window.setTimeout(function(){
                                                    window.location.href = "/index?sid=${param.sid}";
                                                }, 1000 * 3);
                                            }else{
                                                window.top.layer.msg("移交失败，" + d.info, {icon:2});
                                            }
                                        },
                                        error : function(data){
                                            var d = jQuery.parseJSON(data);
                                            window.top.layer.msg("移交失败，" + d.info, {icon:2});
                                        }
                                    });
                                }
                            })
                        });
                    }
                }else{
                    $("#msg").html("请输入验证码");
                    return false;
                }
            }
        })
    });

}


var timeId;
var time =30;
function clock(){
	time--;
	if(time==0){
		 $("#sendMail").attr("onclick","sendPassYzm()");
		 $("#sendMail").attr("class","sendBtn");
		 $("#sendMail").html('获取验证码');
		 clearInterval(timeId);
	}else{
		$("#sendMail").attr("class","resendBtn");
		$("#sendMail").html(time+"秒后再发送");
	}
}
//var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
	//发送验证码
	function sendPassYzm(){
		//接收的邮箱
		/* var email = '${userInfo.email}';
		if(!regE.test(email)){//匹配就添加到成功中
		}else{
			
		} */
		$.ajax({
			  type : "post",
			  url : "/organic/sendPassYzm/${requestURI}?sid=${param.sid}&rnd="+Math.random(),
			  dataType:"json",
			  data:{noticeType : $("[name='noticeType']:checked").val()},
			  beforeSend: function(XMLHttpRequest){
				  	time = 29;
				  	$("#sendMail").attr("class","resendBtn");
					$("#sendMail").html('发送中..');
					$("#sendMail").removeAttr("onclick")
	           },
			  success : function(data){
				  if('y'==data.status){
					  timeId = setInterval("clock()",1000);
				  }else{
					  $("#sendMail").attr("onclick","sendPassYzm()");
					  $("#sendMail").attr("class","sendBtn");
					  $("#sendMail").html('获取验证码');
				  }
			  },
			  error:  function(XMLHttpRequest, textStatus, errorThrown){
				 $("#sendMail").attr("onclick","sendPassYzm()");
				 $("#sendMail").attr("class","sendBtn");
			     $("#sendMail").html('获取验证码');
		      }
		});
	}
//验证码确认
function checkYzm(){
	var yzm  = $("#yzmLayer").val();
	if(yzm){
		$.ajax({
			  type : "post",
			  url :'/organic/checkYzm/${requestURI}?sid=${param.sid}',
			  dataType:"json",
			  async:false,
			  data:{param:yzm,noticeType:$("[name='noticeType']:checked").val()},
			  success : function(data){
				  if(data.status=='f'){
					  $("#msg").html("*"+data.info);
				  }else {
					  $("#msg").html('');
				  }
			  },
			  error:  function(XMLHttpRequest, textStatus, errorThrown){
		      }
		});
	}else{
	    $("#msg").html('');
	}
}
//定义验证码接收函数
function byWhich(way){
	if ($(way).val() == "phone") {
		$("#phone").css("display", "block");
		$("#email").css("display", "none");
	} else if ($(way).val() == "email") {
		$("#phone").css("display", "none");
		$("#email").css("display", "block");
	}
	$("#orgForm input[name='noticeType']").val($("[name='noticeType']:checked").val());
}
</script>
<!-- <script type="text/javascript">
	$(function() {
		$("[name='noticeType']").click(function() {
			if ($("[name='noticeType']:checked").val() == "phone") {
				$("#phone").css("display", "block");
				$("#email").css("display", "none");
			} else if ($("[name='noticeType']:checked").val() == "email") {
				$("#phone").css("display", "none");
				$("#email").css("display", "block");
			}
			//改变验证码的验证参数
			//$("#yzm").attr("ajaxurl","/userInfo/checkYzm/${requestURI}?sid=${param.sid}&noticeType="+$("[name='noticeType']:checked").val());
		});
	})
</script> -->
</body>
</html>
