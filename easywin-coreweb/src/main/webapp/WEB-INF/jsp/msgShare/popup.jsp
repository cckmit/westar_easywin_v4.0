<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<title><%=SystemStrConstant.TITLE_NAME %></title>
<style>
body,td,input,textarea {font-size:9pt}
</style>
<script type="text/javascript">
var URLParams = new Object() ;
var aParams = document.location.search.substr(1).split('&') ;
for (i=0 ; i < aParams.length ; i++) {
	var aParam = aParams[i].split('=') ;
	URLParams[aParam[0]] = aParam[1] ;
}

var sStyle = (URLParams["style"]) ? URLParams["style"] : "popup";

/*
var sLinkID = URLParams["link"];
var oLink = opener.document.getElementById(sLinkID);
if (!oLink){
	oLink = opener.document.getElementsByName(sLinkID)[0];
}*/

function doSave(){
	//oLink.value = document.getElementById("eWebEditor1").contentWindow.getHTML();
	art.dialog.opener.eWebEditor1.setHTML(document.getElementById("eWebEditor1").contentWindow.getHTML());
	art.dialog.close();
}
//opener.setHTML();
//alert(opener.eWebEditor1.getHTML());

function HTMLEncode(s_Html){
	if (s_Html==null){return "";}
	s_Html = s_Html.replace(/&/gi, "&amp;");
	s_Html = s_Html.replace(/\"/gi, "&quot;");
	s_Html = s_Html.replace(/</gi, "&lt;");
	s_Html = s_Html.replace(/>/gi, "&gt;");
	return s_Html;
}

</script>

</head>
<body>

<script type="text/javascript">
document.write ("<form method='post' name='myform'>");
document.write ("<input type='hidden' name='content1' value=\""+HTMLEncode(art.dialog.opener.eWebEditor1.getHTML())+"\">");
document.write ("<iframe id='eWebEditor1' src='/static/plugins/ewebeditor/ewebeditor.htm?id=content1&style=" + sStyle + "' frameborder='0' scrolling='no' width='100%' height='100%'></iframe>");
document.write ("</form>");
</script>
</body>
</html>

