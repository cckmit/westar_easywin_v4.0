<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="UTF-8">
	<title>支付弹出框</title>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<script type="text/javascript" charset="utf-8"
		src="/static/js/order_center/order_center.js"></script>
    <script type="text/javascript">
	    //关闭窗口
	    function closeWin(){
	    	var winIndex = window.top.layer.getFrameIndex(window.name);
	    	closeWindow(winIndex);
	    }
    </script>
</head>
<body>
	<div class="modal-dialog" style="margin:0;">
		<div class="modal-content">
			<div class="modal-header bg-sky-blue">
				<button class="bootbox-close-button close" type="button" data-dismiss="modal" aria-hidden="true" onclick="closeWin();">×</button>
				<h4 class="modal-title">支付</h4>
			</div>
			<div class="modal-body">
				<p>支付完成前请不要关闭此窗口！</p>
				<a href="#">付款遇到问题</a>
			</div>
			<div class="modal-footer">
				<a href="javaScript:void(0);" onclick="closeWin();" class="gray-9 padding-right-20"  data-dismiss="modal">重新支付</a>
				<button class="btn btn-info" type="button" onclick="paidCallBackRefeshOrders('${param.sid}');">我已完成付款</button>
			</div>
		</div>
	</div>
</body>
</html>