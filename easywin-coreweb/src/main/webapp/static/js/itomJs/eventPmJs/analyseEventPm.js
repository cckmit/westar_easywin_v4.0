var nowDate;
$(function(){
	//清空条件
	$("body").off("click",".clearValue").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		if(relateElement=='busId'){
			$("#formTempData").find("input[name='busType']").val('');
		}
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initEventPmTable();
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
		
		initEventPmTable();
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
		
		
		initEventPmTable();
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
			initEventPmTable();
		})
	});
	
	//项目多选
	$("body").off("click",".relateModSelect").on("click",".relateModSelect",function(){
		//关联的业务模块
		var busType=$(this).attr("busType");
		//业务类型代码
		var typeValue=$(this).attr("typeValue");
		//存放的位置
		var relateList = $(this).attr("relateList");
		//是否多选
		var isMore = $(this).attr("isMore");
		isMore = (isMore && isMore=='0')?1:2;
		var _this = $(this);
		
		if(busType=='005'){
			var relateListTemp = relateList;
			itemMoreSelect(isMore, relateListTemp,function(items){
				$("#formTempData").find("#"+relateList).html('');
				$("#"+relateList+"Div").find("span").remove();
				
				if(items && items.length>0){
					
					$.each(items,function(index,item){
						if(isMore === 1){
							$("#formTempData").find("input[name='busId']").val(item.id);
							$("#formTempData").find("input[name='busType']").val(busType);
							
							var _font = $('<font style="font-weight:bold;"></font>')
							$(_font).html(item.itemName)
							var _i = $('<i class="fa fa-angle-down"></i>')
							
							$(_this).parents("ul").prev().html(_font);
							$(_this).parents("ul").prev().append(_i);
							
							initEventPmTable();
							
						}else{
							
							var _option = $("<option selected='selected'></option>")
							$(_option).val(item.id);
							$(_option).html(item.itemName);
							$("#"+relateList).append(_option);
							
							var _span = $("<span></span>");
							$(_span).html(item.itemName)
							$(_span).addClass("label label-default margin-right-5 margin-bottom-5");
							$(_span).attr("title","双击移除");
							$(_span).css("cursor","pointer");
							$("#"+relateList+"Div").append(_span);
							
							$(_span).data("busId",item.id);
							$(_span).data("relateList",relateList);
						}
					})
				}
			})
		}
	});
	
	//双击移除
	$("body").on("dblclick",".moreUserListShow span",function(){
		var userId = $(this).data("userId");
		var relateList = $(this).data("relateList");
		$("#"+relateList).find("option[value='"+userId+"']").remove();
		if($(this).parents(".moreUserListShow").find("span").length<=1){
			$(this).parents(".moreUserListShow").hide();
		}
		$(this).remove();
		initEventPmTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initEventPmTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initEventPmTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initEventPmTable();
	})
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId && insId>0){
			viewSpFlow(insId);
		}
	})
	//添加事件管理过程
	$("body").on("click","a[addEventPms]",function(){
		listBusMapFlows('04201',function(busMapFlow){
			var url = "/busRelateSpFlow/eventPm/addEventPm?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=04201";
			openWinByRight(url);
		})
	})
	//事件数点击穿透
	$("body").on("click","a[degreeNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var eventPriorityDegree = obj.priorityDegree;
		var year= $("body").find("#year").val()
		var startDate = year+"-01-01";
		var endDate = year+"-12-31";
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04201";
		url = url+"&eventPriorityDegree="+encodeURIComponent(eventPriorityDegree);
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		window.self.location = url;
	})
	//事件数点击穿透
	$("body").on("click","a[resolveNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var eventPriorityDegree = obj.priorityDegree;
		var year= $("body").find("#year").val();
		var startDate = year+"-01-01";
		var endDate = year+"-12-31";
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04201";
		url = url+"&eventPriorityDegree="+encodeURIComponent(eventPriorityDegree);
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		url = url+"&eventRepetitionMark=0";
		url = url+"&eventStatus="+encodeURIComponent('关闭,已解决');
		window.self.location = url;
	})
	
	//设置年份选择和周数选择
	getSelfJSON('/organic/findOrgInfo',{"sid":sid},function(data){
		//团队注册时间年份
		var registYear = data.registYear;
		//当前时间年份
		var nowYear = data.nowYear;
		//添加年份选择
		for(var index = nowYear ; index >= registYear ; index--){
			var _li = $('<li></li>');
			var _a = $('<a href="javascript:void(0)"></a>')
			$(_a).attr("data-dataValue",index);
			$(_a).html(index+'年');
			$(_li).append($(_a));
			$("#weekYearUl").append($(_li));
		}
		$("body").on("click","a[data-dataValue]",function(){
			var infoA  = $("#weekYearUl").prev();
			var dataValue = $(this).attr("data-dataValue");
			$(infoA).html('<font style="font-weight:bold;">'+dataValue+'年</font>')
			$(infoA).append('<i class="fa fa-angle-down"></i>')
			$("body").find("#year").val(dataValue);
			initEventPmTable();
		})
	})
	
	initEventPmTable();
})
//出差管理
function initEventPmTable(){
	
	var params={"sid":sid}
	
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
	//取得数据
	getSelfJSON("/iTOm/eventPm/analyseEventPm",params,function(data){
		if(data.status=='y'){
			$("#allTodoBody").html('');
			constrDataTable(data.list);
		 }
	})
}
 //翻页调用   
