<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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
		<script type="text/javascript" src="/static/js/zhbgJs/zhbg.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript" src="/static/js/zhbgJs/gdzcJs/gdzcCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		
		<script type="text/javascript">
			var sid = '${param.sid}';
			var orgName = '${userInfo.orgName}';
			var activityMenu = '${param.activityMenu }';
		</script>
		<script type="text/javascript">
		$(function() {

			$(".subform").Validform({
				tiptype: function(msg, o, cssctl) {
					validMsg(msg, o, cssctl);
				},
				showAllError: true
			});
		})
		
		/**
		 * 清空更多查询条件
		 */
		function resetMoreCon(divId){
			$("#startDate").val('');
			$("#endDate").val('');
			$("#addStartDate").val('');
			$("#endDate").val('');
		}
		
		//窗体点击事件检测
		document.onclick = function(e){
			var evt = e?e:window.event;
			var ele = $(evt.srcElement || evt.target);
			if ($(ele).parents("#moreCondition_Div").length == 1) { 
				if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
					return false;
				}else{
					if(!$("#moreCondition_Div").hasClass("open")){
						$(".searchCond").find(".open").removeClass("open");
						$("#moreCondition_Div").addClass("open");
					}
				}
			} else{
				$("#moreCondition_Div").removeClass("open");
			}
		}
		/**
		 * 展示查询条件中更多
		 */
		function displayMoreCond(divId){
			if($("#"+divId).hasClass("open")){
				$("#"+divId).removeClass("open");
			}else{
				$("#"+divId).addClass("open")
			}
		}
		//固定资产类型筛选
		function gdzcTypeFilter(ts,value){
			//启动加载页面效果
			layer.load(0, {shade: [0.6,'#fff']});
			$("#ssType").val(value);
			$("#searchForm").submit();
		}
		//固定添加类型筛选
		function addTypeFilter(ts,value){
			//启动加载页面效果
			layer.load(0, {shade: [0.6,'#fff']});
			$("#addType").val(value);
			$("#searchForm").submit();
		}
	
	</script>
	<body>
		<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="/WEB-INF/jsp/zhbg/gdzc/gdzcCenter_left.jsp"></jsp:include>
				<c:choose>
					<c:when test="${not empty recycleTab }">
						<jsp:include page="/WEB-INF/jsp/recycleBin/listPagedPreDel.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<c:if test="${param.activityMenu == '026'}">
						<jsp:include page="/WEB-INF/jsp/zhbg/gdzc/listGdzc.jsp"></jsp:include>
						</c:if>
					</c:otherwise>
				</c:choose>
				
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

