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
	    <script src="/static/js/taskJs/taskCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
	    <script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/echarts.js"></script>
	    <c:if test="${not empty recycleTab}">
			<script type="text/javascript" src="/static/js/recyleJs/recycleBin.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript">
			//session标识
			var sid = '${param.sid}';
			//当前显示页面
			var activityMenu = '${param.activityMenu }';
			//默认显示界面标识
			var pageTag = 'taskOfAll';
			//默认当前第1页
			var nowPageNum = 0;
			//默认总待办数为0
			var countTodo=0;
			if(activityMenu=='task_m_1.3'){
				pageTag = 'taskOfMine';
			}else if(activityMenu=='task_m_1.1' || ''==activityMenu){
				var pageLoc = location.search.match(/pager.offset=(\d+)/);
				if(pageLoc){
					//计算当前页数
					nowPageNum = pageLoc[1]/10;
				}
				pageTag = 'taskTodo';
				countTodo ='${countTodo}';
			}
			$(function(){
				$("body").on("click","#forceInBtn",function(){
					forceIn('${param.sid}',$(this),'003')
				});
				$("body").on("click",".addBusTask",function(){
					var actObj = $(this);
					addBusTask(-1,$(actObj).attr("busType"),'${param.version}');//业务任务发布
				});
			});
			//返回集合
			function returnList(tag,options){
				var list =[];
				$("#"+tag+"_show").find('span').remove();
				$("#"+tag+"_select").find("option").remove();
				
				if(options.length>0){
					$("#"+tag+"_show").show();
					for(var i =0;i<options.length;i++){
						$("#"+tag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
					
						var userInfo = new Object();  
			 			userInfo.id = options[i].value;
			 			userInfo.userName =  options[i].text;
			 			list.push(userInfo);
			 			
				 		var html = "<span  style=\"cursor:pointer;\"  title=\"双击移除\" ondblclick=\"removeChoose('"+tag+"','"+sid+"','"+options[i].value+"',this)\" ";
				 		html += " class=\"label label-default margin-right-5 margin-bottom-5\">"+options[i].text+"</span>";
			 			$("#"+tag+"_show").append(html);
					}
				}else{
					$("#"+tag+"_show").hide();
				}
				return list;
			}
			//导出Excel
			 function excelExport(fileName){
				var html = $("#tableView").html();
				tableToXls(html,fileName,sid);
			}
			function excelTwoExport(fileName){
				//询问框
				window.top.layer.confirm('导出数据至EXCEL？', {
				  btn: ['本页','全部'] //按钮
				}, function(index){
					var html = $("#tableView").html();
					tableToXls(html,fileName,sid);
					window.top.layer.close(index);
				}, function(index){
					 var options = $("#owner_select").find("option");
					 var listOwner =[];
					 for(var i = 0;i<options.length;i++){
						 var userInfo = new Object();  
						userInfo.id = options[i].value;
						userInfo.userName =  options[i].text;
						listOwner.push(userInfo);
					 }
					 var listOwnerStr ='';
					 if(listOwner.length>0){
						listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}';
					 }
					 
					 var url = '/task/exportTaskItem?sid='+sid;
					 url += '&state='+$("#state").val();
					 url += '&startDate='+$("#startDate").val();
					 url += '&endDate='+$("#endDate").val();
					 url += '&itemName='+$("#itemName").val();
					 url += '&fileName='+fileName;
					 url += '&listOwnerStr='+listOwnerStr;
					 
					 window.open(url,"_blank");
						window.top.layer.close(index);

				});
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
				<jsp:include page="/WEB-INF/jsp/task/taskCenter_left.jsp"></jsp:include>
				<c:choose>
					<c:when test="${not empty recycleTab }">
						<jsp:include page="/WEB-INF/jsp/recycleBin/listPagedPreDel.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<!--显示待办任务-->
						<c:if test="${(empty param.statisticsType && empty param.activityMenu) || param.activityMenu=='task_m_1.1'}">
						<jsp:include page="/WEB-INF/jsp/task/listTaskToDo.jsp"></jsp:include>
						</c:if>
						<!--显示我负责的任务-->办理中
						<c:if test="${param.activityMenu=='task_m_1.3'}">
						<jsp:include page="/WEB-INF/jsp/task/listTaskOfMine.jsp"></jsp:include>
						</c:if>
						<!--显示我关注的所有任务-->
						<c:if test="${param.activityMenu=='task_m_1.2'}">
							<jsp:include page="/WEB-INF/jsp/task/listTaskOfAtten.jsp"></jsp:include>
						</c:if>
						<!--显示我权限下的所有任务-->
						<c:if test="${param.activityMenu=='task_m_1.5'}">
						<jsp:include page="/WEB-INF/jsp/task/listTaskOfAll.jsp"></jsp:include>
						</c:if>
						<!--显示我权限下的所有任务-->
						<c:if test="${param.activityMenu=='task_m_1.6'}">
						<jsp:include page="/WEB-INF/jsp/task/listTaskFormSupView.jsp"></jsp:include>
						</c:if>
                        <!--显示下属执行任务-->
                        <c:if test="${param.activityMenu=='task_m_1.4'}">
                            <jsp:include page="/WEB-INF/jsp/task/listTaskOfSub.jsp"></jsp:include>
                        </c:if>
                        
						<!--显示待办任务-->
						<c:if test="${param.activityMenu=='task_promote_1.1'}">
						<jsp:include page="/WEB-INF/jsp/task/promote/listTaskToDo.jsp"></jsp:include>
						</c:if>
						<!--显示我负责的任务-->办理中
						<c:if test="${param.activityMenu=='task_promote_1.3'}">
						<jsp:include page="/WEB-INF/jsp/task/promote/listTaskOfMine.jsp"></jsp:include>
						</c:if>
						<!--显示我关注的所有任务-->
						<c:if test="${param.activityMenu=='task_promote_1.2'}">
							<jsp:include page="/WEB-INF/jsp/task/promote/listTaskOfAtten.jsp"></jsp:include>
						</c:if>
						<!--显示我权限下的所有任务-->
						<c:if test="${param.activityMenu=='task_promote_1.5'}">
						<jsp:include page="/WEB-INF/jsp/task/promote/listTaskOfAll.jsp"></jsp:include>
						</c:if>
                        <!--显示下属执行任务-->
                        <c:if test="${param.activityMenu=='task_promote_1.4'}">
                            <jsp:include page="/WEB-INF/jsp/task/promote/listTaskOfSub.jsp"></jsp:include>
                        </c:if>
                        
                        
						<c:if test="${not empty param.statisticsType && param.statisticsType==1}">
							<!--1 紧急度统计-->
							<jsp:include page="/WEB-INF/jsp/statistics/task/taskStatic_taskGrade.jsp"></jsp:include>
						</c:if>
						<c:if test="${not empty param.statisticsType && param.statisticsType==2}">
							<!--2、逾期统计-->
							<jsp:include page="/WEB-INF/jsp/statistics/task/taskStatic_taskOverDue.jsp"></jsp:include>
						</c:if>
						<c:if test="${not empty param.statisticsType && param.statisticsType==3}">
							<!--3、任务执行统计-->
							<jsp:include page="/WEB-INF/jsp/statistics/task/taskStatic_taskExecutor.jsp"></jsp:include>
						</c:if>
						<c:if test="${not empty param.statisticsType && param.statisticsType==4}">
							<!--4、项目任务统计-->
							<jsp:include page="/WEB-INF/jsp/statistics/task/taskStatic_taskItem.jsp"></jsp:include>
						</c:if>
						<c:if test="${not empty param.statisticsType && param.statisticsType eq '6'}">
							<!--6、项目任务详情统计-->
							<jsp:include page="/WEB-INF/jsp/statistics/task/taskStatic_taskItem_detail.jsp"></jsp:include>
						</c:if>
						<c:if test="${not empty param.statisticsType && param.statisticsType==5}">
							<!--5、项目任务统计-->
							<jsp:include page="/WEB-INF/jsp/statistics/task/taskStatic_taskCrm.jsp"></jsp:include>
						</c:if>
					</c:otherwise>
				</c:choose>
				
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

