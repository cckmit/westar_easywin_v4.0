<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<head>
<title>微信支付成功</title>
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="/static/js/qrcode/jquery.qrcode.min.js"></script>
<script type="text/javascript"> 
</script>
</head>
<body>
	<div class="main-container container-fluid">
		<!-- Page Container -->
		<div class="page-container">
			<div class="wechat-wrap">
				<div class="page-content submit-order">
					<div class="bg-white wechat-box">
						<div class="clearfix">
							<h4 class="no-margin">微信支付</h4>
						</div>
						<div class="wechat-bg">
							<span><img src="/static/images/tj.png" />
							</span>交易付款成功！
						</div>
						<div class="other-message-pay">请查看您购买的服务信息；确保交易成功完成。如有疑问；请致电：028-85139468</div>
						<a href="javascript:void(0);" onclick="javascript:window.close();" class="btn btn-darkorange btn-lg pay-close">关闭页面</a>
					</div>
				</div>
			</div>
		</div>


	</div>
</body>
</html>