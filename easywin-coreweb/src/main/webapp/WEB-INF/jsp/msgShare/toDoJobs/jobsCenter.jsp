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
		<script type="text/javascript" src="/static/js/jobsCenterJs/jobsCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript">
		
		$(function(){
			var modTodoList = eval('${modTodoList}');
			if(modTodoList){
				var allNoReadNums = 0;
				var spNoReadNum = 0;
				for(var i=0;i<modTodoList.length;i++){
					var busType = modTodoList[i].busType;
					busType = busType=='1'?'100':busType;
					var noReadNum = modTodoList[i].noReadNum;
					allNoReadNums +=noReadNum;
					
					if(busType=='022'||busType=='02201'){//审批未读
						busType = '022'
					}else if(busType=='027010'||busType=='027011'){//采购未读
						busType = '027010'
					}else if(busType=='027020'||busType=='027021'){//申领未读
						busType = '027020'
					}else if(busType=='017'||busType=='046'){//会议审批
						busType = '017'
					}
					
					var noReadNumT = $("#noReadNumT_"+busType).data("noReadNum");
					if(!noReadNumT){
						noReadNumT = 0;
					}
					noReadNum = noReadNumT+noReadNum;
					
					
					setNoReadNum("noReadNumT_"+busType,noReadNum);
					$("#noReadNumT_"+busType).data("noReadNum",noReadNum);
				}
				setNoReadNum("allNoReadNumT",allNoReadNums);
				$("#allNoReadNumT").data("noReadNum",allNoReadNums);
			}
		});
			//session标识
			var sid = '${param.sid}';
			//默认显示界面标识
			var pageTag = 'allTodo';
			//默认当前第1页
			var nowPageNum = 0;
			//默认总待办数为0
			var countTodo='${countTodo}';
			var pageLoc = location.search.match(/pager.offset=(\d+)/);
			if(pageLoc){
				//计算当前页数
				nowPageNum = pageLoc[1]/10;
			}
			var sessionUserId = '${userInfo.id}';
		</script>
	</head>
	<body>
		<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="jobsCenter_left.jsp"></jsp:include>
				<!--显示待办任务-->
				<jsp:include page="listJobTodo.jsp"></jsp:include>
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

