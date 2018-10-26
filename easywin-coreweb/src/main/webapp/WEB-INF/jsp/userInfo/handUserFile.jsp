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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script> 
$(function() {
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError:true
	});
});

function setStyle(){
	$('.failLi').mouseover(function(){
		var display = $(this).find(".info").css("display");
		if(display=='block'){
			$(this).find(".failUp").show();
			$(this).find(".failsave").hide();
		}else{
			$(this).find(".failUp").hide();
			$(this).find(".failsave").show();
		}
	});
	$('.failLi').mouseout(function(){
		$(this).find(".failUp").hide();
		$(this).find(".failsave").hide();
	});
	$('.sucLi').mouseover(function(){
		$(this).find(".sucDel").show();
	});
	$('.sucLi').mouseout(function(){
		$(this).find(".sucDel").hide();
	});
}
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
//批量邀请
	function invUser(){

		var users = $(":checkbox[name='sucs']");
		var emails = new Array();
		for(var i=0;i<users.length;i++){
			emails.push($(users[i]).attr("value"));
		}
		if(emails.length>0){
			$.ajax({
				  type : "POST",
				  url : "/userInfo/preCheckInvUser?sid=${param.sid}",
				  dataType:"json",
				  traditional :true, 
				  data:{"emails":emails},
				  success : function(data){
					  if(data.status=='y'){
						  var content = "<font size='3px'>以下账号已存在于系统中</font><br>";
						  for(var i=0;i<data.list.length;i++){
							  content = content+data.list[i]+"<br>"
						  }
						  content = content+"共："+data.list.length+"个账号，将不会发送邮件";
						  $("#invEmails").attr("value",emails);
						  if(emails.length>0){
								  $("#invForm input[name='redirectPage']").val(window.location.href);
								  $('#invForm').submit();
						  }else{
							 window.top.layer.open({
								 content: "<font size='4px'>没有账号</font><br>",
								icon:5
							});
							return false;
						  }
					  }else{
						  window.top.layer.open({
								 content: "<font size='4px'>没有账号</font><br>",
								icon:5
							});
						  return false;
					  }
					  
				  }
				 });
		}else{
			 window.top.layer.open({
				 content: "<font size='4px'>没有账号</font><br>",
				icon:5
			});
			 return false;
		}
	}

	//移除左边的未导入成功的
	function removeFails(ts){
		var failNum = $("#failNum").html();
		var users = $(":checkbox[name='fails']");
		for(var i=0;i<users.length;i++){
			if($(users[i]).attr('checked')){
				failNum = failNum-1;
				$(users[i]).parent().parent().parent().remove();
			}
		}
		$("#failNum").html(failNum);
		$(ts).parent().find(":checkbox[name='failAll']").attr("checked",false)
		
	}
	//移除右边的导入成功的
	function removeSucs(ts){
		var sucNum = $("#sucNum").html();
		var users = $(":checkbox[name='sucs']");
		for(var i=0;i<users.length;i++){
			if($(users[i]).attr('checked')){
				sucNum = sucNum-1;
				$(users[i]).parent().parent().parent().remove();
			}
		}
		$("#sucNum").html(sucNum);
		$(ts).parent().find(":checkbox[name='sucAll']").attr("checked",false)
	}

	//删除单个导入成功的
	function removeSuc(ts){
		var sucNum = $("#sucNum").html();
		$(ts).parent().parent().remove();
		$("#sucNum").html(sucNum-1);
	}
	//删除单个导入失败的
	function removeFail(ts){
		var failNumNum = $("#failNum").html();
		$(ts).parent().parent().remove();
		$("#failNum").html(failNumNum-1);
	}

	//修改单个导入失败的
	function modifyFail(ts){
		var parent = $(ts).parent().parent();
		//隐藏修改按钮
		$(parent).find(".failUp").hide();
		//显示保存按钮
		$(parent).find(".failsave").show();
		//显示修改的文本
		$(parent).find(".upInfo").show();
		//隐藏显示信息
		$(parent).find(".info").hide();
	}
	//取消修改单个导入失败的
	function cancleFail(ts){
		var parent = $(ts).parent().parent();
		//修改前的值
		var preVal = $(parent).find(".info").find(":checkbox[name='fails']").val();
		$(parent).find(".upInfo").find("input[name='failsInput']").val(preVal);
		//隐藏修改按钮
		$(parent).find(".failUp").show();
		//显示保存按钮
		$(parent).find(".failsave").show();
		//显示修改的文本
		$(parent).find(".upInfo").hide();
		//隐藏显示信息
		$(parent).find(".info").show();
	}

	//修改没有导入成功的，并保存
	function saveFail(ts){
		var parent = $(ts).parent().parent();
		//取input的值
		var email = $(parent).find(".upInfo").find("input[name='failsInput']").val();
		var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
		if(regE.test($.trim(email))){//匹配就添加到成功中
			var info='';
			info+='\n <div class="form-group form-control2 sucLi">';
			info+='\n 	<div class="checkbox pull-left">';
			info+='\n 		<label>';
			info+='\n 			<input type="checkbox" name="sucs" value="'+email+'"/>'+email;
			info+='\n 		</label>';
			info+='\n 	</div>';
			info+='\n 	<div class="pull-right ws-twoBtn sucDel" style="display: none">';
			info+='\n 		<button class="btn btn-default btn-xs" type="button" onclick="removeSuc(this)">删除</button>';
			info+='\n 	</div>';
			info+='\n 	<div class="ws-clear"></div>';
			info+='\n </div>';
			
			$(".sucul").append(info);
			$(parent).remove();
			
			var sucNum = parseInt($("#sucNum").html());
			$("#sucNum").html(sucNum+1);
			var failNum = $("#failNum").html();
			$("#failNum").html(failNum-1);
			setStyle();
		}else{
			//修改前的值
			$(parent).find(".info").find(":checkbox[name='fails']").val(email);
			$(parent).find(".info").find(".spanInfo").html(email);
			$(parent).find(".upInfo").find(":checkbox[name='fails']").val(email);
			//隐藏修改按钮
			$(parent).find(".failUp").show();
			//显示保存按钮
			$(parent).find(".failsave").show();
			//显示修改的文本
			$(parent).find(".upInfo").hide();
			//隐藏显示信息
			$(parent).find(".info").show();
		}
	}
	
