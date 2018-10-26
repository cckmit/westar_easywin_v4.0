<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
								<form action="/announcement/listPagedAnnoun" id="searchForm" class="subform">
									<input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab }" />
									<input type="hidden" name="sid" value="${param.sid }" />
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" id="enabled" name="enabled" value="${announ.enabled}" />
									<input type="hidden" name="creator" id="creator" value="${announ.creator}">
									<input type="hidden" name="grade" id="grade" value="${announ.grade}">
									<input type="hidden" name="type" id="type" value="${announ.type}">
									<input type="hidden" name="orderBy" id="orderBy" value="${announ.orderBy}">
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown"> <c:choose>
														<c:when test="${not empty  announ.orderBy}">
															<font style="font-weight:bold;"> <c:if test="${announ.orderBy=='readState'}">按消息未读</c:if> <c:if test="${announ.orderBy=='grade'}">按重要程度</c:if> <c:if
																	test="${announ.orderBy=='creator' && param.searchTab != 12}">按创建人</c:if> <c:if test="${announ.orderBy=='crTimeNewest'}">按创建时间(降序)</c:if> <c:if test="${announ.orderBy=='crTimeOldest'}">按创建时间(升序)</c:if>
															</font>
														</c:when>
														<c:otherwise>排序</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearThisElement" relateElement="orderBy">不限条件</a>
													</li>
													<li class="divider"></li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('readState');">按消息未读</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('grade');">按紧急程度</a>
													</li>
													<c:if test="${param.searchTab != 12}">
														<li>
															<a href="javascript:void(0)" onclick="orderBy('creator');">按创建人</a>
														</li>
													</c:if>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeNewest');">按创建时间(降序)</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeOldest');">按创建时间(升序)</a>
													</li>
												</ul>
											</div>
										</div>
										<c:if test="${param.searchTab != 12 }">
										<div class="table-toolbar ps-margin margin-right-5">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown"> <c:choose>
													<c:when test="${not empty announ.creatorName}">
														<font style="font-weight:bold;">${announ.creatorName}</font>
													</c:when>
													<c:otherwise>创建人筛选</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li><a href="javascript:void(0)" class="clearMoreElement" relateList="creator_select">不限条件</a>
												</li>
												<li>
												<a href="javascript:void(0)" class="userMoreElementSelect" relateList="creator_select">人员选择</a>
												</li>
											</ul>
										</div>
									</div>
									<div style="float: left;width: 250px;display: none">
										<select list="listCreator" listkey="id" listvalue="userName" id="creator_select" name="listCreator.id" multiple="multiple"
											moreselect="true" style="width: 100%; height: 100px;">
											<c:forEach items="${announ.listCreator }" var="obj" varStatus="vs">
											<option selected="selected" value="${obj.id }">${obj.userName }</option>
											</c:forEach>
										</select>
									</div>
									</c:if>
										<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
														<c:when test="${not empty announ.startDate || not empty announ.endDate || not empty announ.grade}">
															<font style="font-weight:bold;">筛选中</font>
														</c:when>
														<c:otherwise>
															更多
														</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">重&nbsp;&nbsp;要&nbsp;&nbsp;度：</span>
														<button type="button" class="btn  ${announ.grade==4?'btn-primary':'btn-default'} btn-xs  gradeBtn " onclick="announGradeFilter('4',this)">重要且紧急</button>
														<button type="button" class="btn  ${announ.grade==3?'btn-primary':'btn-default'} btn-xs  gradeBtn" onclick="announGradeFilter('3',this)">紧急</button>
														<button type="button" class="btn  ${announ.grade==2?'btn-primary':'btn-default'} btn-xs  gradeBtn" onclick="announGradeFilter('2',this)">重要</button>
														<button type="button" class="btn  ${announ.grade==1?'btn-primary':'btn-default'} btn-xs  gradeBtn" onclick="announGradeFilter('1',this)">普通</button>
													</div>
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">起止时间：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${announ.startDate}" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${announ.endDate}" id="endDate" name="endDate" placeholder="结束时间"
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
														<a class="btn btn-default dropdown-toggle btn-xs" onclick="delAnnoun()"> 批量删除 </a>
													</div>
												</div>
											</c:if>
										</div>
									</div>

								</form>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input id="announTitle" name="title" value="${announ.title}" class="form-control ps-input" type="text" placeholder="标题关键字">
										<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
								<c:if test="${empty add}">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addAnnounPage('${param.sid}');">
											<i class="fa fa-plus"></i> 新建公告
										</button>
									</div>
								</c:if>
							</div>
							
							<div  class=" padding-top-10 text-left " style="display:${empty announ.listCreator ? 'none':'block'}">
								<strong >发起人筛选:</strong>
								<c:forEach items="${announ.listCreator }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('creator','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
								</c:forEach>
							</div>
						</div>

						<c:choose>
							<c:when test="${not empty list}">
								<div class="widget-body">
									<form action="/announcement/delBatchAnnouncement" method="post" id="delForm">
										<input type="hidden" id="redirectPage" name="redirectPage" />
										<input type="hidden" name="sid" value="${param.sid}" />

										<table class="table table-striped table-hover fixTable" id="editabledatatable">
											<thead>
												<tr role="row">
													<th class=" optTd" width="5%">
														<label>
															<input type="checkbox" class="colored-blue " id="checkAllBox" onclick="checkAll(this,'ids')"> <span class="text"
													style="color: inherit;"></span>
														</label>
													</th>
													<th class="text-center" width="10%">重要度</th>
													<th  width="45%">标题</th>
													<th width="10%">创建人</th>
													<th class="text-center" width="10%">发布时间</th>
													<th width="5%" class="text-center">阅读量</th>
													<th width="8%" class="text-center">操作</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${list}" var="obj" varStatus="vs">
													<tr class="${vs.count mod 2 eq 0 ? 'ramify-bg' :'' } optTr">
														<c:set var="gradeColor">
															<c:choose>
																<c:when test="${obj.grade==4}">label-danger</c:when>
																<c:when test="${obj.grade==3}">label-danger</c:when>
																<c:when test="${obj.grade==2}">label-orange</c:when>
																<c:when test="${obj.grade==1}">label-success</c:when>
															</c:choose>
														</c:set>
														<td class="optTd " style="height: 47px">
															<label class="optCheckBox " style="display: none;width: 20px">
																<input class="colored-blue " type="checkbox" name="ids" value="${obj.id}" ${obj.creator == userInfo.id ? '':'disabled="disabled"'} />
																<span class="text"></span>
															</label>
															<label class="optRowNum " style="display: block;width: 20px">${vs.count}</label>
														</td>
														<td class="text-center">
															<span class="label ${gradeColor}">
																<c:choose>
																	<c:when test="${obj.grade==4}">紧急且重要</c:when>
																	<c:when test="${obj.grade==3}">紧急</c:when>
																	<c:when test="${obj.grade==2}">重要</c:when>
																	<c:when test="${obj.grade==1}">普通</c:when>
																</c:choose>
															</span>
														</td>
														<td>
															<a href="javascript:void(0);" class="${obj.isRead==0?" noread ":" " }" onclick="readMod(this,'announ','${obj.id }', '039'); viewAnnoun('${param.sid }','${obj.id }');"> <c:choose>
																	<c:when test="${obj.type=='1'}">[通知]</c:when>
																	<c:when test="${obj.type=='2'}">[通报]</c:when>
																	<c:when test="${obj.type=='3'}">[决定]</c:when>
																	<c:when test="${obj.type=='0'}">[其他]</c:when>
																</c:choose>${obj.title}</a>
														</td>
														<td class="text-center">
															<div class="ticket-user pull-left other-user-box">
																<div class="ticket-user pull-left other-user-box" data-container="body" data-toggle="popover" data-placement="left" data-user='${obj.creator}' data-busId='${obj.id}' data-busType='039'>
																	<img class="user-avatar" src="/downLoad/userImg/${obj.comId}/${obj.creator}?sid=${param.sid}" title="${obj.creatorName}" />
																	<span class="user-name">
																		<tags:cutString num="10">${obj.creatorName}</tags:cutString>
																	</span>
																</div>
															</div>
														</td>
														<td class="text-center">${fn:substring(obj.recordCreateTime,0,10) }</td>
														<td class="text-center">${obj.readTime }</td>
														<td class="text-center">
															<a href="javascript:void(0);" class="blue" onclick="readMod(this,'announ','${obj.id }', '039'); viewAnnoun('${param.sid }','${obj.id }');">查看</a>
															<c:if test="${obj.creator eq userInfo.id || userInfo.admin> 0}">
															|<a href="javascript:void(0);" class="blue" onclick="readMod(this,'announ','${obj.id }', '039'); editAnnoun('${param.sid }','${obj.id }');">编辑</a>
															</c:if>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/announcement/listPagedAnnoun "></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>您还没有相关公告数据！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
											<a href="javascript:void(0);" onclick="addAnnounPage('${param.sid}');" class="return-btn"><i class="fa fa-plus"></i>新建公告</a>
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
</body>
</html>
