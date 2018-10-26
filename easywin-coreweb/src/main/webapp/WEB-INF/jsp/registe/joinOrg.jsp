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
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<link rel="stylesheet" type="text/css" href="/static/css/new_file.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
	})
	
	//提交前邮箱验证
	var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
	var checkn = /^\d+$/
	//是否存在该团队
	function checkOrgNum(ts){
		$("#orgName").hide();
		var email = $("#email").val();
		var comId = $("#comId").val();
		if(""!=comId&&checkn.test(comId)&&""!=email&&regE.test($.trim(email))){
			$.ajax({
				  type : "POST",
				  url : "/registe/checkOrgNum",
				  dataType:"json",
				  data:{comId:comId,email:email},
				  success : function(data){
					  if("f"==data.status){
						  $("#orgName").html("<font color='red'>"+data.info+"</font>");
						  $("#orgName").show();
						  $("#canSubmit").val(0);
					  }else if("y"==data.status){
						  $("#orgName").html("<font color='green'>可加入该团队</font>");
						  $("#orgName").show();
						  $("#canSubmit").val(1);
					  }
				  }
			});
		}else{
			 $("#canSubmit").val(0);
		}
	}

	function joinOrg(){
		if( $("#canSubmit").val()==1){
			 $("#joinForm input[name='redirectPage']").val(window.location.href);
			 $("#joinForm").submit();
		}
	}
</script>
<style>
	dt{
		font-size: 12px
	}
	.ws-common-dl dd{
		padding-bottom: 0px
	}
	dd span{
		font-size: 12px
	}
	dd input{
		font-size: 12px;border-radius: 5px;
	}
	dd input:focus{
		border-color: #1ABC9C;box-shadow:0 0 8px rgba(38,194,155,0.6);
	}
	.pop-content dl{
		line-height: 38px
	}
</style>
</head>
<body>
	<div class="pop-up-box" style="margin-top:5px;">
		<h2 style="text-align: center">申请加入团队</h2>
		<div class="pop-content">
			<form id="joinForm" class="subform" method="post" action="/registe/joinOrg/${requestURI }">
			<input type="hidden" name="redirectPage"/>
			<%--默认可以提交 --%>
		<input type="hidden" id="canSubmit" value="1"/>
				<dl class="ws-common-dl">
					<dt><span>*</span>邮箱（账号）：</dt>
					<dd>
						<input type="text" style="width:90%" id="email" datatype="e" 
						name="email" value="${joinRecord.email}" onblur="checkOrgNum(this)" nullmsg="请填写邮箱号" errormsg="邮箱格式不正确"/>
					</dd>
				</dl>
				<dl class="ws-common-dl">
					<dt><span>*</span>团队号：</dt>
					<dd>
						<input  type="text" datatype="n" id="comId" name="comId" onblur="checkOrgNum(this)" value="${joinRecord.comId }" style="width: 70%"/><font color="gray">请输入团队编号</font><br/>
						<span id="orgName" style="display: none"></span>
					</dd>
				</dl>
				<dl class="ws-common-dl">
					<dt>附加消息：</dt>
					<dd>
						<input  type="text" name="joinNote"/>
					</dd>
				</dl>
				
				<dl class="ws-common-dl">
					<dt><span>*</span>验证码：
					</dt>
					<dd>
						<div>
							<input type="text" id="yzm" name="yzm" datatype="*" datatype="*" ajaxurl="/registe/checkYzm/${requestURI}" style="width: 50%"/>
							<img style="height: 30px;vertical-align: middle" src="/registe/AuthImage/${requestURI}" id="yz" width="100" 
							onclick="this.src='/registe/AuthImage/${requestURI}?rnd=' + Math.random();$('#yzm').val('')"/>
							<span onclick="$('#yz').click()" style="cursor: pointer;height: 30px;font-size: 15px;color: blue;vertical-align: bottom;">换一张</span>
						</div>
						<div>
						   	 <font color="gray">输入图中字符（不区分大小写）</font>
						</div>
					</dd>
				</dl>
				<div class="ws-btn ws-btn0">
					<button type="button" class="ws-green" onclick="joinOrg();return false;">申请</button>
					<button type="button" class="ws-green" style="background-color: #669999" onclick="art.dialog.close();return false;">取消</button>
				</div>
			</form>
		</div>
	</div>
</body>

</html>
