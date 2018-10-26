$(function () {
    var loading = $('<div id="loading" class="layui-layer-load" style="width: 100%;height: 50px"></div>');
    $("#form-info-div").after($(loading));
    EasyWinForm.renderFormDataByLayout({
            sid: sid,
            instanceId: FLOWINFO.instanceId,
            layoutId: FLOWINFO.layoutId,
            callback: function (form) {
                $("#form-info-div").data("form", form);
                $(loading).remove();
                var sonInstanceId = FLOWINFO.sonInstanceId;
                if (sonInstanceId) {
                    var sonFlow = {}
                    sonFlow.sonFlowName = FLOWINFO.sonFlowName;
                    sonFlow.sonFlowSerialNum = FLOWINFO.sonFlowSerialNum;
                    sonFlow.sonInstanceId = FLOWINFO.sonInstanceId;
                    EasyWinForm.constrSonRelate(sonFlow);
                }
            }
        }
    );

    $("#form-info-div").data("flowInfo", FLOWINFO);
    $("#form-info-div").data("spFlow", EasyWin.spFlow);

    //查询借款信息
    EasyWinForm.initLoanInfo({
        saveType: FLOWINFO.saveType,
        busId: EasyWin.spFlow.busId,
        busType: EasyWin.spFlow.busType,
        loanSpInsId: FLOWINFO.instanceId,
        preFlowState: FLOWINFO.preFlowState,

    });
    $("body").on("click", "#addFormData", function () {//发起流程
        var e = EasyWinForm.checkForAdd({
            flowId: FLOWINFO.flowId,
            dataStatus: 1
        })
        
        if(e){
        	$("#contentBody").removeData("nextStepConfig");
        	$("#contentBody").removeData("stepInfo");
        	
        	EasyWinForm.spflowNextStep(function(nextStepConfig,stepInfo){
        		
        		$("#contentBody").data("nextStepConfig",nextStepConfig);
            	$("#contentBody").data("stepInfo",stepInfo);
            	//开始必填验证
            	EasyWinForm.saveFormData({
            		parentEl: $("#contentBody"),
            		dataStatus: 1,
            		saveType: "add",
            		isDel: 0,
            		module: "freeform",
            		callback: function (data) {
            			var status = data.status;
            			if (status == 'f') {
            				showNotification(2, data.info);
            			} else if (data.message) {
            			} else {
            				openWindow.ReLoad();
            				closeWin();
            			}
            		}
            	})
        	});
        }
    })


    //保存流程
    $("body").on("click", "#saveFormData", function () {
        var e = EasyWinForm.checkForAdd({
            flowId: FLOWINFO.flowId,
            dataStatus: 0
        });
        e ? (
            //开始必填验证
            EasyWinForm.saveFormData({
                parentEl: $("#contentBody"),
                dataStatus: 0,
                saveType: "add",
                isDel: 0,
                module: "freeform",
                callback: function (data) {
                    var status = data.status;
                    if (status == 'f') {
                        showNotification(2, data.info);
                    } else if (data.message) {
                    } else {
                        openWindow.ReLoad();
                        closeWin();
                    }
                }
            })
        ) : "";

    })

    

    //审批转办
    $("body").on("click", "#updateSpInsAssignV2", function () {

        var divp = $("body").find("#contentBody").find("#sp-container");
        var spResult = $(".form-view_js").data("spResult");
        var spState;
        if (spResult) {
            spState = spResult.spState;
        }

        //隐藏步骤回退
        $("#newAssignDiv").show();
        $("#backStepSelect").hide();
        if (spState == 2) {//上次点击的是转办
            $(divp).toggleClass("hide");
            $("#spIdeaText").val('');
            $(".form-view_js").removeData("spResult");

            $("#updateSpInsAssign").attr("class", "purple");
        } else {
            $(divp).removeClass("hide");
            $("#spIdeaText").val('');
            $("#spBtnDiv").data("spResult", {"spState": 2});

            $("#spFormAgree").attr("class", "green");
            $("#spFormReject").attr("class", "red");
            $("#spFormBack").attr("class", "blue");
            $("#updateSpInsAssign").attr("class", "white bg-purple");
        }
        var height = $("#contentBody").find("#form-info-div").offset();
        $("#contentBody").animate({
            scrollTop: height
        }, 500, function () {
            resizeVoteH(EasyWin.ifreamName)
        });

    });
    //审批转办
    $("body").on("click", "#updateSpInsAssign", function () {
        EasyWinForm.updateSpInsAssign(FLOWINFO.instanceId, FLOWINFO.actInstaceId);

    });
  //审批关联任务
	$("body").on("click","#addBusTaskForSp",function(){
		var  reloadState = $("#addBusTaskForSp").data("reloadState");
		EasyWinForm.optForm.constrContent(function(content,fileLists){
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
    $("body").on("click", "#pickSpIns", function () {
        EasyWinForm.pickSpIns(FLOWINFO.instanceId, FLOWINFO.actInstaceId);
    });

    // 审批表单(下一步)
    $("body").on("click", "#spFormAgree", function () {
    	$("#spBtnDiv").data("spResult", {"spState": 1});
        var flag = EasyWinForm.submitCheck($("#contentBody"), 0, function () {})
        if (flag) {
        	$("#contentBody").removeData("nextStepConfig");
        	$("#contentBody").removeData("stepInfo");
        	
        	$("#form-info-div").data("flowInfo").saveType = "update";//提交类型变更
        	EasyWinForm.spflowNextStep(function(nextStepConfig,stepInfo){
        		
        		$("#contentBody").data("nextStepConfig",nextStepConfig);
            	$("#contentBody").data("stepInfo",stepInfo);
        		//开始必填验证
            	EasyWinForm.saveFormData({
            		parentEl: $("#contentBody"),
            		dataStatus: 1,
            		isDel: 0,
            		module: "freeform",
            		callback: function (data) {
            			var status = data.status;
            			if (status == 'f') {
            				showNotification(2, data.info);
            			} else {
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
        	})
        	
        }

    });
    // 审批表单(终止)
    $("body").on("click", "#spFormReject", function () {
    	$("#spBtnDiv").data("spResult", {"spState": 0});
    	$("#form-info-div").data("flowInfo").saveType = "update";//提交类型变更
    	$("#contentBody").removeData("nextStepConfig");
    	$("#contentBody").removeData("stepInfo");

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
					goToSpFLowPage(0);//流程审核
				});
			}else{
				goToSpFLowPage(0);//流程审核 
			}
		});
    });
	//审批会签
	$("body").on("click","#updateSpHuiQian",function(){
		$("#form-info-div").data("flowInfo").saveType="huiqian";//提交类型变更
    	$("#form-info-div").data("flowInfo").activitiTaskId="";
    	var url = '/workFlow/spHuiQian?sid=' + EasyWin.sid;
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
		    				"sid":EasyWin.sid};
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
    	var url = '/workFlow/subSpHuiQianPage?sid=' + EasyWin.sid;
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
		    				"sid":EasyWin.sid};
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
//							window.location.reload();
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
    $("body").on("click", "#spFormBack", function () {
    	$("#form-info-div").data("flowInfo").saveType = "back";//提交类型变更
    	$("#spBtnDiv").data("spResult", {"spState": 0});
    	$("#contentBody").removeData("nextStepConfig");
    	$("#contentBody").removeData("stepInfo");
    	
    	var params={"instanceId":FLOWINFO.instanceId,"sid":sid};
		var url = "/workFlow/ajaxSpFlowNextStep?rnd="+Math.random();
    	postUrl(url,params,function(data){
			if(data.status=='f'){
				showNotification(2,data.info);
			}else if(data.stepInfo.stepType == 'huiqianing'){
    			window.top.layer.confirm('当前流程会签未完成，是否撤销并继续回退？', {icon: 3, title:'确认对话框',
				  btn: ['撤销并回退','等待会签'] //按钮
				}, function(index){
					window.top.layer.close(index);//关闭确认对话框
					goToSpFLowPage(-1);//流程审核
				});
			}else{
				goToSpFLowPage(-1);//流程审核 
			}
		});
    });

    //验证手机号是否存在，并验证该步骤是否需要发送验证码
    if (!strIsNull(FLOWINFO.flowId) && !strIsNull(FLOWINFO.actInstaceId) && FLOWINFO.preFlowState != 2) {
        //默认需要验证码
        $("#spBtnDiv").data("spCheckCfg", {"status": "f", "info": "正在验证后台数据..."});

        //默认验证码没有输入正确
        $("#spBtnDiv").data("passYzm", {"status": "f", "val": ""});

        var params = {
            "instanceId": FLOWINFO.instanceId,
            "actInstaceId": FLOWINFO.actInstaceId,
            "sid": sid
        }
        getSelfJSON("/workFlow/checkStepCfg", params, function (data) {
            var status = data.status;

            if ($.inArray("f", status) >= 0) {//后台有错误
                $("#spBtnDiv").data("spCheckCfg", {"status": "f", "info": data.info});
            } else if ($.inArray("y", status) >= 0) {//不要验证码
                $("#spBtnDiv").data("spCheckCfg", {"status": "y"});
            } else {
                var statusObj = {};
                if ($.inArray("f1", status) >= 0) {
                    statusObj.status = "f1";
                    statusObj.movePhone = data.movePhone;
                }
                if ($.inArray("f2", status) >= 0) {
                    statusObj.phoneStatus = "f2";
                }
                $("#spBtnDiv").data("spCheckCfg", statusObj);
            }
        });
    } else {
        //默认不需要验证码
        $("#spBtnDiv").data("spCheckCfg", {"status": "y"});
    }
    //打印
    $("body").on("click", "#printBtn", function () {
        $("#printBody").jqprint({
            debug: false, //如果是true则可以显示iframe查看效果（iframe默认高和宽都很小，可以再源码中调大），默认是false
            importCSS: true, //true表示引进原来的页面的css，默认是true。（如果是true，先会找$("link[media=print]")，若没有会去找$("link")中的css文件）
            printContainer: true, //表示如果原来选择的对象必须被纳入打印（注意：设置为false可能会打破你的CSS规则）。
            operaSupport: true,//
            isReplace: true//
        });


    })

    //借款申请弹窗查看
    $("body").on("click", ".loanApply2View", function () {
        var instanceId = $("#formInfo").data("loanApply").instanceId;
        if(instanceId!= FLOWINFO.instanceId){
        	var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + instanceId;
        	openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
        }
    });
    //借款申请弹窗查看
    $("body").on("click", ".loanRep2View", function () {
    	var instanceId = $("#formInfo").data("loanOff").loanRepInsId;
    	//直接跳转
		if(instanceId!= FLOWINFO.instanceId){
			var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + instanceId;
			openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
		}
    });
    //累计借款信息
    $("body").on("click",".loanAll",function(){//累计报销信息
    	var listLoan = $(this).data("listLoan");
    	if(listLoan && listLoan[0]){
    		var listLoanArray = new Array();
    		var curLoan;
    		$.each(listLoan,function(index,loanObj){
				if(loanObj.status==4){
					listLoanArray.push(loanObj);
					curLoan = loanObj;
				}
			})
			if(listLoanArray.length === 1){//有一次销账成功
				//直接跳转
				if(curLoan && 
						curLoan.instanceId != FLOWINFO.instanceId){
					var instanceId = curLoan.instanceId;
					var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + instanceId;
					openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
				}
			}else if(listLoanArray.length > 1){//有多次销账成功
				showListLoan(listLoanArray,function(curLoan){
					//直接跳转
					if(curLoan && 
							curLoan.instanceId != FLOWINFO.instanceId){
						var instanceId = curLoan.instanceId;
						var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + instanceId;
						openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
					}
				})
			}
    	}
    });
    //累计销账信息
    $("body").on("click",".loanOffAll",function(){//累计报销信息
    	var listLoanOff = $(this).data("listLoanOff");
    	
    	if(listLoanOff && listLoanOff[0]){
    		var listLoanOffArray = new Array();
    		var curloanOff;
			$.each(listLoanOff,function(index,loanOffObj){
				if(loanOffObj.status==4){
					listLoanOffArray.push(loanOffObj);
					curloanOff = loanOffObj;
				}
			})
			if(listLoanOffArray.length === 1){//有一次销账成功
				//直接跳转
				if(curloanOff && 
						curloanOff.instanceId != FLOWINFO.instanceId){
					var instanceId = curloanOff.instanceId;
					var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + instanceId;
					openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
				}
			}else if(listLoanOffArray.length > 1){//有多次销账成功
				showListLoanOff(listLoanOffArray,function(curloanOff){
					//直接跳转
					if(curloanOff && 
							curloanOff.instanceId != FLOWINFO.instanceId){
						var instanceId = curloanOff.instanceId;
						var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + instanceId;
						openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
					}
				})
			}
		}
    })

})

//展示报销信息
function showListLoan(listLoanArray,callback){
	window.top.layer.open({
		type : 2,
		title : false,
		closeBtn : 0,
		area : [ '600px', '450px' ],
		fix : true, // 不固定
		maxmin : false,
		move : false,
		content : ['/financial/showListLoanAll?sid=' + sid , 'no' ],
		           btn : ['关闭' ],
		           yes : function(index, layero) {
		        	   window.top.layer.close(index)
		        	   //右上角关闭回调
		           },success:function(layero,index){
		        	   var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		        	   iframeWin.initTable(listLoanArray);
		        	   $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
		        		   window.top.layer.close(index);
		        	   })
		        	   $(iframeWin.document).on("click","a[loan]",function(){
		        		   var result = iframeWin.loanSelected($(this));
		        		   if (result) {
		        			   window.top.layer.close(index)
		        			   callback(result);
		        		   }
		        	   })
		        	   
		           }
	});
}
//展示报销信息
function showListLoanOff(listLoanOffArray,callback){
	window.top.layer.open({
		type : 2,
		title : false,
		closeBtn : 0,
		area : [ '600px', '450px' ],
		fix : true, // 不固定
		maxmin : false,
		move : false,
		content : ['/financial/showListLoanOffAll?sid=' + sid , 'no' ],
		btn : ['关闭' ],
		yes : function(index, layero) {
			window.top.layer.close(index)
			//右上角关闭回调
		},success:function(layero,index){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.initTable(listLoanOffArray);
			$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				window.top.layer.close(index);
			})
			$(iframeWin.document).on("click","a[loanOff]",function(){
				var result = iframeWin.loanOffSelected($(this));
				if (result) {
					window.top.layer.close(index)
					callback(result);
				}
			})
			
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
//流程审核
function goToSpFLowPage(spState){
	EasyWinForm.spFlowPage(spState,function(nextStepConfig){
		$("#contentBody").data("nextStepConfig",nextStepConfig);
    	EasyWinForm.saveFormData({
    		parentEl: $("#contentBody"),
    		dataStatus: 1,
    		isDel: 0,
    		module: "freeform",
    		callback: function (data) {
    			var status = data.status;
    			if (status == 'f') {
    				showNotification(2, data.info);
    			} else if (data.message) {
    			} else {
    				openWindow.ReLoad();
    				closeWin();
    			}
    		}
    	})
	});
}

