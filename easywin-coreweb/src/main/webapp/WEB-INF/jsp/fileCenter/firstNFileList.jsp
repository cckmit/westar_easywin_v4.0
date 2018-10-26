<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
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
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript"> 
$(function(){
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
});
</script>
</head>
<body style="background-color: #fff" onload="resizeVoteH('knowledgeCenter');">
<div class="tab-pane active">
	<div class="ws-file">
		<div class="row">
			<c:choose>
       			<c:when test="${not empty fileList}">
       				<c:forEach items="${fileList}" var="file" varStatus="vs">
       					<div class="col-xs-2">
							<c:choose>
									<c:when test="${file.fileExt=='doc' || file.fileExt=='docx' || file.fileExt=='xls' || file.fileExt=='xlsx' || file.fileExt=='ppt' || file.fileExt=='pptx' || file.fileExt=='pdf'}">
	 								<a href="javascript:void(0);" class="ws-oneFile"  onclick="viewOfficePage('${file.fileId}','${file.uuid}','${file.fileName}','${file.fileExt}','${param.sid}','${file.moduleType}','${file.modulePrim}')">
	 									<c:if test="${file.fileExt=='doc' || file.fileExt=='docx'}">
	 										<i title="预览"><img src="/static/images/doc.png"/></i>
	 									</c:if>
	 									<c:if test="${file.fileExt=='xls' || file.fileExt=='xlsx'}">
	 										<i title="预览"><img src="/static/images/xle.png"/></i>
	 									</c:if>
	 									<c:if test="${file.fileExt=='ppt' || file.fileExt=='pptx'}">
	 										<i title="预览"><img src="/static/images/ppt.png"/></i>
	 									</c:if>
	 									<c:if test="${file.fileExt=='pdf'}">
	 										<i title="预览"><img src="/static/images/other.png"/></i>
	 									</c:if>
										<p title="${file.fileName}" style="word-wrap:break-word"><tags:cutString num="36">${file.fileName}</tags:cutString></p>
	 								</a>
									</c:when>
									<c:when test="${file.fileExt=='txt'}">
	 								<a href="javascript:void(0);" class="ws-oneFile"  onclick="viewOfficePage('${file.fileId}','${file.uuid}','${file.fileName}','${file.fileExt}','${param.sid}','${file.moduleType}','${file.modulePrim}')">
		 								<i title="预览"><img src="/static/images/other.png"/></i>
										<p title="${file.fileName}"><tags:cutString num="36">${file.fileName}</tags:cutString></p>
	 								</a>
									</c:when>
									<c:when test="${file.fileExt=='jpg'||file.fileExt=='bmp'||file.fileExt=='gif'||file.fileExt=='jpeg'||file.fileExt=='png'}">
	 								<a href="javascript:void(0);" class="ws-oneFile"  onclick="showPic('/downLoad/down/${file.uuid}/${file.fileName}','${param.sid}','${file.fileId}','${file.moduleType}','${file.modulePrim}')">
		 								<i title="预览"><img src="/static/images/img.png"/></i>
										<p title="${file.fileName}"><tags:cutString num="12">${file.fileName}</tags:cutString></p>
	 								</a>
									</c:when>
									<c:otherwise>
									<a href="javascript:void(0);" class="ws-oneFile" style="cursor:text;">
										<i><img src="/static/images/rar.png"/></i>
										<p title="${file.fileName}"><tags:cutString num="36">${file.fileName}</tags:cutString></p>
									</a>
									</c:otherwise>
								</c:choose>
						</div>
       				</c:forEach>
       			</c:when>
       		</c:choose>
		</div>
	</div>
</div>	
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

