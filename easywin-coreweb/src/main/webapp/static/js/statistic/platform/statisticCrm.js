var pageNum = 0;     //页面索引初始值   
var pageSize =10;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;
$(function(){
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>');
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initStatisticCrmTable();
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
		
		initStatisticCrmTable();
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
		
		
		initStatisticCrmTable();
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
			initStatisticCrmTable();
		})
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
		initStatisticCrmTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initStatisticCrmTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initStatisticCrmTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initStatisticCrmTable();
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
//		window.self.location=url
		window.open(url,"newwindow");
	})
	
	initAjaxData();
	//查看客户详情
	$("body").on("click","a[data-crmId]",function(){
		var crmId =  $(this).attr("data-crmId");
		var busType='012';
		viewModInfo(crmId,busType)
	})
	$("body").on("click","button[optBtn]",function(){
		var crmId =  $(this).attr("data-crmId");
		//添加催办信息
		addBusRemind('012',crmId,function(){
			
		})
	});
	initStatisticCrmTable();
		
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
//出差管理
function initStatisticCrmTable(morePageNum){
	loadDone=0
	layui.use('layer', function() {
		loadingIndex = layer.load(0, {
			shade: [0.5,'#fff'] //0.1透明度的白色背景
		});
		
		intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
		
	})
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
 		//取得数据
 		getSelfJSON("/statistics/platform/ajaxListStatisticCrm",params,function(data){
 			if(data.status=='y'){
 				var pageBean = data.pageBean;
 				nowDate = data.nowDate;
 			 	$("#totalNum").html(pageBean.totalCount);
               	if(pageBean.totalCount<=pageSize){
               		 $("#totalNum").parent().parent().css("display","none")
               	}else{
               		$("#totalNum").parent().parent().css("display","block")
               	}
          		//分页，PageCount是总条目数，这是必选参数，其它参数都是可选
                 $("#pageDiv").pagination(pageBean.totalCount, {
                     callback: PageCallback,  //PageCallback() 为翻页调用次函数。
                     prev_text: "<<",
                     next_text: ">>",
                     items_per_page:pageSize,
                     num_edge_entries: 0,       //两侧首尾分页条目数
                     num_display_entries: 3,    //连续分页主体部分分页条目数
                     current_page: pageNum,   //当前页索引
                 });
               	 $("#allTodoBody").html('');
               	 if(pageBean.totalCount>0){
               		 constrDataTable(pageBean.recordList);
               	 }else{
               		 var tr = $("<tr></tr>");
               		 var td = $("<td></td>");
               		 var len = $("#allTodoBody").parents("table").find("thead tr th").length;
               		td.attr("colspan",len);
               		td.css("text-align","center");
               		td.html("未查询到相关数据");
               		
               		tr.append(td);
               		$("#allTodoBody").append(tr);
               		
               	 }
               	 
               	loadDone = 1;
 			 }
 		})
}
 //翻页调用   
