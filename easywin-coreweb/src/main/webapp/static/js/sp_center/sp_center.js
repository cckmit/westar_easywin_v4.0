//页面加载初始化
$(function(){
	//任务名筛选
	$("#searchFlowName").blur(function(){
		//启动加载页面效果
		layer.load(0, {shade: [0.6,'#fff']});
		$("#searchForm [name='flowName']").val($("#searchFlowName").val());
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#searchFlowName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#searchFlowName").val())){
        		//启动加载页面效果
        		layer.load(0, {shade: [0.6,'#fff']});
        		$("#searchForm [name='flowName']").val($("#searchFlowName").val());
				$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#searchFlowName").focus();
        	}
        }
    });
	//监督人员设置
	$("body").on("click","#forceInBtn",function(){
		forceIn(sid,$(this),'022')
	})
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr ').mouseout(function(){
		var optCheckBox = $(this).find(".optTd .optCheckBox");
			var check = $(optCheckBox).find("input").attr('checked');
			if(check){
				$(this).find(".optTd .optCheckBox").css("display","block");
				$(this).find(".optTd .optRowNum").css("display","none");
			}else{
				$(this).find(".optTd .optCheckBox").css("display","none");
				$(this).find(".optTd .optRowNum").css("display","block");
			}
	});
	
	$(":checkbox[name='ids'][disabled!='disabled']").click(function(){
		var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
		var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
		if(checkLen>0){
			//隐藏查询条件
			$(".searchCond").css("display","none");
			//显示批量操作
			$(".batchOpt").css("display","block");
			if(checkLen==len){
				$("#checkAllBox").attr('checked', true);
			}else{
				$("#checkAllBox").attr('checked', false);
			}
		}else{
			//显示查询条件
			$(".searchCond").css("display","block");
			//隐藏批量操作
			$(".batchOpt").css("display","none");
			
			$("#checkAllBox").attr('checked', false);
		}
	});
});

/**
 * 审批流程JS
 */
