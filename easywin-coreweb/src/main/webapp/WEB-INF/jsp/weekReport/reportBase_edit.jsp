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
	<c:choose>
		<c:when test="${empty weekReport}">
			<div class="error-container text-center" style="padding-top: 150px">
				<h1>
					<i class="fa fa-exclamation-triangle"></i>
				</h1>
				<div class="error-divider">
					<h2>请联系团队管理人员,设置周报模板</h2>
				</div>
			</div>
		</c:when>
		<c:otherwise>
		<!-- 已经汇报 -->
			<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
			<div class="widget-body margin-top-40" id="contentBody"
				style="overflow: hidden;overflow-y:scroll;">
				<form method="post" id="weekRepForm" class="subform"
					action="/weekReport/addWeekReport">
					<input type="hidden" id="num"
						value="${fn:length(weekReport.weekReportPlans)}" /> <input
						type="hidden" id="id" name="id" value="${weekReport.id}" /> <input
						type="hidden" name="sid" value="${param.sid}" /> <input
						type="hidden" id="state" name="state" value="${weekReport.state}" />
					<div class="addWeekReport"
						style="${(weekReport.state!=0 || (weekReport.countVal==0 && weekReport.state==0))?'display:block':'display:none' }">
	
						<div class="widget radius-bordered">
							<div class="widget-header bg-bluegray no-shadow">
								<span class="widget-caption blue">工作汇报</span>
								<div class="widget-buttons btn-div-full">
									<a class="ps-point btn-a-full" data-toggle="collapse"> <i
										class="fa fa-minus blue"></i> </a>
								</div>
							</div>
							<div class="widget-body no-shadow">
								<c:choose>
									<c:when test="${not empty weekReport}">
										<%--条目 --%>
										<c:forEach var="weekReportQ" items="${weekReport.weekReportQs}"
											varStatus="vs">
											<div class="form-group">
												<label for="xsinput"><i
													class="fa fa-pencil-square fa-lg green"></i>
													${weekReportQ.modReportName}</label>
													<c:if test="${weekReportQ.isRequire eq '1'}">
														<font style="color: red;">*</font>
													</c:if>
												<textarea name="${weekReportQ.reportName}" isRequire="${weekReportQ.isRequire}"
													class="form-control taskareaAdd" style="color: #000">${weekReportQ.reportVal} </textarea>
											</div>
										</c:forEach>
										<%--周报填写结束 --%>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
							</div>
	
						</div>
	
						<c:if test="${weekReport.hasPlan==1}">
							<div class="widget radius-bordered">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue">下周工作计划</span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse"> <i
											class="fa fa-minus blue"></i> </a>
									</div>
								</div>
								<div class="widget-body no-shadow">
									<div class="panel-body" id="weekPlan">
										<div class="row">
											<label for="xsinput" class="control-label col-xs-7">
												下周计划任务名称<font style="color: red;">*</font></label> <label for="xsinput" class="control-label col-xs-5">
												计划完成时间</label>
										</div>
	
										<c:choose>
											<c:when test="${not empty weekReport.weekReportPlans}">
												<c:forEach items="${weekReport.weekReportPlans}"
													var="weekRepPlan" varStatus="vs">
													<div class="row ws-plan-line" id="planBody${vs.count-1}"
														style="padding-bottom: 10px">
														<div class="control-label col-xs-7">
															<textarea
																name="weekReportPlans[${vs.count-1}].planContent"
																class="form-control taskarea" style="color: #000">${weekRepPlan.planContent }</textarea>
														</div>
														<div class="col-xs-2">
															<input type="text" value="${weekRepPlan.planTime}"
																name="weekReportPlans[${vs.count-1}].planTime"
																onclick="WdatePicker({dateFmt:'yyyy年MM月dd日'});"
																style="cursor: pointer;width: 125px;padding: 0px 5px"
																class="form-control" readonly="readonly" />
														</div>
														<div class="col-xs-3 ws-plan-btn" style="text-align:center">
															<c:if test="${vs.last}">
																<button class="btn btn-info ws-btnBlue" type="button"
																	onclick="addPlan(this,${vs.count-1})">添加</button>
															</c:if>
															<c:if test="${fn:length(weekReport.weekReportPlans)>1}">
																<button class="btn btn-default" type="button"
																	onclick="delPlan(this,${vs.count-1})">删除</button>
															</c:if>
														</div>
													</div>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<div class="row ws-plan-line" id="planBody0"
													style="padding-bottom: 10px">
													<div class="control-label col-xs-7">
														<textarea name="weekReportPlans[0].planContent"
															class="form-control taskarea" style="color: #000;"></textarea>
													</div>
													<div class="col-xs-2">
														<input type="text" value=""
															name="weekReportPlans[0].planTime"
															onclick="WdatePicker({dateFmt:'yyyy年MM月dd日'});"
															style="cursor: pointer;width: 125px;padding: 0px 5px"
															class="form-control" readonly="readonly" />
													</div>
													<div class="col-xs-3 ws-plan-btn" style="text-align:center">
														<button class="btn btn-info ws-btnBlue" type="button"
															onclick="addPlan(this,0)">添加</button>
													</div>
												</div>
	
											</c:otherwise>
										</c:choose>
	
	
	
									</div>
								</div>
	
							</div>
						</c:if>
	
						<%--上传附件部分 --%>
						<div class="widget radius-bordered">
							<div class="widget-header bg-bluegray no-shadow">
								<span class="widget-caption blue">周报附件</span>
								<div class="widget-buttons btn-div-full">
									<a class="ps-point btn-a-full" data-toggle="collapse"> <i
										class="fa fa-minus blue"></i> </a>
								</div>
							</div>
							<div class="widget-body no-shadow">
								<div style="clear:both" class="ws-process">
									<div id="thelistweekReportFiles_upfileId" style="width: 300px">
										<c:choose>
											<c:when test="${not empty weekReport.weekReportFiles}">
												<c:forEach items="${weekReport.weekReportFiles}" var="upfile"
													varStatus="vs">
													<div id="wu_file_0_-${upfile.upfileId}"
														class="uploadify-queue-item">
														<div class="cancel">
															<a href="javascript:void(0)" fileDone="1"
																fileId="${upfile.upfileId}">X</a>
														</div>
														<span class="fileName" title="${upfile.fileName}"> <tags:cutString
																num="25">${upfile.fileName}</tags:cutString>(已有文件) </span> <span
															class="data"> - 完成</span>
														<div class="uploadify-progress">
															<div class="uploadify-progress-bar" style="width: 100%;"></div>
														</div>
													</div>
												</c:forEach>
											</c:when>
										</c:choose>
									</div>
									<div class="btns btn-sm">
										<div id="pickerweekReportFiles_upfileId">选择文件</div>
									</div>
									<script type="text/javascript">
																		loadWebUpfiles('weekReportFiles_upfileId','${param.sid}','','pickerweekReportFiles_upfileId','thelistweekReportFiles_upfileId','fileweekReportFiles_upfileId');
																	</script>
									<div
										style="position: relative; width: 350px; height: 90px;display: none">
										<div style="float: left;">
											<select list="weekReportFiles" listkey="upfileId"
												listvalue="fileName" id="weekReportFiles_upfileId"
												name="weekReportFiles.upfileId"
												ondblclick="removeClick(this.id)" multiple="multiple"
												moreselect="true" style="width: 100%; height: 90px;">
												<c:choose>
													<c:when test="${not empty weekReport.weekReportFiles}">
														<c:forEach items="${weekReport.weekReportFiles}"
															var="upfile" varStatus="vs">
															<option selected="selected" value="${upfile.upfileId}">${upfile.fileName}</option>
														</c:forEach>
													</c:when>
												</c:choose>
											</select>
										</div>
									</div>
								</div>
							</div>
	
						</div>
						<%--上传附件部分 结束--%>
					</div>
	
					<%--周报添加结束 --%>
					<%--周报查看 --%>
					<div class="viewWeekReport"
						style="${(weekReport.countVal>0 && weekReport.state==0)?'display:block':'display:none' }">
						<div class="widget radius-bordered">
							<div class="widget-header bg-bluegray no-shadow">
								<span class="widget-caption blue">工作汇报</span>
								<div class="widget-buttons btn-div-full">
									<a class="ps-point btn-a-full" data-toggle="collapse"> <i
										class="fa fa-minus blue"></i> </a>
								</div>
							</div>
							<div class="widget-body no-shadow">
								<c:choose>
									<c:when test="${not empty weekReport}">
										<%--条目 --%>
										<c:forEach var="weekReportQ" items="${weekReport.weekReportQs}"
											varStatus="vs">
											<div class="form-group">
												<label for="xsinput"><i
													class="fa fa-check-circle fa-lg blue"></i>${weekReportQ.modReportName}</label>
												<div class="ws-zbContent" style="padding-left: 15px">
													<c:choose>
														<c:when test="${not empty weekReportQ.reportVal}">
															<tags:viewTextArea>${weekReportQ.reportVal}</tags:viewTextArea>
														</c:when>
														<c:otherwise>
															<font color="red">未填写</font>
														</c:otherwise>
													</c:choose>
												</div>
											</div>
										</c:forEach>
										<%--周报内容结束 --%>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
							</div>
	
						</div>
	
	
						<c:if test="${weekReport.hasPlan==1}">
							<div class="widget radius-bordered">
								<div class="widget-header bg-bluegray no-shadow">
									<c:set var="planNums"
										value="${fn:length(weekReport.weekReportPlans)}"></c:set>
									<span class="widget-caption blue">下周工作计划(${planNums})</span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse"> <i
											class="fa fa-minus blue"></i> </a>
									</div>
								</div>
								<div class="widget-body no-shadow">
									<table class="display table table-bordered table-striped">
										<thead>
											<tr>
												<th width="10%">序号</th>
												<th>计划任务名称</th>
												<th width="18%">计划完成时间</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty weekReport.weekReportPlans}">
													<c:forEach items="${weekReport.weekReportPlans}"
														var="weekRepPlan" varStatus="vs">
														<tr class="gradeX">
															<td>${vs.count}</td>
															<td><tags:viewTextArea>${weekRepPlan.planContent }</tags:viewTextArea>
															</td>
															<td>${weekRepPlan.planTime}</td>
														</tr>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr class="gradeX" align="center">
														<td colspan="3">未填写下周计划</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
	
							</div>
						</c:if>
	
	
	
	
						<%--上传附件部分 --%>
						<div class="widget radius-bordered">
							<div class="widget-header bg-bluegray no-shadow">
								<c:set var="fileNums"
									value="${fn:length(weekReport.weekReportFiles)}"></c:set>
								<span class="widget-caption blue">周报附件(${fileNums})</span>
								<div class="widget-buttons btn-div-full">
									<a class="ps-point btn-a-full" data-toggle="collapse"> <i
										class="fa fa-minus blue"></i> </a>
								</div>
							</div>
							<div class="widget-body no-shadow">
								<table class="display table table-bordered table-striped">
									<thead>
										<tr>
											<th width="10%">序号</th>
											<th>附件名称</th>
											<th width="20%">上传时间</th>
											<th width="10%" style="text-align:center;">操作</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty weekReport.weekReportFiles}">
												<c:forEach items="${weekReport.weekReportFiles}" var="upfile"
													varStatus="vs">
													<tr class="gradeX">
														<td>${vs.count}</td>
														<td>${upfile.fileName }</td>
														<td>${upfile.upTime}</td>
														<td align="center"><c:choose>
																<c:when test="${upfile.fileExt=='doc' || upfile.fileExt=='docx' || upfile.fileExt=='xls' || upfile.fileExt=='xlsx' || upfile.fileExt=='ppt' || upfile.fileExt=='pptx'}">
																	<a class="fa fa-download" style="padding-right: 10px"
																		href="javascript:void(0)"
																		onclick="downLoad('${upfile.fileUuid}','${upfile.fileName}','${param.sid }')"
																		title="下载"></a>
																	<a class="fa fa-eye" href="javascript:void(0)"
																		onclick="viewOfficePage('${upfile.upfileId}','${upfile.fileUuid}','${upfile.fileName}','${upfile.fileExt}','${param.sid}','006','${weekReport.id}')"
																		title="预览"> </a>
																</c:when>
																<c:when test="${upfile.fileExt=='txt'|| upfile.fileExt=='pdf'}">
																	<a class="fa fa-download" style="padding-right: 10px"
																		href="/downLoad/down/${upfile.fileUuid}/${upfile.fileName}?sid=${param.sid}"
																		title="下载"></a>
																	<a class="fa fa-eye" href="javascript:void(0)"
																		onclick="viewOfficePage('${upfile.upfileId}','${upfile.fileUuid}','${upfile.fileName}','${upfile.fileExt}','${param.sid}','006','${weekReport.id}')"
																		title="预览"> </a>
																</c:when>
																<c:when test="${upfile.fileExt=='jpg'||upfile.fileExt=='bmp'||upfile.fileExt=='gif'||upfile.fileExt=='jpeg'||upfile.fileExt=='png'}">
																	<a class="fa fa-download" style="padding-right: 10px"
																		href="/downLoad/down/${upfile.fileUuid}/${upfile.fileName}?sid=${param.sid}"
																		title="下载"></a>
																	<a class="fa fa-eye" href="javascript:void(0)"
																		onclick="showPic('/downLoad/down/${upfile.fileUuid}/${upfile.fileName}','${param.sid}','${upfile.upfileId}','006','${weekReport.id}')"
																		title="预览"></a>
																</c:when>
																<c:otherwise>
																	<a class="fa fa-download" style="padding-right: 10px"
																		onclick="downLoad('${upfile.fileUuid}','${upfile.fileName}','${param.sid }')"
																		title="下载"></a>
																</c:otherwise>
															</c:choose></td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr class="gradeX" align="center">
													<td colspan="4">未上传附件</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
						<%--上传附件部分 结束--%>
					</div>
					<%--周报结束 --%>
				</form>
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>
