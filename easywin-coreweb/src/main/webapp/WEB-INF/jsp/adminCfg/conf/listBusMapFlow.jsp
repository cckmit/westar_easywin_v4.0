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
								<form action="/adminCfg/conf/listBusMapFlow" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="orderBy" value="${busMapFlow.orderBy}">
									<input type="hidden" name="description" value="${busMapFlow.description}">
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="排序">
													<c:choose>
														<c:when test="${not empty busMapFlow.orderBy}">
															<font style="font-weight:bold;"> 
															<c:if test="${busMapFlow.orderBy=='executor'}">按审批人</c:if> 
															<c:if test="${busMapFlow.orderBy=='crTimeNewest'}">按创建时间(降序)</c:if> 
															<c:if test="${busMapFlow.orderBy=='crTimeOldest'}">按创建时间(升序)</c:if>
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
														<a href="javascript:void(0)" onclick="orderBy('crTimeNewest');">按创建时间(降序)</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeOldest');">按创建时间(升序)</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													<c:choose>
														<c:when test="${not empty busMapFlow.startDate || not empty busMapFlow.endDate}">
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
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${busMapFlow.startDate}" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${busMapFlow.endDate}" id="endDate" name="endDate" placeholder="结束时间"
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
														<a class="btn btn-default dropdown-toggle btn-xs" onclick="delRow()"> 批量删除 </a>
													</div>
												</div>
											</c:if>
										</div>
									</div>
								</form>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input id="description" value="${busMapFlow.description}" class="form-control ps-input" type="text" placeholder="请输入关键字">
										<a href="#" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
								<c:if test="${empty add}">
									<div class="widget-buttons ps-widget-buttons">
										<c:if test="${userInfo.admin>0}">
											<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" name="busMapFlowCfg" onclick="javascript:void(0);"><i class="fa fa-plus"></i>添加映射配置</button>
										</c:if>
									</div>
								</c:if>
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty listBusMapFlow}">
						<div class="widget-body">
							<form action="/adminCfg/delBusMapFlowByBatch?sid=${param.sid}" method="post" id="delForm">
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover" id="editabledatatable">
									<thead>
										<tr role="row">
											<th width="60px">
											<label> 
												<input type="checkbox"
													class="colored-blue" id="checkAllBox"
													onclick="checkAll(this,'ids')"> <span class="text"
													style="color: inherit;"></span>
											</label>
											</th>
											<th width="120px">类型</th>
											<th>配置描述</th>
											<th>关联流程</th>
											<th width="180px">创建时间</th>
											<th width="120px">操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listBusMapFlow}" var="mapVo" varStatus="vs">
											<tr class="optTr">
												<td class="optTd" style="height: 47px"><label
													class="optCheckBox" style="display: none;width: 20px">
														<input class="colored-blue" type="checkbox" name="ids" value="${mapVo.id}"/> 
														<span class="text"></span>
												</label> <label class="optRowNum"
													style="display: block;width: 20px">${vs.count}</label>
												</td>
												<td>
													<c:choose>
														<c:when test="${mapVo.busType eq '029'}">出差</c:when>
														<c:when test="${mapVo.busType eq '030'}">借款</c:when>
														<c:when test="${mapVo.busType eq '03101'}">出差报销</c:when>
														<c:when test="${mapVo.busType eq '03102'}">一般费用报销</c:when>
														<c:when test="${mapVo.busType eq '03401'}">出差汇报</c:when>
														<c:when test="${mapVo.busType eq '03402'}">一般费用说明</c:when>
														<c:when test="${mapVo.busType eq '035'}">一般借款申请</c:when>
														<c:when test="${mapVo.busType eq '031'}">一般借款</c:when>
														<c:when test="${mapVo.busType eq '036'}">请假</c:when>
														<c:when test="${mapVo.busType eq '037'}">加班</c:when>
														<c:when test="${mapVo.busType eq '04201'}">事件管理过程</c:when>
														<c:when test="${mapVo.busType eq '04202'}">问题管理过程</c:when>
														<c:when test="${mapVo.busType eq '04203'}">变更管理过程</c:when>
														<c:when test="${mapVo.busType eq '04204'}">发布管理过程</c:when>
														<c:when test="${mapVo.busType eq '070'}">需求管理</c:when>
													</c:choose>
												</td>
												<td>
													<a href="javascript:void(0);" onclick="busMapFlowCfg('${param.sid}','${busType}','/adminCfg/initBusMapFlow',${mapVo.id});">
														<tags:cutString num="31">${mapVo.description}</tags:cutString> 
													</a>
												</td>
												<td>${mapVo.flowName}</td>
												<td>${mapVo.recordCreateTime}</td>
												<td>
													<a href="javascript:void(0);" onclick="busMapFlowCfg('${param.sid}','${busType}','/adminCfg/initBusMapFlow',${mapVo.id});" class="blue">编辑</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/adminCfg/conf/listBusMapFlow"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>没有相关配置数据！</h2>
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
			$("[name='busMapFlowCfg']").click(function(){//业务关联配置
				busMapFlowCfg("${param.sid}","${busType}","/adminCfg/initBusMapFlow",-1);
			});
			$("#description").blur(function(){
				//启动加载页面效果
				layer.load(0, {shade: [0.6,'#fff']});
				$("#searchForm [name='description']").val($("#description").val());
				$("#searchForm").submit();
			});
		})
		/**
		 * 展示查询条件中更多
		 */
		function displayMoreCond(divId){
			if($("#"+divId).hasClass("open")){
				$("#"+divId).removeClass("open");
			}else{
				$("#"+divId).addClass("open")
			}
		}
		//排序
		function orderBy(type){
			//启动加载页面效果
			layer.load(0, {shade: [0.6,'#fff']});
			$("#searchForm [name='orderBy']").val(type);
			$("#searchForm").submit();
		}
		//排序默认
		function orderByClean(){
			//启动加载页面效果
			layer.load(0, {shade: [0.6,'#fff']});
			$("#searchForm [name='orderBy']").val('');
			$("#searchForm").submit();
		}
		/**
		 * 清空更多查询条件
		 */
		function resetMoreCon(divId){
			$("#"+divId).find("input").val('');
			var stateBtnArray = $(".stateBtn");
			$.each(stateBtnArray,function(index,item){
				$(this).removeClass("btn-primary");
				if(!$(this).hasClass("btn-default")){
					$(this).addClass("btn-default");
				}
			})
			$("#state").val('');
		}
		//删除
		function delRow(){
			if(checkCkBoxStatus('ids')){
				window.top.layer.confirm('确定要删除吗？', {icon: 3, title:'确认框'}, function(index){
				  var url = reConstrUrl('ids');
				  $("#delForm input[name='redirectPage']").val(url);
				  $('#delForm').submit();
				  window.top.layer.close(index);
				});
			}else{
				window.top.layer.msg('请先选择。', {icon: 6});
			}
		}
		</script>
</body>
</html>
