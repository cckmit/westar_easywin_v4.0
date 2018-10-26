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
                               			 <input type="hidden" name="sid" value="${param.sid}"> 
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
															<a href="javascript:void(0);" onclick="customerTypeFilter(this,'')">不限条件</a>
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
				                                                <li><a href="javascript:void(0);"  onclick="userOneForUserIdCallBack()">不限条件</a></li>
																<li><a href="javascript:void(0);"  onclick="userMoreForUserId('${param.sid}','owner');">人员选择</a></li>
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
												<button class="btn btn-info  btn-xs" style="display:none" id ="excelExport" onclick="excelExport('分类统计表')" >导出excel</button>
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
	    var url = '/statistics/crm/listCrmStatisByType?sid=${param.sid}&statisticsType=1';
	    //缓存数据用户穿透
	    var caseData = {};
		
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

			var areaIdAndTypeT = $(this).attr("areaIdAndTypeA");
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
		//人员筛选
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
		
      
        getSelfJSON(url,null,function (data) {
    	   initChart(data);
    	});
       window.onresize = myChart.resize;
       
	function getRandom(min,max){
		var r = Math.random()*(max-min);
		var re=Math.round(r+min);
		re =Math.max(Math.min(re,max),min);
		return re;
		}
    	   
       //初始化表单数据
       function initChart(data){
    	   //初始化列表视图
     	  initTable(data);
    	 //客户类型
    	   var legendArray = new Array();
    	   //类型统计数据
    	   var dataArray = new Array();
    	   var listBusReport = data.listBusReport;
    	   $.each(listBusReport,function(index,obj){
    		    //图例
	       		legendArray.push(obj.name);
	      		dataArray.push(obj.value);
	      		caseData[obj.name]=obj.type
    	   });
    	    var coler = ['#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3'];
    	    var random=getRandom(0,9);
    	    var colorArray = new Array();
    	    colorArray.push(coler[random])
    	    myChart.setOption({
    	    	color:colorArray,
    	        tooltip : {
    	            //trigger: 'axis',
    	           // axisPointer : {            // 坐标轴指示器，坐标轴触发有效
    	            //    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
    	            //}
    	        },
    	        grid: {
    	        	left: '1%',
    	            right: '10%',
    	            bottom: '10%',
    	            containLabel: true
    	        },
    	        xAxis : [
    	            {
    	                type : 'category',
    	                data : legendArray,
    	                axisLabel :{  
    	    	        	show:true,
    	    	        	 interval: 0,
    	    	        	 rotate: -20
    	    	        },
    	                axisTick: {
    	                    alignWithLabel: true
    	                }
    	            }
    	        ],
    	        yAxis : [
    	            {
    	                type : 'value'
    	            }
    	        ],
    	        series : [
    	            {
    	                name:'客户数',
    	                type:'bar',
    	                barWidth: '30',
    	                barMinHeight :1,
    	                label: {
    	                    normal: {
    	                        show: true,
    	                        position: 'top'
    	                    }
    	                },
    	                data:dataArray
    	            }
    	        ]
    	    }); 
       }
       
       myChart.on("click",function(params){
    	   var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			
    	    var customerTypeId = caseData[params.name];
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
    	    otherUrl +="&listOwnerStr="+listOwnerStr
    	   window.self.location=otherUrl
       })
       //初始化表格视图
       function initTable(data){
    	   
    	   var table = $("#tableView").find("table")
    	   $(table).html('');
    	   
    	   var thead = $("<thead></thead>");
    	   var headTr = $("<tr></tr>");
    	   for(var i=0;i<2;i++){
    		   var headTh = $("<th style='text-align:center;font-size:13px;width:35;font-weight:bold;'>客户类型</th>");
	    	   $(headTr).append(headTh)
    		   headTh = $("<th style='text-align:center;font-size:13px;width:15;font-weight:bold;'>数量</th>");
	    	   $(headTr).append(headTh)
    	   }
    	   $(thead).append(headTr);
    	   $(table).append(thead)
    	   
    	   var listBusReport = data.listBusReport;
    	   var tbody = $("<tbody></tbody>");
    	   var lastLeft = 0;
    	   $.each(listBusReport,function(index,obj){
    		   var y = index % 2;
    		   var x = (index - y)/2;
    		   var bodyTr = $(tbody).find("tr[rownum='"+x+"']")
    		   if(bodyTr.length == 0){
    			   bodyTr = $("<tr rownum='"+x+"'></tr>");
    			   $(tbody).append(bodyTr);
    			   
    			   bodyTr = $(tbody).find("tr[rownum='"+x+"']")
    		   }
    		   var nameTd =$("<td style='font-size:12px;'>"+obj.name+"</td>");
    		   var valueTd =$("<td style='font-size:12px;text-align:center;'>"+obj.value+"</td>");
    		   if(obj.value){
    			   valueTd =$("<td style='font-size:12px;color:rgb(0,91,255);text-align:center;'><a href='javascript:void(0)' typeId='"+obj.type+"'>"+obj.value+"</a></td>");
    		   }
    		   $(bodyTr).append(nameTd);
    		   $(bodyTr).append(valueTd);
    		   lastLeft = index;
    	   });
    	   var y = lastLeft % 2;
		   var x = (lastLeft - y)/2;
		   for(var i = y ;i<1;i++){
			   var bodyTr = $(tbody).find("tr[rownum='"+x+"']")
    		   if(bodyTr.length == 0){
    			   bodyTr = $("<tr rownum='"+x+"'></tr>");
    			   $(tbody).append(bodyTr);
    			   
    			   bodyTr = $(tbody).find("tr[rownum='"+x+"']")
    		   }
    		   var nameTd =$("<td style='font-size:12px;'></td>");
    		   var valueTd =$("<td style='font-size:12px;text-align:center;'></td>");
    		   $(bodyTr).append(nameTd);
    		   $(bodyTr).append(valueTd);
		   }
    	   $(table).append(tbody)
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
	  	    otherUrl +="&customerName="+customerName,
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
				 $("#tableView").hide();
				 $("#chartView").show();
				 $("#excelExport").hide()
			 }
		})
    </script>
</html>

