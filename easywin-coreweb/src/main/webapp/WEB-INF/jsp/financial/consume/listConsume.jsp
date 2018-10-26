<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">

			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div class="checkbox ps-margin pull-left">
								<label> <input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')"> <span class="text" style="color: inherit;">全选</span>
								</label>
							</div>
							<div>
							<form action="/consume/listConsume" class="subform" id="searchForm">
								<input type="hidden" name="searchTab" id="searchTab" value="${empty param.searchTab?'11':param.searchTab}">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" id="pageSize" name="pager.pageSize" value="10"> 
								<input type="hidden" name="activityMenu" value="${param.activityMenu}"> 
								<input type="hidden" name="status" id="status" value="${consume.status}">
								<input type="hidden" name="type" id="type" value="${consume.type}">
								<div class="searchCond" style="display: block">
									
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
													费用类型筛选
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0);" onclick="consumeTypeFilter(this,'')">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="consumeTypeMoreTree('${param.sid}','crmType');">类型选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listConsumeType" listkey="id" listvalue="typeName" id="crmType_select" name="listConsumeType_id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${consume.listConsumeType }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.typeName }</option>
												</c:forEach>
											</select>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
	                                            <a class="btn btn-default dropdown-toggle btn-xs"
	                                               data-toggle="dropdown"> <c:choose>
	                                                <c:when test="${not empty consume.status}">
	                                                    <font style="font-weight:bold;">
	                                                        <c:choose>
	                                                            <c:when test="${consume.status==0}">待报销</c:when>
	                                                        </c:choose>
	                                                        <c:choose>
	                                                            <c:when test="${consume.status==1}">报销中</c:when>
	                                                        </c:choose>
	                                                        <c:choose>
	                                                            <c:when test="${consume.status==2}">已报销</c:when>
	                                                        </c:choose>
	                                                    </font>
	                                                </c:when>
	                                                <c:otherwise>状态</c:otherwise>
	                                            </c:choose> <i class="fa fa-angle-down"></i>
	                                            </a>
	                                            <ul class="dropdown-menu dropdown-default">
	                                                <li><a href="javascript:void(0)" class="clearThisElement" relateElement="status">不限条件</a>
	                                                </li>
	                                                <li><a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="0">待报销</a>
	                                                </li>
	                                                <li><a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="1">报销中</a>
	                                                </li>
	                                                <li><a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="2">已报销</a>
	                                                </li>
	                                            </ul>
                                        	</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													<c:choose>
														<c:when test="${not empty consume.consumeStartDate || not empty consume.consumeEndDate || not empty consume.createStartDate || not empty consume.createEndDate }">
															<font style="font-weight:bold;">筛选中</font>
														</c:when>
														<c:otherwise>
															更多
														</c:otherwise>
													</c:choose>
													
													<i class="fa fa-angle-down"></i>
												</a>
												<div class="dropdown-menu dropdown-default padding-bottom-10 subMore" style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">消费日期：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="consumeStartDate" name="consumeStartDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'consumeEndDate\',{d:-0});}'})" value="${consume.consumeStartDate}"/>
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="consumeEndDate" name="consumeEndDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'consumeStartDate\',{d:+0});}'})" value="${consume.consumeEndDate}"/>
													</div>
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">创建时间：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="createStartDate" name="createStartDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'createEndDate\',{d:-0});}'})" value="${consume.createStartDate}"/>
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="createEndDate" name="createEndDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'createStartDate\',{d:+0});}'})" value="${consume.createEndDate}"/>
													</div>
													<div class="ps-clear padding-top-10" style="text-align: center;">
														<button type="button" class="btn btn-primary btn-xs moreSearchBtn noMoreShow">查询</button>
														<button type="button" class="btn btn-default btn-xs margin-left-10 moreClearBtn noMoreShow">重置</button>
													</div>
												</div>
											</div>
										</div>
										<div class="ps-margin ps-search searchCond">
											<span class="input-icon">
												<input name="describe" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字" value="${consume.describe }">
												<a href="javascript:void(0)" class="ps-searchBtn">
													<i class="glyphicon glyphicon-search circular danger"></i>
												</a>
											</span>
										</div>
										
									</div>
									
								</div>
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" id="delConsumes"> 批量删除 </a>
											</div>
										</div>
									</div>
								</div>
							</form>
								<div class="widget-buttons ps-widget-buttons">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" id="newMeeting" onclick="addConsume('${param.sid}')">
											<i class="fa fa-plus"></i> 消费记录
										</button>
									</div>
								</div>
							</div>
							<div id="listConsumeType_show" class="padding-top-10 text-left " style="display:${empty consume.listConsumeType ? 'none':'block'}">
								<strong>费用类型筛选:</strong>
								<c:forEach items="${consume.listConsumeType }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('crmType','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.typeName }</span>
								</c:forEach>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty listConsumes}">
								<div class="widget-body">
									<form id="delForm">
										<input type="hidden" name="redirectPage"> <input type="hidden" name="sid" value="${param.sid }">
										<table class="table table-striped table-hover">
											<thead>
												<tr role="row">
													<th width="30px" valign="middle">&nbsp;</th>
													<th width="70px" valign="middle">类型</th>
													<th width="100px" valign="middle">金额</th>
													<th width="120px" valign="middle">消费日期</th>
													<th width="120px" valign="middle">地点</th>
													<th valign="middle">描述</th>
													<th width="70px" valign="middle">发票数</th>
													<th width="130px" valign="middle">创建时间</th>
													<th valign="middle">关联报销记录</th>
													<th width="60px" valign="middle">状态</th>
													<th width="90px" valign="middle" style="text-align: center;">操作</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listConsumes}" var="obj" varStatus="vs">
													<tr class="optTr">
														<c:choose>
															<c:when test="${obj.status == 0}">
																<td class="optTd" style="height: 47px"><label class="optCheckBox" style="display: none;width: 20px"> <input class="colored-blue" type="checkbox" name="ids"
																		value="${obj.id}" /> <span class="text"></span>
																</label> <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label></td>
															</c:when>
															<c:otherwise>
																<td style="height: 47px"><label style="display: block;width: 20px">${vs.count}</label></td>
															</c:otherwise>
														</c:choose>
														<td valign="middle">${obj.typeName }</td>
														<td valign="middle" title="￥<fmt:formatNumber value="${obj.amount }" pattern="#,###.##"/>">￥<fmt:formatNumber value="${obj.amount }" pattern="#,###.##"/></td>
														<td valign="middle" class="no-padding-top no-padding-bottom">
															<c:choose>
																<c:when test="${not empty obj.endDate &&  not empty obj.startDate}">
																	<span class="black">${obj.startDate }</span>
																	<br>
																	<span class="gray">${obj.endDate }</span>
																</c:when>
																<c:otherwise>
																	${obj.startDate }${obj.endDate }
																</c:otherwise>
															</c:choose>
														</td>
														<c:choose>
															<c:when test="${not empty obj.leavePlace &&  not empty obj.arrivePlace}">
																<td valign="middle" title="${obj.leavePlace }&nbsp;至&nbsp;${obj.arrivePlace }">
																	${obj.leavePlace }&nbsp;至&nbsp;${obj.arrivePlace }
																</td>
															</c:when>
															<c:otherwise>
																<td valign="middle" title="${obj.leavePlace }${obj.arrivePlace }">
																	${obj.leavePlace }${obj.arrivePlace }
																</td>
															</c:otherwise>
														</c:choose>
														
														<td valign="middle" title="${obj.describe }">${obj.describe }</td>
														<td valign="middle">${obj.invoiceNum > 0 ?obj.invoiceNum:0 }张</td>
														<td valign="middle">${fn:substring(obj.recordCreateTime,0,16) }</td>
														<td valign="middle"><a href="javascript:void(0)" class="spFlowIns" insId="${obj.instanceId }" style="color: #2a6496" title="${obj.spFlowName }">${obj.spFlowName }</a></td>
														<td valign="middle">
															<c:choose>
																<c:when test="${obj.status == 0 }">
																	<span style="color: red;">待报销</span>
																</c:when>
																<c:when test="${obj.status == 1 }">
																	<span style="color: blue;">报销中</span>
																</c:when>
																<c:when test="${obj.status == 2 }">
																	<span style="color: green;">已报销</span>
																</c:when>
															</c:choose>
														
														</td>
														<td valign="middle" style="text-align: center;">
															<c:if test="${obj.status == 0 }">
																<a href="javascript:void(0)" onclick="viewConsume('${obj.id}','edit')" >编辑</a>&nbsp;|
															</c:if>
															<a href="javascript:void(0)" onclick="viewConsume('${obj.id}','view')" >查看</a>
														</td>
														
														
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/consume/listConsume"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>暂无相关消费记录信息！</h2>
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
	<!-- Main Container -->
