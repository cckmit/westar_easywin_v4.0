$(function(){
	//文本框绑定回车提交事件
	$("#content").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#content").val())){
        		$("#sysLogForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#sysLogForm").focus();
        	}
        }
    });
	$("#content").blur(function(){
		$("#sysLogForm").submit();
	});
	
});
//选择日期
function selectDate(){
	$("#sysLogForm").submit();
}

//人员筛选
function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	//经办人筛选
	$("#userId").val(userId);
	$("#userName").val(userName);
	$("#sysLogForm").submit();
}

//部门筛选
function depOneCallBack(orgIdElementId, orgPathNameElementId,orgId,orgName){
	$("#"+orgIdElementId).val(orgId);
	$("#"+orgPathNameElementId).val(orgName);
	$("#sysLogForm").submit();
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
}