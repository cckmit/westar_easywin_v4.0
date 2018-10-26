$(function(){
			//项目名筛选
			$("#modTitle").blur(function(){
				$("#searchForm").submit();
			});
			//文本框绑定回车提交事件
			$("#modTitle").bind("keydown",function(event){
		        if(event.keyCode == "13")    
		        {
		        	if(!strIsNull($("#modTitle").val())){
		        		$("#searchForm").submit();
		        	}
		        }
		    });
		});
		/**
		 * 选择日期
		 */
		function submitForm(){
			$("#searchForm").submit();
		}
		//查看详情
		function viewDetailMod(id,type,sid){
			if('012'==type //客户
					|| '005'==type //项目
                	|| '080'==type //产品
					|| '003'==type){//任务
				viewModInfo(id,type,-1);
			}else if('004'==type){//投票
				$.post("/vote/authorCheck?sid="+sid,{Action:"post",voteId:id},     
						function (msgObjs){
						if(!msgObjs.succ){
							showNotification(1,msgObjs.promptMsg);
						}else{
							var url ="/vote/voteDetail?sid="+sid+"&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
							openWinByRight(url);
						}
				},"json");
			}else if('011'==type){//问答
				var url ="/qas/viewQuesPage?sid="+sid+"&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
				openWinByRight(url);
			}else if('022'==type){//审批
				var url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+id;
				openWinByRight(url);
			}else if('1'==type){//普通分享
				var url ="/msgShare/msgShareViewPage?sid="+sid+"&id="+id+"&type="+type+"&redirectPage="+encodeURIComponent(window.location.href);
				openWinByRight(url);
			}else if('039'==type){//公告
				var url = "/announcement/viewAnnoun?sid=" + sid + "&id=" + id;
				openWinByRight(url);
			}else if('040'==type){//公告
				var url = "/institution/viewInstitu?sid=" + sid + "&id=" + id;
				openWinByRight(url);
			}else if('050'==type){//日报
				var url = "/daily/viewDaily?sid=" + sid + "&id=" + id;
				openWinByRight(url);
			}
			
		}
		//人员筛选
		function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
			$("#"+userIdtag).val(userId);
			$("#"+userNametag).val(userName);
			$("#searchForm").submit();
			
		}
		
/**
 * 关注或是取消星标
 * @param busType 模块类别
 * @param busId 模块主键
 * @param attentionState 原来关注状态0未关注 1已关注
 * @param sid 用户标识
 * @return
 */
function changeAtten(busType,busId,attentionState,sid,ts){
	var onclick = $(ts).attr("onclick");
	$.ajax({
		 type : "post",
		 url : '/attention/ajaxchangeAtten?sid='+sid+'&rnd='+Math.random(),
		 dataType:"json",
		 data:{busType:busType,busId:busId,attentionState:attentionState},
		 beforeSend: function(XMLHttpRequest){
			 $(ts).removeAttr("onclick");
         },
         success : function(data){
        	 if(data.status=='y'){
        		 if(attentionState==0){//原来没有关注
        			 $(ts).attr("onclick","changeAtten('"+busType+"',"+busId+",1,'"+sid+"',this)");
        			 if($(ts).hasClass("fa-star-o")){
        				 $(ts).attr("class","fa fa-star ws-star")
        			 }else{
        				 $(ts).attr("class","fa fa-star-o")
        			 }
        			 showNotification(1,"关注成功");
        		 }else{
        			 $(ts).attr("onclick","changeAtten('"+busType+"',"+busId+",0,'"+sid+"',this)");
        			 
        			 if($(ts).hasClass("fa-star-o")){
        				 $(ts).attr("class","fa fa-star ws-star")
        			 }else{
        				 $(ts).attr("class","fa fa-star-o")
        			 }
        			 showNotification(1,"取消成功");
	 				 window.self.location = reConUrlByClz( ".messages-list .item",1);
        		 }
        	 }else{
        		 $(ts).attr("onclick",onclick);
        	 }
         },
         error:  function(XMLHttpRequest, textStatus, errorThrown){
        	 $(ts).attr("onclick",onclick);
        	 showNotification(2,"系统错误，请联系管理人员！")
	    }
	});
}
//关注事项分类
function attClassify(busType,activityMenu){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	//清空筛选条件
	$("#owner").val("");
	$("#userName").val("");
	$("#modTitle").val("");
	//分类筛选
	$("#modTypes").val(busType);
	$("#busType").val(busType);
	$("#activityMenu").val(activityMenu);
	$("#searchForm").submit();
}

