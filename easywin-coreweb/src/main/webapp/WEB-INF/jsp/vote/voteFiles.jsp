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
//删除投票附件
function delVoteFile(ts,voteId,voteUpFileId,type){
	window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/vote/delVoteUpfile?sid=${param.sid}",{Action:"post",voteId:voteId,voteUpFileId:voteUpFileId},     
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
<body onload="resizeVoteH('voteInfo');initCard('${param.sid }')" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<tr>
		<td width="10%" height="40">
			<h5>序号</h5></td>
		<td width="37%" height="40">
			<h5>名称</h5></td>
		<td width="20%" height="40">
			<h5>上传时间</h5></td>
		<td width="18%" height="40">
			<h5>上传人</h5></td>
		<td width="15%" height="40" align="center"><h5>操作</h5></td>
	</tr>
	<c:choose>
	 	<c:when test="${not empty voteTalkFiles}">
	 		<c:forEach items="${voteTalkFiles}" var="voteFile" varStatus="status">
	 			<tr>
	 				<td height="40">${ status.count}</td>
	 				<td align="left">${ voteFile.filename}</td>
	 				<td>${voteFile.recordCreateTime}</td>
	 				<td style="text-align: left;">
		 				<div class="ticket-user pull-left other-user-box" >
							<img src="/downLoad/userImg/${voteFile.comId}/${voteFile.userId}?sid=${param.sid}"
								title="${voteFile.creatorName}" class="user-avatar"/>
							<i class="user-name">${voteFile.creatorName}</i>
							</div>
	 				</td>
		 			<td height="30" align="center">
			 				<c:choose>
			 					<c:when test="${voteFile.fileExt=='doc' || voteFile.fileExt=='docx' || voteFile.fileExt=='xls' || voteFile.fileExt=='xlsx' || voteFile.fileExt=='ppt' || voteFile.fileExt=='pptx' }">
					 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad('${voteFile.uuid}','${voteFile.filename}','${param.sid }')" title="下载"></a>
					 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${voteFile.upfileId}','${voteFile.uuid}','${voteFile.filename}','${voteFile.fileExt}','${param.sid}','004','${voteId}')" title="预览">
					 				</a>
			 					</c:when>
			 					<c:when test="${voteFile.fileExt=='txt' || voteFile.fileExt=='pdf'}">
			 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${voteFile.uuid}/${voteFile.filename}?sid=${param.sid}" title="下载"></a>
					 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${voteFile.upfileId}','${voteFile.uuid}','${voteFile.filename}','${voteFile.fileExt}','${param.sid}','004','${voteId}')" title="预览">
					 				</a>
			 					</c:when>
			 					<c:when test="${voteFile.fileExt=='jpg'||voteFile.fileExt=='bmp'||voteFile.fileExt=='gif'||voteFile.fileExt=='jpeg'||voteFile.fileExt=='png'}">
			 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${voteFile.uuid}/${voteFile.filename}?sid=${param.sid}" title="下载"></a>
			 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="showPic('/downLoad/down/${voteFile.uuid}/${voteFile.filename}','${param.sid}','${voteFile.upfileId}','004','${voteId}')" title="预览"></a>
			 					</c:when>
			 					<c:otherwise>
					 				&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad('${voteFile.uuid}','${voteFile.filename}','${param.sid }')" title="下载"></a>
			 					</c:otherwise>
			 				</c:choose>
			 				<c:if test="${empty delete && voteFile.userId==userInfo.id}">
					 			<a style="margin-left:5px;" class="fa  fa-times-circle fa-lg" href="javascript:void(0);" title="删除" onclick="delVoteFile(this,${voteFile.voteId},${voteFile.id})"></a>
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
<tags:pageBar url="/vote/voteFilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
