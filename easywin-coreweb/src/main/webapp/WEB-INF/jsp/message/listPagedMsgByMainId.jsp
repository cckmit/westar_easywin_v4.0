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
<title><%=SystemStrConstant.TITLE_NAME%></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError:true
		});
	})
</script>
</head>
<body>
	<div class="tit">
		<span>当前位置：系统消息</span>
	</div>
	<div class="block01">
		<form action="/message/delmsgInfo" method="post" id="delForm">
			<tags:token></tags:token>
			<input type="hidden" name="redirectPage" />
			<table width="100%" border="0" class="grid_tab" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
				<thead>
					<tr>
						<th style="width: 50px;">序号</th>
						<th style="width: 50%;">内容摘要</th>
						<th style="width: 120px;">发送时间</th>
						<th>接收人</th>
						<th>是否已读</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty listMsg}">
							<c:forEach items="${listMsg}" var="msg" varStatus="st">
								<tr>
									<td height="25">${st.count}</td>
									<td style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" align="left"><a href="javascript:void(0)" onclick="showMsgInfo(${msg.id},'${param.sid }')">${msg.title }</a></td>
									<td>${msg.sendtime }</td>
									<td>${msg.receiverName }</td>
									<td><font ${msg.isread==0?'color="red"':'' } ${msg.isread!=0?'color="black"':'' }>${msg.isreadName }</font></td>
									<td><span><a href="javascript:void(0)" onclick="showMsgInfo(${msg.id},'${param.sid }')">查看</a></span></td>
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
		<div class="page">
			<tags:pageBar url="/message/listPagedMsgByMainId"></tags:pageBar>
		</div>
	</div>
</body>
</html>