</body>

<script type="text/javascript">

$(function(){
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsgV2(msg, o, cssctl);
		}
	}); 
	
	//消费记录删除
	$("#delConsumes").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除消费记录吗？',{icon:3,title:'询问框'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').attr("action","/consume/deleteConsumes");
				$('#delForm').submit();
			}, function(){
			});	
		}else{
			window.top.layer.alert('请先选择要删除消费记录。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
		}
	});
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		$("#searchForm").submit();
	})
	//清空时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		$("#searchForm").submit();
	})
	
	$("body").on("blur",".moreSearch",function(){
		$("#searchForm").submit();
	})
	
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId){
			viewSpFlow(insId);
		}
	})
	
})

//流程查看页面
function viewSpFlow(instanceId){
	var url = "/workFlow/viewSpFlow?sid="+'${param.sid}'+"&instanceId="+instanceId;
	openWinByRight(url)
}

//费用类型筛选
function consumeTypeFilter(obj,typeId){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#type").val(typeId);
	if(!typeId){
		$("#crmType_select").find("option").remove();
	}
	$("#searchForm").submit();
}

/**
 *费用类型多选树
 */
function consumeTypeMoreTree(sid,tag){
	 window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ["500px", "470px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/consumeTypeMorePage?sid="+sid,'no'],
		  btn: ['确定','清空','取消'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var options = $("#"+tag+"_select").find("option")
			  iframeWin.setOptions(options);
			   
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  });
		  },
		   yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnConsumeType();
				if(options.length>0){
					//人选被选择后执行动作函数体
					consumeTypeMoreTreeCallBack(options,tag);
					window.top.layer.close(index)
				}else{
					layer.msg("请选择费用类型")
					return false;
				}
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  }
		});
}

//费用类型筛选
function consumeTypeMoreTreeCallBack(options,tag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+tag).val('');
	$("#"+tag+"_select").find("option").remove();
	for(var i =0;i<options.length;i++){
		$("#"+tag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
	}
	$("#searchForm").submit();
}
//移除筛选
function removeChoose(tag,sid,value){
	$("#"+tag+"_select").find("option[value='"+value+"']").remove();
	$("#searchForm").submit();
}

//添加消费记录
function addConsume(sid) {
	var url = '/consume/addConsumePage?sid=' + sid;
	openWinByRight(url);
} 
//查看详情
function viewConsume(id,page) {
	var url = '/consume/viewConsumePage?sid=' + '${param.sid}' + "&id=" + id +"&showPage=" + page;
	openWinByRight(url);
}

</script>
<style type="text/css">
td,th{
white-space: nowrap;
text-overflow: ellipsis;
overflow: hidden;
}
table{
table-layout: fixed;
}
</style>
</html>