</script>
<style>
.scrollright{overflow-y:yes;overflow: scroll;overflow-x:hidden;height: 250px;
}
</style>
</head>
<body onload="setStyle()" style="background-color: #fff">
	<div class="panel">
			<header class="panel-heading" style="padding-top:5px;padding-bottom:10px">
				<div align="center" style="font-size: 15px;padding-top: 10px">
				本次操作一共导入<span id="total">${fn:length(failEmails)+fn:length(sucEmails)}</span>条数据，
				其中正确导入<span id="sucNum">${fn:length(sucEmails)}</span>条，
				未正确导入<span id="failNum">${fn:length(failEmails) }</span>条
				</div>
			</header>
			<div class="panel-body">
				<div class="pull-left ws-twoBox" style="width: 50%">
					<div class="panel">
						<div class="panel-heading" style="padding-top:5px;padding-bottom:10px">
								未正确导入
							  <div class="ws-floatRight">
								<a class="fa fa-trash-o"  style="height: 20px;line-height: 20px" href="javascript:void(0)" onclick="removeFails(this)" title="删除"></a>
							 </div>
						</div>
						<div class="panel-body scrollright" >
							<c:choose>
								<c:when test="${not empty failEmails}">
									<c:forEach items="${failEmails}" var="email">
										 <div class="form-group form-control2 failLi">
											<div class="checkbox pull-left info" style="display: block">
												<label>
													<input type="checkbox" name="fails" value="${email}"/>
													<span class="spanInfo">${email}</span> 
												</label>
											</div>
											
											<div class="input-group input-group-sm m-bot15 pull-left upInfo" style="width:70%; margin-right: 0;display: none">
												<span class="input-group-addon" style="padding:5px 5px;">
													<input type="checkbox" name="fails" value="${email}"/>
												</span>
												<input class="form-control ws-writeInput" type="text" name="failsInput" value="${email}"/>
											</div>
								
											<div class="pull-right ws-twoBtn failUp" style="display: none">
												<button class="btn btn-primary btn-xs" type="button" onclick="modifyFail(this)">修改</button>
												<button class="btn btn-default btn-xs" type="button" onclick="removeFail(this)">删除</button>
											</div>
											<div class="pull-right ws-twoBtn failsave" style="display: none">
												<button class="btn btn-primary btn-xs" type="button" onclick="saveFail(this)">保存</button>
												<button class="btn btn-default btn-xs" type="button" onclick="cancleFail(this)">取消</button>
											</div>
											<div class="ws-clear"></div>
										</div>
									</c:forEach>
								</c:when>
							</c:choose>						
						</div>
						
					</div>
					
				</div>
				<form method="post" id="invForm" action="/userInfo/fileInvUser/${requestURI}?sid=${param.sid}" class="subform">
				<input type="hidden" name="sid" value="${param.sid }"/>
				<input type="hidden" name="invEmails" id="invEmails" value=""/>
				<input type="hidden" name="redirectPage" value=""/>
				<input type="hidden" name="filePath" value="${param.filePath }"/>
				<div class="pull-left ws-twoBox" style="width: 40%">
					<div class="panel">
						<div class="panel-heading" style="padding-top:5px;padding-bottom:10px">
							正确导入
							  <div class="ws-floatRight" >
								<a class="fa fa-trash-o" style="height: 20px;line-height: 20px" href="javascript:void(0)" onclick="removeSucs(this)"></a>
							 </div>
						</div>
						<div class="panel-body scrollright sucul">	
						<c:choose>
							<c:when test="${not empty sucEmails}">
								<c:forEach items="${sucEmails}" var="email">
									 <div class="form-group form-control2 sucLi">
										<div class="checkbox pull-left">
											<label>
												<input type="checkbox" name="sucs" value="${email}"/>${email}
											</label>
										</div>
										<div class="pull-right ws-twoBtn sucDel" style="display: none">
											<button class="btn btn-default btn-xs" type="button" onclick="removeSuc(this)">删除</button>
										</div>
										<div class="ws-clear"></div>
									</div>
								</c:forEach>
							</c:when>
						</c:choose>					
						</div>						
					</div>
					
				</div>
				<div class="ws-clear"></div>
				<div class="panel-body">
					<label class="pull-left ws-noMargin" style="margin-top:5px">验证码</label>
					<div class="pull-left ws-noMargin">
						<div class="pull-left ws-noMargin">
							<input id="yzm" name="yzm" class="form-control input-sm pull-left" style="width: 150px" placeholder="不区分大小写" datatype="*" ajaxurl="/registe/checkYzm/${requestURI}?sid=${param.sid}" type="text" class="yz_input" value=""/>
							<div style="clear: both"></div>
						</div>
				    	<div class="pull-left ws-noMargin">
				    		<img src="/registe/AuthImage/${requestURI}?sid=${param.sid}" id="yz" width="100" height="30" onclick="this.src='/registe/AuthImage/${requestURI}?rnd=' + Math.random();$('#yzm').val('')"/>
				    		<a href="javascript:void(0)" onclick="$('#yz').click()">换一张</a>
				    	</div>				    
				    </div>
			</div>
			</form>
		</div>
</body>
</html>
