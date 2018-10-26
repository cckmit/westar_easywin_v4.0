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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript">
		var pageTag = 'org';
		var sid='${param.sid}'
		var PageData={"sid":"${param.sid}",
				"attenceRule":{"attenceRuleId":"${attenceRule.id}",
					"isSystem":"${attenceRule.isSystem}"},
				"tab":"${param.tab}"};
	</script>
<c:choose>
	<c:when test="${param.tab==11 }">
		<script type="text/javascript" src="/static/js/orgJs/org.js"></script>
	</c:when>
	<c:when test="${param.tab==12}">
	</c:when>
	<c:when test="${param.tab==13 || param.tab == 18}">
	</c:when>
	<c:when test="${param.tab==14}">
	</c:when>
	<c:when test="${param.tab==15}">
		<script type="text/javascript" src="/static/js/orgJs/invUser.js"></script>
	</c:when>
	<c:when test="${param.tab==16}">
	</c:when>
	<c:when test="${param.tab==17}">
		<script type="text/javascript" src="/static/js/orgJs/sysLog.js"></script>
	</c:when>
</c:choose>
</head>
<body>
<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="/WEB-INF/jsp/organic/organic_left.jsp"></jsp:include>
				<c:choose>
					<c:when test="${param.tab==11 }">
						<!-- 企业信息 -->
						<jsp:include page="/WEB-INF/jsp/organic/organicInfo.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==12}">
						<!-- 团队部门 -->
						<jsp:include page="/WEB-INF/jsp/department/framesetDep.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==13}">
						<!-- 团队成员 -->
						<jsp:include page="/WEB-INF/jsp/userInfo/listPagedUserInfo.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==18}">
						<!-- 团队角色 -->
						<jsp:include page="/WEB-INF/jsp/role/listPagedRole.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==14}">
						<!-- 申请审核 -->
						<jsp:include page="/WEB-INF/jsp/userInfo/listPagedForCheck.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==15}">
						<!-- 人员邀请-->
						<jsp:include page="/WEB-INF/jsp/userInfo/inviteUser.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==16}">
						<!-- 团队管治 -->
						<jsp:include page="/WEB-INF/jsp/organic/organicManage.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==17}">
						<!-- 系统日志 -->
						<jsp:include page="/WEB-INF/jsp/systemLog/listPagedOrgSysLog.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==21 }">
						<!-- 考勤设置 -->
						<jsp:include page="/WEB-INF/jsp/attence/viewAttenceSet.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.tab==22 }">
						<!-- 考勤设置 -->
						<jsp:include page="/WEB-INF/jsp/festMod/listFestMod.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='sul_m_1.1'}">
						<!-- 升级日志 -->
						<jsp:include page="/WEB-INF/jsp/sysUpgradeLog/listSysUpLog.jsp"></jsp:include>
					</c:when>
				</c:choose>
				
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>


</body>
</html>
