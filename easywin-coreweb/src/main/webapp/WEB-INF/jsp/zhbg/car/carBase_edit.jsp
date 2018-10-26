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
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="carNum"  datatype="*" name="carNum" nullmsg="请输入车牌号"  value="${car.carNum }" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;车辆型号</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="carModel" name="carModel" value="${car.carModel }" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;发动机号</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="engineNum" name="engineNum" value="${car.engineNum }" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;发动机编号-车架号后6位</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input name="engineNumAfterSix" value="${car.engineNumAfterSix }" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;发动机编号-登记号后7位</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input name="engineNumAfterSeven" value="${car.engineNumAfterSeven }" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;排量(L)</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input name="displacement" value="${car.displacement }" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;颜色</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input name="color" value="${car.color }" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;座位数</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input value="${car.seatNum }" name="seatNum"  dataType="/^\s*$/|n"  class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;购置日期</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input value="${car.buyDate }" name="buyDate" class="colorpicker-default form-control" readonly="readonly" onClick="WdatePicker()" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;购置价格(元)</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input value="${car.buyPrice }" name="buyPrice"  dataType = "/^\s*$/|/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/" errorMsg = "请填入正确的数字！" 
									 class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;购置税价格(元)</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input value="${car.buyTaxPrice }" name="buyTaxPrice" dataType = "/^\s*$/|/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/" errorMsg = "请填入正确的数字！" 
										class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;年检日期</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input value="${car.annualInspection }" name="annualInspection" class="colorpicker-default form-control" readonly="readonly" onClick="WdatePicker()" type="text" value=""
										style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline" style="clear:both">
							<div class="pull-left gray ps-left-text">&nbsp;车辆类型</div>
							<div class="ticket-user pull-left ps-right-box">
								<div datatype="carTypeId" class=" pull-left ">
									<select class="populate" datatype="carTypeId" id="carTypeId" name="carTypeId" style="cursor:auto;width: 200px">
										<optgroup label="选择车辆类型"></optgroup>
										<c:choose>
											<c:when test="${not empty listCarType}">
												<c:forEach items="${listCarType}" var="carType" varStatus="status">
													<option value="${carType.id}" ${(car.carTypeId==carType.id)? "selected='selected'":''}>${carType.typeName}</option>
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
								</div>
							</div>
						</li>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;车辆状态</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<select class="populate" id="stateType" name="stateType">
										<option value="1" ${(car.stateType== 1)? "selected='selected'":''}>可用</option>
										<option value="2" ${(car.stateType== 2)? "selected='selected'":''}>损坏</option>
										<option value="3" ${(car.stateType== 3)? "selected='selected'":''}>维修</option>
										<option value="4" ${(car.stateType== 4)? "selected='selected'":''}>报废</option>
									</select>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
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
						<li class="ticket-item no-shadow autoHeight no-padding">
							<div class="pull-left gray ps-left-text padding-top-10">&nbsp;申请人员范围</div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;">
								<div class="pull-left gray ps-left-text padding-top-10">
									<div style="float: left; width: 250px;display:none;">
										<select list="listScopeUser" listkey="userId" listvalue="userName" id="listScopeUser_userId" name="listScopeUser.userId" ondblclick="removeClick(this.id)" multiple="multiple"
											moreselect="true" style="width: 100%; height: 100px;">
											<c:forEach items="${listScopeUser}" var="scopeUser" varStatus="status">
												<option selected="selected" value="${scopeUser.id }">${scopeUser.userName }</option>
											</c:forEach>
										</select>
									</div>
									<div id="carPersonDiv" class="pull-left" style="max-width:460px">
										<c:forEach items="${listScopeUser}" var="scopeUser" varStatus="status">
											<div class="online-list margin-left-5 margin-bottom-5" style="float:left;cursor:pointer;" id="user_img_listScopeUser_userId_${scopeUser.id }"
												ondblclick="removeClickUser('listScopeUser_userId','${scopeUser.id }')" title="双击移除">
												<img src="/downLoad/userImg/${scopeUser.comId}/${scopeUser.userId}" class="user-avatar">
												<span class="user-name">${scopeUser.userName }</span>
											</div>
										</c:forEach>
									</div>
									<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5" title="人员多选"
										onclick="userMore('listScopeUser_userId','','${param.sid}','yes','carPersonDiv');" style="float: left;"><i class="fa fa-plus"></i>选择</a>

								</div>
							</div>
							<div class="ps-clear"></div>
						</li>

						<li class="ticket-item no-shadow autoHeight no-padding">
							<div class="pull-left gray ps-left-text padding-top-10">&nbsp;申请部门范围</div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;">
								<div class="pull-left gray ps-left-text padding-top-10">
									<div style="float: left;width: 250px;display: none">
										<select list="listScopeDep" listkey="depId" listvalue="depName" id="listScopeDep_depId" name="listScopeDep.depId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true"
											style="width: 100%; height: 100px;">
											<c:forEach items="${listScopeDep}" var="scopeDep" varStatus="status">
											<option selected="selected" value="${scopeDep.depId }">${scopeDep.depName }</option>
											</c:forEach>
										</select>
									</div>
									<div id="carDepDiv" class="pull-left" style="max-width:460px">
									<c:forEach items="${listScopeDep}" var="scopeDep" varStatus="status">
										<span id="dep_span_${scopeDep.depId }" style="cursor:pointer;" title="双击移除" class="label label-default margin-right-5 margin-bottom-5" ondblclick="removeClickDep('listScopeDep_depId',${scopeDep.depId })">${scopeDep.depName }</span>
											</c:forEach>
									</div>
									<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5" title="部门多选" onclick="depMore('listScopeDep_depId','','${param.sid}','null','carDepDiv');"><i
										class="fa fa-plus"></i>选择</a>
								</div>
							</div>
							<div class="ps-clear"></div>
						</li>

						<li class="ticket-item no-shadow autoHeight ps-listline">
							<div class="pull-left gray ps-left-text padding-top-10">&nbsp;相关附件</div>
							<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
								<div class="margin-top-10">
									<tags:uploadMore name="listCarUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
								</div>
							</div>
							<div class="ps-clear"></div>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="widget-body no-shadow"></div>
	</div>
</body>
</html>
