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
<body>
	<div class="widget radius-bordered">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">基础信息</span>
			<div class="widget-buttons btn-div-full">
				<a data-toggle="collapse" class="ps-point btn-a-full"> <i
					class="fa fa-minus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<ul class="tickets-list">
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-group blue"></i>&nbsp;客户名称
						</div>
						<div
							class="ticket-user pull-left ${empty editCrm?'ps-boxinput':'ps-right-box'}">
							<div>
								<strong>${customer.customerName}</strong>
							</div>
						</div></li>
					<li class="ticket-item no-shadow ps-listline" style="clear:both">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-user blue"></i>&nbsp;责任人
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<img src="/downLoad/userImg/${customer.comId}/${customer.owner}?sid=${param.sid}"
								class="user-avatar" title="${customer.ownerName}" />
							<span class="user-name">${customer.ownerName}</span>
						</div></li>
					<li class="ticket-item no-shadow ps-listline"
						style="clear:both;display:${(not empty customer.areaName)?'block':'none'}">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-map-marker blue"></i>&nbsp;客户区域
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<span class="label label-default">${customer.areaName}</span>
						</div></li>
					<li class="ticket-item no-shadow ps-listline"
						style="clear:both;display:${(not empty customer.typeName)?'block':'none'}">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-tags blue"></i>&nbsp;客户类型
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<span class="label label-default">${customer.typeName}</span>
						</div></li>
						<li class="ticket-item no-shadow ps-listline" style="clear:both;">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-list-ol blue"></i>&nbsp;所属阶段
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<span class="label label-default">${customer.stageName}</span>
						</div></li>
						<li class="ticket-item no-shadow ps-listline" style="clear:both;">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-money blue"></i>&nbsp;预算资金
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<span class="label label-default">${customer.budget}</span>
						</div></li>
						<li class="ticket-item no-shadow ps-listline">
					    <div class="pull-left gray ps-left-text">
					    	&nbsp;阅读权限
					    </div>
						<div class="ticket-user pull-left ps-right-box">
							<span class="label label-default">${(customer.pubState== 1)?"公开":"私有"}</span>
						</div>
	                </li>
				
					<li class="ticket-item no-shadow autoHeight no-padding"
						style="clear:both;display:${(customer.pubState== 0)?'block':'none'}">
						<div class="pull-left gray ps-left-text padding-top-10">
							&nbsp;分享人</div>
						<div class="ticket-user pull-left ps-right-box"
							style="height: auto;">
							<div class="pull-left gray ps-left-text padding-top-10">
								<input type="hidden" id="sharerJson" name="sharerJson"
									value="${customer.sharerJson}" />
								<div id="crmSharor_div" style="max-width: 460px"></div>
							</div>
						</div>
						<div class="ps-clear"></div></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="widget radius-bordered"
		style="clear:both;display:${(not empty listLinkMan)?'block':'none'}">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">联系人 <span id="linkManNum"
				class="widget-caption red" style="font-size: 15px !important;">
					<c:if test="${not empty customer.linkManSum}">
												(${customer.linkManSum}) 
											</c:if> </span> </span>

			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i
					class="fa fa-minus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<table class="table table-hover table-striped table-bordered table0"
					id="linkManTable" style="width: 100%">
					<thead>
						<tr>
							<th width="30%">姓名</th>
							<th width="30%">职务</th>
							<th width="40%">联系方式</th>
						</tr>
					</thead>
					<tbody>
						<!--不允许编辑 -->
						<c:choose>
							<c:when test="${not empty listLinkMan}">
								<c:forEach items="${listLinkMan}" var="linkMan"
									varStatus="status">
									<c:choose>
										<c:when test="${not empty linkMan.contactWays}">
											<c:forEach items="${linkMan.contactWays}" var="subWay" varStatus="subStatus">
												<tr olmId="${linkMan.outLinkManId}">
													<td><a href="javascript:void(0);" onclick="viewOlm(${linkMan.outLinkManId});">${linkMan.linkManName }</a>
													</td>
													<td><input type="text" value="${linkMan.post}" disabled style="border: 0;" />
													</td>
													<td>
														<c:if test="${not empty subWay.contactWayValue}">
															${subWay.contactWayValue}：${subWay.contactWay}
														</c:if>
													</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
												<tr olmId="${linkMan.outLinkManId}">
													<td><a href="javascript:void(0);" onclick="viewOlm(${linkMan.outLinkManId});">${linkMan.linkManName }</a>
													</td>
													<td><input type="text" value="${linkMan.post}" disabled style="border: 0;" />
													</td>
													<td>
														
													</td>
												</tr>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:when>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<%-- <div class="widget radius-bordered"
		style="clear:both;">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">共享范围</span>
			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i
					class="fa fa-minus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<ul class="tickets-list">
				
					 <li class="ticket-item no-shadow ps-listline">
					    <div class="pull-left gray ps-left-text">
					    	&nbsp;阅读权限
					    </div>
						<div class="ticket-user pull-left ps-right-box">
						  	<div class="pull-left">
						  		<select class="populate" id="pubState" name="pubState" disabled>
									<option value="0" ${(customer.pubState== 0)? "selected='selected'":''} disabled>私有</option>
									<option value="1" ${(customer.pubState== 1)? "selected='selected'":''} disabled>公开</option>
									</select>
							</div>
						</div>
	                </li>
				
					<li class="ticket-item no-shadow autoHeight no-padding"
						style="clear:both;display:${(customer.pubState== 0)?'block':'none'}">
						<div class="pull-left gray ps-left-text padding-top-10">
							&nbsp;分享人</div>
						<div class="ticket-user pull-left ps-right-box"
							style="height: auto;">
							<div class="pull-left gray ps-left-text padding-top-10">
								<input type="hidden" id="sharerJson" name="sharerJson"
									value="${customer.sharerJson}" />
								<div id="crmSharor_div" style="max-width: 460px"></div>
							</div>
						</div>
						<div class="ps-clear"></div></li>
					<li class="ticket-item no-shadow autoHeight no-padding"
						style="clear:both;display:${(not empty listShareGroup)?'block':'none'}">
						<div class="pull-left gray ps-left-text padding-top-10">
							&nbsp;共享组</div>
						<div class="ticket-user pull-left ps-right-box"
							style="height: auto;">
							<div class="pull-left gray ps-left-text padding-top-10">
								<div id="customerGroup_div" style="max-width: 460px">
									<c:choose>
										<c:when test="${not empty listShareGroup}">
											<c:forEach items="${listShareGroup}" var="grp" varStatus="vs">
												<span id="crmGrp_${grp.id}" style="cursor:pointer;"
													class="label label-default margin-top-5 margin-left-5 margin-bottom-5">${grp.grpName}</span>
											</c:forEach>
										</c:when>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="ps-clear"></div></li>
				</ul>
			</div>
		</div>
	</div> --%>
	<div class="widget radius-bordered"
		style="clear:both;display:${(
	                           not empty customer.linePhone ||
	                           not empty customer.fax ||
	                           not empty customer.postCode ||
	                           not empty customer.address ||
	                           not empty customer.customerRemark
	                           )?'block':'none'}">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">更多配置</span>
			<div class="widget-buttons btn-div-full">
				<a data-toggle="collapse" class="ps-point btn-a-full"> <i
					class="fa fa-minus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<ul class="tickets-list">
					<li class="ticket-item no-shadow ps-listline"
						style="clear:both;display:${(not empty customer.linePhone)?'block':'none'}">
						<div class="pull-left gray ps-left-text">&nbsp;联系电话</div>
						<div
							class="ticket-user pull-left ${empty editCrm?'ps-boxinput':'ps-right-box'}"">
							<span class="user-name">${customer.linePhone}</span>
						</div></li>
					<li class="ticket-item no-shadow ps-listline"
						style="clear:both;display:${(not empty customer.fax)?'block':'none'}">
						<div class="pull-left gray ps-left-text">&nbsp;传真</div>
						<div
							class="ticket-user pull-left ${empty editCrm?'ps-boxinput':'ps-right-box'}"">
							<span class="user-name">${customer.fax}</span>
						</div></li>
					<li class="ticket-item no-shadow ps-listline"
						style="clear:both;display:${(not empty customer.postCode)?'block':'none'}">
						<div class="pull-left gray ps-left-text">&nbsp;邮编</div>
						<div
							class="ticket-user pull-left ${empty editCrm?'ps-boxinput':'ps-right-box'}"">
							<span class="user-name">${customer.postCode}</span>
						</div></li>
					<li class="ticket-item no-shadow ps-listline"
						style="clear:both;display:${(not empty customer.address)?'block':'none'}">
						<div class="pull-left gray ps-left-text">&nbsp;联系地址</div>
						<div
							class="ticket-user pull-left ${empty editCrm?'ps-boxinput':'ps-right-box'}"
							" style="width: 400px">
							<span class="user-name">${customer.address}</span>
						</div></li>
					<li class="ticket-item no-shadow autoHeight"
						style="clear:left;display:${(not empty customer.customerRemark)?'block':'none'}">
						<div class="pull-left gray ps-left-text" style="padding-top:10px">
							&nbsp;客户备注</div>
						<div class="ticket-user pull-left"
							style="width: 400px;line-height: 40px">
							<tags:viewTextArea>${customer.customerRemark }</tags:viewTextArea>
						</div>
						<div class="ps-clear"></div></li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>
