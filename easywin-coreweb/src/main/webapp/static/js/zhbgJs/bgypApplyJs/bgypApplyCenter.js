$(function(){
	//添加采购单记录
	$("#addBgypApply").on("click",function(){
		var url = "/bgypApply/addBgypApplyPage?sid="+EasyWin.sid;
		openWinByRight(url);
	})
	//编辑采购单
	$("body").on("click",".updateBgypApply",function(){
		var busId = $(this).attr("busId");
		var url = "/bgypApply/updateBgypApplyPage?sid="+EasyWin.sid+"&applyId="+busId;
		openWinByRight(url);
	})
	//编辑采购单
	$("body").on("click",".spCheckBgypApply",function(){
		var busId = $(this).attr("busId");
		var url = "/bgypApply/viewBgypApplyPage?sid="+EasyWin.sid+"&applyId="+busId;
		openWinByRight(url);
	})
	//删除采购单
	$("body").on("click",".batchDelData",function(){
		
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除申领单吗？', {icon: 3, title:'提示'}, function(index){
			  var url = reConstrUrl('ids');
			  $("#delForm input[name='redirectPage']").val(url);
			  $('#delForm').submit();
			  window.top.layer.close(index);
			});
		}else{
			window.top.layer.msg('请先选择需要删除的申领单', {icon: 2});
		}
	})
	
})