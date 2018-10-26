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
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	//添加分组
	function add(){
		window.location.href='/userInfo/addUserGroupPage?sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
	}
	//修改分组
	function update(id){
		window.location.href='/userInfo/updateUserGroupPage?id='+id+'&sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
	}

	//批量删除分组
	function del(){
		if(checkCkBoxStatus('ids')){
			art.dialog.confirm('确定要删除该组群吗？', function(){
				$("#delForm input[name='redirectPage']").val(window.location.href);
				$('#delForm').submit();
			}, function(){
				art.dialog.tips('已取消');
			});	
		}else{
			art.dialog.alert('请先勾选需要删除的分组。');
		}
	}
</script>
</head>
<body style="background-color: #fff" onload="resizeVoteH('userCenter');">
<div id="home" class="tab-pane active">
	<div id="tabs-1" class="panel-collapse collapse in">
		<!--nav-tabs style 1-->
		<div class="tc-tabs">
			<!-- Nav tabs style 2-->
			<div class="ws-headBox" style="background-color:#EEEEEE;">
				<div class="pull-left ws-toolbar ws-floatRight">
					<ul class="ws-icon">
						<li>
							<a href="javascript:void(0);" class="fa fa-plus-square-o fa-lg" onclick="add()" title="添加组群"></a>
						</li>
						<li>
							<a href="javascript:void(0);" class="fa fa-trash-o fa-lg" onclick="del()" title="删除组群"></a>
						</li>
					</ul>
				</div>
				<div class="ws-clear"></div>
			</div>
			<!-- Tab panes -->
			<div class="tab-content">
				<form action="/userInfo/delUserGroup" method="post" id="delForm">
				<tags:token></tags:token>
				<input type="hidden" name="redirectPage"/>
				<div class="tab-pane active" id="tab8">
					<div class="panel-body">
						<table class="table table-hover general-table">
							<thead>
								<tr>
									<th width="10%" valign="middle"><input type="checkbox" onclick="checkAll(this,'ids')" /></th>
									<th width="10%" valign="middle">序号</th>
									<th width="20%" valign="middle">团队编号</th>
									<th valign="middle">组名</th>
									<th width="20%" valign="middle">创建时间</th>
									<th width="15%" valign="middle">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
								 	<c:when test="${not empty list}">
								 		<c:forEach items="${list}" var="group" varStatus="st">
								 			<tr>
								 				<td valign="middle"><input type="checkbox" name="ids" value="${group.id}"/></td>
								 				<td valign="middle">${st.count}</td>
								 				<td valign="middle">${group.comId}</td>
								 				<td valign="middle">${group.grpName }</td>
								 				<td valign="middle">${fn:substring(group.recordCreateTime,0,16) }</td>
								 				<td valign="middle"><span><a href="javascript:void(0)" onclick="update(${group.id})">修改</a></span></td>
								 			</tr>
								 		</c:forEach>
								 	</c:when>
								 	<c:otherwise>
								 		<tr>
								 			<td height="35" colspan="7" align="center"><h2>没有建立组群！</h2></td>
								 		</tr>
								 	</c:otherwise>
								 </c:choose>
							</tbody>
						</table>
						<tags:pageBar url="/userInfo/listUserGroup"></tags:pageBar>
					</div>
				</div>
				</form>
			</div>
		</div>
		<!--nav-tabs style 2-->
	</div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
