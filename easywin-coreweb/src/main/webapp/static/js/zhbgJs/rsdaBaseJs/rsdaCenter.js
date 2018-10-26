$(function(){
	
	//修改人事档案
	$("body").on("click",".updateRsdaBase",function(){
		var userId = $(this).attr("userId");
		var url = "/rsdaBase/updateRsdaBasePage?sid="+EasyWin.sid+"&userId="+userId;
		openWinByRight(url);
	})
	//查看人事档案
	$("body").on("click",".viewRsdaBase",function(){
		var userId = $(this).attr("userId");
		var url = "/rsdaBase/viewRsdaBaseByUserId?sid="+EasyWin.sid+"&userId="+userId;
		openWinByRight(url);
	})
	
	
	//添加奖惩
	$("body").on("click","#addRsdaIncBtn",function(){
		var url = "/rsdaInc/addRsdaIncPage?sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//修改奖惩
	$("body").on("click",".updateRsdaInc",function(){
		var rsdaIncId  = $(this).attr("busId");
		var url = "/rsdaInc/updateRsdaIncPage?rsdaIncId="+rsdaIncId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//查看奖惩
	$("body").on("click",".viewRsdaInc",function(){
		var rsdaIncId  = $(this).attr("busId");
		var url = "/rsdaInc/viewRsdaIncPage?rsdaIncId="+rsdaIncId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//删除奖惩
	$("body").on("click",".batchDelRsdaIncData",function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除奖惩信息吗？', {icon: 3, title:'提示'}, function(index){
			  var url = reConstrUrl('ids');
			  $("#delForm input[name='redirectPage']").val(url);
			  $('#delForm').submit();
			  window.top.layer.close(index);
			});
		}else{
			window.top.layer.msg('请先选择需要删除的奖惩信息', {icon: 2});
		}
	});
	
	
	
	//添加工作经历
	$("body").on("click","#addjobHisBtn",function(){
		var url = "/jobHistory/addJobHistoryPage?sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//修改工作经历
	$("body").on("click",".updateJobHis",function(){
		var jobHisId  = $(this).attr("busId");
		var url = "/jobHistory/updateJobHistoryPage?jobHisId="+jobHisId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//查看奖惩
	$("body").on("click",".viewJobHis",function(){
		var jobHisId  = $(this).attr("busId");
		var url = "/jobHistory/viewJobHistoryPage?jobHisId="+jobHisId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//删除工作经历
	$("body").on("click",".batchDelHobHisData",function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除工作经历吗？', {icon: 3, title:'提示'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').submit();
				window.top.layer.close(index);
			});
		}else{
			window.top.layer.msg('请先选择需要删除的工作经历', {icon: 2});
		}
	});
	
	//添加人事调动
	$("body").on("click","#addRsdaTranceBtn",function(){
		var url = "/rsdaTrance/addRsdaTrancePage?sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//修改人事调动
	$("body").on("click",".updateRsdaTrance",function(){
		var radaTranceId  = $(this).attr("busId");
		var url = "/rsdaTrance/updateRsdaTrancePage?radaTranceId="+radaTranceId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//查看人事调动
	$("body").on("click",".viewRsdaTrance",function(){
		var radaTranceId  = $(this).attr("busId");
		var url = "/rsdaTrance/viewRsdaTrancePage?radaTranceId="+radaTranceId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//删除人事调动
	$("body").on("click",".batchDelRsdaTranceData",function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除人事调动经历吗？', {icon: 3, title:'提示'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').submit();
				window.top.layer.close(index);
			});
		}else{
			window.top.layer.msg('请先选择需要删除的人事调动经历', {icon: 2});
		}
	});
	
	//添加离职信息
	$("body").on("click","#addRsdaLeaveBtn",function(){
		var url = "/rsdaLeave/addRsdaLeavePage?sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//修改离职信息
	$("body").on("click",".updateRsdaLeave",function(){
		var rsdaLeaveId  = $(this).attr("busId");
		var url = "/rsdaLeave/updateRsdaLeavePage?rsdaLeaveId="+rsdaLeaveId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//查看离职信息
	$("body").on("click",".viewRsdaLeave",function(){
		var rsdaLeaveId  = $(this).attr("busId");
		var url = "/rsdaLeave/viewRsdaLeavePage?rsdaLeaveId="+rsdaLeaveId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//删除离职信息
	$("body").on("click",".batchDelRsdaLeaveData",function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除离职信息吗？', {icon: 3, title:'提示'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').submit();
				window.top.layer.close(index);
			});
		}else{
			window.top.layer.msg('请先选择需要删除的离职信息', {icon: 2});
		}
	});
	
	//添加复职信息
	$("body").on("click","#addRsdaResumeBtn",function(){
		var url = "/rsdaResume/addRsdaResumePage?sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//修改复职信息
	$("body").on("click",".updateRsdaResume",function(){
		var resumeId  = $(this).attr("busId");
		var url = "/rsdaResume/updateRsdaResumePage?resumeId="+resumeId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//查看复职信息
	$("body").on("click",".viewRsdaResume",function(){
		var resumeId  = $(this).attr("busId");
		var url = "/rsdaResume/viewRsdaResumePage?resumeId="+resumeId+"&sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//删除复职信息
	$("body").on("click",".batchDelRsdaLeaveData",function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除复职信息吗？', {icon: 3, title:'提示'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').submit();
				window.top.layer.close(index);
			});
		}else{
			window.top.layer.msg('请先选择需要删除的复职信息', {icon: 2});
		}
	});
	
	
	
})