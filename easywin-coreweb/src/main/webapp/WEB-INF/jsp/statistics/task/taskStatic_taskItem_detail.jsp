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
											<div>
												<span class="pull-left margin-top-10 blue" id="pageTitle" style="font-size:18px;"></span>
											</div>
											<div class="widget-buttons ps-widget-buttons">
												<button class="btn btn-info  btn-xs" onclick="excelExport('项目任务明细表')" >导出excel</button>
											</div>
										</div>
										<div class=" padding-top-10 text-left ownerDisDiv" style="display:none">
											<strong>责任人筛选:</strong>
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
	    var url = '/statistics/item/listItemTaskDetail?sid=${param.sid}&itemId=${param.itemId}';
	    var pages = 0;//统计数据页数
	    var offset= 0;//当前页码
	    //全局变量配置项
	    var option={};
	    //启动加载页面效果
		window.top.layer.load(0, {shade: [0.6,'#fff'],time:30*1000});
	    //异步取得数据
        getSelfJSON(url,null,function (data) {
           $("#pageTitle").text("项目：“"+data.item.itemName+"”——任务明细");//初始化页面title
    	   initTable(data);
    	});
	    
	    $(function(){
	    	//人员多选
	    	$("body").off("click",".userMoreElementSelect").on("click",".userMoreElementSelect",function(){
	    		userMore(null, null, sid,'yes',null,function(options){
	    			$("#owner_select").html('');
	    			if(options && options.length>0){
	    				$.each(options,function(index,object){
	    					var option = $("<option value='"+$(object).val()+"'>"+$(object).text()+"</option>");
	    					$("#owner_select").append(option)
	    				})
	    				ownerNameDisplay();//筛选责任人名称显示
	    				dataFilter();//数据筛选
	    			}
	    		})
	    	});
	    	//清空责任人筛选条件
	    	$("body").off("click",".userMoreElementClean").on("click",".userMoreElementClean",function(){
	    		$("#owner_select").find("option").remove();
				ownerNameDisplay();//筛选责任人名称显示
				dataFilter();//数据筛选
				$(".ownerDisDiv").css("display","none");
	    	});
	    	//名称筛选
	    	$("#itemName").blur(function(){
	    		dataFilter();//数据筛选
	    	});
	    	//文本框绑定回车提交事件
	    	$("#itemName").bind("keydown",function(event){
	            if(event.keyCode == "13"){
	            	dataFilter();//数据筛选
	            }
	        });
	    	//名称筛选文本框后的A标签点击事件绑定
	    	$("body").off("click","#itemNamea").on("click","#itemNamea",function(){
	    		dataFilter();//数据筛选
	    	});
	    });
        //状态筛选
		$(document).on("click","#stateDiv .state",function(){
			var state = $(this).attr("state");
			$("#searchForm [name='state']").val(state);
			if(state>0){
				var typeName = $(this).text();
				var html = '<font style="font-weight:bold;">'+typeName+'</font>';
				html +='<i class="fa fa-angle-down"></i>';
				$("#stateDis").html(html);
			}else{
				var html = '状态筛选';
				html +='<i class="fa fa-angle-down"></i>';
				$("#stateDis").html(html);
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
			$("#tableView").find("table tbody tr").filter(stateFilter).filter(ownerFilter).filter(dateTimeFilter).filter(nameFilter).show();
			reSetTableRowNum();//列表行号排序
        }
		//项目名称筛选
        function nameFilter(index,obj){
        	var tag =false;
        	if($("#itemName").val()){
            	if($(obj).attr("itemName").indexOf($("#itemName").val())>0){
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
        //项目负责人筛选
        function ownerFilter(index,obj){
        	var tag =false;
        	if($("#owner_select option").length>0){//有才筛选
				$("#owner_select option").each(function(){
					if($(obj).filter("tr[owner='"+$(this).val()+"']").length>0){
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
		function ownerNameDisplay(){
			$(".ownerDisDiv").children("span").remove();
			if($("#owner_select option").length>0){//有才筛选
				$(".ownerDisDiv").css("display","block");
				$("#owner_select option").each(function(){
					var userName = $(this).text();
					var userId = $(this).val();
					var span = $("<span></span>");
					$(span).attr("title","双击移除");
					$(span).css("cursor","pointer");
					$(span).addClass("label label-default margin-right-5 margin-bottom-5");
					$(span).text(userName);
					$(span).dblclick(function(){
						removerUser(userId);//清除筛选人
					});//双击事件绑定
					$(".ownerDisDiv").append(span);
				});
        	}
        }
		//清除筛选人
		function removerUser(userId){
			$("#owner_select").find("option[value='"+userId+"']").remove();
			ownerNameDisplay();//筛选责任人名称显示
			dataFilter();//数据筛选
			if($("#owner_select option").length<1){//无责任人筛选
				$(".ownerDisDiv").css("display","none");
			}
		}
        //项目状态筛选
        function stateFilter(index,obj){
        	var state = $("#searchForm [name='state']").val();
        	if(state>0){
        		if($(obj).filter("tr[state='"+state+"']").length>0){
        			return true;
        		}else{
        			return false;
        		}
			}else{
				return true;
			}
        }
		
      //初始化表格视图
        function initTable(data){
     	   var table = $("#tableView").find("table")
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
     	   trAppend(data);//数据行加载
        }
      //数据列追加
      function trAppend(data){
    	   var tbody = $("#tableView").find("table tbody");
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
      
        //表格穿透
        $(document).on("click","#tableView a",function(){
        	
        	var state = $("#state").val();
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
 		
     //查看
      function viewItem(itemId){
      	$.post("/item/authorCheck?sid="+sid,{Action:"post",itemId:itemId},    
      			function (msgObjs){
      			if(!msgObjs.succ){
      				showNotification(2,msgObjs.promptMsg);
      			}else{
      				var url = "/item/viewItemPage?sid=${param.sid}&id="+itemId;
      				openWinByRight(url);
      			}
      	},"json");
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
     
   //任务查看
     function viewTask(taskId){
     	$.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:taskId},     
     			function (msgObjs){
     			if(!msgObjs.succ){
     				showNotification(1,msgObjs.promptMsg);
     			}else{
     				var url="/task/viewTask?sid="+sid+"&id="+taskId
     					+"&redirectPage="+encodeURIComponent(window.location.href);
     				openWinByRight(url)
     			}
     	},"json");
     }
       
    </script>
</html>



