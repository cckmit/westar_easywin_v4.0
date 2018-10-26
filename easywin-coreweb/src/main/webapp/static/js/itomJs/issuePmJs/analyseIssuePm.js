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
		
		initIssuePmTable();
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
		
		initIssuePmTable();
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
		
		
		initIssuePmTable();
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
			initIssuePmTable();
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
							
							initIssuePmTable();
							
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
		initIssuePmTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initIssuePmTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initIssuePmTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initIssuePmTable();
	})
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId && insId>0){
			viewSpFlow(insId);
		}
	})
	//添加事件管理过程
	$("body").on("click","a[addIssuePms]",function(){
		listBusMapFlows('04202',function(busMapFlow){
			var url = "/busRelateSpFlow/issuePm/addIssuePm?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=04202";
			openWinByRight(url);
		})
	})
	
	//问题总数
	$("body").on("click","a[totalIssueNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.monthOrder;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startDate = yearMonth+"-01";
		var endDate = yearMonth+"-"+getLastDay(Number(year),monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04202";
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		url = url+"&issueRepetitionMark=0";
		url = url+"&issueEndCode="+encodeURIComponent('根本解决,变通方法,无法解决');
		window.self.location=url;
	})
	//成功解决问题
	$("body").on("click","a[resolveDone]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.monthOrder;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startDate = yearMonth+"-01";
		var endDate = yearMonth+"-"+getLastDay(Number(year),monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04202";
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		url = url+"&issueRepetitionMark=0";
		url = url+"&issueEndCode="+encodeURIComponent('根本解决');
		window.self.location=url;
	})
	
	//关闭问题
	$("body").on("click","a[closeNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.monthOrder;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startDate = yearMonth+"-01";
		var endDate = yearMonth+"-"+getLastDay(Number(year),monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04202";
		url = url+"&startDate="+startDate;
		url = url+"&endDate="+endDate;
		url = url+"&status=4";
		url = url+"&issueRepetitionMark=0";
		url = url+"&issueStatus="+encodeURIComponent('结束并关闭');
		window.self.location=url;
	})
	//解决完成问题的数量
	$("body").on("click","a[resolveNumA]",function(){
		var obj = $(this).parents("tr").data("obj");
		var monthOrder = obj.monthOrder;
		var year= $("body").find("#year").val();
		
		var yearMonth = obj.yearMonth;
		var startResolveDate = yearMonth+"-01";
		var endResolveDate = yearMonth+"-"+getLastDay(Number(year),monthOrder);
		
		var url = "/iTOm/itom_center?sid="+sid;
		url = url+"&activityMenu=04202";
		url = url+"&startResolveDate="+startResolveDate;
		url = url+"&endResolveDate="+endResolveDate;
		url = url+"&status=4";
		url = url+"&issueRepetitionMark=0";
		url = url+"&issueEndCode="+encodeURIComponent('根本解决,变通方法,无法解决');
		window.self.location=url;
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
			initIssuePmTable();
		})
	})
		
	initIssuePmTable();
})
		//出差管理
	 	function initIssuePmTable(){
			
			var params={"sid":sid}
			
			$.each($(".searchCond").find("input"),function(){
				var name =  $(this).attr("name");
				var val =  $(this).val();
				if(val){
					params[name]=val;
				}
			});
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
	     		getSelfJSON("/iTOm/issuePm/analyseIssuePm",params,function(data){
	     			if(data.status=='y'){
	     				$("#allTodoBody").html('');
	     				constrDataTable(data.list);
	     			 }
	     		})
	 	}
		 //翻页调用   
        function PageCallback(index, jq) {  
   	   	 pageNum = index;
   	  	 initIssuePmTable(index);
        } 
		 
		 function constrDataTable(eventPmTables){
			 if(eventPmTables && eventPmTables[0]){
				 $.each(eventPmTables,function(objIndex,obj){
					 //一行数据
					 var tr = $("<tr></tr>");
					 $(tr).data("obj",obj);
					 var tdMod = $("<td></td>");
					 tdMod.css("text-align","center");
					 
					 //序号
					 var tdXh = $(tdMod).clone();
					 $(tdXh).html(obj.monthName)
					 $(tr).append(tdXh)
					 //问题总数
					 var tdTotalNum = $(tdMod).clone();
					 var totalNumA = $('<a href="javascript:void(0)" totalIssueNum></a>');
					 $(totalNumA).html(obj.totalNum);
					 $(tdTotalNum).html($(totalNumA));
					 $(tr).append(tdTotalNum);
					 
					 //成功解决问题
					 var tdResolveDoneNum = $(tdMod).clone();
					 var resolveDoneA = $('<a href="javascript:void(0)" resolveDone></a>');
					 $(resolveDoneA).html(obj.resolveDoneNum)
					 $(tdResolveDoneNum).html($(resolveDoneA));
					 $(tr).append(tdResolveDoneNum);
					 
					 //关闭问题数量
					 var tdCloseNum = $(tdMod).clone();
					 var closeNumA = $('<a href="javascript:void(0)" closeNum></a>');
					 $(closeNumA).html(obj.closeNum);
					 $(tdCloseNum).html($(closeNumA));
					 $(tr).append(tdCloseNum);
					 
					 //问题成功解决率
					 var tdResolvePrecent = $(tdMod).clone();
					 if(obj.closeNum === 0){
						 $(tdResolvePrecent).html('0%');
					 }else{
						 var percent = accDiv(obj.resolveDoneNum,obj.closeNum);
						 if(percent > 0){
							 percent = accMul(percent,100);
							 percent = new Number(percent).toFixed(0);
						 }
						 $(tdResolvePrecent).html(percent+"%");
					 }
					 $(tr).append(tdResolvePrecent);
					 
					 //平均响应时间
					 var tdResolveTime = $(tdMod).clone();
					 var  totalResolveTime = obj.resolveTimes;
					 var avgResolveTimeStr = transToUseTime(totalResolveTime);
					 $(tdResolveTime).html(avgResolveTimeStr);
					 $(tr).append(tdResolveTime);
					 
					 //完成的事件
					 var tdResolveNum = $(tdMod).clone();
					 var resolveNumA = $('<a href="javascript:void(0)" resolveNumA></a>');
					 $(resolveNumA).html(obj.resolveNum)
					 $(tdResolveNum).html($(resolveNumA));
					 $(tr).append(tdResolveNum);
					 
					 //平均解决时间
					 var tdResolveTime = $(tdMod).clone();
					 var  totalResoveTime = obj.resolveTimes;
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
