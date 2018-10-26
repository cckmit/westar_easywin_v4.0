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
	//邮箱验证
	var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
	//创建新的团队
	function createOrg(){
		var email = $("#email").val();
		if(''!=email && regE.test(email)){
		$.ajax({
			  type : "post",
			  url : "/registe/checkEmail?rnd="+Math.random(),
			  dataType:"json",
			  data:{param:email},
			  success : function(data){
				  if('y'==data.status){
					window.location.href="/registe/createOrgPage?email="+email+"&redirectPage="+encodeURIComponent(window.location.href);
				  } else if('f'==data.status) {//该邮箱已注册一个公司（可登陆，可加入）
					  artDialog(
						  {	
					            content:'该邮箱已注册团队，选择加入其它团队！',
					            lock:false,
					            style:'succeed noClose'
					        },
					        function(){
								window.location.href="/registe/joinOrgPage?email="+email+"&redirectPage="+encodeURIComponent(window.location.href);
					        },
					        function(){
					        }
						)
				  }
			  }
			 });
		}else{
			window.location.href="/registe/createOrgPage?email=&redirectPage="+encodeURIComponent(window.location.href);
		}
	}
	//加入团队
	function joinOrg(){
		window.location.href="/registe/joinOrgPage?email=${userInfo.email}&redirectPage="+encodeURIComponent(window.location.href);
	}

	function getOrg(ts){
		var email = $(ts).val();
		var selComId = $("#comId").html("");
		selComId.append("<option value=''>请选择团队 </option>");
		if(''!=email){
			$.ajax({
			  type : "POST",
			  url : "/login/listOrg?rnd=" + Math.random(),
			  dataType:"json",
			  data:{param:email},
			  success : function(data){
				  if(data.list.length<1){
					  $("#comId").html("")
					selComId.append("<option value=''>您注册信息还未激活 </option>");
				  }else  if(data.list.length==1){
					  $("#comId").html("")
				  }
					for(var i=0;i<data.list.length;i++){
						var orgNum = data.list[i].orgNum;
						var orgName = data.list[i].orgName;
						if(null==orgName||""==orgName){
							orgName=orgNum;
						}
						selComId.append("<option value='"+orgNum+"'>"+orgName+" </option>")
					}
			  }
			});
		}

	}
</script>
</head>
<body>
<div class="block01">
	<div style="padding-top: 100px">
		<c:choose>
			<c:when test="${not empty listOrg}">
			<%-- 若此时已加入团队，选择就登陆 --%>
			<form class="subform" method="post" action="/registe/login" style="margin-top:15px;">
			<input type="hidden" name="admin" value="${userInfo.admin}"/>
				<table width="90%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
					<tr><td colspan="2" align="left"><h2>登陆</h2></td></tr>
					<tr>
						<td class="td1">账号(邮箱)：</td>
						<td class="td2" colspan="3">
							<input type="text" style="width: 50%" id="email"  datatype="e" name="email" value="${userInfo.email}" onblur="getOrg(this)"/>
						</td>
					</tr>
					<tr>
						<td class="td1">已有团队：<font style="color: red">*</font></td>
						<td class="td2" colspan="3">
								<select datatype="*" name="comId" id="comId" style="width: 50%;text-align: center">
									<c:forEach  items="${listOrg}" var="userOrg" varStatus="vs">
									 <option value="${userOrg.orgNum}">${empty userOrg.orgName?userOrg.orgNum:userOrg.orgName}</option>
									</c:forEach>
								 </select>
	
						</td>
					</tr>
					<tr>
						<td class="td1">密码：<font style="color: red">*</font></td>
						<td class="td2" colspan="3">
						<input class="select" datatype="*" name="password" value="" type="password" style="width: 80%"/>
						</td>
					</tr>
				</table><br/>
				<center>
					<input type="submit" value="登陆" class="green_btn" onclick=""/>&nbsp;
				</center>
			</form>
			<table width="90%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
					<tr>
						<td align="left"><span style="width: 50px;height: 25px;background-color: gray" id="createOrg" onclick="createOrg()">创建新团队</span></td>
						<td align="left"><span style="width: 50px;height: 25px;background-color: gray" onclick="joinOrg()">加入团队</span></td>
					</tr>
			</table>
			</c:when>
			<c:otherwise>
				<table width="90%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
				<input type="hidden" id="email" name="email" value="${userInfo.email}"/>
					<tr><td colspan="2" align="left"><h2>请选择注册方式</h2></td></tr>
					<tr>
						<td align="left" style="width: 50%">邮箱：</td>
						<td align="left" style="width: 50%">${userInfo.email}</td>
					</tr>
					<tr>
						<td align="left" style="width: 50%"><span style="width: 50px;height: 25px;background-color: gray" onclick="createOrg()">创建新团队</span></td>
						<td align="left" style="width: 50%"><span style="width: 50px;height: 25px;background-color: gray" onclick="joinOrg()">加入团队</span></td>
					</tr>
				</table><br/>
			</c:otherwise>
		</c:choose>
	</div>
	
</div>
</body>
</html>
