<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<form action="/financial/writeOff/listWriteOff" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="flowState" value="${writeOff.flowState}">
									<input type="hidden" name="flowName" value="${writeOff.flowName}">
									<input type="hidden" name="executor" value="${writeOff.executor}">
									<input type="hidden" name="executorName" value="${writeOff.executorName}">
									<input type="hidden" name="orderBy" value="${writeOff.orderBy}">
									<input type="hidden" name="creator" value="${writeOff.creator}">
									<input type="hidden" name="creatorName" value="${writeOff.creatorName}">
									<input type="hidden" name="spState" value="${writeOff.spState}">
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="排序">
													<c:choose>
														<c:when test="${not empty writeOff.orderBy}">
															<font style="font-weight:bold;"> <c:if test="${writeOff.orderBy=='executor'}">按审批人</c:if> <c:if test="${writeOff.orderBy=='crTimeNewest'}">按创建时间(降序)</c:if> <c:if
																	test="${writeOff.orderBy=='crTimeOldest'}">按创建时间(升序)</c:if>
															</font>
														</c:when>
														<c:otherwise>排序</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="orderByClean()">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('executor');">按审批人</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeNewest');">按创建时间(降序)</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeOldest');">按创建时间(升序)</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="流程状态筛选">
													<c:choose>
														<c:when test="${not empty writeOff.flowState}">
															<font style="font-weight:bold;"> <c:if test="${writeOff.flowState==1}">审批中的</c:if> <c:if test="${writeOff.flowState==4}">审批完的</c:if> <c:if test="${writeOff.flowState==2}">待发起的</c:if>
															</font>
														</c:when>
														<c:otherwise>流程状态筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="flowStateClean()">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectByFlowState(1);">审批中的</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectByFlowState(4);">已完成的</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectByFlowState(2);">待发起的</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="审批结果筛选">
													<c:choose>
														<c:when test="${not empty writeOff.spState}">
															<font style="font-weight:bold;"> <c:if test="${writeOff.spState==0}">驳回</c:if> <c:if test="${writeOff.spState==1}">通过</c:if>
															</font>
														</c:when>
														<c:otherwise>审批结果筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="spStateClean()">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectBySpState(0);">驳回</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectBySpState(1);">通过</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="当前审批人筛选">
													<c:choose>
														<c:when test="${not empty writeOff.executor}">
															<font style="font-weight:bold;">${writeOff.executorName}</font>
														</c:when>
														<c:otherwise>当前审批人筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="userOneForUserIdCallBack('',
												'executor','','')">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="userMoreForUserId('${param.sid}','executor');">人员选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listExecutor" listkey="id" listvalue="userName" id="executor_select" name="listExecutor.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${writeOff.listExecutor }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
												</c:forEach>
											</select>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													<c:choose>
														<c:when test="${not empty writeOff.startDate || not empty writeOff.endDate}">
															<font style="font-weight:bold;">筛选中</font>
														</c:when>
														<c:otherwise>
				                                            	更多
	                                            			</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">起止时间：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${writeOff.startDate}" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${writeOff.endDate}" id="endDate" name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
													<div class="ps-clear padding-top-10" style="text-align: center;">
														<button type="submit" class="btn btn-primary btn-xs">查询</button>
														<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="batchOpt" style="display: none">
										<div class="btn-group pull-left">
											<c:if test="${empty delete}">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" onclick="delSpFlow()"> 批量删除 </a>
													</div>
												</div>
											</c:if>
										</div>
									</div>
								</form>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input id="searchFlowName" value="${writeOff.flowName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
										<a href="#" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
								<c:if test="${empty add}">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" name="addWriteOff" onclick="javascript:void(0);"><i class="fa fa-plus"></i>报销申请</button>
										<c:if test="${userInfo.admin>0}">
											<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" name="busMapFlowCfg" onclick="javascript:void(0);"><i class="fa fa-wrench"></i>映射配置</button>
										</c:if>
									</div>
								</c:if>
							</div>
							<!-- 筛选条件显示 -->
							<div class=" padding-top-10 text-left " style="display:${empty writeOff.listExecutor ? 'none':'block'}">
								<strong>当前审批人筛选:</strong>
								<c:forEach items="${writeOff.listExecutor }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('executor','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty listWriteOff}">
						<div class="widget-body">
							<form action="/workFlow/delSpFlow?sid=${param.sid}" method="post"
								id="delForm">
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover" id="editabledatatable">
									<thead>
										<tr role="row">
											<th><label> <input type="checkbox"
													class="colored-blue" id="checkAllBox"
													onclick="checkAll(this,'ids')"> <span class="text"
													style="color: inherit;"></span>
											</label>
											</th>
											<th>销账对象(借款记录)</th>
											<th>销账流程</th>
											<th>流程状态</th>
											<th>当前审批人</th>
											<th>审批结果</th>
											<th>申请人</th>
											<th>申请时间</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listWriteOff}" var="writeOffVo" varStatus="vs">
											<tr class="optTr">
												<td class="optTd" style="height: 47px"><label
													class="optCheckBox" style="display: none;width: 20px">
														<input class="colored-blue" type="checkbox" name="ids" value="${writeOffVo.instanceId}"
														${(writeOffVo.creator==userInfo.id and writeOffVo.flowState==2)?'':'disabled="disabled"' } /> <span class="text"></span>
												</label> <label class="optRowNum"
													style="display: block;width: 20px">${vs.count}</label>
												</td>
												<td>
													<a href="javascript:void(0);" onclick="viewSpFlow('${writeOffVo.loanInstanceId}');">
														<tags:cutString num="31">${writeOffVo.loanSpFlowName}</tags:cutString> 
													</a>
												</td>
												<td>
													<a href="javascript:void(0);" onclick="viewSpFlow('${writeOffVo.instanceId}');">
														<tags:cutString num="31">${writeOffVo.flowName}</tags:cutString> 
													</a>
												</td>
												<td>
													<c:if test="${writeOffVo.flowState==0}">
														<span class="gray" style="font-weight:bold;">无效的</span>
													</c:if> <c:if test="${writeOffVo.flowState==1}">
														<span class="green" style="font-weight:bold;">审批中</span>
													</c:if> <c:if test="${writeOffVo.flowState==2}">
														<span style="color:fuchsia;font-weight:bold;">草稿</span>
													</c:if> <c:if test="${writeOffVo.flowState==4}">
														<span class="red" style="font-weight:bold;">完结</span>
													</c:if>
												</td>
												<td class="center">
													<div class="ticket-user pull-left other-user-box">
														<c:choose>
															<c:when test="${writeOffVo.flowState==2 || writeOffVo.flowState==4}">
									 							--
									 						</c:when>
															<c:otherwise>
																<div class="ticket-user pull-left other-user-box" data-container="body" data-placement="left" data-toggle="popover">
																	<img class="user-avatar" src="/downLoad/userImg/${writeOffVo.comId}/${writeOffVo.executor}?sid=${param.sid}" title="${writeOffVo.executorName}" />
																	<i class="user-name">${writeOffVo.executorName}</i>
																</div>
															</c:otherwise>
														</c:choose>
													</div>
												</td>
												<td class="center">
													<c:choose>
														<c:when test="${writeOffVo.flowState==4 }">
															<c:if test="${writeOffVo.spState==0}">
																<span class="red" style="font-weight:bold;">驳回</span>
															</c:if>
															<c:if test="${writeOffVo.spState==1}">
																<span class="green" style="font-weight:bold;">通过</span>
															</c:if>
														</c:when>
														<c:otherwise>
															--
														</c:otherwise>
													</c:choose>
												</td>
												<td>
													<div class="ticket-user pull-left other-user-box">
														<img class="user-avatar"
															src="/downLoad/userImg/${writeOffVo.comId}/${writeOffVo.creator}?sid=${param.sid}"
															title="${writeOffVo.creatorName}" />
														<span class="user-name">${writeOffVo.creatorName}</span>
													</div>
												</td>
												<td>${writeOffVo.recordCreateTime}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/financial/writeOff/listWriteOff"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>没有相关数据！</h2>
										<p class="description">协同提高效率，分享拉近距离。</p>
									</div>
								</section>
							</div>
						</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<!-- /Page Container -->
	<script type="text/javascript">
		$(function(){
			$(".subform").Validform({
				tiptype : function(msg, o, cssctl) {
					validMsg(msg, o, cssctl);
				},
				showAllError : true
			});
			$("[name='busMapFlowCfg']").click(function(){//业务关联配置
				window.location.href="/adminCfg/financial/writeOff/cfg?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.3";
			});
				
			$("[name='addWriteOff']").click(function(){//发起报销
				if("${busMapFlow.id}"){
					window.top.layer.confirm('<span class="red">为准确对业务数据进行统计分析；</br>请慎重选择“报销类型”。</span>', {
						  btn: ['借款报销','普通报销'], //按钮
					      title:'报销类型',
					 	  icon:7
					}, function(index){
						listLoanToSelect("${busMapFlow.id}");
						layer.close(index);
					}, function(){
						var url = "/financial/writeOff/addLoanWriteOff?sid=${param.sid}&busMapFlowId=${busMapFlow.id}";
						openWinByRight(url);
					});
				}else{
					window.top.layer.confirm("请联系系统管理员配置“报销流程”！", {
				 		  btn: ['确定']//按钮
				 	  ,title:'提示框'
				 	  ,icon:2
				 	});
				}
			});
		})
	</script>
</body>
</html>
