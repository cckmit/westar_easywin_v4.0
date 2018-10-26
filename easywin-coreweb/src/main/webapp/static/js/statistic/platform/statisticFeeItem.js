var pageNum = 0;     //页面索引初始值   
var pageSize =20;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;

var myScroll;
$(function(){
	var height = $(window).height()-140;
	$("#editabledatatable").parent().parent().css("max-height",height + "px");
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initStatisticFeeItemTable();
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
		
		initStatisticFeeItemTable();
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
		
		
		initStatisticFeeItemTable();
	})
	//人员多选
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
			initStatisticFeeItemTable();
		})
	});
	//双击移除
	$("body").on("dblclick",".moreDepListShow span",function(){
		var depId = $(this).data("depId");
		var relateList = $(this).data("relateList");
		$("#"+relateList).find("option[value='"+depId+"']").remove();
		if($(this).parents(".moreDepListShow").find("span").length<=1){
			$(this).parents(".moreDepListShow").hide();
		}
		$(this).remove();
		initStatisticFeeItemTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initStatisticFeeItemTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initStatisticFeeItemTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initStatisticFeeItemTable();
	})
	$("body").on("click","a[data-itemfee-month-item]",function(){
		var month = $(this).attr("data-itemfee-month-item");
		
		var obj = $(this).parents("tr").data("obj");
		var itemName = obj.itemName;
		
		var year = $("#year").val();
		
		var yearMonth = year+"-"+(month<10?('0'+month):month);
		var startDate = yearMonth+"-01";
		var endDate = yearMonth+"-"+getLastDay(Number(year),month);
		
		var url = "/financial/loanApply/listLoanApplyOfAuth?sid="+sid;
		url = url+"&pager.pageSize=10"
		url = url+"&activityMenu=m_1.2"
		
		url = url+"&startDate="+startDate
		url = url+"&endDate="+endDate
		url = url+"&busType=005"
		url = url+"&flowName="+encodeURIComponent(itemName);
//		window.self.location=url
		window.open(url,"newwindow");
	})
	$("body").on("click","a[data-itemAllFee]",function(){
		var obj = $(this).parents("tr").data("obj");
		var itemName = obj.itemName;
		
		var year = $("#year").val();
		var startDate = year+"-01-01";
		var endDate = year+"-12-31";
		
		var url = "/financial/loanApply/listLoanApplyOfAuth?sid="+sid;
		url = url+"&pager.pageSize=10"
		url = url+"&activityMenu=m_1.2"
		
		url = url+"&startDate="+startDate
		url = url+"&endDate="+endDate
		url = url+"&busType=005"
		url = url+"&flowName="+encodeURIComponent(itemName);
//		window.self.location=url
		window.open(url,"newwindow");
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
			initStatisticFeeItemTable();
		})
	})
	
	initStatisticFeeItemTable();
		
})
function closeLoad(){
	if(loadDone){
		layui.use('layer', function() {
			layer.closeAll('loading');
		});
		clearInterval(intervalInt);
	}
	
}
var loadingIndex;
var loadDone=0;
var intervalInt;
//前一个部门主键
var preDepId = 0;
//重复数
var preDepNum = 0;

var monThObj = {};

