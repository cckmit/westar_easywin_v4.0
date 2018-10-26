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
										<input type="hidden" id=regisYM value="${regisYM}" >
										<!--客户类型选择 -->
										<input type="hidden" name="customerTypeId" id="customerTypeId" >
										<!--客户责任人-->
										<input type="hidden" name="owner" id="owner">
										<input type="hidden" name="ownerName" id="ownerName">
										 
										<input type="hidden" name="selectYear" id="selectYear" value="${defaultYear}">
										<!--客户区域-->
										<input type="hidden" name="areaIdAndType" id="areaIdAndType">
										
										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
				                                    <div class="table-toolbar ps-margin" id="selectYearDiv">
				                                        <div class="btn-group">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="selectYearA">
				                                            	<font style="font-weight:bold;">${defaultYear}年度</font>
				                                            	<i class="fa fa-angle-down"></i>
				                                            </a>
				                                            <ul class="dropdown-menu dropdown-default" style="width: 150px">
				                                            	<c:choose>
				                                            		<c:when test="${not empty  yeasList}">
				                                            			<c:forEach var="year" items="${yeasList}" varStatus="vs">
							                                                <li><a href="javascript:void(0);"  selectYearA="${year}" class="selectYearA">${year}年度</a></li>
				                                            			</c:forEach>
				                                            		</c:when>
				                                            	</c:choose>
				                                            </ul>
				                                        </div>
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
				                                                <li><a href="javascript:void(0);" ownerType="0"  ownerId="0" class="selectOwner" >不限条件</a></li>
																<li><a href="javascript:void(0);" ownerType="1"  onclick="userMoreForUserId('${param.sid}','owner');">人员选择</a></li>
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
											<button class="btn btn-info  btn-xs" style="display:none" id ="excelExport" onclick="excelExport('增长趋势分析表')" >导出excel</button>
											</div>
                               		 </div>
                               		 
													<div id="listOwner_show" class="padding-top-10 text-left " style="display:${empty customer.listOwner ? 'none':'block'}">
											<strong >责任人筛选:</strong>
											<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
												 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')"
												 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
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
	    var url = '/statistics/crm/listCrmStatisByType?sid=${param.sid}&statisticsType=5';
	    //缓存数据用户穿透
	    var caseData = {};
	    var caseMonthData = {};
	    var monthArray = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
		//客户类型选择
		$(document).on("click","#customerTypeIdDiv .crmTypeId",function(){
			$("#customerTypeId").val($(this).attr("crmtypeid"));
			
			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			var selectYear = $("#selectYear").val();
			
			if(customerTypeId>0){
				var typeName = $(this).text();
				var html = '<font style="font-weight:bold;">'+typeName+'</font>';
				html +='<i class="fa fa-angle-down"></i>';
				$("#crmTypeNameA").html(html);
			}else{
				var html = '客户类型';
				html +='<i class="fa fa-angle-down"></i>';
				$("#crmTypeNameA").html(html);
			}
			var params = {
					"customerTypeId":customerTypeId,
					"owner":owner,
					"areaIdAndType":areaIdAndType,
					"startDate":startDate,
					"endDate":endDate,
					"customerName":customerName,
					"selectYear":selectYear
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
			
		});
		$(function(){
			var selectYear = $("#selectYear").val();
			var params = {
					"selectYear":selectYear
			}
	        getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
			//客户名筛选
			$("#customerName").off("blur").on("blur",function(){
				var customerTypeId = $("#customerTypeId").val();
				var owner = $("#owner").val();
				var areaIdAndType = $("#areaIdAndType").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var customerName = $("#customerName").val();
				var selectYear = $("#selectYear").val();
				
				var params = {
						"customerTypeId":customerTypeId,
						"owner":owner,
						"areaIdAndType":areaIdAndType,
						"startDate":startDate,
						"endDate":endDate,
						"customerName":customerName,
						"selectYear":selectYear
				}
				getSelfJSON(url,params,function (data) {
		    	   initChart(data);
		    	});
			});
			//文本框绑定回车提交事件
			$("#customerName").unbind("keydown").bind("keydown",function(event){
		        if(event.keyCode == "13")    
		        {
		        	var customerTypeId = $("#customerTypeId").val();
					var owner = $("#owner").val();
					var areaIdAndType = $("#areaIdAndType").val();
					var startDate = $("#startDate").val();
					var endDate = $("#endDate").val();
					var customerName = $("#customerName").val();
					var selectYear = $("#selectYear").val();
					
					var params = {
							"customerTypeId":customerTypeId,
							"owner":owner,
							"areaIdAndType":areaIdAndType,
							"startDate":startDate,
							"endDate":endDate,
							"customerName":customerName,
							"selectYear":selectYear
					}
					getSelfJSON(url,params,function (data) {
			    	   initChart(data);
			    	});
		        }
		    });
		})
		
		//查询时间区间
		$(document).on("click","#moreCondition_Div .search",function(){
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			var selectYear = $("#selectYear").val();
			
			var params = {
					"customerTypeId":customerTypeId,
					"owner":owner,
					"areaIdAndType":areaIdAndType,
					"startDate":startDate,
					"endDate":endDate,
					"customerName":customerName,
					"selectYear":selectYear,
					"listOwnerStr":listOwnerStr
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
		});
		//查询时间区间
		$(document).on("click","#moreCondition_Div .reset",function(){
			$("#startDate").val("");
			$("#endDate").val("");
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			var selectYear = $("#selectYear").val();
			
			var params = {
					"customerTypeId":customerTypeId,
					"owner":owner,
					"areaIdAndType":areaIdAndType,
					"startDate":startDate,
					"endDate":endDate,
					"customerName":customerName,
					"selectYear":selectYear,
					"listOwnerStr":listOwnerStr
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
		});
		//客户区域选择
		$(document).on("click","#selectYearDiv .selectYearA",function(){
			var selectYear = $(this).attr("selectYearA");
			var html = '<font style="font-weight:bold;">'+selectYear+'年度</font>';
			html +='<i class="fa fa-angle-down"></i>';
			$("#selectYearA").html(html);
			$("#selectYear").val(selectYear);
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			var selectYear = $("#selectYear").val();
			
			var params = {
					"customerTypeId":customerTypeId,
					"owner":owner,
					"areaIdAndType":areaIdAndType,
					"startDate":startDate,
					"endDate":endDate,
					"customerName":customerName,
					"selectYear":selectYear,
					"listOwnerStr":listOwnerStr
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
		})
		//责任人选择
		$(document).on("click","#ownerDiv .selectOwner",function(){
			//责任人选择类型 0可以直接用,不用弹窗选择
			var ownerType = $(this).attr("ownerType");
			//责任人主键
			var ownerId = $(this).attr("ownerId");
			var ownerName = $(this).text();
			if(ownerType == 1){//需要弹窗选择
				var owner = $("#owner").val();
				var ownerName = $("#ownerName").val();
				if(owner &&　ownerName){//有选择人员
					userOneForUserId(owner,ownerName,'','${param.sid}','owner','');
				}else{//默认当前人员
					userOneForUserId('${userInfo.id}','${userInfo.userName}','','${param.sid}','owner','')
				}
			}else{
				userOneForUserIdCallBack(ownerId,"",ownerName,"");
			}
		})
		//客户区域选择
		$(document).on("click","#areaIdAndTypeDiv .areaIdAndTypeA",function(){
			var areaIdAndTypeT = $(this).attr("areaIdAndTypeA");
			if(areaIdAndTypeT==1){//点击选择弹窗数据
				areaOne('areaIdAndType','areaName','${param.sid}','1',function(result){
					$("#areaIdAndType").val(result.idAndType);
					var options = $("#owner_select").find("option");
					var listOwner = returnListOwner(options);
					var listOwnerStr ='';
					if(listOwner.length>0){
						listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
					}
					var customerTypeId = $("#customerTypeId").val();
					var owner = $("#owner").val();
					var areaIdAndType = $("#areaIdAndType").val();
					var startDate = $("#startDate").val();
					var endDate = $("#endDate").val();
					var customerName = $("#customerName").val();
					var selectYear = $("#selectYear").val();
					
					var html = '<font style="font-weight:bold;">'+result.areaName+'</font>';
					html +='<i class="fa fa-angle-down"></i>';
					$("#areaIdAndTypeA").html(html);
					var params = {
							"customerTypeId":customerTypeId,
							"owner":owner,
							"areaIdAndType":areaIdAndType,
							"startDate":startDate,
							"endDate":endDate,
							"customerName":customerName,
							"selectYear":selectYear,
							"listOwnerStr":listOwnerStr
					}
					getSelfJSON(url,params,function (data) {
			    	   initChart(data);
			    	});
				})
			}else{//所有数据
				var areaIdAndType = $("#areaIdAndType").val('');
				
				var customerTypeId = $("#customerTypeId").val();
				var owner = $("#owner").val();
				var areaIdAndType = $("#areaIdAndType").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var customerName = $("#customerName").val();
				var selectYear = $("#selectYear").val();
				
				var html = '区域筛选';
				html +='<i class="fa fa-angle-down"></i>';
				$("#areaIdAndTypeA").html(html);
				var params = {
						"customerTypeId":customerTypeId,
						"owner":owner,
						"areaIdAndType":areaIdAndType,
						"startDate":startDate,
						"endDate":endDate,
						"customerName":customerName,
						"selectYear":selectYear
				}
				getSelfJSON(url,params,function (data) {
		    	   initChart(data);
		    	});
			}
		});
		function userMoreForUserIdCallBack(options,userIdtag){
			$("#"+userIdtag).val('');
			var listOwner = returnListOwner(options);
			
			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			var selectYear = $("#selectYear").val();
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			var params = {
				"customerTypeId":customerTypeId,
				"owner":owner,
				"areaIdAndType":areaIdAndType,
				"startDate":startDate,
				"endDate":endDate,
				"customerName":customerName,
				"listOwnerStr":listOwnerStr,
				"selectYear":selectYear
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
			var options = $("#"+userIdtag+"_select").find("option");
			var listOwner = returnListOwner(options);
			
			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			var selectYear = $("#selectYear").val();
			var params = {
				"customerTypeId":customerTypeId,
				"owner":owner,
				"areaIdAndType":areaIdAndType,
				"startDate":startDate,
				"endDate":endDate,
				"customerName":customerName,
				"listOwnerStr":listOwnerStr,
				"selectYear":selectYear,
			}
			  getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});   
		}
		//用户返回值用于查询
		function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
			$("#listOwner_show").find('span').remove();
			$("#"+userIdtag+"_select").find("option").remove();
			$("#listOwner_show").hide();
			if(userId && userId>0){//有负责人信息
				$("#owner").val(userId);
				$("#ownerName").val(userName);
				
				var html = '<font style="font-weight:bold;">'+userName+'</font>';
				html +='<i class="fa fa-angle-down"></i>';
				$("#ownerNameA").html(html);
			}else{//不限人员
				$("#owner").val('');
				$("#ownerName").val('');
				
				var html = '责任人筛选';
				html +='<i class="fa fa-angle-down"></i>';
				$("#ownerNameA").html(html);
			}
			
			var customerTypeId = $("#customerTypeId").val();
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var customerName = $("#customerName").val();
			var selectYear = $("#selectYear").val();
			
			var params = {
					"customerTypeId":customerTypeId,
					"owner":owner,
					"areaIdAndType":areaIdAndType,
					"startDate":startDate,
					"endDate":endDate,
					"customerName":customerName,
					"selectYear":selectYear
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
		}
		
       window.onresize = myChart.resize;
       
       //初始化表单数据
       function initChart(data){
    	  //初始化列表视图
    	  initTable(data);
    	 //客户类型
    	   var legendArray = new Array();
    	 //客户类型
    	   var axisArray = new Array();
    	   var month = data.nowMonth;
    	   $.each(monthArray.slice(0,month),function(index,month){
    		   axisArray.push(month)
    	   });
    	   //数据
    	   var seriesArray = new Array();
    	   var listBusReport = data.listBusReport;
    	   $.each(listBusReport,function(index,obj){
    		   caseData[obj.typeName]=obj.id;
    		   legendArray.push(obj.typeName);
    		   
	    	   //类型统计数据
	    	   var dataArray = new Array();
    		   var listModStaticVos = obj.listModStaticVos;
    		   $.each(listModStaticVos,function(indexA,vo){
    			   if(index == 0){
    				   caseMonthData[axisArray[indexA]] = vo.name;
    			   }
    			   dataArray.push(vo.value);
    		   })
    		   var series = {
	                name: obj.typeName,
	                type:'line',
	                symbolSize:10,
	                showAllSymbol:true,
	                //stack: '总量',
	                data: dataArray
	            };
   				seriesArray.push(series);
    	   });
    	    myChart.setOption({
    	    	    tooltip: {
    	    	        //trigger: 'axis'
    	    	    },
    	    	    legend: {
    	    	        data:legendArray
    	    	    },
    	    	    grid: {
    	    	        left: '3%',
    	    	        right: '4%',
    	    	        bottom: '3%',
    	    	        containLabel: true
    	    	    },
    	    	    toolbox: {
    	    	    },
    	    	    xAxis: {
    	    	        type: 'category',
    	    	        boundaryGap: false,
    	    	        data: axisArray
    	    	    },
    	    	    yAxis: {
    	    	        type: 'value'
    	    	    },
    	    	    series: seriesArray
    	    	}); 
       }
       
       myChart.on("click",function(params){
    	   var customerTypeId = caseData[params.seriesName];
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var customerName = $("#customerName").val();
			
			var chooseMonth = caseMonthData[params.name]
			var startDate = chooseMonth+"-01";
			
			var trsDate = new Date(startDate);
			var month = trsDate.getMonth();
			var year = trsDate.getFullYear();
			
			var date = getLastDay(year,month);
			var endDate = date.format('yyyy-MM-dd')
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			
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
       
       function getLastDay(year,month) {      
             var new_year = year;    //取当前的年份          
             var new_month = ++month;//取下一个月的第一天，方便计算（最后一天不固定）          
             if(month>12) {         
              new_month -=12;        //月份减          
              new_year++;            //年份增          
             }         
             var new_date = new Date(new_year,new_month,1);                //取当年当月中的第一天   
             return (new Date(new_date.getTime()-1000*60*60*24));//获取当月最后一天日期          
        }
       
       Date.prototype.format = function(format) {
           var date = {
                  "M+": this.getMonth() + 1,
                  "d+": this.getDate(),
                  "h+": this.getHours(),
                  "m+": this.getMinutes(),
                  "s+": this.getSeconds(),
                  "q+": Math.floor((this.getMonth() + 3) / 3),
                  "S+": this.getMilliseconds()
           };
           if (/(y+)/i.test(format)) {
                  format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
           }
           for (var k in date) {
                  if (new RegExp("(" + k + ")").test(format)) {
                         format = format.replace(RegExp.$1, RegExp.$1.length == 1
                                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
                  }
           }
           return format;
    }
       //初始化表格视图
       function initTable(data){
    	   var table = $("#tableView").find("table")
    	   $(table).html('');
    	   
    	   var selectYear = $("#selectYear").val();
    	   
    	   var thead = $("<thead></thead>");
    	   var headTr = $("<tr></tr>");
    	   var headFirstTh = $("<th style='text-align:center;font-size:13px;width:35;font-weight:bold'>"+selectYear+"年度</th>");
    	   $(headTr).append(headFirstTh)
    	   $.each(monthArray,function(index,monthName){
    		   var headTh = $("<th style='text-align:center;font-size:13px;width:15;font-weight:bold'>"+monthName+"</th>");
	    	   $(headTr).append(headTh)
    	   })
    	   $(thead).append(headTr);
    	   $(table).append(thead)
    	   
    	   
    	   var tbody = $("<tbody></tbody>");
    	   var listBusReport = data.listBusReport;
    	   var month = data.nowMonth;
    	   
   	       
   	       var registYear = $("#regisYM").val().substr(0,4);
   	       var registMonth = parseInt($("#regisYM").val().substr(5,6))-1;
    	   $.each(listBusReport,function(index,obj){
    	       var bodyTr = $("<tr></tr>");
    	       var bodyFirstTd = $("<td style='font-size:13px;'>"+obj.typeName+"</td>");
    	       $(bodyTr).append(bodyFirstTd)
    		   var listModStaticVos = obj.listModStaticVos;
    		   $.each(listModStaticVos,function(indexA,vo){
    			   var bodyTd = $("<td style='font-size:12px;text-align:center;'>"+vo.value+"</td>");
    			   if(selectYear == registYear){
    				   if(registMonth>indexA){
    					   bodyTd = $("<td  style='font-size:12px;text-align:center;'>--</td>");
    				   }else if(vo.value){
	    				   bodyTd = $("<td style='font-size:12px;color:rgb(0,91,255);text-align:center;'><a href='javascript:void(0)' typeId='"+obj.id+"' monthNum='"+vo.name+"'>"+vo.value+"</a></td>");
	    			   }
    			   }else{
	    			   if(vo.value){
	    				   bodyTd = $("<td style='font-size:12px;color:rgb(0,91,255);text-align:center;'><a href='javascript:void(0)' typeId='"+obj.id+"' monthNum='"+vo.name+"'>"+vo.value+"</a></td>");
	    			   }
    			   }
    			   $(bodyTd).attr("cellNum",indexA);
    			   $(bodyTr).append(bodyTd)
    		   })
	    	   for(var i=month;i<12;i++){
	    		   var bodyTd = $("<td  style='font-size:12px;text-align:center;'>--</td>");
	    		   $(bodyTd).attr("cellNum",i);
				   $(bodyTr).append(bodyTd)
	    	   }
	    	   $(tbody).append(bodyTr);
    	   });
    	   
    	   countTotal(table,tbody);
       }
     //表格穿透
       $("#tableView").on("click","a",function(){
    	   var customerTypeId =$(this).attr("typeId");
			
			var owner = $("#owner").val();
			var areaIdAndType = $("#areaIdAndType").val();
			var customerName = $("#customerName").val();
			
			var chooseMonth = $(this).attr("monthNum");
			var startDate = chooseMonth+"-01";
			
			var trsDate = new Date(startDate);
			var month = trsDate.getMonth();
			var year = trsDate.getFullYear();
			
			var date = getLastDay(year,month);
			var endDate = date.format('yyyy-MM-dd')
			var options = $("#owner_select").find("option");
			var listOwner = returnListOwner(options);
			var listOwnerStr ='';
			if(listOwner.length>0){
				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
			}
			
			var otherUrl = "/crm/customerListPage?sid=${param.sid}&searchTab=15";
    	    otherUrl +="&customerTypeId="+customerTypeId
    	    otherUrl +="&owner="+owner
    	    otherUrl +="&areaIdAndType="+areaIdAndType
    	    otherUrl +="&startDate="+startDate
    	    otherUrl +="&endDate="+endDate
    	    otherUrl +="&customerName="+customerName
    	    otherUrl +="&listOwnerStr="+listOwnerStr

    	    window.self.location=otherUrl
       });
       //切换视图
		$(document).on("click","#changeView",function(){
			 var viewState = $("#changeView").attr("viewState");
			 if("A"==viewState){
				 $("#excelExport").show()
				 $("#changeView").attr("viewState","B");
				 $("#chartView").hide();
				 $("#tableView").show();
				 $("#changeView").html("图表视图");
			 }else if("B"==viewState){
				 $("#excelExport").hide()
				 $("#changeView").attr("viewState","A");
				 $("#changeView").html("列表视图");
				 $("#chartView").show();
				 $("#tableView").hide();
			 }
		})
		//统计综合
		function countTotal(table,tbody){
			//纵向统计综总和
    	   var totalX = $("<tr></tr>");
    	   var totalXFirstTd = $("<td  style='text-align: center;font-size:13px;'>总计</td>");
    	   totalX.append(totalXFirstTd);
    	   for(var i=0;i<12;i++){
    		   var aa = $(tbody).find("tr").find("td[cellNum='"+i+"']");
    		   var total = 0;
    		   $.each(aa,function(index,vo){
    			   if($(vo).text()=='--'){
    				   total = -1
    			   }else{
	    			   var num = $(vo).find("a").text();
	    			   if(num){
	    				   total =total+parseInt(num); 
	    			   }
    			   }
    		   })
    		   if(total == -1){
    		    var totalTd = $("<td  style='font-size:12px;text-align:center;'>--</td>");
    	   		totalX.append(totalTd);
    		   }else{
    		    var totalTd = $("<td style='font-size:12px;text-align:center;'>"+total+"</td>");
    	   		totalX.append(totalTd);
    			   
    		   }
    	   }
    	   $(tbody).append(totalX);
    	   $(table).append(tbody);
	    	   
		 //横向统计总和
		   var totalY = $("<th style='text-align: center;font-size:13px;'>总计</th>");
		   $(table).find("thead tr").append(totalY);
		   var bodyTrs = $(tbody).find("tr");
		   $.each(bodyTrs,function(trIndex,trObj){
			   var e = $(this);
			   var total = 0;
			   $.each($(e).find("td"),function(tdIndex,tdObj){
				   var num = $(tdObj).find("a").text();
				   if(trIndex == bodyTrs.length-1){
					   num = $(tdObj).text();
					   if(num=='总计' || num=='--'){
						   num=0;
					   }
				   }
				   if(num){
					   total =total+parseInt(num); 
				   }
			   })
			   var totalTd = $("<td style='font-size:12px;text-align:center;'>"+total+"</td>");
			   $(e).append(totalTd)
		   })
		 }
       
    </script>
</html>



