var pageNum = 0;     //页面索引初始值   
var pageSize =8;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;

var LoanApplySelectOpt = {
		loadData:function(morePageNum){
			var params = LoanApplySelectOpt.constrParam(morePageNum);
			//取得数据
     		getSelfJSON("/financial/loanApply/ajaxLoanApplyForStartSelect",params,function(data){
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
                         callback: LoanApplySelectOpt.pageCallback,  //PageCallback() 为翻页调用次函数。
                         prev_text: "<<",
                         next_text: ">>",
                         items_per_page:pageSize,
                         num_edge_entries: 0,       //两侧首尾分页条目数
                         num_display_entries: 3,    //连续分页主体部分分页条目数
                         current_page: pageNum,   //当前页索引
                     });
	               	 $("#allTodoBody").html('');
	               	 if(pageBean.totalCount>0){
	               		LoanApplySelectOpt.constrDataTable(pageBean.recordList);
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
     		})
		},constrParam:function(morePageNum){//构建数据
			if(morePageNum){
				pageNum = morePageNum;
			}else{
				pageNum = 0;
			}
			var params={"sid":sid,
               	 "pageNum":pageNum,
               	 "pageSize":pageSize,
               	 "isBusinessTrip":pageParam.isBusinessTrip
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
		},pageCallback:function(index, jq){//翻页
			pageNum = index;
			LoanApplySelectOpt.loadData(index);
		},constrDataTable:function(loanApplys){//构建数据表
			 if(loanApplys && loanApplys[0]){
				 $.each(loanApplys,function(index,loanApply){
					 var _tr = $('<tr></tr>');
					 //序号
					 var rowNum = index + 1 + pageNum*pageSize;
					 var _rowNumTd = $('<td></td>');
					 $(_rowNumTd).html(rowNum);
					 $(_tr).append($(_rowNumTd));
					 
					 //额度申请记录
					 var applyRecord = loanApply.flowName;
					 var _applyRecordTd = $('<td></td>');
					 var applyRecordA = $('<a onclick="javascript:void(0)" class="spFlowIns"></a>');
					 $(applyRecordA).html(applyRecord);
					 $(applyRecordA).attr("insId",loanApply.instanceId);
					 
					 $(_applyRecordTd).html(applyRecordA);
					 $(_applyRecordTd).attr("title",applyRecord);
					 
					 $(_tr).append($(_applyRecordTd));
					 
					 //出差地点
					 var applyTripPlace = loanApply.tripPlace;
					 var _applyPlaceTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_applyPlaceTd).html(applyTripPlace);
					 $(_tr).append($(_applyPlaceTd));
					 
					 //起止时间
					 var startTime = loanApply.startTime;
					 var startTimeSpan = $('<span class="black"></span>');
					 $(startTimeSpan).html(startTime);
					 
					 var endTime = loanApply.endTime;
					 var endTimeSpan = $('<span class="gray"></span>');
					 $(endTimeSpan).html(endTime);
					 
					 var _dateIntTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_dateIntTd).append($(startTimeSpan));
					 $(_dateIntTd).append('<br/>');
					 $(_dateIntTd).append($(endTimeSpan));
					 
					 $(_tr).append($(_dateIntTd));
					 
					 
					 var status = loanApply.status;
					 
					 //借款限额
					 var allowedQuota = status === 1?'--':toDecimal2(loanApply.allowedQuota);
					 var _quotaTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_quotaTd).html(allowedQuota);
					 $(_tr).append($(_quotaTd));
					 
					 //总借款
					 var loanFeeTotal = status === 1?'--':toDecimal2(loanApply.loanFeeTotal);
					 var _loanFeeTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_loanFeeTd).html(loanFeeTotal);
					 $(_tr).append($(_loanFeeTd));
					 
					 //总销账
					 var loanOffTotal = status === 1?'--':toDecimal2(loanApply.loanOffTotal);
					 var _loanOffTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_loanOffTd).html(loanOffTotal);
					 $(_tr).append($(_loanOffTd));
					 
					 //状态
//					 var statusName = status === 1?'审核中':'审核通过';
//					 var _statusTd = $('<td style="padding: 0px; text-align: center;"></td>');
//					 $(_statusTd).html(statusName);
//					 $(_tr).append($(_statusTd));
					 
					 var _optTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 var optA = $('<a href="javascript:void(0)" selectBtn>借款</a>')
					 $(optA).addClass(status === 1?'gray':'black');
					 $(_optTd).append(optA);
					 $(_tr).append($(_optTd));
					 
					 $(optA).data("loanApply",status === 1?null:loanApply);
					 
					 $("#allTodoBody").append(_tr);
				 })
			 }
		}
}


$(function(){
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
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		LoanApplySelectOpt.loadData();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		LoanApplySelectOpt.loadData();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		LoanApplySelectOpt.loadData();
	})
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId){
			 var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + insId;
		     openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
		}
	})
	
	LoanApplySelectOpt.loadData();
})

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
