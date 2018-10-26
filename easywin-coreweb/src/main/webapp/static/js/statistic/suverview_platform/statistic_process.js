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
		
		initStatisticDemandTable();
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
		
		initStatisticDemandTable();
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
		
		
		initStatisticDemandTable();
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
			initStatisticDemandTable();
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
		initStatisticDemandTable();
	})
	//人员多选
	$("body").on("click",".productMoreSelect",function(){
		var relateList = $(this).attr("relateList");
		productSelect(2,null, function(products){
			$("#formTempData").find("#"+relateList).html('');
			$("#"+relateList+"Div").find("span").remove();
			if(products && products.length>0){
				$("#"+relateList+"Div").show()
				$.each(products,function(optIndex,product){
					var _option = $("<option selected='selected'></option>")
					$(_option).val(product.id);
					$(_option).html(product.name);
					$("#"+relateList).append(_option);
					
					var _span = $("<span></span>");
					$(_span).html(product.name)
					$(_span).addClass("label label-default margin-right-5 margin-bottom-5");
					$(_span).attr("title","双击移除");
					$(_span).css("cursor","pointer");
					$("#"+relateList+"Div").append(_span);
					
					$(_span).data("productId",product.id);
					$(_span).data("relateList",relateList);
					
				})
			}else{
				$("#"+relateList+"Div").hide();
			}
			initStatisticDemandTable();
		})
	});
	//双击移除
	$("body").on("dblclick",".moreProductListShow span",function(){
		var productId = $(this).data("productId");
		var relateList = $(this).data("relateList");
		$("#"+relateList).find("option[value='"+productId+"']").remove();
		if($(this).parents(".moreProductListShow").find("span").length<=1){
			$(this).parents(".moreProductListShow").hide();
		}
		$(this).remove();
		initStatisticDemandTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initStatisticDemandTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initStatisticDemandTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initStatisticDemandTable();
	})
	$("body").on("click","button[optBtn]",function(){
		var demandId =  $(this).attr("data-demandId");
		//添加催办信息
		addBusRemind('070',demandId,function(){
			
		})
	});
	initStatisticDemandTable();
		
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
/**
 * 初始化数据展示
 * @param morePageNum
 */
function initStatisticDemandTable(morePageNum){
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
 		getSelfJSON("/statistics/suverViewPlatform/ajaxListPagedStatisticDemandProcess",params,function(result){
 			if(result.code=='0'){
 				var pageBean = result.data;
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
 initStatisticDemandTable(index);
} 
 
 function constrDataTable(statisticCrmVos){
	 if(statisticCrmVos && statisticCrmVos.length>0){
		 var rowNumIndex = 1;
		 $.each(statisticCrmVos,function(objIndex,obj){
			 
			 //一行数据
			 var tr = $("<tr></tr>");
			 $(tr).data("obj",obj);
			 var tdMod = $("<td></td>");
			 tdMod.css("text-align","center");
			 
			 //序号
			 var tdXh = $(tdMod).clone();
			 $(tdXh).html(rowNumIndex++);
			 $(tr).append(tdXh)
			 //需求编号
			 var tdSerialNum = $(tdMod).clone();
			 tdSerialNum.css("text-align","left");
			 
			 var serialNumA = $('<a href="javascript:void(0)" class="viewModInfo" ></a>')
			 $(serialNumA).html(obj.serialNum);
			 $(serialNumA).attr("busId",obj.id);
			 $(serialNumA).attr("busTYpe","070");
			 
			 $(tdSerialNum).html( $(serialNumA));
			 $(tr).append(tdSerialNum);
			 //星级等级
			 var tdStartLevel = $(tdMod).clone();
			 var startLevelHtml = constrLevelHtml(obj.startLevel);
			 $(tdStartLevel).html(startLevelHtml);
			 $(tr).append(tdStartLevel);
			 //类型
			 var tdType = $(tdMod).clone();
			 $(tdType).html(obj.typeName);
			 $(tr).append(tdType);
			 //关联项目	
			 var tdItem = $(tdMod).clone();
			 
			 var itemA = $('<a href="javascript:void(0)" class="viewModInfo" ></a>')
			 $(itemA).html(obj.itemName);
			 $(itemA).attr("busId",obj.itemId);
			 $(itemA).attr("busTYpe","005");
			 
			 $(tdItem).html($(itemA));
			 $(tr).append(tdItem);
			 
			 //发布时间
			 var tdDate = $(tdMod).clone();
			 $(tdDate).html(obj.recordCreateTime.substring(0,10));
			 $(tr).append(tdDate);
			 
			 //所属阶段
			 var tdStage = $(tdMod).clone();
			 var stageName = constrStageName(obj.state)
			 $(tdStage).html(stageName);
			 $(tr).append(tdStage);
			 
			 //阶段持续时间
			 var tdStage = $(tdMod).clone();
			 var stageCost = constrstageCost(obj);
			 $(tdStage).html(stageCost);
			 $(tr).append(tdStage);
			 
			 //阶段责任人
			 var tdHandleUser = $(tdMod).clone();
			 if(obj.state == 5 || obj.state == -1){
				 $(tdHandleUser).html('--');
			 }else{
				 $(tdHandleUser).html(obj.handleUserName);
				 
			 }
			 $(tr).append(tdHandleUser);
			 
			 //客户上次跟进日期
			 var tdOpt = $(tdMod).clone();
			 var optA = $('<button class="btn btn-info btn-primary btn-xs" optBtn type="button">催办</button>');
			 if(obj.state == 5 || obj.state == -1){
				 $(optA).attr("disabled","disabled")
			 }else{
				 $(optA).css("color","#fff");
				 $(optA).attr("data-demandId",obj.id);
			 }
			 $(tdOpt).html(optA);
			 $(tr).append(tdOpt);
			 $("#allTodoBody").append($(tr));
			 
		 })
	 }
 }
 //构建星级
 function constrLevelHtml(startLevel){
	 var span = $('<span class="rating"></span>');
	 for(var i=0;i<startLevel;i++){
		 var star = $('<span class="fa fa-star blue pull-left"></span>');
		 $(span).append(star)
	 }
	 return $(span);
 }
 //所属阶段
 function constrStageName(state){
	 if(state == -1){
		 return '不受理';
	 }else if(state == 1){
		 return '需求审核';
	 }else if(state == 2){
		 return '需求审核';
	 }else if(state == 3){
		 return '需求处理';
	 }else if(state == 4){
		 return '成果确认';
	 }else if(state == 5){
		 return '完结';
		 
	 }
	 return "";
 }
 
 function constrstageCost(obj){
	 if(obj.state==5 || obj.state ==-1){
		 return "--";
	 }
	  
	 var useTimeLong = obj.stageCostTime;
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