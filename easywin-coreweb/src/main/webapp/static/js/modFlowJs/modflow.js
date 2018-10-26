
//模块配置信息
var ModFlowBase = {
		initFlowInfo:function(){//模块数据添加界面，初始化配置信息
			if(EasyWin.modFlowConfig.pageOpt 
					&& EasyWin.modFlowConfig.pageOpt == 'add'){//是添加界面
				//自由流程设定步骤操作
				if(EasyWin.modFlowConfig.spFlag == '2'){
					ModFlowBase.constrFreeFlowInfoHtm();
				}
				//固定流程加载开始步骤以及下一步骤集合
				if(EasyWin.modFlowConfig.spFlag == '1' &&　EasyWin.modFlowConfig.busType !== '047'){
					ModFlowBase.loadFlowStartNextStep(function(data){
						$("#contentBody").data("stepData",data);
					});
				}
			}else if(EasyWin.modFlowConfig.pageOpt 
					&& EasyWin.modFlowConfig.pageOpt == 'update'){//是修改界面
				$("#modFlowConfigDiv").remove();
				//初始化按钮
				ModFlowBase.initOptBtn();
			}else{
				$("#modFlowConfigDiv").remove();
			}
		},initOptBtn:function(){//操作按钮
			//当前操作员
			var curUserId = EasyWin.userInfo.userId;
			//当前执行人
			var executor = EasyWin.modFlowConfig.executor;
			//步骤类型
			var stepType = EasyWin.modFlowConfig.stepType;
			//需要认领审批事项
			if(executor == -1){
				ModFlowBase.addPickBtn();
			}
			//审批同意按钮
			if(executor == curUserId && stepType != 'huiqianing'  ){//当前人员是审批人员
				ModFlowBase.addAgreeBtn(stepType);
				if(stepType == 'huiqian'){
					ModFlowBase.addHuiQianTip();
					$("body").find("#huiqianTip").trigger("click");
				}
			}else{
				$("#updateBtn").remove();
			}
			//添加驳回和自由回退按钮
			if(stepType != 'huiqian'  && executor == curUserId){//会签操作没有转办
				if(stepType != 'huiqianing'  ){
					//添加驳回
					ModFlowBase.addRejectBtn();
				}
				//添加更多
				ModFlowBase.addMoreBtn();
			}
		},addPickBtn:function(){//添加转办按钮
			var pickBtn = $('<a href="javascript:void(0)" class="purple padding-right-10 padding-5" id="pickSpIns"></a>')
			$(pickBtn).append('<i class="glyphicon glyphicon-retweet"></i>拾取');
			$("#spBtnDiv").append(pickBtn);
			
			$(pickBtn).on("click",function(){
				ModFlowBase.optForm.pickSpIns(EasyWin.modFlowConfig.busId,
						EasyWin.modFlowConfig.busType,
						EasyWin.modFlowConfig.actInstaceId);
			})
		},addHuiQianTip:function(){//添加会签说明
			var huiqianTipBtn = $('<a href="javascript:void(0)" class="blue padding-right-10 padding-5" id="huiqianTip"></a>')
			$(huiqianTipBtn).append('<i class="glyphicon glyphicon-info-sign"></i>会签说明');
			$("#spBtnDiv").append(huiqianTipBtn);
			$(huiqianTipBtn).on("click",function(){
				var content = EasyWin.modFlowConfig.content;
				if(!content){
					content = "请会签";
				}
				window.top.layer.confirm(content,{
					title:"会签说明" + "",
					icon:7,
					btn:["确定"]
				},function(index){
					window.top.layer.close(index)
				})
			});
		},addAgreeBtn:function(stepType){//添加同意按钮
			var agreeBtn = $('<a href="javascript:void(0)" class="green padding-right-10 padding-5" id="spFormAgree"></a>')
			if(stepType=='huiqian'){
				$(agreeBtn).append('<i class="fa fa-check-square-o"></i>办理');
			}else{
				$(agreeBtn).append('<i class="fa fa-check-square-o"></i>下一步');
			}
			$("#spBtnDiv").append(agreeBtn);
			
			$(agreeBtn).on("click",function(){
				ModFlowBase.optForm.modFlowNextStep(function(nextStepConfig,stepInfo){
					$("#contentBody").data("nextStepConfig",nextStepConfig);
	            	$("#contentBody").data("stepInfo",stepInfo);
	            	if(EasyWin.modFlowConfig.busType =='046'
	            			&& EasyWin.updateSubmit && EasyWin.updateSubmit==1){
	            		addMeet(1);
	            	}else if(EasyWin.modFlowConfig.busType =='047'
	            			&& EasyWin.updateSubmit && EasyWin.updateSubmit==1){
	            		updateMeetSummary();
	            	}else{
	            		ModFlowBase.optForm.submitFlowForm(1);
	            	}
	            	
				});
			})
		},addRejectBtn:function(){//添加终止按钮
			var rejectBtn = $('<a href="javascript:void(0)" class="red padding-right-10 padding-5" id="spFormReject"></a>')
			$(rejectBtn).append('<i class="fa fa-circle-o"></i>终止');
			$("#spBtnDiv").append(rejectBtn);
			$(rejectBtn).on("click",function(){
				ModFlowBase.optForm.spFlowPage(0,function(nextStepConfig){
					$("#contentBody").data("nextStepConfig",nextStepConfig);
	            	ModFlowBase.optForm.submitFlowForm(0);
				});
			})
		},addMoreBtn:function(){//添加更多
			var moreBtn = $('<a class="green ps-point padding-right-10 padding-5" data-toggle="dropdown" title="更多操作"></a>');
			$(moreBtn).append('<i class="fa fa-th"></i>更多');
			$("#spBtnDiv").append(moreBtn);
			
			var moreUl = $('<ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl"></ul>');
			//当前操作员
			var curUserId = EasyWin.userInfo.userId;
			//当前执行人
			var executor = EasyWin.modFlowConfig.executor;
			//步骤类型
			var stepType = EasyWin.modFlowConfig.stepType;
			
			//流程办理人员可以转办事项
			if(stepType != 'huiqian' && stepType != 'huiqianing'  
				&& executor == curUserId){//会签操作没有转办
				var jumpLi = $('<li></li>');
				var jumpBtn = $('<a href="javascript:void(0)" id="spFormBack"></a>');
				$(jumpBtn).append('<i class="fa fa-mail-reply"></i>自由回退');
				$(jumpLi).append(jumpBtn);
				$(moreUl).append(jumpLi);
				
				$(jumpBtn).on("click",function(){
					ModFlowBase.optForm.spFlowPage(-1,function(nextStepConfig){
						$("#contentBody").data("nextStepConfig",nextStepConfig);
		            	ModFlowBase.optForm.submitBackForm(EasyWin.modFlowConfig.busId,
		            			EasyWin.modFlowConfig.busType,
		            			EasyWin.modFlowConfig.actInstaceId);
					});
				});
				
				
				//添加转办
				var turnAssignLi = $('<li></li>');
				var turnAssignBtn = $('<a href="javascript:void(0)" id="updateSpInsAssign"></a>');
				$(turnAssignBtn).append('<i class="glyphicon glyphicon-retweet"></i>转办');
				
				$(turnAssignLi).append(turnAssignBtn);
				$(moreUl).append(turnAssignLi);
				
				$(turnAssignBtn).on("click",function(){
					ModFlowBase.optForm.updateSpInsAssign();
				});
			}
			
			$("#spBtnDiv").append(moreUl);
			
		},constrAddUrl:function(flowType,callback){//模块审批添加 的url
			if(flowType == 'modflow'){
				var conf = {
						"spFlag":"1"	
				}
				ModFlowBase.spFlowForSelect(function(data){
					if(data.status=='y'){
						var list = data.list;
						 if(list && list.length==1){
							 var spFlowModel = list[0];
							 conf.flowId = spFlowModel.id;
							 conf.flowType="modFlow"
							 callback(conf)
						 }else if(list && list.length>1){
							 ModFlowBase.selectShowSpFlow(list,function(spFlowModel){
								 conf.flowId = spFlowModel.id;
								 conf.flowType="modFlow"
								 callback(conf);
							  });
						 }
					}else if(data.status == 'n'){
						window.top.layer.confirm("未设定流程,确认自由流程？",function(index){
							window.top.layer.close(index);
							conf = {
									"spFlag":"2",
									"flowType":"freeflow"
							}
							callback(conf);
						})
					}else{
						showNotification(2,data.info);
					}
				})
			}else if(flowType=='freeflow'){
				var conf = {
						"spFlag":"2",
						"flowType":"freeflow"
				}
				callback(conf);
			}else if(flowType=='normal'){
				var conf = {
					"spFlag":"0",
					"flowType":"normal"
				}
				callback(conf);
			}
		},selectShowSpFlow:function(list,callback) {//审批模块的流程选择
			window.top.layer.open({
				 type: 2,
				  title:false,
				  closeBtn:0,
				  area: ['600px', '500px'],
				  fix: true, //不固定
				  maxmin: false,
				  move: false,
				  scrollbar:false,
				  content: ['/modFlow/listModFlowForSelect?sid='+EasyWin.sid,'no'],
				  btn: ['选择', '取消'],
				  yes: function(index, layero){
					  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					  var spFlowModel = iframeWin.flowSelected();
					  if(spFlowModel){
						  window.top.layer.close(index);
						  callback(spFlowModel)
					  }
				  },success:function(layero,index){
					  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					  iframeWin.initData(list);
					  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
						  window.top.layer.close(index);
					  })
					  $(iframeWin.document).find("#spFlowDataList tr").on("dblclick",function(){
						  var spFlowModel = iframeWin.flowSelectedTr($(this));
						  if(spFlowModel){
							  window.top.layer.close(index);
							  callback(spFlowModel)
						  }
					  })
					  
				  }
			});
		},checkModFlowConfigForStart:function() {//验证审批流程信息
			//判断是否需要验证,默认通过
			var flag = 1;
			if(EasyWin && EasyWin.modFlowConfig 
					&& EasyWin.modFlowConfig.spFlag 
					&& EasyWin.modFlowConfig.spFlag != '0'){//是关联的模块
				var modFlowConfig = EasyWin.modFlowConfig;
				if(modFlowConfig.spFlag == '1'){//验证自由流程
					var stepData = $("#contentBody").data("stepData");
					if(stepData.status == 'y'){
						var nextStepConfig = $("#contentBody").data("nextStepConfig")
						if(!nextStepConfig){
							ModFlowBase.optForm.ModFlowNextStepSelect(stepData.stepInfo,function(nextStepConfig){
								$("#contentBody").data("nextStepConfig",nextStepConfig)
								$(".subform").submit();
							});
							flag = 0;
						}
					}else{
						flag = 0;
						showNotification(2,stepData.info);
					}
				}else if(modFlowConfig.spFlag == '2'){//验证自由流程
					var trs = $("#spStepTable").find("tbody tr");
					if(trs){
						$.each(trs,function(index,tr){
							var spUserIdInput = $(tr).find("td:eq(1)").find("input");
							var spUserId = $(spUserIdInput).val();
							if(!spUserId){
								flag = 0;
								scrollToLocation($("body"),$(spUserIdInput).parent());
								layer.tips("请选择审批人员！",$(spUserIdInput).parent(),{"tips":1});
								return false;
							} 
						})
					}
					
				}
			}
			return flag;
		},checkModFlowConfigForUpdate:function() {//验证步骤信息用于流程扭转
			//判断是否需要验证,默认通过
			var flag = 1;
			if(EasyWin && EasyWin.modFlowConfig 
					&& EasyWin.modFlowConfig.spFlag 
					&& EasyWin.modFlowConfig.spFlag != '0'){//是关联的模块
				var modFlowConfig = EasyWin.modFlowConfig;
				if(modFlowConfig.spFlag == '1'){//验证固定流程
					var nextStepConfig = $("#contentBody").data("nextStepConfig")
					if(!nextStepConfig){
						//选择下一步骤
						ModFlowBase.optForm.modFlowNextStep(function(nextStepConfig,stepInfo){
							$("#contentBody").data("nextStepConfig",nextStepConfig);
			            	$("#contentBody").data("stepInfo",stepInfo);
							$(".subform").submit();
						});
						flag = 0;
					}
				}else if(modFlowConfig.spFlag == '2'){//验证自由流程
					var trs = $("#spStepTable").find("tbody tr");
					if(trs){
						$.each(trs,function(index,tr){
							var spUserIdInput = $(tr).find("td:eq(1)").find("input");
							var spUserId = $(spUserIdInput).val();
							if(!spUserId){
								flag = 0;
								scrollToLocation($("body"),$(spUserIdInput).parent());
								layer.tips("请选择审批人员！",$(spUserIdInput).parent(),{"tips":1});
								return false;
							} 
						})
					}
					
				}
			}
			return flag;
		},constrAddConfData:function(target) {//审批流程添加时，构架数据
			var modFlowConf = {};
			if(EasyWin && EasyWin.modFlowConfig 
					&& EasyWin.modFlowConfig.spFlag 
					&& EasyWin.modFlowConfig.spFlag != '0'){//是关联的模块
				var modFlowConfig = EasyWin.modFlowConfig;
				modFlowConf.relateSpMod = "true";
				modFlowConf.flowType = modFlowConfig.spFlag;
				if(modFlowConfig.spFlag == '1'){//固定流程
					modFlowConf.spFlowModId = modFlowConfig.flowId;
					var nextStepConfig = $("#contentBody").data("nextStepConfig");
					//下一步是否为结束
					var stepType = nextStepConfig.stepType;
					var modFormStepData = {};
					
					//审批附件信息
		        	var spFlowUpfiles = nextStepConfig.spFlowUpfiles;
		            if (spFlowUpfiles) {
		            	modFormStepData.spFlowUpfiles = spFlowUpfiles;
		            }
					//审批意见
		            var spIdea = nextStepConfig.spIdea;
		            if(spIdea){
		            	modFormStepData.spIdea = spIdea;
		            }
					
					modFormStepData.noticeUsers = nextStepConfig.noticeUsers;
					
					if(stepType && stepType=='end'){//是结束步骤
						modFormStepData.nextStepId = nextStepConfig.stepId;
					}else{
						modFormStepData.nextStepId = nextStepConfig.stepId;
						modFormStepData.nextStepUsers = nextStepConfig.nextStepUsers;
						modFormStepData.msgSendFlag = nextStepConfig.msgSendFlag;
						modFormStepData.multLoopState = nextStepConfig.multLoopState;
					}
					modFormStepData.spState = "1";
					modFlowConf.modFormStepData = modFormStepData;
				}else{//自由流程
					var trs = $("#spStepTable").find("tbody tr");
					var spUsers = new Array();
					$.each(trs,function(index,tr){
						var spUserId = $(tr).find("td:eq(1)").find("input").val();
						var spUserName = $.trim($(tr).find("td:eq(1)").find(".spUserImg").html());
						var spUser = {
								"spUserId":spUserId,
								"spUserName":spUserName
						}
						spUsers.push(spUser);
					})
					modFlowConf.spUsers = spUsers;
				}
				
			}else{
				modFlowConf.relateSpMod = "false";
				modFlowConf.flowType = "0";
			}
			var modFlowConfStr = JSON.stringify(modFlowConf);
			
			$(target).find("#modFlowConfDiv").remove();
			
			var div = $("<div></div>");
			$(div).attr("id","modFlowConfDiv");
			$(div).css("display","none");
			var textArea = $("<textarea></textarea>");
			$(textArea).attr("name","modFlowConfStr");
			$(textArea).html(modFlowConfStr);
			$(div).append($(textArea));
			
			$(target).append($(div));
			
		},spFlowForSummarySelect:function() {//会议纪要送审配置下一步骤
			//选择需要走的流程
			ModFlowBase.constrAddUrl('modflow',function(conf){
				//采用的流程
				EasyWin.modFlowConfig.flowId = conf.flowId;
				//加载开始步骤的下一步骤
				ModFlowBase.loadFlowStartNextStep(function(data){
					$("#contentBody").data("stepData",data);
					ModFlowBase.checkModFlowConfigForStart();
				});
			})
		},spFlowForSelect:function(callback) {//加载步骤信息
			var url = "/modFlow/listModSpFlowByAjax" ;
			var params = {
					"sid":EasyWin.sid,
					"busType":EasyWin.modFlowConfig.busType
			}
			postUrl(url,params,function(data){
				 //若是有回调
				  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
					  callback(data);
				  }
			})
		},loadFlowStartNextStep:function(callback){//加载流程下步骤
			var url = "/modFlow/listModFlowStartNextStep";
			var params = {
					"sid":EasyWin.sid,
					"flowId":EasyWin.modFlowConfig.flowId
			}
			postUrl(url,params,function(data){
				callback(data);
			})
		}
}

