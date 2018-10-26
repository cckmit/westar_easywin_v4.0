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
		<!-- 运营纵览私有样式 -->
		<link rel="stylesheet" href="/static/css/operationSummarize.css">
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>

		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/echarts.js"></script>
		<c:choose>
			<c:when test="${param.activityMenu ne 'platform_1.7' and param.activityMenu ne 'platform_1.8' }">
				<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${empty param.activityMenu || param.activityMenu=='platform_1.2'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/statistic/platform/statisticTask.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.activityMenu=='platform_1.3'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/statistic/platform/statisticCrm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.activityMenu=='platform_1.4'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/statistic/platform/statisticFeeCrm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.activityMenu=='platform_1.5'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/statistic/platform/statisticFeeItem.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.activityMenu=='platform_1.7'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/statistic/platform/statisticSuperViewTask.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.activityMenu=='platform_1.8'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/statistic/platform/statisticBusRemind.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.activityMenu=='suverViewPlatform_1.1'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/statistic/suverview_platform/statistic_process.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
		</c:choose>
		<script type="text/javascript">
			//session标识
			var sid = '${param.sid}';
			var EASYWIN = {
					"userInfo":{
						"id":"${userInfo.id}",
						"name":"${userInfo.userName}"
					},
					"sid":"${param.sid}"
			}
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
                    var startDate = '${attenceRecord.startDate}';
                    var endDate = '${attenceRecord.endDate}';
                    var url = "/attence/viewListLeave?sid="+sid+"&spState=1&creator="+creator+"&creatorName="+userName+"&startTime="+startDate+"&endTime="+endDate+"&pager.pageSize=5";
                    window.top.layer.open({
                        title:false,
                        closeBtn:0,
                        type: 2,
                        shadeClose: true,
                        shade: 0.1,
                        shift:0,
                        zIndex:299,
                        fix: true, //固定
                        maxmin: false,
                        move: false,
                        border:1,
                        btn:['确定','关闭'],
                        area: ['600px','400px'],
                        content: [url,'no'], //iframe的url
                        yes:function(index,layero){
                            var iframeWin = window[layero.find('iframe')[0]['name']];
                            iframeWin.closeWin();
                        },
                        success:function(layero,index){
                        },end:function(index){
                        }
                    });

                });
                $(".viewListOverTime").click(function(){
                    var creator = $(this).attr("userId");
                    var userName = $(this).attr("userName");
                    var startDate = '${attenceRecord.startDate}';
                    var endDate = '${attenceRecord.endDate}';
                    var url = "/attence/viewListOverTime?sid="+sid+"&spState=1&creator="+creator+"&creatorName="+userName+"&startTime="+startDate+"&endTime="+endDate+"&pager.pageSize=5";
                    window.top.layer.open({
                        title:false,
                        closeBtn:0,
                        type: 2,
                        shadeClose: true,
                        shade: 0.1,
                        shift:0,
                        zIndex:299,
                        fix: true, //固定
                        maxmin: false,
                        move: false,
                        border:1,
                        btn:['确定','关闭'],
                        area: ['600px','400px'],
                        content: [url,'no'], //iframe的url
                        yes:function(index,layero){
                            var iframeWin = window[layero.find('iframe')[0]['name']];
                            iframeWin.closeWin();
                        },
                        success:function(layero,index){
                        },end:function(index){
                        }
                    });

                });
            });

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
			div[legend]{
				width:20px;
				height:20px;
				-moz-border-radius:10px;
				-webkit-border-radius:10px;
				border-radius:10px;
			}
		</style>
	<body>
		<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="/WEB-INF/jsp/statistics/platform/statisticCenter_left.jsp"></jsp:include>
				
				<c:choose>
					<c:when test="${empty param.activityMenu || param.activityMenu=='platform_1.0'}">
						<jsp:include page="/WEB-INF/jsp/statistics/platform/operationSummarize.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='platform_1.2'}">
						<jsp:include page="/WEB-INF/jsp/statistics/platform/statisticCenter_task.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='platform_1.7'}">
						<%--任务执行督办 --%>
						<jsp:include page="/WEB-INF/jsp/statistics/platform/statisticCenter_superview_task.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='platform_1.3'}">
						<jsp:include page="/WEB-INF/jsp/statistics/platform/statisticCenter_crm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='platform_1.4'}">
						<jsp:include page="/WEB-INF/jsp/statistics/platform/statisticCenter_fee_crm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='platform_1.5'}">
						<jsp:include page="/WEB-INF/jsp/statistics/platform/statisticCenter_fee_item.jsp"></jsp:include>
					</c:when>
					<c:when test="${empty param.activityMenu || param.activityMenu=='platform_1.6'}">
						<jsp:include page="/WEB-INF/jsp/statistics/platform/statisticCenter_attence.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='platform_1.8'}">
						<%--任务执行督办 --%>
						<jsp:include page="/WEB-INF/jsp/statistics/platform/statisticCenter_busRemind.jsp"></jsp:include>
					</c:when>
					<c:when test="${empty param.activityMenu || param.activityMenu=='platform_1.9'}">
						<!-- 日报统计分析 -->
						<jsp:include page="/WEB-INF/jsp/statistics/dailyRep/dailyRepStatistics.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='platform_2.0'}">
						<!-- 统计-->
						<jsp:include page="/WEB-INF/jsp/weekReport/listWeekRepStatistics.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='suverViewPlatform_1.1'}">
						<!-- 统计-->
						<jsp:include page="/WEB-INF/jsp/statistics/suverview_platform/statisticCenter_demand.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

