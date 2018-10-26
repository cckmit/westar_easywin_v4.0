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
		initOrder();//根据默认参数计算
		$("#usersNum").blur(function(){//使用人数失效事件
			if($("#usersNum").val()<20){
				$("#usersNum").val(20);
			}else if($("#usersNum").val()>=500){
				window.top.layer.msg("你的团队规模这么大，请直接联系销售人员！",{icon:6,skin:"showNotification"});
			}else{
				var usersNum = $("#usersNum").val();
				usersNum = (usersNum-(usersNum%10))+((usersNum%10)==0?0:10);
				$("#usersNum").val(usersNum);
				initOrder();//人数变更后，重新计算
			}
		});
		$("#agreedEasyWinRuleCheckBox").click(function(){//条约点击事件
			if($("#agreedEasyWinRuleCheckBox").prop("checked")){
				$("#submitButton").removeAttr("disabled");
				$("#submitButton").removeClass("pale-btn");
				$("#submitButton").addClass("shiny-btn");
			}else{
				$("#submitButton").attr("disabled","disabled");
				$("#submitButton").removeClass("shiny-btn");
				$("#submitButton").addClass("pale-btn");
			}
		});
		$("#usersNumReduce").click(function(){//减少10
			var usersNum = parseInt($("#usersNum").val());
			if(usersNum>20){
				usersNum = usersNum - 10;
			}
			$("#usersNum").val(usersNum);
			initOrder();//使用年数变更后，重新计算
		});
		$("#usersNumIncrease").click(function(){//增加10
			var usersNum = parseInt($("#usersNum").val());
			if(usersNum>=500){
				window.top.layer.msg("你的团队规模这么大，请直接联系销售人员！",{icon:6,skin:"showNotification"});
			}else{
				usersNum = usersNum + 10;
				$("#usersNum").val(usersNum);
				initOrder();//使用年数变更后，重新计算
			}
		});
		$("[name='discountStandardDiv']").click(function(){//折扣优惠事件
			$("[name='discountStandardDiv']").removeClass("current-choice");
			$(this).addClass("current-choice");
		    $("#orderForm [name='discountStandardId']").val($(this).find("[name='discountStandardIdForSelect']").val());
		    $("#orderForm [name='discountStandardId']").attr("years",$(this).find("[name='discountStandardIdForSelect']").attr("years"));
		    initOrder();//使用年数变更后，重新计算
		});
	});
