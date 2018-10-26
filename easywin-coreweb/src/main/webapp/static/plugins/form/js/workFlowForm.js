
define("easywin/workFlow/formView",["form.js"],
function(g) {
	var p = g("form/form-plugin");
	
	var loading = $('<div id="loading" class="layui-layer-load" style="width: 100%;height: 50px"></div>');
	$("#form-info-div").after($(loading))
	
	// 渲染表单
	formPlugin.renderFormDataByLayoutId({
		parentEl: $(".form-view_js"),
		dataId: FLOWINFO.instanceId,
		readOnly: false,
		layoutId: FLOWINFO.layoutId,
		notDefault: !0,
		module: "freeform",
		callback: function(a) {
			$(".form-view_js").data("form").dataId=FLOWINFO.instanceId
			$("#spRelate").parent().show();
			$(loading).remove();
			var sonInstanceId = FLOWINFO.sonInstanceId;
			if(sonInstanceId){
				constrSonRelateHtml();
			}
		}
	})
	$("#form-info-div").data("flowInfo",FLOWINFO);
	$("#busType").data("preBusType",TEAMS.spFlow.busType);
	
	// 保存表单
	$("body").on("click", "#saveFreeForm",function(){
		$("#form-info-div").data("flowInfo").saveType="add";//提交类型变更
		$(".form-view_js").data("spResult",{"spState":1});
		comPonData()&& formPlugin.saveFormData({
             parentEl: $(".form-view_js"),
             dataStatus:0,
             isDel: 0,
             module: "freeform",
             callback: function(data) {
            	var status = data.status;
            	if(status=='f'){
 					showNotification(2,data.info);
 				}else if(data.message){
 				}else{
 					openWindow.ReLoad();
 					closeWin();
 				}
             }
         })
     });
	// 提交表单(发起的时候)
	$("body").on("click", "#submitFreeForm",function(){
		$("#form-info-div").data("flowInfo").saveType="add";//提交类型变更
		$(".form-view_js").data("spResult",{"spState":1});
    	
		//验证借款信息
    	comPonData()&& formPlugin.submitPreCheck({
    		parentEl: $(".form-view_js"),
    		callback:function(msg){
    			if(msg){
					return;
				}
        		formPlugin.saveFormData({
            		parentEl: $(".form-view_js"),
            		dataStatus:1,
            		isDel: 0,
            		module: "freeform",
            		callback: function(data) {
            			var status = data.status;
            			if(status=='f'){
            				showNotification(2,data.info);
            			}else if(data.message){
            			}else{
            				openWindow.ReLoad();
            				closeWin();
            			}
            		}
            	})
        	}
    	}) 
    	
	});
	//审批转办
	$("body").on("click","#updateSpInsAssign",function(){
		updateSpInsAssign(FLOWINFO.instanceId,FLOWINFO.actInstaceId);
		
	});
	//审批关联任务
	$("body").on("click","#addBusTaskForSp",function(){
		var  reloadState = $("#addBusTaskForSp").data("reloadState");
		formPlugin.constrContent(function(content,fileLists){
			var paramObj = {}
			paramObj.busId = FLOWINFO.instanceId;
			paramObj.busType = '022';
			paramObj.busName = FLOWINFO.flowName;
			paramObj.taskRemark = content;
			paramObj.fileLists = fileLists;
			parent.addBusTaskForMod(sid,paramObj);
			
			if(reloadState && reloadState=='1'){
				window.self.location.reload();
			}
			
		},FLOWINFO.instanceId);
	});
	//审批拾取
	$("body").on("click","#pickSpIns",function(){
		pickSpIns(FLOWINFO.instanceId,FLOWINFO.actInstaceId);
	});
	
	// 审批表单(下一步)
	$("body").on("click","#spFormAgree",function(){
		$(".form-view_js").data("spResult",{"spState":1});
		$("#form-info-div").data("flowInfo").saveType="update";//提交类型变更
		//验证借款信息
		comPonData()&& formPlugin.submitPreCheck({
    		parentEl: $(".form-view_js"),
    		callback:function(msg){
    			if(msg){
					return;
				}
        		formPlugin.saveFormData({
            		parentEl: $(".form-view_js"),
            		dataStatus:1,
            		isDel: 0,
            		module: "freeform",
            		callback: function(data) {
            			var status = data.status;
            			if(status=='f'){
            				showNotification(2,data.info);
            			}else if(data.message){
            			}else{
            				var nextStepConfig = $("#contentBody").data("nextStepConfig");
            				if(nextStepConfig && nextStepConfig.addTaskWay=='003'){
            					$("#addBusTaskForSp").data("reloadState","1");
            					$("#addBusTaskForSp").trigger("click");
            				}else{
            					openWindow.ReLoad();
            					closeWin();
            				}
            			}
            		}
            	})
        	}
    	})
	});
	// 审批表单(终止)
	$("body").on("click","#spFormReject",function(){
		$(".form-view_js").data("spResult",{"spState":0});
		$("#form-info-div").data("flowInfo").saveType="update";//提交类型变更
		
		var params={"instanceId":FLOWINFO.instanceId,"sid":sid};
		var url = "/workFlow/ajaxSpFlowNextStep?rnd="+Math.random();
    	postUrl(url,params,function(data){
			if(data.status=='f'){
				showNotification(2,data.info);
			}else if(data.stepInfo.stepType == 'huiqianing'){
    			window.top.layer.confirm('当前流程会签未完成，是否撤销并继续回退？', {icon: 3, title:'确认对话框',
				  btn: ['撤销并终止','等待会签'] //按钮
				}, function(index){
					window.top.layer.close(index);//关闭确认对话框
					nextStepCheckAndSaveData();//步骤其他验证以及表单数据保存
				});
			}else{
				nextStepCheckAndSaveData();//步骤其他验证以及表单数据保存 
			}
		});
	});
	//审批会签
	$("body").on("click","#updateSpHuiQian",function(){
		$("#form-info-div").data("flowInfo").saveType="huiqian";//提交类型变更
    	$("#form-info-div").data("flowInfo").activitiTaskId="";
    	var url = '/workFlow/spHuiQian?sid=' + sid;
		url = url+"&instanceId="+FLOWINFO.instanceId;
		
		window.top.layer.open({
	  		  type: 2,
	  		  title:false,
	  		  closeBtn:0,
	  		  area: ['700px', '480px'],
	  		  fix: true, //不固定
	  		  maxmin: false,
	  		  move: false,
	  		  content: [url,'no'],
	  		  btn: ['确定','关闭'],
	  		  yes: function(index, layero){
	  			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	  			var joinConf = iframeWin.returnJoinConf();
	  			if(joinConf){//有会签人员
	  				window.top.layer.close(index);
	  				
	  				var url = "/workFlow/updateSpHuiQian";
					var params={"instanceId":FLOWINFO.instanceId,
		    				"sid":sid};
					var modFormStepData = {};
					modFormStepData.jointProcessUsers = joinConf.jointProcessUsers;
					modFormStepData.msgSendFlag = joinConf.msgSendFlag;
					modFormStepData.content = joinConf.content;
//					modFormStepData.cover = joinConf.cover;
					params.modFormStepDataStr = JSON.stringify(modFormStepData);
					postUrl(url, params, function(data){
						var status = data.status;
						if(status=='f'){
							showNotification(2,data.info);
						}else{
							$(parent.document,"#tabInfo").find(".index-active").find(".index-close").click();
							window.location.reload();
						}
					})
	  			}
	  		 },success:function(layero,index){
	  			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				 $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 window.top.layer.close(index);
				 })
	  		  },end:function(){
	  		  }
	  		});
	});
	//会签反馈
	$("body").on("click","#subSpHuiQian",function(){
		$("#form-info-div").data("flowInfo").saveType="huiqian";//提交类型变更
    	$("#form-info-div").data("flowInfo").activitiTaskId="";
    	var url = '/workFlow/subSpHuiQianPage?sid=' + sid;
		url = url+"&instanceId="+FLOWINFO.instanceId;
		
		window.top.layer.open({
	  		  type: 2,
	  		  title:false,
	  		  closeBtn:0,
	  		  area: ['700px', '480px'],
	  		  fix: true, //不固定
	  		  maxmin: false,
	  		  move: false,
	  		  content: [url,'no'],
	  		  btn: ['确定','关闭'],
	  		  yes: function(index, layero){
	  			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	  			var joinConf = iframeWin.returnJoinConf();
	  			if(joinConf){//有会签人员
	  				window.top.layer.close(index);
	  				
	  				var url = "/workFlow/subSpHuiQian";
					var params={"busId":FLOWINFO.instanceId,
		    				"sid":sid};
					params.huiQianContent = joinConf.huiQianContent;
					params.msgSendFlag = joinConf.msgSendFlag;
					postUrl(url, params, function(data){
						var status = data.status;
						if(status=='f'){
							showNotification(2,data.info);
						}else{
							openWindow.ReLoad();
							closeWin();
							//$(parent.document,"#tabInfo").find(".index-active").find(".index-close").click();
							//window.location.reload();
						}
					})
	  			}
	  		 },success:function(layero,index){
	  			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				 $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 window.top.layer.close(index);
				 })
	  		  },end:function(){}
	  		});
	});
	// 审批表单(自由回退)
	$("body").on("click","#spFormBack",function(){
		$(".form-view_js").data("spResult",{"spState":-1});
		$("#form-info-div").data("flowInfo").saveType="back";//提交类型变更
		
		var params={"instanceId":FLOWINFO.instanceId,"sid":sid};
		var url = "/workFlow/ajaxSpFlowNextStep?rnd="+Math.random();
    	postUrl(url,params,function(data){
			if(data.status=='f'){
				showNotification(2,data.info);
			}else if(data.stepInfo.stepType == 'huiqianing'){
    			window.top.layer.confirm('当前流程会签未完成，是否撤销并继续回退？', {icon: 3, title:'确认对话框',
				  btn: ['撤销并终止','等待会签'] //按钮
				}, function(index){
					window.top.layer.close(index);//关闭确认对话框
					nextStepCheckAndSaveData();//步骤其他验证以及表单数据保存
				});
			}else{
				nextStepCheckAndSaveData();//步骤其他验证以及表单数据保存 
			}
		});
	});
     $(".form-view_js").data("spFlow",TEAMS.spFlow);
     // 删除审批关联
     $("#spRelate").find(".relateCloseBtn").on("click",function(){
    	// 关联的模块
    	 var busType = $(this).attr("busType");
    	 var e = $(this);
    	// 若是在流程扭转过程中，但但钱步骤不是发起人，可自动修改保存关联审批
	    if(formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true"){
	    	// 设置数据
	    	var formData = {
	    			"instanceId":FLOWINFO.instanceId,// 流程实例化主键
	    			"busId":0, // 业务主键
	    			"busType":"0", // 业务类型
	    			"stagedItemId":0,// 项目阶段主键
	    			"preStageItemId":TEAMS.spFlow.preStageItemId,// 上次选中的项目阶段主键
	    			"optType":0// 操作类型 0 删除 1修改
	    	}
	    	// 删除审批关联
	    	updateSpRelate(formData,function(data){
	    		if(data.status=='f'){// 后台操作失误
	    			showNotification(2,data.info);
	    		}else{// 后台操作成功
	    	    	 TEAMS.spFlow.busId=0;
	     			 TEAMS.spFlow.busType="0";
	     			 TEAMS.spFlow.busName="";
	     			 TEAMS.spFlow.preStageItemId=0;
	     			 TEAMS.spFlow.stagedItemId=0;
	     			 TEAMS.spFlow.stagedItemName="";
	    	    	 showNotification(1,"修改成功！");
	    		}
	    		initSpRelate();
	    	});
	    }else{
	    	 // 清空缓存
	    	 $("#spRelate").removeData("spRelate"+busType);
	    	 busType=='005'&& ($("#spRelate").find(".itemStageNameShow").html(''))
	    	 // 不显示关联模块名称
	    	 $("#spRelate").find(".busNameShow").html("");
	    	 // 还原删除关联类型
	    	 $("#spRelate").find(".relateCloseBtn").removeAttr("busType");
	    	 // 隐藏删除图标
	    	 $("#spRelate").find(".relateCloseBtn").hide();
	    	 
	    	 $("#spRelate").data("spRelate0",{"busId":0,"busName":"","busType":"0","stagedItemId":0,"stagedItemName":""});
	    	 
	    }
     })
     // 默认不可以初始化审批关联
     var a=!1;
     // 是添加表单数据
     formTag && formTag=='addForm' && initSpRelate() && (a = ! 0);
    // 是编辑表单数据
     formTag && formTag=='editForm' && initSpRelate() && (a = ! 0);
     
     a && $("#spRelate").find("#busType").off("change").on("change",function(){
    	 
    	 $("#spRelate").find(".item").eq(2).find(".itemStageNameShow").html("");
    	 // 关联的模块类型
    	 var busType = $(this).val();
    	 if(busType=='0'){// 没有关联模块
    		 // 只显示第一行
    		 $("#spRelate").find(".item").hide();
    		 $("#spRelate").find(".item").eq(0).show();
    		 $("#spRelate").find(".item").eq(0).css("width","100%")
    		 // 关联模块按钮清除点击事件
    		 $("#spRelate").find("#relateBtn").off("click");
    		 
    	 }else if(busType=='003'){// 关联任务
    		 // 关联任务名称
    		 var busName = $("#spRelate").data("spRelate003")? $("#spRelate").data("spRelate003").busName:"";
    		 // 关联任务名称不为空，则填充值
    		 busName && busName!="" ? ($("#spRelate").find(".busNameShow").html(busName),$("#spRelate").find(".relateCloseBtn").attr("busType","003"),$("#spRelate").find(".relateCloseBtn").show()): 
    			 ($("#spRelate").find(".busNameShow").html(''),$("#spRelate").find(".relateCloseBtn").hide());
    		 // 显示审批关联选择和关联任务
    		 $("#spRelate").find(".item").hide();
    		 $("#spRelate").find(".item").eq(0).show();
    		 $("#spRelate").find(".item").eq(1).show();
    		 $("#spRelate").find(".item").eq(1).css("width","100%")
    		 
    		 $("#spRelate").find(".item").eq(1).find("label").html("关联任务:");
    		 // 添加任务选择事件
    		 $("#spRelate").find("#relateBtn").off("click").on("click",function(){
    			 // 任务单选
    			 listTaskForRelevance(sid,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true")){initSpRelate();}});
    		 });
    		 // 关联任务名称为空，则弹出选项
    		 busName || listTaskForRelevance(sid,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true")){initSpRelate();}});
    	 }else if(busType=='005'){// 关联项目
    		 // 关联项目名称
    		 var busName =  $("#spRelate").data("spRelate005")?$("#spRelate").data("spRelate005").busName:"";
    		 // 关联项目名称不为空，则填充值
    		 busName!=""? ($("#spRelate").find(".busNameShow").html(busName),$("#spRelate").find(".relateCloseBtn").attr("busType","005"),$("#spRelate").find(".relateCloseBtn").show()): 
    			 ($("#spRelate").find(".busNameShow").html(''),$("#spRelate").find(".relateCloseBtn").hide());
    		 // 显示审批关联选择和关联项目以及项目阶段
    		 $("#spRelate").find(".item").show();
    		 $("#spRelate").find(".item").eq(1).css("width","60%");
    		 $("#spRelate").find(".item").eq(2).css("width","40%");
    		 
    		 $("#spRelate").find(".item").eq(1).find("label").html("关联项目:");
    		 // 设置项目选择点击事件
    		 $("#spRelate").find("#relateBtn").off("click").on("click",function(){
    			 listItem(sid,null,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true") ){initSpRelate();}});
    		 })
    		 // 设置项目阶段点击事件
    		 $("#spRelate").find("#itemStageBtn").off("click").on("click",function(){
    			 var spRelate005 = $("#spRelate").data("spRelate005");
    			 var busId = spRelate005==null?0:$("#spRelate").data("spRelate005").busId;
    			 busId==0?(layer.tips("请选择关联项目", $("#spRelate").find(".busNameShow"), {tips: 1})):stagedItemSelectedPage(sid,busId)
    		 })
    		  // 关联项目名称为空，则选择项目
    		 busName !="" || listItem(sid,null,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true")){initSpRelate();}});
    	 }else if(busType=='080'){// 关联产品
             // 关联产品名称
             var busName =  $("#spRelate").data("spRelate080")?$("#spRelate").data("spRelate080").busName:"";
             // 关联产品名称不为空，则填充值
             busName!=""? ($("#spRelate").find(".busNameShow").html(busName),$("#spRelate").find(".relateCloseBtn").attr("busType","080"),$("#spRelate").find(".relateCloseBtn").show()):
                 ($("#spRelate").find(".busNameShow").html(''),$("#spRelate").find(".relateCloseBtn").hide());
             // 显示审批关联选择和关联产品以及项目阶段
             $("#spRelate").find(".pro").show();
             $("#spRelate").find(".pro").eq(1).css("width","60%");
             $("#spRelate").find(".pro").eq(2).css("width","40%");

             $("#spRelate").find(".pro").eq(1).find("label").html("关联项目:");
             // 设置产品选择点击事件
             $("#spRelate").find("#relateBtn").off("click").on("click",function(){
                 listItem(sid,null,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true") ){initSpRelate();}});
             })
             // 设置产品阶段点击事件
             $("#spRelate").find("#proStageBtn").off("click").on("click",function(){
                 var spRelate080 = $("#spRelate").data("spRelate080");
                 var busId = spRelate080==null?0:$("#spRelate").data("spRelate080").busId;
                 busId==0?(layer.tips("请选择关联产品", $("#spRelate").find(".busNameShow"), {tips: 1})):stagedProSelectedPage(sid,busId)
             })
             // 关联产品名称为空，则选择项目
             busName !="" || listItem(sid,null,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true")){initSpRelate();}});
         }else if(busType=='012'){// 关联客户
    		 // 关联客户名称
    		 var busName = $("#spRelate").data("spRelate012")? $("#spRelate").data("spRelate012").busName:"";
    		 // 关联客户名称不为空，则填充值
    		 busName && busName!=""? ($("#spRelate").find(".busNameShow").html(busName),$("#spRelate").find(".relateCloseBtn").attr("busType","012"),$("#spRelate").find(".relateCloseBtn").show()): 
    			 ($("#spRelate").find(".busNameShow").html(''),$("#spRelate").find(".relateCloseBtn").hide());
    		 // 显示审批关联选择和关联客户
    		 $("#spRelate").find(".item").hide();
    		 $("#spRelate").find(".item").eq(0).show();
    		 $("#spRelate").find(".item").eq(1).show();
    		 $("#spRelate").find(".item").eq(1).css("width","100%")
    		 
    		 $("#spRelate").find(".item").eq(1).find("label").html("关联客户:");
    		 // 设置客户选择事件
    		 $("#spRelate").find("#relateBtn").off("click").on("click",function(){
    			 listCrm(sid,null,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true")){initSpRelate();}});
    		 })
    		  // 关联客户名称为空，则选择客户
    		 busName || listCrm(sid,null,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true") ){initSpRelate();}});
    	 }
     })
     
     
     
     //打印
     $("body").on("click","#printBtn",function(){
    	 var spStateDiv = $("#formpreview").find("#spStateDiv");
    	 if(!spStateDiv || spStateDiv.length==0){
    		 var stateStr = FLOWINFO.preFlowState==4?(FLOWINFO.preSpstate==1?"通过":FLOWINFO.preSpstate==0?"驳回":""):"办理中";
    		 
    		 var modHtml='<div id="spStateDiv" class="field field_js form-field-textarea field-hoz hide">'
    			 +'<label class="widget-title">'
    		 +'	<span class="widget-title_js">审批状态</span>'
    				+'</label>'
    			+'<div class="widget-content">'
    			+'	<div class="input-instead j_readOnly">'+stateStr+'</div>'
    				+'</div>'
    			+'</div>';
    		 
    		 $("#formpreview").append(modHtml)
    	 }
    	 var spHistorys = $("#spHistorys").data("spHistorys");
    	 if(spHistorys && spHistorys.length>0){
    		 window.top.layer.open({
    			 type:0,
    			 content:'是否需要打印审批记录？',
    			 btn: ['是','否','关闭']//按钮
    			 ,icon:3
    			 ,title:'询问框'
    			 ,yes:function(index){
    				 $("#spHistorys").data("printSpHis","1");
    				 window.top.layer.close(index);
    				 constrSpHtml(spHistorys);
    				 printFlow();
    			 },btn2:function(index){
    				 $("#spHistorys").data("printSpHis","0");
    				 window.top.layer.close(index);
    				 printFlow();
				 },
			 })
    	 }else{
    		 var params = {
 					"instanceId":FLOWINFO.instanceId,
 					"sid":sid
 			}
 			getSelfJSON("/workFlow/ajaxListSpHistory",params,function (data) {
 				if(data.status=='y'){
 					var spHistoryData = data.listSpFlowHiStep;
 					$("#spHistorys").data("spHistorys",spHistoryData);
 					 if(spHistoryData && spHistoryData.length>0){
 						window.top.layer.open({
 			    			 type:0,
 			    			 content:'是否需要打印审批记录？',
 			    			 btn: ['是','否','关闭']//按钮
 			    			 ,icon:3
 			    			 ,title:'询问框'
 			    			 ,yes:function(index){
 			    				 $("#spHistorys").data("printSpHis","1");
 			    				 window.top.layer.close(index);
 			    				 constrSpHtml(spHistoryData);
 			    				 printFlow();
 			    			 },btn2:function(index){
 			    				 $("#spHistorys").data("printSpHis","0");
 			    				 window.top.layer.close(index);
 			    				 printFlow();
 							 },
 						 })
 					 }
 				}else{
 					$("#spHistorys").data("printSpHis","0");
 					printFlow();
 				}
 	    	});
    	 }
    	 
	})
	
	
	//验证手机号是否存在，并验证该步骤是否需要发送验证码
	if(!strIsNull(FLOWINFO.flowId) && !strIsNull(FLOWINFO.actInstaceId) && FLOWINFO.preFlowState!=2 ){
		//默认需要验证码
		$("#spBtnDiv").data("spCheckCfg",{"status":"f","info":"正在验证后台数据..."});
		
		//默认验证码没有输入正确
		$("#spBtnDiv").data("passYzm",{"status":"f","val":""});
		
		var params = {
				"instanceId":FLOWINFO.instanceId,
				"actInstaceId":FLOWINFO.actInstaceId,
				"sid":sid
		}
		getSelfJSON("/workFlow/checkStepCfg",params,function (data) {
			var status = data.status;
			
			if($.inArray("f",status)>=0){//后台有错误
				$("#spBtnDiv").data("spCheckCfg",{"status":"f","info":data.info});
			}else if($.inArray("y",status)>=0){//不要验证码
				$("#spBtnDiv").data("spCheckCfg",{"status":"y"});
			}else{
				var statusObj = {};
				if($.inArray("f1",status)>=0){
					statusObj.status = "f1";
					statusObj.movePhone = data.movePhone;
				}
				if($.inArray("f2",status)>=0){
					statusObj.phoneStatus = "f2";
				}
				$("#spBtnDiv").data("spCheckCfg",statusObj);
			}
		});
	}else{
		//默认不需要验证码
		$("#spBtnDiv").data("spCheckCfg",{"status":"y"});
	}
     
});
// 任务选择的返回值
function taskSelectedReturn(result){
	// 若是在流程扭转过程中，但但钱步骤不是发起人，可自动修改保存关联审批
    if(formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true"){
    	// 设置修改后的审批关联的属性
    	var formData = {
    			"instanceId":FLOWINFO.instanceId,// 流程实例化主键
    			"busId":result.id,// 任务主键
    			"busType":"003",// 关联模块
    			"stagedItemId":0,// 关联的项目阶段主键
    			"preStageItemId":TEAMS.spFlow.preStageItemId,// 先前的关联的项目阶段主键
    			"optType":1,// 操作类型 0 删除 1 修改
    			"isStage":"false" // 不是修改项目阶段主键
    	}
    	// 修改审批关联
    	updateSpRelate(formData,function(data){
    		if(data.status=='f'){// 后台操作错误
    			showNotification(2,data.info);
    		}else{
    			TEAMS.spFlow.busId=result.id;
    			TEAMS.spFlow.busType="003";
    			TEAMS.spFlow.busName=result.taskName;
    			TEAMS.spFlow.preStageItemId=0;
    			TEAMS.spFlow.stagedItemId=0;
    			TEAMS.spFlow.stagedItemName="";
    			
    			showNotification(1,"修改成功");
    		}
    		initSpRelate();
    		
    	});
    }else{
    	// 显示关联任务
    	$("#spRelate").find(".busNameShow").html(result.taskName);
    	// 显示删除按钮
    	$("#spRelate").find(".relateCloseBtn").show();
    	// 设置删除类型
    	$("#spRelate").find(".relateCloseBtn").attr("busType","003");
    	// 添加关联任务缓存
    	$("#spRelate").data("spRelate003",{"busId":result.id,"busName":result.taskName,"busType":"003","stagedItemId":0,"stagedItemName":""});
    	
    }
    	 
}
// 项目阶段返回值
function stagedItemSelectedReturn(result){
	// 修改关联项目缓存
	var spRelate = $("#spRelate").data("spRelate005");
	
	// 若是在流程扭转过程中，但但钱步骤不是发起人，可自动修改保存关联审批
    if(formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true"){
    	// 向后还传送数据对象
    	var formData = {
    			"instanceId":FLOWINFO.instanceId,// 流程实例化主键
    			"busId":spRelate.busId,// 关联业务主键
    			"busType":"005",// 关联业务类型
    			"stagedItemId":result.id,// 关联的项目阶段主键
    			"preStageItemId":TEAMS.spFlow.preStageItemId,// 先前关联的项目阶段名称
    			"optType":1,// 操作类型 0 删除 1修改
    			"isStage":"true"// 修改项目阶段标识
    	}
    	// 修改数据
    	updateSpRelate(formData,function(data){
    		if(data.status=='f'){// 修改失败
				showNotification(2,data.info);
			}else{
    			TEAMS.spFlow.preStageItemId=data.stagedItemId;
    			TEAMS.spFlow.stagedItemId=data.stagedItemId;
    			TEAMS.spFlow.stagedItemName=result.name;
    			showNotification(1,"修改成功！");
			}
    		initSpRelate();
    	});
    }else{
    	// 显示关联项目阶段的名称
    	$("#spRelate").find(".itemStageNameShow").html(result.name);
    	// 修改关联项目缓存
    	$("#spRelate").data("spRelate005",{"busId":spRelate.busId,"busName":spRelate.busName,"busType":"005","stagedItemId":result.id,"stagedItemName":result.name});
    }
}
// 产品阶段返回值
function stagedProSelectedReturn(result){
    // 修改关联项目缓存
    var spRelate = $("#spRelate").data("spRelate080");

    // 若是在流程扭转过程中，但当前步骤不是发起人，可自动修改保存关联审批
    if(formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true"){
        // 向后还传送数据对象
        var formData = {
            "instanceId":FLOWINFO.instanceId,// 流程实例化主键
            "busId":spRelate.busId,// 关联业务主键
            "busType":"080",// 关联业务类型
            "stagedProId":result.id,// 关联的项目阶段主键
            "preStageProId":TEAMS.spFlow.preStageProId,// 先前关联的项目阶段名称
            "optType":1,// 操作类型 0 删除 1修改
            "isStage":"true"// 修改项目阶段标识
        }
        // 修改数据
        updateSpRelate(formData,function(data){
            if(data.status=='f'){// 修改失败
                showNotification(2,data.info);
            }else{
                TEAMS.spFlow.preStageProId=data.stagedProId;
                TEAMS.spFlow.stagedProId=data.stagedProId;
                TEAMS.spFlow.stagedProName=result.name;
                showNotification(1,"修改成功！");
            }
            initSpRelate();
        });
    }else{
        // 显示关联项目阶段的名称
        $("#spRelate").find(".proStageNameShow").html(result.name);
        // 修改关联项目缓存
        $("#spRelate").data("spRelate080",{"busId":spRelate.busId,"busName":spRelate.busName,"busType":"080","stagedProId":result.id,"stagedProName":result.name});
    }
}
// 项目返回值
function itemSelectedReturn(id,name){
	
	// 若是在流程扭转过程中，但当前步骤不是发起人，可自动修改保存关联审批
    if(formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true"){
    	// 修改审批关联信息
    	var formData = {
    			"instanceId":FLOWINFO.instanceId,// 审批关联信息
    			"busId":id,// 关联业务主键
    			"busType":"005",// 关联业务类型
    			"stagedItemId":0,// 项目阶段主键
    			"preStageItemId":TEAMS.spFlow.preStageItemId,// 权益部项目阶段主键
    			"optType":1,// 操作类型 0 删除 1修改
    			"isStage":"false"// 项目阶段男子该标识
    	}
    	
    	updateSpRelate(formData,function(data){
    		if(data.status=='f'){
    			showNotification(2,data.info);
    		}else{
    			// 后台取得最新项目阶段
				stagedItem = data.stagedItem;
				TEAMS.spFlow.busId=id;
    			TEAMS.spFlow.busType="005";
    			TEAMS.spFlow.busName=name;
    			TEAMS.spFlow.preStageItemId=stagedItem.id;
    			TEAMS.spFlow.stagedItemId=stagedItem.id;
    			TEAMS.spFlow.stagedItemName=stagedItem.stagedName;
    			showNotification(1,"修改成功！");
    		}
    		initSpRelate();
    		
    	});
    }else{
    	// 显示关联项目的名称
    	$("#spRelate").find(".busNameShow").html(name);
    	// 显示删除按钮
    	$("#spRelate").find(".relateCloseBtn").show();
    	// 设置删除类型
    	$("#spRelate").find(".relateCloseBtn").attr("busType","005");
    	// 清除关联的项目阶段
    	$("#spRelate").find(".itemStageNameShow").html("");
    	// 添加关联项目缓存
    	$("#spRelate").data("spRelate005",{"busId":id,"busName":name,"busType":"005","stagedItemId":0,"stagedItemName":""});
    	
    	// 异步获取关联项目阶段
    	id && $.ajax({
    		type: "post",
    		url:"/item/itemLatestStagedItem?sid="+sid,
    		data:{itemId:id},
    		dataType: "json",
    		success:function(data){
    			if(data.status=='f'){
    				showNotification(data.info);
    			}else{
    				// 后台取得最新项目阶段
    				stagedItem = data.stagedItem;
    				// 显示项目阶段
    				$("#spRelate").find(".itemStageNameShow").html(stagedItem.stagedName);
    				// 修改关联项目缓存
    				$("#spRelate").data("spRelate005",{"busId":id,"busName":name,"busType":"005","stagedItemId":stagedItem.id,"stagedItemName":stagedItem.stagedName});
    			}
    		}
    	})
    }
    
}
// 客户返回值
function  crmSelectedReturn(crmId,crmName){
	
	// 若是在流程扭转过程中，但但钱步骤不是发起人，可自动修改保存关联审批
    if(formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true"){
    	// 需修改的数据信息
    	var formData = {
    			"instanceId":FLOWINFO.instanceId,// 流程实例化主键
    			"busId":crmId,// 关联业务主键
    			"busType":"012",// 关联业务类型
    			"stagedItemId":0,// 关联的项目阶段主键
    			"preStageItemId":TEAMS.spFlow.preStageItemId,// 先前关联的项目阶段主键
    			"optType":1,// 操作类型 0删除1 修改
    			"isStage":"false"// 修改项目阶段标识
    	}
    	// 修改数据
    	updateSpRelate(formData,function(data){
    		if(data.status=='f'){
    			showNotification(2,data.info);
    		}else{
    			TEAMS.spFlow.busId=crmId;
    			TEAMS.spFlow.busType="012";
    			TEAMS.spFlow.busName=crmName;
    			TEAMS.spFlow.preStageItemId=0;
    			TEAMS.spFlow.stagedItemId=0;
    			TEAMS.spFlow.stagedItemName="";
    			showNotification(1,"修改成功！");
    		}
    		initSpRelate();
    	});
    }else{
    	// 显示关联客户的名称
    	$("#spRelate").find(".busNameShow").html(crmName);
    	// 显示删除按钮
    	$("#spRelate").find(".relateCloseBtn").show();
    	// 设置删除类型
    	$("#spRelate").find(".relateCloseBtn").attr("busType","012");
    	// 添加关联客户缓存
    	$("#spRelate").data("spRelate012",{"busId":crmId,"busName":crmName,"busType":"012","stagedItemId":0,"stagedItemName":""});
    	
    }
}
// 颜值是否关联正确
function checkSpRelated(){
	// 默认关联正确
	var a=!0;
	if(formTag && formTag=='addForm'){// 是添加表单数据
		 var busType = $("#spRelate").find("#busType").val();
		 var spRelate = $("#spRelate").data("spRelate"+busType);
		 if(busType=='003'){// 关联的是任务
			 spRelate && spRelate.busId>0?a=1:(a=0,layer.tips("请选择关联任务", $("#spRelate").find(".busNameShow"), {tips: 1}));
		 }else if(busType=='005'){// 关联的是项目
			 spRelate && spRelate.busId>0?spRelate.stagedItemId>0?a=1:
				 (a=0,layer.tips("请选择关联项目阶段", $("#spRelate").find(".itemStageNameShow"), {tips: 1})):
					 (a=0,layer.tips("请选择关联项目", $("#spRelate").find(".busNameShow"), {tips: 1}));
		 }else if(busType=='012'){// 关联的是客户
			 spRelate && spRelate.busId>0?a=1:(a=0,layer.tips("请选择关联客户", $("#spRelate").find(".busNameShow"), {tips: 1}));
		 }
		 if(!spRelate){
			 busType = TEAMS.spFlow.busType;
			 spRelate = $("#spRelate").data("spRelate"+busType);
		 }
		 spRelate ? $("#spRelate").data("spRelate",spRelate):$("#spRelate").data("spRelate",{"busId":0,"busName":"","busType":"0","stagedItemId":0,"stagedItemName":""});;
		 busType=='0' || ($("#spRelate").find(".item").eq(1).show(),
		 $("#spRelate").find(".item").eq(1).css("width","100%"),
		 $("#spRelate").find(".item").eq(1).find("label").html("关联客户:"))
	}else if(formTag && formTag=='editForm'){
		 var busType = TEAMS.spFlow.busType;
		 var spRelate = $("#spRelate").data("spRelate"+busType);
		 spRelate ? $("#spRelate").data("spRelate",spRelate):$("#spRelate").data("spRelate",{"busId":0,"busName":"","busType":"0","stagedItemId":0,"stagedItemName":""});;
	}
	
	return a;
}
// 初始化审批关联
function initSpRelate(){
	// 默认不可以初始化审批关联
	var a = !1;
	var spFlow = TEAMS.spFlow;
	// 审批关联类型
	var busType = spFlow.busType?spFlow.busType:"0";
	// 审批关联模块主键
	var busId = spFlow.busId;
	// 审批关联模块名称
	var busName = spFlow.busName;
	// 审批关联项目阶段主键
	var stagedItemId = spFlow.stagedItemId;
	// 审批关联项目阶段名称
	var stagedItemName = spFlow.stagedItemName;
	// 流程表单填写人
	var creator = spFlow.creator;
	// 当前操作人员
	var currentUser = TEAMS.currentUser.userId;
	
	if(creator==currentUser){// 可以修改
		 // 清空缓存
   	 	$("#spRelate").removeData("spRelate0");
   	 	$("#spRelate").removeData("spRelate003");
   	 	$("#spRelate").removeData("spRelate005");
   	 	$("#spRelate").removeData("spRelate012");
		// 添加关联客户缓存
		$("#spRelate").data("spRelate"+busType,{"busId":busId,"busName":busName,"busType":busType,"stagedItemId":stagedItemId,"stagedItemName":stagedItemName});
		$("#busType").val(busType)
		var busTypeName = busType=="003"?"关联任务:":busType=="005"?"关联项目:":busType=="012"?"关联客户:":"未关联模块:";
		var width = busType=="005"?"60%":"100%";
		$("#spRelate").find(".item").eq(1).css("width",width);
		$("#spRelate").find(".item").eq(1).find("label").html(busTypeName);
		busType=="0"?$("#spRelate").find(".item").eq(1).hide():$("#spRelate").find(".item").eq(1).show();
		// 显示删除按钮
		busType=="0"?$("#spRelate").find(".relateCloseBtn").hide():$("#spRelate").find(".relateCloseBtn").show();
		// 设置删除类型
		$("#spRelate").find(".relateCloseBtn").attr("busType",busType);
		
		$("#spRelate").find(".item").eq(1).find(".busNameShow").html(busName);
		busType=="005"?"":$("#spRelate").find(".item").eq(2).hide();
		if(busType=="005"){
			$("#spRelate").find(".item").eq(2).css("width","40%");
			$("#spRelate").find(".item").eq(2).show();
			$("#spRelate").find(".item").eq(2).find(".itemStageNameShow").html(stagedItemName);
		}
		// 添加点击事件
		busType=="0" || $("#spRelate").find("#relateBtn").off("click").on("click",function(){
			busType=="003"?listTaskForRelevance(sid,function(flag){if(!flag && (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true")){initSpRelate()}}):busType=="005"?listItem(sid,null,function(flag){if(!flag&& (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true")){initSpRelate();}}):listCrm(sid,null,function(flag){if(!flag&& (formTag && formTag=='editForm' && TEAMS.autoUpDateRelate == "true")){initSpRelate();}});
		 })
		// 添加阶段点击事件
		busType=="005" && $("#spRelate").find("#itemStageBtn").off("click").on("click",function(){
			var spRelate005 = $("#spRelate").data("spRelate005");
			var busIdT = spRelate005==null?0:$("#spRelate").data("spRelate005").busId;
			busIdT==0?(layer.tips("请选择关联项目", $("#spRelate").find(".busNameShow"), {tips: 1})):stagedItemSelectedPage(sid,busIdT)
		});
		a = !0;
	}else{// 不可修改
		var busTypeName = busType=="003"?"任务模块":busType=="005"?"项目模块":busType=="012"?"客户模块":"未关联模块";
		var html = '<a class="j_date ellipsis department applyDepa_js">'+busTypeName+'</a>';
		$("#spRelate").find("#busType").parent().html(html);
		
		busTypeName = busType=="003"?"关联任务":busType=="005"?"关联项目":busType=="012"?"关联客户":"未关联模块";
		var width = busType=="005"?"60%":"100%";
		$("#spRelate").find(".item").eq(1).css("width",width);
		$("#spRelate").find(".item").eq(1).find("label").html(busTypeName);
		busType=="0"?"":$("#spRelate").find(".item").eq(1).show();
		
		$("#spRelate").find(".item").eq(1).find(".busNameShow").html(busName);
		$("#spRelate").find(".item").eq(1).find(".relateCloseBtn").remove();
		$("#spRelate").find(".item").eq(1).find("#relateBtn").remove();
		$("#spRelate").find(".item").eq(1).find(".busNameShow").off("onclick").on("click",function(){
			busType=="003"?$.post("/task/authorCheck?sid="+sid,{Action:"post",taskId:busId},     
					function (msgObjs){
					if(!msgObjs.succ){
						showNotification(2,msgObjs.promptMsg);
					}else{
						var url="/task/viewTask?sid="+sid+"&id="+busId;
						var height = $(window).height();
						window.top.layer.open({
							  type: 2,
							  title: false,
							  closeBtn: 0,
							  shadeClose: true,
							  shade: 0.1,
							  shift:0,
							  fix: true, // 固定
							  maxmin: false,
							  move: false,
							  area: ['800px', height+'px'],
							  content: [url,"no"] // iframe的url
							});
						}
			},"json"):busType=="005"?$.post("/item/authorCheck?sid="+sid,{Action:"post",itemId:busId},     
					function (msgObjs){
						if(!msgObjs.succ){
							showNotification(2,msgObjs.promptMsg);
						}else{
							var url="/item/viewItemPage?sid="+sid+"&id="+busId;
							var height = $(window).height();
							window.top.layer.open({
								  type: 2,
								  title: false,
								  closeBtn: 0,
								  shadeClose: true,
								  shade: 0.1,
								  shift:0,
								  fix: true, // 固定
								  maxmin: false,
								  move: false,
								  area: ['800px', height+'px'],
								  content: [url,'no'] // iframe的url
								});
						}
				},"json"):busType=="012"?$.post("/crm/authorCheck?sid="+sid,{Action:"post",customerId:busId},     
						function (msgObjs){
					if(!msgObjs.succ){
						showNotification(2,msgObjs.promptMsg);
					}else{
						var url = "/crm/viewCustomer?sid="+sid+"&id="+busId;
						var height = $(window).height();
						window.top.layer.open({
							  type: 2,
							  title: false,
							  closeBtn: 0,
							  shadeClose: true,
							  shade: 0.1,
							  shift:0,
							  fix: true, // 固定
							  maxmin: false,
							  move: false,
							  area: ['800px', height+'px'],
							  content: [url,'no'] // iframe的url
							});
					}
			},"json"):""
		})
		
		if(busType=="005"){
			$("#spRelate").find(".item").eq(2).css("width","40%");
			$("#spRelate").find(".item").eq(2).show();
			$("#spRelate").find(".item").eq(2).find(".itemStageNameShow").html(stagedItemName);
			$("#spRelate").find(".item").eq(2).find("#itemStageBtn").remove();
		}
	}
	return a;
}
// 自动修改审批关联
function updateSpRelate(formData,callback){
	$.ajax({
		type: "post",
		url:"/workFlow/updateSpRelate?sid="+sid,
		data:formData,
		dataType: "json",
		success:function(data){
			callback && callback(data)
		}
	})
}
// 组装数据 0保存1提交
function comPonData(){
	var a=1;
	if(formTag && formTag=='addForm'){// 是添加表单数据
		// 关注状态
		var attentionState = $("#attentionState").val();
		$("#form-info-div").data("flowInfo").attentionState = attentionState;
		
		var instanceName = $("#form-name").val();
		if(instanceName && $.trim(instanceName).length>0){
			// 流程名称
			$("#form-info-div").data("flowInfo").instanceName = instanceName;
		}else{
			a=!1;
			layer.tips("请输入流程名称",$("#form-name"),{tips:1});
		}
	}
	var flag = checkSpRelated();
    if(flag==0){
    	return ! 1
    }
	return a;
}
// //验证自定义审批步骤配置
function checkFreeFlowStepConfig(){
	// 默认关联正确
	var a=!0;
	// 判断是否是自定义流程，如果是，则必须要求配置流程步骤
	if(strIsNull(FLOWINFO.flowId)){
		$("#sp-steps-div").css("display","block");// 展开步骤配置
		$("#spStepTable .stepTr").each(function(i){
			if(strIsNull($(this).find("[name='spUser']").val())){
				a=!1;
				layer.tips("请先配置步骤审批人！",$(this).find(".selectUser"),{tips:1});
				return a;
			}
		});
	}
	return a;
}
// 预览表单模板
define("easywin/form/viewFormMod",["form.js"],
		function(g) {
	var p = g("form/form-plugin");
	var h = {};
    h.id = formKey;
    h.comId = TEAMS.currentUser.comId
	$.ajax({
        type: "POST",
        data: h,
        dataType: "json",
        url: "/form/findFormComp?sid="+sid,
        success: function(data) {
        	var c = $(".form-view_js");
        	var k = c.data("form");
        	k = {
        			formId: data.formLayout.formModId,
        			layoutId: data.formLayout.id,
        			readOnly: true
        	};
            var b = false,
            h = !0,
            l = null;
        	formPlugin.analyseLayout(data.formLayout, c, h, b, l);
        	
        },
        error: function() {}
    })
	
});
/**
 * 构造数据
 * @param spHistorys
 */
function constrSpHtml(spHistorys){
	var modHtml='<div class="clearfix margin-bottom-5" style="border-bottom: 1px solid #ddd;">'
		+'<div class="widget-content pull-left" style="padding: 5px 10px; vertical-align: middle; width: 50%">'
		+'<div class="input-instead j_readOnly step1">节点:直属上级审批</div>'
		+'</div>'
		+'<div class="widget-content pull-left" style="padding: 5px 10px; vertical-align: middle; width: 50%">'
		+'<div class="input-instead j_readOnly step2">办理人：罗健</div>'
		+'</div>'
		+'<div class="widget-content pull-left" style="padding: 5px 10px; vertical-align: middle; width: 50%">'
		+'<div class="input-instead j_readOnly step3">办理时间：2017-04-28</div>'
		+'</div>'
		+'<div class="widget-content pull-left" style="padding: 5px 10px; vertical-align: middle; width: 50%">'
		+'<div class="input-instead j_readOnly step4">审批意见：[下一步]</div>'
		+'</div>'
		+'</div>';
		
	if(spHistorys && spHistorys.length>0){
		var stateStr = FLOWINFO.preFlowState==4?(FLOWINFO.preSpstate==1?"通过":FLOWINFO.preSpstate==0?"驳回":""):"办理中";
		$("#spHistorys").html('');
		$.each(spHistorys,function(index,vo){
			var modObj = $(modHtml).clone();
			
			var stepname = vo.stepName ? vo.stepName:"";
			
			var executorName = vo.executorName ? vo.executorName :"";
			var endTime = vo.endTime ? vo.endTime.substr(0,19):"";
			var spMsg = vo.spMsg ? vo.spMsg : "";
			if(FLOWINFO.preFlowState < 4){//没有办结
				if(index==0){
					$(modObj).find(".step1").html("节点："+stepname);
					$(modObj).find(".step2").html("办理人："+executorName);
					$(modObj).find(".step3").html("办理状态：正在办理中...");
					$(modObj).find(".step4").remove();
				}else{
					$(modObj).find(".step1").html("节点："+stepname);
					$(modObj).find(".step2").html("办理人："+executorName);
					$(modObj).find(".step3").html("办理时间："+endTime);
					$(modObj).find(".step4").html("审批意见："+spMsg);
				}
			}else{
				$(modObj).find(".step1").html("节点："+stepname);
				$(modObj).find(".step2").html("办理人："+executorName);
				$(modObj).find(".step3").html("办理时间："+endTime);
				$(modObj).find(".step4").html("审批意见："+spMsg);
			}
			$("#spHistorys").append($(modObj))
		})
	}
}
/**
 * 开始打印
 */
function printFlow(){
	 $("#printBody").jqprint({
    	debug: false, //如果是true则可以显示iframe查看效果（iframe默认高和宽都很小，可以再源码中调大），默认是false
        importCSS: true, //true表示引进原来的页面的css，默认是true。（如果是true，先会找$("link[media=print]")，若没有会去找$("link")中的css文件）
        printContainer: true, //表示如果原来选择的对象必须被纳入打印（注意：设置为false可能会打破你的CSS规则）。
        operaSupport: true//
    });
}
//绑定手机号
function bandMovePhone(){
	window.top.layer.open({
		  type: 2,
		  //title: [title, 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['550px', '350px'],
		  fix: false, //不固定
		  maxmin: false,
		  scrollbar:false,
		  content: ["/userInfo/updateUserMovePhonePage?sid="+sid+"&pageSource=022",'no'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
			  $(iframeWin.document).find("#addBtn").on("click",function(){
				  var result = iframeWin.updateUserMovePhone();
				  if(result && result.status=='y'){
					  var pageSource = result.pageSource;
					  if(pageSource && pageSource=='022'){//跳转到审批验证界面
						  $("#spBtnDiv").data("spCheckCfg").phoneStatus=null;//不需要在验证手机号是否存在
						  var url = "/workFlow/sendSpYzmPage?sid="+sid;
						  iframeWin.location.replace(url);
					  }
				  }
			  })
			  $(iframeWin.document).find("#cancleBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
			  $(iframeWin.document).find("#checkBtn").on("click",function(){//开始验证
				  var result = iframeWin.checkSpYzm();
				  if(result && result.status=='y'){
					  //默认验证码没有输入正确
					  $("#spBtnDiv").data("passYzm",{"status":"y","val":result.spYzm});
					  $("#submitFlowForm").trigger("click");
				  }
			  })
			  
			  
		  }
		});
}
//发送验证码
function sendYzmForSp(){
	window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ['550px', '350px'],
		  fix: false, //不固定
		  maxmin: false,
		  scrollbar:false,
		  content: ["/workFlow/sendSpYzmPage?sid="+sid,'no'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
			  $(iframeWin.document).find("#cancleBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
			  $(iframeWin.document).find("#checkBtn").on("click",function(){//开始验证
				  var result = iframeWin.checkSpYzm();
				  if(result && result.status=='y'){
					  //默认验证码没有输入正确
					  $("#spBtnDiv").data("passYzm",{"status":"y","val":result.spYzm});
					  
					  
					  formPlugin.saveFormData({
		            		parentEl: $(".form-view_js"),
		            		dataStatus:1,
		            		isDel: 0,
		            		module: "freeform",
		            		callback: function(data) {
		            			var status = data.status;
		            			if(status=='f'){
		            				showNotification(2,data.info);
		            			}else if(data.message){
		            			}else{
		            				var nextStepConfig = $("#contentBody").data("nextStepConfig");
		            				if(nextStepConfig && nextStepConfig.addTaskWay=='003'){
		            					$("#addBusTaskForSp").data("reloadState","1");
		            					$("#addBusTaskForSp").trigger("click");
		            				}else{
		            					openWindow.ReLoad();
		            					closeWin();
		            				}
		            			}
		            		}
		            	})
				  }
			  })
			  
			  
		  }
		});
}
//构建关联子流程信息
function constrSonRelateHtml(){
	var html='<div class="field field_js form-field-textarea field-hoz">'
		+'			<div class="pull-left" style="width"50%">'
		+'				<label class="widget-title">'
		+'					<span class="widget-title_js">关联流程</span>'
		+'				</label>'
		+'				<div class="widget-content">'
		+'					<div class="input-instead j_readOnly"><a href="javascript:void(0)" id="relateSonFlow">'+FLOWINFO.sonFlowName+'</a></div>'
		+'				</div>'
		+'			</div>'
		+'			<div class="pull-left" style="width"50%">'
		+'				<label class="widget-title" style="width:90px!important">'
		+'					<span class="widget-title_js">流水号</span>'
		+'				</label>'
		+'				<div class="widget-content" style="width:190px!important">'
		+'					<div class="input-instead j_readOnly">'+FLOWINFO.sonFlowSerialNum+'</div>'
		+'				</div>'
		+'			</div>'
		+'	</div>';
	$("#formpreview").append(html);
	
	$("#relateSonFlow").off("click").on("click",function(){
		var  url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+FLOWINFO.sonInstanceId
		window.location.replace(url);
	})
}
//转办
function updateSpInsAssign(instanceId,actInstanceId){
	userOne('null', 'null', null, sid,function(options){
		if(options && options.length>0){
			var userId = options[0].value
			var userName = options[0].text;
			if(userId == TEAMS.currentUser.userId){
				showNotification(2,"不能转办给自己！");
			}else{
				var url = "/workFlow/updateSpInsAssign";
				var params = {"sid":sid,
						"instanceId":instanceId,
						"newAssignerId":userId,
						"actInstanceId":actInstanceId};
				postUrl(url, params, function(data){
					if(data.status=='y'){
						//window.self.location.reload();
						openWindow.ReLoad();
						closeWin();
					}else{
						showNotification(2,data.info);
					}
				})
			}
		}
	});
}
//拾取
function pickSpIns(instanceId,actInstanceId){
	window.top.layer.confirm("确定要办理该审批任务?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		var url = "/workFlow/pickSpInstance";
		var params = {"sid":sid,
				"instanceId":instanceId,
				"actInstanceId":actInstanceId};
		postUrl(url, params, function(data){
			if(data.status=='y'){
				window.self.location.reload();
			}else{
				showNotification(2,data.info);
			}
		})
	})
}

function spflowNextStep(callback){
	var params={"instanceId":FLOWINFO.instanceId,"sid":sid};
	var url = "/workFlow/ajaxSpFlowNextStep";
	postUrl(url,params,function(data){
		if(data.status=='f'){
			showNotification(2,data.info);
		}else if(data.stepInfo.stepType == 'huiqianing'){
			showNotification(2,"会签完成后，才能继续提交！");
		}else{
			var stepInfo = data.stepInfo;
			//选择下一步骤
			SpFlowNextStepConf(stepInfo,function(nextStepConfig){
				callback(nextStepConfig,stepInfo);
			});
		}
	});
}

function SpFlowNextStepConf(stepInfo,callback){
	window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ['550px', '480px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/workFlow/spFlowNextStepPage?sid='+sid,'no'],//跳转到提交步骤配置界面
		  btn: ['确定','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var nextStepConfig = iframeWin.returnStepConfig();
			  if(nextStepConfig){
				  window.top.layer.close(index);
				  callback(nextStepConfig);
				  return true;
			  }
			  return false;
		  },success: function(layero,index){ 
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.initSpFlowNextStep(stepInfo,$(window.document),window);
		  }
	});
}

function spFlowPage(spState,callback) {//取得审批意见信息
	window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ['550px', '400px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/workFlow/spFlowIdeaPage?sid='+sid,'no'],//跳转到提交步骤配置界面
		  btn: ['确定','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var nextStepConfig = iframeWin.returnStepConfig();
			  if(nextStepConfig){
				  window.top.layer.close(index);
				  callback(nextStepConfig);
				  return true;
			  }
			  return false;
		  },success: function(layero,index){ 
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.initSpFlowIdea(spState,window.document,window);
		  }
	});
}
//查找数据信息
function getConditionVarVal(fieldid){
	var conditionVar;
	var easywincompon = $("body").find("[fieldid="+fieldid+"]");
	var compone = $(easywincompon).attr("easywincompon");
	if('Employee' == compone){
		var tempShowData = $(easywincompon).find(".tempShowData:eq(0)").data("tempShowData");
		if(tempShowData){
			conditionVar = tempShowData.content;
		}
	}else if('Department' == compone){
		var tempShowData = $(easywincompon).find(".tempShowData:eq(0)").data("tempShowData");
		if(tempShowData){
			conditionVar = tempShowData.content;
		}
	}else{
		if($(easywincompon).hasClass("fieldReadOnly")){
			conditionVar = $(easywincompon).data("oldContent");
		}else{
			conditionVar = $(easywincompon).val();
		}
	}
	return conditionVar;
}
/**
 * 下一步步骤配置信息验证以及保存数据
 * @returns
 */
function nextStepCheckAndSaveData(){
	//验证借款信息
	comPonData()&& formPlugin.submitPreCheck({
		parentEl: $(".form-view_js"),
		callback:function(msg){
			if(msg){
				return;
			}
    		formPlugin.saveFormData({
        		parentEl: $(".form-view_js"),
        		dataStatus:1,
        		isDel: 0,
        		module: "freeform",
        		callback: function(data) {
        			var status = data.status;
        			if(status=='f'){
        				showNotification(2,data.info);
        			}else if(data.message){
        			}else{
        				openWindow.ReLoad();
        				closeWin();
        			}
        		}
        	})
    	}
	})
}
