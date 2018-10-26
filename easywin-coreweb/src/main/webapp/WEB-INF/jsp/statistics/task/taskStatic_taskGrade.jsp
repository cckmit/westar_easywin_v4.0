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
<head>

</head>
<body>
	<div class="page-content">
		<div class="page-body">
			<div class="row">
				<div class="col-sm-12">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div id="formDataDiv">
								<input type="hidden" name="state" id="taskState">
								<div class="searchCond" style="display: block">
									<div class="btn-group pull-left">

										<div class="table-toolbar ps-margin" id="taskStateDiv">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="taskStateA"> 状态筛选 <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0);" taskStateA="" class="taskStateA">不限状态</a></li>
													<li><a href="javascript:void(0);" taskStateA="1" class="taskStateA">进行中</a></li>
													<li><a href="javascript:void(0);" taskStateA="3" class="taskStateA">已暂停</a></li>
													<li><a href="javascript:void(0);" taskStateA="4" class="taskStateA">已办结</a></li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown"> <c:choose>
														<c:when test="${not empty task.ownerName}">
															<font style="font-weight:bold;">${task.ownerName}</font>
														</c:when>
														<c:otherwise>责任人筛选</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)" onclick="userOneForUserIdCallBack('', 'owner','','')">不限条件</a></li>
													<li><a href="javascript:void(0)" onclick="userMoreForUserId('${param.sid}','owner');">人员选择</a></li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${task.listOwner }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
												</c:forEach>
											</select>
										</div>
										<div class="btn-group cond" id="moreCondition_Div">
											<div class="pull-left ps-margin ps-search">
												<span class="btn-xs">起止时间：</span> <input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="startDate" name="startDate" placeholder="开始时间"
													onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" /> <span>~</span> <input class="form-control dpd2 no-padding condDate" type="text"
													readonly="readonly" id="endDate" name="endDate" placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
											</div>
											<div class="pull-left ps-margin ps-search margin-left-10" style="text-align: center;">
												<button type="button" class="btn btn-primary btn-xs search">查询</button>
												<button type="button" class="btn btn-default btn-xs margin-left-10 reset" onclick="resetMoreCon('moreCondition_Div')">重置</button>
											</div>
										</div>
									</div>
									<div class="ps-margin ps-search hide">
										<span class="input-icon"> <input id="taskName" name="taskName" class="form-control ps-input" type="text" placeholder="请输入关键字"> <a href="javascript:void(0)"
											class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
										</span>
									</div>
								</div>
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs" type="button" id="changeView" viewState='A'>列表视图</button>
								<button class="btn btn-info  btn-xs" style="display:none" id ="excelExport" onclick="excelExport('紧急度统计表')" >导出excel</button>
								</div>
							</div>
							<div id="owner_show" class=" padding-top-10 text-left " style="display:${empty task.listOwner ? 'none':'block'}">
								<strong>责任人筛选:</strong>
								<c:forEach items="${task.listOwner }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
						</div>
						<div class="widget-body">
							<div class="widget-body" id="chartView" style="display: block">
								<div id="main" style="width:100%;height:500px"></div>
							</div>
							<div class="widget-body" id="tableView" style="display: none">
								<table class="table table-bordered" style="text-align: center;">
								</table>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main'));
	var url = '/statistics/task/listTaskStatisByType?sid=${param.sid}&statisticsType=1';

	//图例数对应显示人数
	var legendNumArray = {}
	legendNumArray[1] = 30;
	legendNumArray[2] = 15;
	legendNumArray[3] = 10;
	legendNumArray[4] = 7;

	//缓存数据用户穿透
	var caseData = {};
	//缓存用户名称与主键
	var caseOwnerData = [];
	//全局变量配置项
	var option = {};
	//异步取得数据
	getSelfJSON(url, null, function(data) {
		initChart(data);
	});
	//初始化表单数据
	function initChart(data) {
		caseOwnerData = [];
		caseData = {};
		option = {};
		initTable(data);
		//客户类型
		var legendArray = new Array();
		//类型统计数据
		var seriesArray = new Array();

		var totalArray = new Array();

		//纵坐标
		var yAxisArray = new Array();
		//后台取得的数据
		var listBusReport = data.listBusReport;
		var height = 0;
		//遍历后台数据结果
		$.each(listBusReport, function(indexType, obj) {
			//缓存客户类型与客户类型主键
			caseData[obj.name] = obj.type;
			//图例
			legendArray.push(obj.name);
			//结果集
			var listModStaticVos = obj.childModStaticVo;
			var dataArray = new Array();
			var total = 0;
			$.each(listModStaticVos, function(indexOwner, vo) {
				if (indexType == 0) {
					yAxisArray.push(vo.name);
					//缓存用户名称与主键
					caseOwnerData.push(vo);
				}
				total = total + vo.value;
				
				
				dataArray.push({'value':vo.value, 'ownerName':vo.name,'ownerId':vo.type,'grade':obj.type});
			})
			totalArray.push({
				'value' : total,
				'name' : obj.name + "(" + total + ")",
				'grade':obj.type
			})
			var seriesObj = {
				name : obj.name,
				type : 'bar',
				barWidth : 18,//固定柱子宽度
				barMinHeight : 1,
				label : {
					normal : {
						show : true,
						position : 'top'
					}
				},
				data : dataArray
			};
			seriesArray.push(seriesObj);
		});

		var series = {
			type : 'pie',
			barMinHeight : 1,
			center : [ '88%', '55%' ],
			radius : '28%',
			data : totalArray
		};
		seriesArray.push(series);
		//全局配置项
		option = {
			tooltip : {
			},
			color : [ '#d73d32', '#fb6e52', 'orange', '#53a93f' ],
			legend : {
				data : legendArray
			},
			dataZoom : {
				show : true
			},
			grid : {
				left : '0%',
				right : '25%',
				bottom : '15%',
				containLabel : true
			},
			xAxis : {
				type : 'category',
				axisLabel : {
					show : true,
					interval : 0,
					rotate : -20
				},
				data : yAxisArray
			},
			yAxis : {
				type : 'value'
			},
			series : seriesArray
		}
		//图例个数
		var legengLen = legendArray.length;
		//根据图例个数取得起始值
		var scopeValue = getDataZoomScope(legengLen);
		if (scopeValue) {
			option.dataZoom = {}
			option.dataZoom.show = true
			option.dataZoom.start = scopeValue.startValue
			option.dataZoom.end = scopeValue.endValue
		} else {
			option.dataZoom = {}
			option.dataZoom.show = false
		}
		//渲染页面
		myChart.setOption(option);
	}
	//图例事件触发
	myChart.on("legendselectchanged", function(params) {
		var selectLength = 0;
		$.each(params.selected, function(type, selected) {
			if (selected) {
				selectLength++;
			}
		})
		//重新取得显示范围
		var scopeValue = getDataZoomScope(selectLength);
		if (scopeValue) {
			option.dataZoom = {}
			option.dataZoom.show = true
			option.dataZoom.start = scopeValue.startValue
			option.dataZoom.end = scopeValue.endValue
		} else {
			option.dataZoom = {}
			option.dataZoom.show = false
		}
		//重新渲染图
		myChart.setOption(option);

	})

	//状态选择
	$(document).on("click","#taskStateDiv .taskStateA", function() {
		//责任人选择类型 0可以直接用,不用弹窗选择
		var state = $(this).attr("taskStateA");

		if (state) {
			var typeName = $(this).text();
			var html = '<font style="font-weight:bold;">' + typeName+ '</font>';
			html += '<i class="fa fa-angle-down"></i>';
			$("#taskStateA").html(html);
		} else {
			var html = '状态筛选';
			html += '<i class="fa fa-angle-down"></i>';
			$("#taskStateA").html(html);
		}

		$("#taskState").attr("value",state);
		//加载数据信息
		loadFormData();
	})

	//更多查询
	$(document).on("click", "#moreCondition_Div .search", function() {
		//加载数据信息
		loadFormData();
	});
	//更多查询 重置
	$(document).on("click", "#moreCondition_Div .reset", function() {
		$("#startDate").val("");
		$("#endDate").val("");
		//加载数据信息
		loadFormData();
	});
	//不限条件
	function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
		//启动加载页面效果
		$("#"+userIdtag+"_select").find("option").remove();
		$("#"+userIdtag+"_show").find("span").remove();
		$("#"+userIdtag+"_show").hide();
		//加载数据信息
		loadFormData();
	}
	//人员多选返回
	function userMoreForUserIdCallBack(options,userIdtag){
		$("#"+userIdtag).val('');
		var listOwner = returnList(userIdtag,options);
		
		//加载数据信息
		loadFormData();
	}
	
	//加载数据信息
	function loadFormData(){
		var params = {};
		//其他参数
		var inputs  = $("#formDataDiv").find("input");
		if(inputs && inputs.get(0)){
			$.each(inputs,function(index,input){
				var inputName = $(input).attr("name");
				var inputVal = $(input).val();
				if(inputVal){
					params[inputName]=inputVal
				}
			})
		}
		var selects = $("#formDataDiv").find("select");
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
		getSelfJSON(url, params, function(data) {
			initChart(data);
		});
	}
	//移除筛选
	function removeChoose(userIdtag,sid,userId,ts){
		$("#"+userIdtag+"_select").find("option[value='"+userId+"']").remove();
		$(ts).remove();
		var options = $("#"+userIdtag+"_select").find("option");
		var listOwner = returnList(userIdtag,options);
		
		//加载数据信息
		loadFormData();
	}
	//取得显示范围
	function getDataZoomScope(legengLen) {
		var dataZoomValue = {};
		//取得开头数值
		if (caseOwnerData.length == 0) {//没有人员,则不显示滚动条
			return null;
		}
		
		dataZoomValue['startValue'] = 0;
		//取得结尾数值 
		if (legengLen > 15) {
			dataZoomValue['endValue'] =(100/caseOwnerData.length );
		} else {//人员个数不超过15个
			//取得设定的人员个数
			var userNum = legendNumArray[legengLen];
			//人员总数
			var userLen = caseOwnerData.length;
			if (userNum > userLen) {//设定个数大于总数,全部显示
				dataZoomValue['endValue'] = 100
			} else {//人员总数超过设定总数,取设定数
				var value = ((userNum-1)*100/caseOwnerData.length );
				dataZoomValue['endValue'] = value
			}
		}
		return dataZoomValue;
	}

	//初始化表格视图
	function initTable(data) {
		var table = $("#tableView").find("table")
		$(table).html('');
		var thead = $("<thead></thead>");
		var headTr = $("<tr></tr>");
		var headFirstTh = $("<th style='text-align:center;font-size:13px;width:20;font-weight:bold'>人员姓名</th>");
		$(headTr).append(headFirstTh)
		var listBusReport = data.listBusReport;
		var tbody = $("<tbody></tbody>");
		$.each(listBusReport,function(index, obj) {
			var headTh = $("<th style='text-align:center;font-size:13px;width:20;font-weight:bold'>"+ obj.name + "</th>");
			$(headTr).append(headTh)
			var listModStaticVos = obj.childModStaticVo;
			$.each(listModStaticVos,function(indexA, vo) {
				var bodyFirstTr = $(tbody).find("tr[userId='"+ vo.type+ "']");
				var bodyFirstTd = $(tbody).find("td[userId='"+ vo.type+ "']");
				if (bodyFirstTr.length == 0) {
					bodyFirstTr = $("<tr userId='"+vo.type+"'></tr>");
					bodyFirstTd = $("<td  style='font-size:12px;' userId='"+vo.type+"'>"+ vo.name + "</td>");
					$(bodyFirstTr).append(bodyFirstTd);
					$(tbody).append(bodyFirstTr);
				}
				var bodyTd = $("<td style='font-size:12px;text-align:center;'>"+ vo.value + "</td>");
				if (vo.value) {
					bodyTd = $("<td style='font-size:12px;color:rgb(0,91,255);text-align:center;' ><a href='javascript:void(0)' typeId='"+ obj.type+ "' userId='"+ vo.type+ "' userName='"+vo.name+"'>"+ vo.value+ "</a></td>");
				}
				$(bodyTd).attr("cellNum", index);
				$(bodyFirstTr).append(bodyTd);
				$(tbody).append(bodyFirstTr);
			})
		});
		$(thead).append(headTr);

		$(table).append(thead)
		$(table).append(tbody)

		countTotal(table, tbody);
	}

	//点击事件，跳转到
	myChart.on("click",function(params) {
		var data = params.data;
		var grade;
		var ownerName;
		var ownerId;
		if (params.seriesType == "pie") {
			grade = data.grade;
		} else {
			grade = data.grade;
			ownerName = data.ownerName;
			ownerId = data.ownerId;
		}
		viewTaskAllPage(grade,ownerName,ownerId)
		

	})
	//表格穿透
	$(document).on("click","#tableView a",function() {
		var grade = $(this).attr("typeid");
		var ownerId = $(this).attr("userid");
		var ownerName = $(this).attr("userName");
		viewTaskAllPage(grade,ownerName,ownerId)
	});
	
	
	//切换视图
	$(document).on("click", "#changeView", function() {
		var viewState = $("#changeView").attr("viewState");
		if ("A" == viewState) {
			$("#changeView").attr("viewState", "B");
			$("#chartView").hide();
			$("#tableView").show();
			$("#changeView").html("图表视图");
			 $("#excelExport").show()
		} else if ("B" == viewState) {
			$("#changeView").attr("viewState", "A");
			$("#changeView").html("列表视图");
			$("#chartView").show();
			$("#tableView").hide();
			$("#excelExport").hide()
		}
	})
	//穿透查询
	function viewTaskAllPage(grade,ownerName,ownerId){
		var otherUrl = "/task/listTaskOfAllPage?sid=${param.sid}&pager.pageSize=10&activityMenu=task_m_1.5";
		//紧急程度
		if(grade){
			otherUrl = otherUrl+"&grade="+grade
		}
		//默认需要穿透负责人
		var ownerState = 1;
		if(ownerId && ownerName){//指定了负责人则不需要
			var select = $("#owner_select")
			var list =$(select).attr("list");
			var listkey =$(select).attr("listkey");
			var listvalue =$(select).attr("listvalue");
			otherUrl =otherUrl+"&"+list+"[0]."+listkey+"="+ownerId
			otherUrl =otherUrl+"&"+list+"[0]."+listvalue+"="+ownerName
			ownerState = 0;
		}
		//其他参数
		var inputs  = $("#formDataDiv").find("input");
		if(inputs && inputs.get(0)){
			$.each(inputs,function(index,input){
				var inputName = $(input).attr("name");
				var inputVal = $(input).val();
				if(inputVal){
					otherUrl = otherUrl+"&"+inputName+"="+inputVal
				}
			})
		}
		var selects = $("#formDataDiv").find("select");
		if(selects && selects.get(0)){
			$.each(selects,function(selectIndex,select){
				if($(select).attr("id")=="owner_select" && !ownerState){
					return true;
				}
				var list =$(select).attr("list");
				var listkey =$(select).attr("listkey");
				var listvalue =$(select).attr("listvalue");
				var options = $(select).find("option");
				if(options && options.get(0)){
					$.each(options,function(index,option){
						otherUrl =otherUrl+"&"+list+"["+index+"]."+listkey+"="+$(option).val()
						otherUrl =otherUrl+"&"+list+"["+index+"]."+listvalue+"="+$(option).html()
					})
				}
				
			})
		}
		window.self.location = otherUrl
	}

	//统计综合
	function countTotal(table, tbody) {
		//纵向统计综总和
		var totalX = $("<tr></tr>");
		var totalXFirstTd = $("<td style='text-align:center;font-size:13px;width:15;font-weight:bold'>总计</td>");
		totalX.append(totalXFirstTd);
		var colNum = $(table).find("thead").find("tr").find("th").length;
		for (var i = 0; i < colNum - 1; i++) {
			var aa = $(tbody).find("tr").find("td[cellNum='" + i + "']");
			var total = 0;
			$.each(aa, function(index, vo) {
				var num = $(vo).find("a").text();
				if (num) {
					total = total + parseInt(num);
				}
			})
			var totalTd = $("<td style='font-size:12px;text-align:center;'>" + total + "</td>");
			totalX.append(totalTd);
		}
		$(tbody).append(totalX);
		$(table).append(tbody);

		//横向统计总和
		var totalY = $("<th style='text-align:center;font-size:13px;width:15;font-weight:bold'>总计</th>");
		$(table).find("thead tr").append(totalY);
		var bodyTrs = $(tbody).find("tr");
		$.each(bodyTrs, function(trIndex, trObj) {
			var e = $(this);
			var total = 0;
			$.each($(e).find("td"), function(tdIndex, tdObj) {
				var num = $(tdObj).find("a").text();
				if (trIndex == bodyTrs.length - 1) {
					num = $(tdObj).text();
					if (num == '总计' || num == '--') {
						num = 0;
					}
				}
				if (num) {
					total = total + parseInt(num);
				}
			})
			var totalTd = $("<td style='font-size:12px;text-align:center;'>" + total + "</td>");
			$(e).append(totalTd)
		})
	}

	window.onresize = myChart.resize;
</script>
</html>



