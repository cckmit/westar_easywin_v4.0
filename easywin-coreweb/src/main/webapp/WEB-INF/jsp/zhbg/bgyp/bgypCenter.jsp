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
		<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<c:choose>
			<c:when test="${param.searchTab=='272' or param.searchTab=='273'}">
				<script type="text/javascript" src="/static/js/zhbgJs/bgPurOrderJs/bgPurOrderCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.searchTab=='274' or param.searchTab=='275'}">
				<script type="text/javascript" src="/static/js/zhbgJs/bgypApplyJs/bgypApplyCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
		</c:choose>
		<script type="text/javascript">
			var EASYWIN = {
					"sid":"${param.sid}",
					"userInfo":{
						"id":"${userInfo.id}",
						"admin":"${userInfo.admin}",
						"depId":"${userInfo.depId}"
					},
					"orgName":"${userInfo.orgName}",
					"comId":"${userInfo.comId}"
			}
			var sid = '${param.sid}';
			var cgy;
			var gly;
			$(function(){
				//是否为办公用品管理人员（审核采购和申领）
				getSelfJSON("/modAdmin/authCheckModAdmin",{sid:sid,busType:'027,02701'},function(data){
					//采购人员
					cgy = data.modAdminFlag['02701'];
					//管理员
					gly = data.modAdminFlag['027'];
					
					if(!cgy && !gly){//普通人员
						$("#bgypMenuOne").find("li[busType='271']").remove();
						$("#bgypMenuOne").find("li[busType='272']").remove();
						$("#bgypMenuOne").find("li[busType='273']").remove();
						$("#bgypMenuOne").find("li[busType='275']").remove();
					}else if(cgy && !gly){//采购人员
						$("#bgypMenuOne").find("li[busType='271']").remove();
						$("#bgypMenuOne").find("li[busType='273']").remove();
						$("#bgypMenuOne").find("li[busType='275']").remove();
						
						$("#addBgypPurOrder").removeClass("hide")
					}else if(!cgy && gly){//管理员
						$("#bgypMenuOne").find("li[busType='272']").remove();
					}else{
						$("#addBgypPurOrder").removeClass("hide")
					}
					$("#bgypMenuOne").find("li").css("display","block");
					
					if($("#addBgypPurOrder").hasClass("hide")){
						$("#addBgypPurOrder").parent().remove();
					}
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
				<jsp:include page="/WEB-INF/jsp/zhbg/bgyp/bgypCenter_left.jsp"></jsp:include>
				<c:choose>
					<c:when test="${not empty recycleTab }">
						<jsp:include page="/WEB-INF/jsp/recycleBin/listPagedPreDel.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${param.searchTab=='271'}">
								<!-- 基础维护 -->
								<jsp:include page="/WEB-INF/jsp/zhbg/bgyp/frameBgypfl.jsp"></jsp:include>
							</c:when>
							<c:when test="${param.searchTab=='272'}">
								<!-- 采购查询-->
								<jsp:include page="/WEB-INF/jsp/zhbg/bgyp/listBgypPurOrder.jsp"></jsp:include>
							</c:when>
							<c:when test="${param.searchTab=='273'}">
								<!-- 采购查询-->
								<jsp:include page="/WEB-INF/jsp/zhbg/bgyp/listSpBgypPurOrder.jsp"></jsp:include>
							</c:when>
							
							<c:when test="${param.searchTab=='274'}">
								<!-- 申领记录-->
								<jsp:include page="/WEB-INF/jsp/zhbg/bgyp/listBgypApply.jsp"></jsp:include>
							</c:when>
							<c:when test="${param.searchTab=='275'}">
								<!-- 申领记录-->
								<jsp:include page="/WEB-INF/jsp/zhbg/bgyp/listSpBgypApply.jsp"></jsp:include>
							</c:when>
							<c:when test="${param.searchTab=='276'}">
								<!-- 库存信息 -->
								<jsp:include page="/WEB-INF/jsp/zhbg/bgyp/listBgypStore.jsp"></jsp:include>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
				
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

