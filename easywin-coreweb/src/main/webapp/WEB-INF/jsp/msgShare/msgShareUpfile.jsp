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

	$(document).ready(function() {
		resizeVoteH('otherIframe1');
		//初始化名片
		initCard('${param.sid}');
	});
	//删除分享附件
	function delMsgFile(ts,msgId,msgUpFileId){
		window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
			window.top.layer.close(index);
			$.post("/msgShare/delMsgUpfile?sid=${param.sid}",{Action:"post",msgId:msgId,msgUpFileId:msgUpFileId},     
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
<style type="text/css">
		#infoList table{
			table-layout: fixed;
		}
		#infoList td,#infoList th{
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden; 
		}
	</style>
	
</head>
<body style="background-color:#FFFFFF">
<div id="infoList">
	<table  class="table table-hover general-table" style="background-color:#FFFFFF;width: 100%">
					<tr>
						<td width="10%" height="40">
							<h5>序号</h5></td>
						<td width="20%" height="40">
							<h5>名称</h5></td>
						<td width="35%" height="40">
							<h5>上传时间</h5></td>
						<td width="20%" height="40">
							<h5>上传人</h5></td>
						<td width="100px" height="40" align="center"><h5>操作</h5></td>
					</tr>
					<c:choose>
					 	<c:when test="${not empty listMsgShareTalkUpfile}">
					 		<c:forEach items="${listMsgShareTalkUpfile}" var="msgShareTalkUpfileVo" varStatus="status">
					 			<tr>
					 				<td height="40">${ status.count}</td>
					 				<td align="left">${ msgShareTalkUpfileVo.filename}</td>
					 				<td>${msgShareTalkUpfileVo.recordCreateTime}</td>
					 				<td style="text-align: left;">
						 				<div class="ws-position" data-container="body" data-toggle="popover" data-placement="left"  
						 				data-user='${msgShareTalkUpfileVo.userId}' data-busId='${msgShareTalkUpfileVo.msgId}' data-busType='1'>
													<img src="/downLoad/userImg/${userInfo.comId }/${msgShareTalkUpfileVo.userId}"
														title="${msgShareTalkUpfileVo.creatorName}" class="user-avatar"/>
											<i class="ws-smallName">${msgShareTalkUpfileVo.creatorName}</i>
											</div>
					 				</td>
						 			<td height="30" align="center">
							 				<c:choose>
							 					<c:when test="${msgShareTalkUpfileVo.fileExt=='doc' || msgShareTalkUpfileVo.fileExt=='docx' || msgShareTalkUpfileVo.fileExt=='xls' || msgShareTalkUpfileVo.fileExt=='xlsx' || msgShareTalkUpfileVo.fileExt=='ppt' || msgShareTalkUpfileVo.fileExt=='pptx' }">
									 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad('${msgShareTalkUpfileVo.uuid}','${msgShareTalkUpfileVo.filename}','${param.sid}')" title="下载"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${msgShareTalkUpfileVo.upfileId}','${msgShareTalkUpfileVo.uuid}','${msgShareTalkUpfileVo.filename}','${msgShareTalkUpfileVo.fileExt}','${param.sid}','1','${msgShareTalkUpfileVo.msgId}')" title="预览">
									 				</a>
							 					</c:when>
							 					<c:when test="${msgShareTalkUpfileVo.fileExt=='txt' || msgShareTalkUpfileVo.fileExt=='pdf'}">
							 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${msgShareTalkUpfileVo.uuid}/${msgShareTalkUpfileVo.filename}?sid=${param.sid}" title="下载"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${msgShareTalkUpfileVo.upfileId}','${msgShareTalkUpfileVo.uuid}','${msgShareTalkUpfileVo.filename}','${msgShareTalkUpfileVo.fileExt}','${param.sid}','1','${msgShareTalkUpfileVo.msgId}')" title="预览">
									 				</a>
							 					</c:when>
							 					<c:when test="${msgShareTalkUpfileVo.fileExt=='jpg'||msgShareTalkUpfileVo.fileExt=='bmp'||msgShareTalkUpfileVo.fileExt=='gif'||msgShareTalkUpfileVo.fileExt=='jpeg'||msgShareTalkUpfileVo.fileExt=='png'}">
							 						&nbsp;&nbsp;<a class="fa fa-download"  href="/downLoad/down/${msgShareTalkUpfileVo.uuid}/${msgShareTalkUpfileVo.filename}?sid=${param.sid}" title="下载"></a>
							 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="showPic('/downLoad/down/${msgShareTalkUpfileVo.uuid}/${msgShareTalkUpfileVo.filename}','${param.sid}','${msgShareTalkUpfileVo.upfileId}','1','${msgShareTalkUpfileVo.msgId}')" title="预览"></a>
							 					</c:when>
							 					<c:otherwise>
									 				&nbsp;&nbsp;<a class="fa fa-download"  onclick="downLoad('${msgShareTalkUpfileVo.uuid}','${msgShareTalkUpfileVo.filename}','${param.sid}')" title="下载"></a>
							 					</c:otherwise>
							 				</c:choose>
							 				<c:if test="${empty delete && msgShareTalkUpfileVo.userId==userInfo.id}">
									 			<a style="margin-left:5px;" class="fa  fa-times-circle fa-lg" href="javascript:void(0);" title="删除" onclick="delMsgFile(this,${msgShareTalkUpfileVo.msgId},${msgShareTalkUpfileVo.id})"></a>
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
</div>

<tags:pageBar url="/msgShare/msgShareUpfilePage" maxIndexPages="3"></tags:pageBar>

<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
