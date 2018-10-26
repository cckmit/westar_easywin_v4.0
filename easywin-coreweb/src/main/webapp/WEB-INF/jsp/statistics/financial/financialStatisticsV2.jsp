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
										
										<div>
											<form id="searchForm" action="/statistics/trip/listtripStatics" class="subform">
												<input type="hidden" name="flowState" id="flowState" />
												<input type="hidden" name="spState" id="spState" />
											
												<div class="searchCond" style="display: block">
													<div class="btn-group pull-left">
														<div class="table-toolbar ps-margin">
															<div class="btn-group" id="flowStateDiv">
																<a class="btn btn-default dropdown-toggle btn-xs" id="flowStateDis" data-toggle="dropdown">
																	流程状态筛选 <i class="fa fa-angle-down"></i>
																</a>
																<ul class="dropdown-menu dropdown-default">
																	<li>
																		<a href="javascript:void(0)" class="flowState" flowState="-1">全部状态</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="flowState" flowState="1">审批中的</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="flowState" flowState="4">已完成的</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="flowState" flowState="2">待发起的</a>
																	</li>
																</ul>
															</div>
														</div>
														<div class="table-toolbar ps-margin">
															<div class="btn-group" id="spStateDiv">
																<a class="btn btn-default dropdown-toggle btn-xs" id="spStateDis" data-toggle="dropdown">
																	审批结果筛选 <i class="fa fa-angle-down"></i>
																</a>
																<ul class="dropdown-menu dropdown-default">
																	<li>
																		<a href="javascript:void(0)" class="spState" spState="-1">全部状态</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="spState" spState="0">驳回</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="spState" spState="1">通过</a>
																	</li>
																</ul>
															</div>
														</div>
														<div class="table-toolbar ps-margin">
															<div class="btn-group">
																<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
																	发起人筛选
																	<i class="fa fa-angle-down"></i>
																</a>
																<ul class="dropdown-menu dropdown-default">
																	<li>
																		<a href="javascript:void(0)" class="creatorMoreElementClean">不限条件</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="creatorMoreElementSelect">人员选择</a>
																	</li>
																</ul>
															</div>
														</div>
														<div style="float: left;width: 250px;display: none">
															<select list="listCreator" listkey="id" listvalue="creatorName" id="creator_select" name="listCreator.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
															</select>
														</div>
														
														<div class="table-toolbar ps-margin">
															<div class="btn-group">
																<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
																	审批人筛选
																	<i class="fa fa-angle-down"></i>
																</a>
																<ul class="dropdown-menu dropdown-default">
																	<li>
																		<a href="javascript:void(0)" class="executorMoreElementClean">不限条件</a>
																	</li>
																	<li>
																		<a href="javascript:void(0)" class="executorMoreElementSelect">人员选择</a>
																	</li>
																</ul>
															</div>
														</div>
														<div style="float: left;width: 250px;display: none">
															<select list="listExecutor" listkey="id" listvalue="executorName" id="executor_select" name="listExecutor.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
															</select>
														</div>
			
														<div class="table-toolbar ps-margin">
															<div class="btn-group cond" id="moreCondition_Div">
																<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
																	<span>更多</span>
																	<i class="fa fa-angle-down"></i>
																</a>
																<div class="dropdown-menu dropdown-default padding-bottom-10 moreConsSelect" style="min-width: 330px;">
																	<div class="ps-margin ps-search padding-left-10">
																		<span class="btn-xs">起止时间：</span>
																		<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="" id="startDate" name="startDate" placeholder="开始时间"
																			onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
																		<span>~</span>
																		<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="" id="endDate" name="endDate" placeholder="结束时间"
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
															<input id="applyName" class="form-control ps-input" name="applyName" type="text" placeholder="请输入关键字" value="">
															<a href="javascript:void(0)" class="ps-searchBtn" id="applyNamea">
																<i class="glyphicon glyphicon-search circular danger"></i>
															</a>
														</span>
													</div>
												</div>
			
											</form>
											<div class="widget-buttons ps-widget-buttons">
												<button class="btn btn-info  btn-xs" onclick="excelExport('项目任务统计表')" >导出excel</button>
											</div>
										</div>
										<div class=" padding-top-10 text-left creatorDisDiv" style="display:none">
											<strong>发起人筛选:</strong>
										</div>
										<div class=" padding-top-10 text-left executorDisDiv" style="display:none">
											<strong>审批人筛选:</strong>
										</div>
                               		 </div>
	                                <div class="widget-body">
										<div class="widget-body" id="tableView" style="display: block">
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
	<script type="text/javascript">
	    var url = '/financial/financialStatisticsV2?sid=${param.sid}';
	    var pages = 0;//统计数据页数
	    var offset= 0;//当前页码
	    //全局变量配置项
	    var userInfo="";
	    //启动加载页面效果
		window.top.layer.load(0, {shade: [0.6,'#fff'],time:30*1000});
	    //异步取得数据
        getSelfJSON(url,null,function (data) {
           userInfo = data.userInfo;
    	   initTable(data);
    	});
	    
	    $(function(){
	    	//人员多选
	    	$("body").off("click",".creatorMoreElementSelect").on("click",".creatorMoreElementSelect",function(){
	    		userMore(null, null, sid,'yes',null,function(options){
	    			$("#creator_select").html('');
	    			if(options && options.length>0){
	    				$.each(options,function(index,object){
	    					var option = $("<option value='"+$(object).val()+"'>"+$(object).text()+"</option>");
	    					$("#creator_select").append(option)
	    				})
	    				creatorNameDisplay();//筛选责任人名称显示
	    				dataFilter();//数据筛选
	    			}
	    		})
	    	});
	    	//清空责任人筛选条件
	    	$("body").off("click",".creatorMoreElementClean").on("click",".creatorMoreElementClean",function(){
	    		$("#creator_select").find("option").remove();
				creatorNameDisplay();//筛选责任人名称显示
				dataFilter();//数据筛选
				$(".creatorDisDiv").css("display","none");
	    	});
	    	//审批人筛选
	    	$("body").off("click",".executorMoreElementSelect").on("click",".executorMoreElementSelect",function(){
	    		userMore(null, null, sid,'yes',null,function(options){
	    			$("#executor_select").html('');
	    			if(options && options.length>0){
	    				$.each(options,function(index,object){
	    					var option = $("<option value='"+$(object).val()+"'>"+$(object).text()+"</option>");
	    					$("#executor_select").append(option)
	    				})
	    				executorNameDisplay();//筛选审批人名称显示
	    				dataFilter();//数据筛选
	    			}
	    		})
	    	});
	    	//清空审批人筛选条件
	    	$("body").off("click",".executorMoreElementClean").on("click",".executorMoreElementClean",function(){
	    		$("#executor_select").find("option").remove();
	    		executorNameDisplay();//筛选责任人名称显示
				dataFilter();//数据筛选
				$(".executorDisDiv").css("display","none");
	    	});
	    	//名称筛选
	    	$("#applyName").blur(function(){
	    		dataFilter();//数据筛选
	    	});
	    	//文本框绑定回车提交事件
	    	$("#applyName").bind("keydown",function(event){
	            if(event.keyCode == "13"){
	            	dataFilter();//数据筛选
	            }
	        });
	    	//名称筛选文本框后的A标签点击事件绑定
	    	$("body").off("click","#applyNamea").on("click","#applyNamea",function(){
	    		dataFilter();//数据筛选
	    	});
	    	//点击事件绑定
	    	$("body").off("click",".clickClass").on("click",".clickClass",function(){
	    		clickEventBinding($(this),userInfo);//数据筛选
	    	});
	    });
        //流程状态筛选
		$(document).on("click","#flowStateDiv .flowState",function(){
			var flowState = $(this).attr("flowState");
			$("#searchForm [name='flowState']").val(flowState);
			if(flowState>0){
				var typeName = $(this).text();
				var html = '<font style="font-weight:bold;">'+typeName+'</font>';
				html +='<i class="fa fa-angle-down"></i>';
				$("#flowStateDis").html(html);
			}else{
				var html = '状态筛选';
				html +='<i class="fa fa-angle-down"></i>';
				$("#flowStateDis").html(html);
			}
			dataFilter();//数据筛选
		})
		
		//审批状态筛选
		$(document).on("click","#spStateDiv .spState",function(){
			var spState = $(this).attr("spState");
			$("#searchForm [name='spState']").val(spState);
			if(spState>=0){
				var typeName = $(this).text();
				var html = '<font style="font-weight:bold;">'+typeName+'</font>';
				html +='<i class="fa fa-angle-down"></i>';
				$("#spStateDis").html(html);
			}else{
				var html = '状态筛选';
				html +='<i class="fa fa-angle-down"></i>';
				$("#spStateDis").html(html);
			}
			dataFilter();//数据筛选
		})
		
		//查询起始时间筛选
		$(document).on("click","#moreCondition_Div .search",function(){
			dataFilter();//列表筛选
			$("#moreCondition_Div").removeClass("open");
			if($("#startDate").val() || $("#endDate").val()){
				$("#moreCondition_Div a").children("span").remove();
				$("#moreCondition_Div a").append($("<span><font style=\"font-weight:bold;\">筛选中</font></span>"));
			}else{
				$("#moreCondition_Div a").children("span").remove();
				$("#moreCondition_Div a").append($("<span>更多</span>"));
			}
		});
        //充值查询时间
		$(document).on("click","#moreCondition_Div .reset",function(){
		    $("#startDate").val("");
		    $("#endDate").val("");
		});
		
		//数据筛选
		function dataFilter(){
			$("#tableView").find("table tbody tr").hide();
			$("#tableView").find("table tbody tr").filter(flowStateFilter).filter(spStateFilter).
			filter(creatorFilter).filter(executorFilter).
			filter(dateTimeFilter).filter(nameFilter).show();
			reSetTableRowNum();//列表行号排序
        }
		//项目名称筛选
        function nameFilter(index,obj){
        	var tag =false;
        	if($("#applyName").val()){
            	if($(obj).attr("applyName").indexOf($("#applyName").val())>-1){
            		tag =true;
            	}
        	}else{
        		tag =true;
        	}
			return tag;
        }
        //创建时间筛选
        function dateTimeFilter(index,obj){
        	var tag =false;
			if($("#startDate").val() || $("#endDate").val()){
				var startTime=$("#startDate").val();  
			    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
			    var endTime=$("#endDate").val();  
			    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
			    var recordCreateTime = new Date($(obj).attr("recordCreateTime").replace("-", "/").replace("-", "/"));
			    if($("#startDate").val() && $("#endDate").val()){
			    	if(recordCreateTime>=start && recordCreateTime<=end){
						tag =true;
			    	}
			    }else if($("#startDate").val()){
			    	if(recordCreateTime>=start){
						tag =true;
			    	}
			    }else if($("#endDate").val()){
			    	if(recordCreateTime<=end){
						tag =true;
			    	}
			    }
			}else{
				tag =true;
			}
			return tag;
        }
        //发起人筛选
        function creatorFilter(index,obj){
        	var tag =false;
        	if($("#creator_select option").length>0){//有才筛选
				$("#creator_select option").each(function(){
					if($(obj).filter("tr[creator='"+$(this).val()+"']").length>0){
						tag=true;
	        			return false;
	        		}
				});
        	}else{//没有筛选动作
        		tag=true;
        	}
			return tag;
        }
        //审批人筛选
        function executorFilter(index,obj){
        	var tag =false;
        	if($("#executor_select option").length>0){//有才筛选
				$("#executor_select option").each(function(){
					if($(obj).filter("tr[executor='"+$(this).val()+"']").length>0){
						tag=true;
	        			return false;
	        		}
				});
        	}else{//没有筛选动作
        		tag=true;
        	}
			return tag;
        }
		//筛选责任人名称显示
		function creatorNameDisplay(){
			$(".creatorDisDiv").children("span").remove();
			if($("#creator_select option").length>0){//有才筛选
				$(".creatorDisDiv").css("display","block");
				$("#creator_select option").each(function(){
					var creatorName = $(this).text();
					var userId = $(this).val();
					var span = $("<span></span>");
					$(span).attr("title","双击移除");
					$(span).css("cursor","pointer");
					$(span).addClass("label label-default margin-right-5 margin-bottom-5");
					$(span).text(creatorName);
					$(span).dblclick(function(){
						removerUser(userId);//清除筛选人
					});//双击事件绑定
					$(".creatorDisDiv").append(span);
				});
        	}
        }
		//清除筛选人
		function removerUser(userId){
			$("#creator_select").find("option[value='"+userId+"']").remove();
			creatorNameDisplay();//筛选责任人名称显示
			dataFilter();//数据筛选
			if($("#creator_select option").length<1){//无责任人筛选
				$(".creatorDisDiv").css("display","none");
			}
		}
		
		//筛选审批人名称显示
		function executorNameDisplay(){
			$(".executorDisDiv").children("span").remove();
			if($("#executor_select option").length>0){//有才筛选
				$(".executorDisDiv").css("display","block");
				$("#executor_select option").each(function(){
					var creatorName = $(this).text();
					var userId = $(this).val();
					var span = $("<span></span>");
					$(span).attr("title","双击移除");
					$(span).css("cursor","pointer");
					$(span).addClass("label label-default margin-right-5 margin-bottom-5");
					$(span).text(creatorName);
					$(span).dblclick(function(){
						removerExecutor(userId);//清除筛选人
					});//双击事件绑定
					$(".executorDisDiv").append(span);
				});
        	}
        }
		//清除审批筛选人
		function removerExecutor(userId){
			$("#executor_select").find("option[value='"+userId+"']").remove();
			executorNameDisplay();//筛选审批人名称显示
			dataFilter();//数据筛选
			if($("#executor_select option").length<1){//无责任人筛选
				$(".executorDisDiv").css("display","none");
			}
		}
		
        //流程状态筛选
        function flowStateFilter(index,obj){
        	var flowState = $("#searchForm [name='flowState']").val();
        	var tag = false;
        	if(flowState && (flowState>0)){
        		if($(obj).filter("tr[flowState='"+flowState+"']").length>0){
        			tag = true;
        		}
			}else{
				tag = true;
			}
        	return tag;
        }
        
        //审批状态筛选
        function spStateFilter(index,obj){
        	var spState = $("#searchForm [name='spState']").val();
        	var tag = false;
        	if(spState && (spState>=0)){
        		if($(obj).filter("tr[spState='"+spState+"']").length>0){
        			tag = true;
        		}
			}else{
				tag = true;
			}
        	return tag;
        }
		
      //初始化表格视图
        function initTable(data){
     	   var table = $("#tableView").find("table")
     	   $(table).html('');
     	   var thead = $("<thead></thead>");
     	   var headTr = $("<tr></tr>");
     	   var headFirstTh = "<th style='text-align: center;font-weight:bold;'>序号</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>姓名</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>申请记录</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>开始时间</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>结束时间</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>预算金额</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;'>审批结果</th>";
     	   headFirstTh += "<th style='text-align: center;font-weight:bold;' colspan=4>借款明细</th>";
     	   $(headTr).append(headFirstTh)
     	   $(thead).append(headTr);
     	   $(table).append(thead);
    	   var tbody = $("<tbody></tbody>");
     	   $(table).append(tbody);
     	   trAppend(data);//数据行加载
 		   //countTotal(table,tbody);
     	   pages = Math.ceil(data.totalCount/data.pageSize);
     	   dataGet(data.pageSize);//持续追加数据
        }
      //数据列追加
      function trAppend(data){
    	   var tbody = $("#tableView").find("table tbody");
    	   var listLoanApplyOfAuth = data.listLoanApplyOfAuth;
    	   var userInfo = data.userInfo;
    	   $.each(listLoanApplyOfAuth,function(index,apply){
			   var tr = $("<tr dataRow='1' flowState='"+apply.flowState+"' spState='"+apply.spState+"' executor='"+apply.executor+"' creator='"+apply.creator+"' recordCreateTime='"+apply.recordCreateTime+"' applyName='"+apply.flowName+"'></tr>");
			   var td1 = $("<td applyId='"+apply.id+"' isParent='1' style='font-size:12px;text-align: center;'>"+(index+1)+"</td>");
			   var td2 = $("<td applyId='"+apply.id+"' isParent='1' style='font-size:12px;text-align: center;'>"+apply.creatorName+"</td>");
			   var td3 = $("<td applyId='"+apply.id+"' isParent='1' style='font-size:12px;text-align: center;'><a href='javascript:void(0)' onclick ='viewSpFlow("+apply.instanceId+");' title='详情查看'>"+apply.flowName+"</a></td>");
			   var td4 = $("<td applyId='"+apply.id+"' isParent='1' style='font-size:12px;text-align: center;'>"+(strIsNull(apply.startTime)?'/':apply.startTime)+"</td>");
			   var td5 = $("<td applyId='"+apply.id+"' isParent='1' style='font-size:12px;text-align: center;'>"+(strIsNull(apply.endTime)?'/':apply.endTime)+"</td>");
			   var td6 = $("<td applyId='"+apply.id+"' isParent='1' style='font-size:12px;text-align: center;'>"+(strIsNull(apply.allowedQuota)?'/':(apply.allowedQuota+"（元）"))+"</td>");
			   //出差申请审批记录状态
			   var stateInfo = tripState(apply);
			   var td7 = $("<td applyId='"+apply.id+"' isParent='1' style='font-size:12px;text-align:center;color:"+stateInfo.color+";font-weight:bold;'>"+stateInfo.flowSate+"</td>");
			   $(tr).append(td1);
			   $(tr).append(td2);
			   $(tr).append(td3);
			   $(tr).append(td4);
			   $(tr).append(td5);
			   $(tr).append(td6);
			   $(tr).append(td7);
			   $(tbody).append(tr);
	    		//有效子元素的个数，默认从0 开始
	    	   var listLoan = apply.listLoan;
	    	   if(strIsNull(listLoan)){//未借款
	    		   var td9 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;color:red;font-weight:bold;' colspan=4>未借款</td>");
				   $(tr).append(td9);
	    	   }else if(listLoan.length==1){//一次借款记录
				   //借款申请审批记录状态
				   var stateInfo = loanSate(listLoan[0],userInfo);
	    		   if(listLoan[0].flowState==4 && listLoan[0].spState==1){//
		    		   var td9 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;'colspan=2><a href='javascript:void(0)' class='clickClass' title='详情查看'>借款：<span style='color:red;font-weight:bold;'>"+listLoan[0].borrowingBalance+"</span>（元）</a></td>");
					   $(tr).append(td9);
					   var writeOff =(listLoan[0].writeOffStatus == 4 
								&& listLoan[0].writeOffFlowSpState == 1 
								&& listLoan[0].writeOffFlowState == 4);//是否销账
		    		   var td10 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;font-weight:bold;' colspan="+(writeOff?1:2)+"><a href='javascript:void(0)' class='clickClass "+(writeOff?'':'otherClass')+"'><span style='color:"+stateInfo.color+";'>"+stateInfo.loanFlowSate+"</span></a></td>");
					   $(tr).append(td10);
					   if(writeOff){//完成销账的
			    		   var td11 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;' colspan=2><a href='javascript:void(0)' class='clickClass writeOffClass' title='详情查看'>销账：<span style='color:red;font-weight:bold;'>"+listLoan[0].writeOffBalance+"</span>（元）</a></td>");
						   $(tr).append(td11);
					   }
	    		   }else{
		    		   var td9 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;' colspan=2><a href='javascript:void(0)' class='clickClass' title='详情查看'>"+listLoan[0].flowName+"</a></td>");
					   $(tr).append(td9);
		    		   var td10 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;font-weight:bold;' colspan=2><span style='color:"+stateInfo.color+";'>"+stateInfo.loanFlowSate+"</span></td>");
					   $(tr).append(td10);
	    		   }
	    		   $(tr).data("loanData",listLoan[0]); //数据对象绑定
	    		   
	    	   }else if(listLoan.length>1){//多次借款记录
	    		   $.each(listLoan,function(loanIndex,loan){
		   	  		   $(tbody).find("td[applyId='"+apply.id+"'][isParent='1']").attr("rowspan",(listLoan.length));
			   	  		 //借款申请审批记录状态
						 var stateInfo = loanSate(loan,userInfo);
		  				 if(loanIndex==0){//第一行需添加到第一个tr中
		  					 var p = $(tbody).find("td[applyId='"+apply.id+"'][isParent='1']").parent();
		  					 var td2 = $("<td style='font-size:12px;text-align: center;'>"+(loanIndex+1)+"</td>");
			   		    	 $(p).append(td2);
		   		    		if(loan.flowState==4 && loan.spState==1){//已完成销账
				    		   var td9 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;'><a href='javascript:void(0)' class='clickClass' title='详情查看'>借款：<span style='color:red;font-weight:bold;'>"+loan.borrowingBalance+"</span>（元）</a></td>");
				    		   $(p).append(td9);
							   var writeOff =(loan.writeOffStatus == 4 
										&& loan.writeOffFlowSpState == 1 
										&& loan.writeOffFlowState == 4);//是否销账
				    		   var td10 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;font-weight:bold;' colspan="+(writeOff?1:2)+"><a href='javascript:void(0)' class='clickClass "+(writeOff?'':'otherClass')+"'><span style='color:"+stateInfo.color+";'>"+stateInfo.loanFlowSate+"</span></a></td>");
				    		   $(p).append(td10);
							   if(writeOff){//完成销账的
					    		   var td11 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;'><a href='javascript:void(0)' class='clickClass writeOffClass' title='详情查看'>销账：<span style='color:red;font-weight:bold;'>"+loan.writeOffBalance+"</span>（元）</a></td>");
					    		   $(p).append(td11);
							   }
			    		   }else{
				    		   var td9 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;'><a href='javascript:void(0)' class='clickClass' title='详情查看'>"+loan.flowName+"</a></td>");
				    		   $(p).append(td9);
				    		   var td10 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;font-weight:bold;' colspan=2><span style='color:"+stateInfo.color+";'>"+stateInfo.loanFlowSate+"<span></td>");
				    		   $(p).append(td10);
			    		   }
		   		    		$(p).data("loanData",loan); //数据对象绑定
		  				 }else{//否则重新整一行
		  					var newTr = $("<tr flowState='"+apply.flowState+"' spState='"+apply.spState+"' executor='"+apply.executor+"' creator='"+apply.creator+"' recordCreateTime='"+apply.recordCreateTime+"' applyName='"+apply.flowName+"'></tr>");
		  				 	$(tbody).append(newTr);
		  				 	//添加数据
		  					var td2 = $("<td style='font-size:12px;text-align: center;'>"+(loanIndex+1)+"</td>");
			   		    	$(newTr).append(td2);
		  					if(loan.flowState==4 && loan.spState==1){//已完成销账
				    		   var td9 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;'><a href='javascript:void(0)' class='clickClass' title='详情查看'>借款：<span style='color:red;font-weight:bold;'>"+loan.borrowingBalance+"</span>（元）</a></td>");
				    		   $(newTr).append(td9);
							   var writeOff =(loan.writeOffStatus == 4 
										&& loan.writeOffFlowSpState == 1 
										&& loan.writeOffFlowState == 4);//是否销账
				    		   var td10 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;font-weight:bold;' colspan="+(writeOff?1:2)+"><a href='javascript:void(0)' class='clickClass "+(writeOff?'':'otherClass')+"'><span style='color:"+stateInfo.color+";'>"+stateInfo.loanFlowSate+"</span></a></td>");
				    		   $(newTr).append(td10);
							   if(writeOff){//完成销账的
					    		   var td11 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;'><a href='javascript:void(0)' class='clickClass writeOffClass' title='详情查看'>销账：<span style='color:red;font-weight:bold;'>"+loan.writeOffBalance+"</span>（元）</a></td>");
					    		   $(newTr).append(td11);
							   }
			    		   }else{
				    		   var td9 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;'><a href='javascript:void(0)' class='clickClass' title='详情查看'>"+loan.flowName+"</a></td>");
				    		   $(newTr).append(td9);
				    		   var td10 = $("<td applyId='"+apply.id+"' style='font-size:12px;text-align: center;font-weight:bold;' colspan=2><span style='color:"+stateInfo.color+";'>"+stateInfo.loanFlowSate+"</span></td>");
				    		   $(newTr).append(td10);
			    		   }
		  					$(newTr).data("loanData",loan); //数据对象绑定
		  				 }
	    		   })
	    	   }
    	   });
      }
      //持续追加数据
      function dataGet(pageSize){
    	  if(pages>1){
      		  offset +=1;
    		  $.ajax({  
			        url : url+"&pager.offset="+(offset*pageSize)+"&pager.pageSize="+pageSize,  
			        async : true, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
			        type : "POST",  
			        dataType : "json",
			        success : function(reData) {  
			        	trAppend(reData);//数据行加载
			        	if(offset<pages){
			        		dataGet(pageSize)
			        	}else{
			        		reSetTableRowNum();//列表行号排序
			        		layer.closeAll("loading");
			        	}
			        }  
		      });
    	  }else{
    		  layer.closeAll("loading");
    	  }
      }
      
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
 		
     //列表行号排序
     function reSetTableRowNum(){
    	  var rowNum=1;
	   	  $("#dataTable tr[dataRow='1']").each(function(index){
	   	     if($(this).css("display").indexOf("none")==-1){
		   	     $(this).find("td:eq(0)").text(rowNum);
		   	  	 rowNum++;
	   	     }
	   	  });
     }
       
    </script>
</html>



