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
	function add(){
		if("${userInfo.anyOrgId}"!=""){
			window.location.href='/userInfo/addUserInfoPage?orgRootId=${userInfo.orgRootId}&orgId=${userInfo.anyOrgId}&orgPathName=${userInfo.anyNameLike}&sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
		}else{
			window.location.href='/userInfo/addUserInfoPage?orgRootId=${userInfo.orgRootId}&sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
		}
	}

	function del(){
		if(checkCkBoxStatus('ids')){
			art.dialog.confirm('确定要删除该用户吗？', function(){
				$("#delForm input[name='redirectPage']").val(window.location.href);
				$('#delForm').submit();
			}, function(){
				art.dialog.tips('已取消');
			});	
		}else{
			art.dialog.alert('请先选择一个用户。');
		}
	}

	function view(id){
		window.location.href='/userInfo/viewUserInfo?id='+id+'&sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
	}

	function update(id){
		window.location.href='/userInfo/updateUserInfoPage?orgRootId=${userInfo.orgRootId}&id='+id+'&sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
	}
</script>
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
</script> 
</head>
<body>
<div class="tit"><span>当前位置：系统管理>>人员维护</span></div>
<div class="block01">
<form action="/userInfo/listPagedUserInfoOrg">
 <input type="hidden" name="sid" value="${param.sid }" />
 <input type="hidden" name="orgRootId" value="${userInfo.orgRootId }" />
  <table  width="100%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
   <tr>
     <td class="td1">用户名称：</td>
     <td class="td2"><input name="userName" type="text" value="${userInfo.userName }"/></td>
     <td class="td1">所在机构：</td>
     <td class="td2">
    	<tags:orgOne queryParam="{\'rootId\':\'${userInfo.orgRootId }\'}" name="anyOrgId" showName="anyNameLike" value="${userInfo.anyOrgId }" showValue="${userInfo.anyNameLike }" readOnly="false"></tags:orgOne>
     </td>
   </tr>
   <tr>
     <td colspan="4" align="center">
     <input type="submit"  value="查询" class="green_btn"/>
     </td>
   </tr>
  </table>
 </form>
</div>
<div class="block01">
<div class="oper_btn">
 <span>
 <a href="javascript:void(0)" onclick="add()" class="add">新增</a>
 <a href="javascript:void(0)" onclick="del()" class="del">删除</a>
 </span>
 </div>
 </div>
<div class="block01">
<form action="/userInfo/delUserInfo" method="post" id="delForm">
    <tags:token></tags:token>
 <input type="hidden" name="redirectPage"/>
    <table width="100%" border="0" class="grid_tab"  cellpadding="0" cellspacing="0">
    <thead>
    <tr>
    <th><input type="checkbox" onclick="checkAll(this,'ids')" /></th>
    <th>序号</th>
    <th>登录名</th>
    <th>用户名称</th>
    <th>所在机构</th>
    <th>启用状态</th>
    <th>操作</th>
    </tr>
    </thead>
    <tbody>
 <c:choose>
 	<c:when test="${not empty list}">
 		<c:forEach items="${list}" var="userInfo" varStatus="status">
 			<tr>
 				<td height="25"><input type="checkbox" name="ids" value="${userInfo.id}"/> </td>
 				<td>${ status.count}</td>
 				<td>${ userInfo.loginName}</td>
 				<td>${ userInfo.userName}</td>
 				<td>${ userInfo.orgPathName}</td>
 				<td><tags:viewDataDic type="enabled" code="${userInfo.enabled}"></tags:viewDataDic></td>
 				<td>
 				  <span><a href="javascript:void(0)" onclick="update(${userInfo.id})">修改</a></span>
 				  &nbsp;&nbsp;
 				  <span><a href="javascript:void(0)" onclick="view(${userInfo.id})">查看</a></span>
 				</td>
 			</tr>
 		</c:forEach>
 	</c:when>
 	<c:otherwise>
 		<tr>
 			<td height="25" colspan="7" align="center"><h3>没有相关信息！</h3></td>
 		</tr>
 	</c:otherwise>
 </c:choose>
 </tbody>
</table>
</form>
<div class="page"><tags:pageBar url="/userInfo/listPagedUserInfoOrg"></tags:pageBar></div>
</div>
</body>
</html>