ModFlowBase.optForm = {
		submitFlowForm:function(spState){
			if(spState == 1){//下一步
				ModFlowBase.optForm.submitAgreeForm(EasyWin.modFlowConfig.busId,EasyWin.modFlowConfig.busType,EasyWin.modFlowConfig.actInstaceId);
			}else if(spState == 0){//撤回或不同意	
				ModFlowBase.optForm.submitRejectForm(EasyWin.modFlowConfig.busId,EasyWin.modFlowConfig.busType,EasyWin.modFlowConfig.actInstaceId);
			}
		},submitAgreeForm:function(busId,busType,actInstaceId){
			var url = "/modFlow/updateSpModFlow";
			var params = {"sid":EasyWin.sid,
					"busId":busId,
					"busType":busType,
					"actInstaceId":actInstaceId};
			
			var nextStepConfig = $("#contentBody").data("nextStepConfig");
			var stepInfo = $("#contentBody").data("stepInfo");
			var modFormStepData = {};
			
			//审批附件信息
        	var spFlowUpfiles = nextStepConfig.spFlowUpfiles;
            if (spFlowUpfiles) {
            	modFormStepData.spFlowUpfiles = spFlowUpfiles;
            }
			//审批意见
            var spIdea = nextStepConfig.spIdea;
            if(spIdea){
            	modFormStepData.spIdea = spIdea;
            }
            modFormStepData.spState = "1";
			//步骤信息
			if(stepInfo.stepType 
					&& (stepInfo.stepType == 'huiqian'
						|| stepInfo.stepType =='multExecute')){//当前步骤为会签，则直接提交
				modFormStepData.spState = "1";
				modFormStepData.nextStepId = "0";
				params.modFormStepDataStr = JSON.stringify(modFormStepData);
				
			}else{
				//下一步是否为结束
				var stepType = nextStepConfig.stepType;
				
				modFormStepData.noticeUsers = nextStepConfig.noticeUsers;
				
				if(stepType && stepType=='end'){//是结束步骤
					modFormStepData.nextStepId = nextStepConfig.stepId;
				}else{
					modFormStepData.nextStepId = nextStepConfig.stepId;
					modFormStepData.nextStepUsers = nextStepConfig.nextStepUsers;
					modFormStepData.msgSendFlag = nextStepConfig.msgSendFlag;
					modFormStepData.multLoopState = nextStepConfig.multLoopState;
				}
				modFormStepData.spState = "1";
				params.modFormStepDataStr = JSON.stringify(modFormStepData);
			}
			
			postUrl(url, params, function(data){
				if(data.status=='y'){
					window.self.location.reload();
				}else{
					showNotification(2,data.info);
				}
			})
		},submitRejectForm:function(busId,busType,actInstaceId){
			var url = "/modFlow/updateSpModFlow";
			var params = {"sid":EasyWin.sid,
					"busId":busId,
					"busType":busType,
					"actInstaceId":actInstaceId};
			
			var nextStepConfig = $("#contentBody").data("nextStepConfig");
			var stepInfo = $("#contentBody").data("stepInfo");
			
			var modFormStepData = {};
			
			//审批附件信息
        	var spFlowUpfiles = nextStepConfig.spFlowUpfiles;
            if (spFlowUpfiles) {
            	modFormStepData.spFlowUpfiles = spFlowUpfiles;
            }
			//审批意见
            var spIdea = nextStepConfig.spIdea;
            if(spIdea){
            	modFormStepData.spIdea = spIdea;
            }
			
			modFormStepData.spState = "0";
			params.modFormStepDataStr = JSON.stringify(modFormStepData);
			postUrl(url, params, function(data){
				if(data.status=='y'){
					window.self.location.reload();
				}else{
					showNotification(2,data.info);
				}
			})
		},submitBackForm:function(busId,busType,actInstaceId){//事项回退
			var url = "/modFlow/updateSpInsBack";
			var params = {"sid":EasyWin.sid,
					"busId":busId,
					"busType":busType,
					"actInstaceId":actInstaceId};
			var modFormStepData = {};
			var nextStepConfig = $("#contentBody").data("nextStepConfig");
			modFormStepData.spIdea = nextStepConfig.spIdea;
			modFormStepData.activitiTaskId = nextStepConfig.activitiTaskId;
			//审批附件信息
        	var spFlowUpfiles = nextStepConfig.spFlowUpfiles;
            if (spFlowUpfiles) {
            	modFormStepData.spFlowUpfiles = spFlowUpfiles;
            }
			params.modFormStepDataStr = JSON.stringify(modFormStepData);
			postUrl(url, params, function(data){
				if(data.status=='y'){
					window.self.location.reload();
				}else{
					showNotification(2,data.info);
				}
			})
		},pickSpIns:function(busId,busType,actInstaceId){////事项拾取
			window.top.layer.confirm("确定要办理该审批事项?", {icon: 3, title:'确认对话框'}, function(index){
				window.top.layer.close(index);
				var url = "/modFlow/pickSpFlowTask";
				var params = {"sid":EasyWin.sid,
						"busId":busId,
						"busType":busType,
						"actInstaceId":actInstaceId};
				postUrl(url, params, function(data){
					if(data.status=='y'){
						window.self.location.reload();
					}else{
						showNotification(2,data.info);
					}
				})
			})
		},modFlowNextStep:function(callback){
			var params={"busId":EasyWin.modFlowConfig.busId,
					"busType":EasyWin.modFlowConfig.busType,
					"actInstaceId":EasyWin.modFlowConfig.actInstaceId,
    				"sid":EasyWin.sid};
    		var url = "/modFlow/ajaxSpFlowNextStep";
    		postUrl(url,params,function(data){
    			if(data.status=='f'){
    				showNotification(2,data.info);
    			}else if(data.stepInfo.stepType == 'huiqianing'){
	    			showNotification(2,"会签完成后，才能继续提交！");
    			}else{
    				var stepInfo = data.stepInfo;
    				
    				//步骤信息
	    			var stepInfo = data.stepInfo;
	    			if(stepInfo.stepType 
	    					&& (stepInfo.stepType == 'huiqian'
	    						|| stepInfo.stepType =='multExecute')){//当前步骤为会签，则直接提交
	    				callback(null,stepInfo)
	    			}else{
	    				//选择下一步骤
	    				ModFlowBase.optForm.ModFlowNextStepSelect(stepInfo,function(nextStepConfig){
	    					callback(nextStepConfig,stepInfo)
	    				});
	    			}
    			}
    		})
		},ModFlowNextStepSelect:function(stepInfo,callback){
			window.top.layer.open({
				  type: 2,
				  title:false,
				  closeBtn:0,
				  area: ['550px', '400px'],
				  fix: true, //不固定
				  maxmin: false,
				  move: false,
				  scrollbar:false,
				  content: ['/modFlow/spFlowNextStepPage?sid='+EasyWin.sid,'no'],//跳转到提交步骤配置界面
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
		 },spFlowPage: function (spState,callback) {//取得审批意见信息
		    	window.top.layer.open({
					  type: 2,
					  title:false,
					  closeBtn:0,
					  area: ['550px', '400px'],
					  fix: true, //不固定
					  maxmin: false,
					  move: false,
					  scrollbar:false,
					  content: ['/modFlow/spFlowIdeaPage?sid='+EasyWin.sid,'no'],//跳转到提交步骤配置界面
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
		},updateSpInsAssign:function(){//转办人员选择界面
			userOne('null', 'null', null, EasyWin.sid, function (options) {
		        if (options && options.length > 0) {
		            var userId = options[0].value
		            var userName = options[0].text;
		            if (userId == EasyWin.userInfo.userId) {
		                showNotification(2, "不能转办给自己！");
		            } else {
		            	var url = "/modFlow/updateSpInsAssign";
						var params={"busId":EasyWin.modFlowConfig.busId,
								"busType":EasyWin.modFlowConfig.busType,
								"actInstaceId":EasyWin.modFlowConfig.actInstaceId,
			    				"sid":EasyWin.sid};
						var modFormStepData = {};
						
						var newAssigner = {
								"id":userId,
								"userName":userName
						}
						
						modFormStepData.newAssigner = newAssigner;
						params.modFormStepDataStr = JSON.stringify(modFormStepData);
		                postUrl(url, params, function (data) {
		                    if (data.status == 'y') {
		                        window.self.location.reload();
		                    } else {
		                        showNotification(2, data.info);
		                    }
		                })
		            }
		        }
		    });
		},SpInsAssignSelect:function(callback){//转办人员选择界面
			var url = '/modFlow/spInsUserSetPage?sid=' + EasyWin.sid;
			url = url+"&busId="+EasyWin.modFlowConfig.busId;
			url = url+"&busType="+EasyWin.modFlowConfig.busType;
			url = url+"&actInstaceId="+EasyWin.modFlowConfig.actInstaceId;
			window.top.layer.open({
		  		  type: 2,
		  		  title:false,
		  		  closeBtn:0,
		  		  area: ['550px', '450px'],
		  		  fix: true, //不固定
		  		  maxmin: false,
		  		  move: false,
		  		  content: [url,'no'],
		  		  btn: ['确定','关闭'],
		  		  yes: function(index, layero){
		  			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		  			var assignConf = iframeWin.returnAssignConf();
		  			  
		  			if(assignConf){//有会签人员
		  				 callback(assignConf);
		  				 window.top.layer.close(index)
		  			}
		  		 },success:function(layero,index){
		  			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					 $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
						 window.top.layer.close(index);
					 })
		  		  }
		  		});
		},jointProcessUser:function(joinConf){
			var url = "/modFlow/updateModJoinProcess";
			var params={"busId":EasyWin.modFlowConfig.busId,
					"busType":EasyWin.modFlowConfig.busType,
					"actInstaceId":EasyWin.modFlowConfig.actInstaceId,
    				"sid":EasyWin.sid};
			var modFormStepData = {};
			modFormStepData.jointProcessUsers = joinConf.jointProcessUsers;
			modFormStepData.msgSendFlag = joinConf.msgSendFlag;
			modFormStepData.content = joinConf.content;
			modFormStepData.cover = joinConf.cover;
			params.modFormStepDataStr = JSON.stringify(modFormStepData);
			postUrl(url, params, function(data){
				if(data.status=='y'){
					window.self.location.reload();
				}else{
					showNotification(2,data.info);
				}
			})
		},jointProcessUserSelect:function(callback){
			var url = '/modFlow/jointProcessUserSetPage?sid=' + EasyWin.sid;
			url = url+"&busId="+EasyWin.modFlowConfig.busId;
			url = url+"&busType="+EasyWin.modFlowConfig.busType;
			url = url+"&actInstaceId="+EasyWin.modFlowConfig.actInstaceId;
			
			window.top.layer.open({
		  		  type: 2,
		  		  title:false,
		  		  closeBtn:0,
		  		  area: ['750px', '530px'],
		  		  fix: true, //不固定
		  		  maxmin: false,
		  		  move: false,
		  		  content: [url,'no'],
		  		  btn: ['确定','关闭'],
		  		  yes: function(index, layero){
		  			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		  			  var joinConf = iframeWin.returnJoinConf();
		  			  
		  			  if(joinConf){//有会签人员
		  				  callback(joinConf);
		  				  window.top.layer.close(index)
		  			  }
		  		  },success:function(layero,index){
		  			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					 $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
						 window.top.layer.close(index);
					 })
		  		  }
		  		});
		}
}

$(function(){
	if(EasyWin && EasyWin.modFlowConfig 
		&& EasyWin.modFlowConfig.spFlag 
		&& EasyWin.modFlowConfig.spFlag != '0'){//是关联的模块
			ModFlowBase.initFlowInfo();
	}
})