function PageCallback(index, jq) {  
 pageNum = index;
 initStatisticCrmTable(index);
} 
 
 function constrDataTable(statisticCrmVos){
	 if(statisticCrmVos && statisticCrmVos.length>0){
		 var rowNumIndex = 1;
		 $.each(statisticCrmVos,function(objIndex,obj){
			 
			 //一行数据
			 var tr = $("<tr></tr>");
			 $(tr).data("obj",obj);
			 $(tr).attr("userId",obj.userId);
			 $(tr).attr("depId",obj.depId);
			 var tdMod = $("<td></td>");
			 tdMod.css("text-align","center");
			 
			 //序号
			 var tdXh = $(tdMod).clone();
			 $(tdXh).html(rowNumIndex++);
			  $(tr).append(tdXh)
			 //客户名称
			 var tdCrmName = $(tdMod).clone();
			 tdCrmName.css("text-align","left");
			 var crmNameA = $('<a href="javascript:void(0)"></a>');
			 $(crmNameA).html(obj.crmName);
			 $(crmNameA).attr("data-crmId",obj.crmId);
			 $(tdCrmName).html($(crmNameA));
			 $(tr).append(tdCrmName);
			 //客户类型
			 var tdCrmType = $(tdMod).clone();
			 $(tdCrmType).html(obj.crmTypeName);
			 $(tr).append(tdCrmType);
			 //客户责任人
			 var tdOwner = $(tdMod).clone();
			 $(tdOwner).html(obj.ownerName);
			 $(tr).append(tdOwner);
			 var modifyPeriod = obj.modifyPeriod;
			 var lastTimes = obj.lastTimes;
			 //客户上次跟进
			 var tdLastUpdateDate = $(tdMod).clone();
			 $(tdLastUpdateDate).html(transToUseTime(lastTimes));
			 $(tr).append(tdLastUpdateDate);
			 //跟进周期
			 var tdModifyPeriod = $(tdMod).clone();
			 $(tdModifyPeriod).html(modifyPeriod?modifyPeriod:'--');
			 $(tr).append(tdModifyPeriod);
			 
			 //逾期状态
			 var tdOverTimeLevel = $(tdMod).clone();
			 var bgcolor;
			 var levelName;
			 switch(obj.overTimeLevel){
				 case 1:
				 case 2:
				 case 3:
				 case 4:
					 bgcolor='#e51c23';
					 levelName="逾期"
						 break;
				 case 5:
					 bgcolor='#259b24';
					 levelName="正常"
					 break;
				 default:
				 break;
			 }
					 
			 
			 //$(tdOverTimeLevel).css("background-color",bgcolor);
			 var div = $('<div legend></div>');
			 $(div).css("margin","0 auto");
			 $(div).css("background-color",bgcolor);
			 $(tdOverTimeLevel).html($(div));
			 $(tr).append(tdOverTimeLevel);
			 
			 //客户上次跟进日期
			 var tdOpt = $(tdMod).clone();
			 var optA = $('<button class="btn btn-info btn-primary btn-xs" optBtn type="button">催办</button>');
			 $(optA).css("color","#fff");
			 $(optA).attr("data-crmId",obj.crmId);
			 $(tdOpt).html(optA);
			 $(tr).append(tdOpt);
			 $("#allTodoBody").append($(tr));
			 
		 })
	 }
 }
 //构建逾期等级
 function constrOverLevel(modifyPeriod,lastTimes){
	 if(modifyPeriod>0){
		 var modifyPeriodTime =  modifyPeriod * 24 * 60 * 60 * 1000;
		 var leftTime = lastTimes - modifyPeriod;
		 if(leftTime<=0){
			 return '五级';
		 }
		 if(leftTime > (15* 24 * 60 * 60 * 1000)){
			 return '一级';
		 }
		 if(leftTime > (7* 24 * 60 * 60 * 1000)){
			 return '二级';
		 }
		 if(leftTime > (3* 24 * 60 * 60 * 1000)){
			 return '三级';
		 }
		 if(leftTime > (1* 24 * 60 * 60 * 1000)){
			 return '四级';
		 }
		 
	 } 
	 return '五级';
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
		 if(dd>360){
			 return '超过一年';
		 }
		 if(dd>180){
			 return '半年前';
		 }
		 if(dd>90){
			 return '三个月前';
		 }
		 if(dd>60){
			 return '两个月前';
		 }
		 if(dd>30){
			 return '一个月前';
		 }
//				 if(dd>15){
//					 return '半个月前';
//				 }
		 if(dd==7){
			 return '一周前';
		 }
//				 if(dd>3){
//					 return '三天前';
//				 }
//				 if(dd>2){
//					 return '两天前';
//				 }
		 if(dd>=1){
			 return dd+'天前';
		 }
		 return useTime;
	 }
	 if(hh>0){
		 useTime = useTime+hh+"小时前";
		 return useTime;
	 }
	 if(mm>0){
		 useTime = useTime+mm+"分钟前";
		 return useTime;
	 }
	 if(dd>=0 && hh>=0 && ss>0){
		 useTime = useTime+ss+"秒前";
	 }else if(dd<=0 && hh<=0 && ss==0){
		 useTime = "--";
	 }
	 return useTime;
	 
 }
function initAjaxData(){
	//设置年份选择和周数选择
	getSelfJSON('/common/listCrmType',{"sid":sid},function(data){
		if(data.status=='y'){
			var list = data.list;
			if(list && list[0]){
				$.each(list,function(index,crmTypeObj){
					var _li = $('<li></li>');
					var _a = $('<a href="javascript:void(0)"></a>')
					$(_a).attr("data-crmType",crmTypeObj.id);
					$(_a).html(crmTypeObj.typeName);
					$(_a).data("obj",crmTypeObj);
					$(_li).append($(_a));
					$("#crmTypeUl").append($(_li));
				});
				$("body").on("click","a[data-crmType]",function(){
					var infoA  = $("#crmTypeUl").prev();
					var crmTypeObj = $(this).data("obj");
					$(infoA).html('<font style="font-weight:bold;">'+crmTypeObj.typeName+'</font>')
					$(infoA).append('<i class="fa fa-angle-down"></i>');
					$("body").find("#crmTypeId").val(crmTypeObj.id);
					initStatisticCrmTable();
				})
			}
		}
		
	})
}