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
<link rel="stylesheet" href="/static/plugins/layui/css/layui.css">
<!--Basic Styles-->
<link href="/static/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">

 <!--Beyond styles-->
<link href="/static/assets/css/beyond.min.css" rel="stylesheet"  type="text/css">

<link href="/static/assets/css/new_file.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/plugins/layui/layui.js"></script>
<style>
    body{overflow-x: hidden}
   .ps-pageText{display: none}
</style>
</head>
<body  style="background-color:#FFFFFF;overflow-y:hidden">
<div class="bg-white"style="position: fixed;width: 100%;z-index: 999999;height: 30px">
	<div class="ps-margin ps-search bg-white no-margin" style="position: fixed;right: 0;">
		<span class="input-icon">
		<form action="/chat/listPagedChatHistory" id="searchForm">
			<input type="hidden" name="sid" value="${param.sid}">
			<input type="hidden" name="comId" value="${chatsMsg.comId}">
			<input type="hidden" name="fromUser" value="${param.fromUser}">
			<input type="hidden" name="toUser" value="${param.toUser }">
			<input type="hidden" name="type" value="${param.type}">
			<input id="content" name="content" value="${chatsMsg.content}"
				class="form-control ps-input" type="text"
				placeholder="请输入关键字"> <a href="javascript:void(0)" class="ps-searchBtn" onclick="$('#searchForm').submit()">
				<i class="glyphicon glyphicon-search circular danger"></i> 
			</a> 
		</form>
		</span>
	</div>
</div>
<script>
    layui.use(['layim'], function(){
        var layim = layui.layim
        var data = JSON.parse('${history}')
        var html = '';
        for(var key in data){
            var item = data[key];
            var sendtime = item.timestamp;
            html += '<li><div class="layim-chat-user"><img src="'+item.avatar+'"><cite>'+item.username+'<i>'+sendtime+'</i></cite></div><div class="layim-chat-text">'+layim.content(item.content)+'</div></li>';
        }
        $(".layim-chat-main ul").append(html);
    });
</script>

<div class="layim-chat layim-chat-friend ps-clear" style="padding-top: 30px">
    <div class="layim-chat-main" style="width:70%; height:100%">
        <ul>

        </ul>
    </div>
    <div style="width: 70%">
    <tags:pageBar url="/chat/listPagedChatHistory"></tags:pageBar>
    </div>
    
</div>
    
<%--用与测量当前页面的高度 --%>
</body>
</html>