<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	var valid;
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	var type = '${param.moduleType}';
	$(function() {
		valid = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
	})
	function formSub(){
		$(".subform").submit();
	}
</script>
<style type="text/css">
body:before {
	background-color: #fff;
	z-index: 0
}
</style>
</head>
<body>
	<div class="container no-padding" style="width: 100%">
		<div class="widget" style="border-bottom: 0px">
			<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
				<span class="widget-caption themeprimary ps-layerTitle">周报汇报时间设置</span>
				<div class="widget-buttons">
					<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
						<i class="fa fa-times themeprimary"></i>
					</a>
				</div>
			</div>
			<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
				<form class="subform" method="post" action="/weekReport/updateSubTimeSet">
					<tags:token></tags:token>
					<div class="widget-header bg-bluegray no-shadow" style="clear:both">
						<span class="widget-caption blue">汇报截止时间：</span>
						<div class="widget-buttons btn-div-full"></div>
					</div>

					<div class="ws-introduce2 padding-20 ">
						<tags:radioDataDic type="subTimeType" name="timeType" style="ws-radio" value="${not empty subTimeSet.timeType ? subTimeSet.timeType : -1}"></tags:radioDataDic>
						<div class="ws-clear"></div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
