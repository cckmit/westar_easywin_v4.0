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
<html xmlns="http://www.w3.org/1999/xhtml">
	<body>
	<!-- Page Content -->
            <div class="page-content">
                <!-- Page Body -->
                <div class="page-body">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="widget">
                                <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
										<div id="listTableDiv">
											<!-- <form id="searchForm" action="/statistics/item/listItemStatics" class="subform"> -->
												<input type="hidden" name="redirectPage" />
												<input type="hidden" name="sid" value="${param.sid}" />
												<input type="hidden" name="state" id="state" value="${item.state}" />
												<input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab}">
			
												<div class="searchCond" style="display: block">
													<div class="btn-group pull-left">
														<div class="table-toolbar ps-margin">
															<div class="btn-group" id="stateDiv">
																<a class="btn btn-default dropdown-toggle btn-xs" id="stateDis" data-toggle="dropdown">
																	状态筛选 <i class="fa fa-angle-down"></i>
																</a>
																<ul class="dropdown-menu dropdown-default">
																	<li>
																		<a href="javascript:void(0)" class="state" state="0">全部状态</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="state" state="1">进行中</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="state" state="4">已完结</a>
																	</li>
																</ul>
															</div>
														</div>
														<c:if test="${param.searchTab!=11 && not empty param.searchTab}">
															<div class="table-toolbar ps-margin">
																<div class="btn-group">
																	<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
																		<c:choose>
																			<c:when test="${not empty item.ownerName}">
																				<font style="font-weight:bold;">${item.ownerName}</font>
																			</c:when>
																			<c:otherwise>责任人</c:otherwise>
																		</c:choose>
																		<i class="fa fa-angle-down"></i>
																	</a>
																	<ul class="dropdown-menu dropdown-default">
																		<li>
																			<a href="javascript:void(0)" class="userMoreElementClean">不限条件</a>
																		</li>
																		<li>
																			<a href="javascript:void(0)" class="userMoreElementSelect">人员选择</a>
																		</li>
																	</ul>
																</div>
															</div>
															<div style="float: left;width: 250px;display: none">
																<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
														</c:if>
			
														<div class="table-toolbar ps-margin">
															<div class="btn-group cond" id="moreCondition_Div">
																<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
																	<span>更多</span>
																	<i class="fa fa-angle-down"></i>
																</a>
																<div class="dropdown-menu dropdown-default padding-bottom-10 moreConsSelect" style="min-width: 330px;">
																	<div class="ps-margin ps-search padding-left-10">
																		<span class="btn-xs">起止时间：</span>
																		<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.startDate}" id="startDate" name="startDate" placeholder="开始时间"
																			onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
																		<span>~</span>
																		<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.endDate}" id="endDate" name="endDate" placeholder="结束时间"
																			onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
																	</div>
																	<div class="ps-clear padding-top-10" style="text-align: center;">
																		<button type="button" class="btn btn-primary btn-xs search">查询</button>
																		<button type="button" class="btn btn-default btn-xs reset margin-left-10">重置</button>
																	</div>
																</div>
															</div>
														</div>
			
													</div>
													<div class="ps-margin ps-search">
														<span class="input-icon">
															<input id="itemName" class="form-control ps-input" name="itemName" type="text" placeholder="请输入关键字" value="">
															<a href="javascript:void(0)" class="ps-searchBtn" id="itemNamea">
																<i class="glyphicon glyphicon-search circular danger"></i>
															</a>
														</span>
													</div>
												</div>
			
											<!-- </form> -->
											<div class="widget-buttons ps-widget-buttons">
												<button class="btn btn-info  btn-xs" onclick="excelTwoExport('项目任务统计表')" >导出excel</button>
											</div>
										</div>
										<div class=" padding-top-10 text-left ownerDisDiv" style="display:none">
											<strong>责任人筛选:</strong>
										</div>
										
										<div id="detailTableDiv">
											<span class="widget-caption themeprimary pull-left margin-top-10 blue" id="pageTitle" style="font-size:20px;cursor: pointer;"></span>
										</div>
										
                               		 </div>
	                                <div class="widget-body">
										<div class="widget-body" id="tableView" style="display: block">
											<table id="dataTable" class="table table-bordered" style="text-align: center;">
											</table>
											<div class="panel-body ps-page bg-white" style="font-size: 12px">
								 <p class="pull-left ps-pageText">共<b class="badge" id="totalNum"></b>条记录</p>
								 <ul class="pagination pull-right" id="pageDiv">
								 </ul>
							</div>
										</div>
		                                <div class="widget-body" id="detailTableView" style="display: none">
											<table id="dataTable" class="table table-bordered" style="text-align: center;">
											</table>
										</div>
	                                </div>
                            	</div>
                        	</div>
	                    </div>
                	</div>
                <!-- /Page Body -->
            	</div>
            <!-- /Page Content -->
            
        </div>
        <!-- /Page Container -->
	</body>
	<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
	var url = '/statistics/item/listItemStatics'; //全局变量配置项
	var option = {}; //启动加载页面效果
	var pageSize = 10;
	var pageNum = 0;
	var sid = '${param.sid}';
	
	 //异步取得数据
	function getData() {
		 
		 
		var params = {
			"sid": sid,
			"pageNum": pageNum,
			"pageSize": pageSize
		};
		
		//其他参数
		var inputs  = $("#listTableDiv").find("input");
		if(inputs && inputs.get(0)){
			$.each(inputs,function(index,input){
				var inputName = $(input).attr("name");
				var inputVal = $(input).val();
				if(inputVal){
					params[inputName]=inputVal
				}
			})
		}
		var selects = $("#listTableDiv").find("select");
		if(selects && selects.get(0)){
			$.each(selects,function(selectIndex,select){
				var list =$(select).attr("list");
				var listkey =$(select).attr("listkey");
				var listvalue =$(select).attr("listvalue");
				var options = $(select).find("option");
				if(options && options.get(0)){
					$.each(options,function(index,option){
						params[list+"["+index+"]."+listkey]=$(option).val()
						params[list+"["+index+"]."+listvalue] =$(option).html()
					})
				}
			})
		}
		getSelfJSON(url, params,function(data) {
			if (data.status == 'y') {
				var pageBean = data.pageBean;
				$("#totalNum").html(pageBean.totalCount);
				$("#totalNum").parent().parent().css("display", "block");
				if (pageBean.totalCount <= pageSize) {
					$("#totalNum").parent().parent().css("display", "none");
				} //分页，PageCount是总条目数，这是必选参数，其它参数都是可选
				$("#pageDiv").pagination(pageBean.totalCount, {
					callback: PageCallback,
					//PageCallback() 为翻页调用次函数。
					prev_text: "<<",
					next_text: ">>",
					items_per_page: pageSize,
					num_edge_entries: 0,
					//两侧首尾分页条目数
					num_display_entries: 3,
					//连续分页主体部分分页条目数
					current_page: pageNum,
					//当前页索引
				});
				$("#dataTable").html('');
				initTable(pageBean.recordList);
			} else {
				console.log(data);
			}
		});
	} //翻页调用   
	function PageCallback(index, jq) {
		pageNum = index;
		getData();
	}
	$(function() {
		$(".subform").Validform({
			tiptype: function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError: true
		});
		getData(); 
		//人员多选
		$("body").off("click", ".userMoreElementSelect").on("click", ".userMoreElementSelect",function() {
			userMore(null, null, sid, 'yes', null,function(options) {
				$("#owner_select").html('');
				if (options && options.length > 0) {
					$.each(options,
					function(index, object) {
						var option = $("<option value='" + $(object).val() + "'>" + $(object).text() + "</option>");
						$("#owner_select").append(option)
					}); 
					ownerNameDisplay(); //筛选责任人名称显示
					getData();
				}
			});
		}); 
		//清空责任人筛选条件
		$("body").off("click", ".userMoreElementClean").on("click", ".userMoreElementClean",function() {
			$("#owner_select").find("option").remove();
			ownerNameDisplay(); //筛选责任人名称显示
			getData();
			$(".ownerDisDiv").css("display", "none");
		}); //名称筛选
		$("#itemName").blur(function() {
			getData();
		}); //文本框绑定回车提交事件
		$("#itemName").bind("keydown",
		function(event) {
			if (event.keyCode == "13") {
				getData();
			}
		}); //名称筛选文本框后的A标签点击事件绑定
		$("body").off("click", "#itemNamea").on("click", "#itemNamea",
		function() {
			getData();
		});
	}); //状态筛选
	$(document).on("click", "#stateDiv .state",function() {
		var state = $(this).attr("state");
		$("#searchForm [name='state']").val(state);
		if (state > 0) {
			var typeName = $(this).text();
			var html = '<font style="font-weight:bold;">' + typeName + '</font>';
			html += '<i class="fa fa-angle-down"></i>';
			$("#stateDis").html(html);
		} else {
			var html = '状态筛选';
			html += '<i class="fa fa-angle-down"></i>';
			$("#stateDis").html(html);
		}
		getData();
	}); 
	//查询起始时间筛选
	$(document).on("click", "#moreCondition_Div .search",function() {
		getData();
		$("#moreCondition_Div").removeClass("open");
		if ($("#startDate").val() || $("#endDate").val()) {
			$("#moreCondition_Div a").children("span").remove();
			$("#moreCondition_Div a").append($("<span><font style=\"font-weight:bold;\">筛选中</font></span>"));
		} else {
			$("#moreCondition_Div a").children("span").remove();
			$("#moreCondition_Div a").append($("<span>更多</span>"));
		}
	}); //充值查询时间
	$(document).on("click", "#moreCondition_Div .reset",
	function() {
		$("#startDate").val("");
		$("#endDate").val("");
	}); //项目名称筛选
	function nameFilter(index, obj) {
		var tag = false;
		if ($("#itemName").val()) {
			if ($(obj).attr("itemName").indexOf($("#itemName").val()) > -1) {
				tag = true;
			}
		} else {
			tag = true;
		}
		return tag;
	} //创建时间筛选
	function dateTimeFilter(index, obj) {
		var tag = false;
		if ($("#startDate").val() || $("#endDate").val()) {
			var startTime = $("#startDate").val();
			var start = new Date(startTime.replace("-", "/").replace("-", "/"));
			var endTime = $("#endDate").val();
			var end = new Date(endTime.replace("-", "/").replace("-", "/"));
			var recordCreateTime = new Date($(obj).attr("recordCreateTime").replace("-", "/").replace("-", "/"));
			if ($("#startDate").val() && $("#endDate").val()) {
				if (recordCreateTime >= start && recordCreateTime <= end) {
					tag = true;
				}
			} else if ($("#startDate").val()) {
				if (recordCreateTime >= start) {
					tag = true;
				}
			} else if ($("#endDate").val()) {
				if (recordCreateTime <= end) {
					tag = true;
				}
			}
		} else {
			tag = true;
		}
		return tag;
	} 
	//项目负责人筛选
	function ownerFilter(index, obj) {
		var tag = false;
		if ($("#owner_select option").length > 0) { //有才筛选
			$("#owner_select option").each(function() {
				if ($(obj).filter("tr[owner='" + $(this).val() + "']").length > 0) {
					tag = true;
					return false;
				}
			});
		} else { //没有筛选动作
			tag = true;
		}
		return tag;
	} 
	//筛选责任人名称显示
	function ownerNameDisplay() {
		$(".ownerDisDiv").children("span").remove();
		if ($("#owner_select option").length > 0) { //有才筛选
			$(".ownerDisDiv").css("display", "block");
			$("#owner_select option").each(function() {
				var userName = $(this).text();
				var userId = $(this).val();
				var span = $("<span></span>");
				$(span).attr("title", "双击移除");
				$(span).css("cursor", "pointer");
				$(span).addClass("label label-default margin-right-5 margin-bottom-5");
				$(span).text(userName);
				$(span).dblclick(function() {
					removerUser(userId); //清除筛选人
				}); //双击事件绑定
				$(".ownerDisDiv").append(span);
			});
		}
	} 
	//清除筛选人
	function removerUser(userId) {
		$("#owner_select").find("option[value='" + userId + "']").remove();
		ownerNameDisplay(); //筛选责任人名称显示
		getData();
		if ($("#owner_select option").length < 1) { //无责任人筛选
			$(".ownerDisDiv").css("display", "none");
		}
	} 
	//项目状态筛选
	function stateFilter(index, obj) {
		var state = $("#searchForm [name='state']").val();
		if (state > 0) {
			if ($(obj).filter("tr[state='" + state + "']").length > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	} 
	//总计详情
	$(document).on("click", ".taskItemDetail",function() {
		var itemId = $(this).attr("itemId");
		 var detailUrl = '/statistics/item/listItemTaskDetail';
		 var params = {
					"sid": sid,
					"itemId": itemId,
				};
		//异步取得数据
	        getSelfJSON(detailUrl,params,function (data) {
	        	$("#listTableDiv").hide(500,null);
	        	$("#detailTableDiv").show(500,null);
	           $("#pageTitle").html("&lt;&lt;项目：“"+data.item.itemName+"”——任务明细");//初始化页面title
	           $("#tableView").hide(500,null);
	          
	    	   initDetailTable(data);
	    	});
	}); 
	//返回列表
	$(document).on("click", "#pageTitle",function() {
    	$("#listTableDiv").show(500,null);
    	$("#detailTableDiv").hide(500,null);
       	$("#tableView").show(500,null);
   		$("#detailTableView").hide(500,null);
	}); 
	
	//初始化表格视图
	function initTable(data) {
		var table = $("#tableView").find("table");
		$(table).html('');
		var thead = $("<thead></thead>");
		var headTr = $("<tr></tr>");
		var headFirstTh = "<th style='text-align: center;font-weight:bold;'>序号</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>项目名称</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>负责人</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>创建于</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>项目阶段</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>任务数</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>已耗时（小时）</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>超时（小时）</th>";
		$(headTr).append(headFirstTh);
		$(thead).append(headTr);
		$(table).append(thead);
		var tbody = $("<tbody></tbody>");
		$(table).append(tbody);
		
		if(data){
			 var preItemId=0;
			 var itemIndex = 1;
			$.each(data,function(index,stageInfo){
				 var _tr = $("<tr></tr>");
				 $(_tr).attr("itemId",stageInfo.itemId);
				 $(_tr).attr("state",stageInfo.state);
				 $(_tr).attr("owner",stageInfo.owner);
				 $(_tr).attr("recordcreatetime",stageInfo.recordcreatetime);
				 //序号
				 var _td1 = $("<td ' isParent='1' style='font-size:12px;text-align: center;'></td>");
				 
				 $(_td1).attr("itemId",stageInfo.itemId);
				 //项目名称
				 var _td2 = $("<td ' isParent='1' style='font-size:12px;text-align: center;'></td>");
				 $(_td2).html("<a href='javascript:void(0)' onclick ='viewItem(" + stageInfo.itemId + ");' title='项目详情查看'>" + stageInfo.itemName + "</a>");
				 $(_td2).attr("itemId",stageInfo.itemId);
				 //负责人
				 var _td3 = $("<td ' isParent='1' style='font-size:12px;text-align: center;'></td>");
				 $(_td3).html(stageInfo.ownerName);
				 $(_td3).attr("itemId",stageInfo.itemId);
				//创建于
				 var _td4 = $("<td ' isParent='1' style='font-size:12px;text-align: center;'></td>");
				
				 $(_td4).html(stageInfo.recordcreatetime);
				 $(_td4).attr("itemId",stageInfo.itemId);
				//阶段名
				 var _td5 = $("<td style='font-size:12px;text-align: center;'></td>");
				 $(_td5).html(stageInfo.name);
				
				//任务数
				 var _td6 = $("<td style='font-size:12px;text-align: center;color:green;'></td>");
				 if(stageInfo.name){
				 	$(_td6).html(stageInfo.listTask.length);
					}
				 $(_td6).attr("taskNum",stageInfo.itemId);
				//已耗时
				 var _td7 = $("<td style='font-size:12px;text-align: center;color:green;'></td>");
				 if(stageInfo.name){
					 $(_td7).html(stageInfo.usedTimes);
					}
				 $(_td7).attr("userTimes",stageInfo.itemId);
				//超时
				 var _td8 = $("<td style='font-size:12px;text-align: center;color:green;'></td>");
				 if(stageInfo.name){
					 $(_td8).html(stageInfo.overTimes > 0 ? stageInfo.overTimes: 0);
					}
				 $(_td8).attr("overTimes",stageInfo.itemId);
				 
				//判断是否是同一项目
			 	if(stageInfo.itemId == preItemId){
			 		 var firstTr = $(tbody).find("tr[itemId='"+stageInfo.itemId+"']:eq(0)");
				     var num =  $(tbody).find("tr[itemId='"+stageInfo.itemId+"']").length +1;
				 	 $(firstTr).find("td:eq(0)").attr("rowspan",num);//行号
				 	 $(firstTr).find("td:eq(1)").attr("rowspan",num);//项目名称
				 	 $(firstTr).find("td:eq(2)").attr("rowspan",num);//负责人
				 	 $(firstTr).find("td:eq(3)").attr("rowspan",num);//创建于
				 	 $(_tr).append(_td5);
					 $(_tr).append(_td6);
					 $(_tr).append(_td7);
					 $(_tr).append(_td8);
			 	}else{
			 		 var preFirstTr = $(tbody).find("tr[itemId='"+preItemId+"']:eq(0)");
			 		 var stageName = $(preFirstTr).find("td:eq(4)").html();
			 		//添加总计一行
			 		if(stageName && index != 0 && stageName.length>0){
			 			 var _trTotal = $("<tr></tr>");
						 $(_trTotal).attr("itemId",preItemId);
			 			
					     var num =  $(tbody).find("tr[itemId='"+preItemId+"']").length +1;
					 	 $(preFirstTr).find("td:eq(0)").attr("rowspan",num);//行号
					 	 $(preFirstTr).find("td:eq(1)").attr("rowspan",num);//项目名称
					 	 $(preFirstTr).find("td:eq(2)").attr("rowspan",num);//负责人
					 	 $(preFirstTr).find("td:eq(3)").attr("rowspan",num);//创建于
					 	 
					 	 var taskTd = $(tbody).find("td[taskNum='"+preItemId+"']");
				 		 var usedTimesTd = $(tbody).find("td[userTimes='"+preItemId+"']");
					 	 var overTimesTd = $(tbody).find("td[overTimes='"+preItemId+"']");
					 	 
					 	 var  taskTotal= 0;
					 	 var  usedTimeTotal= 0;
					 	var  overTimeTotal= 0;
					 	
					  	 for(var i =0;i<taskTd.length;i++){
					  		taskTotal += parseInt(taskTd.eq(i).html());
					  		usedTimeTotal += parseInt(usedTimesTd.eq(i).html());
					  		overTimeTotal += parseInt(overTimesTd.eq(i).html());
					  	}  
					 	
					 	var td1 = $("<td style='font-size:12px;text-align: center;color:red;'><a href = 'javaScript:void(0)' class='taskItemDetail' itemId=" +preItemId + " style='color:red;' title='项目任务明细查看'>合计</a></td>");
						var td2 = $("<td style='font-size:12px;text-align: center;color:red;'>" + taskTotal + "</td>");
						var td3 = $("<td style='font-size:12px;text-align: center;color:red;'>" + usedTimeTotal + "</td>");
						var td4 = $("<td style='font-size:12px;text-align: center;color:red;'>" + overTimeTotal + "</td>");
					 	 
					 	 $(_trTotal).append(td1);
						 $(_trTotal).append(td2);
						 $(_trTotal).append(td3);
						 $(_trTotal).append(td4);
						 $(tbody).append(_trTotal);
			 		}
			 		
		 			$(_td1).html(itemIndex);
			 		 $(_tr).append(_td1);
					 $(_tr).append(_td2);
					 $(_tr).append(_td3);
					 $(_tr).append(_td4);
					 $(_tr).append(_td5);
					 $(_tr).append(_td6);
					 $(_tr).append(_td7);
					 $(_tr).append(_td8);
					 itemIndex += 1;
			 		
			 	}
				 $(tbody).append(_tr);
				 
				 //最后一行加总计
				 if(data.length-1 == index ){
				 		var lastTr = $(tbody).find("tr[itemId='"+stageInfo.itemId+"']:eq(0)");
				 		var _trTotal = $("<tr></tr>");
						 $(_trTotal).attr("itemId",stageInfo.itemId);
			 			
					     var num =  $(tbody).find("tr[itemId='"+stageInfo.itemId+"']").length +1;
					 	 $(lastTr).find("td:eq(0)").attr("rowspan",num);//行号
					 	 $(lastTr).find("td:eq(1)").attr("rowspan",num);//项目名称
					 	 $(lastTr).find("td:eq(2)").attr("rowspan",num);//负责人
					 	 $(lastTr).find("td:eq(3)").attr("rowspan",num);//创建于
					 	 
					 	 var taskTd = $(tbody).find("td[taskNum='"+stageInfo.itemId+"']");
				 		 var usedTimesTd = $(tbody).find("td[userTimes='"+stageInfo.itemId+"']");
					 	 var overTimesTd = $(tbody).find("td[overTimes='"+stageInfo.itemId+"']");
					 	 
					 	 var  taskTotal= 0;
					 	 var  usedTimeTotal= 0;
					 	var  overTimeTotal= 0;
					 	
					  	 for(var i =0;i<taskTd.length;i++){
					  		taskTotal += parseInt(taskTd.eq(i).html());
					  		usedTimeTotal += parseInt(usedTimesTd.eq(i).html());
					  		overTimeTotal += parseInt(overTimesTd.eq(i).html());
					  	}  
					 	
					 	var td1 = $("<td style='font-size:12px;text-align: center;color:red;'><a href = 'javaScript:void(0)' class='taskItemDetail' itemId=" +stageInfo.itemId+ " style='color:red;' title='项目任务明细查看'>合计</a></td>");
						var td2 = $("<td style='font-size:12px;text-align: center;color:red;'>" + taskTotal + "</td>");
						var td3 = $("<td style='font-size:12px;text-align: center;color:red;'>" + usedTimeTotal + "</td>");
						var td4 = $("<td style='font-size:12px;text-align: center;color:red;'>" + overTimeTotal + "</td>");
					 	 
					 	 $(_trTotal).append(td1);
						 $(_trTotal).append(td2);
						 $(_trTotal).append(td3);
						 $(_trTotal).append(td4);
						 $(tbody).append(_trTotal);
			 	}
				 preItemId = stageInfo.itemId;
			});
		}
	} 
	//数据列追加
	function trAppend(data) {
		var tbody = $("#tableView").find("table tbody");
		var listBusReport = data;
		$.each(listBusReport,
		function(index, obj) {
			var taskTotal = 0; //任务总数
			var usedTimeTotal = 0; //任务总耗时
			var overTimeTotal = 0; //任务总超时
			var listStagedItemInfo = obj.listStagedItemInfo;
			var tr = $("<tr dataRow='1' state='" + obj.state + "' owner='" + obj.owner + "' recordCreateTime='" + obj.recordCreateTime + "' itemName='" + obj.itemName + "'></tr>");
			var rowNumTd = $("<td itemId='" + obj.id + "' isParent='1' style='font-size:12px;text-align: center;'>" + (index + 1) + "</td>");
			$(tr).append(rowNumTd);
			var td = $("<td itemId='" + obj.id + "' isParent='1' style='font-size:12px;text-align: center;'><a href='javascript:void(0)' onclick ='viewItem(" + obj.id + ");' title='项目详情查看'>" + obj.itemName + "</a></td>");
			var ownerTd = $("<td itemId='" + obj.id + "' isParent='1' style='font-size:12px;text-align: center;'>" + obj.ownerName + "</td>");
			$(tr).append(td);
			$(tr).append(ownerTd);
			var timeTd = $("<td itemId='" + obj.id + "' isParent='1' style='font-size:12px;text-align: center;'>" + obj.recordCreateTime + "</td>");
			$(tr).append(timeTd);
			$(tbody).append(tr); //有效子元素的个数，默认从0 开始
			$.each(listStagedItemInfo,
			function(indexA, vo) {
				$(tbody).find("td[itemId='" + obj.id + "'][isParent='1']").attr("rowspan", (listStagedItemInfo.length + 1));
				if (indexA == 0) { //第一行需添加到第一个tr中
					var p = $(tbody).find("td[itemId='" + obj.id + "'][isParent='1']").parent();
					var td2 = $("<td style='font-size:12px;text-align: center;'>" + vo.name + "</td>");
					var td3 = $("<td style='font-size:12px;text-align: center;color:green;'>" + vo.listTask.length + "</td>");
					var td4 = $("<td style='font-size:12px;text-align: center;color:green;'> " + vo.usedTimes + "</td>");
					var td5 = $("<td style='font-size:12px;text-align: center;color:green;'> " + (vo.overTimes > 0 ? vo.overTimes: 0) + "</td>");
					$(p).attr("itemId", obj.id);
					$(p).append(td2);
					$(p).append(td3);
					$(p).append(td4);
					$(p).append(td5);
					taskTotal += vo.listTask.length; //任务数累加
					usedTimeTotal += vo.usedTimes; //任务耗时累加
					overTimeTotal += (vo.overTimes > 0 ? vo.overTimes: 0); //任务超时累加
				} else { //否则重新整一行
					var newTr = $("<tr itemId='" + obj.id + "' rownum='" + indexA + "' state='" + obj.state + "' owner='" + obj.owner + "' recordCreateTime='" + obj.recordCreateTime + "' itemName='" + obj.itemName + "'></tr>");
					$(tbody).append(newTr); //添加数据
					var td2 = $("<td style='font-size:12px;text-align: center;'>" + vo.name + "</td>");
					var td3 = $("<td style='font-size:12px;text-align: center;color:green;'>" + vo.listTask.length + "</td>");
					var td4 = $("<td style='font-size:12px;text-align: center;color:green;'> " + vo.usedTimes + "</td>");
					var td5 = $("<td style='font-size:12px;text-align: center;color:green;'> " + (vo.overTimes > 0 ? vo.overTimes: 0) + "</td>");
					$(newTr).attr("itemId", obj.id);
					$(newTr).append(td2);
					$(newTr).append(td3);
					$(newTr).append(td4);
					$(newTr).append(td5);
					taskTotal += vo.listTask.length; //任务数累加
					usedTimeTotal += vo.usedTimes; //任务耗时累加
					overTimeTotal += (vo.overTimes > 0 ? vo.overTimes: 0); //任务超时累加
				}
				if (indexA == (listStagedItemInfo.length - 1)) { //到最后一次循环式
					//合计
					var newTr = $("<tr itemId='" + obj.id + "' rownum='" + (indexA + 1) + "' state='" + obj.state + "' owner='" + obj.owner + "' recordCreateTime='" + obj.recordCreateTime + "' itemName='" + obj.itemName + "'></tr>");
					$(tbody).append(newTr); //添加数据
					var td2 = $("<td style='font-size:12px;text-align: center;color:red;'><a href='/statistics/task/statisticsTaskPage?sid=${param.sid}&searchTab=24&statisticsType=6&itemId=" + obj.id + "' style='color:red;' title='项目任务明细查看'>合计</a></td>");
					var td3 = $("<td style='font-size:12px;text-align: center;color:red;'>" + taskTotal + "</td>");
					var td4 = $("<td style='font-size:12px;text-align: center;color:red;'>" + usedTimeTotal + "</td>");
					var td5 = $("<td style='font-size:12px;text-align: center;color:red;'>" + overTimeTotal + "</td>");
					$(newTr).attr("itemId", obj.id);
					$(newTr).append(td2);
					$(newTr).append(td3);
					$(newTr).append(td4);
					$(newTr).append(td5);
				}
			});
		});
	}
	//项目详情
	function initDetailTable(data){
		 $("#detailTableView").find("table").html('');
		 $("#detailTableView").show();
		var table = $("#detailTableView").find("table")
     	   $(table).html('');
     	   var thead = $("<thead></thead>");
     	   var headTr = $("<tr></tr>");
     	   var headFirstTh = "<th style='text-align: center;font-weight:bold;'>序号</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>项目阶段</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>任务</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>状态</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>办理时限</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>开始时间</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>结束时间</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>已耗时（小时）</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>超时（小时）</th>";
     	   $(headTr).append(headFirstTh)
     	   $(thead).append(headTr);
     	   $(table).append(thead);
    	   var tbody = $("<tbody></tbody>");
     	   $(table).append(tbody);
     	   trDetailAppend(data);//数据行加载
	}
	      //数据列追加
      function trDetailAppend(data){
    	   var tbody = $("#detailTableView").find("table tbody");
    	   var item = data.item;
    	   var listStagedItemInfo = item.listStagedItemInfo;
		   $.each(listStagedItemInfo,function(indexA,stageVo){
			   var tr = $("<tr dataRow='1' state='"+item.state+"' owner='"+item.owner+"' recordCreateTime='"+item.recordCreateTime+"' itemName='"+item.itemName+"'></tr>");
			   var rowNumTd = $("<td stageId='"+stageVo.realId+"' isParent='1' style='font-size:12px;text-align: center;'>"+(indexA+1)+"</td>");
			   $(tr).append(rowNumTd);
			   //阶段名称行
			   var stageTd = $("<td style='font-size:12px;text-align: center;' stageId='"+stageVo.realId+"' isParent='1'>"+stageVo.name+"</td>");
			   $(tr).append(stageTd);
			   $(tbody).append(tr);

			   var listTask = stageVo.listTask;
			   var usedTimeTotal = 0;//任务总耗时
			   var overTimeTotal = 0;//任务总超时
 		       var taskTotal = listTask.length;//任务总数
	  		   	 if(listTask.length>0){//阶段有任务
		  		   	$.each(listTask,function(taskIndex,taskVo){
		 	  		    $(tbody).find("td[stageId='"+stageVo.realId+"'][isParent='1']").attr("rowspan",(listTask.length+1));
			  		   	if(taskIndex==0){//第一行需添加到第一个tr中
							 var p = $(tbody).find("td[stageId='"+stageVo.realId+"'][isParent='1']").parent();
							 var td2 = $("<td style='font-size:12px;text-align: left;'><a href='javascript:void(0)' onclick ='viewTask("+taskVo.id+");'>"+taskVo.taskName+"</a></td>");
			   		    	 var td3 = $("<td style='font-size:12px;text-align: center;color:"+(taskVo.state==4?'red':'green')+";'>"+(taskVo.state==1?'进行中':(taskVo.state==4?'已完成':挂起))+"</td>");
			   		    	 var td4 = $("<td style='font-size:12px;text-align: center;'> "+(strIsNull(taskVo.dealTimeLimit)?'/':taskVo.dealTimeLimit)+"</td>");
			   		    	 var td5 = $("<td style='font-size:12px;text-align: center;'> "+taskVo.recordCreateTime+"</td>");
			   		    	 var td6 = $("<td style='font-size:12px;text-align: center;'> "+(taskVo.state==4?taskVo.endTime:'/')+"</td>");
			   		    	 var td7 = $("<td style='font-size:12px;text-align: center;'> "+taskVo.usedTimes+"</td>");
			   		    	 var td8 = $("<td style='font-size:12px;text-align: center;'> "+(strIsNull(taskVo.dealTimeLimit)?'/':taskVo.overTimes)+"</td>");
			   		    	 $(p).append(td2);
			   		    	 $(p).append(td3);
			   		    	 $(p).append(td4);
			   		    	 $(p).append(td5);
			   		    	 $(p).append(td6);
			   		    	 $(p).append(td7);
			   		    	 $(p).append(td8);
			   		    	 usedTimeTotal += taskVo.usedTimes;//任务耗时累加
			   		    	 overTimeTotal += (taskVo.overTimes>0?taskVo.overTimes:0);//任务超时累加
						 }else{//否则重新整一行
							 var newTr = $("<tr itemId='"+item.id+"' rownum='"+taskIndex+"' state='"+item.state+"' owner='"+item.owner+"' recordCreateTime='"+item.recordCreateTime+"' itemName='"+item.itemName+"'></tr>");
						 	 $(tbody).append(newTr);
						 	 //添加数据
			   				 var td2 = $("<td style='font-size:12px;text-align: left;'><a href='javascript:void(0)' onclick ='viewTask("+taskVo.id+");'>"+taskVo.taskName+"</a></td>");
			   		    	 var td3 = $("<td style='font-size:12px;text-align: center;color:"+(taskVo.state==4?'red':'green')+";'>"+(taskVo.state==1?'进行中':(taskVo.state==4?'已完成':挂起))+"</td>");
			   		    	 var td4 = $("<td style='font-size:12px;text-align: center;'> "+(strIsNull(taskVo.dealTimeLimit)?'/':taskVo.dealTimeLimit)+"</td>");
			   		    	 var td5 = $("<td style='font-size:12px;text-align: center;'> "+taskVo.recordCreateTime+"</td>");
			   		    	 var td6 = $("<td style='font-size:12px;text-align: center;'> "+(taskVo.state==4?taskVo.endTime:'/')+"</td>");
			   		    	 var td7 = $("<td style='font-size:12px;text-align: center;'> "+taskVo.usedTimes+"</td>");
			   		    	 var td8 = $("<td style='font-size:12px;text-align: center;'> "+(strIsNull(taskVo.dealTimeLimit)?'/':taskVo.overTimes)+"</td>");
			   		    	 $(newTr).append(td2);
			   		    	 $(newTr).append(td3);
			   		    	 $(newTr).append(td4);
			   		    	 $(newTr).append(td5);
			   		    	 $(newTr).append(td6);
			   		    	 $(newTr).append(td7);
			   		    	 $(newTr).append(td8);
			   		    	 usedTimeTotal += taskVo.usedTimes;//任务耗时累加
			   		    	 overTimeTotal += (taskVo.overTimes>0?taskVo.overTimes:0);//任务超时累加
						 }
			  		     if(taskIndex==(listTask.length-1)){//到最后一次循环式
			  	    	    //合计
			  		    	var newTr = $("<tr itemId='"+item.id+"' rownum='"+(taskIndex+1)+"' state='"+item.state+"' owner='"+item.owner+"' recordCreateTime='"+item.recordCreateTime+"' itemName='"+item.itemName+"'></tr>");
			  			    $(tbody).append(newTr);
			  			    //添加数据
			  				var td1 = $("<td style='font-size:12px;text-align: center;color:red;' colspan=2>任务总数："+taskTotal+"（个）</td>");
			  				var td2 = $("<td style='font-size:12px;text-align: center;color:red;' colspan=3>用时合计</td>");
			  		    	var td3 = $("<td style='font-size:12px;text-align: center;color:red;'>"+usedTimeTotal+"</td>");
			  		    	var td4 = $("<td style='font-size:12px;text-align: center;color:red;'>"+overTimeTotal+"</td>");
			  		    	$(newTr).append(td1);
			  		    	$(newTr).append(td2);
			  		    	$(newTr).append(td3);
			  		    	$(newTr).append(td4);
					 	}
		  		   	});
	  		   	 }else{//阶段无任务记录
		  		   	 var p = $(tbody).find("td[stageId='"+stageVo.realId+"'][isParent='1']").parent();
					 var td = "<td style='font-size:12px;text-align: center;'>/</td>";
					 td += "<td style='font-size:12px;text-align: center;'>/</td>";
					 td += "<td style='font-size:12px;text-align: center;'>/</td>";
					 td += "<td style='font-size:12px;text-align: center;'>/</td>";
					 td += "<td style='font-size:12px;text-align: center;'>/</td>";
					 td += "<td style='font-size:12px;text-align: center;'>/</td>";
					 td += "<td style='font-size:12px;text-align: center;'>/</td>";
	  		    	 $(p).attr("itemId",item.id);
	  		    	 $(p).append(td);
	  		   	 }
		   });
    		
	   	   reSetTableRowNum();//列表行号排序
	   	   layer.closeAll("loading");
      }
 	//查看
	function viewItem(itemId) {
		viewModInfo(itemId,"005",-1);
	} 
 	//列表行号排序
	function reSetTableRowNum() {
		var rowNum = 1;
		$("#dataTable tr[dataRow='1']").each(function(index) {
			if ($(this).css("display").indexOf("none") == -1) {
				$(this).find("td:eq(0)").text(rowNum);
				rowNum++;
			}
		});
	}
    </script>
</html>



