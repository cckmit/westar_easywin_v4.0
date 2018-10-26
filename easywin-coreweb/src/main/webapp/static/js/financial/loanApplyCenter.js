var pageNum = 0;     //页面索引初始值   
var pageSize =10;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;
$(function(){
	//加载数据
	initLoanApplyTable();
	
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initLoanApplyTable();
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
		
		initLoanApplyTable();
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
		
		
		initLoanApplyTable();
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
			initLoanApplyTable();
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
		initLoanApplyTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initLoanApplyTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initLoanApplyTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initLoanApplyTable();
	})
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		//发起出差申请或一般借款申请
		$("[name='addLoanApply']").click(function(){//发起出差
			var busType = $(this).attr("busType");
			listBusMapFlows(busType,function(busMapFlow){
				var url = "/busRelateSpFlow/loanApply/addLoanApply?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType="+busType;
				openWinByRight(url);
			})
		});
		//添加借款
		$("body").on("click",".addLoan",function(){
			var actObj = $(this);
			var loanApplyData = $(actObj).parents("tr").data("loanApplyData");
			if(!loanApplyData){
				loanApplyData = $("#subDataDiv").data("loanApplyData");
			}
			var feeBudgetId = loanApplyData.id;
			var busType=loanApplyData.isBusinessTrip==1?'030':'031';
			getSelfJSON("/financial/checkLoanOffAll",{sid:sid,feeBudgetId:feeBudgetId,busType:busType},function(data){
				if(data.status=='y'){
					listBusMapSelect(data.listBusMapFlows,busType,function(busMapFlow){
						var url = "/busRelateSpFlow/loan/addLoan?sid="+sid;
						url +="&busMapFlowId="+busMapFlow.id
						url +="&feeBudgetId="+feeBudgetId
						url +="&busType="+busType
						openWinByRight(url);
					})
				}else{
					showNotification(2,data.info);
				}
			})
			
		})
		//报销；从发起报销说明开始
		$("body").on("click",".addLoanReport",function(){
			var actObj = $(this);
			var loanApplyData = $(actObj).parents("tr").data("loanApplyData");
			if(!loanApplyData){
				loanApplyData = $("#subDataDiv").data("loanApplyData");
			}
			var feeBudgetId = loanApplyData.id;
			postUrl("/financial/checkFeeBudget4LoanOff",{sid:sid,feeBudgetId:feeBudgetId},function(data){
				if(data.status=='y'){
					if(data.reports && data.reports[0]){//报销失败的
						addLoanOff(data.reports[0].id,data.reports[0].feeBudgetId);
					}else{
						addLoanReport(actObj);//发起报销说明
					}
				}else{
					if(data.feeLoanOffOnDoing){
						if(data.feeLoanOffOnDoing.instanceId){//报销有正在进行中的，直接查看进度
							window.top.layer.confirm('报销进行中，是否查看进度？', {icon: 3, title:'确认对话框',
							  btn: ['查看','取消'] //按钮
							}, function(index){
								window.top.layer.close(index);//关闭确认对话框
								viewSpFlow(data.feeLoanOffOnDoing.instanceId);
							});
						}else{
							addLoanOff(data.feeLoanOffOnDoing.loanReportId,data.feeLoanOffOnDoing.feeBudgetId);
							//showNotification(2,"需要去“报销记录”手动发起差旅费报销单！");
						}
					}else{
						showNotification(2,data.info);
					}
				}
			});
		})
		//发起报销单
		$("body").on("click",".addLoanOff",function(){
			var loanOff = $(this).parents("tr").data("loanOffData");
			var feeBudgetId = $("#subDataDiv").data("loanApplyData").id;
			var loanReportId = loanOff.loanReportId;
			getSelfJSON("/financial/checkLoanRep4Off",{sid:sid,loanReportId:loanReportId,feeBudgetId:feeBudgetId},function(data){
				if(data.status=='y'){
					var busType = data.busType;
					listBusMapSelect(data.listBusMapFlows,busType,function(busMapFlow){
						var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
						url +="&busMapFlowId="+busMapFlow.id
						url +="&feeBudgetId="+feeBudgetId
						url +="&loanReportId="+loanReportId
						url +="&busType="+busType
						url +="&loanWay=1"
						url +="&loanReportWay=1"
						openWinByRight(url);
					})
				}else{
					showNotification(2,data.info);
				}
			})
		})
		//查看审批
		$("body").on("click",".spFlowIns",function(){
			var insId = $(this).attr("insId");
			if(insId && insId>0){
				viewSpFlow(insId);
			}
		})
		//查看借款
		$("body").on("click",".loanTimes",function(){
			var loanApplyData = $(this).parents("tr").data("loanApplyData");
			
			 var listLoan = loanApplyData.listFeeLoan;
			 if(listLoan && listLoan.length>0){
				$("#subDataDiv").data("loanApplyData",loanApplyData);
				
				$(".subTitle").html("&lt;&lt;"+loanApplyData.flowName);
				$(".subType").html("--借款记录");
				
				var addLoanState = $(this).parents("tr").data("addLoanState");
				if(addLoanState && addLoanState==1){
					var addLoanBtn = $('<buttontype="button" onclick="javascript:void(0);"></button>')
					$(addLoanBtn).addClass("btn btn-info btn-primary btn-xs margin-left-5 addLoan");
					$(addLoanBtn).html('<i class="fa fa-plus"></i>借款');
					$(".subBtn").html(addLoanBtn);
				}
				
				$("#mainDataDiv").hide(1000,null);
				$("#subDataDiv").show(1000,initLoanTable);
			 }
			
		})
		//查看汇报
		$("body").on("click",".loanRepTimes",function(){
			var loanApplyData = $(this).parents("tr").data("loanApplyData");
			
			 var listLoanOff = loanApplyData.listLoanOff;
			 if(listLoanOff && listLoanOff.length>0){
				$("#subDataDiv").data("loanApplyData",loanApplyData);
				
				$(".subTitle").html("&lt;&lt;"+loanApplyData.flowName);
				$(".subType").html("--汇报记录");
				
				var addLoanRepState = $(this).parents("tr").data("addLoanRepState");
				if(addLoanRepState && addLoanRepState==1){
					var addLoanBtn = $('<button type="button" onclick="javascript:void(0);"></button>')
					$(addLoanBtn).addClass("btn btn-info btn-primary btn-xs margin-left-5 addLoanReport");
					$(addLoanBtn).html('<i class="fa fa-plus"></i>报销');
					$(".subBtn").html(addLoanBtn);
				}
				
				$("#mainDataDiv").hide(1000,null);
				$("#subDataDiv").show(1000,initLoanOffTable);
			 }
			
		})
			
		//返回列表
		$("body").on("click",".subTitle",function(){
			$("#subDataDiv").hide(1000,null);
			$("#mainDataDiv").show(1000,null);
			$("#subDataDiv").removeData("loanApplyData");
		})
})
/**
 * 新增费用报销说明
 * @param actObj
 * @returns
 */
