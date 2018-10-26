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
	<!-- Page Content -->
            <div class="page-content">
                <!-- Page Body -->
                <div class="page-body">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="widget">
                                <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                                <div>
										<!--客户类型选择 -->
										<input type="hidden" name="customerTypeId" id="customerTypeId" >
										<!--客户责任人-->
										<input type="hidden" name="owner" id="owner">
										<input type="hidden" name="ownerName" id="ownerName">
										<!--客户区域-->
										<input type="hidden" name="areaIdAndType" id="areaIdAndType">
										
										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														客户类型筛选
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li>
															<a href="javascript:void(0);" onclick="customerTypeFilter()">不限条件</a>
														</li>
														<li>
															<a href="javascript:void(0);" onclick="crmTypeMoreTree('${param.sid}','crmType');">类型选择</a>
														</li>
													</ul>
												</div>
											</div>
											<div style="float: left;width: 250px;display: none">
												<select list="listCrmType" listkey="id" listvalue="typeName" id="crmType_select" name="listCrmType.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													<c:forEach items="${customer.listCrmType }" var="obj" varStatus="vs">
														<option selected="selected" value="${obj.id }">${obj.typeName }</option>
													</c:forEach>
												</select>
											</div>
				                                    <div class="table-toolbar ps-margin" id="areaIdAndTypeDiv">
				                                        <div class="btn-group">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="areaIdAndTypeA">
				                                            	区域筛选
				                                            	<i class="fa fa-angle-down"></i>
				                                            </a>
				                                            <ul class="dropdown-menu dropdown-default">
				                                                <li><a href="javascript:void(0);"  areaIdAndTypeA="0" class="areaIdAndTypeA">不限区域</a></li>
																<li><a href="javascript:void(0);"  areaIdAndTypeA="1" class="areaIdAndTypeA">区域选择</a></li>
				                                            </ul>
				                                        </div>
				                                    </div>
				                                    <div class="table-toolbar ps-margin" id="ownerDiv">
				                                        <div class="btn-group">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="ownerNameA">
				                                            	责任人筛选
				                                            	<i class="fa fa-angle-down"></i>
				                                            </a>
				                                            <ul class="dropdown-menu dropdown-default">
				                                                <li><a href="javascript:void(0);" onclick="userOneForUserIdCallBack()">不限条件</a></li>
																<li><a href="javascript:void(0);" onclick="userMoreForUserId('${param.sid}','owner');">人员选择</a></li>
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
                           							<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple"
														moreselect="true" style="width: 100%; height: 100px;">
													<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
													</c:forEach>
													</select>
													</div>
				                                    <div class="btn-group cond" id="moreCondition_Div">
			                                        	<div class="pull-left ps-margin ps-search">
															<span class="btn-xs">起止时间：</span>
				                                        	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="startDate" name="startDate" 
																placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
																<span>~</span>
																<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="endDate"  name="endDate"
																placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
				                                    	</div>
				                                    	<div class="pull-left ps-margin ps-search margin-left-10" style="text-align: center;">
				                                    		<button type="button" class="btn btn-primary btn-xs search">查询</button>
				                                    		<button type="button" class="btn btn-default btn-xs margin-left-10 reset" onclick="resetMoreCon('moreCondition_Div')">重置</button>
				                                    	</div>
			                                        </div>
												</div>
		                                    	<div class="ps-margin ps-search hide">
													<span class="input-icon">
														<input id="customerName" name="customerName" class="form-control ps-input" 
														type="text" placeholder="请输入关键字" >
														<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
													</span>
												</div>
											</div>
											<div class="widget-buttons ps-widget-buttons">
												<button class="btn btn-info btn-primary btn-xs" type="button" id="changeView" viewState='A'>列表视图</button>
												<button class="btn btn-info  btn-xs" style="display:none" id ="excelExport" onclick="excelExport('更新统计表')" >导出excel</button>
											</div>
											</div>
												<div id="listOwner_show" class="padding-top-10 text-left " style="display:${empty customer.listOwner ? 'none':'block'}">
											<strong >责任人筛选:</strong>
											<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
												 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')"
												 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
											</c:forEach>
										</div>
											<div id="listCrmType_show" class="padding-top-10 text-left " style="display:${empty customer.listCrmType ? 'none':'block'}">
											<strong>客户类型筛选:</strong>
											<c:forEach items="${customer.listCrmType }" var="obj" varStatus="vs">
												<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('crmType','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.typeName }</span>
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
                <!-- /Page Body -->
            	</div>
            <!-- /Page Content -->
            
        </div>
        <!-- /Page Container -->
	</body>
	<script type="text/javascript">
		 // 基于准备好的dom，初始化echarts实例
	    var myChart = echarts.init(document.getElementById('main'));
	    var url = '/statistics/crm/listCrmStatisByType?sid=${param.sid}&statisticsType=2';
	    
	    //缓存数据用户穿透
	    var caseData = {};
	    var caseFrequeData = {};
	    
	   
		//查询时间区间
		$(document).on("click","#moreCondition_Div .search",function(){
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr = '';
			if(listOwner.length > 0) {
				listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
			}
			var crmTypeOptions = $("#crmType_select").find("option");
			var listCrmType = returnListCrmType(crmTypeOptions);
			var listCrmTypeStr = '';
			if(listCrmType.length > 0) {
				listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
			}

			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();

			var params = {
				"areaIdAndType": areaIdAndType,
				"startDate": startDate,
				"endDate": endDate,
				"listOwnerStr": listOwnerStr,
				"listCrmTypeStr":listCrmTypeStr
			}
			getSelfJSON(url, params, function(data) {
				initChart(data);
			});
		});
		//查询时间区间
		$(document).on("click","#moreCondition_Div .reset",function(){
			$("#startDate").val("");
			$("#endDate").val("");
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr = '';
			if(listOwner.length > 0) {
				listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
			}
			var crmTypeOptions = $("#crmType_select").find("option");
			var listCrmType = returnListCrmType(crmTypeOptions);
			var listCrmTypeStr = '';
			if(listCrmType.length > 0) {
				listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
			}

			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();

			var params = {
				"areaIdAndType": areaIdAndType,
				"startDate": startDate,
				"endDate": endDate,
				"listOwnerStr": listOwnerStr,
				"listCrmTypeStr":listCrmTypeStr
			}
			getSelfJSON(url, params, function(data) {
				initChart(data);
			});
		});
		//客户区域选择
		$(document).on("click","#areaIdAndTypeDiv .areaIdAndTypeA",function(){
			var areaIdAndTypeT = $(this).attr("areaIdAndTypeA");
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			var crmTypeOptions = $("#crmType_select").find("option");
			var listCrmType = returnListCrmType(crmTypeOptions);
			var listCrmTypeStr = '';
			if(listCrmType.length > 0) {
				listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
			}
			if(areaIdAndTypeT==1){//点击选择弹窗数据
				areaOne('areaIdAndType','areaName','${param.sid}','1',function(result){
					var areaIdAndType = $("#areaIdAndType").val(result.idAndType);
					
					var areaIdAndType = $("#areaIdAndType").val();
					var startDate = $("#startDate").val();
					var endDate = $("#endDate").val();
					
					var html = '<font style="font-weight:bold;">'+result.areaName+'</font>';
					html +='<i class="fa fa-angle-down"></i>';
					$("#areaIdAndTypeA").html(html);
					var params = {
							"areaIdAndType":areaIdAndType,
							"startDate":startDate,
							"endDate":endDate,
							"listOwnerStr":listOwnerStr,
							"listCrmTypeStr":listCrmTypeStr
					}
					getSelfJSON(url,params,function (data) {
			    	   initChart(data);
			    	});
				})
			}else{//所有数据
				var areaIdAndType = $("#areaIdAndType").val('');
				
				var areaIdAndType = $("#areaIdAndType").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				
				var html = '区域筛选';
				html +='<i class="fa fa-angle-down"></i>';
				$("#areaIdAndTypeA").html(html);
				var params = {
						"areaIdAndType":areaIdAndType,
						"startDate":startDate,
						"endDate":endDate,
						"listOwnerStr":listOwnerStr,
						"listCrmTypeStr":listCrmTypeStr
				}
				getSelfJSON(url,params,function (data) {
		    	   initChart(data);
		    	});
			}
		});
		function userMoreForUserIdCallBack(options,userIdtag){
			$("#"+userIdtag).val('');
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			
			var crmTypeOptions = $("#crmType_select").find("option");
			var listCrmType = returnListCrmType(crmTypeOptions);
			var listCrmTypeStr = '';
			if(listCrmType.length > 0) {
				listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
			}
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var params = {
				"areaIdAndType":areaIdAndType,
				"startDate":startDate,
				"endDate":endDate,
				"listOwnerStr":listOwnerStr,
				"listCrmTypeStr":listCrmTypeStr
			}
			  getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});   
			
		}
		function customerTypeFilter(){
			$("#listCrmType_show").find('span').remove();
			$("#crmType_select").find("option").remove();
			$("#listCrmType_show").hide();							
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr = '';
			if(listOwner.length > 0) {
				listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
			}
			var params = {
				"areaIdAndType": areaIdAndType,
				"startDate": startDate,
				"endDate": endDate,
				"listOwnerStr": listOwnerStr,
			}
			getSelfJSON(url, params, function(data) {
				initChart(data);
			});
		}
		//客户类型多选返回
		function crmTypeMoreTreeCallBack(options,tag){
			var listCrmType = returnListCrmType(options);
			var listCrmTypeStr = '';
			if(listCrmType.length > 0) {
				listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
			}
			
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr = '';
			if(listOwner.length > 0) {
				listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
			}
			var params = {
				"areaIdAndType": areaIdAndType,
				"startDate": startDate,
				"endDate": endDate,
				"listOwnerStr": listOwnerStr,
				"listCrmTypeStr":listCrmTypeStr
			}
			getSelfJSON(url, params, function(data) {
				initChart(data);
			});
		}
		//移除筛选
		function removeChoose(userIdtag,sid,userId,ts){
			$("#"+userIdtag+"_select").find("option[value='"+userId+"']").remove();
			$("#"+userIdtag).val('');
			$(ts).remove();
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr = '';
			if(listOwner.length > 0) {
				listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
			}
			var crmTypeOptions = $("#crmType_select").find("option");
			var listCrmType = returnListCrmType(crmTypeOptions);
			var listCrmTypeStr = '';
			if(listCrmType.length > 0) {
				listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
			}

			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();

			var params = {
				"areaIdAndType": areaIdAndType,
				"startDate": startDate,
				"endDate": endDate,
				"listOwnerStr": listOwnerStr,
				"listCrmTypeStr":listCrmTypeStr
			}
			getSelfJSON(url, params, function(data) {
				initChart(data);
			});
		}
		//用户返回值用于查询
		function userOneForUserIdCallBack(){
			$("#listOwner_show").find('span').remove();
			$("#owner_select").find("option").remove();
			$("#listOwner_show").hide();
			
			var crmTypeOptions = $("#crmType_select").find("option");
			var listCrmType = returnListCrmType(crmTypeOptions);
			var listCrmTypeStr = '';
			if(listCrmType.length > 0) {
				listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
			}
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var params = {
					"areaIdAndType":areaIdAndType,
					"startDate":startDate,
					"endDate":endDate,
					"listCrmTypeStr":listCrmTypeStr
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
		}
		
      var weekAgoDate;
      var halfMonthAgo;
      var monthAgo;
        getSelfJSON(url,null,function (data) {
    	   initChart(data);
        	weekAgoDate = data.weekAgoDate;
        	halfMonthAgo = data.halfMonthAgo;
        	monthAgo = data.monthAgo;
    	});
       window.onresize = myChart.resize;
       
       //初始化表单数据
       function initChart(data){
    	   
    	  
    	 //客户类型
    	 var legendArray = new Array();
    	 //图例
 		legendArray.push("一周未维护");
 		legendArray.push("半个月未维护");
 		legendArray.push("一个月未维护");
 		 //初始化列表视图
    	  initTable(data,legendArray);
    	 //类型统计数据
    	 var seriesArray = new Array();
    	 
    	 //纵坐标
    	 var yAxisArray = new Array();
    	 
    	 var listBusReport = data.listBusReport;
    	 
    	 var height = 0;
    	 
    	 var totalArray = new Array();
    	 
    	 $.each(listBusReport,function(indexType,listModStaticVos){
    		//结果集
    		var dataArray = new Array();
    		var total = 0;
    		$.each(listModStaticVos,function(indexOwner,vo){
    			if(indexType == 0){
    				yAxisArray.push(vo.name)
    				height = height+30;
    				caseData[vo.name]=vo.type
    			}
    			total = total+parseInt(vo.value);
    			dataArray.push(vo.value);
    		});
    		totalArray.push({'value':total, 'name':indexType==0?"一周未维护":indexType==1?"半个月未维护":"一个月未维护",'type':indexType});
    		var name = indexType==0?"一周未维护":indexType==1?"半个月未维护":"一个月未维护";
    		caseFrequeData[name]=(indexType+1);
    		var series = {
	                name: name,
	                type: 'bar',
	                barMinHeight :1,
	                label: {
	                    normal: {
	                        show: true,
	                        position: 'top'
	                    }
	                },
	                data: dataArray
	            };
    		seriesArray.push(series);
    	  });
    	 var series = {
                name: '占比',
                type: 'pie',
                barMinHeight : 1,
                center: ['85%', '65%'],
                radius: '28%',
                data: totalArray
            };
 		seriesArray.push(series);
    	 if(height>=510){
	    	 $("#main").css("height",height+"px");
	    	 myChart.resize()
    	  }
    	    myChart.setOption({
    	        tooltip : {
    	        	 //trigger: 'axis',
    	             //axisPointer : {            // 坐标轴指示器，坐标轴触发有效
    	                 //type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
    	             //}
    	        },
    	        legend: {
    	            data: legendArray
    	        },
    	        //dataZoom:[{
    	        	 //type: 'inside',    //支持单独的滑动条缩放
                     //start: 0,            //默认数据初始缩放范围为10%到90%
                     //end: 100
    	        //}],
    	        grid: {
    	            left: '0%',
    	            right: '25%',
    	            bottom: '15%',
    	            containLabel: true
    	        },
    	        xAxis:  {
    	            type: 'category',
	    	        axisLabel :{  
	    	        	show:true,
	    	        	 interval: 0,
	    	        	 rotate: -20
	    	        },
    	            data: yAxisArray
    	        },
    	        yAxis: {
    	            type: 'value'
    	        },
    	        series: seriesArray
    	    }); 
       }
       
       myChart.on("click",function(params){
    	   var customerTypeId = caseData[params.name];
    	   var frequeType = caseFrequeData[params.seriesName];
    	   if(params.seriesName && params.seriesName=='占比' && params.seriesType=='pie'){
    		   customerTypeId = '';
    		   frequeType = caseFrequeData[params.name];
    	   }
    	   var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			var crmTypeOptions = $("#crmType_select").find("option");
			var listCrmType = returnListCrmType(crmTypeOptions);
			var listCrmTypeStr = '';
			if(listCrmType.length > 0) {
				listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
			}
			var otherUrl = "/crm/customerListPage?sid=${param.sid}&searchTab=15";
			if(customerTypeId){
				otherUrl +="&customerTypeId="+customerTypeId
			}else{
				otherUrl +="&listCrmTypeStr="+listCrmTypeStr
			}
	   	    otherUrl +="&owner="+owner
	   	    otherUrl +="&areaIdAndType="+areaIdAndType
	   	    otherUrl +="&startDate="+startDate
	   	    otherUrl +="&endDate="+endDate
	   	    otherUrl +="&customerName="+customerName
	   	    otherUrl +="&frequeType="+frequeType
			if(frequeType=='1'){
		   	    otherUrl +="&frequenStartDate="+halfMonthAgo
		   	    otherUrl +="&frequenEndDate="+weekAgoDate
			}else if(frequeType=='2'){
		   	    otherUrl +="&frequenStartDate="+monthAgo
		   	    otherUrl +="&frequenEndDate="+halfMonthAgo
			}else if(frequeType=='3'){
		   	    otherUrl +="&frequenEndDate="+monthAgo
				
			}
	   	 	otherUrl +="&listOwnerStr="+listOwnerStr
	   	    window.self.location=otherUrl
       })
        //初始化列表视图
       function initTable(data,legendArray){
    	   var table = $("#tableView").find("table")
    	   $(table).html('');
    	   
    	   var thead = $("<thead></thead>");
    	   var headTr = $("<tr></tr>");
   	   	var headFirstTh = $("<th style='text-align:center;font-size:13px;width:35;font-weight:bold'>客户类型</th>");
    	   $(headTr).append(headFirstTh)
    	   $.each(legendArray,function(index,vo){
    		   var headTh = $("<th style='text-align:center;font-size:13px;width:20;font-weight:bold'>"+vo+"</th>");
	    	   $(headTr).append(headTh)
    	   })
    	   $(thead).append(headTr)
    	   $(table).append(thead)
    	   
    	   
    	    var listBusReport = data.listBusReport;
    	 
    	   var tbody = $("<tbody></tbody>");
	    	 $.each(listBusReport,function(indexType,listModStaticVos){
	    		$.each(listModStaticVos,function(indexOwner,vo){
	    			var bodyFirstTr = $(tbody).find("tr[typeId='"+vo.type+"']");
	    			var bodyFirstTd = $(tbody).find("td[typeId='"+vo.type+"']");
	    			if(bodyFirstTr.length==0){
	    				bodyFirstTr = $("<tr typeId='"+vo.type+"'></tr>");
    				    bodyFirstTd = $("<td style='font-size:12px;' typeId='"+vo.type+"'>"+vo.name+"</td>");
    				    $(bodyFirstTr).append(bodyFirstTd);
    				    $(tbody).append(bodyFirstTr);
	    			}
	    			var bodyTd = $("<td style='font-size:12px;text-align:center;'>"+vo.value+"</td>");
       			   if(vo.value){
       				   bodyTd = $("<td style='font-size:12px;color:rgb(0,91,255);text-align:center;'><a href='javascript:void(0)' typeId='"+vo.type+"' frequeType='"+(indexType+1)+"'>"+vo.value+"</a></td>");
       			   }
				    $(bodyTd).attr("cellNum",indexType);
       			   $(bodyFirstTr).append(bodyTd);
				    $(tbody).append(bodyFirstTr);
	    		});
    		});
			 $(table).append(tbody)
			 
			 countTotal(table,tbody);
    	   
       }
       //表格穿透
       $("#tableView").on("click","a",function(){
    	   var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
    	   var customerTypeId = $(this).attr("typeId");
    	   var frequeType = $(this).attr("frequeType");
    	   var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			
			var otherUrl = "/crm/customerListPage?sid=${param.sid}&searchTab=15";
	   	    otherUrl +="&customerTypeId="+customerTypeId
	   	    otherUrl +="&owner="+owner
	   	    otherUrl +="&areaIdAndType="+areaIdAndType
	   	    otherUrl +="&startDate="+startDate
	   	    otherUrl +="&endDate="+endDate
	   	    otherUrl +="&customerName="+customerName
	   	    otherUrl +="&frequeType="+frequeType
			if(frequeType=='1'){
		   	    otherUrl +="&frequenStartDate="+halfMonthAgo
		   	    otherUrl +="&frequenEndDate="+weekAgoDate
			}else if(frequeType=='2'){
		   	    otherUrl +="&frequenStartDate="+monthAgo
		   	    otherUrl +="&frequenEndDate="+halfMonthAgo
			}else if(frequeType=='3'){
		   	    otherUrl +="&frequenEndDate="+monthAgo
				
			}
	   	 otherUrl +="&listOwnerStr="+listOwnerStr
	   	    window.self.location=otherUrl
       });
       //切换视图
		$(document).on("click","#changeView",function(){
			 var viewState = $("#changeView").attr("viewState");
			 if("A"==viewState){
				 $("#changeView").attr("viewState","B");
				 $("#chartView").hide();
				 $("#tableView").show();
				 $("#changeView").html("图表视图");
				 $("#excelExport").show()
			 }else if("B"==viewState){
				 $("#changeView").attr("viewState","A");
				 $("#changeView").html("列表视图");
				 $("#chartView").show();
				 $("#tableView").hide();
				 $("#excelExport").hide()
			 }
		})
		//统计综合
		function countTotal(table,tbody){
			//纵向统计综总和
    	   var totalX = $("<tr></tr>");
    	   var totalXFirstTd = $("<td style='font-size:12px;'>总计</td>");
    	   totalX.append(totalXFirstTd);
		   var colNum = $(table).find("thead").find("tr").find("th").length;
		   for(var i=0;i<colNum-1;i++){
			   var aa = $(tbody).find("tr").find("td[cellNum='"+i+"']");
    		   var total = 0;
    		   $.each(aa,function(index,vo){
    			   var num = $(vo).find("a").text();
	    			   if(num){
	    				   total =total+parseInt(num); 
	    			   }
    		   })
    		    var totalTd = $("<td style='font-size:12px;text-align:center;'>"+total+"</td>");
    	   		totalX.append(totalTd);
		   }
		   $(tbody).append(totalX);
    	   $(table).append(tbody);
		}
    </script>
</html>

