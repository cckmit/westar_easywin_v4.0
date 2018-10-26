/**
 * 关联出差记录的借款申请
 * @param busMapFlowIdOfLoan 借款申请配置主键
 */
function listLoanApplyToSelect(busMapFlowIdOfLoan){
	window.top.layer.open({
		type: 2,
		title:"<span style=\"font-weight:bold;font-size:20px;color:#2dc3e8;\">个人出差记录</span>",
		closeBtn:0,
		area: ['700px','500px'],
		fix: true, //不固定
		maxmin: false,
		scrollbar:false,
		move: false,
		zIndex:1010,
		shade:0.3,
		scrollbar:false,
		content: ["/financial/loanApply/listLoanApplyToSelect?pager.pageSize=10&sid="+sid,'no'],
		btn: ['确认','关闭'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var trip = iframeWin.selectedTrip();
			var url = "/financial/loan/addLoan?sid="+sid+"&busMapFlowId="+busMapFlowIdOfLoan+"&tripId="+trip.tripId;
			openWinByRight(url);
			layer.close(index);
		},end:function(){
		}
	});
}

/**
 * 关联出差记录的借款申请
 * @param busMapFlowIdOfLoan 借款申请配置主键
 */
function listLoanToSelect(busMapFlowIdOfLoan){
	window.top.layer.open({
		type: 2,
		title:"<span style=\"font-weight:bold;font-size:20px;color:#2dc3e8;\">个人借款记录</span>",
		closeBtn:0,
		area: ['700px','500px'],
		fix: true, //不固定
		maxmin: false,
		scrollbar:false,
		move: false,
		zIndex:1010,
		shade:0.3,
		scrollbar:false,
		content: ["/financial/loan/listLoanToSelect?pager.pageSize=10&sid="+sid,'no'],
		btn: ['确认','关闭'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var loan = iframeWin.selectedTrip();
			var url = "/financial/writeOff/addLoanWriteOff?sid="+sid+"&busMapFlowId="+busMapFlowIdOfLoan+"&loanId="+loan.loanId;
			openWinByRight(url);
			layer.close(index);
		},end:function(){
		}
	});
}
//导出Excel
function excelExport(fileName){
	var html = $("#tableView").html();
	tableToXls(html,fileName,sid);
}
//出差申请审批记录状态
function tripState(trip){
	var stateInfo={"flowSate":"","color":""}
    if(trip.flowState==0){
 	  stateInfo["flowSate"] = "无效的";
 	  stateInfo["color"] = "gray";
    }else if(trip.flowState==1){
 	  stateInfo["flowSate"] = "审批中";
 	  stateInfo["color"] = "blue";
    }else if(trip.flowState==2){
 	  stateInfo["flowSate"] = "草稿";
 	  stateInfo["color"] = "fuchsia";
    }else if(trip.flowState==4){
 	   if(trip.spState==0){
    	 	  stateInfo["flowSate"] = "驳回";
    	 	  stateInfo["color"] = "red";
 	   }else if(trip.spState==1){
    	 	  stateInfo["flowSate"] = "通过";
    	 	  stateInfo["color"] = "green";
 	   }
    }
	return stateInfo;
}
//借款申请审批记录状态
function loanSate(loanVo,userInfo){
	var stateInfo={"loanFlowSate":"","color":""}
    if(loanVo.flowState==0){
 	   stateInfo["loanFlowSate"] = "无效的";
 	   stateInfo["color"] = "gray";
    }else if(loanVo.flowState==1){
 	   stateInfo["loanFlowSate"] = "审批中";
 	   stateInfo["color"] = "blue";
    }else if(loanVo.flowState==2){
 	   stateInfo["loanFlowSate"] = "草稿";
 	   stateInfo["color"] = "fuchsia";
    }else if(loanVo.flowState==4){
 	   if(loanVo.spState==0){
     	   stateInfo["loanFlowSate"] = "驳回";
     	   stateInfo["color"] = "red";
 	   }else if(loanVo.spState==1){
	    	   if(!strIsNull(loanVo.writeOffFlowInstanceId) && loanVo.writeOffFlowState != 0){
	    		   if(loanVo.writeOffStatus == 1 
	    				   && loanVo.writeOffFlowSpState == 1 
	    				   && loanVo.writeOffFlowState == 1){
			     	   stateInfo["loanFlowSate"] = "销账中";
			     	   stateInfo["color"] = "blue";
	    		   }else if(loanVo.writeOffStatus == 4 
							&& loanVo.writeOffFlowSpState == 1 
							&& loanVo.writeOffFlowState == 4){
			     	   stateInfo["loanFlowSate"] = "已销账";
			     	   stateInfo["color"] = "green";
	    		   }
	    	   }else{
	    		  if(loanVo.creator == userInfo.id){
			     	   stateInfo["loanFlowSate"] = "销账";
			     	   stateInfo["color"] = "red";
	    		  }else{
			     	   stateInfo["loanFlowSate"] = "未销账";
			     	   stateInfo["color"] = "red";
	    		  } 
	    	   }
 	   }
    }
	return stateInfo;
}

//点击事件绑定
function clickEventBinding(clickObj,userInfo){
	var data = $(clickObj).parents("tr").data("loanData");
	if($(clickObj).hasClass("writeOffClass")){
		viewSpFlow(data.writeOffFlowInstanceId);
	}else if($(clickObj).hasClass("otherClass")){
		if(!strIsNull(data.writeOffFlowInstanceId) && data.writeOffFlowState != 0){
			viewSpFlow(data.writeOffFlowInstanceId);
 	   }else{
 		  if(data.creator == userInfo.id){
 		   alert("添加销账事件。");
 		  } 
 	   }
	}else{
		viewSpFlow(data.instanceId);
	}
}


//费用类型基本信息配置
function settingConsume(index,ts){
	
	//当前活动选项
	var preActive = $(".submenu").find(".active");
	//当前活动选项移除背景色
	$(preActive).removeClass();
	//配置设置背景色
	$(ts).parent().addClass("active bg-themeprimary");
	
	if(index==1){//费用类型维护
		layer.open({
			  type: 2,
			  title: false,
			  closeBtn:0,
			  area: ['800px', '500px'],
			  fix: false, //不固定
			  maxmin: false,
			  scrollbar:false,
			  content: ["/consume/listConsumeType?sid="+sid,'no'],
			  cancel: function(){ 
			    //右上角关闭回调
			  },end:function(index){
				//配置删除背景色
				  $(ts).parent().removeClass();
				  //恢复前一个选项的背景色
				  $(preActive).addClass("active bg-themeprimary");
			  }
			});
	}
}