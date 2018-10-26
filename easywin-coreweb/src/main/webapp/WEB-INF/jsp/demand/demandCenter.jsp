<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<script type="text/javascript" src="/static/js/demandJs/demandCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript">
			//session标识
			var sid = '${param.sid}';
			var pageParam = {
					"sid":"${param.sid}"
			}
			$(function(){
				 $(".subform").Validform({
					tiptype : function(msg, o, cssctl) {
						validMsgV2(msg, o, cssctl);
					},
					callback:function (form){
						//提交前验证是否在上传附件
						return sumitPreCheck(null);
					},
					showAllError : true
				}); 
			})
			
			
		</script>
	<body>
		<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="/WEB-INF/jsp/demand/demandCenter_left.jsp"></jsp:include>
				<c:choose>
					<c:when test="${empty param.activityMenu || param.activityMenu=='demand_1.1' }">
						<!--5、项目任务统计-->
						<jsp:include page="/WEB-INF/jsp/demand/listPagedMineDemand.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='demand_1.2' }">
						<jsp:include page="/WEB-INF/jsp/demand/listPagedDemandForAccept.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='demand_1.3' }">
						<jsp:include page="/WEB-INF/jsp/demand/listPagedDemandForHandle.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='demand_1.4' }">
						<jsp:include page="/WEB-INF/jsp/demand/listPagedDemandForConfirm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='demand_1.5' }">
						<jsp:include page="/WEB-INF/jsp/demand/listPagedDemandForAll.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='demandMod_1.1' }">
						<jsp:include page="/WEB-INF/jsp//demand/demand_module_cfg/listPagedDemandModuleCfg.jsp"></jsp:include>
					</c:when>
				</c:choose>
				
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

