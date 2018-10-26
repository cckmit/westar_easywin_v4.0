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
										
										<!--任务状态-->
										<input type="hidden" name="taskState" id="taskState">
										
										<div class="searchCond" style="display: none">
											<div class="btn-group pull-left">
									
				                                    <div class="table-toolbar ps-margin" id="taskStateDiv">
				                                        <div class="btn-group">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="taskStateA">
				                                            	状态筛选
				                                            	<i class="fa fa-angle-down"></i>
				                                            </a>
				                                            <ul class="dropdown-menu dropdown-default">
				                                                <li><a href="javascript:void(0);"  taskStateA="-1" class="taskStateA">不限状态</a></li>
																<li><a href="javascript:void(0);"  taskStateA="1" class="taskStateA">进行中</a></li>
																<li><a href="javascript:void(0);"  taskStateA="4" class="taskStateA">已办结</a></li>
				                                            </ul>
				                                        </div>
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
												<button class="btn btn-info btn-primary btn-xs" type="button"  id="changeView" viewState='A'>列表视图</button>
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
	    var url = '/statistics/task/listTaskStatisByType?sid=${param.sid}&statisticsType=5';
	    
	  //图例数对应显示人数
	    var legendNumArray={}
	    legendNumArray[1]=30;
	    legendNumArray[2]=15;
	    legendNumArray[3]=10;
	    legendNumArray[4]=7;
	    
	   //缓存数据用户穿透
	    var caseData = {};
	   	//缓存用户名称与主键
	    var caseOwnerData = {};
	    //全局变量配置项
	    var option={};
	  //异步取得数据
        getSelfJSON(url,null,function (data) {
    	   initChart(data);
    	});
        //初始化表单数据
        function initChart(data){
        	 initTable(data);
        	 //客户类型
	       	 var legendArray = new Array();
	       	 //类型统计数据
	       	 var seriesArray = new Array();
	       	 
	       	 //纵坐标
	       	 var yAxisArray = new Array();
	       	 caseOwnerData = {};
	       	 //后台取得的数据
	       	 var listBusReport = data.listBusReport;
	       	 var height = 0;
	       	 //遍历后台数据结果
	       	 $.each(listBusReport,function(indexType,obj){
	       		 //缓存客户类型与客户类型主键
	       		caseData[obj.name]=obj.type;
	       		 //图例
	       		legendArray.push(obj.name);
	       		//结果集
	       		var listModStaticVos = obj.childModStaticVo;
	       		var dataArray = new Array();
	       		$.each(listModStaticVos,function(indexOwner,vo){
	       			if(indexType == 0){//
	       				yAxisArray.push(vo.name);
	       				//缓存用户名称与主键
	       				caseOwnerData[vo.name] = vo.type;
	       			}
	       			dataArray.push(vo.value);
	       		})
	       		var seriesObj = {
	   	                name: obj.name,
	   	                type: 'bar',
	   	                barWidth: 18,//固定柱子宽度
	   	                barMinHeight :1,
	   	                label: {
	   	                    normal: {
	   	                        show: true,
	   	                        position: 'top'
	   	                    }
	   	                },
	   	                data: dataArray
	   	            };
	       		seriesArray.push(seriesObj);
	       	  });
	       	 
	       	 //全局配置项
	       	 option = {
	       	        tooltip : {
	       	            //trigger: 'item',
	       	            //axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	       	            //    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	       	            //}
	       	        },
	       	        legend: {
	       	            data: legendArray
	       	        },
	       	        dataZoom:{
	       	        	 show: true
	       	        },
	       	     	grid: {
	    	            left: '5%',
	    	            right: '5%',
	    	            bottom: '15%',
	    	            containLabel: true
	    	        },
	       	        xAxis:  {
	       	            type: 'category',
	       	            axisLabel :{  
	   	    	        	show:true,
	   	    	        	 interval: 0,
	   	    	        	 rotate: -10
	   	    	        },
	       	            data: yAxisArray
	       	        },
	       	        yAxis: {
	       	            type: 'value'
	       	        },
	       	        series: seriesArray
	       	    }
	       	 //图例个数
	       	 var legengLen = legendArray.length;
	       	 //根据图例个数取得起始值
	       	 var scopeValue = getDataZoomScope(legengLen);
	       	 if(scopeValue){
	       		 option.dataZoom ={}
	   	    	 option.dataZoom.show =true
	       		 option.dataZoom.startValue=scopeValue.startValue
	       		 option.dataZoom.endValue=scopeValue.endValue
	       	 }else{
	       		 option.dataZoom ={}
	   	    	 option.dataZoom.show =false 
	       	 }
       	  	//渲染页面
       	    myChart.setOption(option); 
          }
        //图例事件触发
        myChart.on("legendselectchanged",function(params){
     	   var selectLength = 0;
     	   $.each(params.selected,function(type,selected){
     		   if(selected){
     			   selectLength++;
     		   }
     	   })
     	   //重新取得显示范围
     	   var scopeValue = getDataZoomScope(selectLength);
 	       if(scopeValue){
 	    	 option.dataZoom ={}
 	    	 option.dataZoom.show =true
       		 option.dataZoom.startValue=scopeValue.startValue
       		 option.dataZoom.endValue=scopeValue.endValue
       	 }else{
       		option.dataZoom ={}
 	    	 option.dataZoom.show =false
       	 }
 	       //重新渲染图
       	    myChart.setOption(option); 
     	  
        })
        
        //责任人选择
		$(document).on("click","#taskStateDiv .taskStateA",function(){
			//责任人选择类型 0可以直接用,不用弹窗选择
			var state = $(this).attr("taskStateA");
			
			
			if(state>0){
				var typeName = $(this).text();
				var html = '<font style="font-weight:bold;">'+typeName+'</font>';
				html +='<i class="fa fa-angle-down"></i>';
				$("#taskStateA").html(html);
			}else{
				var html = '状态筛选';
				html +='<i class="fa fa-angle-down"></i>';
				$("#taskStateA").html(html);
			}
			
			$("#taskState").val(state);
			if(state && state==-1){
			   $("#taskState").val('');
				state = null;
			}
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var params = {
					"state":state,
					"startDate":startDate,
					"endDate":endDate
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
		})
		
		//查询时间区间
		$(document).on("click","#moreCondition_Div .search",function(){
			var state = $("#taskState").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var params = {
					"state":state,
					"startDate":startDate,
					"endDate":endDate
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
		});
      //查询时间区间
		$(document).on("click","#moreCondition_Div .reset",function(){
			$("#startDate").val("");
			$("#endDate").val("");
			
			var state = $("#taskState").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var params = {
					"state":state,
					"startDate":startDate,
					"endDate":endDate
			}
			getSelfJSON(url,params,function (data) {
	    	   initChart(data);
	    	});
		});
		
		
		
      //取得显示范围
        function getDataZoomScope(legengLen){
     	  var dataZoomValue={};
     	  //取得开头数值
     	   var tempStartIndex = 0;
     	  if(caseOwnerData.length==0){//没有人员,则不显示滚动条
 			  return null;
     	  }
     	   $.each(caseOwnerData,function(name,type){
 				 if(tempStartIndex==0){
 					 dataZoomValue['startValue']=name
 					 return false;
 				 }
 			 })
 			//取得结尾数值 
     	   if(legengLen>15){
     		 //若是超过15个图例，则默认显示一个人员的数据
       		 var userNum = 1;
     		 //只显示一个人
     		 var tempEndIndex = 0;
       		 $.each(caseOwnerData,function(name,type){
 				 if(tempEndIndex==0){
 					 dataZoomValue['endValue']=name
 					 return false;
 				 }
 			 })
       	 }else{//人员个数不超过15个
       		 //取得设定的人员个数
       		 var userNum = legendNumArray[legengLen];
       	 	//人员总数
       		 var userLen = caseOwnerData.length;
       		 if(userNum > userLen){//设定个数大于总数,全部显示
       			 var tempIndex = 0;
      			 $.each(caseOwnerData,function(name,type){
      				 tempIndex ++;
      				 if(tempIndex==userLen){
      					dataZoomValue['endValue']=name
   					 	return false;
      				 }
      			 })
       		 }else{//人员总数超过设定总数,取设定数
       			 var tempIndex = 0;
       			 $.each(caseOwnerData,function(name,type){
       				 tempIndex ++;
       				 if(tempIndex==userNum){
       					dataZoomValue['endValue']=name
    					 	return false;
       				 }
       			 })
       			 
       		 }
       	 }
     	   return dataZoomValue;
        }
        
      //初始化表格视图
        function initTable(data){
     	   var table = $("#tableView").find("table")
     	   $(table).html('');
     	   var thead = $("<thead></thead>");
     	   var headTr = $("<tr></tr>");
     	   var headFirstTh = $("<th style='text-align: center'>客户名称</th>");
     	   $(headTr).append(headFirstTh)
     	   var listBusReport = data.listBusReport;
     	   var tbody = $("<tbody></tbody>");
     	   $.each(listBusReport,function(index,obj){
     		   var headTh = $("<th style='text-align: center'>"+obj.name+"</th>");
 	    	   $(headTr).append(headTh)
 	    	   var listModStaticVos = obj.childModStaticVo;
     		   $.each(listModStaticVos,function(indexA,vo){
     			  var bodyFirstTr = $(tbody).find("tr[itemId='"+vo.type+"']");
    			   var bodyFirstTd = $(tbody).find("td[itemId='"+vo.type+"']");
    			   if(bodyFirstTr.length==0){
     				   bodyFirstTr = $("<tr itemId='"+vo.type+"'></tr>");
     				   bodyFirstTd = $("<td itemId='"+vo.type+"'>"+vo.name+"</td>");
     				   $(bodyFirstTr).append(bodyFirstTd);
     				   $(tbody).append(bodyFirstTr);
     			   }
    			   var bodyTd = $("<td>"+vo.value+"</td>");
    			   if(vo.value){
    				   bodyTd = $("<td><a href='javascript:void(0)' typeId='"+obj.type+"' itemId='"+vo.type+"'>"+vo.value+"</a></td>");
    			   }
    			   $(bodyTd).attr("cellNum",index);
    			   $(bodyFirstTr).append(bodyTd);
    			   $(tbody).append(bodyFirstTr);
        			
     		   })
     	   });
     	   $(thead).append(headTr);
     	   
     	   $(table).append(thead)
     	   $(table).append(tbody)
 		   
 		   countTotal(table,tbody);
        }
      
      //点击事件，跳转到
        myChart.on("click",function(params){
        	var owner =''
        	var grade ='';
        	if(params.seriesType == "pie"){
        		var name = params.name;
        		name = name.substring(0,name.indexOf("("));
     	    	grade = caseData[name];
        	}else{
     	    	owner = caseOwnerData[params.name];
     	    	grade = caseData[params.seriesName];
        	}
 			
     	    var state = $("#taskState").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var otherUrl = "/task/listTaskOfAllPage?sid=${param.sid}&pager.pageSize=10&activityMenu=task_m_1.5";
	  	    otherUrl +="&owner="+owner
	  	    otherUrl +="&startDate="+startDate
	  	    otherUrl +="&endDate="+endDate
	  	    otherUrl +="&state="+state
	  	    otherUrl +="&grade="+grade
	  	    //window.self.location=otherUrl
 			
        })
        //表格穿透
        $(document).on("click","#tableView a",function(){
        	
        	var state = $("#taskState").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var grade = $(this).attr("typeid");
			var owner = $(this).attr("userid");
			
 			var otherUrl = "/task/listTaskOfAllPage?sid=${param.sid}&pager.pageSize=10&activityMenu=task_m_1.5";
    	    otherUrl +="&owner="+owner
    	    otherUrl +="&startDate="+startDate
    	    otherUrl +="&endDate="+endDate
    	    otherUrl +="&state="+state
    	    //window.self.location=otherUrl
        });
      //切换视图
 		$(document).on("click","#changeView",function(){
 			 var viewState = $("#changeView").attr("viewState");
 			 if("A"==viewState){
 				 $("#changeView").attr("viewState","B");
 				 $("#chartView").hide();
 				 $("#tableView").show();
 				 $("#changeView").html("图表视图");
 			 }else if("B"==viewState){
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
    	   var totalXFirstTd = $("<td>总计</td>");
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
    		    var totalTd = $("<td>"+total+"</td>");
    	   		totalX.append(totalTd);
		   }
		   $(tbody).append(totalX);
    	   $(table).append(tbody);
		   
		   //横向统计总和
		   var totalY = $("<th style='text-align: center'>总计</th>");
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
			   var totalTd = $("<td>"+total+"</td>");
			   $(e).append(totalTd)
		   })
		}
 		
       window.onresize = myChart.resize;
       
      
       
    </script>
</html>



