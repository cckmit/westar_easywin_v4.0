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
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	<style>
		body:before{
			background-color: #FBFBFB;
		}	
	</style>
<script type="text/javascript">
	$(function() {
		resizeVoteH('rightFrame')
		parent.resizeVoteH('${functionList.iframe}')
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		$("#save").click(function(){
			$(".subform").submit();
		});
		
		//文本框
		$("#functionDescribe").click(function() {
			resizeVoteH('rightFrame')
			parent.resizeVoteH('${functionList.iframe}')
        })
		
	})

	
</script>
</head>
<body onload="resizeVoteH('rightFrame')">
	<div class="widget radius-bordered no-border" style="cursor: default;">
		<form class="subform" method="post" action="/functionList/addFunction">
		<input type="hidden" name="parentId" value="${functionList.parentId }"/>
		<input type="hidden" name="sid" value="${param.sid }"/>
		<input type="hidden" name="busType" value="${functionList.busType }"/>
		<input type="hidden" name="busId" value="${functionList.busId }"/>
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">新增功能</span>
			<button class="btn btn-info btn-primary btn-xs" type="button" id="save" style="float: right;margin-top: 5px;margin-right: 10px;">保存</button>
		 </div>
		<div class="widget-body no-shadow">
				<ul class="tickets-list">
					<li class="ticket-item no-shadow ps-listline no-border">
						<div class="pull-left gray ps-left-text">
							&nbsp;上级目录
						</div>
						<div class="ticket-user pull-left ps-right-box no-border" style="border-bottom: 0px !important;">
								<div id="busName" class="no-border" style="border-bottom: 0px !important;">${functionList.busName }</div>
						</div>
					  </li>
					 <li class="ticket-item no-shadow ps-listline no-border">
						<div class="pull-left gray ps-left-text">
							&nbsp;功能名
							<span style="color: red">*</span>
						</div>
						<div class="pull-left gray ps-left-text">
								<input id="functionName" datatype="*" name="functionName" 
								class="colorpicker-default form-control" type="text" value=""
								style="width: 270px;float: left">
						</div>
					  </li>
					  <li class="ticket-item no-shadow autoHeight no-padding ">
				    	<div class="pull-left gray ps-left-text padding-top-10">
				    		&nbsp;功能备注
				    	</div>
						<div class="pull-left gray ps-left-text padding-top-10" style="width: 270px;height: auto;">
					  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;margin-left: 0" id="functionDescribe" name="functionDescribe" rows="" cols=""></textarea>
						</div> 
						<div class="ps-clear"></div> 
                    </li>
                    <li class="ticket-item no-shadow autoHeight no-padding" >
				    	<div class="pull-left gray ps-left-text padding-top-10">
				    		 <div class="ps-clear"></div> 
				    	</div>
                    </li>
                   
				</ul>
		</div>
		</form>	
	</div>

<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>

</body>
</html>