var depMonthObj = {};
//加载数据
function initStatisticFeeItemTable(morePageNum){
	if(morePageNum){
		pageNum = morePageNum;
	}else{
		loadDone = 0
		layui.use('layer', function() {
			loadingIndex = layer.load(0, {
				shade: [0.5,'#fff'] //0.1透明度的白色背景
			});
			
			intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
			
		})
		pageNum = 0;
		$("#allTodoBody").html('');
		
		preDepId = 0;
		preDepNum = 0;
		//合计数字
		for(var i = 1;i < 13;i++){
			monThObj[i] = 0;
		}
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
	
		//查看客户详情
	$("body").on("click","a[data-itemId]",function(){
		var itemId =  $(this).attr("data-itemId");
		var busType='005';
		viewModInfo(itemId,busType)
	})
	//取得数据
	getSelfJSON("/statistics/platform/ajaxListStatisticFeeItem",params,function(data){
		if(data.status=='y'){
			var pageBean = data.pageBean;
			var recordList = pageBean.recordList;
			var nowMonth = data.nowMonth;
           	if(recordList && recordList[0]){
           		 constrDataTable(pageBean.recordList,nowMonth);
           		 initStatisticFeeItemTable(++pageNum);
           		 return;
           	}else{
           		loadDone = 1;
           		
           		constrDepAll(nowMonth);
           		constrAll(nowMonth);
           		if(!morePageNum){ 
           			var tr = $("<tr></tr>");
           			var td = $("<td></td>");
           			var len = $("#allTodoBody").parents("table").find("thead tr th").length;
           			td.attr("colspan",len);
           			td.css("text-align","center");
           			td.html("未查询到相关数据");
           			
           			tr.append(td);
           			$("#allTodoBody").append(tr);
           			
           			myScroll =  $("#editabledatatable").parent().slimScroll({
           				height:"40px",
           				alwaysVisible: true,
           			    disableFadeOut: false
           			});
           			
           		}else{
           			var height = $(window).height()-240;
           			var trLen=  $("#allTodoBody").find("tr").length;
           			if(trLen<10){
           				height = 40 * trLen;
           			}
           			myScroll =  $("#editabledatatable").parent().slimScroll({
           				height:height + "px",
           				alwaysVisible: true,
           			    disableFadeOut: false
           			});
           		}
           		
           	 }
		 }
	})
}
//构建数据
function constrDataTable(statisticCrmVos,nowMonth){
	 if(statisticCrmVos && statisticCrmVos[0]){
		 
		 $.each(statisticCrmVos,function(objIndex,obj){
			 
			 //一行数据
			 var tr = $("<tr></tr>");
			 $(tr).data("obj",obj);
			 $(tr).attr("userId",obj.userId);
			 $(tr).attr("depId",obj.depId);
			 var tdMod = $("<td></td>");
			 tdMod.css("text-align","center");
			 tdMod.css("padding-left","0");
			 tdMod.css("padding-right","0");
			 //部门
			 var depTr = $("#allTodoBody").find("tr[depid='"+obj.depId+"']");
			 var tdDepName = $(tdMod).clone();
			 if(!depTr || !depTr.get(0)){
				 $(tdDepName).html(obj.depName);
				 $(tdDepName).css("width","80px");
			 }else{
				 tdDepName = $("#allTodoBody").find("tr[depid='"+obj.depId+"']:eq(0)").find("td:eq(0)"); 
			 }
			 if(preDepId == obj.depId){
				 preDepNum = preDepNum + 1;
				 $(tdDepName).attr("rowspan",preDepNum);
			 }else{
				 preDepId = obj.depId;
				 preDepNum = 1;
				 $(tr).append(tdDepName);
				 depMonthObj[obj.depId] = {};
			 }
			 //姓名
			 var tdItemName = $(tdMod).clone();
			 tdItemName.css("text-align","left");
			 var itemA = $('<a href="javascript:void(0)"></a>');
			 $(itemA).attr("data-itemId",obj.itemId);
			 $(itemA).html(obj.itemName);
			 $(itemA).attr("title",obj.itemName);
			 $(tdItemName).html($(itemA));
			 $(tr).append(tdItemName);
			 
			 var tdAll = $(tdMod).clone();
			 $(tdAll).css("width","70px");
			 var tdAllNum = 0;
			 //月份数据
			 for(var i = 1;i < 13;i++){
				 
				 var tdMonth = $(tdMod).clone();
				 var itemFee = eval('(obj.itemFee'+i+')');
				 
				 var year = $("#year").val();
				 var myDate=new Date();
				 myDate.setFullYear(year,(i-1),1);
				 var actDate = new Date();
				 actDate.setFullYear(nowMonth.split("-")[0],Number(nowMonth.split("-")[1])-1,1);
				 if(myDate>actDate){
					 $(tdMonth).html('--');
				 }else{
					 
					 if(itemFee && itemFee>0){
						 var itemFeeA = $('<a href="javascript:void(0)"></a>');
						 $(itemFeeA).html(toDecimal2(itemFee));
						 $(itemFeeA).attr("data-itemfee-month-item",i);
						 $(tdMonth).html($(itemFeeA));
					 }else{
						 $(tdMonth).html("0");
						 
					 }
				 }
				 
				 $(tdMonth).css("width","65px");
				 $(tdMonth).data("itemFee",itemFee);
				 $(tr).append($(tdMonth));
				 
				 monThObj[i] = accAdd(monThObj[i],itemFee);
				 var to = depMonthObj[obj.depId][i];
				 depMonthObj[obj.depId][i] = accAdd(to?to:0,itemFee);
				 
				 tdAllNum = accAdd(tdAllNum,itemFee);
			 }
			 var  totalAll = $('<a href="javascript:void(0)" data-itemAllFee></a>');
			 $(totalAll).html(toDecimal2(tdAllNum));
			 $(tdAll).html($(totalAll))
			 $(tr).append($(tdAll));
			 $("#allTodoBody").append($(tr));
			 
		 })
	 }
 }

function constrDepAll(nowMonth){
	$.each(depMonthObj,function(depId,obj){
		var lastTr = $("#allTodoBody").find("tr[depid='"+depId+"']:last");
		
		var trAll = $("<tr></tr>");
		$(trAll).css("background-color","#99ccff")
		var tdDesc = $('<td colspan="2">小计</td>');
		tdDesc.css("text-align","center");
		$(trAll).append($(tdDesc));
		
		 var tdAll = $('<td></td>');
		 tdAll.css("text-align","center");
		 tdAll.css("padding-left","0");
		 tdAll.css("padding-right","0");
		 $(tdAll).css("width","70px");
		 var tdAllNum = 0;
		 
		for(var i = 1;i < 13;i++){
			var rowTdAll = $('<td></td>');
			rowTdAll.css("text-align","center");
			rowTdAll.css("padding-left","0");
			rowTdAll.css("padding-right","0");
			$(rowTdAll).css("width","65px");
			

			var year = $("#year").val();
			var myDate=new Date();
			myDate.setFullYear(year,(i-1),1);
			var actDate = new Date();
			actDate.setFullYear(nowMonth.split("-")[0],Number(nowMonth.split("-")[1])-1,1);
			if(myDate>actDate){
				$(rowTdAll).html('--');
			}else{
				$(rowTdAll).html(toDecimal2(obj[i]));
			}
			
			$(trAll).append($(rowTdAll));
			
			tdAllNum = accAdd(tdAllNum,obj[i]);
		}
		 $(tdAll).html(toDecimal2(tdAllNum))
		 $(trAll).append($(tdAll));
		 $(lastTr).after($(trAll));
	})
}

//构建大合计
function constrAll(nowMonth){
	$("#sumtable").html('');
	var trAll = $("<tr></tr>");
	$(trAll).css("background-color","red")
	$(trAll).css("color","#fff")
	var tdDesc = $('<td colspan="2">合计</td>');
	tdDesc.css("text-align","center");
	$(trAll).append($(tdDesc));
	
	 var tdAll = $('<td></td>');
	 tdAll.css("text-align","center");
	 tdAll.css("padding-left","0");
	 tdAll.css("padding-right","0");
	 $(tdAll).css("width","70px");
	 var tdAllNum = 0;
	 
	for(var i = 1;i < 13;i++){
		var rowTdAll = $('<td></td>');
		rowTdAll.css("text-align","center");
		rowTdAll.css("padding-left","0");
		rowTdAll.css("padding-right","0");
		$(rowTdAll).css("width","65px");
		
		var year = $("#year").val();
		var myDate=new Date();
		myDate.setFullYear(year,(i-1),1);
		var actDate = new Date();
		actDate.setFullYear(nowMonth.split("-")[0],Number(nowMonth.split("-")[1])-1,1);
		if(myDate>actDate){
			$(rowTdAll).html('--');
		}else{
			$(rowTdAll).html(toDecimal2(monThObj[i]));
		}
		 
		$(trAll).append($(rowTdAll));
		
		tdAllNum = accAdd(tdAllNum,monThObj[i]);
	}
	 $(tdAll).html(toDecimal2(tdAllNum))
	 $(trAll).append($(tdAll));
	 $("#sumtable").append($(trAll));
}
		
