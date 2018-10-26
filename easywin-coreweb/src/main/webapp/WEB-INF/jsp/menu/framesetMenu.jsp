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
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
</head>
<body style="margin-left:20px;">
<div class="tit"><span>当前位置：系统管理>>菜单维护</span></div>
<div class="block01">
  <div class="oper_btn">
  <span>
  <a href="javascript:void(0)" onclick="add()" class="add">新增</a>
  <a href="javascript:void(0)" onclick="enabled()" class="eye">启用</a>
  <a href="javascript:void(0)" onclick="disEnabled()" class="trash">禁用</a> 
  <a href="javascript:void(0)" onclick="del()" class="del">删除</a>
  </span>
  </div>
  <div id="container" style="width: 100%;margin-left: auto;margin-right: auto;display: none" align="center" > 
	<div id="left" style="width: 20%;float: left;border-right: #999999 1px dashed">
		<iframe id="leftFrame" name="leftFrame" src="/menu/treeMenuPage?businessType=${param.businessType }&sid=${param.sid }" style="width: 100%;height: 479px;" frameborder="0" scrolling="auto"></iframe>
	</div>
	<div id="right" style="width: 79%;float: left;">
		<iframe id="rightFrame" name="rightFrame" src="/blank?sid=${param.sid }" style="width: 100%;height: 100%;" frameborder="0" scrolling="no"></iframe>
	</div>
  </div>
</div>
</body>
<script type="text/javascript">
function add(){
	leftFrame.add();
}

function enabled(){
	leftFrame.enabled();
}

function disEnabled(){
	leftFrame.disEnabled();
}

function del(){
	leftFrame.del();
}


$(document).ready(function() {
	var h = document.documentElement.clientHeight;
    $('#left').css("height",510);
    $('#right').css("height",510);
    $('#container').show();
    var obj = window.parent.document.getElementById("iframe_body_right");  //取得父页面IFrame对象
	obj.height = 570;  //调整父页面中IFrame的高度为此页面的高度  
});

var resizeTimer = null; 
$(window).resize(function () { 
  if (resizeTimer) clearTimeout(resizeTimer); 
   var h = document.documentElement.clientHeight;
    $('#left').css("height",h-100);
    $('#right').css("height",h-100);
    $('#container').show();
});

</script>
</html>
