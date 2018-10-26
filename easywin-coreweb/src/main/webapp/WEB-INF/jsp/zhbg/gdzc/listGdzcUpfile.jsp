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
$(function(){
	 //页面刷新
	$("#refreshImg").click(function(){
		window.self.location.reload();
	});
});
//删除固定资产附件
function delGdzcFile(ts,busId,gdzcFileId,busType){
	window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/gdzc/delGdzcUpfile?sid=${param.sid}",{Action:"post",busId:busId,gdzcFileId:gdzcFileId,busType:busType},     
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
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
</script>
</head>
<body onload="resizeVoteH('otherGdzcAttrIframe');" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<thead>
		<tr>
			<th width="8%" valign="middle">序号</th>
			<th valign="middle" class="hidden-phone">附件名称</th>
			<th width="22%" valign="middle">来源于</th>
			<th width="20%" valign="middle">上传时间</th>
			<th width="12%" valign="middle">上传人</th>
			<th width="12%" valign="middle" style="text-align: center;">操作</th>
		</tr>
	</thead>
	<tbody>
	<c:choose>
	 	<c:when test="${not empty listGdzcUpfile}">
	 		<c:forEach items="${listGdzcUpfile}" var="gdzcUpfile" varStatus="status">
	 			<tr>
	 				<td class="fileOrder">${ status.count}</td>
	 				<td align="left" style="font-size: 13px">${ gdzcUpfile.fileName}</td>
	 				<td style="font-size: 13px">
	 					<c:set var="fileSource">
	 						<c:choose>
								<c:when test="${'026' eq gdzcUpfile.busType }">
 									[固定资产]
								</c:when>
									<c:when test="${'02603' eq gdzcUpfile.busType }">
 									[减少]
								</c:when>
									<c:when test="${'02604' eq gdzcUpfile.busType }">
 									[维修]
								</c:when>
							</c:choose>
	 					</c:set>
	 					<c:choose>
	 						<c:when test="${gdzcUpfile.busId==gdzc.id}">
	 							<font color="#757575">
	 								${fileSource}
	 							</font>
	 						</c:when>
	 						<c:otherwise>
	 							${fileSource}
	 							</a>
	 						</c:otherwise>
	 					</c:choose>
	 				</td>
	 				<td>${fn:substring(gdzcUpfile.recordCreateTime,0,16)}</td>
	 				<td style="text-align: left;">
	 					<div class="ws-position" data-container="body" data-toggle="popover" data-placement="left"  
	 					data-user='${gdzcUpfile.userId}' data-busId='${gdzcUpfile.busId}' data-busType='${gdzcUpfile.busType }'>
						<img src="/downLoad/userImg/${gdzcUpfile.comId}/${gdzcUpfile.creator}?sid=${param.sid}"
							title="${gdzcUpfile.creatorName}" class="user-avatar"/>
						<i class="ws-smallName">${gdzcUpfile.creatorName}</i>
						</div>
	 				</td>
	 				<td align="center">
		 				<c:choose>
		 					<c:when test="${gdzcUpfile.fileExt=='doc' || gdzcUpfile.fileExt=='docx' || gdzcUpfile.fileExt=='xls' || gdzcUpfile.fileExt=='xlsx' || gdzcUpfile.fileExt=='ppt' || gdzcUpfile.fileExt=='pptx'}">
				 				<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${gdzcUpfile.uuid}','${gdzcUpfile.fileName}','${param.sid }')"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${gdzcUpfile.upfileId}','${gdzcUpfile.uuid}','${gdzcUpfile.fileName}','${gdzcUpfile.fileExt}','${param.sid}','${gdzcUpfile.busType }','${gdzcUpfile.busId}')"></a>
		 					</c:when>
		 					<c:when test="${gdzcUpfile.fileExt=='txt'  || gdzcUpfile.fileExt=='pdf'}">
				 				<a class="fa fa-download" href="/downLoad/down/${gdzcUpfile.uuid}/${gdzcUpfile.fileName}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${gdzcUpfile.upfileId}','${gdzcUpfile.uuid}','${gdzcUpfile.fileName}','${gdzcUpfile.fileExt}','${param.sid}','${gdzcUpfile.busType }','${gdzcUpfile.busId}')"></a>
		 					</c:when>
		 					<c:when test="${gdzcUpfile.fileExt=='jpg'||gdzcUpfile.fileExt=='bmp'||gdzcUpfile.fileExt=='gif'||gdzcUpfile.fileExt=='jpeg'||gdzcUpfile.fileExt=='png'}">
				 				<a class="fa fa-download" href="/downLoad/down/${gdzcUpfile.uuid}/${gdzcUpfile.fileName}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${gdzcUpfile.uuid}/${gdzcUpfile.fileName}','${param.sid}','${gdzcUpfile.upfileId}','${gdzcUpfile.busType }','${gdzcUpfile.busId}')"></a>
		 					</c:when>
		 					<c:otherwise>
				 				<a class="fa fa-download" onclick="downLoad('${gdzcUpfile.uuid}','${gdzcUpfile.fileName}','${param.sid }')" title="下载"></a>
		 					</c:otherwise>
		 				</c:choose>
		 				<c:if test="${empty delete  && isModAdmin }">
				 			<a style="margin-left:5px;" class="fa  fa-times-circle fa-lg" href="javascript:void(0);" title="删除" onclick="delGdzcFile(this,'${gdzcUpfile.busId}','${gdzcUpfile.id}','${gdzcUpfile.busType}')"></a>
		 				</c:if>
		 			</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="9" align="center"><h3>没有相关附件！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
	</tbody>
</table>
<tags:pageBar url="/gdzc/gdzcUpfilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
