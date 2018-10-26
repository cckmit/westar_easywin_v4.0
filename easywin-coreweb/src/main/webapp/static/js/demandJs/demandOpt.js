$(function(){
	//选择按钮操作
	$("body").on("click","[opt-selectBtn]",function(){
		var opt = $(this).attr("opt-selectBtn");
		var f = eval("demandOpt."+opt + "()");
	});
	//移除项目属性
	$("body").on("dblclick","#itemName",function(){
		demandOpt.removeItemInfo();
	});
	//星级选择
	$("body").on("click","#startLevelSpan span",function(){
		var index = $(this).index();
		var startLevel = index + 1;
		$("#startLevel").val(startLevel);
		
		$("#startLevelSpan").find("span").removeClass("fa-star");
		$("#startLevelSpan").find("span").removeClass("fa-star-o");
		for(var i=0;i<5;i++){
			if(i <= index){
				$("#startLevelSpan").find("span:eq("+(i)+")").addClass("fa-star");
			}else{
				$("#startLevelSpan").find("span:eq("+(i)+")").addClass("fa-star-o");
			}
		}
	});
	//操作按钮
	$("body").on("click","[opt-handleBtn]",function(){
		var opt = $(this).attr("opt-handleBtn");
		var f = eval("demandOpt."+opt + "()");
	});
	
	//初始化操作按钮
	if(pageParam && pageParam.tag && pageParam.tag == 'updateOpt'){
		demandOpt.initOptBtn();
	}
})

