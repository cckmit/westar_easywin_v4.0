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

//删除会议附件
function delMeetFile(ts,meetingId,meetUpFileId,type){
	window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/meeting/delMeetUpfile?sid=${param.sid}",{Action:"post",meetingId:meetingId,meetUpFileId:meetUpFileId,type:type},     
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
<body onload="resizeVoteH('otherAttrIframe');" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<thead>
		<tr>
			<th width="8%" valign="middle">序号</th>
			<th valign="middle" class="hidden-phone">附件名称</th>
			<th width="12%" valign="middle">来源于</th>
			<th width="18%" valign="middle">上传时间</th>
			<th width="18%" valign="middle">上传人</th>
			<th width="15%" valign="middle">操作</th>
		</tr>
	</thead>
	<tbody>
	<c:choose>
	 	<c:when test="${not empty listMeetUpfiles}">
	 		<c:forEach items="${listMeetUpfiles}" var="taskUpfileVo" varStatus="status">
	 			<tr>
	 				<td>${ status.count}</td>
	 				<td align="left">${ taskUpfileVo.filename}</td>
	 				<td>
	 					<c:choose>
	 						<c:when test="${taskUpfileVo.sourceType==0 }">
	 							会议纪要
	 						</c:when>
	 						<c:otherwise>
	 							会议留言
	 						</c:otherwise>
	 					</c:choose>
	 				</td>
	 				<td>${fn:substring(taskUpfileVo.recordCreateTime,0,16)}</td>
	 				<td style="text-align: left;">
	 					<div class="ticket-user pull-left other-user-box" data-container="body" data-toggle="popover" data-placement="left"  
	 					data-user='${taskUpfileVo.userId}' data-busId='${taskUpfileVo.meetingId}' data-busType='017'>
							<img src="/downLoad/userImg/${taskUpfileVo.comId}/${taskUpfileVo.userId}?sid=${param.sid}"
								title="${taskUpfileVo.creatorName}" class="user-avatar" />
						<i class="user-name">${taskUpfileVo.creatorName}</i>
						</div>
	 				</td>
	 				<td align="center">
		 				<c:choose>
		 					<c:when test="${taskUpfileVo.fileExt=='doc' || taskUpfileVo.fileExt=='docx' || taskUpfileVo.fileExt=='xls' || taskUpfileVo.fileExt=='xlsx' || taskUpfileVo.fileExt=='ppt' || taskUpfileVo.fileExt=='pptx'}">
				 				<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${taskUpfileVo.uuid}','${taskUpfileVo.filename}','${param.sid }')"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${taskUpfileVo.upfileId}','${taskUpfileVo.uuid}','${taskUpfileVo.filename}','${taskUpfileVo.fileExt}','${param.sid}','017','${taskUpfileVo.meetingId}')"></a>
		 					</c:when>
		 					<c:when test="${taskUpfileVo.fileExt=='txt'  || taskUpfileVo.fileExt=='pdf'}">
				 				<a class="fa fa-download" href="/downLoad/down/${taskUpfileVo.uuid}/${taskUpfileVo.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${taskUpfileVo.upfileId}','${taskUpfileVo.uuid}','${taskUpfileVo.filename}','${taskUpfileVo.fileExt}','${param.sid}','017','${taskUpfileVo.meetingId}')"></a>
		 					</c:when>
		 					<c:when test="${taskUpfileVo.fileExt=='jpg'||taskUpfileVo.fileExt=='bmp'||taskUpfileVo.fileExt=='gif'||taskUpfileVo.fileExt=='jpeg'||taskUpfileVo.fileExt=='png'}">
				 				<a class="fa fa-download" href="/downLoad/down/${taskUpfileVo.uuid}/${taskUpfileVo.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${taskUpfileVo.uuid}/${taskUpfileVo.filename}','${param.sid}','${taskUpfileVo.upfileId}','017','${taskUpfileVo.meetingId}')"></a>
		 					</c:when>
		 					<c:otherwise>
				 				<a class="fa fa-download" onclick="downLoad('${taskUpfileVo.uuid}','${taskUpfileVo.filename}','${param.sid }')" title="下载"></a>
		 					</c:otherwise>
		 				</c:choose>
		 				<c:if test="${empty delete && taskUpfileVo.userId==userInfo.id}">
				 			<a style="margin-left:10px;" class="fa  fa-times-circle fa-lg" href="javascript:void(0);" title="删除" onclick="delMeetFile(this,${taskUpfileVo.meetingId},${taskUpfileVo.sourceId},'${taskUpfileVo.sourceType}')"></a>
		 				</c:if>
		 			</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="9" align="center"><h3>没有相关信息！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
	</tbody>
</table>
<tags:pageBar url="/meeting/listPagedMeetUpfiles"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
