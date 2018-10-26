var nowDate;
$(function(){
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		if(relateElement=='busId'){
			$("#formTempData").find("input[name='busType']").val('');
		}
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initReleasePmTable();
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
		
		initReleasePmTable();
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
		
		
		initReleasePmTable();
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
			initReleasePmTable();
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
							
							initReleasePmTable();
							
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
		initReleasePmTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initReleasePmTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initReleasePmTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initReleasePmTable();
	})
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId && insId>0){
			viewSpFlow(insId);
		}
	})
	//添加事件管理过程
	$("body").on("click","a[addReleasePms]",function(){
		listBusMapFlows('04204',function(busMapFlow){
			var url = "/busRelateSpFlow/releasePm/addReleasePm?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=04204";
			openWinByRight(url);
		})
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
			initReleasePmTable();
		})
	})
	
	//发布总数
	$("body").on("click","a[totalNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.quarterLevel * 3;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startDate = year+"-"+((monthOrder-2)<10?('0'+(monthOrder-2)):(monthOrder-2))+"-01";
		var endDate = year+"-"+(monthOrder<10?('0'+monthOrder):monthOrder)+"-"+getLastDay(year,monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04204";
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		window.self.location=url;
	})
	//发布成功的数量
	$("body").on("click","a[totalDone]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.quarterLevel * 3;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startDate = year+"-"+((monthOrder-2)<10?('0'+(monthOrder-2)):(monthOrder-2))+"-01";
		var endDate = year+"-"+(monthOrder<10?('0'+monthOrder):monthOrder)+"-"+getLastDay(year,monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04204";
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		url = url+"&releaseEndCode="+encodeURIComponent('成功');
		window.self.location=url;
	})
	//按计划完成
	$("body").on("click","a[scheduleRNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.quarterLevel * 3;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startDate = year+"-"+((monthOrder-2)<10?('0'+(monthOrder-2)):(monthOrder-2))+"-01";
		var endDate = year+"-"+(monthOrder<10?('0'+monthOrder):monthOrder)+"-"+getLastDay(year,monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04204";
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		url = url+"&scheduleState=1";
		window.self.location=url;
	})
	//应执行的配置更行
	$("body").on("click","a[shouldNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.quarterLevel * 3;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startDate = year+"-"+((monthOrder-2)<10?('0'+(monthOrder-2)):(monthOrder-2))+"-01";
		var endDate = year+"-"+(monthOrder<10?('0'+monthOrder):monthOrder)+"-"+getLastDay(year,monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04203";
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		window.self.location=url;
	})
	//应执行的配置更行
	$("body").on("click","a[scheduleMNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.quarterLevel * 3;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startDate = year+"-"+((monthOrder-2)<10?('0'+(monthOrder-2)):(monthOrder-2))+"-01";
		var endDate = year+"-"+(monthOrder<10?('0'+monthOrder):monthOrder)+"-"+getLastDay(year,monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04203";
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		url = url+"&scheduleState=1";
		window.self.location=url;
	})
	
	initReleasePmTable();
})
//出差管理
function initReleasePmTable(){
	
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
	getSelfJSON("/iTOm/releasePm/analyseReleasePm",params,function(data){
		if(data.status=='y'){
			$("#allTodoBody").html('');
			constrDataTable(data.list);
		 }
	})
}
 //翻页调用   
function PageCallback(index, jq) {  
 pageNum = index;
 initReleasePmTable(index);
} 
 
 function constrDataTable(eventPmTables){
	 if(eventPmTables && eventPmTables[0]){
		 $.each(eventPmTables,function(objIndex,obj){
			 //一行数据
			 var tr = $("<tr></tr>");
			 $(tr).data("obj",obj);
			 var tdMod = $("<td></td>");
			 tdMod.css("text-align","center");
			 
			 //发布季度
			 var tdXh = $(tdMod).clone();
			 $(tdXh).html(obj.quarterName);
			 $(tr).append(tdXh)
			 //发布总数
			 var tdTotalNum = $(tdMod).clone();
			 var totalNumA = $('<a href="javascript:void(0)" totalNum ></a>');
			 $(totalNumA).html(obj.totalNum);
			 $(tdTotalNum).html( $(totalNumA));
			 $(tr).append(tdTotalNum)
			 //发布成功的数量
			 var tdTotalDoneNum = $(tdMod).clone();
			 var totalDoneNumA = $('<a href="javascript:void(0)" totalDone ></a>');
			 $(totalDoneNumA).html(obj.totalDoneNum);
			 $(tdTotalDoneNum).html($(totalDoneNumA));
			 $(tr).append(tdTotalDoneNum);
			 //发布成功的数量
			 var tdDonePercent = $(tdMod).clone();
			 var donePercent = 0;
			 if(obj.totalNum>0){
				 donePercent = accDiv(obj.totalDoneNum,obj.totalNum);
			 }
			 if(donePercent > 0){
				 donePercent = accMul(donePercent,100);
				 donePercent = new Number(donePercent).toFixed(0);
			 }
			 $(tdDonePercent).html(donePercent+"%");
			 $(tr).append(tdDonePercent);
			 
			 //按计划完成的发布数量
			 var tdScheduleRNum = $(tdMod).clone();
			 var scheduleRNumA = $('<a href="javascript:void(0)" scheduleRNum ></a>');
			 $(scheduleRNumA).html(obj.scheduleRNum);
			 $(tdScheduleRNum).html($(scheduleRNumA));
			 $(tr).append(tdScheduleRNum);
			//发布及时率
			 var tdScheduleRPercent = $(tdMod).clone();
			 var scheduleRPercent = 0;
			 if(obj.totalNum>0){
				 scheduleRPercent = accDiv(obj.scheduleRNum,obj.totalNum);
			 }
			 if(scheduleRPercent > 0){
				 scheduleRPercent = accMul(scheduleRPercent,100);
				 scheduleRPercent = new Number(scheduleRPercent).toFixed(0);
			 }
			 $(tdScheduleRPercent).html(scheduleRPercent+"%");
			 $(tr).append(tdScheduleRPercent);
			 
			 
			 //应执行的配置更行次数
			 var tdShouldNum = $(tdMod).clone();
			 var shouldNumA = $('<a href="javascript:void(0)" shouldNum ></a>');
			 $(shouldNumA).html(obj.shouldNum)
			 $(tdShouldNum).html($(shouldNumA));
			 $(tr).append(tdShouldNum);
			 
			 //按时执行的配置更新次数
			 var tdScheduleMNum = $(tdMod).clone();
			 var scheduleMNumA = $('<a href="javascript:void(0)" scheduleMNum ></a>');
			 $(scheduleMNumA).html(obj.scheduleMNum);
			 $(tdScheduleMNum).html($(scheduleMNumA));
			 $(tr).append(tdScheduleMNum);
			 
			//配置更新及时率
			 var tdScheduleMPercent = $(tdMod).clone();
			 var scheduleMPercent = 0;
			 if(obj.totalNum>0){
				 scheduleMPercent = accDiv(obj.scheduleMNum,obj.shouldNum);
			 }
			 if(scheduleMPercent > 0){
				 scheduleMPercent = accMul(scheduleMPercent,100);
				 scheduleMPercent = new Number(scheduleMPercent).toFixed(0);
			 }
			 $(tdScheduleMPercent).html(scheduleMPercent+"%");
			 $(tr).append(tdScheduleMPercent);
			 
			 $("#allTodoBody").append($(tr));
			 
		 })
	 }
 }
