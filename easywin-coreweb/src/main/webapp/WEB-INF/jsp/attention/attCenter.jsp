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
		<script type="text/javascript" src="/static/js/attCenterJs/attCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript">
		var sid='${param.sid }';
		var pageTag = 'attrCent';
		
		$(function(){
			var modTodoList = eval('${modTodoList}');
			if(modTodoList){
				var allNoReadNums = 0;
				for(var i=0;i<modTodoList.length;i++){
					var busType = modTodoList[i].busType;
					var noReadNum = modTodoList[i].noReadNum;
					allNoReadNums +=noReadNum;
					if(busType=='003'){//任务未读
						setNoReadNum("taskNoReadNumT",noReadNum);
					}else if(busType=='012'){//客户未读
						setNoReadNum("crmNoReadNumT",noReadNum);
					}else if(busType=='005'){//项目未读
						setNoReadNum("itemNoReadNumT",noReadNum);
					}else if(busType=='011'){//问答未读
						setNoReadNum("qasNoReadNumT",noReadNum);
					}else if(busType=='004'){//投票未读
						setNoReadNum("voteNoReadNumT",noReadNum);
					}else if(busType=='022'){//审批消息
						setNoReadNum("spNoReadNumT",noReadNum);
					}else if(busType=='1'){//分享消息
						setNoReadNum("shareNoReadNumT",noReadNum);
					}
				}
				if(allNoReadNums > 0){
					$("#allNoReadNumT").show();
					$("#allNoReadNumT").html(allNoReadNums);
				}else{
					$("#allNoReadNumT").hide();
				}
			}
			
		})
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
				<jsp:include page="attCenter_left.jsp"></jsp:include>
				<!--显示待办任务-->
				<jsp:include page="listpagedAtt.jsp"></jsp:include>
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

