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
<!--Basic Styles-->
<link href="/static/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">

 <!--Beyond styles-->
<link href="/static/assets/css/beyond.min.css" rel="stylesheet"  type="text/css">
<link href="/static/assets/css/beyond_2.min.css" rel="stylesheet"  type="text/css">
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>

<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">

<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script> 
$(document).ready(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	//将当前团队放置第一位
	var nowOrg = $("#orgList").find(".btn-self").clone();
	$("#orgList").find(".btn-self").remove();
	$("#orgList").prepend(nowOrg);
});
</script>
<style type="text/css">
.btn-self{ width:100%; height: 40px;  background:#00cfff;color:#fff; text-align: center; 
	line-height:40px; display: block; border-radius: 4px;margin-top: 5px}
.btn-changeA{ width:100%; height: 40px; background-color:rgba(0,0,0,.3); color:#fff; text-align: center; 
	line-height:40px; display: block; border-radius: 4px;margin-top: 5px}
.btn-changeA:hover{ background-color:rgba(0,0,0,.4);}
.ps-layerTitle{
	font-size: 18px !important;
}
</style>
</head>
<body>
		<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
			<div class="widget-caption" style="text-align:center;width: 90%;">
				<span class="themeprimary ps-layerTitle">团队切换</span>
			</div>
			<div class="widget-buttons">
				<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
					<i class="fa fa-times themeprimary"></i>
				</a>
			</div>
		</div>
	<div id="contentBody" class="widget-body" style="overflow:hidden;overflow-y:auto;position: relative;">
		     <div class="loginbox-submit" id="orgList">
				<c:forEach items="${list}" var="org" varStatus="vs">
					<c:if test="${org.isSysUser== 1 && org.inService==1}">
						<c:choose>
							<c:when test="${comId == org.orgNum}">
								<span class="btn-self" >[${org.orgNum}]${org.orgName}</span>
							</c:when>
							<c:otherwise>
								<a href="javascript:void(0)" class="btn-changeA singleOrg" data="${org.orgNum}">[${org.orgNum}]${org.orgName}</a>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:forEach> 
			 </div>
	</div>
</body>
</html>
