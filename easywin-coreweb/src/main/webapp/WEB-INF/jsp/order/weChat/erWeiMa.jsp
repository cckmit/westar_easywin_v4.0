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
    <title>微信扫码支付</title>
	<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="/static/js/qrcode/jquery.qrcode.min.js"></script>
    <script type="text/javascript"> 
    function generateQRCode(rendermethod, picwidth, picheight, url) {
        $("#qrcode").qrcode({ 
            render: rendermethod, // 渲染方式有table方式（IE兼容）和canvas方式
            width: picwidth, //宽度 
            height:picheight, //高度 
            text: utf16to8(url), //内容 
            typeNumber:-1,//计算模式
            correctLevel:2,//二维码纠错级别
            background:"#ffffff",//背景颜色
            foreground:"#000000"  //二维码颜色

        });
    }
    function init() {
        generateQRCode("table",300,300,"${appLoadUrl}");
    }
    //中文编码格式转换
    function utf16to8(str) {
        var out, i, len, c;
        out = "";
        len = str.length;
        for (i = 0; i < len; i++) {
            c = str.charCodeAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                out += str.charAt(i);
            } else if (c > 0x07FF) {
                out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
                out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
            } else {
                out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
                out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
            }
        }
        return out;
    }   

  	//定时器 异步运行 
    function orderStatusCheck(){ 
    	$.post("/order/orderStatusCheck?sid=${param.sid}",{Action:"post",orderTradeNo:"${order.orderTradeNo}",},     
			function (order){
    			if(order.status==1){
    				window.clearInterval(timerTask);
    				window.location.href="/order/weChatPaidOk?sid=${param.sid}";
    			}
		},"json"); 
    } 
    var timerTask = window.setInterval("orderStatusCheck()",1000);//定时检查订单支付状态
    </script>
</head>
<body onLoad="init()">
    <!-- Main Container -->
    <div class="main-container container-fluid">
           <div class="wechat-wrap">
           	<div class="pay-header clearfix">
		    		<div class="pull-left">
		    			<img src="/static/images/pay-logo.png"/>
		    		</div>
		    		<div class="pull-right">
		    			<%--<ul class="help-list clearfix">
		    				<li><a href="#">彭小鱼</a><a href="#">退出</a></li>
		    				<li><a href="">我的订单</a></li>
		    				<li><a href="">支付帮助</a></li>
		    				<li><a href="#">问题反馈</a></li>
		    			</ul>
		    		--%>
		    		</div>
		    	</div>
           	<div class="page-content submit-order"> 
               <div class="clearfix">
               	<div class="pull-left">
               		<h4>订单提交成功，请您尽快付款！订单号：${order.orderTradeNo}</h4>
               	</div>
               	<div class="pull-right payable">应付金额：<span class="orange">${order.orderCost-order.orgBalanceMoney}</span>元</div>
               </div>
               <div class="bg-white wechat-box">
               	<div class="clearfix">
               		<h4 class="no-margin pull-left">微信支付</h4>
               		<%--<div class="pull-left red-notes">
               			二维码已过期，<a href="#">刷新</a>页面重新获取二维码。
               		</div>
               	--%>
               	</div>
               	<div class="wechat-content clearfix">
               		<div class="wechat-ma pull-left">
               			<div id="qrcode"></div>
               			<div class="pay-notes clearfix">
               				<div class="pull-left">
               					<p>请使用微信扫一扫</p>
               					<p>扫描二维码支付</p>
               				</div>
               			</div>
               		</div>
               		<div class="phone-bg pull-left">
               			<img src="/static/images/phone-bg.png"/>
               		</div>
               	</div>
               	<%--<div class="panel-body">
               		<a href="#"><i class="fa fa-angle-left padding-right-5 fa-lg"></i>选择其他付款方式</a>
               	</div>
               	
               --%>
               </div>
           </div>
           <div class="pay-foot">
           	<p>成都西辰软件版权所有</p>
           </div>
       </div>
   </div>
</body>
</html>