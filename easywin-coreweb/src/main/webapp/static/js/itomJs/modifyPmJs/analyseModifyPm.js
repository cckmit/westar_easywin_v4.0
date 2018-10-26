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
		
		initModifyPmTable();
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
		
		initModifyPmTable();
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
		
		
		initModifyPmTable();
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
			initModifyPmTable();
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
							
							initModifyPmTable();
							
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
		initModifyPmTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initModifyPmTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initModifyPmTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initModifyPmTable();
	})
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId && insId>0){
			viewSpFlow(insId);
		}
	})
	
	//失败的变更数量
	$("body").on("click","a[failNum]",function(){
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
		url = url+"&modifyEndCode="+encodeURIComponent('失败');
		window.self.location=url;
	})
	//已执行变更数量
	$("body").on("click","a[totalDone]",function(){
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
	//取消变更数量
	$("body").on("click","a[cancelNum]",function(){
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
		url = url+"&modifyEndCode="+encodeURIComponent('取消');
		window.self.location=url;
	})
	//变更请求总数
	$("body").on("click","a[totalNum]",function(){
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
		url = url+"&status=-14";
		window.self.location=url;
	})
	//变更请求总数
	$("body").on("click","a[illegalNum]",function(){
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
		url = url+"&illegalState=1";
		window.self.location=url;
	})
	
	//添加事件管理过程
	$("body").on("click","a[addModifyPms]",function(){
		listBusMapFlows('04203',function(busMapFlow){
			var url = "/busRelateSpFlow/modifyPm/addModifyPm?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=04203";
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
			initModifyPmTable();
		})
	})
	
	initModifyPmTable();
})
//出差管理
function initModifyPmTable(){
	
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
 		getSelfJSON("/iTOm/modifyPm/analyseModifyPm",params,function(data){
 			if(data.status=='y'){
 				$("#allTodoBody").html('');
 				constrDataTable(data.list);
 			 }
 		})
}
 //翻页调用   
function PageCallback(index, jq) {  
 pageNum = index;
 initModifyPmTable(index);
} 
 
 function constrDataTable(eventPmTables){
	 if(eventPmTables && eventPmTables[0]){
		 $.each(eventPmTables,function(objIndex,obj){
			 //一行数据
			 var tr = $("<tr></tr>");
			 $(tr).data("obj",obj);
			 var tdMod = $("<td></td>");
			 tdMod.css("text-align","center")
			 
			 //序号
			 var tdXh = $(tdMod).clone();
			 $(tdXh).html(obj.quarterName);
			 $(tr).append(tdXh)
			 //失败的变更数量
			 var tdFailNum = $(tdMod).clone();
			 var failNumA = $('<a href="javascript:void(0)" failNum></a>');
			 $(failNumA).html(obj.failNum);
			 $(tdFailNum).html($(failNumA));
			 $(tr).append(tdFailNum);
			 
			 //已执行的变更总数
			 var tdTotalDoneNum = $(tdMod).clone();
			 var totalDoneA = $('<a href="javascript:void(0)" totalDone></a>');
			 $(totalDoneA).html(obj.totalDoneNum)
			 $(tdTotalDoneNum).html($(totalDoneA));
			 $(tr).append(tdTotalDoneNum);
			 
			 //失败变更占比
			 var tdFailPercent = $(tdMod).clone();
			 var failPercent = 0;
			 if(obj.totalDoneNum>0){
				 failPercent = accDiv(obj.failNum,obj.totalDoneNum);
			 }
			 if(failPercent > 0){
				 failPercent = accMul(failPercent,100);
				 failPercent = new Number(failPercent).toFixed(0);
			 }
			 $(tdFailPercent).html(failPercent+"%");
			 $(tr).append(tdFailPercent);
			 
			 //取消的变更数量
			 var tdCancelNum = $(tdMod).clone();
			 var cancelNumA = $('<a href="javascript:void(0)" cancelNum></a>');
			 $(cancelNumA).html(obj.cancelNum);
			 $(tdCancelNum).html($(cancelNumA));
			 $(tr).append(tdCancelNum);
			 
			 //变更请求总数
			 var tdTotalNum = $(tdMod).clone();
			 var totalNumA = $('<a href="javascript:void(0)" totalNum></a>');
			 $(totalNumA).html(obj.totalNum);
			 $(tdTotalNum).html($(totalNumA));
			 $(tr).append(tdTotalNum);
			 
			 //取消变更率
			 var tdCancelPercent = $(tdMod).clone();
			 var cancelPercent = 0;
			 if(obj.totalDoneNum>0){
				 cancelPercent = accDiv(obj.cancelNum,obj.totalNum);
			 }
			 if(cancelPercent > 0){
				 cancelPercent = accMul(cancelPercent,100);
				 cancelPercent = new Number(cancelPercent).toFixed(0);
			 }
			 $(tdCancelPercent).html(cancelPercent+"%");
			 $(tr).append(tdCancelPercent);
			 
			//非法变更数量
			 var tdIllegalNum = $(tdMod).clone();
			 var illegalNumA = $('<a href="javascript:void(0)" illegalNum></a>');
			 $(illegalNumA).html(obj.illegalNum);
			 $(tdIllegalNum).html($(illegalNumA));
			 $(tr).append(tdIllegalNum);
			 
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