function PageCallback(index, jq) {  
	pageNum = index;
	initEventPmTable(index);
} 
		 
 function constrDataTable(eventPmTables){
	 if(eventPmTables && eventPmTables[0]){
		 var rowNumIndex = 1;
		 $.each(eventPmTables,function(objIndex,obj){
			 //一行数据
			 var tr = $("<tr></tr>");
			 $(tr).data("obj",obj);
			 var tdMod = $("<td></td>");
			 tdMod.css("text-align","center")
			 //事件级别
			 var tdPriorityDegree = $(tdMod).clone();
			 $(tdPriorityDegree).html(obj.priorityDegree);
			 $(tr).append(tdPriorityDegree);
			 
			 //事件数
			 var tdDegreeNum = $(tdMod).clone();
			 var degreeA = $('<a href="javascript:void(0)" degreeNum ></a>');
			 $(degreeA).html(obj.degreeNum);
			 $(tdDegreeNum).html($(degreeA));
			 $(tr).append(tdDegreeNum);
			 
			 //平均响应时间
			 var tdResponeTime = $(tdMod).clone();
			 var  totalResponeTime = obj.totalResponeTime;
			 var avgResponeTime = 0;
			 if(obj.degreeNum && obj.degreeNum>0){
				 avgResponeTime = accDiv(totalResponeTime,obj.degreeNum);
			 }
			 if(avgResponeTime > 0){
				 avgResponeTime = new Number(avgResponeTime).toFixed(0);
			 }
			 var avgResponeTimeStr = transToUseTime(avgResponeTime);
			 $(tdResponeTime).html(avgResponeTimeStr);
			 $(tr).append(tdResponeTime);
			 
			 //完成的事件
			 var tdResolveNum = $(tdMod).clone();
			 var resolveA = $('<a href="javascript:void(0)" resolveNum ></a>');
			 $(resolveA).html(obj.resolveNum);
			 $(tdResolveNum).html($(resolveA));
			 $(tr).append(tdResolveNum);
			 
			 //平均解决时间
			 var tdResolveTime = $(tdMod).clone();
			 var  totalResoveTime = obj.totalResoveTime;
			 var avgResolveTime = 0;
			 if(obj.resolveNum && obj.resolveNum>0){
				 avgResolveTime = accDiv(totalResoveTime,obj.resolveNum);
			 }
			 if(avgResolveTime > 0){
				 avgResolveTime = new Number(avgResolveTime).toFixed(0);
			 }
			 var avgResolveTimeStr = transToUseTime(avgResolveTime);
			 $(tdResolveTime).html(avgResolveTimeStr);
			 $(tr).append(tdResolveTime);
			 
			 totalResoveTime
			 
			 $("#allTodoBody").append($(tr));
			 
		 })
	 }
 }
		 
 function constrTagHtml(status,title){
	 var html = "";
	 if(status == 4){//通过
		 html = '<i class="fa fa-check green" title="'+title+'成功"></i>';
	 }else if (status == 1){//审核中
		 html = '<i class="fa fa-gavel blue" title="'+title+'中"></i>';
	 }else if (status == 2){//草稿
		 html = '<i class="fa fa-pencil purple" title="草稿"></i>';
	 }else if (status == -1){//未通过
		 html = '<i class="fa fa-times red" title="'+title+'失败"></i>';
	 }
	 return html;
 }
 
 function transToUseTime(useTimeParam){
	 var useTimeLong = useTimeParam;
	 //天数
	 var dd = useTimeLong >=(1000*60*60*24)?(useTimeLong - useTimeLong % (1000*60*60*24))/1000/60/60/24:0;
	 var useTimeD = useTimeLong - dd * 1000*60*60*24;
	 var hh = useTimeD>=(1000*60*60)?(useTimeD - useTimeD %(1000*60*60)) /1000/60/60:0;
	 var useTimeH = useTimeD - hh*1000*60*60;
	 var mm = useTimeH>=(1000*60)?(useTimeH - useTimeH %(1000*60)) /1000/60:0;
	 var useTimeM = useTimeH - mm*1000*60;
	 var ss = useTimeM>=(1000)?(useTimeM - useTimeM %(1000)) /1000:0;
	 
	 var useTime = "";
	 if(dd>0){
		 useTime = useTime+dd+"天";
	 }
	 if(hh>0){
		 useTime = useTime+hh+"时";
	 }
	 if(mm>0){
		 useTime = useTime+mm+"分";
	 }
	 if(dd>=0 && hh>=0 && ss>0){
		 useTime = useTime+ss+"秒";
	 }else if(dd<=0 && hh<=0 && ss==0){
		 useTime = "--";
	 }
	 return useTime;
	 
 }
