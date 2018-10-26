$(function(){
	//添加采购单记录
	$("#addBgypPurOrder").on("click",function(){
		if(cgy){//只有采购员才有资格
			var url = "/bgypPurOrder/addBgypPurOrderPage?sid="+sid;
			openWinByRight(url);
		}
	})
	//编辑采购单
	$("body").on("click",".updatePurOrder",function(){
		if(cgy){//只有采购员才有资格
			var busId = $(this).attr("busId");
			var url = "/bgypPurOrder/updateBgypPurOrderPage?sid="+sid+"&purOrderId="+busId;
			openWinByRight(url);
		}
	})
	//采购单送审
	$("body").on("click",".sendSpPurOrder",function(){
		var purOrderId = $(this).attr("busId");
		
		//是否为办公用品管理人员（审核采购和申领）
		getSelfJSON("/modAdmin/ajaxListModAdmin",{sid:sid,busType:'02701'},function(data){
			if(data.status=='y'){
				var listModAdmin = data.listModAdmin;
				if(listModAdmin && listModAdmin.length>0){
					//取得采购单详情信息
					getSelfJSON('/bgypPurOrder/updateBgypPurOrderState',{sid:sid,purOrderId:purOrderId,purOrderState:1},function(data){
						if(data.status=='y'){
							showNotification(1,"操作成功")
							window.self.location.reload();
						}else{
							showNotification(2,data.info);
						}
						
					})
				}else{
					//处理没有管理员的状况
					parent.handleNoAdmin('027');
				}
			}else{
				showNotification(2,data.info);
			}
		})
	})
	//采购单审核
	$("body").on("click",".spViewPurOrder,.viewPurOrder",function(){
		var busId = $(this).attr("busId");
		var url = "/bgypPurOrder/viewBgypPurOrderPage?sid="+sid+"&purOrderId="+busId;
		openWinByRight(url);
	})
	//查看采购单
	$("body").on("click",".purOrderDetail",function(){
		
	})
	//删除采购单
	$("body").on("click",".batchDelData",function(){
		
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除采购单吗？', {icon: 3, title:'提示'}, function(index){
			  var url = reConstrUrl('ids');
			  $("#delForm input[name='redirectPage']").val(url);
			  $('#delForm').submit();
			  window.top.layer.close(index);
			});
		}else{
			window.top.layer.msg('请先选择需要删除的采购单', {icon: 6});
		}
	})
	
})