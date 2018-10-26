var pageNum = 0;     //页面索引初始值   
var pageSize =8;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;

var LoanSelectOpt = {
		loadData:function(morePageNum){
			var params = LoanSelectOpt.constrParam(morePageNum);
			//取得数据
     		getSelfJSON("/financial/loan/ajaxLoanForOffSelect",params,function(data){
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
                         callback: LoanSelectOpt.pageCallback,  //PageCallback() 为翻页调用次函数。
                         prev_text: "<<",
                         next_text: ">>",
                         items_per_page:pageSize,
                         num_edge_entries: 0,       //两侧首尾分页条目数
                         num_display_entries: 3,    //连续分页主体部分分页条目数
                         current_page: pageNum,   //当前页索引
                     });
	               	 $("#allTodoBody").html('');
	               	 if(pageBean.totalCount>0){
	               		LoanSelectOpt.constrDataTable(pageBean.recordList);
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
			LoanSelectOpt.loadData(index);
		},constrDataTable:function(loans){//构建数据表
			 if(loans && loans[0]){
				 $.each(loans,function(index,loan){
					 var _tr = $('<tr></tr>');
					 //序号
					 var rowNum = index + 1 + pageNum*pageSize;
					 var _rowNumTd = $('<td></td>');
					 $(_rowNumTd).html(rowNum);
					 $(_tr).append($(_rowNumTd));
					 
					 //申请时间
					 var applyDate = loan.recordCreateTime;
					 applyDate = applyDate.substring(0,10);
					 var _applyDateTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_applyDateTd).html(applyDate);
					 $(_tr).append($(_applyDateTd));
					 
					 //额度申请记录
					 var applyRecord = loan.flowName;
					 var _applyRecordTd = $('<td></td>');
					 $(_applyRecordTd).html(applyRecord);
					 $(_applyRecordTd).attr("title",applyRecord);
					 
					 $(_tr).append($(_applyRecordTd));
					 
					 //起止时间
					 var startTime = loan.startTime;
					 var startTimeSpan = $('<span class="black"></span>');
					 $(startTimeSpan).html(startTime);
					 
					 var endTime = loan.endTime;
					 var endTimeSpan = $('<span class="gray"></span>');
					 $(endTimeSpan).html(endTime);
					 
					 var _dateIntTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_dateIntTd).append($(startTimeSpan));
					 $(_dateIntTd).append('<br/>');
					 $(_dateIntTd).append($(endTimeSpan));
					 
					 $(_tr).append($(_dateIntTd));
					 
					 
					 var status = loan.status;
					 
					 //借款限额
					 var allowedQuota = status === 1?'--':toDecimal2(loan.allowedQuota);
					 var _quotaTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_quotaTd).html(allowedQuota);
					 $(_tr).append($(_quotaTd));
					 
					 //状态
					 var statusName = status === 1?'审核中':'审核通过';
					 var _statusTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 $(_statusTd).html(statusName);
					 $(_tr).append($(_statusTd));
					 
					 var _optTd = $('<td style="padding: 0px; text-align: center;"></td>');
					 var optA = $('<a href="javascript:void(0)" selectBtn>选择</a>')
					 $(optA).addClass(status === 1?'gray':'black');
					 $(_optTd).append(optA);
					 $(_tr).append($(_optTd));
					 
					 $(optA).data("loan",status === 1?null:loan);
					 
					 $("#allTodoBody").append(_tr);
				 })
			 }
		}
}


$(function(){
	
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initLoanTable();
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
		
		initLoanTable();
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
		
		
		initLoanTable();
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
			initLoanTable();
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
		initLoanTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initLoanTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initLoanTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initLoanTable();
	})
	
	LoanSelectOpt.loadData();
})

