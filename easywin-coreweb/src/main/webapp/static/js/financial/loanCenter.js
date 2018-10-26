var pageNum = 0;     //页面索引初始值   
var pageSize =10;     //每页显示条数初始化，修改显示条数，修改这里即可 
$(function(){
	
	//加载数据
	initTable();
	//借款
	$("body").on("click","button[name='addLoanBtn']",function(){
		addHeadBus("030",sid);
	})
	//借款
	$("body").on("click","a[addloan]",function(){
		var loanData = $(this).parents("tr").data("loanData");
		if(loanData.isBusinessTrip=='1'){
			listBusMapFlows('030',function(busMapFlow){
				var url = "/busRelateSpFlow/loan/addLoan?sid="+sid;
				url = url+"&busMapFlowId="+busMapFlow.id;
				url = url+"&busType=030&isBusinessTrip=1";
				url = url+"&feeBudgetId="+loanData.feeBudgetId;
				openWinByRight(url);
			})
		}else{
			listBusMapFlows('031',function(busMapFlow){
				var url = "/busRelateSpFlow/loan/addLoan?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=031&isBusinessTrip=0";
				url = url+"&feeBudgetId="+loanData.feeBudgetId;
				openWinByRight(url);
			})
		}
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
			viewSpFlow(insId);
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
		getSelfJSON("/financial/loan/ajaxListLoanOfAuth",params,function(data){
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
	 initTable(index);
} 
 
 function constrDataTable(loanList){
	 var preFeeBudgetId = 0;
	 if(loanList && loanList.length>0){
		 //前一行的工作汇报主键
		 $.each(loanList,function(loanIndex,loanObj){
			 //当前行的工作汇报主键
			 var feeBudgetId = loanObj.feeBudgetId;
			 //一行数据
			 var _tr = $("<tr></tr>");
			 $(_tr).attr("feeBudgetId",feeBudgetId);
			 
			 
			 var _tdMod = $("<td></td>");
			 _tdMod.css("padding","10px 0px")
			 _tdMod.css("text-align","center")
			 
			 if(preFeeBudgetId == feeBudgetId){
				 var firstTr = $("#allTodoBody").find("tr[feeBudgetId='"+feeBudgetId+"']:eq(0)");
			     var num = $("#allTodoBody").find("tr[feeBudgetId='"+feeBudgetId+"']").length +1;
			 	 $(firstTr).find("td:eq(0)").attr("rowspan",num);//序
			 	 $(firstTr).find("td:eq(1)").attr("rowspan",num);//借款类型	
			 	 $(firstTr).find("td:eq(2)").attr("rowspan",num);//借款人员	
			 	 $(firstTr).find("td:eq(3)").attr("rowspan",num);//借款依据
			 	 
			 	 //借款流程
				 var _tdLoan = $(_tdMod).clone();
				 var _loanFlowNameA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
				 $(_loanFlowNameA).attr("insId",loanObj.instanceId)
				 _loanFlowNameA.css("padding-left","5px")
				 
				 var loanFlowName = loanObj.flowName;
				 $(_loanFlowNameA).html(loanFlowName)
				 $(_loanFlowNameA).attr("title",loanFlowName)
				 
				 $(_tdLoan).html(_loanFlowNameA)
				 $(_tdLoan).css("text-align","left")
				 $(_tr).append(_tdLoan)
				 
				 //借款日期
				 var _tdLoanDate = $(_tdMod).clone();
				 var recordCreateTime = loanObj.recordCreateTime;
				 if(recordCreateTime){
					 recordCreateTime =  cutString(loanObj.recordCreateTime,16);
				 }
				 _tdLoanDate.html(recordCreateTime)
				 $(_tr).append(_tdLoanDate);
				 
				 var balanceState = loanObj.balanceState;
				 
				//待审金额
				 var loaning = $(_tdMod).clone();
				 if(balanceState == 1 || loanObj.status==-1){
					 loaning.html("--");
				 }else{
					 var loanMoney = loanObj.loanMoney;
					 if(loanMoney && loanMoney>0){
						 loanMoney = toDecimal2(loanMoney);
					 }
					 loaning.html(loanMoney) 
				 }
				 $(_tr).append(loaning);
				 
				 //借款金额
				 var _loanFee = $(_tdMod).clone();
				 if(balanceState == 1){
					 var borrowingBalance = loanObj.borrowingBalance;
					 if(borrowingBalance && borrowingBalance>0){
						 borrowingBalance = toDecimal2(borrowingBalance);
					 }
					 _loanFee.html(borrowingBalance) 
				 }else{
					 _loanFee.html('--') 
				 }
				 $(_tr).append(_loanFee);
				 
				 //审批状态
				 var _tsSpState = $(_tdMod).clone();
				
				 if(balanceState == 1){
					 _tsSpState.css("color","green");
					 _tsSpState.html('已领款');
					 if(EASYWIN.userInfo.id == loanObj.creator && loanObj.canDirectBalance == 1){
						
						 $(_tsSpState).append("|")
						 var _loanRepA = $("<a href='javascript:void(0)' class='blue' onclick='directBalance("+loanObj.feeBudgetId+","+loanObj.borrowingBalance+")'></a>");
						$(_loanRepA).append("不予报销")
						$(_tsSpState).append(_loanRepA)
					 }
				 }else{
					 var  status=loanObj.status;
					 if(status == 4){
						 _tsSpState.css("color","blue");
						 if(loanObj.sendNotice==1){
							 _tsSpState.html('待领款');
						 }else{
							 _tsSpState.html('核算中');
						 }
					 }else if(status == 1){
						 _tsSpState.css("color","#428bca");
						 _tsSpState.html('审核中');
					 }else if(status == -1){
						 _tsSpState.css("color","red");
						 _tsSpState.html('失败');
					 }else {
						 var loanApplyState = loanObj.loanApplyState;
						 if(loanApplyState == 4){
							 _tsSpState.css("color","blue");
							//提交报销
							 if(EASYWIN.userInfo.id == loanObj.creator){
								 var loanOffA = $('<a addloan="1" href="javascript:void(0)"></a>');
								 $(loanOffA).html('待提交');
								 _tsSpState.html($(loanOffA)); 
								 
							 }else{
								 var loanOffA = $('<span style="color:grey"></span>');
								 $(loanOffA).html('待提交');
								 _tsSpState.html($(loanOffA)); 
							 }
						 }else if(loanApplyState == 1){
							 _tsSpState.css("color","#428bca");
							 _tsSpState.html('审核中');
						 }else if(loanApplyState == -1){
							 _tsSpState.css("color","red");
							 _tsSpState.html('失败');
						 }else{
							 _tsSpState.css("color","purple");
							 //提交报销
							 if(EASYWIN.userInfo.id == loanObj.creator && loanObj.loanApplyInsId>0){
								 var loanOffA = $('<a class="spFlowIns"  href="javascript:void(0)"></a>');
								 $(loanOffA).attr('insId',loanObj.loanApplyInsId);
								 $(loanOffA).html('待提交');
								 _tsSpState.html($(loanOffA)); 
								 
							 }else{
								 _tsSpState.html("待提交"); 
							 }
						 }
					 }
				 }
				 $(_tr).append(_tsSpState);
				 $(_tr).data("loanData",loanObj);
				
				 $("#allTodoBody").append(_tr);
			 	 
				return; 
			 }
			 preFeeBudgetId = feeBudgetId
			 
			
			 //序号
			 var _tdXh = $(_tdMod).clone();
			 _tdXh.html(loanIndex);
			 $(_tr).append(_tdXh);
			 
			 //借款类型
			 var _tdLoanType = $(_tdMod).clone();
			 if(loanObj.isBusinessTrip==1){
				 _tdLoanType.html("出差借款");
			 }else{
				 _tdLoanType.html("一般借款");
			 }
			 $(_tr).append(_tdLoanType);
				 
			 //借款人员
			 var _tdUser = $(_tdMod).clone();
			 _tdUser.html(loanObj.creatorName);
			 $(_tr).append(_tdUser);
			 
			 //借款依据
			 var _tdLoanApply = $(_tdMod).clone();
			 var _applyFlowNameA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
			 $(_applyFlowNameA).attr("insId",loanObj.loanApplyInsId)
			 _applyFlowNameA.css("padding-left","5px");
			 
			 var loanApplyFlowName = loanObj.loanApplyInsName;
			 if(loanObj.apyInitStatus == 1){
				 loanApplyFlowName = '2018年2月28日前未销账金额初始化';
				 $(_applyFlowNameA).removeAttr("insId")
			 }
			 $(_applyFlowNameA).html(loanApplyFlowName)
			 $(_applyFlowNameA).attr("title",loanApplyFlowName)
			 
			 $(_tdLoanApply).html(_applyFlowNameA)
			 $(_tdLoanApply).css("text-align","left")
			 $(_tr).append(_tdLoanApply)
			 
			 //借款流程
			 var _tdLoan = $(_tdMod).clone();
			 var _loanFlowNameA = $("<a href='javascript:void(0)' class='spFlowIns'></a>");
			 $(_loanFlowNameA).attr("insId",loanObj.instanceId)
			 _loanFlowNameA.css("padding-left","5px")
			 
			 var loanFlowName = loanObj.flowName;
			 $(_loanFlowNameA).html(loanFlowName)
			 $(_loanFlowNameA).attr("title",loanFlowName)
			 
			 $(_tdLoan).html(_loanFlowNameA)
			 $(_tdLoan).css("text-align","left")
			 $(_tr).append(_tdLoan)
			 
			 //借款日期
			 var _tdLoanDate = $(_tdMod).clone();
			 var recordCreateTime = loanObj.recordCreateTime;
			 if(recordCreateTime){
				 recordCreateTime =  cutString(loanObj.recordCreateTime,16);
			 }
			 _tdLoanDate.html(recordCreateTime)
			 $(_tr).append(_tdLoanDate);
			 var balanceState = loanObj.balanceState;
			 
			 //待审金额
			 var loaning = $(_tdMod).clone();
			 if(balanceState == 1 || loanObj.status==-1 || loanObj.loanApplyState==-1){
				 loaning.html("--");
			 }else{
				 var loanMoney = loanObj.loanMoney;
				 if(loanMoney && loanMoney>0){
					 loanMoney = toDecimal2(loanMoney);
				 }
				 loaning.html(loanMoney) 
			 }
			 $(_tr).append(loaning);
			 
			 //借款金额
			 var _loanFee = $(_tdMod).clone();
			 if(balanceState == 1){
				 var borrowingBalance = loanObj.borrowingBalance;
				 if(borrowingBalance && borrowingBalance>0){
					 borrowingBalance = toDecimal2(borrowingBalance);
				 }
				 _loanFee.html(borrowingBalance) 
			 }else{
				 _loanFee.html('--') 
			 }
			 $(_tr).append(_loanFee);
			 
			 //审批状态
			 var _tsSpState = $(_tdMod).clone();
			
			 if(balanceState == 1){
				 _tsSpState.css("color","green");
				 _tsSpState.html('已领款');
				 if(EASYWIN.userInfo.id == loanObj.creator && loanObj.canDirectBalance == 1){
					 
					 $(_tsSpState).append("|")
					 var _loanRepA = $("<a href='javascript:void(0)' class='blue' onclick='directBalance("+loanObj.feeBudgetId+","+loanObj.borrowingBalance+")'></a>");
					$(_loanRepA).append("不予报销")
					$(_tsSpState).append(_loanRepA)
				 }
			 }else{
				 var  status=loanObj.status;
				 if(status == 4){
					 _tsSpState.css("color","blue");
					 if(loanObj.sendNotice==1){
						 _tsSpState.html('待领款');
					 }else{
						 _tsSpState.html('核算中');
					 }
					 _tsSpState.html('待领款');
				 }else if(status == 1){
					 _tsSpState.css("color","#428bca");
					 _tsSpState.html('审核中');
				 }else if(status == -1){
					 var  reLoanState = loanObj.reLoanState;
					//提交报销
					 if(EASYWIN.userInfo.id == loanObj.creator && reLoanState==1){
						 var loanOffA = $('<a addloan="1" href="javascript:void(0)"></a>');
						 $(loanOffA).html('再提交');
						 _tsSpState.html($(loanOffA)); 
						 
					 }else{
						 _tsSpState.css("color","red");
						 _tsSpState.html('失败');
					 }
					 
				 }else {
					 var loanApplyState = loanObj.loanApplyState;
					 if(loanApplyState == 4){
						 _tsSpState.css("color","blue");
						 //提交报销
						 if(EASYWIN.userInfo.id == loanObj.creator){
							 var loanOffA = $('<a addLoan=1 href="javascript:void(0)"></a>');
							 $(loanOffA).html('待提交');
							 _tsSpState.html($(loanOffA)); 
							 
						 }else{
							 var loanOffA = $('<span style="color:grey"></span>');
							 $(loanOffA).html('待提交');
							 _tsSpState.html($(loanOffA)); 
						 }
					 }else if(loanApplyState == 1){
						 _tsSpState.css("color","#428bca");
						 _tsSpState.html('审核中');
					 }else if(loanApplyState == -1){
						 _tsSpState.css("color","red");
						 _tsSpState.html('失败');
					 }else{
						 _tsSpState.css("color","purple");
						 //提交报销
						 if(EASYWIN.userInfo.id == loanObj.creator && loanObj.loanApplyInsId>0){
							 var loanOffA = $('<a class="spFlowIns"  href="javascript:void(0)"></a>');
							 $(loanOffA).attr('insId',loanObj.loanApplyInsId);
							 $(loanOffA).html('待提交');
							 _tsSpState.html($(loanOffA)); 
							 
						 }else{
							 var loanOffA = $('<span style="color:grey"></span>');
							 $(loanOffA).html('待提交');
							 _tsSpState.html($(loanOffA)); 
						 }
					 }
				 }
			 }
			 $(_tr).append(_tsSpState);
			 
			 $(_tr).data("loanData",loanObj);
			
			 $("#allTodoBody").append(_tr);
		 })
	 }
 }
 
 function resetIndex(){
	 var rowIndex = 1;
	 var preLoanRepId = 0;
	 $.each($("#allTodoBody").find("tr"),function(trIndex,trObj){
		 var  feeBudgetId = $(this).attr("feeBudgetId");
		 if(preLoanRepId == feeBudgetId ){
			 return true;
		 }else{
			 $(this).find("td:eq(0)").html(rowIndex);
			 rowIndex = rowIndex+1;
			 preLoanRepId = feeBudgetId
		 }
	 })
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
 