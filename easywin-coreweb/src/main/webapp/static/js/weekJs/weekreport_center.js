var pageNum = 0;     //页面索引初始值   
var pageSize =30;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;
$(function(){
	
	var height = $(window).height()-120;
	$("#editabledatatable").parent().parent().css("max-height",height + "px");
	
	$('body').on('click','label',function(){
		$('.event_year>li').removeClass('current');
		$(this).parent('li').addClass('current');
		var year = $(this).attr('for');
		$('#'+year).parent().prevAll('div').slideUp(800);
		$('#'+year).parent().slideDown(800).nextAll('div').slideDown(800);
	});

	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initWeekRepTable();
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
		
		initWeekRepTable();
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
		
		
		initWeekRepTable();
	})
	
	$("body").on("click","#weekRepMore",function(){
		pageNum++;
		initWeekRepTable(pageNum);
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
			initWeekRepTable();
		})
	});
	//双击移除
	$("body").on("dblclick",".moreUserListShow span",function(){
		var userId = $(this).data("userId");
		var relateList = $(this).data("relateList");
		if(!userId){
			userId = $(this).attr("data-userId");
			relateList = $(this).attr("relateList");
		}
		$("#"+relateList).find("option[value='"+userId+"']").remove();
		if($(this).parents(".moreUserListShow").find("span").length<=1){
			$(this).parents(".moreUserListShow").hide();
		}
		$(this).remove();
		initWeekRepTable();
	})
	
	//部门单选
	$("body").off("click",".depOneElementSelect").on("click",".depOneElementSelect",function(){
		var depId = $(this).attr("relateElement");
		var depName = $(this).attr("relateElementName");
		var _this = $(this);
		depOne(depId, depName, null, sid,function(result){
			 $("#formTempData").find("input[name='"+depId+"']").val(result.orgId);
			 $("#formTempData").find("input[name='"+depName+"']").val(result.orgName);
			 
			 var _font = $('<font style="font-weight:bold;"></font>')
			$(_font).html(result.orgName)
			var _i = $('<i class="fa fa-angle-down"></i>')
			
			$(_this).parents("ul").prev().html(_font);
			$(_this).parents("ul").prev().append(_i);
				
			 initWeekRepTable();
		})
	});
	
	
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
			initWeekRepTable();
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
		initWeekRepTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initWeekRepTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initWeekRepTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initWeekRepTable();
	})
	//设置年份选择和周数选择
	getSelfJSON('/weekReport/findOrgInfo',{"sid":sid},function(data){
		//团队注册时间年份
		var registYear = data.registYear;
		//当前时间年份
		var nowYear = data.nowYear;
		//添加年份选择
		for(var index = nowYear ; index >= registYear ; index--){
			var _li = $('<li></li>');
			var _a = $('<a href="javascript:void(0)"></a>');
			$(_a).attr("data-dataValue",index);
			$(_a).html(index+'年');
			$(_li).append($(_a));
			$("#weekYearUl").append($(_li));
			
		}
		$("body").on("click","a[data-dataValue]",function(){
			var infoA  = $("#weekYearUl").prev();
			var dataValue = $(this).attr("data-dataValue");
			$(infoA).html('<font style="font-weight:bold;">'+dataValue+'年</font>')
			$(infoA).append('<i class="fa fa-angle-down"></i>');
			$("body").find("#weekYear").val(dataValue);
			constrWdatePicker(dataValue);
			initWeekRepTable(0);
			
			
		})
		
	})
	
	constrWdatePicker(pageParam.nowYear);
	initWeekRepTable();
		
})

function constrWdatePicker(year){
	$(document).off("focus","#startDate").on("focus","#startDate",function(){
		WdatePicker({dateFmt:'yyyy年MM月dd日',maxDate: '#F{$dp.$D(\'endDate\',{d:-0})||\''+year+'年12月31日\';}',minDate:year+'年01月01日'  })
	})
	$(document).off("focus","#endDate").on("focus","#endDate",function(){
		WdatePicker({dateFmt:'yyyy年MM月dd日',minDate: '#F{$dp.$D(\'startDate\',{d:+0})||\''+year+'年01月01日\';}',maxDate:year+'年12月31日'})
	})
}
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

var preMonth = 0;
var preWeek;
var monthTop = {};

