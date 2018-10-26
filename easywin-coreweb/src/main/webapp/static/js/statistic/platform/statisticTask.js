var pageNum = 0;     //页面索引初始值   
var pageSize =30;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;
$(function(){
	
	var height = $(window).height()-120;
	$("#editabledatatable").parent().parent().css("max-height",height + "px");

	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initStatisticTaskTable();
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
		
		initStatisticTaskTable();
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
		
		
		initStatisticTaskTable();
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
			initStatisticTaskTable();
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
		initStatisticTaskTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initStatisticTaskTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initStatisticTaskTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initStatisticTaskTable();
	})
	$("body").on("click","a[doingTotalNum]",function(){
		var obj = $(this).parents("tr").data("obj");
		var userId = obj.userId;
		var userName = obj.userName;
		var year = $("#year").val();
		var url = "/task/listTaskOfAllPage?sid="+sid;
		url = url+"&pager.pageSize=10"
		url = url+"&activityMenu=task_m_1.5"
		url = url+"&state=1"
		url = url+"&startDate="+year+"-01-01"
		url = url+"&endDate="+year+"-12-31"
		url = url+"&listExecutor[0].id="+userId
		url = url+"&listExecutor[0].userName="+encodeURIComponent(userName);
		
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
			initStatisticTaskTable();
		})
	})
	
	initStatisticTaskTable();
		
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

var depScoreObj = {};

