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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">
var sid="${param.sid}";

//删除客户附件
function delConsumeFile(ts,consumeId,consumeUpFileId){
	window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/consume/delConsumeUpfile?sid=${param.sid}",{Action:"post",consumeId:consumeId,consumeUpFileId:consumeUpFileId},     
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
<body onload="resizeVoteH('otherCustomerAttrIframe');" style="background-color:#FFFFFF;">
	<table class="table table-striped table-hover general-table fixTable">
     	<thead>
        	<tr>
                <th width="5%" valign="middle">&nbsp;</th>
                <th valign="middle">附件名称</th>
				<th width="25%" valign="middle">上传时间</th>
				<th width="20%" valign="middle">上传人</th>
				<th width="20%" valign="middle" style="text-align: center;">操作</th>
            </tr>
       </thead>
       <tbody>
       	<c:choose>
	 	<c:when test="${not empty listConsumeUpfile}">
	 		<c:forEach items="${listConsumeUpfile}" var="file" varStatus="status">
	 			<tr>
	 				<td height="40">${ status.count}</td>
	 				<td align="left" title="${ file.filename}">${file.filename}</td>
	 				<td>${fn:substring(file.recordCreateTime,0,10)}</td>
	 				<td style="text-align: left;">
	 					<div class="ticket-user pull-left other-user-box">
								<img src="/downLoad/userImg/${userInfo.comId}/${file.userId}?sid=${param.sid}"
										title="${file.creatorName}" class="user-avatar" />
							<span class="user-name">${file.creatorName}</span>
						</div>
	 				</td>
	 				<td height="30" align="center">
		 				<c:choose>
		 					<c:when test="${file.fileExt=='doc' || file.fileExt=='docx' || file.fileExt=='xls' || file.fileExt=='xlsx' || file.fileExt=='ppt' || file.fileExt=='pptx'}">
				 				<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${file.uuid}','${file.filename}','${param.sid }')"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${file.upfileId}','${file.uuid}','${file.filename}','${file.fileExt}','${param.sid}','${file.type}','${file.busId}')"></a>
		 					</c:when>
		 					<c:when test="${file.fileExt=='txt'  || file.fileExt=='pdf'}">
				 				<a class="fa fa-download" href="/downLoad/down/${file.uuid}/${file.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${file.upfileId}','${file.uuid}','${file.filename}','${file.fileExt}','${param.sid}','${file.type}','${file.busId}')"></a>
		 					</c:when>
		 					<c:when test="${file.fileExt=='jpg'||file.fileExt=='bmp'||file.fileExt=='gif'||file.fileExt=='jpeg'||file.fileExt=='png'}">
				 				<a class="fa fa-download" href="/downLoad/down/${file.uuid}/${file.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${file.uuid}/${file.filename}','${param.sid}','${file.upfileId}','${file.type}','${file.busId}')"></a>
		 					</c:when>
		 					<c:otherwise>
				 				<a class="fa fa-download" onclick="downLoad('${file.uuid}','${file.filename}','${param.sid }')" title="下载"></a>
		 					</c:otherwise>
		 				</c:choose>
		 				<c:if test="${empty delete && file.userId==userInfo.id && consumeStatus == 0 && param.delete != false}">
				 			<a style="margin-left:10px;" class="fa  fa-times-circle" href="javascript:void(0);" title="删除" onclick="delConsumeFile(this,${file.consumeId},${file.id})"></a>
		 				</c:if>
		 			</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="30" colspan="5" align="center"><h3>没有相关信息！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
       </tbody>
      </table>
<tags:pageBar url="/consume/consumeUpfilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
