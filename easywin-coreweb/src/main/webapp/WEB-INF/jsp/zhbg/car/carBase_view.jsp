<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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
</head>
<body>
	<div class="widget-body no-shadow" style="padding:0 12px;">
		<div class="widget radius-bordered">
			<div class="widget-header bg-bluegray no-shadow">
				<span class="widget-caption blue">基础信息</span>
				<div class="widget-buttons btn-div-full">
					<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-minus blue"></i>
					</a>
				</div>
			</div>
			<div class="widget-body no-shadow">
				<div class="tickets-container bg-white">
					<ul class="tickets-list">
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;车牌号
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${car.carNum }
								</div>
							</div>
						</li>
						<c:if test="${not empty  car.carModel}">
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;车辆型号</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${car.carModel }
								</div>
							</div>
						</li>
						</c:if>
						<c:if test="${not empty  car.displacement}">
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;排量</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									${car.displacement }
								</div>
							</div>
						</li>
						</c:if>
						<c:if test="${not empty  car.color}">
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;颜色</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${car.color }
								</div>
							</div>
						</li>
						</c:if>
						<c:if test="${not empty  car.seatNum}">
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;座位数</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${car.seatNum }
								</div>
							</div>
						</li>
						</c:if>
						<c:if test="${not empty  car.annualInspection}">
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;年检日期</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${car.annualInspection }
								</div>
							</div>
						</li>
						</c:if>
						<li class="ticket-item no-shadow ps-listline" style="clear:both">
							<div class="pull-left gray ps-left-text">&nbsp;车辆类型</div>
							<div class="ticket-user pull-left ps-right-box">
								<div datatype="carTypeId" class=" pull-left ">
									<c:forEach items="${listCarType}" var="carType" varStatus="vs">
									<c:if test="${carType.id == car.carTypeId }">${carType.typeName }</c:if>
									</c:forEach>
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;车辆状态</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								<c:choose>
									<c:when test="${car.stateType== 1 }">可用</c:when>
									<c:when test="${car.stateType== 2 }">损坏</c:when>
									<c:when test="${car.stateType== 3 }">维修</c:when>
									<c:when test="${car.stateType== 4 }">报废</c:when>
								</c:choose>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