//出差管理
function initWeekRepTable(morePageNum){
	loadDone=0;
	layui.use('layer', function() {
		loadingIndex = layer.load(0, {
			shade: [0.5,'#fff'] //0.1透明度的白色背景
		});
		intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
	})
	if(morePageNum){
		pageNum = morePageNum;
	}else{
		depScoreObj = {};
		pageNum = 0;
		//前一个部门主键
		preDepId = 0;
		//重复数
		preDepNum = 0;
		preMonth = 0
		preWeek = 0;
		monthTop = {}
		$(".event_list").html('');
		$(".event_year").html('');
	}
	if(pageParam.searchTab =='11'){
		pageSize = 10;
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
 		getSelfJSON("/weekReport/ajaxListPagedWeekreport",params,function(data){
 			if(data.status=='y'){
 				var pageBean =  data.pageBean;
 				var totalCount = pageBean.totalCount;
                $("#weekRepMore").show();
                var currentPage = pageSize * pageNum + pageBean.recordList.length;
                if(totalCount === currentPage){
                    $("#weekRepMore").hide();
                }
 				if(pageParam.searchTab =='11'){
 					
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
	               		constrData(pageBean.recordList);
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
	               	 
 				}else{
 					constrDataV2(pageBean.recordList);
 				}
 			}
 			loadDone = 1;
 		})
 		
}

//翻页调用   
function PageCallback(index, jq) {  
  	pageNum = index;
  	initWeekRepTable(index);
} 
/**
 * 构建数据
 * @param dataList
 */
function constrDataV2(dataList){
	if(dataList && dataList[0]){
		$.each(dataList,function(index,weekReportVo){
			//填写周报的所在周数的最后一天
			var weekE = weekReportVo.weekE;
			var weekMonth = weekE.substring(5,7);
			var weekYear = weekE.substring(0,4);
			//取得月份
			weekMonth = +weekMonth;
			//与前一个月份不相同，添加月份显示
			if(weekMonth!=preMonth){
				
				var $li = $('<li></li>');
				//是今年的当月
				if(weekMonth==pageParam.nowMonth && weekYear == pageParam.nowYear){
					$li.addClass("current");
				}else if(weekYear != pageParam.nowYear && !preMonth){//默认选中第一个显示的月份
					$li.addClass("current");
				}
				//缓存前一月份的数据
				preMonth = weekMonth;
				//快捷时间轴上显示月份
				var $label = $('<label></label>');
				$($label).attr("for","month_"+preMonth);
				$($label).html(preMonth+"月");
				$($li).append($($label));
				
				$(".event_year").append($($li));
			}
			
			var hTop = monthTop[weekMonth];
			//判断时间轴上是否已显示月份,若不存在，则添加数据
			if(!hTop || !hTop.get(0)){
				var div = $('<div></div>');
				var hTop = $('<h3></h3>');
				$(hTop).attr("id","month_"+weekMonth);
				$(hTop).html(weekMonth+"月");
				monthTop[weekMonth] = $(hTop);
				$(div).append($(hTop));
				$(".event_list").append($(div));
			}
			
			//月份时间所在的区域
			var div = $(hTop).parent();
			//添加周报数据开始
			var li = $('<li></li>');
			var lispan = $('<span></span>');
			var indexStart = weekReportVo.weekRepName.indexOf('年第');
			//周数
			var week = weekReportVo.weekRepName.substring(indexStart+2);
			
			//不是同一周的，在时间轴左边显示周数和时间
			if(!preWeek || preWeek != week){
				preWeek = week;
				var weekS = weekReportVo.weekS.substring(weekReportVo.weekS.indexOf('月')+1)
				$(lispan).append(weekS);
				$(lispan).append('~');
				var weekE = weekReportVo.weekE.substring(weekReportVo.weekE.indexOf('月')+1)
				$(lispan).append(weekE);
				$(lispan).append("("+week+")");
			}
			$(li).append($(lispan));
			
			//周报标题信息
			var p = $('<p></p>');
			var contentSpan = $('<span></span>');
			
			//周报名称以及操作显示
			var weekNameA;
			if(weekReportVo.state==0){
				weekNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'week\',0,\'006\');viewWeekReport('+weekReportVo.id+');"></a>')
				$(weekNameA).html(weekReportVo.weekRepName);
			}else if(weekReportVo.year==pageParam.nowYear && weekReportVo.weekNum==pageParam.nowWeekNum 
					&& weekReportVo.reporterId==pageParam.userInfo.id){
				weekNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'week\',0,\'006\');addWeekReport(\''+weekReportVo.weekS+'\');"></a>')
				$(weekNameA).html(weekReportVo.weekRepName);
				$(contentSpan).css("background-color","#fb6e52");
				$(contentSpan).css("color","#fff");
				$(weekNameA).css("color","#fff");
			}else if(weekReportVo.reporterId==pageParam.userInfo.id){
				weekNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'week\',0,\'006\');addWeekReport(\''+weekReportVo.weekS+'\');"></a>')
				$(weekNameA).html(weekReportVo.weekRepName);
				$(contentSpan).css("background-color","#fb6e52");
				$(contentSpan).css("color","#fff");
				$(weekNameA).css("color","#fff");
			}else{
				weekNameA = weekReportVo.weekRepName;
				$(contentSpan).css("background-color","#fb6e52");
				$(contentSpan).css("color","#fff");
			}
			$(contentSpan).html(weekNameA);
			
			//周报日期以及操作显示
			var dateSpan = $('<lable class="pull-right"></lable>');
			if(weekReportVo.state==0){
				$(dateSpan).append(weekReportVo.recordCreateTime.substring(0,19));
			}else if(weekReportVo.year==pageParam.nowYear && weekReportVo.weekNum==pageParam.nowWeekNum 
					&& weekReportVo.reporterId==pageParam.userInfo.id){
				var weekA = $('<a href="javascript:void(0);" style="color: blue" onclick="readMod(this,\'week\',0,\'006\');addWeekReport(\''+weekReportVo.weekS+'\');"> 发布 </a>')
				$(dateSpan).append(weekA);
				$(weekA).css("color","#fff");
			}else if(weekReportVo.reporterId==pageParam.userInfo.id){
				var weekA = $('<a href="javascript:void(0);" style="color: blue" onclick="readMod(this,\'week\',0,\'006\');addWeekReport(\''+weekReportVo.weekS+'\');"> 补发 </a>')
				$(dateSpan).append(weekA);
				$(weekA).css("color","#fff");
			}else{
				$(dateSpan).append('未发布');
			}
			$(contentSpan).append(dateSpan);
			
			$(p).append(contentSpan);
			//周报内容添加结束
			
			$(li).append($(p));
			
			$(div).append(li);
			//周报数据添加结束
			
		})
	}
}