function openWinOfAddFlow(activityMenu){
	layer.open({
		  type: 2,
		  title: false,
		  shadeClose: true,
		  scrollbar:false,
		  fix: true, //固定
		  maxmin: false,
		  move: false,
		  area: ['90%','90%'],
		  content: "/flowDesign/flowCfg?sid="+sid+"&activityMenu="+activityMenu //iframe的url
		});
}
//新增流程
function addFlowPage(){
	var url = "/flowDesign/addFlowPage?sid="+sid;
	tabIndex = window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		shadeClose: false,
		shade: 0.3,
		shift:0,
		zIndex:1010,
		scrollbar:false,
		fix: true, //固定
		maxmin: false,
		move: false,
		area: ['750px', '300px'],
		content: [url,'no'], //iframe的url
		success:function(layero,index){
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		}
	});
}
//流程新建编辑页面
function initSpFlow(sp_flowModel_id){
	var url = "/workFlow/initSpFlowPage?sid="+sid+"&sp_flowModel_id="+sp_flowModel_id;
	var height = $(window).height()-45
	tabIndex = layer.open({
		  type: 2,
		  title: false,
		  closeBtn: 0,
		  shadeClose: true,
		  shade: 0,
		  shift:0,
		  offset: 'rb',
		  scrollbar:false,
		  fix: true, //固定
		  maxmin: false,
		  move: false,
		  area: ['800px', height+'px'],
		  content: url //iframe的url
		});
}
//流程查看页面
function viewSpFlow(instanceId){
	var url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+instanceId;
	openWinByRight(url);
}
//删除流程模型
function delFlowModel(flowId){
	window.top.layer.confirm('确定要删除？', {icon: 3, title:'确认对话框'}, function(index){
	  $("#delForm [name='flowId']").val(flowId);
	  $('#delForm').submit();
	  window.top.layer.close(index);
	});
}
//新增流程步骤节点
function addFlowStep(flowId,pstepId){
	var url = "/flowDesign/addFlowStepPage?sid="+sid+"&flowId="+flowId+"&pstepId="+pstepId;
	tabIndex = window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		shadeClose: false,
		shade: 0.3,
		shift:0,
		zIndex:1010,
		scrollbar:false,
		fix: true, //固定
		maxmin: false,
		move: false,
		area: ['650px', '400px'],
		content: [url,'no'], //iframe的url
		success:function(layero,index){
		}
	});
}
//删除流程步骤
function delFlowStep(flowId,stepId){
	window.top.layer.confirm('确定要删除？', {icon: 3, title:'确认对话框'}, function(index){
	  $("#flowStepForm [name='flowId']").val(flowId);
	  $("#flowStepForm [name='stepId']").val(stepId);
	  $('#flowStepForm').submit();
	  window.top.layer.close(index);
	});
}
//更新流程属性
function updateFlowAttr(attrType){
	$("#editFlowForm [name='attrType']").val(attrType);
	$('#editFlowForm').submit();
}
//编辑流程步骤节点
function editFlowStep(flowId,stepId,nextStepWay){
	var url = "/flowDesign/editFlowStepPage?sid="+sid+"&flowId="+flowId+"&stepId="+stepId+"&nextStepWay="+nextStepWay;
	tabIndex = window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		shadeClose: false,
		shade: 0.3,
		shift:0,
		zIndex:1010,
		scrollbar:false,
		fix: true, //固定
		maxmin: false,
		move: false,
		area: ['680px', '400px'],
		content: [url,'no'], //iframe的url
		success:function(layero,index){
		}
	});
}
//构建下步步骤HTML
function nextStepHtml(type,steps,name){
	var stepHtml="";
	if(steps){
		for(var i=0;i<steps.length;i++){
			stepHtml= stepHtml+"<table style=\"float:left;\">";
			stepHtml= stepHtml+"<tr><td>";
			stepHtml= stepHtml+"<label class=\"padding-left-5\">";
			if(type=="checkbox"){
				stepHtml= stepHtml+"<input type=\"checkbox\"";
			}else if(type=="radio"){
				stepHtml= stepHtml+"<input type=\"radio\"";
			}
			stepHtml= stepHtml+"class=\"colored-blue\" name=\""+name+"\" value=\""+steps[i].id+"\" ";
			if(steps[i].isMine==1){//是否是下步步骤
				stepHtml= stepHtml+" checked=\"checked\"";
			}else if(steps[i].defaultStep==1){//是否是默认步骤
				stepHtml= stepHtml+" checked=\"checked\"";
			}
			stepHtml= stepHtml+"/>";
			stepHtml= stepHtml+"<span class=\"text\" style=\"color:inherit;\">"+steps[i].stepName+"</span>";
			stepHtml= stepHtml+"</label>";
			stepHtml= stepHtml+"</td></tr>";
			stepHtml= stepHtml+"</table>";
		}
	}
	return stepHtml;
}
//是否显示默认步骤
function defaultStepForSelectLiDis(){
	var nextStepWay  = $("input[name='nextStepWay']:checked").val();
	//是否默认步骤
	if("branch"==nextStepWay){
		defaultStepSelect();//构建默认步骤候选项
		$("#defaultStepForSelectLi").css("display","block");
	}else{
		$("#defaultStepForSelectLi").css("display","none");
	}
}
//构建默认步骤候选项
function defaultStepSelect(){
	var steps=new Array();
	$("input[name='nextStepIds']:checked").each(function(){
		steps.push({"id":$(this).val(),"stepName":$(this).attr("stepName")});
	});
	var html = nextStepHtml("radio",steps,"defaultStepId")
	$("#defaultStepTableDiv").html(html)
}
//在步骤编辑页面上初始化默认步骤
function initDefaultStepSelect(steps){
	var nextStepWay  = $("input[name='nextStepWay']:checked").val();
	//是否默认步骤
	if("branch"==nextStepWay){
		var html = nextStepHtml("radio",steps,"defaultStepId")
		$("#defaultStepTableDiv").html(html)
		$("#defaultStepForSelectLi").css("display","block");
	}else{
		$("#defaultStepForSelectLi").css("display","none");
	}
}
//弹窗打开表单候选列表页面
function formModListForSelect(formModId) {
	window.top.layer.open({
		 type: 2,
		  //title: ['表单候选列表', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['550px', '550px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/form/formModListForSelect?pager.pageSize=9&sid='+sid+"&id="+formModId,'no'],
		  btn: ['选择', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.formModSelected();
			  if(result){
				  formModSelectedReturn(result.formModId,result.formModName)
				  window.top.layer.close(index)
			  }
		  }
		  ,btn2: function(){ 
		    //右上角关闭回调
		  },success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
	});
}
//表单选择后回调
function formModSelectedReturn(formModId,formModName){
	if(!strIsNull(formModId) && !strIsNull(formModName)){
		$("#formName").val(formModName);
		$("#formKey").val(formModId);
	}else{
		window.top.layer.msg("请选择\"表单关联失败\"！formName="+formName+" & formKey="+formKey,{icon:2});
	}
}
//弹窗打开表单控件候选列表页面
function formComponListForSelect(formModId,flowId,stepId) {
	if(strIsNull(formModId)){
		window.top.layer.msg("流程未关联表单！formModId="+formModId,{icon:2});
		return;
	}
	window.top.layer.open({
		 type: 2,
		  //title: ['表单控件候选列表', 'font-size:18px;'],
		 title:false,
		  closeBtn:0,
		  area: ['550px', '550px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/form/formComponListForSelect?pager.pageSize=7&sid='+sid+"&formModId="+formModId+"&flowId="+flowId+"&stepId="+stepId,'no'],
		  btn: ['选择', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var conls = iframeWin.formComponSelected();
			  if(conls){
				  formComponSelectedReturn(conls)
				  window.top.layer.close(index)
			  }
		  }
		  ,cancel: function(){ 
		    //右上角关闭回调
		  }
	});
}
//表单控件授权回调
function formComponSelectedReturn(conls) {
    //先删除原授权控件
    $(".subform [name='formComponIds']").remove();
    if (conls) {
        var formCompons = "";
        for (var i = 0; i < conls.length; i++) {
            formCompons = formCompons + "<input type=\"hidden\" name=\"formComponIds\" value=\"" + conls[i].formComponId + ";" + conls[i].option + "\"><br />";
            /*alert("<input type=\"hidden\" name=\"formComponIds\" value=\""+conls[i].formComponId+","+conls[i].option+"\"><br />");*/
        }
        $(".subform").append(formCompons);
        $("#spStepFormControl").text("已授权（" + conls.length + "）");
    }
}
//设置流程步骤条件
function addFlowStepConditions(flowId,stepId){
	var url = "/flowDesign/addFlowStepConditionsPage?sid="+sid+"&flowId="+flowId+"&stepId="+stepId;
	tabIndex = window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		shadeClose: false,
		shade: 0.3,
		shift:0,
		zIndex:1010,
		scrollbar:false,
		fix: true, //固定
		maxmin: false,
		move: false,
		area: ['780px', '420px'],
		content: [url,'no'], //iframe的url
		success:function(layero,index){
		}
	});
}
//发起审批
function startSpFlow(flowId){
	var url="/workFlow/startSpFlow?sid="+sid+'&flowId='+flowId;
	openWinByRight(url)
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}
/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	var stateBtnArray = $(".stateBtn");
	$.each(stateBtnArray,function(index,item){
		$(this).removeClass("btn-primary");
		if(!$(this).hasClass("btn-default")){
			$(this).addClass("btn-default");
		}
	})
	$("#state").val('');
}
//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
//排序
function orderBy(type){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#searchForm [name='orderBy']").val(type);
	$("#searchForm").submit();
}
//排序默认
function orderByClean(){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#searchForm [name='orderBy']").val('');
	$("#searchForm").submit();
}
//状态筛选
function selectByFlowState(flowState){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#searchForm [name='flowState']").val(flowState);
	$("#searchForm").submit();
}
//清楚状态筛选条件
function flowStateClean(){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#searchForm [name='flowState']").val('');
	$("#searchForm").submit();
}
//人员筛选
function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
	$("#"+userIdtag+"_select").find("option").remove();
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	if(!strIsNull(userIdtag) && "executor"==userIdtag){
		//审批人员筛选
		$("#searchForm [name='executor']").val(userId);
		$("#searchForm [name='executorName']").val(userName);
		$("#searchForm").submit();
	}else if(!strIsNull(userIdtag) && "creator"==userIdtag){
		//审批发起人筛选
		$("#searchForm [name='creator']").val(userId);
		$("#searchForm [name='creatorName']").val(userName);
		$("#searchForm").submit();
	}else if(!strIsNull(userIdtag) && "operator"==userIdtag){
	}
}

