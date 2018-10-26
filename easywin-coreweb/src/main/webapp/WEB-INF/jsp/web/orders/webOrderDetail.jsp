<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script src="/static/js/order_center/order_center.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">

//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
	openPageTag = openWindow.pageTag;
	openTabIndex = openWindow.tabIndex;
}
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
});
</script>
</head>
<body>
<form id="subform" class="subform" method="post">
	<div class="container" style="padding: 0px 0px;width:100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">订单详情</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin();" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div>
                     <!--Widget Header-->
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow-y:auto;position: relative;">
                         <div class="widget-body no-shadow">
							<div class="row margin-bottom-5">
								<div class="col-sm-12">
									<c:choose>
										<c:when test="${order.orgBalanceMoney>=order.orderCost}">
											<div class="form-group" style="font-weight:bold;">
												<a href="javascript:void(0);" onclick="organicSpaceCfgInfo('${param.sid}');">
													<span class="pull-left">团队结余：</span>
													<span class="margin-left-10 pull-left">
														<span style="color:green;">${order.orgBalanceMoney}&nbsp;元</span>
														<span style="color:red;">（团队结余大于交易额；团队结余充足，无需升级。）</span>
													</span>
												</a>
											</div>
										</c:when>
										<c:otherwise>
											<div class="form-group" style="font-weight:bold;">
												<span class="pull-left">订单状态：</span>
												<span class="margin-left-10 pull-left">
												<c:choose>
													<c:when test="${order.status==0 and order.overDates > 7}">
														<span style="color:red;">已失效</span>
													</c:when>
													<c:otherwise>
														<c:if test="${order.status==0}"><span style="color:green;">待付款</span></c:if>
														<c:if test="${order.status==1}"><span style="color:blue;">已支付</span></c:if>
														<c:if test="${order.status==2}"><span style="color:red;">订单失败</span></c:if>
													</c:otherwise>
												</c:choose>
												</span>
											</div>
										</c:otherwise>
									</c:choose>
								</div>
								<c:if test="${order.status==0 and order.overDates <= 7 and (order.orgBalanceMoney<order.orderCost)}">
									<div class="col-sm-12 margin-top-5">
										<a href="javascript:void(0);" onclick="orderToPayByparent(${order.id},'${param.sid}');" class="btn btn-primary">付款</a>
									</div>
								</c:if>
							</div>	
							<div class="form-title themeprimary" style="padding:5px 0;">订单信息</div>
							<div class="row">
								<div class="col-sm-12">
									<table class="table table-striped table-hover"
										id="editabledatatable">
										<thead>
											<tr role="row">
												<th>类型</th>
												<th>交易金额（元）</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>${order.orderTypeName}</td>
												<td>${order.orderCost}</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>	
							<div class="form-title themeprimary" style="padding:5px 0;">购买明细</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<span class="pull-left">购买团队</span>
										<span class="margin-left-10 pull-left">${order.orgName}</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">使用人数</span>
										<span class="margin-left-10 pull-left">${order.productNum}&nbsp;人</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">消费档位</span>
										<span class="margin-left-10 pull-left">&lt;=${order.chargingstandard}&nbsp;人</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">档位单价</span>
										<span class="margin-left-10 pull-left">${order.originalPrice}&nbsp;元/人/年</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">使用年限</span>
										<span class="margin-left-10 pull-left">${order.years}&nbsp;年</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">优惠折扣</span>
										<span class="margin-left-10 pull-left">${order.discountDescrible}</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">交易单价</span>
										<span class="margin-left-10 pull-left">${order.actualPrice}&nbsp;元/人/年</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">&nbsp;&nbsp;&nbsp;原&nbsp;&nbsp;价&nbsp;&nbsp;</span>
										<span class="margin-left-10 pull-left">${order.originalTotalPrice}&nbsp;元</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">&nbsp;&nbsp;&nbsp;优&nbsp;&nbsp;惠&nbsp;&nbsp;</span>
										<span class="margin-left-10 pull-left">${order.discountPrice}&nbsp;元</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">&nbsp;成&nbsp;交&nbsp;价</span>
										<span class="margin-left-10 pull-left">${order.orderCost}&nbsp;元</span>
									</div>
								</div>
							</div>	
							<div class="form-title themeprimary" style="padding:5px 0;">支付信息</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<span class="pull-left">支付方式</span>
										<span class="margin-left-10 pull-left">付款</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">下单时间</span>
										<span class="margin-left-10 pull-left">${order.recordCreateTime}</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span class="pull-left">支付时间</span>
										<span class="margin-left-10 pull-left">${order.paidTime}</span>
									</div>
								</div>
							</div>
                         </div> 
                      </div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>