function addLoanReport(actObj) {
	var loanApplyData = $(actObj).parents("tr").data("loanApplyData");
	if (!loanApplyData) {
		loanApplyData = $("#subDataDiv").data("loanApplyData");
	}
	var feeBudgetId = loanApplyData.id;
	var busType = '03401';
	if (loanApplyData.isBusinessTrip == 0) {
		busType = '03402'
	}
	listBusMapFlows(busType, function(busMapFlow) {
		var url = "/busRelateSpFlow/loanReport/addLoanReport?sid=" + sid
				+ "&busMapFlowId=" + busMapFlow.id + "&feeBudgetId=" + feeBudgetId;
		url = url + "&busType=" + busType
		openWinByRight(url);
	})
}
//新增报销申请
function addLoanOff(loanReportId,feeBudgetId){
	if(loanReportId && loanReportId>0){
		getSelfJSON("/financial/checkLoanRep4Off",{sid:sid,loanReportId:loanReportId,feeBudgetId:feeBudgetId},function(data){
			if(data.status=='y'){
				var busType = data.busType;
				listBusMapSelect(data.listBusMapFlows,busType,function(busMapFlow){
					var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
					url +="&busMapFlowId="+busMapFlow.id
					url +="&feeBudgetId="+feeBudgetId
					url +="&loanReportId="+loanReportId
					url +="&busType="+busType
					url +="&loanWay=1";
					url +="&loanReportWay=1"
						openWinByRight(url);
				})
			}else{
				showNotification(2,data.info);
			}
		})
	}else{
		//直接报销03102
		listBusMapFlows('03102',function(busMapFlow){
			var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
			url +="&busMapFlowId="+busMapFlow.id
			url +="&feeBudgetId="+feeBudgetId
			url +="&loanReportId=0"
			url +="&busType=03102";
			url +="&loanWay=1";//额度借款
			url +="&loanReportWay=1";//需要汇报的
			openWinByRight(url);
		});
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
var loadingIndex;
var loadDone=0;
var intervalInt;

//出差管理
function initLoanApplyTable(morePageNum){
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
 		getSelfJSON("/financial/loanApply/ajaxListLoanApplyOfAuth",params,function(data){
 			loadDone = 1;
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
               		 constrTripDataTable(pageBean.recordList,nowDate);
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
}
 //翻页调用   
function PageCallback(index, jq) {  
 pageNum = index;
 initLoanApplyTable(index);
} 
 //统计列表异步构建
 function constrTripDataTable(loanApplys,nowDate){
	 if(loanApplys && loanApplys.length>0){
		 var rowNumIndex = 1;
		 var preReptNum = 1;
		 var tempLoanRepId = 0;
		 $.each(loanApplys,function(loanApplyIndex,loanApplyObj){
			 if(loanApplyObj.initStatus==1){
				 loanApplyObj.flowName='2018年2月28日前未销账金额初始化';
				 loanApplyObj.flowState=4;
				 loanApplyObj.spState=1;
				 loanApplyObj.status=4;
			 }
			 
			 var busId = loanApplyObj.busId;
			 var busTypeName = loanApplyObj.busType == '005'?"【项目】":loanApplyObj.busType == '012'?"【客户】":"";
			 //一行数据
			 var _tr = $("<tr></tr>");
			 $(_tr).attr("loanRepId",loanApplyObj.id);
			 var tdMod = $("<td></td>");
			 tdMod.css("text-align","center")
			 tdMod.css("padding","0")
			 
			 var loanRepId = loanApplyObj.id;
			 if(tempLoanRepId == loanRepId){//重复数据合并行数据
				 preReptNum  = preReptNum+1;
				 //添加项目
				 if(loanApplyObj.isBusinessTrip!=0){
					 var _tdItemName = $(tdMod).clone();
					 
					 $(_tdItemName).css("height","30px");
					 $(_tdItemName).css("text-align","left");
					 var _modA = $("<a href='javascript:void(0)'></a>");
					 
					 $(_modA).attr("busId",loanApplyObj.busId);
					 $(_modA).attr("busType",loanApplyObj.busType);
					 $(_modA).addClass("viewModInfo");
					 $(_modA).html(loanApplyObj.tripBusName);
					 
					 $(_tdItemName).html(busTypeName);
					 $(_tdItemName).append($(_modA));
					 
					 $(_tdItemName).attr("title",loanApplyObj.tripBusName)
					 $(_tr).append(_tdItemName)
				 }
				 
				 $("#allTodoBody").append(_tr);
				 $(_tr).data("loanApplyData",loanApplyObj);
				 $("#allTodoBody").find("tr[loanRepId='"+loanRepId+"']:eq(0)").find("td").attr("rowspan",preReptNum);
				 $("#allTodoBody").find("tr[loanRepId='"+loanRepId+"']:eq(0)").find("td:eq(3)").attr("rowspan",1);
				 return true;
				 
			 }else{
				 tempLoanRepId = loanRepId;
				 preReptNum = 1;
			 }
			 
			
			 //序号
			 var _tdXh = $(tdMod).clone();
			 $(_tdXh).html(rowNumIndex++)
			  $(_tr).append(_tdXh)
			 //申请人
			 var _tdUser = $(tdMod).clone();
			 $(_tdUser).html(loanApplyObj.creatorName)
			 $(_tr).append(_tdUser)
			 
			 //出差流程	
			 var _tdlc = $(tdMod).clone();
			 var _flowNameA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
			 $(_flowNameA).attr("insId",loanApplyObj.instanceId)
			 _flowNameA.css("padding-left","5px")
			 
			 //var loanApplyFlowName = loanApplyObj.flowName.replace(loanApplyObj.creatorName,'');
			 var loanApplyFlowName = loanApplyObj.flowName;
			 $(_flowNameA).html(loanApplyFlowName)
			 $(_flowNameA).attr("title",loanApplyFlowName)
			 
			 $(_tdlc).html(_flowNameA)
			 $(_tdlc).css("text-align","left")
			 $(_tr).append(_tdlc)
			 
			 var _tdItemName = $(tdMod).clone();
			 $(_tdItemName).css("height","30px");
			 $(_tdItemName).css("text-align","left");
			 var _modA = $("<a href='javascript:void(0)'></a>");
			 
			 $(_modA).attr("busId",loanApplyObj.busId);
			 $(_modA).attr("busType",loanApplyObj.busType);
			 $(_modA).addClass("viewModInfo");
			 $(_modA).html(loanApplyObj.tripBusName);
			 
			 $(_tdItemName).html(busTypeName);
			 $(_tdItemName).append($(_modA));
			 $(_tdItemName).attr("title",loanApplyObj.tripBusName)
			 $(_tr).append(_tdItemName)
			 
			 var _tdPlace = $(tdMod).clone();
			 $(_tdPlace).html(loanApplyObj.tripPlace)
			 $(_tr).append(_tdPlace)
			 
			 //开始时间	
			 var _tdDate = $("<td></td>");
			 var _spanS = $("<span></span>")
			 _spanS.html(loanApplyObj.startTime)
			 _spanS.addClass("black")
			 var _spanE = $("<span></span>")
			 _spanE.html(loanApplyObj.endTime)
			 _spanE.addClass("gray")
			 $(_tdDate).append(_spanS)
			 $(_tdDate).append("<br>")
			 $(_tdDate).append(_spanE)
			 _tdDate.css("padding","0")
			 _tdDate.css("text-align","center")
			 $(_tr).append(_tdDate)
			 
			//结束时间
			  var _tdDays = $("<td></td>");
			  _tdDays.css("padding","0")
			  _tdDays.css("text-align","center")
			  if(loanApplyObj.startTime && loanApplyObj.endTime){
				  $(_tdDays).html(DateDiff(loanApplyObj.startTime,loanApplyObj.endTime,'d'));
			  }else{
				  $(_tdDays).html('--');
			  }
			  $(_tr).append(_tdDays)
			 
			//借款额度
			var _tdJkTop = $(tdMod).clone();
			var status = loanApplyObj.status;
			if(status == 1){
				$(_tdJkTop).css('color','#428bca');
				$(_tdJkTop).html('审核中');
			}else if(status == -1){
				$(_tdJkTop).css('color','red');
				$(_tdJkTop).html('失败');
			}else if(status == 4){
				$(_tdJkTop).html(toDecimal2(loanApplyObj.allowedQuota));
			}else{
				$(_tdJkTop).html('待申请');
			}
			$(_tdJkTop).css("text-align","center")
			$(_tr).append(_tdJkTop)
				
			 //借款合计
			 var _tdLoanDone = $(tdMod).clone();
			 var loanFeeTotal = loanApplyObj.loanFeeTotal;
			 var _loanTimesA = $("<a href='javascript:void(0)' class='loanTimes'></a>");
			 if(loanFeeTotal>0){
				 loanFeeTotal = toDecimal2(loanFeeTotal)
				 $(_loanTimesA).attr("title",loanFeeTotal);
			 }
			 $(_tdLoanDone).html($(_loanTimesA).clone().html(loanFeeTotal))
			 $(_tdLoanDone).css("text-align","center")
			 $(_tr).append(_tdLoanDone)
			 
			 //报销合计
			 var _td10 = $(tdMod).clone();
			 var _loanOffA = $("<a href='javascript:void(0)' class='loanRepTimes'></a>");
			 var loanItemTotal = loanApplyObj.loanItemTotal;
			 if(loanItemTotal>0){
				 loanItemTotal = toDecimal2(loanItemTotal);
				 $(_loanOffA).attr("title",loanItemTotal);
			 }
			 $(_loanOffA).html(loanItemTotal)
			 $(_td10).html(_loanOffA)
			 $(_td10).css("text-align","center")
			 $(_tr).append(_td10);
			 
			 //销账合计
			 var _td11 = $(tdMod).clone();
			 var _loanOffA = $("<a href='javascript:void(0)' class='loanRepTimes'></a>");
			 var loanOffTotal = loanApplyObj.loanOffTotal;
			 if(loanOffTotal>0){
				 loanOffTotal = toDecimal2(loanOffTotal);
				 $(_loanOffA).attr("title",loanOffTotal);
			 }
			 $(_loanOffA).html(loanOffTotal)
			 $(_td11).html(_loanOffA)
			 $(_td11).css("text-align","center")
			 $(_tr).append(_td11);
			 
			 //操作列
			 var _tdOpt = $("<td></td>");
			_tdOpt.css("padding","10px 0px")
			_tdOpt.css("text-align","center")
			if(loanApplyObj.creator==EASYWIN.userInfo.id){//操作人是数据发起人时
				
				if(loanApplyObj.directBalanceState != 1){
					var canFeeLoanTag = canFeeLoan(loanApplyObj.endTime,nowDate,loanApplyObj.allowedQuota,loanApplyObj.loanFeeTotal);
					var canFeeLoanOffTag = canFeeLoanOff(loanApplyObj);
					if(canFeeLoanTag){//能否继续借款验证
						var _loanA = $("<a href='javascript:void(0)' class='blue addLoan'></a>");
						$(_loanA).append("借款")
						$(_tdOpt).append(_loanA)
					}
					if(canFeeLoanTag && canFeeLoanOffTag){//两者都有时，才需要
						$(_tdOpt).append("|")
					}
					if(canFeeLoanOffTag){//能否继续报销验证
						var _loanRepA = $("<a href='javascript:void(0)' class='blue addLoanReport'></a>");
						$(_loanRepA).append("报销")
						$(_tdOpt).append(_loanRepA)
					}
					if((canFeeLoanTag || canFeeLoanOffTag) && loanApplyObj.canDirectBalance == 1){
						$(_tdOpt).append("|")
					}
					if(loanApplyObj.canDirectBalance == 1){
						 
						var _loanRepA = $("<a href='javascript:void(0)' class='blue' onclick='directBalance("+loanApplyObj.id+"," +loanApplyObj.loanFeeTotal+")'></a>");
						$(_loanRepA).append("不予报销")
						$(_tdOpt).append(_loanRepA)
					}
					if(!canFeeLoanTag && !canFeeLoanOffTag){//既不能借款也不能报销时；视为已经结算
						if(loanApplyObj.status==4){
							var _loanRepA = $("<a href='javascript:void(0)' class='green'></a>");
							$(_loanRepA).append("已结算")
							$(_tdOpt).append(_loanRepA)
						}else{
							$(_tdOpt).append("- -")
						}
					}
				}else{
					var _loanRepA = $("<a href='javascript:void(0)' class='green'></a>");
					$(_loanRepA).append("已结算")
					$(_tdOpt).append(_loanRepA)
				}
				
				
			}else{
				if(loanApplyObj.directBalanceState != 1){
					var canFeeLoanTag = canFeeLoan(loanApplyObj.endTime,nowDate,loanApplyObj.allowedQuota,loanApplyObj.loanFeeTotal);
					var canFeeLoanOffTag = canFeeLoanOff(loanApplyObj);
					if(canFeeLoanTag){//能否继续借款验证
						var _loanA = $("<span></span>");
						$(_loanA).css("color","grey");
						$(_loanA).append("借款")
						$(_tdOpt).append(_loanA)
					}
					if(canFeeLoanTag && canFeeLoanOffTag){//两者都有时，才需要
						$(_tdOpt).append("|")
					}
					if(canFeeLoanOffTag){//能否继续报销验证
						var _loanRepA = $("<span></span>");
						$(_loanRepA).css("color","grey");
						$(_loanRepA).append("报销")
						$(_tdOpt).append(_loanRepA)
					}
					if(!canFeeLoanTag && !canFeeLoanOffTag){//既不能借款也不能报销时；视为已经结算
						if(loanApplyObj.status==4){
							var _loanRepA = $("<span class='green'></span>");
							$(_loanRepA).append("已结算")
							$(_tdOpt).append(_loanRepA)
						}else{
							$(_tdOpt).append("- -")
						}
					}
				}else{
					if(loanApplyObj.status==4){
						var _loanRepA = $("<span class='green'></span>");
						$(_loanRepA).append("已结算")
						$(_tdOpt).append(_loanRepA)
					}else{
						$(_tdOpt).append("- -")
					}
				}
			}
			 $(_tr).append(_tdOpt)
			 //添加行
			$("#allTodoBody").append(_tr);
			$(_tr).data("loanApplyData",loanApplyObj);
			
		 })
	 }
 }
		 
 //列出借款申请信息
 function initLoanTable(){
	 $("#subDataDiv").find("table tbody").html('');
	 $("#subDataDiv").find("table thead").html('');
	 
	 var loanApplyData  = $("#subDataDiv").data("loanApplyData");
	 var listFeeLoan = loanApplyData.listFeeLoan;
	 
	 /**初始化头部信息开始**/
	 var _trh = $('<tr role="row"></tr>');
	 var headMod = $('<th></th>')
	 headMod.css("text-align","center");
	 headMod.css("font-weight","bold");
	 //序号
	 var _th1 = $(headMod).clone();
	 _th1.css("width","50px");
	 _th1.html("序号");
	 //借款人员
	 var _th2 = $(headMod).clone();
	 _th2.css("width","90px");
	 _th2.html("借款人员");
	 //申请时间
	 var _th3 = $(headMod).clone();
	 _th3.css("width","130px");
	 _th3.html("借款申请时间");
	 //借款人员
	 var _th4 = $(headMod).clone();
	 _th4.html("借款流程");
	 //审核状态
	 var _th5 = $(headMod).clone();
	 _th5.css("width","90px");
	 _th5.html("审核状态");
	 //当前审核人
	 var _th6 = $(headMod).clone();
	 _th6.css("width","100px");
	 _th6.html("当前审核人");
	 //借款金额
	 var _th7 = $(headMod).clone();
	 _th7.css("width","100px");
	 _th7.html("借款金额");
	 //审核金额
	 var _th8 = $(headMod).clone();
	 _th8.css("width","100px");
	 _th8.html("审核金额");
	 
	 $(_trh).append(_th1);
	 $(_trh).append(_th2);
	 $(_trh).append(_th3);
	 $(_trh).append(_th4);
	 $(_trh).append(_th5);
	 $(_trh).append(_th6);
	 $(_trh).append(_th7);
	 $(_trh).append(_th8);
	 
	 var thead =  $("#subDataDiv").find("table thead");
	 $(thead).html(_trh);
	 /**初始化头部信息结束**/
	 
	 /**初始化数据信息开始**/
	 var tbody =  $("#subDataDiv").find("table tbody");
	 var totalNum = 0;
	 $.each(listFeeLoan,function(loanIndex,loanObj){
		 
		 if(loanObj.initStatus==1){
			 loanObj.flowName='2018年2月28日前未销账金额初始化';
			 loanObj.flowState=4;
			 loanObj.spState=1;
		 }
		 var _tr = $("<tr></tr>");
		 var _td1 = $("<td></td>");//序号
		 $(_td1).html(loanIndex+1)
		 
		 var _td2 = $("<td></td>");//借款人员
		 $(_td2).html(loanObj.creatorName)
		 _td2.css("text-align","center");
		 
		 var _td3 = $("<td></td>");//申请时间
		 $(_td3).html(loanObj.recordCreateTime)
		 
		 var _td4 = $("<td></td>");//借款流程
		 var _flowNameA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
		 $(_flowNameA).attr("insId",loanObj.instanceId)
		 $(_flowNameA).html(loanObj.flowName)
		 
		 $(_td4).html(_flowNameA)
		 
		 var _td5 = $("<td></td>");//审核状态
		 _td5.css("text-align","center");
		 var _td6 = $("<td></td>");//当前审核人
		 _td6.css("text-align","center");
		 var _td7 = $("<td></td>");//借款金额
		 _td7.css("text-align","center");
		 
		 var _td8 = $("<td></td>");//批准金额
		 _td8.css("text-align","center");
		 
		 $(_td7).html("- -")
		 $(_td8).html("- -")
		 
		 $(_tr).append(_td1);
		 $(_tr).append(_td2);
		 $(_tr).append(_td3);
		 $(_tr).append(_td4);
		 
		 if(loanObj.flowState == 4){//流程结束了
			 if(loanObj.spState == 1){
				 $(_td5).html(constrTagHtml(4,"申请"))
				 $(_td8).html(toDecimal2(loanObj.borrowingBalance))
				 totalNum = accAdd(totalNum,loanObj.borrowingBalance)
			 }else{
				 $(_td5).html(constrTagHtml(-1,"申请"))
			 }
			 $(_td6).html("- -")
		 }else if(loanObj.flowState == 2){//草稿
			 $(_td5).html(constrTagHtml(2,"申请"))
			 $(_td6).html("- -")
		 }else{//审批中
			 $(_td5).html(constrTagHtml(1,"申请"))
			 $(_td6).html(loanObj.executorName);
			 $(_td7).html(toDecimal2(loanObj.loanMoney))
		 }
		
		 $(_tr).append(_td5);
		 $(_tr).append(_td6);
		 $(_tr).append(_td7);
		 $(_tr).append(_td8);
		 tbody.append(_tr)
	 })
	 /**初始化数据信息结束**/
	 
	 /**合计开始**/
	 var _trHj = $("<tr></tr>");
	 var _tdHj1 = $("<td></td>");//序号
	 _tdHj1.html("合计")
	 var _tdHj3= $("<td></td>");//序号
	 _tdHj3.html(changeNumMoneyToChinese(totalNum))
	 _tdHj3.attr("colspan","5")
	 _tdHj3.css("text-align","right")
	 var _tdHj4 = $("<td></td>");//序号
	 _tdHj4.html("小写")
	 var _tdHj5 = $("<td></td>");//小写合计
	 _tdHj5.css("text-align","center")
	 if(totalNum>0){
		 totalNum = toDecimal2(totalNum);
	 }
	 _tdHj5.html(totalNum)
	 
	 _trHj.append(_tdHj1)
	 _trHj.append(_tdHj3)
	 _trHj.append(_tdHj4)
	 _trHj.append(_tdHj5)
	 tbody.append(_trHj)
	 /**合计结束**/
 }
 //查看汇报信息
 function initLoanOffTable(){
	 $("#subDataDiv").find("table tbody").html('');
	 $("#subDataDiv").find("table thead").html('');
	 
	 var loanApplyData  = $("#subDataDiv").data("loanApplyData");
	 var listLoanOff = loanApplyData.listLoanOff;
	 /**初始化头部信息开始**/
	 var _trh = $('<tr role="row"></tr>');
	 var headMod = $('<th></th>')
	 headMod.css("text-align","center");
	 headMod.css("font-weight","bold");
	 //序号
	 var _thXh = $(headMod).clone();
	 _thXh.css("width","30px");
	 _thXh.html("序");
	 $(_trh).append(_thXh);
	 //借款人员
	 var _thUser = $(headMod).clone();
	 _thUser.css("width","60px");
	 _thUser.html("汇报人");
	 $(_trh).append(_thUser);
	 //申请时间
	 var _thRepFlow = $(headMod).clone();
	 _thRepFlow.html("汇报流程");
	 $(_trh).append(_thRepFlow);
	 
	 //借款人员
	 var _thDate = $(headMod).clone();
	 _thDate.css("width","120px");
	 _thDate.html("汇报时间");
	 $(_trh).append(_thDate);
	 //报销流程
	 var _thLoanOffFlow = $(headMod).clone();
	 _thLoanOffFlow.html("报销流程");
	 $(_trh).append(_thLoanOffFlow);
	 
	 //报销时间
	 var _thLoanOffDate = $(headMod).clone();
	 _thLoanOffDate.css("width","120px");
	 _thLoanOffDate.html("报销时间");
	 $(_trh).append(_thLoanOffDate);
	 
	 //报销状态
	 var _thLoanOffState = $(headMod).clone();
	 _thLoanOffState.css("width","70px");
	 _thLoanOffState.css("padding","0");
	 _thLoanOffState.html("报销状态");
	 $(_trh).append(_thLoanOffState);
	 
	 //借款金额
	 var _thLoanOffFee = $(headMod).clone();
	 _thLoanOffFee.css("width","90px");
	 _thLoanOffFee.css("padding","0");
	 _thLoanOffFee.html("报销金额");
	 $(_trh).append(_thLoanOffFee);
	 
	 var thead =  $("#subDataDiv").find("table thead");
	 $(thead).html(_trh);
	 /**初始化头部信息结束**/
	 
	  /**初始化数据信息开始**/
	 var tbody =  $("#subDataDiv").find("table tbody");
	 var totalOffNum = 0;
	 //前一行的工作汇报主键
	 var preLoanRepId=0;
	 var rowIndex = 0;
	 $.each(listLoanOff,function(loanOffIndex,loanOffObj){
		 if(loanOffObj.loanRepFlowStatus == 0){
			 return true;
		 }
		//当前行的工作汇报主键
		 var loanRepId = loanOffObj.loanReportId;
		 if(loanRepId==preLoanRepId){//是同一个工作汇报
			 return true;
		 }else{//不是同一个工作汇报
			 rowIndex++;
			 
			 //一行数据
			 var _tr = $("<tr></tr>");
			 //序号
			 var _tdXh = $("<td></td>");
			 $(_tdXh).html(rowIndex)
			 $(_tr).append(_tdXh)//行号
			 
			 //报销人员
			 var _tdUser = $("<td></td>")
			 $(_tdUser).append(loanOffObj.creatorName)
			  _tdUser.css("text-align","center");
			 $(_tr).append(_tdUser)//报销人员
			 
			 //汇报流程
			 var _tdLoanRep = $("<td></td>")
			 var _loanRepA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
			 $(_loanRepA).attr("insId",loanOffObj.loanRepInsId)
			 
			 var loanReportName = loanOffObj.loanReportName.replace(loanOffObj.creatorName,'');
			 $(_loanRepA).append(loanReportName);
			 $(_tdLoanRep).append(_loanRepA)
			 $(_tdLoanRep).attr("title",loanReportName)
			 
			 $(_tr).append(_tdLoanRep)//汇报流程
			 
			 //报销人员
			 var _tdDate = $("<td></td>")
			 $(_tdDate).append(loanOffObj.loanRepDate)
			  _tdDate.css("text-align","center");
			 _tdDate.css("padding","0")
			 $(_tr).append(_tdDate)//报销人员
			 
			 
			 if(loanOffObj.loanRepStatus!=4){//汇报没有通过的
				 
				 //报销流程
				 var _td5 = $("<td></td>")
				 if(loanOffObj.loanRepStatus == -1){
					 var _loanMsgA = $("<a href='javascript:void(0)' class='red'></a>");
					 _loanMsgA.html("汇报失败")
					 $(_td5).append(_loanMsgA)
				 }else{
					 
					 var _loanSpInsA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
					 if(loanOffObj.loanRepFlowStatus==1){//审批中
						 $(_loanSpInsA).append("汇报审批中")
						 $(_loanSpInsA).addClass("blue")
					}else{
						$(_loanSpInsA).append("汇报草稿")
						$(_loanSpInsA).addClass("purple")
					}
					 $(_loanSpInsA).attr("insId",loanOffObj.loanRepInsId)
					 $(_td5).html(_loanSpInsA)
				 }
				 $(_td5).css("text-align","center")
				 $(_td5).attr("colspan",3);
				 $(_tr).append(_td5)//报销流程
				 //报销流程
				 var _td6 = $("<td></td>")
				 $(_td6).append("- -")
				 $(_td6).css("text-align","center")
				 $(_tr).append(_td6)//报销流程
				 
				 
				 $(tbody).append(_tr);
				 
				 $(_tr).data("loanOffData",loanOffObj);
				 preLoanRepId = loanRepId; 
				 return true;
			 }
			 /**汇报成功了的**/
			 if(loanOffObj.instanceId && loanOffObj.instanceId>0){//报销过的
				 //报销流程
				 var _td5 = $("<td></td>")
				 var _loanOffA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
				 $(_loanOffA).attr("insId",loanOffObj.instanceId)
				 if(loanOffObj.loanOffName){
					 var loanOffName = loanOffObj.loanOffName.replace(loanOffObj.creatorName,'');
					 $(_loanOffA).append(loanOffName)
					 _td5.attr("title",loanOffName)
				 }
				 $(_td5).append(_loanOffA)
				 
				 
				 //报销时间
				 var _td7 = $("<td></td>")
				  _td7.css("padding","0")
				  _td7.css("text-align","center")
				 $(_td7).append(loanOffObj.recordCreateTime)
				 //实际报销金额
				 var _td8 = $("<td></td>")
				 var actFee = loanOffObj.loanOffItemFee?loanOffObj.loanOffItemFee:0;
				 $(_td8).append(toDecimal2(actFee))
				 
				if(loanOffObj.status == 4){//报销完成并通过了的
					$(_tr).append(_td5)//报销流程
					
					$(_tr).append(_td7)//报销状态
					//报销流程
					var _td6 = $("<td></td>")
					_td6.css("text-align","center")
					$(_td6).html(constrTagHtml(4,"审批"))
					$(_tr).append(_td6)//报销流程
					$(_tr).append(_td8)//实际报销金额
					$(_td8).css("text-align","center")
					var actFee = loanOffObj.loanOffItemFee?loanOffObj.loanOffItemFee:0;
					totalOffNum = accAdd(totalOffNum,actFee)
				}else if(loanOffObj.status == -1){//报销失败的
					//报销流程
					 if(loanOffObj.status == -1){
						 
						 _td5 = $("<td></td>")
						 _td5.addClass("red")
						 _td5.html("报销失败")
						 $(_td5).css("text-align","center")
						 
						 $(_td5).attr("colspan",3);
						 $(_tr).append(_td5)//报销流程
						 
						 if(loanOffObj.creator == EASYWIN.userInfo.id){
							 var _td6 = $("<td></td>")
							 var _loanOffA = $("<a href='javascript:void(0)'></a>");
							 _loanOffA.addClass("green addLoanOff")
							 _loanOffA.html("再次报销")
							 $(_td6).append(_loanOffA)
							 $(_td6).css("text-align","center")
							 $(_td6).css("padding","0")
							 $(_tr).append(_td6)//报销流程
							 
						 }else{
							 //报销流程
							 var _td6 = $("<td></td>")
							 $(_td6).append("- -")
							 $(_td6).css("text-align","center")
							 $(_tr).append(_td6)//报销流程
						 }
						 
					 }
				}else{
					$(_tr).append(_td5)//报销流程
					$(_tr).append(_td7)//报销状态
					//报销流程
					var _td6 = $("<td></td>")
					_td6.css("text-align","center")
					if(loanOffObj.flowState==1){//审批中
						$(_td6).html(constrTagHtml(1,"审批"))
					}else{
						$(_td6).html(constrTagHtml(2,"申请"))
					}
					$(_tr).append(_td6)//报销流程
					//报销流程
					 var _td7 = $("<td></td>")
					 $(_td7).append("- -")
					 $(_td7).css("text-align","center")
					 $(_tr).append(_td7)//报销流程
				}
				 
				 
			 }else{//没有报销过
				 _td5 = $("<td></td>")
				 _td5.html("待报销")
				 $(_td5).css("text-align","center")
				 $(_td5).addClass("gray")
				 
				 $(_td5).attr("colspan",3);
				 $(_tr).append(_td5)//报销流程
				 
				 if(loanOffObj.creator == EASYWIN.userInfo.id){
					 var _td6 = $("<td></td>")
					 var _loanOffA = $("<a href='javascript:void(0)' class='addLoanOff'></a>");
					 _loanOffA.html("报销")
					 $(_loanOffA).addClass("green")
					 $(_td6).append(_loanOffA)
					 $(_td6).css("text-align","center")
					 
					 $(_tr).append(_td6)//报销流程
				 }else{
					 
					 //报销流程
					 var _td6 = $("<td></td>")
					 $(_td6).append("- -")
					 $(_td6).css("text-align","center")
					 $(_tr).append(_td6)//报销流程
				 }
			 }
		 }
		 
		 $(tbody).append(_tr);
		 
		 $(_tr).data("loanOffData",loanOffObj);
		 preLoanRepId = loanRepId; 
	 })
	 /**合计开始**/
	 var _trHj = $("<tr></tr>");
	 var _tdHj1 = $("<td></td>");//序号
	 _tdHj1.html("合计")
	 _tdHj1.attr("colspan","2")
	 var _tdHj3= $("<td></td>");//序号
	 _tdHj3.html(changeNumMoneyToChinese(totalOffNum))
	 _tdHj3.attr("colspan","4")
	 _tdHj3.css("text-align","right")
	 var _tdHj4 = $("<td></td>");//序号
	 _tdHj4.html("小写")
	 var _tdHj5 = $("<td></td>");//小写合计
	 if(totalOffNum>0){
		 totalOffNum = toDecimal2(totalOffNum);
	 }
	 _tdHj5.html(totalOffNum)
	 $(_tdHj5).css("text-align","center")
	 _trHj.append(_tdHj1)
	 _trHj.append(_tdHj3)
	 _trHj.append(_tdHj4)
	 _trHj.append(_tdHj5)
	 tbody.append(_trHj)
	 /**合计结束**/
 }
		 
 function constrLoanOffHtml(status){
	 var html = "";
	 if(status == 4){//通过
		 html = '<i class="fa fa-check green" title="报销成功"></i>';
	 }else if (status == 1){//审核中
		 html = '<i class="fa fa-gavel blue" title="报销中"></i>';
	 }else if (status == -1){//未通过
		 html = '<i class="fa fa-times red" title="报销失败"></i>';
	 }
	 return html;
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
 /**
  * 能否继续借款验证
  * @param endTime 计划结束时间
  * @param nowDate 当前时间
  * @param allowedQuota 允许的借款限额
  * @param loanFeeTotal 已经借款金额
  * @returns
  */
 function canFeeLoan(endTime,nowDate,allowedQuota,loanFeeTotal){
	 if(nowDate<=endTime && loanFeeTotal<allowedQuota){//当前时间小于结束时间 且 总借款小于限制额度的
		 return true;
	 }else{
		 return false;
	 }
 }
 /**
  * 能否继续报销验证
  * @param loanFeeTotal 借款金额
  * @param loanOffTotal 销账金额
  * @param loanItemTotal 报销金额
  * @returns
  */
 function canFeeLoanOff(loanApplyObj){
	 //借款金额
	 var loanFeeTotal = loanApplyObj.loanFeeTotal;
	 // 销账金额
	 var loanOffTotal = loanApplyObj.loanOffTotal;
	 // 报销金额
	 var loanItemTotal = loanApplyObj.loanItemTotal;
	 var status = loanApplyObj.status;
	 if(status!=4){
		 return false;
	 }
	 if(loanFeeTotal>0){//有借款
		 if(loanFeeTotal!=loanOffTotal){//借款金额与销账金额不对等时，可继续报销
			 return true;
		 }else{
			 return false;
		 }
	 }else{//没借款
		 if(loanItemTotal>0){//没借款的，可进行一次性报销销账；如果loanItemTotal>0,表示其已经报销过；所以，不能再报销
			 return false;
		 }else{
			 return true;
		 }
	 }
 }
 
 //不予报销
 function directBalance(id,amount){
	 
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['600px', '340px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/financial/directBalancePage?sid='+sid+"&amount="+amount,'no'],
		 btn:['确定','关闭'],
		 yes: function(index, layero){
			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 var content = $(iframeWin.document).find("#content").val();
			 iframeWin.formSub();
			 if(content){
				 $.post("/financial/directBalance?sid="+sid,{Action:"post",id:id,content:content},     
						function (msgObjs){
						showNotification(1,msgObjs);
						window.top.layer.close(index);
						window.location.reload();
					});
			 }
		 }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		 
	    }
	});
	
 }
 