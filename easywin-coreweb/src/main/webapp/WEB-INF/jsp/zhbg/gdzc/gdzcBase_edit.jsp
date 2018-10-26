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
								&nbsp;资产编号
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="gdzcNum" datatype="*" name="gdzcNum" value="${gdzc.gdzcNum }" nullmsg="请输入资产编号" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;资产名称
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="gdzcName" datatype="*" name="gdzcName" value="${gdzc.gdzcName }" nullmsg="请输入资产名称" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline" style="clear:both">
							<div class="pull-left gray ps-left-text">
								&nbsp;资产类型
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div datatype="ssType" class=" pull-left ">
									<select class="populate" datatype="ssType" id="ssType" name="ssType" style="cursor:auto;width: 200px">
										<optgroup label="选择资产类型"></optgroup>
										<c:choose>
											<c:when test="${not empty listGdzcType}">
												<c:forEach items="${listGdzcType}" var="gdzcType" varStatus="status">
													<option value="${gdzcType.id}" ${(gdzc.ssType==gdzcType.id)? "selected='selected'":''}>${gdzcType.typeName}</option>
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;所属部门
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<input type="hidden" name="depId" id="depId" value="${gdzc.depId }" dataType="*" nullMsg="请选择所属部门！">
								<span id="depName" style="cursor:pointer;" class="label label-default margin-right-5 margin-bottom-5">${gdzc.depName }</span>
								<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5" title="部门单选" onclick="depOne('depId','depName','','${param.sid }','yes');"><i class="fa fa-plus"></i>选择</a>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;保管人
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<tags:userOne datatype="s" name="manager" defaultInit="true" showValue="${gdzc.managerName}" value="${gdzc.manager}" uuid="${gdzc.userUuid}" filename="${gdzc.userFileName}"
									gender="${gdzc.gender}" onclick="true" showName="managerName"></tags:userOne>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;添加日期
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="addDate" name="addDate" dataType="*" value="${gdzc.addDate }" class="colorpicker-default form-control" readonly="readonly" onClick="WdatePicker()" nullmsg="请选择添加日期！" type="text"
										value="" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline" style="clear:both">
							<div class="pull-left gray ps-left-text">
								&nbsp;添加类型
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div datatype="addType" class=" pull-left ">
									<select class="populate" datatype="addType" id="addType" name="addType" style="cursor:auto;width: 200px">
										<optgroup label="选择添加类型"></optgroup>
										<c:choose>
											<c:when test="${not empty listAddGdzcType}">
												<c:forEach items="${listAddGdzcType}" var="addGdzcType" varStatus="status">
													<option value="${addGdzcType.id}" ${(gdzc.addType==addGdzcType.id)? "selected='selected'":''}>${addGdzcType.typeName}</option>
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;资产原值(元)
								<span style="color: red">*</span>
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="gdzcPrice" datatype="/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/" value="${gdzc.gdzcPrice }" errormsg="请输入正确的浮点数！" name="gdzcPrice" nullmsg="请输入资产原值"
										class="colorpicker-default form-control" type="text" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;资产残值率(%)</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="residualRate" name="residualRate" value="${gdzc.residualRate }" dataType="/^\s*$/|/^(([0-9]+)|([0-9]+\.[0-9]{1,3}))$/" errormsg="请输入正确的浮点数！"
										class="colorpicker-default form-control" type="text" style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;资产折旧年限</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<input id="depreciationYear" name="depreciationYear" value="${gdzc.depreciationYear }" dataType="/^\s*$/|n" class="colorpicker-default form-control" type="text"
										style="width: 200px;float: left">
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;固定资产状态</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
									<select class="populate" id="state" name="state">
										<option value="1" ${(gdzc.state== 1)? "selected='selected'":''}>使用中</option>
										<option value="2" ${(gdzc.state== 2)? "selected='selected'":''}>闲置中</option>
										<option value="3" ${(gdzc.state== 3)? "selected='selected'":''}>维修中</option>
										<option value="4" ${(gdzc.state== 4)? "selected='selected'":''}>已减少</option>
									</select>
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow autoHeight ps-listline">
							<div class="pull-left gray ps-left-text padding-top-10">&nbsp;相关附件</div>
							<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
								<div class="margin-top-10">
									<tags:uploadMore name="listGdzcUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
								</div>
							</div>
							<div class="ps-clear"></div>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
