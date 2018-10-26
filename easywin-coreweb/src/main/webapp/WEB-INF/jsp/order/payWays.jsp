<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
	$(function(){
		$("[name='payWayLi']").click(function(){
			$("[name='payWay']").removeAttr("checked");
			$("[name='moneySpan']").css("display","none");
			$("[name='payWayLi']").removeClass("current-payment");
			$(this).addClass("current-payment");
			$(this).find("[name='payWay']").attr("checked","checked");
			$(this).find("[name='moneySpan']").css("display","block");
		});
		
	});
</script>
<body>
    <!-- Main Container -->
    <div class="main-container container-fluid">
       <!-- Page Container -->
       <div class="page-container">
		<!-- Page Body -->
		<!-- 提交订单S -->
		<div class="pay-box">
			<div class="page-content submit-order">
				<div class="clearfix">
					<div class="pull-left">
						<div class="pull-left padding-top-30 padding-right-20">
							<img src="/static/images/tj.png" />
						</div>
						<div class="pull-left padding-top-20">
							<span style="font-size:28px;">订单提交成功，请您尽快付款！</span>
							<p class="gray-9">请您在提交订单后7个自然日内完成付款，否则订单会自动取消</p>
						</div>
					</div>
					<c:choose>
						<c:when test="${order.orgBalanceMoney>0}">
							<div class="pull-right payableV2">
								<div>订单金额：<span class="orange">${order.orderCost}</span>元</div>
								<div>团队结余：<a href="javascript:void(0);" onclick="organicSpaceCfgInfo('${param.sid}')"><span class="orange">${order.orgBalanceMoney}</span>元</a></div>
								<div>应付金额：<span class="orange">${order.orderCost-order.orgBalanceMoney}</span>元</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="pull-right payable padding-top-30">
								<div>应付金额：<span class="orange">${order.orderCost}</span>元</div>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
				<form action="/order/toPay" method="post" class="subform" id="payForm">
				<tags:token></tags:token>
				<div class="bg-white payment-box">
					<ul class="item-box no-padding">
						<li class="item current-payment sum-position" name="payWayLi">
							<div class="radio">
								<label> 
									<input class="colored-blue" name="payWay" type="radio" checked="checked" value="<%=ConstantInterface.ORDER_PAIDWAY_ALIPAY%>"> 
									<span class="text">支付宝支付</span> 
								</label>
							</div> 
							<span class="sum" name="moneySpan">支付：<span class="orange">${order.orderCost-order.orgBalanceMoney}</span>元</span>
						</li>
						<li class="item sum-position" name="payWayLi">
							<div class="radio">
								<label> 
									<input name="payWay" type="radio" value="<%=ConstantInterface.ORDER_PAIDWAY_WECHAT%>"> 
									<span class="text">微信支付</span> 
								</label>
							</div>
							<span class="sum" name="moneySpan" style="display:none;">支付：<span class="orange">${order.orderCost-order.orgBalanceMoney}</span>元</span>
						</li>
					</ul>
					<div class="panel-body">
						<input type="buttion" class="btn btn-info btn-lg" value="确认并支付" onclick="toPay(${order.id},'${param.sid}');"/>
					</div>
				</div>
				</form>
				<!-- 提交订单E -->
			</div>
		</div>
		</div>
	</div>
	<!-- /Page Container -->
</body>
</html>