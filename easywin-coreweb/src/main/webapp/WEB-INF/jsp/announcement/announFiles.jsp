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
</head>
<body onload="resizeVoteH('otherannounAttrIframe');initCard('${param.sid }')" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<tr>
		<td width="10%" height="40">
			<h5>序号</h5></td>
		<td width="30%" height="40">
			<h5>名称</h5></td>
		<td width="20%" height="40">
			<h5>上传时间</h5></td>
		<td width="20%" height="40">
			<h5>上传人</h5></td>
		<td width="10%" height="40" align="center">
		<h5>操作</h5></td>
	</tr>
	<c:choose>
	 	<c:when test="${not empty announTalkFiles}">
	 		<c:forEach items="${announTalkFiles}" var="announFile" varStatus="status">
	 			<tr>
	 				<td height="40">${ status.count}</td>
	 				<td align="left">${ announFile.filename}</td>
	 				<td>${announFile.recordCreateTime}</td>
	 				<td style="text-align: left;">
		 				<div class="ticket-user pull-left other-user-box" data-container="body" data-toggle="popover" data-placement="left" 
		 				 data-user='${announFile.userId}' data-busId='${announFile.busId}' data-busType='039'>
							<img src="/downLoad/userImg/${announFile.comId}/${announFile.creator}?sid=${param.sid}"
								title="${announFile.creatorName}" class="user-avatar"/>
							<i class="user-name">${announFile.creatorName}</i>
							</div>
	 				</td>
		 			<td height="30" align="center">
			 				<c:choose>
			 					<c:when test="${announFile.fileExt=='doc' || announFile.fileExt=='docx' || announFile.fileExt=='xls' || announFile.fileExt=='xlsx' || announFile.fileExt=='ppt' || announFile.fileExt=='pptx' }">
					 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad('${announFile.uuid}','${announFile.filename}','${param.sid }')" title="下载"></a>
					 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${announFile.upfileId}','${announFile.uuid}','${announFile.filename}','${announFile.fileExt}','${param.sid}','039','${announId}')" title="预览">
					 				</a>
			 					</c:when>
			 					<c:when test="${announFile.fileExt=='txt' || announFile.fileExt=='pdf'}">
			 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${announFile.uuid}/${announFile.filename}?sid=${param.sid}" title="下载"></a>
					 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${announFile.upfileId}','${announFile.uuid}','${announFile.filename}','${announFile.fileExt}','${param.sid}','039','${announId}')" title="预览">
					 				</a>
			 					</c:when>
			 					<c:when test="${announFile.fileExt=='jpg'||announFile.fileExt=='bmp'||announFile.fileExt=='gif'||announFile.fileExt=='jpeg'||announFile.fileExt=='png'}">
			 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${announFile.uuid}/${announFile.filename}?sid=${param.sid}" title="下载"></a>
			 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="showPic('/downLoad/down/${announFile.uuid}/${announFile.filename}','${param.sid}','${announFile.upfileId}','039','${announId}')" title="预览"></a>
			 					</c:when>
			 					<c:otherwise>
					 				&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad('${announFile.uuid}','${announFile.filename}','${param.sid }')" title="下载"></a>
			 					</c:otherwise>
			 				</c:choose>
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
</table>
<tags:pageBar url="/announcement/announFilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