//出差管理
function initStatisticTaskTable(morePageNum){
	if(morePageNum){
		pageNum = morePageNum;
	}else{
		depScoreObj = {};
		pageNum = 0;
		loadDone = 0;
		layui.use('layer', function() {
			loadingIndex = layer.load(0, {
				shade: [0.5,'#fff'] //0.1透明度的白色背景
			});
			
			intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
			
		})
		//前一个部门主键
		preDepId = 0;
		//重复数
		preDepNum = 0;
		$("#allTodoBody").html('');
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
 		//取得数据
 		getSelfJSON("/statistics/platform/ajaxListStatisticTask",params,function(data){
 			if(data.status=='y'){
 				var pageBean = data.pageBean;
 				nowDate = data.nowDate;
 				
 				var recordList = pageBean.recordList;
 				if(recordList && recordList[0]){
               		constrDataTable(pageBean.recordList);
               		initStatisticTaskTable(++pageNum);
               		return;
               	 }else{
               		loadDone = 1;
               		if(!morePageNum){ 
               			var _tr = $("<tr></tr>");
                  		 var _td = $("<td></td>");
                  		 var len = $("#allTodoBody").parents("table").find("thead tr th").length;
                  		_td.attr("colspan",len);
                  		_td.css("text-align","center");
                  		_td.html("未查询到相关数据");
                  		
                  		_tr.append(_td);
                  		$("#allTodoBody").append(_tr);
               			myScroll =  $("#editabledatatable").parent().slimScroll({
               				height:"40px",
               				alwaysVisible: true,
               			    disableFadeOut: false
               			});
               		}else{
               			var height = $(window).height()-210;
               			
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
               		
               		constrPercent(depScoreObj);
               	 }
             	
 			 }
 		})
}
 //翻页调用   
function PageCallback(index, jq) {  
 pageNum = index;
 initStatisticTaskTable(index);
} 
 function constrDataTable(statisticTaskVos){
	 if(statisticTaskVos && statisticTaskVos.length>0){
		 var rowNumIndex = 1;
		 $.each(statisticTaskVos,function(objIndex,obj){
			 
			 //一行数据
			 var _tr = $("<tr></tr>");
			 $(_tr).data("obj",obj);
			 $(_tr).attr("userId",obj.userId);
			 $(_tr).attr("depId",obj.depId);
			 var tdMod = $("<td></td>");
			 tdMod.css("text-align","center");
			 
			 //部门
			 var depTr = $("#allTodoBody").find("tr[depid='"+obj.depId+"']");
			 var tdDepName = $(tdMod).clone();
			 tdDepName.css("width","120px");
			 if(!depTr || !depTr.get(0)){
				 $(tdDepName).html(obj.depName);
			 }else{
				 tdDepName = $("#allTodoBody").find("tr[depid='"+obj.depId+"']:eq(0)").find("td:eq(0)"); 
			 }
			 var depId=obj.depId;
			 if(preDepId == obj.depId){
				 preDepNum = preDepNum + 1;
				 $(tdDepName).attr("rowspan",preDepNum);
				 depScoreObj[depId].preDepNum = preDepNum;
			 }else{
				 preDepId = obj.depId;
				 preDepNum = 1;
				 $(_tr).append(tdDepName);
				 depScoreObj[depId] = {"depId":depId,"preDepNum":preDepNum};
			 }
			 
			 //人员
			 var _tdUser = $(tdMod).clone();
			 $(_tdUser).html(obj.userName);
			 _tdUser.css("width","100px");
			 $(_tr).append(_tdUser);
			 //在办数
			 var _tdDoingTotalNum = $(tdMod).clone();
			 _tdDoingTotalNum.css("width","80px");
			 if(obj.doingTotalNum>0){
				 var doingTotalNumA = $('<a href="javascript:void(0)" doingTotalNum></a>')
				 $(doingTotalNumA).html(obj.doingTotalNum);
				 $(_tdDoingTotalNum).html($(doingTotalNumA));
			 }else{
				 $(_tdDoingTotalNum).html(0);
			 }
			 $(_tr).append(_tdDoingTotalNum);
			 //逾期在办
			 var _tdYesterdayNum = $(tdMod).clone();
			 $(_tdYesterdayNum).css("background-color","#c00000");
			 $(_tdYesterdayNum).css("color","#fff");
			 $(_tdYesterdayNum).html(obj.doingYesterdayNum);
			 $(_tr).append(_tdYesterdayNum);
			 //今日在办
			 var _tdTodayNum = $(tdMod).clone();
			 $(_tdTodayNum).css("background-color","#ff0000");
			 $(_tdTodayNum).css("color","#fff");
			 $(_tdTodayNum).html(obj.doingTodayNum);
			 $(_tr).append(_tdTodayNum);
			 //明日在办
			 var _tdTomarrowNum = $(tdMod).clone();
			 $(_tdTomarrowNum).css("background-color","#ffc000");
			 $(_tdTomarrowNum).css("color","#fff");
			 $(_tdTomarrowNum).html(obj.doingTomarrowNum);
			 $(_tr).append(_tdTomarrowNum);
//					 //后天在办
//					 var _tdAfterNum = $(tdMod).clone();
//					 $(_tdAfterNum).html(obj.doingAfterNum);
//					 $(_tr).append(_tdAfterNum);
			 //将来在办
			 var _tdFutureNum = $(tdMod).clone();
			 $(_tdFutureNum).css("background-color","#00af50");
			 $(_tdFutureNum).css("color","#fff");
			 $(_tdFutureNum).html(obj.doingFutureNum);
			 $(_tr).append(_tdFutureNum);
			 
			 //总任务数
			 var _tdTotalNum = $(tdMod).clone();
			 _tdTotalNum.css("width","80px");
			 $(_tdTotalNum).html(obj.totalNum+obj.doingTotalNum);
			 $(_tr).append(_tdTotalNum);
			 //个人积分
			 var _tdTotalJifen = $(tdMod).clone();
			 _tdTotalJifen.css("width","100px");
			 $(_tdTotalJifen).html(obj.totalJifen);
			 $(_tr).append(_tdTotalJifen);
			 
			 var preJifen = depScoreObj[depId].totalDepjifen;
			 var totalDepjifen = accAdd(obj.totalJifen,preJifen?preJifen:"0");
			 depScoreObj[depId].totalDepjifen = totalDepjifen;
			 
			 //部门贡献占比
			 var _tdjifenPrecent = $(tdMod).clone();
			 _tdjifenPrecent.css("width","100px");
			 _tdjifenPrecent.attr("percentJifenTd","yes");
			 $(_tr).append(_tdjifenPrecent);
			 
			 $("#allTodoBody").append($(_tr));
			 
		 })
	 }
 }
 
 function constrPercent(list){
	 $.each(list,function(depId,obj){
		 var trs = $("#allTodoBody").find('tr[depId="'+depId+'"]');
		 var totalDepjifen = obj.totalDepjifen;
		 $.each(trs,function(index,userTd){
			 var subObj = $(userTd).data("obj");
			 var totalJifen = subObj.totalJifen;
			 var _tdjifenPrecent = $(userTd).find("td:last");
			 
			 if(totalDepjifen == 0 ){
				 $(_tdjifenPrecent).html('0%');
			 }else{
				 var percent = accDiv(subObj.totalJifen,totalDepjifen);
				 if(percent > 0){
					 percent = accMul(percent,100);
					 percent = new Number(percent).toFixed(2);
				 }
				 $(_tdjifenPrecent).html(percent+"%");
			 }
		 })
	 })
 }
