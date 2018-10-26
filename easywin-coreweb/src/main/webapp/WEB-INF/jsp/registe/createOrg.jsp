<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
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
	var regE = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
	var checkp = /^[\w\W]{6,16}$/
	//是否存在该团队
	function checkPassword(){
		$("#msg").hide();
		var email = $("#email").val();
		var password = $("#password").val();
		if(""!=password&&checkp.test(password)&&""!=email&&regE.test(email)){
			$.ajax({
				  type : "POST",
				  url : "/registe/checkPassword",
				  dataType:"json",
				  data:{param:password,email:email},
				  success : function(data){
					  if("f"==data.status){
						  $("#msg").html("<br><font color='red'>"+data.info+"</font>");
						  $("#msg").show();
					  }else if("y"==data.status){
						  $("#msg").html("<br><font color='green'>可创建团队</font>");
						  $("#msg").show();
					  }
				  }
			});
		}
	}
</script>
</head>
<body>
<div class="block01" style="padding-top: 20px">
	<form class="subform" method="post" action="/registe/registeOrg" style="margin-top:15px;">
	<tags:token></tags:token>
				<table width="90%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
					<tr><td colspan="4" align="left"><h2>创建团队信息填写</h2></td></tr>
					<tr>
						<td class="td1">注册邮箱：<font style="color: red">*</font></td>
						<td class="td2" colspan="3">
								<input type="text" id="email" style="width: 50%" ajaxurl="/registe/checkEmail" datatype="e" name="email" value="${userInfo.email}"/>
						</td>
					</tr>
					<tr>
						<td class="td1">团队名称：<font style="color: red">*</font></td>
						<td class="td2" colspan="3">
								<input type="text" style="width: 50%" id="orgName" datatype="s" name="orgName"/>
						</td>
					</tr>
					<tr>
						<td class="td1">用户姓名：<font style="color: red">*</font></td>
						<td class="td2" colspan="3">
								<input type="text" style="width: 50%" datatype="*" name="userName" value="${userInfo.userName }"/>
						</td>
					</tr>
					<tr>
						<td class="td1">设置密码：<font style="color: red">*</font></td>
						<td class="td2" colspan="3">
						<input datatype="s6-18"  style="width: 50%" id="password" type="password" name="password" value="" onblur="checkPassword()"/>&nbsp;	<font color="gray">密码长度6至18个字符！</font>
						<span id="msg"></span>
						</td>
					</tr>
					<tr>
						<td class="td1">再次输入：<font style="color: red">*</font></td>
						<td class="td2" colspan="3">
							<input datatype="*" style="width: 50%" type="password" recheck="password" name="password2" value="" />
						</td>
					</tr>
				</table><br/>
				<center>
					<input type="submit" value="创建" class="green_btn"/>&nbsp;
				</center>
			</form>	
</div>
</body>
</html>