</script>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="widget padding-10 bg-white no-margin-bottom">
				<div class="widget-header lined">
					<span class="widget-caption blue-title">收费标准</span>
				</div>
				<div class="widget-body no-border0">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>使用人数</th>
								<th>价格</th>
								<th>存储空间</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty chargingStandards}">
									<c:forEach items="${chargingStandards}" var="obj">
										<c:if test="${obj.chargingStandard <= 500 }">
											<tr>
												<td><=${obj.chargingStandard}人</td>
												<td><span class="orange">￥${obj.price}</span>/人/年</td>
												<td>${obj.storageSpace}G/人/年</td>
											</tr>
										</c:if>
										<c:if test="${obj.chargingStandard > 500 }">
											<tr>
												<td>>500人</td>
												<td colspan="2"><a href="javascript:void(0);" class="blue" onclick="javascript:alert('弹出我们销售联系方式');">联系我们</a></td>
											</tr>
										</c:if>
									</c:forEach>
								</c:when>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
			<!--选购服务 S-->
			<form action="/order/addOrder" method="post" class="subform" id="orderForm">
			<tags:token></tags:token>
			<input type="hidden" name="sid" value="${param.sid}">
			<input type="hidden" name="orderType" value="<%=ConstantInterface.ORDER_ORDERTYPE_ORGUPGRADE%>">
			<div class="widget padding-10 bg-white no-margin-bottom">
				<div class="widget-header lined">
					<span class="widget-caption blue-title">选购服务</span>
				</div>
				<div class="widget-body no-border0">
					<div class="form-group clearfix">
						<label class="pull-left use-number gray-9">使用人数</label>
						<div
							class="spinner spinner-horizontal spinner-two-sided pull-left">
							<div class="spinner-buttons	btn-group spinner-buttons-left">
								<button type="button" class="btn spinner-down danger" id="usersNumReduce">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<input name="usersNum" id="usersNum" class="spinner-input form-control" maxlength="3"
								type="text" value="${defaultUsersNum}" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" 
											onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
							<div class="spinner-buttons	btn-group spinner-buttons-right">
								<button type="button" class="btn spinner-up blue" id="usersNumIncrease">
									<i class="fa fa-plus"></i>
								</button>
							</div>
						</div>
						<div class="pull-left number-notes">
							<p class="gray-9">20人起售，以10的倍数递增或递减</p>
						</div>
					</div>
					<div class="form-group clearfix margin-top-20">
						<label class="pull-left use-number gray-9">使用年限</label>
						<div class="pull-left clearfix use-year">
							<c:choose>
								<c:when test="${not empty discountStandards}">
									<c:forEach items="${discountStandards}" var="ds" varStatus="status">
										<div class="pull-left year-choice ${status.index==0?'current-choice':''}" name="discountStandardDiv">
											<c:if test="${status.index==0}"><input type="hidden" name="discountStandardId" value="${ds.id}" years="${ds.discountStandard}"></c:if>
											<a href="javascript:void(0);">${ds.describle}</a>
											<c:if test="${ds.discountStandard==2}"><span class="price-tag">9折</span></c:if>
											<c:if test="${ds.discountStandard==3}"><span class="price-tag">8折</span></c:if>
											<input type="hidden" name="discountStandardIdForSelect" value="${ds.id}" years="${ds.discountStandard}">
										</div>
									</c:forEach>
								</c:when>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<!--选购服务 E-->
			<!--联系方式 S-->
			<div class="widget padding-10 bg-white no-margin-bottom">
				<div class="widget-header lined">
					<span class="widget-caption blue-title">联系方式</span>
				</div>
				<div class="widget-body no-border0">
					<div class="pull-left">
						<span class="gray-9 padding-right-10">联系人 </span>${userInfo.userName}
						<input name="linkerName" value="${userInfo.userName}" type="hidden">
					</div>
					<div class="pull-left padding-left-50">
						<span class="gray-9 padding-right-10">电话</span>${userInfo.movePhone}
						<input name="linkerPhone" value="${userInfo.movePhone}" type="hidden">
					</div>
					<a href="#" class="padding-left-30"><%--<i class="fa fa-pencil"></i>修改--%></a>
				</div>
			</div>
			<!--联系方式 E-->
			<!--订单确认 S-->
			<div class="widget padding-10 bg-white no-margin-bottom">
				<div class="widget-header lined">
					<span class="widget-caption blue-title">订单确认</span>
				</div>
				<div class="widget-body no-border0">
					<div class="padding-top-10">
					<span id="orderParamDisspan" class="orange" style="font-size:23.4px;">当前已选择20人，使用年限1年</span>
						<%--<div class="checkbox">
							<label class="no-padding"> <input type="checkbox">
								<span class="text gray-9">需要发票</span> </label>
						</div>
						--%>
						<div class="checkbox">
							<label class="no-padding"> <input checked="checked" type="checkbox"  id="agreedEasyWinRuleCheckBox"> 
								<span class="text gray-9">本人已阅读并同意签署《<a href="javascript:void(0);" onclick="easyWinRules();" class="blue">企业版服务协议</a>》</span> 
							</label>
						</div>
					</div>
					<div class="price-box padding-top-10">
						<div class="gray-9"> <span id="originalTotalPriceSpan">原价：7200.00</span></div>
						<div class="gray-9"><span id="discountTotalPriceSpan">优惠：-￥0元</span></div>
					</div>
					<div class="price-e">
						交易价：<span class="orange-price" id="orderCostSpan">7200.00</span>
					</div>
					<div class="buy-btn-box">
						<input id="submitButton" type="button" onclick="addOrder(${orgBalanceMoney});" class="shiny-btn" value="立即购买"/>
					</div>
				</div>
			</div>
			<!--订单确认 E-->
			</form>
		</div>
		<!-- 收费标准E -->
	</div>
	<!-- /Page Container -->
</body>
</html>