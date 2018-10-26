var pageNum = 0;     //页面索引初始值   
var pageSize =8;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;

var LoanApplySelectOpt = {
		loadData:function(morePageNum){
			var params = LoanApplySelectOpt.constrParam(morePageNum);
			//取得数据
     		getSelfJSON("/financial/loanApply/ajaxLoanApplyForOffSelect",params,function(data){
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
					 
					 var _dataTimeTd = $('<td></td>');
					 var recordCreateTime = loanApply.recordCreateTime.substring(0,16);
					 $(_dataTimeTd).html(recordCreateTime);
					 $(_tr).append($(_dataTimeTd));
					 
					 
					 //额度申请记录
					 var applyRecord = loanApply.flowName;
					 var _applyRecordTd = $('<td></td>');
					 
					 var applyRecordA = $('<a onclick="javascript:void(0)" class="spFlowIns"></a>');
					 $(applyRecordA).html(applyRecord);
					 $(applyRecordA).attr("insId",loanApply.instanceId);
					 
					 $(_applyRecordTd).html(applyRecordA);
					 
					 $(_applyRecordTd).attr("title",applyRecord);
					 
					 $(_tr).append($(_applyRecordTd));
					 
					 //借款限额
					 var allowedQuota = loanApply.allowedQuota;
					 var _quotaTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_quotaTd).html(allowedQuota);
					 $(_tr).append($(_quotaTd));
					 
					 //总借款
					 var loanFeeTotal = loanApply.loanFeeTotal;
					 var _loanFeeTotalTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_loanFeeTotalTd).html(loanFeeTotal);
					 $(_tr).append($(_loanFeeTotalTd));
					 
					 //总报销
					 var loanOffTotal = loanApply.loanOffTotal;
					 var _loanOffTotalTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_loanOffTotalTd).html(loanOffTotal);
					 $(_tr).append($(_loanOffTotalTd));
					 
					 var stepth = 0;
					 
					 var _optTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 var optA = $('<a href="javascript:void(0)" selectBtn>报销</a>');
					 $(optA).addClass('black');
					 $(optA).data("loanApply",loanApply);
					 $(optA).attr("stepth",stepth);
					 
					 $(_optTd).append(optA);
					 $(_tr).append($(_optTd));
					 
					 
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
		if(insId && insId > 0){
			 var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + insId;
		     openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
		}
	})
	
	LoanApplySelectOpt.loadData();
}) //窗体点击事件检测
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



