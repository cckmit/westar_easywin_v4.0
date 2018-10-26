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
	//初始化名片
	initCard('${param.sid}');
	 //页面刷新
	$("#refreshImg").click(function(){
		window.self.location.reload();
	});
});
//删除车辆附件
function delCarFile(ts,busId,carFileId,busType){
	window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/car/delCarUpfile?sid=${param.sid}",{Action:"post",busId:busId,carFileId:carFileId,busType:busType},     
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
<body onload="resizeVoteH('otherCarAttrIframe');" style="background-color:#FFFFFF;">
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
	 	<c:when test="${not empty listCarUpfile}">
	 		<c:forEach items="${listCarUpfile}" var="carUpfile" varStatus="status">
	 			<tr>
	 				<td class="fileOrder">${ status.count}</td>
	 				<td align="left" style="font-size: 13px">${ carUpfile.fileName}</td>
	 				<td style="font-size: 13px">
	 					<c:set var="fileSource">
	 						<c:choose>
								<c:when test="${'025' eq carUpfile.busType }">
 									[车辆]
								</c:when>
									<c:when test="${'02501' eq carUpfile.busType }">
 									[强险]
								</c:when>
									<c:when test="${'02502' eq carUpfile.busType }">
 									[商业险]
								</c:when>
							</c:choose>
	 					</c:set>
	 					<c:choose>
	 						<c:when test="${carUpfile.busId==car.id}">
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
	 				<td>${fn:substring(carUpfile.recordCreateTime,0,16)}</td>
	 				<td style="text-align: left;">
	 					<div class="ws-position" data-container="body" data-toggle="popover" data-placement="left"  
	 					data-user='${carUpfile.userId}' data-busId='${carUpfile.busId}' data-busType='${carUpfile.busType }'>
						<img src="/downLoad/userImg/${carUpfile.comId}/${carUpfile.creator}?sid=${param.sid}"
							title="${carUpfile.creatorName}" class="user-avatar"/>
						<i class="ws-smallName">${carUpfile.creatorName}</i>
						</div>
	 				</td>
	 				<td align="center">
		 				<c:choose>
		 					<c:when test="${carUpfile.fileExt=='doc' || carUpfile.fileExt=='docx' || carUpfile.fileExt=='xls' || carUpfile.fileExt=='xlsx' || carUpfile.fileExt=='ppt' || carUpfile.fileExt=='pptx'}">
				 				<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${carUpfile.uuid}','${carUpfile.fileName}','${param.sid }')"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${carUpfile.upfileId}','${carUpfile.uuid}','${carUpfile.fileName}','${carUpfile.fileExt}','${param.sid}','${carUpfile.busType }','${carUpfile.busId}')"></a>
		 					</c:when>
		 					<c:when test="${carUpfile.fileExt=='txt'  || carUpfile.fileExt=='pdf'}">
				 				<a class="fa fa-download" href="/downLoad/down/${carUpfile.uuid}/${carUpfile.fileName}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${carUpfile.upfileId}','${carUpfile.uuid}','${carUpfile.fileName}','${carUpfile.fileExt}','${param.sid}','${carUpfile.busType }','${carUpfile.busId}')"></a>
		 					</c:when>
		 					<c:when test="${carUpfile.fileExt=='jpg'||carUpfile.fileExt=='bmp'||carUpfile.fileExt=='gif'||carUpfile.fileExt=='jpeg'||carUpfile.fileExt=='png'}">
				 				<a class="fa fa-download" href="/downLoad/down/${carUpfile.uuid}/${carUpfile.fileName}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${carUpfile.uuid}/${carUpfile.fileName}','${param.sid}','${carUpfile.upfileId}','${carUpfile.busType }','${carUpfile.busId}')"></a>
		 					</c:when>
		 					<c:otherwise>
				 				<a class="fa fa-download" onclick="downLoad('${carUpfile.uuid}','${carUpfile.fileName}','${param.sid }')" title="下载"></a>
		 					</c:otherwise>
		 				</c:choose>
		 				<c:if test="${empty delete  && isModAdmin }">
				 			<a style="margin-left:5px;" class="fa  fa-times-circle fa-lg" href="javascript:void(0);" title="删除" onclick="delCarFile(this,'${carUpfile.busId}','${carUpfile.id}','${carUpfile.busType}')"></a>
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
<tags:pageBar url="/car/carUpfilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
