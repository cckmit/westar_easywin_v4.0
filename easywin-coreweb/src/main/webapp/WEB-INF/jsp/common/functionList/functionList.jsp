<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page
	import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript"
	src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
	<script type="text/javascript">
        $(function(){
            $(".functionImport").click(function(){
                functionImport(${functionList.busId},'${functionList.busType}','${param.sid}');
            });
		});
	</script>
<style>
	body:before{
		background-color: #FBFBFB;
	}	
</style>
</head>
<body onload="resizeVoteH('${functionList.iframe}')">
		<div
			class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<c:choose>
				<c:when test="${param.fromType != 'view' }">
					<div class="btn-group pull-left">
						<div class="table-toolbar ps-margin">
							<div class="btn-group">
								<a class="btn btn-default dropdown-toggle btn-xs"
									data-toggle="dropdown" onclick="del()"> 功能删除 </a>
							</div>
						</div>
					</div>
					<div class="btn-group pull-left" >
						<a href="javascript:void(0);" class="btn btn-xs margin-top-10 margin-bottom-5 margin-left-5 functionImport" title="批量导入"
						   style="float: left;"><i class="fa fa-plus"></i>批量导入</a>
					</div>
					<div class="btn-group pull-left">
						<button class="btn btn-xs margin-top-10 margin-bottom-5 margin-left-20" onclick="window.location.reload();"
								style="height:24px;background-color: #437FED;color:white;border-width:1px;border-radius:2px;border-color:#dadada;border-style: solid;">&nbsp;刷&nbsp;&nbsp;新&nbsp;</button>
					</div>
				</c:when>
				<c:otherwise>
					<span class=" widget-caption themeprimary margin-top-5"
						style="font-size: 18px">功能信息</span>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="widget-body" style="clear: both">
			<div id="container" style="width: 100%;" align="center">
				<div id="left" style="width: 39%; float: left;">
					<iframe id="leftFrame" name="leftFrame"
						src="/functionList/treeFunPage?sid=${param.sid }&iframe=${functionList.iframe}&busId=${functionList.busId}&busType=${functionList.busType}&busName=${functionList.busName}&fromType=${param.fromType}" width=98%
						height=100% frameborder="0" scrolling="auto"></iframe>
				</div>
				<div id="right" style="width: 60%; float: left; text-align: left;">
					<iframe id="rightFrame" name="rightFrame" src=""
					width=98% border="0" frameborder="0" allowTransparency="true"
					noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
					<div id="decribe" style="display: none">
						<span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px;" id="titleItemName" >
	                        	功能备注:
	                     </span>
	                     <div class="ticket-user pull-left ps-right-box"
							style="width: 100%;height: auto;">
							<div class="margin-top-10 margin-bottom-10 margin-left-10" id="describeValue">

							</div>
						</div>
					</div>
				</div>
			</div>
			<div style="clear: both"></div>
		</div>
		<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
	<script type="text/javascript">
		//添加部门
		function add() {
			leftFrame.add();
		}
		//删除部门
		function del() {
			leftFrame.del();
		}

		$(function() {
			resizeVoteH('${functionList.iframe}')
		});
	</script>
</body>
</html>
