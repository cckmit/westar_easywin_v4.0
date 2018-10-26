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
	<div class="page-content">
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 no-padding-right">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						
						<div>
							<form action="/task/listPagedTaskForSupView" id="searchForm" class="subform">
                                <input type="hidden" name="sid" id="sid" value="${param.sid}">
								<input type="hidden" name="pager.pageSize" value="10"> 
								<input type="hidden" name="activityMenu" value="${param.activityMenu}">
								<input type="hidden" name="state" id="state" value="${task.state}"> 
								<input type="hidden" name="taskName" id="taskName" value="${task.taskName}"> 
								<input type="hidden" name="attentionState" value="${task.attentionState}" /> 
								<input type="hidden" name="execuType" value="${task.execuType}" /> 
								<input type="hidden" name="overdue" id="overdue" value="${task.overdue}"> 
								<input type="hidden" name="orderBy" id="orderBy" value="${task.orderBy}"> 
								<input type="hidden" name="grade" id="grade" value="${task.grade}">
								<input type="hidden" name="relateModType" value="${task.relateModType}">
								<div class="btn-group pull-left searchCond">
									<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown"> <c:choose>
														<c:when test="${not empty task.orderBy}">
															<font style="font-weight:bold;">
																<c:choose>
																	<c:when test="${task.orderBy=='grade'}">按紧急程度</c:when>
																	<c:when test="${task.orderBy=='owner'}">按负责人</c:when>
																	<c:when test="${task.orderBy=='executor'}">按办理人</c:when>
																	<c:when test="${task.orderBy=='crTimeNewest'}">按任务创建时间(降序)</c:when>
																	<c:when test="${task.orderBy=='crTimeOldest'}">按任务创建时间(升序)</c:when>
																	<c:when test="${task.orderBy=='limitTimeNewest'}">按任务到期时间(降序)</c:when>
																	<c:when test="${task.orderBy=='limitTimeOldest'}">按任务到期时间(升序)</c:when>
																</c:choose>
															</font>
														</c:when>
														<c:otherwise>排序</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)" class="clearThisElement" relateElement="orderBy">不限条件</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="grade">按紧急程度</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="owner">按负责人</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="executor">按办理人</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="crTimeNewest">按任务创建时间(降序)</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="crTimeOldest">按任务创建时间(升序)</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="limitTimeNewest">按任务到期时间(降序)</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="limitTimeOldest">按任务到期时间(升序)</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown">
													关联模块筛选
													 <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearMoreElement" relateList="relateModes_select">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="relateModSelect" busType="005" isMore="1" typeValue="relateModType" relateList="relateModes_select">项目选择</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="relateModSelect" busType="012" isMore="1" typeValue="relateModType" relateList="relateModes_select">客户选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listRelateModes" listkey="busId" listvalue="busName" id="relateModes_select" name="listRelateModes.busId" multiple="multiple"
												moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${task.listRelateModes }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.busId}">${obj.busName }</option>
												</c:forEach>
											</select>
										</div>
									<div class="table-toolbar ps-margin margin-right-5">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown">
												办理人筛选
												<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li>
													<a href="javascript:void(0)" class="clearMoreElement" relateList="executor_select">不限条件</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="userMoreElementSelect" relateList="executor_select">人员选择</a>
												</li>
											</ul>
										</div>
									</div>
									<div style="float: left;width: 250px;display: none">
										<select list="listExecutor" listkey="id" listvalue="userName" id="executor_select" name="listExecutor.id" multiple="multiple"
											moreselect="true" style="width: 100%; height: 100px;">
											<c:forEach items="${task.listExecutor }" var="obj" varStatus="vs">
												<option selected="selected" value="${obj.id }">${obj.userName }</option>
											</c:forEach>
										</select>
									</div>
									<div style="float: left;width: 250px;display: none">
										<select list="listOperator" listkey="id" listvalue="userName" id="operator_select" name="listOperator.id" multiple="multiple"
											moreselect="true" style="width: 100%; height: 100px;">
											<c:forEach items="${task.listOperator }" var="obj" varStatus="vs">
											<option selected="selected" value="${obj.id }">${obj.userName }</option>
											</c:forEach>
										</select>
									</div>
									<%-- <div class="table-toolbar ps-margin margin-right-5">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown"> <c:choose>
													<c:when test="${not empty task.ownerName}">
														<font style="font-weight:bold;">${task.ownerName}</font>
													</c:when>
													<c:otherwise>负责人筛选</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li>
													<a href="javascript:void(0)" class="clearMoreElement" relateList="owner_select">不限条件</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="userMoreElementSelect" relateList="owner_select">人员选择</a>
												</li>
											</ul>
										</div>
									</div> --%>
									<div style="float: left;width: 250px;display: none">
										<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple"
											moreselect="true" style="width: 100%; height: 100px;">
											<c:forEach items="${task.listOwner }" var="obj" varStatus="vs">
											<option selected="selected" value="${obj.id }">${obj.userName }</option>
											</c:forEach>
										</select>
									</div>
									<div style="float: left;width: 250px;display: none">
										<select list="listExecuteDep" listkey="id" listvalue="depName" id="executeDep_select" name="listExecuteDep.id" multiple="multiple"
											moreselect="true" style="width: 100%; height: 100px;">
											<c:forEach items="${task.listExecuteDep }" var="obj" varStatus="vs">
												<option selected="selected" value="${obj.id }">${obj.depName }</option>
											</c:forEach>
										</select>
									</div>
									<div class="table-toolbar ps-margin margin-right-5">
										<div class="btn-group cond" id="moreCondition_Div">
											<a class="btn btn-default dropdown-toggle btn-xs"
												onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
													<c:when
														test="${not empty task.startDate 
														|| not empty task.endDate 
														|| not empty task.operatStartDate
														|| not empty task.operatEndDate 
														|| not empty task.state
														|| task.overdue==true
														|| not empty task.listExecuteDep[0]
                                                        || not empty task.listOwner[0]}">
														<font style="font-weight:bold;">筛选中</font>
													</c:when>
													<c:otherwise>
				                                            	更多
	                                            			</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">任务状态：</span>
														<button type="button" id="toDoState" class="btn ${task.state==1?'btn-primary':'btn-default'} btn-xs  stateBtn"
															onclick="toDoStateFilter('1',this)">进行中</button>
														<button type="button" class="btn ${task.state==3?'btn-primary':'btn-default'} btn-xs margin-left-10 stateBtn"
															onclick="doneStateFilter('3',this)">暂停中</button>
														<button type="button"
															class="btn ${task.state==4?'btn-primary':'btn-default'} btn-xs margin-left-10 stateBtn"
															onclick="doneStateFilter('4',this)">已完成</button>
													</div>
												<!-- 下属执行则没有状态选择 -->
												<div class="ps-clear ps-margin ps-search padding-left-10">
													<span class="btn-xs">执行部门：</span>
													<button type="button" id="toDoState" class="btn btn-default btn-xs  stateBtn"
														onclick="depMoreForDepId('${param.sid}','executeDep')">部门选择</button>
												</div>
												 <div class="ps-clear ps-margin ps-search padding-left-10">
                                                    <span class="btn-xs">负&nbsp;&nbsp;责&nbsp;&nbsp;人：</span>
                                                    <button type="button"  class="btn btn-default btn-xs  stateBtn userMoreElementSelect" relateList="owner_select">负责人选择</button>
                                                </div>
												<div class="ps-margin ps-search padding-left-10">
													<span class="btn-xs">发布时间：</span> <input
														class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${task.startDate}"
														id="startDate" name="startDate" placeholder="开始时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
													<span>~</span> <input
														class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${task.endDate}" id="endDate"
														name="endDate" placeholder="结束时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
												</div>
												<div class="ps-margin ps-search padding-left-10">
                                                    <span class="btn-xs">办理时间：</span> <input
                                                        class="form-control dpd2 no-padding condDate" type="text"
                                                        readonly="readonly" value="${task.operatStartDate}"
                                                        id="operatStartDate" name="operatStartDate" placeholder="开始时间"
                                                        onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'operatEndDate\',{d:-0});}'})" />
                                                    <span>~</span> <input
                                                        class="form-control dpd2 no-padding condDate" type="text"
                                                        readonly="readonly" value="${task.operatEndDate}" id="operatEndDate"
                                                        name="operatEndDate" placeholder="结束时间"
                                                        onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'operatStartDate\',{d:+0});}'})" />
                                                </div>
												<div class="ps-clear padding-top-10"
													style="text-align: center;">
													<button type="submit" class="btn btn-primary btn-xs">查询</button>
													<button type="button"
														class="btn btn-default btn-xs margin-left-10 clearMoreElement"
														onclick="resetMoreCon('moreCondition_Div')" relateList="owner_select">重置</button>
												</div>
											</div>
										</div>
									</div>
								</div>
							</form>
							<div class="ps-margin ps-search searchCond">
								<span class="input-icon"> 
								<input id="searchTaskName" name="searchTaskName" value="${task.taskName}" class="form-control ps-input" type="text" placeholder="任务名称关键字">
									<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
								</span>
							</div>
							<c:if test="${empty add}">
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs addBusTask" type="button" busType="005">
										<i class="fa fa-plus"></i> 项目任务
									</button>
									<button class="btn btn-info btn-primary btn-xs margin-left-5 addBusTask" type="button" busType="012">
										<i class="fa fa-plus"></i> 客户任务
									</button>
									<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button"
										onclick="addTask('${param.sid}');">
										<i class="fa fa-plus"></i> 普通任务
									</button>
								</div>
							</c:if>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty task.listExecutor ? 'none':'block'}">
								<strong>办理人筛选:</strong>
								<c:forEach items="${task.listExecutor }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('executor','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
								</c:forEach>
							</div>
							<div  class=" padding-top-10 text-left " style="display:${empty task.listOwner ? 'none':'block'}">
								<strong>负责人筛选:</strong>
								<c:forEach items="${task.listOwner }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
								</c:forEach>
							</div>
							<div class="padding-top-10 text-left executeDepSpanDiv" style="display:${empty task.listExecuteDep ? 'none':'block'}">
								<strong>执行部门筛选:</strong>
								<c:forEach items="${task.listExecuteDep }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('executeDep','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.depName }</span>	
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty task.relateModType or empty task.listRelateModes ? 'none':'block'}">
								<strong>
									<c:choose>
										<c:when test="${task.relateModType == '005'}">项目筛选:</c:when>
										<c:when test="${task.relateModType == '012'}">客户筛选:</c:when>
									</c:choose>
								</strong>
								<c:forEach items="${task.listRelateModes }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('relateModes','${param.sid}','${obj.busId }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.busName }</span>	
								</c:forEach>
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty list}">
						<div class="widget-body">
							<form method="post" id="delForm">
								<table class="table table-striped table-hover fixTable" id="editabledatatable">
									<thead>
										<tr role="row">
											<th style="width: 30px">&nbsp;</th>
											<th>任务名称</th>
											<th>关联模块</th>
											<th style="width: 50px">进度</th>
											<th style="width: 90px">接收日期</th>
											<th style="width: 90px">完成日期</th>
											
											<th style="width: 90px">办理时限</th>
											<th style="width: 40px" class="no-padding">预警</th>
											<th style="width: 85px">办理人</th>
											<th style="width: 90px">发布日期</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${list}" var="taskVo" varStatus="vs">
											<tr class="optTr" taskID="${taskVo.id}" style="clear: both">
												<td class="optTd" style="height: 47px">
													<label style="display: block;width: 20px" class="optRowNum">${vs.count}</label></td>
												<td>
													<a href="javascript:void(0);" title="${taskVo.taskName}" class="${taskVo.isRead==0?" noread":"" }"
													onclick="readMod(this,'taskCenter','${taskVo.id }', '003');viewTask('${param.sid}','${taskVo.id}');">
														${taskVo.taskName}
													</a>
												</td>
												<td>
													<c:set var="busTypeName">
														<c:choose>
															<c:when test="${taskVo.busType=='005' }">
																[项目]
															</c:when>
															<c:when test="${taskVo.busType=='012' }">
																[客户]
															</c:when>
															<c:when test="${taskVo.busType=='022' }">
																[审批]
															</c:when>
														</c:choose>
													</c:set>
													<c:choose>
														<c:when test="${empty taskVo.busType or taskVo.busType=='0'  }">
															未关联
														</c:when>
														<c:otherwise>
															<a href="javascript:void(0)" title="${taskVo.busName}" class="viewModInfo" busType="${taskVo.busType}" busId="${taskVo.busId}">${busTypeName}${taskVo.busName}</a>
														</c:otherwise>
													</c:choose>
												</td>
												<td class="no-padding" align="center">
													<c:choose>
														<c:when test="${not empty taskVo.operatEndDate }">
															--
														</c:when>
														<c:when test="${taskVo.state==3}"><font style="color:gray;">已暂停</font></c:when>
														<c:when test="${taskVo.state==4}">
															<font style="color:red;">已完成</font>
														</c:when>
														<c:otherwise>
															<font style="color:green;">${(not empty taskVo.taskProgress and taskVo.taskProgress > 0)?taskVo.taskProgress:0}%</font>
														</c:otherwise>
													</c:choose>
												</td>
												
												<td class="no-padding" align="center">
													${fn:substring(taskVo.operatStartDate,0,10) }
												</td>
												<td class="no-padding" align="center">
													<c:choose>
														<c:when test="${not empty taskVo.operatEndDate }">
															${fn:substring(taskVo.operatEndDate,0,10) }
														</c:when>
														<c:otherwise>
															--
														</c:otherwise>
													</c:choose>
												</td>
												
												
												
												<td class="center">
													${taskVo.dealTimeLimit}
												</td>
												<td>
													<img src="/static/images/light_${taskVo.overDueLevel}.gif">
												</td>
												<td class="no-padding-left no-padding-right" align="center">
													<div class="ticket-user pull-left other-user-box">
														<div class="ticket-user pull-left other-user-box">
															<img class="user-avatar" alt="${taskVo.executorName}"
															src="/downLoad/userImg/${userInfo.comId }/${taskVo.executor }"/>
															<i class="user-name">${taskVo.executorName}</i>
														</div>
													</div>
												</td>
												
												<td>${fn:substring(taskVo.recordCreateTime,0,10) }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/task/listPagedTaskForSupView"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>您还没有相关任务数据！</h2>
										<p class="description">协同提高效率，分享拉近距离。</p>
										<a href="javascript:void(0);" onclick="addTask('${param.sid}');"
											class="return-btn"><i class="fa fa-plus"></i>普通任务</a>
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
		})
	$(function(){
		taskFormRowSpan($("#editabledatatable"));
		if($("#toDoState").hasClass("btn-primary")){
			$("#overDiv").show();
		}
		var selectData = {};
		var selects = $("#searchForm").find("select");
		if(selects && selects.get(0)){
			$.each(selects,function(selectIndex,select){
				var list =$(select).attr("list");
				var listkey =$(select).attr("listkey");
				var listvalue =$(select).attr("listvalue");
				var options = $(select).find("option");
				if(options && options.get(0)){
					$.each(options,function(index,option){
						selectData[list+"["+index+"]."+listkey]  = $(option).val();
						selectData[list+"["+index+"]."+listvalue]  = $(option).html();
					})
				}
			})
		}
	});
		
		//合并行数据
		function taskFormRowSpan(table){
			var trs = $(table).find("tbody").find("tr");
			if(trs && trs.get(0)){
				var preTaskIdT = 0;
				var rowSpan = 1;
				$.each(trs,function(index,trObj){
					var taskId = $(trObj).attr("taskId");
					if(taskId == preTaskIdT){
						rowSpan = rowSpan+1;
						//开始合并
						var firstTr = $(table).find("tbody").find("tr[taskId='"+taskId+"']:eq(0)");
						$(firstTr).find("td:eq(0)").attr("rowspan",rowSpan);
						$(firstTr).find("td:eq(1)").attr("rowspan",rowSpan);
						$(firstTr).find("td:eq(2)").attr("rowspan",rowSpan);
						
						var td0 = $(trObj).find("td:eq(0)");
						var td1 = $(trObj).find("td:eq(1)");
						var td2 = $(trObj).find("td:eq(2)");
						
						$(td0).remove();
						$(td1).remove();
						$(td2).remove();
					}else{
						preTaskIdT = taskId;
						rowSpan = 1;
					}
				});
				
				var subIndexA = 1;
				var preTaskIdT = 0;
				//重新索引号
				$.each(trs,function(index,trObj){
					var taskId = $(trObj).attr("taskId");
					
					if(taskId != preTaskIdT){
						preTaskIdT = taskId;
						$(trObj).find("td:eq(0)").find(".optRowNum").html(subIndexA);
						subIndexA = subIndexA+1;
					}
				})
			}
		}
</script>
</body>
</html>
