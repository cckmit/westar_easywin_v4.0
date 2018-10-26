n<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
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
							<div>
								<!--客户类型选择 -->
								<input type="hidden" name="customerTypeId" id="customerTypeId">
								<!--客户责任人-->
								<input type="hidden" name="owner" id="owner"> <input type="hidden" name="ownerName" id="ownerName">
								<!--客户区域-->
								<input type="hidden" name="areaIdAndType" id="areaIdAndType">

								<div class="searchCond" style="display: block">
									<div class="btn-group pull-left">

										<div class="table-toolbar ps-margin" id="areaIdAndTypeDiv">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="areaIdAndTypeA"> 区域筛选 <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0);" areaIdAndTypeA="0" class="areaIdAndTypeA">不限区域</a></li>
													<li><a href="javascript:void(0);" areaIdAndTypeA="1" class="areaIdAndTypeA">区域选择</a></li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin" id="ownerDiv">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="ownerNameA"> 责任人筛选 <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0);" ownerType="0" ownerId="0" class="selectOwner">不限条件</a></li>
													<li><a href="javascript:void(0);" ownerType="1" onclick="userMoreForUserId('${param.sid}','owner');">人员选择</a></li>
													<%-- <c:choose>
																	<c:when test="${not empty listOwners}">
																		<hr style="margin: 8px 0px" />
																		<c:forEach items="${listOwners}" var="owner" varStatus="vs">
																			<li><a href="javascript:void(0);" ownerType="0" ownerId="${owner.id}" class="selectOwner">${owner.userName}</a></li>
																		</c:forEach>
																	</c:when>
																</c:choose> --%>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
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
										<span class="input-icon"> <input id="customerName" name="customerName" class="form-control ps-input" type="text" placeholder="请输入关键字"> <a href="javascript:void(0)"
											class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
										</span>
									</div>
								</div>
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs" type="button" id="changeView" viewState='A'>列表视图</button>
								<button class="btn btn-info  btn-xs" style="display:none" id ="excelExport" onclick="excelExport('归属统计表')" >导出excel</button>
								</div>
							</div>
							<div id="listOwner_show" class="padding-top-10 text-left " style="display:${empty customer.listOwner ? 'none':'block'}">
								<strong>责任人筛选:</strong>
								<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
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
	//基础链接
	var url = '/statistics/crm/listCrmStatisByType?sid=${param.sid}&statisticsType=4';
	//图例数对应显示人数
	var legendNumArray = {}
	legendNumArray[1] = 30;
	legendNumArray[2] = 15;
	legendNumArray[3] = 10;
	legendNumArray[4] = 7;
	legendNumArray[5] = 6;
	legendNumArray[6] = 5;
	legendNumArray[7] = 4;
	legendNumArray[8] = 3;
	legendNumArray[9] = 3;
	legendNumArray[10] = 3;
	legendNumArray[11] = 2;
	legendNumArray[12] = 2;
	legendNumArray[13] = 2;
	legendNumArray[14] = 2;
	legendNumArray[15] = 2;

	//缓存数据用户穿透
	var caseData = {};
	//缓存用户名称与主键
	var caseOwnerData = [];
	//全局变量配置项
	var option = {};

	//客户类型选择
	$(document).on("click","#customerTypeIdDiv .crmTypeId",function() {
		$("#customerTypeId").val($(this).attr("crmtypeid"));

		var customerTypeId = $("#customerTypeId").val();
		var owner = $("#owner").val();
		var areaIdAndType = $("#areaIdAndType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var customerName = $("#customerName").val();

		if (customerTypeId > 0) {
			var typeName = $(this).text();
			var html = '<font style="font-weight:bold;">' + typeName
					+ '</font>';
			html += '<i class="fa fa-angle-down"></i>';
			$("#crmTypeNameA").html(html);
		} else {
			var html = '客户类型';
			html += '<i class="fa fa-angle-down"></i>';
			$("#crmTypeNameA").html(html);
		}
		var params = {
			"customerTypeId" : customerTypeId,
			"owner" : owner,
			"areaIdAndType" : areaIdAndType,
			"startDate" : startDate,
			"endDate" : endDate,
			"customerName" : customerName
		}
		getSelfJSON(url, params, function(data) {
			initChart(data);
		});

	});
	//页面触发事件
	$(function() {
		//客户名筛选
		$("#customerName").off("blur").on("blur", function() {
			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();

			var params = {
				"customerTypeId" : customerTypeId,
				"owner" : owner,
				"areaIdAndType" : areaIdAndType,
				"startDate" : startDate,
				"endDate" : endDate,
				"customerName" : customerName
			}
			getSelfJSON(url, params, function(data) {
				initChart(data);
			});
		});
		//文本框绑定回车提交事件
		$("#customerName").unbind("keydown").bind("keydown", function(event) {
			if (event.keyCode == "13") {
				var customerTypeId = $("#customerTypeId").val();
				var owner = $("#owner").val();
				var areaIdAndType = $("#areaIdAndType").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var customerName = $("#customerName").val();

				var params = {
					"customerTypeId" : customerTypeId,
					"owner" : owner,
					"areaIdAndType" : areaIdAndType,
					"startDate" : startDate,
					"endDate" : endDate,
					"customerName" : customerName
				}
				getSelfJSON(url, params, function(data) {
					initChart(data);
				});
			}
		});
	})

	//查询时间区间
	$(document).on("click", "#moreCondition_Div .search", function() {
		var options = $("#owner_select").find("option");
		var listOwner = returnListOwner(options);
		var customerTypeId = $("#customerTypeId").val();
		var owner = $("#owner").val();
		var areaIdAndType = $("#areaIdAndType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var customerName = $("#customerName").val();
		var listOwnerStr = '';
		if (listOwner.length > 0) {
			listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
		}
		var params = {
			"customerTypeId" : customerTypeId,
			"owner" : owner,
			"areaIdAndType" : areaIdAndType,
			"startDate" : startDate,
			"endDate" : endDate,
			"customerName" : customerName,
			"listOwnerStr" : listOwnerStr
		}
		getSelfJSON(url, params, function(data) {
			initChart(data);
		});
	});
	//查询时间区间
	$(document).on("click", "#moreCondition_Div .reset", function() {
		$("#startDate").val("");
		$("#endDate").val("");
		var options = $("#owner_select").find("option");
		var listOwner = returnListOwner(options);
		var customerTypeId = $("#customerTypeId").val();
		var owner = $("#owner").val();
		var areaIdAndType = $("#areaIdAndType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var customerName = $("#customerName").val();
		var listOwnerStr = '';
		if (listOwner.length > 0) {
			listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
		}
		var params = {
			"customerTypeId" : customerTypeId,
			"owner" : owner,
			"areaIdAndType" : areaIdAndType,
			"startDate" : startDate,
			"endDate" : endDate,
			"customerName" : customerName,
			"listOwnerStr" : listOwnerStr
		}
		getSelfJSON(url, params, function(data) {
			initChart(data);
		});
	});
	//责任人选择
	$(document).on( "click","#ownerDiv .selectOwner",function() {
		//责任人选择类型 0可以直接用,不用弹窗选择
		var ownerType = $(this).attr("ownerType");
		//责任人主键
		var ownerId = $(this).attr("ownerId");
		var ownerName = $(this).text();
		if (ownerType == 1) {//需要弹窗选择
			var owner = $("#owner").val();
			var ownerName = $("#ownerName").val();
			if (owner && ownerName) {//有选择人员
				userOneForUserId(owner, ownerName, '', '${param.sid}',
						'owner', '');
			} else {//默认当前人员
				userOneForUserId('${userInfo.id}',
						'${userInfo.userName}', '', '${param.sid}',
						'owner', '')
			}
		} else {
			userOneForUserIdCallBack(ownerId, "", ownerName, "");
		}
	})
	//客户区域选择
	$(document).on("click","#areaIdAndTypeDiv .areaIdAndTypeA",function() {
		var areaIdAndTypeT = $(this).attr("areaIdAndTypeA");
		var options = $("#owner_select").find("option");
		var listOwner = returnListOwner(options);
		var listOwnerStr = '';
		if (listOwner.length > 0) {
			listOwnerStr = '{"listOwner":'+ JSON.stringify(listOwner) + '}'
		}
		if (areaIdAndTypeT == 1) {//点击选择弹窗数据
			areaOne('areaIdAndType','areaName','${param.sid}','1',function(result) {
				var areaIdAndType = $("#areaIdAndType").val(result.idAndType);

				var customerTypeId = $("#customerTypeId").val();
				var owner = $("#owner").val();
				var areaIdAndType = $("#areaIdAndType").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var customerName = $("#customerName").val();

				var html = '<font style="font-weight:bold;">'+ result.areaName + '</font>';
				html += '<i class="fa fa-angle-down"></i>';
				$("#areaIdAndTypeA").html(html);
				var params = {
					"customerTypeId" : customerTypeId,
					"owner" : owner,
					"areaIdAndType" : areaIdAndType,
					"startDate" : startDate,
					"endDate" : endDate,
					"customerName" : customerName,
					"listOwnerStr" : listOwnerStr
				}
				getSelfJSON(url, params,function(data) {
							initChart(data);
				});
			})
		} else {//所有数据
			var areaIdAndType = $("#areaIdAndType").val('');

			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();

			var html = '区域筛选';
			html += '<i class="fa fa-angle-down"></i>';
			$("#areaIdAndTypeA").html(html);
			var params = {
				"customerTypeId" : customerTypeId,
				"owner" : owner,
				"areaIdAndType" : areaIdAndType,
				"startDate" : startDate,
				"endDate" : endDate,
				"customerName" : customerName,
				"listOwnerStr" : listOwnerStr
			}
			getSelfJSON(url, params, function(data) {
				initChart(data);
			});
		}
	});
	//人员筛选
	function userMoreForUserIdCallBack(options, userIdtag) {
		$("#" + userIdtag).val('');
		var listOwner = returnListOwner(options);

		var customerTypeId = $("#customerTypeId").val();
		var owner = $("#owner").val();
		var areaIdAndType = $("#areaIdAndType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var customerName = $("#customerName").val();
		var listOwnerStr = '';
		if (listOwner.length > 0) {
			listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
		}
		var params = {
			"customerTypeId" : customerTypeId,
			"owner" : owner,
			"areaIdAndType" : areaIdAndType,
			"startDate" : startDate,
			"endDate" : endDate,
			"customerName" : customerName,
			"listOwnerStr" : listOwnerStr
		}
		getSelfJSON(url, params, function(data) {
			initChart(data);
		});

	}

	//移除筛选
	function removeChoose(userIdtag, sid, userId, ts) {
		$("#" + userIdtag + "_select").find("option[value='" + userId + "']")
				.remove();
		$("#" + userIdtag).val('');
		$(ts).remove();
		var options = $("#" + userIdtag + "_select").find("option");
		var listOwner = returnListOwner(options);

		var customerTypeId = $("#customerTypeId").val();
		var owner = $("#owner").val();
		var areaIdAndType = $("#areaIdAndType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var customerName = $("#customerName").val();
		var listOwnerStr = '';
		if (listOwner.length > 0) {
			listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
		}
		var params = {
			"customerTypeId" : customerTypeId,
			"owner" : owner,
			"areaIdAndType" : areaIdAndType,
			"startDate" : startDate,
			"endDate" : endDate,
			"customerName" : customerName,
			"listOwnerStr" : listOwnerStr
		}
		getSelfJSON(url, params, function(data) {
			initChart(data);
		});
	}

	//用户返回值用于查询
	function userOneForUserIdCallBack(userId, userIdtag, userName, userNametag) {
		$("#listOwner_show").find('span').remove();
		$("#" + userIdtag + "_select").find("option").remove();
		$("#listOwner_show").hide();
		if (userId && userId > 0) {//有负责人信息
			$("#owner").val(userId);
			$("#ownerName").val(userName);

			var html = '<font style="font-weight:bold;">' + userName
					+ '</font>';
			html += '<i class="fa fa-angle-down"></i>';
			$("#ownerNameA").html(html);
		} else {//不限人员
			$("#owner").val('');
			$("#ownerName").val('');

			var html = '责任人筛选';
			html += '<i class="fa fa-angle-down"></i>';
			$("#ownerNameA").html(html);
		}

		var customerTypeId = $("#customerTypeId").val();
		var owner = $("#owner").val();
		var areaIdAndType = $("#areaIdAndType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var customerName = $("#customerName").val();

		var params = {
			"customerTypeId" : customerTypeId,
			"owner" : owner,
			"areaIdAndType" : areaIdAndType,
			"startDate" : startDate,
			"endDate" : endDate,
			"customerName" : customerName
		}
		getSelfJSON(url, params, function(data) {
			initChart(data);
		});
	}

	//异步取得数据
	getSelfJSON(url, null, function(data) {
		initChart(data);
	});
	window.onresize = myChart.resize;

	//初始化表单数据
	function initChart(data) {
		//初始化表格视图
		initTable(data);
		//客户类型
		var legendArray = new Array();
		//类型统计数据
		var seriesArray = new Array();

		//纵坐标
		var yAxisArray = new Array();
		//后台取得的数据
		var listBusReport = data.listBusReport;
		var height = 0;
		//遍历后台数据结果
		$.each(listBusReport, function(indexType, obj) {
			//缓存客户类型与客户类型主键
			caseData[obj.typeName] = obj.id;
			//图例
			legendArray.push(obj.typeName);
			//结果集
			var listModStaticVos = obj.listModStaticVos;
			var dataArray = new Array();
			$.each(listModStaticVos, function(indexOwner, vo) {
				if (indexType == 0) {//
					yAxisArray.push(vo.name);
					//缓存用户名称与主键
					caseOwnerData.push(vo);
				}
				dataArray.push(vo.value);
			})
			var series = {
				name : obj.typeName,
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
			seriesArray.push(series);
		});
		//全局配置项
		option = {
			tooltip : {
				trigger : 'item',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend : {
				data : legendArray
			},
			dataZoom : {
				show : true
			//startValue :'陈焱春',
			//endValue:'11区钟燕如'
			},
			grid : {
				left : '5%',
				right : '10%',
				bottom : '10%',
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
			option.dataZoom.start= scopeValue.startValue
			option.dataZoom.end = scopeValue.endValue
		} else {
			option.dataZoom = {}
			option.dataZoom.show = false
		}
		//渲染页面
		myChart.setOption(option);
	}
	//点击事件，跳转到
	myChart.on("click", function(params) {
		var customerTypeId = caseData[params.seriesName];
		var owner = caseOwnerData[params.dataIndex].type;
		var areaIdAndType = $("#areaIdAndType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var customerName = $("#customerName").val();

		var options = $("#owner_select").find("option");
		var listOwner = returnListOwner(options);
		var listOwnerStr = '';
		if (listOwner.length > 0) {
			listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
		}

		var otherUrl = "/crm/customerListPage?sid=${param.sid}&searchTab=15";
		otherUrl += "&customerTypeId=" + customerTypeId
		otherUrl += "&owner=" + owner
		otherUrl += "&areaIdAndType=" + areaIdAndType
		otherUrl += "&startDate=" + startDate
		otherUrl += "&endDate=" + endDate
		otherUrl += "&customerName=" + customerName
		otherUrl += "&listOwnerStr=" + listOwnerStr
		window.self.location = otherUrl

	})
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
	//取得显示范围
	function getDataZoomScope(legengLen) {
		var dataZoomValue = {};
		//取得开头数值
		if (caseOwnerData.length == 0) {//没有人员,则不显示滚动条
			return null;
		}
		
		dataZoomValue['startValue'] = 0
		
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
		var headFirstTh = $("<th style='text-align: center;font-size:13px;width:15;font-weight:bold;'>名称</th>");
		$(headTr).append(headFirstTh)
		var listBusReport = data.listBusReport;
		var month = data.nowMonth;
		var tbody = $("<tbody></tbody>");
		$.each(listBusReport,function(index, obj) {
		var headTh = $("<th style='text-align:center;font-size:13px;width:20;font-weight:bold;'>"+ obj.typeName + "</th>");
		$(headTr).append(headTh)
		var listModStaticVos = obj.listModStaticVos;
		$.each(listModStaticVos,function(indexA, vo) {
				var bodyFirstTr = $(tbody).find("tr[userId='"+ vo.type+ "']");
				var bodyFirstTd = $(tbody).find("td[userId='"+ vo.type+ "']");
				if (bodyFirstTr.length == 0) {
					bodyFirstTr = $("<tr  userId='"+vo.type+"'></tr>");
					bodyFirstTd = $("<td  style='font-size:12px;' userId='"+vo.type+"'>"+ vo.name + "</td>");
					$(bodyFirstTr).append(bodyFirstTd);
					$(tbody).append(bodyFirstTr);
				}
				var bodyTd = $("<td style='font-size:12px;text-align:center;'>"+ vo.value + "</td>");
				if (vo.value) {
					bodyTd = $("<td style='font-size:12px;color:rgb(0,91,255);text-align:center;'><a href='javascript:void(0)' typeId='"+ obj.id+ "' userId='"+ vo.type+ "'>"+ vo.value+ "</a></td>");
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
	//表格穿透
	$("#tableView").on("click", "a", function() {
		var customerTypeId = $(this).attr("typeId");
		var owner = $(this).attr("userId");
		var areaIdAndType = $("#areaIdAndType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var customerName = $("#customerName").val();
		var options = $("#owner_select").find("option");
		var listOwner = returnListOwner(options);
		var listOwnerStr = '';
		if (listOwner.length > 0) {
			listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
		}

		var otherUrl = "/crm/customerListPage?sid=${param.sid}&searchTab=15";
		otherUrl += "&customerTypeId=" + customerTypeId
		otherUrl += "&owner=" + owner
		otherUrl += "&areaIdAndType=" + areaIdAndType
		otherUrl += "&startDate=" + startDate
		otherUrl += "&endDate=" + endDate
		otherUrl += "&customerName=" + customerName
		otherUrl += "&listOwnerStr=" + listOwnerStr
		window.self.location = otherUrl
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
	//统计综合
	function countTotal(table, tbody) {
		//纵向统计综总和
		var totalX = $("<tr></tr>");
		var totalXFirstTd = $("<td style='font-size:13px;'>总计</td>");
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
		var totalY = $("<th style='text-align: center;font-size:13px;width:10;'>总计</th>");
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
		});
	}
</script>
</html>

