<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
	function registe(){
		var email = $("#email").val();
		var reg = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/ 
		if(''!=email && reg.test(email)){
		$.ajax({
			  type : "post",
			  url : "/registe/checkEmail?rnd="+Math.random(),
			  dataType:"json",
			  data:{param:email},
			  success : function(data){
				  //该邮箱没有注册一个公司（可登陆，可注册，可加入）
				  if('y'==data.status){
					  art.dialog.open("/registe/chooseOption?email="+email+"&admin=0&rnd=" + Math.random(), {
						    title:"企业注册",
							lock : true,
							max : false,
							min : false,
							width : 600,
							height : 400,
							close : function() {
							}
						});
				  } else if('f'==data.status) {//该邮箱已注册一个公司（可登陆，可加入）
					  art.dialog.open("/registe/chooseOption?email="+email+"&admin=1&rnd=" + Math.random(), {
						    title:"企业注册",
							lock : true,
							max : false,
							min : false,
							width : 600,
							height : 400,
							close : function() {
							}
						});
				 }
			  }
			 });
		}
	}
</script>
</head>
<body>
<div class="block01">
<form class="subform" style="margin-top:15px;">
	<tags:token></tags:token>
	<table width="75%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
		<tr><td colspan="2" align="left"><h2>新用户注册</h2></td></tr>
		<tr>
			<td class="td1">邮箱地址：<font style="color: red">*</font></td>
			<td class="td2" colspan="3">
			<input type="text" id="email" datatype="e" name="email" value="" style="width: 80%"/>
			</td>
		</tr>
		<tr>
			<td class="td1">验证码：<font style="color: red">*</font></td>
			<td class="td2" colspan="3">
			<input class="select" datatype="*" name="SecurityCode" value="" type="text" style="width: 80%"/>
			</td>
		</tr>
	</table><br/>
	<center>
		<input type="button" value="注册" class="green_btn" onclick="registe()"/>&nbsp;
		<input type="button" value="返回" class="blue_btn"  onclick="window.self.location='/login.jsp';" />&nbsp;&nbsp;
	</center>
</form>
</div>
</body>
</html>
