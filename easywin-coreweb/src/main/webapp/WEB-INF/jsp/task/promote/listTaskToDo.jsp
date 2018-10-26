<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
				<div class="row">
					<div class="col-md-9 col-xs-12 ">
						<div class="widget">
							<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<form action="/task/listTaskToDoPage" id="searchForm" class="subform">
									<input type="hidden" name="sid" id="sid" value="${param.sid}">
									<input type="hidden" name="version" id="version" value="${param.version}">
									<input type="hidden" id="pageSize" name="pager.pageSize" value="10"> 
									<input type="hidden" name="activityMenu" value="${param.activityMenu}"> 
									<input type="hidden" name="taskName" id="taskName" value="${task.taskName}"> 
									<input type="hidden" name="overdue" id="overdue" value="${task.overdue}"> 
									<input type="hidden" name="orderBy" id="orderBy" value="${task.orderBy}">
									<input type="hidden" name="countType" id="countType" value="${task.countType}">
									<input type="hidden" name="grade" id="grade" value="${task.grade}">
									<input type="hidden" name="relateModType" value="${task.relateModType}">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown"> <c:choose>
														<c:when test="${not empty task.orderBy}">
															<font style="font-weight:bold;">
																<c:choose>
																	<c:when test="${task.orderBy=='readState'}">按消息未读</c:when>
																	<c:when test="${task.orderBy=='grade'}">按紧急程度</c:when>
																	<c:when test="${task.orderBy=='crTimeNewest'}">按任务创建时间(降序)</c:when>
																	<c:when test="${task.orderBy=='crTimeOldest'}">按任务创建时间(升序)</c:when>
																	<c:when test="${task.orderBy=='limitTimeNewest'}">按任务到期时间(降序)</c:when>
																	<c:when test="${task.orderBy=='limitTimeOldest'}">按任务到期时间(升序)</c:when>
																	<c:when test="${task.orderBy=='handTimeNewest'}">按任务接收时间(降序)</c:when>
																	<c:when test="${task.orderBy=='handTimeOldest'}">按任务接收时间(升序)</c:when>
																</c:choose>
															</font>
														</c:when>
														<c:otherwise>排序</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)" class="clearThisElement" relateElement="orderBy">不限条件</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="readState">按消息未读</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="grade">按紧急程度</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="crTimeNewest">按任务创建时间(降序)</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="crTimeOldest">按任务创建时间(升序)</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="limitTimeNewest">按任务到期时间(降序)</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="limitTimeOldest">按任务到期时间(升序)</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="handTimeNewest">按任务接收时间(降序)</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="handTimeOldest">按任务接收时间(升序)</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown">
													推送人筛选
													 <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearMoreElement" relateList="fromUser_select">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="userMoreElementSelect" relateList="fromUser_select">人员选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listFromUser" listkey="id" listvalue="userName" id="fromUser_select" name="listFromUser.id" multiple="multiple"
												moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${task.listFromUser }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id}">${obj.userName }</option>
												</c:forEach>
											</select>
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
										<div style="float: left;width: 250px;display: none">
											<select list="listOperator" listkey="id" listvalue="userName" id="operator_select" name="listOperator.id" multiple="multiple"
												moreselect="true" style="width: 100%; height: 100px;">
											<c:forEach items="${task.listOperator }" var="obj" varStatus="vs">
											<option selected="selected" value="${obj.id }">${obj.userName }</option>
											</c:forEach>
											</select>
										</div>
										<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs"
													onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
														<c:when
															test="${not empty task.startDate || not empty task.endDate|| task.overdue==true|| not empty task.grade}">
															<font style="font-weight:bold;">筛选中</font>
														</c:when>
														<c:otherwise>
			                                            	更多
                                            			</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												
												<div class="dropdown-menu dropdown-default padding-bottom-10"
													style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
												<span class="btn-xs">时限状态：</span>
												<button type="button"  class="btn ${task.overdue==true?'btn-primary':'btn-default'} btn-xs  overBtn"
													onclick="taskOverFilte('true',this)">逾期</button>
											</div>
											<div class="ps-margin ps-search padding-left-10">
													<span class="btn-xs">紧&nbsp;&nbsp;急&nbsp;&nbsp;度：</span>
													<button type="button" class="btn  ${task.grade==4?'btn-primary':'btn-default'} btn-xs  gradeBtn " 
													onclick="taskGradeFilter('4',this)">重要且紧急</button>
													<button type="button" class="btn  ${task.grade==3?'btn-primary':'btn-default'} btn-xs  gradeBtn" 
													onclick="taskGradeFilter('3',this)">紧急</button>
													<button type="button" class="btn  ${task.grade==2?'btn-primary':'btn-default'} btn-xs  gradeBtn" 
													onclick="taskGradeFilter('2',this)">重要</button>
													<button type="button" class="btn  ${task.grade==1?'btn-primary':'btn-default'} btn-xs  gradeBtn"
													onclick="taskGradeFilter('1',this)">普通</button>
												</div>
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">起止时间：</span> <input
															class="form-control dpd2 no-padding condDate"
															type="text" readonly="readonly"
															value="${task.startDate}" id="startDate"
															name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span> <input
															class="form-control dpd2 no-padding condDate"
															type="text" readonly="readonly" value="${task.endDate}"
															id="endDate" name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
													<div class="ps-clear padding-top-10"
														style="text-align: center;">
														<button type="submit" class="btn btn-primary btn-xs">查询</button>
														<button type="button"
															class="btn btn-default btn-xs margin-left-10"
															onclick="resetMoreCon('moreCondition_Div')">重置</button>
													</div>
												</div>
											</div>
										</div>
									</div>
								</form>
								<div class="ps-margin ps-search">
									<span class="input-icon"> <input id="searchTaskName"
										name="searchTaskName" value="${task.taskName}"
										class="form-control ps-input" type="text"
										placeholder="任务名称关键字"> <a href="javaScript:void(0);"
										class="ps-searchBtn"><i
											class="glyphicon glyphicon-search circular danger"></i> </a>
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
											onclick="addTask('${param.sid}',2)">
											<i class="fa fa-plus"></i> 普通任务
										</button>
									</div>
								</c:if>
								</div>
								<div class=" padding-top-10 text-left " style="display:${empty task.listOwner ? 'none':'block'}">
								<strong >责任人筛选:</strong>
								<c:forEach items="${task.listOwner }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty task.listFromUser ? 'none':'block'}">
								<strong >推送人筛选:</strong>
								<c:forEach items="${task.listFromUser }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('fromUser','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty task.listOperator ? 'none':'block'}">
								<strong >经办人筛选:</strong>
								<c:forEach items="${task.listOperator }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('operator','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
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
								<table class="table table-striped table-hover fixTable"
									id="editabledatatable">
									<thead>
										<tr role="row">
											<th style="width: 40px" class="no-padding">序号</th>
											<th style="min-width: 120px">任务名称</th>
											<th>关联模块</th>
											<th style="width: 90px">紧急度</th>
											<th style="width: 40px" class="no-padding">预警</th>
											<th style="width: 50px">进度</th>
											<th style="width: 80px">所剩时间</th>
											<th style="width: 100px">推送人</th>
											<th style="width: 130px">推送时间</th>
											<th style="width: 60px">操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${list}" var="taskVo" varStatus="vs">
											<tr class="optTr" taskID="${taskVo.id}" style="clear: both">
												<td class="optTd" style="height: 47px"><label
													class="optCheckBox" style="display: none;width: 10px">
														<input class="colored-blue" type="checkbox" name="ids"
														value="${taskVo.id}" state="${taskVo.state }" /> <span
														class="text"></span>
												</label> <label class="optRowNum"
													style="display: block;width: 20px">${vs.count}</label>
												</td>
												<td>
													<a href="javascript:void(0);" title="${taskVo.taskName}" 
													class="${taskVo.isRead==0?"
													noread":"" }"
													onclick="readMod(this,'taskCenter','${taskVo.id }', '003');viewTask('${param.sid}','${taskVo.id}');">
													${taskVo.taskName}</a>
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
															<c:when test="${taskVo.busType=='070' }">
																[需求]
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
												<c:set var="gradeColor">
													<c:choose>
														<c:when test="${taskVo.grade==4}">label-red</c:when>
														<c:when test="${taskVo.grade==3}">label-orange</c:when>
														<c:when test="${taskVo.grade==2}">label-darkpink</c:when>
														<c:when test="${taskVo.grade==1}">label-green</c:when>
													</c:choose>
												</c:set>
												<td class="no-padding"><span class="label ${gradeColor}"> <c:choose>
															<c:when test="${taskVo.grade==4}">紧急且重要</c:when>
															<c:when test="${taskVo.grade==3}">紧急</c:when>
															<c:when test="${taskVo.grade==2}">重要</c:when>
															<c:when test="${taskVo.grade==1}">普通</c:when>
														</c:choose>
												</span>
												</td>
												<td>
													<img src="/static/images/light_${taskVo.overDueLevel}.gif">
												</td>
												<td class="no-padding" align="center">
													<c:choose>
														<c:when test="${taskVo.executeState==3 && empty taskVo.curStartTime}"><font style="color:gray;">未开始</font></c:when>
														<c:when test="${taskVo.executeState==3 && not empty taskVo.curStartTime}"><font style="color:gray;">暂停中</font></c:when>
														<c:when test="${taskVo.executeState==4}">
															<font style="color:red;">已完成</font>
														</c:when>
														<c:when test="${taskVo.executeState==0}">
															<font style="color:gray;">待认领</font>
														</c:when>
														<c:otherwise>
															<font style="color:green;">${(not empty taskVo.taskProgress and taskVo.taskProgress > 0)?taskVo.taskProgress:0}%</font>
														</c:otherwise>
													</c:choose>
												</td>
												<td>${taskVo.remainingTime }小时</td>
												<td>
													<div class="ticket-user pull-left other-user-box" data-container="body"
														data-toggle="popover" data-placement="left"
														data-user='${taskVo.pushUserId}'
														data-busId='${taskVo.id}' data-busType='003'>
															<img class="user-avatar userImg" title="${taskVo.fromUserName}" 
																src="/downLoad/userImg/${taskVo.comId}/${taskVo.pushUserId}"/>
														<span class="user-name">${taskVo.fromUserName}</span>
													</div>
												</td>
												<td>${fn:substring(taskVo.handTime,0,16) }</td>
												<td>
													<c:choose>
														<c:when test="${taskVo.executeState==3}"><a href="javascript:void(0)" onclick="remarkTaskExecuteState(${taskVo.id},1)" >开始</a></c:when>
														<c:when test="${taskVo.executeState==4}">
															<a href="javascript:void(0)" onclick="remarkTaskExecuteState(${taskVo.id},1)" >重启</a>
														</c:when>
														<c:when test="${taskVo.executeState==0}">
															<a href="javascript:void(0)" onclick="acceptTask(${taskVo.id})" >认领</a>
														</c:when>
														<c:otherwise>
															<a href="javascript:void(0)" onclick="remarkTaskExecuteState(${taskVo.id},3)" >暂停</a>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<tags:pageBar url="/task/listTaskToDoPage"></tags:pageBar>
							</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>当前没有任务需要您办理！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
											<a href="javascript:void(0);" onclick="addTask('${param.sid}',2)"
												class="return-btn"><i class="fa fa-plus"></i>普通任务</a>
										</div>
									</section>
								</div>
							</c:otherwise>
						</c:choose>
						</div>
					</div>
					<div class="col-md-3 col-xs-12" id="countMine">
						<div class="widget">
							<div class="widget-header bordered-bottom bordered-themeprimary ps-titleHeader" style="padding-top:5px">
								<i class="widget-icon fa fa-tasks themeprimary"></i> <span
									class="widget-caption themeprimary">个人统计</span>
							</div>
							<div class="widget-body" style="padding-top: 3px;padding-bottom: 20px;">
								<div class="sub-each"  style="border-bottom: 0;">
									 <div class="pull-left sub-textDiv" style="width: 33%;text-align: center;">
										 <p style="text-align:center;">
										 	<a class="sub-doingNum" id="todoNum" href="javascript:void(0);" >3个</a>
										 </p>
										 <span style="text-indent:1em;font-size:10px;">我的待办</span>
									 </div>
									 <div class="pull-left sub-line" style="left: 0px;"></div>
									 <div class="pull-left sub-textDiv" style="width: 33%;text-align: center;">
										 <p style="text-align:center;">
										 	<a class="sub-doneNum" id="doneTime" href="javascript:void(0);" onclick="statisticsTask(23,3,2)">2小时</a>
										 </p>
										 <span style="text-indent:1em;font-size:10px;text-align:center;">本周任务平均<br>处理时间</span>
									 </div>
									 <div class="pull-left sub-line" style="left: 0px;"></div>
									 <div class="pull-left sub-textDiv" style="width: 33%;text-align: center;">
										 <p style="text-align:center;">
										 	<a class="sub-doneNum" id="doneNum" href="javascript:void(0);" onclick="statisticsTask(23,3,2)">1个</a>
										 </p>
										 <span style="text-indent:1em;font-size:10px;">本周完成<br>任务数</span>
									 </div>
								</div>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test="${userInfo.countSub>0}">
						<div class="col-md-3 col-xs-12" id="countSub" >
						<div class="widget">
							<div class="widget-header bordered-bottom bordered-themeprimary ps-titleHeader" style="padding-top:5px">
								<i class="widget-icon fa fa-tasks themeprimary"></i>
								<span class="widget-caption themeprimary">任务统计</span>
								<%-- <a href="javascript:void(0)" class="widget-caption blue padding-left-5" title="关注更多" onclick="userMoreForUserId('${param.sid}')">
										<i class="fa fa-star ws-star"></i>
									</a> --%>
									<div class="widget-buttons ps-widget-buttons">
	                                    	<a href="javascript:void(0)" onclick="statisticsTask(23,3,2)">
			                                    <span class="menu-text">更多</span>
			                                </a>
	                                    </div>
							</div>
							<div class="widget-body" >
								<div id="subDiv" style="width:100%;margin-top:-10px;">
								</div>
								<div class="widget-buttons ps-widget-buttons" style="width:100%;margin-top:10px;display:none;" id="moreCount" >
									<button class="btn btn-info btn-primary btn-xs" type="button" style="width:100%;height:35px" onclick="statisticsTask(23,3,2)">
										更多统计
									</button>
								</div>
								<div class="widget-buttons ps-widget-buttons" style="width:100%;display:none;" id="attention">
									
									<div style="float: left; width: 250px;display:none;">
										<select list="listRangeUser" listkey="userId" listvalue="userName" id="listRangeUser_userId" name="listRangeUser.userId" ondblclick="removeClick(this.id)" multiple="multiple"
											moreselect="true" style="width: 100%; height: 100px;">
										</select>
									</div>
									
									<button class="btn btn-info btn-primary btn-xs" type="button" style="width:100%;height:35px;background-color: green !important;"
										onclick="userMore('listRangeUser_userId','','${param.sid}','yes','');">
										关注更多
									</button>
								</div>
							</div>
						</div>
					</div>
					</c:when>
					<c:otherwise>
					<div class="col-md-3 col-xs-12" id="countTask" >
						<div class="widget">
							<div class="widget-header bordered-bottom bordered-themeprimary ps-titleHeader" style="padding-top:5px">
								<i class="widget-icon fa fa-tasks themeprimary"></i> <span
									class="widget-caption themeprimary">任务时限统计</span>

							</div>
							<div class="widget-body">
								<div id="main" style="width:100%;height:535px;"></div>
							</div>
						</div>
					</div>
					</c:otherwise>
					</c:choose>
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
        //合并行
        // taskFormRowSpan($("#editabledatatable"));
        // if($("#toDoState").hasClass("btn-primary")){
        //     $("#overDiv").show();
        // }
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
		$("#searchForm").ajaxSubmit({
		     	type:"POST",
		         url:"/task/ajaxAllTaskCount?entry=1&t="+Math.random(),
		         dataType:"json",
		         data:selectData,
		         beforeSend: function(){
		     		$("#subDiv").css("background","url(/static/plugins/layer/skin/default/loading-0.gif) no-repeat center");
		     		$("#main").css("background","url(/static/plugins/layer/skin/default/loading-0.gif) no-repeat center");
		        	},
		         success:function(data){
		        	 $("#todoNum").text(data.todoNum+'个');
		        	 $("#doneTime").text(data.doneTime+'小时');
		        	 $("#doneNum").text(data.doneNum+'个');
		        	 $("#subDiv").css("background","");
		        	 $("#main").css("background","");
					if(data.countType == 1){
						var subUser = data.subUser;
						if(subUser.length<999){
							$("#moreCount").hide();
							$("#attention").show();
						}else{
						    $("#moreCount").show();
						    $("#attention").hide();
						}
						$("#subDiv .sub-each").remove();
						var subHtml = getAllTaskSubCount(subUser);
						$("#subDiv").append(subHtml);
						
						var attentionHtml ="";
						if(data.attentions && data.attentions[0]){
							for (var i = 0; i < data.attentions.length; i++) {
								attentionHtml += '<option selected="selected" value="'+data.attentions[i].userId+'">'+data.attentions[i].userName+'</option>';
							}
						}
						$("#listRangeUser_userId").html(attentionHtml);
					}else{
						countTaskState(data.allTaskCount);
					}
		         },
		         error:function(error){
		         }
		     });
		//下属任务统计穿透
		$("#subDiv").on("click","a",function(){
			var url = "/task/listTaskOfAllPage?pager.pageSize=10&version=2&activityMenu=task_promote_1.5";
            var operatorNotExecutor =$(this).attr("operatorNotExecutor");
			var executor = $(this).attr("executor");
			if(executor || operatorNotExecutor){
				if(executor){//存在执行人筛选条件的情况
					var executorName = $(this).attr("userName");

					//添加执行人
					url = url+"&listExecutor[0].id="+executor;
					url = url+"&listExecutor[0].userName="+executorName;
					//添加执行人的任务状态
                    url = url+"&state=1";
				}else if(operatorNotExecutor){//存在有经办人筛选条件的情况
					var executorName = $(this).attr("userName");

					//添加经办人
					url = url+"&listOperator[0].id="+operatorNotExecutor;
					url = url+"&listOperator[0].userName="+executorName;
                    url = url + "&containExecutor=1";
                }

                //sid设置

				var sid = $("#sid").val();
				url = url + "&sid=" + sid;

                //其他参数
                // var inputs  = $("#searchForm").find("input");
                // if(inputs && inputs.get(0)){
                //     $.each(inputs,function(index,input){
                //         var inputName = $(input).attr("name");
                //         var inputVal = $(input).val();
                //         //需要过滤掉影响下属统计的参数，否则跳转到所有任务时因为有不相关的参数干扰，会出现数据错误。
                //         if(inputVal && inputName!='activityMenu' && inputName!='overdue'){
                //             url = url+"&"+inputName+"="+ inputVal;
                //         }
                //     })
                // }
                // var selects = $("#searchForm").find("select");
                // if(selects && selects.get(0)){
                //     $.each(selects,function(selectIndex,select){
                //         var list =$(select).attr("list");
                //         var listkey =$(select).attr("listkey");
                //         var listvalue =$(select).attr("listvalue");
                //         var options = $(select).find("option");
                //         if(options && options.get(0)){
                //             //需要过滤掉影响下属统计的参数，否则跳转到所有任务时因为有不相关的参数干扰，会出现数据错误。
                //             $.each(options,function(index,option){
                //                 url = url+"&"+list+"["+index+"]."+listkey+"="+$(option).val();
                //                 url = url+"&"+list+"["+index+"]."+listvalue+"="+$(option).html();
                //             })
                //         }
                //     })
                // }
            }
            window.self.location=url;
		});
	});
	
</script>

