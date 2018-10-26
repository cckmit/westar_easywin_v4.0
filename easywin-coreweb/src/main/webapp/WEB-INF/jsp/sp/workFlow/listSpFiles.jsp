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
<script type="text/javascript">
//删除任务附件
function delSpFile(ts,busId,upfileId,addType){
	window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/workFlow/delSpUpfile?sid=${param.sid}",{Action:"post",busId:busId,upfileId:upfileId,addType:addType},     
				function (data){
				if(data.status=='y'){
					showNotification(1,"操作成功");
					$(ts).parents("tr").remove();
					$.each($(".fileOrder"),function(index,item){
						$(this).html(index+1)
					})
				}else{
					showNotification(2,data.info);
					
				}
		},"json");
	});
	
	
}
</script>
</head>
<body onload="resizeVoteH('otherSpAttrIframe');initCard('${param.sid }')" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<tr>
		<td width="7%" height="40">
			<h5>序号</h5></td>
		<td height="40">
			<h5>名称</h5></td>
		<td width="10%" height="40">
		<h5>来源</h5></td>
		<td width="20%" height="40">
			<h5>上传时间</h5></td>
		<td width="18%" height="40">
			<h5>上传人</h5></td>
		<td width="15%" height="40" align="center"><h5>操作</h5></td>
	</tr>
	<c:choose>
	 	<c:when test="${not empty listSpFiles}">
	 		<c:forEach items="${listSpFiles}" var="spFile" varStatus="status">
	 			<tr>
	 				<td height="40">${ status.count}</td>
	 				<td align="left">${ spFile.filename}</td>
	 				<td >
						<c:choose>
							<c:when test="${spFile.addType eq 0}">
								[流程]
							</c:when>
							<c:otherwise >
								[表单]
							</c:otherwise>
						</c:choose>
					</td>
	 				<td>${spFile.recordCreateTime}</td>
	 				<td style="text-align: left;">
		 				<div class="ticket-user pull-left other-user-box">
							<img
								src="/downLoad/userImg/${userInfo.comId}/${spFile.userId}?sid=${param.sid}"
								title="${spFile.creatorName}" class="user-avatar" />
							<i class="user-name">${spFile.creatorName}</i>
							</div>
	 				</td>
		 			<td height="30" align="center">
			 				<c:choose>
			 					<c:when test="${spFile.fileExt=='doc' || spFile.fileExt=='docx' || spFile.fileExt=='xls' || spFile.fileExt=='xlsx' || spFile.fileExt=='ppt' || spFile.fileExt=='pptx' }">
					 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad('${spFile.uuid}','${spFile.filename}','${param.sid }')" title="下载"></a>
					 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${spFile.upfileId}','${spFile.uuid}','${spFile.filename}','${spFile.fileExt}','${param.sid}','022','${param.instanceId}')" title="预览">
					 				</a>
			 					</c:when>
			 					<c:when test="${spFile.fileExt=='txt' || spFile.fileExt=='pdf'}">
			 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${spFile.uuid}/${spFile.filename}?sid=${param.sid}" title="下载"></a>
					 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${spFile.upfileId}','${spFile.uuid}','${spFile.filename}','${spFile.fileExt}','${param.sid}','022','${param.instanceId}')" title="预览">
					 				</a>
			 					</c:when>
			 					<c:when test="${spFile.fileExt=='jpg'||spFile.fileExt=='bmp'||spFile.fileExt=='gif'||spFile.fileExt=='jpeg'||spFile.fileExt=='png'}">
			 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${spFile.uuid}/${spFile.filename}?sid=${param.sid}" title="下载"></a>
			 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="showPic('/downLoad/down/${spFile.uuid}/${spFile.filename}','${param.sid}','${spFile.upfileId}','022','${param.instanceId}')" title="预览"></a>
			 					</c:when>
			 					<c:otherwise>
					 				&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad('${spFile.uuid}','${spFile.filename}','${param.sid }')" title="下载"></a>
			 					</c:otherwise>
			 				</c:choose>
			 				<c:if test="${empty delete && spFile.userId==userInfo.id}">
					 			<a style="margin-left:5px;" class="fa  fa-times-circle fa-lg" href="javascript:void(0);" title="删除" onclick="delSpFile(this,${spFile.busId},${spFile.upfileId},${spFile.addType})"></a>
			 				</c:if>
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
<tags:pageBar url="/workFlow/listSpFiles"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
