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
<html>
  <head>
    <meta charset="utf-8" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	    
	    <jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/easywin.ui.min.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/font-icons.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/colpick.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-select.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-view.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-typeahead.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/org.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/plugins.min.css" />
		<script src="/static/plugins/form/js/libs.js"></script>
		<script src="/static/plugins/form/js/plugins.js"></script>
		<script type="text/javascript" src="/static/plugins/form/js/colpick.js"></script>
  </head>
  <style type="text/css">
input[type="radio"],input[type="checkbox"] {
    top: 3px !important;
    left:1px !important;
    opacity: 1 !important;
    width: 15px !important;
    height: 15px !important;
}
.webuploader-pick{
	height: 30px
}
</style>
  <body class="edit edit-form">
	<div class="form-preview-wrapper">
		<div id="form-preview" class="form-preview clearfix">
			<div class="form-view">
				<div class="form-head">
					<p class="form-name">${formMod.modName }</p>
					<div class="form-description">${formMod.modDescrib}</div>
					<a class="btn btn-warning btn-sm back-to-edit" onclick='window.self.close()'>
					<i class="glyphicon icon-undo"></i>关闭</a>
				</div>
				<div id="formpreview" class="widget-control form-view_js">
					
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	var sid='${param.sid}'
	var formTag = 'viewMod';
	var formKey='${formMod.id}';
	var TEAMS={
			"currentUser":{"userId":"${userInfo.id}",
				"id":"${userInfo.id}",
				"username":"${userInfo.userName}",
				"comId":"${userInfo.comId}",
				"department":{"name":"${userInfo.depName}","id":"${userInfo.depId}"}
			},
			"currentTenant":{"tenantKey":"t5y8pa3o0l"}
	}
	</script>
	<script type="text/javascript">
			seajs.config({base:"/static/plugins/form/js",preload:["workFlowForm.js?v=88f3d7c56c32a31026b3f37492c9287f"],charset:"utf-8",debug:!1}),seajs.use("easywin/form/viewFormMod");  
	</script>
  </body>
</html>
