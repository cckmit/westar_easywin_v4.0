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
   
    
  //绑定提交事件
	$("body").bind("checkSpFlowInfo","#form-info-div",function() {
		if(needCheckState == '1'){
			EasyWinForm.submitMobCheck($("#contentBody"), 1, function(checkInfo){
				var code = checkInfo.code;
				if(code == -1){
					var WorkFlowDataResultInfo = {};
					WorkFlowDataResultInfo.code = "error";
					WorkFlowDataResultInfo.info = checkInfo.message;
					if(appWay == 'ios'){
						checkSpFlowIos(JSON.stringify(WorkFlowDataResultInfo));
					}else{
						checkSpFlow(JSON.stringify(WorkFlowDataResultInfo));
					}
				}else{
					var formDataResult = EasyWinForm.submitAssembleForm({parentEl:$("#contentBody"),dataStatus:1});
					var WorkFlowDataResultInfo = {};
					WorkFlowDataResultInfo.code = "success";
					WorkFlowDataResultInfo.workFlowData = formDataResult;
					if(appWay == 'ios'){
						checkSpFlowIos(JSON.stringify(WorkFlowDataResultInfo));
					}else{
						checkSpFlow(JSON.stringify(WorkFlowDataResultInfo));
					}
				}
			});
		}else{
			var formDataResult = EasyWinForm.submitAssembleForm({parentEl:$("#contentBody"),dataStatus:1});
			var WorkFlowDataResultInfo = {};
			WorkFlowDataResultInfo.code = "success";
			WorkFlowDataResultInfo.workFlowData = formDataResult;
			if(appWay == 'ios'){
				checkSpFlowIos(JSON.stringify(WorkFlowDataResultInfo));
			}else{
				checkSpFlow(JSON.stringify(WorkFlowDataResultInfo));
			}
		}
    });
	
	 //借款申请弹窗查看
    $("body").on("click", ".loanApply2View", function () {
        var instanceId = $("#formInfo").data("loanApply").instanceId;
        if(instanceId!= FLOWINFO.instanceId){
        	viewSpFlowDetail(appWay,instanceId)
        }
    });
  //借款申请弹窗查看
    $("body").on("click", ".loanRep2View", function () {
    	var instanceId = $("#formInfo").data("loanOff").loanRepInsId;
    	//直接跳转
		if(instanceId!= FLOWINFO.instanceId){
			viewSpFlowDetail(appWay,instanceId)
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
					viewSpFlowDetail(appWay,instanceId);
				}
			}else if(listLoanArray.length > 1){//有多次销账成功
				showListLoan(appWay,listLoanArray)
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
					viewSpFlowDetail(appWay,instanceId);
				}
			}else if(listLoanOffArray.length > 1){//有多次销账成功
				showListLoanOff(appWay,listLoanOffArray)
			}
		}
    })

})

