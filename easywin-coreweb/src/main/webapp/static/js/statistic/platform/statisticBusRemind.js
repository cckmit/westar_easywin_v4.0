var pageNum = 0;     //页面索引初始值   
var pageSize =10;     //每页显示条数初始化，修改显示条数，修改这里即可

var loadingIndex;
var loadDone=0;
var intervalInt;

$(function(){
	//加载数据
	busRemind.loadData(0);
	
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>');
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		busRemind.loadData(0);
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
		
		busRemind.loadData(0);
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
		
		busRemind.loadData(0);
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
			busRemind.loadData(0);
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
		busRemind.loadData(0);
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
			busRemind.loadData(0);
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
		busRemind.loadData(0);
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		busRemind.loadData(0);
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		busRemind.loadData(0);
	})
	//模糊查询
	$("body").on("blur",".moreSearch",function(){
		busRemind.loadData(0);
	})
	
	
	$("body").on("click","a[viewData-btn]",function(){
		var obj = $(this).parents("tr").data("obj");
		var busId = obj.BUSID;
		var busType = obj.BUSTYPE;
		viewModInfo(busId,busType);
		
		
	})
	$("body").on("click","a[viewData-record-btn]",function(){
		var obj = $(this).parents("tr").data("obj");
		var busId = obj.BUSID;
		var busType = obj.BUSTYPE;
		var remindUserId = obj.REMINDUSERID;
		
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		var url = "/busRemind/listPagedBusRemindForUserBus?sid="+sid;
		url = url + "&busId=" + busId;
		url = url + "&busType=" + busType;
		url = url + "&remindUserId=" + remindUserId;
		url = url + "&pager.pageSize=10";
		if(startDate){
			url = url + "&startDate=" + startDate;
		}
		if(endDate){
			url = url + "&endDate=" + endDate;
		}
		openWinByRight(url);
	})
	
})
		
var busRemind = {
	loadData:function(morePageNum){
		loadDone=0;
		layui.use('layer', function() {
			loadingIndex = layer.load(0, {
				shade: [0.5,'#fff'] //0.1透明度的白色背景
			});
			intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
			
		})
		var pageUrl = "/statistics/platform/ajaxListPagedStatisticBusRemind?sid="+sid+"&t="+Math.random();
		var params = busRemind.constaParam(morePageNum);
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
                     callback: busRemind.PageCallback,  //PageCallback() 为翻页调用次函数。
                     prev_text: "<<",
                     next_text: ">>",
                     items_per_page:pageSize,
                     num_edge_entries: 0,       //两侧首尾分页条目数
                     num_display_entries: 5,    //连续分页主体部分分页条目数
                     current_page: pageNum,   //当前页索引
                 });
               	 $("#allTodoBody").html('');
               	 if(pageBean.totalCount>0){
               		busRemind.constrDataTable(pageBean.recordList);
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
		busRemind.loadData(index);
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
	},constrDataTable:function(list){
		if(list && list[0]){
			$.each(list,function(index,object){
				var tdMod = $('<td></td>');
				$(tdMod).css("text-align","center");
				
				var tr = $('<tr></tr>');
				$(tr).data("obj",object);
				
				//序号
				var indexTd = $(tdMod).clone();
				$(indexTd).append((index + 1));
				$(tr).append($(indexTd));
				
				//催办模块
				var busModTd = $(tdMod).clone();
				$(busModTd).css("text-align","left");
				var busName = constrBusName(object.BUSTYPE);
				if(busName){
					busName = "["+busName+"]"
				}
				$(busModTd).append(busName);
				
				var busModName = object.BUSMODNAME;
				busName = busName + busModName;
				var busModA = $('<a href="javascript:void(0)" viewData-btn></a>');
				$(busModA).html(busModName);
				
				$(busModTd).append($(busModA));
				$(tr).append($(busModTd));
				
				//部门
				var depTd = $(tdMod).clone();
				$(depTd).html(object.DEPNAME);
				$(tr).append($(depTd));
				
				//被催办人员
				var userTd = $(tdMod).clone();
				$(userTd).html(object.USERNAME);
				$(tr).append($(userTd));
				
				//催办次数
				var timesTd = $(tdMod).clone();
				$(timesTd).html(object.BUSREMINDTIMES);
				$(tr).append($(timesTd));
				
				//操作
				var optTd = $(tdMod).clone();
				var optA = $('<a href="javascript:void(0)" viewData-record-btn>催办记录</a>');
				$(optTd).append($(optA));
				$(tr).append($(optTd));
				
				$("#allTodoBody").append($(tr));
				
			});
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

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else if($(ele).hasClass("noMoreShow")){
			$("#moreCondition_Div").removeClass("open");
			$("#moreCondition_Div").trigger("hideMoreDiv")
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
		
	}
}

/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}

/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	var stateBtnArray = $(".stateBtn");
	$.each(stateBtnArray,function(index,item){
		$(this).removeClass("btn-primary");
		if(!$(this).hasClass("btn-default")){
			$(this).addClass("btn-default");
		}
	})
	
	var gradeBtnArray = $(".gradeBtn");
	$.each(gradeBtnArray,function(index,item){
		$(this).removeClass("btn-primary");
		if(!$(this).hasClass("btn-default")){
			$(this).addClass("btn-default");
		}
	})
	var overBtnArray = $(".overBtn");
		$.each(overBtnArray,function(index,item){
			$(this).removeClass("btn-primary");
			if(!$(this).hasClass("btn-default")){
				$(this).addClass("btn-default");
			}
		})	
	
}