//审批流程分类
function listSpFlowType(){
	layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  //title: ['流程分类维护', 'font-size:18px;'],
		  area: ['550px', '400px'],
		  fix: false, //不固定
		  //btn:['关闭'],
		  maxmin: false,
		  scrollbar:false,
		  content: ["/flowDesign/listSpFlowType?sid="+sid,'no'],
		  cancel: function(){ 
		    //右上角关闭回调
		  },end:function(index){
		  }
		});
}
//流程分类筛选
function selectBySpFlowType(obj){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	if($(obj).attr("typeId")==0){
		$("#searchForm [name='spFlowTypeName']").val("无类别");
	}else{
		$("#searchForm [name='spFlowTypeName']").val($(obj).attr("typeName"));
	}
	$("#searchForm [name='spFlowTypeId']").val($(obj).attr("typeId"));
	$("#searchForm").submit();
}
//清楚流程分类筛选条件
function spFlowTypeClean(){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#searchForm [name='spFlowTypeId']").val('');
	$("#searchForm").submit();
}
//新增审批
function newSpFlow(){
	var url = "/flowDesign/listSpFlow?pager.pageSize=10&sid="+sid;
	openWinByRight(url);
}
//审批结果筛选
function selectBySpState(spState){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#searchForm [name='spState']").val(spState);
	$("#searchForm [name='flowState']").val(4);
	$("#searchForm").submit();
}
//清楚审批结果筛选条件
function spStateClean(){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#searchForm [name='spState']").val('');
	$("#searchForm [name='flowState']").val('');
	$("#searchForm").submit();
}
//删除审批流程
function delSpFlow(){
	if(checkCkBoxStatus('ids')){
		window.top.layer.confirm('确定要删除被选择的审批流程吗？', {icon: 3, title:'提示'}, function(index){
		  var url = reConstrUrl('ids');
		  $("#delForm input[name='redirectPage']").val(url);
		  $('#delForm').submit();
		  window.top.layer.close(index);
		});
	}else{
		window.top.layer.msg('请先选择需要删除的审批流程', {icon: 6});
	}
}
//配置自由审批步骤节点
function addSpFlowStep(obj){
	var tr = newStepTr();   
	$(obj).parent().parent().after(tr);
	orderNum();
}
//删除审批步骤节点
function delSpFlowStep(obj){
	window.top.layer.confirm('确定要删除此步骤吗？', {icon: 3, title:'提示'}, function(index){
	  $(obj).parent().parent().remove();   
	  orderNum();
	  window.top.layer.close(index);
	});
}
//步骤行序号排序
function orderNum(){
    for (var i = 0; i<spStepTable.rows.length; i++) {
         if (i != 0)
         spStepTable.rows[i].cells[0].innerHTML = i;
    }
}
//构建新审批步骤行
function newStepTr(){
	var stepHtml= stepHtml+"<tr class=\"stepTr\">";
	var stepHtml= stepHtml+"<td>1</td>";
	var stepHtml= stepHtml+"<td class=\"other-user-box\">";
	var stepHtml= stepHtml+"<span class=\"pull-left\">审批人：</span>";
	var stepHtml= stepHtml+"<input type=\"hidden\" name=\"spUser\" id=\"spUser"+spStepTable.rows.length+"\" value=\"\">";
	var stepHtml= stepHtml+"<div id=\"userOneImgDivspUser"+spStepTable.rows.length+"\" class=\"ticket-user pull-left\">";
	var stepHtml= stepHtml+"<img id=\"userOneImgspUser"+spStepTable.rows.length+"\" style=\"display:none;float:left\" class=\"user-avatar\" " +
							"onclick=\"userOne('spUser"+spStepTable.rows.length+"','','','"+sid+"','no');\" " +
							"src='' title=''/>";
	var stepHtml= stepHtml+"<span class=\"user-name\" style=\"font-size:10px;\" id=\"userOneName_spUser"+spStepTable.rows.length+"\"></span>";
	var stepHtml= stepHtml+"</div>";
	var stepHtml= stepHtml+"<a href=\"javascript:void(0);\" class=\"fa fa-user selectUser\" style=\"padding: 0px 10px;float: left;" +
							"margin-top:4px;font-size:24px;\" title=\"人员单选\"" +
							"onclick=\"userOne('spUser"+spStepTable.rows.length+"','','','"+sid+"','no');\"></a>";
	var stepHtml= stepHtml+"</td>";
	var stepHtml= stepHtml+"<td>";
	var stepHtml= stepHtml+"<a href=\"javascript:void(0);\" onclick=\"addSpFlowStep(this);\">添加下一步</a>&nbsp;|&nbsp;" +
			"<a href=\"javascript:void(0);\" onclick=\"delSpFlowStep(this);\">删除</a>";
	var stepHtml= stepHtml+"</td>";
	var stepHtml= stepHtml+"</tr>";
	return stepHtml;
}

