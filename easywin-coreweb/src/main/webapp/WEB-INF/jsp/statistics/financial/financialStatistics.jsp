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
								<form action="/financial/financialStatistics" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="flowState" value="${loanApply.flowState}">
									<input type="hidden" name="executor" value="${loanApply.executor}">
									<input type="hidden" name="executorName" value="${loanApply.executorName}">
									<input type="hidden" name="orderBy" value="${loanApply.orderBy}">
									<input type="hidden" name="creator" value="${loanApply.creator}">
									<input type="hidden" name="creatorName" value="${loanApply.creatorName}">
									<input type="hidden" name="spState" value="${loanApply.spState}">
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="排序">
													<c:choose>
														<c:when test="${not empty loanApply.orderBy}">
															<font style="font-weight:bold;"> 
															<c:if test="${loanApply.orderBy=='executor'}">按审批人</c:if> 
															<c:if test="${loanApply.orderBy=='crTimeNewest'}">按创建时间(降序)</c:if> 
															<c:if test="${loanApply.orderBy=='crTimeOldest'}">按创建时间(升序)</c:if>
															</font>
														</c:when>
														<c:otherwise>排序</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)"  class="clearThisElement" relateElement="orderBy" relateElementName="">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="executor">按审批人</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="crTimeNewest">按创建时间(降序)</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="crTimeOldest">按创建时间(升序)</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="流程状态筛选">
													<c:choose>
														<c:when test="${not empty loanApply.flowState}">
															<font style="font-weight:bold;"> <c:if test="${loanApply.flowState==1}">审批中的</c:if> <c:if test="${loanApply.flowState==4}">审批完的</c:if> <c:if test="${loanApply.flowState==2}">待发起的</c:if>
															</font>
														</c:when>
														<c:otherwise>流程状态筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearThisElement" relateElement="flowState">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="flowState" dataValue="1">审批中的</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="flowState" dataValue="4">已完成的</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="flowState" dataValue="2">待发起的</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="审批结果筛选">
													<c:choose>
														<c:when test="${not empty loanApply.spState}">
															<font style="font-weight:bold;"> <c:if test="${loanApply.spState==0}">驳回</c:if> <c:if test="${loanApply.spState==1}">通过</c:if>
															</font>
														</c:when>
														<c:otherwise>审批结果筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearThisElement" relateElement="spState">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="spState" dataValue="0">驳回</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="spState" dataValue="1">通过</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="申请人筛选">
													<c:choose>
														<c:when test="${not empty loanApply.creator}">
															<font style="font-weight:bold;">${loanApply.creatorName}</font>
														</c:when>
														<c:otherwise>申请人筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearMoreElement" relateList="creator_select">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="userMoreElementSelect" relateList="creator_select">人员选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listCreator" listkey="id" listvalue="userName" id="creator_select" name="listCreator.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${loanApply.listCreator }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
												</c:forEach>
											</select>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="当前审批人筛选">
													<c:choose>
														<c:when test="${not empty loanApply.executor}">
															<font style="font-weight:bold;">${loanApply.executorName}</font>
														</c:when>
														<c:otherwise>当前审批人筛选</c:otherwise>
													</c:choose>
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
											<select list="listExecutor" listkey="id" listvalue="userName" id="executor_select" name="listExecutor.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${loanApply.listExecutor }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
												</c:forEach>
											</select>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													<c:choose>
														<c:when test="${not empty loanApply.startDate || not empty loanApply.endDate}">
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
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${loanApply.startDate}" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${loanApply.endDate}" id="endDate" name="endDate" placeholder="结束时间"
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
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input id="searchFlowName" name="flowName" value="${loanApply.flowName}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键字">
										<a href="#" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
								</form>
								<c:if test="${empty add}">
									<div class="widget-buttons ps-widget-buttons">
										<a href="javascript:void(0);" onclick="menuClick('/financial/financialStatistics?sid=${param.sid}&activityMenu=m_1.1.1');">
		                                    <span class="menu-text">差旅一览Ajax</span>
		                                </a>
										<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" name="addLoanApply" onclick="javascript:void(0);"><i class="fa fa-plus"></i>出差申请</button>
										<c:if test="${userInfo.admin>0}">
											<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" name="busMapFlowCfg" onclick="javascript:void(0);"><i class="fa fa-wrench"></i>映射配置</button>
										</c:if>
										<%--<button class="btn btn-info  btn-xs" onclick="excelExport('差旅一览')" >导出excel</button>
									--%>
									</div>
								</c:if>
							</div>
							<!-- 筛选条件显示 -->
							<div class=" padding-top-10 text-left " style="display:${empty loanApply.listCreator ? 'none':'block'}">
								<strong>申请人筛选:</strong>
								<c:forEach items="${loanApply.listCreator }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('creator','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty loanApply.listExecutor ? 'none':'block'}">
								<strong>当前审批人筛选:</strong>
								<c:forEach items="${loanApply.listExecutor }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('executor','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty listLoanApplyOfAuth}">
						<div class="widget-body" id="tableView">
							<table class="table table-bordered" id="dataTable">
								<thead>
									<tr>
										<th style="width:30px;text-align:center;">序号</th>
										<th style="width:80px;text-align:center;">姓名</th>
										<th>出差记录</th>
										<th style="text-align:center;">目的地</th>
										<th style="text-align:center;">支付方式</th>
										<th style="text-align:center;">开始时间</th>
										<th style="text-align:center;">结束时间</th>
										<th style="text-align:center;">审批结果</th>
										<th style="text-align:center;">借款明细</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${listLoanApplyOfAuth}" var="tripVo" varStatus="vs">
										<tr class="optTr">
											<td style="text-align:center;">
												<label>${vs.count}</label>
											</td>
											<td style="text-align:center;">${tripVo.creatorName}</td>
											<td>
												<a href="javascript:void(0);" onclick="viewSpFlow('${tripVo.instanceId}');">
													<tags:cutString num="31">${tripVo.flowName}</tags:cutString> 
												</a>
											</td>
											<td style="text-align:center;">${tripVo.purpose}</td>
											<td style="text-align:center;">${tripVo.costPaidWay}</td>
											<td style="text-align:center;">${tripVo.startTime}</td>
											<td style="text-align:center;">${tripVo.endTime}</td>
											<td style="text-align:center;">
												<c:if test="${tripVo.flowState==0}">
													<span class="gray" style="font-weight:bold;">无效的</span>
												</c:if> <c:if test="${tripVo.flowState==1}">
													<span class="blue" style="font-weight:bold;">审批中</span>
												</c:if> <c:if test="${tripVo.flowState==2}">
													<span style="color:fuchsia;font-weight:bold;">草稿</span>
												</c:if> 
												<c:if test="${tripVo.flowState==4}">
													<c:if test="${tripVo.spState==0}">
														<span class="red" style="font-weight:bold;">驳回</span>
													</c:if>
													<c:if test="${tripVo.spState==1}">
														<span class="green" style="font-weight:bold;">通过</span>
													</c:if>
												</c:if>
											</td>
											<td>
												<c:choose>
													<c:when test="${not empty tripVo.listLoan}">
														<table class="table table-bordered">
															<tbody>
																<c:forEach items="${tripVo.listLoan}" var="loanVo" varStatus="vs">
																	<tr>
																		<td style="text-align:center;display:${fn:length(tripVo.listLoan)==1?'none':''}">
																			<label class="optRowNum">${vs.count}</label>
																		</td>
																		<td ${fn:length(tripVo.listLoan)==1?'colspan="2"':''}>
																			<a href="javascript:void(0);" onclick="viewSpFlow('${loanVo.instanceId}');">
																				<c:choose>
																					<c:when test="${loanVo.flowState==4 and loanVo.spState==1}">
																						借款<span class="red" style="font-weight:bold;">${loanVo.borrowingBalance}</span>元
																					</c:when>
																					<c:otherwise>
																						<tags:cutString num="31">${loanVo.flowName}</tags:cutString>
																					</c:otherwise>
																				</c:choose>
																			</a>
																		</td>
																		<td style="text-align:center;width:80px;" ${(loanVo.writeOffStatus eq 1 
																							and loanVo.writeOffFlowSpState eq 1 
																							and loanVo.writeOffFlowState eq 4)?'':
																							((loanVo.flowState==4 and loanVo.spState==0)?'colspan="3"':'colspan="2"')}>
																			<c:if test="${loanVo.flowState==0}">
																				<span class="gray" style="font-weight:bold;">无效的</span>
																			</c:if> <c:if test="${loanVo.flowState==1}">
																				<span class="green" style="font-weight:bold;">审批中</span>
																			</c:if> <c:if test="${loanVo.flowState==2}">
																				<span style="color:fuchsia;font-weight:bold;">草稿</span>
																			</c:if> 
																			<c:if test="${loanVo.flowState==4}">
																				<c:if test="${loanVo.spState==0}">
																					<span class="red" style="font-weight:bold;">驳回</span>
																				</c:if>
																				<c:if test="${loanVo.spState==1}">
																					<c:choose>
																						<c:when test="${not empty loanVo.writeOffFlowInstanceId and loanVo.writeOffFlowState ne 0}">
																							<c:if test="${loanVo.writeOffStatus eq 0 
																											and loanVo.writeOffFlowSpState eq 1 
																											and loanVo.writeOffFlowState eq 1}">
																								<a href="javascript:void(0);" onclick="viewSpFlow('${loanVo.writeOffFlowInstanceId}');" title="${loanVo.writeOffFlowName}">
																									<span class="blue" style="font-weight:bold;">销账中</span>
																								</a>
																							</c:if>
																							<c:if test="${loanVo.writeOffStatus eq 1 
																											and loanVo.writeOffFlowSpState eq 1 
																											and loanVo.writeOffFlowState eq 4}">
																								<a href="javascript:void(0);" onclick="viewSpFlow('${loanVo.writeOffFlowInstanceId}');" title="${loanVo.writeOffFlowName}">
																									<span class="green" style="font-weight:bold;">已销账</span>
																								</a>
																							</c:if>
																						</c:when>
																						<c:otherwise>
																							<c:if test="${loanVo.creator eq userInfo.id}">
																								<a href="javascript:void(0);" name="addLoanWriteOff" loanId="${loanVo.id}"><span class="red" style="font-weight:bold;">销账</span></a>
																							</c:if>
																							<c:if test="${loanVo.creator ne userInfo.id}">
																								<span class="red" style="font-weight:bold;">未销账</span>
																							</c:if>
																						</c:otherwise>
																					</c:choose>
																				</c:if>
																			</c:if>
																		</td>
																		<td style="display:${(loanVo.writeOffStatus eq 1 
																							and loanVo.writeOffFlowSpState eq 1 
																							and loanVo.writeOffFlowState eq 4)?'':'none'}">
																			<c:if test="${loanVo.flowState eq 4 and loanVo.spState eq 1 
																							and loanVo.writeOffStatus eq 1 
																							and loanVo.writeOffFlowSpState eq 1 
																							and loanVo.writeOffFlowState eq 4}">
																				<a href="javascript:void(0);" onclick="viewSpFlow('${loanVo.writeOffFlowInstanceId}');" title="${loanVo.writeOffFlowName}">
																					<span style="font-weight:bold;">报销金额：<span class="red">${loanVo.writeOffBalance}</span>元</span>
																				</a>
																			</c:if>
																		</td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
													</c:when>
													<c:otherwise><span class="red">未借款</span></c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>没有相关出差数据！</h2>
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
				window.location.href="/adminCfg/financial/loanApply/cfg?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.1";
			});
				
			$("[name='addLoanApply']").click(function(){//发起出差
				if("${busMapFlow.id}"){
					var url = "/financial/loanApply/addLoanApply?sid=${param.sid}&busMapFlowId=${busMapFlow.id}";
					openWinByRight(url);
				}else{
					window.top.layer.confirm("请联系管理员配置“出差流程”！", {
				 		  btn: ['确定']//按钮
				 	  ,title:'提示框'
				 	  ,icon:2
				 	});
				}
			});
			$("[name='addLoan']").click(function(){//借款
				if("${busMapFlowOfLoan.id}"){
					var tripId = $(this).attr("tripId");
					$.ajax({  
				        url : "/financial/isLoanWriteOff?sid=${param.sid}",  
				        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
				        type : "POST",  
				        dataType : "json", 
				        success : function(result) { 
				        	if(result.status=="yes"){
				        		if(tripId){
									var url = "/financial/loan/addLoan?sid=${param.sid}&busMapFlowId=${busMapFlowOfLoan.id}&tripId="+tripId;
									openWinByRight(url);
								}else{
									window.top.layer.confirm("借款异常，请联系管理员！", {
								 		  btn: ['确定']//按钮
								 	  ,title:'提示框'
								 	  ,icon:2
								 	});
								}
				        	}else{
				        		window.top.layer.confirm(result.msg,{
						 		  btn: ['详情查看','关闭'],//按钮
						 		  title:'提示框',
						 		  icon:2,
								  yes: function(index, layero){
									  viewSpFlow(result.instanceId);//详情查看
									  window.top.layer.close(index);
								  }
							 	});
				        	}
				        }  
				    });
				}else{
					window.top.layer.confirm("请联系管理员配置“借款流程”！", {
				 		  btn: ['确定']//按钮
				 	  ,title:'提示框'
				 	  ,icon:2
				 	});
				}
			});
			
			$("[name='loanMuchTimes']").click(function(){//多次借款记录查看
				window.top.layer.open({
				  type: 2,
				  title:"借款记录",
				  closeBtn:0,
				  area: ['650px', '400px'],
				  fix: true, //不固定
				  maxmin: false,
				  move: false,
				  scrollbar:false,
				  content: ["/financial/loanApply/listLoanOfTrip?sid=${param.sid}&tripId="+$(this).attr("tripId"),'no'],
				  btn: ['查看', '取消'],
				  yes: function(index, layero){
					  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					  var result = iframeWin.selectedLoan();
					  if(result.instanceId){
						  viewSpFlow(result.instanceId);
					  }
					  window.top.layer.close(index);
				  }
				  ,cancel: function(){ 
				    //右上角关闭回调
				  },success:function(layero,index){
					//页面创建完成时，调用
				  }
				});
			});
		})
		
		</script>
</body>
</html>