function constrData(dataList){
	var weekIndex = 1;
	if(dataList && dataList[0]){
		$.each(dataList,function(index,weekReportVo){
			var weekTr = $('<tr></tr>');
			//序号
			var xhTd = $('<td valign="middle"></td>');
			$(xhTd).append(weekIndex++);
			$(weekTr).append(xhTd);
			
			//名称
			var weekNameTd = $('<td valign="middle" class="hidden-phone"></td>');
			
			//周报名称
			var weekNameA;
			//周报填写时间
			var dateTime;
			//周报时间区域
			var weekScope;
			
			var weekS =weekReportVo.weekS.substring(5,11);
			var weekE =weekReportVo.weekE.substring(5,11);
		
			if(weekReportVo.state==0){//周报已发布
				weekNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'week\',0,\'006\');viewWeekReport('+weekReportVo.id+');"></a>')
				
				if(weekReportVo.isRead==0){
					$(weekNameA).addClass('noread');
				}
				
				dateTime = $(weekNameA).clone();
				weekScope = $(weekNameA).clone();
				$(weekNameA).html(weekReportVo.weekRepName);
				dateTime = weekReportVo.recordCreateTime.substring(0,19);
				$(weekScope).append(weekS+"~"+weekE);
				
			}else if(weekReportVo.year==pageParam.nowYear //同年份
					&& weekReportVo.weekNum==pageParam.nowWeekNum //同周数
					&& weekReportVo.reporterId==pageParam.userInfo.id){//当前操作人员发布
				weekNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'week\',0,\'006\');addWeekReport(\''+weekReportVo.weekS+'\');"></a>')
				if(weekReportVo.isRead==0){
					$(weekNameA).addClass('noread');
				}
				dateTime = $(weekNameA).clone();
				weekScope = $(weekNameA).clone();
				$(weekNameA).html(weekReportVo.weekRepName);
				$(dateTime).append('发布');
				$(weekScope).append(weekS+"~"+weekE);
				$(weekNameA).css("color","red");
				$(dateTime).css("color","red");
			}else if(weekReportVo.reporterId==pageParam.userInfo.id){//当前操作人员补发
				weekNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'week\',0,\'006\');addWeekReport(\''+weekReportVo.weekS+'\');"></a>')
				if(weekReportVo.isRead==0){
					$(weekNameA).addClass('noread');
				}
				dateTime = $(weekNameA).clone();
				weekScope = $(weekNameA).clone();
				$(weekNameA).html(weekReportVo.weekRepName);
				$(dateTime).append('补发');
				$(weekScope).append(weekS+"~"+weekE);
				$(weekNameA).css("color","red");
				$(dateTime).css("color","red");
			}else{//查看未发布
				weekNameA = weekReportVo.weekRepName;
				dateTime = '未发布';
				weekScope = weekS+"~"+weekE;
				$(weekNameTd).css("color","red");
			}
			$(weekNameTd).html(weekNameA);
			$(weekTr).append(weekNameTd);
			//汇报范围
			var weekScopeTd = $('<td valign="middle"></td>');
			$(weekScopeTd).append(weekScope);
			$(weekTr).append(weekScopeTd);
			
			//部门
			var depTd = $('<td valign="middle"></td>');
			$(depTd).append(weekReportVo.depName);
			$(weekTr).append(depTd);
			
			//汇报时间
			var dateTimeTd = $('<td valign="middle"></td>');
			if(dateTime=='未发布'){
				$(dateTimeTd).css("color","red");
			}
			$(dateTimeTd).append(dateTime);
			$(weekTr).append(dateTimeTd);
			
			//头像
			var imgTd = $('<td valign="middle"></td>');
			var imgDiv = $('<div class="ticket-user pull-left other-user-box"></div>');
			var img = $('<img class="user-avatar" />');
			
			$(img).attr("src","/downLoad/userImg/"+weekReportVo.comId+"/"+weekReportVo.reporterId);
			$(imgDiv).append(img);
			var spanName = $('<span class="user-name">'+weekReportVo.userName+'</span>');
			$(imgDiv).append(spanName);
			
			$(imgTd).append(imgDiv);
			$(weekTr).append(imgTd);
			
			$("#allTodoBody").append($(weekTr));
			
		})
	}
}
 
 

