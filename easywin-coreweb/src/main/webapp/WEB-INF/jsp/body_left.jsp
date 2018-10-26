<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link href="/static/2015/style/comm.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function gotourl(url,target){
	if(target=="_blank"){
    	window.open(addSid(url,'${param.sid}'));
    }else{
    	parent.frames['iframe_body_right'].location.href=addSid(url,'${param.sid}');
    }
}
</script> 
</head>
<body scroll="no" style="overflow:hidden;">
	<div class="wrap_l fl">
	    <ul class="menu_l">
	    <li><a href="javascript:gotourl('/crm/customerListPage?pager.pageSize=10','center')" class="ico01">客户中心</a></li>
	    <li><a href="javascript:gotourl('/item/listItemPage?pager.pageSize=10','center')" class="ico02">项目中心</a></li>
	    <li><a href="javascript:gotourl('/task/taskListPage?pager.pageSize=10','center')" class="ico01">任务中心</a></li>
	    <li><a href="javascript:gotourl('/weekReport/listPagedWeekRep?pager.pageSize=10','center')" class="ico04">周报中心</a></li>
		<li><a href="javascript:gotourl('/daily/listPagedDaily?pager.pageSize=12','center')" class="ico06">分享中心</a></li>
	    <li><a href="javascript:gotourl('/qas/listPagedQas?pager.pageSize=10','center')" class="ico05">知识管理</a></li>
	    <!-- 
	    <li><a href="javascript:gotourl('/vote/listPagedVote?pager.pageSize=5','center')" class="ico06">投票中心</a></li>
	     -->
	    <li><a href="javascript:gotourl('/userInfo/listPagedUserInfo?pager.pageSize=10','center')" class="ico01">人员维护</a></li>
	    <li><a href="javascript:gotourl('/menu/framesetMenuPage?businessType=6','center')" class="ico03">菜单管理</a></li>
	    <li><a href="javascript:gotourl('/department/framesetDepPage','center')" class="ico01">部门维护</a></li>
	    <li><a href="javascript:gotourl('/organic/organicInfo','center')" class="ico01">团队信息</a></li>
	    <li><a href="javascript:gotourl('/jiFen/listJifenConfigPage','center')" class="ico06">积分设置</a></li>
	   </ul>
	<div class="add_apl rad"><a href="#">应用添加</a></div>
	</div>
</body>
</html>

