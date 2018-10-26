<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-minus blue"></i>
				</a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<ul class="tickets-list">
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-group blue"></i>&nbsp;客户名称
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<div>
								<c:choose>
									<c:when test="${empty editCrm}">
										<input id="customerName" datatype="input,sn" defaultLen="52" name="customerName" nullmsg="请填写客户名称" class="colorpicker-default form-control" type="text" title="${customer.customerName}"
											value="${customer.customerName}" onpropertychange="handleName()" onkeyup="handleName()" style="width: 400px;float: left">
										<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
									</c:when>
									<c:otherwise>
										<strong>${customer.customerName}</strong>
									</c:otherwise>
								</c:choose>

							</div>
							<span id="addCrmWarm" style="float:left;margin-left:2px;"></span>
						</div>
						<c:if test="${empty editCrm}">
							<script>
								//当状态改变的时候执行的函数 
								function handleName() {
									var value = $('#customerName').val();
									var len = charLength(value.replace(/\s+/g,""));
									if (len % 2 == 1) {
										len = (len + 1) / 2;
									} else {
										len = len / 2;
									}
									if (len > 26) {
										$('#msgTitle').html("(<font color='red'>" + len+ "</font>/26)");
									} else {
										$('#msgTitle').html("(" + len + "/26)");
									}
								}
								//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
								if (/msie/i.test(navigator.userAgent)) { //ie浏览器 
									document.getElementById('customerName').onpropertychange = handleName
								} else {//非ie浏览器，比如Firefox 
									document.getElementById('customerName').addEventListener("input",handleName, false);
								}
							</script>
						</c:if>
					</li>
					<li class="ticket-item no-shadow ps-listline" style="clear:both">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-user blue"></i>&nbsp;责任人
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<input type="hidden" id = "owner" value="${customer.owner }">
							<img src="/downLoad/userImg/${customer.comId}/${customer.owner}?sid=${param.sid}" class="user-avatar" title="${customer.ownerName}" />
							<span class="user-name">${customer.ownerName}</span>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline" style="clear: both">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-map-marker blue"></i>&nbsp;客户区域
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<input type="hidden" id="areaIdAndType" name="areaIdAndType" value="${customer.areaIdAndType}" preValue="${customer.areaIdAndType}" />
							<div style="float: left">
								<c:choose>
									<c:when test="${empty editCrm }">
										<input id="areaName" datatype="area" name="areaName" class="colorpicker-default form-control pull-left" type="text" onclick="areaOne('areaIdAndType','areaName','${param.sid}','1');" readonly
											style="cursor:auto;width: 200px;float:left" value="${customer.areaName}" title="双击选择">
										<a href="javascript:void(0)" class="fa fa-map-marker pull-left" style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px" title="区域选择"
											onclick="areaOne('areaIdAndType','areaName','${param.sid}','1');"></a>
									</c:when>
									<c:otherwise>
										<span class="label label-default">${customer.areaName}</span>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline" style="clear:both">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-tags blue"></i>&nbsp;客户类型
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<c:choose>
								<c:when test="${empty editCrm}">
									<select class="populate" datatype="customerType" id="customerTypeId" name="customerTypeId" style="cursor:auto;width: 200px">
										<optgroup label="选择客户类型"></optgroup>
										<c:choose>
											<c:when test="${not empty listCustomerType}">
												<c:forEach items="${listCustomerType}" var="customerType" varStatus="status">
													<option value="${customerType.id}" ${(customer.customerTypeId==customerType.id)?
														"selected='selected'":''}>${customerType.typeName}</option>
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
								</c:when>
								<c:otherwise>
									<span class="label label-default">${customer.typeName}</span>
								</c:otherwise>
							</c:choose>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline" style="clear:both">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-list-ol blue"></i>&nbsp;所属阶段
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<div datatype="stage" class=" pull-left ">
								<select class="populate" datatype="stage" id="stage" name="stage" style="cursor:auto;width: 200px">
									<optgroup label="选择所属阶段"></optgroup>
									<c:choose>
										<c:when test="${not empty listCrmStage}">
											<c:forEach items="${listCrmStage}" var="stage" varStatus="status">
												<option value="${stage.id}" ${(customer.stage==stage.id)? "selected='selected'":''} >${stage.stageName}</option>
											</c:forEach>
										</c:when>
									</c:choose>
								</select>
							</div>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-money blue"></i>&nbsp;资金预算
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<div class="pull-left">
								<input id="budget" datatype="float" value="${customer.budget }" name="budget" nullmsg="请填写资金预算" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
							</div>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline">
					    <div class="pull-left gray ps-left-text">
					    	&nbsp;阅读权限
					    </div>
						<div class="ticket-user pull-left ps-right-box">
						  	<div class="pull-left">
						  		<select class="populate" id="pubState" name="pubState" onchange="showMans()">
									<option value="0" ${(customer.pubState== 0)? "selected='selected'":''}>私有</option>
									<option value="1" ${(customer.pubState== 1)? "selected='selected'":''}>公开</option>
									</select>
							</div>
						</div>
	                </li>
					
				
					<li class="ticket-item no-shadow autoHeight no-padding" id="mans" style="display: block;">
						<div class="pull-left gray ps-left-text padding-top-10">&nbsp;分享人</div>
						<div class="ticket-user pull-left ps-right-box" style="height: auto;">
							<div class="pull-left gray ps-left-text padding-top-10">
								<input type="hidden" id="sharerJson" name="sharerJson" value="${customer.sharerJson}" />
								<c:choose>
									<c:when test="${empty editCrm}">
										<tags:userMore name="listCustomerSharer.userId" showName="userName" disDivKey="crmSharor_div" callBackStart="yes"></tags:userMore>
									</c:when>
									<c:otherwise>
										<div id="crmSharor_div"></div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="ps-clear"></div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="widget radius-bordered collapsed">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">
				联系人
				<span id="linkManNum" class="widget-caption red" style="font-size: 15px !important;">
					<c:if test="${not empty customer.linkManSum}">
												(${customer.linkManSum}) 
											</c:if> 
				</span>
			</span>

			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse" onclick="showBut()"> <i class="fa fa-plus blue"></i>
				</a>
			</div>
			<c:if test="${empty editCrm}">
				<button type="button" class="btn btn-info ws-btnBlue"  onclick="showOlm('${param.sid}')" style="margin-top: 3px;margin-right:5% ;display: none;float: right;line-height: 0.8;" id="but"><i class="fa fa-chain"></i> 联系人</button>
			</c:if>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<%-- <c:if test="${empty editCrm}">
					<div class="pull-right" style="margin: 10px 0px">
						<button type="button" class="btn btn-info ws-btnBlue" onclick="showOlm('${param.sid}')">联系人关联</button>
						<!-- <button type="button" class="btn btn-info ws-btnBlue" onclick="customerLinkManUpdate()">保存</button> -->
					</div>
				</c:if> --%>
				<table class="table table-hover table-striped table-bordered table0" id="linkManTable" style="width: 100%;display: none">
					<thead>
						<tr>
							<th width="30%">姓名</th>
							<th width="30%">职务</th>
							<th width="40%">联系方式</th>
							<c:choose>
								<c:when test="${empty editCrm}">
									<th>&nbsp;</th>
								</c:when>
							</c:choose>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${empty editCrm}">
								<!-- 允许进行编辑 -->
								<c:choose>
									<c:when test="${not empty listLinkMan}">
										<c:forEach items="${listLinkMan}" var="linkMan" varStatus="status">
											<c:choose>
												<c:when test="${not empty linkMan.contactWays}">
													<c:forEach items="${linkMan.contactWays}" var="subWay" varStatus="subStatus">
														<tr olmId="${linkMan.outLinkManId}">
															<td>
																<a href="javascript:void(0);" onclick="viewOlm(${linkMan.outLinkManId});">${linkMan.linkManName}</a>
																<input  type="hidden" id="listLinkMan[${status.count-1}].olmId" name="listLinkMan[${status.count-1}].olmId" value="${linkMan.outLinkManId}" />
															</td>
															<td>
																<input style="border: 0;" disabled type="text" id="listLinkMan[${status.count-1}].post" name="listLinkMan[${status.count-1}].post" value="${linkMan.post}" />
															</td>
															<td>
																<c:if test="${not empty subWay.contactWayValue}">
																	${subWay.contactWayValue}：${subWay.contactWay}
																</c:if>
															</td>
															<td>
																<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLinkManTr(this,'${linkMan.id}','${linkMan.outLinkManId}')" title="删除本行"></a>
															</td>
														</tr>
													</c:forEach>
												</c:when>
												<c:otherwise>
														<tr olmId="${linkMan.outLinkManId}">
															<td>
																<a href="javascript:void(0);" onclick="viewOlm(${linkMan.outLinkManId});">${linkMan.linkManName}</a>
																<input  type="hidden" id="listLinkMan[${status.count-1}].olmId" name="listLinkMan[${status.count-1}].olmId" value="${linkMan.outLinkManId}" />
															</td>
															<td>
																<input style="border: 0;" disabled type="text" id="listLinkMan[${status.count-1}].post" name="listLinkMan[${status.count-1}].post" value="${linkMan.post}" />
															</td>
															<td>
																<c:if test="${not empty linkMan.contactWayValue}">
																	${linkMan.contactWayValue}：${linkMan.contactWay}
																</c:if>
															</td>
															<td>
																<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLinkManTr(this,'${linkMan.id}','${linkMan.outLinkManId}')" title="删除本行"></a>
															</td>
														</tr>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr style="display: none">
											<td>
												<input  style="border: 0;" disabled class="table_input"  type="text"  id="listLinkMan[0].linkManName" name="listLinkMan[0].linkManName" />
											</td>
											<td>
												<input style="border: 0;" disabled class="table_input" type="text" id="listLinkMan[0].post" name="listLinkMan[0].post" />
											</td>
											<td>
												<input style="border: 0;" disabled class="table_input" type="text" ignore='ignore' id="listLinkMan[0].contactWay" name="listLinkMan[0].contactWay" />
											</td>
											<td>
												<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLinkManTr(this,0)" title="删除本行"></a>
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<!--不允许编辑 -->
								<c:choose>
									<c:when test="${not empty listLinkMan}">
										<c:forEach items="${listLinkMan}" var="linkMan" varStatus="status">
										
											<c:choose>
												<c:when test="${not empty linkMan.contactWays}">
													<c:forEach items="${linkMan.contactWays}" var="subWay" varStatus="subStatus">
														<tr olmId="${linkMan.outLinkManId}">
															<td>
																<a href="javascript:void(0);" onclick="viewOlm(${linkMan.outLinkManId});">${linkMan.linkManName}</a>
															</td>
															<td>
																<input type="text" value="${linkMan.post}" disabled style="border: 0;" />
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
															<td>
																<a href="javascript:void(0);" onclick="viewOlm(${linkMan.outLinkManId});">${linkMan.linkManName}</a>
															</td>
															<td>
																<input type="text" value="${linkMan.post}" disabled style="border: 0;" />
															</td>
															<td>
																
															</td>
														</tr>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:when>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<%-- <div class="widget radius-bordered collapsed" style="clear:both">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">共享范围设置</span>
			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-plus blue"></i>
				</a>
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
						  		<select class="populate" id="pubState" name="pubState" onchange="showMans()">
									<option value="0" ${(customer.pubState== 0)? "selected='selected'":''}>私有</option>
									<option value="1" ${(customer.pubState== 1)? "selected='selected'":''}>公开</option>
									</select>
							</div>
						</div>
	                </li>
					
				
					<li class="ticket-item no-shadow autoHeight no-padding" id="mans" style="display: block;">
						<div class="pull-left gray ps-left-text padding-top-10">&nbsp;分享人</div>
						<div class="ticket-user pull-left ps-right-box" style="height: auto;">
							<div class="pull-left gray ps-left-text padding-top-10">
								<input type="hidden" id="sharerJson" name="sharerJson" value="${customer.sharerJson}" />
								<c:choose>
									<c:when test="${empty editCrm}">
										<tags:userMore name="listCustomerSharer.userId" showName="userName" disDivKey="crmSharor_div" callBackStart="yes"></tags:userMore>
									</c:when>
									<c:otherwise>
										<div id="crmSharor_div"></div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="ps-clear"></div>
					</li>
					<li class="ticket-item no-shadow autoHeight no-padding">
						<div class="pull-left gray ps-left-text padding-top-10">&nbsp;共享组</div>
						<c:choose>
							<c:when test="${empty editCrm}">
								<div class="ticket-user pull-left ps-right-box" style="height: auto;">
									<div class="pull-left gray ps-left-text padding-top-10">
										<div id="customerGroup_div" style="max-width: 460px" class="pull-left">
											<c:choose>
												<c:when test="${not empty listShareGroup}">
													<c:forEach items="${listShareGroup}" var="grp" varStatus="vs">
														<span id="crmGrp_${grp.id}" style="cursor:pointer" title="双击移除" class="label label-default margin-top-5 margin-left-5 margin-bottom-5" ondblclick="delCrmShareGroup('${grp.id}')">${grp.grpName}</span>
													</c:forEach>
												</c:when>
											</c:choose>
										</div>
										<a href="javascript:void(0)" onclick="querySelfGroupForCustomer(0,'${param.sid}')" class="btn btn-primary btn-xs margin-top-5 margin-left-5" title="组选择"><i class="fa fa-plus"></i>添加</a>
										用于保持数据
										<div style="display:none" id="tempSelectDiv">
											<select style="display:none" id="selfGrouplist">
												<c:choose>
													<c:when test="${not empty listShareGroup}">
														<c:forEach items="${listShareGroup}" var="grp" varStatus="vs">
															<option value="${grp.id}">${grp.grpName}</option>
														</c:forEach>
													</c:when>
												</c:choose>
											</select>
										</div>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="ticket-user pull-left ps-right-box" style="height: auto;">
									<div class="pull-left gray ps-left-text padding-top-10">
										<div id="customerGroup_div" style="max-width: 460px">
											<c:choose>
												<c:when test="${not empty listShareGroup}">
													<c:forEach items="${listShareGroup}" var="grp" varStatus="vs">
														<span id="crmGrp_${grp.id}" style="cursor:pointer;" class="label label-default margin-top-5 margin-left-5 margin-bottom-5">${grp.grpName}</span>
													</c:forEach>
												</c:when>
											</c:choose>
										</div>
									</div>
								</div>

							</c:otherwise>
						</c:choose>
						<div class="ps-clear"></div>
					</li>
				</ul>
			</div>
		</div>
	</div> --%>
	<div class="widget radius-bordered collapsed" style="clear:both">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">更多配置</span>
			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-plus blue"></i>
				</a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<ul class="tickets-list">
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">&nbsp;联系电话</div>
						<div class="ticket-user pull-left ps-right-box">
							<c:choose>
								<c:when test="${empty editCrm }">
									<input class="colorpicker-default form-control" type="text" value="${customer.linePhone}" ignore="ignore" id="linePhone" name="linePhone">
								</c:when>
								<c:otherwise>
									<span class="user-name">${customer.linePhone}</span>
								</c:otherwise>
							</c:choose>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">&nbsp;传真</div>
						<div class="ticket-user pull-left ps-right-box">
							<c:choose>
								<c:when test="${empty editCrm}">
									<input class="colorpicker-default form-control" type="text" value="${customer.fax}" id="fax" name="fax">
								</c:when>
								<c:otherwise>
									<span class="user-name">${customer.fax}</span>
								</c:otherwise>
							</c:choose>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">&nbsp;邮编</div>
						<div class="ticket-user pull-left ps-right-box">
							<c:choose>
								<c:when test="${empty editCrm}">
									<input class="colorpicker-default form-control" type="text" value="${customer.postCode}" ignore="ignore" id="postCode" name="postCode">
								</c:when>
								<c:otherwise>
									<span class="user-name">${customer.postCode}</span>
								</c:otherwise>
							</c:choose>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">&nbsp;联系地址</div>
						<div class="ticket-user pull-left ps-right-box" style="width: 400px">
							<c:choose>
								<c:when test="${empty editCrm}">
									<input class="colorpicker-default form-control" id="address" name="address" type="text" value="${customer.address}">
								</c:when>
								<c:otherwise>
									<span class="user-name">${customer.address}</span>
								</c:otherwise>
							</c:choose>
						</div>
					</li>
					<li class="ticket-item no-shadow autoHeight no-padding">
						<div class="pull-left gray ps-left-text padding-top-10">&nbsp;客户备注</div>
						<c:choose>
							<c:when test="${empty editCrm}">
								<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
									<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" id="customerRemark" name="customerRemark" rows="" cols="">${customer.customerRemark }</textarea>
								</div>
							</c:when>
							<c:otherwise>
								<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
									<div class="margin-top-10 margin-bottom-10 margin-left-10">
										<tags:viewTextArea>${customer.customerRemark }</tags:viewTextArea>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
						<div class="ps-clear"></div>
					</li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>