var demandOpt = {
	btns:{
		"accept":'<a href="javascript:void(0)" class="blue" opt-handleBtn="acceptDemand"><i class="fa fa-save"></i>受理</a>'
		,"reject":'<a href="javascript:void(0)" class="blue" opt-handleBtn="rejectDemand"><i class="fa fa-save"></i>拒绝</a>'
		,"trans":'<a href="javascript:void(0)" class="blue" opt-handleBtn="handleDemand"><i class="fa fa-save"></i>移交处理</a>'
		,"spFlow":'<a href="javascript:void(0)" class="blue" opt-handleBtn="addDeamandSpflow"><i class="fa fa-save"></i>发起流程 </a>'
		,"task":'<a href="javascript:void(0)" class="blue" opt-handleBtn="addDeamandTask"><i class="fa fa-save"></i>任务安排 </a>'
		,"submit":'<a href="javascript:void(0)" class="blue" opt-handleBtn="confirmDemandResult"><i class="fa fa-save"></i>成果确认 </a>'
		,"finish":'<a href="javascript:void(0)" class="blue" opt-handleBtn="finishDeamand"><i class="fa fa-save"></i>结束 </a>'
		
	},
	initOptBtn:function(){
		var demand = pageParam.demand;
		var sessionUser = pageParam.userInfo;
		
		var state = demand.state;
		if(state == '1' && sessionUser.id == demand.handleUser){
			//需求受理
			$("body").find("#btnsDiv").append(demandOpt.btns.accept);
			$("body").find("#btnsDiv").append(demandOpt.btns.reject);
		}else if(state == '2' && sessionUser.id == demand.handleUser){
			//需求分析
			$("body").find("#btnsDiv").append(demandOpt.btns.trans);
		}else if(state == '3' && sessionUser.id == demand.handleUser){
			//需求处理
			$("body").find("#btnsDiv").append(demandOpt.btns.spFlow);
			$("body").find("#btnsDiv").append(demandOpt.btns.task);
			$("body").find("#btnsDiv").append(demandOpt.btns.submit);
		}else if(state == '4' && sessionUser.id == demand.creator){
			//需求确认
			$("body").find("#btnsDiv").append(demandOpt.btns.finish);
		}
	},itemSelectForDemand:function(){
		//项目选择用于需求
		itemSelectForDemand(1,null,function(items){
			if(items && items[0]){
				$.each(items,function(index,item){
					//设置关联项目
					$("body").find("input[name=itemId]").val(item.id);
					$("body").find("#itemName").val(item.itemName);
					
					//设置项目负责人
					$("body").find("input[name=handleUser]").val(item.owner);
					$("body").find("#itemOwner").val(item.ownerName);
					
					//设置所属产品
					$("body").find("input[name=productId]").val(item.productId);
					$("body").find("input[name=productName]").val(item.productName);
				});
			}
		});
	},
	//移除选择信息
	removeItemInfo:function(){
		//设置关联项目
		$("body").find("input[name=itemId]").val('');
		$("body").find("#itemName").val('');
		//设置项目负责人
		$("body").find("input[name=handleUser]").val("");
		$("body").find("#itemOwner").val('');
		//设置所属产品
		$("body").find("input[name=productId]").val('');
		$("body").find("input[name=productName]").val('');
		//设置所属模块
		$("body").find("input[name=productId]").val('');
		$("body").find("input[name=productName]").val('');
		//设置所属模块
		$("body").find("input[name=itemModId]").val('');
		$("body").find("input[name=itemModName]").val('');
	},
	//产品模块选择
	itemModSelectForDemand:function(){
		var itemId = $("body").find("input[name=itemId]").val();
		if(itemId){
			var param = {
					busId:itemId
					,busName:$("#itemName").val()
					,busType:"005"
					,sid:pageParam.sid
			}
			functionSelect(1,param,function(result){
				if(result){
	                $("#itemModName").val(result.busName);
	                $("#itemModId").val(result.parentId);
	            }else{
	            	 $("#itemModName").val('');
	                 $("#itemModId").val('');
	            }
			})
		}else{
			layer.tips("请先选择项目信息",$("#itemName"),{"tips":1});
		}
	},
	//需求的任务安排
	addDeamandTask:function(){
		var param = {
				 busId:pageParam.demand.id,
				 busType:"070",
				 busName:"需求"+pageParam.demand.serialNum,
				 taskRemark:"",
				 expectTime:pageParam.demand.expectFinishDate,
				 version:"2"
		}
		//发起审批关联任务业务
		addBusTaskForMod(pageParam.sid,param);
	},
	//接收需求信息
	addDeamandSpflow:function(){
		var busType = '070';
		listBusMapFlows(busType,function(busMapFlow){
			var url = "/busRelateSpFlow/demand/addDemanfHandleApply?sid="+pageParam.sid;
			url +="&busMapFlowId="+busMapFlow.id
			url +="&demandId="+pageParam.demand.id
			url +="&busType="+busType;
			openWinByRight(url);
		})
	},
	//接收需求信息
	acceptDemand:function(){
		window.top.layer.confirm("开始受理该需求?", {icon: 3, title:'确认对话框'}, function(index){
			window.top.layer.close(index);
			
			var param = {
					"sid":pageParam.sid,
					"id":pageParam.demand.id,
					"state":"2"
			}
			var url = "/demand/doHandleDemand"
			postUrl(url,param,function(result){
				if(result.code == 0){
					showNotification(1, "操作成功！");
					window.self.location.reload();
				}else{
					showNotification(2, result.msg);
				}
			})
		});
	//移交需求用于处理
	},handleDemand:function(){
			window.top.layer.confirm("移交办理该需求?", {icon: 3, title:'确认对话框'}, function(index){
				window.top.layer.close(index);
				var param = {
						"sid":pageParam.sid,
						"id":pageParam.demand.id,
						"state":"3"
				}
				var url = "/demand/doHandleDemand"
					postUrl(url,param,function(result){
						if(result.code == 0){
							showNotification(1, "操作成功！");
							window.self.location.reload();
						}else{
							showNotification(2, result.msg);
						}
					})
			});
	//确认需求信息
	},confirmDemandResult:function(){
			window.top.layer.confirm("需求已处理，提交确认成果?", {icon: 3, title:'确认对话框'}, function(index){
				window.top.layer.close(index);
				
				var param = {
						"sid":pageParam.sid,
						"id":pageParam.demand.id,
						"state":"4"
				}
				var url = "/demand/doHandleDemand"
					postUrl(url,param,function(result){
						if(result.code == 0){
							showNotification(1, "操作成功！");
							window.self.location.reload();
						}else{
							showNotification(2, result.msg);
						}
					})
			});
			//接收需求信息
	},finishDeamand:function(){
		window.top.layer.confirm("成果已确认，结束此需求?", {icon: 3, title:'确认对话框'}, function(index){
			window.top.layer.close(index);
			
			var param = {
					"sid":pageParam.sid,
					"id":pageParam.demand.id,
					"state":"5"
			}
			var url = "/demand/doHandleDemand"
				postUrl(url,param,function(result){
					if(result.code == 0){
						showNotification(1, "操作成功！");
						window.self.location.reload();
					}else{
						showNotification(2, result.msg);
					}
				})
		});
	},rejectDemand:function(){
		demandOpt.rejectDemandDialog(function(rejectReason){
			var param = {
					"sid":pageParam.sid,
					"id":pageParam.demand.id,
					"state":"-1",
					"rejectReason":rejectReason
			}
			var url = "/demand/doHandleDemand"
			postUrl(url,param,function(result){
				if(result.code == 0){
					showNotification(1, "操作成功！");
					window.self.location.reload();
				}else{
					showNotification(2, result.msg);
				}
			})
		})
	},rejectDemandDialog:function(callback){
		window.top.layer.prompt({
			formType: 2,
			  value: '',
			  title: '请输入拒绝的理由'
			}, function(value,index){
				if(!value){
					showNotification(2,"拒绝理由不能为空！")
					return;
				}
				callback(value);
				window.top.layer.close(index);
			});
	}
}
//移除修改模块
function removeProductMod() {
	//设置所属模块
	$("body").find("input[name=itemModId]").val('');
	$("body").find("input[name=itemModName]").val('');
}