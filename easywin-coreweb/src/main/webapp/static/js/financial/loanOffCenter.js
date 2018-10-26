var pageNum = 0;     //页面索引初始值   
var pageSize =10;     //每页显示条数初始化，修改显示条数，修改这里即可
$(function(){
	//加载数据
	initTable();
	//借款
	$("body").on("click","button[name='addLoanOffBtn']",function(){
		addHeadBus("031",sid);
	})
	
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initTable();
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
		
		initTable();
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
		
		
		initTable();
	})
	//人员多选
	$("body").on("click",".userMoreSelect",function(){
		var relateList = $(this).attr("relateList");
		$("#formTempData").find("#"+relateList).html('');
		$("#"+relateList+"Div").find("span").remove();
		userMore(relateList, null, sid, null, null, function(options){
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
			initTable();
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
		initTable();
	})
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initTable();
	})
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId){
			parent.viewSpFlow(insId);
		}
	})
	$("body").on("click",".addLoanOff",function(){
		var loanOff = $(this).parents("tr").data("loanOffData");
		var feeBudgetId = loanOff.feeBudgetId;
		var loanReportId = loanOff.loanReportId;
		var isBusinessTrip = loanOff.isBusinessTrip;
		
		if(loanReportId && loanReportId>0){
			getSelfJSON("/financial/checkLoanRep4Off",{sid:sid,loanReportId:loanReportId,feeBudgetId:feeBudgetId},function(data){
				if(data.status=='y'){
					if(data.flowState == 2){//草稿
						viewSpFlow(data.instanceId);
					}else{
						var busType = data.busType;
						listBusMapSelect(data.listBusMapFlows,busType,function(busMapFlow){
							var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
							url +="&busMapFlowId="+busMapFlow.id
							url +="&feeBudgetId="+feeBudgetId
							url +="&loanReportId="+loanReportId
							url +="&busType="+busType;
							url +="&loanWay=1";//额度借款
							url +="&loanReportWay=1";//需要汇报的
							openWinByRight(url);
						})
					}
					
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
	})
	
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

function initTable(pageMoreNum){
	loadDone=0;
	layui.use('layer', function() {
		loadingIndex = layer.load(0, {
			shade: [0.5,'#fff'] //0.1透明度的白色背景
		});
		
		intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
		
	})
	
		if(pageMoreNum){
			pageNum = pageMoreNum;
		}else{
			pageNum = 0;
		}
		var params={
		 "sid":sid,
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
		getSelfJSON("/financial/loanOff/ajaxListLoanOffOfAuth",params,function(data){
			loadDone=1;
			if(data.status=='y'){
				var pageBean = data.pageBean;
			 	$("#totalNum").html(pageBean.totalCount);
	           	$("#totalNum").parent().parent().css("display","block")
	           	if(pageBean.totalCount<=pageSize){
	           		 $("#totalNum").parent().parent().css("display","none")
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
	           		 resetIndex()
	           	 }else{
	           		 var _tr = $("<tr></tr>");
	           		 var _td = $("<td></td>");
	           		 var len = $("#allTodoBody").parents("table").find("thead tr th").length-1;
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
	 initTable(index);
} 
 
 function constrDataTable(loanOffList){
	 if(loanOffList && loanOffList.length>0){
		 //前一行的工作汇报主键
		 var preLoanRepId=0;
		 var preFeeBudgetId=0;
		 $.each(loanOffList,function(loanOffIndex,loanOffObj){
			 
			 //当前行的工作汇报主键
			 var feeBudgetId = loanOffObj.feeBudgetId;
			 
			 var loanRepId = loanOffObj.loanReportId;
			 loanRepId = loanRepId?loanRepId:0;
			 var loanOffId = loanOffObj.id;
			 loanOffId = loanOffId?loanOffId:0;
			 
			 //一行数据
			 var _tr = $("<tr></tr>");
			 $(_tr).attr("feeBudgetId",feeBudgetId);
			 $(_tr).attr("loanRepId",loanRepId);
			 
			 if(preFeeBudgetId == feeBudgetId ){//是同一个依据
				 var firstTr = $("#allTodoBody").find("tr[feeBudgetId='"+feeBudgetId+"']:eq(0)");
			     var num = $("#allTodoBody").find("tr[feeBudgetId='"+feeBudgetId+"']").length +1;
			 	 $(firstTr).find("td:eq(0)").attr("rowspan",num);//行号
			 	 $(firstTr).find("td:eq(1)").attr("rowspan",num);//报销类型
			 	 $(firstTr).find("td:eq(2)").attr("rowspan",num);//报销人员
			 	 $(firstTr).find("td:eq(3)").attr("rowspan",num);//报销依据
			 	 
			 	 
			 	 if(loanRepId>0 && preLoanRepId == loanRepId){//同一个汇报
			 		 var subnum = $("#allTodoBody").find("tr[feeBudgetId='"+feeBudgetId+"'][loanRepId='"+loanRepId+"']").length +1;
			 		 $(firstTr).find("td:eq(4)").attr("rowspan",subnum);//报销说明
			 	 }else{
			 		 preLoanRepId = loanRepId;
			 		 //报销说明
			 		 var loanRepFlow = $('<td></td>');
			 		 var loanRepInsId = loanOffObj.loanRepInsId;
			 		 if(loanRepInsId && loanRepInsId>0){
			 			 var _loanRepA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
			 			 $(_loanRepA).html(loanOffObj.loanReportName);
			 			 $(_loanRepA).attr("title",loanOffObj.loanReportName);
			 			 $(_loanRepA).attr("insId",loanOffObj.loanRepInsId);
			 			 $(loanRepFlow).append($(_loanRepA));
			 		 }else{
			 			 $(loanRepFlow).append(''); 
			 		 }
			 		 $(_tr).append($(loanRepFlow)); //报销单
			 	 }
			 	 
			 	 
			 	 
			 	 
			 	 //报销单
				 var loanOffFlow = $('<td></td>');
				 var loanOffInsId = loanOffObj.instanceId;
				 if(loanOffInsId && loanOffInsId>0){
					 var _loanOffA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
					 $(_loanOffA).html(loanOffObj.loanOffName);
					 $(_loanOffA).attr("title",loanOffObj.loanOffName);
					 $(_loanOffA).attr("insId",loanOffObj.instanceId);
					 $(loanOffFlow).append($(_loanOffA));
				 }else{
					 $(loanOffFlow).append(''); 
				 }
				 $(_tr).append($(loanOffFlow));//报销单
				 
//				//报销时间
//				 var _lonaOffTimeTd = $("<td></td>");
//				 var lonaOffTime = loanOffObj.recordCreateTime;
//				 var  loanRepDate = loanOffObj.loanRepDate;
//				 if(lonaOffTime){
//					 $(_lonaOffTimeTd).html(lonaOffTime.substring(0,16));
//				 }else{
//					 $(_lonaOffTimeTd).html(loanRepDate.substring(0,16));
//				 }
//				 $(_tr).append($(_lonaOffTimeTd));//报销时间
				 
				 $(firstTr).find("td:eq(6)").attr("rowspan",num);//累计借款
				 
				 var balanceState = loanOffObj.balanceState;
				 //待报销
				 var  loanOffPreTd = $("<td></td>");
				 $(loanOffPreTd).css("text-align","center");
				 if(balanceState==1){//已结算
					 loanOffPreTd.html("--");
				 }else{
					 var loanOffPreFee = loanOffObj.loanOffPreFee;
					 if(loanOffPreFee && loanOffPreFee>0){
						 loanOffPreFee = toDecimal2(loanOffPreFee);
					 }else{
						 loanOffPreFee = 0;
					 }
					 loanOffPreTd.html(loanOffPreFee) 
				 }
				 $(_tr).append($(loanOffPreTd));//待报销
				 
				 //销账金额
				 var loanOffItemTd = $("<td></td>");
				 $(loanOffItemTd).css("text-align","center");
				 loanOffItemTd.css("padding","0");
				 if(balanceState==1){//已结算
					 var actMoney = loanOffObj.loanOffBalance?loanOffObj.loanOffBalance:0;
					 $(loanOffItemTd).append(toDecimal2(actMoney))
				 }else{
					 $(loanOffItemTd).append('--');
				 }
				 $(_tr).append($(loanOffItemTd));//销账金额
				 
				 //报销金额
				 var loanOffTd = $("<td></td>");
				 $(loanOffTd).css("text-align","center");
				 loanOffTd.css("padding","0");
				 if(balanceState==1){//已结算
					 var actMoney = loanOffObj.loanOffItemFee?loanOffObj.loanOffItemFee:0;
					 $(loanOffTd).append(toDecimal2(actMoney))
				 }else{
					 $(loanOffTd).append('--');
				 }
				 $(_tr).append($(loanOffTd));//报销金额
				 
				 
				 
				 //报销状态
				 var stateTd = $("<td></td>");
				 $(stateTd).css("text-align","center");
				 
				 if(balanceState==1){//已结算
					 stateTd.css("color","green"); 
					 stateTd.html("已结算");
				 }else{
					 var status = loanOffObj.status;
					 if(status == 4 ){
						 stateTd.css("color","blue");
						 if(loanOffObj.sendNotice==1){
							 stateTd.html("待结算"); 
						 }else{
							 stateTd.html('核算中');
						 }
					 }else if(status == 1){
						 stateTd.css("color","#428bca"); 
						 stateTd.html("审核中"); 
					 }else if(status == -1){
						 stateTd.css("color","red"); 
						 stateTd.html('失败');
					 }else{
						 var loanRepStatus = loanOffObj.loanRepStatus;
						 if(loanRepStatus == 4){
							 stateTd.css("color","blue");
							 //提交报销
							 if(EASYWIN.userInfo.id == loanOffObj.creator){
								 var loanOffA = $('<a class="addLoanOff" href="javascript:void(0)"></a>');
								 $(loanOffA).html('待提交');
								 stateTd.html($(loanOffA)); 
								 
							 }else{
								 var loanOffA = $('<span style="color:grey"></span>');
								 $(loanOffA).html('待提交');
								 stateTd.html($(loanOffA)); 
							 }
							 
						 }else if(loanRepStatus == 1){
							 stateTd.css("color","#428bca"); 
							 stateTd.html("审核中"); 
						 }else if(loanRepStatus == -1){
							 stateTd.css("color","red"); 
							 stateTd.html("失败");
						 }else{
							 stateTd.css("color","purple");
							 
							 if(EASYWIN.userInfo.id == loanOffObj.creator && loanOffObj.loanRepInsId>0){
								 var loanOffA = $('<a href="javascript:void(0)"></a>');
								 $(loanOffA).attr('insId',loanOffObj.loanRepInsId);
								 $(loanOffA).html('待提交');
								 stateTd.html($(loanOffA)); 
								 
							 }else{
								 var loanOffA = $('<span style="color:grey"></span>');
								 $(loanOffA).html('待提交');
								 stateTd.html($(loanOffA)); 
							 }
						 }
					 }
					 
				 }
				 $(_tr).append($(stateTd));//报销状态
				 
			 }else{//不是同一个工作汇报
				 
				 preLoanRepId = loanRepId;
				 preFeeBudgetId = feeBudgetId;
				 
				//序号
				 var _tdXh = $("<td></td>");
				 $(_tdXh).html(loanOffIndex+1)
				 $(_tr).append(_tdXh)//行号
				 
				 var _tdType = $("<td></td>")
				 if(loanOffObj.isBusinessTrip==1){
					 $(_tdType).append("出差报销")
				 }else if(loanOffObj.isBusinessTrip==0){
					 $(_tdType).append("一般报销")
				 }
				 _tdType.css("padding","0")
				 _tdType.css("text-align","center")
				 $(_tr).append(_tdType)//报销类型
				 
				 //报销人员
				 var _tdUser = $("<td></td>");
				 $(loanOffItemTd).css("text-align","center");
				 $(_tdUser).append(loanOffObj.creatorName)
				 $(_tr).append(_tdUser)//报销人员
				 
				 //借款说明
				 var feeBudgetFlow = $('<td></td>');
				 var loanApplyInsId = loanOffObj.loanApplyInsId;
				 var _loanApplyA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
				 $(_loanApplyA).attr("title",loanOffObj.loanApplyName);
				 $(_loanApplyA).attr("insId",loanOffObj.loanApplyInsId);
				 $(_loanApplyA).html(loanOffObj.loanApplyName);
				 if(loanOffObj.apyInitStatus == 1){
					 $(_loanApplyA).html('2018年2月28日前未销账金额初始化');
					 $(_loanApplyA).removeAttr("insId")
				 }
				 $(feeBudgetFlow).append($(_loanApplyA));
				 
				 $(_tr).append($(feeBudgetFlow))//借款说明
				 
				 //报销说明
				 var loanRepFlow = $('<td></td>');
				 var loanRepInsId = loanOffObj.loanRepInsId;
				 if(loanRepInsId && loanRepInsId>0){
					 var _loanRepA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
					 $(_loanRepA).html(loanOffObj.loanReportName);
					 $(_loanRepA).attr("title",loanOffObj.loanReportName);
					 $(_loanRepA).attr("insId",loanOffObj.loanRepInsId);
					 $(loanRepFlow).append($(_loanRepA));
				 }else{
					 $(loanRepFlow).append(''); 
				 }
				 $(_tr).append($(loanRepFlow)); //报销单
				 
				 //报销单
				 var loanOffFlow = $('<td></td>');
				 var loanOffInsId = loanOffObj.instanceId;
				 if(loanOffInsId && loanOffInsId>0){
					 var _loanOffA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
					 $(_loanOffA).html(loanOffObj.loanOffName);
					 $(_loanOffA).attr("title",loanOffObj.loanOffName);
					 $(_loanOffA).attr("insId",loanOffObj.instanceId);
					 $(loanOffFlow).append($(_loanOffA));
				 }else{
					 $(loanOffFlow).append(''); 
				 }
				 $(_tr).append($(loanOffFlow));//报销单
				 
//				 //报销时间
//				 var _lonaOffTimeTd = $("<td></td>");
//				 var lonaOffTime = loanOffObj.recordCreateTime;
//				 var  loanRepDate = loanOffObj.loanRepDate;
//				 if(lonaOffTime){
//					 $(_lonaOffTimeTd).html(lonaOffTime.substring(0,16));
//				 }else if(loanRepDate){
//					 $(_lonaOffTimeTd).html(loanRepDate.substring(0,16));
//				 }else{
//					 $(_lonaOffTimeTd).css("text-align","center");
//					 $(_lonaOffTimeTd).html('--');
//				 }
//				 $(_tr).append($(_lonaOffTimeTd));//报销单
				 
				//累计借款
				 var  loanTotalTd = $("<td></td>");
				 $(loanTotalTd).css("text-align","center");
				 var loanFeeTotal = loanOffObj.loanFeeTotal;
				 if(loanFeeTotal && loanFeeTotal>0){
					 loanFeeTotal = toDecimal2(loanFeeTotal);
				 }else{
					 loanFeeTotal = 0;
				 }
				 loanTotalTd.html(loanFeeTotal); 
				 
				 $(_tr).append($(loanTotalTd));//待报销
				 
				 var balanceState = loanOffObj.balanceState;
				 
				//待报销
				 var  loanOffPreTd = $("<td></td>");
				 $(loanOffPreTd).css("text-align","center");
				 if(balanceState==1){//已结算
					 loanOffPreTd.html("--");
				 }else{
					 var loanOffPreFee = loanOffObj.loanOffPreFee;
					 if(loanOffPreFee && loanOffPreFee>0){
						 loanOffPreFee = toDecimal2(loanOffPreFee);
					 }else{
						 loanOffPreFee = 0;
					 }
					 loanOffPreTd.html(loanOffPreFee) 
				 }
				 $(_tr).append($(loanOffPreTd));//待报销
				 
				 //销账金额
				 var loanOffItemTd = $("<td></td>");
				 $(loanOffItemTd).css("text-align","center");
				 loanOffItemTd.css("padding","0");
				 if(balanceState==1){//已结算
					 var actMoney = loanOffObj.loanOffBalance?loanOffObj.loanOffBalance:0;
					 $(loanOffItemTd).append(toDecimal2(actMoney))
				 }else{
					 $(loanOffItemTd).append('--');
				 }
				 $(_tr).append($(loanOffItemTd));//销账金额
				 
				 //报销金额
				 var loanOffTd = $("<td></td>");
				 $(loanOffTd).css("text-align","center");
				 loanOffTd.css("padding","0");
				 if(balanceState==1){//已结算
					 var actMoney = loanOffObj.loanOffItemFee?loanOffObj.loanOffItemFee:0;
					 $(loanOffTd).append(toDecimal2(actMoney))
				 }else{
					 $(loanOffTd).append('--');
				 }
				 $(_tr).append($(loanOffTd));//报销金额
				 
				 
				 
				 //报销状态
				 var stateTd = $("<td></td>");
				 $(stateTd).css("text-align","center");
				 
				 if(balanceState==1){//已结算
					 stateTd.css("color","green"); 
					 stateTd.html("已结算");
				 }else{
					 var status = loanOffObj.status;
					 if(status == 4 ){
						 stateTd.css("color","blue");
						 if(loanOffObj.sendNotice==1){
							 stateTd.html("待结算"); 
						 }else{
							 stateTd.html('核算中');
						 }
					 }else if(status == 1){
						 stateTd.css("color","#428bca"); 
						 stateTd.html("审核中"); 
					 }else if(status == -1){
						 var  reLoanState = loanOffObj.reLoanState;
						 if(EASYWIN.userInfo.id == loanOffObj.creator && reLoanState==1){
							 var reOpt = $('<a class="addLoanOff" href="javascript:void(0)"></a>');
							 $(reOpt).html('再提交');
							 stateTd.append($(reOpt)); 
						 }else{
							 var failSpan = $('<span style="color:red">失败</span>')
							 stateTd.html(failSpan);
						 }
					 }else{
						 var loanRepStatus = loanOffObj.loanRepStatus;
						 if(loanRepStatus == 4){
							 stateTd.css("color","blue");
							//提交报销
							 if(EASYWIN.userInfo.id == loanOffObj.creator){
								 var loanOffA = $('<a class="addLoanOff" href="javascript:void(0)"></a>');
								 $(loanOffA).html('待提交');
								 stateTd.html($(loanOffA)); 
								 
							 }else{
								 var loanOffA = $('<span style="color:grey"></span>');
								 $(loanOffA).html('待提交');
								 stateTd.html($(loanOffA)); 
							 }
						 }else if(loanRepStatus == 1){
							 stateTd.css("color","#428bca"); 
							 stateTd.html("审核中"); 
						 }else if(loanRepStatus == -1){
							 stateTd.css("color","red"); 
							 stateTd.html("失败");
						 }else{
							 stateTd.css("color","purple"); 
							 if(EASYWIN.userInfo.id == loanOffObj.creator && loanOffObj.loanRepInsId>0){
								 var loanOffA = $('<a href="javascript:void(0)"></a>');
								 $(loanOffA).attr('insId',loanOffObj.loanRepInsId);
								 $(loanOffA).html('待提交');
								 stateTd.html($(loanOffA)); 
								 
							 }else{
								 var loanOffA = $('<span style="color:grey"></span>');
								 $(loanOffA).html('待提交');
								 stateTd.html($(loanOffA)); 
							 }
						 }
					 }
					 
				 }
				 $(_tr).append($(stateTd));//报销状态
				 
			 }
			 
			 
			 $("#allTodoBody").append(_tr);
			 
			 $(_tr).data("loanOffData",loanOffObj);
		 })
	 }
 }
 
 function resetIndex(){
	 var rowIndex = 1;
	 var preFeeBudgetId = 0;
	 $.each($("#allTodoBody").find("tr"),function(trIndex,trObj){
		 var  feeBudgetId = $(this).attr("feeBudgetId");
		 if(preFeeBudgetId == feeBudgetId ){
			 return true;
		 }else{
			 $(this).find("td:eq(0)").html(rowIndex);
			 rowIndex = rowIndex+1;
			 preFeeBudgetId = feeBudgetId;
		 }
	 })
 }