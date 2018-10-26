<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>捷成协同办公平台</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="协同  OA 云平台">
<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->
<script type="text/javascript">
	function helpClassify(id,obj){
		$("[name='classType']").each(function(){
			$(this).removeAttr("class");
        });
		$(obj).find("li").attr("class","active");
		$("#helpAns").attr("src","/web/helpQA?pId="+id);
	}
	$(function(){
		//帮助筛选
		$("#nameCheck").blur(function(){
			if(!strIsNull($("#nameCheck").val())){
				$("#helpAns").attr("src","/web/helpQASreach?nameCheck="+$("#nameCheck").val());
        	}
		});
		//文本框绑定回车提交事件
		$("#nameCheck").bind("keydown",function(event){
	        if(event.keyCode == "13")    
	        {
	        	if(!strIsNull($("#nameCheck").val())){
					$("#helpAns").attr("src","/web/helpQASreach?nameCheck="+$("#nameCheck").val());
	        	}else{
	        		layer.msg("请输入检索内容！", {icon:2})
	    			$("#nameCheck").focus();
	        	}
	        }
	    });
		//按钮收索事件绑定
		$("#nameCheck_a").click(function(){
			if(!strIsNull($("#nameCheck").val())){
				$("#helpAns").attr("src","/web/helpQASreach?nameCheck="+$("#nameCheck").val());
        	}else{
        		layer.msg("请输入检索内容！", {icon:2});
    			$("#nameCheck").focus();
    			return;
        	}
		});
	});
</script>
</head>
<body>
	<!----头部开始---->
	<div class="top">
		<jsp:include page="head.jsp"></jsp:include>
	</div>
	<!----头部结束---->
	<!----搜索 S---->
    <div class="search-box">
    	<div class="search-in">
    		<input type="text" id="nameCheck" value="" placeholder='请输入关键字，如"团队"'/>
    		<a href="javascript:void(0);" id="nameCheck_a" class="search-btn">搜索</a>
    	</div>
    </div>
    <!----搜索 E---->
	<!--常见问题 S-->
	<div class="FAQ-box">
		<div class="pull-left left-menu">
			<h3>帮助分类</h3>
			<ul>
				<c:choose>
					<c:when test="${not empty typeList}">
						<c:forEach items="${typeList}" var="vo" varStatus="vs">
							<a href="javascript:void(0);" onclick="helpClassify(${vo.id},this);">
							<li name="classType">${vo.name}</li>
							</a>
						</c:forEach>
					</c:when>
				</c:choose>
				<!-- <li class="active"><a href="#">新手行动指南</a></li> -->
			</ul>
		</div>
		<div class="pull-left help-content-right">
			<iframe id="helpAns" class="layui-layer-load"
				src="/web/helpDescrible" border="0" frameborder="0"
				allowTransparency="true" noResize scrolling="no" width=100%
				height=100% vspale="0"></iframe>
		</div>
	</div>

	<div class="bottom">
		<jsp:include page="bottom.jsp"></jsp:include>
	</div>
</body>
</html>