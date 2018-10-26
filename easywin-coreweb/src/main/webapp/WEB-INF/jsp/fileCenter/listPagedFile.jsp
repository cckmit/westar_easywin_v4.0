<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/fileJs/fileCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">

var pageTag = 'fileCenter';

function formSub(){
	$("#fileSearchForm").find("input[name='startDate']").val($("#startDate").val());
	$("#fileSearchForm").find("input[name='endDate']").val($("#endDate").val());
	$("#fileSearchForm").submit();
}
$(function(){
	//更多筛选条件显示层
	$("#moreFilterCondition").click(function(){
        var display = $("#moreFilterCondition_div").css("display");
        if("none"==display){
            $(this).html('隐藏');
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }else if("block"==display){
            $(this).html('更多筛选')
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }

      });
	//任务名筛选
	$("#fileName").blur(function(){
		formSub();
	});
	//文本框绑定回车提交事件
	$("#fileName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	formSub();
        }
    });
});

//查询模块
function searchFileTab(busType){
 	//启动加载页面效果
 	layer.load(0, {shade: [0.6,"#fff"]});
	$("#modTypes").val(busType);
	$("#fileSearchForm").submit();
}
//查询文档类型
function setFileTypes(fileTypes){
	$("#fileTypes").val(fileTypes+'');
	$("#fileSearchForm").submit();
}
</script>
<style>
.btn5{
	padding:6px 7px;
}
</style>
</head>
<body  onload="setStyle();">
	<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="/WEB-INF/jsp/fileCenter/listFile_left.jsp"></jsp:include>
				<!-- 列表-->
				<jsp:include page="/WEB-INF/jsp/fileCenter/listFile_middle.jsp"></jsp:include>
	        </div>
	   		 <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
</body>
</html>
