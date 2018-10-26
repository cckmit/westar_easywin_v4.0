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
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i
					class="fa fa-minus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<ul class="tickets-list">
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-pencil-square blue"></i>&nbsp;项目名称
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<strong>${item.itemName}</strong>
						</div></li>
					<li class="ticket-item no-shadow ps-listline" style="width: 50%;float: left;">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-user blue"></i>&nbsp;项目经理
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<img style="float: left" src="/downLoad/userImg/${userInfo.comId}/${item.owner}"
								title="${item.ownerName}" class="user-avatar" />
							<span class="user-name">${item.ownerName}</span>
						</div></li>
						<c:if test="${not empty item.developLeaderName}">
							<li class="ticket-item no-shadow ps-listline" style="width: 50%;float: left;">
								<div class="pull-left gray ps-left-text">
									<i class="fa fa-user blue"></i>&nbsp;研发负责人
								</div>
								<div class="ticket-user pull-left ps-right-box">
									<img style="float: left" src="/downLoad/userImg/${userInfo.comId}/${item.developLeader}"
										title="${item.developLeaderName}" class="user-avatar" />
									<span class="user-name">${item.developLeaderName}</span>
								</div>
							</li>
						</c:if>
						<c:if test="${not empty item.productName}">
							<li class="ticket-item no-shadow ps-listline" style="width: 50%;float: left;">
								<div class="pull-left gray ps-left-text">
									<i class="fa fa-gears blue"></i>&nbsp;所属产品
								</div>
								<div class="ticket-user pull-left ps-right-box">
									${item.productName}</div>
							</li>
						</c:if>
						<c:if test="${not empty item.productManagerName}">
							<li class="ticket-item no-shadow ps-listline" style="width: 50%;float: left;">
								<div class="pull-left gray ps-left-text">
									<i class="fa fa-user blue"></i>&nbsp;产品经理
								</div>
								<div class="ticket-user pull-left ps-right-box">
									${item.productManagerName}</div>
							</li>
						</c:if>
					<c:if test="${not empty item.partnerName}">
						<li class="ticket-item no-shadow ps-listline" style="width: 50%;float: left;">
							<div class="pull-left gray ps-left-text">
								<i class="fa fa-gears blue"></i>&nbsp;关联客户
							</div>
							<div class="ticket-user pull-left ps-right-box">
								${item.partnerName}</div></li>
					</c:if>
					<li class="ticket-item no-shadow ps-listline"
						style="display:${(not empty item.pItemName)?'block':'none'};width: 50%;float: left;">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-sitemap blue"></i>&nbsp;父级项目
						</div>
						<div class="ticket-user pull-left ps-right-box">
							${item.pItemName}</div></li>
					<%-- <li class="ticket-item no-shadow ps-listline"
						style="clear:both;display:${(not empty item.itemProgress)?'block':'none'}">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-tags blue"></i>&nbsp;项目进度
						</div>
						<div class="ticket-user pull-left ps-right-box">
							${item.itemProgressDescribe}</div></li> --%>
					<c:if test="${not empty item.amount}">
						<li class="ticket-item no-shadow ps-listline" style="width: 50%;float: left;">
							<div class="pull-left gray ps-left-text">
								<i class="fa fa-gears blue"></i>&nbsp;项目金额
							</div>
							<div class="ticket-user pull-left ps-right-box">
								${item.amount}万</div></li>
					</c:if>
					<c:if test="${not empty item.serviceDate}">
						<li class="ticket-item no-shadow ps-listline" style="width: 50%;float: left;">
							<div class="pull-left gray ps-left-text">
								<i class="fa fa-gears blue"></i>&nbsp;服务期限
							</div>
							<div class="ticket-user pull-left ps-right-box">
								${item.serviceDate}</div></li>
					</c:if>
					<li class="ticket-item no-shadow autoHeight no-padding"
						style="clear:both;display:${(not empty item.itemRemark)?'block':'none'}">
						<div class="pull-left gray ps-left-text padding-top-10">
							<i class="fa fa-list-ol blue"></i>&nbsp;&nbsp;项目备注
						</div>
						<div class="ticket-user pull-left ps-right-box"
							style="width: 400px;height: auto;">
							<div class="margin-top-10 margin-bottom-10 margin-left-10">
								<tags:viewTextArea>${item.itemRemark}</tags:viewTextArea>
							</div>
						</div>
						<div class="ps-clear"></div></li>
						<li  class="ticket-item no-shadow autoHeight no-padding" style="clear:both;">
	                    	<div class="pull-left gray ps-left-text padding-top-10">
					    		<i class="fa fa-list-ol blue"></i>&nbsp;&nbsp;项目进度
					    	</div>
					    	<div class="pull-right gray ps-right-text padding-top-10" style="display: ${item.state != 4?'block':'none'}">
					    		<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addBusRemind('005','${item.id}')">催一下</button>
					    	</div>
							<div class="pull-left gray ps-left-text padding-top-10" style="height: auto;margin-left: 40px;">
						  		<div class="wedgt">
									<div class="fm_progress">
										<c:choose>
											<c:when test="${item.state != 4}">
												<c:forEach items="${item.listItemProgress}" var="obj" varStatus="vs">
													<div class="pull-left itemDiv">
														<div class="quan pull-left" >
															<span ${(not empty obj.startTime )?'class="active"':''}></span>
														</div>
														<c:if test="${item.listItemProgress.size() != vs.count}">
															<div class="line pull-left">
																<span ${(not empty obj.startTime &&  not empty obj.finishTime)?'class="active"':''}></span>
															</div>
														</c:if>
														<c:if test="${item.listItemProgress.size() == vs.count}">
															<div class="line pull-left">
																<span></span>
															</div>
														</c:if>
														<div class="ps-clear"></div>
														<div class="lc_name">
															<span ${(not empty obj.startTime )?'class="active"':''}>${obj.progressName }</span>
														</div>
														<div class="per_name">
															<span>${obj.userName }</span>
														</div>
														<div class="lc_time">
															<span>${obj.startTime }</span>
														</div>
													</div>
												</c:forEach>
												<div class="pull-left itemDiv">
													<div class="quan pull-left">
														<span></span>
													</div>
													<div class="ps-clear"></div>
													<div class="lc_name">
														<span>关闭</span>
													</div>
													<div class="per_name">
														<span></span>
													</div>
													<div class="lc_time">
														<span></span>
													</div>
												</div>
											</c:when>
											<c:otherwise>
												<c:forEach items="${item.listItemProgress}" var="obj" varStatus="vs">
													<div class="pull-left itemDiv">
														<div class="quan pull-left" >
															<span ${(not empty obj.startTime )?'class="active"':''}></span>
														</div>
														<div class="line pull-left">
															<span class="active"></span>
														</div>
														<div class="ps-clear"></div>
														<div class="lc_name">
															<span ${(not empty obj.startTime )?'class="active"':''}>${obj.progressName }</span>
														</div>
														<div class="per_name">
															<span>${obj.userName }</span>
														</div>
														<div class="lc_time">
															<span>${obj.startTime }</span>
														</div>
													</div>
												</c:forEach>
												<div class="pull-left itemDiv">
													<div class="quan pull-left">
														<span class="active"  style="background-color: red;"></span>
													</div>
													<div class="ps-clear"></div>
													<div class="lc_name">
														<span class="active" style="color: red;">关闭</span>
													</div>
													<div class="per_name">
														<span>${item.ownerName }</span>
													</div>
													<div class="lc_time">
														<span>${fn:substring(item.modifyDate,0,16)}</span>
													</div>
												</div>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div> 
							<div class="ps-clear"></div>   
                    	</li>
						
						<li class="ticket-item no-shadow ps-listline">
					    <div class="pull-left gray ps-left-text">
					    	<i class="fa fa-list-ol blue"></i>&nbsp;&nbsp;阅读权限
					    </div>
						<div class="ticket-user pull-left ps-right-box">
							<c:if test="${item.pubState== 0}">
								私有
							</c:if>
						 	<c:if test="${item.pubState== 1}">
								公开
							</c:if>
						</div>
	                </li>
				
					<li class="ticket-item no-shadow autoHeight no-padding"
						style="clear:both;display:${(item.pubState== 0)?'block':'none'}">
						<div class="pull-left gray ps-left-text padding-top-10">
							<i class="fa fa-user blue"></i>&nbsp;&nbsp;分享人</div>
						<div class="ticket-user pull-left ps-right-box"
							style="height: auto;">
							<input type="hidden" id="itemSharerJson" name="itemSharerJson"
								value="${item.itemSharerJson}" />
							<div class="pull-left gray ps-left-text padding-top-10">
								<div id="itemSharor_div" class="pull-left"
									style="max-width: 460px"></div>
							</div>
						</div>
						<div class="ps-clear"></div></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="widget radius-bordered"
		style="clear:both;display:${(not empty listSonItem)?'block':'none'}">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">子项目</span>

			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i
					class="fa fa-minus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<table class="table table-hover table-striped table-bordered table0"
					id="allSonItem" style="width: 100%">
					<thead>
						<tr>
							<th width="14%">序号</th>
							<th>项目名称</th>
							<th width="14%">项目经理</th>
							<th width="14%">项目状态</th>
						</tr>
					</thead>
					<tbody id="sonItem">
						<c:choose>
							<c:when test="${not empty listSonItem}">
								<c:forEach items="${listSonItem}" var="sonItem"
									varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td><a href="javascript:void(0)"
											onclick="viewSonItem(${sonItem.id})">${sonItem.itemName}</a>
										</td>
										<td>
											<div class="ticket-user pull-left other-user-box">
												<img
															src="/downLoad/userImg/${userInfo.comId}/${sonItem.owner}?sid=${param.sid}"
															title="${sonItem.ownerName}" class="user-avatar" />
												<span class="user-name">${sonItem.ownerName}</span>
											</div></td>
										<td><input type="hidden" name="sonItemState"
											value="${empty sonItem.state?'1':sonItem.state}"> <c:choose>
												<c:when test="${sonItem.state==1}">进行中</c:when>
												<c:when test="${sonItem.state==3}">挂起中</c:when>
												<c:when test="${sonItem.state==4}">已完结</c:when>
												<c:otherwise>
																			正常
																		</c:otherwise>
											</c:choose></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="4" style="text-align: center;">没有子项目</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
