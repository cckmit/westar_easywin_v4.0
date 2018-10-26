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
			<span class="widget-caption blue">基础配置</span>
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
							<i class="fa fa-bookmark blue"></i> &nbsp;标题 <span style="color: red">*</span>
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<div class="pull-left">
								<input id="instituTitle" datatype="input,sn" defaultLen="52" name="title" nullmsg="请填写制度标题" class="colorpicker-default form-control" type="text" value="${institution.title }"
									onpropertychange="handleName()" onkeyup="handleName()" style="width: 400px;float: left"> <span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
							</div>
						</div> <script>
													//当状态改变的时候执行的函数 
													function handleName() {
														var value = $('#instituTitle').val();
														var len = charLength(value);
														if (len % 2 == 1) {
															len = (len + 1) / 2;
														} else {
															len = len / 2;
														}
														if (len > 26) {
															$('#msgTitle').html("(<font color='red'>" + len + "</font>/26)");
														} else {
															$('#msgTitle').html("(" + len + "/26)");
														}
													}
													//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
													if (/msie/i.test(navigator.userAgent)) { //ie浏览器 
														document.getElementById('instituTitle').onpropertychange = handleName
													} else { //非ie浏览器，比如Firefox 
														document.getElementById('instituTitle').addEventListener("input", handleName, false);
													}
												</script>
					</li>
					<li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-volume-off blue" style="font-size: 15px"></i> &nbsp;制度类型 <span style="color: red">*</span>
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<div class=" pull-left ">
								<select class="populate" id="instituType" name="instituType" style="cursor:auto;width: 200px">
									<optgroup label="选择制度类型"></optgroup>
									<c:choose>
										<c:when test="${not empty listInstituType}">
											<c:forEach items="${listInstituType}" var="instituType" varStatus="status">
												<option value="${instituType.id}" ${instituType.id eq institution.instituType ? "selected=\"selected\"":""}>${instituType.typeName}</option>
											</c:forEach>
										</c:when>
									</c:choose>
								</select>
							</div>
						</div>
					</li>
					<li class="ticket-item no-shadow ps-listline" style="clear:both">
						<div class="pull-left gray ps-left-text">
							<i class="fa fa-user blue"></i> &nbsp;查看范围
						</div>
						<div class="ticket-user pull-left ps-right-box" id="scope" style="min-width: 135px">
							<label class="padding-left-5">
								<button id="dep" class="btn btn-info btn-primary btn-xs" type="button">按部门</button>
							</label> <label class="padding-left-5">
								<button id="user" class="btn btn-info btn-primary btn-xs" type="button">按人员</button>
							</label> <span style="color: red">*</span> <span>未设置查看范围则全体成员可见</span>
						</div>

						<div style="float: left;width: 250px;display: none">
							<select list="listScopeDep" listkey="id" listvalue="depName" id="scopeDep_select" name="listScopeDep.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
								<c:forEach items="${scopeDep }" var="obj" varStatus="vs">
									<option selected="selected" value="${obj.id }">${obj.depName }</option>
								</c:forEach>
							</select>
						</div>
						<div style="float: left;width: 250px;display: none">
							<select list="listScopeUser" listkey="userId" listvalue="userName" id="scopeUser_select" name="listScopeUser.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
								<c:forEach items="${scopeUser }" var="obj" varStatus="vs">
									<option selected="selected" value="${obj.id }">${obj.userName }</option>
								</c:forEach>
							</select>
						</div>
					</li>
					<li id="depLi" class="ticket-item no-shadow ps-listline" style="clear:both; display:${empty scopeDep ? 'none':'block'}">
						<div class="pull-left gray ps-left-text">&nbsp;部门</div>
						<div class="pull-left gray ps-left-text padding-left-5" style="width:70%" id="depBox">
							<c:forEach items="${scopeDep }" var="dep">
								<c:set var="creator">
															title="双击移除" ondblclick="removeChoose('dep','${dep.comId}','${dep.id }',this)"
														</c:set>
								<span style="cursor:pointer;" class="label label-default margin-right-5 margin-bottom-5" ${creator }>${dep.depName }</span>
							</c:forEach>
						</div>
						<div style="clear:both;"></div>
					</li>
					<li id="userLi" class="ticket-item no-shadow ps-listline" style="clear:both;display:${empty scopeUser ? 'none':'block'}">
						<div class="pull-left gray ps-left-text">&nbsp;人员</div>
						<div class="pull-left gray ps-left-text padding-left-5" style="width:70%" id="userBox">
							<c:forEach items="${scopeUser }" var="user">
								<c:set var="creator">
															title="双击移除" ondblclick="removeChoose('user','${user.comId}','${user.id }',this)"
														</c:set>
								<span style="cursor:pointer;" title="双击移除" class="label label-default margin-right-5 margin-bottom-5" ${creator }>${user.userName }</span>
							</c:forEach>
						</div>
						<div style="clear:both;"></div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="widget radius-bordered  " id="remark" style="clear:both">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue"><span style="color: red">*</span>制度内容</span>
			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-plus blue"></i>
				</a>
			</div>
		</div>
		<!--tip="请填写制度内容"   -->
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<textarea class="form-control" id="instituRemark" name="instituRemark" rows="" cols="" style="width: 100%;height: 110px;display:none;">${institution.instituRemark}</textarea>
				<iframe id="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=instituRemark&style=blue" frameborder="0" scrolling="no" width="100%" height="350"></iframe>
				<button type="button" onclick="saveRemark()" class="btn btn-info ws-btnBlue pull-right">保存</button>
			</div>
		</div>
	</div>
	<div class="widget-body no-shadow"></div>
	</div>

</body>
</html>