//发起审批前验证
function startSpFlowAfterCheck(flowId){
	var url="/workFlow/startSpFlow?sid="+sid+"&flowId="+flowId;
	//验证流程内是否配置直属上级审批节点；如果有，则验证创建人是否设置了直属上级
	$.ajax({  
        url : "/workFlow/haveSetDirectLeader?sid="+sid+"&flowId="+flowId,  
        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
        type : "POST",  
        dataType : "json", 
        success : function(spFlowModel) {   
        	if(spFlowModel.succ){
        		window.top.layer.confirm("此审批过程需要你的直属上级审批！请先设定。", {
    				title : "警告",
    				closeBtn:0,
    				move:false,
    				btn : [ "设定", "取消" ],
    				icon : 0
    			}, function(index) {
    				window.top.layer.close(index);
    				setDirectLeader(sid);//弹窗设置直属上级
    			});
        	}else{
        		//启动加载页面效果
        		layer.load(0, {shade: [0.6,'#fff']});
        		window.location.href=url;
        	}
        }  
    });
}

//关联流程选择
function spFlowModelForSelect(sid){
	window.top.layer.open({
		 type: 2,
		  title:false,
		  closeBtn:0,
		  area: ['600px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/flowDesign/listFlowModelForSelect?pager.pageSize=10&sid='+sid,'no'],
		  btn: ['选择', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.flowSelected();
			  if(result){
				  flowSelectedReturn(result.flowId,result.flowName);//页面回调赋值
				  window.top.layer.close(index)
			  }
		  }
	});
}

//关联流程配置
function spFlowModelRelevance(pFlowId){
	window.top.layer.open({
		 type: 2,
		  title:false,
		  closeBtn:0,
		  area: ['650px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/flowDesign/spFlowModelRelevancePage?sid='+sid+'&pFlowId='+pFlowId,'no'],
		  btn: ['确定', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.formSubmit();//表单提交
		  }
	});
}
//人员筛选
function userMoreForUserIdCallBack(options,userIdtag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+userIdtag+"_select").find("option").remove();
	for(var i =0;i<options.length;i++){
		$("#"+userIdtag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
	}
	$("#searchForm").submit();
}
//移除筛选
function removeChoose(userIdtag,sid,userId){
	$("#"+userIdtag+"_select").find("option[value='"+userId+"']").remove();
	$("#searchForm").submit();
}

//拾取流程审批
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