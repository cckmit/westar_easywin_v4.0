var pageNum = 0;     //页面索引初始值   
var pageSize =10;     //每页显示条数初始化，修改显示条数，修改这里即可

var loadingIndex;
var loadDone=0;
var intervalInt;

$(function(){
	//加载数据
	supviewTask.loadData(0);
	
	//查看详情
	$("body").on("click","a[taskNameA]",function(){
		var busId = $(this).parents("tr").attr("taskId");
		viewModInfo(busId,'003');
	});
	
	//催办
	$("body").on("click","button[optBtn]",function(){
		var busId = $(this).parents("tr").attr("taskId");
		//添加催办信息
		addBusRemind('003',busId,function(){})
	});
	
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>');
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		supviewTask.loadData(0);
	})
	$("#moreCondition_Div").bind("hideMoreDiv",function(){
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		if(startDate || endDate){
			var _font = $('<font style="font-weight:bold;"></font>')
			$(_font).html("筛选中")
			var _i = $('<i class="fa fa-angle-down"></i>')
			
			$(this).find("a").html(_font);
			$(this).find("a").append(_i);
		}else{
			var _i = $('<i class="fa fa-angle-down"></i>')
			$(this).find("a").html("更多");
			$(this).find("a").append(_i);
		}
	})
	//清空条件
	$("body").on("click",".clearMoreValue",function(){
		var relateList = $(this).attr("relateList");
		$("#formTempData").find("#"+relateList).html('');
		$("#"+relateList+"Div").find("span").remove();
		$("#"+relateList+"Div").hide();
		
		supviewTask.loadData(0);
	})
	
	//遍历需要设置字典表的字段
	$.each($("body").find(".dataDicClz"),function(index,obj){
		var relateElement = $(this).attr("relateElement");
		var relateElementName = $(this).attr("relateElementName");
		var _this = $(this);
		$(_this).empty();
		var type=$(this).attr("dataDic");
		if(relateElementName){
			$(this).append('<li><a href="javascript:void(0)" class="clearValue" relateElement="'+relateElement+'" relateElementName="'+relateElementName+'">不限条件</a></li>')
		}else{
			$(this).append('<li><a href="javascript:void(0)" class="clearValue" relateElement="'+relateElement+'">不限条件</a></li>')
		}
		//取得字典表数据
		listDataDic(type,function(dataDics){
			if(dataDics){
				$.each(dataDics,function(key,dataDic){
					if(dataDic.parentId==-1){
						return true;
					}
					if(relateElementName){
						$(_this).append('<li><a href="javascript:void(0)" class="setValue" relateElement="'+relateElement+'" dataValue="'+dataDic.code+'" relateElementName="'+relateElementName+'">'+dataDic.zvalue+'</a></li>')
					}else{
						$(_this).append('<li><a href="javascript:void(0)" class="setValue" relateElement="'+relateElement+'" dataValue="'+dataDic.code+'">'+dataDic.zvalue+'</a></li>')
					}
				})
			}
		})
	})
	
	
	//单个赋值
	$("body").on("click",".setValue",function(){
		var relateElement = $(this).attr("relateElement");
		var dataValue = $(this).attr("dataValue");
		$("#formTempData").find("input[name='"+relateElement+"']").val(dataValue);
		
		var _font = $('<font style="font-weight:bold;"></font>')
		$(_font).html($(this).html())
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html(_font);
		$(this).parents("ul").prev().append(_i);
		
		
		supviewTask.loadData(0);
	})
	//部门多选
	$("body").on("click",".depMoreSelect",function(){
		var relateList = $(this).attr("relateList");
		var _this = $(this);
		depMoreBack(relateList, null, sid,'yes',null,function(options){
			$("#formTempData").find("#"+relateList).html('');
			$("#"+relateList+"Div").find("span").remove();
			if(options && options.length>0){
				$("#"+relateList+"Div").show()
				$.each(options,function(optIndex,option){
					var _option = $("<option selected='selected'></option>")
					$(_option).val($(option).val());
					$(_option).html($(option).text());
					$("#"+relateList).append(_option);
					
					var _span = $("<span></span>");
					$(_span).html($(option).text())
					$(_span).addClass("label label-default margin-right-5 margin-bottom-5");
					$(_span).attr("title","双击移除");
					$(_span).css("cursor","pointer");
					$("#"+relateList+"Div").append(_span);
					
					$(_span).data("depId",$(option).val());
					$(_span).data("relateList",relateList);
					
				})
			}else{
				$("#"+relateList+"Div").hide();
			}
			supviewTask.loadData(0);
		})
	});
	//双击移除
	$("body").on("dblclick",".moreDepListShow span",function(){
		var depId = $(this).data("depId");
		if(!depId){
			depId = $(this).attr("depId");
		}
		var relateList = $(this).data("relateList");
		if(!relateList){
			relateList = $(this).attr("relateList");
		}
		$("#"+relateList).find("option[value='"+depId+"']").remove();
		if($(this).parents(".moreDepListShow").find("span").length<=1){
			$(this).parents(".moreDepListShow").hide();
		}
		$(this).remove();
		supviewTask.loadData(0);
	})
	
	//人员多选
	$("body").on("click",".userMoreSelect",function(){
		var relateList = $(this).attr("relateList");
		userMore(relateList, null, sid, null, null, function(options){
			$("#formTempData").find("#"+relateList).html('');
			$("#"+relateList+"Div").find("span").remove();
			if(options && options.length>0){
				$("#"+relateList+"Div").show()
				$.each(options,function(optIndex,option){
					var _option = $("<option selected='selected'></option>")
					$(_option).val($(option).val());
					$(_option).html($(option).text());
					$("#"+relateList).append(_option);
					
					var _span = $("<span></span>");
					$(_span).html($(option).text())
					$(_span).addClass("label label-default margin-right-5 margin-bottom-5");
					$(_span).attr("title","双击移除");
					$(_span).css("cursor","pointer");
					$("#"+relateList+"Div").append(_span);
					
					$(_span).data("userId",$(option).val());
					$(_span).data("relateList",relateList);
					
				})
			}else{
				$("#"+relateList+"Div").hide();
			}
			supviewTask.loadData(0);
		})
	});
	//双击移除
	$("body").on("dblclick",".moreUserListShow span",function(){
		var userId = $(this).data("userId");
		if(!userId){
			userId = $(this).attr("userId");
		}
		var relateList = $(this).data("relateList");
		if(!relateList){
			relateList = $(this).attr("relateList");
		}
		
		$("#"+relateList).find("option[value='"+userId+"']").remove();
		if($(this).parents(".moreUserListShow").find("span").length<=1){
			$(this).parents(".moreUserListShow").hide();
		}
		$(this).remove();
		supviewTask.loadData(0);
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		supviewTask.loadData(0);
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		supviewTask.loadData(0);
	})
	//模糊查询
	$("body").on("blur",".moreSearch",function(){
		supviewTask.loadData(0);
	})
	
	
})
		