//设置消息未读
function changeAttNoReadNum(busType){
	var allNoReadNum = parseInt($("#allNoReadNumT").html());
	allNoReadNum = allNoReadNum-1;
	$("#allNoReadNumT").html(allNoReadNum)
	
	if(busType=='003'){//任务未读
		var modNoReadNumStr = $("#taskNoReadNumT").html();
		if(checkn.test($.trim(modNoReadNumStr))){
			var noReadNum = parseInt($.trim(modNoReadNumStr));
			setNoReadNum("taskNoReadNumT",noReadNum-1);
		}else{
			setNoReadNum("taskNoReadNumT",0);
		}
	}else if(busType=='012'){//客户未读
		var modNoReadNumStr = $("#crmNoReadNumT").html();
		if(checkn.test($.trim(modNoReadNumStr))){
			var noReadNum = parseInt($.trim(modNoReadNumStr));
			setNoReadNum("crmNoReadNumT",noReadNum-1);
		}else{
			setNoReadNum("crmNoReadNumT",0);
		}
	}else if(busType=='005'){//项目未读
		var modNoReadNumStr = $("#itemNoReadNumT").html();
		if(checkn.test($.trim(modNoReadNumStr))){
			var noReadNum = parseInt($.trim(modNoReadNumStr));
			setNoReadNum("itemNoReadNumT",noReadNum-1);
		}else{
			setNoReadNum("itemNoReadNumT",0);
		}
	}else if(busType=='080'){//项目未读
        var modNoReadNumStr = $("#proNoReadNumT").html();
        if(checkn.test($.trim(modNoReadNumStr))){
            var noReadNum = parseInt($.trim(modNoReadNumStr));
            setNoReadNum("proNoReadNumT",noReadNum-1);
        }else{
            setNoReadNum("proNoReadNumT",0);
        }
    }else if(busType=='004'){//投票中心
		var modNoReadNumStr = $("#voteNoReadNumT").html();
		if(checkn.test($.trim(modNoReadNumStr))){
			var noReadNum = parseInt($.trim(modNoReadNumStr));
			setNoReadNum("voteNoReadNumT",noReadNum-1);
		}else{
			setNoReadNum("voteNoReadNumT",0);
		}
	}else if(busType=='011'){//问答中心
		var modNoReadNumStr = $("#qasNoReadNumT").html();
		if(checkn.test($.trim(modNoReadNumStr))){
			var noReadNum = parseInt($.trim(modNoReadNumStr));
			setNoReadNum("qasNoReadNumT",noReadNum-1);
		}else{
			setNoReadNum("qasNoReadNumT",0);
		}
	}else if(busType=='022'){//审批中心
		var modNoReadNumStr = $("#spNoReadNumT").html();
		if(checkn.test($.trim(modNoReadNumStr))){
			var noReadNum = parseInt($.trim(modNoReadNumStr));
			setNoReadNum("spNoReadNumT",noReadNum-1);
		}else{
			setNoReadNum("spNoReadNumT",0);
		}
	}else if(busType=='100'){//分享消息
		var modNoReadNumStr = $("#shareNoReadNumT").html();
		if(checkn.test($.trim(modNoReadNumStr))){
			var noReadNum = parseInt($.trim(modNoReadNumStr));
			setNoReadNum("shareNoReadNumT",noReadNum-1);
		}else{
			setNoReadNum("shareNoReadNumT",0);
		}
	}
}
//状态查询
function setReadState(type){
	$("#readState").val(type);
	submitForm();
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
	
}
