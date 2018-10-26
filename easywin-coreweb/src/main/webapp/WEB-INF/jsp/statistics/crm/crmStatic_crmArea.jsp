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
                                <div >
										<!--客户类型选择 -->
										<input type="hidden" name="customerTypeId" id="customerTypeId" >
										<!--客户责任人-->
										<input type="hidden" name="owner" id="owner">
										<input type="hidden" name="ownerName" id="ownerName">
										<!--客户区域-->
										<input type="hidden" name="areaIdAndType" id="areaIdAndType">
										
										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
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
				                                    <div class="table-toolbar ps-margin" id="areaDiv" style="display:none">
				                                        <div class="btn-group">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="areaNameA">
				                                            	区域筛选
				                                            	<i class="fa fa-angle-down"></i>
				                                            </a>
				                                            <ul class="dropdown-menu dropdown-default margin-bottom-10" id="areaListUl"
				                                            style="height: 250px;overflow: no;overflow-y:scroll ">
				                                                <li><a href="javascript:void(0);" provinceId="0" class="selectArea" >不限条件</a></li>
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
												<button class="btn btn-info btn-primary btn-xs" type="button" id="changeView" viewState='A'>列表视图</button>
												<button class="btn btn-info  btn-xs" style="display:none" id ="excelExport" onclick="excelExport('分布统计表')" >导出excel</button>
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
											<table class="table table-bordered" style="text-align: center;" id="dataTable">
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
	    var url = '/statistics/crm/listCrmStatisByType?sid=${param.sid}&statisticsType=3';
	    //缓存各个省市的数据
		var caseData = {};
	    //缓存各个市区的名称对应主键
		var caseAreaData = {};
	    //缓存省份对应区域主键
		var caseRegionData = {};
		option = {
	    	    tooltip : {
	    	        trigger: 'item'
	    	    },
	    	    toolbox: {
	    	        show : true,
	    	        orient: 'vertical',
	    	        x:'right',
	    	        y:'center'
	    	    },
	    	    series : [
	    	        {
	    	            tooltip: {
	    	                trigger: 'item',
	    	                formatter: function (params){
	    	                	var data = params.data;
	    	                	return data.name+"<br>客户数："+data.value
	    	                }
	    	            },
	    	            name: '客户数',
	    	            type: 'map',
	    	            mapType: 'china',
	    	            mapLocation: {
	    	            	x:'center',
	    	            	top:'10',
	    	            	width:'570px'
	    	            },
	    	            roam: false,
	    	            selectedMode : 'single',
	    	            itemStyle:{
	    	                //normal:{label:{show:true}},
	    	                emphasis:{label:{show:true}}
	    	            },
	    	            zlevel:10
	    	        }
	    	    ],
	    	    animation: false
	    	};
		 myChart.showLoading({
             text : '数据获取中',
             effect: 'whirling'
         });
		getProvinceTotal(function(result){
			option.legend = {
	     	        x:'right',
	     	        data:['客户数']
	     	    };
	     	    option.dataRange = {
	     	        orient: 'horizontal',
	     	        x: 'right',
	     	        min: 0,
	     	        max: 100,
	     	        color:['orange','green'],
	     	        text:['高','低'],           // 文本，默认为数值文本
	     	        splitNumber:0
	     	    };
			option.series[0].data=result
	    	 myChart.setOption(option,true); 
			 myChart.hideLoading();
		});
		
	    
	    
	    	myChart.on("mapselectchanged", function (param){
	    		
	    	    var selected = param.selected;
	    	    var selectedProvince;
	    	   
	    	    var name;
	    	    for (var i = 0, l = option.series[0].data.length; i < l; i++) {
	    	        name = option.series[0].data[i].name;
	    	        option.series[0].data[i].selected = selected[name];
	    	        if (selected[name]) {
	    	            selectedProvince = name;
	    	        }
	    	    }
	    	    if (typeof selectedProvince == 'undefined') {
	    	        option.series.splice(1);
	    	        option.legend = null;
	    	        option.dataRange = null;
	    	        option.legend = {
	    	   	            x:'right',
	    	   	            data:['客户数']
	    	   	        };
	       	     	    option.dataRange = {
	       	     	        orient: 'horizontal',
	       	     	        x: 'right',
	       	     	        min: 0,
	       	     	        max: 100,
	       	     	        color:['orange','green'],
	       	     	        text:['高','低'],           // 文本，默认为数值文本
	       	     	        splitNumber:0
	       	     	    };
	    	        option.series[0].mapLocation = {
	    	        		x:'center',
	    	            	top:'10',
	    	            	width:'570px'
	    	        };
	    	        myChart.setOption(option, true);
	    	        return;
	    	    }else{
	    	    	option.series.splice(1);
	    	        option.legend = null;
	    	        option.dataRange = null;
	    	    	option.series[0].mapLocation = {
	    	    			 left:'10',
		    	            	top:'10',
		    	            	width:'300px'
	    	    	};
	    	    }
	    	    
	    	    var provinceData = caseData[selectedProvince]
	    	    option.series[1] = null;
	    	    option.series[1] = {
   	    	        name: '客户数',
   	    	        type: 'map',
   	    	        mapType: selectedProvince,
   	    	        itemStyle:{
   	    	            normal:{label:{show:true}},
   	    	            emphasis:{label:{show:true}}
   	    	        },
   	    	        zlevel:100,
   	    	        mapLocation: {
   	    	        	 right: '0',
   	    	             top: '10',
   	    	             width: '40%'
   	    	        },
   	    	        roam: true,
   	    	        data:provinceData
   	    	    };
	    	    
	    	    option.legend = {
	   	            x:'right',
	   	            data:['客户数']
	   	        };
   	     	    option.dataRange = {
   	     	        orient: 'horizontal',
   	     	        x: 'right',
   	     	        min: 0,
   	     	        max: 100,
   	     	        color:['orange','green'],
   	     	        text:['高','低'],           // 文本，默认为数值文本
   	     	        splitNumber:0
   	     	    };
	    	    myChart.setOption(option, true);
	    	})
	    	
	    	
	    	//取得省份的统计数
    	    function getProvinceTotal(callback){
    		    var regionArray = new Array();
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
 				
    	    	 var params = {
    						"customerTypeId":customerTypeId,
    						"owner":owner,
    						"areaIdAndType":areaIdAndType,
    						"startDate":startDate,
    						"endDate":endDate,
    						"customerName":customerName,
    						"listOwnerStr":listOwnerStr
    				}
    		    getSelfJSON(url,params,function (data) {
    		    	initTable(data);
    		    	$.each(data.listBusReport,function(index,vo){
    		    		caseRegionData[vo.name]=vo.type;
    		    		var region = {name:vo.name,selected:false,value:vo.value}
    		    		regionArray.push(region)
    		    		var tempData = new Array()
    		    		var childModStaticVo = vo.childModStaticVo;
	    	    		 $.each(childModStaticVo,function(index,childVo){
	    	    			 var aa = {name:childVo.name,value:childVo.value};
		    	    		 caseAreaData[childVo.name]=childVo.type;
		    	    		 tempData.push(aa);
		    	    		 
	    	    		 })
	    	    		 caseData[vo.name]=tempData;
    		    	});
    		    	var region = {name: '台湾', selected:false,value:0}
    		    	regionArray.push(region)
    	            region = {name: '香港', selected:false,value:0}
    		    	regionArray.push(region)
    	            region =  {name: '澳门', selected:false,value:0}
    		    	regionArray.push(region)
	    		    callback(regionArray)
    		    });
    	    }
	    	
    	    function getSubOption(option,selectedProvince,provinceData){
    	    	option.series[1] = {
   	    	        name: '客户数',
   	    	        type: 'map',
   	    	        mapType: selectedProvince,
   	    	        itemStyle:{
   	    	            normal:{label:{show:true}},
   	    	            emphasis:{label:{show:true}}
   	    	        },
   	    	        zlevel:100,
   	    	        mapLocation: {
   	    	        	 right: '0',
   	    	             top: '10',
   	    	             width: '40%'
   	    	        },
   	    	        roam: true,
   	    	        data:provinceData
   	    	    };
   	    	    option.legend = {
	   	            x:'right',
	   	            data:['客户数']
	   	        };
   	     	    option.dataRange = {
   	     	        orient: 'horizontal',
   	     	        x: 'right',
   	     	        min: 0,
   	     	        max: 100,
   	     	        color:['orange','green'],
   	     	        text:['高','低'],           // 文本，默认为数值文本
   	     	        splitNumber:0
   	     	    };
   	    	    return  option;
   	   		 }
    	    
    	    
    	  //双击查看详情列表
            myChart.on("dblclick", function (param){
        	   var seriesName=param.seriesName;
        	   var options = $("#owner_select").find("option");
        		var listOwner = returnListOwner(options);
 				var listOwnerStr ='';
 				if(listOwner.length>0){
 					listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
 				}
        	   if(param.seriesIndex ==0 && (seriesName.trim()=="客户数"||seriesName.trim()=="")){//由于全国地图和省地图都要触发这个事  件，所以要判断是省还是地级市
        		   //双击省份
        		   var areaId =  caseRegionData[param.name];
    	    	   if(areaId){
    	    		    var customerTypeId = $("#customerTypeId").val();
    	 				var owner = $("#owner").val();
    	 				var areaIdAndType = areaId+"@0";
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
    	 	    	    window.self.location=otherUrl;
    	    	   }
        	   }else if(param.seriesIndex ==1 && (seriesName.trim()=="客户数"||seriesName.trim()=="")){//由于全国地图和省地图都要触发这个事 件，所以要判断是省还是地级市
        		   //双击市县
        		   var areaId =  caseAreaData[param.name];
    	    	   if(areaId){
    	    		    var customerTypeId = $("#customerTypeId").val();
    	 				var owner = $("#owner").val();
    	 				var areaIdAndType = areaId+"@0";
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
    	    	   }
        	   }
        	});
    	  
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
    				if(owner && ownerName){//有选择人员
    					userOneForUserId(owner,ownerName,'','${param.sid}','owner','');
    				}else{//默认当前人员
    					userOneForUserId('${userInfo.id}','${userInfo.userName}','','${param.sid}','owner','')
    				}
    			}else{
    				userOneForUserIdCallBack(ownerId,"",ownerName,"");
    			}
    		})
    		
   		function userMoreForUserIdCallBack(options,userIdtag){
			$("#"+userIdtag).val('');
			returnListOwner(options)
			 //缓存各个省市的数据
			caseData = {};
		    //缓存各个市区的名称对应主键
			caseAreaData = {};
		    //缓存省份对应区域主键
			caseRegionData = {};
			 var viewState = $("#changeView").attr("viewState");
			 var loadingIndex = -1;
 			 if("A"==viewState){
 				 myChart.showLoading({
 		             text : '数据获取中',
 		             effect: 'whirling',
 		             zlevel: 100
 		         });
 			 }else if("B"==viewState){
 				//启动加载页面效果
 				loadingIndex = layer.load(0, {shade: [0.6,'#fff']});
 			 }
			getProvinceTotal(function(regionArray){
				option.series[0].data = regionArray;
				option.series.splice(1);
		        option.legend = null;
		        option.dataRange = null;
		        option.legend = {
		   	            x:'right',
		   	            data:['客户数']
		   	        };
	   	     	    option.dataRange = {
	   	     	        orient: 'horizontal',
	   	     	        x: 'right',
	   	     	        min: 0,
	   	     	        max: 100,
	   	     	        color:['orange','green'],
	   	     	        text:['高','低'],           // 文本，默认为数值文本
	   	     	        splitNumber:0
	   	     	    };
		        option.series[0].mapLocation = {
		        		x:'center',
		            	top:'10',
		            	width:'570px'
		        };
		        myChart.setOption(option, true);
	 			 if("A"==viewState){
	 				 myChart.hideLoading();
	 			 }else if("B"==viewState){
	 				 console.log(loadingIndex)
	 				//关闭加载页面效果
	 				layer.close(loadingIndex);
	 				loadingIndex = -1;
	 			 }
		       
			});
			
		}
		
		//移除筛选
		function removeChoose(userIdtag,sid,userId,ts){
			$("#"+userIdtag+"_select").find("option[value='"+userId+"']").remove();
			$("#"+userIdtag).val('');
			$(ts).remove();
			
			 //缓存各个省市的数据
			caseData = {};
		    //缓存各个市区的名称对应主键
			caseAreaData = {};
		    //缓存省份对应区域主键
			caseRegionData = {};
			 var viewState = $("#changeView").attr("viewState");
			 var loadingIndex = -1;
 			 if("A"==viewState){
 				 myChart.showLoading({
 		             text : '数据获取中',
 		             effect: 'whirling',
 		             zlevel: 100
 		         });
 			 }else if("B"==viewState){
 				//启动加载页面效果
 				loadingIndex = layer.load(0, {shade: [0.6,'#fff']});
 			 }
			getProvinceTotal(function(regionArray){
				option.series[0].data = regionArray;
				option.series.splice(1);
		        option.legend = null;
		        option.dataRange = null;
		        option.legend = {
		   	            x:'right',
		   	            data:['客户数']
		   	        };
	   	     	    option.dataRange = {
	   	     	        orient: 'horizontal',
	   	     	        x: 'right',
	   	     	        min: 0,
	   	     	        max: 100,
	   	     	        color:['orange','green'],
	   	     	        text:['高','低'],           // 文本，默认为数值文本
	   	     	        splitNumber:0
	   	     	    };
		        option.series[0].mapLocation = {
		        		x:'center',
		            	top:'10',
		            	width:'570px'
		        };
		        myChart.setOption(option, true);
	 			 if("A"==viewState){
	 				 myChart.hideLoading();
	 			 }else if("B"==viewState){
	 				 console.log(loadingIndex)
	 				//关闭加载页面效果
	 				layer.close(loadingIndex);
	 				loadingIndex = -1;
	 			 }
		       
			});
		}
    		//用户返回值用于查询
		function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
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
			 //缓存各个省市的数据
			caseData = {};
		    //缓存各个市区的名称对应主键
			caseAreaData = {};
		    //缓存省份对应区域主键
			caseRegionData = {};
			 var viewState = $("#changeView").attr("viewState");
			 var loadingIndex = -1;
 			 if("A"==viewState){
 				 myChart.showLoading({
 		             text : '数据获取中',
 		             effect: 'whirling',
 		             zlevel: 100
 		         });
 			 }else if("B"==viewState){
 				//启动加载页面效果
 				loadingIndex = layer.load(0, {shade: [0.6,'#fff']});
 			 }
			getProvinceTotal(function(regionArray){
				option.series[0].data = regionArray;
				option.series.splice(1);
		        option.legend = null;
		        option.dataRange = null;
		        option.legend = {
		   	            x:'right',
		   	            data:['客户数']
		   	        };
	   	     	    option.dataRange = {
	   	     	        orient: 'horizontal',
	   	     	        x: 'right',
	   	     	        min: 0,
	   	     	        max: 100,
	   	     	        color:['orange','green'],
	   	     	        text:['高','低'],           // 文本，默认为数值文本
	   	     	        splitNumber:0
	   	     	    };
		        option.series[0].mapLocation = {
		        		x:'center',
		            	top:'10',
		            	width:'570px'
		        };
		        myChart.setOption(option, true);
	 			 if("A"==viewState){
	 				 myChart.hideLoading();
	 			 }else if("B"==viewState){
	 				 console.log(loadingIndex)
	 				//关闭加载页面效果
	 				layer.close(loadingIndex);
	 				loadingIndex = -1;
	 			 }
		       
			});
			
		}
          //初始化列表视图
          function initTable(data){
        	  /***************表格布局区域******************/
        	  //清空表格数据
		       var table = $("#tableView").find("table")
	    	   $(table).html('');
	    	   
        	  //表格头部开始
	    	   var thead = $("<thead></thead>");
	    	   var headTr = $("<tr></tr>");
		    	   
   		   		var headTh = $("<th style='text-align:center;width:15;font-size:13px;font-weight:bold'>省份</th>");
	    	   $(headTr).append(headTh)
   		   		headTh = $("<th style='text-align: center;width:5;font-size:13px;font-weight:bold'>数量</th>");
	    	   $(headTr).append(headTh)
	    	   for(var i=0;i<3;i++){
	    		   var headTh = $("<th style='text-align: center;width:35;font-size:13px;font-weight:bold'>县市区</th>");
		    	   $(headTr).append(headTh)
	    		   headTh = $("<th style='text-align: center;width:5;font-size:13px;font-weight:bold'>数量</th>");
		    	   $(headTr).append(headTh)
	    	   }
	    	   
	    	   $(thead).append(headTr);
	    	   $(table).append(thead)
	    	   //表格头部结束
	    	   //表格内容开始
	    	   var tbody = $("<tbody></tbody>");
		    	/***************表格布局区域******************/ 
		    	$.each(data.listBusReport,function(index,vo){
    		    		//取得省市的子元素集合
    		    		var childModStaticVo = vo.childModStaticVo;
    		    		//表格省的位置存放
    		    		var provenceTr = $("<tr></tr>");
    		    		//省份的名字
    		    		var provenceNameTd = $("<td style='font-size:12px;' provinceId='"+vo.type+"' isParent='1'>"+vo.name+"</td>");
    		    		
    		    		var li = $('<li><a href="javascript:void(0);" provinceId="'+vo.type+'" class="selectArea" >'+vo.name+'</a></li>');
    		    		$("#areaListUl").append(li)
    		    		//省份的客户数
    		    		var provenceValueTd = $("<td style='font-size:12px;text-align:center;' provinceId='"+vo.type+"' isParent='1'><a href='javascript:void(0)' areaId='"+vo.type+"'>"+vo.value+"</a></td>");
    		    		if(vo.value){//省份有数据
	    		    		$(provenceTr).append(provenceNameTd);
	    		    		$(provenceTr).append(provenceValueTd);
	    		    		
	    		    		$(tbody).append(provenceTr)
	    		    		
	    		    		 var tempData = new Array();
	    		    		//有效子元素的个数，默认从0 开始
	    		    		 var rowspanNum = 0;
		    	    		 $.each(childModStaticVo,function(index,childVo){
			    	    		 if(childVo.value){//子元素有客户数
			    	    			 //列数
				    	    		 var y = rowspanNum % 3;
			    	    		     //行数
		    		   				 var x = (rowspanNum - y)/3;
		    		   				 //设定行数
		    		   				 var cityTr = $(tbody).find("tr[provinceId='"+vo.type+"'][rownum='"+x+"']");
		    		   				 
		    		   				 if(x==0){//第一行需添加到第一个tr中
		    		   					 var p = $(tbody).find("td[provinceId='"+vo.type+"'][isParent='1']").parent();
		    		   					 var cityNameTd = $("<td style='font-size:12px;'> "+childVo.name+"</td>");
					    		    	 var cityValueTd = $("<td style='font-size:12px;text-align:center;'><a href='javascript:void(0)' areaId='"+childVo.type+"' onclick = >"+childVo.value+"</a></td>");
					    		    	 $(p).attr("provinceId",vo.type)
					    		    	 $(p).append(cityNameTd)
					    		    	 $(p).append(cityValueTd)
		    		   				 }else{//否则重新整一行
			    		   				 if(cityTr.length==0){//该行数没有数据，重新整一行
			    		   					cityTr = $("<tr provinceId='"+vo.type+"' rownum='"+x+"'></tr>");
			    		   				 	$(tbody).append(cityTr);
						    	    		 
			    		   				 }
		    		   				 	//添加数据
		    		   					var cityNameTd = $("<td style='font-size:12px;'>"+childVo.name+"</td>");
					    		    	var cityValueTd = $("<td style='font-size:12px;text-align:center;'><a href='javascript:void(0)' areaId='"+childVo.type+"'>"+childVo.value+"</a></td>");
					    		    	 
					    		    	 $(cityTr).append(cityNameTd);
					    		    	 $(cityTr).append(cityValueTd);
		    		   				 }
				    		    	 rowspanNum++;
			    	    		 }
		    	    		 })
		    	    		 if(rowspanNum<=3){//显示数据的子元素不足3个，添加到第一行
		    	    			 for(var i = rowspanNum ;i<3;i++){
		    	    				 var p = $(tbody).find("td[provinceId='"+vo.type+"'][isParent='1']").parent();
	    		   					 var cityNameTd = $("<td style='font-size:12px;'></td>");
				    		    	 var cityValueTd = $("<td style='font-size:12px;text-align:center;'></td>");
				    		    	 $(p).append(cityNameTd)
				    		    	 $(p).append(cityValueTd)
		    	    			 }
		    	    		 }else{
		    	    			 //有效列数
			    	    		 var y = rowspanNum % 3;
		    	    			 //有效行数
			    	  		     var x = (rowspanNum - y)/3;
			    	  		     if(y){//数据没有满一行
				    	  		   for(var i = y ;i<3;i++){//填充数据
										var cityTr = $(tbody).find("tr[provinceId='"+vo.type+"'][rownum='"+x+"']");
										if(cityTr.length == 0){
											cityTr = $("<tr provinceId='"+vo.type+"' rownum='"+x+"'></tr>");
											$(tbody).append(cityTr);
										}
										var cityNameTd = $("<td  style='font-size:12px;' class=aa></td>");
										var cityValueTd = $("<td style='font-size:12px;text-align:center;' class=aa></td>");
			
										$(cityTr).append(cityNameTd);
										$(cityTr).append(cityValueTd);
				    			   }
				    	  		   $(tbody).find("td[provinceId='"+vo.type+"'][isParent='1']").attr("rowspan",(x+1))
			    	  		     }else{
				    	  		   $(tbody).find("td[provinceId='"+vo.type+"'][isParent='1']").attr("rowspan",(x))
			    	  		     }
		    	    		 }
    		    		}
    		    		
	    	  		   
    		    	});
    		    	$(table).append(tbody)
    		    	
    		    	 //表格内容结束
          }
          //表格穿透
          $("#tableView").on("click","a",function(){
        	  var options = $("#owner_select").find("option");
  			var listOwner = returnListOwner(options);
  			var listOwnerStr ='';
  			if(listOwner.length>0){
  				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
  			}
		   		var areaId = $(this).attr("areaId");
    		    var customerTypeId = $("#customerTypeId").val();
 				var owner = $("#owner").val();
 				var areaIdAndType = areaId+"@0";
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
  				 $("#areaDiv").show();
  			 }else if("B"==viewState){
  				 $("#changeView").attr("viewState","A");
  				 $("#changeView").html("列表视图");
  				 $("#chartView").show();
  				  $("#excelExport").hide()
 
  				 $("#tableView").hide();
  				 $("#areaDiv").hide();
  			 }
  		})
  		
  		//客户区域选择
		$(document).on("click","#areaDiv .selectArea",function(){
			var provinceId = $(this).attr("provinceId");
			if(provinceId && provinceId>0){//点击选择弹窗数据
				var html = '<font style="font-weight:bold;">'+$(this).text()+'</font>';
				html +='<i class="fa fa-angle-down"></i>';
				$("#areaNameA").html(html);
				$("#dataTable").find("tbody").find("tr").hide();
				$("#dataTable").find("tbody").find("tr[provinceId='"+provinceId+"']").show();
			}else{//所有数据
				var html = '区域筛选';
				html +='<i class="fa fa-angle-down"></i>';
				$("#areaNameA").html(html);
				$("#dataTable").find("tbody").find("tr").show();
			}
		});
  		
  		//查询时间区间
		$(document).on("click","#moreCondition_Div .search",function(){
			//缓存各个省市的数据
			caseData = {};
		    //缓存各个市区的名称对应主键
			caseAreaData = {};
		    //缓存省份对应区域主键
			caseRegionData = {};
			 var viewState = $("#changeView").attr("viewState");
			 var loadingIndex = -1;
 			 if("A"==viewState){
 				 myChart.showLoading({
 		             text : '数据获取中',
 		             effect: 'whirling',
 		             zlevel: 100
 		         });
 			 }else if("B"==viewState){
 				//启动加载页面效果
 				loadingIndex = layer.load(0, {shade: [0.6,'#fff']});
 			 }
			getProvinceTotal(function(regionArray){
				option.series[0].data = regionArray;
				option.series.splice(1);
		        option.legend = null;
		        option.dataRange = null;
		        option.legend = {
		   	            x:'right',
		   	            data:['客户数']
		   	        };
	   	     	    option.dataRange = {
	   	     	        orient: 'horizontal',
	   	     	        x: 'right',
	   	     	        min: 0,
	   	     	        max: 100,
	   	     	        color:['orange','green'],
	   	     	        text:['高','低'],           // 文本，默认为数值文本
	   	     	        splitNumber:0
	   	     	    };
		        option.series[0].mapLocation = {
		        		x:'center',
		            	top:'10',
		            	width:'570px'
		        };
		        myChart.setOption(option, true);
	 			 if("A"==viewState){
	 				 myChart.hideLoading();
	 			 }else if("B"==viewState){
	 				 console.log(loadingIndex)
	 				//关闭加载页面效果
	 				layer.close(loadingIndex);
	 				loadingIndex = -1;
	 			 }
		       
			});
		});
		//查询时间区间
		$(document).on("click","#moreCondition_Div .reset",function(){
			$("#startDate").val("");
			$("#endDate").val("");
			
			//缓存各个省市的数据
			caseData = {};
		    //缓存各个市区的名称对应主键
			caseAreaData = {};
		    //缓存省份对应区域主键
			caseRegionData = {};
			 var viewState = $("#changeView").attr("viewState");
			 var loadingIndex = -1;
 			 if("A"==viewState){
 				 myChart.showLoading({
 		             text : '数据获取中',
 		             effect: 'whirling',
 		             zlevel: 100
 		         });
 			 }else if("B"==viewState){
 				//启动加载页面效果
 				loadingIndex = layer.load(0, {shade: [0.6,'#fff']});
 			 }
			getProvinceTotal(function(regionArray){
				option.series[0].data = regionArray;
				option.series.splice(1);
		        option.legend = null;
		        option.dataRange = null;
		        option.legend = {
		   	            x:'right',
		   	            data:['客户数']
		   	        };
	   	     	    option.dataRange = {
	   	     	        orient: 'horizontal',
	   	     	        x: 'right',
	   	     	        min: 0,
	   	     	        max: 100,
	   	     	        color:['orange','green'],
	   	     	        text:['高','低'],           // 文本，默认为数值文本
	   	     	        splitNumber:0
	   	     	    };
		        option.series[0].mapLocation = {
		        		x:'center',
		            	top:'10',
		            	width:'570px'
		        };
		        myChart.setOption(option, true);
	 			 if("A"==viewState){
	 				 myChart.hideLoading();
	 			 }else if("B"==viewState){
	 				//关闭加载页面效果
	 				layer.close(loadingIndex);
	 				loadingIndex = -1;
	 			 }
		       
			});
		});
    </script>
</html>

