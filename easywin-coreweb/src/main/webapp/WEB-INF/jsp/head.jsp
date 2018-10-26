<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.westar.base.util.RequestContextHolderUtil"%>
<%@page import="com.westar.base.cons.SessionKeyConstant"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
/*************此处用于查找当前用户的公司信息************************/
UserInfo sessionUser = new UserInfo();

Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) RequestContextHolderUtil.getSession().getAttribute(SessionKeyConstant.SESSION_CONTEXT);
Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
if (m == null) {
	m = new HashMap<String, Object>();
}
String path = "";
String sid = RequestContextHolderUtil.getRequest().getParameter("sid");
if (map != null && sid != null) {
	sessionUser =(UserInfo) m.get(sid);
	if(null == sessionUser.getLogoUuid()||"".equals(sessionUser.getLogoUuid())){
		path = "/static/2015/img/logo.png";
	}else{
		path = "/downLoad/down/"+sessionUser.getLogoUuid()+"/"+sessionUser.getLogoName()+"?sid="+sid;
	}
}
%>
<script type="text/javascript">
//问答中心检索触发开关
function qasIndexSearch(){
	if(strIsNull(document.getElementById("searchStr").value)){
		layer.alert("输入检索内容才能检索！");
		document.getElementById("searchStr").focus()
	}else{
	    var form = document.getElementById("searchForm");
	    form.submit();
	}
}
//字符串是否为空判断
function strIsNull(str){
	if(str!="" && str!="null" && str!=null && str!="undefined" && str!=undefined){
		return false;
	}else{
		return true;
	}
}

function exit(){
	window.parent.location.href='/exit?sid=${param.sid}';
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link href="/static/2015/style/comm.css" rel="stylesheet" type="text/css" />
</head>
<body scroll="no" style="overflow:hidden;">
<div class="top">
 <div class="w_width">
 <span class="fl" >
 	<a href="javascript:void(0);"  onclick="parent.location.href='/index?sid=${param.sid}';">
 	<img src="<%=path %>" width="120" height="50" style="display:inline-block;margin-top:10px;float:left;" />
 </a>
 <a href="#" class="tx">今日提醒</a></span>
 <span class="fr"><a href="#" class="td">快速新建</a>
  <form action="/index/searchIndexInCom" method="post" id="searchForm" class="searchForm" target="iframe_body_right">
		 <input type="hidden" name="redirectPage" id="redirectPage"/>
		 <input type="hidden" name="sid" value="${param.sid}"/>
		<div style="float:left;" id="navmenu">
			<ul  style="float:left;">
				<li style="float:left;height:30px;line-height:30px;position:relative;width:100px;text-align:center;padding-left: 5px">
					<input name="searchStr" id="searchStr" type="text" datatype="*" value="" class="ser_box"/>
				</li>
				<li style="float:left;height:30px;line-height:30px;position:relative;width:100px;text-align:center;padding-left:85px;margin-top:14px;">
					<input type="button" onclick="qasIndexSearch();" value="查询"/>
				</li>
			</ul>
		</div>
	</form>
		 <div style="float:left">
			 <input type="button" value="注销" onclick="exit()"/>
		 </div>
 </span>
 </div>
</div>
</body>
</html>

