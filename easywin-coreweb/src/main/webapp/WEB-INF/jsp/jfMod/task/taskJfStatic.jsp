<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page
	import="com.westar.core.web.InitServlet"%>
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
						<div
							class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div id="listTableDiv">
								<!-- <form id="searchForm" action="/statistics/item/listItemStatics" class="subform"> -->
								<input type="hidden" name="redirectPage" /> <input
									type="hidden" name="sid" value="${param.sid}" /> <input
									type="hidden" id="searchYear" name="searchYear"
									value="${jfScore.searchYear}">

								<div class="searchCond" style="display: block">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin margin-right-10">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown"> <c:choose>
														<c:when test="${not empty jfScore.searchYear}">
															<font style="font-weight: bold;" id="searchYearShow">
																${jfScore.searchYear}年</font>
														</c:when>
														<c:otherwise>年份筛选</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default" id="searchYearUl">
													<!--数据异步获得  #searchYearUl-->
												</ul>
											</div>
										</div>

										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs"
													onclick="displayMoreCond('moreCondition_Div')"> <span>更多</span>
													<i class="fa fa-angle-down"></i>
												</a>
												<div
													class="dropdown-menu dropdown-default padding-bottom-10 moreConsSelect"
													style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">起止时间：</span> <input
															class="form-control dpd2 no-padding condDate" type="text"
															readonly="readonly" value="${item.startDate}"
															id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span> <input
															class="form-control dpd2 no-padding condDate" type="text"
															readonly="readonly" value="${item.endDate}" id="endDate"
															name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
													<div class="ps-clear padding-top-10"
														style="text-align: center;">
														<button type="button"
															class="btn btn-primary btn-xs search">查询</button>
														<button type="button"
															class="btn btn-default btn-xs reset margin-left-10">重置</button>
													</div>
												</div>
											</div>
										</div>

									</div>
								</div>

								<!-- </form> -->
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info  btn-xs"
										onclick="excelTwoExport('下属任务积分统计表')">导出excel</button>
								</div>
							</div>

							<div id="detailTableDiv">
								<span class="widget-caption themeprimary pull-left margin-top-10 blue"
									id="pageTitle" style="font-size: 20px; cursor: pointer;"></span>
							</div>

						</div>
						
						<div class="widget-body " id="dataShow" style="display:none">
							<div class="widget-body " id="tableView" style="display: block">
								<table id="dataTable" class="table table-bordered"
									style="text-align: center;">
								</table>
								<div class="panel-body ps-page bg-white" style="font-size: 12px">
									<p class="pull-left ps-pageText">
										共<b class="badge" id="totalNum"></b>条记录
									</p>
									<ul class="pagination pull-right" id="pageDiv">
									</ul>
								</div>
							</div>
							<div class="widget-body" id="detailTableView"
								style="display: none">
								<table id="dataTable" class="table table-bordered"
									style="text-align: center;">
								</table>
							</div>
						</div>
					<div id="loading" class="layui-layer-load widget-body" style="width: 100%;height: 500px">
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
<script src="/static/ajaxPageJs/jquery.paginatio.js"
	type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	var url = '/jfMod/task/statisticsTaskJf'; //全局变量配置项
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
				$("#loading").remove();
				$("#dataShow").show();
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
		
		//设置年份选择和周数选择
		getSelfJSON('/weekReport/findOrgInfo',{"sid":sid},function(data){
			//团队注册时间年份
			var registYear = data.registYear;
			//当前时间年份
			var nowYear = data.nowYear;
			//添加年份选择
			for(var index = nowYear ; index >= registYear ; index--){
				var _li = $('<li></li>');
				var _a = $('<a href="javascript:void(0)" class="searchYear" relateElement="searchYear"></a>')
				$(_a).attr("dataValue",index);
				$(_a).html(index+'年');
				$(_li).append($(_a));
				$("#searchYearUl").append($(_li));
			}
		})
		getData(); 
		
	});
	//查询起始时间筛选
	$(document).on("click", ".searchYear",function() {
		$("#searchYear").val($(this).attr("dataValue"));
		$("#searchYearShow").html($(this).html())
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
	}); //重置查询时间
	$(document).on("click", "#moreCondition_Div .reset",function() {
		$("#startDate").val("");
		$("#endDate").val("");
		getData();
	});
	//初始化表格视图
	function initTable(data) {
		var table = $("#tableView").find("table");
		$(table).html('');
		var thead = $("<thead></thead>");
		var headTr = $("<tr></tr>");
		var headFirstTh = "<th style='text-align: center;font-weight:bold;'>序号</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>部门名</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>人员名称</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>总任务数</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>已评任务数</th>";
		headFirstTh += "<th style='text-align: center;font-weight:bold;'>总得分</th>";
		$(headTr).append(headFirstTh);
		$(thead).append(headTr);
		$(table).append(thead);
		var tbody = $("<tbody></tbody>");
		$(table).append(tbody);
		
		if(data){
			 var preDepId=0;
			 var depIndex = 1;
			$.each(data,function(index,taskJf){
				 var _tr = $("<tr></tr>");
				 $(_tr).attr("depId",taskJf.depId);
				 //序号
				 var _td1 = $("<td ' isParent='1' style='font-size:12px;text-align: center;'></td>");
				 
				 $(_td1).attr("depId",taskJf.depId);
				 //部门名
				 var _td2 = $("<td ' isParent='1' style='font-size:12px;text-align: center;'></td>");
				 $(_td2).html(taskJf.depName);
				 $(_td2).attr("depId",taskJf.depId);
				 //人员名称
				 var _td3 = $("<td ' isParent='1' style='font-size:12px;text-align: center;'></td>");
				 $(_td3).html(taskJf.userName);
				//总任务数
				 var _td4 = $("<td '  style='font-size:12px;text-align: center;color:green;'></td>");
				if(taskJf.taskTotal){
					 $(_td4).html(taskJf.taskTotal);
				}else{
					 $(_td4).html(0);
				}
				
				 $(_td4).attr("taskTotal",taskJf.depId);
				//已评任务数
				 var _td5 = $("<td style='font-size:12px;text-align: center;color:green;'></td>");
				 if(taskJf.jfTaskNum){
					 $(_td5).html(taskJf.jfTaskNum);
				}else{
					 $(_td5).html(0);
				}
				 
				 $(_td5).attr("jfTaskNum",taskJf.depId);
				//总得分
				 var _td6 = $("<td style='font-size:12px;text-align: center;color:green;'></td>");
				 if(taskJf.scoreTotal){
					 $(_td6).html(taskJf.scoreTotal);
					}else{
						 $(_td6).html(0);
					}
				
				 $(_td6).attr("scoreTotal",taskJf.depId);
				 
				//判断是否是同一项目
			 	if(taskJf.depId == preDepId){
			 		 var firstTr = $(tbody).find("tr[depId='"+taskJf.depId+"']:eq(0)");
				     var num =  $(tbody).find("tr[depId='"+taskJf.depId+"']").length +1;
				 	 $(firstTr).find("td:eq(0)").attr("rowspan",num);//行号
				 	 $(firstTr).find("td:eq(1)").attr("rowspan",num);//部门名
				 	 $(_tr).append(_td3);
					 $(_tr).append(_td4);
					 $(_tr).append(_td5);
					 $(_tr).append(_td6);
			 	}else{
			 		 var preFirstTr = $(tbody).find("tr[depId='"+preDepId+"']:eq(0)");
			 		 var userName = $(preFirstTr).find("td:eq(2)").html();
			 		//添加总计一行
			 		if(userName && index != 0 && userName.length>0){
			 			 var _trTotal = $("<tr></tr>");
						 $(_trTotal).attr("depId",preDepId);
			 			
					     var num =  $(tbody).find("tr[depId='"+preDepId+"']").length +1;
					 	 $(preFirstTr).find("td:eq(0)").attr("rowspan",num);//行号
					 	 $(preFirstTr).find("td:eq(1)").attr("rowspan",num);//项目名称
					 	 
					 	 var taskTd = $(tbody).find("td[taskTotal='"+preDepId+"']");
				 		 var jfTaskTd = $(tbody).find("td[jfTaskNum='"+preDepId+"']");
					 	 var scoreTd = $(tbody).find("td[scoreTotal='"+preDepId+"']");
					 	 
					 	 var  taskTotal= 0;
					 	 var  jfTaskTotal= 0;
					 	 var  scoreTotal= 0;
					 	
					  	 for(var i =0;i<taskTd.length;i++){
					  		taskTotal += parseFloat(taskTd.eq(i).html());
					  		jfTaskTotal += parseFloat(jfTaskTd.eq(i).html());
					  		scoreTotal += parseFloat(scoreTd.eq(i).html());
					  	}  
					 	
					 	var td1 = $("<td style='font-size:12px;text-align: center;color:red;'>合计</td>");
						var td2 = $("<td style='font-size:12px;text-align: center;color:red;'>" + taskTotal + "</td>");
						var td3 = $("<td style='font-size:12px;text-align: center;color:red;'>" + jfTaskTotal + "</td>");
						var td4 = $("<td style='font-size:12px;text-align: center;color:red;'>" + scoreTotal + "</td>");
					 	 
					 	 $(_trTotal).append(td1);
						 $(_trTotal).append(td2);
						 $(_trTotal).append(td3);
						 $(_trTotal).append(td4);
						 $(tbody).append(_trTotal);
			 		}
			 		
		 			$(_td1).html(depIndex);
			 		 $(_tr).append(_td1);
					 $(_tr).append(_td2);
					 $(_tr).append(_td3);
					 $(_tr).append(_td4);
					 $(_tr).append(_td5);
					 $(_tr).append(_td6);
					 depIndex += 1;
			 	}
				 $(tbody).append(_tr);
				 
				 //最后一行加总计
				 if(data.length-1 == index ){
				 		var lastTr = $(tbody).find("tr[depId='"+taskJf.depId+"']:eq(0)");
				 		var _trTotal = $("<tr></tr>");
						 $(_trTotal).attr("depId",taskJf.depId);
			 			
					     var num =  $(tbody).find("tr[depId='"+taskJf.depId+"']").length +1;
					 	 $(lastTr).find("td:eq(0)").attr("rowspan",num);//行号
					 	 $(lastTr).find("td:eq(1)").attr("rowspan",num);//项目名称
					 	 
					 	 var taskTd = $(tbody).find("td[taskTotal='"+preDepId+"']");
				 		 var jfTaskTd = $(tbody).find("td[jfTaskNum='"+preDepId+"']");
					 	 var scoreTd = $(tbody).find("td[scoreTotal='"+preDepId+"']");
					 	 
					 	 var  taskTotal= 0;
					 	 var  jfTaskTotal= 0;
					 	 var  scoreTotal= 0;
					 	
					  	 for(var i =0;i<taskTd.length;i++){
					  		taskTotal += parseFloat(taskTd.eq(i).html());
					  		jfTaskTotal += parseFloat(jfTaskTd.eq(i).html());
					  		scoreTotal += parseFloat(scoreTd.eq(i).html());
					  	}  
					 	
					  	var td1 = $("<td style='font-size:12px;text-align: center;color:red;'>合计</td>");
						var td2 = $("<td style='font-size:12px;text-align: center;color:red;'>" + taskTotal + "</td>");
						var td3 = $("<td style='font-size:12px;text-align: center;color:red;'>" + jfTaskTotal + "</td>");
						var td4 = $("<td style='font-size:12px;text-align: center;color:red;'>" + scoreTotal + "</td>");
					 	 
					 	 $(_trTotal).append(td1);
						 $(_trTotal).append(td2);
						 $(_trTotal).append(td3);
						 $(_trTotal).append(td4);
						 $(tbody).append(_trTotal);
			 	}
				 preDepId = taskJf.depId;
			});
		}
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
 	
	function excelTwoExport(fileName){
		//询问框
		window.top.layer.confirm('导出数据至EXCEL？', {
		  btn: ['本页','全部'] //按钮
		}, function(index){
			var html = $("#tableView").html();
			tableToXls(html,fileName,sid);
			window.top.layer.close(index);
		}, function(index){
			 var url = '/jfMod/exportTaskJfStatistics?sid='+sid;
			 url += '&searchYear='+$("#searchYear").val();
			 url += '&startDate='+$("#startDate").val();
			 url += '&endDate='+$("#endDate").val();
			 url += '&fileName='+fileName;
			 
			 window.open(url,"_blank");
				window.top.layer.close(index);

		});
	}
    </script>
</html>