var supviewTask = {
	loadData:function(morePageNum){
		loadDone=0;
		layui.use('layer', function() {
			loadingIndex = layer.load(0, {
				shade: [0.5,'#fff'] //0.1透明度的白色背景
			});
			intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
			
		})
		var pageUrl = "/statistics/platform/ajaxListStatisticSupTask?sid="+sid+"&t="+Math.random();
		var params = supviewTask.constaParam(morePageNum);
		//取得数据
 		getSelfJSON(pageUrl,params,function(result){
 			loadDone = 1;
 			if(result.code == 0){
 				var pageBean = result.data;
 				$("#totalNum").html(pageBean.totalCount);
               	if(pageBean.totalCount<=pageSize){
               		 $("#totalNum").parent().parent().css("display","none")
               	}else{
               		$("#totalNum").parent().parent().css("display","block")
               	}
          		//分页，PageCount是总条目数，这是必选参数，其它参数都是可选
                 $("#pageDiv").pagination(pageBean.totalCount, {
                     callback: supviewTask.PageCallback,  //PageCallback() 为翻页调用次函数。
                     prev_text: "<<",
                     next_text: ">>",
                     items_per_page:pageSize,
                     num_edge_entries: 0,       //两侧首尾分页条目数
                     num_display_entries: 5,    //连续分页主体部分分页条目数
                     current_page: pageNum,   //当前页索引
                 });
               	 $("#allTodoBody").html('');
               	 if(pageBean.totalCount>0){
               		var list = supviewTask.combineData(pageBean.recordList);
               		supviewTask.constrTaskDataTable(list);
               	 }else{
               		 var _tr = $("<tr></tr>");
               		 var _td = $("<td></td>");
               		 var len = $("#allTodoBody").parents("table").find("thead tr th").length;
               		_td.attr("colspan",len);
               		_td.css("text-align","center");
               		_td.html("未查询到相关数据");
               		
               		_tr.append(_td);
               		$("#allTodoBody").append(_tr);
               		
               	 }
 			}
 		});
	},PageCallback:function(index, jq){
		pageNum = index;
		supviewTask.loadData(index);
	},constaParam:function(morePageNum){
		if(morePageNum){
			pageNum = morePageNum;
		}else{
			pageNum = 0;
		}
		var params={"sid":sid,
	           	 "pageNum":pageNum,
	           	 "pageSize":pageSize	
	 	}
		$.each($(".searchCond").find("input"),function(){
			var name =  $(this).attr("name");
			var val =  $(this).val();
			if(val){
				params[name]=val;
			}
		})
		$.each($("#formTempData").find("input"),function(index,obj){
			var name =  $(this).attr("name");
			var val =  $(this).val();
			if(val){
				params[name]=val;
			}
		})
		$.each($("#formTempData").find("select"),function(index,obj){
			
			var list =  $(this).attr("list");
			var listkey =  $(this).attr("listkey");
			var listvalue =  $(this).attr("listvalue");
			
			var options = $(this).find("option");
			if(options && options.length>0){
				$.each(options,function(optIndex,option){
					var nameKey = list+"["+optIndex+"]."+listkey;
					var nameValue = list+"["+optIndex+"]."+listvalue;
					params[nameKey]=$(option).val();
					params[nameValue]=$(option).text();
				})
			}
		})
		return params;
	},combineData:function(list){
		var result = new Array();
		if(list && list[0]){
			var preTaskId = -1;
			$.each(list,function(index,object){
				var taskId = object.TASKID;
				var key = "task_"+taskId;
				var task = result[key];
				if(task){
					//任务执行人的执行状态	
					var executeState = object.EXECUTESTATE;
					
					var executors = task.EXECUTORS;
					executors.push({"userId":object.EXECUTOR,"userName":object.USERNAME,"executeState":executeState})
					task.EXECUTORS = executors;
					
					//任务执行	
					var executeProgress = object.EXECUTEPROGRESS;
					executeProgress = (executeProgress && executeProgress>0)?executeProgress:0;
					
					//任务总进度	
					var totalProgress = task.TOTALPROGRESS;
					if(executeState == 4){
						totalProgress = accAdd(totalProgress, 100);
					}else{
						totalProgress =  accAdd(totalProgress,executeProgress);
					}
					task.TOTALPROGRESS = totalProgress;
				}else{
					task = $.extend({},object);
					
					//任务执行人的执行状态	
					var executeState = object.EXECUTESTATE;
					
					var executors = new Array();
					executors.push({"userId":object.EXECUTOR,"userName":object.USERNAME,"executeState":executeState})
					task.EXECUTORS = executors;
					//任务总进度	
					var totalProgress = object.EXECUTEPROGRESS;
					if(executeState == 4){
						totalProgress = 100;
					}else{
						totalProgress = (totalProgress && totalProgress>0)?totalProgress:0;
					}
					task.TOTALPROGRESS = totalProgress
				}
				result[key] = task;
			})
		}
		return result;
		
	},constrTaskDataTable:function(list){
		if(list){
			var rowIndex = 1;
			for(var index in list){
				if(index == 'in_array'){
					return true;
				}
				var object = list[index];
				
				var taskId = object.TASKID;
				var key = "task_"+taskId;
				
				var tr = $('<tr></tr>');
				$(tr).attr("taskId",taskId);
				
				var executings = new Array();
				$.each(object.EXECUTORS,function(subIndex,subObject){
					var executeState = subObject.executeState;
					if(executeState == 4){
						return true;
					}
					executings.push(subObject);
				})
				
				var rowSpan = executings.length;	
				
				var  tdMod = $('<td rowSpan="'+rowSpan+'"></td>');
				$(tdMod).css("text-align","center");
				
				//序号
				var indexTd = $(tdMod).clone();
				$(indexTd).append((rowIndex ++));
				$(tr).append($(indexTd));
				
				//任务名称
				var taskNameTd = $(tdMod).clone();
				$(taskNameTd).css("text-align","left");
				
				var taskNameA = $('<a href="javascript:void(0)" taskNameA></a>')
				$(taskNameA).append(object.TASKNAME)
				$(taskNameTd).append($(taskNameA));
				$(tr).append($(taskNameTd));
				
				//紧急度
				var taskGradeTd = $(tdMod).clone();
				var taskGrade = object.GRADE;
				
				var gradeSpan = null;
				if(taskGrade == 1){
					gradeSpan = $('<span class="label label-green"></span>')
				}else if(taskGrade == 2){
					gradeSpan = $('<span class="label label-darkpink"></span>')
				}else if(taskGrade == 3){
					gradeSpan = $('<span class="label label-orange"></span>')
				}else if(taskGrade == 4){
					gradeSpan = $('<span class="label label-red"></span>')
				}
				$(gradeSpan).append(object.GRADENAME);
				
				$(taskGradeTd).append($(gradeSpan));
				$(tr).append($(taskGradeTd));
				
				//任务时限
				var taskDealTimeTd = $(tdMod).clone();
				if(object.DEALTIMELIMIT){
					$(taskDealTimeTd).append(object.DEALTIMELIMIT);
					
				}else{
					$(taskDealTimeTd).append('--');
				}
				$(tr).append($(taskDealTimeTd));
				
				//任务进度
				var taskProgressTd = $(tdMod).clone();
				$(taskProgressTd).css("text-align","left");
				
				var percent = accDiv(object.TOTALPROGRESS,object.EXECUTORS.length);
				if(percent > 0){
					 percent = new Number(percent).toFixed(0);
				}
				var progressDiv = $('<div class="progress"></div>');
				var barDiv = $('<div class="bar" style="width: 0%;"></div>')
				$(barDiv).css("width",percent+"%");
				$(progressDiv).append($(barDiv));
				
				$(taskProgressTd).append($(progressDiv));
				var basSpan = $('<span>'+percent+'%</span>');
				$(basSpan).css("padding-left","5px");
				$(basSpan).css("font-size","12px");
				$(taskProgressTd).append($(basSpan));
				$(taskProgressTd).css("padding","0 2px")
				
				$(tr).append($(taskProgressTd));
				
				
				//预警状态
				var taskDealTimeTd = $(tdMod).clone();
				var overDueLevel = $('<img src="/static/images/light_'+object.OVERDUELEVEL+'.gif">')
				$(taskDealTimeTd).append($(overDueLevel));
				$(tr).append($(taskDealTimeTd));
				//操作
				var optTd = $(tdMod).clone();
				
				var optA = $('<button class="btn btn-info btn-primary btn-xs" optBtn type="button">催办</button>');
				$(optTd).append($(optA));
				$(tr).append($(optTd));
				
				$("#allTodoBody").append($(tr));
				
				$.each(executings,function(subIndex,subObj){
					if(subIndex == 0){
						var tr = $("#allTodoBody").find("tr[taskId='"+taskId+"']:eq(0)");
						//办理人
						var exectiorTd = $('<td></td>');
						$(exectiorTd).css("text-align","center");
						$(exectiorTd).append(subObj.userName);
						
						$(tr).find("td:eq(4)").after($(exectiorTd));
					}else{
						var subTr = $('<tr></tr>');
						$(subTr).attr("taskId",taskId);
						//办理人
						var exectiorTd = $('<td></td>');
						$(exectiorTd).css("text-align","center");
						$(exectiorTd).append(subObj.userName);
						
						$(subTr).append($(exectiorTd));
						
						$("#allTodoBody").append($(subTr));
					}
				})
			}
		}
	}
}

function closeLoad(){
	if(loadDone){
		layui.use('layer', function() {
			layer.closeAll('loading');
		});
		clearInterval(intervalInt);
	}
}