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
		<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript">
			var sid = '${param.sid}';
			var activityMenu = '${param.activityMenu }';
			var userAdmin = '${userInfo.admin}';
		</script>
		<script type="text/javascript">
		function getSubmit(){
			$("#searchForm").submit();
		}
		function listAttenceRecord(userId){
			var url = '/attence/listAttenceRecord?activityMenu=038&searchTab=03802&sid='+sid+'&userId='+userId;
			url+='&startDate='+$('#startDate').val();
			url +='&endDate='+$('#endDate').val();
			window.location.href = url;
		}
		$(function() {

			$(".subform").Validform({
				tiptype: function(msg, o, cssctl) {
					validMsg(msg, o, cssctl);
				},
				showAllError: true
			});
			
			$(".viewListLeave").click(function(){
				var creator = $(this).attr("userId");
				var userName = $(this).attr("userName");
				var url = "/attence/viewListLeave?sid="+sid+"&spState=1&creator="+creator+"&creatorName="+userName+"&startTime="+startDate+"&endTime="+endDate+"&pager.pageSize=5";
				window.top.layer.open({
						title:false,
						closeBtn:0,
						type: 2,
						shadeClose: true,
						shade: 0.1,
						shift:0,
						fix: true, //固定
						maxmin: false,
						move: false,
						border:1,
					  	btn:['关闭'],
						area: ['600px','400px'],
						content: [url,'no'], //iframe的url
						yes:function(index,layero){
							window.top.layer.close(index);
					  	},
					  	 success:function(layero,index){
						}
				 });
				
			});
			$(".viewListOverTime").click(function(){
				var creator = $(this).attr("userId");
				var userName = $(this).attr("userName");
				var url = "/attence/viewListOverTime?sid="+sid+"&spState=1&creator="+creator+"&creatorName="+userName+"&startTime="+startDate+"&endTime="+endDate+"&pager.pageSize=5";
				window.top.layer.open({
						title:false,
						closeBtn:0,
						type: 2,
						shadeClose: true,
						shade: 0.1,
						shift:0,
						fix: true, //固定
						maxmin: false,
						move: false,
						border:1,
					  	btn:['关闭'],
						area: ['600px','400px'],
						content: [url,'no'], //iframe的url
						yes:function(index,layero){
							window.top.layer.close(index);
					  	},
				  	 	success:function(layero,index){
						}
				 });
				
			});
		});

	</script>
	<body>
		<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="/WEB-INF/jsp/attence/attenceCenter_left.jsp"></jsp:include>

				<c:if test="${param.searchTab=='03801'}">
					<jsp:include page="/WEB-INF/jsp/attence/listAttenceState.jsp"></jsp:include>
				</c:if>
				<c:if test="${param.searchTab=='03802'}">
					<jsp:include page="/WEB-INF/jsp/attence/listAttenceRecord.jsp"></jsp:include>
				</c:if>
				<c:if test="${param.searchTab=='03803'}">
					<jsp:include page="/WEB-INF/jsp/attence/attenceStatistics.jsp"></jsp:include>
				</c:if>
				<!--显示个人请假记录-->
				<c:if test="${param.searchTab=='03804'}">
					<jsp:include page="/WEB-INF/jsp/attence/listLeave.jsp"></jsp:include>
				</c:if>
				<!--显示个人加班记录-->
				<c:if test="${param.searchTab=='03805'}">
					<jsp:include page="/WEB-INF/jsp/attence/listOverTime.jsp"></jsp:include>
				</c:if>
				<!--显示个人考勤记录-->
				<c:if test="${param.searchTab=='03806'}">
					<jsp:include page="/WEB-INF/jsp/attence/listAttenceRecordOfSelf.jsp"></jsp:include>
				</c:if>
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

