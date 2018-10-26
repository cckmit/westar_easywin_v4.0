<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<title>登陆跳转</title>
<script type="text/javascript" src="/static/js/cookieInfo.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">

//注册填完信息，不保存此次到本地
function saveInfo(){
	var account = '${loginName}';
	sessionStorage.setItem("a_i_p_m_l_c",account); 
	var url = "/userInfo/addInfoPage?sid=${sid}&comId=${userInfo.comId}&id=${userInfo.id}&page=regist";
	layer.open({
		id:'layerConfig',
		//title:'个人配置',
		title:false,
		closeBtn:0,
		type: 2,
		shadeClose: true,
		shade: 0.1,
		shift:0,
		zIndex:299,
		fix: true, //固定
		maxmin: false,
		move: false,
		border:1,
		area: ['600px','460px'],
		content: [url,'no'], //iframe的url
		btn:['关闭'],
		yes:function(index, layero){
			layer.close(index);
		},success: function(layero,index){
			var iframeWin = window[layero.find('iframe')[0]['name']];
			//iframeWin.setWindow(window.document,window,data.list,index);
			 $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				layer.close(index);
		     })
		},end:function(index){
			window.location.href="/index?sid=${sid}";
		}
	});
	
	
}
</script>
</head>
<body onload="saveInfo()">
</body>
</html>

